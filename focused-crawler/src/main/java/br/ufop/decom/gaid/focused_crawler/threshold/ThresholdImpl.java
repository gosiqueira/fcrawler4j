package br.ufop.decom.gaid.focused_crawler.threshold;

public class ThresholdImpl implements Threshold {

	private double threshold;
	
	public ThresholdImpl(double threshold) {
		this.threshold = threshold;
	}
	
	@Override
	public double getThreshold() throws Exception {
		return this.threshold;
	}

}
