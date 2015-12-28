package br.ufop.decom.gaid.focused_crawler.threshold.clustering;

import java.util.ArrayList;
import java.util.List;

import br.ufop.decom.gaid.focused_crawler.similarity.SimilarityMetric;
import br.ufop.decom.gaid.focused_crawler.threshold.Threshold;
import edu.uci.ics.crawler4j.url.WebURL;
import smile.clustering.KMeans;

public class KMeansClustering implements Threshold {

	private SimilarityMetric similarityMetric;
	private List<WebURL> seeds;

	public KMeansClustering(SimilarityMetric similarityMetric, List<WebURL> seeds) {
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

		double[][] similaritiesMatrix = new double[similarities.size()][1];
		for(int i = 0; i < similarities.size(); i++) {
			similaritiesMatrix[i][0] = similarities.get(i);
		}
		
		KMeans kmeans = new KMeans(similaritiesMatrix, 2);
		
		threshold = kmeans.centroids()[0][0];

		return threshold;
	}

}
