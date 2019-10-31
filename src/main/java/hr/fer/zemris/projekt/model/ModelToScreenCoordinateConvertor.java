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
	
	private double minXD;
	private double maxXD;
	private double minYD;
	private double maxYD;
	
	private long minXI;
	private long maxXI;
	private long minYI;
	private long maxYI;
	
	private double scaleX;
	private double scaleY;
	
	public ModelToScreenCoordinateConvertor(double minXD, double maxXD, double minYD, double maxYD, long minXI, long maxXI,
			long minYI, long maxYI) {
		this.minXD = minXD;
		this.maxXD = maxXD;
		this.minYD = minYD;
		this.maxYD = maxYD;
		this.minXI = minXI;
		this.maxXI = maxXI;
		this.minYI = minYI;
		this.maxYI = maxYI;
		refreshScaling();		
	}
	
	private void refreshScaling() {
		this.scaleX = (maxXI-minXI) / (maxXD-minXD);
		this.scaleY = (maxYI-minYI) / (maxYD-minYD);
	}
	
	public LongPoint convert(DoublePoint dp) {
		LongPoint point = new LongPoint();
		point.x = Math.round(dp.x * scaleX);
		point.y = maxYI-Math.round(dp.y * scaleY);
		return point;
	}

	public double getMinXD() {
		return minXD;
	}

	public void setMinXD(double minXD) {
		this.minXD = minXD;
		refreshScaling();
	}

	public double getMaxXD() {
		return maxXD;
	}

	public void setMaxXD(double maxXD) {
		this.maxXD = maxXD;
		refreshScaling();
	}

	public double getMinYD() {
		return minYD;
	}

	public void setMinYD(double minYD) {
		this.minYD = minYD;
		refreshScaling();
	}

	public double getMaxYD() {
		return maxYD;
	}

	public void setMaxYD(double maxYD) {
		this.maxYD = maxYD;
		refreshScaling();
	}

	public long getMinXI() {
		return minXI;
	}

	public void setMinXI(long minXI) {
		this.minXI = minXI;
		refreshScaling();
	}

	public long getMaxXI() {
		return maxXI;
	}

	public void setMaxXI(long maxXI) {
		this.maxXI = maxXI;
		refreshScaling();
	}

	public long getMinYI() {
		return minYI;
	}

	public void setMinYI(long minYI) {
		this.minYI = minYI;
		refreshScaling();
	}

	public long getMaxYI() {
		return maxYI;
	}

	public void setMaxYI(long maxYI) {
		this.maxYI = maxYI;
		refreshScaling();
	}

}
