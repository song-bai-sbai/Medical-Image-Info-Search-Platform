package tpt.hbase;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import tpt.info.ImgPoint;
import tpt.info.PageSelectionInfo;

public class RetriveImage {

	public static BufferedImage retriveImg(String rowKey, String coords) {
		String imagepath = getImagePath(rowKey);
		System.out.println("Image path:-" + imagepath);
		BufferedImage result = null;

		result = ImageClip.processImage(imagepath, coords, rowKey);

		// RetriveImage.StoreResult(result);

		return result;
	}

	public static String getImagePath(String rowkey) {
		String path = PageSelectionInfo.sourceImgPath;
		File folder = new File(path);
		String org;
		File[] listOfOrgs = folder.listFiles();
		for (int k = 0; k < listOfOrgs.length; k++) {
			String org_path = path;
			org = listOfOrgs[k].getName();
			// System.out.println("The organization name:" + org);
			org_path = org_path + "/" + org;
			String slide;
			File[] listOfSlides = listOfOrgs[k].listFiles();

			for (int i = 0; i < listOfSlides.length; i++) {
				String slide_path = org_path;
				if (listOfSlides[i].isDirectory()) {
					slide = listOfSlides[i].getName();
					// System.out.println("	The slide name:" + slide);
					String slide_prefix = getSlideprefix(rowkey);
					if (slide.contains(slide_prefix)) {
						// System.out.println("		The slide name matched:" +
						// slide);
						slide_path = slide_path + "/" + slide;

						String segment_image;
						File[] listOfSegments = listOfSlides[i].listFiles();
						for (int j = 0; j < listOfSegments.length; j++) {
							// System.out.println("Inside Secondfor loop");
							String seg_path = slide_path;
							if (listOfSegments[j].isFile()) {
								segment_image = listOfSegments[j].getName();
								if (segment_image.endsWith(".jpg")) {
									String req_seg = getSegment(rowkey);
									if (segment_image.contains(req_seg)) {
										String result = seg_path + "/"
												+ segment_image;
										return result;
									}

								}
							}
						}
					}
				}
			}

		}

		return null;
	}

	public static void StoreResult(BufferedImage result) {
		cleanDestination("/data07/shared/result/");

		// File out = new
		// File("C:\\Users\\Ashwin\\workspace\\Data Analytics\\src\\imageClip\\src\\result.jpg");
		File out = new File("/data07/shared/result/result.jpg");
		try {
			ImageIO.write(result, "jpg", out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void cleanDestination(String folder) {
		File result_folder = new File(folder);

		for (File file : result_folder.listFiles())
			if (!file.isDirectory())
				file.delete();

	}

	public static String getSlideprefix(String rowkey) {

		String temp = rowkey;
		String[] splitString = temp.split("\\:");
		String req_parts = splitString[0];
		return req_parts;
	}

	public static String getSegment(String rowkey) {

		String temp = rowkey;
		String[] splitString = temp.split("\\:");
		String req_parts = splitString[1];
		return req_parts;
	}

	public static List<Integer> getXY(String rowkey) {
		rowkey = rowkey.substring(rowkey.indexOf(':') + 1);
		rowkey = rowkey.substring(0, rowkey.indexOf(':'));
		String one = rowkey.substring(0, rowkey.indexOf('.'));
		String two = rowkey.substring(rowkey.indexOf('.') + 1);
		List<Integer> result = new LinkedList<Integer>();
		result.add(Integer.valueOf(one));
		result.add(Integer.valueOf(two));
		return result;
	}

	public static BufferedImage getSquareCenterAt(BufferedImage image, int x,
			int y, int length) {
		// BufferedImage img=convertToARGB(image);
		Color color = new Color(0, 255, 0);
		int rgb = color.getRGB();

		int width = image.getWidth(), height = image.getHeight();
		int minX = Math.max(x - length, 0), maxX = Math.min(x + length, width), minY = Math
				.max(y - length, 0), maxY = Math.min(y + length, height);

		for (int i = minY; i < maxY; i++)
			for (int j = minX; j < maxX; j++)
				image.setRGB(j, i, rgb);

		return image;

	}

	public static BufferedImage resize(BufferedImage source, int targetW,
			int targetH) {
		int type = source.getType();
		BufferedImage target = null;
		double sx = (double) targetW / source.getWidth();
		double sy = (double) targetH / source.getHeight();
		if (sx < sy) {
			sx = sy;
			targetW = (int) (sx * source.getWidth());
		} else {
			sy = sx;
			targetH = (int) (sy * source.getHeight());
		}
		if (type == BufferedImage.TYPE_CUSTOM) {
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetW,
					targetH);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else
			target = new BufferedImage(targetW, targetH, type);
		Graphics2D g = target.createGraphics();
		// smoother than exlax:
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return target;
	}

	public static BufferedImage convertToARGB(BufferedImage image) {
		BufferedImage newImage = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}

	public static BufferedImage retriveSlide(String rowKey) {
		String imagepath = getImagePath(rowKey);
		System.out.println("Image path:-" + imagepath);
		BufferedImage result = null;

		try {
			result = ImageIO.read(new File(imagepath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static BufferedImage getSquareCentersAt(BufferedImage image,
			List<ImgPoint> points) {
		// BufferedImage img=convertToARGB(image);
		Color color = new Color(0, 255, 0);
		int rgb = color.getRGB();
		int additional = 1;
		int width = image.getWidth(), height = image.getHeight();
		int minX, maxX, minY, maxY;
		if (points.size() < 30)
			additional = 5;
		for (int p = 0; p < points.size(); p++) {
			System.out.println("points " + points.get(p).getX() + ", "
					+ points.get(p).getY());
			minX = Math.max(points.get(p).getX() - points.get(p).getR()
					* additional, 0);
			maxX = Math.min(points.get(p).getX() + points.get(p).getR()
					* additional, width);
			minY = Math.max(points.get(p).getY() - points.get(p).getR()
					* additional, 0);
			maxY = Math.min(points.get(p).getY() + points.get(p).getR()
					* additional, height);
			for (int i = minY; i < maxY; i++)
				for (int j = minX; j < maxX; j++)
					image.setRGB(j, i, rgb);

		}

		return image;
	}

	public static void generateResultImage(String rowKey, List<ImgPoint> points) {
		BufferedImage img = RetriveImage.retriveSlide(rowKey);
		BufferedImage markedImg = RetriveImage.getSquareCentersAt(img, points);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] imageInByte = null;
		try {
			ImageIO.write(markedImg, "jpg", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String splitString[] = rowKey.split("\\:");
		String imgName = splitString[0] + ":" + splitString[1];
		File file = new File(PageSelectionInfo.resultPath + imgName + ".jpg");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(imageInByte);
		} catch (FileNotFoundException e) {
		} catch (IOException ioe) {
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ioe) {
			}
		}

	}
}
