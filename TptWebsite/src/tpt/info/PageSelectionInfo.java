package tpt.info;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PageSelectionInfo {

	public static String tableName = "CancerDatabaseNew";
	public static String minMaxFile = "/data07/shared/G5/min_max.txt";
	public static String sourceImgPath = "/data07/shared/matlabOutput2";
	public static String resultPath = "/data07/shared/results/";
	public static String[] Project = { "", "TCGA" };
	public static String[] TSS = { "", "06", "12", "14", "19", "27" };
	public static String[] Paticipant = { "", "0208", "0648", "5417 ", "0657",
			"1829", "4065 ", "1836 " };
	public static String[] Sample = { "", "01" };
	public static String[] Vial = { "", "Z" };
	public static String[] Portion = { "", "00" };

	public static Map<String, String[]> minMaxValue = new HashMap<String, String[]>();

	public static String defaultQuery = "Perimeter=' '; Eccentricity=' '; Circularity=' ';"
			+ "MajorAxisLength=' '; MinorAxisLength=' '; Extent=' '; Solidity=' '; FSD1=' ';"
			+ "FSD2=' '; FSD3=' '; FSD4=' '; FSD5=' '; FSD6=' '; HematoxlyinMeanIntensity=' ';"
			+ "HematoxlyinMeanMedianDifferenceIntensity=' '; HematoxlyinMaxIntensity=' ';"
			+ "HematoxlyinMinIntensity=' '; HematoxlyinStdIntensity=' '; HematoxlyinEntropy=' ';"
			+ "HematoxlyinEnergy=' '; HematoxlyinSkewness=' '; HematoxlyinKurtosis=' ';"
			+ "HematoxlyinMeanGradMag=' '; HematoxlyinStdGradMag=' '; HematoxlyinEntropyGradMag=' ';"
			+ "HematoxlyinEnergyGradMag=' '; HematoxlyinSkewnessGradMag=' '; HematoxlyinKurtosisGradMag=' ';"
			+ "HematoxlyinSumCanny=' '; HematoxlyinMeanCanny=' '; CytoplasmMeanIntensity=' ';"
			+ "CytoplasmMeanMedianDifferenceIntensity=' '; CytoplasmMaxIntensity=' ';"
			+ "CytoplasmMinIntensity=' '; CytoplasmStdIntensity=' '; CytoplasmEntropy=' ';"
			+ "CytoplasmEnergy=' '; CytoplasmSkewness=' '; CytoplasmKurtosis=' '; CytoplasmMeanGradMag=' ';"
			+ "CytoplasmStdGradMag=' '; CytoplasmEntropyGradMag=' '; CytoplasmEnergyGradMag=' ';"
			+ "CytoplasmSkewnessGradMag=' '; CytoplasmKurtosisGradMag=' '; CytoplasmSumCanny=' ';"
			+ "CytoplasmMeanCanny=' ';";

	static {
		generateMaxMin();
	}

	public static List<String> getInfoList(String[] info) {
		List<String> result = new LinkedList<String>();
		for (int i = 0; i < info.length; i++) {
			result.add(info[i]);
		}
		return result;
	}

	public static Map<String, String[]> getAllSelection() {
		Map<String, String[]> result = new LinkedHashMap<String, String[]>();
		result.put("Project", Project);
		result.put("TSS", TSS);
		result.put("Paticipant", Paticipant);
		result.put("Sample", Sample);
		result.put("Vial", Vial);

		return result;
	}

	public static void generateMaxMin() {
		String fileName = minMaxFile;
		String line, feature;
		String[] temp;
		double min, max;
		try {
			FileReader inputFile = new FileReader(fileName);
			BufferedReader bufferReader = new BufferedReader(inputFile);

			while ((line = bufferReader.readLine()) != null) {
				temp = line.split(";");
				feature = temp[0];
				min = Double.parseDouble(temp[1]);
				max = Double.parseDouble(temp[2]);
				String[] mm = new String[2];
				mm[0] = String.valueOf(min);
				mm[1] = String.valueOf(max);
				minMaxValue.put(feature, mm);
			}
			bufferReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
