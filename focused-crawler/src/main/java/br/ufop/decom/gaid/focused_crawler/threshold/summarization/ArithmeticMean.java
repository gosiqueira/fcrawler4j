package br.ufop.decom.gaid.focused_crawler.threshold.summarization;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import br.ufop.decom.gaid.focused_crawler.similarity.SimilarityMetric;
import br.ufop.decom.gaid.focused_crawler.threshold.Threshold;
import br.ufop.decom.gaid.focused_crawler.threshold.preprocessing.IQR;
import br.ufop.decom.gaid.focused_crawler.threshold.preprocessing.Preprocess;
import edu.uci.ics.crawler4j.url.WebURL;

public class ArithmeticMean implements Threshold {

	private SimilarityMetric similarityMetric;
	private List<WebURL> seeds;

	public ArithmeticMean(SimilarityMetric similarityMetric, List<WebURL> seeds) {
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
		DescriptiveStatistics statistic = new DescriptiveStatistics(
				preprocess.analyze(similarities).stream().mapToDouble(d -> d).toArray());
		
		threshold = statistic.getMean();

		return threshold;
	}

}
