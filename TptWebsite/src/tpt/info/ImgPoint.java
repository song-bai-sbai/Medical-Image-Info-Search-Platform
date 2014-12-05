package tpt.info;

import tpt.hbase.ImageClip;

public class ImgPoint {
	private int x;
	private int y;
	private int r;

	public ImgPoint(String rowkey) {
		int xy[] = ImageClip.getOnePoint(rowkey);
		x = xy[0];
		y = xy[1];
		r = 10;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

}
