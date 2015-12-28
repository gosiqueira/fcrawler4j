package br.ufop.decom.gaid.focused_crawler.threshold.preprocessing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class IQR implements Preprocess {

	@Override
	public List<Double> analyze(List<Double> similarities) {
		List<Double> processedSimilarities = new ArrayList<>();
		
		Collections.sort(similarities);
		
		DescriptiveStatistics statistic = new DescriptiveStatistics(similarities.stream().mapToDouble(d -> d).toArray());
		
		double q1 = statistic.getPercentile(25);
		double q3 = statistic.getPercentile(75);
		double iqr = q3 - q1;
		
		double min = q1 - 1.5*iqr;
		double max = q3 + 1.5*iqr;	
		for(double value : similarities) {
			if(value >= min && value <= max) {
				processedSimilarities.add(value);
			}
		}

		statistic.clear();
		
		return processedSimilarities;
	}

}
