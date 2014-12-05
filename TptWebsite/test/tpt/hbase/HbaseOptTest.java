package tpt.hbase;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import tpt.info.PageSelectionInfo;
import tpt.info.QueryCondition;

public class HbaseOptTest {

	@Test
	public void testA() {
		String t[] = { "test" };
		try {
			System.out.println("start test");
			// System.out.println("Start scan...");
			// HbaseOpt.scan("test");
			System.out.println("test condition");
			QueryCondition u1 = new QueryCondition("CytoplasmMinIntensity",
					"=", "31.129194");
			List<QueryCondition> allCon = new ArrayList<QueryCondition>();
			allCon.add(u1);
			HbaseOpt.searchByCon("CancerDatabase", allCon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testB() {

		// String fts[] = { "features" };
		// HbaseOpt.creatTable("CancerDatabase", fts);
		try {

			// insertDatafile("/data07/shared/matlabOutput/");
			String imagepath = RetriveImage
					.getImagePath("TCGA-06-0208-01Z-00-DX3:012288.012288:14222.2:15250.7");
			System.out.println("Image path:-" + imagepath);
			BufferedImage result = null;
			String coords = "0.3588,0.0995;0.1384,0.8903;0.0888,0.1631;";

			result = ImageClip.processImage(imagepath, coords, "");

			RetriveImage.StoreResult(result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testC() {
		String t = "TCGA-19-4065-01Z-00-DX1:069632.008192:71381.9:9222.1";
		List<Integer> result = RetriveImage.getXY(t);
		System.out.print(result.toString());
	}
	
	@Test
	public void testD() {
		String t = "TCGA-19-4065-01Z-00-DX1:069632.008192:71381.9:9222.1";
		int result[] = ImageClip.getOnePoint(t);
		System.out.println(result[0]);
		System.out.println(result[1]);
	}

	@Test
	public void testE() {
		PageSelectionInfo.generateMaxMin();
	}

}
