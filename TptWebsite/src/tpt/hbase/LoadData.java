package tpt.hbase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class LoadData {
	public static void insertDatafile(String path) {
		// String path = ".";
		// System.out.println("Inside insertdata");

		File folder = new File(path);
		String org;

		File[] listOfOrgs = folder.listFiles();
		for (int k = 0; k < listOfOrgs.length; k++) {
			String org_path = path;
			org = listOfOrgs[k].getName();
			System.out.println("The organization name:" + org);
			org_path = org_path + "/" + org;
			String slide;
			File[] listOfSlides = listOfOrgs[k].listFiles();

			for (int i = 0; i < listOfSlides.length; i++) {
				// System.out.println("Inside firstfor loop");
				String slide_path = org_path;
				if (listOfSlides[i].isDirectory()) {
					slide = listOfSlides[i].getName();

					// System.out.println("Slide name:"+slide);
					slide_path = slide_path + "/" + slide;

					String segment;
					File[] listOfSegments = listOfSlides[i].listFiles();
					for (int j = 0; j < listOfSegments.length; j++) {
						// System.out.println("Inside Secondfor loop");
						String seg_path = slide_path;
						if (listOfSegments[j].isFile()) {
							segment = listOfSegments[j].getName();
							if (segment.endsWith(".txt")) {
								// System.out.println("Segment text file:"+segment);
								String file_name = segment;
								String file_path = seg_path;
								// System.out.println("file_path:- "+file_path+"/"+segment);
								processFile(file_path, file_name);
							}
						}
					}
				}
			}
		}

	}

	public static void processFile(String filepath, String file) {

		List<?> fields = null;
		List<?> values = null;
		HTable table = null;
		try {
			table = new HTable(HbaseOpt.cfg, "CancerDatabase");
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			try {
				Scanner fileinput = new Scanner(new FileReader(filepath + "/"
						+ file));
				if (fileinput.hasNextLine()) {
					fields = processFirstLine(fileinput.nextLine());
				}
				// System.out.println(fields);
				// System.out.println(fields.size());
				String row_key;
				row_key = processFileName(file);
				while (fileinput.hasNextLine()) {
					values = processValues(fileinput.nextLine()); // Process the
																	// values of
																	// the first
																	// entry in
																	// the
																	// file(Trial)
					// System.out.println(values);
					// System.out.println(values.size());

					String x = (values.get(1)).toString();
					String y = (values.get(2)).toString();

					// System.out.println(x);
					// System.out.println(y);

					for (int i = 3; i < values.size(); i++) {
						// System.out.println(i);
						// System.out.println(""+fields.get(i)+":-"+values.get(i));
						Put p1 = new Put(Bytes.toBytes(row_key + ":" + x + ":"
								+ y + ""));
						p1.add(Bytes.toBytes("features"),
								Bytes.toBytes((fields.get(i)).toString()),
								Bytes.toBytes((values.get(i)).toString()));
						table.put(p1);
						// HbaseOpt.put("CancerDatabase",row_key+":"+x+":"+y+"","features",(fields.get(i)).toString(),(values.get(i)).toString());
					}
				}
				fileinput.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (table != null) {
				table.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String processFileName(String filename) {

		String temp = filename;
		String[] splitString = temp.split("\\.");
		String req_parts = splitString[0] + ":" + splitString[3] + "."
				+ splitString[4];
		return req_parts;
	}

	private static List<String> processFirstLine(String nextLine) {
		List<String> fields = new ArrayList<String>();
		Scanner getFields = new Scanner(nextLine);
		while (getFields.hasNext()) {
			// System.out.println(getFields.next());
			fields.add(getFields.next());
		}
		getFields.close();
		return fields;

	}

	private static List<String> processValues(String nextLine) {
		List<String> valuelist = new ArrayList<String>();
		Scanner getFields = new Scanner(nextLine);
		while (getFields.hasNext()) {
			// System.out.println(getFields.next());
			valuelist.add(getFields.next());
		}
		getFields.close();
		return valuelist;

	}

}
