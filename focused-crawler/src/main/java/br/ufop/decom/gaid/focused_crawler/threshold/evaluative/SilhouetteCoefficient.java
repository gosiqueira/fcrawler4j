package br.ufop.decom.gaid.focused_crawler.threshold.evaluative;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufop.decom.gaid.focused_crawler.similarity.SimilarityMetric;
import br.ufop.decom.gaid.focused_crawler.threshold.Threshold;
import edu.uci.ics.crawler4j.url.WebURL;

public class SilhouetteCoefficient implements Threshold {

	private SimilarityMetric similarityMetric;
	private List<WebURL> seeds;

	public SilhouetteCoefficient(SimilarityMetric similarityMetric, List<WebURL> seeds) {
		this.similarityMetric = similarityMetric;
		this.seeds = seeds;
	}

	@Override
	public double getThreshold() throws Exception {
		double threshold = 0.0;

		double limInf = 0.0;
		double limSup = 1.0;
		double alpha = 0.01;
		// TODO implement beta using 0.001 as value;
		double gama = 0;
		double bestLim = 0;
		double bestGama = 0;
		
		int n = (int) (Math.round((limSup - limInf) / alpha) + 1);
		
		List<WebURL> cm = new ArrayList<WebURL>();
		List<WebURL> ct = new ArrayList<WebURL>();
		
		List<Double> limits = new ArrayList<Double>();
		
		double limit = limInf;
		
		for (int i = 0; i < n; i++) {
			limits.add(limit);
			limit += alpha;
		}
		
		Map<WebURL, Double> seedSet = new HashMap<WebURL, Double>();
		
		for (WebURL seed : seeds) {
			
			seedSet.put(seed, similarityMetric.similarity(seed.getTag() + " " + seed.getAnchor() + " " + seed.getURL()));
		}
		
		for(Double lim : limits) {
			for (WebURL s : seedSet.keySet()) {
				if(seedSet.get(s) >= lim) {
					cm.add(s);
				} else {
					ct.add(s);
				}
			}
			
			gama = calculateSilhouetteCoefficient(cm, ct, seedSet);
			
			if(gama > bestGama) {
				bestLim = lim;
				bestGama = gama;
			}
			cm.clear();
			ct.clear();
		}			

		threshold = bestLim;
		
		return threshold;
	}

	public double calculateMeanDistance(WebURL seed, List<WebURL> cluster, Map<WebURL, Double> universe) {
		double val = 0;
		
		for(WebURL elem : universe.keySet()) {
			if (cluster.contains(elem)) {
				val += Math.abs(universe.get(seed) - universe.get(elem));
			}
		}
		
		return val / cluster.size();
	}
	
	public double calculateS(double a, double b) {
		if (a > b) {
			return (b - a) / a;
		} else {
			return (b - a) / b;
		}
	}

	public double calculateSilhouetteCoefficient(List<WebURL> cm, List<WebURL> ct, Map<WebURL, Double> seedSet) {
		double a = 0;
		double b = 0;
		double[] s = new double[cm.size()];
		
		double sk = 0;
		
		int j = 0;
		for (WebURL i : seedSet.keySet()) {
			if (cm.contains(i)) {
				a = calculateMeanDistance(i, cm, seedSet);
				b = calculateMeanDistance(i, ct, seedSet);
				s[j++] = calculateS(a, b);
			} else {
				a = calculateMeanDistance(i, ct, seedSet);
				b = calculateMeanDistance(i, cm, seedSet);
			}
		}
		
		for (double val : s) {
			sk += val;
		}
		
		if (sk > 0 && s.length > 0) {
			sk = sk / s.length;
		} else {
			sk = 0.0;
		}
		
		return sk;
		
	}
	
}
