package br.ufop.decom.gaid.focused_crawler.threshold.preprocessing;

import java.util.ArrayList;
import java.util.List;

public class NFirst implements Preprocess {

	private int numSeeds;
	
	public NFirst(int numSeeds) {
		this.numSeeds = numSeeds;
	}

	@Override
	public List<Double> analyze(List<Double> similarities) {
		List<Double> nFirst = new ArrayList<>();
		
		for(int i = 0; i < numSeeds; i++) {
			nFirst.add(similarities.get(i));
		}
		
		return nFirst;
	}

}
