package tpt.hbase;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import tpt.action.CacheInfo;
import tpt.info.QueryCondition;
import tpt.info.ReturnedResults;
import tpt.info.TransRecord;

public class HbaseOpt {

	public static Configuration cfg = null;

	static {

		cfg = HBaseConfiguration.create();
	}

	public static ReturnedResults searchByConition(String tablename,
			List<QueryCondition> conList) {
		HTable table = null;
		List<Filter> filters = new LinkedList<Filter>();
		// List<TransRecord> transrecords = new LinkedList<TransRecord>();
		// List<ImgPoint> allPoints = new LinkedList<ImgPoint>();
		Map<String, List<TransRecord>> imageCellMap = new HashMap<String, List<TransRecord>>();
		int count = 0;
		String stat = "";
		ReturnedResults rr;
		try {
			table = new HTable(cfg, tablename);
			Scan s = new Scan();
			for (QueryCondition con : conList) {
				CompareOp op = CompareOp.EQUAL;
				if (con.getOperator().equals("=")) {
					op = CompareOp.EQUAL;
				} else if (con.getOperator().equals("<")) {
					op = CompareOp.LESS;
				} else if (con.getOperator().equals(">")) {
					op = CompareOp.GREATER;
				} else if (con.getOperator().equals("<=")) {
					op = CompareOp.LESS_OR_EQUAL;
				} else if (con.getOperator().equals(">=")) {
					op = CompareOp.GREATER_OR_EQUAL;
				} else if (con.getOperator().equals("!=")) {
					op = CompareOp.NOT_EQUAL;
				}
				System.out.println(con.getVariable() + " " + op + " "
						+ con.getValue());
				SingleColumnValueFilter colValFilter = new SingleColumnValueFilter(
						Bytes.toBytes("features"), Bytes.toBytes(con
								.getVariable()), op, new BinaryComparator(
								Bytes.toBytes(con.getValue())));
				colValFilter.setFilterIfMissing(true);
				filters.add(colValFilter);
			}
			// and
			FilterList filterList = new FilterList(Operator.MUST_PASS_ALL,
					filters);
			s.setFilter(filterList);

			ResultScanner scanner = table.getScanner(s);
			String key = new String("~");
			String keyFlag = new String("~");
			System.out.println("Scanning table... ");

			String temp = null;
			String[] splitString = null;
			String imgName = null;
			List<TransRecord> tempList = null;
			List<Double> tempValueList = null;

			for (Result result : scanner) {
				key = "~";
				count++;
				String keyString = "";
				String contentString = "";
				TransRecord tr = null;
				for (KeyValue kv : result.raw()) {

					if (key.compareTo(keyFlag) == 0) {
						key = Bytes.toString(kv.getRow());
						keyString = key;
						// System.out.print("Key: " + key);
					}
					/*
					 * System.out.print(", " + Bytes.toString(kv.getFamily()) +
					 * "." + Bytes.toString(kv.getQualifier()));
					 * System.out.print("=" + Bytes.toString(kv.getValue()));
					 */
					if (Bytes.toString(kv.getQualifier()).equals("Boundaries:")) {
						continue;
					}
					contentString += Bytes.toString(kv.getQualifier()) + "="
							+ Bytes.toString(kv.getValue()) + ";";

					tempValueList = CacheInfo.featureValues.get(Bytes
							.toString(kv.getQualifier()));
					if (tempValueList != null)
						tempValueList.add(Double.parseDouble(Bytes.toString(kv
								.getValue())));
					else {
						List<Double> newValueList = new LinkedList<Double>();
						try {
							newValueList.add(Double.parseDouble(Bytes
									.toString(kv.getValue())));
							CacheInfo.featureValues.put(
									Bytes.toString(kv.getQualifier()),
									newValueList);
						} catch (NumberFormatException e) {
							;
						}
					}
					// transrecords.add(tr);
					// allPoints.add(new ImgPoint(keyString));

				}
				tr = new TransRecord(keyString, contentString);

				temp = keyString;
				splitString = temp.split("\\:");
				imgName = splitString[0] + ":" + splitString[1];
				tempList = imageCellMap.get(imgName);

				if (tempList != null)
					tempList.add(tr);
				else {
					List<TransRecord> newImageCellList = new LinkedList<TransRecord>();
					newImageCellList.add(tr);
					imageCellMap.put(imgName, newImageCellList);
				}
			}

			System.out.println("Total number of results: " + count);
			System.out.println("---------------------");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (table != null) {
					table.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		rr = new ReturnedResults(count, stat, imageCellMap);
		System.out.println("Number of images: " + imageCellMap.size());
		/*
		 * for (String key : rr.getImageCellMap().keySet()) {
		 * System.out.println("Key: " + key + ", Size: " +
		 * rr.getImageCellMap().get(key).size()); }
		 */

		return rr;
	}

	private static boolean checkSp(String keyString,
			List<QueryCondition> spConditions) {
		for (QueryCondition qc : spConditions) {
			String value = qc.getValue().trim();
			if (!keyString.contains(value)) {
				System.out.println(value + ":" + keyString);
				return false;
			}
		}
		return true;
	}

	public static List<Result> searchByCon(String tablename,
			List<QueryCondition> conList) {
		HTable table = null;
		List<Result> results = new LinkedList<Result>();
		List<Filter> filters = new LinkedList<Filter>();
		try {
			table = new HTable(cfg, tablename);
			Scan s = new Scan();
			for (QueryCondition con : conList) {
				CompareOp op = CompareOp.EQUAL;
				if (con.getOperator().equals("=")) {
					op = CompareOp.EQUAL;
				} else if (con.getOperator().equals("<")) {
					op = CompareOp.LESS;
				} else if (con.getOperator().equals(">")) {
					op = CompareOp.GREATER;
				} else if (con.getOperator().equals("<=")) {
					op = CompareOp.LESS_OR_EQUAL;
				} else if (con.getOperator().equals(">=")) {
					op = CompareOp.GREATER_OR_EQUAL;
				} else if (con.getOperator().equals("!=")) {
					op = CompareOp.NOT_EQUAL;
				}
				SingleColumnValueFilter a = new SingleColumnValueFilter(
						Bytes.toBytes("features"), Bytes.toBytes(con
								.getVariable()), op, Bytes.toBytes(con
								.getValue()));

				filters.add(a);
			}
			// and
			FilterList filterList = new FilterList(Operator.MUST_PASS_ALL,
					filters);
			s.setFilter(filterList);
			ResultScanner rs = table.getScanner(s);
			for (Result r : rs) {
				System.out.println("search result: " + r);
				results.add(r);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (table != null) {
					table.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

	// create table
	public static void creatTable(String tableName, String[] familys) {
		HBaseAdmin admin = null;
		try {
			admin = new HBaseAdmin(cfg);
			if (admin.tableExists(tableName)) {
				System.out.println("table already exists!");
			} else {
				HTableDescriptor tableDesc = new HTableDescriptor(
						TableName.valueOf(tableName));
				for (int i = 0; i < familys.length; i++) {
					tableDesc.addFamily(new HColumnDescriptor(familys[i]));
				}
				admin.createTable(tableDesc);
				System.out.println("create table " + tableName + " ok.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (admin != null) {
					admin.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// insert a record
	public static void put(String tablename, String row, String columnFamily,
			String column, String data) {
		HTable table = null;
		try {
			table = new HTable(cfg, tablename);
			Put p1 = new Put(Bytes.toBytes(row));
			p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),
					Bytes.toBytes(data));
			table.put(p1);
			System.out.println("put '" + row + "','" + columnFamily + ":"
					+ column + "','" + data + "'");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (table != null) {
					table.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// get a record
	public static Result get(String tablename, String row) {
		HTable table = null;
		Result result = null;
		try {
			table = new HTable(cfg, tablename);
			Get g = new Get(Bytes.toBytes(row));
			result = table.get(g);
			System.out.println("Get: " + result);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (table != null) {
					table.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (result != null && !result.isEmpty()) {
			return result;
		} else {
			return null;
		}
	}

	// scan the table, get all result
	public static List<Result> scan(String tablename) {
		HTable table = null;
		List<Result> results = new LinkedList<Result>();
		try {
			table = new HTable(cfg, tablename);
			Scan s = new Scan();
			ResultScanner rs = table.getScanner(s);
			for (Result r : rs) {
				System.out.println("Scan: " + r);
				results.add(r);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (table != null) {
					table.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

	// delete table
	public static boolean deleteTable(String tablename) {
		HBaseAdmin admin = null;
		try {
			admin = new HBaseAdmin(cfg);
			if (admin.tableExists(tablename)) {
				admin.disableTable(tablename);
				admin.deleteTable(tablename);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (admin != null) {
					admin.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public static String getBoundary(String tablename, String rowkey) {
		String boundary = "";
		Result result = null;
		try {
			Get get = new Get(Bytes.toBytes(rowkey));
			HTable table;
			table = new HTable(cfg, Bytes.toBytes(tablename));
			result = table.get(get);
			for (KeyValue kv : result.list()) {

				if (Bytes.toString(kv.getQualifier()).equals("Boundaries:")) {
					System.out
							.println("Value " + Bytes.toString(kv.getValue()));
					boundary = Bytes.toString(kv.getValue());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return boundary;
	}

}
