package br.ufop.decom.gaid.focused_crawler.similarity;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CosineSimilarity implements SimilarityMetric {

	Logger logger = LoggerFactory.getLogger(CosineSimilarity.class);

	private List<String> genreTerms, contentTerms, urlTerms;

	/**
	 * Genre and content weights sum must be equal to 1. Benchmark values -
	 * genre: 0.3, content:0.7.
	 */
	private double genreWeight, contentWeight;

	/**
	 * A weight for genre and content weighted mean result.
	 */
	private double genreAndContentWeight;

	/**
	 * URL similarity weight = (Genre and content combination weight) - 1.
	 */
	private double urlWeight;
	private double threshold;

	public CosineSimilarity() {
		this.genreTerms = new ArrayList<>();
		this.contentTerms = new ArrayList<>();
		this.urlTerms = new ArrayList<>();

		this.genreWeight = 0.5;
		this.contentWeight = 0.5;
		this.genreAndContentWeight = 0.5;
		this.urlWeight = 0.5;

		this.threshold = 0.0;

	}

	public CosineSimilarity(List<String> genreTerms, List<String> contentTerms, double genreWeight,
			double contentWeight) {
		this.genreTerms = genreTerms;
		this.contentTerms = contentTerms;

		if (genreWeight + contentWeight > 1) {
			this.genreWeight = genreWeight / (genreWeight + contentWeight);
			this.contentWeight = contentWeight / (genreWeight + contentWeight);
		} else {
			this.genreWeight = genreWeight;
			this.contentWeight = contentWeight;
		}
	}

	public CosineSimilarity(List<String> genreTerms, List<String> contentTerms, List<String> urlTerms,
			double genreWeight, double contentWeight, double genreAndContentWeight, double urlWeight,
			double threshold) {
		this.genreTerms = genreTerms;
		this.contentTerms = contentTerms;
		this.urlTerms = urlTerms;

		this.genreWeight = genreWeight;
		this.contentWeight = contentWeight;

		this.genreAndContentWeight = genreAndContentWeight;
		this.urlWeight = urlWeight;

		this.threshold = threshold;
	}

	@Override
	public double similarity(String text) throws IOException {
		double finalSimilarity = 0.0;

		double genreSimilarity = sim(logarithmicTermFrequency(this.genreTerms, text));
		double contentSimilarity = sim(logarithmicTermFrequency(this.contentTerms, text));

		finalSimilarity = (this.genreWeight * genreSimilarity + this.contentWeight * contentSimilarity) / (this.genreWeight + this.contentWeight);

		return finalSimilarity;
	}

	@Override
	public double similarity(String pageText, String urlText) throws IOException {
		double finalSimilarity = 0.0;

		double genreSimilarity = sim(logarithmicTermFrequency(this.genreTerms, pageText));
		double contentSimilarity = sim(logarithmicTermFrequency(this.contentTerms, pageText));
		double urlSimilarity = sim(logarithmicTermFrequency(this.urlTerms, urlText));

		double genreAndContentCombination = (this.genreWeight * genreSimilarity + this.contentWeight * contentSimilarity) / (this.genreWeight + this.contentWeight);

		finalSimilarity = (this.genreAndContentWeight * genreAndContentCombination + this.urlWeight * urlSimilarity) / (genreAndContentCombination + this.urlWeight);

		return finalSimilarity;
	}

	@Override
	public boolean isSimilar(double similarityValue) {
		return similarityValue >= this.threshold;
	}

	public List<Double> augmentedTermFrequency(List<String> terms, String text) throws IOException {
		Map<String, Integer> index = new HashMap<>();
		List<Double> tf = new ArrayList<>();

		int maxShingle = 2;

		for (String term : terms) {
			index.put(StringUtils.lowerCase(StringUtils.stripAccents(term)), 0);
			int shingleSize = term.split(" ").length;
			if (shingleSize > maxShingle) {
				maxShingle = shingleSize;
			}
		}

		text = StringUtils.lowerCase(StringUtils.stripAccents(text));

		Analyzer analyzer = new StandardAnalyzer(new StringReader(""));
		ShingleFilter filter = new ShingleFilter(analyzer.tokenStream(null, text));
		filter.setMaxShingleSize(maxShingle);
		filter.reset();

		while (filter.incrementToken()) {
			String token = filter.getAttribute(CharTermAttribute.class).toString();
			if (index.containsKey(token)) {
				index.replace(token, index.get(token) + 1);
			}
		}

		filter.end();
		filter.close();
		analyzer.close();

		for (String key : index.keySet()) {
			tf.add((double) index.get(key));
		}

		double maxFreq = Collections.max(tf);

		for (int i = 0; i < tf.size(); i++) {
			tf.set(i, 0.5 + (0.5 * tf.get(i) / maxFreq));
		}

		return tf;
	}
	
	public List<Double> logarithmicTermFrequency(List<String> terms, String text) throws IOException {
		Map<String, Integer> index = new HashMap<>();
		List<Double> tf = new ArrayList<>();

		int maxShingle = 2;

		for (String term : terms) {
			index.put(StringUtils.lowerCase(StringUtils.stripAccents(term)), 0);
			int shingleSize = term.split(" ").length;
			if (shingleSize > maxShingle) {
				maxShingle = shingleSize;
			}
		}

		text = StringUtils.lowerCase(StringUtils.stripAccents(text));

		Analyzer analyzer = new StandardAnalyzer(new StringReader(""));
		ShingleFilter filter = new ShingleFilter(analyzer.tokenStream(null, text));
		filter.setMaxShingleSize(maxShingle);
		filter.reset();

		while (filter.incrementToken()) {
			String token = filter.getAttribute(CharTermAttribute.class).toString();
			if (index.containsKey(token)) {
				index.replace(token, index.get(token) + 1);
			}
		}

		filter.end();
		filter.close();
		analyzer.close();

		for (String key : index.keySet()) {
			if (index.get(key) > 0) {
				tf.add(1.0 + Math.log10(index.get(key)));
			} else {
				tf.add(0.0);
			}
		}
		
		return tf;
	}

	public double sim(List<Double> normalizedTF) {
		double similarity = 0.0;
		double dividend = 0.0;
		double divisor = 0.0;

		for (Double freq : normalizedTF) {
			dividend += freq;
			divisor += freq * freq;
		}

		divisor = Math.sqrt(divisor);
		divisor *= Math.sqrt(normalizedTF.size());

		if (divisor > 0) {
			similarity = dividend / divisor;
		}

		return similarity;
	}

}
