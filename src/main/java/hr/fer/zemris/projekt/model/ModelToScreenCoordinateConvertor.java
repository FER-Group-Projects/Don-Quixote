package hr.fer.zemris.projekt.model;

public class ModelToScreenCoordinateConvertor {
	
	public static class DoublePoint {
		public double x;
		public double y;
	}
	
	public static class LongPoint {
		public long x;
		public long y;
	}
	
	/**
	 * Minimum and maximum coordinates of the model
	 */
	private double minXM;
	private double maxXM;
	private double minYM;
	private double maxYM;
	
	/**
	 * Minimum and maximum coordinates of the screen
	 */
	private long minXS;
	private long maxXS;
	private long minYS;
	private long maxYS;
	
	private double scaleX;
	private double scaleY;
	
	public ModelToScreenCoordinateConvertor(double minXM, double maxXM, double minYM, double maxYM, long minXS,
			long maxXS, long minYS, long maxYS) {
		this.minXM = minXM;
		this.maxXM = maxXM;
		this.minYM = minYM;
		this.maxYM = maxYM;
		this.minXS = minXS;
		this.maxXS = maxXS;
		this.minYS = minYS;
		this.maxYS = maxYS;
		
		refreshScaling();
	}

	private void refreshScaling() {
		this.scaleX = (maxXS-minXS) / (maxXM-minXM);
		this.scaleY = (maxYS-minYS) / (maxYM-minYM);
	}
	
	public LongPoint convert(DoublePoint dp) {
		LongPoint point = new LongPoint();
		point.x = Math.round(dp.x * scaleX);
		point.y = maxYS-Math.round(dp.y * scaleY);
		return point;
	}
	
	public double scaleWidth(double width) {
		return width * this.scaleX;
	}
	
	public double scaleHeight(double height) {
		return height * this.scaleY;
	}

	public double getMinXM() {
		return minXM;
	}

	public void setMinXM(double minXM) {
		this.minXM = minXM;
		refreshScaling();
	}

	public double getMaxXM() {
		return maxXM;
	}

	public void setMaxXM(double maxXM) {
		this.maxXM = maxXM;
		refreshScaling();
	}

	public double getMinYM() {
		return minYM;
	}

	public void setMinYM(double minYM) {
		this.minYM = minYM;
		refreshScaling();
	}

	public double getMaxYM() {
		return maxYM;
	}

	public void setMaxYM(double maxYM) {
		this.maxYM = maxYM;
		refreshScaling();
	}

	public long getMinXS() {
		return minXS;
	}

	public void setMinXS(long minXS) {
		this.minXS = minXS;
		refreshScaling();
	}

	public long getMaxXS() {
		return maxXS;
	}

	public void setMaxXS(long maxXS) {
		this.maxXS = maxXS;
		refreshScaling();
	}

	public long getMinYS() {
		return minYS;
	}

	public void setMinYS(long minYS) {
		this.minYS = minYS;
		refreshScaling();
	}

	public long getMaxYS() {
		return maxYS;
	}

	public void setMaxYS(long maxYS) {
		this.maxYS = maxYS;
		refreshScaling();
	}

}
