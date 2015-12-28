package br.ufop.decom.gaid.focused_crawler.threshold.clustering;

import java.util.ArrayList;
import java.util.List;

import br.ufop.decom.gaid.focused_crawler.similarity.SimilarityMetric;
import br.ufop.decom.gaid.focused_crawler.threshold.Threshold;
import edu.uci.ics.crawler4j.url.WebURL;
import smile.clustering.BIRCH;

public class BIRCHClustering implements Threshold {

	private SimilarityMetric similarityMetric;
	private List<WebURL> seeds;

	public BIRCHClustering(SimilarityMetric similarityMetric, List<WebURL> seeds) {
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
		
		BIRCH birch = new BIRCH(1, 3, 0.2);
		
		birch.add(similarities.stream().mapToDouble(d -> d).toArray());
		threshold = birch.partition(2);

		return threshold;
	}

}
