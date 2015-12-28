package br.ufop.decom.gaid.focused_crawler.threshold.preprocessing;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomChoice implements Preprocess{

	private int choices;
	
	public RandomChoice(int choices) {
		this.choices = choices;
	}

	@Override
	public List<Double> analyze(List<Double> similarities) {
		List<Double> choosenSeeds = new ArrayList<>();
		
		Random rand = new Random();
		Set<Integer> generated = new LinkedHashSet<>();
		
		while(choosenSeeds.size() < this.choices) {
			int index = rand.nextInt(similarities.size());
			if(generated.contains(index)) {
				continue;
			}
			generated.add(index);
			choosenSeeds.add(similarities.get(index));
		}
		
		return choosenSeeds;
	}

}
