package br.ufop.decom.gaid.focused_crawler.threshold.preprocessing;

import java.util.List;

public interface Preprocess {

	public List<Double> analyze(List<Double> similarities);
	
}
