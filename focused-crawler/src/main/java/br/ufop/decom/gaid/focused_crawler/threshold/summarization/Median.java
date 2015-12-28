package br.ufop.decom.gaid.focused_crawler.threshold.summarization;

import java.util.ArrayList;
import java.util.List;

import br.ufop.decom.gaid.focused_crawler.similarity.SimilarityMetric;
import br.ufop.decom.gaid.focused_crawler.threshold.Threshold;
import br.ufop.decom.gaid.focused_crawler.threshold.preprocessing.IQR;
import br.ufop.decom.gaid.focused_crawler.threshold.preprocessing.Preprocess;
import edu.uci.ics.crawler4j.url.WebURL;

public class Median implements Threshold {

	private SimilarityMetric similarityMetric;
	private List<WebURL> seeds;
	
	public Median(SimilarityMetric similarityMetric, List<WebURL> seeds) {
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
		List<Double> processedSeeds = preprocess.analyze(similarities);
		
		if(processedSeeds.size()%2 == 0) {
			threshold = (processedSeeds.get(processedSeeds.size()/2) + processedSeeds.get((processedSeeds.size() + 1)/2))/2; 
		} else {
			threshold = (processedSeeds.get(processedSeeds.size()/2));
		}
		
		return threshold;
	}

}
