package tpt.hbase;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import tpt.info.TransRecord;

public class Histogram {

	public static HistogramInfo generateHistogram(String feature,
			List<Double> list) {
		TransRecord tr1, tr2, tr3, tr4;
		List<TransRecord> temp;
		HistogramInfo hi;
		if (list.size() == 1) {
			tr1 = new TransRecord(list.get(0).toString(), "1");
			tr2 = new TransRecord("N/A", "0");
			tr3 = new TransRecord("N/A", "0");
			tr4 = new TransRecord("N/A", "0");

		} else {
			Collections.sort(list);
			double min = list.get(0), max = list.get(list.size() - 1);
			double m1 = min + (max - min) / 4, m2 = min + (max - min) / 2, m3 = min
					+ (max - min) * 3 / 4;
			int c1 = 0, c2 = 0, c3 = 0, c4 = 0;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) < m1)
					c1++;
				else if (list.get(i) < m2)
					c2++;
				else if (list.get(i) < m3)
					c3++;
				else
					c4++;
			}
			tr1 = new TransRecord("[" + Double.toString(min).substring(0, 3)
					+ "," + Double.toString(m1).substring(0, 3) + "]",
					Integer.toString(c1));
			tr2 = new TransRecord("[" + Double.toString(m1).substring(0, 3)
					+ "," + Double.toString(m2).substring(0, 3) + "]",
					Integer.toString(c2));
			tr3 = new TransRecord("[" + Double.toString(m2).substring(0, 3)
					+ "," + Double.toString(m3).substring(0, 3) + "]",
					Integer.toString(c3));
			tr4 = new TransRecord("[" + Double.toString(m3).substring(0, 3)
					+ "," + Double.toString(max).substring(0, 3) + "]",
					Integer.toString(c4));
		}
		temp = new LinkedList<TransRecord>();
		temp.add(tr1);
		temp.add(tr2);
		temp.add(tr3);
		temp.add(tr4);
		hi = new HistogramInfo(feature, temp);
		return hi;

	}
}
