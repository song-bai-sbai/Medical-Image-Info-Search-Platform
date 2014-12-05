package tpt.hbase;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageClip {
	public static BufferedImage clip(BufferedImage image, float... clipVerts) /*
																			 * {x1
																			 * ,
																			 * y1
																			 * ,
																			 * x2
																			 * ,
																			 * y2
																			 * ...
																			 * }
																			 * value
																			 * between
																			 * 0
																			 * -
																			 * 1
																			 */
	{
		assert clipVerts.length >= 6;
		assert clipVerts.length % 2 == 0;

		int[] xp = new int[clipVerts.length / 2];
		int[] yp = new int[xp.length];

		int minX = image.getWidth(), minY = image.getHeight(), maxX = 0, maxY = 0;

		for (int j = 0; j < xp.length; j++) {
			xp[j] = Math.round(clipVerts[2 * j] * image.getWidth());
			yp[j] = Math.round(clipVerts[2 * j + 1] * image.getHeight());

			minX = Math.min(minX, xp[j]);
			minY = Math.min(minY, yp[j]);
			maxX = Math.max(maxX, xp[j]);
			maxY = Math.max(maxY, yp[j]);
		}

		for (int i = 0; i < xp.length; i++) {
			xp[i] -= minX;
			yp[i] -= minY;
		}

		Polygon clip = new Polygon(xp, yp, xp.length);
		BufferedImage out = new BufferedImage(maxX - minX, maxY - minY,
				image.getType());
		Graphics g = out.getGraphics();
		g.setClip(clip);

		g.drawImage(image, -minX, -minY, null);
		g.dispose();

		return out;
	}

	public static float[] getCoords(String coords) {
		System.out.println("Into getCoords");
		String temp = coords;
		String[] splitString = temp.split("[,;]");
		float[] coord = new float[splitString.length];
		for (int i = 0; i < splitString.length; i++) {
			coord[i] = Float.parseFloat(splitString[i]);
		}
		return coord;
	}

	public static BufferedImage processImage(String path, String coords,
			String rowkey) {
		BufferedImage img = null;
		BufferedImage result = null;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		float coordinate[] = getCoords(coords);
		List<Integer> xy = RetriveImage.getXY(rowkey);
		for (int i = 0; i < coordinate.length;) {
			coordinate[i] = (coordinate[i] - xy.get(0)) / img.getWidth();
			coordinate[i + 1] = (coordinate[i + 1] - xy.get(1))
					/ img.getHeight();
			i = i + 2;
		}

		result = clip(img, coordinate);
		return result;

	}

	public static int[] getOnePoint(String rowkey) {
		List<Integer> xy = RetriveImage.getXY(rowkey);
		String cy = rowkey.substring(rowkey.lastIndexOf(":") + 1);
		rowkey = rowkey.substring(0, rowkey.lastIndexOf(":") - 1);
		String cx = rowkey.substring(rowkey.lastIndexOf(":") + 1);
		float x = Float.valueOf(cx);
		float y = Float.valueOf(cy);

		int result[] = new int[2];
		result[0] = (int) (x - xy.get(0));
		result[1] = (int) (y - xy.get(1));
		return result;
	}
}
