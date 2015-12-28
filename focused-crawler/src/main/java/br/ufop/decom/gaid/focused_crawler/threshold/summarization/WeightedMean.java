package br.ufop.decom.gaid.focused_crawler.threshold.summarization;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import br.ufop.decom.gaid.focused_crawler.similarity.SimilarityMetric;
import br.ufop.decom.gaid.focused_crawler.threshold.Threshold;
import br.ufop.decom.gaid.focused_crawler.threshold.preprocessing.IQR;
import br.ufop.decom.gaid.focused_crawler.threshold.preprocessing.Preprocess;
import edu.uci.ics.crawler4j.url.WebURL;

public class WeightedMean implements Threshold {

	private SimilarityMetric similarityMetric;
	private List<WebURL> seeds;
	
	public WeightedMean(SimilarityMetric similarityMetric, List<WebURL> seeds) {
		this.similarityMetric = similarityMetric;
		this.seeds = seeds;
	}

	@Override
	public double getThreshold() throws Exception {
		double threshold = 0.0;
		
		List<Double> similarities = new ArrayList<>();
				
		for (WebURL seed : seeds) {
			similarities.add(similarityMetric.similarity(seed.getTag() + " " + seed.getAnchor() + " " + seed.getURL()));
		}
		
		Preprocess preprocess = new IQR();
		Mean mean = new Mean();
		
		List<Double> processedSimilarities = preprocess.analyze(similarities);
		List<Double> weights = new ArrayList<>();
		double currentWeight = processedSimilarities.size();
		
		for (int i = 0; i < processedSimilarities.size(); i++) {
			weights.add(currentWeight);
			currentWeight -= 1.0;
		}
			
		threshold = mean.evaluate(processedSimilarities.stream().mapToDouble(d -> d).toArray(), weights.stream().mapToDouble(d -> d).toArray());
		
		return threshold;
	}

}
