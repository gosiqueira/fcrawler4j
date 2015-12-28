package br.ufop.decom.gaid.focused_crawler.threshold.preprocessing;

import java.util.ArrayList;
import java.util.List;

public class NLast implements Preprocess {

	private int numSeeds;

	public NLast(int numSeeds) {
		this.numSeeds = numSeeds;
	}

	@Override
	public List<Double> analyze(List<Double> similarities) {
		List<Double> nLast = new ArrayList<>();

		for (int i = numSeeds - 1; i < similarities.size(); i++) {
			nLast.add(similarities.get(i));
		}

		return nLast;
	}

}
