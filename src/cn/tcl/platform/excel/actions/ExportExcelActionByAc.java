package cn.tcl.platform.excel.actions;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.json.util.WebUtils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.excel.service.IExcelService;
import cn.tcl.platform.excel.service.IExportExcelService;
import cn.tcl.platform.excel.vo.Excel;

/**
 * 报表导出
 * 
 * @author 陈婕
 * 
 */
public class ExportExcelActionByAc extends BaseAction {

	@Autowired
	@Qualifier("excelService")
	private IExcelService excelService;

	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	String user;
	String countryId;
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdw = new SimpleDateFormat("E");
	static SimpleDateFormat formatEn = new SimpleDateFormat("MMMM.dd,yyyy",
			Locale.ENGLISH);

	static SimpleDateFormat mm = new SimpleDateFormat("MMMM", Locale.ENGLISH);
	static SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.ENGLISH);

	/**
	 * 周报 0.1459 min
	 * 
	 * @throws Exception
	 */
	public void exportWeekly() throws Exception {
		long time = System.currentTimeMillis();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ "Weekly Reports" + ".xlsx");
		// 创建工作簿
		SXSSFWorkbook wb = new SXSSFWorkbook();
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		if (WebPageUtil.getLoginedUserId() != null) {
			user = WebPageUtil.getLoginedUserId();
			countryId = excelService.selectCountryByUser(user);
		}

		String partyName = exportExcelService.selectPartyByUser(user);
	
		LinkedList<HashMap<String, Object>> list = DateUtil.getWeek(beginDate,
				endDate);
		String [] days=endDate.split("-");
		String tBeginDate=days[0]+"-"+days[1]+"-01";
		String tEndDate=days[0]+"-"+days[1]+"-31";
		
		common(wb, list, "Weekly Reports",tBeginDate,tEndDate);
		
		exportWeekThread(wb, beginDate, endDate,tBeginDate,tEndDate);
		// 此处为重点产品周销售
		//weekCPUsellout(wb, partyName, beginDate, endDate,tBeginDate,tEndDate);
		System.out.println("耗时：" + (System.currentTimeMillis() - time) + "毫秒");
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();

	}

	/**
	 * 月报 4.7052668
	 * 
	 * @throws Exception
	 */
	public void exportMonthly() throws Exception {

		long time = System.currentTimeMillis();
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ "Monthly Reports" + ".xlsx");
		SXSSFWorkbook wb = new SXSSFWorkbook();
		if (WebPageUtil.getLoginedUserId() != null) {
			user = WebPageUtil.getLoginedUserId();
			countryId = excelService.selectCountryByUser(user);
		}

		String partyName = exportExcelService.selectPartyByUser(user);

		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		LinkedList<HashMap<String, Object>> dateList = new LinkedList<HashMap<String, Object>>();
		List<KeyValueForDate> list = DateUtil.getKeyValueForDate(beginDate,
				endDate);
		for (KeyValueForDate keyValueForDate : list) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("beginDate", keyValueForDate.getStartDate());
			map.put("endDate", keyValueForDate.getEndDate());
			dateList.add(map);
		}
		String tBeginDate=beginDate;
		String tEndDate=endDate;
		
		common(wb, dateList, "Monthly Reports",tBeginDate,tEndDate);// 8.978 S
		sellOutSummary(wb, partyName, beginDate, endDate,tBeginDate,tEndDate);// 20.378 S
		comparative(wb, partyName, beginDate, endDate,tBeginDate,tEndDate);// 130.147 S
		DealerSellout(wb, partyName, beginDate, endDate,tBeginDate,tEndDate);// 6.733 S
		//monthCPUsellout(wb, partyName, countryId, beginDate, endDate,tBeginDate,tEndDate);//
		// 13.318
		// S
		monthYTDsellout(wb, partyName, beginDate, endDate);// 114.281 S
		System.out.println("耗时：" + (System.currentTimeMillis() - time) + "毫秒");
		// 月报耗时： 耗时：2847434毫秒
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();

	}

	/**
	 * 季报 6.0038168min
	 * 
	 * @throws Exception
	 */
	public void exportQuarterly() throws Exception {
		long time = System.currentTimeMillis();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ "Quarterly Reports" + ".xlsx");
		SXSSFWorkbook wb = new SXSSFWorkbook();
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		if (WebPageUtil.getLoginedUserId() != null) {
			user = WebPageUtil.getLoginedUserId();
			countryId = excelService.selectCountryByUser(user);
		}

		String q = request.getParameter("qua");

		String partyName = exportExcelService.selectPartyByUser(user);
		LinkedList<HashMap<String, Object>> dataList = DateUtil.splitSeason(
				beginDate, endDate);
		String tBeginDate=beginDate;
		String tEndDate=endDate;
		// 累计数据报表
		common(wb, dataList, "Quarterly Reports",tBeginDate,tEndDate);

		LinkedList<HashMap<String, Object>> dateList = new LinkedList<HashMap<String, Object>>();
		List<KeyValueForDate> list = DateUtil.getKeyValueForDate(beginDate,
				endDate);
		for (KeyValueForDate keyValueForDate : list) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("beginDate", keyValueForDate.getStartDate());
			map.put("endDate", keyValueForDate.getEndDate());
			dateList.add(map);
		}

		quaSellOutSummary(wb, dateList, partyName, beginDate, endDate,tBeginDate,tEndDate); // comm
																		// 3.8832334
		quaComparative(wb, dateList, q, partyName, beginDate, endDate,tBeginDate,tEndDate);// 3.4190334

		quaDealerSellout(wb, dateList, q, partyName, beginDate, endDate,tBeginDate,tEndDate);// 1.8575167
																			// 分(min)
		//monthCPUsellout(wb, partyName, countryId, beginDate, endDate,tBeginDate,tEndDate);// 0.2197333
																		// 分(min)
		quaYTDsellout(wb, dateList, q, partyName, beginDate, endDate);// 1.8575167
		System.out.println("耗时：" + (System.currentTimeMillis() - time) + "毫秒");

		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 年报
	 * 
	 * @throws Exception
	 */
	public void exportAnnual() throws Exception {
		long time = System.currentTimeMillis();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ "Annual Reports" + ".xlsx");
		SXSSFWorkbook wb = new SXSSFWorkbook();
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		if (WebPageUtil.getLoginedUserId() != null) {
			user = WebPageUtil.getLoginedUserId();
			countryId = excelService.selectCountryByUser(user);
		}

		String partyName = exportExcelService.selectPartyByUser(user);
		LinkedList<HashMap<String, Object>> dataList = DateUtil.splitYear(
				beginDate, endDate);
		String tBeginDate=beginDate;
		String tEndDate=endDate;
		common(wb, dataList, "Annual Reports",tBeginDate,tEndDate);
		LinkedList<HashMap<String, Object>> dateList = new LinkedList<HashMap<String, Object>>();
		List<KeyValueForDate> list = DateUtil.getKeyValueForDate(beginDate,
				endDate);
		for (KeyValueForDate keyValueForDate : list) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("beginDate", keyValueForDate.getStartDate());
			map.put("endDate", keyValueForDate.getEndDate());
			dateList.add(map);
		}
		quaSellOutSummary(wb, dateList, partyName, beginDate, endDate,tBeginDate,tEndDate);
		yearComparative(wb, dateList, partyName, beginDate, endDate,tBeginDate,tEndDate);
		yearDealerSellout(wb, dateList, partyName, beginDate, endDate,tBeginDate,tEndDate);
		//monthCPUsellout(wb, partyName, countryId, beginDate, endDate,tBeginDate,tEndDate);
		yearYTDsellout(wb, partyName, beginDate, endDate);
		System.out.println("耗时：" + (System.currentTimeMillis() - time) + "毫秒");
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 自定义报表
	 * 
	 * @throws Exception
	 */
	public void exportCustom() throws Exception {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ "Custom Reports" + ".xlsx");
		SXSSFWorkbook wb = new SXSSFWorkbook();
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String [] tBeginDates=beginDate.split("-");
		String [] tEndDates=endDate.split("-");
		
		String tBeginDate=tBeginDates[0]+"-"+tBeginDates[1]+"-01";
		String tEndDate=tEndDates[0]+"-"+tEndDates[1]+"-31";
		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("beginDate", beginDate);
		hashMap.put("endDate", endDate);
		dataList.add(hashMap);
		common(wb, dataList, "Custom Reports",tBeginDate,tEndDate);
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 将字符串放入字符串数组
	 * 
	 * @param arr
	 *            字符串数组
	 * @param str
	 *            字符串
	 * @return
	 */
	private static String[] insert(String[] arr, String str) {
		int size = arr.length;
		String[] tmp = new String[size + 1];
		System.arraycopy(arr, 0, tmp, 0, size);
		tmp[size] = str;
		return tmp;
	}

	public void common(SXSSFWorkbook wb,
			LinkedList<HashMap<String, Object>> list, String wbName,String tBeginDate,String tEndDate) {

		try {
			if (WebPageUtil.getLoginedUserId() != null) {
				user = WebPageUtil.getLoginedUserId();
			}
			String partyName = exportExcelService.selectPartyByUser(user);

			String searchStr = null;
			String conditions = "";
			String center = "";
			String country = "";
			String region = "";
			String office = "";

			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			if (!WebPageUtil.isHAdmin()) {
				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")
						|| request.getParameter("country") != null
						&& !request.getParameter("country").equals("")
						|| request.getParameter("region") != null
						&& !request.getParameter("region").equals("")
						|| request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {

					if (request.getParameter("center") != null
							&& !request.getParameter("center").equals("")) {
						center = request.getParameter("center");
						conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
								+ center + "')   ";
					}

					if (request.getParameter("country") != null
							&& !request.getParameter("country").equals("")) {
						country = request.getParameter("country");
						conditions = "  pa.country_id= " + country + "  ";
					}
					if (request.getParameter("region") != null
							&& !request.getParameter("region").equals("")) {
						region = request.getParameter("region");
						conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
								+ region
								+ "'  OR PARTY_ID='"+region+"'))  ";
					}
					if (request.getParameter("office") != null
							&& !request.getParameter("office").equals("")) {
						office = request.getParameter("office");
						conditions = "    pa.party_id IN ('" + office + "')  ";
					}

				} else {
					if (null != userPartyIds && !"".equals(userPartyIds)) {
						conditions = "  pa.party_id in (" + userPartyIds + ")  ";
					} else {
						conditions = "  1=2  ";
					}
				}

			} else {
				conditions = " 1=1 ";

			}
			if (request.getParameter("level") != null
					&& !request.getParameter("level").equals("")) {
				conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
			}
			List<Excel> excels = null;
			String sheetName = "";

			for (int r = 0; r < list.size(); r++) {
				String beginDate = (String) list.get(r).get("beginDate");
				String endDate = (String) list.get(r).get("endDate");
				sheetName = beginDate + " — " + endDate + "";
				// 表头数据
				String[] headers = { "SELL-OUT INFORMATION SHEET_REG.",
						"SELL-OUT INFORMATION SHEET_TYPE",
						"SELL-OUT INFORMATION SHEET_NO OF SHOP",
						"SELL-OUT INFORMATION SHEET_DEALER",
						"SELL-OUT INFORMATION SHEET_STORE", "AC FPS",
						"PROMODISER NAME", "DATE OF HIRED", "SHOP ID", "ACFO",
						"AREA", "SALESMAN", "AGENCY", "SHOP CLASS",
						"AIRCON SELL-OUT and TARGET_TTL AIRCON SO_ _QTY",
						"AIRCON SELL-OUT and TARGET_TTL AIRCON SO_ _AMOUNT",
						"AIRCON SELL-OUT and TARGET_TARGET",
						"AIRCON SELL-OUT and TARGET_Ach." };

				// 查询机型销售数据
				
				List<HashMap<String, Object>> modelMapListByWindow = excelService
						.selectModelBySpecByAc(beginDate, endDate,"%Window%", searchStr, conditions,WebPageUtil.isHQRole());

				for (int i = 0; i < modelMapListByWindow.size(); i++) {
					BigDecimal bd = new BigDecimal(modelMapListByWindow.get(i)
							.get("price").toString());
					String am = bd.toPlainString();
					double shop = Double.parseDouble(am);
					double price = shop
							/ Integer.parseInt(modelMapListByWindow.get(i)
									.get("shop").toString());
					long lnum = Math.round(price);
					String m = "WINDOW TYPE AIRCON" + "_"
							+ modelMapListByWindow.get(i).get("model") + "_"
							+ lnum + "_" + "sold";
					headers = insert(headers, m);

				}
				HashMap<String, ArrayList<HashMap<String, Object>>> WindowMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < modelMapListByWindow.size(); m++) {
					if (WindowMap.get(modelMapListByWindow.get(m)
							.get("model").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(modelMapListByWindow.get(m));
						WindowMap.put(modelMapListByWindow.get(m)
								.get("model").toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = WindowMap
								.get(modelMapListByWindow.get(m).get("model")
										.toString());
						modelList.add(modelMapListByWindow.get(m));
					}

				}

				List<HashMap<String, Object>> modelMapListByFission = excelService
						.selectModelBySpecByAc(beginDate, endDate,"%Fission%", searchStr, conditions,WebPageUtil.isHQRole());

				// 将查出来的机型销售数据放入表头，形成三级标题
				for (int i = 0; i < modelMapListByFission.size(); i++) {
					BigDecimal bd = new BigDecimal(modelMapListByFission
							.get(i).get("price").toString());
					String am = bd.toPlainString();
					double shop = Double.parseDouble(am);
					double price = shop
							/ Integer.parseInt(modelMapListByFission.get(i)
									.get("shop").toString());

					long lnum = Math.round(price);
					String m = "SPLIT TYPE AIRCON" + "_"
							+ modelMapListByFission.get(i).get("model") + "_"
							+ lnum + "_" + "sold";
					headers = insert(headers, m);
				

				}

				HashMap<String, ArrayList<HashMap<String, Object>>> FissionMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < modelMapListByFission.size(); m++) {
					if (FissionMap.get(modelMapListByFission.get(m)
							.get("model").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(modelMapListByFission.get(m));
						FissionMap.put(
								modelMapListByFission.get(m).get("model")
										.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = FissionMap
								.get(modelMapListByFission.get(m).get("model")
										.toString());
						modelList.add(modelMapListByFission.get(m));
					}

				}

				
				
				
				headers = insert(headers, "AIRCON  SUB-TOTAL_QTY");
				headers = insert(headers, "AIRCON  SUB-TOTAL_AMOUNT");
				
				List<HashMap<String, Object>> stockMapListByWindow = excelService
						.selectStockBymodelByAc("%Window%", searchStr, conditions,WebPageUtil.isHQRole(),beginDate,endDate);

				// 将查出来的机型销售数据放入表头，形成三级标题
				for (int i = 0; i < stockMapListByWindow.size(); i++) {
					String m = "WINDOW TYPE AIRCON" + "_"
							+ stockMapListByWindow.get(i).get("model") + "_"
							+ " " + "_" + "Stocks";
					headers = insert(headers, m);

				}

				HashMap<String, ArrayList<HashMap<String, Object>>> stockWindowMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < stockMapListByWindow.size(); m++) {
					if (stockWindowMap.get(stockMapListByWindow.get(m)
							.get("model").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(stockMapListByWindow.get(m));
						stockWindowMap.put(
								stockMapListByWindow.get(m).get("model")
										.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = stockWindowMap
								.get(stockMapListByWindow.get(m).get("model")
										.toString());
						modelList.add(stockMapListByWindow.get(m));
					}

				}

				List<HashMap<String, Object>> stockMapListByFission = excelService
						.selectStockBymodelByAc("%Fission%", searchStr, conditions,WebPageUtil.isHQRole(),beginDate,endDate);

				// 将查出来的机型销售数据放入表头，形成三级标题
				for (int i = 0; i < stockMapListByFission.size(); i++) {
					String m = "INTERNET STOCKS" + "_"
							+ stockMapListByFission.get(i).get("model") + "_"
							+ " " + "_" + "Stocks";
					headers = insert(headers, m);

				}

				HashMap<String, ArrayList<HashMap<String, Object>>> stockFissionMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < stockMapListByFission.size(); m++) {
					if (stockFissionMap.get(stockMapListByFission.get(m)
							.get("model").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(stockMapListByFission.get(m));
						stockFissionMap.put(
								stockMapListByFission.get(m).get("model")
										.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = stockFissionMap
								.get(stockMapListByFission.get(m).get("model")
										.toString());
						modelList.add(stockMapListByFission.get(m));
					}

				}
				
				
				
				
				
				
				headers = insert(headers, "AIRCON TTL INVENTORY_Stocks");

				List<HashMap<String, Object>> disMapListByWindow = excelService
						.selectDisplayBymodelByAc("%Window%", searchStr,
								conditions,WebPageUtil.isHQRole(),beginDate,endDate);

				// 将查出来的机型销售数据放入表头，形成三级标题
				for (int i = 0; i < disMapListByWindow.size(); i++) {
					String m = "WINDOW TYPE AIRCON" + "_"
							+ disMapListByWindow.get(i).get("model") + "_"
							+ " " + "_" + "DisPlay";
					headers = insert(headers, m);

				}

				HashMap<String, ArrayList<HashMap<String, Object>>> disWindowMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < disMapListByWindow.size(); m++) {
					if (disWindowMap.get(disMapListByWindow.get(m)
							.get("model").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(disMapListByWindow.get(m));
						disWindowMap.put(
								disMapListByWindow.get(m).get("model")
										.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = disWindowMap
								.get(disMapListByWindow.get(m).get("model")
										.toString());
						modelList.add(disMapListByWindow.get(m));
					}

				}

				
				
				List<HashMap<String, Object>> disMapListByFission = excelService
						.selectDisplayBymodelByAc("%Fission%", searchStr,
								conditions,WebPageUtil.isHQRole(),beginDate,endDate);

				// 将查出来的机型销售数据放入表头，形成三级标题
				for (int i = 0; i < disMapListByFission.size(); i++) {
					String m = "SPLIT TYPE AIRCON" + "_"
							+ disMapListByFission.get(i).get("model") + "_"
							+ " " + "_" + "DisPlay";
					headers = insert(headers, m);

				}

				HashMap<String, ArrayList<HashMap<String, Object>>> disFissionMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < disMapListByFission.size(); m++) {
					if (disFissionMap.get(disMapListByFission.get(m)
							.get("model").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(disMapListByFission.get(m));
						disFissionMap.put(
								disMapListByFission.get(m).get("model")
										.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = disFissionMap
								.get(disMapListByFission.get(m).get("model")
										.toString());
						modelList.add(disMapListByFission.get(m));
					}

				}

				
				

				headers = insert(headers, "AIRCON TTL DISPLAY_Display");

				// 按照对应格式将表头传入
				ArrayList columns = new ArrayList();
				for (int i = 0; i < headers.length; i++) {
					HashMap<String, Object> columnMap = new HashMap<String, Object>();
					columnMap.put("header", headers[i]);
					columnMap.put("field", headers[i]);
					columns.add(columnMap);
				}

				
				// 查询门店机型销售数据
				List<HashMap<String, Object>> modeldataListByWindow = excelService
						.selectModelListBySpecByAc("%Window%", beginDate, endDate,
								searchStr, conditions,WebPageUtil.isHQRole());

				// 按照门店进行销售数据分组
				HashMap<String, ArrayList<HashMap<String, Object>>> shopMapByWindow = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < modeldataListByWindow.size(); m++) {
					if (shopMapByWindow.get(modeldataListByWindow.get(m)
							.get("shop_id").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(modeldataListByWindow.get(m));
						shopMapByWindow.put(
								modeldataListByWindow.get(m).get("shop_id")
										.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = shopMapByWindow
								.get(modeldataListByWindow.get(m)
										.get("shop_id").toString());
						modelList.add(modeldataListByWindow.get(m));
					}

				}

				// 查询门店机型销售数据
				List<HashMap<String, Object>> modeldataListByFission = excelService
						.selectModelListBySpecByAc("%Fission%", beginDate,
								endDate, searchStr, conditions,WebPageUtil.isHQRole());

				// 按照门店进行销售数据分组
				HashMap<String, ArrayList<HashMap<String, Object>>> shopMapByFission = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < modeldataListByFission.size(); m++) {
					if (shopMapByFission.get(modeldataListByFission.get(m)
							.get("shop_id").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(modeldataListByFission.get(m));
						shopMapByFission.put(modeldataListByFission.get(m)
								.get("shop_id").toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = shopMapByFission
								.get(modeldataListByFission.get(m)
										.get("shop_id").toString());
						modelList.add(modeldataListByFission.get(m));
					}

				}
				
				
				List<HashMap<String, Object>> stockDataListByWindow = excelService
						.selectStockByDataByAc("%Window%", searchStr, conditions,WebPageUtil.isHQRole(),beginDate,endDate);

				// 按照门店进行销售数据分组
				HashMap<String, ArrayList<HashMap<String, Object>>> stockMapByWindow = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < stockDataListByWindow.size(); m++) {
					if (stockMapByWindow.get(stockDataListByWindow.get(m)
							.get("shop_id").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(stockDataListByWindow.get(m));
						stockMapByWindow.put(stockDataListByWindow.get(m)
								.get("shop_id").toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = stockMapByWindow
								.get(stockDataListByWindow.get(m)
										.get("shop_id").toString());
						modelList.add(stockDataListByWindow.get(m));
					}

				}

				List<HashMap<String, Object>> stockDataListByFission = excelService
						.selectStockByDataByAc("%Fission%", searchStr, conditions,WebPageUtil.isHQRole(),beginDate,endDate);

				// 按照门店进行销售数据分组
				HashMap<String, ArrayList<HashMap<String, Object>>> stockMapByFission = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < stockDataListByFission.size(); m++) {
					if (stockMapByFission.get(stockDataListByFission.get(m)
							.get("shop_id").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(stockDataListByFission.get(m));
						stockMapByFission.put(stockDataListByFission.get(m)
								.get("shop_id").toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = stockMapByFission
								.get(stockDataListByFission.get(m)
										.get("shop_id").toString());
						modelList.add(stockDataListByFission.get(m));
					}

				}
				
				
				
				
				
			

				List<HashMap<String, Object>> disDataListByWindow = excelService
						.selectDisplayByDataByAc("%Window%", searchStr, conditions,WebPageUtil.isHQRole(),beginDate,endDate);
				// 按照门店进行销售数据分组
				HashMap<String, ArrayList<HashMap<String, Object>>> disMapByWindow= new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < disDataListByWindow.size(); m++) {
					if (disMapByWindow.get(disDataListByWindow.get(m)
							.get("shop_id").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(disDataListByWindow.get(m));
						disMapByWindow.put(
								disDataListByWindow.get(m).get("shop_id")
										.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = disMapByWindow
								.get(disDataListByWindow.get(m).get("shop_id")
										.toString());
						modelList.add(disDataListByWindow.get(m));
					}

				}

				List<HashMap<String, Object>> disDataListByFission = excelService
						.selectDisplayByDataByAc("%Fission%", searchStr,
								conditions,WebPageUtil.isHQRole(),beginDate,endDate);
				// 按照门店进行销售数据分组
				HashMap<String, ArrayList<HashMap<String, Object>>> disMapByFission = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < disDataListByFission.size(); m++) {
					if (disMapByFission.get(disDataListByFission.get(m)
							.get("shop_id").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(disDataListByFission.get(m));
						disMapByFission.put(
								disDataListByFission.get(m).get("shop_id")
										.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = disMapByFission
								.get(disDataListByFission.get(m)
										.get("shop_id").toString());
						modelList.add(disDataListByFission.get(m));
					}

				}
				
				
				
				
			
				List<HashMap<String, Object>> StockTotalBySpec = excelService
						.selectStockByTotalByAc(searchStr, conditions,beginDate,endDate);
				// 按照门店进行销售数据分组
				HashMap<String, ArrayList<HashMap<String, Object>>> stockMapByTotal = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < StockTotalBySpec.size(); m++) {
					if (stockMapByTotal.get(StockTotalBySpec.get(m)
							.get("shop_id").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(StockTotalBySpec.get(m));
						stockMapByTotal.put(
								StockTotalBySpec.get(m).get("shop_id")
										.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = stockMapByTotal
								.get(StockTotalBySpec.get(m).get("shop_id")
										.toString());
						modelList.add(StockTotalBySpec.get(m));
					}

				}

				List<HashMap<String, Object>> disTotalBySpec = excelService
						.selectDisPlayByTotalByAc(searchStr, conditions,beginDate,endDate);

				// 按照门店进行销售数据分组
				HashMap<String, ArrayList<HashMap<String, Object>>> disPlayMapByTotal = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < disTotalBySpec.size(); m++) {
					if (disPlayMapByTotal.get(disTotalBySpec.get(m)
							.get("shop_id").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(disTotalBySpec.get(m));
						disPlayMapByTotal
								.put(disTotalBySpec.get(m).get("shop_id")
										.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = disPlayMapByTotal
								.get(disTotalBySpec.get(m).get("shop_id")
										.toString());
						modelList.add(disTotalBySpec.get(m));
					}

				}
				// 查询所有导购员，督导与业务员
				List<HashMap<String, Object>> salerList = excelService
						.selectSalerDatas(searchStr, conditions);

				HashMap<String, ArrayList<HashMap<String, Object>>> salerDatas = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < salerList.size(); m++) {
					if (salerDatas.get(salerList.get(m).get("shop_id")
							.toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(salerList.get(m));
						salerDatas.put(salerList.get(m).get("shop_id")
								.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = salerDatas
								.get(salerList.get(m).get("shop_id").toString());
						modelList.add(salerList.get(m));
					}

				}
				

				// 根据门店取得对应销售数据与目标数据
				List<HashMap<String, Object>> targetList = excelService
						.selectTargetByshop(searchStr, conditions,tBeginDate,tEndDate,WebPageUtil.isHQRole(),2);

				HashMap<String, ArrayList<HashMap<String, Object>>> targetMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < targetList.size(); m++) {
					if (targetMap.get(targetList.get(m).get("shop_id")
							.toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(targetList.get(m));
						targetMap.put(targetList.get(m).get("shop_id")
								.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = targetMap
								.get(targetList.get(m).get("shop_id")
										.toString());
						modelList.add(targetList.get(m));
					}

				}

		
				List<HashMap<String, Object>> saleListByAc = excelService
						.selectSaleDataByshopByAc(beginDate, endDate, searchStr,
								conditions,WebPageUtil.isHQRole());
				HashMap<String, ArrayList<HashMap<String, Object>>> saleMapByAc = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < saleListByAc.size(); m++) {
					if (saleMapByAc.get(saleListByAc.get(m).get("shop_id").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(saleListByAc.get(m));
						saleMapByAc.put(saleListByAc.get(m).get("shop_id").toString(),
								modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = saleMapByAc
								.get(saleListByAc.get(m).get("shop_id").toString());
						modelList.add(saleListByAc.get(m));
					}

				}

				
				
				
				
				
				
			

				List<HashMap<String, Object>> modelTotalByspec = excelService
						.selectModelBySpecTotalByAc(beginDate, endDate, searchStr,
								conditions,WebPageUtil.isHQRole());

				HashMap<String, ArrayList<HashMap<String, Object>>> modelSpecByTotal = new HashMap<String, ArrayList<HashMap<String, Object>>>();

				for (int m = 0; m < modelTotalByspec.size(); m++) {
					if (modelSpecByTotal.get(modelTotalByspec.get(m)
							.get("shop_id").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(modelTotalByspec.get(m));
						modelSpecByTotal.put(
								modelTotalByspec.get(m).get("shop_id")
										.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = modelSpecByTotal
								.get(modelTotalByspec.get(m).get("shop_id")
										.toString());
						modelList.add(modelTotalByspec.get(m));
					}

				}

				
				
				
				// 用于放置表格数据
				LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
				// 查询门店所有数据
				excels = new ArrayList<Excel>(excelService.selectDatas(
						searchStr, conditions));
				int rowOne = 7;
				for (Excel excel : excels) {

					// 用于放置表格数据
					HashMap<String, Object> dataMap = new HashMap<String, Object>();

					String shop_id = excel.getShopId();
					dataMap.put("SELL-OUT INFORMATION SHEET_REG.",
							excel.getReg());
					dataMap.put("SELL-OUT INFORMATION SHEET_TYPE", "TV");
					dataMap.put("SELL-OUT INFORMATION SHEET_NO OF SHOP", 1);
					dataMap.put("SELL-OUT INFORMATION SHEET_DEALER",
							excel.getDealer());
					dataMap.put("SELL-OUT INFORMATION SHEET_STORE",
							excel.getShopName());
					dataMap.put("AC FPS", 1);

					// AS SALESMAN 0
					// AS PROM_NAME 1
					// AS ACFO 2

					String sale = "";
					String pro = "";
					String acfo = "";
					if (salerDatas.get(shop_id) != null) {
						ArrayList<HashMap<String, Object>> modelList = salerDatas
								.get(shop_id);
						for (int i = 0; i < modelList.size(); i++) {
							if (modelList.get(i).get("salerType").equals(0)
									|| modelList.get(i).get("salerType")
											.equals("0")) {
								sale += modelList.get(i).get("userId") + ",";

							} else if (modelList.get(i).get("salerType")
									.equals(1)
									|| modelList.get(i).get("salerType")
											.equals("1")) {
								pro += modelList.get(i).get("userId") + ",";

							} else if (modelList.get(i).get("salerType")
									.equals(2)
									|| modelList.get(i).get("salerType")
											.equals("2")) {
								acfo += modelList.get(i).get("userId") + ",";
							}

						}

					}

					if (sale.length() > 0) {
						dataMap.put("SALESMAN",
								sale.substring(0, sale.length() - 1));
					}
					if (pro.length() > 0) {
						dataMap.put("PROMODISER NAME",
								pro.substring(0, pro.length() - 1));
					}
					if (acfo.length() > 0) {
						dataMap.put("ACFO",
								acfo.substring(0, acfo.length() - 1));
					}

					dataMap.put("DATE OF HIRED", excel.getDateOfHired());
					dataMap.put("SHOP ID", excel.getShopId());
					dataMap.put("AREA", excel.getArea());
					dataMap.put("AGENCY", "AGENCY");
					dataMap.put("SHOP CLASS", excel.getLevel());

				
					Double saleSum = 0.0;
					
					if (saleMapByAc.get(shop_id) != null) {
						ArrayList<HashMap<String, Object>> modelList = saleMapByAc
								.get(shop_id);
						for (int i = 0; i < modelList.size(); i++) {

							BigDecimal bd = new BigDecimal(modelList.get(i)
									.get("saleQty").toString());
							String am = bd.toPlainString();
							dataMap.put("AIRCON SELL-OUT and TARGET_TTL AIRCON SO_ _QTY",
									am);
							bd = new BigDecimal(modelList.get(i).get("saleSum")
									.toString());
							am = bd.toPlainString();
							dataMap.put(
									"AIRCON SELL-OUT and TARGET_TTL AIRCON SO_ _AMOUNT",
									am);
						}

					}
					

					if (targetMap.get(shop_id) != null) {
						ArrayList<HashMap<String, Object>> modelList = targetMap
								.get(shop_id);
						for (int i = 0; i < modelList.size(); i++) {
							BigDecimal bd = new BigDecimal(modelList.get(i)
									.get("targetSum").toString());
							String am = bd.toPlainString();
							dataMap.put("AIRCON SELL-OUT and TARGET_TARGET",
									am);
							
						}

					}

				
					rowOne++;
					
					
				

					dataMap.put("AIRCON SELL-OUT and TARGET_Ach.", "TEXT(P"
							+ rowOne + "/Q" + rowOne + ",\"0.00%\")");


			

					if (shopMapByWindow.get(shop_id) != null) {
						ArrayList<HashMap<String, Object>> modelListByDIGITAL = shopMapByWindow
								.get(shop_id);
						for (int i = 0; i < modelListByDIGITAL.size(); i++) {
							if (WindowMap.get(modelListByDIGITAL.get(i).get(
									"model")) != null) {
								ArrayList<HashMap<String, Object>> priceList = WindowMap
										.get(modelListByDIGITAL.get(i).get(
												"model"));
								for (int j = 0; j < priceList.size(); j++) {
									BigDecimal bd = new BigDecimal(priceList
											.get(j).get("price").toString());
									String am = bd.toPlainString();
									double shop = Double.parseDouble(am);
									double price = shop
											/ Integer.parseInt(priceList.get(j)
													.get("shop").toString());

									long lnum = Math.round(price);

									String key = "WINDOW TYPE AIRCON"
											+ "_"
											+ modelListByDIGITAL.get(i).get(
													"model") + "_" + lnum + "_"
											+ "sold";

									bd = new BigDecimal(modelListByDIGITAL
											.get(i).get("quantity").toString());

									dataMap.put(key, bd.longValue());
								}

							}

						}

					}

					if (shopMapByFission.get(shop_id) != null) {
						ArrayList<HashMap<String, Object>> modelListByINTERNET = shopMapByFission
								.get(shop_id);
						for (int i = 0; i < modelListByINTERNET.size(); i++) {
							if (FissionMap.get(modelListByINTERNET.get(i).get(
									"model")) != null) {
								ArrayList<HashMap<String, Object>> priceList = FissionMap
										.get(modelListByINTERNET.get(i).get(
												"model"));
								for (int j = 0; j < priceList.size(); j++) {
									BigDecimal bd = new BigDecimal(priceList
											.get(j).get("price").toString());
									String am = bd.toPlainString();
									double shop = Double.parseDouble(am);
									double price = shop
											/ Integer.parseInt(priceList.get(j)
													.get("shop").toString());

									long lnum = Math.round(price);

									String key = "SPLIT TYPE AIRCON"
											+ "_"
											+ modelListByINTERNET.get(i).get(
													"model") + "_" + lnum + "_"
											+ "sold";

									bd = new BigDecimal(modelListByINTERNET
											.get(i).get("quantity").toString());

									dataMap.put(key, bd.longValue());
								}

							}

						}
					}
					
					
					
					
					
					if (stockMapByFission.get(shop_id) != null) {

						ArrayList<HashMap<String, Object>> stockListByDIGITAL = stockMapByFission
								.get(shop_id);
						for (int i = 0; i < stockListByDIGITAL.size(); i++) {

							String key = "SPLIT TYPE AIRCON" + "_"
									+ stockListByDIGITAL.get(i).get("model")
									+ "_" + " " + "_" + "Stocks";
							BigDecimal bd = new BigDecimal(stockListByDIGITAL
									.get(i).get("quantity").toString());

							dataMap.put(key, bd.longValue());
						}

					}

					if (stockMapByWindow.get(shop_id) != null) {

						ArrayList<HashMap<String, Object>> modelListByINTERNET = stockMapByWindow
								.get(shop_id);
						for (int i = 0; i < modelListByINTERNET.size(); i++) {

							String key = "WINDOW TYPE AIRCON" + "_"
									+ modelListByINTERNET.get(i).get("model")
									+ "_" + " " + "_" + "Stocks";

							BigDecimal bd = new BigDecimal(modelListByINTERNET
									.get(i).get("quantity").toString());

							dataMap.put(key, bd.longValue());
						}

					}
					
					
					
					

					if (disMapByWindow.get(shop_id) != null) {

						ArrayList<HashMap<String, Object>> disListByDIGITAL = disMapByWindow
								.get(shop_id);
						for (int i = 0; i < disListByDIGITAL.size(); i++) {

							String key = "WINDOW TYPE AIRCON" + "_"
									+ disListByDIGITAL.get(i).get("model")
									+ "_" + "" + "_" + "DisPlay";
							BigDecimal bd = new BigDecimal(disListByDIGITAL
									.get(i).get("quantity").toString());

							dataMap.put(key, bd.longValue());
						}

					}

					if (disMapByFission.get(shop_id) != null) {

						ArrayList<HashMap<String, Object>> disListByINTERNET = disMapByFission
								.get(shop_id);
						for (int i = 0; i < disListByINTERNET.size(); i++) {

							String key = "SPLIT TYPE AIRCON" + "_"
									+ disListByINTERNET.get(i).get("model")
									+ "_" + " " + "_" + "DisPlay";

							BigDecimal bd = new BigDecimal(disListByINTERNET
									.get(i).get("quantity").toString());

							dataMap.put(key, bd.longValue());
						}

					}
					
					
					
					

					
					// 查询类型总台数
					if (modelSpecByTotal.get(shop_id) != null) {
						ArrayList<HashMap<String, Object>> disListByCURVE = modelSpecByTotal
								.get(shop_id);
						for (int i = 0; i < disListByCURVE.size(); i++) {

							String qtyKey = "AIRCON  SUB-TOTAL_" + "QTY";
							String sumKey = "AIRCON  SUB-TOTAL_" + "AMOUNT";

							BigDecimal bd = new BigDecimal(disListByCURVE
									.get(i).get("quantity").toString());

							dataMap.put(qtyKey, bd.longValue());
							bd = new BigDecimal(disListByCURVE.get(i)
									.get("amount").toString());
							String am = bd.toPlainString();
							long lnum = Math.round(bd.doubleValue());
							if (am.contains(".")) {
								dataMap.put(sumKey, lnum);
							} else {
								dataMap.put(sumKey, am);
							}

						}

					}

					
					if (stockMapByTotal.get(shop_id) != null) {
						ArrayList<HashMap<String, Object>> stockTTL = stockMapByTotal
								.get(shop_id);
						for (int i = 0; i < stockTTL.size(); i++) {
							String key = "AIRCON TTL INVENTORY_Stocks";

							BigDecimal bd = new BigDecimal(stockTTL.get(i)
									.get("quantity").toString());

							dataMap.put(key, bd.longValue());

						}
					}

					if (disPlayMapByTotal.get(shop_id) != null) {
						ArrayList<HashMap<String, Object>> disTTL = disPlayMapByTotal
								.get(shop_id);
						for (int i = 0; i < disTTL.size(); i++) {
							String key = "AIRCON TTL INVENTORY_Display";
							BigDecimal bd = new BigDecimal(disTTL.get(i)
									.get("quantity").toString());
							dataMap.put(key, bd.longValue());
						}
					}

					dataList.add(dataMap);
				}

				String[] header = new String[columns.size()];
				String[] fields = new String[columns.size()];
				for (int i = 0, l = columns.size(); i < l; i++) {

					HashMap columnMap = (HashMap) columns.get(i);
					header[i] = columnMap.get("header").toString();
					fields[i] = columnMap.get("field").toString();

				}

				// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
				Sheet sheet = wb.createSheet(sheetName);
				sheet.setZoom(3, 4);

				// 创建字体
				Font fontinfo = wb.createFont();
				fontinfo.setFontHeightInPoints((short) 11); // 字体大小
				fontinfo.setFontName("Trebuchet MS");
				Font fonthead = wb.createFont();
				fonthead.setFontHeightInPoints((short) 12);
				fonthead.setFontName("Trebuchet MS");

				// colSplit, rowSplit,leftmostColumn, topRow,
				sheet.createFreezePane(7, 6, 9, 7);
				CellStyle cellStylename = wb.createCellStyle();// 表名样式
				cellStylename.setFont(fonthead);

				CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
				cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
				cellStyleinfo.setFont(fontinfo);

				CellStyle cellStyleDate = wb.createCellStyle();

				DataFormat dataFormat = wb.createDataFormat();

				cellStyleDate.setDataFormat(dataFormat
						.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
															// hh:mm:ss

				CellStyle cellStylehead = wb.createCellStyle();// 表头样式
				cellStylehead.setFont(fonthead);
				cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
				cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
				cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
				cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
				cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
				cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
				cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
				cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				cellStylehead.setWrapText(true);

				CellStyle cellStyleWHITE = wb.createCellStyle();// 表头样式
				cellStyleWHITE.setFont(fonthead);
				cellStyleWHITE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cellStyleWHITE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
				cellStyleWHITE.setBottomBorderColor(HSSFColor.BLACK.index);
				cellStyleWHITE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cellStyleWHITE.setLeftBorderColor(HSSFColor.BLACK.index);
				cellStyleWHITE.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cellStyleWHITE.setRightBorderColor(HSSFColor.BLACK.index);
				cellStyleWHITE.setBorderTop(HSSFCellStyle.BORDER_THIN);
				cellStyleWHITE.setTopBorderColor(HSSFColor.BLACK.index);
				cellStyleWHITE.setFillForegroundColor(HSSFColor.WHITE.index);
				cellStyleWHITE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				cellStyleWHITE.setWrapText(true);

				CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
				cellStyleGreen.setFont(fonthead);
				cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
				cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
				cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
				cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
				cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
				cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
				cellStyleGreen
						.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
				cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				cellStyleGreen.setWrapText(true);

				CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
				cellStyleYellow.setFont(fonthead);
				cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
				cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
				cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
				cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
				cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
				cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
				cellStyleYellow
						.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
				cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				cellStyleYellow.setWrapText(true);

				CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
				cellStyleRED.setFont(fonthead);
				cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
				cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
				cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
				cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
				cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
				cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
				cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
				cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				cellStyleRED.setWrapText(true);

				CellStyle cellStylePERCENT = wb.createCellStyle();// 表头样式
				cellStylePERCENT.setFont(fonthead);
				cellStylePERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cellStylePERCENT.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
				cellStylePERCENT.setBottomBorderColor(HSSFColor.BLACK.index);
				cellStylePERCENT.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cellStylePERCENT.setLeftBorderColor(HSSFColor.BLACK.index);
				cellStylePERCENT.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cellStylePERCENT.setRightBorderColor(HSSFColor.BLACK.index);
				cellStylePERCENT.setBorderTop(HSSFCellStyle.BORDER_THIN);
				cellStylePERCENT.setTopBorderColor(HSSFColor.BLACK.index);
				cellStylePERCENT
						.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
				cellStylePERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				cellStylePERCENT.setWrapText(true);

				CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
				cellStyle.setWrapText(true);// 自动换行
				cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
				cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
				cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
				cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
				cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
				cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));

				cellStyle.setWrapText(true);// 设置自动换行

				CellStyle contextstyle = wb.createCellStyle();
				contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
				contextstyle.setFont(fontinfo);
				// 开始创建表头
				// int col = header.length;
				// 创建第一行
				Row row = sheet.createRow((short) 0);

				// 创建这一行的一列，即创建单元格(CELL)
				Cell cell = row.createCell((short) 0);
				// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
				cell.setCellStyle(cellStylename);
				cell.setCellValue("TCL " + partyName);// 标题
				// int firstRow, int lastRow, int firstCol, int lastCol
				// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
				cell = row.createCell((short) 2);
				// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellStyle(cellStylename);
				cell.setCellValue(""); // 信息
				// int firstRow, int lastRow, int firstCol, int lastCol
				// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 7));

				// 第二行
				Row rowSec = sheet.createRow((short) 1);
				cell = rowSec.createCell((short) 0);
				cell.setCellStyle(cellStylename);
				cell.setCellValue("MARKETING DEPARTMENT");
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
				rowSec.setHeightInPoints(40);

				cell = rowSec.createCell((short) 6);
				cell.setCellStyle(cellStylename);
				cell.setCellValue("");
				// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 9));

				// 第二行
				Row rowSix = sheet.createRow((short) 6);
				DataFormat df = wb.createDataFormat();
				int size = excels.size() + 8;

				cell = rowSix.createCell((short) 2);
				cell.setCellValue("SUBTOTAL(9,C8:C" + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9,C8:C" + size + ")");

				// 第二行
				cell = rowSix.createCell((short) 5);
				cell.setCellValue("SUBTOTAL(9,F8:F" + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9,F8:F" + size + ")");

			
				// 第二行
				cell = rowSix.createCell((short) 14);
				cell.setCellValue("SUBTOTAL(9,O8:O" + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9,O8:O" + size + ")");

				// 第二行
				cell = rowSix.createCell((short) 15);
				cell.setCellValue("SUBTOTAL(9,P8:P" + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9,P8:P" + size + ")");

				// 第二行
				cell = rowSix.createCell((short) 16);
				cell.setCellValue("SUBTOTAL(9,Q8:Q" + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9,Q8:Q" + size + ")");


				// 第二行
				cell = rowSix.createCell((short) 17);
				cell.setCellValue("TEXT(P7/Q7,\"0.00%\")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("TEXT(P7/Q7,\"0.00%\")");

				// 第二行
				cell = rowSix.createCell((short) 18);
				cell.setCellValue("SUBTOTAL(9,S8:S" + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9,S8:S" + size + ")");

				// 第二行
				cell = rowSix.createCell((short) 19);
				cell.setCellValue("SUBTOTAL(9,T8:T" + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9,T8:T" + size + ")");

				// 第二行
				cell = rowSix.createCell((short) 20);
				cell.setCellValue("SUBTOTAL(9,U8:U" + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9,U8:U" + size + ")");
				
				// 第二行
				cell = rowSix.createCell((short) 21);
				cell.setCellValue("SUBTOTAL(9,V8:V" + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9,V8:V" + size + ")");
				
				// 第二行
				cell = rowSix.createCell((short) 22);
				cell.setCellValue("SUBTOTAL(9,W8:W" + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9,W8:W" + size + ")");
				
				// 第二行
				cell = rowSix.createCell((short) 23);
				cell.setCellValue("SUBTOTAL(9,X8:X" + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9,X8:X" + size + ")");
				
				// 第二行
				cell = rowSix.createCell((short) 24);
				cell.setCellValue("SUBTOTAL(9,Y8:Y" + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9,Y8:Y" + size + ")");
				
				
				// 第二行
				cell = rowSix.createCell((short) 25);
				cell.setCellValue("SUBTOTAL(9,Z8:Z" + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9,Z8:Z" + size + ")");

				// 头部标题长度-20就是后面需要计算的列
				String[] line = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
						"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
						"U", "V", "W", "X", "Y", "Z" };

				String[] big = {};
				// 写一个循环，让他们自动组合，放入big 1)AA,AB.......AZ 2)BA,BB........BZ 以此类推到Z
				for (int i = 0; i < line.length; i++) {
					for (int j = 0; j < line.length; j++) {
						String a = line[i];
						String b = line[j];
						big = insert(big, a + b);
					}
				}
				int start = 26;
				for (int i = 0; i < headers.length - 26; i++) {

					cell = rowSix.createCell((short) start);
					cell.setCellValue("SUBTOTAL(9," + big[i] + "8:" + big[i]
							+ size + ")");
					cell.setCellType(Cell.CELL_TYPE_FORMULA);
					cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
					cell.setCellStyle(cellStylePERCENT);
					cell.setCellFormula("SUBTOTAL(9," + big[i] + "8:" + big[i]
							+ size + ")");

					start++;
				}

				int rows_max = 0; // 最大的一个项有几个子项

				for (int i = 0; i < header.length; i++) {
					String h = header[i];

					if (h.split("_").length > rows_max) {
						rows_max = h.split("_").length;
					}
				}
				Map map = new HashMap();
				for (int k = 0; k < rows_max; k++) {
					row = sheet.createRow((short) k + 2);

					for (int i = 0; i < header.length; i++) {

						String headerTemp = header[i];
						String[] s = headerTemp.split("_");
						String sk = "";
						int num = i;
						sheet.setColumnWidth(num, s.toString().length() * 156);
						if (s.length == 1) { // 如果是简单表头项
							cell = row.createCell((short) (num));

							if (i < 14) {
								cell.setCellStyle(cellStyleWHITE);
							} else if (i > 13 && i < 18) {
								cell.setCellStyle(cellStylehead);
							} else if (i > 17) {
								cell.setCellStyle(cellStyleYellow);
								if (s[0].contains("SUB-TOTAL")
										|| s[0].contains("TTL")) {
									cell.setCellStyle(cellStyleRED);
								}
							}
							// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
							sheet.addMergedRegion(new CellRangeAddress(2,
									rows_max + 1, (num), (num)));
							sk = headerTemp;
							cell.setCellValue(sk);

						} else {
							cell = row.createCell((short) (num));
							if (i < 14) {
								cell.setCellStyle(cellStyleWHITE);
							} else if (i > 13 && i < 18) {
								cell.setCellStyle(cellStylehead);
							} else if (i > 17) {
								cell.setCellStyle(cellStyleYellow);
								if (s[0].contains("SUB-TOTAL")
										|| s[0].contains("TTL")) {
									cell.setCellStyle(cellStyleRED);
								}
							}
							int cols = 0;
							if (map.containsKey(headerTemp)) {
								continue;
							}
							for (int d = 0; d <= k; d++) {
								if (d != k) {
									sk += s[d] + "_";
								} else {
									sk += s[d];
								}
							}
							if (map.containsKey(sk)) {
								continue;
							}
							for (int j = 0; j < header.length; j++) {
								if (header[j].indexOf(sk) != -1) {
									cols++;
								}
							}
							cell.setCellValue(s[k]);

							// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
								// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
								sheet.addMergedRegion(new CellRangeAddress(
										k + 2, k + 2, (num), (num + cols - 1)));
								if (sk.equals(headerTemp)) {
									sheet.addMergedRegion(new CellRangeAddress(
											k + 2, k + 2 + rows_max - s.length,
											num, num));
								}

						}
						if (s.length > k) {
							if (!map.containsKey(sk)) {
								String key = "";
								if (k > 0) {
									key = sk;
								} else {
									key = s[k];
								}
								map.put(key, null);
							}
						}
					}
				}

				for (int i = 0; i < header.length; i++) {
					String h = header[i];

					if (h.split("_").length > rows_max) {
						rows_max = h.split("_").length;
					}
				}

				for (int d = 0; d < dataList.size(); d++) {

					HashMap<String, Object> dataMap = dataList.get(d);
					// 创建一行
					Row datarow = sheet.createRow(d + 2 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

					Cell scell = datarow.createCell((short) 0);
					// 创建列
					for (int c = 0; c < fields.length; c++) {

						cell = datarow.createCell((short) (c));

						Cell contentCell = datarow.createCell(c);
						Boolean isNum = false;// data是否为数值型
						Boolean isInteger = false;// data是否为整数
						Boolean isPercent = false;// data是否为百分数
						Boolean isGongshi = false;// data是否为百分数
						Boolean isGongshiOne = false;
						if (dataMap.get(fields[c]) != null
								&& dataMap.get(fields[c]).toString().length() > 0

						) {

							// 判断data是否为数值型
							isNum = dataMap.get(fields[c]).toString()
									.matches("^(-?\\d+)(\\.\\d+)?$");
							// 判断data是否为整数（小数部分是否为0）
							isInteger = dataMap.get(fields[c]).toString()
									.matches("^[-\\+]?[\\d]*$");
							// 判断data是否为百分数（是否包含“%”）
							isPercent = dataMap.get(fields[c]).toString()
									.contains("%");
							isGongshi = dataMap.get(fields[c]).toString()
									.contains("SUM");
							isGongshiOne = dataMap.get(fields[c]).toString()
									.contains("TEXT");
							// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
							if (isNum && !isPercent) {
								if (isInteger) {
									contextstyle.setDataFormat(df
											.getFormat("#,##0"));// 数据格式只显示整数
								} else {
									contextstyle.setDataFormat(df
											.getFormat("#,##0"));// 保留两位小数点
								}
								// 设置单元格格式
								contentCell.setCellStyle(contextstyle);
								// 设置单元格内容为double类型
								contentCell.setCellValue(Double
										.parseDouble(dataMap.get(fields[c])
												.toString()));
							} else if (isGongshi) {

								contentCell
										.setCellType(Cell.CELL_TYPE_FORMULA);
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(contextstyle);
								contentCell.setCellFormula(dataMap.get(
										fields[c]).toString());
								int s = dataMap.get(fields[c]).toString()
										.length() * 512;
								sheet.setColumnWidth(c, s);
							} else if (isGongshiOne) {
								contentCell
										.setCellType(Cell.CELL_TYPE_FORMULA);
								contentCell.setCellStyle(contextstyle);
								contentCell.setCellFormula(dataMap.get(
										fields[c]).toString());

							} else {
								contentCell.setCellStyle(contextstyle);
								// 设置单元格内容为字符型
								contentCell.setCellValue(dataMap.get(fields[c])
										.toString());

							}
						} else {
							contentCell.setCellValue("");
						}

						/*
						 * if (dataMap.get(fields[c]) != null &&
						 * dataMap.get(fields[c]).toString().length() > 0) {
						 * cell.setCellValue(dataMap.get(fields[c]).toString());
						 * // 信息 } else { cell.setCellValue(""); // 信息
						 * 
						 * }
						 */
						// sheet.addMergedRegion(new CellRangeAddress(0, (short)
						// 2, 0,
						// (short)
						// c));
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 日报 耗时：8.863秒
	 */
	public void exportDaily() {
		long time = System.currentTimeMillis();
		String searchStr = null;
		String conditions = "";
		String center = "";
		String country = "";
		String region = "";
		String office = "";

		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if (!WebPageUtil.isHAdmin()) {
			if (request.getParameter("center") != null
					&& !request.getParameter("center").equals("")
					|| request.getParameter("country") != null
					&& !request.getParameter("country").equals("")
					|| request.getParameter("region") != null
					&& !request.getParameter("region").equals("")
					|| request.getParameter("office") != null
					&& !request.getParameter("office").equals("")) {

				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")) {
					center = request.getParameter("center");
					conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
							+ center + "')   ";
				}

				if (request.getParameter("country") != null
						&& !request.getParameter("country").equals("")) {
					country = request.getParameter("country");
					conditions = "  pa.country_id= " + country + "  ";
				}
				if (request.getParameter("region") != null
						&& !request.getParameter("region").equals("")) {
					region = request.getParameter("region");
					conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
							+ region
							+ "'  OR PARTY_ID='"+region+"'))  ";
				}
				if (request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {
					office = request.getParameter("office");
					conditions = "    pa.party_id IN ('" + office + "')  ";
				}

			} else {
				if (null != userPartyIds && !"".equals(userPartyIds)) {
					conditions = "  pa.party_id in (" + userPartyIds + ")  ";
				} else {
					conditions = "  1=2  ";
				}
			}

		} else {
			conditions = " 1=1 ";

		}
		if (request.getParameter("level") != null
				&& !request.getParameter("level").equals("")) {
			conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
		}

		try {
			SXSSFWorkbook wb = new SXSSFWorkbook();
			if (WebPageUtil.getLoginedUserId() != null) {
				user = WebPageUtil.getLoginedUserId();
			}
			String partyName = exportExcelService.selectPartyByUser(user);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ "Daily Reports" + ".xlsx");

			/*
			 * String beginDate = request.getParameter("beginDate"); String
			 * endDate = request.getParameter("endDate");
			 */
			String date = request.getParameter("date");
			/*
			 * String[] beginDay = beginDate.split("-"); String[] endDay =
			 * endDate.split("-"); int begin = Integer.parseInt(beginDay[2]);
			 * int end = Integer.parseInt(endDay[2]); int day = end - begin + 1;
			 * 
			 * Date dBegin = format.parse(beginDate); Date dEnd =
			 * format.parse(endDate); List<Date> listDate =
			 * DateUtil.getDatesBetweenTwoDate(dBegin, dEnd);
			 */

			String sheetName = "";
			List<Excel> excels = null;
			String [] dayDate=request.getParameter("date").split("-");
			String beginDate=dayDate[0]+"-"+dayDate[1]+"-"+dayDate[2];
			String endDate=dayDate[0]+"-"+dayDate[1]+"-"+dayDate[2];
			// for (int z = 0; z < listDate.size(); z++) {
			Date dates = format.parse(request.getParameter("date"));
			sheetName = formatEn.format(/* listDate.get(z) */dates);
			String[] headers = { "SELL-OUT INFORMATION SHEET_REG.",
					"SELL-OUT INFORMATION SHEET_TYPE",
					"SELL-OUT INFORMATION SHEET_NO OF SHOP",
					"SELL-OUT INFORMATION SHEET_DEALER",
					"SELL-OUT INFORMATION SHEET_STORE", "AC FPS",
					"PROMODISER NAME", "DATE OF HIRED", "SHOP ID", "ACFO",
					"AREA", "SALESMAN", "AGENCY", "SHOP CLASS",
					"AIRCON SELL-OUT and TARGET_TTL AIRCON SO_ _QTY",
					"AIRCON SELL-OUT and TARGET_TTL AIRCON SO_ _AMOUNT",
					"AIRCON SELL-OUT and TARGET_TARGET",
					"AIRCON SELL-OUT and TARGET_Ach." };

			
			
			
			
			// 查询机型销售数据
			
			List<HashMap<String, Object>> modelMapListByWindow = exportExcelService
					.selectModelBySpecByAc(date,"%Window%", searchStr, conditions,WebPageUtil.isHQRole());

			for (int i = 0; i < modelMapListByWindow.size(); i++) {
				BigDecimal bd = new BigDecimal(modelMapListByWindow.get(i)
						.get("price").toString());
				String am = bd.toPlainString();
				double shop = Double.parseDouble(am);
				double price = shop
						/ Integer.parseInt(modelMapListByWindow.get(i)
								.get("shop").toString());
				long lnum = Math.round(price);
				String m = "WINDOW TYPE AIRCON" + "_"
						+ modelMapListByWindow.get(i).get("model") + "_"
						+ lnum + "_" + "sold";
				headers = insert(headers, m);

			}
			HashMap<String, ArrayList<HashMap<String, Object>>> WindowMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelMapListByWindow.size(); m++) {
				if (WindowMap.get(modelMapListByWindow.get(m)
						.get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelMapListByWindow.get(m));
					WindowMap.put(modelMapListByWindow.get(m)
							.get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = WindowMap
							.get(modelMapListByWindow.get(m).get("model")
									.toString());
					modelList.add(modelMapListByWindow.get(m));
				}

			}

			List<HashMap<String, Object>> modelMapListByFission = exportExcelService
					.selectModelBySpecByAc(date,"%Fission%", searchStr, conditions,WebPageUtil.isHQRole());

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < modelMapListByFission.size(); i++) {
				BigDecimal bd = new BigDecimal(modelMapListByFission
						.get(i).get("price").toString());
				String am = bd.toPlainString();
				double shop = Double.parseDouble(am);
				double price = shop
						/ Integer.parseInt(modelMapListByFission.get(i)
								.get("shop").toString());

				long lnum = Math.round(price);
				String m = "SPLIT TYPE AIRCON" + "_"
						+ modelMapListByFission.get(i).get("model") + "_"
						+ lnum + "_" + "sold";
				headers = insert(headers, m);
			

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> FissionMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelMapListByFission.size(); m++) {
				if (FissionMap.get(modelMapListByFission.get(m)
						.get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelMapListByFission.get(m));
					FissionMap.put(
							modelMapListByFission.get(m).get("model")
									.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = FissionMap
							.get(modelMapListByFission.get(m).get("model")
									.toString());
					modelList.add(modelMapListByFission.get(m));
				}

			}

			
			
			
			headers = insert(headers, "AIRCON  SUB-TOTAL_QTY");
			headers = insert(headers, "AIRCON  SUB-TOTAL_AMOUNT");
			
			List<HashMap<String, Object>> stockMapListByWindow = excelService
					.selectStockBymodelByAc("%Window%", searchStr, conditions,WebPageUtil.isHQRole(),beginDate,endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < stockMapListByWindow.size(); i++) {
				String m = "WINDOW TYPE AIRCON" + "_"
						+ stockMapListByWindow.get(i).get("model") + "_"
						+ " " + "_" + "Stocks";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> stockWindowMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockMapListByWindow.size(); m++) {
				if (stockWindowMap.get(stockMapListByWindow.get(m)
						.get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockMapListByWindow.get(m));
					stockWindowMap.put(
							stockMapListByWindow.get(m).get("model")
									.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockWindowMap
							.get(stockMapListByWindow.get(m).get("model")
									.toString());
					modelList.add(stockMapListByWindow.get(m));
				}

			}

			List<HashMap<String, Object>> stockMapListByFission = excelService
					.selectStockBymodelByAc("%Fission%", searchStr, conditions,WebPageUtil.isHQRole(),beginDate,endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < stockMapListByFission.size(); i++) {
				String m = "INTERNET STOCKS" + "_"
						+ stockMapListByFission.get(i).get("model") + "_"
						+ " " + "_" + "Stocks";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> stockFissionMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockMapListByFission.size(); m++) {
				if (stockFissionMap.get(stockMapListByFission.get(m)
						.get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockMapListByFission.get(m));
					stockFissionMap.put(
							stockMapListByFission.get(m).get("model")
									.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockFissionMap
							.get(stockMapListByFission.get(m).get("model")
									.toString());
					modelList.add(stockMapListByFission.get(m));
				}

			}
			
			
			
			
			
			
			headers = insert(headers, "AIRCON TTL INVENTORY_Stocks");

			List<HashMap<String, Object>> disMapListByWindow = excelService
					.selectDisplayBymodelByAc("%Window%", searchStr,
							conditions,WebPageUtil.isHQRole(),beginDate,endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < disMapListByWindow.size(); i++) {
				String m = "WINDOW TYPE AIRCON" + "_"
						+ disMapListByWindow.get(i).get("model") + "_"
						+ " " + "_" + "DisPlay";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> disWindowMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disMapListByWindow.size(); m++) {
				if (disWindowMap.get(disMapListByWindow.get(m)
						.get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disMapListByWindow.get(m));
					disWindowMap.put(
							disMapListByWindow.get(m).get("model")
									.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disWindowMap
							.get(disMapListByWindow.get(m).get("model")
									.toString());
					modelList.add(disMapListByWindow.get(m));
				}

			}

			
			
			List<HashMap<String, Object>> disMapListByFission = excelService
					.selectDisplayBymodelByAc("%Fission%", searchStr,
							conditions,WebPageUtil.isHQRole(),beginDate,endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < disMapListByFission.size(); i++) {
				String m = "SPLIT TYPE AIRCON" + "_"
						+ disMapListByFission.get(i).get("model") + "_"
						+ " " + "_" + "DisPlay";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> disFissionMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disMapListByFission.size(); m++) {
				if (disFissionMap.get(disMapListByFission.get(m)
						.get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disMapListByFission.get(m));
					disFissionMap.put(
							disMapListByFission.get(m).get("model")
									.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disFissionMap
							.get(disMapListByFission.get(m).get("model")
									.toString());
					modelList.add(disMapListByFission.get(m));
				}

			}

			
			

			headers = insert(headers, "AIRCON TTL DISPLAY_Display");

			// 按照对应格式将表头传入
			ArrayList columns = new ArrayList();
			for (int i = 0; i < headers.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headers[i]);
				columnMap.put("field", headers[i]);
				columns.add(columnMap);
			}

			
			// 查询门店机型销售数据
			List<HashMap<String, Object>> modeldataListByWindow = exportExcelService
					.selectModelListBySpecByAc("%Window%", date,
							searchStr, conditions,WebPageUtil.isHQRole());

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> shopMapByWindow = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modeldataListByWindow.size(); m++) {
				if (shopMapByWindow.get(modeldataListByWindow.get(m)
						.get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modeldataListByWindow.get(m));
					shopMapByWindow.put(
							modeldataListByWindow.get(m).get("shop_id")
									.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = shopMapByWindow
							.get(modeldataListByWindow.get(m)
									.get("shop_id").toString());
					modelList.add(modeldataListByWindow.get(m));
				}

			}

			// 查询门店机型销售数据
			List<HashMap<String, Object>> modeldataListByFission = exportExcelService
					.selectModelListBySpecByAc("%Fission%", date, searchStr, conditions,WebPageUtil.isHQRole());

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> shopMapByFission = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modeldataListByFission.size(); m++) {
				if (shopMapByFission.get(modeldataListByFission.get(m)
						.get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modeldataListByFission.get(m));
					shopMapByFission.put(modeldataListByFission.get(m)
							.get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = shopMapByFission
							.get(modeldataListByFission.get(m)
									.get("shop_id").toString());
					modelList.add(modeldataListByFission.get(m));
				}

			}
			
			
			List<HashMap<String, Object>> stockDataListByWindow = excelService
					.selectStockByDataByAc("%Window%", searchStr, conditions,WebPageUtil.isHQRole(),beginDate,endDate);

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> stockMapByWindow = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockDataListByWindow.size(); m++) {
				if (stockMapByWindow.get(stockDataListByWindow.get(m)
						.get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockDataListByWindow.get(m));
					stockMapByWindow.put(stockDataListByWindow.get(m)
							.get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockMapByWindow
							.get(stockDataListByWindow.get(m)
									.get("shop_id").toString());
					modelList.add(stockDataListByWindow.get(m));
				}

			}

			List<HashMap<String, Object>> stockDataListByFission = excelService
					.selectStockByDataByAc("%Fission%", searchStr, conditions,WebPageUtil.isHQRole(),beginDate,endDate);

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> stockMapByFission = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockDataListByFission.size(); m++) {
				if (stockMapByFission.get(stockDataListByFission.get(m)
						.get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockDataListByFission.get(m));
					stockMapByFission.put(stockDataListByFission.get(m)
							.get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockMapByFission
							.get(stockDataListByFission.get(m)
									.get("shop_id").toString());
					modelList.add(stockDataListByFission.get(m));
				}

			}
			
			
			
			
			
		

			List<HashMap<String, Object>> disDataListByWindow = excelService
					.selectDisplayByDataByAc("%Window%", searchStr, conditions,WebPageUtil.isHQRole(),beginDate,endDate);
			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> disMapByWindow= new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disDataListByWindow.size(); m++) {
				if (disMapByWindow.get(disDataListByWindow.get(m)
						.get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disDataListByWindow.get(m));
					disMapByWindow.put(
							disDataListByWindow.get(m).get("shop_id")
									.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disMapByWindow
							.get(disDataListByWindow.get(m).get("shop_id")
									.toString());
					modelList.add(disDataListByWindow.get(m));
				}

			}

			List<HashMap<String, Object>> disDataListByFission = excelService
					.selectDisplayByDataByAc("%Fission%", searchStr,
							conditions,WebPageUtil.isHQRole(),beginDate,endDate);
			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> disMapByFission = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disDataListByFission.size(); m++) {
				if (disMapByFission.get(disDataListByFission.get(m)
						.get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disDataListByFission.get(m));
					disMapByFission.put(
							disDataListByFission.get(m).get("shop_id")
									.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disMapByFission
							.get(disDataListByFission.get(m)
									.get("shop_id").toString());
					modelList.add(disDataListByFission.get(m));
				}

			}
			
			
			
			
		
			List<HashMap<String, Object>> StockTotalBySpec = excelService
					.selectStockByTotalByAc(searchStr, conditions,beginDate,endDate);
			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> stockMapByTotal = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < StockTotalBySpec.size(); m++) {
				if (stockMapByTotal.get(StockTotalBySpec.get(m)
						.get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(StockTotalBySpec.get(m));
					stockMapByTotal.put(
							StockTotalBySpec.get(m).get("shop_id")
									.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockMapByTotal
							.get(StockTotalBySpec.get(m).get("shop_id")
									.toString());
					modelList.add(StockTotalBySpec.get(m));
				}

			}

			List<HashMap<String, Object>> disTotalBySpec = excelService
					.selectDisPlayByTotalByAc(searchStr, conditions,beginDate,endDate);

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> disPlayMapByTotal = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disTotalBySpec.size(); m++) {
				if (disPlayMapByTotal.get(disTotalBySpec.get(m)
						.get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disTotalBySpec.get(m));
					disPlayMapByTotal
							.put(disTotalBySpec.get(m).get("shop_id")
									.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disPlayMapByTotal
							.get(disTotalBySpec.get(m).get("shop_id")
									.toString());
					modelList.add(disTotalBySpec.get(m));
				}

			}
			// 查询所有导购员，督导与业务员
			List<HashMap<String, Object>> salerList = excelService
					.selectSalerDatas(searchStr, conditions);

			HashMap<String, ArrayList<HashMap<String, Object>>> salerDatas = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < salerList.size(); m++) {
				if (salerDatas.get(salerList.get(m).get("shop_id")
						.toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(salerList.get(m));
					salerDatas.put(salerList.get(m).get("shop_id")
							.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = salerDatas
							.get(salerList.get(m).get("shop_id").toString());
					modelList.add(salerList.get(m));
				}

			}
			
			String  [] tbe=date.split("-");
			String tBeginDate=tbe[0]+"-"+tbe[1]+"-"+"01";
			String tEndDate=tbe[0]+"-"+tbe[1]+"-"+"31";
			// 根据门店取得对应销售数据与目标数据
			List<HashMap<String, Object>> targetList = excelService
					.selectTargetByshop(searchStr, conditions,tBeginDate,tEndDate,WebPageUtil.isHQRole(),2);

			HashMap<String, ArrayList<HashMap<String, Object>>> targetMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < targetList.size(); m++) {
				if (targetMap.get(targetList.get(m).get("shop_id")
						.toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(targetList.get(m));
					targetMap.put(targetList.get(m).get("shop_id")
							.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = targetMap
							.get(targetList.get(m).get("shop_id")
									.toString());
					modelList.add(targetList.get(m));
				}

			}

	
			List<HashMap<String, Object>> saleListByAc = exportExcelService
					.selectSaleDataByshopByAc(date, searchStr,
							conditions,WebPageUtil.isHQRole());
			HashMap<String, ArrayList<HashMap<String, Object>>> saleMapByAc = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < saleListByAc.size(); m++) {
				if (saleMapByAc.get(saleListByAc.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(saleListByAc.get(m));
					saleMapByAc.put(saleListByAc.get(m).get("shop_id").toString(),
							modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = saleMapByAc
							.get(saleListByAc.get(m).get("shop_id").toString());
					modelList.add(saleListByAc.get(m));
				}

			}

			
			
			
			
			
			
		

			List<HashMap<String, Object>> modelTotalByspec = exportExcelService
					.selectModelBySpecTotalByAc(date, searchStr,
							conditions,WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> modelSpecByTotal = new HashMap<String, ArrayList<HashMap<String, Object>>>();

			for (int m = 0; m < modelTotalByspec.size(); m++) {
				if (modelSpecByTotal.get(modelTotalByspec.get(m)
						.get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelTotalByspec.get(m));
					modelSpecByTotal.put(
							modelTotalByspec.get(m).get("shop_id")
									.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = modelSpecByTotal
							.get(modelTotalByspec.get(m).get("shop_id")
									.toString());
					modelList.add(modelTotalByspec.get(m));
				}

			}

			
			
			
			// 用于放置表格数据
			LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
			// 查询门店所有数据
			excels = new ArrayList<Excel>(exportExcelService.selectDatas(
					searchStr, conditions));
			int rowOne = 7;
			for (Excel excel : excels) {

				// 用于放置表格数据
				HashMap<String, Object> dataMap = new HashMap<String, Object>();

				String shop_id = excel.getShopId();
				dataMap.put("SELL-OUT INFORMATION SHEET_REG.",
						excel.getReg());
				dataMap.put("SELL-OUT INFORMATION SHEET_TYPE", "TV");
				dataMap.put("SELL-OUT INFORMATION SHEET_NO OF SHOP", 1);
				dataMap.put("SELL-OUT INFORMATION SHEET_DEALER",
						excel.getDealer());
				dataMap.put("SELL-OUT INFORMATION SHEET_STORE",
						excel.getShopName());
				dataMap.put("AC FPS", 1);

				// AS SALESMAN 0
				// AS PROM_NAME 1
				// AS ACFO 2

				String sale = "";
				String pro = "";
				String acfo = "";
				if (salerDatas.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = salerDatas
							.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {
						if (modelList.get(i).get("salerType").equals(0)
								|| modelList.get(i).get("salerType")
										.equals("0")) {
							sale += modelList.get(i).get("userId") + ",";

						} else if (modelList.get(i).get("salerType")
								.equals(1)
								|| modelList.get(i).get("salerType")
										.equals("1")) {
							pro += modelList.get(i).get("userId") + ",";

						} else if (modelList.get(i).get("salerType")
								.equals(2)
								|| modelList.get(i).get("salerType")
										.equals("2")) {
							acfo += modelList.get(i).get("userId") + ",";
						}

					}

				}

				if (sale.length() > 0) {
					dataMap.put("SALESMAN",
							sale.substring(0, sale.length() - 1));
				}
				if (pro.length() > 0) {
					dataMap.put("PROMODISER NAME",
							pro.substring(0, pro.length() - 1));
				}
				if (acfo.length() > 0) {
					dataMap.put("ACFO",
							acfo.substring(0, acfo.length() - 1));
				}

				dataMap.put("DATE OF HIRED", excel.getDateOfHired());
				dataMap.put("SHOP ID", excel.getShopId());
				dataMap.put("AREA", excel.getArea());
				dataMap.put("AGENCY", "AGENCY");
				dataMap.put("SHOP CLASS", excel.getLevel());

			
				Double saleSum = 0.0;
				
				if (saleMapByAc.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = saleMapByAc
							.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {

						BigDecimal bd = new BigDecimal(modelList.get(i)
								.get("saleQty").toString());
						String am = bd.toPlainString();
						dataMap.put("AIRCON SELL-OUT and TARGET_TTL AIRCON SO_ _QTY",
								am);
						bd = new BigDecimal(modelList.get(i).get("saleSum")
								.toString());
						am = bd.toPlainString();
						dataMap.put(
								"AIRCON SELL-OUT and TARGET_TTL AIRCON SO_ _AMOUNT",
								am);
					}

				}
				

				if (targetMap.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = targetMap
							.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {
						BigDecimal bd = new BigDecimal(modelList.get(i)
								.get("targetSum").toString());
						String am = bd.toPlainString();
						dataMap.put("AIRCON SELL-OUT and TARGET_TARGET",
								am);
						
					}

				}

			
				rowOne++;
				
				
			

				dataMap.put("AIRCON SELL-OUT and TARGET_Ach.", "TEXT(P"
						+ rowOne + "/Q" + rowOne + ",\"0.00%\")");


		

				if (shopMapByWindow.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelListByDIGITAL = shopMapByWindow
							.get(shop_id);
					for (int i = 0; i < modelListByDIGITAL.size(); i++) {
						if (WindowMap.get(modelListByDIGITAL.get(i).get(
								"model")) != null) {
							ArrayList<HashMap<String, Object>> priceList = WindowMap
									.get(modelListByDIGITAL.get(i).get(
											"model"));
							for (int j = 0; j < priceList.size(); j++) {
								BigDecimal bd = new BigDecimal(priceList
										.get(j).get("price").toString());
								String am = bd.toPlainString();
								double shop = Double.parseDouble(am);
								double price = shop
										/ Integer.parseInt(priceList.get(j)
												.get("shop").toString());

								long lnum = Math.round(price);

								String key = "WINDOW TYPE AIRCON"
										+ "_"
										+ modelListByDIGITAL.get(i).get(
												"model") + "_" + lnum + "_"
										+ "sold";

								bd = new BigDecimal(modelListByDIGITAL
										.get(i).get("quantity").toString());

								dataMap.put(key, bd.longValue());
							}

						}

					}

				}

				if (shopMapByFission.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelListByINTERNET = shopMapByFission
							.get(shop_id);
					for (int i = 0; i < modelListByINTERNET.size(); i++) {
						if (FissionMap.get(modelListByINTERNET.get(i).get(
								"model")) != null) {
							ArrayList<HashMap<String, Object>> priceList = FissionMap
									.get(modelListByINTERNET.get(i).get(
											"model"));
							for (int j = 0; j < priceList.size(); j++) {
								BigDecimal bd = new BigDecimal(priceList
										.get(j).get("price").toString());
								String am = bd.toPlainString();
								double shop = Double.parseDouble(am);
								double price = shop
										/ Integer.parseInt(priceList.get(j)
												.get("shop").toString());

								long lnum = Math.round(price);

								String key = "SPLIT TYPE AIRCON"
										+ "_"
										+ modelListByINTERNET.get(i).get(
												"model") + "_" + lnum + "_"
										+ "sold";

								bd = new BigDecimal(modelListByINTERNET
										.get(i).get("quantity").toString());

								dataMap.put(key, bd.longValue());
							}

						}

					}
				}
				
				
				
				
				
				if (stockMapByFission.get(shop_id) != null) {

					ArrayList<HashMap<String, Object>> stockListByDIGITAL = stockMapByFission
							.get(shop_id);
					for (int i = 0; i < stockListByDIGITAL.size(); i++) {

						String key = "SPLIT TYPE AIRCON" + "_"
								+ stockListByDIGITAL.get(i).get("model")
								+ "_" + " " + "_" + "Stocks";
						BigDecimal bd = new BigDecimal(stockListByDIGITAL
								.get(i).get("quantity").toString());

						dataMap.put(key, bd.longValue());
					}

				}

				if (stockMapByWindow.get(shop_id) != null) {

					ArrayList<HashMap<String, Object>> modelListByINTERNET = stockMapByWindow
							.get(shop_id);
					for (int i = 0; i < modelListByINTERNET.size(); i++) {

						String key = "WINDOW TYPE AIRCON" + "_"
								+ modelListByINTERNET.get(i).get("model")
								+ "_" + " " + "_" + "Stocks";

						BigDecimal bd = new BigDecimal(modelListByINTERNET
								.get(i).get("quantity").toString());

						dataMap.put(key, bd.longValue());
					}

				}
				
				
				
				

				if (disMapByWindow.get(shop_id) != null) {

					ArrayList<HashMap<String, Object>> disListByDIGITAL = disMapByWindow
							.get(shop_id);
					for (int i = 0; i < disListByDIGITAL.size(); i++) {

						String key = "WINDOW TYPE AIRCON" + "_"
								+ disListByDIGITAL.get(i).get("model")
								+ "_" + "" + "_" + "DisPlay";
						BigDecimal bd = new BigDecimal(disListByDIGITAL
								.get(i).get("quantity").toString());

						dataMap.put(key, bd.longValue());
					}

				}

				if (disMapByFission.get(shop_id) != null) {

					ArrayList<HashMap<String, Object>> disListByINTERNET = disMapByFission
							.get(shop_id);
					for (int i = 0; i < disListByINTERNET.size(); i++) {

						String key = "SPLIT TYPE AIRCON" + "_"
								+ disListByINTERNET.get(i).get("model")
								+ "_" + " " + "_" + "DisPlay";

						BigDecimal bd = new BigDecimal(disListByINTERNET
								.get(i).get("quantity").toString());

						dataMap.put(key, bd.longValue());
					}

				}
				
				
				
				

				
				// 查询类型总台数
				if (modelSpecByTotal.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> disListByCURVE = modelSpecByTotal
							.get(shop_id);
					for (int i = 0; i < disListByCURVE.size(); i++) {

						String qtyKey = "AIRCON  SUB-TOTAL_" + "QTY";
						String sumKey = "AIRCON  SUB-TOTAL_" + "AMOUNT";

						BigDecimal bd = new BigDecimal(disListByCURVE
								.get(i).get("quantity").toString());

						dataMap.put(qtyKey, bd.longValue());
						bd = new BigDecimal(disListByCURVE.get(i)
								.get("amount").toString());
						String am = bd.toPlainString();
						long lnum = Math.round(bd.doubleValue());
						if (am.contains(".")) {
							dataMap.put(sumKey, lnum);
						} else {
							dataMap.put(sumKey, am);
						}

					}

				}

				
				if (stockMapByTotal.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> stockTTL = stockMapByTotal
							.get(shop_id);
					for (int i = 0; i < stockTTL.size(); i++) {
						String key = "AIRCON TTL INVENTORY_Stocks";

						BigDecimal bd = new BigDecimal(stockTTL.get(i)
								.get("quantity").toString());

						dataMap.put(key, bd.longValue());

					}
				}

				if (disPlayMapByTotal.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> disTTL = disPlayMapByTotal
							.get(shop_id);
					for (int i = 0; i < disTTL.size(); i++) {
						String key = "AIRCON TTL INVENTORY_Display";
						BigDecimal bd = new BigDecimal(disTTL.get(i)
								.get("quantity").toString());
						dataMap.put(key, bd.longValue());
					}
				}

				dataList.add(dataMap);
			}

			String[] header = new String[columns.size()];
			String[] fields = new String[columns.size()];
			for (int i = 0, l = columns.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columns.get(i);
				header[i] = columnMap.get("header").toString();
				fields[i] = columnMap.get("field").toString();

			}

			// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
			Sheet sheet = wb.createSheet(sheetName);
			sheet.setZoom(3, 4);

			// 创建字体
			Font fontinfo = wb.createFont();
			fontinfo.setFontHeightInPoints((short) 11); // 字体大小
			fontinfo.setFontName("Trebuchet MS");
			Font fonthead = wb.createFont();
			fonthead.setFontHeightInPoints((short) 12);
			fonthead.setFontName("Trebuchet MS");

			// colSplit, rowSplit,leftmostColumn, topRow,
			sheet.createFreezePane(7, 6, 9, 7);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat
					.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
														// hh:mm:ss

			CellStyle cellStylehead = wb.createCellStyle();// 表头样式
			cellStylehead.setFont(fonthead);
			cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylehead.setWrapText(true);

			CellStyle cellStyleWHITE = wb.createCellStyle();// 表头样式
			cellStyleWHITE.setFont(fonthead);
			cellStyleWHITE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleWHITE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleWHITE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyleWHITE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleWHITE.setWrapText(true);

			CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fonthead);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen
					.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fonthead);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleYellow.setWrapText(true);

			CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
			cellStyleRED.setFont(fonthead);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);

			CellStyle cellStylePERCENT = wb.createCellStyle();// 表头样式
			cellStylePERCENT.setFont(fonthead);
			cellStylePERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePERCENT.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePERCENT.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT
					.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStylePERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePERCENT.setWrapText(true);

			CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));

			cellStyle.setWrapText(true);// 设置自动换行

			CellStyle contextstyle = wb.createCellStyle();
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			contextstyle.setFont(fontinfo);
			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			Row row = sheet.createRow((short) 0);

			// 创建这一行的一列，即创建单元格(CELL)
			Cell cell = row.createCell((short) 0);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("TCL " + partyName);// 标题
			// int firstRow, int lastRow, int firstCol, int lastCol
			// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
			cell = row.createCell((short) 2);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellStyle(cellStylename);
			cell.setCellValue(""); // 信息
			// int firstRow, int lastRow, int firstCol, int lastCol
			// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 7));

			// 第二行
			Row rowSec = sheet.createRow((short) 1);
			cell = rowSec.createCell((short) 0);
			cell.setCellStyle(cellStylename);
			cell.setCellValue("MARKETING DEPARTMENT");
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
			rowSec.setHeightInPoints(40);

			cell = rowSec.createCell((short) 6);
			cell.setCellStyle(cellStylename);
			cell.setCellValue("");
			// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 9));

			// 第二行
			Row rowSix = sheet.createRow((short) 6);
			DataFormat df = wb.createDataFormat();
			int size = excels.size() + 8;

			cell = rowSix.createCell((short) 2);
			cell.setCellValue("SUBTOTAL(9,C8:C" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,C8:C" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 5);
			cell.setCellValue("SUBTOTAL(9,F8:F" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,F8:F" + size + ")");

		
			// 第二行
			cell = rowSix.createCell((short) 14);
			cell.setCellValue("SUBTOTAL(9,O8:O" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,O8:O" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 15);
			cell.setCellValue("SUBTOTAL(9,P8:P" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,P8:P" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 16);
			cell.setCellValue("SUBTOTAL(9,Q8:Q" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,Q8:Q" + size + ")");


			// 第二行
			cell = rowSix.createCell((short) 17);
			cell.setCellValue("TEXT(P7/Q7,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT(P7/Q7,\"0.00%\")");

			// 第二行
			cell = rowSix.createCell((short) 18);
			cell.setCellValue("SUBTOTAL(9,S8:S" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,S8:S" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 19);
			cell.setCellValue("SUBTOTAL(9,T8:T" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,T8:T" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 20);
			cell.setCellValue("SUBTOTAL(9,U8:U" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,U8:U" + size + ")");
			
			// 第二行
			cell = rowSix.createCell((short) 21);
			cell.setCellValue("SUBTOTAL(9,V8:V" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,V8:V" + size + ")");
			
			// 第二行
			cell = rowSix.createCell((short) 22);
			cell.setCellValue("SUBTOTAL(9,W8:W" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,W8:W" + size + ")");
			
			// 第二行
			cell = rowSix.createCell((short) 23);
			cell.setCellValue("SUBTOTAL(9,X8:X" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,X8:X" + size + ")");
			
			// 第二行
			cell = rowSix.createCell((short) 24);
			cell.setCellValue("SUBTOTAL(9,Y8:Y" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,Y8:Y" + size + ")");
			
			
			// 第二行
			cell = rowSix.createCell((short) 25);
			cell.setCellValue("SUBTOTAL(9,Z8:Z" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,Z8:Z" + size + ")");
			

			// 头部标题长度-20就是后面需要计算的列
			String[] line = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
					"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
					"W", "X", "Y", "Z" };

			String[] big = {};
			// 写一个循环，让他们自动组合，放入big 1)AA,AB.......AZ 2)BA,BB........BZ 以此类推到Z
			for (int i = 0; i < line.length; i++) {
				for (int j = 0; j < line.length; j++) {
					String a = line[i];
					String b = line[j];
					big = insert(big, a + b);
				}
			}
			int start = 26;
			for (int i = 0; i < headers.length - 26; i++) {

				cell = rowSix.createCell((short) start);
				cell.setCellValue("SUBTOTAL(9," + big[i] + "8:" + big[i] + size
						+ ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9," + big[i] + "8:" + big[i]
						+ size + ")");

				start++;
			}

			int rows_max = 0; // 最大的一个项有几个子项

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map map = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 2);

				for (int i = 0; i < header.length; i++) {

					String headerTemp = header[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));

						if (i < 14) {
							cell.setCellStyle(cellStyleWHITE);
						} else if (i > 13 && i < 18) {
							cell.setCellStyle(cellStylehead);
						} else if (i > 17) {
							cell.setCellStyle(cellStyleYellow);
							if (s[0].contains("SUB-TOTAL")
									|| s[0].contains("TTL")) {
								cell.setCellStyle(cellStyleRED);
							}
						}
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2,
								rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						if (i < 14) {
							cell.setCellStyle(cellStyleWHITE);
						} else if (i > 13 && i < 18) {
							cell.setCellStyle(cellStylehead);
						} else if (i > 17) {
							cell.setCellStyle(cellStyleYellow);
							if (s[0].contains("SUB-TOTAL")
									|| s[0].contains("TTL")) {
								cell.setCellStyle(cellStyleRED);
							}
						}
						int cols = 0;
						if (map.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (map.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < header.length; j++) {
							if (header[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
							// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
							sheet.addMergedRegion(new CellRangeAddress(
									k + 2, k + 2, (num), (num + cols - 1)));
							if (sk.equals(headerTemp)) {
								sheet.addMergedRegion(new CellRangeAddress(
										k + 2, k + 2 + rows_max - s.length,
										num, num));
							}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			for (int d = 0; d < dataList.size(); d++) {

				HashMap<String, Object> dataMap = dataList.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 2 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fields.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fields[c]) != null
							&& dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fields[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fields[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());
							int s = dataMap.get(fields[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c])
									.toString());

						}
					} else {
						contentCell.setCellValue("");
					}

				}
			}

			// }
			System.out.println("耗时：" + (System.currentTimeMillis() - time)
					+ "毫秒");

			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void exportWeekThread(SXSSFWorkbook wb, String beginDateOne,
			String endDateOne,String tBeginDate,String tEndDate) {

		// 判断传过来的时间区
		String searchStr = null;
		String conditions = "";
		String center = "";
		String country = "";
		String region = "";
		String office = "";

		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if (!WebPageUtil.isHAdmin()) {
			if (request.getParameter("center") != null
					&& !request.getParameter("center").equals("")
					|| request.getParameter("country") != null
					&& !request.getParameter("country").equals("")
					|| request.getParameter("region") != null
					&& !request.getParameter("region").equals("")
					|| request.getParameter("office") != null
					&& !request.getParameter("office").equals("")) {

				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")) {
					center = request.getParameter("center");
					conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
							+ center + "')   ";
				}

				if (request.getParameter("country") != null
						&& !request.getParameter("country").equals("")) {
					country = request.getParameter("country");
					conditions = "  pa.country_id= " + country + "  ";
				}
				if (request.getParameter("region") != null
						&& !request.getParameter("region").equals("")) {
					region = request.getParameter("region");
					conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
							+ region
							+ "'  OR PARTY_ID='"+region+"'))  ";
				}
				if (request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {
					office = request.getParameter("office");
					conditions = "    pa.party_id IN ('" + office + "')  ";
				}

			} else {
				if (null != userPartyIds && !"".equals(userPartyIds)) {
					conditions = "  pa.party_id in (" + userPartyIds + ")  ";
				} else {
					conditions = "  1=2  ";
				}
			}

		} else {
			conditions = " 1=1 ";

		}
		
		if (request.getParameter("level") != null
				&& !request.getParameter("level").equals("")) {
			conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
		}
		try {
			if (WebPageUtil.getLoginedUserId() != null) {
				user = WebPageUtil.getLoginedUserId();
			}
			String partyName = excelService.selectPartyByUser(user);
			// ===========================================================
			// Sell-out Summary
			// ====================================================================//
			String[] days = beginDateOne.split("-");
			// 创建一个sheet【Sell-out Summary】
			DateUtil.year = Integer.parseInt(days[0]);
			DateUtil.month = Integer.parseInt(days[1]);

			// 表头数据
			String[] headers = { "Weekly Trend", "TTL AIRCON SO_Qty",
					"TTL AIRCON SO_Amt", "TARGET", "ACH" };
			// 按照对应格式将表头传入
			ArrayList columns = new ArrayList();
			for (int i = 0; i < headers.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headers[i]);
				columnMap.put("field", headers[i]);
				columns.add(columnMap);
			}
			String[] header = new String[columns.size()];
			String[] fields = new String[columns.size()];
			for (int i = 0, l = columns.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columns.get(i);
				header[i] = columnMap.get("header").toString();
				fields[i] = columnMap.get("field").toString();

			}

			// 用于放置表格数据
			LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();

			LinkedList<HashMap<String, Object>> list = DateUtil.getWeek(days[0]
					+ "-" + days[1] + "-01", endDateOne);
			for (int i = 0; i < list.size(); i++) {
				String beginDate = list.get(i).get("beginDate").toString();
				String endDate = list.get(i).get("endDate").toString();
				String[] be = beginDateOne.split("-");
				String[] en = endDateOne.split("-");
				int day = Integer.parseInt(en[2]) - Integer.parseInt(be[2]) + 1;
				// 用于放置表格数据
				HashMap<String, Object> data = new HashMap<String, Object>();

				int j = i + 1;
				data.put("Weekly Trend", "Week " + j);
				
				List<Excel> sumDatasByAc = excelService.selectSaleDataBySumByAc(
						beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());
				double saleSumByAc = 0.0;
				if (sumDatasByAc.size() == 0) {
					data.put("TTL AIRCON SO_Qty", "");
					data.put("TTL AIRCON SO_Amt", "");
					saleSumByAc = 0.0;
				} else {
					for (Excel excel : sumDatasByAc) {
						data.put("TTL AIRCON SO_Qty", excel.getSaleQty());
						data.put("TTL AIRCON SO_Amt", excel.getSaleSum());
						saleSumByAc = Double.parseDouble(excel.getSaleSum());
					}
				}

				
				
				
				// 查询当月销售目标总额
				List<Excel> TargetDatas = excelService.selectTargetDataBySum(
						beginDateOne, endDateOne, searchStr, conditions, tBeginDate, tEndDate,WebPageUtil.isHQRole(),2);
				if (TargetDatas.size() > 0) {
					BigDecimal bd = new BigDecimal(TargetDatas.get(0)
							.getTargetSum());
					String am = bd.toPlainString();
					data.put("TARGET", am);
					Double targetSum = TargetDatas.get(0).getTargetSum();
					double ach = saleSumByAc / targetSum * 100;
					long lnum = Math.round(ach);
					data.put("ACH", lnum + "%");

				}
				dataList.add(data);
			}
			
			
			HashMap<String, Object> data = new HashMap<String, Object>();
			String[] en = endDateOne.split("-");
			int day = Integer.parseInt(en[2]);

			
			// 按周查询销售总额
			List<Excel> sumDatasByAc = excelService.selectSaleDataBySumByAc(days[0]
					+ "-" + days[1] + "-01", endDateOne, searchStr, conditions,WebPageUtil.isHQRole());
			double saleSumByAc = 0.0;
			if (sumDatasByAc.size() == 0) {
				data.put("Weekly Trend", "Total");
				data.put("TTL AIRCON SO_Qty", "");
				data.put("TTL AIRCON SO_Amt", "");
				saleSumByAc = 0.0;
			} else {
				for (Excel excel : sumDatasByAc) {
					data.put("Weekly Trend", "Total");
					data.put("TTL AIRCON SO_Qty", excel.getSaleQty());
					data.put("TTL AIRCON SO_Amt", excel.getSaleSum());
					saleSumByAc = Double.parseDouble(excel.getSaleSum());
				}
			}
			
			
			// 查询当月销售目标总额
			List<Excel> TargetDatas = excelService.selectTargetDataBySum(
					beginDateOne, endDateOne, searchStr, conditions, tBeginDate, tEndDate,WebPageUtil.isHQRole(),2);
			if (TargetDatas.size() > 0) {
				BigDecimal bd = new BigDecimal(TargetDatas.get(0)
						.getTargetSum());
				String am = bd.toPlainString();
				data.put("TARGET", am);
				Double targetSum = TargetDatas.get(0).getTargetSum();
				double ach = saleSumByAc / targetSum * 100;
				long lnum = Math.round(ach);
				data.put("ACH", lnum + "%");

				
			}
			dataList.add(data);

			// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
			Sheet sheet = wb.createSheet("Sell-out Summary");
			sheet.setZoom(3, 4);
			// 创建字体
			Font fontinfo = wb.createFont();
			fontinfo.setFontHeightInPoints((short) 10); // 字体大小
			fontinfo.setFontName("Trebuchet MS");
			Font fonthead = wb.createFont();
			fonthead.setFontHeightInPoints((short) 10);
			fonthead.setFontName("Trebuchet MS");

			// colSplit, rowSplit,leftmostColumn, topRow,
			sheet.createFreezePane(3, 2, 3, 2);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat
					.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
														// hh:mm:ss

			CellStyle cellStylehead = wb.createCellStyle();// 表头样式
			cellStylehead.setFont(fonthead);
			cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylehead.setWrapText(true);

			CellStyle cellStyleLIGHTBLUE = wb.createCellStyle();// 表头样式
			cellStyleLIGHTBLUE.setFont(fonthead);
			cellStyleLIGHTBLUE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleLIGHTBLUE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleLIGHTBLUE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleLIGHTBLUE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleLIGHTBLUE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleLIGHTBLUE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleLIGHTBLUE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleLIGHTBLUE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleLIGHTBLUE.setTopBorderColor(HSSFColor.ROYAL_BLUE.index);
			cellStyleLIGHTBLUE
					.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
			cellStyleLIGHTBLUE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleLIGHTBLUE.setWrapText(true);

			CellStyle cellStyleWHITE = wb.createCellStyle();// 表头样式
			cellStyleWHITE.setFont(fonthead);
			cellStyleWHITE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleWHITE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleWHITE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyleWHITE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleWHITE.setWrapText(true);

			CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fonthead);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fonthead);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleYellow.setWrapText(true);

			CellStyle cellStylePERCENT = wb.createCellStyle();// 表头样式
			cellStylePERCENT.setFont(fonthead);
			cellStylePERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePERCENT.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePERCENT.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT
					.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStylePERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePERCENT.setWrapText(true);

			CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
			cellStyleRED.setFont(fonthead);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);

			CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			CellStyle contextstyle = wb.createCellStyle();
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			contextstyle.setFont(fontinfo);
			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			Row row = sheet.createRow((short) 0);
			// 创建这一行的一列，即创建单元格(CELL)
			Cell cell = row.createCell((short) 0);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("TCL " + partyName);// 标题

			// 第二行
			Row rowSec = sheet.createRow((short) 1);
			cell = rowSec.createCell((short) 0);
			cell.setCellStyle(cellStylename);
			cell.setCellValue("SELL-OUT SUMMARY");
			// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));

			// 从5行开始放另一个表格的表头

			int rows_max = 0; // 最大的一个项有几个子项

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map map = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 2);
				for (int i = 0; i < header.length; i++) {
					if (i <= 1) {
						cell.setCellStyle(cellStyleWHITE);
					} else if (i > 1 && i <= 3) {
						cell.setCellStyle(cellStyleLIGHTBLUE);
					} else if (i >= 4 && i < 8) {
						cell.setCellStyle(cellStylehead);
					} else {
						cell.setCellStyle(cellStyleGreen);
					}
					String headerTemp = header[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i + 2;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2,
								rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (map.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (map.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < header.length; j++) {
							if (header[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 2,
								k + 2, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k
									+ 2 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			DataFormat df = wb.createDataFormat();
			for (int d = 0; d < dataList.size(); d++) {

				HashMap<String, Object> dataMap = dataList.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 1 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);
				// 创建列
				for (int c = 0; c < fields.length; c++) {

					Cell contentCell = datarow.createCell(c + 2);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fields[c]) != null
							&& dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fields[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式

						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fields[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型

							contentCell.setCellValue(dataMap.get(fields[c])
									.toString());
						}

						if (d == dataList.size() - 1) {
							cellStyleYellow
									.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(cellStyleYellow);
						}

						/*
						 * if(isPercent){
						 * 
						 * cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat
						 * ("0%")); contentCell.setCellStyle(cellStyle); String
						 * s=dataMap.get(fields[c])
						 * .toString().substring(0,dataMap.get(fields[c])
						 * .toString().indexOf("%"));
						 * contentCell.setCellValue(Double.parseDouble(s)/100);
						 * 
						 * 
						 * }
						 */

					} else {
						contentCell.setCellValue("");
					}

				}
				// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
				sheet.addMergedRegion(new CellRangeAddress(4, 4 + dataList
						.size() - 1, 5, 5));
				

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void weekCPUsellout(SXSSFWorkbook wb, String partyName,
			String beginDateOne, String endDateOne,String tBeginDate,String tEndDate) {

		try {

			// 判断传过来的时间区
			String searchStr = null;
			String conditions = "";
			String center = "";
			String country = "";
			String region = "";
			String office = "";

			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			if (!WebPageUtil.isHAdmin()) {
				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")
						|| request.getParameter("country") != null
						&& !request.getParameter("country").equals("")
						|| request.getParameter("region") != null
						&& !request.getParameter("region").equals("")
						|| request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {

					if (request.getParameter("center") != null
							&& !request.getParameter("center").equals("")) {
						center = request.getParameter("center");
						conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
								+ center + "')   ";
					}

					if (request.getParameter("country") != null
							&& !request.getParameter("country").equals("")) {
						country = request.getParameter("country");
						countryId=country;
						conditions = "  pa.country_id= " + country + "  ";
					}
					if (request.getParameter("region") != null
							&& !request.getParameter("region").equals("")) {
						region = request.getParameter("region");
						conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
								+ region
								+ "'  OR PARTY_ID='"+region+"'))  ";
					}
					if (request.getParameter("office") != null
							&& !request.getParameter("office").equals("")) {
						office = request.getParameter("office");
						conditions = "    pa.party_id IN ('" + office + "')  ";
					}

				} else {
					if (null != userPartyIds && !"".equals(userPartyIds)) {
						conditions = "  pa.party_id in (" + userPartyIds + ")  ";
					} else {
						conditions = "  1=2  ";
					}
				}

			} else {
				conditions = " 1=1 ";

			}
			
			if (request.getParameter("level") != null
					&& !request.getParameter("level").equals("")) {
				conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
			}
			String[] days = beginDateOne.split("-");
			int year = Integer.parseInt(days[0]);
			int month = Integer.parseInt(days[1]);
			String beginDate = beginDateOne;
			String endDate = endDateOne;

			// 表头数据
			String[] headers = { "Weekly Trend"

			};
			HashMap<String, ArrayList<HashMap<String, Object>>> coreMap = null;
			List<HashMap<String, Object>> coreProduct = excelService
					.selectCoreProductByCountry(countryId,WebPageUtil.isHQRole());
			if (coreProduct.size() > 0) {
				coreMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < coreProduct.size(); m++) {
					if (coreMap.get(coreProduct.get(m).get("product_line")
							.toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(coreProduct.get(m));
						coreMap.put(coreProduct.get(m).get("product_line")
								.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = coreMap
								.get(coreProduct.get(m).get("product_line")
										.toString());
						modelList.add(coreProduct.get(m));
					}

				}

			}
			List<HashMap<String, Object>> coreLine = excelService
					.selectCoreLine(countryId);
			if (coreLine.size() > 0) {
				String order = coreLine.get(0).get("product_line").toString();

			}
			for (int i = 0; i < coreLine.size(); i++) {

				if (coreMap != null) {
					if (coreMap.get(coreLine.get(i).get("product_line")) != null) {
						ArrayList<HashMap<String, Object>> coreP = coreMap
								.get(coreLine.get(i).get("product_line"));
						for (int j = 0; j < coreP.size(); j++) {
							headers = insert(
									headers,
									coreP.get(j).get("product_line")
											+ " sellout_"
											+ coreP.get(j).get("model"));
						}
						headers = insert(headers,
								coreLine.get(i).get("product_line")
										+ " TTL Sellout");
						headers = insert(headers,
								coreLine.get(i).get("product_line") + " TARGET");
						headers = insert(headers,
								coreLine.get(i).get("product_line") + " ACH");
					}
				}

			}

			// 按照对应格式将表头传入
			ArrayList columns = new ArrayList();
			for (int i = 0; i < headers.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headers[i]);
				columnMap.put("field", headers[i]);
				columns.add(columnMap);
			}
			String[] header = new String[columns.size()];
			String[] fields = new String[columns.size()];
			for (int i = 0, l = columns.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columns.get(i);
				header[i] = columnMap.get("header").toString();
				fields[i] = columnMap.get("field").toString();

			}

			LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();

			LinkedList<HashMap<String, Object>> list = DateUtil.getWeek(days[0]
					+ "-" + days[1] + "-01", endDateOne);
			for (int i = 0; i < list.size(); i++) {
				beginDate = list.get(i).get("beginDate").toString();
				endDate = list.get(i).get("endDate").toString();
				List<HashMap<String, Object>> coreData = excelService
						.selectCoreSellData(searchStr, conditions, countryId,
								beginDate, endDate,WebPageUtil.isHQRole());
				HashMap<String, Object> data = new HashMap<String, Object>();
				int s = i + 1;
				data.put("Weekly Trend", "Week " + s);
				for (int j = 0; j < coreData.size(); j++) {
					BigDecimal bd = new BigDecimal(coreData.get(j)
							.get("quantity").toString());

					String key = coreData.get(j).get("product_line")
							+ " sellout_" + coreData.get(j).get("model");
					data.put(key, bd.longValue());
				}

				List<HashMap<String, Object>> coreDataTotal = excelService
						.selectCoreSellTotal(searchStr, conditions, countryId,
								beginDate, endDate,WebPageUtil.isHQRole());
				List<HashMap<String, Object>> coreDataTarget = excelService
						.selectCoreByTarget(searchStr, conditions,
								beginDate, endDate, countryId, tBeginDate, tEndDate,WebPageUtil.isHQRole());
				for (int j = 0; j < coreDataTotal.size(); j++) {
					String key = coreDataTotal.get(j).get("product_line")
							+ " TTL Sellout";
					BigDecimal bd = new BigDecimal(coreDataTotal.get(j)
							.get("quantity").toString());

					data.put(key, bd.longValue());

					Long saleQty = bd.longValue();
				
					for (int k = 0; k < coreDataTarget.size(); k++) {
						if (coreDataTotal
								.get(j)
								.get("product_line")
								.equals(coreDataTarget.get(k).get(
										"product_line"))) {
							bd = new BigDecimal(coreDataTarget.get(k)
									.get("quantity").toString());
							Long targetQty = bd.longValue();
							data.put(coreDataTarget.get(k).get("product_line")
									+ " TARGET", targetQty);
							if(targetQty!=0){
								double ach = saleQty / targetQty * 100;
								long lnum = Math.round(ach);
								data.put(coreDataTarget.get(k).get("product_line")
										+ " ACH", lnum + "%");
							}
							
						}

					}
				}

				dataList.add(data);

			}

			List<HashMap<String, Object>> coreData = excelService
					.selectCoreSellData(searchStr, conditions, countryId,
							days[0] + "-" + days[1] + "-01", endDateOne,WebPageUtil.isHQRole());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("Weekly Trend", "Total");
			for (int j = 0; j < coreData.size(); j++) {
				BigDecimal bd = new BigDecimal(coreData.get(j).get("quantity")
						.toString());

				String key = coreData.get(j).get("product_line") + " sellout_"
						+ coreData.get(j).get("model");
				data.put(key, bd.longValue());
			}

			List<HashMap<String, Object>> coreDataTotal = excelService
					.selectCoreSellTotal(searchStr, conditions, countryId,
							days[0] + "-" + days[1] + "-01", endDateOne,WebPageUtil.isHQRole());
			List<HashMap<String, Object>> coreDataTarget = excelService
					.selectCoreByTarget(searchStr, conditions, days[0]
							+ "-" + days[1] + "-01", endDate, countryId, tBeginDate, tEndDate,WebPageUtil.isHQRole());
			for (int j = 0; j < coreDataTotal.size(); j++) {
				String key = coreDataTotal.get(j).get("product_line")
						+ " TTL Sellout";
				BigDecimal bd = new BigDecimal(coreDataTotal.get(j)
						.get("quantity").toString());
				data.put(key, bd.longValue());
				Long saleQty = bd.longValue();
				
				for (int k = 0; k < coreDataTarget.size(); k++) {
					if (coreDataTarget.get(k).get("product_line")
							.equals(coreDataTotal.get(j).get("product_line"))) {

						bd = new BigDecimal(coreDataTarget.get(k)
								.get("quantity").toString());

						Long targetQty = bd.longValue();
						data.put(coreDataTarget.get(k).get("product_line")
								+ " TARGET", targetQty);
						if(targetQty!=0){
							double ach = saleQty / targetQty * 100;
							long lnum = Math.round(ach);
							data.put(coreDataTarget.get(k).get("product_line")
									+ " ACH", lnum + "%");
						}
					}

				}
			}

			dataList.add(data);

			// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
			Sheet sheet = wb.createSheet("Core Product sellout");
			sheet.setZoom(3, 4);
			// 创建字体
			Font fontinfo = wb.createFont();
			fontinfo.setFontHeightInPoints((short) 11); // 字体大小
			fontinfo.setFontName("Trebuchet MS");
			Font fonthead = wb.createFont();
			fonthead.setFontHeightInPoints((short) 12);
			fonthead.setFontName("Trebuchet MS");

			// colSplit, rowSplit,leftmostColumn, topRow,
			sheet.createFreezePane(3, 2, 3, 2);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat
					.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
														// hh:mm:ss

			CellStyle cellStylehead = wb.createCellStyle();// 表头样式
			cellStylehead.setFont(fonthead);
			cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
			cellStyleRED.setFont(fonthead);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);

			CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fonthead);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fonthead);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			Row row = sheet.createRow((short) 0);
			// 创建这一行的一列，即创建单元格(CELL)
			Cell cell = row.createCell((short) 0);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("TCL " + partyName);// 标题
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

			// 第二行
			Row rowSec = sheet.createRow((short) 1);
			cell = rowSec.createCell((short) 0);
			cell.setCellStyle(cellStylename);
			cell.setCellValue("Core Product sellout");
			// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

			CellStyle contextstyle = wb.createCellStyle();
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			contextstyle.setFont(fontinfo);
			// 从17行开始放另一个表格的表头

			int rows_max = 0; // 最大的一个项有几个子项

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map map = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 2);
				for (int i = 0; i < header.length; i++) {

					String headerTemp = header[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					cell.setCellStyle(cellStylehead);

					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2,
								rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (map.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (map.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < header.length; j++) {
							if (header[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 2,
								k + 2, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k
									+ 2 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int d = 0; d < dataList.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataList.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 1 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 定义单元格为字符串类型
				scell.setCellType(Cell.CELL_TYPE_NUMERIC);
				// 创建列
				for (int c = 0; c < fields.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fields[c]) != null
							&& dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fields[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数

							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点

							}

							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fields[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());

						} else {

							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c])
									.toString());
						}

						if (d == dataList.size() - 1) {
							cellStyleYellow
									.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(cellStyleYellow);

						}
						if (dataMap.get(fields[c]).toString().contains("%")) {
							String b = dataMap.get(fields[c]).toString(); // 去掉%
																			// String
							String tempB = b.substring(0, b.lastIndexOf("%")); // 精确表示
																				// Integer
							Integer dataB = Integer.parseInt(tempB); // 大于为1，相同为0，小于为-1
																		// if
							if (dataB >= 80 && dataB < 100/*
														 * .compareTo(dataA) ==
														 * 0
														 */) {
								contentCell.setCellStyle(cellStyleYellow);
							} else if (dataB >= 100/*
													 * .compareTo(dataA) == 1
													 */) {
								contentCell.setCellStyle(cellStyleGreen);
							} else if (dataB < 80/* .compareTo(dataA) == -1 */) {
								contentCell.setCellStyle(cellStyleRED);
							}

						}

					} else {
						contentCell.setCellValue("");
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sellOutSummary(SXSSFWorkbook wb, String partyName,
			String beginDateOne, String endDateOne,String tBeginDate,String tEndDate) {
		try {
			// ===========================================================
			// Sell-out Summary
			// ====================================================================//
			String[] days = beginDateOne.split("-");
			// 创建一个sheet【Sell-out Summary】
			DateUtil.year = Integer.parseInt(days[0]);
			DateUtil.month = Integer.parseInt(days[1]);
			String beginDate = beginDateOne;
			String endDate = endDateOne;

			// 表头数据
			String[] headers = { "Weekly Trend", "TTL AIRCON SO_Qty",
					"TTL AIRCON SO_Amt", "TARGET", "ACH" };
			// 按照对应格式将表头传入
			ArrayList columns = new ArrayList();
			for (int i = 0; i < headers.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headers[i]);
				columnMap.put("field", headers[i]);
				columns.add(columnMap);
			}
			String[] header = new String[columns.size()];
			String[] fields = new String[columns.size()];
			for (int i = 0, l = columns.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columns.get(i);
				header[i] = columnMap.get("header").toString();
				fields[i] = columnMap.get("field").toString();

			}

			String[] headersTwo = { "Rank", "Regional Head", "AREA",
					"No of Shops", "AC FPS", "TTL AIRCON SO_Qty",
					"TTL AIRCON SO_Amt", "TARGET", "ACH" };

			// 按照对应格式将表头传入
			ArrayList columnsTwo = new ArrayList();
			for (int i = 0; i < headersTwo.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersTwo[i]);
				columnMap.put("field", headersTwo[i]);
				columnsTwo.add(columnMap);
			}

			String[] headerTwo = new String[columnsTwo.size()];
			String[] fieldsTwo = new String[columnsTwo.size()];
			for (int i = 0, l = columnsTwo.size(); i < l; i++) {
				HashMap columnMap = (HashMap) columnsTwo.get(i);
				headerTwo[i] = columnMap.get("header").toString();
				fieldsTwo[i] = columnMap.get("field").toString();

			}

			// 表头数据
			String[] headersThree = { "Rank", "Saleman", "Account",
					"No of Shops", "No of FPS", "TTL AIRCON SO_Qty",
					"TTL AIRCON SO_Amt", "TARGET", "ACH" };

			ArrayList columnsThree = new ArrayList();
			for (int i = 0; i < headersThree.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersThree[i]);
				columnMap.put("field", headersThree[i]);
				columnsThree.add(columnMap);
			}

			String[] headerThree = new String[columnsThree.size()];
			String[] fieldsThree = new String[columnsThree.size()];
			for (int i = 0, l = columnsThree.size(); i < l; i++) {
				HashMap columnMap = (HashMap) columnsThree.get(i);
				headerThree[i] = columnMap.get("header").toString();
				fieldsThree[i] = columnMap.get("field").toString();

			}

			// 表头数据
			String[] headersFour = { "Rank", "Acfo", "Area", "No of Shops",
					"No of FPS", "TTL AIRCON SO_Qty",
					"TTL AIRCON SO_Amt", "TARGET", "ACH" };

			ArrayList columnsFour = new ArrayList();
			for (int i = 0; i < headersFour.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersFour[i]);
				columnMap.put("field", headersFour[i]);
				columnsFour.add(columnMap);
			}

			String[] headerFour = new String[columnsFour.size()];
			String[] fieldsFour = new String[columnsFour.size()];
			for (int i = 0, l = columnsFour.size(); i < l; i++) {
				HashMap columnMap = (HashMap) columnsFour.get(i);
				headerFour[i] = columnMap.get("header").toString();
				fieldsFour[i] = columnMap.get("field").toString();

			}

			String searchStr = null;
			String conditions = "";
			String center = "";
			String country = "";
			String region = "";
			String office = "";

			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			if (!WebPageUtil.isHAdmin()) {
				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")
						|| request.getParameter("country") != null
						&& !request.getParameter("country").equals("")
						|| request.getParameter("region") != null
						&& !request.getParameter("region").equals("")
						|| request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {

					if (request.getParameter("center") != null
							&& !request.getParameter("center").equals("")) {
						center = request.getParameter("center");
						conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
								+ center + "')   ";
					}

					if (request.getParameter("country") != null
							&& !request.getParameter("country").equals("")) {
						country = request.getParameter("country");
						conditions = "  pa.country_id= " + country + "  ";
					}
					if (request.getParameter("region") != null
							&& !request.getParameter("region").equals("")) {
						region = request.getParameter("region");
						conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
								+ region
								+ "'  OR PARTY_ID='"+region+"'))  ";
					}
					if (request.getParameter("office") != null
							&& !request.getParameter("office").equals("")) {
						office = request.getParameter("office");
						conditions = "    pa.party_id IN ('" + office + "')  ";
					}

				} else {
					if (null != userPartyIds && !"".equals(userPartyIds)) {
						conditions = "  pa.party_id in (" + userPartyIds + ")  ";
					} else {
						conditions = "  1=2  ";
					}
				}

			} else {
				conditions = " 1=1 ";

			}
			
			if (request.getParameter("level") != null
					&& !request.getParameter("level").equals("")) {
				conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
			}
			// 用于放置表格数据
			LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();

			for (int r = 1; r <= 6; r++) {
				int day = 0;
				// 用于放置表格数据
				HashMap<String, Object> data = new HashMap<String, Object>();
				DateUtil.year = Integer.parseInt(days[0]);
				DateUtil.month = Integer.parseInt(days[1]);
				if (r == 6) {
					beginDate = beginDateOne;
					endDate = endDateOne;
					String[] st = beginDateOne.split("-");
					String[] en = endDateOne.split("-");
					day = Integer.parseInt(en[2]) - Integer.parseInt(st[2]) + 1;
				} else {
					DateUtil.setWeekend(r);
					DateUtil.count();
					String s = "";
					String e = "";
					if (String.valueOf(DateUtil.getStart()).length() == 1) {
						s = "0" + DateUtil.getStart();
					} else {
						s = DateUtil.getStart() + "";
					}
					if (String.valueOf(DateUtil.getEnd()).length() == 1) {
						e = "0" + DateUtil.getEnd();
					} else {
						e = DateUtil.getEnd() + "";
					}
					beginDate = days[0] + "-" + days[1] + "-" + s;
					endDate = days[0] + "-" + days[1] + "-" + e;
					if(r==5 &&  !endDate.equals(endDateOne)){
						endDate=endDateOne;
					}
					day = Integer.parseInt(endDate.split("-")[2]) - DateUtil.getStart() + 1;
				
				}
				
				
				// 按周查询销售总额
				List<Excel> sumDatasByAc = excelService.selectSaleDataBySumByAc(
						beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());
				double saleSumByAc = 0.0;
				if (sumDatasByAc.size() == 0) {
					if (r == 6) {
						data.put("Weekly Trend", "Total");
					} else {
						data.put("Weekly Trend", "Week " + r);
					}
					data.put("TTL AIRCON SO_Qty", "");
					data.put("TTL AIRCON SO_Amt", "");
					saleSumByAc = 0.0;
				} else {
					for (Excel excel : sumDatasByAc) {
						if (r == 6) {
							data.put("Weekly Trend", "Total");
						} else {
							data.put("Weekly Trend", "Week " + r);
						}
						data.put("TTL AIRCON SO_Qty", excel.getSaleQty());
						data.put("TTL AIRCON SO_Amt", excel.getSaleSum());

						saleSumByAc = Double.parseDouble(excel.getSaleSum());
					}
				}
				
				
				

				// 查询当月销售目标总额
				List<Excel> TargetDatas = excelService.selectTargetDataBySum(
						beginDateOne, endDateOne, searchStr, conditions, tBeginDate, tEndDate,WebPageUtil.isHQRole(),2);
				if (TargetDatas.size() > 0) {
					BigDecimal bd = new BigDecimal(TargetDatas.get(0)
							.getTargetSum());
					String am = bd.toPlainString();
					data.put("TARGET", am);
					Double targetSum = TargetDatas.get(0).getTargetSum();
					double ach = saleSumByAc / targetSum * 100;
					long lnum = Math.round(ach);
					data.put("ACH", lnum + "%");

					
				}
				dataList.add(data);

			}

			LinkedList<HashMap<String, Object>> dataListTwo = new LinkedList<HashMap<String, Object>>();

			List<HashMap<String, Object>> areaDatasByAc = excelService
					.selectDataByAreaByAc(beginDateOne, endDateOne, searchStr,
							conditions,WebPageUtil.isHQRole());
			
			List<HashMap<String, Object>> re = excelService
					.selectRegionalHeadByParty(searchStr, conditions);
			int shops = 0;
			List<HashMap<String, Object>> targetDatas = excelService
					.selectTargetByArea( searchStr,
							conditions, tBeginDate, tEndDate,WebPageUtil.isHQRole(),2);
			for (int j = 0; j < areaDatasByAc.size(); j++) {
				HashMap<String, Object> dataMap = new HashMap<String, Object>();

				for (int i = 0; i < re.size(); i++) {
					if (areaDatasByAc.get(j).get("partyId")
							.equals(re.get(i).get("PARTY_ID"))) {
						dataMap.put("Regional Head", re.get(i).get("userName"));
					}
				}

				dataMap.put("AREA", areaDatasByAc.get(j).get("AREA"));
				dataMap.put("No of Shops", areaDatasByAc.get(j).get("noOfShops"));
				dataMap.put("AC FPS",  areaDatasByAc.get(j).get("tvFps"));
				BigDecimal bd = new BigDecimal(areaDatasByAc.get(j).get("saleQty")
						.toString());

				bd = new BigDecimal(areaDatasByAc.get(j).get("saleQty")
						.toString());

				dataMap.put("TTL AIRCON SO_Qty", bd.longValue());
				bd = new BigDecimal(areaDatasByAc.get(j).get("saleSum").toString());
				String  saleSumOne = bd.toPlainString();
				dataMap.put("TTL AIRCON SO_Amt", saleSumOne);
				Double saleSum = Double.parseDouble(saleSumOne);
				
				for (HashMap<String, Object> hashMap : targetDatas) {
					if(hashMap.get("PARTY_ID")
							.toString().equals(areaDatasByAc.get(j).get("partyId").toString())){
						BigDecimal a = new BigDecimal(hashMap.get("targetSum")
								.toString());
						String targetSumOne = a.toPlainString();
						dataMap.put("TARGET", targetSumOne);
						Double targetSum = Double.parseDouble(targetSumOne);
						double ach = saleSum / targetSum * 100;
						long lnum = Math.round(ach);
						dataMap.put("ACH", lnum + "%");

					}
					
				}
				
				
				
			
				dataListTwo.add(dataMap);
			}

			DateUtil.Order(dataListTwo, "ACH");
			for (int i = 0; i < dataListTwo.size(); i++) {
				dataListTwo.get(i).put("Rank", i + 1);
			}
			HashMap<String, Object> data = new HashMap<String, Object>();
			int rows = 17 + dataListTwo.size() - 1;
			int end = rows + 1;
			data.put("Regional Head", "Total");
			data.put("No of Shops", "SUM(D17:D" + rows + ")");
			data.put("AC FPS", "SUM(E17:E" + rows + ")");
			

			data.put("TTL AIRCON SO_Qty", "SUM(F17:F" + rows + ")");
			data.put("TTL AIRCON SO_Amt", "SUM(G17:G" + rows + ")");
			data.put("TARGET", "SUM(H17:H" + rows + ")");

			data.put("ACH", "TEXT(G" + end + "/H" + end + ",\"0.00%\")");

			dataListTwo.add(data);

			LinkedList<HashMap<String, Object>> dataListThree = new LinkedList<HashMap<String, Object>>();

			
			List<HashMap<String, Object>> salemanDatasByAc = excelService
					.selectTargetBySalemanByAc(beginDateOne, endDateOne, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);
			
			List<HashMap<String, Object>> Account = excelService
					.selectPartyNameByuser(searchStr, conditions);

			List<HashMap<String, Object>> saleFps = excelService
					.selectFpsNameByShop(beginDateOne, endDateOne, searchStr,
							conditions);

			for (int i = 0; i < salemanDatasByAc.size(); i++) {
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				// dataMap.put("Rank", i + 1);
				dataMap.put("Saleman", salemanDatasByAc.get(i).get("userName"));

				if (Account.size() > 1) {
					String a = "";
					for (int k = 0; k < Account.size(); k++) {
						if (salemanDatasByAc.get(i).get("userId")
								.equals(Account.get(k).get("userId"))) {
							a += Account.get(k).get("PARTY_NAME") + ",";
						}

					}
					dataMap.put("Account", a.substring(0, a.length() - 1));
				}
				int tvFps = 0;
				for (int j = 0; j < saleFps.size(); j++) {
					if (saleFps.get(j).get("USER")
							.equals(salemanDatasByAc.get(i).get("userId"))) {
						tvFps = Integer.parseInt(saleFps.get(j).get("TVFPS")
								.toString());
						dataMap.put("No of FPS", tvFps);

					}
				}

				dataMap.put("No of Shops", salemanDatasByAc.get(i).get("noOfShops"));

				BigDecimal bd = new BigDecimal(salemanDatasByAc.get(i)
						.get("saleQty").toString());
				dataMap.put("TTL AIRCON SO_Qty", bd.longValue());
				BigDecimal a = new BigDecimal(salemanDatasByAc.get(i)
						.get("saleSum").toString());
				String saleSumOne = a.toPlainString();
				dataMap.put("TTL AIRCON SO_Amt", saleSumOne);
			
				Double saleSum=Double.parseDouble(saleSumOne);

				BigDecimal b = new BigDecimal(salemanDatasByAc.get(i)
						.get("targetSum").toString());
				String targetSumOne = b.toPlainString();
				dataMap.put("TARGET", targetSumOne);
				Double targetSum = Double.parseDouble(targetSumOne);
				double ach = saleSum / targetSum * 100;
				long lnum = Math.round(ach);
				dataMap.put("ACH", lnum + "%");

			
				

			
				
				
				dataListThree.add(dataMap);

			}

			DateUtil.Order(dataListThree, "ACH");
			for (int i = 0; i < dataListThree.size(); i++) {

				dataListThree.get(i).put("Rank", i + 1);
			}
			HashMap<String, Object> dataThree = new HashMap<String, Object>();
			int stratRow = 17 + dataListTwo.size() + 10;
			int rowsBysale = 17 + dataListTwo.size() + 10
					+ dataListThree.size() - 1;
			end = rowsBysale + 1;
			dataThree.put("Saleman", "Total");
			dataThree.put("No of Shops", "SUM(D" + stratRow + ":D" + rowsBysale
					+ ")");
			dataThree.put("No of FPS", "SUM(E" + stratRow + ":E" + rowsBysale
					+ ")");

			dataThree.put("TTL AIRCON SO_Qty", "SUM(F" + stratRow + ":F"
					+ rowsBysale + ")");
			dataThree.put("TTL AIRCON SO_Amt", "SUM(G" + stratRow + ":G"
					+ rowsBysale + ")");
			dataThree.put("TARGET", "SUM(H" + stratRow
					+ ":H" + rowsBysale + ")");

			dataThree.put("ACH", "TEXT(G" + end + "/H" + end
					+ ",\"0.00%\")");

		

			

			dataListThree.add(dataThree);

			LinkedList<HashMap<String, Object>> dataListFour = new LinkedList<HashMap<String, Object>>();

			
			List<HashMap<String, Object>> acfoDatasByAc = excelService
					.selectTargetByAcfoByAc(beginDateOne, endDateOne, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);
			
			List<HashMap<String, Object>> Area = excelService.selectAreaByUser(
					searchStr, conditions);

			List<HashMap<String, Object>> acfoFps = excelService
					.selectFpsNameByShopByAc(beginDateOne, endDateOne, searchStr,
							conditions);

			for (int i = 0; i < acfoDatasByAc.size(); i++) {
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("Rank", i + 1);
				dataMap.put("Acfo", acfoDatasByAc.get(i).get("userName"));

				if (Area.size() > 1) {
					String a = "";
					for (int k = 0; k < Area.size(); k++) {
						if (acfoDatasByAc.get(i).get("userId").toString()
								.equals(Area.get(k).get("userId"))) {

							a += Area.get(k).get("PARTY_NAME") + ",";
						}

					}
					if (a.length() > 0) {
						dataMap.put("Area", a.substring(0, a.length() - 1));
					}
				}

				int tvFps = 0;
				for (int j = 0; j < acfoFps.size(); j++) {
					if (acfoFps.get(j).get("USER")
							.equals(acfoDatasByAc.get(i).get("userId"))) {
						tvFps = Integer.parseInt(acfoFps.get(j).get("TVFPS")
								.toString());
						dataMap.put("No of FPS", tvFps);

					}
				}

				dataMap.put("No of Shops", acfoDatasByAc.get(i).get("noOfShops"));

				BigDecimal a = new BigDecimal(acfoDatasByAc.get(i).get("saleQty")
						.toString());
				dataMap.put("TTL AIRCON SO_Qty", a.longValue());
				a = new BigDecimal(acfoDatasByAc.get(i).get("saleSum").toString());
				String saleSumOne = a.toPlainString();
				dataMap.put("TTL AIRCON SO_Amt", saleSumOne);
				
				
				Double saleSum = Double.parseDouble(saleSumOne);

				BigDecimal b = new BigDecimal(acfoDatasByAc.get(i).get("targetSum")
						.toString());
				String targetSumOne = b.toPlainString();
				dataMap.put("TARGET", targetSumOne);
				Double targetSum = Double.parseDouble(targetSumOne);
				double ach = saleSum / targetSum * 100;
				long lnum = Math.round(ach);
				dataMap.put("ACH", lnum + "%");


		
						

				
			
				dataListFour.add(dataMap);

			}
			DateUtil.Order(dataListFour, "ACH");
			for (int i = 0; i < dataListFour.size(); i++) {
				dataListFour.get(i).put("Rank", i + 1);
			}
			HashMap<String, Object> dataFour = new HashMap<String, Object>();
			stratRow = 17 + dataListTwo.size() + 10 + dataListThree.size() + 10;
			rowsBysale = 17 + dataListTwo.size() + 10 + dataListThree.size()
					+ 9 + dataListFour.size();
			end = rowsBysale + 1;
			dataFour.put("Acfo", "Total");
			dataFour.put("No of Shops", "SUM(D" + stratRow + ":D" + rowsBysale
					+ ")");
			dataFour.put("No of FPS", "SUM(E" + stratRow + ":E" + rowsBysale
					+ ")");

			dataFour.put("TTL AIRCON SO_Qty", "SUM(F" + stratRow + ":F"
					+ rowsBysale + ")");
			dataFour.put("TTL AIRCON SO_Amt", "SUM(G" + stratRow + ":G"
					+ rowsBysale + ")");
			dataFour.put("TARGET", "SUM(H" + stratRow
					+ ":H" + rowsBysale + ")");

			dataFour.put("ACH", "TEXT(G" + end + "/H" + end
					+ ",\"0.00%\")");

			
			

			dataListFour.add(dataFour);

			// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
			Sheet sheet = wb.createSheet("Sell-out Summary");
			sheet.setZoom(3, 4);
			// 创建字体
			Font fontinfo = wb.createFont();
			fontinfo.setFontHeightInPoints((short) 10); // 字体大小
			fontinfo.setFontName("Trebuchet MS");
			Font fonthead = wb.createFont();
			fonthead.setFontHeightInPoints((short) 10);
			fonthead.setFontName("Trebuchet MS");

			// colSplit, rowSplit,leftmostColumn, topRow,
			sheet.createFreezePane(3, 2, 3, 2);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat
					.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
														// hh:mm:ss

			CellStyle cellStylehead = wb.createCellStyle();// 表头样式
			cellStylehead.setFont(fonthead);
			cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylehead.setWrapText(true);

			CellStyle cellStyleLIGHTBLUE = wb.createCellStyle();// 表头样式
			cellStyleLIGHTBLUE.setFont(fonthead);
			cellStyleLIGHTBLUE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleLIGHTBLUE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleLIGHTBLUE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleLIGHTBLUE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleLIGHTBLUE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleLIGHTBLUE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleLIGHTBLUE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleLIGHTBLUE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleLIGHTBLUE.setTopBorderColor(HSSFColor.ROYAL_BLUE.index);
			cellStyleLIGHTBLUE
					.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
			cellStyleLIGHTBLUE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleLIGHTBLUE.setWrapText(true);

			CellStyle cellStyleWHITE = wb.createCellStyle();// 表头样式
			cellStyleWHITE.setFont(fonthead);
			cellStyleWHITE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleWHITE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleWHITE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyleWHITE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleWHITE.setWrapText(true);

			CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fonthead);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fonthead);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleYellow.setWrapText(true);

			CellStyle cellStylePERCENT = wb.createCellStyle();// 表头样式
			cellStylePERCENT.setFont(fonthead);
			cellStylePERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePERCENT.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePERCENT.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT
					.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStylePERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePERCENT.setWrapText(true);

			CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
			cellStyleRED.setFont(fonthead);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);

			CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			CellStyle contextstyle = wb.createCellStyle();
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			contextstyle.setFont(fontinfo);
			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			Row row = sheet.createRow((short) 0);
			// 创建这一行的一列，即创建单元格(CELL)
			Cell cell = row.createCell((short) 0);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("TCL " + partyName);// 标题

			// 第二行
			Row rowSec = sheet.createRow((short) 1);
			cell = rowSec.createCell((short) 0);
			cell.setCellStyle(cellStylename);
			cell.setCellValue("SELL-OUT SUMMARY");
			// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));

			// 从17行开始放另一个表格的表头

			int rows_max = 0; // 最大的一个项有几个子项

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map map = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 2);
				for (int i = 0; i < header.length; i++) {
					if (i <= 1) {
						cell.setCellStyle(cellStyleWHITE);
					} else if (i > 1 && i <= 3) {
						cell.setCellStyle(cellStyleLIGHTBLUE);
					} else if (i >= 4 && i < 8) {
						cell.setCellStyle(cellStylehead);
					} else {
						cell.setCellStyle(cellStyleGreen);
					}
					String headerTemp = header[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i + 2;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2,
								rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (map.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (map.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < header.length; j++) {
							if (header[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 2,
								k + 2, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k
									+ 2 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerTwo.length; i++) {
				String h = headerTwo[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapTwo = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 14);
				for (int i = 0; i < headerTwo.length; i++) {
					if (i < 6) {
						cell.setCellStyle(cellStyleWHITE);
					} else if (i >= 6 && i < 9) {
						cell.setCellStyle(cellStyleLIGHTBLUE);
					} else if (i >= 9 && i < 15) {
						cell.setCellStyle(cellStylehead);
					} else {
						cell.setCellStyle(cellStyleGreen);
					}
					String headerTemp = headerTwo[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(14,
								rows_max + 13, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);
					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapTwo.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapTwo.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerTwo.length; j++) {
							if (headerTwo[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 14,
								k + 14, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 14,
									k + 14 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapTwo.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapTwo.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerThree.length; i++) {
				String h = headerThree[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapThree = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 14 + dataListTwo.size() + 10);
				for (int i = 0; i < headerThree.length; i++) {
					if (i < 6) {
						cell.setCellStyle(cellStyleWHITE);
					} else if (i >= 6 && i < 8) {
						cell.setCellStyle(cellStyleLIGHTBLUE);
					} else if (i >= 8 && i < 14) {
						cell.setCellStyle(cellStylehead);
					} else {
						cell.setCellStyle(cellStyleGreen);
					}
					String headerTemp = headerThree[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(
								14 + dataListTwo.size() + 10, rows_max + 13
										+ dataListTwo.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapThree.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapThree.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerThree.length; j++) {
							if (headerThree[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 14
								+ dataListTwo.size() + 10, k + 14
								+ dataListTwo.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 14
									+ dataListTwo.size() + 10, k + 14
									+ dataListTwo.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapThree.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapThree.put(key, null);
						}
					}
				}
			}

			// ACFO
			for (int i = 0; i < headerFour.length; i++) {
				String h = headerFour[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapFour = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 14 + dataListTwo.size() + 10
						+ dataListThree.size() + 10);
				for (int i = 0; i < headerFour.length; i++) {
					if (i < 6) {
						cell.setCellStyle(cellStyleWHITE);
					} else if (i >= 6 && i < 8) {
						cell.setCellStyle(cellStyleLIGHTBLUE);
					} else if (i >= 8 && i < 14) {
						cell.setCellStyle(cellStylehead);
					} else {
						cell.setCellStyle(cellStyleGreen);
					}
					String headerTemp = headerFour[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(14
								+ +dataListTwo.size() + 10
								+ dataListThree.size() + 10, rows_max + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapFour.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapFour.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerFour.length; j++) {
							if (headerFour[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 14
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10,

						k + 14 + dataListTwo.size() + 10 + dataListThree.size()
								+ 10, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 14
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10, k + 14
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapFour.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapFour.put(key, null);
						}
					}
				}
			}
			DataFormat df = wb.createDataFormat();
			for (int d = 0; d < dataList.size(); d++) {

				HashMap<String, Object> dataMap = dataList.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 1 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);
				// 创建列
				for (int c = 0; c < fields.length; c++) {

					Cell contentCell = datarow.createCell(c + 2);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fields[c]) != null
							&& dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fields[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							if (d == dataList.size() - 1) {
								contentCell.setCellStyle(cellStyleYellow);

							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fields[c]).toString()));
						}else	if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());
						} else if (isGongshiOne) {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());

						}  else {

							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c])
									.toString());
							
							
						}
						
						
						
					} else {
						contentCell.setCellValue("");
					}
					if (d == dataList.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
						
					}
				}
				// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
				sheet.addMergedRegion(new CellRangeAddress(4, 9, 5, 5));
				
			}

			for (int d = 0; d < dataListTwo.size(); d++) {
				HashMap<String, Object> dataMap = dataListTwo.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 1 + rows_max + 13);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				// 创建列
				for (int c = 0; c < fieldsTwo.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsTwo[c]) != null
							&& dataMap.get(fieldsTwo[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTwo[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTwo[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTwo[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsTwo[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsTwo[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsTwo[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsTwo[c])
									.toString());
						}

						if (c == 9 && d != dataListTwo.size() - 1) {

							if (dataMap.get(fieldsTwo[c]).toString()
									.contains("%")
							/*
							 * &&
							 * dataMapLast.get(fieldsTwo[c]).toString().contains
							 * ("%")
							 */
							) {

								// String a =
								// dataMapLast.get(fieldsTwo[c]).toString();
								String b = dataMap.get(fieldsTwo[c]).toString(); // 去掉%
																					// String
								// String tempA = a.substring(0,
								// a.lastIndexOf("%"));
								String tempB = b.substring(0,
										b.lastIndexOf("%")); // 精确表示 Integer
								// Integer dataA = Integer.parseInt(tempA);
								BigDecimal dataB = new BigDecimal(tempB); // 大于为1，相同为0，小于为-1
																			// if
								if (dataB.compareTo(BigDecimal.valueOf(100)) == -1

										&& (dataB.compareTo(BigDecimal
												.valueOf(80)) == 1 || dataB
												.compareTo(BigDecimal
														.valueOf(80)) == 0)
								/*
								 * .compareTo(dataA) == 0
								 */) {
									contentCell.setCellStyle(cellStyleYellow);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(100)) == 1
										|| dataB.compareTo(BigDecimal
												.valueOf(100)) == 1
								/*
								 * dataB >= 100 .compareTo(dataA) == 1
								 */) {
									contentCell.setCellStyle(cellStyleGreen);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(80)) == -1/*
															 * .compareTo(dataA)
															 * == -1
															 */) {
									contentCell.setCellStyle(cellStyleRED);
								}

								// }
							}

						}

					} else {
						contentCell.setCellValue("");
					}

					if (d == dataListTwo.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}
				}
			}

			for (int d = 0; d < dataListThree.size(); d++) {
				HashMap<String, Object> dataMap = dataListThree.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 1 + rows_max + 13
						+ dataListTwo.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// scell.setEncoding(HSSFCell.ENCODING_UTF_16);

				// 定义单元格为字符串类型
				scell.setCellType(Cell.CELL_TYPE_NUMERIC);
				// scell.setCellValue(d+1); // 序号
				// 创建列
				for (int c = 0; c < fieldsThree.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsThree[c]) != null
							&& dataMap.get(fieldsThree[c]).toString().length() > 0

					) {
						// 判断data是否为数值型
						isNum = dataMap.get(fieldsThree[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsThree[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsThree[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsThree[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsThree[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsThree[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsThree[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsThree[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap
									.get(fieldsThree[c]).toString());
						}
						/*
						 * if (c == 8) {
						 * contentCell.setCellStyle(cellStyleYellow); } if
						 * (dataMap.get(fieldsThree[c]).toString().contains("-")
						 * && d != dataListThree.size() - 1 ) {
						 * contentCell.setCellStyle(cellStyleRED); } else if
						 * (dataMap.get(fieldsThree[c]).toString() .equals("0"))
						 * { contentCell.setCellStyle(cellStyleRED); }else if
						 * (dataMap.get(fieldsThree[c]).toString()
						 * .equals("0%")) {
						 * contentCell.setCellStyle(cellStyleRED); }
						 */
						if (c == 8 && d != dataListThree.size() - 1/* d!=0 */) {
							// HashMap<String, Object> dataMapLast =
							// dataListThree.get(d-1);
							/*
							 * if(dataMapLast.get(fieldsThree[c])!=null &&
							 * dataMapLast
							 * .get(fieldsThree[c]).toString().length() > 0){
							 */
							if (dataMap.get(fieldsThree[c]).toString()
									.contains("%")
							/*
							 * &&
							 * dataMapLast.get(fieldsThree[c]).toString().contains
							 * ("%")
							 */
							) {
								// String a =
								// dataMapLast.get(fieldsThree[c]).toString();
								String b = dataMap.get(fieldsThree[c])
										.toString(); // 去掉% String
								// String tempA = a.substring(0,
								// a.lastIndexOf("%"));
								String tempB = b.substring(0,
										b.lastIndexOf("%")); // 精确表示 Integer
								// Integer dataA = Integer.parseInt(tempA);
								BigDecimal dataB = new BigDecimal(tempB); // 大于为1，相同为0，小于为-1
																			// if
								if (dataB.compareTo(BigDecimal.valueOf(100)) == -1

										&& (dataB.compareTo(BigDecimal
												.valueOf(80)) == 1 || dataB
												.compareTo(BigDecimal
														.valueOf(80)) == 0)
								/*
								 * .compareTo(dataA) == 0
								 */) {
									contentCell.setCellStyle(cellStyleYellow);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(100)) == 1
										|| dataB.compareTo(BigDecimal
												.valueOf(100)) == 1
								/*
								 * dataB >= 100 .compareTo(dataA) == 1
								 */) {
									contentCell.setCellStyle(cellStyleGreen);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(80)) == -1/*
															 * .compareTo(dataA)
															 * == -1
															 */) {
									contentCell.setCellStyle(cellStyleRED);
								}

								// }
							}

						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListThree.size() - 1) {
						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}
				}
			}

			for (int d = 0; d < dataListFour.size(); d++) {
				HashMap<String, Object> dataMap = dataListFour.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 1 + rows_max + 13
						+ dataListTwo.size() + 10 + dataListThree.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// scell.setEncoding(HSSFCell.ENCODING_UTF_16);

				// 定义单元格为字符串类型
				scell.setCellType(Cell.CELL_TYPE_NUMERIC);
				// scell.setCellValue(d+1); // 序号
				// 创建列
				for (int c = 0; c < fieldsFour.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsFour[c]) != null
							&& dataMap.get(fieldsFour[c]).toString().length() > 0

					) {
						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFour[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFour[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFour[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsFour[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsFour[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsFour[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFour[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFour[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsFour[c])
									.toString());
						}
						/*
						 * if
						 * (dataMap.get(fieldsFour[c]).toString().contains("-")
						 * && d != dataListFour.size() - 1 ) {
						 * contentCell.setCellStyle(cellStyleRED); } else if
						 * (dataMap.get(fieldsFour[c]).toString() .equals("0"))
						 * { contentCell.setCellStyle(cellStyleRED); }else if
						 * (dataMap.get(fieldsFour[c]).toString() .equals("0%"))
						 * { contentCell.setCellStyle(cellStyleRED); } if (c ==
						 * 8) { contentCell.setCellStyle(cellStyleYellow); }
						 */
						if (c == 8 && d != dataListFour.size() - 1/* d!=0 */) {
							// HashMap<String, Object> dataMapLast =
							// dataListFour.get(d-1);
							/*
							 * if(dataMapLast.get(fieldsFour[c])!=null
							 * 
							 * &&
							 * dataMapLast.get(fieldsFour[c]).toString().length
							 * () > 0){
							 */
							if (dataMap.get(fieldsFour[c]).toString()
									.contains("%")
							/*
							 * &&
							 * dataMapLast.get(fieldsFour[c]).toString().contains
							 * ("%")
							 */
							) {
								

								// String a =
								// dataMapLast.get(fieldsFour[c]).toString();
								String b = dataMap.get(fieldsFour[c])
										.toString(); // 去掉% String
								// String tempA = a.substring(0,
								// a.lastIndexOf("%"));
								String tempB = b.substring(0,
										b.lastIndexOf("%")); // 精确表示 Integer
								// Integer dataA = Integer.parseInt(tempA);
								BigDecimal dataB = new BigDecimal(tempB); // 大于为1，相同为0，小于为-1
								// if
								if (dataB.compareTo(BigDecimal.valueOf(100)) == -1

										&& (dataB.compareTo(BigDecimal
												.valueOf(80)) == 1 || dataB
												.compareTo(BigDecimal
														.valueOf(80)) == 0)
								/*
								 * .compareTo(dataA) == 0
								 */) {
									contentCell.setCellStyle(cellStyleYellow);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(100)) == 1
										|| dataB.compareTo(BigDecimal
												.valueOf(100)) == 1
								/*
								 * dataB >= 100 .compareTo(dataA) == 1
								 */) {
									contentCell.setCellStyle(cellStyleGreen);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(80)) == -1/*
															 * .compareTo(dataA)
															 * == -1
															 */) {
									contentCell.setCellStyle(cellStyleRED);
								}

								// }
							}

						}

					} else {
						contentCell.setCellValue("");
					}

					if (d == dataListFour.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void comparative(SXSSFWorkbook wb, String partyName,
			String beginDateOne, String endDateOne,String tBeginDate,String tEndDate) throws ParseException {
		String beginDate = beginDateOne;
		String endDate = endDateOne;
		String[] days = beginDateOne.split("-");
		Date timeb = format.parse(beginDateOne);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(timeb);
		Date daysOne = format.parse(format.format(rightNow.getTime()));

		String searchStr = null;
		String conditions = "";
		String center = "";
		String country = "";
		String region = "";
		String office = "";

		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if (!WebPageUtil.isHAdmin()) {
			if (request.getParameter("center") != null
					&& !request.getParameter("center").equals("")
					|| request.getParameter("country") != null
					&& !request.getParameter("country").equals("")
					|| request.getParameter("region") != null
					&& !request.getParameter("region").equals("")
					|| request.getParameter("office") != null
					&& !request.getParameter("office").equals("")) {

				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")) {
					center = request.getParameter("center");
					conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
							+ center + "')   ";
				}

				if (request.getParameter("country") != null
						&& !request.getParameter("country").equals("")) {
					country = request.getParameter("country");
					conditions = "  pa.country_id= " + country + "  ";
				}
				if (request.getParameter("region") != null
						&& !request.getParameter("region").equals("")) {
					region = request.getParameter("region");
					conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
							+ region
							+ "'  OR PARTY_ID='"+region+"'))  ";
				}
				if (request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {
					office = request.getParameter("office");
					conditions = "    pa.party_id IN ('" + office + "')  ";
				}

			} else {
				if (null != userPartyIds && !"".equals(userPartyIds)) {
					conditions = "  pa.party_id in (" + userPartyIds + ")  ";
				} else {
					conditions = "  1=2  ";
				}
			}

		} else {
			conditions = " 1=1 ";

		}
		
		
		if (request.getParameter("level") != null
				&& !request.getParameter("level").equals("")) {
			conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
		}
		// 表头数据
		String[] headers = {};
		// 用于放置表格数据

		for (int r = 1; r <= 8; r++) {
			DateUtil.year = Integer.parseInt(days[0]) - 1;
			DateUtil.month = Integer.parseInt(days[1]);
			String lastDay = DateUtil.getLastDayOfMonth(DateUtil.year,
					DateUtil.month);
			String[] lastDays = lastDay.split("-");
			
			if (r == 1) {
				headers = insert(headers, sdf.format(daysOne) + "."
						+ DateUtil.year);
			}
			if (r == 7) {
				headers = insert(headers, "Total");
			} else if (r <= 5) {
				DateUtil.setWeekend(r);
				DateUtil.count();
				String s = "";
				String e = "";
				if (String.valueOf(DateUtil.getStart()).length() == 1) {
					s = "0" + DateUtil.getStart();
				} else {
					s = DateUtil.getStart() + "";
				}
				if (String.valueOf(DateUtil.getEnd()).length() == 1) {
					e = "0" + DateUtil.getEnd();
				} else {
					e = DateUtil.getEnd() + "";
				}
				if(r==5 &&  !e.equals(lastDays[2])){
					e=lastDays[2];
				}
				
				beginDate = DateUtil.year + "-" + days[1] + "-" + s;
				endDate = DateUtil.year + "-" + days[1] + "-" + e;
				
				
				if (lastDays[2].equals(s)) {
					beginDate = DateUtil.year + "-" + days[1] + "-" + s;
					endDate = DateUtil.year + "-" + days[1] + "-" + s;
					headers = insert(headers, "W" + r + "(" + s + ")");
				} else {
					headers = insert(headers, "W" + r + "(" + s + "-" + e + ")");
				}

			}
			if (r == 8) {
				headers = insert(headers, "Amount");
			}

		}

		// 按照对应格式将表头传入
		ArrayList columns = new ArrayList();
		for (int i = 0; i < headers.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headers[i]);
			columnMap.put("field", headers[i]);
			columns.add(columnMap);

		}

		String[] header = new String[columns.size()];
		String[] fields = new String[columns.size()];
		for (int i = 0, l = columns.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columns.get(i);
			header[i] = columnMap.get("header").toString();
			fields[i] = columnMap.get("field").toString();

		}

		String[] headersTwo = { "REGIONAL sell-out performances_RANK",
				"REGIONAL sell-out performances_REGIONAL HEAD",
				"REGIONAL sell-out performances_AREA" };

		String[] toYear = endDateOne.split("-");

		Date timec = format.parse(beginDateOne);
		Calendar rightNowc = Calendar.getInstance();
		rightNowc.setTime(timec);
		Date toYearOne = format.parse(format.format(rightNowc.getTime()));

		int laYear = Integer.parseInt(toYear[0].toString()) - 1;
		headersTwo = insert(headersTwo, "FPS_Yr-" + laYear);
		headersTwo = insert(headersTwo, "FPS_Yr-" + toYear[0]);
		String laDays = DateUtil.getLastDayOfMonth(laYear,
				Integer.parseInt(toYear[1]));
		String[] laDay = laDays.split("-");
		headersTwo = insert(headersTwo,
				"Total Flat Panel TV Quantity_" + sdf.format(toYearOne)
						+ ". 01-" + laDay[2] + "," + laYear);
		headersTwo = insert(headersTwo,
				"Total Flat Panel TV Quantity_" + sdf.format(toYearOne)
						+ ". 01-" + toYear[2] + "," + toYear[0]);
		headersTwo = insert(headersTwo,
				"Total Flat Panel TV Quantity_SO Growth/day");
		headersTwo = insert(headersTwo, "Total Amount_" + sdf.format(toYearOne)
				+ ". 01-" + laDay[2] + "," + laYear);
		headersTwo = insert(headersTwo, "Total Amount_" + sdf.format(toYearOne)
				+ ". 01-" + toYear[2] + "," + toYear[0]);
		headersTwo = insert(headersTwo, "Total Amount_SO Growth/day");
		headersTwo = insert(headersTwo, "Average sellout per fps_" + laYear
				+ " Ave.qty/fps");
		headersTwo = insert(headersTwo, "Average sellout per fps_" + toYear[0]
				+ " Ave.qty/fps");
		headersTwo = insert(headersTwo, "Average sellout per fps_" + laYear
				+ " Ave.amt/fps");
		headersTwo = insert(headersTwo, "Average sellout per fps_" + toYear[0]
				+ " Ave.amt/fps");

		// 按照对应格式将表头传入
		ArrayList columnsTwo = new ArrayList();
		for (int i = 0; i < headersTwo.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersTwo[i]);
			columnMap.put("field", headersTwo[i]);
			columnsTwo.add(columnMap);

		}

		String[] headerTwo = new String[columnsTwo.size()];
		String[] fieldsTwo = new String[columnsTwo.size()];
		for (int i = 0, l = columnsTwo.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsTwo.get(i);
			headerTwo[i] = columnMap.get("header").toString();
			fieldsTwo[i] = columnMap.get("field").toString();

		}

		String[] headersThree = { "SALESMAN sell-out performances_RANK",
				"SALESMAN sell-out performances_SALESMAN",
				"SALESMAN sell-out performances_REGION" };

		headersThree = insert(headersThree, "FPS_Yr-" + laYear);
		headersThree = insert(headersThree, "FPS_Yr-" + toYear[0]);
		headersThree = insert(headersThree, "Total Flat Panel TV Quantity_"
				+ sdf.format(toYearOne) + ". 01-" + laDay[2] + "," + laYear);
		headersThree = insert(headersThree, "Total Flat Panel TV Quantity_"
				+ sdf.format(toYearOne) + ". 01-" + toYear[2] + "," + toYear[0]);
		headersThree = insert(headersThree,
				"Total Flat Panel TV Quantity_SO Growth/day");
		headersThree = insert(headersThree,
				"Total Amount_" + sdf.format(toYearOne) + ". 01-" + laDay[2]
						+ "," + laYear);
		headersThree = insert(headersThree,
				"Total Amount_" + sdf.format(toYearOne) + ". 01-" + toYear[2]
						+ "," + toYear[0]);
		headersThree = insert(headersThree, "Total Amount_SO Growth/day");
		headersThree = insert(headersThree, "Average sellout per fps_" + laYear
				+ " Ave.qty/fps");
		headersThree = insert(headersThree, "Average sellout per fps_"
				+ toYear[0] + " Ave.qty/fps");
		headersThree = insert(headersThree, "Average sellout per fps_" + laYear
				+ " Ave.amt/fps");
		headersThree = insert(headersThree, "Average sellout per fps_"
				+ toYear[0] + " Ave.amt/fps");

		// 按照对应格式将表头传入
		ArrayList columnsThree = new ArrayList();
		for (int i = 0; i < headersThree.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersThree[i]);
			columnMap.put("field", headersThree[i]);
			columnsThree.add(columnMap);

		}

		String[] headerThree = new String[columnsThree.size()];
		String[] fieldsThree = new String[columnsThree.size()];
		for (int i = 0, l = columnsThree.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsThree.get(i);
			headerThree[i] = columnMap.get("header").toString();
			fieldsThree[i] = columnMap.get("field").toString();

		}

		String[] headersFour = { "ACFO sell-out performances_RANK",
				"ACFO sell-out performances_ACFO",
				"ACFO sell-out performances_REGION" };

		headersFour = insert(headersFour, "FPS_Yr-" + laYear);
		headersFour = insert(headersFour, "FPS_Yr-" + toYear[0]);
		headersFour = insert(headersFour,
				"Total Flat Panel TV Quantity_" + sdf.format(toYearOne)
						+ ". 01-" + laDay[2] + "," + laYear);
		headersFour = insert(headersFour,
				"Total Flat Panel TV Quantity_" + sdf.format(toYearOne)
						+ ". 01-" + toYear[2] + "," + toYear[0]);
		headersFour = insert(headersFour,
				"Total Flat Panel TV Quantity_SO Growth/day");
		headersFour = insert(headersFour,
				"Total Amount_" + sdf.format(toYearOne) + ". 01-" + laDay[2]
						+ "," + laYear);
		headersFour = insert(headersFour,
				"Total Amount_" + sdf.format(toYearOne) + ". 01-" + toYear[2]
						+ "," + toYear[0]);
		headersFour = insert(headersFour, "Total Amount_SO Growth/day");
		headersFour = insert(headersFour, "Average sellout per fps_" + laYear
				+ " Ave.qty/fps");
		headersFour = insert(headersFour, "Average sellout per fps_"
				+ toYear[0] + " Ave.qty/fps");
		headersFour = insert(headersFour, "Average sellout per fps_" + laYear
				+ " Ave.amt/fps");
		headersFour = insert(headersFour, "Average sellout per fps_"
				+ toYear[0] + " Ave.amt/fps");

		// 按照对应格式将表头传入
		ArrayList columnsFour = new ArrayList();
		for (int i = 0; i < headersFour.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersFour[i]);
			columnMap.put("field", headersFour[i]);
			columnsFour.add(columnMap);

		}

		String[] headerFour = new String[columnsFour.size()];
		String[] fieldsFour = new String[columnsFour.size()];
		for (int i = 0, l = columnsFour.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsFour.get(i);
			headerFour[i] = columnMap.get("header").toString();
			fieldsFour[i] = columnMap.get("field").toString();

		}

		String[] headersFive = { "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_Category"
		// "YTD-" + toYear[0] + "  Monthly sellout trend per size_TTL"
		};

		if (toYear[1].equals("01")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_Jan");
		} else if (toYear[1].equals("02")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_Feb");
		} else if (toYear[1].equals("03")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_Mar");
		} else if (toYear[1].equals("04")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_Apr");
		} else if (toYear[1].equals("05")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_May");
		} else if (toYear[1].equals("06")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_June");
		} else if (toYear[1].equals("07")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_July");
		} else if (toYear[1].equals("08")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_August");
		} else if (toYear[1].equals("09")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_September");
		} else if (toYear[1].equals("10")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_October");
		} else if (toYear[1].equals("11")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_November");
		} else if (toYear[1].equals("12")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_December");
		}

		// 按照对应格式将表头传入
		ArrayList columnsFive = new ArrayList();
		for (int i = 0; i < headersFive.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersFive[i]);
			columnMap.put("field", headersFive[i]);
			columnsFive.add(columnMap);

		}

		String[] headerFive = new String[columnsFive.size()];
		String[] fieldsFive = new String[columnsFive.size()];
		for (int i = 0, l = columnsFive.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsFive.get(i);
			headerFive[i] = columnMap.get("header").toString();
			fieldsFive[i] = columnMap.get("field").toString();

		}

		String[] headersSix = { "Market Share_Category"
		// "Market Share_TTL"

		};
		if (toYear[1].equals("01")) {
			headersSix = insert(headersSix, "Market Share_Jan");
		} else if (toYear[1].equals("02")) {
			headersSix = insert(headersSix, "Market Share_Feb");
		} else if (toYear[1].equals("03")) {
			headersSix = insert(headersSix, "Market Share_Mar");
		} else if (toYear[1].equals("04")) {
			headersSix = insert(headersSix, "Market Share_Apr");
		} else if (toYear[1].equals("05")) {
			headersSix = insert(headersSix, "Market Share_May");
		} else if (toYear[1].equals("06")) {
			headersSix = insert(headersSix, "Market Share_June");
		} else if (toYear[1].equals("07")) {
			headersSix = insert(headersSix, "Market Share_July");
		} else if (toYear[1].equals("08")) {
			headersSix = insert(headersSix, "Market Share_August");
		} else if (toYear[1].equals("09")) {
			headersSix = insert(headersSix, "Market Share_September");
		} else if (toYear[1].equals("10")) {
			headersSix = insert(headersSix, "Market Share_October");
		} else if (toYear[1].equals("11")) {
			headersSix = insert(headersSix, "Market Share_November");
		} else if (toYear[1].equals("12")) {
			headersSix = insert(headersSix, "Market Share_December");
		}

		// 按照对应格式将表头传入
		ArrayList columnsSix = new ArrayList();
		for (int i = 0; i < headersSix.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersSix[i]);
			columnMap.put("field", headersSix[i]);
			columnsSix.add(columnMap);

		}

		String[] headerSix = new String[columnsSix.size()];
		String[] fieldsSix = new String[columnsSix.size()];
		for (int i = 0, l = columnsSix.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsSix.get(i);
			headerSix[i] = columnMap.get("header").toString();
			fieldsSix[i] = columnMap.get("field").toString();

		}

		String[] headersEight = { "Different catgory sell-out quantity_Category"

		// "Different catgory sell-out quantity_TTL"

		};

		if (toYear[1].equals("01")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_Jan");
		} else if (toYear[1].equals("02")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_Feb");
		} else if (toYear[1].equals("03")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_Mar");
		} else if (toYear[1].equals("04")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_Apr");
		} else if (toYear[1].equals("05")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_May");
		} else if (toYear[1].equals("06")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_June");
		} else if (toYear[1].equals("07")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_July");
		} else if (toYear[1].equals("08")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_August");
		} else if (toYear[1].equals("09")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_September");
		} else if (toYear[1].equals("10")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_October");
		} else if (toYear[1].equals("11")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_November");
		} else if (toYear[1].equals("12")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_December");
		}

		// 按照对应格式将表头传入
		ArrayList columnsEight = new ArrayList();
		for (int i = 0; i < headersEight.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersEight[i]);
			columnMap.put("field", headersEight[i]);
			columnsEight.add(columnMap);

		}

		String[] headerEight = new String[columnsEight.size()];
		String[] fieldsEight = new String[columnsEight.size()];
		for (int i = 0, l = columnsEight.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsEight.get(i);
			headerEight[i] = columnMap.get("header").toString();
			fieldsEight[i] = columnMap.get("field").toString();

		}

		String[] headersNight = { "Growth rate_Category"

		// "Growth rate_TTL"

		};

		if (toYear[1].equals("01")) {
			headersNight = insert(headersNight, "Growth rate_Jan");
		} else if (toYear[1].equals("02")) {
			headersNight = insert(headersNight, "Growth rate_Feb");
		} else if (toYear[1].equals("03")) {
			headersNight = insert(headersNight, "Growth rate_Mar");
		} else if (toYear[1].equals("04")) {
			headersNight = insert(headersNight, "Growth rate_Apr");
		} else if (toYear[1].equals("05")) {
			headersNight = insert(headersNight, "Growth rate_May");
		} else if (toYear[1].equals("06")) {
			headersNight = insert(headersNight, "Growth rate_June");
		} else if (toYear[1].equals("07")) {
			headersNight = insert(headersNight, "Growth rate_July");
		} else if (toYear[1].equals("08")) {
			headersNight = insert(headersNight, "Growth rate_August");
		} else if (toYear[1].equals("09")) {
			headersNight = insert(headersNight, "Growth rate_September");
		} else if (toYear[1].equals("10")) {
			headersNight = insert(headersNight, "Growth rate_October");
		} else if (toYear[1].equals("11")) {
			headersNight = insert(headersNight, "Growth rate_November");
		} else if (toYear[1].equals("12")) {
			headersNight = insert(headersNight, "Growth rate_December");
		}

		// 按照对应格式将表头传入
		ArrayList columnsNight = new ArrayList();
		for (int i = 0; i < headersNight.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersNight[i]);
			columnMap.put("field", headersNight[i]);
			columnsNight.add(columnMap);

		}

		String[] headerNight = new String[columnsNight.size()];
		String[] fieldsNight = new String[columnsNight.size()];
		for (int i = 0, l = columnsNight.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsNight.get(i);
			headerNight[i] = columnMap.get("header").toString();
			fieldsNight[i] = columnMap.get("field").toString();

		}

		String[] headersTen = { "DEALER" };

		for (int r = 1; r <= 7; r++) {
			DateUtil.year = Integer.parseInt(days[0]);
			DateUtil.month = Integer.parseInt(days[1]);

			if (r == 6) {
				headersTen = insert(headersTen, "Total");
			} else if (r <= 5) {
				DateUtil.setWeekend(r);
				DateUtil.count();
				String s = "";
				String e = "";
				if (String.valueOf(DateUtil.getStart()).length() == 1) {
					s = "0" + DateUtil.getStart();
				} else {
					s = DateUtil.getStart() + "";
				}
				if (String.valueOf(DateUtil.getEnd()).length() == 1) {
					e = "0" + DateUtil.getEnd();
				} else {
					e = DateUtil.getEnd() + "";
				}
				beginDate = DateUtil.year + "-" + days[1] + "-" + s;
				endDate = DateUtil.year + "-" + days[1] + "-" + e;
				String lastDay = DateUtil.getLastDayOfMonth(DateUtil.year,
						DateUtil.month);
				String[] lastDays = lastDay.split("-");
				if (lastDays[2].equals(s)) {
					beginDate = DateUtil.year + "-" + days[1] + "-" + s;
					endDate = DateUtil.year + "-" + days[1] + "-" + s;
					headersTen = insert(headersTen, "W" + r + "(" + s + ")");
				} else {
					headersTen = insert(headersTen, "W" + r + "(" + s + "-" + e
							+ ")");
				}

			}
			if (r == 7) {
				headersTen = insert(headersTen, "Ave So per day");
			}

		}

		// 按照对应格式将表头传入
		ArrayList columnsTen = new ArrayList();
		for (int i = 0; i < headersTen.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersTen[i]);
			columnMap.put("field", headersTen[i]);
			columnsTen.add(columnMap);

		}

		String[] headerTen = new String[columnsTen.size()];
		String[] fieldsTen = new String[columnsTen.size()];
		for (int i = 0, l = columnsTen.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsTen.get(i);
			headerTen[i] = columnMap.get("header").toString();
			fieldsTen[i] = columnMap.get("field").toString();

		}
		long start = System.currentTimeMillis();
		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
		String keys = "";
		for (int r = 1; r <= 6; r++) {

			HashMap<String, Object> data = new HashMap<String, Object>();

			if (r == 1) {
				for (int i = 1; i <= 7; i++) {
					DateUtil.year = Integer.parseInt(days[0]) - 1;
					DateUtil.month = Integer.parseInt(days[1]);
					String lastDay = DateUtil.getLastDayOfMonth(DateUtil.year,
							DateUtil.month);
					String[] lastDays = lastDay.split("-");
					if (i == 6 || i == 7) {
						beginDate = DateUtil.year + "-" + days[1] + "-"
								+ days[2];
						endDate = DateUtil.year + "-" + days[1] + "-"
								+ lastDays[2];
					} else if (i < 6) {
						DateUtil.setWeekend(i);
						DateUtil.count();
						String s = "";
						String e = "";
						if (String.valueOf(DateUtil.getStart()).length() == 1) {
							s = "0" + DateUtil.getStart();
						} else {
							s = DateUtil.getStart() + "";
						}
						if (String.valueOf(DateUtil.getEnd()).length() == 1) {
							e = "0" + DateUtil.getEnd();
						} else {
							e = DateUtil.getEnd() + "";
						}
						if(i==5 &&  !e.equals(lastDay.split("-")[2])){
							e=lastDay.split("-")[2];
						}
						
						if (lastDays[2].equals(s)) {
							beginDate = DateUtil.year + "-" + days[1] + "-" + s;
							endDate = DateUtil.year + "-" + days[1] + "-" + s;
							keys = "W" + i + "(" + s + ")";
						} else {
							keys = "W" + i + "(" + s + "-" + e + ")";
							beginDate = DateUtil.year + "-" + days[1] + "-" + s;
							endDate = DateUtil.year + "-" + days[1] + "-" + e;
						}

					}

					data.put(sdf.format(daysOne) + "." + DateUtil.year,
							"TTL QTY");
					List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
							beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());

					if (sumDatas.size() > 0) {
						data.put("Total", sumDatas.get(0).getSaleQty());
						BigDecimal bd = new BigDecimal(sumDatas.get(0)
								.getSaleSum());
						long lnum = Math.round(bd.doubleValue());
						data.put("Amount", lnum);
					}

					data.put(keys, sumDatas.get(0).getSaleQty());
				}
			} else if (r == 2) {
				start = System.currentTimeMillis();
				int day = 0;
				data.put(sdf.format(daysOne) + "." + DateUtil.year, "Ave/day");
				for (int i = 1; i <= 7; i++) {
					DateUtil.year = Integer.parseInt(days[0]) - 1;
					DateUtil.month = Integer.parseInt(days[1]);
					String lastDay = DateUtil.getLastDayOfMonth(DateUtil.year,
							DateUtil.month);
					String[] lastDays = lastDay.split("-");
					if (i == 6 || i == 7) {
						beginDate = DateUtil.year + "-" + days[1] + "-"
								+ days[2];
						endDate = DateUtil.year + "-" + lastDays[1] + "-"
								+ lastDays[2];
						day = Integer.parseInt(lastDays[2])
								- Integer.parseInt(days[2]) + 1;
					} else if (i < 6) {
						DateUtil.setWeekend(i);
						DateUtil.count();
						String s = "";
						String e = "";
						if (String.valueOf(DateUtil.getStart()).length() == 1) {
							s = "0" + DateUtil.getStart();
						} else {
							s = DateUtil.getStart() + "";
						}
						if (String.valueOf(DateUtil.getEnd()).length() == 1) {
							e = "0" + DateUtil.getEnd();
						} else {
							e = DateUtil.getEnd() + "";
						}
						if(i==5 &&  !e.equals(lastDay.split("-")[2])){
							e=lastDay.split("-")[2];
						}
						
						day = Integer.parseInt(e) - DateUtil.getStart() + 1;
						if (lastDays[2].equals(s)) {
							beginDate = DateUtil.year + "-" + days[1] + "-" + s;
							endDate = DateUtil.year + "-" + days[1] + "-" + s;
							keys = "W" + i + "(" + s + ")";
						} else {
							keys = "W" + i + "(" + s + "-" + e + ")";
							beginDate = DateUtil.year + "-" + days[1] + "-" + s;
							endDate = DateUtil.year + "-" + days[1] + "-" + e;
						}
					}

					List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
							beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());
					if (!sumDatas.get(0).getSaleQty().equals("0") && day != 0) {

						data.put("Total",
								Math.round(Double.parseDouble(sumDatas.get(0).getSaleQty())
										/ day));
					} else {
						data.put("Total", 0);
					}
					if (!sumDatas.get(0).getSaleSum().equals("")
							&& !sumDatas.get(0).getSaleSum().equals("0")
							&& sumDatas.get(0).getSaleSum() != null && day != 0) {
						data.put(
								"Amount",
								Math.round( Double.parseDouble(sumDatas.get(0)
										.getSaleSum()) / day));
					} else {
						data.put("Amount", 0);
					}
					if (Long.parseLong(sumDatas.get(0).getSaleQty()) != 0
							&& day != 0) {
						data.put(keys,
								Math.round(Double.parseDouble(sumDatas.get(0).getSaleQty())
										/ day));
					} else {
						data.put(keys, 0);
					}
				}

			} else if (r == 3) {
				start = System.currentTimeMillis();
				for (int i = 1; i <= 7; i++) {
					DateUtil.year = Integer.parseInt(days[0]) - 1;
					DateUtil.month = Integer.parseInt(days[1]);
					int lastYear = DateUtil.year;
					if (i == 6) {
						data.put("Total", "Total");
					} else if (i == 7) {
						data.put("Amount", "Amount");
					} else if (i < 6) {
						DateUtil.setWeekend(i);
						DateUtil.count();
						String lastDay = DateUtil.getLastDayOfMonth(
								DateUtil.year, DateUtil.month);
						String[] lastDays = lastDay.split("-");
						String s = "";
						String e = "";
						if (String.valueOf(DateUtil.getStart()).length() == 1) {
							s = "0" + DateUtil.getStart();
						} else {
							s = DateUtil.getStart() + "";
						}
						if (String.valueOf(DateUtil.getEnd()).length() == 1) {
							e = "0" + DateUtil.getEnd();
						} else {
							e = DateUtil.getEnd() + "";
						}
						if(i==5 &&  !e.equals(lastDay.split("-")[2])){
							e=lastDay.split("-")[2];
						}
						if (lastDays[2].equals(s)) {
							keys = "W" + i + "(" + s + ")";
						} else {
							keys = "W" + i + "(" + s + "-" + e + ")";
						}
						DateUtil.year = Integer.parseInt(days[0]);
						DateUtil.month = Integer.parseInt(days[1]);
						DateUtil.setWeekend(i);
						DateUtil.count();
						String last = DateUtil.getLastDayOfMonth(DateUtil.year,
								DateUtil.month);
						String[] la = last.split("-");
						String value = "";
						data.put(sdf.format(daysOne) + "." + lastYear,
								sdf.format(daysOne) + "." + DateUtil.year);
						if (String.valueOf(DateUtil.getStart()).length() == 1) {
							s = "0" + DateUtil.getStart();
						} else {
							s = DateUtil.getStart() + "";
						}
						if (String.valueOf(DateUtil.getEnd()).length() == 1) {
							e = "0" + DateUtil.getEnd();
						} else {
							e = DateUtil.getEnd() + "";
						}

						if(i==5 &&  !e.equals(endDateOne.split("-")[2])){
							e=endDateOne.split("-")[2];
						}
						if (la[2].equals(DateUtil.getStart())) {
							value = "W" + i + "(" + s + ")";
						} else {
							value = "W" + i + "(" + s + "-" + e + ")";
						}
						
						
						data.put(keys, value);

					}

				}

			} else if (r == 4) {
				start = System.currentTimeMillis();
				for (int i = 1; i <= 7; i++) {
					DateUtil.year = Integer.parseInt(days[0]) - 1;
					DateUtil.month = Integer.parseInt(days[1]);
					data.put(sdf.format(daysOne) + "." + DateUtil.year,
							"TTL QTY");
					if (i == 6 || i == 7) {
						beginDate = beginDateOne;
						endDate = endDateOne;
						List<Excel> sumDatas = excelService
								.selectSaleDataBySumByAc(beginDate, endDate,
										searchStr, conditions,WebPageUtil.isHQRole());
						data.put("Total",
								Long.parseLong(sumDatas.get(0).getSaleQty()));
						data.put("Amount", (long) Double.parseDouble(sumDatas
								.get(0).getSaleSum()));
					} else if (i < 6) {
						DateUtil.setWeekend(i);
						DateUtil.count();
						String lastDay = DateUtil.getLastDayOfMonth(
								DateUtil.year, DateUtil.month);
						String[] lastDays = lastDay.split("-");
						String s = "";
						String e = "";
						if (String.valueOf(DateUtil.getStart()).length() == 1) {
							s = "0" + DateUtil.getStart();
						} else {
							s = DateUtil.getStart() + "";
						}
						if (String.valueOf(DateUtil.getEnd()).length() == 1) {
							e = "0" + DateUtil.getEnd();
						} else {
							e = DateUtil.getEnd() + "";
						}
						if(i==5 &&  !e.equals(lastDay.split("-")[2])){
							e=lastDay.split("-")[2];
						}
						if (lastDays[2].equals(s)) {
							keys = "W" + i + "(" + s + ")";
						} else {
							keys = "W" + i + "(" + s + "-" + e + ")";
						}
						DateUtil.year = Integer.parseInt(days[0]);
						DateUtil.month = Integer.parseInt(days[1]);
						DateUtil.setWeekend(i);
						DateUtil.count();
						String so = "";
						String eo = "";
						if (String.valueOf(DateUtil.getStart()).length() == 1) {
							so = "0" + DateUtil.getStart();
						} else {
							so = DateUtil.getStart() + "";
						}
						if (String.valueOf(DateUtil.getEnd()).length() == 1) {
							eo = "0" + DateUtil.getEnd();
						} else {
							eo = DateUtil.getEnd() + "";
						}
						
						beginDate = DateUtil.year + "-" + days[1] + "-" + so;
						endDate = DateUtil.year + "-" + days[1] + "-" + eo;
						if(i==5 &&  !endDate.equals(endDateOne)){
							endDate=endDateOne;
						}
						
						List<Excel> sumDatas = excelService
								.selectSaleDataBySumByAc(beginDate, endDate,
										searchStr, conditions,WebPageUtil.isHQRole());
						data.put(keys,
								Long.parseLong(sumDatas.get(0).getSaleQty()));
					}

				}

			} else if (r == 5) {
				start = System.currentTimeMillis();
				int day = 0;
				data.put(sdf.format(daysOne) + "." + DateUtil.year, "Ave/day");
				for (int i = 1; i <= 7; i++) {
					DateUtil.year = Integer.parseInt(days[0]) - 1;
					DateUtil.month = Integer.parseInt(days[1]);
					String[] last = endDateOne.split("-");
					day = Integer.parseInt(last[2]) - Integer.parseInt(days[2])
							+ 1;
					if (i == 6 || i == 7) {
						beginDate = beginDateOne;
						endDate = endDateOne;
						List<Excel> sumDatas = excelService
								.selectSaleDataBySumByAc(beginDate, endDate,
										searchStr, conditions,WebPageUtil.isHQRole());
						if (Long.parseLong(sumDatas.get(0).getSaleQty()) != 0
								&& day != 0) {
							data.put(
									"Total",
									Math.round(Double.parseDouble(sumDatas.get(0).getSaleQty())
										/ day));
						} else {
							data.put("Total", 0);
						}
						if (!sumDatas.get(0).getSaleSum().equals("")
								&& !sumDatas.get(0).getSaleSum().equals("0")
								&& sumDatas.get(0).getSaleSum() != null
								&& day != 0) {
							data.put(
									"Amount",
									Math.round(Double.parseDouble(sumDatas.get(0)
											.getSaleSum()) / day));
						} else {
							data.put("Amount", 0);
						}
					} else if (i < 6) {
						DateUtil.setWeekend(i);
						DateUtil.count();
						String lastDay = DateUtil.getLastDayOfMonth(
								DateUtil.year, DateUtil.month);
						String[] lastDays = lastDay.split("-");
						String s = "";
						String e = "";
						if (String.valueOf(DateUtil.getStart()).length() == 1) {
							s = "0" + DateUtil.getStart();
						} else {
							s = DateUtil.getStart() + "";
						}
						if (String.valueOf(DateUtil.getEnd()).length() == 1) {
							e = "0" + DateUtil.getEnd();
						} else {
							e = DateUtil.getEnd() + "";
						}
						if(i==5 &&  !e.equals(lastDay.split("-")[2])){
							e=lastDay.split("-")[2];
						}
						if (lastDays[2].equals(s)) {
							keys = "W" + i + "(" + s + ")";
						} else {
							keys = "W" + i + "(" + s + "-" + e + ")";
						}
						DateUtil.year = Integer.parseInt(days[0]);
						DateUtil.month = Integer.parseInt(days[1]);
						DateUtil.setWeekend(i);
						DateUtil.count();
						String so = "";
						String eo = "";
						if (String.valueOf(DateUtil.getStart()).length() == 1) {
							so = "0" + DateUtil.getStart();
						} else {
							so = DateUtil.getStart() + "";
						}
						if (String.valueOf(DateUtil.getEnd()).length() == 1) {
							eo = "0" + DateUtil.getEnd();
						} else {
							eo = DateUtil.getEnd() + "";
						}
						beginDate = DateUtil.year + "-" + days[1] + "-" + so;
						endDate = DateUtil.year + "-" + days[1] + "-" + eo;

						if(i==5 &&  !endDate.equals(endDateOne)){
							endDate=endDateOne;
						}
						
						
						day = Integer.parseInt(endDate.split("-")[2])- DateUtil.getStart() + 1;
						List<Excel> sumDatas = excelService
								.selectSaleDataBySumByAc(beginDate, endDate,
										searchStr, conditions,WebPageUtil.isHQRole());

						if (Long.parseLong(sumDatas.get(0).getSaleQty()) != 0
								&& day != 0) {
							data.put(
									keys,
									Math.round(Double.parseDouble(sumDatas.get(0).getSaleQty())
											/ day));
						} else {
							data.put(keys, 0);
						}
					}

				}

			} else if (r == 6) {
				start = System.currentTimeMillis();
				data.put(sdf.format(daysOne) + "." + DateUtil.year,
						"Sellout Growth per day");
				data.put("Total", "SUM(I8,-I5)");
				data.put("Amount", "SUM(J8,-J5)");
				for (int i = 1; i <= 7; i++) {
					DateUtil.year = Integer.parseInt(days[0]) - 1;
					DateUtil.month = Integer.parseInt(days[1]);
					String[] last = endDateOne.split("-");
					if (i < 6) {
						DateUtil.setWeekend(i);
						DateUtil.count();
						String lastDay = DateUtil.getLastDayOfMonth(
								DateUtil.year, DateUtil.month);
						String[] lastDays = lastDay.split("-");
						String s = "";
						String e = "";
						if (String.valueOf(DateUtil.getStart()).length() == 1) {
							s = "0" + DateUtil.getStart();
						} else {
							s = DateUtil.getStart() + "";
						}
						if (String.valueOf(DateUtil.getEnd()).length() == 1) {
							e = "0" + DateUtil.getEnd();
						} else {
							e = DateUtil.getEnd() + "";
						}
						if(i==5 &&  !e.equals(lastDay.split("-")[2])){
							e=lastDay.split("-")[2];
						}
						if (lastDays[2].equals(s)) {
							keys = "W" + i + "(" + s + ")";
						} else {
							keys = "W" + i + "(" + s + "-" + e + ")";
						}
						if (i == 1) {
							data.put(keys, "SUM(D8,-D5)");
						} else if (i == 2) {
							data.put(keys, "SUM(E8,-E5)");
						} else if (i == 3) {
							data.put(keys, "SUM(F8,-F5)");
						} else if (i == 4) {
							data.put(keys, "SUM(G8,-G5)");
						} else if (i == 5) {
							data.put(keys, "SUM(H8,-H5)");
						}
					}

				}

			}

			dataList.add(data);

		}
		System.out
				.println("=====第一个查询=" + (System.currentTimeMillis() - start));

		try {

			LinkedList<HashMap<String, Object>> dataListTwo = new LinkedList<HashMap<String, Object>>();

			start = System.currentTimeMillis();
			List<HashMap<String, Object>> toDatas = excelService
					.selectDataByAreaByAc(beginDateOne, endDateOne, searchStr,
							conditions,WebPageUtil.isHQRole());
			List<HashMap<String, Object>> laDatas = excelService
					.selectDataByAreaByAc(laYear + "-" + toYear[1] + "-" + "01",
							laDays, searchStr, conditions,WebPageUtil.isHQRole());

			List<HashMap<String, Object>> areaInfo = excelService
					.selectDataByAreaInfo(searchStr, conditions);

			List<HashMap<String, Object>> res = excelService
					.selectRegionalHeadByParty(searchStr, conditions);

			for (int s = 0; s < areaInfo.size(); s++) {
				double ts = 0.0;
				double ls =0.0;
				double tq = 0.0;
				double lq =0.0;
				int tfps = 0;
				int lfps = 0;
				double co = 0.0;
				BigDecimal bd = null;
				String am = "";

				HashMap<String, Object> data = new HashMap<String, Object>();

				for (int i = 0; i < res.size(); i++) {
					if (areaInfo.get(s).get("partyId")
							.equals(res.get(i).get("PARTY_ID"))) {
						data.put(
								"REGIONAL sell-out performances_REGIONAL HEAD",
								res.get(i).get("userName"));
					}
				}

				// data.put("REGIONAL sell-out performances_RANK", s + 1);
				data.put("REGIONAL sell-out performances_AREA", areaInfo.get(s)
						.get("AREA"));

				for (int j = 0; j < toDatas.size(); j++) {
					if (areaInfo.get(s).get("AREA").toString()
							.equals(toDatas.get(j).get("AREA").toString())) {

						data.put("FPS_Yr-" + toYear[0],
								toDatas.get(j).get("tvFps"));
						if (toDatas.get(j).get("tvFps") == null
								|| toDatas.get(j).get("tvFps").equals("")) {
							tfps = 0;
						} else {
							tfps = Integer.parseInt(toDatas.get(j).get("tvFps")
									.toString());
						}
						BigDecimal bq = new BigDecimal(toDatas.get(j)
								.get("saleQty").toString());

						am = bq.toPlainString();

						data.put(
								"Total Flat Panel TV Quantity_"
										+ sdf.format(toYearOne) + ". 01-"
										+ toYear[2] + "," + toYear[0],
								bq.longValue());
						if (toDatas.get(j).get("saleQty") == null
								|| toDatas.get(j).get("saleQty").equals("")) {
							tq = (long) 0;
						} else {
							tq = bq.longValue();
						}

						bd = new BigDecimal(toDatas.get(j).get("saleSum")
								.toString());

						am = bd.toPlainString();

						data.put("Total Amount_" + sdf.format(toYearOne)
								+ ". 01-" + toYear[2] + "," + toYear[0], am);
						if (toDatas.get(j).get("saleSum") == null
								|| toDatas.get(j).get("saleSum").equals("")) {
							ts = (long) 0;
						} else {
							ts = bd.longValue();
						}

					}

				}
				for (int z = 0; z < laDatas.size(); z++) {
					if (areaInfo.get(s).get("AREA").toString()
							.equals(laDatas.get(z).get("AREA").toString())) {
						if (laDatas.get(z).get("tvFps") == null
								|| laDatas.get(z).get("tvFps").equals("")) {
							lfps = 0;
						} else {
							lfps = Integer.parseInt(laDatas.get(z).get("tvFps")
									.toString());
						}
						data.put("FPS_Yr-" + laYear, laDatas.get(z)
								.get("tvFps"));

						bd = new BigDecimal(laDatas.get(z).get("saleQty")
								.toString());

						data.put(
								"Total Flat Panel TV Quantity_"
										+ sdf.format(toYearOne) + ". 01-"
										+ laDay[2] + "," + laYear,
								bd.longValue());
						if (laDatas.get(z).get("saleQty") == null
								|| laDatas.get(z).get("saleQty").equals("")) {
							lq = (long) 0;
						} else {
							lq = bd.longValue();
						}

						BigDecimal lbd = new BigDecimal(laDatas.get(z)
								.get("saleSum").toString());
						String lam = lbd.toPlainString();
						data.put("Total Amount_" + sdf.format(toYearOne)
								+ ". 01-" + laDay[2] + "," + laYear, lam);
						if (laDatas.get(z).get("saleSum") == null
								|| laDatas.get(z).get("saleSum").equals("")) {
							ls = (long) 0;
						} else {
							ls = lbd.longValue();
						}

					}

				}

				if (tq != 0) {
					co = tq - lq;
					double qg = co / tq * 100;
					long lnum = Math.round(qg);
					data.put("Total Flat Panel TV Quantity_SO Growth/day", lnum
							+ "%");
				}

				if (lfps != 0) {
					double lf = (lq / lfps);
					data.put("Average sellout per fps_" + laYear
							+ " Ave.qty/fps", Math.round(lf));
				}
				if (tfps != 0) {
					double tf = (tq / tfps);
					data.put("Average sellout per fps_" + toYear[0]
							+ " Ave.qty/fps", Math.round(tf));
				}
				if (lfps != 0) {

					double lff =  (ls / lfps);
					data.put("Average sellout per fps_" + laYear
							+ " Ave.amt/fps", Math.round(lff));
				}
				if (tfps != 0) {
					double tff = (ts / tfps);
					data.put("Average sellout per fps_" + toYear[0]
							+ " Ave.amt/fps", Math.round(tff));
				}

				if (ts != 0.0) {
					co = ts - ls;
					double re = co / ts * 100;
					long lnum = Math.round(re);
					data.put("Total Amount_SO Growth/day", lnum + "%");
				}

				dataListTwo.add(data);

			}

			DateUtil.Order(dataListTwo, "Total Amount_SO Growth/day");
			for (int i = 0; i < dataListTwo.size(); i++) {
				dataListTwo.get(i).put("REGIONAL sell-out performances_RANK",
						i + 1);
			}

			HashMap<String, Object> data = new HashMap<String, Object>();
			int rows = 15 + dataListTwo.size();
			int end = rows + 1;
			data.put("REGIONAL sell-out performances_REGIONAL HEAD", "Total");
			data.put("FPS_Yr-" + laYear, "SUM(D16:D" + rows + ")");
			data.put("FPS_Yr-" + toYear[0], "SUM(E16:E" + rows + ")");
			data.put("Total Flat Panel TV Quantity_" + sdf.format(toYearOne)
					+ ". 01-" + laDay[2] + "," + laYear, "SUM(F16:F" + rows
					+ ")");
			data.put("Total Flat Panel TV Quantity_" + sdf.format(toYearOne)
					+ ". 01-" + toYear[2] + "," + toYear[0], "SUM(G16:G" + rows
					+ ")");

			data.put("Total Flat Panel TV Quantity_SO Growth/day", "TEXT((G"
					+ end + "-F" + end + ")/G" + end + ",\"0.00%\")");

			data.put("Total Amount_" + sdf.format(toYearOne) + ". 01-"
					+ laDay[2] + "," + laYear, "SUM(I16:I" + rows + ")");
			data.put("Total Amount_" + sdf.format(toYearOne) + ". 01-"
					+ toYear[2] + "," + toYear[0], "SUM(J16:J" + rows + ")");

			data.put("Total Amount_SO Growth/day", "TEXT((J" + end + "-I" + end
					+ ")/J" + end + ",\"0.00%\")");
			data.put("Average sellout per fps_" + laYear + " Ave.qty/fps",
					"SUM(L16:L" + rows + ")");
			data.put("Average sellout per fps_" + toYear[0] + " Ave.qty/fps",
					"SUM(M16:M" + rows + ")");
			data.put("Average sellout per fps_" + laYear + " Ave.amt/fps",
					"SUM(N16:N" + rows + ")");
			data.put("Average sellout per fps_" + toYear[0] + " Ave.amt/fps",
					"SUM(O16:O" + rows + ")");
			dataListTwo.add(data);

			System.out.println("=====第2个查询="
					+ (System.currentTimeMillis() - start));
			/*
			 * "SALESMAN sell-out performances_RANK",
			 * "SALESMAN sell-out performances_SALESMAN",
			 * "SALESMAN sell-out performances_REGION"
			 */

			LinkedList<HashMap<String, Object>> dataListThree = new LinkedList<HashMap<String, Object>>();

			start = System.currentTimeMillis();
			List<HashMap<String, Object>> toDataBySalesInfo = excelService
					.selectTargetBySalemanInfo(searchStr, conditions);

			List<HashMap<String, Object>> toDataBySales = excelService
					.selectTargetBySalemanByAc(beginDateOne, endDateOne, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			List<HashMap<String, Object>> laDataBysales = excelService
					.selectTargetBySalemanByAc(laYear + "-" + toYear[1] + "-"
							+ "01", laDays, searchStr, conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			List<HashMap<String, Object>> Account = excelService
					.selectPartyNameByuser(searchStr, conditions);

			List<HashMap<String, Object>> toFps = excelService
					.selectFpsNameByShopByAc(beginDateOne, endDateOne, searchStr,
							conditions);

			List<HashMap<String, Object>> laFps = excelService
					.selectFpsNameByShopByAc(laYear + "-" + toYear[1] + "-" + "01",
							laDays, searchStr, conditions);

			for (int w = 0; w < toDataBySalesInfo.size(); w++) {
				double ts = 0.0;
				double ls =0.00;
				double tq = 0.0;
				double lq =0.0;
				int tfps = 0;
				int lfps = 0;
				int tvFps = 0;
				double co = 0.0;
				BigDecimal bd = null;
				String am = "";
				HashMap<String, Object> dataMap = new HashMap<String, Object>();

				// dataMap.put("SALESMAN sell-out performances_RANK", w + 1);
				dataMap.put("SALESMAN sell-out performances_SALESMAN",
						toDataBySalesInfo.get(w).get("userName"));

				for (int j = 0; j < toDataBySales.size(); j++) {
					tvFps = 0;

					if (toDataBySalesInfo
							.get(w)
							.get("userName")
							.toString()
							.equals(toDataBySales.get(j).get("userName")
									.toString())) {
						for (int i = 0; i < toFps.size(); i++) {
							if (toFps.get(i).get("USER")
									.equals(toDataBySales.get(j).get("userId"))) {
								tvFps = Integer.parseInt(toFps.get(i)
										.get("TVFPS").toString());
								dataMap.put("FPS_Yr-" + toYear[0], tvFps);
								tfps = tvFps;

							}
						}

						if (Account.size() > 1) {
							String a = "";
							for (int k = 0; k < Account.size(); k++) {
								if (toDataBySales.get(j).get("userId")
										.equals(Account.get(k).get("userId"))) {
									a += Account.get(k).get("PARTY_NAME") + ",";
								}

							}
							if (a.length() > 0) {
								dataMap.put(
										"SALESMAN sell-out performances_REGION",
										a.substring(0, a.length() - 1));
							}
						}

						bd = new BigDecimal(toDataBySales.get(j).get("saleQty")
								.toString());
						dataMap.put(
								"Total Flat Panel TV Quantity_"
										+ sdf.format(toYearOne) + ". 01-"
										+ toYear[2] + "," + toYear[0],
								bd.longValue());

						tq = bd.longValue();

						bd = new BigDecimal(toDataBySales.get(j).get("saleSum")
								.toString());
						am = bd.toPlainString();
						dataMap.put("Total Amount_" + sdf.format(toYearOne)
								+ ". 01-" + toYear[2] + "," + toYear[0], am);
						ts = bd.longValue();

					}

				}

				for (int s = 0; s < laDataBysales.size(); s++) {

					if (toDataBySalesInfo
							.get(w)
							.get("userName")
							.toString()
							.equals(laDataBysales.get(s).get("userName")
									.toString())) {

						for (int i = 0; i < laFps.size(); i++) {
							if (laFps.get(i).get("USER")
									.equals(laDataBysales.get(s).get("userId"))) {
								tvFps = Integer.parseInt(laFps.get(i)
										.get("TVFPS").toString());
								dataMap.put("FPS_Yr-" + laYear, tvFps);

								lfps = tvFps;

							}
						}

						bd = new BigDecimal(laDataBysales.get(s).get("saleQty")
								.toString());

						dataMap.put(
								"Total Flat Panel TV Quantity_"
										+ sdf.format(toYearOne) + ". 01-"
										+ laDay[2] + "," + laYear,
								bd.longValue());

						lq = bd.longValue();
						bd = new BigDecimal(laDataBysales.get(s).get("saleSum")
								.toString());
						am = bd.toPlainString();
						dataMap.put("Total Amount_" + sdf.format(toYearOne)
								+ ". 01-" + laDay[2] + "," + laYear, am);
						ls = bd.longValue();
					}

				}

				if (tq != 0) {
					co = (tq - lq);
					double qg = co / tq * 100;
					long lnum = Math.round(qg);
					dataMap.put("Total Flat Panel TV Quantity_SO Growth/day",
							lnum + "%");
				}

				if (lfps != 0) {
					double lf = (lq / lfps);
					dataMap.put("Average sellout per fps_" + laYear
							+ " Ave.qty/fps",Math.round(lf));
				}
				if (tfps != 0) {
					double tf = (tq / tfps);
					dataMap.put("Average sellout per fps_" + toYear[0]
							+ " Ave.qty/fps", Math.round(tf));
				}
				if (lfps != 0) {
					double lff = (ls / lfps);
					dataMap.put("Average sellout per fps_" + laYear
							+ " Ave.amt/fps", Math.round(lff));
				}
				if (tfps != 0) {
					double tff = (ts / tfps);
					dataMap.put("Average sellout per fps_" + toYear[0]
							+ " Ave.amt/fps", Math.round(tff));
				}

				if (ts != 0.0) {
					co = (ts - ls);
					double re = co / ts * 100;
					long lnum = Math.round(re);
					dataMap.put("Total Amount_SO Growth/day", lnum + "%");
				}

				dataListThree.add(dataMap);
			}

			DateUtil.Order(dataListThree, "Total Amount_SO Growth/day");
			for (int i = 0; i < dataListThree.size(); i++) {
				dataListThree.get(i).put("SALESMAN sell-out performances_RANK",
						i + 1);
			}
			HashMap<String, Object> dataThree = new HashMap<String, Object>();
			int stratRow = 15 + dataListTwo.size() + 11;

			int rowsBysale = 15 + dataListTwo.size() + 10
					+ +dataListThree.size();
			end = rowsBysale + 1;
			dataThree.put("SALESMAN sell-out performances_SALESMAN", "Total");
			dataThree.put("FPS_Yr-" + laYear, "SUM(D" + stratRow + ":D"
					+ rowsBysale + ")");
			dataThree.put("FPS_Yr-" + toYear[0], "SUM(E" + stratRow + ":E"
					+ rowsBysale + ")");
			dataThree.put(
					"Total Flat Panel TV Quantity_" + sdf.format(toYearOne)
							+ ". 01-" + laDay[2] + "," + laYear, "SUM(F"
							+ stratRow + ":F" + rowsBysale + ")");
			dataThree.put(
					"Total Flat Panel TV Quantity_" + sdf.format(toYearOne)
							+ ". 01-" + toYear[2] + "," + toYear[0], "SUM(G"
							+ stratRow + ":G" + rowsBysale + ")");

			dataThree.put("Total Flat Panel TV Quantity_SO Growth/day",
					"TEXT((G" + end + "-F" + end + ")/G" + end + ",\"0.00%\")");

			dataThree.put("Total Amount_" + sdf.format(toYearOne) + ". 01-"
					+ laDay[2] + "," + laYear, "SUM(I" + stratRow + ":I"
					+ rowsBysale + ")");
			dataThree.put("Total Amount_" + sdf.format(toYearOne) + ". 01-"
					+ toYear[2] + "," + toYear[0], "SUM(J" + stratRow + ":J"
					+ rowsBysale + ")");

			dataThree.put("Total Amount_SO Growth/day", "TEXT((J" + end + "-I"
					+ end + ")/J" + end + ",\"0.00%\")");
			dataThree.put("Average sellout per fps_" + laYear + " Ave.qty/fps",
					"SUM(L" + stratRow + ":L" + rowsBysale + ")");
			dataThree.put("Average sellout per fps_" + toYear[0]
					+ " Ave.qty/fps", "SUM(M" + stratRow + ":M" + rowsBysale
					+ ")");

			dataThree.put("Average sellout per fps_" + laYear + " Ave.amt/fps",
					"SUM(N" + stratRow + ":N" + rowsBysale + ")");
			dataThree.put("Average sellout per fps_" + toYear[0]
					+ " Ave.amt/fps", "SUM(O" + stratRow + ":O" + rowsBysale
					+ ")");
			dataListThree.add(dataThree);

			System.out.println("=====第3个查询="
					+ (System.currentTimeMillis() - start));

			LinkedList<HashMap<String, Object>> dataListFour = new LinkedList<HashMap<String, Object>>();

			start = System.currentTimeMillis();
			List<HashMap<String, Object>> toDataByAcfoInfo = excelService
					.selectTargetByAcfoInfo(searchStr, conditions);

			List<HashMap<String, Object>> toDataByAcfo = excelService
					.selectTargetByAcfoByAc(beginDateOne, endDateOne, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			List<HashMap<String, Object>> laDataByAcfo = excelService
					.selectTargetByAcfoByAc(laYear + "-" + toYear[1] + "-" + "01",
							laDays, searchStr, conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			List<HashMap<String, Object>> Area = excelService.selectAreaByUser(
					searchStr, conditions);

			toFps = excelService.selectFpsNameByShopByAc(beginDateOne, endDateOne,
					searchStr, conditions);
			laFps = excelService.selectFpsNameByShopByAc(laYear + "-" + toYear[1]
					+ "-" + "01", laDays, searchStr, conditions);

			for (int z = 0; z < toDataByAcfoInfo.size(); z++) {
				double ts = 0.0;
				double ls = 0.0;
				double tq = 0.0;
				double lq = 0.0;
				int tfps = 0;
				int lfps = 0;
				double co = 0.0;
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				// dataMap.put("ACFO sell-out performances_RANK", z + 1);

				int tvFps = 0;
				BigDecimal bd = null;
				String am = "";
				for (int j = 0; j < toDataByAcfo.size(); j++) {

					if (toDataByAcfoInfo
							.get(z)
							.get("userName")
							.toString()
							.equals(toDataByAcfo.get(j).get("userName")
									.toString())) {

						dataMap.put("ACFO sell-out performances_ACFO",
								toDataByAcfo.get(j).get("userName"));

						if (Area.size() > 1) {
							String a = "";
							for (int k = 0; k < Area.size(); k++) {
								if (toDataByAcfo.get(j).get("userId")
										.equals(Area.get(k).get("userId"))) {

									a += Area.get(k).get("PARTY_NAME") + ",";
								}

							}
							if (a.length() > 0) {
								dataMap.put(
										"ACFO sell-out performances_REGION",
										a.substring(0, a.length() - 1));
							}

						}

						for (int i = 0; i < toFps.size(); i++) {
							if (toFps.get(i).get("USER")
									.equals(toDataByAcfo.get(j).get("userId"))) {
								tvFps = Integer.parseInt(toFps.get(i)
										.get("TVFPS").toString());
								dataMap.put("FPS_Yr-" + toYear[0], tvFps);
								tfps = tvFps;

							}
						}

						bd = new BigDecimal(toDataByAcfo.get(j).get("saleQty")
								.toString());

						dataMap.put(
								"Total Flat Panel TV Quantity_"
										+ sdf.format(toYearOne) + ". 01-"
										+ toYear[2] + "," + toYear[0],
								bd.longValue());
						tq = bd.longValue();
						bd = new BigDecimal(toDataByAcfo.get(j).get("saleSum")
								.toString());
						am = bd.toPlainString();
						dataMap.put("Total Amount_" + sdf.format(toYearOne)
								+ ". 01-" + toYear[2] + "," + toYear[0], am);
						ts = bd.longValue();
					}

				}

				for (int s = 0; s < laDataByAcfo.size(); s++) {
					if (toDataByAcfoInfo
							.get(z)
							.get("userName")
							.toString()
							.equals(laDataByAcfo.get(s).get("userName")
									.toString())) {

						for (int i = 0; i < laFps.size(); i++) {
							if (laFps.get(i).get("USER")
									.equals(laDataByAcfo.get(s).get("userId"))) {
								tvFps = Integer.parseInt(laFps.get(i)
										.get("TVFPS").toString());
								dataMap.put("FPS_Yr-" + laYear, tvFps);

								lfps = tvFps;

							}
						}

						bd = new BigDecimal(laDataByAcfo.get(s).get("saleQty")
								.toString());

						dataMap.put(
								"Total Flat Panel TV Quantity_"
										+ sdf.format(toYearOne) + ". 01-"
										+ laDay[2] + "," + laYear,
								bd.longValue());
						lq = bd.longValue();
						bd = new BigDecimal(laDataByAcfo.get(s).get("saleSum")
								.toString());
						am = bd.toPlainString();
						dataMap.put("Total Amount_" + sdf.format(toYearOne)
								+ ". 01-" + laDay[2] + "," + laYear, am);
						ls = bd.longValue();
					}

				}
				if (tq != 0) {
					co = (tq - lq);
					double qg = co / tq * 100;
					long lnum = Math.round(qg);
					dataMap.put("Total Flat Panel TV Quantity_SO Growth/day",
							lnum + "%");
				}

				if (lfps != 0) {
					double lf = (lq / lfps);
					dataMap.put("Average sellout per fps_" + laYear
							+ " Ave.qty/fps", Math.round(lf));
				}
				if (tfps != 0) {
					double tf = (tq / tfps);
					dataMap.put("Average sellout per fps_" + toYear[0]
							+ " Ave.qty/fps", Math.round(tf));
				}
				if (lfps != 0) {
					double lff = (ls / lfps);
					dataMap.put("Average sellout per fps_" + laYear
							+ " Ave.amt/fps", Math.round(lff));
				}
				if (tfps != 0) {
					double tff = (ts / tfps);
					dataMap.put("Average sellout per fps_" + toYear[0]
							+ " Ave.amt/fps", Math.round(tff));
				}

				if (ts != 0.0) {
					co = (ts - ls);
					double re = co / ts * 100;
					long lnum = Math.round(re);
					dataMap.put("Total Amount_SO Growth/day", lnum + "%");
				}

				dataListFour.add(dataMap);
			}

			DateUtil.Order(dataListFour, "Total Amount_SO Growth/day");
			for (int i = 0; i < dataListFour.size(); i++) {
				dataListFour.get(i).put("ACFO sell-out performances_RANK",
						i + 1);
			}
			HashMap<String, Object> dataFour = new HashMap<String, Object>();

			stratRow = 15 + dataListTwo.size() + 11 + dataListThree.size() + 10;
			rowsBysale = 15 + dataListTwo.size() + 11 + dataListThree.size()
					+ 9 + dataListFour.size();
			end = rowsBysale + 1;
			dataFour.put("ACFO sell-out performances_ACFO", "Total");
			dataFour.put("FPS_Yr-" + laYear, "SUM(D" + stratRow + ":D"
					+ rowsBysale + ")");
			dataFour.put("FPS_Yr-" + toYear[0], "SUM(E" + stratRow + ":E"
					+ rowsBysale + ")");
			dataFour.put(
					"Total Flat Panel TV Quantity_" + sdf.format(toYearOne)
							+ ". 01-" + laDay[2] + "," + laYear, "SUM(F"
							+ stratRow + ":F" + rowsBysale + ")");
			dataFour.put(
					"Total Flat Panel TV Quantity_" + sdf.format(toYearOne)
							+ ". 01-" + toYear[2] + "," + toYear[0], "SUM(G"
							+ stratRow + ":G" + rowsBysale + ")");
			dataFour.put("Total Flat Panel TV Quantity_SO Growth/day",
					"TEXT((G" + end + "-F" + end + ")/G" + end + ",\"0.00%\")");
			dataFour.put("Total Amount_" + sdf.format(toYearOne) + ". 01-"
					+ laDay[2] + "," + laYear, "SUM(I" + stratRow + ":I"
					+ rowsBysale + ")");
			dataFour.put("Total Amount_" + sdf.format(toYearOne) + ". 01-"
					+ toYear[2] + "," + toYear[0], "SUM(J" + stratRow + ":J"
					+ rowsBysale + ")");
			dataFour.put("Total Amount_SO Growth/day", "TEXT((J" + end + "-I"
					+ end + ")/J" + end + ",\"0.00%\")");
			dataFour.put("Average sellout per fps_" + laYear + " Ave.qty/fps",
					"SUM(L" + stratRow + ":L" + rowsBysale + ")");
			dataFour.put("Average sellout per fps_" + toYear[0]
					+ " Ave.qty/fps", "SUM(M" + stratRow + ":M" + rowsBysale
					+ ")");

			dataFour.put("Average sellout per fps_" + laYear + " Ave.amt/fps",
					"SUM(N" + stratRow + ":N" + rowsBysale + ")");
			dataFour.put("Average sellout per fps_" + toYear[0]
					+ " Ave.amt/fps", "SUM(O" + stratRow + ":O" + rowsBysale
					+ ")");
			dataListFour.add(dataFour);

			System.out.println("=====第4个查询="
					+ (System.currentTimeMillis() - start));

			LinkedList<HashMap<String, Object>> dataListFive = new LinkedList<HashMap<String, Object>>();
			LinkedList<HashMap<String, Object>> dataListSix = new LinkedList<HashMap<String, Object>>();
			start = System.currentTimeMillis();

			String beginSize = "";
			String endSize = "";

			String lastDay = DateUtil.getLastDayOfMonth(
					Integer.parseInt(toYear[0]), 1);
			String[] lastDays = lastDay.split("-");
			String startDay = "";
			String Month = "";
			HashMap<String, Object> dataFive = new HashMap<String, Object>();
			HashMap<String, Object> dataSix = new HashMap<String, Object>();
			long sttt = System.currentTimeMillis();
			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMap = new LinkedHashMap<String, LinkedHashMap<String, Object>>();

			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapSix = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
			
			if (toYear[1].equals("01")) {
				Month = "Jan";
			} else if (toYear[1].equals("02")) {
				Month = "Feb";
			} else if (toYear[1].equals("03")) {
				Month = "Mar";
			} else if (toYear[1].equals("04")) {
				Month = "Apr";
			} else if (toYear[1].equals("05")) {
				Month = "May";
			} else if (toYear[1].equals("06")) {
				Month = "June";
			} else if (toYear[1].equals("07")) {
				Month = "July";
			} else if (toYear[1].equals("08")) {
				Month = "August";
			} else if (toYear[1].equals("09")) {
				Month = "September";
			} else if (toYear[1].equals("10")) {
				Month = "October";
			} else if (toYear[1].equals("11")) {
				Month = "November";
			} else if (toYear[1].equals("12")) {
				Month = "December";
			}
			
				List<LinkedHashMap<String, Object>> sizeDatasOne = excelService
						.selectSaleDataBySizeByAc(beginSize, endSize, beginDateOne,
								endDateOne, searchStr, conditions,WebPageUtil.isHQRole());

				List<LinkedHashMap<String, Object>> sizeDatasOneData = excelService
						.selectSaleTotalBySizeByAc(beginDateOne, endDateOne, searchStr,
								conditions,WebPageUtil.isHQRole());

				for (int a = 0; a < sizeDatasOne.size(); a++) {
					LinkedHashMap<String, Object> colMap = sizeDatasOne.get(a);

					String key = colMap.get("sizeT").toString();

					if (allDataMap.get(key) != null) {
						LinkedHashMap<String, Object> rowMap = allDataMap
								.get(key);

						BigDecimal bd = new BigDecimal(sizeDatasOne.get(a)
								.get("quantity").toString());

						rowMap.put("YTD-" + toYear[0]
								+ "  Monthly sellout trend per size_" + Month
								+ "", bd.longValue());

						allDataMap.put(key, rowMap);

						double one = bd.longValue();

						if (sizeDatasOneData.size() == 1) {

							LinkedHashMap<String, Object> colMapSix = sizeDatasOneData
									.get(0);
							if (allDataMapSix.get(key) != null) {
								LinkedHashMap<String, Object> rowMapSix = allDataMapSix
										.get(key);

								bd = new BigDecimal(sizeDatasOneData.get(0)
										.get("quantity").toString());
								double oneData = bd.longValue();

								double oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapSix.put("Market Share_" + Month + "",
										lnum + "%");
								allDataMapSix.put(key, rowMapSix);

							} else {

								LinkedHashMap<String, Object> rowMapSix = new LinkedHashMap<String, Object>();

								rowMapSix.put("Market Share_Category", key);

								bd = new BigDecimal(sizeDatasOneData.get(0)
										.get("quantity").toString());
								double oneData = bd.longValue();

								double oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapSix.put("Market Share_" + Month + "",
										lnum + "%");
								allDataMapSix.put(key, rowMapSix);
							}

						}
					} else {
						LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();
						rowMap.put("YTD-" + toYear[0]
								+ "  Monthly sellout trend per size_Category",
								key);
						rowMap.put("YTD-" + toYear[0]
								+ "  Monthly sellout trend per size_" + Month
								+ "", key);
						BigDecimal bd = new BigDecimal(sizeDatasOne.get(a)
								.get("quantity").toString());

						rowMap.put("YTD-" + toYear[0]
								+ "  Monthly sellout trend per size_" + Month
								+ "", bd.longValue());

						double one = bd.longValue();

						if (sizeDatasOneData.size() == 1) {

							LinkedHashMap<String, Object> colMapSix = sizeDatasOneData
									.get(0);
							if (allDataMapSix.get(key) != null) {
								LinkedHashMap<String, Object> rowMapSix = allDataMapSix
										.get(key);

								bd = new BigDecimal(sizeDatasOneData.get(0)
										.get("quantity").toString());
								double oneData = bd.longValue();

								double oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapSix.put("Market Share_" + Month + "",
										lnum + "%");
								allDataMapSix.put(key, rowMapSix);

							} else {
								LinkedList<HashMap<String, Object>> rowListSix = new LinkedList<HashMap<String, Object>>();
								rowListSix.addLast(colMapSix);
								LinkedHashMap<String, Object> rowMapSix = new LinkedHashMap<String, Object>();

								rowMapSix.put("Market Share_Category", key);

								bd = new BigDecimal(sizeDatasOneData.get(0)
										.get("quantity").toString());
								double oneData = bd.longValue();

								double oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapSix.put("Market Share_" + Month + "",
										lnum + "%");
								allDataMapSix.put(key, rowMapSix);
							}

						}

						allDataMap.put(key, rowMap);
					}

				}


			Set<String> sizeSet = allDataMap.keySet();

			Iterator<String> sizeIter = sizeSet.iterator();
			HashMap<String, Object> totalMap = new HashMap<String, Object>();
			String total_header = "";
			while (sizeIter.hasNext()) {
				String key = sizeIter.next();
				HashMap<String, Object> rowMap = allDataMap.get(key);
				dataListFive.addLast(rowMap);

				Set<String> rowSet = rowMap.keySet();

				Iterator<String> rowIter = rowSet.iterator();
				while (rowIter.hasNext()) {
					String rheader = rowIter.next();

					if (!rheader.contains("Category")) {
						int quantity = Integer.parseInt(rowMap.get(rheader)
								.toString());
						if (totalMap.get(rheader) == null) {

							totalMap.put(rheader, quantity);
						} else {
							int totalQuantity = Integer.parseInt(totalMap.get(
									rheader).toString());
							totalQuantity += quantity;
							totalMap.put(rheader, totalQuantity);
						}

					} else {
						total_header = rheader;
					}

				}

			}
			totalMap.put(total_header, "total");
			dataListFive.addLast(totalMap);

			Set<String> sizeSetSix = allDataMapSix.keySet();

			Iterator<String> sizeIterSix = sizeSetSix.iterator();
			HashMap<String, Object> totalMapSix = new HashMap<String, Object>();
			while (sizeIterSix.hasNext()) {
				String key = sizeIterSix.next();
				HashMap<String, Object> rowMap = allDataMapSix.get(key);
				dataListSix.addLast(rowMap);

				Set<String> rowSet = rowMap.keySet();
				Iterator<String> rowIter = rowSet.iterator();

				while (rowIter.hasNext()) {
					String rheader = rowIter.next();

					if (!rheader.contains("Category")) {

						if (totalMap.get(rheader) == null) {

							totalMap.put(rheader, "100%");
						} else {
							totalMap.put(rheader, "100%");
						}

					} else {
						total_header = rheader;
					}

				}

			}
			totalMap.put("Market Share_Category", "total");
			dataListSix.addLast(totalMap);

			LinkedList<HashMap<String, Object>> dataListEight = new LinkedList<HashMap<String, Object>>();
			LinkedList<HashMap<String, Object>> dataListNight = new LinkedList<HashMap<String, Object>>();

			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapEight = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapNight = new LinkedHashMap<String, LinkedHashMap<String, Object>>();

			start = System.currentTimeMillis();
			// for (int i = 1; i <= 7; i++) {
			HashMap<String, Object> dataEight = new HashMap<String, Object>();
			HashMap<String, Object> dataNight = new HashMap<String, Object>();
			String spec = "";
			double one = 0.0;
			double oneData = 0.0;
			double oneAch = 0.0;
			if (toYear[1].equals("01")) {
				Month = "Jan";
			} else if (toYear[1].equals("02")) {
				Month = "Feb";
			} else if (toYear[1].equals("03")) {
				Month = "Mar";
			} else if (toYear[1].equals("04")) {
				Month = "Apr";
			} else if (toYear[1].equals("05")) {
				Month = "May";
			} else if (toYear[1].equals("06")) {
				Month = "June";
			} else if (toYear[1].equals("07")) {
				Month = "July";
			} else if (toYear[1].equals("08")) {
				Month = "August";
			} else if (toYear[1].equals("09")) {
				Month = "September";
			} else if (toYear[1].equals("10")) {
				Month = "October";
			} else if (toYear[1].equals("11")) {
				Month = "November";
			} else if (toYear[1].equals("12")) {
				Month = "December";
			}
			
				List<LinkedHashMap<String, Object>> sizeDatasO = excelService
						.selectQtyTotalBySpecByAc(spec, beginDateOne, endDateOne,
								searchStr, conditions,WebPageUtil.isHQRole());
				List<LinkedHashMap<String, Object>> sizeDatasTwTotal = excelService
						.selectSaleTotalBySizeByAc(beginDateOne, endDateOne, searchStr,
								conditions,WebPageUtil.isHQRole());

				for (int a = 0; a < sizeDatasO.size(); a++) {
					LinkedHashMap<String, Object> colMap = sizeDatasO
							.get(a);

					String key = colMap.get("SPEC").toString();

					if (allDataMapEight.get(key) != null) {
						LinkedHashMap<String, Object> rowMap = allDataMapEight
								.get(key);

						BigDecimal bd = new BigDecimal(sizeDatasO.get(a)
								.get("quantity").toString());

						rowMap.put("Different catgory sell-out quantity_"
								+ Month + "", bd.longValue());

						allDataMapEight.put(key, rowMap);

						one = bd.longValue();

						if (sizeDatasTwTotal.size() == 1) {

							LinkedHashMap<String, Object> colMapNight = sizeDatasTwTotal
									.get(0);
							if (allDataMapNight.get(key) != null) {
								LinkedHashMap<String, Object> rowMapNight = allDataMapNight
										.get(key);

								one = bd.longValue();
								bd = new BigDecimal(sizeDatasTwTotal.get(0)
										.get("quantity").toString());
								oneData = bd.longValue();
								oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapNight.put("Growth rate_" + Month + "",
										lnum + "%");
								allDataMapNight.put(key, rowMapNight);

							} else {

								LinkedHashMap<String, Object> rowMapNight = new LinkedHashMap<String, Object>();

								rowMapNight.put("Growth rate_Category", key);
								one = bd.longValue();
								bd = new BigDecimal(sizeDatasTwTotal.get(0)
										.get("quantity").toString());
								oneData = bd.longValue();
								oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapNight.put("Growth rate_" + Month + "",
										lnum + "%");
								allDataMapNight.put(key, rowMapNight);
							}

						}
					} else {
						LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
						rowList.addLast(colMap);

						LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();
						BigDecimal bd = new BigDecimal(sizeDatasO.get(a)
								.get("quantity").toString());

						rowMap.put(
								"Different catgory sell-out quantity_Category",
								key);
						rowMap.put("Different catgory sell-out quantity_"
								+ Month + "", key);

						rowMap.put("Different catgory sell-out quantity_"
								+ Month + "", bd.longValue());

						allDataMapEight.put(key, rowMap);

						one = bd.longValue();

						if (sizeDatasTwTotal.size() == 1) {

							HashMap<String, Object> colMapNight = sizeDatasTwTotal
									.get(0);
							if (allDataMapNight.get(key) != null) {
								LinkedHashMap<String, Object> rowMapNight = allDataMapNight
										.get(key);

								one = bd.longValue();
								bd = new BigDecimal(sizeDatasTwTotal.get(0)
										.get("quantity").toString());
								oneData = bd.longValue();
								oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapNight.put("Growth rate_" + Month + "",
										lnum + "%");
								allDataMapNight.put(key, rowMapNight);

							} else {

								LinkedHashMap<String, Object> rowMapNight = new LinkedHashMap<String, Object>();

								rowMapNight.put("Growth rate_Category", key);
								one = bd.longValue();
								bd = new BigDecimal(sizeDatasTwTotal.get(0)
										.get("quantity").toString());
								oneData = bd.longValue();
								oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapNight.put("Growth rate_" + Month + "",
										lnum + "%");
								allDataMapNight.put(key, rowMapNight);
							}

						}

					}

				}


			Set<String> sizeSetEight = allDataMapEight.keySet();

			Iterator<String> sizeIterEight = sizeSetEight.iterator();
			HashMap<String, Object> totalMapEight = new HashMap<String, Object>();
			while (sizeIterEight.hasNext()) {
				String key = sizeIterEight.next();
				HashMap<String, Object> rowMap = allDataMapEight.get(key);
				dataListEight.addLast(rowMap);

				Set<String> rowSet = rowMap.keySet();

				Iterator<String> rowIter = rowSet.iterator();
				while (rowIter.hasNext()) {
					String rheader = rowIter.next();

					if (!rheader.contains("Category")) {
						int quantity = Integer.parseInt(rowMap.get(rheader)
								.toString());
						if (totalMapEight.get(rheader) == null) {

							totalMapEight.put(rheader, quantity);
						} else {
							int totalQuantity = Integer.parseInt(totalMapEight
									.get(rheader).toString());
							totalQuantity += quantity;
							totalMapEight.put(rheader, totalQuantity);
						}

					} else {
						total_header = rheader;
					}

				}

			}
			totalMapEight.put(total_header, "Total");
			dataListEight.addLast(totalMapEight);

			Set<String> sizeSetNight = allDataMapNight.keySet();

			Iterator<String> sizeIterNight = sizeSetNight.iterator();
			HashMap<String, Object> totalMapNight = new HashMap<String, Object>();
			while (sizeIterNight.hasNext()) {
				String key = sizeIterNight.next();
				HashMap<String, Object> rowMap = allDataMapNight.get(key);
				dataListNight.addLast(rowMap);

				Set<String> rowSet = rowMap.keySet();
				Iterator<String> rowIter = rowSet.iterator();

				while (rowIter.hasNext()) {
					String rheader = rowIter.next();

					if (!rheader.contains("Category")) {

						if (totalMapNight.get(rheader) == null) {

							totalMapNight.put(rheader, "100%");
						} else {
							totalMapNight.put(rheader, "100%");
						}

					} else {
						total_header = rheader;
					}

				}

			}
			totalMapNight.put("Growth rate_Category", "Total");
			dataListNight.addLast(totalMapNight);

			System.out.println("=====第7,8个查询="
					+ (System.currentTimeMillis() - start));

			LinkedList<HashMap<String, Object>> dataListTen = new LinkedList<HashMap<String, Object>>();

			int day = 0;
			long startTime = System.currentTimeMillis();

			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapTen = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
			LinkedHashMap<String, Object> rowMapTTL = new LinkedHashMap<String, Object>();
			for (int i = 1; i <= 7; i++) {
				DateUtil.year = Integer.parseInt(days[0]);
				DateUtil.month = Integer.parseInt(days[1]);
				lastDay = DateUtil.getLastDayOfMonth(DateUtil.year,
						DateUtil.month);
				lastDays = lastDay.split("-");
				if (i == 6) {
					beginDate = DateUtil.year + "-" + days[1] + "-" + days[2];
					endDate = DateUtil.year + "-" + days[1] + "-" + lastDays[2];
					keys = "Total";
				} else if (i == 7) {
					beginDate = DateUtil.year + "-" + days[1] + "-" + days[2];
					endDate = DateUtil.year + "-" + days[1] + "-" + lastDays[2];
					keys = "Ave So per day";
					DateUtil.start = Integer.parseInt(days[2]);
					end = Integer.parseInt(lastDays[2]);
					day = DateUtil.end - DateUtil.start;
				} else if (i < 6) {
					DateUtil.setWeekend(i);
					DateUtil.count();
					String s = "";
					String e = "";
					if (String.valueOf(DateUtil.getStart()).length() == 1) {
						s = "0" + DateUtil.getStart();
					} else {
						s = DateUtil.getStart() + "";
					}
					if (String.valueOf(DateUtil.getEnd()).length() == 1) {
						e = "0" + DateUtil.getEnd();
					} else {
						e = DateUtil.getEnd() + "";
					}

					if (lastDays[2].equals(s)) {
						beginDate = DateUtil.year + "-" + days[1] + "-" + s;
						endDate = DateUtil.year + "-" + days[1] + "-" + s;
						keys = "W" + i + "(" + s + ")";
					} else {
						keys = "W" + i + "(" + s + "-" + e + ")";
						beginDate = DateUtil.year + "-" + days[1] + "-" + s;
						endDate = DateUtil.year + "-" + days[1] + "-" + e;
					}

				}

				List<LinkedHashMap<String, Object>> sumDatas = excelService
						.selectSaleDataByDEALERByAc(beginDate, endDate, searchStr,
								conditions,WebPageUtil.isHQRole());

				for (int z = 0; z < sumDatas.size(); z++) {
					LinkedHashMap<String, Object> colMap = sumDatas.get(z);
					String key = colMap.get("DEALER").toString();
					if (allDataMapTen.get(key) != null) {
						LinkedHashMap<String, Object> rowMap = allDataMapTen
								.get(key);

						BigDecimal bd = new BigDecimal(sumDatas.get(z)
								.get("saleQty").toString());
						rowMap.put(keys, bd.longValue());
						rowMap.put("DEALER", sumDatas.get(z).get("DEALER"));

						if (i == 7) {
							double qty = bd.longValue();
							rowMap.put(keys, Math.round(qty / day));
						}
						allDataMapTen.put(key, rowMap);
					} else {
						LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
						rowList.addLast(colMap);

						LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();
						BigDecimal bd = new BigDecimal(sumDatas.get(z)
								.get("saleQty").toString());
						rowMap.put("DEALER", sumDatas.get(z).get("DEALER"));
						rowMap.put(keys, key);
						rowMap.put(keys, bd.longValue());

						if (i == 7) {
							double qty = bd.longValue();
							rowMap.put(keys, Math.round(qty / day));
						}
						allDataMapTen.put(key, rowMap);

					}

				}

				List<HashMap<String, Object>> sumTTL = excelService
						.selectSaleTTLByDEALERByAc(beginDate, endDate, searchStr,
								conditions,WebPageUtil.isHQRole());
				String key = keys;
				for (int s = 0; s < sumTTL.size(); s++) {
					HashMap<String, Object> colMap = sumTTL.get(s);
					rowMapTTL.put(keys, key);
					BigDecimal bd = new BigDecimal(sumTTL.get(s).get("saleQty")
							.toString().toString());
					rowMapTTL.put(keys, bd.longValue());
					rowMapTTL.put("DEALER", "Total");
					if (i == 7) {
						double qty = bd.longValue();
						rowMapTTL.put(keys, Math.round(qty / day));
					}

				}

			}
			allDataMapTen.put("Total", rowMapTTL);

			Set<String> sizeSetTen = allDataMapTen.keySet();

			Iterator<String> sizeIterTen = sizeSetTen.iterator();
			HashMap<String, Object> totalMapTen = new HashMap<String, Object>();
			while (sizeIterTen.hasNext()) {
				String key = sizeIterTen.next();
				HashMap<String, Object> rowMap = allDataMapTen.get(key);
				dataListTen.addLast(rowMap);

				Set<String> rowSet = rowMap.keySet();
				Iterator<String> rowIter = rowSet.iterator();

			}

			System.out.println("=====第9个="
					+ (System.currentTimeMillis() - startTime));
			// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
			Sheet sheet = wb.createSheet(Integer.parseInt(toYear[0]) - 1
					+ "  vs. " + toYear[0] + " comparative");
			sheet.setZoom(3, 4);
			// 创建字体
			Font fontinfo = wb.createFont();
			fontinfo.setFontHeightInPoints((short) 11); // 字体大小
			fontinfo.setFontName("Trebuchet MS");
			Font fonthead = wb.createFont();
			fonthead.setFontHeightInPoints((short) 12);
			fonthead.setFontName("Trebuchet MS");
			// colSplit, rowSplit,leftmostColumn, topRow,
			sheet.createFreezePane(3, 2, 3, 2);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat
					.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
														// hh:mm:ss

			CellStyle cellStylehead = wb.createCellStyle();// 表头样式
			cellStylehead.setFont(fonthead);
			cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylehead.setWrapText(true);

			CellStyle cellStyleWHITE = wb.createCellStyle();// 表头样式
			cellStyleWHITE.setFont(fonthead);
			cellStyleWHITE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleWHITE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleWHITE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyleWHITE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleWHITE.setWrapText(true);

			CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fonthead);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fonthead);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleYellow.setWrapText(true);

			CellStyle cellStylePERCENT = wb.createCellStyle();// 表头样式
			cellStylePERCENT.setFont(fonthead);
			cellStylePERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePERCENT.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePERCENT.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT
					.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStylePERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePERCENT.setWrapText(true);

			CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
			cellStyleRED.setFont(fonthead);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);

			CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			Row row = sheet.createRow((short) 0);
			// 创建这一行的一列，即创建单元格(CELL)
			Cell cell = row.createCell((short) 0);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("TCL " + partyName);// 标题
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

			// 第二行
			Row rowSec = sheet.createRow((short) 1);
			cell = rowSec.createCell((short) 0);
			cell.setCellStyle(cellStylename);
			cell.setCellValue("SELL-OUT ANALYSIS");
			// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
			CellStyle contextstyle = wb.createCellStyle();
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			contextstyle.setFont(fontinfo);
			// 从17行开始放另一个表格的表头

			int rows_max = 0; // 最大的一个项有几个子项

			startTime = System.currentTimeMillis();

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map map = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 2);
				for (int i = 0; i < header.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = header[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i + 2;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2,
								rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (map.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (map.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < header.length; j++) {
							if (header[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 2,
								k + 2, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k
									+ 2 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerTwo.length; i++) {
				String h = headerTwo[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapTwo = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13);
				for (int i = 0; i < headerTwo.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerTwo[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13,
								rows_max + 12, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapTwo.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapTwo.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerTwo.length; j++) {
							if (headerTwo[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13,
								k + 13, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13,
									k + 13 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapTwo.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapTwo.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerThree.length; i++) {
				String h = headerThree[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapThree = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10);
				for (int i = 0; i < headerThree.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerThree[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(
								13 + dataListTwo.size() + 10, rows_max + 12
										+ dataListTwo.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapThree.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapThree.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerThree.length; j++) {
							if (headerThree[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10, k + 13
								+ dataListTwo.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10, k + 13
									+ dataListTwo.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapThree.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapThree.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerFour.length; i++) {
				String h = headerFour[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapFour = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10
						+ dataListThree.size() + 10);
				for (int i = 0; i < headerFour.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerFour[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10, rows_max + 12
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10 + 5, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapFour.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapFour.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerFour.length; j++) {
							if (headerFour[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10, k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapFour.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapFour.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerFive.length; i++) {
				String h = headerFive[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapFive = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10
						+ dataListThree.size() + 10 + dataListFour.size() + 10);
				for (int i = 0; i < headerFive.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerFive[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10, rows_max + 12
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapFive.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapFive.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerFive.length; j++) {
							if (headerFive[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10, k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapFive.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapFive.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerSix.length; i++) {
				String h = headerSix[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapSix = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10
						+ dataListThree.size() + 10 + dataListFour.size() + 10
						+ dataListFive.size() + 10);
				for (int i = 0; i < headerSix.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerSix[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10, rows_max + 12
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapSix.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapSix.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerSix.length; j++) {
							if (headerSix[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10, k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapSix.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapSix.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerEight.length; i++) {
				String h = headerEight[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapEight = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10
						+ dataListThree.size() + 10 + dataListFour.size() + 10
						+ dataListFive.size() + 10 + dataListSix.size() + 10);
				for (int i = 0; i < headerEight.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerEight[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10,

						rows_max + 12 + dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapEight.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapEight.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerEight.length; j++) {
							if (headerEight[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10, k + 13 + dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapEight.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapEight.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerNight.length; i++) {
				String h = headerNight[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapNight = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10
						+ dataListThree.size() + 10 + dataListFour.size() + 10
						+ dataListFive.size() + 10 + dataListSix.size() + 10
						+ dataListEight.size() + 10);
				for (int i = 0; i < headerNight.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerNight[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10, rows_max + 12
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapNight.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapNight.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerNight.length; j++) {
							if (headerNight[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10, k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10, (num), (num
								+ cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10
									+ dataListEight.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10
									+ dataListEight.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapNight.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapNight.put(key, null);
						}
					}
				}
			}

			Map mapTen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet
						.createRow((short) k + 13 + dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10
								+ dataListNight.size() + 10);
				for (int i = 0; i < headerTen.length; i++) {
					cell.setCellStyle(cellStyleGreen);
					String headerTemp = headerTen[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10
								+ dataListNight.size() + 10, rows_max + 12
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10
								+ dataListNight.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapTen.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapTen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerTen.length; j++) {
							if (headerTen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10
								+ dataListNight.size() + 10, k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10
								+ dataListNight.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10
									+ dataListEight.size() + 10
									+ dataListNight.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10
									+ dataListEight.size() + 10
									+ dataListNight.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapTen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapTen.put(key, null);
						}
					}
				}
			}

			System.out.println("=====create header  cost time="
					+ (System.currentTimeMillis() - startTime));

			startTime = System.currentTimeMillis();
			for (int d = 0; d < dataList.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataList.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fields.length; c++) {

					Cell contentCell = datarow.createCell(c + 2);

					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fields[c]) != null
							&& dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fields[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fields[c]).toString()));

						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							// contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());

						} else {

							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c])
									.toString());
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataList.size() - 1) {
						cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleGreen);

					}
					/*
					 * if ( d == dataList.size() - 1 &&
					 * dataMap.get(fields[c]).toString().contains("-") ) {
					 * contentCell.setCellStyle(cellStyleRED); }
					 */

					if (d == 2) {
						contentCell.setCellStyle(cellStylePERCENT);
					}

				}

			}

			System.out.println("=====create dataListFirst  cost time="
					+ (System.currentTimeMillis() - startTime));

			startTime = System.currentTimeMillis();
			for (int d = 0; d < dataListTwo.size(); d++) {

				HashMap<String, Object> dataMap = dataListTwo.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsTwo.length; c++) {
					DataFormat df = wb.createDataFormat();
					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsTwo[c]) != null
							&& dataMap.get(fieldsTwo[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTwo[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTwo[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTwo[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsTwo[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsTwo[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsTwo[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsTwo[c])
									.toString());
						}
						if (c == 7 || c == 10 || c == 12 || c == 14) {
							contentCell.setCellStyle(cellStyleGreen);
							if (dataMap.get(fieldsTwo[c]).toString()
									.contains("-")
									&& d != dataListTwo.size() - 1) {
								contentCell.setCellStyle(cellStyleRED);
							}
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListTwo.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}

				}

			}

			System.out.println("=====create dataListTwo  cost time="
					+ (System.currentTimeMillis() - startTime));

			startTime = System.currentTimeMillis();
			for (int d = 0; d < dataListThree.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataListThree.get(d);

				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsThree.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsThree[c]) != null
							&& dataMap.get(fieldsThree[c]).toString().length() > 0

					) {
						// 判断data是否为数值型
						isNum = dataMap.get(fieldsThree[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsThree[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsThree[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsThree[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsThree[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsThree[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsThree[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsThree[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap
									.get(fieldsThree[c]).toString());
						}
						if (c == 7 || c == 10 || c == 12 || c == 14) {
							contentCell.setCellStyle(cellStyleGreen);
							if (dataMap.get(fieldsThree[c]).toString()
									.contains("-")
									&& d != dataListThree.size() - 1) {
								contentCell.setCellStyle(cellStyleRED);

							}
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListThree.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}

				}

			}

			System.out.println("=====create dataList3  cost time="
					+ (System.currentTimeMillis() - startTime));

			startTime = System.currentTimeMillis();

			for (int d = 0; d < dataListFour.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataListFour.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsFour.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsFour[c]) != null
							&& dataMap.get(fieldsFour[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFour[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFour[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFour[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsFour[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsFour[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsFour[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFour[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFour[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap.get(fieldsFour[c])
									.toString());
						}
						if (c == 7 || c == 10 || c == 12 || c == 14) {
							contentCell.setCellStyle(cellStyleGreen);
							if (dataMap.get(fieldsFour[c]).toString()
									.contains("-")
									&& d != dataListFour.size() - 1) {
								contentCell.setCellStyle(cellStyleRED);

							}
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListFour.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}

				}

			}

			System.out.println("=====create dataList4 cost time="
					+ (System.currentTimeMillis() - startTime));

			startTime = System.currentTimeMillis();

			for (int d = 0; d < dataListFive.size(); d++) {

				HashMap<String, Object> dataMap = dataListFive.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10
						+ dataListFour.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsFive.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsFive[c]) != null
							&& dataMap.get(fieldsFive[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFive[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFive[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFive[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsFive[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsFive[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						DataFormat df = wb.createDataFormat(); // 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsFive[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFive[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFive[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap.get(fieldsFive[c])
									.toString());
						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}

			System.out.println("=====create dataList5  cost time="
					+ (System.currentTimeMillis() - startTime));

			startTime = System.currentTimeMillis();
			for (int d = 0; d < dataListSix.size(); d++) {

				HashMap<String, Object> dataMap = dataListSix.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10
						+ dataListFour.size() + 10 + dataListFive.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsSix.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsSix[c]) != null
							&& dataMap.get(fieldsSix[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsSix[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsSix[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsSix[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsSix[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsSix[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						DataFormat df = wb.createDataFormat(); // 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsSix[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsSix[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsSix[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap.get(fieldsSix[c])
									.toString());
						}
						if (c > 1 && c != fieldsSix.length - 1
								&& d != dataListSix.size()) {
							if (dataMap.get(fieldsSix[c - 1]).toString() != null) {
								if (dataMap.get(fieldsSix[c - 1]).toString()
										.contains("%")
										&& dataMap.get(fieldsSix[c]).toString()
												.contains("%")) {

									String a = dataMap.get(fieldsSix[c - 1])
											.toString();
									String b = dataMap.get(fieldsSix[c])
											.toString();
									// 去掉%
									String tempA = a.substring(0,
											a.lastIndexOf("%"));
									String tempB = b.substring(0,
											b.lastIndexOf("%"));
									// 精确表示
									Integer dataA = Integer.parseInt(tempA);
									Integer dataB = Integer.parseInt(tempB);
									// 大于为1，相同为0，小于为-1
									if (dataB.compareTo(dataA) == 0) {
										contentCell
												.setCellStyle(cellStyleYellow);
									} else if (dataB.compareTo(dataA) == 1) {
										contentCell
												.setCellStyle(cellStyleGreen);
									} else if (dataB.compareTo(dataA) == -1) {
										contentCell.setCellStyle(cellStyleRED);
									}
								}
							}

						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}

			System.out.println("=====create dataList7  cost time="
					+ (System.currentTimeMillis() - startTime));

			startTime = System.currentTimeMillis();
			for (int d = 0; d < dataListEight.size(); d++) {

				HashMap<String, Object> dataMap = dataListEight.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10
						+ dataListFour.size() + 10 + dataListFive.size() + 10
						+ dataListSix.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsEight.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsEight[c]) != null
							&& dataMap.get(fieldsEight[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsEight[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsEight[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsEight[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsEight[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsEight[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						DataFormat df = wb.createDataFormat(); // 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsEight[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsEight[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsEight[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap
									.get(fieldsEight[c]).toString());
						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}
			System.out.println("=====create dataList8  cost time="
					+ (System.currentTimeMillis() - startTime));

			startTime = System.currentTimeMillis();
			for (int d = 0; d < dataListNight.size(); d++) {

				HashMap<String, Object> dataMap = dataListNight.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10
						+ dataListFour.size() + 10 + dataListFive.size() + 10
						+ dataListSix.size() + 10 + dataListEight.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsNight.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsNight[c]) != null
							&& dataMap.get(fieldsNight[c]).toString().length() > 0

					) {
						// 判断data是否为数值型
						isNum = dataMap.get(fieldsNight[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsNight[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsNight[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsNight[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsNight[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						DataFormat df = wb.createDataFormat(); // 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsNight[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsNight[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsNight[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap
									.get(fieldsNight[c]).toString());
						}
						if (c > 1 && c != fieldsNight.length - 1
								&& d != dataListNight.size()) {
							if (dataMap.get(fieldsNight[c - 1]) != null) {
								if (dataMap.get(fieldsNight[c - 1]).toString()
										.contains("%")
										&& dataMap.get(fieldsNight[c])
												.toString().contains("%")) {

									String a = dataMap.get(fieldsNight[c - 1])
											.toString();
									String b = dataMap.get(fieldsNight[c])
											.toString();
									// 去掉%
									String tempA = a.substring(0,
											a.lastIndexOf("%"));
									String tempB = b.substring(0,
											b.lastIndexOf("%"));
									// 精确表示
									Integer dataA = Integer.parseInt(tempA);
									Integer dataB = Integer.parseInt(tempB);
									// 大于为1，相同为0，小于为-1
									if (dataB.compareTo(dataA) == 0) {
										contentCell
												.setCellStyle(cellStyleYellow);
									} else if (dataB.compareTo(dataA) == 1) {
										contentCell
												.setCellStyle(cellStyleGreen);
									} else if (dataB.compareTo(dataA) == -1) {
										contentCell.setCellStyle(cellStyleRED);
									}
								}
							}

						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}
			System.out.println("=====create dataList8  cost time="
					+ (System.currentTimeMillis() - startTime));

			startTime = System.currentTimeMillis();

			for (int d = 0; d < dataListTen.size(); d++) {

				HashMap<String, Object> dataMap = dataListTen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10
						+ dataListFour.size() + 10 + dataListFive.size() + 10
						+ dataListSix.size() + 10 + dataListEight.size() + 10
						+ dataListNight.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsTen.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsTen[c]) != null
							&& dataMap.get(fieldsTen[c]).toString().length() > 0

					) {
						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTen[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTen[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTen[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsTen[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsTen[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						DataFormat df = wb.createDataFormat(); // 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsTen[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTen[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTen[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap.get(fieldsTen[c])
									.toString());
						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}

			System.out.println("=====create dataList9 cost time="
					+ (System.currentTimeMillis() - startTime));

			startTime = System.currentTimeMillis();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Dealer Sellout
	public void DealerSellout(SXSSFWorkbook wb, String partyName,
			String beginDateOne, String endDateOne,String tBeginDate,String tEndDate) throws ParseException {

		// 表头数据
		String[] headers = { "REG", "RANK", "DEALER", };

		String[] toYear = beginDateOne.split("-");

		Date timec = format.parse(beginDateOne);
		Calendar rightNowc = Calendar.getInstance();
		rightNowc.setTime(timec);
		Date toYearOne = format.parse(format.format(rightNowc.getTime()));

		int laYear = Integer.parseInt(toYear[0].toString()) - 1;
		String laDays = DateUtil.getLastDayOfMonth(laYear,
				Integer.parseInt(toYear[1]));
		String[] laDay = laDays.split("-");
		headers = insert(headers, sdf.format(toYearOne) + "." + laYear + "_"
				+ "NO. OF SHOP");
		headers = insert(headers, sdf.format(toYearOne) + "." + laYear + "_"
				+ "NO. OF FPS");
		headers = insert(headers, sdf.format(toYearOne) + "." + laYear + "_"
				+ "TOTAL QTY.");
		headers = insert(headers, sdf.format(toYearOne) + "." + laYear + "_"
				+ "TOTAL AMOUNT");

		headers = insert(headers, sdf.format(toYearOne) + "." + toYear[0] + "_"
				+ "NO. OF SHOP");
		headers = insert(headers, sdf.format(toYearOne) + "." + toYear[0] + "_"
				+ "NO. OF FPS");
		
		headers = insert(headers, sdf.format(toYearOne) + "." + toYear[0] + "_"
				+ "TTL AIRCON SO_QTY");
		headers = insert(headers, sdf.format(toYearOne) + "." + toYear[0] + "_"
				+ "TTL AIRCON SO_AMT");
		headers = insert(headers, "BASIC TARGET");

		headers = insert(headers, "ACH.");
		headers = insert(headers, "MARKET SHARE");
		headers = insert(headers, "GROWTH_QTY");
		headers = insert(headers, "GROWTH_AMOUNT");
		headers = insert(headers, laYear + " AVE.SO/FPS_QTY");
		headers = insert(headers, laYear + " AVE.SO/FPS_AMT");
		headers = insert(headers, toYear[0] + " AVE.SO/FPS_QTY");
		headers = insert(headers, toYear[0] + " AVE.SO/FPS_AMT");
		headers = insert(headers, "SELL-OUT EFFICIENCY_QTY");
		headers = insert(headers, "SELL-OUT EFFICIENCY_AMT");

		// 按照对应格式将表头传入
		ArrayList columns = new ArrayList();
		for (int i = 0; i < headers.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headers[i]);
			columnMap.put("field", headers[i]);
			columns.add(columnMap);
		}

		String[] header = new String[columns.size()];
		String[] fields = new String[columns.size()];
		for (int i = 0, l = columns.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columns.get(i);
			header[i] = columnMap.get("header").toString();
			fields[i] = columnMap.get("field").toString();

		}

		String searchStr = null;
		String conditions = "";
		String center = "";
		String country = "";
		String region = "";
		String office = "";

		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if (!WebPageUtil.isHAdmin()) {
			if (request.getParameter("center") != null
					&& !request.getParameter("center").equals("")
					|| request.getParameter("country") != null
					&& !request.getParameter("country").equals("")
					|| request.getParameter("region") != null
					&& !request.getParameter("region").equals("")
					|| request.getParameter("office") != null
					&& !request.getParameter("office").equals("")) {

				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")) {
					center = request.getParameter("center");
					conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
							+ center + "')   ";
				}

				if (request.getParameter("country") != null
						&& !request.getParameter("country").equals("")) {
					country = request.getParameter("country");
					conditions = "  pa.country_id= " + country + "  ";
				}
				if (request.getParameter("region") != null
						&& !request.getParameter("region").equals("")) {
					region = request.getParameter("region");
					conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
							+ region
							+ "'  OR PARTY_ID='"+region+"'))  ";
				}
				if (request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {
					office = request.getParameter("office");
					conditions = "    pa.party_id IN ('" + office + "')  ";
				}

			} else {
				if (null != userPartyIds && !"".equals(userPartyIds)) {
					conditions = "  pa.party_id in (" + userPartyIds + ")  ";
				} else {
					conditions = "  1=2  ";
				}
			}

		} else {
			conditions = " 1=1 ";

		}
		
		if (request.getParameter("level") != null
				&& !request.getParameter("level").equals("")) {
			conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
		}
		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
		try {
			String beginOne = laYear + "-" + toYear[1] + "-" + "01";
			String endOne = laDays;

			String beginTwo = beginDateOne;
			String endTwo = endDateOne;
			List<HashMap<String, Object>> DealerSelloutLast = excelService
					.selectSelloutByDealerByAc(beginOne, endOne, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			HashMap<String, ArrayList<HashMap<String, Object>>> selloutLastMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();

			for (int m = 0; m < DealerSelloutLast.size(); m++) {
				if (selloutLastMap.get(DealerSelloutLast.get(m).get("DEALER")
						.toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(DealerSelloutLast.get(m));
					selloutLastMap.put(DealerSelloutLast.get(m).get("DEALER")
							.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = selloutLastMap
							.get(DealerSelloutLast.get(m).get("DEALER")
									.toString());
					modelList.add(DealerSelloutLast.get(m));
				}

			}

			List<HashMap<String, Object>> DealerSelloutInfo = excelService
					.selectSelloutByDealerInfo(searchStr, conditions);


			List<HashMap<String, Object>> DealerSelloutTo = excelService
					.selectSelloutByDealerByAc(beginTwo, endTwo, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			HashMap<String, ArrayList<HashMap<String, Object>>> selloutToMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();

			for (int m = 0; m < DealerSelloutTo.size(); m++) {
				if (selloutToMap.get(DealerSelloutTo.get(m).get("DEALER")
						.toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(DealerSelloutTo.get(m));
					selloutToMap.put(DealerSelloutTo.get(m).get("DEALER")
							.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = selloutToMap
							.get(DealerSelloutTo.get(m).get("DEALER")
									.toString());
					modelList.add(DealerSelloutTo.get(m));
				}

			}
			
		
			for (int w = 0; w < DealerSelloutInfo.size(); w++) {
				double ach = 0.0;
				double avg = 0.0;
				double tqavg = 0.0;
				double tsavg = 0.0;
				double lqavg = 0.0;
				double lsavg = 0.0;
				int lq = 0;
				int lfps = 0;
				int lshop = 0;
				Double lt = 0.0;
				Double ls = 0.0;
				Double ts = 0.0;
				int tq =  0;
				int tfps = 0;
				int tshop = 0;
				Double tt = 0.0;
				double ltq = 0.0;
				HashMap<String, Object> dataMap = new HashMap<String, Object>();

				dataMap.put("REG", DealerSelloutInfo.get(w).get("REG"));
				dataMap.put("DEALER", DealerSelloutInfo.get(w).get("DEALER"));
				// dataMap.put("RANK", w + 1);

				BigDecimal bd = null;
				String am = "";
				String aq = "";
				if (selloutToMap.get(DealerSelloutInfo.get(w).get("DEALER")) != null) {

					ArrayList<HashMap<String, Object>> list = selloutToMap
							.get(DealerSelloutInfo.get(w).get("DEALER"));
					for (int i = 0; i < list.size(); i++) {

						int row = 7 + i;
						dataMap.put(sdf.format(toYearOne) + "." + toYear[0]
								+ "_" + "NO. OF SHOP",
								list.get(i).get("noOfShops"));
						dataMap.put(sdf.format(toYearOne) + "." + toYear[0]
								+ "_" + "NO. OF FPS", list.get(i).get("tvFps"));

						BigDecimal bq = new BigDecimal(list.get(i)
								.get("saleQty").toString());
						aq = bq.toPlainString();

						dataMap.put(sdf.format(toYearOne) + "." + toYear[0]
								+ "_" + "TTL AIRCON SO_QTY", bq.longValue());

						bd = new BigDecimal(list.get(i).get("saleSum")
								.toString());
						am = bd.toPlainString();

						dataMap.put(sdf.format(toYearOne) + "." + toYear[0]
								+ "_" + "TTL AIRCON SO_AMT", am);

						BigDecimal td = new BigDecimal(list.get(i)
								.get("targetSum").toString());
						String tm = td.toPlainString();
						dataMap.put("BASIC TARGET", tm);

						

						tshop = Integer.parseInt(list.get(i).get("noOfShops")
								.toString());
						tfps = Integer.parseInt(list.get(i).get("tvFps")
								.toString());
						tq =  bd.intValue();

						ts = Double.parseDouble(am);
						tt = Double.parseDouble(tm);
						if (tt != 0.0) {
							ach = ts / tt * 100;
							long lnum = Math.round(ach);
							dataMap.put("ACH.", lnum + "%");
						}

						if (tfps != 0) {
							avg = tq / tfps;
							dataMap.put(toYear[0] + " AVE.SO/FPS_QTY",Math.round( avg));
							tqavg = Math.round( avg);

							avg =  (ts / tfps);
							dataMap.put(toYear[0] + " AVE.SO/FPS_AMT", Math.round( avg));
							tsavg = Math.round( avg);
						}

					}

				}
				if (selloutLastMap.get(DealerSelloutInfo.get(w).get("DEALER")) != null) {

					ArrayList<HashMap<String, Object>> list = selloutLastMap
							.get(DealerSelloutInfo.get(w).get("DEALER"));
					for (int j = 0; j < list.size(); j++) {
						dataMap.put(sdf.format(toYearOne) + "." + laYear + "_"
								+ "NO. OF SHOP", list.get(j).get("noOfShops"));
						dataMap.put(sdf.format(toYearOne) + "." + laYear + "_"
								+ "NO. OF FPS", list.get(j).get("tvFps"));
						dataMap.put(sdf.format(toYearOne) + "." + laYear + "_"
								+ "TOTAL QTY.", list.get(j).get("saleQty"));
						BigDecimal lbd = new BigDecimal(list.get(j)
								.get("saleSum").toString());
						String lam = lbd.toPlainString();
						dataMap.put(sdf.format(toYearOne) + "." + laYear + "_"
								+ "TOTAL AMOUNT", lam);
						lshop = Integer.parseInt(list.get(j).get("noOfShops")
								.toString());

						lfps = Integer.parseInt(list.get(j).get("tvFps")
								.toString());

						lbd = new BigDecimal(list.get(j).get("saleQty")
								.toString());

						lq =  lbd.intValue();
						ls = Double.parseDouble(lam);

						if (lfps != 0) {
							avg =  (lq / lfps);
							dataMap.put(laYear + " AVE.SO/FPS_QTY", Math.round( avg));
							lqavg = Math.round( avg);

							avg =  (ls / lfps);
							dataMap.put(laYear + " AVE.SO/FPS_AMT", Math.round( avg));
							lsavg = Math.round( avg);
						}

					}
				}

				if (tq != 0) {
					ltq = tq - lq;
					ach = ltq / tq * 100;
					long lnum = Math.round(ach);
					dataMap.put("GROWTH_QTY", lnum + "%");

				} else if (tq == 0 && lq == 0) {
					dataMap.put("GROWTH_QTY", "100%");
				} else if (tq == 0 && lq != 0) {
					dataMap.put("GROWTH_QTY", "-100%");
				}
				if (ts != 0.0) {
					ltq = ts - ls;
					ach = ltq / ts * 100;
					long lnum = Math.round(ach);
					dataMap.put("GROWTH_AMOUNT", lnum + "%");
				} else if (ts == 0.0 && ls == 0.0) {
					dataMap.put("GROWTH_AMOUNT", "100%");
				} else if (ts == 0.0 && ls != 0.0) {
					dataMap.put("GROWTH_AMOUNT", "-100%");
				}

				ach = (tqavg - lqavg);
				bd = new BigDecimal(ach);
				am = bd.toPlainString();
				dataMap.put("SELL-OUT EFFICIENCY_QTY", am);

				ach = (tsavg - lsavg);
				bd = new BigDecimal(ach);
				am = bd.toPlainString();
				dataMap.put("SELL-OUT EFFICIENCY_AMT", am);

				dataList.add(dataMap);

			}
			DateUtil.Order(dataList, "GROWTH_AMOUNT");

			for (int i = 0; i < dataList.size(); i++) {
				dataList.get(i).put("RANK", i + 1);
			}
			// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
			Sheet sheet = wb.createSheet("Dealer Sellout");
			sheet.setZoom(3, 4);
			// 创建字体
			Font fontinfo = wb.createFont();
			fontinfo.setFontHeightInPoints((short) 9); // 字体大小
			fontinfo.setFontName("Trebuchet MS");
			Font fonthead = wb.createFont();
			fonthead.setFontHeightInPoints((short) 10);
			fonthead.setFontName("Trebuchet MS");

			// colSplit, rowSplit,leftmostColumn, topRow,
			sheet.createFreezePane(3, 5, 3, 5);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat
					.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
														// hh:mm:ss

			CellStyle cellStylehead = wb.createCellStyle();// 表头样式
			cellStylehead.setFont(fonthead);
			cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			CellStyle cellStyleWHITE = wb.createCellStyle();// 表头样式
			cellStyleWHITE.setFont(fonthead);
			cellStyleWHITE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleWHITE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleWHITE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyleWHITE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleWHITE.setWrapText(true);

			CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fonthead);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fonthead);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleYellow.setWrapText(true);

			CellStyle cellStylePERCENT = wb.createCellStyle();// 表头样式
			cellStylePERCENT.setFont(fonthead);
			cellStylePERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePERCENT.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePERCENT.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT
					.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStylePERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePERCENT.setWrapText(true);

			CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
			cellStyleRED.setFont(fonthead);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);

			CellStyle cellStylePE = wb.createCellStyle();// 表头样式
			cellStylePE.setFont(fonthead);
			cellStylePE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePE.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			cellStylePE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePE.setWrapText(true);

			CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			Row row = sheet.createRow((short) 0);
			// 创建这一行的一列，即创建单元格(CELL)
			Cell cell = row.createCell((short) 0);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("DEALER  SELL-OUT PERFORMANCES");// 标题
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

			CellStyle contextstyle = wb.createCellStyle();
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			contextstyle.setFont(fontinfo);
			DataFormat df = wb.createDataFormat();
			//
			int ce = DealerSelloutInfo.size() + 6;

			row = sheet.createRow((short) 5);
			// 创建这一行的一列，即创建单元格(CELL)
			cell = row.createCell((short) 3);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,D7:D" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,D7:D" + ce + ")");

			cell = row.createCell((short) 4);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,E7:E" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,E7:E" + ce + ")");

			cell = row.createCell((short) 5);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,F7:F" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,F7:F" + ce + ")");

			cell = row.createCell((short) 6);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,G7:G" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,G7:G" + ce + ")");

			cell = row.createCell((short) 7);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,H7:H" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,H7:H" + ce + ")");

			cell = row.createCell((short) 8);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,I7:I" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,I7:I" + ce + ")");

			cell = row.createCell((short) 9);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,J7:J" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,J7:J" + ce + ")");

			cell = row.createCell((short) 10);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,K7:K" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,K7:K" + ce + ")");

			cell = row.createCell((short) 11);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,L7:L" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,L7:L" + ce + ")");

			cell = row.createCell((short) 12);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("TEXT(K6/L6,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT(K6/L6,\"0.00%\")");

			cell = row.createCell((short) 13);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellValue("100%");

			
			cell = row.createCell((short) 14);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("TEXT((J6-F6)/F6,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT((J6-F6)/F6,\"0.00%\")");
			
			
			cell = row.createCell((short) 15);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("TEXT((K6-G6)/G6,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT((K6-G6)/G6,\"0.00%\")");
			
			
			cell = row.createCell((short) 16);
			cell.setCellValue("SUM(F6/E6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(F6/E6)");

			
			
			cell = row.createCell((short) 17);
			cell.setCellValue("SUM(G6/E6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(G6/E6)");
			
			
			cell = row.createCell((short) 18);
			cell.setCellValue("SUM(J6/I6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(J6/I6)");

			cell = row.createCell((short) 19);
			cell.setCellValue("SUM(K6/I6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(K6/I6)");
			

			

			cell = row.createCell((short) 20);
			cell.setCellValue("SUM(S6-Q6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(S6-Q6)");

			cell = row.createCell((short) 21);
			cell.setCellValue("SUM(T6-R6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(T6-R6)");

			
			
			
			
			// 从17行开始放另一个表格的表头

			int rows_max = 0; // 最大的一个项有几个子项

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map map = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 2);
				for (int i = 0; i < header.length; i++) {

					String headerTemp = header[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (i >= 4 && i < 8) {
						cell.setCellStyle(cellStylePERCENT);
					} else if (i >= 8 && i < 16) {
						cell.setCellStyle(cellStyleYellow);
					} else {
						cell.setCellStyle(cellStyleWHITE);
					}
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2,
								rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (map.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (map.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < header.length; j++) {
							if (header[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 2,
								k + 2, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k
									+ 2 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int d = 0; d < dataList.size(); d++) {

				HashMap<String, Object> dataMap = dataList.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 2 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fields.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fields[c]) != null
							&& dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fields[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
								cellStylePERCENT.setDataFormat(df
										.getFormat("#,##0"));
								cellStyleGreen.setDataFormat(df
										.getFormat("#,##0"));
								cellStyleWHITE.setDataFormat(df
										.getFormat("#,##0"));
								cellStylePE
										.setDataFormat(df.getFormat("#,##0"));
								cellStyleRED.setDataFormat(df
										.getFormat("#,##0"));
								cellStyleYellow.setDataFormat(df
										.getFormat("#,##0"));
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fields[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c])
									.toString());
						}
						if (c >= 3 && c < 7) {
							cellStylePERCENT.setDataFormat(df
									.getFormat("#,##0"));
							contentCell.setCellStyle(cellStylePERCENT);
							if (dataMap.get(fields[c]).toString().contains("-")) {
								cellStyleRED.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStyleRED);
							}
						} else if (c >= 7 && c < 15) {
							cellStyleYellow
									.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(cellStyleYellow);
							if (dataMap.get(fields[c]).toString().contains("-")) {
								cellStyleRED.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStyleRED);
							}
						} else if (c > 17 && c <= 19) {
							cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(cellStyleGreen);
							if (dataMap.get(fields[c]).toString().contains("-")) {
								cellStyleRED.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStyleRED);
							}
						} else if (c > 23) {
							cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(cellStyleGreen);
							if (dataMap.get(fields[c]).toString().contains("-")) {
								cellStyleRED.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStyleRED);
							}
						} else {
							cellStyleWHITE.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(cellStyleWHITE);
						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 2016 YTD sellout
	 */
	public void monthYTDsellout(SXSSFWorkbook wb, String partyName,
			String beginDateOne, String endDateOne) {

		String[] toYear = beginDateOne.split("-");
		int laYear = Integer.parseInt(toYear[0].toString()) - 1;
		String laDays = DateUtil.getLastDayOfMonth(laYear,
				Integer.parseInt(toYear[1]));
		String[] laDay = laDays.split("-");
		int lastYear = Integer.parseInt(toYear[0]) - 1;

		// 表头数据
		String[] headers = { "TCL MONTHLY SELLOUT TREND_Year",
				"TCL MONTHLY SELLOUT TREND_Month"

		};

		if (toYear[1].equals("01")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_Jan");
		}
		if (toYear[1].equals("02")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_Feb");
		}
		if (toYear[1].equals("03")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_Mar");
		}
		if (toYear[1].equals("04")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_Apr");
		}
		if (toYear[1].equals("05")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_May");
		}
		if (toYear[1].equals("06")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_June");
		}
		if (toYear[1].equals("07")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_July");
		}
		if (toYear[1].equals("08")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_August");
		}
		if (toYear[1].equals("09")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_September");
		}
		if (toYear[1].equals("10")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_October");
		}
		if (toYear[1].equals("11")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_November");
		}
		if (toYear[1].equals("12")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_December");
		}
		// headers=insert(headers, "TCL MONTHLY SELLOUT TREND_TOTAL");

		// 按照对应格式将表头传入
		ArrayList columns = new ArrayList();
		for (int i = 0; i < headers.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headers[i]);
			columnMap.put("field", headers[i]);
			columns.add(columnMap);
		}

		String[] header = new String[columns.size()];
		String[] fields = new String[columns.size()];
		for (int i = 0, l = columns.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columns.get(i);
			header[i] = columnMap.get("header").toString();
			fields[i] = columnMap.get("field").toString();

		}

		// 表头数据
		String[] headersTwo = {
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_Category",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_MODELS"

		};

		if (toYear[1].equals("01")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_Jan");
		}
		if (toYear[1].equals("02")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_Feb");
		}
		if (toYear[1].equals("03")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_Mar");
		}
		if (toYear[1].equals("04")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_Apr");
		}
		if (toYear[1].equals("05")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_May");
		}
		if (toYear[1].equals("06")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_June");
		}
		if (toYear[1].equals("07")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_July");
		}
		if (toYear[1].equals("08")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_August");
		}
		if (toYear[1].equals("09")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_September");
		}
		if (toYear[1].equals("10")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_October");
		}
		if (toYear[1].equals("11")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_November");
		}
		if (toYear[1].equals("12")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_December");
		}
		// headersTwo=insert(headersTwo,"SELL-OUT TREND PER MODEL YR-" +
		// toYear[0] + "_TOTAL");

		// 按照对应格式将表头传入
		ArrayList columnsTwo = new ArrayList();
		for (int i = 0; i < headersTwo.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersTwo[i]);
			columnMap.put("field", headersTwo[i]);
			columnsTwo.add(columnMap);
		}

		String[] headerTwo = new String[columnsTwo.size()];
		String[] fieldsTwo = new String[columnsTwo.size()];
		for (int i = 0, l = columnsTwo.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsTwo.get(i);
			headerTwo[i] = columnMap.get("header").toString();
			fieldsTwo[i] = columnMap.get("field").toString();

		}

		String searchStr = null;
		String conditions = "";
		String center = "";
		String country = "";
		String region = "";
		String office = "";

		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if (!WebPageUtil.isHAdmin()) {
			if (request.getParameter("center") != null
					&& !request.getParameter("center").equals("")
					|| request.getParameter("country") != null
					&& !request.getParameter("country").equals("")
					|| request.getParameter("region") != null
					&& !request.getParameter("region").equals("")
					|| request.getParameter("office") != null
					&& !request.getParameter("office").equals("")) {

				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")) {
					center = request.getParameter("center");
					conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
							+ center + "')   ";
				}

				if (request.getParameter("country") != null
						&& !request.getParameter("country").equals("")) {
					country = request.getParameter("country");
					conditions = "  pa.country_id= " + country + "  ";
				}
				if (request.getParameter("region") != null
						&& !request.getParameter("region").equals("")) {
					region = request.getParameter("region");
					conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
							+ region
							+ "'  OR PARTY_ID='"+region+"'))  ";
				}
				if (request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {
					office = request.getParameter("office");
					conditions = "    pa.party_id IN ('" + office + "')  ";
				}

			} else {
				if (null != userPartyIds && !"".equals(userPartyIds)) {
					conditions = "  pa.party_id in (" + userPartyIds + ")  ";
				} else {
					conditions = "  1=2  ";
				}
			}

		} else {
			conditions = " 1=1 ";

		}
		
		if (request.getParameter("level") != null
				&& !request.getParameter("level").equals("")) {
			conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
		}
		long start = System.currentTimeMillis();
		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
		LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapOne = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> rowMapOne = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> rowMapO = new LinkedHashMap<String, Object>();
		String lastDay = DateUtil.getLastDayOfMonth(
				Integer.parseInt(toYear[0]), 1);
		String[] lastDays = lastDay.split("-");
		String spec = "";
		double one = 0.0;
		double oneData = 0.0;
		double oneAch = 0.0;
		List<LinkedHashMap<String, Object>> sizeDatasTwTotal = null;
		String startDay = "";
		String Month = "";
		try {
			HashMap<String, Object> data = new HashMap<String, Object>();
			List<LinkedHashMap<String, Object>> sizeDatas = null;

			List<LinkedHashMap<String, Object>> sizeLa = excelService
					.selectQtyTotalBySpecTypeByAc(
							spec,
							laYear + "-" + toYear[1] + "-01",
							DateUtil.getLastDayOfMonth(laYear,
									Integer.parseInt(toYear[1])), searchStr,
							conditions,WebPageUtil.isHQRole());

			List<LinkedHashMap<String, Object>> sizeTo = excelService
					.selectQtyTotalBySpecTypeByAc(spec, beginDateOne, endDateOne,
							searchStr, conditions,WebPageUtil.isHQRole());

			if (sizeLa.size() > sizeTo.size()) {
				sizeDatas = sizeLa;
			} else if (sizeTo.size() == sizeLa.size()) {
				sizeDatas = sizeLa;
			} else if (sizeTo.size() > sizeLa.size()) {
				sizeDatas = sizeTo;
			}

			lastDay = lastYear + "-" + endDateOne.split("-")[1] + "-"
					+ endDateOne.split("-")[2];
			lastDays = lastDay.split("-");
			startDay = lastYear + "-" + beginDateOne.split("-")[1] + "-"
					+ beginDateOne.split("-")[2];
			if (toYear[1].equals("01")) {
				Month = "Jan";
			} else if (toYear[1].equals("02")) {
				Month = "Feb";
			} else if (toYear[1].equals("03")) {
				Month = "Mar";
			} else if (toYear[1].equals("04")) {
				Month = "Apr";
			} else if (toYear[1].equals("05")) {
				Month = "May";
			} else if (toYear[1].equals("06")) {
				Month = "June";
			} else if (toYear[1].equals("07")) {
				Month = "July";
			} else if (toYear[1].equals("08")) {
				Month = "August";
			} else if (toYear[1].equals("10")) {
				Month = "October";
			} else if (toYear[1].equals("11")) {
				Month = "November";
			} else if (toYear[1].equals("12")) {
				Month = "December";
			}

			List<LinkedHashMap<String, Object>> sizeDatasOneData = excelService
					.selectQtyTotalBySpecTypeByAc(spec, startDay, lastDay,
							searchStr, conditions,WebPageUtil.isHQRole());

			for (int z = 0; z < sizeDatasOneData.size(); z++) {
				LinkedHashMap<String, Object> colMap = sizeDatasOneData.get(z);
				String key = lastYear + colMap.get("SPEC").toString();
				if (allDataMapOne.get(key) != null) {
					LinkedHashMap<String, Object> rowMap = allDataMapOne
							.get(key);

					BigDecimal bd = new BigDecimal(sizeDatasOneData.get(z)
							.get("quantity").toString());
					String am = bd.toPlainString();
					// 分成12个月份查，查一个放一个
					rowMap.put("TCL MONTHLY SELLOUT TREND_" + Month + "", am);

					rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
							+ lastYear);
					rowMap.put("TCL MONTHLY SELLOUT TREND_Month",
							sizeDatasOneData.get(z).get("SPEC").toString());

					allDataMapOne.put(key, rowMap);
				} else {
					LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
					rowList.addLast(colMap);

					LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();

					BigDecimal bd = new BigDecimal(sizeDatasOneData.get(z)
							.get("quantity").toString());
					String am = bd.toPlainString();
					// 分成12个月份查，查一个放一个
					rowMap.put("TCL MONTHLY SELLOUT TREND_" + Month + "", am);

					rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
							+ lastYear);
					rowMap.put("TCL MONTHLY SELLOUT TREND_Month",
							sizeDatasOneData.get(z).get("SPEC").toString());

					allDataMapOne.put(key, rowMap);

				}
			}

			sizeDatasTwTotal = excelService.selectSaleTotalBySizeByAc(startDay,
					lastDay, searchStr, conditions,WebPageUtil.isHQRole());
			if (sizeDatasTwTotal.size() == 1) {
				BigDecimal bd = new BigDecimal(sizeDatasTwTotal.get(0)
						.get("quantity").toString());
				String am = bd.toPlainString();

				rowMapO.put("TCL MONTHLY SELLOUT TREND_" + Month + "", am);
				rowMapO.put("TCL MONTHLY SELLOUT TREND_Month", "TOTAL");
			}

			// }

			if (sizeLa.size() < sizeTo.size() && sizeLa.size() != 0) {
				for (int i = 0; i < sizeTo.size() - sizeLa.size(); i++) {
					LinkedHashMap<String, Object> rw = new LinkedHashMap<String, Object>();
					allDataMapOne.put("S" + i, rw);
				}
			}

			if (sizeLa.size() == 0) {
				for (int j = 0; j < sizeTo.size(); j++) {
					LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();

					rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
							+ lastYear);
					rowMap.put("TCL MONTHLY SELLOUT TREND_Month", sizeTo.get(j)
							.get("SPEC").toString());
					allDataMapOne.put(lastYear
							+ sizeTo.get(j).get("SPEC").toString(), rowMap);
				}
			}

			System.out.println(allDataMapOne);

			allDataMapOne.put("TotalTwo", rowMapO);

			LinkedHashMap<String, Object> rw = new LinkedHashMap<String, Object>();
			allDataMapOne.put("S", rw);

			lastDay = endDateOne;
			lastDays = lastDay.split("-");
			startDay = beginDateOne;
			if (toYear[1].equals("01")) {
				Month = "Jan";
			} else if (toYear[1].equals("02")) {
				Month = "Feb";
			} else if (toYear[1].equals("03")) {
				Month = "Mar";
			} else if (toYear[1].equals("04")) {
				Month = "Apr";
			} else if (toYear[1].equals("05")) {
				Month = "May";
			} else if (toYear[1].equals("06")) {
				Month = "June";
			} else if (toYear[1].equals("07")) {
				Month = "July";
			} else if (toYear[1].equals("08")) {
				Month = "August";
			} else if (toYear[1].equals("10")) {
				Month = "October";
			} else if (toYear[1].equals("11")) {
				Month = "November";
			} else if (toYear[1].equals("12")) {
				Month = "December";
			}

			sizeDatasOneData = excelService.selectQtyTotalBySpecTypeByAc(spec,
					startDay, lastDay, searchStr, conditions,WebPageUtil.isHQRole());

			for (int z = 0; z < sizeDatasOneData.size(); z++) {
				LinkedHashMap<String, Object> colMap = sizeDatasOneData.get(z);
				String key = toYear[0] + colMap.get("SPEC").toString();
				if (allDataMapOne.get(key) != null) {
					LinkedHashMap<String, Object> rowMap = allDataMapOne
							.get(key);

					BigDecimal bd = new BigDecimal(sizeDatasOneData.get(z)
							.get("quantity").toString());
					String am = bd.toPlainString();
					// 分成12个月份查，查一个放一个
					rowMap.put("TCL MONTHLY SELLOUT TREND_" + Month + "", am);

					rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
							+ toYear[0]);
					rowMap.put("TCL MONTHLY SELLOUT TREND_Month",
							sizeDatasOneData.get(z).get("SPEC").toString());

					allDataMapOne.put(key, rowMap);
				} else {
					LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
					rowList.addLast(colMap);

					LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();

					BigDecimal bd = new BigDecimal(sizeDatasOneData.get(z)
							.get("quantity").toString());
					String am = bd.toPlainString();
					// 分成12个月份查，查一个放一个
					rowMap.put("TCL MONTHLY SELLOUT TREND_" + Month + "", am);

					rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
							+ toYear[0]);
					rowMap.put("TCL MONTHLY SELLOUT TREND_Month",
							sizeDatasOneData.get(z).get("SPEC").toString());

					allDataMapOne.put(key, rowMap);

				}
			}

			sizeDatasTwTotal = excelService.selectSaleTotalBySizeByAc(startDay,
					lastDay, searchStr, conditions,WebPageUtil.isHQRole());
			if (sizeDatasTwTotal.size() == 1) {
				BigDecimal bd = new BigDecimal(sizeDatasTwTotal.get(0)
						.get("quantity").toString());
				String am = bd.toPlainString();

				rowMapOne.put("TCL MONTHLY SELLOUT TREND_" + Month + "", am);
				rowMapOne.put("TCL MONTHLY SELLOUT TREND_Month", "TOTAL");

			}

			// }

			if (sizeTo.size() == 0) {
				for (int j = 0; j < sizeLa.size(); j++) {
					LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();

					rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
							+ lastYear);
					rowMap.put("TCL MONTHLY SELLOUT TREND_Month", sizeLa.get(j)
							.get("SPEC").toString());
					allDataMapOne.put(toYear[0]
							+ sizeLa.get(j).get("SPEC").toString(), rowMap);
				}
			}

			if (sizeTo.size() < sizeLa.size() && sizeTo.size() != 0) {
				for (int i = 0; i < sizeLa.size() - sizeTo.size(); i++) {
					LinkedHashMap<String, Object> r = new LinkedHashMap<String, Object>();
					allDataMapOne.put("I" + i, r);
				}
			}

			allDataMapOne.put("TotalOne", rowMapOne);
			Set<String> sizeSet = allDataMapOne.keySet();

			Iterator<String> sizeIter = sizeSet.iterator();
			while (sizeIter.hasNext()) {
				String key = sizeIter.next();
				LinkedHashMap<String, Object> rowMap = allDataMapOne.get(key);
				dataList.add(rowMap);

				Set<String> rowSet = rowMap.keySet();
				Iterator<String> rowIter = rowSet.iterator();
			}

			int stRow = 13;
			int enRow = 22;

			if (sizeDatas.size() == 1) {
				stRow = 7;
				enRow = 10;
			}  else if (sizeDatas.size() == 2) {
				stRow = 8;
				enRow = 12;
			}  else if (sizeDatas.size() == 3) {
				stRow = 9;
				enRow = 14;
			} else if (sizeDatas.size() == 4) {
				stRow = 10;
				enRow = 16;
			}else if (sizeDatas.size() == 5) {
				stRow = 11;
				enRow = 18;
			}else if (sizeDatas.size() == 6) {
				stRow = 12;
				enRow = 20;
			}else if (sizeDatas.size() == 7) {
				stRow = 13;
				enRow = 22;
			} else if (sizeDatas.size() == 8) {
				stRow = 14;
				enRow = 24;
			}  
			HashMap<String, Object> dataOne = new HashMap<String, Object>();
			dataOne.put("TCL MONTHLY SELLOUT TREND_Month", "Growth vs. "
					+ lastYear);
			dataOne.put("TCL MONTHLY SELLOUT TREND_Jan", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_Feb", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_Mar", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_Apr", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_May", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_June", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_July", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_August", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_September", "TEXT((K"
					+ enRow + "-K" + stRow + ")/K" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_October", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_November", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_December", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_TOTAL", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataList.add(dataOne);

			System.out.println("+++++++++++++++YTD 第一个++++++++++++++++++"
					+ (System.currentTimeMillis() - start));

			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapTwo = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
			LinkedHashMap<String, Object> rowMapTwo = new LinkedHashMap<String, Object>();

			LinkedList<LinkedHashMap<String, Object>> dataListTwo = new LinkedList<LinkedHashMap<String, Object>>();

			start = System.currentTimeMillis();

			lastDay = endDateOne;
			lastDays = lastDay.split("-");
			startDay = beginDateOne;

			if (toYear[1].equals("01")) {
				Month = "Jan";
			} else if (toYear[1].equals("02")) {
				Month = "Feb";
			} else if (toYear[1].equals("03")) {
				Month = "Mar";
			} else if (toYear[1].equals("04")) {
				Month = "Apr";
			} else if (toYear[1].equals("05")) {
				Month = "May";
			} else if (toYear[1].equals("06")) {
				Month = "June";
			} else if (toYear[1].equals("07")) {
				Month = "July";
			} else if (toYear[1].equals("08")) {
				Month = "August";
			} else if (toYear[1].equals("10")) {
				Month = "October";
			} else if (toYear[1].equals("11")) {
				Month = "November";
			} else if (toYear[1].equals("12")) {
				Month = "December";
			}

			List<LinkedHashMap<String, Object>> selectYearDataBySpecOne = excelService
					.selectQtyTotalBySpecYearByAc(spec, startDay, lastDay,
							searchStr, conditions,WebPageUtil.isHQRole());

			for (int z = 0; z < selectYearDataBySpecOne.size(); z++) {
				LinkedHashMap<String, Object> colMap = selectYearDataBySpecOne
						.get(z);
				String key = colMap.get("model").toString();
				if (allDataMapTwo.get(key) != null) {
					LinkedHashMap<String, Object> rowMap = allDataMapTwo
							.get(key);

					rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
							+ "_Category",
							selectYearDataBySpecOne.get(z).get("SPEC"));

					rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
							+ "_MODELS",
							selectYearDataBySpecOne.get(z).get("model"));

					BigDecimal bd = new BigDecimal(selectYearDataBySpecOne
							.get(z).get("quantity").toString());

					String am = bd.toPlainString();
					rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_"
							+ Month + "", am);

					allDataMapTwo.put(key, rowMap);
				} else {
					LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
					rowList.addLast(colMap);

					LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();
					rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
							+ "_Category",
							selectYearDataBySpecOne.get(z).get("SPEC"));

					rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
							+ "_MODELS",
							selectYearDataBySpecOne.get(z).get("model"));

					BigDecimal bd = new BigDecimal(selectYearDataBySpecOne
							.get(z).get("quantity").toString());
					String am = bd.toPlainString();
					rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_"
							+ Month + "", am);
					allDataMapTwo.put(key, rowMap);

				}
			}
			List<HashMap<String, Object>> sizeDatasOneTotal = excelService
					.selectQtyTotalBySpecTotalYearByAc(startDay, lastDay,
							searchStr, conditions,WebPageUtil.isHQRole());
			for (int s = 0; s < sizeDatasOneTotal.size(); s++) {
				HashMap<String, Object> colMap = sizeDatasOneTotal.get(s);
				rowMapTwo.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
						+ "_Category", "GRAND TOTAL");

				BigDecimal bd = new BigDecimal(sizeDatasOneTotal.get(0)
						.get("quantity").toString());
				String am = bd.toPlainString();

				rowMapTwo.put("SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_"
						+ Month + "", am);

			}

			// }
			allDataMapTwo.put("TotalTwo", rowMapTwo);

			Set<String> sizeSetTen = allDataMapTwo.keySet();

			Iterator<String> sizeIterTen = sizeSetTen.iterator();
			LinkedHashMap<String, Object> totalMapTen = new LinkedHashMap<String, Object>();
			while (sizeIterTen.hasNext()) {
				String key = sizeIterTen.next();
				LinkedHashMap<String, Object> rowMap = allDataMapTwo.get(key);
				dataListTwo.add(rowMap);

				Set<String> rowSet = rowMap.keySet();
				Iterator<String> rowIter = rowSet.iterator();
			}

			System.out.println("+++++++++++++++YTD 第二个++++++++++++++++++"
					+ (System.currentTimeMillis() - start));

			// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
			Sheet sheet = wb.createSheet(toYear[0] + " YTD sellout");
			sheet.setZoom(3, 4);
			// 创建字体
			Font fontinfo = wb.createFont();
			fontinfo.setFontHeightInPoints((short) 11); // 字体大小
			fontinfo.setFontName("Trebuchet MS");
			Font fonthead = wb.createFont();
			fonthead.setFontHeightInPoints((short) 12);
			fonthead.setFontName("Trebuchet MS");

			// colSplit, rowSplit,leftmostColumn, topRow,
			sheet.createFreezePane(3, 5, 3, 5);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat
					.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
														// hh:mm:ss

			CellStyle cellStylehead = wb.createCellStyle();// 表头样式
			cellStylehead.setFont(fonthead);
			cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			CellStyle cellStyleWHITE = wb.createCellStyle();// 表头样式
			cellStyleWHITE.setFont(fonthead);
			cellStyleWHITE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleWHITE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleWHITE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyleWHITE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleWHITE.setWrapText(true);

			CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fonthead);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
			cellStyleRED.setFont(fonthead);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);
			CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fonthead);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleYellow.setWrapText(true);

			CellStyle cellStylePERCENT = wb.createCellStyle();// 表头样式
			cellStylePERCENT.setFont(fonthead);
			cellStylePERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePERCENT.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePERCENT.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT
					.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStylePERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePERCENT.setWrapText(true);

			CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			Row row = sheet.createRow((short) 0);
			// 创建这一行的一列，即创建单元格(CELL)
			Cell cell = row.createCell((short) 0);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("TCL " + partyName);// 标题

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			row = sheet.createRow((short) 1);
			// 创建这一行的一列，即创建单元格(CELL)
			cell = row.createCell((short) 1);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("CUMULATIVE SELLOUT");// 标题

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			row = sheet.createRow((short) 2);
			// 创建这一行的一列，即创建单元格(CELL)
			cell = row.createCell((short) 1);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("YTD- " + toYear[0]);// 标题

			CellStyle contextstyle = wb.createCellStyle();
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			contextstyle.setFont(fontinfo);
			// 从17行开始放另一个表格的表头

			int rows_max = 0; // 最大的一个项有几个子项

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map map = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 3);
				for (int i = 0; i < header.length; i++) {
					cell.setCellStyle(cellStyleWHITE);
					String headerTemp = header[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(3,
								rows_max + 2, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (map.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (map.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < header.length; j++) {
							if (header[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 3,
								k + 3, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 3, k
									+ 3 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerTwo.length; i++) {
				String h = headerTwo[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapTwo = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + dataList.size() + 15);
				for (int i = 0; i < headerTwo.length; i++) {
					cell.setCellStyle(cellStyleWHITE);
					String headerTemp = headerTwo[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(dataList
								.size() + 15, rows_max + dataList.size() + 14,
								(num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapTwo.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapTwo.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerTwo.length; j++) {
							if (headerTwo[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k
								+ dataList.size() + 15, k + dataList.size()
								+ 15, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k
									+ dataList.size() + 15, k + dataList.size()
									+ 15 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int d = 0; d < dataList.size(); d++) {
				DataFormat df = wb.createDataFormat();

				HashMap<String, Object> dataMap = dataList.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 2 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fields.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fields[c]) != null
							&& dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fields[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fields[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());

						}

						else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c])
									.toString());
						}
						if (sizeDatas.size() == 7) {
							if (d == 7 || d == 16) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 8) {
							if (d == 8 || d == 18) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 6) {
							if (d == 6 || d == 14) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 5) {
							if (d == 5 || d == 12) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 4) {
							if (d == 4 || d == 10) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 3) {
							if (d == 3 || d == 8) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 2) {
							if (d == 2 || d == 6) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 1) {
							if (d == 1 || d == 4) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						}
					} else {
						contentCell.setCellValue("");
					}
					if (d == dataList.size() - 1) {
						cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleGreen);

					}

				}
				if (sizeDatas.size() == 7) {
					sheet.addMergedRegion(new CellRangeAddress(5, 12, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(14, 21, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(13, 13, 0, 15));

				} else if (sizeDatas.size() == 8) {
					sheet.addMergedRegion(new CellRangeAddress(5, 13, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(15, 22, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(14, 14, 0, 15));
				} else if (sizeDatas.size() == 6) {
					sheet.addMergedRegion(new CellRangeAddress(5, 11, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(13, 20, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(12, 12, 0, 15));
				} else if (sizeDatas.size() == 5) {
					sheet.addMergedRegion(new CellRangeAddress(5, 10, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(12, 19, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(11, 11, 0, 15));
				} else if (sizeDatas.size() == 4) {
					sheet.addMergedRegion(new CellRangeAddress(5, 9, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(11, 18, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(10, 10, 0, 15));
				} else if (sizeDatas.size() == 3) {
					sheet.addMergedRegion(new CellRangeAddress(5, 8, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(10, 17, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(9, 9, 0, 15));
				} else if (sizeDatas.size() == 2) {
					sheet.addMergedRegion(new CellRangeAddress(5, 7, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(9, 16, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 15));
				} else if (sizeDatas.size() == 1) {
					sheet.addMergedRegion(new CellRangeAddress(5, 6, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(8, 15, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 15));
				}

			}

			for (int d = 0; d < dataListTwo.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataListTwo.get(d);

				// 创建一行
				Row datarow = sheet.createRow(d + dataList.size() + 14
						+ rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，
				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsTwo.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsTwo[c]) != null
							&& dataMap.get(fieldsTwo[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTwo[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTwo[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTwo[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsTwo[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsTwo[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsTwo[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsTwo[c])
									.toString());
						}

						if (d != dataListTwo.size() - 1) {
							HashMap<String, Object> dataMapTwo = dataListTwo
									.get(d + 1);
							if (dataMap.get(fieldsTwo[c]).equals(
									dataMapTwo.get(fieldsTwo[c]))) {
								sheet.addMergedRegion(new CellRangeAddress(d
										+ dataList.size() + 14 + rows_max + 1,
										d + dataList.size() + 14 + rows_max + 1
												+ 1, 0, 0));

							}
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListTwo.size() - 1) {
						cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleGreen);
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void quaSellOutSummary(SXSSFWorkbook wb,
			LinkedList<HashMap<String, Object>> dateList, String partyName,
			String beginDateOne, String endDateOne,String tBeginDate,String tEndDate) {
		long startTime = System.currentTimeMillis();
		try {
			// ===========================================================
			// Sell-out Summary
			// ====================================================================//
			String[] days = beginDateOne.split("-");
			// 创建一个sheet【Sell-out Summary】
			DateUtil.year = Integer.parseInt(days[0]);
			DateUtil.month = Integer.parseInt(days[1]);
			String beginDate = beginDateOne;
			String endDate = endDateOne;

			// 表头数据
			String[] headers = { "Monthly Trend", "TTL AIRCON SO_Qty",
					"TTL AIRCON SO_Amt", "TARGET", "ACH" };
			// 按照对应格式将表头传入
			ArrayList columns = new ArrayList();
			for (int i = 0; i < headers.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headers[i]);
				columnMap.put("field", headers[i]);
				columns.add(columnMap);
			}
			String[] header = new String[columns.size()];
			String[] fields = new String[columns.size()];
			for (int i = 0, l = columns.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columns.get(i);
				header[i] = columnMap.get("header").toString();
				fields[i] = columnMap.get("field").toString();

			}

			String[] headersTwo = { "Rank", "Regional Head", "AREA",
					"No of Shops",  "AC FPS", "TTL AIRCON SO_Qty",
					"TTL AIRCON SO_Amt", "TARGET", "ACH" };

			// 按照对应格式将表头传入
			ArrayList columnsTwo = new ArrayList();
			for (int i = 0; i < headersTwo.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersTwo[i]);
				columnMap.put("field", headersTwo[i]);
				columnsTwo.add(columnMap);
			}

			String[] headerTwo = new String[columnsTwo.size()];
			String[] fieldsTwo = new String[columnsTwo.size()];
			for (int i = 0, l = columnsTwo.size(); i < l; i++) {
				HashMap columnMap = (HashMap) columnsTwo.get(i);
				headerTwo[i] = columnMap.get("header").toString();
				fieldsTwo[i] = columnMap.get("field").toString();

			}

			// 表头数据
			String[] headersThree = { "Rank", "Saleman", "Account",
					"No of Shops", "No of FPS", "TTL AIRCON SO_Qty",
					"TTL AIRCON SO_Amt", "TARGET", "ACH" };

			ArrayList columnsThree = new ArrayList();
			for (int i = 0; i < headersThree.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersThree[i]);
				columnMap.put("field", headersThree[i]);
				columnsThree.add(columnMap);
			}

			String[] headerThree = new String[columnsThree.size()];
			String[] fieldsThree = new String[columnsThree.size()];
			for (int i = 0, l = columnsThree.size(); i < l; i++) {
				HashMap columnMap = (HashMap) columnsThree.get(i);
				headerThree[i] = columnMap.get("header").toString();
				fieldsThree[i] = columnMap.get("field").toString();

			}

			// 表头数据
			String[] headersFour = { "Rank", "Acfo", "Area", "No of Shops",
					"No of FPS",  "TTL AIRCON SO_Qty",
					"TTL AIRCON SO_Amt", "TARGET", "ACH" };

			ArrayList columnsFour = new ArrayList();
			for (int i = 0; i < headersFour.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersFour[i]);
				columnMap.put("field", headersFour[i]);
				columnsFour.add(columnMap);
			}

			String[] headerFour = new String[columnsFour.size()];
			String[] fieldsFour = new String[columnsFour.size()];
			for (int i = 0, l = columnsFour.size(); i < l; i++) {
				HashMap columnMap = (HashMap) columnsFour.get(i);
				headerFour[i] = columnMap.get("header").toString();
				fieldsFour[i] = columnMap.get("field").toString();

			}

			String searchStr = null;
			String conditions = "";
			String center = "";
			String country = "";
			String region = "";
			String office = "";

			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			if (!WebPageUtil.isHAdmin()) {
				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")
						|| request.getParameter("country") != null
						&& !request.getParameter("country").equals("")
						|| request.getParameter("region") != null
						&& !request.getParameter("region").equals("")
						|| request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {

					if (request.getParameter("center") != null
							&& !request.getParameter("center").equals("")) {
						center = request.getParameter("center");
						conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
								+ center + "')   ";
					}

					if (request.getParameter("country") != null
							&& !request.getParameter("country").equals("")) {
						country = request.getParameter("country");
						conditions = "  pa.country_id= " + country + "  ";
					}
					if (request.getParameter("region") != null
							&& !request.getParameter("region").equals("")) {
						region = request.getParameter("region");
						conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
								+ region
								+ "'  OR PARTY_ID='"+region+"'))  ";
					}
					if (request.getParameter("office") != null
							&& !request.getParameter("office").equals("")) {
						office = request.getParameter("office");
						conditions = "    pa.party_id IN ('" + office + "')  ";
					}

				} else {
					if (null != userPartyIds && !"".equals(userPartyIds)) {
						conditions = "  pa.party_id in (" + userPartyIds + ")  ";
					} else {
						conditions = "  1=2  ";
					}
				}

			} else {
				conditions = " 1=1 ";

			}
			
			if (request.getParameter("level") != null
					&& !request.getParameter("level").equals("")) {
				conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
			}

			// 用于放置表格数据
			LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();

			for (int i = 0; i < dateList.size(); i++) {
				beginDate = dateList.get(i).get("beginDate").toString();
				endDate = dateList.get(i).get("endDate").toString();
				String[] be = beginDateOne.split("-");
				String[] en = endDateOne.split("-");
				int day = Integer.parseInt(en[2]) - Integer.parseInt(be[2]) + 1;
				// 用于放置表格数据
				HashMap<String, Object> data = new HashMap<String, Object>();
				int j = i + 1;
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(format.parse(beginDate));
				Date time = format.parse(format.format(rightNow.getTime()));
				String timew = mm.format(time);
				data.put("Monthly Trend", timew);
				List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
						beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());
				double saleSum = 0.0;
				if (sumDatas.size() == 0) {
					data.put("TTL AIRCON SO_Qty", "");
					data.put("TTL AIRCON SO_Amt", "");
					saleSum = 0.0;
				} else {
					for (Excel excel : sumDatas) {
						int row = i + 5;
						data.put("TTL AIRCON SO_Qty", excel.getSaleQty());
						data.put("TTL AIRCON SO_Amt", excel.getSaleSum());
						data.put("Ave.SO per DAY_Qty", "SUM(D" + row + "/"
								+ day + ")");
						data.put("Ave.SO per DAY_Amount", "SUM(E" + row + "/"
								+ day + ")");
						saleSum = Double.parseDouble(excel.getSaleSum());
					}
				}

				
				
			
				
				
				
				
				
				// 查询当月销售目标总额
				List<Excel> TargetDatas = excelService.selectTargetDataBySum(
						beginDateOne, endDateOne, searchStr, conditions, tBeginDate, tEndDate,WebPageUtil.isHQRole(),2);
				if (TargetDatas.size() > 0) {
					BigDecimal bd = new BigDecimal(TargetDatas.get(0)
							.getTargetSum());
					String am = bd.toPlainString();
					data.put("TARGET", am);
					Double targetSum = TargetDatas.get(0).getTargetSum();
					double ach = saleSum / targetSum * 100;
					long lnum = Math.round(ach);
					data.put("ACH", lnum + "%");

					bd = new BigDecimal(TargetDatas.get(0).getChallengeSum());
					am = bd.toPlainString();
					data.put("TV ACHIEVEMENT_Challenge TV Target", am);
					Double ChallengeTargetSum = Double.parseDouble(am);
					ach = saleSum / ChallengeTargetSum * 100;
					lnum = Math.round(ach);
					data.put("TV ACHIEVEMENT_Challenge TV  Ach", lnum + "%");

				}
				dataList.add(data);
			}
			HashMap<String, Object> dataOne = new HashMap<String, Object>();
			String[] en = endDateOne.split("-");
			int day = Integer.parseInt(en[2]);

			// 按周查询销售总额
			List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
					beginDateOne, endDateOne, searchStr, conditions,WebPageUtil.isHQRole());
			double saleSum = 0.0;
			if (sumDatas.size() == 0) {
				dataOne.put("Monthly Trend", "Total");
				dataOne.put("TTL AIRCON SO_Qty", "");
				dataOne.put("TTL AIRCON SO_Amt", "");
				saleSum = 0.0;
			} else {
				for (Excel excel : sumDatas) {
					dataOne.put("Monthly Trend", "Total");

					int row = dataList.size() + 5;
					dataOne.put("TTL AIRCON SO_Qty", excel.getSaleQty());
					dataOne.put("TTL AIRCON SO_Amt", excel.getSaleSum());
					dataOne.put("Ave.SO per DAY_Qty", "SUM(D" + row + "/" + day
							+ ")");
					dataOne.put("Ave.SO per DAY_Amount", "SUM(E" + row + "/"
							+ day + ")");

					saleSum = Double.parseDouble(excel.getSaleSum());
				}
			}

			// 查询当月销售目标总额
			List<Excel> TargetDatas = excelService.selectTargetDataBySum(
					beginDateOne, endDateOne, searchStr, conditions, tBeginDate, tEndDate,WebPageUtil.isHQRole(),2);
			if (TargetDatas.size() > 0) {
				BigDecimal bd = new BigDecimal(TargetDatas.get(0)
						.getTargetSum());
				String am = bd.toPlainString();
				dataOne.put("TARGET", am);
				Double targetSum = TargetDatas.get(0).getTargetSum();
				double ach = saleSum / targetSum * 100;
				long lnum = Math.round(ach);
				dataOne.put("ACH", lnum + "%");

				bd = new BigDecimal(TargetDatas.get(0).getChallengeSum());
				am = bd.toPlainString();
				dataOne.put("TV ACHIEVEMENT_Challenge TV Target", am);
				Double ChallengeTargetSum = Double.parseDouble(am);
				ach = saleSum / ChallengeTargetSum * 100;
				lnum = Math.round(ach);
				dataOne.put("TV ACHIEVEMENT_Challenge TV  Ach", lnum + "%");

			}
			dataList.add(dataOne);
						LinkedList<HashMap<String, Object>> dataListTwo = new LinkedList<HashMap<String, Object>>();

						List<HashMap<String, Object>> areaDatasByAc = excelService
								.selectDataByAreaByAc(beginDateOne, endDateOne, searchStr,
										conditions,WebPageUtil.isHQRole());
						
						List<HashMap<String, Object>> re = excelService
								.selectRegionalHeadByParty(searchStr, conditions);
						int shops = 0;
						List<HashMap<String, Object>> targetDatas = excelService
								.selectTargetByArea( searchStr,
										conditions, tBeginDate, tEndDate,WebPageUtil.isHQRole(),2);
						for (int j = 0; j < areaDatasByAc.size(); j++) {
							HashMap<String, Object> dataMap = new HashMap<String, Object>();

							for (int i = 0; i < re.size(); i++) {
								if (areaDatasByAc.get(j).get("partyId")
										.equals(re.get(i).get("PARTY_ID"))) {
									dataMap.put("Regional Head", re.get(i).get("userName"));
								}
							}

							dataMap.put("AREA", areaDatasByAc.get(j).get("AREA"));
							dataMap.put("No of Shops", areaDatasByAc.get(j).get("noOfShops"));
							dataMap.put("AC FPS",  areaDatasByAc.get(j).get("tvFps"));
							BigDecimal bd = new BigDecimal(areaDatasByAc.get(j).get("saleQty")
									.toString());

							bd = new BigDecimal(areaDatasByAc.get(j).get("saleQty")
									.toString());

							dataMap.put("TTL AIRCON SO_Qty", bd.longValue());
							bd = new BigDecimal(areaDatasByAc.get(j).get("saleSum").toString());
							String  saleSumOne = bd.toPlainString();
							dataMap.put("TTL AIRCON SO_Amt", saleSumOne);
							 saleSum = Double.parseDouble(saleSumOne);
							
							for (HashMap<String, Object> hashMap : targetDatas) {
								if(hashMap.get("PARTY_ID")
										.toString().equals(areaDatasByAc.get(j).get("partyId").toString())){
									BigDecimal a = new BigDecimal(hashMap.get("targetSum")
											.toString());
									String targetSumOne = a.toPlainString();
									dataMap.put("TARGET", targetSumOne);
									Double targetSum = Double.parseDouble(targetSumOne);
									double ach = saleSum / targetSum * 100;
									long lnum = Math.round(ach);
									dataMap.put("ACH", lnum + "%");

								}
								
							}
							
							
							
						
							dataListTwo.add(dataMap);
						}
					
						DateUtil.Order(dataListTwo, "ACH");
						for (int i = 0; i < dataListTwo.size(); i++) {
							dataListTwo.get(i).put("Rank", i + 1);
						}
						HashMap<String, Object> data = new HashMap<String, Object>();
						int rows = 17 + dataListTwo.size() - 1;
						int end = rows + 1;
						data.put("Regional Head", "Total");
						data.put("No of Shops", "SUM(D17:D" + rows + ")");
						data.put("AC FPS", "SUM(E17:E" + rows + ")");
						

						data.put("TTL AIRCON SO_Qty", "SUM(F17:F" + rows + ")");
						data.put("TTL AIRCON SO_Amt", "SUM(G17:G" + rows + ")");
						data.put("TARGET", "SUM(H17:H" + rows + ")");

						data.put("ACH", "TEXT(G" + end + "/H" + end + ",\"0.00%\")");

						dataListTwo.add(data);

						LinkedList<HashMap<String, Object>> dataListThree = new LinkedList<HashMap<String, Object>>();

						
						List<HashMap<String, Object>> salemanDatasByAc = excelService
								.selectTargetBySalemanByAc(beginDateOne, endDateOne, searchStr,
										conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);
						
						List<HashMap<String, Object>> Account = excelService
								.selectPartyNameByuser(searchStr, conditions);

						List<HashMap<String, Object>> saleFps = excelService
								.selectFpsNameByShop(beginDateOne, endDateOne, searchStr,
										conditions);

						for (int i = 0; i < salemanDatasByAc.size(); i++) {
							HashMap<String, Object> dataMap = new HashMap<String, Object>();
							// dataMap.put("Rank", i + 1);
							dataMap.put("Saleman", salemanDatasByAc.get(i).get("userName"));

							if (Account.size() > 1) {
								String a = "";
								for (int k = 0; k < Account.size(); k++) {
									if (salemanDatasByAc.get(i).get("userId")
											.equals(Account.get(k).get("userId"))) {
										a += Account.get(k).get("PARTY_NAME") + ",";
									}

								}
								dataMap.put("Account", a.substring(0, a.length() - 1));
							}
							int tvFps = 0;
							for (int j = 0; j < saleFps.size(); j++) {
								if (saleFps.get(j).get("USER")
										.equals(salemanDatasByAc.get(i).get("userId"))) {
									tvFps = Integer.parseInt(saleFps.get(j).get("TVFPS")
											.toString());
									dataMap.put("No of FPS", tvFps);

								}
							}

							dataMap.put("No of Shops", salemanDatasByAc.get(i).get("noOfShops"));

							BigDecimal bd = new BigDecimal(salemanDatasByAc.get(i)
									.get("saleQty").toString());
							dataMap.put("TTL AIRCON SO_Qty", bd.longValue());
							BigDecimal a = new BigDecimal(salemanDatasByAc.get(i)
									.get("saleSum").toString());
							String saleSumOne = a.toPlainString();
							dataMap.put("TTL AIRCON SO_Amt", saleSumOne);
						
							 saleSum=Double.parseDouble(saleSumOne);

							BigDecimal b = new BigDecimal(salemanDatasByAc.get(i)
									.get("targetSum").toString());
							String targetSumOne = b.toPlainString();
							dataMap.put("TARGET", targetSumOne);
							Double targetSum = Double.parseDouble(targetSumOne);
							double ach = saleSum / targetSum * 100;
							long lnum = Math.round(ach);
							dataMap.put("ACH", lnum + "%");

						
							

						
							
							
							dataListThree.add(dataMap);

						}

						DateUtil.Order(dataListThree, "ACH");
						for (int i = 0; i < dataListThree.size(); i++) {

							dataListThree.get(i).put("Rank", i + 1);
						}
						HashMap<String, Object> dataThree = new HashMap<String, Object>();
						int stratRow = 17 + dataListTwo.size() + 10;
						int rowsBysale = 17 + dataListTwo.size() + 10
								+ dataListThree.size() - 1;
						end = rowsBysale + 1;
						dataThree.put("Saleman", "Total");
						dataThree.put("No of Shops", "SUM(D" + stratRow + ":D" + rowsBysale
								+ ")");
						dataThree.put("No of FPS", "SUM(E" + stratRow + ":E" + rowsBysale
								+ ")");

						dataThree.put("TTL AIRCON SO_Qty", "SUM(F" + stratRow + ":F"
								+ rowsBysale + ")");
						dataThree.put("TTL AIRCON SO_Amt", "SUM(G" + stratRow + ":G"
								+ rowsBysale + ")");
						dataThree.put("TARGET", "SUM(H" + stratRow
								+ ":H" + rowsBysale + ")");

						dataThree.put("ACH", "TEXT(G" + end + "/H" + end
								+ ",\"0.00%\")");

					

						

						dataListThree.add(dataThree);

						LinkedList<HashMap<String, Object>> dataListFour = new LinkedList<HashMap<String, Object>>();

						
						List<HashMap<String, Object>> acfoDatasByAc = excelService
								.selectTargetByAcfoByAc(beginDateOne, endDateOne, searchStr,
										conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);
						
						List<HashMap<String, Object>> Area = excelService.selectAreaByUser(
								searchStr, conditions);

						List<HashMap<String, Object>> acfoFps = excelService
								.selectFpsNameByShop(beginDateOne, endDateOne, searchStr,
										conditions);

						for (int i = 0; i < acfoDatasByAc.size(); i++) {
							HashMap<String, Object> dataMap = new HashMap<String, Object>();
							dataMap.put("Rank", i + 1);
							dataMap.put("Acfo", acfoDatasByAc.get(i).get("userName"));

							if (Area.size() > 1) {
								String a = "";
								for (int k = 0; k < Area.size(); k++) {
									if (acfoDatasByAc.get(i).get("userId").toString()
											.equals(Area.get(k).get("userId"))) {

										a += Area.get(k).get("PARTY_NAME") + ",";
									}

								}
								if (a.length() > 0) {
									dataMap.put("Area", a.substring(0, a.length() - 1));
								}
							}

							int tvFps = 0;
							for (int j = 0; j < acfoFps.size(); j++) {
								if (acfoFps.get(j).get("USER")
										.equals(acfoDatasByAc.get(i).get("userId"))) {
									tvFps = Integer.parseInt(acfoFps.get(j).get("TVFPS")
											.toString());
									dataMap.put("No of FPS", tvFps);

								}
							}

							dataMap.put("No of Shops", acfoDatasByAc.get(i).get("noOfShops"));

							BigDecimal a = new BigDecimal(acfoDatasByAc.get(i).get("saleQty")
									.toString());
							dataMap.put("TTL AIRCON SO_Qty", a.longValue());
							a = new BigDecimal(acfoDatasByAc.get(i).get("saleSum").toString());
							String saleSumOne = a.toPlainString();
							dataMap.put("TTL AIRCON SO_Amt", saleSumOne);
							
							
							 saleSum = Double.parseDouble(saleSumOne);

							BigDecimal b = new BigDecimal(acfoDatasByAc.get(i).get("targetSum")
									.toString());
							String targetSumOne = b.toPlainString();
							dataMap.put("TARGET", targetSumOne);
							Double targetSum = Double.parseDouble(targetSumOne);
							double ach = saleSum / targetSum * 100;
							long lnum = Math.round(ach);
							dataMap.put("ACH", lnum + "%");


					
									

							
						
							dataListFour.add(dataMap);

						}
						DateUtil.Order(dataListFour, "ACH");
						for (int i = 0; i < dataListFour.size(); i++) {
							dataListFour.get(i).put("Rank", i + 1);
						}
						HashMap<String, Object> dataFour = new HashMap<String, Object>();
						stratRow = 17 + dataListTwo.size() + 10 + dataListThree.size() + 10;
						rowsBysale = 17 + dataListTwo.size() + 10 + dataListThree.size()
								+ 9 + dataListFour.size();
						end = rowsBysale + 1;
						dataFour.put("Acfo", "Total");
						dataFour.put("No of Shops", "SUM(D" + stratRow + ":D" + rowsBysale
								+ ")");
						dataFour.put("No of FPS", "SUM(E" + stratRow + ":E" + rowsBysale
								+ ")");

						dataFour.put("TTL AIRCON SO_Qty", "SUM(F" + stratRow + ":F"
								+ rowsBysale + ")");
						dataFour.put("TTL AIRCON SO_Amt", "SUM(G" + stratRow + ":G"
								+ rowsBysale + ")");
						dataFour.put("TARGET", "SUM(H" + stratRow
								+ ":H" + rowsBysale + ")");

						dataFour.put("ACH", "TEXT(G" + end + "/H" + end
								+ ",\"0.00%\")");

						

			dataListFour.add(dataFour);

			System.out.println("====Sell-out Summary ==="
					+ (System.currentTimeMillis() - startTime));
			// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
			Sheet sheet = wb.createSheet("Sell-out Summary");
			sheet.setZoom(3, 4);
			// 创建字体
			Font fontinfo = wb.createFont();
			fontinfo.setFontHeightInPoints((short) 10); // 字体大小
			fontinfo.setFontName("Trebuchet MS");
			Font fonthead = wb.createFont();
			fonthead.setFontHeightInPoints((short) 10);
			fonthead.setFontName("Trebuchet MS");

			// colSplit, rowSplit,leftmostColumn, topRow,
			sheet.createFreezePane(3, 2, 3, 2);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat
					.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
														// hh:mm:ss

			CellStyle cellStylehead = wb.createCellStyle();// 表头样式
			cellStylehead.setFont(fonthead);
			cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylehead.setWrapText(true);

			CellStyle cellStyleLIGHTBLUE = wb.createCellStyle();// 表头样式
			cellStyleLIGHTBLUE.setFont(fonthead);
			cellStyleLIGHTBLUE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleLIGHTBLUE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleLIGHTBLUE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleLIGHTBLUE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleLIGHTBLUE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleLIGHTBLUE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleLIGHTBLUE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleLIGHTBLUE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleLIGHTBLUE.setTopBorderColor(HSSFColor.ROYAL_BLUE.index);
			cellStyleLIGHTBLUE
					.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
			cellStyleLIGHTBLUE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleLIGHTBLUE.setWrapText(true);

			CellStyle cellStyleWHITE = wb.createCellStyle();// 表头样式
			cellStyleWHITE.setFont(fonthead);
			cellStyleWHITE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleWHITE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleWHITE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyleWHITE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleWHITE.setWrapText(true);

			CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fonthead);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fonthead);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleYellow.setWrapText(true);

			CellStyle cellStylePERCENT = wb.createCellStyle();// 表头样式
			cellStylePERCENT.setFont(fonthead);
			cellStylePERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePERCENT.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePERCENT.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT
					.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStylePERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePERCENT.setWrapText(true);

			CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
			cellStyleRED.setFont(fonthead);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);

			CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			CellStyle contextstyle = wb.createCellStyle();
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			contextstyle.setFont(fontinfo);
			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			Row row = sheet.createRow((short) 0);
			// 创建这一行的一列，即创建单元格(CELL)
			Cell cell = row.createCell((short) 0);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("TCL " + partyName);// 标题

			// 第二行
			Row rowSec = sheet.createRow((short) 1);
			cell = rowSec.createCell((short) 0);
			cell.setCellStyle(cellStylename);
			cell.setCellValue("SELL-OUT SUMMARY");
			// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));

			// 从17行开始放另一个表格的表头

			int rows_max = 0; // 最大的一个项有几个子项

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map map = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 2);
				for (int i = 0; i < header.length; i++) {
					if (i <= 1) {
						cell.setCellStyle(cellStyleWHITE);
					} else if (i > 1 && i <= 3) {
						cell.setCellStyle(cellStyleLIGHTBLUE);
					} else if (i >= 4 && i < 8) {
						cell.setCellStyle(cellStylehead);
					} else {
						cell.setCellStyle(cellStyleGreen);
					}
					String headerTemp = header[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i + 2;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2,
								rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (map.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (map.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < header.length; j++) {
							if (header[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 2,
								k + 2, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k
									+ 2 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerTwo.length; i++) {
				String h = headerTwo[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapTwo = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + dataList.size() + 10);
				for (int i = 0; i < headerTwo.length; i++) {
					if (i < 7) {
						cell.setCellStyle(cellStyleWHITE);
					} else if (i >= 7 && i < 9) {
						cell.setCellStyle(cellStyleLIGHTBLUE);
					} else if (i >= 9 && i < 15) {
						cell.setCellStyle(cellStylehead);
					} else {
						cell.setCellStyle(cellStyleGreen);
					}
					String headerTemp = headerTwo[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(dataList
								.size() + 10, rows_max + dataList.size() + 9,
								(num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);
					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapTwo.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapTwo.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerTwo.length; j++) {
							if (headerTwo[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k
								+ dataList.size() + 10, k + dataList.size()
								+ 10, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k
									+ dataList.size() + 10, k + dataList.size()
									+ 10 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapTwo.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapTwo.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerThree.length; i++) {
				String h = headerThree[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapThree = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + dataList.size() + 10
						+ dataListTwo.size() + 10);
				for (int i = 0; i < headerThree.length; i++) {
					if (i < 6) {
						cell.setCellStyle(cellStyleWHITE);
					} else if (i >= 6 && i < 8) {
						cell.setCellStyle(cellStyleLIGHTBLUE);
					} else if (i >= 8 && i < 14) {
						cell.setCellStyle(cellStylehead);
					} else {
						cell.setCellStyle(cellStyleGreen);
					}
					String headerTemp = headerThree[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(dataList
								.size() + 10 + dataListTwo.size() + 10,
								rows_max + dataList.size() + 9
										+ dataListTwo.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapThree.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapThree.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerThree.length; j++) {
							if (headerThree[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k
								+ dataList.size() + 10 + dataListTwo.size()
								+ 10, k + dataList.size() + 10
								+ dataListTwo.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k
									+ dataList.size() + 10 + dataListTwo.size()
									+ 10, k + dataList.size() + 10
									+ dataListTwo.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapThree.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapThree.put(key, null);
						}
					}
				}
			}

			// ACFO
			for (int i = 0; i < headerFour.length; i++) {
				String h = headerFour[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapFour = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + dataList.size() + 10
						+ dataListTwo.size() + 10 + dataListThree.size() + 10);
				for (int i = 0; i < headerFour.length; i++) {
					if (i < 6) {
						cell.setCellStyle(cellStyleWHITE);
					} else if (i >= 6 && i < 8) {
						cell.setCellStyle(cellStyleLIGHTBLUE);
					} else if (i >= 8 && i < 14) {
						cell.setCellStyle(cellStylehead);
					} else {
						cell.setCellStyle(cellStyleGreen);
					}
					String headerTemp = headerFour[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(dataList
								.size()
								+ 10
								+ +dataListTwo.size()
								+ 10
								+ dataListThree.size() + 10, rows_max
								+ dataList.size() + 9 + dataListTwo.size() + 10
								+ dataListThree.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapFour.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapFour.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerFour.length; j++) {
							if (headerFour[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k
								+ dataList.size() + 10 + dataListTwo.size()
								+ 10 + dataListThree.size() + 10,

						k + dataList.size() + 10 + dataListTwo.size() + 10
								+ dataListThree.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k
									+ dataList.size() + 10 + dataListTwo.size()
									+ 10 + dataListThree.size() + 10, k
									+ dataList.size() + 10 + dataListTwo.size()
									+ 10 + dataListThree.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapFour.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapFour.put(key, null);
						}
					}
				}
			}
			DataFormat df = wb.createDataFormat();
			for (int d = 0; d < dataList.size(); d++) {

				HashMap<String, Object> dataMap = dataList.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 1 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);
				// 创建列
				for (int c = 0; c < fields.length; c++) {

					Cell contentCell = datarow.createCell(c + 2);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fields[c]) != null
							&& dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fields[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
								
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fields[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());
						} else if (isGongshiOne) {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());

						} else {

							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c])
									.toString());
							
							
						}
						
						
						
					} else {
						contentCell.setCellValue("");
					}
					if (d == dataList.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);

					}
					
				}
				// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
				sheet.addMergedRegion(new CellRangeAddress(4, 4 + dataList
						.size() - 1, 5, 5));
				// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
				sheet.addMergedRegion(new CellRangeAddress(4, 4 + dataList
						.size() - 1, 7, 7));
				// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
				sheet.addMergedRegion(new CellRangeAddress(4, 4 + dataList
						.size() - 1, 13, 13));
			}

			for (int d = 0; d < dataListTwo.size(); d++) {
				HashMap<String, Object> dataMap = dataListTwo.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 1 + rows_max
						+ dataList.size() + 9);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				// 创建列
				for (int c = 0; c < fieldsTwo.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsTwo[c]) != null
							&& dataMap.get(fieldsTwo[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTwo[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTwo[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTwo[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsTwo[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsTwo[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsTwo[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsTwo[c])
									.toString());
						}

						if (c == 9 && d != dataListTwo.size() - 1) {

							if (dataMap.get(fieldsTwo[c]).toString()
									.contains("%")

							) {

								String b = dataMap.get(fieldsTwo[c]).toString(); // 去掉%
																					// String
/*
								String tempB = b.substring(0,
										b.lastIndexOf("%")); // 精确表示 Integer
								Integer dataB = Integer.parseInt(tempB); // 大于为1，相同为0，小于为-1
																			// if
								if (dataB < 100 && dataB >= 80
															 * .compareTo(dataA)
															 * == 0
															 ) {
									contentCell.setCellStyle(cellStyleYellow);
								} else if (dataB >= 100
														 * .compareTo(dataA) ==
														 * 1
														 ) {
									contentCell.setCellStyle(cellStyleGreen);
								} else if (dataB < 80 .compareTo(dataA) == -1 ) {
									contentCell.setCellStyle(cellStyleRED);
								}

								// }
							}*/
								String tempB = b.substring(0,
										b.lastIndexOf("%")); // 精确表示 Integer
								// Integer dataA = Integer.parseInt(tempA);
								BigDecimal dataB = new BigDecimal(tempB); // 大于为1，相同为0，小于为-1
																			// if
								if (dataB.compareTo(BigDecimal.valueOf(100)) == -1

										&& (dataB.compareTo(BigDecimal
												.valueOf(80)) == 1 || dataB
												.compareTo(BigDecimal
														.valueOf(80)) == 0)
								/*
								 * .compareTo(dataA) == 0
								 */) {
									contentCell.setCellStyle(cellStyleYellow);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(100)) == 1
										|| dataB.compareTo(BigDecimal
												.valueOf(100)) == 1
								/*
								 * dataB >= 100 .compareTo(dataA) == 1
								 */) {
									contentCell.setCellStyle(cellStyleGreen);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(80)) == -1/*
															 * .compareTo(dataA)
															 * == -1
															 */) {
									contentCell.setCellStyle(cellStyleRED);
								}

								// }
							}


						}
						/*
						 * if(isPercent && d != dataListTwo.size() - 1){
						 * cellStyle
						 * .setDataFormat(HSSFDataFormat.getBuiltinFormat
						 * ("0%")); contentCell.setCellStyle(cellStyle); }
						 */

					} else {
						contentCell.setCellValue("");
					}

					if (d == dataListTwo.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}
				}
			}

			for (int d = 0; d < dataListThree.size(); d++) {
				HashMap<String, Object> dataMap = dataListThree.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 1 + rows_max
						+ dataList.size() + 9 + dataListTwo.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// scell.setEncoding(HSSFCell.ENCODING_UTF_16);

				// 定义单元格为字符串类型
				scell.setCellType(Cell.CELL_TYPE_NUMERIC);
				// scell.setCellValue(d+1); // 序号
				// 创建列
				for (int c = 0; c < fieldsThree.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsThree[c]) != null
							&& dataMap.get(fieldsThree[c]).toString().length() > 0

					) {
						// 判断data是否为数值型
						isNum = dataMap.get(fieldsThree[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsThree[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsThree[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsThree[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsThree[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsThree[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsThree[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsThree[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap
									.get(fieldsThree[c]).toString());
						}

						if (c == 8 && d != dataListThree.size() - 1/* d!=0 */) {

							if (dataMap.get(fieldsThree[c]).toString()
									.contains("%")) {
								String b = dataMap.get(fieldsThree[c])
										.toString(); // 去掉% String
								// String tempA = a.substring(0,
								// a.lastIndexOf("%"));
								String tempB = b.substring(0,
										b.lastIndexOf("%")); // 精确表示 Integer
								// Integer dataA = Integer.parseInt(tempA);
								BigDecimal dataB = new BigDecimal(tempB); // 大于为1，相同为0，小于为-1
																			// if
								if (dataB.compareTo(BigDecimal.valueOf(100)) == -1

										&& (dataB.compareTo(BigDecimal
												.valueOf(80)) == 1 || dataB
												.compareTo(BigDecimal
														.valueOf(80)) == 0)
								/*
								 * .compareTo(dataA) == 0
								 */) {
									contentCell.setCellStyle(cellStyleYellow);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(100)) == 1
										|| dataB.compareTo(BigDecimal
												.valueOf(100)) == 1
								/*
								 * dataB >= 100 .compareTo(dataA) == 1
								 */) {
									contentCell.setCellStyle(cellStyleGreen);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(80)) == -1/*
															 * .compareTo(dataA)
															 * == -1
															 */) {
									contentCell.setCellStyle(cellStyleRED);
								}

								// }
							}


						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListThree.size() - 1) {
						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}
				}
			}

			for (int d = 0; d < dataListFour.size(); d++) {
				HashMap<String, Object> dataMap = dataListFour.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 1 + rows_max
						+ dataList.size() + 9 + dataListTwo.size() + 10
						+ dataListThree.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// scell.setEncoding(HSSFCell.ENCODING_UTF_16);

				// 定义单元格为字符串类型
				scell.setCellType(Cell.CELL_TYPE_NUMERIC);
				// scell.setCellValue(d+1); // 序号
				// 创建列
				for (int c = 0; c < fieldsFour.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsFour[c]) != null
							&& dataMap.get(fieldsFour[c]).toString().length() > 0

					) {
						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFour[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFour[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFour[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsFour[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsFour[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsFour[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFour[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFour[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsFour[c])
									.toString());
						}

						if (c == 8 && d != dataListFour.size() - 1/* d!=0 */) {
							// HashMap<String, Object> dataMapLast =
							// dataListFour.get(d-1);
							/*
							 * if(dataMapLast.get(fieldsFour[c])!=null
							 * 
							 * &&
							 * dataMapLast.get(fieldsFour[c]).toString().length
							 * () > 0){
							 */
							if (dataMap.get(fieldsFour[c]).toString()
									.contains("%")
							/*
							 * &&
							 * dataMapLast.get(fieldsFour[c]).toString().contains
							 * ("%")
							 */
							) {
								// String a =
								// dataMapLast.get(fieldsFour[c]).toString();
								String b = dataMap.get(fieldsFour[c])
										.toString(); // 去掉% String
								// String tempA = a.substring(0,
								// a.lastIndexOf("%"));
								String tempB = b.substring(0,
										b.lastIndexOf("%")); // 精确表示 Integer
								// Integer dataA = Integer.parseInt(tempA);
								BigDecimal dataB = new BigDecimal(tempB); // 大于为1，相同为0，小于为-1
																			// if
								if (dataB.compareTo(BigDecimal.valueOf(100)) == -1

										&& (dataB.compareTo(BigDecimal
												.valueOf(80)) == 1 || dataB
												.compareTo(BigDecimal
														.valueOf(80)) == 0)
								/*
								 * .compareTo(dataA) == 0
								 */) {
									contentCell.setCellStyle(cellStyleYellow);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(100)) == 1
										|| dataB.compareTo(BigDecimal
												.valueOf(100)) == 1
								/*
								 * dataB >= 100 .compareTo(dataA) == 1
								 */) {
									contentCell.setCellStyle(cellStyleGreen);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(80)) == -1/*
															 * .compareTo(dataA)
															 * == -1
															 */) {
									contentCell.setCellStyle(cellStyleRED);
								}

								// }
							}


						}

					} else {
						contentCell.setCellValue("");
					}

					if (d == dataListFour.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void quaComparative(SXSSFWorkbook wb,
			LinkedList<HashMap<String, Object>> dateList, String q,
			String partyName, String beginDateOne, String endDateOne,String tBeginDate,String tEndDate)
			throws ParseException {
		String beginDate = beginDateOne;
		String endDate = endDateOne;
		String[] days = beginDateOne.split("-");

		String searchStr = null;
		String conditions = "";
		String center = "";
		String country = "";
		String region = "";
		String office = "";

		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if (!WebPageUtil.isHAdmin()) {
			if (request.getParameter("center") != null
					&& !request.getParameter("center").equals("")
					|| request.getParameter("country") != null
					&& !request.getParameter("country").equals("")
					|| request.getParameter("region") != null
					&& !request.getParameter("region").equals("")
					|| request.getParameter("office") != null
					&& !request.getParameter("office").equals("")) {

				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")) {
					center = request.getParameter("center");
					conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
							+ center + "')   ";
				}

				if (request.getParameter("country") != null
						&& !request.getParameter("country").equals("")) {
					country = request.getParameter("country");
					conditions = "  pa.country_id= " + country + "  ";
				}
				if (request.getParameter("region") != null
						&& !request.getParameter("region").equals("")) {
					region = request.getParameter("region");
					conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
							+ region
							+ "'  OR PARTY_ID='"+region+"'))  ";
				}
				if (request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {
					office = request.getParameter("office");
					conditions = "    pa.party_id IN ('" + office + "')  ";
				}

			} else {
				if (null != userPartyIds && !"".equals(userPartyIds)) {
					conditions = "  pa.party_id in (" + userPartyIds + ")  ";
				} else {
					conditions = "  1=2  ";
				}
			}

		} else {
			conditions = " 1=1 ";

		}
		if (request.getParameter("level") != null
				&& !request.getParameter("level").equals("")) {
			conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
		}
		// 表头数据
		String[] headers = {};
		// 用于放置表格数据

		DateUtil.year = Integer.parseInt(days[0]) - 1;
		DateUtil.month = Integer.parseInt(days[1]);
		headers = insert(headers, q + "," + DateUtil.year);

		for (int i = 0; i < dateList.size(); i++) {
			beginDate = dateList.get(i).get("beginDate").toString();
			endDate = dateList.get(i).get("endDate").toString();
			days = dateList.get(i).get("beginDate").toString().split("-");
			Date timec = format.parse(beginDate);
			Calendar rightNowc = Calendar.getInstance();
			rightNowc.setTime(timec);
			Date daysOne = format.parse(format.format(rightNowc.getTime()));

			headers = insert(headers, sdf.format(daysOne));

		}
		headers = insert(headers, "Total");
		headers = insert(headers, "Amount");

		// 按照对应格式将表头传入
		ArrayList columns = new ArrayList();
		for (int i = 0; i < headers.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headers[i]);
			columnMap.put("field", headers[i]);
			columns.add(columnMap);

		}

		String[] header = new String[columns.size()];
		String[] fields = new String[columns.size()];
		for (int i = 0, l = columns.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columns.get(i);
			header[i] = columnMap.get("header").toString();
			fields[i] = columnMap.get("field").toString();

		}

		String[] headersTwo = { "REGIONAL sell-out performances_RANK",
				"REGIONAL sell-out performances_REGIONAL HEAD",
				"REGIONAL sell-out performances_AREA" };

		String[] toYear = endDateOne.split("-");
		String[] year = beginDateOne.split("-");
		int laYear = Integer.parseInt(toYear[0].toString()) - 1;
		headersTwo = insert(headersTwo, "FPS_Yr-" + laYear);
		headersTwo = insert(headersTwo, "FPS_Yr-" + toYear[0]);
		String laDays = DateUtil.getLastDayOfMonth(laYear,
				Integer.parseInt(toYear[1]));
		String[] laDay = laDays.split("-");

		headersTwo = insert(headersTwo, "Total Flat Panel TV Quantity_" + q
				+ "," + laYear);
		headersTwo = insert(headersTwo, "Total Flat Panel TV Quantity_" + q
				+ "," + toYear[0]);
		headersTwo = insert(headersTwo,
				"Total Flat Panel TV Quantity_SO Growth/day");
		headersTwo = insert(headersTwo, "Total Amount_" + q + "," + laYear);
		headersTwo = insert(headersTwo, "Total Amount_" + q + "," + toYear[0]);
		headersTwo = insert(headersTwo, "Total Amount_SO Growth/day");
		headersTwo = insert(headersTwo, "Average sellout per fps_" + laYear
				+ " Ave.qty/fps");
		headersTwo = insert(headersTwo, "Average sellout per fps_" + toYear[0]
				+ " Ave.qty/fps");
		headersTwo = insert(headersTwo, "Average sellout per fps_" + laYear
				+ " Ave.amt/fps");
		headersTwo = insert(headersTwo, "Average sellout per fps_" + toYear[0]
				+ " Ave.amt/fps");

		// 按照对应格式将表头传入
		ArrayList columnsTwo = new ArrayList();
		for (int i = 0; i < headersTwo.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersTwo[i]);
			columnMap.put("field", headersTwo[i]);
			columnsTwo.add(columnMap);

		}

		String[] headerTwo = new String[columnsTwo.size()];
		String[] fieldsTwo = new String[columnsTwo.size()];
		for (int i = 0, l = columnsTwo.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsTwo.get(i);
			headerTwo[i] = columnMap.get("header").toString();
			fieldsTwo[i] = columnMap.get("field").toString();

		}

		String[] headersThree = { "SALESMAN sell-out performances_RANK",
				"SALESMAN sell-out performances_SALESMAN",
				"SALESMAN sell-out performances_REGION" };

		headersThree = insert(headersThree, "FPS_Yr-" + laYear);
		headersThree = insert(headersThree, "FPS_Yr-" + toYear[0]);
		headersThree = insert(headersThree, "Total Flat Panel TV Quantity_" + q
				+ "," + laYear);
		headersThree = insert(headersThree, "Total Flat Panel TV Quantity_" + q
				+ "," + toYear[0]);
		headersThree = insert(headersThree,
				"Total Flat Panel TV Quantity_SO Growth/day");
		headersThree = insert(headersThree, "Total Amount_" + q + "," + laYear);
		headersThree = insert(headersThree, "Total Amount_" + q + ","
				+ toYear[0]);
		headersThree = insert(headersThree, "Total Amount_SO Growth/day");
		headersThree = insert(headersThree, "Average sellout per fps_" + laYear
				+ " Ave.qty/fps");
		headersThree = insert(headersThree, "Average sellout per fps_"
				+ toYear[0] + " Ave.qty/fps");
		headersThree = insert(headersThree, "Average sellout per fps_" + laYear
				+ " Ave.amt/fps");
		headersThree = insert(headersThree, "Average sellout per fps_"
				+ toYear[0] + " Ave.amt/fps");

		// 按照对应格式将表头传入
		ArrayList columnsThree = new ArrayList();
		for (int i = 0; i < headersThree.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersThree[i]);
			columnMap.put("field", headersThree[i]);
			columnsThree.add(columnMap);

		}

		String[] headerThree = new String[columnsThree.size()];
		String[] fieldsThree = new String[columnsThree.size()];
		for (int i = 0, l = columnsThree.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsThree.get(i);
			headerThree[i] = columnMap.get("header").toString();
			fieldsThree[i] = columnMap.get("field").toString();

		}

		String[] headersFour = { "ACFO sell-out performances_RANK",
				"ACFO sell-out performances_ACFO",
				"ACFO sell-out performances_REGION" };

		headersFour = insert(headersFour, "FPS_Yr-" + laYear);
		headersFour = insert(headersFour, "FPS_Yr-" + toYear[0]);
		headersFour = insert(headersFour, "Total Flat Panel TV Quantity_" + q
				+ "," + laYear);
		headersFour = insert(headersFour, "Total Flat Panel TV Quantity_" + q
				+ "," + toYear[0]);
		headersFour = insert(headersFour,
				"Total Flat Panel TV Quantity_SO Growth/day");
		headersFour = insert(headersFour, "Total Amount_" + q + "," + laYear);
		headersFour = insert(headersFour, "Total Amount_" + q + "," + toYear[0]);
		headersFour = insert(headersFour, "Total Amount_SO Growth/day");
		headersFour = insert(headersFour, "Average sellout per fps_" + laYear
				+ " Ave.qty/fps");
		headersFour = insert(headersFour, "Average sellout per fps_"
				+ toYear[0] + " Ave.qty/fps");
		headersFour = insert(headersFour, "Average sellout per fps_" + laYear
				+ " Ave.amt/fps");
		headersFour = insert(headersFour, "Average sellout per fps_"
				+ toYear[0] + " Ave.amt/fps");

		// 按照对应格式将表头传入
		ArrayList columnsFour = new ArrayList();
		for (int i = 0; i < headersFour.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersFour[i]);
			columnMap.put("field", headersFour[i]);
			columnsFour.add(columnMap);

		}

		String[] headerFour = new String[columnsFour.size()];
		String[] fieldsFour = new String[columnsFour.size()];
		for (int i = 0, l = columnsFour.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsFour.get(i);
			headerFour[i] = columnMap.get("header").toString();
			fieldsFour[i] = columnMap.get("field").toString();

		}

		String[] headersFive = { "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_Category"

		};

		if (q.equals("Q1")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_Jan");
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_Feb");
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_Mar");
		}

		if (q.equals("Q2")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_Apr");
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_May");
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_June");
		}
		if (q.equals("Q3")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_July");
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_August");
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_September");
		}
		if (q.equals("Q4")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_October");
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_November");
			headersFive = insert(headersFive, "YTD-" + toYear[0]
					+ "  Monthly sellout trend per size_December");
		}
		headersFive = insert(headersFive, "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_TTL");

		// 按照对应格式将表头传入
		ArrayList columnsFive = new ArrayList();
		for (int i = 0; i < headersFive.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersFive[i]);
			columnMap.put("field", headersFive[i]);
			columnsFive.add(columnMap);

		}

		String[] headerFive = new String[columnsFive.size()];
		String[] fieldsFive = new String[columnsFive.size()];
		for (int i = 0, l = columnsFive.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsFive.get(i);
			headerFive[i] = columnMap.get("header").toString();
			fieldsFive[i] = columnMap.get("field").toString();

		}

		String[] headersSix = { "Market Share_Category"

		};

		if (q.equals("Q1")) {
			headersSix = insert(headersSix, "Market Share_Jan");
			headersSix = insert(headersSix, "Market Share_Feb");
			headersSix = insert(headersSix, "Market Share_Mar");
		}

		if (q.equals("Q2")) {
			headersSix = insert(headersSix, "Market Share_Apr");
			headersSix = insert(headersSix, "Market Share_May");
			headersSix = insert(headersSix, "Market Share_June");
		}
		if (q.equals("Q3")) {
			headersSix = insert(headersSix, "Market Share_July");
			headersSix = insert(headersSix, "Market Share_August");
			headersSix = insert(headersSix, "Market Share_September");
		}
		if (q.equals("Q4")) {
			headersSix = insert(headersSix, "Market Share_October");
			headersSix = insert(headersSix, "Market Share_November");
			headersSix = insert(headersSix, "Market Share_December");
		}
		headersSix = insert(headersSix, "Market Share_TTL");

		// 按照对应格式将表头传入
		ArrayList columnsSix = new ArrayList();
		for (int i = 0; i < headersSix.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersSix[i]);
			columnMap.put("field", headersSix[i]);
			columnsSix.add(columnMap);

		}

		String[] headerSix = new String[columnsSix.size()];
		String[] fieldsSix = new String[columnsSix.size()];
		for (int i = 0, l = columnsSix.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsSix.get(i);
			headerSix[i] = columnMap.get("header").toString();
			fieldsSix[i] = columnMap.get("field").toString();

		}

		String[] headersEight = { "Different catgory sell-out quantity_Category"

		};

		if (q.equals("Q1")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_Jan");
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_Feb");
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_Mar");
		}

		if (q.equals("Q2")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_Apr");
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_May");
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_June");
		}
		if (q.equals("Q3")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_July");
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_August");
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_September");
		}
		if (q.equals("Q4")) {
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_October");
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_November");
			headersEight = insert(headersEight,
					"Different catgory sell-out quantity_December");
		}
		headersEight = insert(headersEight,
				"Different catgory sell-out quantity_TTL");

		// 按照对应格式将表头传入
		ArrayList columnsEight = new ArrayList();
		for (int i = 0; i < headersEight.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersEight[i]);
			columnMap.put("field", headersEight[i]);
			columnsEight.add(columnMap);

		}

		String[] headerEight = new String[columnsEight.size()];
		String[] fieldsEight = new String[columnsEight.size()];
		for (int i = 0, l = columnsEight.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsEight.get(i);
			headerEight[i] = columnMap.get("header").toString();
			fieldsEight[i] = columnMap.get("field").toString();

		}

		String[] headersNight = { "Growth rate_Category"

		};

		if (q.equals("Q1")) {
			headersNight = insert(headersNight, "Growth rate_Jan");
			headersNight = insert(headersNight, "Growth rate_Feb");
			headersNight = insert(headersNight, "Growth rate_Mar");
		}

		if (q.equals("Q2")) {
			headersNight = insert(headersNight, "Growth rate_Apr");
			headersNight = insert(headersNight, "Growth rate_May");
			headersNight = insert(headersNight, "Growth rate_June");
		}
		if (q.equals("Q3")) {
			headersNight = insert(headersNight, "Growth rate_July");
			headersNight = insert(headersNight, "Growth rate_August");
			headersNight = insert(headersNight, "Growth rate_September");
		}
		if (q.equals("Q4")) {
			headersNight = insert(headersNight, "Growth rate_October");
			headersNight = insert(headersNight, "Growth rate_November");
			headersNight = insert(headersNight, "Growth rate_December");
		}
		headersNight = insert(headersNight, "Growth rate_TTL");

		// 按照对应格式将表头传入
		ArrayList columnsNight = new ArrayList();
		for (int i = 0; i < headersNight.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersNight[i]);
			columnMap.put("field", headersNight[i]);
			columnsNight.add(columnMap);

		}

		String[] headerNight = new String[columnsNight.size()];
		String[] fieldsNight = new String[columnsNight.size()];
		for (int i = 0, l = columnsNight.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsNight.get(i);
			headerNight[i] = columnMap.get("header").toString();
			fieldsNight[i] = columnMap.get("field").toString();

		}

		String[] headersTen = { "DEALER" };
		if (q.equals("Q1")) {
			headersTen = insert(headersTen, "Jan");
			headersTen = insert(headersTen, "Feb");
			headersTen = insert(headersTen, "Mar");
		}

		if (q.equals("Q2")) {
			headersTen = insert(headersTen, "Apr");
			headersTen = insert(headersTen, "May");
			headersTen = insert(headersTen, "June");
		}
		if (q.equals("Q3")) {
			headersTen = insert(headersTen, "July");
			headersTen = insert(headersTen, "August");
			headersTen = insert(headersTen, "September");
		}
		if (q.equals("Q4")) {
			headersTen = insert(headersTen, "October");
			headersTen = insert(headersTen, "November");
			headersTen = insert(headersTen, "December");
		}
		headersTen = insert(headersTen, "Total");
		headersTen = insert(headersTen, "Ave So per day");

		// 按照对应格式将表头传入
		ArrayList columnsTen = new ArrayList();
		for (int i = 0; i < headersTen.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersTen[i]);
			columnMap.put("field", headersTen[i]);
			columnsTen.add(columnMap);

		}

		String[] headerTen = new String[columnsTen.size()];
		String[] fieldsTen = new String[columnsTen.size()];
		for (int i = 0, l = columnsTen.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsTen.get(i);
			headerTen[i] = columnMap.get("header").toString();
			fieldsTen[i] = columnMap.get("field").toString();

		}

		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();

		String keys = "";
		for (int r = 1; r <= 6; r++) {
			HashMap<String, Object> data = new HashMap<String, Object>();

			if (r == 1) {
				DateUtil.year = Integer.parseInt(days[0]) - 1;
				for (int i = 0; i < dateList.size(); i++) {
					beginDate = dateList.get(i).get("beginDate").toString();
					endDate = dateList.get(i).get("endDate").toString();
					String[] beginDays = beginDate.split("-");
					String[] endDays = endDate.split("-");
					Date timec = format.parse(beginDate);
					Calendar rightNowc = Calendar.getInstance();
					rightNowc.setTime(timec);
					Date beOne = format
							.parse(format.format(rightNowc.getTime()));
					keys = sdf.format(beOne);

					int lastYear = Integer.parseInt(beginDays[0]) - 1;

					beginDate = lastYear + "-" + beginDays[1] + "-"
							+ beginDays[2];
					endDate = lastYear + "-" + endDays[1] + "-" + endDays[2];

					data.put(q + "," + DateUtil.year, "TTL QTY");
					List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
							beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());

					data.put(keys, Long.parseLong(sumDatas.get(0).getSaleQty()));

				}

				String[] beginDays = beginDateOne.split("-");
				String[] endDays = endDateOne.split("-");
				int lastYear = Integer.parseInt(beginDays[0]) - 1;
				beginDate = lastYear + "-" + beginDays[1] + "-" + beginDays[2];
				endDate = lastYear + "-" + endDays[1] + "-" + endDays[2];

				data.put(q + "," + DateUtil.year, "TTL QTY");
				List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
						beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());

				data.put("Total", sumDatas.get(0).getSaleQty());
				data.put("Amount",
						(long) Double.parseDouble(sumDatas.get(0).getSaleSum()));

			} else if (r == 2) {
				int day = 0;
				data.put(q + "," + DateUtil.year, "Ave/day");
				DateUtil.year = Integer.parseInt(days[0]) - 1;
				DateUtil.month = Integer.parseInt(days[1]);

				String lastDay = DateUtil.getLastDayOfMonth(DateUtil.year,
						DateUtil.month);
				String[] lastDays = lastDay.split("-");

				for (int j = 0; j < dateList.size(); j++) {
					beginDate = dateList.get(j).get("beginDate").toString();
					endDate = dateList.get(j).get("endDate").toString();
					String[] beginDays = beginDate.split("-");
					String[] endDays = endDate.split("-");
					Date timec = format.parse(beginDate);
					Calendar rightNowc = Calendar.getInstance();
					rightNowc.setTime(timec);
					Date beOne = format
							.parse(format.format(rightNowc.getTime()));

					keys = sdf.format(beOne);

					int lastYear = Integer.parseInt(beginDays[0]) - 1;

					beginDate = lastYear + "-" + beginDays[1] + "-"
							+ beginDays[2];
					endDate = lastYear + "-" + endDays[1] + "-" + endDays[2];

					List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
							beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());
					day = Integer.parseInt(endDays[2])
							- Integer.parseInt(beginDays[2]) + 1;
					
					if(sumDatas.size()>0){
					
						if (sumDatas.get(0).getSaleQty()!=null&&
								
								Double.parseDouble(sumDatas.get(0).getSaleQty()) != 0.0
								&& day != 0) {
							data.put(keys,
									Math.round(Double.parseDouble(sumDatas.get(0).getSaleQty())
											/ day));
						} else {
							data.put(keys, 0);
						}
					}
					

				}
				String[] beginDays = beginDateOne.split("-");
				String[] endDays = endDateOne.split("-");
				int lastYear = Integer.parseInt(beginDays[0]) - 1;
				beginDate = lastYear + "-" + beginDays[1] + "-" + beginDays[2];
				endDate = lastYear + "-" + endDays[1] + "-" + endDays[2];
				 Date d1=format.parse(beginDate);  
			     Date d2=format.parse(endDate);  
			        
				day =DateUtil.daysBetween(d1,d2);

				List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
						beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());
				if(sumDatas.size()>0){
					if (sumDatas.get(0).getSaleQty()!=null&&
							
							Double.parseDouble(sumDatas.get(0).getSaleQty()) != 0.0
							&& day != 0) {
						data.put("Total",
								Math.round(Double.parseDouble(sumDatas.get(0).getSaleQty()) / day));
					} else {
						data.put("Total", 0);

					}
				}
				
				if (!sumDatas.get(0).getSaleSum().equals("")
						&& !sumDatas.get(0).getSaleSum().equals("0")
						&& sumDatas.get(0).getSaleSum() != null && day != 0) {
					data.put(
							"Amount",
							Math.round( Double.parseDouble(sumDatas.get(0)
									.getSaleSum()) /day));
				} else {
					data.put("Amount", 0);
				}
				
			} else if (r == 3) {
				DateUtil.year = Integer.parseInt(days[0]) - 1;
				DateUtil.month = Integer.parseInt(days[1]);
				data.put(q + "," + DateUtil.year, q + "," + days[0]);
				int lastYear = DateUtil.year;

				for (int k = 0; k < dateList.size(); k++) {
					beginDate = dateList.get(k).get("beginDate").toString();
					endDate = dateList.get(k).get("endDate").toString();
					String[] beginDays = beginDate.split("-");
					String[] endDays = endDate.split("-");
					Date timec = format.parse(beginDate);
					Calendar rightNowc = Calendar.getInstance();
					rightNowc.setTime(timec);
					Date beOne = format
							.parse(format.format(rightNowc.getTime()));

					keys = sdf.format(beOne);
					lastYear = Integer.parseInt(beginDays[0]) - 1;
					data.put(keys, keys);

				}

				data.put("Total", "Total");
				data.put("Amount", "Amount");
			} else if (r == 4) {
				DateUtil.year = Integer.parseInt(days[0]) - 1;
				DateUtil.month = Integer.parseInt(days[1]);
				data.put(q + "," + DateUtil.year, "TTL QTY");

				for (int j = 0; j < dateList.size(); j++) {
					beginDate = dateList.get(j).get("beginDate").toString();
					endDate = dateList.get(j).get("endDate").toString();
					String[] beginDays = beginDate.split("-");
					String[] endDays = endDate.split("-");

					Date timec = format.parse(beginDate);
					Calendar rightNowc = Calendar.getInstance();
					rightNowc.setTime(timec);
					Date beOne = format
							.parse(format.format(rightNowc.getTime()));

					keys = sdf.format(beOne);
					int lastYear = Integer.parseInt(beginDays[0]) - 1;

					beginDate = beginDate;
					endDate = endDate;

					List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
							beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());
					data.put(keys, Long.parseLong(sumDatas.get(0).getSaleQty()));

				}

				beginDate = beginDateOne;
				endDate = endDateOne;
				List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
						beginDateOne, endDateOne, searchStr, conditions,WebPageUtil.isHQRole());
				data.put("Total", Long.parseLong(sumDatas.get(0).getSaleQty()));
				data.put("Amount",
						(long) Double.parseDouble(sumDatas.get(0).getSaleSum()));
			} else if (r == 5) {
				int day = 0;
				data.put(q + "," + DateUtil.year, "Ave/day");
				DateUtil.year = Integer.parseInt(days[0]) - 1;
				DateUtil.month = Integer.parseInt(days[1]);
				String[] last = endDateOne.split("-");
				day = Integer.parseInt(last[2]) - Integer.parseInt(days[2]) + 1;

				for (int j = 0; j < dateList.size(); j++) {
					beginDate = dateList.get(j).get("beginDate").toString();
					endDate = dateList.get(j).get("endDate").toString();
					String[] beginDays = beginDate.split("-");
					String[] endDays = endDate.split("-");

					Date timec = format.parse(beginDate);
					Calendar rightNowc = Calendar.getInstance();
					rightNowc.setTime(timec);
					Date beOne = format
							.parse(format.format(rightNowc.getTime()));

					keys = sdf.format(beOne);
					int lastYear = Integer.parseInt(beginDays[0]) - 1;

					beginDate = beginDate;
					endDate = endDate;

					day = Integer.parseInt(endDays[2])
							- Integer.parseInt(beginDays[2]) + 1;
					List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
							beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());
					if(sumDatas.size()>0){
						if (
								sumDatas.get(0).getSaleQty()!=null &&
								Double.parseDouble(sumDatas.get(0).getSaleQty()) != 0.0
								&& day != 0) {
							data.put(keys,
									Math.round(Double.parseDouble(sumDatas.get(0).getSaleQty()) 
											/ day));
						} else {
							data.put(keys, 0);
						}
						
					}
				

				}

				beginDate = beginDateOne;
				endDate = endDateOne;
				Date d1=format.parse(beginDate);  
			     Date d2=format.parse(endDate);  
			        
				day =DateUtil.daysBetween(d1,d2);
				List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
						beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());
				if(sumDatas.size()>0){
					if (Double.parseDouble(sumDatas.get(0).getSaleQty()) != 0.0
							&& day != 0) {
						data.put("Total",
								Math.round(Double.parseDouble(sumDatas.get(0).getSaleQty()) / day));
					} else {
						data.put("Total", 0);
					}
					if (!sumDatas.get(0).getSaleSum().equals("")
							&& !sumDatas.get(0).getSaleSum().equals("0")
							&& sumDatas.get(0).getSaleSum() != null && day != 0) {
						data.put(
								"Amount",
								Math.round( Double.parseDouble(sumDatas.get(0)
										.getSaleSum()) / day));
					} else {
						data.put("Amount", 0);
					}
					
				}
				
			} else if (r == 6) {
				data.put(q + "," + DateUtil.year, "Sellout Growth per day");
				data.put("Total", "SUM(G8,-G5)");
				data.put("Amount", "SUM(H8,-H5)");
				for (int j = 0; j < dateList.size(); j++) {
					beginDate = dateList.get(j).get("beginDate").toString();
					endDate = dateList.get(j).get("endDate").toString();
					String[] beginDays = beginDate.split("-");
					String[] endDays = endDate.split("-");

					Date timec = format.parse(beginDate);
					Calendar rightNowc = Calendar.getInstance();
					rightNowc.setTime(timec);
					Date beOne = format
							.parse(format.format(rightNowc.getTime()));

					keys = sdf.format(beOne);
					if (j == 0) {
						data.put(keys, "SUM(D8,-D5)");
					} else if (j == 1) {
						data.put(keys, "SUM(E8,-E5)");
					} else if (j == 2) {
						data.put(keys, "SUM(F8,-F5)");
					}

				}

			}
			dataList.add(data);
		}

		try {

			LinkedList<HashMap<String, Object>> dataListTwo = new LinkedList<HashMap<String, Object>>();
			List<HashMap<String, Object>> areaInfo = excelService
					.selectDataByAreaInfo(searchStr, conditions);

			List<HashMap<String, Object>> res = excelService
					.selectRegionalHeadByParty(searchStr, conditions);

			List<HashMap<String, Object>> toDatas = excelService
					.selectDataByAreaByAc(beginDateOne, endDateOne, searchStr,
							conditions,WebPageUtil.isHQRole());
			List<HashMap<String, Object>> laDatas = excelService
					.selectDataByAreaByAc(laYear + "-" + year[1] + "-"
							+ year[2], laDays, searchStr, conditions,WebPageUtil.isHQRole());
			
			for (int s = 0; s < areaInfo.size(); s++) {
				double ts = 0.0;
				double ls =  0.0;
				double tq =  0.0;
				double lq = 0.0;
				int tfps = 0;
				int lfps = 0;
				double co = 0.0;
				HashMap<String, Object> data = new HashMap<String, Object>();

				for (int i = 0; i < res.size(); i++) {
					if (areaInfo.get(s).get("partyId")
							.equals(res.get(i).get("PARTY_ID"))) {
						data.put(
								"REGIONAL sell-out performances_REGIONAL HEAD",
								res.get(i).get("userName"));
					}
				}

				data.put("REGIONAL sell-out performances_AREA", areaInfo.get(s)
						.get("AREA"));

				for (int j = 0; j < toDatas.size(); j++) {
					if (areaInfo.get(s).get("AREA").toString()
							.equals(toDatas.get(j).get("AREA").toString())) {

						data.put("FPS_Yr-" + toYear[0],
								toDatas.get(j).get("tvFps"));
						if (toDatas.get(j).get("tvFps") == null
								|| toDatas.get(j).get("tvFps").equals("")) {
							tfps = 0;
						} else {
							tfps = Integer.parseInt(toDatas.get(j).get("tvFps")
									.toString());
						}
						BigDecimal bd = new BigDecimal(toDatas.get(j)
								.get("saleQty").toString());
						data.put("Total Flat Panel TV Quantity_" + q + ","
								+ toYear[0], bd.longValue());
						if (toDatas.get(j).get("saleQty") == null
								|| toDatas.get(j).get("saleQty").equals("")) {
							tq = (long) 0;
						} else {

							tq = bd.longValue();
						}

						bd = new BigDecimal(toDatas.get(j).get("saleSum")
								.toString());
						String am = bd.toPlainString();
						data.put("Total Amount_" + q + "," + toYear[0], am);
						if (toDatas.get(j).get("saleSum") == null
								|| toDatas.get(j).get("saleSum").equals("")) {
							ts = (long) 0;
						} else {
							ts = bd.longValue();
						}

					}

				}
				
				for (int z = 0; z < laDatas.size(); z++) {
					
					if (areaInfo.get(s).get("AREA").toString()
							.equals(laDatas.get(z).get("AREA").toString())) {
						if (laDatas.get(z).get("tvFps") == null
								|| laDatas.get(z).get("tvFps").equals("")) {
							lfps = 0;
						} else {
							lfps = Integer.parseInt(laDatas.get(z).get("tvFps")
									.toString());
						}
						data.put("FPS_Yr-" + laYear, laDatas.get(z)
								.get("tvFps"));

						BigDecimal bd = new BigDecimal(laDatas.get(z)
								.get("saleQty").toString());

						data.put("Total Flat Panel TV Quantity_" + q + ","
								+ laYear, bd.longValue());
						if (laDatas.get(z).get("saleQty") == null
								|| laDatas.get(z).get("saleQty").equals("")) {
							lq = (long) 0;
						} else {
							lq = bd.longValue();
						}

						BigDecimal lbd = new BigDecimal(laDatas.get(z)
								.get("saleSum").toString());
						String lam = lbd.toPlainString();
						data.put("Total Amount_" + q + "," + laYear, lam);
						if (laDatas.get(z).get("saleSum") == null
								|| laDatas.get(z).get("saleSum").equals("")) {
							ls = (long) 0;
						} else {
							ls = lbd.longValue();
						}

					}

				}

				if (tq != 0) {
					co = tq - lq;
					double qg = co / tq * 100;
					long lnum = Math.round(qg);
					data.put("Total Flat Panel TV Quantity_SO Growth/day", lnum
							+ "%");
				}

				if (lfps != 0) {
					double lf = (lq / lfps);
					data.put("Average sellout per fps_" + laYear
							+ " Ave.qty/fps",Math.round(lf));
				}
				if (tfps != 0) {
					double tf = (tq / tfps);
					data.put("Average sellout per fps_" + toYear[0]
							+ " Ave.qty/fps", Math.round(tf));
				}
				if (lfps != 0) {
					double lff = (ls / lfps);
					data.put("Average sellout per fps_" + laYear
							+ " Ave.amt/fps", Math.round(lff));
				}
				if (tfps != 0) {
					double tff = (ts / tfps);
					data.put("Average sellout per fps_" + toYear[0]
							+ " Ave.amt/fps", Math.round(tff));
				}

				if (ts != 0.0) {
					co = ts - ls;
					double re = co / ts * 100;
					long lnum = Math.round(re);
					data.put("Total Amount_SO Growth/day", lnum + "%");
				}

				dataListTwo.add(data);

			}

			System.out.println("============="+dataListTwo.size());
			DateUtil.Order(dataListTwo, "Total Amount_SO Growth/day");
			for (int i = 0; i < dataListTwo.size(); i++) {
				dataListTwo.get(i).put("REGIONAL sell-out performances_RANK",
						i + 1);
			}

			HashMap<String, Object> data = new HashMap<String, Object>();
			int rows = 15 + dataListTwo.size();
			int end = rows + 1;
			data.put("REGIONAL sell-out performances_REGIONAL HEAD", "Total");
			data.put("FPS_Yr-" + laYear, "SUM(D16:D" + rows + ")");
			data.put("FPS_Yr-" + toYear[0], "SUM(E16:E" + rows + ")");
			data.put("Total Flat Panel TV Quantity_" + q + "," + laYear,
					"SUM(F16:F" + rows + ")");
			data.put("Total Flat Panel TV Quantity_" + q + "," + toYear[0],
					"SUM(G16:G" + rows + ")");

			data.put("Total Flat Panel TV Quantity_SO Growth/day", "TEXT((G"
					+ end + "-F" + end + ")/G" + end + ",\"0.00%\")");

			data.put("Total Amount_" + q + "," + laYear, "SUM(I16:I" + rows
					+ ")");
			data.put("Total Amount_" + q + "," + toYear[0], "SUM(J16:J" + rows
					+ ")");

			data.put("Total Amount_SO Growth/day", "TEXT((J" + end + "-I" + end
					+ ")/J" + end + ",\"0.00%\")");
			data.put("Average sellout per fps_" + laYear + " Ave.qty/fps",
					"SUM(L16:L" + rows + ")");
			data.put("Average sellout per fps_" + toYear[0] + " Ave.qty/fps",
					"SUM(M16:M" + rows + ")");
			data.put("Average sellout per fps_" + laYear + " Ave.amt/fps",
					"SUM(N16:N" + rows + ")");
			data.put("Average sellout per fps_" + toYear[0] + " Ave.amt/fps",
					"SUM(O16:O" + rows + ")");
			dataListTwo.add(data);

			/*
			 * "SALESMAN sell-out performances_RANK",
			 * "SALESMAN sell-out performances_SALESMAN",
			 * "SALESMAN sell-out performances_REGION"
			 */

			LinkedList<HashMap<String, Object>> dataListThree = new LinkedList<HashMap<String, Object>>();
			List<HashMap<String, Object>> toDataBySalesInfo = excelService
					.selectTargetBySalemanInfo(searchStr, conditions);

			List<HashMap<String, Object>> toDataBySales = excelService
					.selectTargetBySalemanByAc(beginDateOne, endDateOne, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			List<HashMap<String, Object>> laDataBysales = excelService
					.selectTargetBySalemanByAc(laYear + "-" + year[1] + "-"
							+ year[2], laDays, searchStr, conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			List<HashMap<String, Object>> Account = excelService
					.selectPartyNameByuser(searchStr, conditions);

			List<HashMap<String, Object>> toFps = excelService
					.selectFpsNameByShopByAc(beginDateOne, endDateOne, searchStr,
							conditions);
			List<HashMap<String, Object>> laFps = excelService
					.selectFpsNameByShopByAc(laYear + "-" + year[1] + "-"
							+ year[2], laDays, searchStr, conditions);

			for (int w = 0; w < toDataBySalesInfo.size(); w++) {
				double ts =  0.0;
				double ls =  0.0;
				double tq =  0.0;
				double lq =  0.0;
				int tfps = 0;
				int lfps = 0;
				int tvFps = 0;
				double co = 0.0;
				BigDecimal bd = null;
				String am = "";
				HashMap<String, Object> dataMap = new HashMap<String, Object>();

				// dataMap.put("SALESMAN sell-out performances_RANK", w + 1);
				dataMap.put("SALESMAN sell-out performances_SALESMAN",
						toDataBySalesInfo.get(w).get("userName"));

				for (int j = 0; j < toDataBySales.size(); j++) {
					if (toDataBySalesInfo
							.get(w)
							.get("userName")
							.toString()
							.equals(toDataBySales.get(j).get("userName")
									.toString())) {

						if (Account.size() > 1) {
							String a = "";
							for (int k = 0; k < Account.size(); k++) {
								if (toDataBySales.get(j).get("userId")
										.equals(Account.get(k).get("userId"))) {

									a += Account.get(k).get("PARTY_NAME") + ",";
								}

							}
							if (a.length() > 0) {
								dataMap.put(
										"SALESMAN sell-out performances_REGION",
										a.substring(0, a.length() - 1));
							}
						}

						for (int i = 0; i < toFps.size(); i++) {
							if (toFps.get(i).get("USER")
									.equals(toDataBySales.get(j).get("userId"))) {
								tvFps = Integer.parseInt(toFps.get(i)
										.get("TVFPS").toString());
							}

						}

						dataMap.put("FPS_Yr-" + toYear[0], tvFps);

						tfps = tvFps;

						bd = new BigDecimal(toDataBySales.get(j).get("saleQty")
								.toString());
						dataMap.put("Total Flat Panel TV Quantity_" + q + ","
								+ toYear[0], bd.longValue());

						tq = bd.longValue();

						bd = new BigDecimal(toDataBySales.get(j).get("saleSum")
								.toString());
						am = bd.toPlainString();
						dataMap.put("Total Amount_" + q + "," + toYear[0], am);
						ts = bd.longValue();

					}

				}

				for (int s = 0; s < laDataBysales.size(); s++) {

					if (toDataBySalesInfo
							.get(w)
							.get("userName")
							.toString()
							.equals(laDataBysales.get(s).get("userName")
									.toString())) {

						for (int i = 0; i < laFps.size(); i++) {
							if (laFps.get(i).get("USER")
									.equals(laDataBysales.get(s).get("userId"))) {
								tvFps = Integer.parseInt(laFps.get(i)
										.get("TVFPS").toString());
							}

						}

						dataMap.put("FPS_Yr-" + laYear, tvFps);

						lfps = tvFps;
						bd = new BigDecimal(laDataBysales.get(s).get("saleQty")
								.toString());
						dataMap.put("Total Flat Panel TV Quantity_" + q + ","
								+ laYear, bd.longValue());

						lq = bd.longValue();
						bd = new BigDecimal(laDataBysales.get(s).get("saleSum")
								.toString());
						am = bd.toPlainString();
						dataMap.put("Total Amount_" + q + "," + laYear, am);
						ls = bd.longValue();
					}

				}

				if (tq != 0) {
					co = (tq - lq);
					double qg = co / tq * 100;
					long lnum = Math.round(qg);
					dataMap.put("Total Flat Panel TV Quantity_SO Growth/day",
							lnum + "%");
				}

				if (lfps != 0) {
					double lf = (lq / lfps);
					dataMap.put("Average sellout per fps_" + laYear
							+ " Ave.qty/fps", Math.round(lf));
				}
				if (tfps != 0) {
					double tf = (tq / tfps);
					dataMap.put("Average sellout per fps_" + toYear[0]
							+ " Ave.qty/fps", Math.round(tf));
				}
				if (lfps != 0) {
					double lff = (ls / lfps);
					dataMap.put("Average sellout per fps_" + laYear
							+ " Ave.amt/fps", Math.round(lff));
				}
				if (tfps != 0) {
					double tff = (ts / tfps);
					dataMap.put("Average sellout per fps_" + toYear[0]
							+ " Ave.amt/fps", Math.round(tff));
				}

				if (ts != 0.0) {
					co = (ts - ls);
					double re = co / ts * 100;
					long lnum = Math.round(re);
					dataMap.put("Total Amount_SO Growth/day", lnum + "%");
				}

				dataListThree.add(dataMap);
			}

			DateUtil.Order(dataListThree, "Total Amount_SO Growth/day");
			for (int i = 0; i < dataListThree.size(); i++) {
				dataListThree.get(i).put("SALESMAN sell-out performances_RANK",
						i + 1);
			}
			HashMap<String, Object> dataThree = new HashMap<String, Object>();
			int stratRow = 15 + dataListTwo.size() + 11;
			int rowsBysale = 15 + dataListTwo.size() + 10
					+ dataListThree.size();
			end = rowsBysale + 1;
			dataThree.put("SALESMAN sell-out performances_SALESMAN", "Total");
			dataThree.put("FPS_Yr-" + laYear, "SUM(D" + stratRow + ":D"
					+ rowsBysale + ")");
			dataThree.put("FPS_Yr-" + toYear[0], "SUM(E" + stratRow + ":E"
					+ rowsBysale + ")");
			dataThree.put("Total Flat Panel TV Quantity_" + q + "," + laYear,
					"SUM(F" + stratRow + ":F" + rowsBysale + ")");
			dataThree.put(
					"Total Flat Panel TV Quantity_" + q + "," + toYear[0],
					"SUM(G" + stratRow + ":G" + rowsBysale + ")");

			dataThree.put("Total Flat Panel TV Quantity_SO Growth/day",
					"TEXT((G" + end + "-F" + end + ")/G" + end + ",\"0.00%\")");

			dataThree.put("Total Amount_" + q + "," + laYear, "SUM(I"
					+ stratRow + ":I" + rowsBysale + ")");
			dataThree.put("Total Amount_" + q + "," + toYear[0], "SUM(J"
					+ stratRow + ":J" + rowsBysale + ")");

			dataThree.put("Total Amount_SO Growth/day", "TEXT((J" + end + "-I"
					+ end + ")/J" + end + ",\"0.00%\")");
			dataThree.put("Average sellout per fps_" + laYear + " Ave.qty/fps",
					"SUM(L" + stratRow + ":L" + rowsBysale + ")");
			dataThree.put("Average sellout per fps_" + toYear[0]
					+ " Ave.qty/fps", "SUM(M" + stratRow + ":M" + rowsBysale
					+ ")");

			dataThree.put("Average sellout per fps_" + laYear + " Ave.amt/fps",
					"SUM(N" + stratRow + ":N" + rowsBysale + ")");
			dataThree.put("Average sellout per fps_" + toYear[0]
					+ " Ave.amt/fps", "SUM(O" + stratRow + ":O" + rowsBysale
					+ ")");
			dataListThree.add(dataThree);

			LinkedList<HashMap<String, Object>> dataListFour = new LinkedList<HashMap<String, Object>>();

			List<HashMap<String, Object>> toDataByAcfoInfo = excelService
					.selectTargetByAcfoInfo(searchStr, conditions);

			List<HashMap<String, Object>> toDataByAcfo = excelService
					.selectTargetByAcfoByAc(beginDateOne, endDateOne, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			List<HashMap<String, Object>> laDataByAcfo = excelService
					.selectTargetByAcfoByAc(laYear + "-" + year[1] + "-"
							+ year[2], laDays, searchStr, conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			List<HashMap<String, Object>> AccountByAcfo = excelService
					.selectAreaByUser(searchStr, conditions);

			toFps = excelService.selectFpsNameByShopByAc(beginDateOne, endDateOne,
					searchStr, conditions);

			laFps = excelService.selectFpsNameByShopByAc(laYear + "-" + year[1]
					+ "-" + year[2], laDays, searchStr, conditions);

			for (int z = 0; z < toDataByAcfoInfo.size(); z++) {
				double ts = 0.0;
				double ls = 0.0;
				double tq = 0.0;
				double lq = 0.0;
				int tfps = 0;
				int lfps = 0;
				double co = 0.0;
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				// dataMap.put("ACFO sell-out performances_RANK", z + 1);

				int tvFps = 0;
				BigDecimal bd = null;
				String am = "";
				for (int j = 0; j < toDataByAcfo.size(); j++) {

					if (toDataByAcfoInfo
							.get(z)
							.get("userName")
							.toString()
							.equals(toDataByAcfo.get(j).get("userName")
									.toString())) {

						dataMap.put("ACFO sell-out performances_ACFO",
								toDataByAcfo.get(j).get("userName"));

						if (AccountByAcfo.size() > 1) {
							String a = "";
							for (int k = 0; k < AccountByAcfo.size(); k++) {
								if (toDataByAcfo
										.get(j)
										.get("userId")
										.equals(AccountByAcfo.get(k).get(
												"userId"))) {

									a += AccountByAcfo.get(k).get("PARTY_NAME")
											+ ",";
								}

							}
							if (a.length() > 0) {
								dataMap.put(
										"ACFO sell-out performances_REGION",
										a.substring(0, a.length() - 1));

							}
						}

						for (int i = 0; i < toFps.size(); i++) {
							if (toFps.get(i).get("USER")
									.equals(toDataByAcfo.get(j).get("userId"))) {
								tvFps = Integer.parseInt(toFps.get(i)
										.get("TVFPS").toString());

							}
						}

						tfps = tvFps;
						dataMap.put("FPS_Yr-" + toYear[0], tvFps);

						bd = new BigDecimal(toDataByAcfo.get(j).get("saleQty")
								.toString());
						dataMap.put("Total Flat Panel TV Quantity_" + q + ","
								+ toYear[0], bd.longValue());

						tq = bd.longValue();
						bd = new BigDecimal(toDataByAcfo.get(j).get("saleSum")
								.toString());
						am = bd.toPlainString();
						dataMap.put("Total Amount_" + q + "," + toYear[0], am);
						ts = bd.longValue();
					}

				}

				for (int s = 0; s < laDataByAcfo.size(); s++) {
					if (toDataByAcfoInfo
							.get(z)
							.get("userName")
							.toString()
							.equals(laDataByAcfo.get(s).get("userName")
									.toString())) {

						for (int i = 0; i < laFps.size(); i++) {
							if (laFps.get(i).get("USER")
									.equals(laDataByAcfo.get(s).get("userId"))) {
								tvFps = Integer.parseInt(laFps.get(i)
										.get("TVFPS").toString());

							}
						}

						dataMap.put("FPS_Yr-" + laYear, tvFps);

						lfps = tvFps;
						bd = new BigDecimal(laDataByAcfo.get(s).get("saleQty")
								.toString());

						dataMap.put("Total Flat Panel TV Quantity_" + q + ","
								+ laYear, bd.longValue());

						bd = new BigDecimal(laDataByAcfo.get(s).get("saleQty")
								.toString());

						lq = bd.longValue();
						bd = new BigDecimal(laDataByAcfo.get(s).get("saleSum")
								.toString());
						am = bd.toPlainString();
						dataMap.put("Total Amount_" + q + "," + laYear, am);
						ls = bd.longValue();
					}

				}
				if (tq != 0) {
					co = (tq - lq);
					double qg = co / tq * 100;
					long lnum = Math.round(qg);
					dataMap.put("Total Flat Panel TV Quantity_SO Growth/day",
							lnum + "%");
				}

				if (lfps != 0) {
					double lf = (lq / lfps);
					dataMap.put("Average sellout per fps_" + laYear
							+ " Ave.qty/fps", Math.round(lf));
				}
				if (tfps != 0) {
					double tf = (tq / tfps);
					dataMap.put("Average sellout per fps_" + toYear[0]
							+ " Ave.qty/fps", Math.round(tf));
				}
				if (lfps != 0) {
					double lff = (ls / lfps);
					dataMap.put("Average sellout per fps_" + laYear
							+ " Ave.amt/fps", Math.round(lff));
				}
				if (tfps != 0) {
					double tff = (ts / tfps);
					dataMap.put("Average sellout per fps_" + toYear[0]
							+ " Ave.amt/fps", Math.round(tff));
				}

				if (ts != 0.0) {
					co = (ts - ls);
					double re = co / ts * 100;
					long lnum = Math.round(re);
					dataMap.put("Total Amount_SO Growth/day", lnum + "%");
				}

				dataListFour.add(dataMap);
			}

			DateUtil.Order(dataListFour, "Total Amount_SO Growth/day");
			for (int i = 0; i < dataListFour.size(); i++) {
				dataListFour.get(i).put("ACFO sell-out performances_RANK",
						i + 1);
			}
			HashMap<String, Object> dataFour = new HashMap<String, Object>();

			stratRow = 15 + dataListTwo.size() + 11 + dataListThree.size() + 10;

			rowsBysale = 15 + dataListTwo.size() + 11 + dataListThree.size()
					+ 9 + dataListFour.size();
			end = rowsBysale + 1;
			dataFour.put("ACFO sell-out performances_ACFO", "Total");
			dataFour.put("FPS_Yr-" + laYear, "SUM(D" + stratRow + ":D"
					+ rowsBysale + ")");
			dataFour.put("FPS_Yr-" + toYear[0], "SUM(E" + stratRow + ":E"
					+ rowsBysale + ")");
			dataFour.put("Total Flat Panel TV Quantity_" + q + "," + laYear,
					"SUM(F" + stratRow + ":F" + rowsBysale + ")");
			dataFour.put("Total Flat Panel TV Quantity_" + q + "," + toYear[0],
					"SUM(G" + stratRow + ":G" + rowsBysale + ")");
			dataFour.put("Total Flat Panel TV Quantity_SO Growth/day",
					"TEXT((G" + end + "-F" + end + ")/G" + end + ",\"0.00%\")");
			dataFour.put("Total Amount_" + q + "," + laYear, "SUM(I" + stratRow
					+ ":I" + rowsBysale + ")");
			dataFour.put("Total Amount_" + q + "," + toYear[0], "SUM(J"
					+ stratRow + ":J" + rowsBysale + ")");
			dataFour.put("Total Amount_SO Growth/day", "TEXT((J" + end + "-I"
					+ end + ")/J" + end + ",\"0.00%\")");
			dataFour.put("Average sellout per fps_" + laYear + " Ave.qty/fps",
					"SUM(L" + stratRow + ":L" + rowsBysale + ")");
			dataFour.put("Average sellout per fps_" + toYear[0]
					+ " Ave.qty/fps", "SUM(M" + stratRow + ":M" + rowsBysale
					+ ")");

			dataFour.put("Average sellout per fps_" + laYear + " Ave.amt/fps",
					"SUM(N" + stratRow + ":N" + rowsBysale + ")");
			dataFour.put("Average sellout per fps_" + toYear[0]
					+ " Ave.amt/fps", "SUM(O" + stratRow + ":O" + rowsBysale
					+ ")");
			dataListFour.add(dataFour);

			LinkedList<HashMap<String, Object>> dataListFive = new LinkedList<HashMap<String, Object>>();
			LinkedList<HashMap<String, Object>> dataListSix = new LinkedList<HashMap<String, Object>>();

			String beginSize = "";
			String endSize = "";

			String lastDay = DateUtil.getLastDayOfMonth(
					Integer.parseInt(toYear[0]), 1);
			String[] lastDays = lastDay.split("-");
			String startDay = "";
			String Month = "";
			HashMap<String, Object> dataFive = new HashMap<String, Object>();
			HashMap<String, Object> dataSix = new HashMap<String, Object>();
			long sttt = System.currentTimeMillis();
			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMap = new LinkedHashMap<String, LinkedHashMap<String, Object>>();

			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapSix = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
			for (int j = 1; j <= 4; j++) {
				if (q.equals("Q1")) {
					if (j == 1) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 1);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-01-01";
						Month = "Jan";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 2);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-02-01";
						Month = "Feb";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 3);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-03-01";
						Month = "Mar";
					} else if (j == 4) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TTL";
					}
				} else if (q.equals("Q2")) {
					if (j == 1) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 4);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-04-01";
						Month = "Apr";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 5);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-05-01";
						Month = "May";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 6);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-06-01";
						Month = "June";
					} else if (j == 4) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TTL";
					}
				} else if (q.equals("Q3")) {
					if (j == 1) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 7);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-07-01";
						Month = "July";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 8);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-08-01";
						Month = "August";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 9);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-09-01";
						Month = "September";
					} else if (j == 4) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TTL";
					}
				} else if (q.equals("Q4")) {
					if (j == 1) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 10);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-10-01";
						Month = "October";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 11);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-11-01";
						Month = "November";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 12);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-12-01";
						Month = "December";
					} else if (j == 4) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TTL";
					}
				}

				List<LinkedHashMap<String, Object>> sizeDatasOne = excelService
						.selectSaleDataBySizeByAc(beginSize, endSize, startDay,
								lastDay, searchStr, conditions,WebPageUtil.isHQRole());

				List<LinkedHashMap<String, Object>> sizeDatasOneData = excelService
						.selectSaleTotalBySizeByAc(startDay, lastDay, searchStr,
								conditions,WebPageUtil.isHQRole());

				for (int a = 0; a < sizeDatasOne.size(); a++) {
					LinkedHashMap<String, Object> colMap = sizeDatasOne.get(a);

					String key = colMap.get("sizeT").toString();

					if (allDataMap.get(key) != null) {
						LinkedHashMap<String, Object> rowMap = allDataMap
								.get(key);

						BigDecimal bd = new BigDecimal(sizeDatasOne.get(a)
								.get("quantity").toString());

						rowMap.put("YTD-" + toYear[0]
								+ "  Monthly sellout trend per size_" + Month
								+ "", bd.longValue());

						allDataMap.put(key, rowMap);

						double one = bd.longValue();

						if (sizeDatasOneData.size() == 1) {

							HashMap<String, Object> colMapSix = sizeDatasOneData
									.get(0);
							if (allDataMapSix.get(key) != null) {
								LinkedHashMap<String, Object> rowMapSix = allDataMapSix
										.get(key);

								bd = new BigDecimal(sizeDatasOneData.get(0)
										.get("quantity").toString());
								double oneData = bd.longValue();

								double oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapSix.put("Market Share_" + Month + "",
										lnum + "%");
								allDataMapSix.put(key, rowMapSix);

							} else {
								LinkedList<HashMap<String, Object>> rowListSix = new LinkedList<HashMap<String, Object>>();
								rowListSix.addLast(colMapSix);
								LinkedHashMap<String, Object> rowMapSix = new LinkedHashMap<String, Object>();

								rowMapSix.put("Market Share_Category", key);

								bd = new BigDecimal(sizeDatasOneData.get(0)
										.get("quantity").toString());
								double oneData = bd.longValue();

								double oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapSix.put("Market Share_" + Month + "",
										lnum + "%");
								allDataMapSix.put(key, rowMapSix);
							}

						}
					} else {
						LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
						rowList.addLast(colMap);

						LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();
						rowMap.put("YTD-" + toYear[0]
								+ "  Monthly sellout trend per size_Category",
								key);
						rowMap.put("YTD-" + toYear[0]
								+ "  Monthly sellout trend per size_" + Month
								+ "", key);
						BigDecimal bd = new BigDecimal(sizeDatasOne.get(a)
								.get("quantity").toString());

						rowMap.put("YTD-" + toYear[0]
								+ "  Monthly sellout trend per size_" + Month
								+ "", bd.longValue());

						double one = bd.longValue();

						if (sizeDatasOneData.size() == 1) {

							LinkedHashMap<String, Object> colMapSix = sizeDatasOneData
									.get(0);
							if (allDataMapSix.get(key) != null) {
								LinkedHashMap<String, Object> rowMapSix = allDataMapSix
										.get(key);

								bd = new BigDecimal(sizeDatasOneData.get(0)
										.get("quantity").toString());
								double oneData = bd.longValue();

								double oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapSix.put("Market Share_" + Month + "",
										lnum + "%");
								allDataMapSix.put(key, rowMapSix);

							} else {

								LinkedHashMap<String, Object> rowMapSix = new LinkedHashMap<String, Object>();

								rowMapSix.put("Market Share_Category", key);

								bd = new BigDecimal(sizeDatasOneData.get(0)
										.get("quantity").toString());
								double oneData = bd.longValue();

								double oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapSix.put("Market Share_" + Month + "",
										lnum + "%");
								allDataMapSix.put(key, rowMapSix);
							}

						}

						allDataMap.put(key, rowMap);
					}

				}

			}

			Set<String> sizeSet = allDataMap.keySet();

			Iterator<String> sizeIter = sizeSet.iterator();
			HashMap<String, Object> totalMap = new HashMap<String, Object>();
			String total_header = "";
			while (sizeIter.hasNext()) {
				String key = sizeIter.next();
				HashMap<String, Object> rowMap = allDataMap.get(key);
				dataListFive.addLast(rowMap);

				Set<String> rowSet = rowMap.keySet();

				Iterator<String> rowIter = rowSet.iterator();
				while (rowIter.hasNext()) {
					String rheader = rowIter.next();

					if (!rheader.contains("Category")) {
						int quantity = Integer.parseInt(rowMap.get(rheader)
								.toString());
						if (totalMap.get(rheader) == null) {

							totalMap.put(rheader, quantity);
						} else {
							int totalQuantity = Integer.parseInt(totalMap.get(
									rheader).toString());
							totalQuantity += quantity;
							totalMap.put(rheader, totalQuantity);
						}

					} else {
						total_header = rheader;
					}

				}

			}
			totalMap.put(total_header, "total");
			dataListFive.addLast(totalMap);

			Set<String> sizeSetSix = allDataMapSix.keySet();

			Iterator<String> sizeIterSix = sizeSetSix.iterator();
			HashMap<String, Object> totalMapSix = new HashMap<String, Object>();
			while (sizeIterSix.hasNext()) {
				String key = sizeIterSix.next();
				HashMap<String, Object> rowMap = allDataMapSix.get(key);
				dataListSix.addLast(rowMap);

				Set<String> rowSet = rowMap.keySet();
				Iterator<String> rowIter = rowSet.iterator();

				while (rowIter.hasNext()) {
					String rheader = rowIter.next();

					if (!rheader.contains("Category")) {

						if (totalMap.get(rheader) == null) {

							totalMap.put(rheader, "100%");
						} else {
							totalMap.put(rheader, "100%");
						}

					} else {
						total_header = rheader;
					}

				}

			}
			totalMap.put("Market Share_Category", "total");
			dataListSix.addLast(totalMap);

			LinkedList<HashMap<String, Object>> dataListEight = new LinkedList<HashMap<String, Object>>();
			LinkedList<HashMap<String, Object>> dataListNight = new LinkedList<HashMap<String, Object>>();

			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapEight = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapNight = new LinkedHashMap<String, LinkedHashMap<String, Object>>();

			long start = System.currentTimeMillis();
			// for (int i = 1; i <= 7; i++) {
			HashMap<String, Object> dataEight = new HashMap<String, Object>();
			HashMap<String, Object> dataNight = new HashMap<String, Object>();
			String spec = "";
			double one = 0.0;
			double oneData = 0.0;
			double oneAch = 0.0;
			for (int j = 1; j <= 4; j++) {

				if (q.equals("Q1")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 1);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-01-01";
						Month = "Jan";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 2);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-02-01";
						Month = "Feb";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 3);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-03-01";
						Month = "Mar";
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TTL";
					}
				} else if (q.equals("Q2")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 4);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-04-01";
						Month = "Apr";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 5);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-05-01";
						Month = "May";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 6);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-06-01";
						Month = "June";
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TTL";
					}
				} else if (q.equals("Q3")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 7);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-07-01";
						Month = "July";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 8);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-08-01";
						Month = "August";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 9);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-09-01";
						Month = "September";
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TTL";
					}
				} else if (q.equals("Q4")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 10);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-10-01";
						Month = "October";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 11);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-11-01";
						Month = "November";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 12);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-12-01";
						Month = "December";
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TTL";
					}

				}

				List<LinkedHashMap<String, Object>> sizeDatasOneData = excelService
						.selectQtyTotalBySpecByAc(spec, startDay, lastDay,
								searchStr, conditions,WebPageUtil.isHQRole());
				List<LinkedHashMap<String, Object>> sizeDatasTwTotal = excelService
						.selectSaleTotalBySizeByAc(startDay, lastDay, searchStr,
								conditions,WebPageUtil.isHQRole());

				for (int a = 0; a < sizeDatasOneData.size(); a++) {
					HashMap<String, Object> colMap = sizeDatasOneData.get(a);

					String key = colMap.get("SPEC").toString();

					if (allDataMapEight.get(key) != null) {
						LinkedHashMap<String, Object> rowMap = allDataMapEight
								.get(key);

						BigDecimal bd = new BigDecimal(sizeDatasOneData.get(a)
								.get("quantity").toString());

						rowMap.put("Different catgory sell-out quantity_"
								+ Month + "", bd.longValue());

						allDataMapEight.put(key, rowMap);

						one = bd.longValue();

						if (sizeDatasTwTotal.size() == 1) {

							LinkedHashMap<String, Object> colMapNight = sizeDatasTwTotal
									.get(0);
							if (allDataMapNight.get(key) != null) {
								LinkedHashMap<String, Object> rowMapNight = allDataMapNight
										.get(key);

								one = bd.longValue();
								bd = new BigDecimal(sizeDatasTwTotal.get(0)
										.get("quantity").toString());
								oneData = bd.longValue();
								oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapNight.put("Growth rate_" + Month + "",
										lnum + "%");
								allDataMapNight.put(key, rowMapNight);

							} else {
								LinkedList<HashMap<String, Object>> rowListNight = new LinkedList<HashMap<String, Object>>();
								rowListNight.addLast(colMapNight);
								LinkedHashMap<String, Object> rowMapNight = new LinkedHashMap<String, Object>();

								rowMapNight.put("Growth rate_Category", key);
								one = bd.longValue();
								bd = new BigDecimal(sizeDatasTwTotal.get(0)
										.get("quantity").toString());
								oneData = bd.longValue();
								oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapNight.put("Growth rate_" + Month + "",
										lnum + "%");
								allDataMapNight.put(key, rowMapNight);
							}

						}
					} else {
						LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
						rowList.addLast(colMap);

						LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();
						BigDecimal bd = new BigDecimal(sizeDatasOneData.get(a)
								.get("quantity").toString());

						rowMap.put(
								"Different catgory sell-out quantity_Category",
								key);
						rowMap.put("Different catgory sell-out quantity_"
								+ Month + "", key);

						rowMap.put("Different catgory sell-out quantity_"
								+ Month + "", bd.longValue());

						allDataMapEight.put(key, rowMap);

						one = bd.longValue();

						if (sizeDatasTwTotal.size() == 1) {

							LinkedHashMap<String, Object> colMapNight = sizeDatasTwTotal
									.get(0);
							if (allDataMapNight.get(key) != null) {
								LinkedHashMap<String, Object> rowMapNight = allDataMapNight
										.get(key);

								one = bd.longValue();
								bd = new BigDecimal(sizeDatasTwTotal.get(0)
										.get("quantity").toString());
								oneData = bd.longValue();
								oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapNight.put("Growth rate_" + Month + "",
										lnum + "%");
								allDataMapNight.put(key, rowMapNight);

							} else {
								LinkedList<HashMap<String, Object>> rowListNight = new LinkedList<HashMap<String, Object>>();
								rowListNight.addLast(colMapNight);
								LinkedHashMap<String, Object> rowMapNight = new LinkedHashMap<String, Object>();

								rowMapNight.put("Growth rate_Category", key);
								one = bd.longValue();
								bd = new BigDecimal(sizeDatasTwTotal.get(0)
										.get("quantity").toString());
								oneData = bd.longValue();
								oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapNight.put("Growth rate_" + Month + "",
										lnum + "%");
								allDataMapNight.put(key, rowMapNight);
							}

						}

					}

				}

			}

			Set<String> sizeSetEight = allDataMapEight.keySet();

			Iterator<String> sizeIterEight = sizeSetEight.iterator();
			HashMap<String, Object> totalMapEight = new HashMap<String, Object>();
			while (sizeIterEight.hasNext()) {
				String key = sizeIterEight.next();
				HashMap<String, Object> rowMap = allDataMapEight.get(key);
				dataListEight.addLast(rowMap);

				Set<String> rowSet = rowMap.keySet();

				Iterator<String> rowIter = rowSet.iterator();
				while (rowIter.hasNext()) {
					String rheader = rowIter.next();

					if (!rheader.contains("Category")) {
						int quantity = Integer.parseInt(rowMap.get(rheader)
								.toString());
						if (totalMapEight.get(rheader) == null) {

							totalMapEight.put(rheader, quantity);
						} else {
							int totalQuantity = Integer.parseInt(totalMapEight
									.get(rheader).toString());
							totalQuantity += quantity;
							totalMapEight.put(rheader, totalQuantity);
						}

					} else {
						total_header = rheader;
					}

				}

			}
			totalMapEight.put(total_header, "Total");
			dataListEight.addLast(totalMapEight);

			Set<String> sizeSetNight = allDataMapNight.keySet();

			Iterator<String> sizeIterNight = sizeSetNight.iterator();
			HashMap<String, Object> totalMapNight = new HashMap<String, Object>();
			while (sizeIterNight.hasNext()) {
				String key = sizeIterNight.next();
				HashMap<String, Object> rowMap = allDataMapNight.get(key);
				dataListNight.addLast(rowMap);

				Set<String> rowSet = rowMap.keySet();
				Iterator<String> rowIter = rowSet.iterator();

				while (rowIter.hasNext()) {
					String rheader = rowIter.next();

					if (!rheader.contains("Category")) {

						if (totalMapNight.get(rheader) == null) {

							totalMapNight.put(rheader, "100%");
						} else {
							totalMapNight.put(rheader, "100%");
						}

					} else {
						total_header = rheader;
					}

				}

			}
			totalMapNight.put("Growth rate_Category", "Total");
			dataListNight.addLast(totalMapNight);

			LinkedList<HashMap<String, Object>> dataListTen = new LinkedList<HashMap<String, Object>>();

			int day = 0;
			long startTime = System.currentTimeMillis();

			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapTen = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
			LinkedHashMap<String, Object> rowMapTTL = new LinkedHashMap<String, Object>();
			for (int j = 1; j <= 5; j++) {
				if (q.equals("Q1")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 1);
						startDay = toYear[0] + "-01-01";
						Month = "Jan";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 2);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-02-01";
						Month = "Feb";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 3);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-03-01";
						Month = "Mar";
					} else if (j == 5) {
						String sd = beginDateOne.split("-")[2];
						String ed = endDateOne.split("-")[2];
						day = Integer.parseInt(ed) - Integer.parseInt(sd) + 1;
						Month = "Ave So per day";
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "Total";
					}
				} else if (q.equals("Q2")) {

					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 4);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-04-01";
						Month = "Apr";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 5);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-05-01";
						Month = "May";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 6);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-06-01";
						Month = "June";
					} else if (j == 5) {
						String sd = beginDateOne.split("-")[2];
						String ed = endDateOne.split("-")[2];
						day = Integer.parseInt(ed) - Integer.parseInt(sd) + 1;
						Month = "Ave So per day";
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "Total";
					}

				} else if (q.equals("Q3")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 7);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-07-01";
						Month = "July";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 8);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-08-01";
						Month = "August";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 9);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-09-01";
						Month = "September";
					} else if (j == 5) {
						String sd = beginDateOne.split("-")[2];
						String ed = endDateOne.split("-")[2];
						day = Integer.parseInt(ed) - Integer.parseInt(sd) + 1;
						Month = "Ave So per day";
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "Total";
					}

				} else if (q.equals("Q4")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 10);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-10-01";
						Month = "October";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 11);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-11-01";
						Month = "November";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 12);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-12-01";
						Month = "December";
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "Total";
					} else if (j == 5) {
						String sd = beginDateOne.split("-")[2];
						String ed = endDateOne.split("-")[2];
						day = Integer.parseInt(ed) - Integer.parseInt(sd) + 1;
						Month = "Ave So per day";
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
					}
				}

				List<LinkedHashMap<String, Object>> sumDatas = excelService
						.selectSaleDataByDEALERByAc(startDay, lastDay, searchStr,
								conditions,WebPageUtil.isHQRole());

				for (int z = 0; z < sumDatas.size(); z++) {
					LinkedHashMap<String, Object> colMap = sumDatas.get(z);
					String key = colMap.get("DEALER").toString();

					if (allDataMapTen.get(key) != null) {
						LinkedHashMap<String, Object> rowMap = allDataMapTen
								.get(key);

						BigDecimal bd = new BigDecimal(sumDatas.get(z)
								.get("saleQty").toString());
						rowMap.put(Month, bd.longValue());
						rowMap.put("DEALER", sumDatas.get(z).get("DEALER"));

						if (j == 14) {
							double qty = bd.longValue();
							rowMap.put(Month, Math.round(qty/day));
						}
						allDataMapTen.put(key, rowMap);
					} else {
						LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
						rowList.addLast(colMap);

						LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();
						BigDecimal bd = new BigDecimal(sumDatas.get(z)
								.get("saleQty").toString());
						rowMap.put("DEALER", sumDatas.get(z).get("DEALER"));
						rowMap.put(Month, key);
						rowMap.put(Month, bd.longValue());

						if (j == 14) {
							double qty = bd.longValue();
							rowMap.put(Month, Math.round(qty/day));
						}
						allDataMapTen.put(key, rowMap);

					}

				}

				List<HashMap<String, Object>> sumTTL = excelService
						.selectSaleTTLByDEALERByAc(startDay, lastDay, searchStr,
								conditions,WebPageUtil.isHQRole());
				String key = Month;
				for (int s = 0; s < sumTTL.size(); s++) {
					HashMap<String, Object> colMap = sumTTL.get(s);
					rowMapTTL.put(Month, key);
					BigDecimal bd = new BigDecimal(sumTTL.get(s).get("saleQty")
							.toString().toString());
					rowMapTTL.put(Month, bd.longValue());
					rowMapTTL.put("DEALER", "Total");
					if (j == 14) {
						double qty = bd.longValue();
						rowMapTTL.put(Month, Math.round(qty/day));
					}

				}

			}
			allDataMapTen.put("Total", rowMapTTL);

			System.out.println(allDataMapTen);

			Set<String> sizeSetTen = allDataMapTen.keySet();

			Iterator<String> sizeIterTen = sizeSetTen.iterator();
			HashMap<String, Object> totalMapTen = new HashMap<String, Object>();
			while (sizeIterTen.hasNext()) {
				String key = sizeIterTen.next();
				HashMap<String, Object> rowMap = allDataMapTen.get(key);
				dataListTen.addLast(rowMap);

				Set<String> rowSet = rowMap.keySet();
				Iterator<String> rowIter = rowSet.iterator();

			}

			// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
			Sheet sheet = wb.createSheet(Integer.parseInt(toYear[0]) - 1
					+ "  vs. " + toYear[0] + " comparative");
			sheet.setZoom(3, 4);
			// 创建字体
			Font fontinfo = wb.createFont();
			fontinfo.setFontHeightInPoints((short) 11); // 字体大小
			fontinfo.setFontName("Trebuchet MS");
			Font fonthead = wb.createFont();
			fonthead.setFontHeightInPoints((short) 12);
			fonthead.setFontName("Trebuchet MS");
			// colSplit, rowSplit,leftmostColumn, topRow,
			sheet.createFreezePane(3, 2, 3, 2);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat
					.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
														// hh:mm:ss

			CellStyle cellStylehead = wb.createCellStyle();// 表头样式
			cellStylehead.setFont(fonthead);
			cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylehead.setWrapText(true);

			CellStyle cellStyleWHITE = wb.createCellStyle();// 表头样式
			cellStyleWHITE.setFont(fonthead);
			cellStyleWHITE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleWHITE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleWHITE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyleWHITE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleWHITE.setWrapText(true);

			CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fonthead);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fonthead);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleYellow.setWrapText(true);

			CellStyle cellStylePERCENT = wb.createCellStyle();// 表头样式
			cellStylePERCENT.setFont(fonthead);
			cellStylePERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePERCENT.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePERCENT.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT
					.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStylePERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePERCENT.setWrapText(true);

			CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
			cellStyleRED.setFont(fonthead);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);

			CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			Row row = sheet.createRow((short) 0);
			// 创建这一行的一列，即创建单元格(CELL)
			Cell cell = row.createCell((short) 0);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("TCL " + partyName);// 标题
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

			// 第二行
			Row rowSec = sheet.createRow((short) 1);
			cell = rowSec.createCell((short) 0);
			cell.setCellStyle(cellStylename);
			cell.setCellValue("SELL-OUT ANALYSIS");
			// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
			CellStyle contextstyle = wb.createCellStyle();
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			contextstyle.setFont(fontinfo);
			// 从17行开始放另一个表格的表头

			int rows_max = 0; // 最大的一个项有几个子项

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map map = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 2);
				for (int i = 0; i < header.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = header[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i + 2;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2,
								rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (map.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (map.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < header.length; j++) {
							if (header[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 2,
								k + 2, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k
									+ 2 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerTwo.length; i++) {
				String h = headerTwo[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapTwo = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13);
				for (int i = 0; i < headerTwo.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerTwo[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13,
								rows_max + 12, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapTwo.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapTwo.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerTwo.length; j++) {
							if (headerTwo[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13,
								k + 13, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13,
									k + 13 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapTwo.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapTwo.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerThree.length; i++) {
				String h = headerThree[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapThree = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10);
				for (int i = 0; i < headerThree.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerThree[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(
								13 + dataListTwo.size() + 10, rows_max + 12
										+ dataListTwo.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapThree.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapThree.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerThree.length; j++) {
							if (headerThree[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10, k + 13
								+ dataListTwo.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10, k + 13
									+ dataListTwo.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapThree.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapThree.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerFour.length; i++) {
				String h = headerFour[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapFour = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10
						+ dataListThree.size() + 10);
				for (int i = 0; i < headerFour.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerFour[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10, rows_max + 12
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10 + 5, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapFour.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapFour.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerFour.length; j++) {
							if (headerFour[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10, k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapFour.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapFour.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerFive.length; i++) {
				String h = headerFive[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapFive = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10
						+ dataListThree.size() + 10 + dataListFour.size() + 10);
				for (int i = 0; i < headerFive.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerFive[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10, rows_max + 12
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapFive.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapFive.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerFive.length; j++) {
							if (headerFive[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10, k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapFive.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapFive.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerSix.length; i++) {
				String h = headerSix[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapSix = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10
						+ dataListThree.size() + 10 + dataListFour.size() + 10
						+ dataListFive.size() + 10);
				for (int i = 0; i < headerSix.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerSix[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10, rows_max + 12
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapSix.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapSix.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerSix.length; j++) {
							if (headerSix[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10, k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapSix.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapSix.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerEight.length; i++) {
				String h = headerEight[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapEight = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10
						+ dataListThree.size() + 10 + dataListFour.size() + 10
						+ dataListFive.size() + 10 + dataListSix.size() + 10);
				for (int i = 0; i < headerEight.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerEight[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10,

						rows_max + 12 + dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapEight.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapEight.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerEight.length; j++) {
							if (headerEight[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10, k + 13 + dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapEight.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapEight.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerNight.length; i++) {
				String h = headerNight[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapNight = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10
						+ dataListThree.size() + 10 + dataListFour.size() + 10
						+ dataListFive.size() + 10 + dataListSix.size() + 10
						+ dataListEight.size() + 10);
				for (int i = 0; i < headerNight.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerNight[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10, rows_max + 12
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapNight.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapNight.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerNight.length; j++) {
							if (headerNight[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10, k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10, (num), (num
								+ cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10
									+ dataListEight.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10
									+ dataListEight.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapNight.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapNight.put(key, null);
						}
					}
				}
			}

			Map mapTen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet
						.createRow((short) k + 13 + dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10
								+ dataListNight.size() + 10);
				for (int i = 0; i < headerTen.length; i++) {
					cell.setCellStyle(cellStyleGreen);
					String headerTemp = headerTen[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10
								+ dataListNight.size() + 10, rows_max + 12
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10
								+ dataListNight.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapTen.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapTen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerTen.length; j++) {
							if (headerTen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10
								+ dataListNight.size() + 10, k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10
								+ dataListNight.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10
									+ dataListEight.size() + 10
									+ dataListNight.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10
									+ dataListEight.size() + 10
									+ dataListNight.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapTen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapTen.put(key, null);
						}
					}
				}
			}
			for (int d = 0; d < dataList.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataList.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fields.length; c++) {

					Cell contentCell = datarow.createCell(c + 2);

					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fields[c]) != null
							&& dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fields[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fields[c]).toString()));

						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							// contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());

						} else {

							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c])
									.toString());
						}

					} else {
						contentCell.setCellValue(0);
					}
					if (d == dataList.size() - 1) {
						cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleGreen);

					}
					/*
					 * if ( d == dataList.size() - 1 &&
					 * dataMap.get(fields[c]).toString().contains("-") ) {
					 * contentCell.setCellStyle(cellStyleRED); }
					 */

					if (d == 2) {
						contentCell.setCellStyle(cellStylePERCENT);
					}

				}

			}

			for (int d = 0; d < dataListTwo.size(); d++) {

				HashMap<String, Object> dataMap = dataListTwo.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsTwo.length; c++) {
					DataFormat df = wb.createDataFormat();
					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsTwo[c]) != null
							&& dataMap.get(fieldsTwo[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTwo[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTwo[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTwo[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsTwo[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsTwo[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsTwo[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsTwo[c])
									.toString());
						}
						if (c == 7 || c == 10 || c == 12 || c == 14) {
							contentCell.setCellStyle(cellStyleGreen);
							if (dataMap.get(fieldsTwo[c]).toString()
									.contains("-")
									&& d != dataListTwo.size() - 1) {
								contentCell.setCellStyle(cellStyleRED);
							}
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListTwo.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}

				}

			}

			for (int d = 0; d < dataListThree.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataListThree.get(d);

				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsThree.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsThree[c]) != null
							&& dataMap.get(fieldsThree[c]).toString().length() > 0

					) {
						// 判断data是否为数值型
						isNum = dataMap.get(fieldsThree[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsThree[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsThree[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsThree[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsThree[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsThree[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsThree[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsThree[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap
									.get(fieldsThree[c]).toString());
						}
						if (c == 7 || c == 10 || c == 12 || c == 14) {
							contentCell.setCellStyle(cellStyleGreen);
							if (dataMap.get(fieldsThree[c]).toString()
									.contains("-")
									&& d != dataListThree.size() - 1) {
								contentCell.setCellStyle(cellStyleRED);

							}
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListThree.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}

				}

			}

			for (int d = 0; d < dataListFour.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataListFour.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsFour.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsFour[c]) != null
							&& dataMap.get(fieldsFour[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFour[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFour[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFour[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsFour[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsFour[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsFour[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFour[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFour[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap.get(fieldsFour[c])
									.toString());
						}
						if (c == 7 || c == 10 || c == 12 || c == 14) {
							contentCell.setCellStyle(cellStyleGreen);
							if (dataMap.get(fieldsFour[c]).toString()
									.contains("-")
									&& d != dataListFour.size() - 1) {
								contentCell.setCellStyle(cellStyleRED);

							}
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListFour.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}

				}

			}

			for (int d = 0; d < dataListFive.size(); d++) {

				HashMap<String, Object> dataMap = dataListFive.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10
						+ dataListFour.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsFive.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsFive[c]) != null
							&& dataMap.get(fieldsFive[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFive[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFive[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFive[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsFive[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsFive[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						DataFormat df = wb.createDataFormat(); // 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsFive[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFive[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFive[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap.get(fieldsFive[c])
									.toString());
						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}

			for (int d = 0; d < dataListSix.size(); d++) {

				HashMap<String, Object> dataMap = dataListSix.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10
						+ dataListFour.size() + 10 + dataListFive.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsSix.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsSix[c]) != null
							&& dataMap.get(fieldsSix[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsSix[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsSix[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsSix[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsSix[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsSix[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						DataFormat df = wb.createDataFormat(); // 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsSix[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsSix[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsSix[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap.get(fieldsSix[c])
									.toString());
						}
						if (c > 1 && c != fieldsSix.length - 1
								&& d != dataListSix.size()) {
							if (dataMap.get(fieldsSix[c - 1])!= null) {
								if (dataMap.get(fieldsSix[c - 1]).toString()
										.contains("%")
										&& dataMap.get(fieldsSix[c]).toString()
												.contains("%")) {

									String a = dataMap.get(fieldsSix[c - 1])
											.toString();
									String b = dataMap.get(fieldsSix[c])
											.toString();
									// 去掉%
									String tempA = a.substring(0,
											a.lastIndexOf("%"));
									String tempB = b.substring(0,
											b.lastIndexOf("%"));
									// 精确表示
									Integer dataA = Integer.parseInt(tempA);
									Integer dataB = Integer.parseInt(tempB);
									// 大于为1，相同为0，小于为-1
									if (dataB.compareTo(dataA) == 0) {
										contentCell
												.setCellStyle(cellStyleYellow);
									} else if (dataB.compareTo(dataA) == 1) {
										contentCell
												.setCellStyle(cellStyleGreen);
									} else if (dataB.compareTo(dataA) == -1) {
										contentCell.setCellStyle(cellStyleRED);
									}
								}
							}

						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}

			for (int d = 0; d < dataListEight.size(); d++) {

				HashMap<String, Object> dataMap = dataListEight.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10
						+ dataListFour.size() + 10 + dataListFive.size() + 10
						+ dataListSix.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsEight.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsEight[c]) != null
							&& dataMap.get(fieldsEight[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsEight[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsEight[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsEight[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsEight[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsEight[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						DataFormat df = wb.createDataFormat(); // 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsEight[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsEight[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsEight[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap
									.get(fieldsEight[c]).toString());
						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}

			for (int d = 0; d < dataListNight.size(); d++) {

				HashMap<String, Object> dataMap = dataListNight.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10
						+ dataListFour.size() + 10 + dataListFive.size() + 10
						+ dataListSix.size() + 10 + dataListEight.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsNight.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsNight[c]) != null
							&& dataMap.get(fieldsNight[c]).toString().length() > 0

					) {
						// 判断data是否为数值型
						isNum = dataMap.get(fieldsNight[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsNight[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsNight[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsNight[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsNight[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						DataFormat df = wb.createDataFormat(); // 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsNight[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsNight[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsNight[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap
									.get(fieldsNight[c]).toString());
						}
						if (c > 1 && c != fieldsNight.length - 1
								&& d != dataListNight.size()) {
							if (dataMap.get(fieldsNight[c - 1]) != null) {
								if (dataMap.get(fieldsNight[c - 1]).toString()
										.contains("%")
										&& dataMap.get(fieldsNight[c])
												.toString().contains("%")) {

									String a = dataMap.get(fieldsNight[c - 1])
											.toString();
									String b = dataMap.get(fieldsNight[c])
											.toString();
									// 去掉%
									String tempA = a.substring(0,
											a.lastIndexOf("%"));
									String tempB = b.substring(0,
											b.lastIndexOf("%"));
									// 精确表示
									Integer dataA = Integer.parseInt(tempA);
									Integer dataB = Integer.parseInt(tempB);
									// 大于为1，相同为0，小于为-1
									if (dataB.compareTo(dataA) == 0) {
										contentCell
												.setCellStyle(cellStyleYellow);
									} else if (dataB.compareTo(dataA) == 1) {
										contentCell
												.setCellStyle(cellStyleGreen);
									} else if (dataB.compareTo(dataA) == -1) {
										contentCell.setCellStyle(cellStyleRED);
									}
								}
							}

						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}

			for (int d = 0; d < dataListTen.size(); d++) {

				HashMap<String, Object> dataMap = dataListTen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10
						+ dataListFour.size() + 10 + dataListFive.size() + 10
						+ dataListSix.size() + 10 + dataListEight.size() + 10
						+ dataListNight.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsTen.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsTen[c]) != null
							&& dataMap.get(fieldsTen[c]).toString().length() > 0

					) {
						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTen[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTen[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTen[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsTen[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsTen[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						DataFormat df = wb.createDataFormat(); // 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsTen[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTen[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTen[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap.get(fieldsTen[c])
									.toString());
						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void quaDealerSellout(SXSSFWorkbook wb,
			LinkedList<HashMap<String, Object>> dateList, String q,
			String partyName, String beginDateOne, String endDateOne,String tBeginDate,String tEndDate) {

		// 表头数据
		String[] headers = { "REG", "RANK", "DEALER", };

		String[] toYear = beginDateOne.split("-");
		String[] toYearEnd = endDateOne.split("-");
		int laYear = Integer.parseInt(toYear[0].toString()) - 1;

		headers = insert(headers, q + "." + laYear + "_" + "NO. OF SHOP");
		headers = insert(headers, q + "." + laYear + "_" + "NO. OF FPS");
		headers = insert(headers, q + "." + laYear + "_" + "TOTAL QTY.");
		headers = insert(headers, q + "." + laYear + "_" + "TOTAL AMOUNT");

		headers = insert(headers, q + "." + toYear[0] + "_" + "NO. OF SHOP");
		headers = insert(headers, q + "." + toYear[0] + "_" + "NO. OF FPS");
		
		headers = insert(headers, q + "." + toYear[0] + "_"
				+ "TTL AIRCON SO_QTY");
		headers = insert(headers, q + "." + toYear[0] + "_"
				+ "TTL AIRCON SO_AMT");
		headers = insert(headers, "BASIC TARGET");
		headers = insert(headers, "ACH.");
		headers = insert(headers, "MARKET SHARE");
		headers = insert(headers, "GROWTH_QTY");
		headers = insert(headers, "GROWTH_AMOUNT");
		headers = insert(headers, laYear + " AVE.SO/FPS_QTY");
		headers = insert(headers, laYear + " AVE.SO/FPS_AMT");
		headers = insert(headers, toYear[0] + " AVE.SO/FPS_QTY");
		headers = insert(headers, toYear[0] + " AVE.SO/FPS_AMT");
		headers = insert(headers, "SELL-OUT EFFICIENCY_QTY");
		headers = insert(headers, "SELL-OUT EFFICIENCY_AMT");

		// 按照对应格式将表头传入
		ArrayList columns = new ArrayList();
		for (int i = 0; i < headers.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headers[i]);
			columnMap.put("field", headers[i]);
			columns.add(columnMap);
		}

		String[] header = new String[columns.size()];
		String[] fields = new String[columns.size()];
		for (int i = 0, l = columns.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columns.get(i);
			header[i] = columnMap.get("header").toString();
			fields[i] = columnMap.get("field").toString();

		}

		String searchStr = null;
		String conditions = "";
		String center = "";
		String country = "";
		String region = "";
		String office = "";

		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if (!WebPageUtil.isHAdmin()) {
			if (request.getParameter("center") != null
					&& !request.getParameter("center").equals("")
					|| request.getParameter("country") != null
					&& !request.getParameter("country").equals("")
					|| request.getParameter("region") != null
					&& !request.getParameter("region").equals("")
					|| request.getParameter("office") != null
					&& !request.getParameter("office").equals("")) {

				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")) {
					center = request.getParameter("center");
					conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
							+ center + "')   ";
				}

				if (request.getParameter("country") != null
						&& !request.getParameter("country").equals("")) {
					country = request.getParameter("country");
					conditions = "  pa.country_id= " + country + "  ";
				}
				if (request.getParameter("region") != null
						&& !request.getParameter("region").equals("")) {
					region = request.getParameter("region");
					conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
							+ region
							+ "'  OR PARTY_ID='"+region+"'))  ";
				}
				if (request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {
					office = request.getParameter("office");
					conditions = "    pa.party_id IN ('" + office + "')  ";
				}

			} else {
				if (null != userPartyIds && !"".equals(userPartyIds)) {
					conditions = "  pa.party_id in (" + userPartyIds + ")  ";
				} else {
					conditions = "  1=2  ";
				}
			}

		} else {
			conditions = " 1=1 ";

		}
		if (request.getParameter("level") != null
				&& !request.getParameter("level").equals("")) {
			conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
		}
		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
		try {
			String beginOne = laYear + "-" + toYear[1] + "-" + toYear[2];
			String endOne = laYear + "-" + toYearEnd[1] + "-" + toYearEnd[2];

			String beginTwo = beginDateOne;
			String endTwo = endDateOne;
			List<HashMap<String, Object>> DealerSelloutLast = excelService
					.selectSelloutByDealerByAc(beginOne, endOne, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			List<HashMap<String, Object>> DealerSelloutInfo = excelService
					.selectSelloutByDealerInfo(searchStr, conditions);


			List<HashMap<String, Object>> DealerSelloutTo = excelService
					.selectSelloutByDealerByAc(beginTwo, endTwo, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			for (int w = 0; w < DealerSelloutInfo.size(); w++) {
				double ach = 0.0;
				double avg =  0.0;
				double tqavg = 0.0;
				double tsavg = 0.0;
				double lqavg = 0.0;
				double lsavg = 0.0;
				double lq =  0.0;
				int lfps = 0;
				int lshop = 0;
				Double lt = 0.0;
				Double ls = 0.0;
				Double ts = 0.0;
				double tq = 0.0;
				int tfps = 0;
				int tshop = 0;
				Double tt = 0.0;
				double ltq = 0.0;
				HashMap<String, Object> dataMap = new HashMap<String, Object>();

				dataMap.put("REG", DealerSelloutInfo.get(w).get("REG"));
				dataMap.put("DEALER", DealerSelloutInfo.get(w).get("DEALER"));
				// dataMap.put("RANK", w + 1);

				for (int i = 0; i < DealerSelloutTo.size(); i++) {

					if (DealerSelloutInfo.get(w).get("DEALER")
							.equals(DealerSelloutTo.get(i).get("DEALER"))) {

						int row = 7 + i;
						dataMap.put(q + "." + toYear[0] + "_" + "NO. OF SHOP",
								DealerSelloutTo.get(i).get("noOfShops"));
						dataMap.put(q + "." + toYear[0] + "_" + "NO. OF FPS",
								DealerSelloutTo.get(i).get("tvFps"));

						BigDecimal bd = new BigDecimal(DealerSelloutTo.get(i)
								.get("saleSum").toString());
						String am = bd.toPlainString();
						dataMap.put(
								q + "." + toYear[0] + "_" + "TTL AIRCON SO_AMT", am);

						BigDecimal td = new BigDecimal(DealerSelloutTo.get(i)
								.get("targetSum").toString());
						String tm = td.toPlainString();
						dataMap.put("BASIC TARGET", tm);

						

						tshop = Integer.parseInt(DealerSelloutTo.get(i)
								.get("noOfShops").toString());
						tfps = Integer.parseInt(DealerSelloutTo.get(i)
								.get("tvFps").toString());

						bd = new BigDecimal(DealerSelloutTo.get(i)
								.get("saleQty").toString());
						dataMap.put(
								q + "." + toYear[0] + "_" + "TTL AIRCON SO_QTY",
								bd.longValue());

						tq = bd.longValue();
						ts = Double.parseDouble(am);
						tt = Double.parseDouble(tm);
						if (tt != 0.0) {
							ach = ts / tt * 100;
							long lnum = Math.round(ach);
							dataMap.put("ACH.", lnum + "%");
						}

			
						if (tfps != 0) {
							avg = tq / tfps;
							dataMap.put(toYear[0] + " AVE.SO/FPS_QTY", Math.round(avg));
							tqavg =  Math.round(avg);

							avg =  (ts / tfps);
							dataMap.put(toYear[0] + " AVE.SO/FPS_AMT", Math.round(avg));
							tsavg = Math.round(avg);
						}

					}

				}

				for (int j = 0; j < DealerSelloutLast.size(); j++) {

					if (DealerSelloutInfo.get(w).get("DEALER")
							.equals(DealerSelloutLast.get(j).get("DEALER"))) {

						dataMap.put(q + "." + laYear + "_" + "NO. OF SHOP",
								DealerSelloutLast.get(j).get("noOfShops"));
						dataMap.put(q + "." + laYear + "_" + "NO. OF FPS",
								DealerSelloutLast.get(j).get("tvFps"));

						BigDecimal lbd = new BigDecimal(DealerSelloutLast
								.get(j).get("saleSum").toString());
						String lam = lbd.toPlainString();
						dataMap.put(q + "." + laYear + "_" + "TOTAL AMOUNT",
								lam);
						lshop = Integer.parseInt(DealerSelloutLast.get(j)
								.get("noOfShops").toString());
						lfps = Integer.parseInt(DealerSelloutLast.get(j)
								.get("tvFps").toString());
						BigDecimal bd = new BigDecimal(DealerSelloutLast.get(j)
								.get("saleQty").toString());

						dataMap.put(q + "." + laYear + "_" + "TOTAL QTY.",
								bd.longValue());

						lq = bd.longValue();
						ls = Double.parseDouble(lam);

						if (lfps != 0) {
							avg = lq / lfps;
							dataMap.put(laYear + " AVE.SO/FPS_QTY", Math.round(avg));
							lqavg = Math.round(avg);

							avg = (ls / lfps);
							dataMap.put(laYear + " AVE.SO/FPS_AMT", Math.round(avg));
							lsavg = Math.round(avg);
						}

					}

				}

				if (tq != 0) {
					ltq = tq - lq;
					ach = ltq / tq * 100;
					long lnum = Math.round(ach);
					dataMap.put("GROWTH_QTY", lnum + "%");

				} else if (tq == 0 && lq == 0) {
					dataMap.put("GROWTH_QTY", "100%");
				} else if (tq == 0 && lq != 0) {
					dataMap.put("GROWTH_QTY", "-100%");
				}
				if (ts != 0.0) {
					ltq = ts - ls;
					ach = ltq / ts * 100;
					long lnum = Math.round(ach);
					dataMap.put("GROWTH_AMOUNT", lnum + "%");
				} else if (ts == 0.0 && ls == 0.0) {
					dataMap.put("GROWTH_AMOUNT", "100%");
				} else if (ts == 0.0 && ls != 0.0) {
					dataMap.put("GROWTH_AMOUNT", "-100%");
				}

				ach = (tqavg - lqavg);
				BigDecimal bd = new BigDecimal(ach);
				String am = bd.toPlainString();
				dataMap.put("SELL-OUT EFFICIENCY_QTY", am);

				ach = (tsavg - lsavg);
				bd = new BigDecimal(ach);
				am = bd.toPlainString();
				dataMap.put("SELL-OUT EFFICIENCY_AMT", am);

				dataList.add(dataMap);
			}
			DateUtil.Order(dataList, "GROWTH_AMOUNT");

			for (int i = 0; i < dataList.size(); i++) {
				dataList.get(i).put("RANK", i + 1);
			}
			// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
			Sheet sheet = wb.createSheet("Dealer Sellout");
			sheet.setZoom(3, 4);
			// 创建字体
			Font fontinfo = wb.createFont();
			fontinfo.setFontHeightInPoints((short) 9); // 字体大小
			fontinfo.setFontName("Trebuchet MS");
			Font fonthead = wb.createFont();
			fonthead.setFontHeightInPoints((short) 10);
			fonthead.setFontName("Trebuchet MS");

			// colSplit, rowSplit,leftmostColumn, topRow,
			sheet.createFreezePane(3, 5, 3, 5);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat
					.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
														// hh:mm:ss

			CellStyle cellStylehead = wb.createCellStyle();// 表头样式
			cellStylehead.setFont(fonthead);
			cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			CellStyle cellStyleWHITE = wb.createCellStyle();// 表头样式
			cellStyleWHITE.setFont(fonthead);
			cellStyleWHITE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleWHITE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleWHITE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyleWHITE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleWHITE.setWrapText(true);

			CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fonthead);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fonthead);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleYellow.setWrapText(true);

			CellStyle cellStylePERCENT = wb.createCellStyle();// 表头样式
			cellStylePERCENT.setFont(fonthead);
			cellStylePERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePERCENT.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePERCENT.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT
					.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStylePERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePERCENT.setWrapText(true);

			CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
			cellStyleRED.setFont(fonthead);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);

			CellStyle cellStylePE = wb.createCellStyle();// 表头样式
			cellStylePE.setFont(fonthead);
			cellStylePE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePE.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			cellStylePE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePE.setWrapText(true);

			CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			Row row = sheet.createRow((short) 0);
			// 创建这一行的一列，即创建单元格(CELL)
			Cell cell = row.createCell((short) 0);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("DEALER  SELL-OUT PERFORMANCES");// 标题
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

			CellStyle contextstyle = wb.createCellStyle();
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			contextstyle.setFont(fontinfo);
			DataFormat df = wb.createDataFormat();
			//
			int ce = DealerSelloutInfo.size() + 6;

			row = sheet.createRow((short) 5);
			// 创建这一行的一列，即创建单元格(CELL)
			cell = row.createCell((short) 3);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,D7:D" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,D7:D" + ce + ")");

			cell = row.createCell((short) 4);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,E7:E" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,E7:E" + ce + ")");

			cell = row.createCell((short) 5);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,F7:F" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,F7:F" + ce + ")");

			cell = row.createCell((short) 6);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,G7:G" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,G7:G" + ce + ")");

			cell = row.createCell((short) 7);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,H7:H" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,H7:H" + ce + ")");

			cell = row.createCell((short) 8);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,I7:I" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,I7:I" + ce + ")");

			cell = row.createCell((short) 9);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,J7:J" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,J7:J" + ce + ")");

			cell = row.createCell((short) 10);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,K7:K" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,K7:K" + ce + ")");

			cell = row.createCell((short) 11);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,L7:L" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,L7:L" + ce + ")");

			cell = row.createCell((short) 12);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("TEXT(K6/L6,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT(K6/L6,\"0.00%\")");

			cell = row.createCell((short) 13);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellValue("100%");

			
			cell = row.createCell((short) 14);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("TEXT((J6-F6)/F6,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT((J6-F6)/F6,\"0.00%\")");
			
			
			cell = row.createCell((short) 15);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("TEXT((K6-G6)/G6,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT((K6-G6)/G6,\"0.00%\")");
			
			
			cell = row.createCell((short) 16);
			cell.setCellValue("SUM(F6/E6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(F6/E6)");

			
			
			cell = row.createCell((short) 17);
			cell.setCellValue("SUM(G6/E6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(G6/E6)");
			
			
			cell = row.createCell((short) 18);
			cell.setCellValue("SUM(J6/I6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(J6/I6)");

			cell = row.createCell((short) 19);
			cell.setCellValue("SUM(K6/I6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(K6/I6)");
			

			

			cell = row.createCell((short) 20);
			cell.setCellValue("SUM(S6-Q6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(S6-Q6)");

			cell = row.createCell((short) 21);
			cell.setCellValue("SUM(T6-R6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(T6-R6)");

			
			// 从17行开始放另一个表格的表头

			int rows_max = 0; // 最大的一个项有几个子项

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map map = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 2);
				for (int i = 0; i < header.length; i++) {

					String headerTemp = header[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (i >= 4 && i < 8) {
						cell.setCellStyle(cellStylePERCENT);
					} else if (i >= 8 && i < 16) {
						cell.setCellStyle(cellStyleYellow);
					} else {
						cell.setCellStyle(cellStyleWHITE);
					}
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2,
								rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (map.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (map.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < header.length; j++) {
							if (header[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 2,
								k + 2, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k
									+ 2 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int d = 0; d < dataList.size(); d++) {

				HashMap<String, Object> dataMap = dataList.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 2 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fields.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fields[c]) != null
							&& dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fields[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
								cellStylePERCENT.setDataFormat(df
										.getFormat("#,##0"));
								cellStyleGreen.setDataFormat(df
										.getFormat("#,##0"));
								cellStyleWHITE.setDataFormat(df
										.getFormat("#,##0"));
								cellStylePE
										.setDataFormat(df.getFormat("#,##0"));
								cellStyleRED.setDataFormat(df
										.getFormat("#,##0"));
								cellStyleYellow.setDataFormat(df
										.getFormat("#,##0"));
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fields[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c])
									.toString());
						}
						if (c >= 3 && c < 7) {
							cellStylePERCENT.setDataFormat(df
									.getFormat("#,##0"));
							contentCell.setCellStyle(cellStylePERCENT);
							if (dataMap.get(fields[c]).toString().contains("-")) {
								cellStyleRED.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStyleRED);
							}
						} else if (c >= 7 && c < 15) {
							cellStyleYellow
									.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(cellStyleYellow);
							if (dataMap.get(fields[c]).toString().contains("-")) {
								cellStyleRED.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStyleRED);
							}
						} else if (c > 17 && c <= 19) {
							cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(cellStyleGreen);
							if (dataMap.get(fields[c]).toString().contains("-")) {
								cellStyleRED.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStyleRED);
							}
						} else if (c > 23) {
							cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(cellStyleGreen);
							if (dataMap.get(fields[c]).toString().contains("-")) {
								cellStyleRED.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStyleRED);
							}
						} else {
							cellStyleWHITE.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(cellStyleWHITE);
						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void monthCPUsellout(SXSSFWorkbook wb, String partyName,
			String CountryId, String beginDateOne, String endDateOne,String tBeginDate,String tEndDate) {

		try {

			// 判断传过来的时间区
			String searchStr = null;
			String conditions = "";
			String center = "";
			String country = "";
			String region = "";
			String office = "";

			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			if (!WebPageUtil.isHAdmin()) {
				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")
						|| request.getParameter("country") != null
						&& !request.getParameter("country").equals("")
						|| request.getParameter("region") != null
						&& !request.getParameter("region").equals("")
						|| request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {

					if (request.getParameter("center") != null
							&& !request.getParameter("center").equals("")) {
						center = request.getParameter("center");
						conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
								+ center + "')   ";
					}

					if (request.getParameter("country") != null
							&& !request.getParameter("country").equals("")) {
						country = request.getParameter("country");
						CountryId=country;
						conditions = "  pa.country_id= " + country + "  ";
					}
					if (request.getParameter("region") != null
							&& !request.getParameter("region").equals("")) {
						region = request.getParameter("region");
						conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
								+ region
								+ "'  OR PARTY_ID='"+region+"'))  ";
					}
					if (request.getParameter("office") != null
							&& !request.getParameter("office").equals("")) {
						office = request.getParameter("office");
						conditions = "    pa.party_id IN ('" + office + "')  ";
					}

				} else {
					if (null != userPartyIds && !"".equals(userPartyIds)) {
						conditions = "  pa.party_id in (" + userPartyIds + ")  ";
					} else {
						conditions = "  1=2  ";
					}
				}

			} else {
				conditions = " 1=1 ";

			}
			if (request.getParameter("level") != null
					&& !request.getParameter("level").equals("")) {
				conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
			}
			String[] days = beginDateOne.split("-");
			// 创建一个sheet【Sell-out Summary】
			int year = Integer.parseInt(days[0]);
			int month = Integer.parseInt(days[1]);
			String beginDate = beginDateOne;
			String endDate = endDateOne;

			// 表头数据
			String[] headers = { "Rank", "Regional Head", "AREA" };
			HashMap<String, ArrayList<HashMap<String, Object>>> coreMap = null;
			List<HashMap<String, Object>> coreProduct = excelService
					.selectCoreProductByCountry(CountryId,WebPageUtil.isHQRole());

			if (coreProduct.size() > 0) {
				coreMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < coreProduct.size(); m++) {
					if (coreMap.get(coreProduct.get(m).get("product_line")
							.toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(coreProduct.get(m));
						coreMap.put(coreProduct.get(m).get("product_line")
								.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = coreMap
								.get(coreProduct.get(m).get("product_line")
										.toString());
						modelList.add(coreProduct.get(m));
					}

				}

			}
			List<HashMap<String, Object>> coreLine = excelService
					.selectCoreLine(CountryId);
			String order = "";
			if (coreLine.size() > 0) {
				order = coreLine.get(0).get("product_line").toString();
			}

			for (int i = 0; i < coreLine.size(); i++) {
				if (coreMap != null) {
					if (coreMap.get(coreLine.get(i).get("product_line")) != null) {
						ArrayList<HashMap<String, Object>> coreP = coreMap
								.get(coreLine.get(i).get("product_line"));
						for (int j = 0; j < coreP.size(); j++) {
							headers = insert(
									headers,
									coreP.get(j).get("product_line")
											+ " sellout_"
											+ coreP.get(j).get("model"));
						}
						headers = insert(headers,
								coreLine.get(i).get("product_line")
										+ " TTL Sellout");
						headers = insert(headers,
								coreLine.get(i).get("product_line") + " TARGET");
						headers = insert(headers,
								coreLine.get(i).get("product_line") + " ACH");
					}
				}

			}

			// 按照对应格式将表头传入
			ArrayList columns = new ArrayList();
			for (int i = 0; i < headers.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headers[i]);
				columnMap.put("field", headers[i]);
				columns.add(columnMap);
			}
			String[] header = new String[columns.size()];
			String[] fields = new String[columns.size()];
			for (int i = 0, l = columns.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columns.get(i);
				header[i] = columnMap.get("header").toString();
				fields[i] = columnMap.get("field").toString();

			}

			String[] headersTwo = { "Rank", "Saleman", "Account", };
			for (int i = 0; i < coreLine.size(); i++) {
				if (coreMap != null) {
					if (coreMap.get(coreLine.get(i).get("product_line")) != null) {
						ArrayList<HashMap<String, Object>> coreP = coreMap
								.get(coreLine.get(i).get("product_line"));
						for (int j = 0; j < coreP.size(); j++) {
							headersTwo = insert(
									headersTwo,
									coreP.get(j).get("product_line")
											+ " sellout_"
											+ coreP.get(j).get("model"));
						}
						headersTwo = insert(headersTwo,
								coreLine.get(i).get("product_line")
										+ " TTL Sellout");
						headersTwo = insert(headersTwo,
								coreLine.get(i).get("product_line") + " TARGET");
						headersTwo = insert(headersTwo,
								coreLine.get(i).get("product_line") + " ACH");
					}

				}

			}
			// 按照对应格式将表头传入
			ArrayList columnsTwo = new ArrayList();
			for (int i = 0; i < headersTwo.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersTwo[i]);
				columnMap.put("field", headersTwo[i]);
				columnsTwo.add(columnMap);
			}

			String[] headerTwo = new String[columnsTwo.size()];
			String[] fieldsTwo = new String[columnsTwo.size()];
			for (int i = 0, l = columnsTwo.size(); i < l; i++) {
				HashMap columnMap = (HashMap) columnsTwo.get(i);
				headerTwo[i] = columnMap.get("header").toString();
				fieldsTwo[i] = columnMap.get("field").toString();

			}

			// 表头数据
			String[] headersThree = { "Rank", "Acfo", "Area",

			};

			for (int i = 0; i < coreLine.size(); i++) {
				if (coreMap != null) {
					if (coreMap.get(coreLine.get(i).get("product_line")) != null) {
						ArrayList<HashMap<String, Object>> coreP = coreMap
								.get(coreLine.get(i).get("product_line"));
						for (int j = 0; j < coreP.size(); j++) {
							headersThree = insert(headersThree, coreP.get(j)
									.get("product_line")
									+ " sellout_"
									+ coreP.get(j).get("model"));
						}
						headersThree = insert(headersThree, coreLine.get(i)
								.get("product_line") + " TTL Sellout");
						headersThree = insert(headersThree, coreLine.get(i)
								.get("product_line") + " TARGET");
						headersThree = insert(headersThree, coreLine.get(i)
								.get("product_line") + " ACH");
					}
				}

			}

			ArrayList columnsThree = new ArrayList();
			for (int i = 0; i < headersThree.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersThree[i]);
				columnMap.put("field", headersThree[i]);
				columnsThree.add(columnMap);
			}

			String[] headerThree = new String[columnsThree.size()];
			String[] fieldsThree = new String[columnsThree.size()];
			for (int i = 0, l = columnsThree.size(); i < l; i++) {
				HashMap columnMap = (HashMap) columnsThree.get(i);
				headerThree[i] = columnMap.get("header").toString();
				fieldsThree[i] = columnMap.get("field").toString();

			}

			// 表头数据
			String[] headersFour = { "No of shop", "DEALER", "STORE", };

			for (int i = 0; i < coreLine.size(); i++) {
				if (coreMap != null) {
					if (coreMap.get(coreLine.get(i).get("product_line")) != null) {
						ArrayList<HashMap<String, Object>> coreP = coreMap
								.get(coreLine.get(i).get("product_line"));
						for (int j = 0; j < coreP.size(); j++) {
							headersFour = insert(
									headersFour,
									coreP.get(j).get("product_line")
											+ " sellout_"
											+ coreP.get(j).get("model"));
						}
						headersFour = insert(headersFour,
								coreLine.get(i).get("product_line")
										+ " TTL Sellout");
					}
				}

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> stockMap = null;
			List<HashMap<String, Object>> coreStockProduct = excelService
					.selectCoreProductByStock(CountryId,WebPageUtil.isHQRole(),beginDate,endDate);

			if (coreStockProduct.size() > 0) {
				stockMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < coreStockProduct.size(); m++) {
					if (stockMap.get(coreStockProduct.get(m)
							.get("product_line").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(coreStockProduct.get(m));
						stockMap.put(coreStockProduct.get(m)
								.get("product_line").toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = stockMap
								.get(coreStockProduct.get(m)
										.get("product_line").toString());
						modelList.add(coreStockProduct.get(m));
					}

				}

			}

			for (int i = 0; i < coreLine.size(); i++) {
				if (stockMap != null) {
					if (stockMap.get(coreLine.get(i).get("product_line")) != null) {
						ArrayList<HashMap<String, Object>> coreI = stockMap
								.get(coreLine.get(i).get("product_line"));
						for (int j = 0; j < coreI.size(); j++) {
							headersFour = insert(
									headersFour,
									coreI.get(j).get("product_line")
											+ " inventory_"
											+ coreI.get(j).get("model"));
						}
						headersFour = insert(headersFour,
								coreLine.get(i).get("product_line")
										+ " TTL Inventory");
					}
				}

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> sampleMap = null;
			List<HashMap<String, Object>> coreSampleProduct = excelService
					.selectCoreProductBySample(countryId,WebPageUtil.isHQRole(),beginDate,endDate);

			if (coreSampleProduct.size() > 0) {
				sampleMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				for (int m = 0; m < coreSampleProduct.size(); m++) {
					if (sampleMap.get(coreSampleProduct.get(m)
							.get("product_line").toString()) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						modelList.add(coreSampleProduct.get(m));
						sampleMap.put(
								coreSampleProduct.get(m).get("product_line")
										.toString(), modelList);
					} else {
						ArrayList<HashMap<String, Object>> modelList = sampleMap
								.get(coreSampleProduct.get(m)
										.get("product_line").toString());
						modelList.add(coreSampleProduct.get(m));
					}

				}

			}

			for (int i = 0; i < coreLine.size(); i++) {
				if (sampleMap != null) {

					if (sampleMap.get(coreLine.get(i).get("product_line")) != null) {
						ArrayList<HashMap<String, Object>> coreS = sampleMap
								.get(coreLine.get(i).get("product_line"));
						for (int j = 0; j < coreS.size(); j++) {
							headersFour = insert(
									headersFour,
									coreS.get(j).get("product_line")
											+ " Display"
											+ coreS.get(j).get("model"));
						}
						headersFour = insert(headersFour,
								coreLine.get(i).get("product_line")
										+ " TTL Display");
					}
				}

			}

			ArrayList columnsFour = new ArrayList();
			for (int i = 0; i < headersFour.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersFour[i]);
				columnMap.put("field", headersFour[i]);
				columnsFour.add(columnMap);
			}

			String[] headerFour = new String[columnsFour.size()];
			String[] fieldsFour = new String[columnsFour.size()];
			for (int i = 0, l = columnsFour.size(); i < l; i++) {
				HashMap columnMap = (HashMap) columnsFour.get(i);
				headerFour[i] = columnMap.get("header").toString();
				fieldsFour[i] = columnMap.get("field").toString();

			}

			LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
			List<HashMap<String, Object>> CPUByRHInfo = excelService
					.selectInfoByCPURH(beginDate, endDate, searchStr,
							conditions);

			List<HashMap<String, Object>> CPUByRHTotal = excelService
					.selectTotalByCPURH(beginDate, endDate, searchStr,
							conditions, countryId,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			HashMap<String, ArrayList<HashMap<String, Object>>> CPUByRHTotalMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < CPUByRHTotal.size(); m++) {
				if (CPUByRHTotalMap.get(CPUByRHTotal.get(m).get("PARTY_NAME")
						.toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(CPUByRHTotal.get(m));
					CPUByRHTotalMap.put(CPUByRHTotal.get(m).get("PARTY_NAME")
							.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = CPUByRHTotalMap
							.get(CPUByRHTotal.get(m).get("PARTY_NAME")
									.toString());
					modelList.add(CPUByRHTotal.get(m));
				}

			}

			List<HashMap<String, Object>> CPUByRHDatas = excelService
					.selectDataByCPURH(beginDate, endDate, countryId,
							searchStr, conditions,WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> CPUByRHDatasMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < CPUByRHDatas.size(); m++) {
				if (CPUByRHDatasMap.get(CPUByRHDatas.get(m).get("PARTY_NAME")
						.toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(CPUByRHDatas.get(m));
					CPUByRHDatasMap.put(CPUByRHDatas.get(m).get("PARTY_NAME")
							.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = CPUByRHDatasMap
							.get(CPUByRHDatas.get(m).get("PARTY_NAME")
									.toString());
					modelList.add(CPUByRHDatas.get(m));
				}

			}

			List<HashMap<String, Object>> re = excelService
					.selectRegionalHeadByParty(searchStr, conditions);

			for (int i = 0; i < CPUByRHInfo.size(); i++) {
				HashMap<String, Object> data = new HashMap<String, Object>();

				for (int j = 0; j < re.size(); j++) {
					if (CPUByRHInfo.get(i).get("PARTY_ID")
							.equals(re.get(j).get("PARTY_ID"))) {
						data.put("Regional Head", re.get(j).get("userName"));
					}
				}

				// data.put("Rank", i + 1);
				data.put("AREA", CPUByRHInfo.get(i).get("PARTY_NAME"));

				if (CPUByRHTotalMap.get(CPUByRHInfo.get(i).get("PARTY_NAME")) != null) {

					ArrayList<HashMap<String, Object>> disListBySMART = CPUByRHTotalMap
							.get(CPUByRHInfo.get(i).get("PARTY_NAME"));

					for (int j = 0; j < disListBySMART.size(); j++) {
						BigDecimal bd = new BigDecimal(disListBySMART.get(j)
								.get("saleQty").toString());
						// String am = bd.toPlainString();
						data.put(disListBySMART.get(j).get("product_line")
								+ " TTL Sellout", bd.longValue());

						Long cs = bd.longValue();

						bd = new BigDecimal(disListBySMART.get(j)
								.get("targetQty").toString());
						// String target = bd.toPlainString();

						data.put(disListBySMART.get(j).get("product_line")
								+ " TARGET", bd.longValue());

						double ct = bd.longValue();

						double ach = cs / ct * 100;
						long lnum = Math.round(ach);
						data.put(disListBySMART.get(j).get("product_line")
								+ " ACH", lnum + "%");
					}

				}

				if (CPUByRHDatasMap.get(CPUByRHInfo.get(i).get("PARTY_NAME")) != null) {

					ArrayList<HashMap<String, Object>> disListBySMART = CPUByRHDatasMap
							.get(CPUByRHInfo.get(i).get("PARTY_NAME"));

					for (int j = 0; j < disListBySMART.size(); j++) {
						BigDecimal bd = new BigDecimal(disListBySMART.get(j)
								.get("quantity").toString());

						data.put(
								disListBySMART.get(j).get("product_line")
										+ " sellout_"
										+ disListBySMART.get(j).get("model"),
								bd.longValue());
					}

				}

				dataList.add(data);

			}
			DateUtil.Order(dataList, order);

			for (int i = 0; i < dataList.size(); i++) {
				dataList.get(i).put("Rank", i + 1);
			}

			HashMap<String, Object> dataMapOne = new HashMap<String, Object>();
			dataMapOne.put("Regional Head", "Total");
			List<HashMap<String, Object>> TTL = excelService.selectTTLByCPURH(
					beginDate, endDate, searchStr, conditions, countryId,WebPageUtil.isHQRole());
			for (int j = 0; j < TTL.size(); j++) {

				BigDecimal bd = new BigDecimal(TTL.get(j).get("quantity")
						.toString());

				dataMapOne.put(TTL.get(j).get("product_line") + " sellout_"
						+ TTL.get(j).get("model"), bd.longValue());
			}

			List<HashMap<String, Object>> SaleTTL = excelService
					.selectSaleTTLByCPURH(beginDate, endDate, searchStr,
							conditions, countryId,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			for (int j = 0; j < SaleTTL.size(); j++) {
				Long cSum = (long) 0;
				BigDecimal bd = null;
				String cTarget = "";
				Long ct = (long) 0;

				bd = new BigDecimal(SaleTTL.get(j).get("saleSum").toString());

				String cs = bd.toPlainString();
				cSum = bd.longValue();
				dataMapOne.put(SaleTTL.get(j).get("product_line")
						+ " TTL Sellout", cSum);

				bd = new BigDecimal(SaleTTL.get(j).get("targetSum").toString());

				ct = bd.longValue();

				dataMapOne.put(SaleTTL.get(j).get("product_line") + " TARGET",
						ct);

				if(ct!=0){
					double Cach = cSum / ct * 100;
					long lnum = Math.round(Cach);
					dataMapOne.put(SaleTTL.get(j).get("product_line") + " ACH",
							lnum + "%");
				}

			}
			dataList.add(dataMapOne);

			List<HashMap<String, Object>> saleTotal = excelService
					.selectTotalByCPUSALE(beginDate, endDate, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			HashMap<String, ArrayList<HashMap<String, Object>>> saleTotalMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < saleTotal.size(); m++) {
				if (saleTotalMap.get(saleTotal.get(m).get("userName")
						.toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(saleTotal.get(m));
					saleTotalMap.put(saleTotal.get(m).get("userName")
							.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = saleTotalMap
							.get(saleTotal.get(m).get("userName").toString());
					modelList.add(saleTotal.get(m));
				}

			}

			List<HashMap<String, Object>> modelTotal = excelService
					.selectModelByCPUSALE(beginDate, endDate, searchStr,
							conditions,WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> modelTotalMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelTotal.size(); m++) {
				if (modelTotalMap.get(modelTotal.get(m).get("userName")
						.toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelTotal.get(m));
					modelTotalMap.put(modelTotal.get(m).get("userName")
							.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = modelTotalMap
							.get(modelTotal.get(m).get("userName").toString());
					modelList.add(modelTotal.get(m));
				}

			}

			LinkedList<HashMap<String, Object>> dataListTwo = new LinkedList<HashMap<String, Object>>();
			List<HashMap<String, Object>> saleInfo = excelService
					.selectInfoByCPUSALE(beginDate, endDate, searchStr,
							conditions);
			List<HashMap<String, Object>> Account = excelService
					.selectPartyNameByuser(searchStr, conditions);
			for (int i = 0; i < saleInfo.size(); i++) {
				HashMap<String, Object> data = new HashMap<String, Object>();
				// data.put("Rank", i + 1);
				data.put("Saleman", saleInfo.get(i).get("userName"));

				if (Account.size() > 1) {
					String a = "";
					for (int k = 0; k < Account.size(); k++) {
						if (saleInfo.get(i).get("userId")
								.equals(Account.get(k).get("userId"))) {

							a += Account.get(k).get("PARTY_NAME") + ",";
						}

					}
					if (a.length() > 0) {
						data.put("Account", a.substring(0, a.length() - 1));
					}
				}

				if (saleTotalMap.get(saleInfo.get(i).get("userName")) != null) {
					Long cSum = (long) 0;
					BigDecimal bd = null;
					String cTarget = "";
					Long ct = (long) 0;
					ArrayList<HashMap<String, Object>> disListBySMART = saleTotalMap
							.get(saleInfo.get(i).get("userName"));

					for (int j = 0; j < disListBySMART.size(); j++) {
						bd = new BigDecimal(disListBySMART.get(j)
								.get("saleQty").toString());
						cSum = bd.longValue();
						data.put(disListBySMART.get(j).get("product_line")
								+ " TTL Sellout", cSum);

						bd = new BigDecimal(disListBySMART.get(j)
								.get("targetQty").toString());
						ct = bd.longValue();
						data.put(disListBySMART.get(j).get("product_line")
								+ " TARGET", ct);

						if(ct!=0){
							double Cach = cSum / ct * 100;
							long lnum = Math.round(Cach);
							data.put(disListBySMART.get(j).get("product_line")
									+ " ACH", lnum + "%");
						}
					}

				}

				if (modelTotalMap.get(saleInfo.get(i).get("userName")) != null) {
					Long cSum = (long) 0;
					BigDecimal bd = null;
					String cTarget = "";
					Long ct = (long) 0;
					ArrayList<HashMap<String, Object>> disListBySMART = modelTotalMap
							.get(saleInfo.get(i).get("userName"));

					for (int k = 0; k < disListBySMART.size(); k++) {
						bd = new BigDecimal(disListBySMART.get(k)
								.get("saleQty").toString());

						data.put(
								disListBySMART.get(k).get("product_line")
										+ " sellout_"
										+ disListBySMART.get(k).get("model"),
								bd.longValue());
					}

				}

				dataListTwo.add(data);
			}

			DateUtil.Order(dataListTwo, order);
			for (int i = 0; i < dataListTwo.size(); i++) {
				dataListTwo.get(i).put("Rank", i + 1);
			}

			HashMap<String, Object> dataTwo = new HashMap<String, Object>();
			dataTwo.put("Saleman", "Total");

			List<HashMap<String, Object>> modelTTL = excelService
					.selectModelTTLByCPUSALE(beginDate, endDate, searchStr,
							conditions,WebPageUtil.isHQRole());
			for (int k = 0; k < modelTTL.size(); k++) {
				if (modelTTL.get(k).get("product_line") != null) {
					BigDecimal bd = new BigDecimal(modelTTL.get(k)
							.get("saleQty").toString());

					dataTwo.put(modelTTL.get(k).get("product_line")
							+ " sellout_" + modelTTL.get(k).get("model"),
							bd.longValue());

				}

			}

			List<HashMap<String, Object>> TTLBysaleMan = excelService
					.selectTTLByCPUSALE(beginDate, endDate, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);
			for (int j = 0; j < TTLBysaleMan.size(); j++) {
				Long cSum = (long) 0;
				BigDecimal bd = null;
				String cTarget = "";
				Long ct = (long) 0;

				if (TTLBysaleMan.get(j).get("product_line") != null) {

					bd = new BigDecimal(TTLBysaleMan.get(j).get("saleQty")
							.toString());
					cSum = bd.longValue();
					dataTwo.put(TTLBysaleMan.get(j).get("product_line")
							+ " TTL Sellout", cSum);

					bd = new BigDecimal(TTLBysaleMan.get(j).get("targetQty")
							.toString());
					ct = bd.longValue();
					dataTwo.put(TTLBysaleMan.get(j).get("product_line")
							+ " TARGET", ct);

					if(ct!=0){
						double Cach = cSum / ct * 100;
						long lnum = Math.round(Cach);
						dataTwo.put(TTLBysaleMan.get(j).get("product_line")
								+ " ACH", lnum + "%");
					}

				}

			}

			dataListTwo.add(dataTwo);

			LinkedList<HashMap<String, Object>> dataListThree = new LinkedList<HashMap<String, Object>>();

			List<HashMap<String, Object>> acfoTotal = excelService
					.selectTotalByCPUACFO(beginDate, endDate, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			HashMap<String, ArrayList<HashMap<String, Object>>> acfoTotalMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < acfoTotal.size(); m++) {
				if (acfoTotalMap.get(acfoTotal.get(m).get("userName")
						.toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(acfoTotal.get(m));
					acfoTotalMap.put(acfoTotal.get(m).get("userName")
							.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = acfoTotalMap
							.get(acfoTotal.get(m).get("userName").toString());
					modelList.add(acfoTotal.get(m));
				}

			}

			modelTotal = excelService.selectModelByCPUACFO(beginDate, endDate,
					searchStr, conditions,WebPageUtil.isHQRole());
			modelTotalMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelTotal.size(); m++) {
				if (modelTotalMap.get(modelTotal.get(m).get("userName")
						.toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelTotal.get(m));
					modelTotalMap.put(modelTotal.get(m).get("userName")
							.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = modelTotalMap
							.get(modelTotal.get(m).get("userName").toString());
					modelList.add(modelTotal.get(m));
				}

			}

			List<HashMap<String, Object>> acfoInfo = excelService
					.selectInfoByCPUACFO(beginDate, endDate, searchStr,
							conditions);
			List<HashMap<String, Object>> Area = excelService.selectAreaByUser(
					searchStr, conditions);

			for (int i = 0; i < acfoInfo.size(); i++) {
				HashMap<String, Object> dataThree = new HashMap<String, Object>();
				dataThree.put("Rank", i + 1);
				dataThree.put("Acfo", acfoInfo.get(i).get("userName"));

				if (Area.size() > 1) {
					String a = "";
					for (int k = 0; k < Area.size(); k++) {
						if (acfoInfo.get(i).get("userId").toString()
								.equals(Area.get(k).get("userId"))) {

							a += Area.get(k).get("PARTY_NAME") + ",";
						}

					}
					if (a.length() > 0) {

						dataThree.put("Area", a.substring(0, a.length() - 1));
					}

					if (acfoTotalMap.get(acfoInfo.get(i).get("userName")) != null) {
						Long cSum = (long) 0;
						BigDecimal bd = null;
						String cTarget = "";
						Long ct = (long) 0;
						ArrayList<HashMap<String, Object>> disListBySMART = acfoTotalMap
								.get(acfoInfo.get(i).get("userName"));

						for (int j = 0; j < disListBySMART.size(); j++) {
							bd = new BigDecimal(disListBySMART.get(j)
									.get("saleQty").toString());

							cSum = bd.longValue();
							dataThree.put(
									disListBySMART.get(j).get("product_line")
											+ " TTL Sellout", cSum);

							bd = new BigDecimal(disListBySMART.get(j)
									.get("targetQty").toString());

							ct = bd.longValue();
							dataThree.put(
									disListBySMART.get(j).get("product_line")
											+ " TARGET", ct);

							if(ct!=0){
								double Cach = cSum / ct * 100;
								long lnum = Math.round(Cach);
								dataThree.put(
										disListBySMART.get(j).get("product_line")
												+ " ACH", lnum + "%");
							}
						}

					}

					if (modelTotalMap.get(acfoInfo.get(i).get("userName")) != null) {
						Long cSum = (long) 0;
						BigDecimal bd = null;
						String cTarget = "";
						Long ct = (long) 0;
						ArrayList<HashMap<String, Object>> disListBySMART = modelTotalMap
								.get(acfoInfo.get(i).get("userName"));

						for (int k = 0; k < disListBySMART.size(); k++) {
							bd = new BigDecimal(disListBySMART.get(k)
									.get("saleQty").toString());

							dataThree.put(
									disListBySMART.get(k).get("product_line")
											+ " sellout_"
											+ disListBySMART.get(k)
													.get("model"),
									bd.longValue());
						}

					}

					dataListThree.add(dataThree);
				}
			}
			DateUtil.Order(dataListThree, order);
			for (int i = 0; i < dataListThree.size(); i++) {
				dataListThree.get(i).put("Rank", i + 1);
			}

			HashMap<String, Object> dataThreeMap = new HashMap<String, Object>();
			dataThreeMap.put("Acfo", "Total");

			List<HashMap<String, Object>> modelTTLByAcfo = excelService
					.selectModelTTLByCPUACFO(beginDate, endDate, searchStr,
							conditions,WebPageUtil.isHQRole());
			for (int k = 0; k < modelTTLByAcfo.size(); k++) {

				BigDecimal bd = new BigDecimal(modelTTLByAcfo.get(k)
						.get("saleQty").toString());

				dataThreeMap.put(modelTTLByAcfo.get(k).get("product_line")
						+ " sellout_" + modelTTLByAcfo.get(k).get("model"),
						bd.longValue());

			}

			List<HashMap<String, Object>> TTLByAcfo = excelService
					.selectTTLByCPUACFO(beginDate, endDate, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);
			for (int j = 0; j < TTLByAcfo.size(); j++) {
				Long cSum = (long) 0;
				BigDecimal bd = null;
				String cTarget = "";
				Long ct = (long) 0;

				bd = new BigDecimal(TTLByAcfo.get(j).get("saleQty").toString());

				cSum = bd.longValue();
				dataThreeMap.put(TTLByAcfo.get(j).get("product_line")
						+ " TTL Sellout", cSum);

				bd = new BigDecimal(TTLByAcfo.get(j).get("targetQty")
						.toString());
				ct = bd.longValue();
				dataThreeMap.put(TTLByAcfo.get(j).get("product_line")
						+ " TARGET", ct);

				if(ct!=0){
					double Cach = cSum / ct * 100;
					long lnum = Math.round(Cach);
					dataThreeMap.put(TTLByAcfo.get(j).get("product_line") + " ACH",
							lnum + "%");
				}

			}

			dataListThree.add(dataThreeMap);
			LinkedList<HashMap<String, Object>> dataListFour = new LinkedList<HashMap<String, Object>>();

			List<HashMap<String, Object>> BRANCHBysell = excelService
					.selectSellDataBYBRANCH(beginDate, endDate, searchStr,
							conditions,WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> BRANCHBysellMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < BRANCHBysell.size(); m++) {
				if (BRANCHBysellMap.get(BRANCHBysell.get(m).get("SHOP_ID")
						.toString()) == null) {

					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(BRANCHBysell.get(m));
					BRANCHBysellMap.put(BRANCHBysell.get(m).get("SHOP_ID")
							.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = BRANCHBysellMap
							.get(BRANCHBysell.get(m).get("SHOP_ID").toString());
					modelList.add(BRANCHBysell.get(m));
				}

			}

			List<HashMap<String, Object>> BRANCHByInven = excelService
					.selectCPUInventoryBYBRANCH(searchStr, conditions,WebPageUtil.isHQRole(),beginDate,endDate);

			HashMap<String, ArrayList<HashMap<String, Object>>> BRANCHByInvenMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < BRANCHByInven.size(); m++) {
				if (BRANCHByInvenMap.get(BRANCHByInven.get(m).get("SHOP_ID")
						.toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(BRANCHByInven.get(m));
					BRANCHByInvenMap.put(BRANCHByInven.get(m).get("SHOP_ID")
							.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = BRANCHByInvenMap
							.get(BRANCHByInven.get(m).get("SHOP_ID").toString());
					modelList.add(BRANCHByInven.get(m));
				}

			}

			List<HashMap<String, Object>> BRANCHByDis = excelService
					.selectCPUDisplayBYBRANCH(searchStr, conditions,WebPageUtil.isHQRole(),beginDate,endDate);

			HashMap<String, ArrayList<HashMap<String, Object>>> BRANCHByDisMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < BRANCHByDis.size(); m++) {
				if (BRANCHByDisMap.get(BRANCHByDis.get(m).get("SHOP_ID")
						.toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(BRANCHByDis.get(m));
					BRANCHByDisMap.put(BRANCHByDis.get(m).get("SHOP_ID")
							.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = BRANCHByDisMap
							.get(BRANCHByDis.get(m).get("SHOP_ID").toString());
					modelList.add(BRANCHByDis.get(m));
				}

			}

			List<HashMap<String, Object>> BRANCHBysellTotal = excelService
					.selectSellDataByTotal(beginDate, endDate, searchStr,
							conditions,WebPageUtil.isHQRole());
			HashMap<String, ArrayList<HashMap<String, Object>>> BRANCHBysellTotalMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < BRANCHBysellTotal.size(); m++) {
				if (BRANCHBysellTotalMap.get(BRANCHBysellTotal.get(m)
						.get("SHOP_ID").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(BRANCHBysellTotal.get(m));
					BRANCHBysellTotalMap.put(
							BRANCHBysellTotal.get(m).get("SHOP_ID").toString(),
							modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = BRANCHBysellTotalMap
							.get(BRANCHBysellTotal.get(m).get("SHOP_ID")
									.toString());
					modelList.add(BRANCHBysellTotal.get(m));
				}

			}

			List<HashMap<String, Object>> BRANCHByInvenTotal = excelService
					.selectCPUInventoryByTotal(searchStr, conditions,beginDate,endDate);

			HashMap<String, ArrayList<HashMap<String, Object>>> BRANCHByInvenTotalMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < BRANCHByInvenTotal.size(); m++) {
				if (BRANCHByInvenTotalMap.get(BRANCHByInvenTotal.get(m)
						.get("SHOP_ID").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(BRANCHByInvenTotal.get(m));
					BRANCHByInvenTotalMap
							.put(BRANCHByInvenTotal.get(m).get("SHOP_ID")
									.toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = BRANCHByInvenTotalMap
							.get(BRANCHByInvenTotal.get(m).get("SHOP_ID")
									.toString());
					modelList.add(BRANCHByInvenTotal.get(m));
				}

			}

			List<HashMap<String, Object>> BRANCHByDisTotal = excelService
					.selectCPUDisplayByTotal(searchStr, conditions,beginDate,endDate);

			HashMap<String, ArrayList<HashMap<String, Object>>> BRANCHByDisTotalMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < BRANCHByDisTotal.size(); m++) {
				if (BRANCHByDisTotalMap.get(BRANCHByDisTotal.get(m)
						.get("SHOP_ID").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(BRANCHByDisTotal.get(m));
					BRANCHByDisTotalMap.put(
							BRANCHByDisTotal.get(m).get("SHOP_ID").toString(),
							modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = BRANCHByDisTotalMap
							.get(BRANCHByDisTotal.get(m).get("SHOP_ID")
									.toString());
					modelList.add(BRANCHByDisTotal.get(m));
				}

			}

			List<HashMap<String, Object>> BRANCHByInfo = excelService
					.selectCPUInfoByBRANCH(searchStr, conditions);
			HashMap<String, Object> dataFourByde = new HashMap<String, Object>();
			for (int i = 0; i < BRANCHByInfo.size(); i++) {
				HashMap<String, Object> dataFour = new HashMap<String, Object>();
				dataFour.put("No of shop", i + 1);
				dataFour.put("DEALER", BRANCHByInfo.get(i).get("DEALER"));
				dataFour.put("STORE", BRANCHByInfo.get(i).get("SHOP_NAME"));
				if (BRANCHBysellMap.get("" + BRANCHByInfo.get(i).get("SHOP_ID")
						+ "") != null) {
					BigDecimal bd = null;
					String cTarget = "";
					ArrayList<HashMap<String, Object>> disListBySMART = BRANCHBysellMap
							.get("" + BRANCHByInfo.get(i).get("SHOP_ID") + "");

					for (int k = 0; k < disListBySMART.size(); k++) {
						bd = new BigDecimal(disListBySMART.get(k)
								.get("quantity").toString());

						dataFour.put(
								disListBySMART.get(k).get("product_line")
										+ " sellout_"
										+ disListBySMART.get(k).get("model"),
								bd.longValue());
					}

				}

				if (BRANCHByInvenMap.get(""
						+ BRANCHByInfo.get(i).get("SHOP_ID") + "") != null) {
					ArrayList<HashMap<String, Object>> disListBySMART = BRANCHByInvenMap
							.get("" + BRANCHByInfo.get(i).get("SHOP_ID") + "");

					for (int k = 0; k < disListBySMART.size(); k++) {
						BigDecimal bd = new BigDecimal(disListBySMART.get(k)
								.get("quantity").toString());

						dataFour.put(
								disListBySMART.get(k).get("product_line")
										+ " inventory_"
										+ disListBySMART.get(k).get("model"),
								bd.longValue());
					}

				}

				if (BRANCHByDisMap.get("" + BRANCHByInfo.get(i).get("SHOP_ID")
						+ "") != null) {
					ArrayList<HashMap<String, Object>> disListBySMART = BRANCHByDisMap
							.get("" + BRANCHByInfo.get(i).get("SHOP_ID") + "");

					for (int k = 0; k < disListBySMART.size(); k++) {
						BigDecimal bd = new BigDecimal(disListBySMART.get(k)
								.get("quantity").toString());

						dataFour.put(
								disListBySMART.get(k).get("product_line")
										+ " display_"
										+ disListBySMART.get(k).get("model"),
								bd.longValue());

					}

				}

				if (BRANCHBysellTotalMap.get(""
						+ BRANCHByInfo.get(i).get("SHOP_ID") + "") != null) {
					ArrayList<HashMap<String, Object>> disListBySMART = BRANCHBysellTotalMap
							.get("" + BRANCHByInfo.get(i).get("SHOP_ID") + "");

					for (int k = 0; k < disListBySMART.size(); k++) {
						BigDecimal bd = new BigDecimal(disListBySMART.get(k)
								.get("quantity").toString());

						dataFour.put(disListBySMART.get(k).get("product_line")
								+ " TTL Sellout", bd.longValue());
					}

				}

				if (BRANCHByInvenTotalMap.get(""
						+ BRANCHByInfo.get(i).get("SHOP_ID") + "") != null) {
					ArrayList<HashMap<String, Object>> disListBySMART = BRANCHByInvenTotalMap
							.get("" + BRANCHByInfo.get(i).get("SHOP_ID") + "");

					for (int k = 0; k < disListBySMART.size(); k++) {

						BigDecimal bd = new BigDecimal(disListBySMART.get(k)
								.get("quantity").toString());

						dataFour.put(disListBySMART.get(k).get("product_line")
								+ " TTL Inventory", bd.longValue());
					}

				}

				if (BRANCHByDisTotalMap.get(""
						+ BRANCHByInfo.get(i).get("SHOP_ID") + "") != null) {
					ArrayList<HashMap<String, Object>> disListBySMART = BRANCHByDisTotalMap
							.get("" + BRANCHByInfo.get(i).get("SHOP_ID") + "");

					for (int k = 0; k < disListBySMART.size(); k++) {

						BigDecimal bd = new BigDecimal(disListBySMART.get(k)
								.get("quantity").toString());

						dataFour.put(disListBySMART.get(k).get("product_line")
								+ " TTL Display", bd.longValue());

					}

				}

				dataListFour.add(dataFour);
			}

			List<HashMap<String, Object>> BRANCHBysellTTL = excelService
					.selectSellDataByTTL(beginDate, endDate, searchStr,
							conditions,WebPageUtil.isHQRole());
			for (int k = 0; k < BRANCHBysellTTL.size(); k++) {
				BigDecimal bd = new BigDecimal(BRANCHBysellTTL.get(k)
						.get("quantity").toString());

				dataFourByde.put(BRANCHBysellTTL.get(k).get("product_line")
						+ " sellout_" + BRANCHBysellTTL.get(k).get("model"),
						bd.longValue());

			}

			List<HashMap<String, Object>> BRANCHByInvenTTL = excelService
					.selectCPUInventoryByTTL(searchStr, conditions,WebPageUtil.isHQRole(),beginDate,endDate);
			for (int k = 0; k < BRANCHByInvenTTL.size(); k++) {
				BigDecimal bd = new BigDecimal(BRANCHByInvenTTL.get(k)
						.get("quantity").toString());

				dataFourByde.put(BRANCHByInvenTTL.get(k).get("product_line")
						+ " inventory_" + BRANCHByInvenTTL.get(k).get("model"),
						bd.longValue());

			}

			List<HashMap<String, Object>> BRANCHByDisTTL = excelService
					.selectCPUDisplayByTTL(searchStr, conditions,WebPageUtil.isHQRole(),beginDate,endDate);
			for (int k = 0; k < BRANCHByDisTTL.size(); k++) {
				BigDecimal bd = new BigDecimal(BRANCHByDisTTL.get(k)
						.get("quantity").toString());

				dataFourByde.put(BRANCHByDisTTL.get(k).get("product_line")
						+ " display_" + BRANCHByDisTTL.get(k).get("model"),
						bd.longValue());

			}

			List<HashMap<String, Object>> BRANCHBysellSUM = excelService
					.selectSellDataBySUM(beginDate, endDate, searchStr,
							conditions,WebPageUtil.isHQRole());
			for (int k = 0; k < BRANCHBysellSUM.size(); k++) {
				BigDecimal bd = new BigDecimal(BRANCHBysellSUM.get(k)
						.get("quantity").toString());

				dataFourByde.put(BRANCHBysellSUM.get(k).get("product_line")
						+ " TTL Sellout", bd.longValue());

			}

			List<HashMap<String, Object>> BRANCHByInvenSUM = excelService
					.selectCPUInventoryBySUM(searchStr, conditions,beginDate,endDate);
			for (int k = 0; k < BRANCHByInvenSUM.size(); k++) {
				BigDecimal bd = new BigDecimal(BRANCHByInvenSUM.get(k)
						.get("quantity").toString());

				dataFourByde.put(BRANCHByInvenSUM.get(k).get("product_line")
						+ " TTL Inventory", bd.longValue());

			}

			List<HashMap<String, Object>> BRANCHByDisSUM = excelService
					.selectCPUDisplayBySUM(searchStr, conditions,beginDate,endDate);
			for (int k = 0; k < BRANCHByDisSUM.size(); k++) {
				BigDecimal bd = new BigDecimal(BRANCHByDisSUM.get(k)
						.get("quantity").toString());

				dataFourByde.put(BRANCHByDisSUM.get(k).get("product_line")
						+ " TTL Display", bd.longValue());

			}

			dataFourByde.put("No of shop", "Sub-Total");
			dataListFour.add(dataFourByde);

			// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
			Sheet sheet = wb.createSheet("Core Product sellout");
			sheet.setZoom(3, 4);
			// 创建字体
			Font fontinfo = wb.createFont();
			fontinfo.setFontHeightInPoints((short) 11); // 字体大小
			fontinfo.setFontName("Trebuchet MS");
			Font fonthead = wb.createFont();
			fonthead.setFontHeightInPoints((short) 12);
			fonthead.setFontName("Trebuchet MS");

			// colSplit, rowSplit,leftmostColumn, topRow,
			sheet.createFreezePane(3, 2, 3, 2);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat
					.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
														// hh:mm:ss

			CellStyle cellStylehead = wb.createCellStyle();// 表头样式
			cellStylehead.setFont(fonthead);
			cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
			cellStyleRED.setFont(fonthead);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);

			CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fonthead);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fonthead);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			Row row = sheet.createRow((short) 0);
			// 创建这一行的一列，即创建单元格(CELL)
			Cell cell = row.createCell((short) 0);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("TCL " + partyName);// 标题
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

			// 第二行
			Row rowSec = sheet.createRow((short) 1);
			cell = rowSec.createCell((short) 0);
			cell.setCellStyle(cellStylename);
			cell.setCellValue("Core Product sellout");
			// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

			CellStyle contextstyle = wb.createCellStyle();
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			contextstyle.setFont(fontinfo);
			// 从17行开始放另一个表格的表头

			int rows_max = 0; // 最大的一个项有几个子项

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map map = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 2);
				for (int i = 0; i < header.length; i++) {

					String headerTemp = header[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					cell.setCellStyle(cellStylehead);

					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2,
								rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (map.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (map.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < header.length; j++) {
							if (header[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 2,
								k + 2, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k
									+ 2 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerTwo.length; i++) {
				String h = headerTwo[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapTwo = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + dataList.size() + 10);
				for (int i = 0; i < headerTwo.length; i++) {
					cell.setCellStyle(cellStyleinfo);
					String headerTemp = headerTwo[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					cell.setCellStyle(cellStylehead);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(dataList
								.size() + 10, rows_max + dataList.size() + 9,
								(num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);
					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapTwo.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapTwo.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerTwo.length; j++) {
							if (headerTwo[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k
								+ dataList.size() + 10, k + dataList.size()
								+ 10, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k
									+ dataList.size() + 10, k + dataList.size()
									+ 10 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapTwo.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapTwo.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerThree.length; i++) {
				String h = headerThree[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapThree = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + dataList.size() + 10
						+ dataListTwo.size() + 10);
				for (int i = 0; i < headerThree.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerThree[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(dataList
								.size() + 10 + dataListTwo.size() + 10,
								rows_max + +dataList.size() + 10
										+ dataListTwo.size() + 9, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapThree.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapThree.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerThree.length; j++) {
							if (headerThree[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k
								+ dataList.size() + 10 + dataListTwo.size()
								+ 10, k + dataList.size() + 10
								+ dataListTwo.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k
									+ dataList.size() + 10 + dataListTwo.size()
									+ 10, k + dataList.size() + 10
									+ dataListTwo.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapThree.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapThree.put(key, null);
						}
					}
				}
			}

			// ACFO
			for (int i = 0; i < headerFour.length; i++) {
				String h = headerFour[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapFour = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + dataList.size() + 10
						+ dataListTwo.size() + 10 + dataListThree.size() + 10);
				for (int i = 0; i < headerFour.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerFour[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(dataList
								.size()
								+ 10
								+ dataListTwo.size()
								+ 10
								+ dataListThree.size() + 10, rows_max
								+ dataList.size() + 10 + dataListTwo.size()
								+ 10 + dataListThree.size() + 9, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapFour.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapFour.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerFour.length; j++) {
							if (headerFour[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k
								+ dataList.size() + 10 + dataListTwo.size()
								+ 10 + dataListThree.size() + 10,

						k + dataList.size() + 10 + dataListTwo.size() + 10
								+ dataListThree.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k
									+ dataList.size() + 10 + dataListTwo.size()
									+ 10 + dataListThree.size() + 10, k
									+ dataList.size() + 10 + dataListTwo.size()
									+ 10 + dataListThree.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapFour.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapFour.put(key, null);
						}
					}
				}
			}

			for (int d = 0; d < dataList.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataList.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 1 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// scell.setEncoding(HSSFCell.ENCODING_UTF_16);

				// 定义单元格为字符串类型
				// scell.setCellType(Cell.CELL_TYPE_STRING);
				scell.setCellType(Cell.CELL_TYPE_NUMERIC);
				// scell.setCellValue(d+1); // 序号
				// 创建列
				for (int c = 0; c < fields.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fields[c]) != null
							&& dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fields[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数

							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点

							}

							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fields[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());

						} else {

							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c])
									.toString());
						}
						/*
						 * if (dataMap.get(fields[c]).toString().contains("-")
						 * && d != dataList.size() - 1 && isNum ) {
						 * contentCell.setCellStyle(cellStyleRED); } else if
						 * (dataMap.get(fields[c]).toString() .equals("0")) {
						 * contentCell.setCellStyle(cellStyleRED); }else if
						 * (dataMap.get(fields[c]).toString() .equals("0%")) {
						 * contentCell.setCellStyle(cellStyleRED); }
						 */
						if (/* d!=0 && */d != dataList.size() - 1) {
							// HashMap<String, Object> dataMapLast =
							// dataList.get(d-1);
							/*
							 * if(dataMapLast.get(fields[c]) != null &&
							 * dataMapLast.get(fields[c]).toString().length() >
							 * 0){
							 */
							if (dataMap.get(fields[c]).toString().contains("%")
							/*
							 * &&
							 * dataMapLast.get(fields[c]).toString().contains(
							 * "%")
							 */
							) {
								// String a =
								// dataMapLast.get(fields[c]).toString();
								String b = dataMap.get(fields[c]).toString(); // 去掉%
																				// String
								// String tempA = a.substring(0,
								// a.lastIndexOf("%"));
								String tempB = b.substring(0,
										b.lastIndexOf("%")); // 精确表示 Integer
								// Integer dataA = Integer.parseInt(tempA);
								BigDecimal dataB = new BigDecimal(tempB); // 大于为1，相同为0，小于为-1
																			// if
								if (dataB.compareTo(BigDecimal.valueOf(100)) == -1

										&& (dataB.compareTo(BigDecimal
												.valueOf(80)) == 1 || dataB
												.compareTo(BigDecimal
														.valueOf(80)) == 0)
								/*
								 * .compareTo(dataA) == 0
								 */) {
									contentCell.setCellStyle(cellStyleYellow);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(100)) == 1
										|| dataB.compareTo(BigDecimal
												.valueOf(100)) == 1
								/*
								 * dataB >= 100 .compareTo(dataA) == 1
								 */) {
									contentCell.setCellStyle(cellStyleGreen);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(80)) == -1/*
															 * .compareTo(dataA)
															 * == -1
															 */) {
									contentCell.setCellStyle(cellStyleRED);
								}

								// }
							}

						}

					} else {
						contentCell.setCellValue("");
					}

					if (d == dataList.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);

					}

				}
			}

			for (int d = 0; d < dataListTwo.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataListTwo.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + rows_max
						+ dataList.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// scell.setEncoding(HSSFCell.ENCODING_UTF_16);

				// 定义单元格为字符串类型
				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				// 创建列
				for (int c = 0; c < fieldsTwo.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsTwo[c]) != null
							&& dataMap.get(fieldsTwo[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTwo[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTwo[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTwo[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsTwo[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsTwo[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsTwo[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsTwo[c])
									.toString());
						}

						if (/* d!=0 && */d != dataListTwo.size() - 1) {

							if (dataMap.get(fieldsTwo[c]).toString()
									.contains("%")) {

								String b = dataMap.get(fieldsTwo[c]).toString(); // 去掉%
																					// String
								// String tempA = a.substring(0,
								// a.lastIndexOf("%"));
								String tempB = b.substring(0,
										b.lastIndexOf("%")); // 精确表示 Integer
								// Integer dataA = Integer.parseInt(tempA);
								BigDecimal dataB = new BigDecimal(tempB); // 大于为1，相同为0，小于为-1
																			// if
								if (dataB.compareTo(BigDecimal.valueOf(100)) == -1

										&& (dataB.compareTo(BigDecimal
												.valueOf(80)) == 1 || dataB
												.compareTo(BigDecimal
														.valueOf(80)) == 0)
								/*
								 * .compareTo(dataA) == 0
								 */) {
									contentCell.setCellStyle(cellStyleYellow);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(100)) == 1
										|| dataB.compareTo(BigDecimal
												.valueOf(100)) == 1
								/*
								 * dataB >= 100 .compareTo(dataA) == 1
								 */) {
									contentCell.setCellStyle(cellStyleGreen);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(80)) == -1/*
															 * .compareTo(dataA)
															 * == -1
															 */) {
									contentCell.setCellStyle(cellStyleRED);
								}

								// }
							}

						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListTwo.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}

				}
			}

			for (int d = 0; d < dataListThree.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataListThree.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + rows_max
						+ dataList.size() + 10 + dataListTwo.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// scell.setEncoding(HSSFCell.ENCODING_UTF_16);

				// 定义单元格为字符串类型
				scell.setCellType(Cell.CELL_TYPE_NUMERIC);
				// scell.setCellValue(d+1); // 序号
				// 创建列
				for (int c = 0; c < fieldsThree.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsThree[c]) != null
							&& dataMap.get(fieldsThree[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsThree[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsThree[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsThree[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsThree[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsThree[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsThree[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsThree[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsThree[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap
									.get(fieldsThree[c]).toString());
						}
						/*
						 * if
						 * (dataMap.get(fieldsThree[c]).toString().contains("-")
						 * && d != dataListThree.size() - 1 && isNum ) {
						 * contentCell.setCellStyle(cellStyleRED); } else if
						 * (dataMap.get(fieldsThree[c]).toString() .equals("0"))
						 * { contentCell.setCellStyle(cellStyleRED); }else if
						 * (dataMap.get(fieldsThree[c]).toString()
						 * .equals("0%")) {
						 * contentCell.setCellStyle(cellStyleRED); }
						 */
						if (d != dataListThree.size() - 1) {
							// HashMap<String, Object> dataMapLast =
							// dataListThree.get(d-1);
							/*
							 * if(dataMapLast.get(fieldsThree[c])!=null &&
							 * dataMapLast
							 * .get(fieldsThree[c]).toString().length() > 0){
							 */
							if (dataMap.get(fieldsThree[c]).toString()
									.contains("%")
							/*
							 * &&
							 * dataMapLast.get(fieldsThree[c]).toString().contains
							 * ("%")
							 */
							) {
								// String a =
								// dataMapLast.get(fieldsThree[c]).toString();
								String b = dataMap.get(fieldsThree[c])
										.toString(); // 去掉% String
								// String tempA = a.substring(0,
								// a.lastIndexOf("%"));
								String tempB = b.substring(0,
										b.lastIndexOf("%")); // 精确表示 Integer
								// Integer dataA = Integer.parseInt(tempA);
								// Integer dataA = Integer.parseInt(tempA);
								BigDecimal dataB = new BigDecimal(tempB); // 大于为1，相同为0，小于为-1
																			// if
								if (dataB.compareTo(BigDecimal.valueOf(100)) == -1

										&& (dataB.compareTo(BigDecimal
												.valueOf(80)) == 1 || dataB
												.compareTo(BigDecimal
														.valueOf(80)) == 0)
								/*
								 * .compareTo(dataA) == 0
								 */) {
									contentCell.setCellStyle(cellStyleYellow);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(100)) == 1
										|| dataB.compareTo(BigDecimal
												.valueOf(100)) == 1
								/*
								 * dataB >= 100 .compareTo(dataA) == 1
								 */) {
									contentCell.setCellStyle(cellStyleGreen);
								} else if (dataB.compareTo(BigDecimal
										.valueOf(80)) == -1/*
															 * .compareTo(dataA)
															 * == -1
															 */) {
									contentCell.setCellStyle(cellStyleRED);
								}

								// }
							}

						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListThree.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}

				}
			}

			for (int d = 0; d < dataListFour.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataListFour.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + rows_max
						+ dataList.size() + 10 + dataListTwo.size() + 10
						+ dataListThree.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// scell.setEncoding(HSSFCell.ENCODING_UTF_16);

				// 定义单元格为字符串类型
				scell.setCellType(Cell.CELL_TYPE_NUMERIC);
				// scell.setCellValue(d+1); // 序号
				// 创建列
				for (int c = 0; c < fieldsFour.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsFour[c]) != null
							&& dataMap.get(fieldsFour[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFour[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFour[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFour[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsFour[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsFour[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsFour[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFour[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFour[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsFour[c])
									.toString());
						}
						if (dataMap.get(fieldsFour[c]).toString().contains("-")
								&& d != dataListFour.size() - 1 && isNum) {
							contentCell.setCellStyle(cellStyleRED);
						} else if (dataMap.get(fieldsFour[c]).toString()
								.equals("0")) {
							contentCell.setCellStyle(cellStyleRED);
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListFour.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 2016 YTD sellout
	 */
	public void quaYTDsellout(SXSSFWorkbook wb,
			LinkedList<HashMap<String, Object>> dateList, String q,
			String partyName, String beginDateOne, String endDateOne) {

		String[] toYear = beginDateOne.split("-");
		int laYear = Integer.parseInt(toYear[0].toString()) - 1;
		String laDays = DateUtil.getLastDayOfMonth(laYear,
				Integer.parseInt(toYear[1]));
		String[] laDay = laDays.split("-");
		int lastYear = Integer.parseInt(toYear[0]) - 1;
		String [] laMonth=endDateOne.split("-");
		// 表头数据
		String[] headers = { "TCL MONTHLY SELLOUT TREND_Year",
				"TCL MONTHLY SELLOUT TREND_Month"

		};

		if (q.endsWith("Q1")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_Jan");
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_Feb");
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_Mar");

		}

		if (q.endsWith("Q2")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_Apr");
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_May");
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_June");

		}
		if (q.endsWith("Q3")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_July");
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_August");
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_September");
		}
		if (q.endsWith("Q4")) {
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_October");
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_November");
			headers = insert(headers, "TCL MONTHLY SELLOUT TREND_December");
		}

		headers = insert(headers, "TCL MONTHLY SELLOUT TREND_TOTAL");

		// 按照对应格式将表头传入
		ArrayList columns = new ArrayList();
		for (int i = 0; i < headers.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headers[i]);
			columnMap.put("field", headers[i]);
			columns.add(columnMap);
		}

		String[] header = new String[columns.size()];
		String[] fields = new String[columns.size()];
		for (int i = 0, l = columns.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columns.get(i);
			header[i] = columnMap.get("header").toString();
			fields[i] = columnMap.get("field").toString();

		}

		// 表头数据
		String[] headersTwo = {
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_Category",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_MODELS"

		};

		if (q.endsWith("Q1")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_Jan");
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_Feb");
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_Mar");
		}

		if (q.endsWith("Q2")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_Apr");
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_May");
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_June");

		}
		if (q.endsWith("Q3")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_July");
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_August");
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_September");
		}
		if (q.endsWith("Q4")) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_October");
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_November");
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
					+ toYear[0] + "_December");
		}

		headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-"
				+ toYear[0] + "_TOTAL");

		// 按照对应格式将表头传入
		ArrayList columnsTwo = new ArrayList();
		for (int i = 0; i < headersTwo.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersTwo[i]);
			columnMap.put("field", headersTwo[i]);
			columnsTwo.add(columnMap);
		}

		String[] headerTwo = new String[columnsTwo.size()];
		String[] fieldsTwo = new String[columnsTwo.size()];
		for (int i = 0, l = columnsTwo.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsTwo.get(i);
			headerTwo[i] = columnMap.get("header").toString();
			fieldsTwo[i] = columnMap.get("field").toString();

		}

		String searchStr = null;
		String conditions = "";
		String center = "";
		String country = "";
		String region = "";
		String office = "";

		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if (!WebPageUtil.isHAdmin()) {
			if (request.getParameter("center") != null
					&& !request.getParameter("center").equals("")
					|| request.getParameter("country") != null
					&& !request.getParameter("country").equals("")
					|| request.getParameter("region") != null
					&& !request.getParameter("region").equals("")
					|| request.getParameter("office") != null
					&& !request.getParameter("office").equals("")) {

				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")) {
					center = request.getParameter("center");
					conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
							+ center + "')   ";
				}

				if (request.getParameter("country") != null
						&& !request.getParameter("country").equals("")) {
					country = request.getParameter("country");
					conditions = "  pa.country_id= " + country + "  ";
				}
				if (request.getParameter("region") != null
						&& !request.getParameter("region").equals("")) {
					region = request.getParameter("region");
					conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
							+ region
							+ "'  OR PARTY_ID='"+region+"'))  ";
				}
				if (request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {
					office = request.getParameter("office");
					conditions = "    pa.party_id IN ('" + office + "')  ";
				}

			} else {
				if (null != userPartyIds && !"".equals(userPartyIds)) {
					conditions = "  pa.party_id in (" + userPartyIds + ")  ";
				} else {
					conditions = "  1=2  ";
				}
			}

		} else {
			conditions = " 1=1 ";

		}
		
		if (request.getParameter("level") != null
				&& !request.getParameter("level").equals("")) {
			conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
		}
		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
		LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapOne = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> rowMapOne = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> rowMapO = new LinkedHashMap<String, Object>();
		String lastDay = DateUtil.getLastDayOfMonth(
				Integer.parseInt(toYear[0]), 1);
		String[] lastDays = lastDay.split("-");
		String spec = "";
		double one = 0.0;
		double oneData = 0.0;
		double oneAch = 0.0;
		List<LinkedHashMap<String, Object>> sizeDatasTwTotal = null;
		String startDay = "";
		String Month = "";
		try {
			HashMap<String, Object> data = new HashMap<String, Object>();

			List<LinkedHashMap<String, Object>> sizeDatas = null;

			List<LinkedHashMap<String, Object>> sizeLa = excelService
					.selectQtyTotalBySpecTypeByAc(
							spec,
							laYear + "-" + toYear[1] + "-" + toYear[2],
							DateUtil.getLastDayOfMonth(laYear,
									Integer.parseInt(laMonth[1])), searchStr,
							conditions,WebPageUtil.isHQRole());

			List<LinkedHashMap<String, Object>> sizeTo = excelService
					.selectQtyTotalBySpecTypeByAc(spec, beginDateOne, endDateOne,
							searchStr, conditions,WebPageUtil.isHQRole());

			if (sizeLa.size() > sizeTo.size()) {
				sizeDatas = sizeLa;
			} else if (sizeTo.size() == sizeLa.size()) {
				sizeDatas = sizeLa;
			} else if (sizeTo.size() > sizeLa.size()) {
				sizeDatas = sizeTo;
			}
			
			System.out.println("===========sizeLa============"+sizeLa.size());
			System.out.println("===========sizeTo=================="+sizeTo.size());
			for (int j = 1; j <= 4; j++) {
				if (q.equals("Q1")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(lastYear, 1);
						lastDays = lastDay.split("-");
						startDay = lastYear + "-01-01";
						Month = "Jan";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(lastYear, 2);
						lastDays = lastDay.split("-");
						startDay = lastYear + "-02-01";
						Month = "Feb";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(lastYear, 3);
						lastDays = lastDay.split("-");
						startDay = lastYear + "-03-01";
						Month = "Mar";
					} else if (j == 1) {
						lastDay = lastYear + "-" + endDateOne.split("-")[1]
								+ "-" + endDateOne.split("-")[2];
						lastDays = lastDay.split("-");
						startDay = lastYear + "-" + beginDateOne.split("-")[1]
								+ "-" + beginDateOne.split("-")[2];
						Month = "TOTAL";
					}
				} else if (q.equals("Q2")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(lastYear, 4);
						lastDays = lastDay.split("-");
						startDay = lastYear + "-04-01";
						Month = "Apr";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(lastYear, 5);
						lastDays = lastDay.split("-");
						startDay = lastYear + "-05-01";
						Month = "May";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(lastYear, 6);
						lastDays = lastDay.split("-");
						startDay = lastYear + "-06-01";
						Month = "June";
					} else if (j == 1) {
						lastDay = lastYear + "-" + endDateOne.split("-")[1]
								+ "-" + endDateOne.split("-")[2];
						lastDays = lastDay.split("-");
						startDay = lastYear + "-" + beginDateOne.split("-")[1]
								+ "-" + beginDateOne.split("-")[2];
						Month = "TOTAL";
					}
				} else if (q.equals("Q3")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(lastYear, 7);
						lastDays = lastDay.split("-");
						startDay = lastYear + "-07-01";
						Month = "July";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(lastYear, 8);
						lastDays = lastDay.split("-");
						startDay = lastYear + "-08-01";
						Month = "August";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(lastYear, 9);
						lastDays = lastDay.split("-");
						startDay = lastYear + "-09-01";
						Month = "September";
					} else if (j == 1) {
						lastDay = lastYear + "-" + endDateOne.split("-")[1]
								+ "-" + endDateOne.split("-")[2];
						lastDays = lastDay.split("-");
						startDay = lastYear + "-" + beginDateOne.split("-")[1]
								+ "-" + beginDateOne.split("-")[2];
						Month = "TOTAL";
					}
				} else if (q.equals("Q4")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(lastYear, 10);
						lastDays = lastDay.split("-");
						startDay = lastYear + "-10-01";
						Month = "October";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(lastYear, 11);
						lastDays = lastDay.split("-");
						startDay = lastYear + "-11-01";
						Month = "November";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(lastYear, 12);
						lastDays = lastDay.split("-");
						startDay = lastYear + "-12-01";
						Month = "December";
					} else if (j == 1) {
						lastDay = lastYear + "-" + endDateOne.split("-")[1]
								+ "-" + endDateOne.split("-")[2];
						lastDays = lastDay.split("-");
						startDay = lastYear + "-" + beginDateOne.split("-")[1]
								+ "-" + beginDateOne.split("-")[2];
						Month = "TOTAL";
					}
				}

				List<LinkedHashMap<String, Object>> sizeDatasOneData = excelService
						.selectQtyTotalBySpecTypeByAc(spec, startDay, lastDay,
								searchStr, conditions,WebPageUtil.isHQRole());

				for (int z = 0; z < sizeDatasOneData.size(); z++) {
					LinkedHashMap<String, Object> colMap = sizeDatasOneData
							.get(z);
					String key = lastYear + colMap.get("SPEC").toString();
					if (allDataMapOne.get(key) != null) {
						LinkedHashMap<String, Object> rowMap = allDataMapOne
								.get(key);

						BigDecimal bd = new BigDecimal(sizeDatasOneData.get(z)
								.get("quantity").toString());
						String am = bd.toPlainString();
						// 分成12个月份查，查一个放一个
						rowMap.put("TCL MONTHLY SELLOUT TREND_" + Month + "",
								am);

						rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
								+ lastYear);
						rowMap.put("TCL MONTHLY SELLOUT TREND_Month",
								sizeDatasOneData.get(z).get("SPEC").toString());

						allDataMapOne.put(key, rowMap);
					} else {
						LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
						rowList.addLast(colMap);

						LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();

						BigDecimal bd = new BigDecimal(sizeDatasOneData.get(z)
								.get("quantity").toString());
						String am = bd.toPlainString();
						// 分成12个月份查，查一个放一个
						rowMap.put("TCL MONTHLY SELLOUT TREND_" + Month + "",
								am);

						rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
								+ lastYear);
						rowMap.put("TCL MONTHLY SELLOUT TREND_Month",
								sizeDatasOneData.get(z).get("SPEC").toString());

						allDataMapOne.put(key, rowMap);

					}
				}
				sizeDatasTwTotal = excelService.selectSaleTotalBySizeByAc(startDay,
						lastDay, searchStr, conditions,WebPageUtil.isHQRole());
				if (sizeDatasTwTotal.size() == 1) {
					BigDecimal bd = new BigDecimal(sizeDatasTwTotal.get(0)
							.get("quantity").toString());
					String am = bd.toPlainString();

					rowMapO.put("TCL MONTHLY SELLOUT TREND_" + Month + "", am);
					rowMapO.put("TCL MONTHLY SELLOUT TREND_Month", "TOTAL");
				}

			}

			if (sizeLa.size() < sizeTo.size() && sizeLa.size() != 0) {
				for (int i = 0; i < sizeTo.size() - sizeLa.size(); i++) {
					LinkedHashMap<String, Object> rw = new LinkedHashMap<String, Object>();
					allDataMapOne.put("S" + i, rw);
				}
			}

			if (sizeLa.size() == 0) {
				for (int j = 0; j < sizeTo.size(); j++) {
					LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();

					rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
							+ lastYear);
					rowMap.put("TCL MONTHLY SELLOUT TREND_Month", sizeTo.get(j)
							.get("SPEC").toString());
					allDataMapOne.put(lastYear
							+ sizeTo.get(j).get("SPEC").toString(), rowMap);
				}
			}

			allDataMapOne.put("TotalTwo", rowMapO);
			LinkedHashMap<String, Object> rw = new LinkedHashMap<String, Object>();
			allDataMapOne.put("S", rw);
			for (int j = 1; j <= 4; j++) {

				if (q.equals("Q1")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 1);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-01-01";
						Month = "Jan";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 2);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-02-01";
						Month = "Feb";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 3);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-03-01";
						Month = "Mar";
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TOTAL";
					}
				} else if (q.equals("Q2")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 4);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-04-01";
						Month = "Apr";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 5);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-05-01";
						Month = "May";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 6);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-06-01";
						Month = "June";
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TOTAL";
					}
				} else if (q.equals("Q3")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 7);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-07-01";
						Month = "July";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 8);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-08-01";
						Month = "August";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 9);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-09-01";
						Month = "September";
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TOTAL";
					}
				} else if (q.equals("Q4")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 10);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-10-01";
						Month = "October";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 11);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-11-01";
						Month = "November";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 12);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-12-01";
						Month = "December";
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TOTAL";
					}
				}

				List<LinkedHashMap<String, Object>> sizeDatasOneData = excelService
						.selectQtyTotalBySpecTypeByAc(spec, startDay, lastDay,
								searchStr, conditions,WebPageUtil.isHQRole());

				for (int z = 0; z < sizeDatasOneData.size(); z++) {
					LinkedHashMap<String, Object> colMap = sizeDatasOneData
							.get(z);
					String key = toYear[0] + colMap.get("SPEC").toString();
					if (allDataMapOne.get(key) != null) {
						LinkedHashMap<String, Object> rowMap = allDataMapOne
								.get(key);

						BigDecimal bd = new BigDecimal(sizeDatasOneData.get(z)
								.get("quantity").toString());
						String am = bd.toPlainString();
						// 分成12个月份查，查一个放一个
						rowMap.put("TCL MONTHLY SELLOUT TREND_" + Month + "",
								am);

						rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
								+ toYear[0]);
						rowMap.put("TCL MONTHLY SELLOUT TREND_Month",
								sizeDatasOneData.get(z).get("SPEC").toString());

						allDataMapOne.put(key, rowMap);
					} else {
						LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
						rowList.addLast(colMap);

						LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();

						BigDecimal bd = new BigDecimal(sizeDatasOneData.get(z)
								.get("quantity").toString());
						String am = bd.toPlainString();
						// 分成12个月份查，查一个放一个
						rowMap.put("TCL MONTHLY SELLOUT TREND_" + Month + "",
								am);

						rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
								+ toYear[0]);
						rowMap.put("TCL MONTHLY SELLOUT TREND_Month",
								sizeDatasOneData.get(z).get("SPEC").toString());

						allDataMapOne.put(key, rowMap);

					}
				}
				sizeDatasTwTotal = excelService.selectSaleTotalBySizeByAc(startDay,
						lastDay, searchStr, conditions,WebPageUtil.isHQRole());
				if (sizeDatasTwTotal.size() == 1) {
					BigDecimal bd = new BigDecimal(sizeDatasTwTotal.get(0)
							.get("quantity").toString());
					String am = bd.toPlainString();

					rowMapOne
							.put("TCL MONTHLY SELLOUT TREND_" + Month + "", am);
					rowMapOne.put("TCL MONTHLY SELLOUT TREND_Month", "TOTAL");

				}

			}

			if (sizeTo.size() < sizeLa.size() && sizeTo.size() != 0) {
				for (int i = 0; i < sizeLa.size() - sizeTo.size(); i++) {
					LinkedHashMap<String, Object> r = new LinkedHashMap<String, Object>();
					allDataMapOne.put("I" + i, r);
				}
			}

			if (sizeTo.size() == 0) {
				for (int j = 0; j < sizeLa.size(); j++) {
					LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();

					rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
							+ toYear[0]);
					rowMap.put("TCL MONTHLY SELLOUT TREND_Month", sizeLa.get(j)
							.get("SPEC").toString());
					allDataMapOne.put(toYear[0]
							+ sizeLa.get(j).get("SPEC").toString(), rowMap);
				}
			}

			allDataMapOne.put("TotalOne", rowMapOne);
			Set<String> sizeSet = allDataMapOne.keySet();

			Iterator<String> sizeIter = sizeSet.iterator();
			while (sizeIter.hasNext()) {
				String key = sizeIter.next();
				LinkedHashMap<String, Object> rowMap = allDataMapOne.get(key);
				dataList.add(rowMap);

				Set<String> rowSet = rowMap.keySet();
				Iterator<String> rowIter = rowSet.iterator();
			}

			int stRow = 13;
			int enRow = 22;

			if (sizeDatas.size() == 1) {
				stRow = 7;
				enRow = 10;
			}  else if (sizeDatas.size() == 2) {
				stRow = 8;
				enRow = 12;
			}  else if (sizeDatas.size() == 3) {
				stRow = 9;
				enRow = 14;
			} else if (sizeDatas.size() == 4) {
				stRow = 10;
				enRow = 16;
			}else if (sizeDatas.size() == 5) {
				stRow = 11;
				enRow = 18;
			}else if (sizeDatas.size() == 6) {
				stRow = 12;
				enRow = 20;
			}else if (sizeDatas.size() == 7) {
				stRow = 13;
				enRow = 22;
			} else if (sizeDatas.size() == 8) {
				stRow = 14;
				enRow = 24;
			}  
			HashMap<String, Object> dataOne = new HashMap<String, Object>();
			dataOne.put("TCL MONTHLY SELLOUT TREND_Month", "Growth vs. "
					+ lastYear);
			dataOne.put("TCL MONTHLY SELLOUT TREND_Jan", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_Feb", "TEXT((D" + enRow
					+ "-D" + stRow + ")/D" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_Mar", "TEXT((E" + enRow
					+ "-E" + stRow + ")/E" + enRow + ",\"0.00%\")");

			dataOne.put("TCL MONTHLY SELLOUT TREND_Apr", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_May", "TEXT((D" + enRow
					+ "-D" + stRow + ")/D" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_June", "TEXT((E" + enRow
					+ "-E" + stRow + ")/E" + enRow + ",\"0.00%\")");

			dataOne.put("TCL MONTHLY SELLOUT TREND_July", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_August", "TEXT((D" + enRow
					+ "-D" + stRow + ")/D" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_September", "TEXT((E"
					+ enRow + "-E" + stRow + ")/E" + enRow + ",\"0.00%\")");

			dataOne.put("TCL MONTHLY SELLOUT TREND_October", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_November", "TEXT((D" + enRow
					+ "-D" + stRow + ")/D" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_December", "TEXT((E" + enRow
					+ "-E" + stRow + ")/E" + enRow + ",\"0.00%\")");

			dataOne.put("TCL MONTHLY SELLOUT TREND_TOTAL", "TEXT((F" + enRow
					+ "-F" + stRow + ")/F" + enRow + ",\"0.00%\")");
			dataList.add(dataOne);

			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapTwo = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
			LinkedHashMap<String, Object> rowMapTwo = new LinkedHashMap<String, Object>();

			LinkedList<LinkedHashMap<String, Object>> dataListTwo = new LinkedList<LinkedHashMap<String, Object>>();

			long start = System.currentTimeMillis();
			for (int j = 1; j <= 4; j++) {
				if (q.equals("Q1")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 1);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-01-01";
						Month = "Jan";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 2);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-02-01";
						Month = "Feb";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 3);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-03-01";
						Month = "Mar";
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TOTAL";
					}
				} else if (q.equals("Q2")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 4);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-04-01";
						Month = "Apr";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 5);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-05-01";
						Month = "May";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 6);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-06-01";
						Month = "June";
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TOTAL";
					}
				} else if (q.equals("Q3")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 7);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-07-01";
						Month = "July";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 8);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-08-01";
						Month = "August";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 9);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-09-01";
						Month = "September";
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TOTAL";
					}
				} else if (q.equals("Q4")) {
					if (j == 4) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 10);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-10-01";
						Month = "October";
					} else if (j == 2) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 11);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-11-01";
						Month = "November";
					} else if (j == 3) {
						lastDay = DateUtil.getLastDayOfMonth(
								Integer.parseInt(toYear[0]), 12);
						lastDays = lastDay.split("-");
						startDay = toYear[0] + "-12-01";
						Month = "December";
					} else if (j == 1) {
						lastDay = endDateOne;
						lastDays = lastDay.split("-");
						startDay = beginDateOne;
						Month = "TOTAL";
					}
				}

				List<LinkedHashMap<String, Object>> selectYearDataBySpecOne = excelService
						.selectQtyTotalBySpecYearByAc(spec, startDay, lastDay,
								searchStr, conditions,WebPageUtil.isHQRole());

				for (int z = 0; z < selectYearDataBySpecOne.size(); z++) {
					LinkedHashMap<String, Object> colMap = selectYearDataBySpecOne
							.get(z);
					String key = colMap.get("model").toString();
					if (allDataMapTwo.get(key) != null) {
						LinkedHashMap<String, Object> rowMap = allDataMapTwo
								.get(key);

						rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
								+ "_Category", selectYearDataBySpecOne.get(z)
								.get("SPEC"));

						rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
								+ "_MODELS", selectYearDataBySpecOne.get(z)
								.get("model"));

						BigDecimal bd = new BigDecimal(selectYearDataBySpecOne
								.get(z).get("quantity").toString());

						String am = bd.toPlainString();
						rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
								+ "_" + Month + "", am);

						allDataMapTwo.put(key, rowMap);
					} else {
						LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
						rowList.addLast(colMap);

						LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();
						rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
								+ "_Category", selectYearDataBySpecOne.get(z)
								.get("SPEC"));

						rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
								+ "_MODELS", selectYearDataBySpecOne.get(z)
								.get("model"));

						BigDecimal bd = new BigDecimal(selectYearDataBySpecOne
								.get(z).get("quantity").toString());
						String am = bd.toPlainString();
						rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
								+ "_" + Month + "", am);
						allDataMapTwo.put(key, rowMap);

					}
				}
				List<HashMap<String, Object>> sizeDatasOneTotal = excelService
						.selectQtyTotalBySpecTotalYearByAc(startDay, lastDay,
								searchStr, conditions,WebPageUtil.isHQRole());
				for (int s = 0; s < sizeDatasOneTotal.size(); s++) {
					HashMap<String, Object> colMap = sizeDatasOneTotal.get(s);
					rowMapTwo.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
							+ "_Category", "GRAND TOTAL");

					BigDecimal bd = new BigDecimal(sizeDatasOneTotal.get(0)
							.get("quantity").toString());
					String am = bd.toPlainString();

					rowMapTwo.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
							+ "_" + Month + "", am);

				}

			}
			allDataMapTwo.put("TotalTwo", rowMapTwo);

			Set<String> sizeSetTen = allDataMapTwo.keySet();

			Iterator<String> sizeIterTen = sizeSetTen.iterator();
			LinkedHashMap<String, Object> totalMapTen = new LinkedHashMap<String, Object>();
			while (sizeIterTen.hasNext()) {
				String key = sizeIterTen.next();
				LinkedHashMap<String, Object> rowMap = allDataMapTwo.get(key);
				dataListTwo.add(rowMap);

				Set<String> rowSet = rowMap.keySet();
				Iterator<String> rowIter = rowSet.iterator();
			}

			// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
			Sheet sheet = wb.createSheet(toYear[0] + " YTD sellout");
			sheet.setZoom(3, 4);
			// 创建字体
			Font fontinfo = wb.createFont();
			fontinfo.setFontHeightInPoints((short) 11); // 字体大小
			fontinfo.setFontName("Trebuchet MS");
			Font fonthead = wb.createFont();
			fonthead.setFontHeightInPoints((short) 12);
			fonthead.setFontName("Trebuchet MS");

			// colSplit, rowSplit,leftmostColumn, topRow,
			sheet.createFreezePane(3, 5, 3, 5);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat
					.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
														// hh:mm:ss

			CellStyle cellStylehead = wb.createCellStyle();// 表头样式
			cellStylehead.setFont(fonthead);
			cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			CellStyle cellStyleWHITE = wb.createCellStyle();// 表头样式
			cellStyleWHITE.setFont(fonthead);
			cellStyleWHITE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleWHITE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleWHITE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyleWHITE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleWHITE.setWrapText(true);

			CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fonthead);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
			cellStyleRED.setFont(fonthead);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);
			CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fonthead);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleYellow.setWrapText(true);

			CellStyle cellStylePERCENT = wb.createCellStyle();// 表头样式
			cellStylePERCENT.setFont(fonthead);
			cellStylePERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePERCENT.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePERCENT.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT
					.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStylePERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePERCENT.setWrapText(true);

			CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			Row row = sheet.createRow((short) 0);
			// 创建这一行的一列，即创建单元格(CELL)
			Cell cell = row.createCell((short) 0);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("TCL " + partyName);// 标题

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			row = sheet.createRow((short) 1);
			// 创建这一行的一列，即创建单元格(CELL)
			cell = row.createCell((short) 1);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("CUMULATIVE SELLOUT");// 标题

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			row = sheet.createRow((short) 2);
			// 创建这一行的一列，即创建单元格(CELL)
			cell = row.createCell((short) 1);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("YTD- " + toYear[0]);// 标题

			CellStyle contextstyle = wb.createCellStyle();
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			contextstyle.setFont(fontinfo);
			// 从17行开始放另一个表格的表头

			int rows_max = 0; // 最大的一个项有几个子项

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map map = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 3);
				for (int i = 0; i < header.length; i++) {
					cell.setCellStyle(cellStyleWHITE);
					String headerTemp = header[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(3,
								rows_max + 2, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (map.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (map.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < header.length; j++) {
							if (header[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 3,
								k + 3, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 3, k
									+ 3 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerTwo.length; i++) {
				String h = headerTwo[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapTwo = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + dataList.size() + 15);
				for (int i = 0; i < headerTwo.length; i++) {
					cell.setCellStyle(cellStyleWHITE);
					String headerTemp = headerTwo[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(dataList
								.size() + 15, rows_max + dataList.size() + 14,
								(num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapTwo.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapTwo.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerTwo.length; j++) {
							if (headerTwo[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k
								+ dataList.size() + 15, k + dataList.size()
								+ 15, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k
									+ dataList.size() + 15, k + dataList.size()
									+ 15 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int d = 0; d < dataList.size(); d++) {
				DataFormat df = wb.createDataFormat();

				HashMap<String, Object> dataMap = dataList.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 2 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fields.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fields[c]) != null
							&& dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fields[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fields[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());

						}

						else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c])
									.toString());
						}
						if (sizeDatas.size() == 7) {
							if (d == 7 || d == 16) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 8) {
							if (d == 8 || d == 18) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 6) {
							if (d == 6 || d == 14) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 5) {
							if (d == 5 || d == 12) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 4) {
							if (d == 4 || d == 10) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 3) {
							if (d == 3 || d == 8) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 2) {
							if (d == 2 || d == 6) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 1) {
							if (d == 1 || d == 4) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataList.size() - 1) {
						cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleGreen);

					}

				}
				if (sizeDatas.size() == 7) {
					sheet.addMergedRegion(new CellRangeAddress(5, 12, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(14, 21, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(13, 13, 0, 15));

				} else if (sizeDatas.size() == 8) {
					sheet.addMergedRegion(new CellRangeAddress(5, 13, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(15, 22, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(14, 14, 0, 15));
				} else if (sizeDatas.size() == 6) {
					sheet.addMergedRegion(new CellRangeAddress(5, 11, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(13, 20, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(12, 12, 0, 15));
				} else if (sizeDatas.size() == 5) {
					sheet.addMergedRegion(new CellRangeAddress(5, 10, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(12, 19, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(11, 11, 0, 15));
				} else if (sizeDatas.size() == 4) {
					sheet.addMergedRegion(new CellRangeAddress(5, 9, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(11, 18, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(10, 10, 0, 15));
				} else if (sizeDatas.size() == 3) {
					sheet.addMergedRegion(new CellRangeAddress(5, 8, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(10, 17, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(9, 9, 0, 15));
				} else if (sizeDatas.size() == 2) {
					sheet.addMergedRegion(new CellRangeAddress(5, 7, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(9, 16, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 15));
				} else if (sizeDatas.size() == 1) {
					sheet.addMergedRegion(new CellRangeAddress(5, 6, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(8, 15, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 15));
				}

			}

			for (int d = 0; d < dataListTwo.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataListTwo.get(d);

				// 创建一行
				Row datarow = sheet.createRow(d + dataList.size() + 14
						+ rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，
				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsTwo.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsTwo[c]) != null
							&& dataMap.get(fieldsTwo[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTwo[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTwo[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTwo[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsTwo[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsTwo[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsTwo[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsTwo[c])
									.toString());
						}

						if (d != dataListTwo.size() - 1) {
							HashMap<String, Object> dataMapTwo = dataListTwo
									.get(d + 1);
							if (dataMap.get(fieldsTwo[c]).equals(
									dataMapTwo.get(fieldsTwo[c]))) {
								sheet.addMergedRegion(new CellRangeAddress(d
										+ dataList.size() + 14 + rows_max + 1,
										d + dataList.size() + 14 + rows_max + 1
												+ 1, 0, 0));

							}
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListTwo.size() - 1) {
						cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleGreen);
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void yearComparative(SXSSFWorkbook wb,
			LinkedList<HashMap<String, Object>> dateList,

			String partyName, String beginDateOne, String endDateOne,String tBeginDate,String tEndDate)
			throws ParseException {
		String beginDate = beginDateOne;
		String endDate = endDateOne;
		String[] days = beginDateOne.split("-");

		String searchStr = null;
		String conditions = "";
		String center = "";
		String country = "";
		String region = "";
		String office = "";

		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if (!WebPageUtil.isHAdmin()) {
			if (request.getParameter("center") != null
					&& !request.getParameter("center").equals("")
					|| request.getParameter("country") != null
					&& !request.getParameter("country").equals("")
					|| request.getParameter("region") != null
					&& !request.getParameter("region").equals("")
					|| request.getParameter("office") != null
					&& !request.getParameter("office").equals("")) {

				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")) {
					center = request.getParameter("center");
					conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
							+ center + "')   ";
				}

				if (request.getParameter("country") != null
						&& !request.getParameter("country").equals("")) {
					country = request.getParameter("country");
					conditions = "  pa.country_id= " + country + "  ";
				}
				if (request.getParameter("region") != null
						&& !request.getParameter("region").equals("")) {
					region = request.getParameter("region");
					conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
							+ region
							+ "'  OR PARTY_ID='"+region+"'))  ";
				}
				if (request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {
					office = request.getParameter("office");
					conditions = "    pa.party_id IN ('" + office + "')  ";
				}

			} else {
				if (null != userPartyIds && !"".equals(userPartyIds)) {
					conditions = "  pa.party_id in (" + userPartyIds + ")  ";
				} else {
					conditions = "  1=2  ";
				}
			}

		} else {
			conditions = " 1=1 ";

		}
		
		if (request.getParameter("level") != null
				&& !request.getParameter("level").equals("")) {
			conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
		}
		// 表头数据
		String[] headers = {};
		// 用于放置表格数据

		DateUtil.year = Integer.parseInt(days[0]) - 1;
		DateUtil.month = Integer.parseInt(days[1]);
		headers = insert(headers, "YR-" + DateUtil.year);

		for (int i = 0; i < dateList.size(); i++) {
			beginDate = dateList.get(i).get("beginDate").toString();
			endDate = dateList.get(i).get("endDate").toString();
			days = dateList.get(i).get("beginDate").toString().split("-");

			Date timec = format.parse(beginDate);
			Calendar rightNowc = Calendar.getInstance();
			rightNowc.setTime(timec);
			Date daysOne = format.parse(format.format(rightNowc.getTime()));

			headers = insert(headers, sdf.format(daysOne));

		}
		headers = insert(headers, "Total");
		headers = insert(headers, "Amount");

		// 按照对应格式将表头传入
		ArrayList columns = new ArrayList();
		for (int i = 0; i < headers.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headers[i]);
			columnMap.put("field", headers[i]);
			columns.add(columnMap);

		}

		String[] header = new String[columns.size()];
		String[] fields = new String[columns.size()];
		for (int i = 0, l = columns.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columns.get(i);
			header[i] = columnMap.get("header").toString();
			fields[i] = columnMap.get("field").toString();

		}

		String[] headersTwo = { "REGIONAL sell-out performances_RANK",
				"REGIONAL sell-out performances_REGIONAL HEAD",
				"REGIONAL sell-out performances_AREA" };
		String[] year = beginDateOne.split("-");
		String[] toYear = endDateOne.split("-");
		int laYear = Integer.parseInt(toYear[0].toString()) - 1;
		headersTwo = insert(headersTwo, "FPS_Yr-" + laYear);
		headersTwo = insert(headersTwo, "FPS_Yr-" + toYear[0]);
		String laDays = DateUtil.getLastDayOfMonth(laYear,
				Integer.parseInt(toYear[1]));
		String[] laDay = laDays.split("-");

		headersTwo = insert(headersTwo, "Total Flat Panel TV Quantity_"
				+ laYear);
		headersTwo = insert(headersTwo, "Total Flat Panel TV Quantity_"
				+ toYear[0]);
		headersTwo = insert(headersTwo,
				"Total Flat Panel TV Quantity_SO Growth/day");
		headersTwo = insert(headersTwo, "Total Amount_" + laYear);
		headersTwo = insert(headersTwo, "Total Amount_" + toYear[0]);
		headersTwo = insert(headersTwo, "Total Amount_SO Growth/day");
		headersTwo = insert(headersTwo, "Average sellout per fps_" + laYear
				+ " Ave.qty/fps");
		headersTwo = insert(headersTwo, "Average sellout per fps_" + toYear[0]
				+ " Ave.qty/fps");
		headersTwo = insert(headersTwo, "Average sellout per fps_" + laYear
				+ " Ave.amt/fps");
		headersTwo = insert(headersTwo, "Average sellout per fps_" + toYear[0]
				+ " Ave.amt/fps");

		// 按照对应格式将表头传入
		ArrayList columnsTwo = new ArrayList();
		for (int i = 0; i < headersTwo.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersTwo[i]);
			columnMap.put("field", headersTwo[i]);
			columnsTwo.add(columnMap);

		}

		String[] headerTwo = new String[columnsTwo.size()];
		String[] fieldsTwo = new String[columnsTwo.size()];
		for (int i = 0, l = columnsTwo.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsTwo.get(i);
			headerTwo[i] = columnMap.get("header").toString();
			fieldsTwo[i] = columnMap.get("field").toString();

		}

		String[] headersThree = { "SALESMAN sell-out performances_RANK",
				"SALESMAN sell-out performances_SALESMAN",
				"SALESMAN sell-out performances_REGION" };

		headersThree = insert(headersThree, "FPS_Yr-" + laYear);
		headersThree = insert(headersThree, "FPS_Yr-" + toYear[0]);
		headersThree = insert(headersThree, "Total Flat Panel TV Quantity_"
				+ laYear);
		headersThree = insert(headersThree, "Total Flat Panel TV Quantity_"
				+ toYear[0]);
		headersThree = insert(headersThree,
				"Total Flat Panel TV Quantity_SO Growth/day");
		headersThree = insert(headersThree, "Total Amount_" + laYear);
		headersThree = insert(headersThree, "Total Amount_" + toYear[0]);
		headersThree = insert(headersThree, "Total Amount_SO Growth/day");
		headersThree = insert(headersThree, "Average sellout per fps_" + laYear
				+ " Ave.qty/fps");
		headersThree = insert(headersThree, "Average sellout per fps_"
				+ toYear[0] + " Ave.qty/fps");
		headersThree = insert(headersThree, "Average sellout per fps_" + laYear
				+ " Ave.amt/fps");
		headersThree = insert(headersThree, "Average sellout per fps_"
				+ toYear[0] + " Ave.amt/fps");

		// 按照对应格式将表头传入
		ArrayList columnsThree = new ArrayList();
		for (int i = 0; i < headersThree.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersThree[i]);
			columnMap.put("field", headersThree[i]);
			columnsThree.add(columnMap);

		}

		String[] headerThree = new String[columnsThree.size()];
		String[] fieldsThree = new String[columnsThree.size()];
		for (int i = 0, l = columnsThree.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsThree.get(i);
			headerThree[i] = columnMap.get("header").toString();
			fieldsThree[i] = columnMap.get("field").toString();

		}

		String[] headersFour = { "ACFO sell-out performances_RANK",
				"ACFO sell-out performances_ACFO",
				"ACFO sell-out performances_REGION" };

		headersFour = insert(headersFour, "FPS_Yr-" + laYear);
		headersFour = insert(headersFour, "FPS_Yr-" + toYear[0]);
		headersFour = insert(headersFour, "Total Flat Panel TV Quantity_"
				+ laYear);
		headersFour = insert(headersFour, "Total Flat Panel TV Quantity_"
				+ toYear[0]);
		headersFour = insert(headersFour,
				"Total Flat Panel TV Quantity_SO Growth/day");
		headersFour = insert(headersFour, "Total Amount_" + laYear);
		headersFour = insert(headersFour, "Total Amount_" + toYear[0]);
		headersFour = insert(headersFour, "Total Amount_SO Growth/day");
		headersFour = insert(headersFour, "Average sellout per fps_" + laYear
				+ " Ave.qty/fps");
		headersFour = insert(headersFour, "Average sellout per fps_"
				+ toYear[0] + " Ave.qty/fps");
		headersFour = insert(headersFour, "Average sellout per fps_" + laYear
				+ " Ave.amt/fps");
		headersFour = insert(headersFour, "Average sellout per fps_"
				+ toYear[0] + " Ave.amt/fps");

		// 按照对应格式将表头传入
		ArrayList columnsFour = new ArrayList();
		for (int i = 0; i < headersFour.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersFour[i]);
			columnMap.put("field", headersFour[i]);
			columnsFour.add(columnMap);

		}

		String[] headerFour = new String[columnsFour.size()];
		String[] fieldsFour = new String[columnsFour.size()];
		for (int i = 0, l = columnsFour.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsFour.get(i);
			headerFour[i] = columnMap.get("header").toString();
			fieldsFour[i] = columnMap.get("field").toString();

		}

		String[] headersFive = { "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_Category"

		};

		headersFive = insert(headersFive, "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_Jan");
		headersFive = insert(headersFive, "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_Feb");
		headersFive = insert(headersFive, "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_Mar");

		headersFive = insert(headersFive, "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_Apr");
		headersFive = insert(headersFive, "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_May");
		headersFive = insert(headersFive, "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_June");
		headersFive = insert(headersFive, "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_July");
		headersFive = insert(headersFive, "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_August");
		headersFive = insert(headersFive, "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_September");
		headersFive = insert(headersFive, "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_October");
		headersFive = insert(headersFive, "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_November");
		headersFive = insert(headersFive, "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_December");
		headersFive = insert(headersFive, "YTD-" + toYear[0]
				+ "  Monthly sellout trend per size_TTL");

		// 按照对应格式将表头传入
		ArrayList columnsFive = new ArrayList();
		for (int i = 0; i < headersFive.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersFive[i]);
			columnMap.put("field", headersFive[i]);
			columnsFive.add(columnMap);

		}

		String[] headerFive = new String[columnsFive.size()];
		String[] fieldsFive = new String[columnsFive.size()];
		for (int i = 0, l = columnsFive.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsFive.get(i);
			headerFive[i] = columnMap.get("header").toString();
			fieldsFive[i] = columnMap.get("field").toString();

		}

		String[] headersSix = { "Market Share_Category"

		};

		headersSix = insert(headersSix, "Market Share_Jan");
		headersSix = insert(headersSix, "Market Share_Feb");
		headersSix = insert(headersSix, "Market Share_Mar");

		headersSix = insert(headersSix, "Market Share_Apr");
		headersSix = insert(headersSix, "Market Share_May");
		headersSix = insert(headersSix, "Market Share_June");
		headersSix = insert(headersSix, "Market Share_July");
		headersSix = insert(headersSix, "Market Share_August");
		headersSix = insert(headersSix, "Market Share_September");
		headersSix = insert(headersSix, "Market Share_October");
		headersSix = insert(headersSix, "Market Share_November");
		headersSix = insert(headersSix, "Market Share_December");
		headersSix = insert(headersSix, "Market Share_TTL");

		// 按照对应格式将表头传入
		ArrayList columnsSix = new ArrayList();
		for (int i = 0; i < headersSix.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersSix[i]);
			columnMap.put("field", headersSix[i]);
			columnsSix.add(columnMap);

		}

		String[] headerSix = new String[columnsSix.size()];
		String[] fieldsSix = new String[columnsSix.size()];
		for (int i = 0, l = columnsSix.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsSix.get(i);
			headerSix[i] = columnMap.get("header").toString();
			fieldsSix[i] = columnMap.get("field").toString();

		}

		String[] headersEight = { "Different catgory sell-out quantity_Category"

		};

		headersEight = insert(headersEight,
				"Different catgory sell-out quantity_Jan");
		headersEight = insert(headersEight,
				"Different catgory sell-out quantity_Feb");
		headersEight = insert(headersEight,
				"Different catgory sell-out quantity_Mar");

		headersEight = insert(headersEight,
				"Different catgory sell-out quantity_Apr");
		headersEight = insert(headersEight,
				"Different catgory sell-out quantity_May");
		headersEight = insert(headersEight,
				"Different catgory sell-out quantity_June");
		headersEight = insert(headersEight,
				"Different catgory sell-out quantity_July");
		headersEight = insert(headersEight,
				"Different catgory sell-out quantity_August");
		headersEight = insert(headersEight,
				"Different catgory sell-out quantity_September");
		headersEight = insert(headersEight,
				"Different catgory sell-out quantity_October");
		headersEight = insert(headersEight,
				"Different catgory sell-out quantity_November");
		headersEight = insert(headersEight,
				"Different catgory sell-out quantity_December");
		headersEight = insert(headersEight,
				"Different catgory sell-out quantity_TTL");

		// 按照对应格式将表头传入
		ArrayList columnsEight = new ArrayList();
		for (int i = 0; i < headersEight.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersEight[i]);
			columnMap.put("field", headersEight[i]);
			columnsEight.add(columnMap);

		}

		String[] headerEight = new String[columnsEight.size()];
		String[] fieldsEight = new String[columnsEight.size()];
		for (int i = 0, l = columnsEight.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsEight.get(i);
			headerEight[i] = columnMap.get("header").toString();
			fieldsEight[i] = columnMap.get("field").toString();

		}

		String[] headersNight = { "Growth rate_Category"

		};

		headersNight = insert(headersNight, "Growth rate_Jan");
		headersNight = insert(headersNight, "Growth rate_Feb");
		headersNight = insert(headersNight, "Growth rate_Mar");

		headersNight = insert(headersNight, "Growth rate_Apr");
		headersNight = insert(headersNight, "Growth rate_May");
		headersNight = insert(headersNight, "Growth rate_June");
		headersNight = insert(headersNight, "Growth rate_July");
		headersNight = insert(headersNight, "Growth rate_August");
		headersNight = insert(headersNight, "Growth rate_September");
		headersNight = insert(headersNight, "Growth rate_October");
		headersNight = insert(headersNight, "Growth rate_November");
		headersNight = insert(headersNight, "Growth rate_December");
		headersNight = insert(headersNight, "Growth rate_TTL");

		// 按照对应格式将表头传入
		ArrayList columnsNight = new ArrayList();
		for (int i = 0; i < headersNight.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersNight[i]);
			columnMap.put("field", headersNight[i]);
			columnsNight.add(columnMap);

		}

		String[] headerNight = new String[columnsNight.size()];
		String[] fieldsNight = new String[columnsNight.size()];
		for (int i = 0, l = columnsNight.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsNight.get(i);
			headerNight[i] = columnMap.get("header").toString();
			fieldsNight[i] = columnMap.get("field").toString();

		}

		// 表头数据
		String[] headersTen = { "DEALER", "Jan", "Feb", "Mar", "Apr", "May",
				"June", "July", "August", "September", "October", "November",
				"December", "Total", "Ave So per day"

		};

		// 按照对应格式将表头传入
		ArrayList columnsTen = new ArrayList();
		for (int i = 0; i < headersTen.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersTen[i]);
			columnMap.put("field", headersTen[i]);
			columnsTen.add(columnMap);

		}

		String[] headerTen = new String[columnsTen.size()];
		String[] fieldsTen = new String[columnsTen.size()];
		for (int i = 0, l = columnsTen.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsTen.get(i);
			headerTen[i] = columnMap.get("header").toString();
			fieldsTen[i] = columnMap.get("field").toString();

		}
		long star = System.currentTimeMillis();
		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();

		String keys = "";
		for (int r = 1; r <= 6; r++) {
			HashMap<String, Object> data = new HashMap<String, Object>();

			if (r == 1) {
				DateUtil.year = Integer.parseInt(days[0]) - 1;
				for (int i = 0; i < dateList.size(); i++) {
					beginDate = dateList.get(i).get("beginDate").toString();
					endDate = dateList.get(i).get("endDate").toString();
					String[] beginDays = beginDate.split("-");
					String[] endDays = endDate.split("-");
					Date timec = format.parse(beginDate);
					Calendar rightNowc = Calendar.getInstance();
					rightNowc.setTime(timec);
					Date be = format.parse(format.format(rightNowc.getTime()));

					keys = sdf.format(be);

					int lastYear = Integer.parseInt(beginDays[0]) - 1;

					beginDate = lastYear + "-" + beginDays[1] + "-"
							+ beginDays[2];
					endDate = lastYear + "-" + endDays[1] + "-" + endDays[2];

					data.put("YR-" + DateUtil.year, "TTL QTY");
					List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
							beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());

					data.put(keys, Long.parseLong(sumDatas.get(0).getSaleQty()));

				}

				String[] beginDays = beginDateOne.split("-");
				String[] endDays = endDateOne.split("-");
				int lastYear = Integer.parseInt(beginDays[0]) - 1;
				beginDate = lastYear + "-" + beginDays[1] + "-" + beginDays[2];
				endDate = lastYear + "-" + endDays[1] + "-" + endDays[2];

				data.put("YR-" + DateUtil.year, "TTL QTY");
				List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
						beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());

				data.put("Total", Long.parseLong(sumDatas.get(0).getSaleQty()));
				data.put("Amount",
						(long) Double.parseDouble(sumDatas.get(0).getSaleSum()));

				System.out.println("==============R=1==="
						+ (System.currentTimeMillis() - star));
			}

			else if (r == 2) {
				star = System.currentTimeMillis();
				int day = 0;
				data.put("YR-" + DateUtil.year, "Ave/day");
				DateUtil.year = Integer.parseInt(days[0]) - 1;
				DateUtil.month = Integer.parseInt(days[1]);

				String lastDay = DateUtil.getLastDayOfMonth(DateUtil.year,
						DateUtil.month);
				String[] lastDays = lastDay.split("-");

				for (int j = 0; j < dateList.size(); j++) {
					beginDate = dateList.get(j).get("beginDate").toString();
					endDate = dateList.get(j).get("endDate").toString();
					String[] beginDays = beginDate.split("-");
					String[] endDays = endDate.split("-");
					Date timec = format.parse(beginDate);
					Calendar rightNowc = Calendar.getInstance();
					rightNowc.setTime(timec);
					Date be = format.parse(format.format(rightNowc.getTime()));

					keys = sdf.format(be);

					int lastYear = Integer.parseInt(beginDays[0]) - 1;

					beginDate = lastYear + "-" + beginDays[1] + "-"
							+ beginDays[2];
					endDate = lastYear + "-" + endDays[1] + "-" + endDays[2];

					List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
							beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());
					day = Integer.parseInt(endDays[2])
							- Integer.parseInt(beginDays[2]) + 1;
					

					
					if (Long.parseLong(sumDatas.get(0).getSaleQty()) != 0
							&& day != 0) {
						data.put(keys,
								Math.round(Double.parseDouble(sumDatas.get(0).getSaleQty())
										/ day));
					} else {
						data.put(keys, 0);
					}

				}
				String[] beginDays = beginDateOne.split("-");
				String[] endDays = endDateOne.split("-");
				int lastYear = Integer.parseInt(beginDays[0]) - 1;
				beginDate = lastYear + "-" + beginDays[1] + "-" + beginDays[2];
				endDate = lastYear + "-" + endDays[1] + "-" + endDays[2];
				 Date d1=format.parse(beginDate);  
			     Date d2=format.parse(endDate);  
			        
				day =DateUtil.daysBetween(d1,d2);

				List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
						beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());
				if (Long.parseLong(sumDatas.get(0).getSaleQty()) != 0
						&& day != 0) {
					data.put("Total",
								Math.round(Double.parseDouble(sumDatas.get(0).getSaleQty())
										/ day));
				} else {
					data.put("Total", 0);
				}
				if (!sumDatas.get(0).getSaleSum().equals("")
						&& !sumDatas.get(0).getSaleSum().equals("0")
						&& sumDatas.get(0).getSaleSum() != null && day != 0) {
					data.put(
							"Amount",
							Math.round( Double.parseDouble(sumDatas.get(0)
									.getSaleSum()) / day));
				} else {
					data.put("Amount", 0);
				}

				System.out.println("==============R=2==="
						+ (System.currentTimeMillis() - star));
			} else if (r == 3) {

				DateUtil.year = Integer.parseInt(days[0]) - 1;
				DateUtil.month = Integer.parseInt(days[1]);
				data.put("YR-" + DateUtil.year, "YR-" + days[0]);
				int lastYear = DateUtil.year;

				for (int k = 0; k < dateList.size(); k++) {
					beginDate = dateList.get(k).get("beginDate").toString();
					endDate = dateList.get(k).get("endDate").toString();
					String[] beginDays = beginDate.split("-");
					String[] endDays = endDate.split("-");
					Date timec = format.parse(beginDate);
					Calendar rightNowc = Calendar.getInstance();
					rightNowc.setTime(timec);
					Date be = format.parse(format.format(rightNowc.getTime()));

					keys = sdf.format(be);
					lastYear = Integer.parseInt(beginDays[0]) - 1;
					data.put(keys, keys);

				}
				data.put("Total", "Total");
				data.put("Amount", "Amount");

			} else if (r == 4) {
				DateUtil.year = Integer.parseInt(days[0]) - 1;
				DateUtil.month = Integer.parseInt(days[1]);
				data.put("YR-" + DateUtil.year, "TTL QTY");

				for (int j = 0; j < dateList.size(); j++) {
					beginDate = dateList.get(j).get("beginDate").toString();
					endDate = dateList.get(j).get("endDate").toString();
					String[] beginDays = beginDate.split("-");
					String[] endDays = endDate.split("-");
					Date timec = format.parse(beginDate);
					Calendar rightNowc = Calendar.getInstance();
					rightNowc.setTime(timec);
					Date be = format.parse(format.format(rightNowc.getTime()));

					keys = sdf.format(be);
					int lastYear = Integer.parseInt(beginDays[0]) - 1;

					beginDate = beginDate;
					endDate = endDate;

					data.put(DateUtil.year + "." + days[1], "TTL QTY");
					List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
							beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());
					data.put(keys, Long.parseLong(sumDatas.get(0).getSaleQty()));

				}

				beginDate = beginDateOne;
				endDate = endDateOne;
				List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
						beginDateOne, endDateOne, searchStr, conditions,WebPageUtil.isHQRole());
				data.put("Total", sumDatas.get(0).getSaleQty());
				BigDecimal bd = new BigDecimal(sumDatas.get(0).getSaleSum());
				long lnum = Math.round(bd.doubleValue());

				data.put("Amount", lnum);
			} else if (r == 5) {
				int day = 0;
				data.put("YR-" + DateUtil.year, "Ave/day");
				DateUtil.year = Integer.parseInt(days[0]) - 1;
				DateUtil.month = Integer.parseInt(days[1]);
				String[] last = endDateOne.split("-");
				day = Integer.parseInt(last[2]) - Integer.parseInt(days[2]) + 1;

				for (int j = 0; j < dateList.size(); j++) {
					beginDate = dateList.get(j).get("beginDate").toString();
					endDate = dateList.get(j).get("endDate").toString();
					String[] beginDays = beginDate.split("-");
					String[] endDays = endDate.split("-");
					Date timec = format.parse(beginDate);
					Calendar rightNowc = Calendar.getInstance();
					rightNowc.setTime(timec);
					Date be = format.parse(format.format(rightNowc.getTime()));

					keys = sdf.format(be);
					int lastYear = Integer.parseInt(beginDays[0]) - 1;

					beginDate = beginDate;
					endDate = endDate;

					day = Integer.parseInt(endDays[2])
							- Integer.parseInt(beginDays[2]) + 1;
					List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
							beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());

					if (Long.parseLong(sumDatas.get(0).getSaleQty()) != 0
							&& day != 0) {
						data.put(keys,
									Math.round(Double.parseDouble(sumDatas.get(0).getSaleQty())
											/ day));
					} else {
						data.put(keys, 0);
					}

				}

				beginDate = beginDateOne;
				endDate = endDateOne;
				 Date d1=format.parse(beginDate);  
			     Date d2=format.parse(endDate);  
			        
				day =DateUtil.daysBetween(d1,d2);
				List<Excel> sumDatas = excelService.selectSaleDataBySumByAc(
						beginDate, endDate, searchStr, conditions,WebPageUtil.isHQRole());
				if (Long.parseLong(sumDatas.get(0).getSaleQty()) != 0
						&& day != 0) {
					data.put("Total",
							Math.round(Double.parseDouble(sumDatas.get(0).getSaleQty())
										/ day));
				} else {
					data.put("Total", 0);
				}
				if (!sumDatas.get(0).getSaleSum().equals("")
						&& !sumDatas.get(0).getSaleSum().equals("0")
						&& sumDatas.get(0).getSaleSum() != null && day != 0) {
					data.put(
							"Amount",
									Math.round(Double.parseDouble(sumDatas.get(0)
											.getSaleSum()) / day));
				} else {
					data.put("Amount", 0);
				}

			} else if (r == 6) {
				data.put("YR-" + DateUtil.year, "Sellout Growth per day");
				data.put("Total", "SUM(N8,-N5)");
				data.put("Amount", "SUM(O8,-O5)");
				for (int j = 0; j < dateList.size(); j++) {
					beginDate = dateList.get(j).get("beginDate").toString();
					endDate = dateList.get(j).get("endDate").toString();
					String[] beginDays = beginDate.split("-");
					String[] endDays = endDate.split("-");
					Date timec = format.parse(beginDate);
					Calendar rightNowc = Calendar.getInstance();
					rightNowc.setTime(timec);
					Date be = format.parse(format.format(rightNowc.getTime()));

					keys = sdf.format(be);
					if (j == 0) {
						data.put(keys, "SUM(B8,-B5)");
					} else if (j == 1) {
						data.put(keys, "SUM(C8,-C5)");
					} else if (j == 2) {
						data.put(keys, "SUM(D8,-D5)");
					} else if (j == 3) {
						data.put(keys, "SUM(E8,-E5)");
					} else if (j == 4) {
						data.put(keys, "SUM(F8,-F5)");
					} else if (j == 5) {
						data.put(keys, "SUM(G8,-G5)");
					} else if (j == 6) {
						data.put(keys, "SUM(H8,-H5)");
					} else if (j == 7) {
						data.put(keys, "SUM(I8,-I5)");
					} else if (j == 8) {
						data.put(keys, "SUM(J8,-J5)");
					} else if (j == 9) {
						data.put(keys, "SUM(K8,-K5)");
					} else if (j == 10) {
						data.put(keys, "SUM(L8,-L5)");
					} else if (j == 11) {
						data.put(keys, "SUM(M8,-M5)");
					}

				}

			}
			dataList.add(data);
		}

		try {

			LinkedList<HashMap<String, Object>> dataListTwo = new LinkedList<HashMap<String, Object>>();
			List<HashMap<String, Object>> areaInfo = excelService
					.selectDataByAreaInfo(searchStr, conditions);

			List<HashMap<String, Object>> res = excelService
					.selectRegionalHeadByParty(searchStr, conditions);

			List<HashMap<String, Object>> toDatas = excelService
					.selectDataByAreaByAc(beginDateOne, endDateOne, searchStr,
							conditions,WebPageUtil.isHQRole());

			List<HashMap<String, Object>> laDatas = excelService
					.selectDataByAreaByAc(laYear + "-" + year[1] + "-"
							+ year[2], laDays, searchStr, conditions,WebPageUtil.isHQRole());

			for (int s = 0; s < areaInfo.size(); s++) {
				double ts = 0.0;
				double ls =0.0;
				double tq =0.0;
				double lq = 0.0;
				int tfps = 0;
				int lfps = 0;
				double co = 0.0;
				HashMap<String, Object> data = new HashMap<String, Object>();

				for (int i = 0; i < res.size(); i++) {
					if (areaInfo.get(s).get("partyId")
							.equals(res.get(i).get("PARTY_ID"))) {
						data.put(
								"REGIONAL sell-out performances_REGIONAL HEAD",
								res.get(i).get("userName"));
					}
				}

				data.put("REGIONAL sell-out performances_AREA", areaInfo.get(s)
						.get("AREA"));

				for (int j = 0; j < toDatas.size(); j++) {
					if (areaInfo.get(s).get("AREA").toString()
							.equals(toDatas.get(j).get("AREA").toString())) {

						data.put("FPS_Yr-" + toYear[0],
								toDatas.get(j).get("tvFps"));
						if (toDatas.get(j).get("tvFps") == null
								|| toDatas.get(j).get("tvFps").equals("")) {
							tfps = 0;
						} else {
							tfps = Integer.parseInt(toDatas.get(j).get("tvFps")
									.toString());
						}

						BigDecimal bd = new BigDecimal(toDatas.get(j)
								.get("saleQty").toString());

						data.put("Total Flat Panel TV Quantity_" + toYear[0],
								bd.longValue());
						if (toDatas.get(j).get("saleQty") == null
								|| toDatas.get(j).get("saleQty").equals("")) {
							tq = (long) 0;
						} else {
							tq = bd.longValue();
						}

						bd = new BigDecimal(toDatas.get(j).get("saleSum")
								.toString());
						String am = bd.toPlainString();
						data.put("Total Amount_" + toYear[0], am);
						if (toDatas.get(j).get("saleSum") == null
								|| toDatas.get(j).get("saleSum").equals("")) {
							ts = (long) 0;
						} else {
							ts = bd.longValue();
						}

					}

				}
				for (int z = 0; z < laDatas.size(); z++) {
					if (areaInfo.get(s).get("AREA").toString()
							.equals(laDatas.get(z).get("AREA").toString())) {
						if (laDatas.get(z).get("tvFps") == null
								|| laDatas.get(z).get("tvFps").equals("")) {
							lfps = 0;
						} else {
							lfps = Integer.parseInt(laDatas.get(z).get("tvFps")
									.toString());
						}
						data.put("FPS_Yr-" + laYear, laDatas.get(z)
								.get("tvFps"));

						BigDecimal lbd = new BigDecimal(laDatas.get(z)
								.get("saleQty").toString());

						data.put("Total Flat Panel TV Quantity_" + laYear,
								lbd.longValue());
						if (laDatas.get(z).get("saleQty") == null
								|| laDatas.get(z).get("saleQty").equals("")) {
							lq = (long) 0;
						} else {
							lq = lbd.longValue();
						}

						lbd = new BigDecimal(laDatas.get(z).get("saleSum")
								.toString());
						String lam = lbd.toPlainString();
						data.put("Total Amount_" + laYear, lam);
						if (laDatas.get(z).get("saleSum") == null
								|| laDatas.get(z).get("saleSum").equals("")) {
							ls = (long) 0;
						} else {
							ls = lbd.longValue();
						}

					}

				}

				if (tq != 0) {
					co = tq - lq;
					double qg = co / tq * 100;
					long lnum = Math.round(qg);
					data.put("Total Flat Panel TV Quantity_SO Growth/day", lnum
							+ "%");
				}

				if (lfps != 0) {
					double lf = (lq / lfps);
					data.put("Average sellout per fps_" + laYear
							+ " Ave.qty/fps", Math.round(lf));
				}
				if (tfps != 0) {
					double tf = (tq / tfps);
					data.put("Average sellout per fps_" + toYear[0]
							+ " Ave.qty/fps", Math.round(tf));
				}
				if (lfps != 0) {
					double lff = (ls / lfps);
					data.put("Average sellout per fps_" + laYear
							+ " Ave.amt/fps", Math.round(lff));
				}
				if (tfps != 0) {
					double tff = (ts / tfps);
					data.put("Average sellout per fps_" + toYear[0]
							+ " Ave.amt/fps", Math.round(tff));
				}

				if (ts != 0.0) {
					co = ts - ls;
					double re = co / ts * 100;
					long lnum = Math.round(re);
					data.put("Total Amount_SO Growth/day", lnum + "%");
				}

				dataListTwo.add(data);

			}

			DateUtil.Order(dataListTwo, "Total Amount_SO Growth/day");
			for (int i = 0; i < dataListTwo.size(); i++) {
				dataListTwo.get(i).put("REGIONAL sell-out performances_RANK",
						i + 1);
			}

			HashMap<String, Object> data = new HashMap<String, Object>();
			int rows = 15 + dataListTwo.size();
			int end = rows + 1;
			data.put("REGIONAL sell-out performances_REGIONAL HEAD", "Total");
			data.put("FPS_Yr-" + laYear, "SUM(D16:D" + rows + ")");
			data.put("FPS_Yr-" + toYear[0], "SUM(E16:E" + rows + ")");
			data.put("Total Flat Panel TV Quantity_" + laYear, "SUM(F16:F"
					+ rows + ")");
			data.put("Total Flat Panel TV Quantity_" + toYear[0], "SUM(G16:G"
					+ rows + ")");

			data.put("Total Flat Panel TV Quantity_SO Growth/day", "TEXT((G"
					+ end + "-F" + end + ")/G" + end + ",\"0.00%\")");

			data.put("Total Amount_" + laYear, "SUM(I16:I" + rows + ")");
			data.put("Total Amount_" + toYear[0], "SUM(J16:J" + rows + ")");

			data.put("Total Amount_SO Growth/day", "TEXT((J" + end + "-I" + end
					+ ")/J" + end + ",\"0.00%\")");
			data.put("Average sellout per fps_" + laYear + " Ave.qty/fps",
					"SUM(L16:L" + rows + ")");
			data.put("Average sellout per fps_" + toYear[0] + " Ave.qty/fps",
					"SUM(M16:M" + rows + ")");
			data.put("Average sellout per fps_" + laYear + " Ave.amt/fps",
					"SUM(N16:N" + rows + ")");
			data.put("Average sellout per fps_" + toYear[0] + " Ave.amt/fps",
					"SUM(O16:O" + rows + ")");
			dataListTwo.add(data);

			LinkedList<HashMap<String, Object>> dataListThree = new LinkedList<HashMap<String, Object>>();
			List<HashMap<String, Object>> toDataBySalesInfo = excelService
					.selectTargetBySalemanInfo(searchStr, conditions);

			List<HashMap<String, Object>> toDataBySales = excelService
					.selectTargetBySalemanByAc(beginDateOne, endDateOne, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			List<HashMap<String, Object>> laDataBysales = excelService
					.selectTargetBySalemanByAc(laYear + "-" + year[1] + "-"
							+ year[2], laDays, searchStr, conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			List<HashMap<String, Object>> toFps = excelService
					.selectFpsNameByShopByAc(beginDateOne, endDateOne, searchStr,
							conditions);

			List<HashMap<String, Object>> laFps = excelService
					.selectFpsNameByShopByAc(laYear + "-" + year[1] + "-"
							+ year[2], laDays, searchStr, conditions);

			for (int w = 0; w < toDataBySalesInfo.size(); w++) {
				double ts = 0.0;
				double ls = 0.0;
				double tq = 0.0;
				double lq = 0.0;
				int tfps = 0;
				int lfps = 0;
				int tvFps = 0;
				double co = 0.0;
				BigDecimal bd = null;
				String am = "";
				HashMap<String, Object> dataMap = new HashMap<String, Object>();

				// dataMap.put("SALESMAN sell-out performances_RANK", w + 1);
				dataMap.put("SALESMAN sell-out performances_SALESMAN",
						toDataBySalesInfo.get(w).get("userName"));

				List<HashMap<String, Object>> Account = excelService
						.selectPartyNameByuser(searchStr, conditions);

				for (int j = 0; j < toDataBySales.size(); j++) {
					if (toDataBySalesInfo
							.get(w)
							.get("userName")
							.toString()
							.equals(toDataBySales.get(j).get("userName")
									.toString())) {

						if (Account.size() > 1) {
							String a = "";
							for (int k = 0; k < Account.size(); k++) {
								if (toDataBySales.get(j).get("userId")
										.equals(Account.get(k).get("userId"))) {

									a += Account.get(k).get("PARTY_NAME") + ",";
								}

							}
							if (a.length() > 0) {
								dataMap.put(
										"SALESMAN sell-out performances_REGION",
										a.substring(0, a.length() - 1));
							}
						}

						for (int i = 0; i < toFps.size(); i++) {
							if (toFps.get(i).get("USER")
									.equals(toDataBySales.get(j).get("userId"))) {
								tvFps = Integer.parseInt(toFps.get(i)
										.get("TVFPS").toString());
							}
						}

						dataMap.put("FPS_Yr-" + toYear[0], tvFps);

						tfps = tvFps;

						bd = new BigDecimal(toDataBySales.get(j).get("saleQty")
								.toString());

						dataMap.put(
								"Total Flat Panel TV Quantity_" + toYear[0],
								bd.longValue());

						tq = bd.longValue();

						bd = new BigDecimal(toDataBySales.get(j).get("saleSum")
								.toString());
						am = bd.toPlainString();
						dataMap.put("Total Amount_" + toYear[0], am);
						ts = bd.longValue();

					}

				}

				for (int s = 0; s < laDataBysales.size(); s++) {

					if (toDataBySalesInfo
							.get(w)
							.get("userName")
							.toString()
							.equals(laDataBysales.get(s).get("userName")
									.toString())) {

						for (int i = 0; i < laFps.size(); i++) {
							if (laFps.get(i).get("USER")
									.equals(laDataBysales.get(s).get("userId"))) {
								tvFps = Integer.parseInt(laFps.get(i)
										.get("TVFPS").toString());
							}
						}

						dataMap.put("FPS_Yr-" + laYear, tvFps);

						lfps = tvFps;

						bd = new BigDecimal(laDataBysales.get(s).get("saleQty")
								.toString());

						dataMap.put("Total Flat Panel TV Quantity_" + laYear,
								bd.longValue());

						lq = bd.longValue();
						bd = new BigDecimal(laDataBysales.get(s).get("saleSum")
								.toString());
						am = bd.toPlainString();
						dataMap.put("Total Amount_" + laYear, am);
						ls = bd.longValue();
					}

				}

				if (tq != 0) {
					co = (tq - lq);
					double qg = co / tq * 100;
					long lnum = Math.round(qg);
					dataMap.put("Total Flat Panel TV Quantity_SO Growth/day",
							lnum + "%");
				}

				if (lfps != 0) {
					double lf = (lq / lfps);
					dataMap.put("Average sellout per fps_" + laYear
							+ " Ave.qty/fps", Math.round(lf));
				}
				if (tfps != 0) {
					double tf = (tq / tfps);
					dataMap.put("Average sellout per fps_" + toYear[0]
							+ " Ave.qty/fps", Math.round(tf));
				}
				if (lfps != 0) {
					double lff = (ls / lfps);
					dataMap.put("Average sellout per fps_" + laYear
							+ " Ave.amt/fps", Math.round(lff));
				}
				if (tfps != 0) {
					double tff = (ts / tfps);
					dataMap.put("Average sellout per fps_" + toYear[0]
							+ " Ave.amt/fps", Math.round(tff));
				}

				if (ts != 0.0) {
					co = (ts - ls);
					double re = co / ts * 100;
					long lnum = Math.round(re);
					dataMap.put("Total Amount_SO Growth/day", lnum + "%");
				}

				dataListThree.add(dataMap);
			}

			DateUtil.Order(dataListThree, "Total Amount_SO Growth/day");
			for (int i = 0; i < dataListThree.size(); i++) {
				dataListThree.get(i).put("SALESMAN sell-out performances_RANK",
						i + 1);
			}
			HashMap<String, Object> dataThree = new HashMap<String, Object>();
			int stratRow = 15 + dataListTwo.size() + 11;
			int rowsBysale = 15 + dataListTwo.size() + 10
					+ dataListThree.size();
			end = rowsBysale + 1;
			dataThree.put("SALESMAN sell-out performances_SALESMAN", "Total");
			dataThree.put("FPS_Yr-" + laYear, "SUM(D" + stratRow + ":D"
					+ rowsBysale + ")");
			dataThree.put("FPS_Yr-" + toYear[0], "SUM(E" + stratRow + ":E"
					+ rowsBysale + ")");
			dataThree.put("Total Flat Panel TV Quantity_" + laYear, "SUM(F"
					+ stratRow + ":F" + rowsBysale + ")");
			dataThree.put("Total Flat Panel TV Quantity_" + toYear[0], "SUM(G"
					+ stratRow + ":G" + rowsBysale + ")");

			dataThree.put("Total Flat Panel TV Quantity_SO Growth/day",
					"TEXT((G" + end + "-F" + end + ")/G" + end + ",\"0.00%\")");

			dataThree.put("Total Amount_" + laYear, "SUM(I" + stratRow + ":I"
					+ rowsBysale + ")");
			dataThree.put("Total Amount_" + toYear[0], "SUM(J" + stratRow
					+ ":J" + rowsBysale + ")");

			dataThree.put("Total Amount_SO Growth/day", "TEXT((J" + end + "-I"
					+ end + ")/J" + end + ",\"0.00%\")");
			dataThree.put("Average sellout per fps_" + laYear + " Ave.qty/fps",
					"SUM(L" + stratRow + ":L" + rowsBysale + ")");
			dataThree.put("Average sellout per fps_" + toYear[0]
					+ " Ave.qty/fps", "SUM(M" + stratRow + ":M" + rowsBysale
					+ ")");

			dataThree.put("Average sellout per fps_" + laYear + " Ave.amt/fps",
					"SUM(N" + stratRow + ":N" + rowsBysale + ")");
			dataThree.put("Average sellout per fps_" + toYear[0]
					+ " Ave.amt/fps", "SUM(O" + stratRow + ":O" + rowsBysale
					+ ")");
			dataListThree.add(dataThree);

			LinkedList<HashMap<String, Object>> dataListFour = new LinkedList<HashMap<String, Object>>();

			List<HashMap<String, Object>> toDataByAcfoInfo = excelService
					.selectTargetByAcfoInfo(searchStr, conditions);

			List<HashMap<String, Object>> toDataByAcfo = excelService
					.selectTargetByAcfoByAc(beginDateOne, endDateOne, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			List<HashMap<String, Object>> laDataByAcfo = excelService
					.selectTargetByAcfoByAc(laYear + "-" + year[1] + "-"
							+ year[2], laDays, searchStr, conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			toFps = excelService.selectFpsNameByShopByAc(beginDateOne, endDateOne,
					searchStr, conditions);

			laFps = excelService.selectFpsNameByShopByAc(laYear + "-" + year[1]
					+ "-" + year[2], laDays, searchStr, conditions);

			List<HashMap<String, Object>> Account = excelService
					.selectAreaByUser(searchStr, conditions);
			for (int z = 0; z < toDataByAcfoInfo.size(); z++) {
				double ts =  0.0;
				double ls =  0.0;
				double tq =  0.0;
				double lq =  0.0;
				int tfps = 0;
				int lfps = 0;
				double co = 0.0;
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				// dataMap.put("ACFO sell-out performances_RANK", z + 1);

				int tvFps = 0;
				BigDecimal bd = null;
				String am = "";
				for (int j = 0; j < toDataByAcfo.size(); j++) {

					if (toDataByAcfoInfo
							.get(z)
							.get("userName")
							.toString()
							.equals(toDataByAcfo.get(j).get("userName")
									.toString())) {

						dataMap.put("ACFO sell-out performances_ACFO",
								toDataByAcfo.get(j).get("userName"));

						if (Account.size() > 1) {
							String a = "";
							for (int k = 0; k < Account.size(); k++) {
								if (toDataByAcfo.get(j).get("userId")
										.equals(Account.get(k).get("userId"))) {

									a += Account.get(k).get("PARTY_NAME") + ",";
								}

							}
							if (a.length() > 0) {

								dataMap.put(
										"ACFO sell-out performances_REGION",
										a.substring(0, a.length() - 1));

							}
						}

						for (int i = 0; i < toFps.size(); i++) {
							if (toFps.get(i).get("USER")
									.equals(toDataByAcfo.get(j).get("userId"))) {
								tvFps = Integer.parseInt(toFps.get(i)
										.get("TVFPS").toString());
							}

						}

						tfps = tvFps;
						dataMap.put("FPS_Yr-" + toYear[0], tvFps);
						bd = new BigDecimal(toDataByAcfo.get(j).get("saleQty")
								.toString());

						dataMap.put(
								"Total Flat Panel TV Quantity_" + toYear[0],
								bd.longValue());
						tq = bd.longValue();
						bd = new BigDecimal(toDataByAcfo.get(j).get("saleSum")
								.toString());
						am = bd.toPlainString();
						dataMap.put("Total Amount_" + toYear[0], am);
						ts = bd.longValue();
					}

				}

				for (int s = 0; s < laDataByAcfo.size(); s++) {
					if (toDataByAcfoInfo
							.get(z)
							.get("userName")
							.toString()
							.equals(laDataByAcfo.get(s).get("userName")
									.toString())) {

						for (int i = 0; i < laFps.size(); i++) {
							if (laFps.get(i).get("USER")
									.equals(laDataByAcfo.get(s).get("userId"))) {
								tvFps = Integer.parseInt(laFps.get(i)
										.get("TVFPS").toString());
							}

						}

						dataMap.put("FPS_Yr-" + laYear, tvFps);

						lfps = tvFps;

						bd = new BigDecimal(laDataByAcfo.get(s).get("saleQty")
								.toString());

						dataMap.put("Total Flat Panel TV Quantity_" + laYear,
								bd.longValue());

						lq = bd.longValue();
						bd = new BigDecimal(laDataByAcfo.get(s).get("saleSum")
								.toString());
						am = bd.toPlainString();
						dataMap.put("Total Amount_" + laYear, am);
						ls = bd.longValue();
					}

				}
				if (tq != 0) {
					co = (tq - lq);
					double qg = co / tq * 100;
					long lnum = Math.round(qg);
					dataMap.put("Total Flat Panel TV Quantity_SO Growth/day",
							lnum + "%");
				}

				if (lfps != 0) {
					double lf = (lq / lfps);
					dataMap.put("Average sellout per fps_" + laYear
							+ " Ave.qty/fps", Math.round(lf));
				}
				if (tfps != 0) {
					double tf = (tq / tfps);
					dataMap.put("Average sellout per fps_" + toYear[0]
							+ " Ave.qty/fps", Math.round(tf));
				}
				if (lfps != 0) {
					double lff = (ls / lfps);
					dataMap.put("Average sellout per fps_" + laYear
							+ " Ave.amt/fps", Math.round(lff));
				}
				if (tfps != 0) {
					double tff = (ts / tfps);
					dataMap.put("Average sellout per fps_" + toYear[0]
							+ " Ave.amt/fps", Math.round(tff));
				}

				if (ts != 0.0) {
					co = (ts - ls);
					double re = co / ts * 100;
					long lnum = Math.round(re);
					dataMap.put("Total Amount_SO Growth/day", lnum + "%");
				}

				dataListFour.add(dataMap);
			}

			DateUtil.Order(dataListFour, "Total Amount_SO Growth/day");
			for (int i = 0; i < dataListFour.size(); i++) {
				dataListFour.get(i).put("ACFO sell-out performances_RANK",
						i + 1);
			}
			HashMap<String, Object> dataFour = new HashMap<String, Object>();
			stratRow = 15 + dataListTwo.size() + 11 + dataListThree.size() + 10;

			rowsBysale = 15 + dataListTwo.size() + 11 + dataListThree.size()
					+ 9 + dataListFour.size();
			end = rowsBysale + 1;
			dataFour.put("ACFO sell-out performances_ACFO", "Total");
			dataFour.put("FPS_Yr-" + laYear, "SUM(D" + stratRow + ":D"
					+ rowsBysale + ")");
			dataFour.put("FPS_Yr-" + toYear[0], "SUM(E" + stratRow + ":E"
					+ rowsBysale + ")");
			dataFour.put("Total Flat Panel TV Quantity_" + laYear, "SUM(F"
					+ stratRow + ":F" + rowsBysale + ")");
			dataFour.put("Total Flat Panel TV Quantity_" + toYear[0], "SUM(G"
					+ stratRow + ":G" + rowsBysale + ")");
			dataFour.put("Total Flat Panel TV Quantity_SO Growth/day",
					"TEXT((G" + end + "-F" + end + ")/G" + end + ",\"0.00%\")");
			dataFour.put("Total Amount_" + laYear, "SUM(I" + stratRow + ":I"
					+ rowsBysale + ")");
			dataFour.put("Total Amount_" + toYear[0], "SUM(J" + stratRow + ":J"
					+ rowsBysale + ")");
			dataFour.put("Total Amount_SO Growth/day", "TEXT((J" + end + "-I"
					+ end + ")/J" + end + ",\"0.00%\")");
			dataFour.put("Average sellout per fps_" + laYear + " Ave.qty/fps",
					"SUM(L" + stratRow + ":L" + rowsBysale + ")");
			dataFour.put("Average sellout per fps_" + toYear[0]
					+ " Ave.qty/fps", "SUM(M" + stratRow + ":M" + rowsBysale
					+ ")");

			dataFour.put("Average sellout per fps_" + laYear + " Ave.amt/fps",
					"SUM(N" + stratRow + ":N" + rowsBysale + ")");
			dataFour.put("Average sellout per fps_" + toYear[0]
					+ " Ave.amt/fps", "SUM(O" + stratRow + ":O" + rowsBysale
					+ ")");
			dataListFour.add(dataFour);

			LinkedList<HashMap<String, Object>> dataListFive = new LinkedList<HashMap<String, Object>>();
			LinkedList<HashMap<String, Object>> dataListSix = new LinkedList<HashMap<String, Object>>();
			long start = System.currentTimeMillis();

			String beginSize = "";
			String endSize = "";

			String lastDay = DateUtil.getLastDayOfMonth(
					Integer.parseInt(toYear[0]), 1);
			String[] lastDays = lastDay.split("-");
			String startDay = "";
			String Month = "";
			HashMap<String, Object> dataFive = new HashMap<String, Object>();
			HashMap<String, Object> dataSix = new HashMap<String, Object>();
			long sttt = System.currentTimeMillis();
			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMap = new LinkedHashMap<String, LinkedHashMap<String, Object>>();

			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapSix = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
			for (int j = 1; j <= 13; j++) {
				if (j == 13) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 1);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-01-01";
					Month = "Jan";
				} else if (j == 2) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 2);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-02-01";
					Month = "Feb";
				} else if (j == 3) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 3);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-03-01";
					Month = "Mar";
				} else if (j == 4) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 4);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-04-01";
					Month = "Apr";
				} else if (j == 5) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 5);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-05-01";
					Month = "May";
				} else if (j == 6) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 6);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-06-01";
					Month = "June";
				} else if (j == 7) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 7);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-07-01";
					Month = "July";
				} else if (j == 8) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 8);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-08-01";
					Month = "August";
				} else if (j == 9) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 9);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-09-01";
					Month = "September";
				} else if (j == 10) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 10);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-10-01";
					Month = "October";
				} else if (j == 11) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 11);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-11-01";
					Month = "November";
				} else if (j == 12) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 12);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-12-01";
					Month = "December";
				} else if (j == 1) {
					lastDay = endDateOne;
					lastDays = lastDay.split("-");
					startDay = beginDateOne;
					Month = "TTL";
				}

				List<LinkedHashMap<String, Object>> sizeDatasOne = excelService
						.selectSaleDataBySizeByAc(beginSize, endSize, startDay,
								lastDay, searchStr, conditions,WebPageUtil.isHQRole());

				List<LinkedHashMap<String, Object>> sizeDatasOneData = excelService
						.selectSaleTotalBySizeByAc(startDay, lastDay, searchStr,
								conditions,WebPageUtil.isHQRole());

				for (int a = 0; a < sizeDatasOne.size(); a++) {
					LinkedHashMap<String, Object> colMap = sizeDatasOne.get(a);

					String key = colMap.get("sizeT").toString();

					if (allDataMap.get(key) != null) {
						LinkedHashMap<String, Object> rowMap = allDataMap
								.get(key);

						BigDecimal bd = new BigDecimal(sizeDatasOne.get(a)
								.get("quantity").toString());

						rowMap.put("YTD-" + toYear[0]
								+ "  Monthly sellout trend per size_" + Month
								+ "", bd.longValue());

						allDataMap.put(key, rowMap);

						double one = bd.longValue();

						if (sizeDatasOneData.size() == 1) {

							LinkedHashMap<String, Object> colMapSix = sizeDatasOneData
									.get(0);
							if (allDataMapSix.get(key) != null) {
								LinkedHashMap<String, Object> rowMapSix = allDataMapSix
										.get(key);

								bd = new BigDecimal(sizeDatasOneData.get(0)
										.get("quantity").toString());
								double oneData = bd.longValue();

								double oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapSix.put("Market Share_" + Month + "",
										lnum + "%");
								allDataMapSix.put(key, rowMapSix);

							} else {
								LinkedList<HashMap<String, Object>> rowListSix = new LinkedList<HashMap<String, Object>>();
								rowListSix.addLast(colMapSix);
								LinkedHashMap<String, Object> rowMapSix = new LinkedHashMap<String, Object>();

								rowMapSix.put("Market Share_Category", key);

								bd = new BigDecimal(sizeDatasOneData.get(0)
										.get("quantity").toString());
								double oneData = bd.longValue();

								double oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapSix.put("Market Share_" + Month + "",
										lnum + "%");
								allDataMapSix.put(key, rowMapSix);
							}

						}
					} else {

						LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();
						rowMap.put("YTD-" + toYear[0]
								+ "  Monthly sellout trend per size_Category",
								key);
						rowMap.put("YTD-" + toYear[0]
								+ "  Monthly sellout trend per size_" + Month
								+ "", key);
						BigDecimal bd = new BigDecimal(sizeDatasOne.get(a)
								.get("quantity").toString());

						rowMap.put("YTD-" + toYear[0]
								+ "  Monthly sellout trend per size_" + Month
								+ "", bd.longValue());

						double one = bd.longValue();

						if (sizeDatasOneData.size() == 1) {

							LinkedHashMap<String, Object> colMapSix = sizeDatasOneData
									.get(0);
							if (allDataMapSix.get(key) != null) {
								LinkedHashMap<String, Object> rowMapSix = allDataMapSix
										.get(key);

								bd = new BigDecimal(sizeDatasOneData.get(0)
										.get("quantity").toString());
								double oneData = bd.longValue();

								double oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapSix.put("Market Share_" + Month + "",
										lnum + "%");
								allDataMapSix.put(key, rowMapSix);

							} else {
								LinkedList<HashMap<String, Object>> rowListSix = new LinkedList<HashMap<String, Object>>();
								rowListSix.addLast(colMapSix);
								LinkedHashMap<String, Object> rowMapSix = new LinkedHashMap<String, Object>();

								rowMapSix.put("Market Share_Category", key);

								bd = new BigDecimal(sizeDatasOneData.get(0)
										.get("quantity").toString());
								double oneData = bd.longValue();

								double oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapSix.put("Market Share_" + Month + "",
										lnum + "%");
								allDataMapSix.put(key, rowMapSix);
							}

						}

						allDataMap.put(key, rowMap);
					}

				}

			}

			Set<String> sizeSet = allDataMap.keySet();

			Iterator<String> sizeIter = sizeSet.iterator();
			HashMap<String, Object> totalMap = new HashMap<String, Object>();
			String total_header = "";
			while (sizeIter.hasNext()) {
				String key = sizeIter.next();
				HashMap<String, Object> rowMap = allDataMap.get(key);
				dataListFive.addLast(rowMap);

				Set<String> rowSet = rowMap.keySet();

				Iterator<String> rowIter = rowSet.iterator();
				while (rowIter.hasNext()) {
					String rheader = rowIter.next();

					if (!rheader.contains("Category")) {
						int quantity = Integer.parseInt(rowMap.get(rheader)
								.toString());
						if (totalMap.get(rheader) == null) {

							totalMap.put(rheader, quantity);
						} else {
							int totalQuantity = Integer.parseInt(totalMap.get(
									rheader).toString());
							totalQuantity += quantity;
							totalMap.put(rheader, totalQuantity);
						}

					} else {
						total_header = rheader;
					}

				}

			}
			totalMap.put(total_header, "total");
			dataListFive.addLast(totalMap);

			Set<String> sizeSetSix = allDataMapSix.keySet();

			Iterator<String> sizeIterSix = sizeSetSix.iterator();
			HashMap<String, Object> totalMapSix = new HashMap<String, Object>();
			while (sizeIterSix.hasNext()) {
				String key = sizeIterSix.next();
				HashMap<String, Object> rowMap = allDataMapSix.get(key);
				dataListSix.addLast(rowMap);

				Set<String> rowSet = rowMap.keySet();
				Iterator<String> rowIter = rowSet.iterator();

				while (rowIter.hasNext()) {
					String rheader = rowIter.next();

					if (!rheader.contains("Category")) {

						if (totalMap.get(rheader) == null) {

							totalMap.put(rheader, "100%");
						} else {
							totalMap.put(rheader, "100%");
						}

					} else {
						total_header = rheader;
					}

				}

			}
			totalMap.put("Market Share_Category", "total");
			dataListSix.addLast(totalMap);

			LinkedList<HashMap<String, Object>> dataListEight = new LinkedList<HashMap<String, Object>>();
			LinkedList<HashMap<String, Object>> dataListNight = new LinkedList<HashMap<String, Object>>();

			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapEight = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapNight = new LinkedHashMap<String, LinkedHashMap<String, Object>>();

			start = System.currentTimeMillis();
			// for (int i = 1; i <= 7; i++) {
			HashMap<String, Object> dataEight = new HashMap<String, Object>();
			HashMap<String, Object> dataNight = new HashMap<String, Object>();
			String spec = "";
			double one = 0.0;
			double oneData = 0.0;
			double oneAch = 0.0;
			for (int j = 1; j <= 13; j++) {
				if (j == 13) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 1);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-01-01";
					Month = "Jan";
				} else if (j == 2) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 2);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-02-01";
					Month = "Feb";
				} else if (j == 3) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 3);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-03-01";
					Month = "Mar";
				} else if (j == 4) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 4);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-04-01";
					Month = "Apr";
				} else if (j == 5) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 5);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-05-01";
					Month = "May";
				} else if (j == 6) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 6);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-06-01";
					Month = "June";
				} else if (j == 7) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 7);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-07-01";
					Month = "July";
				} else if (j == 8) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 8);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-08-01";
					Month = "August";
				} else if (j == 9) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 9);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-09-01";
					Month = "September";
				} else if (j == 10) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 10);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-10-01";
					Month = "October";
				} else if (j == 11) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 11);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-11-01";
					Month = "November";
				} else if (j == 12) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 12);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-12-01";
					Month = "December";
				} else if (j == 1) {
					lastDay = endDateOne;
					lastDays = lastDay.split("-");
					startDay = beginDateOne;
					Month = "TTL";
				}

				List<LinkedHashMap<String, Object>> sizeDatasOneData = excelService
						.selectQtyTotalBySpecByAc(spec, startDay, lastDay,
								searchStr, conditions,WebPageUtil.isHQRole());
				List<LinkedHashMap<String, Object>> sizeDatasTwTotal = excelService
						.selectSaleTotalBySizeByAc(startDay, lastDay, searchStr,
								conditions,WebPageUtil.isHQRole());

				for (int a = 0; a < sizeDatasOneData.size(); a++) {
					HashMap<String, Object> colMap = sizeDatasOneData.get(a);

					String key = colMap.get("SPEC").toString();

					if (allDataMapEight.get(key) != null) {
						LinkedHashMap<String, Object> rowMap = allDataMapEight
								.get(key);

						BigDecimal bd = new BigDecimal(sizeDatasOneData.get(a)
								.get("quantity").toString());

						rowMap.put("Different catgory sell-out quantity_"
								+ Month + "", bd.longValue());

						allDataMapEight.put(key, rowMap);

						one = bd.longValue();

						if (sizeDatasTwTotal.size() == 1) {

							LinkedHashMap<String, Object> colMapNight = sizeDatasTwTotal
									.get(0);
							if (allDataMapNight.get(key) != null) {
								LinkedHashMap<String, Object> rowMapNight = allDataMapNight
										.get(key);

								one = bd.longValue();
								bd = new BigDecimal(sizeDatasTwTotal.get(0)
										.get("quantity").toString());
								oneData = bd.longValue();
								oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapNight.put("Growth rate_" + Month + "",
										lnum + "%");
								allDataMapNight.put(key, rowMapNight);

							} else {
								LinkedList<HashMap<String, Object>> rowListNight = new LinkedList<HashMap<String, Object>>();
								rowListNight.addLast(colMapNight);
								LinkedHashMap<String, Object> rowMapNight = new LinkedHashMap<String, Object>();

								rowMapNight.put("Growth rate_Category", key);
								one = bd.longValue();
								bd = new BigDecimal(sizeDatasTwTotal.get(0)
										.get("quantity").toString());
								oneData = bd.longValue();
								oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapNight.put("Growth rate_" + Month + "",
										lnum + "%");
								allDataMapNight.put(key, rowMapNight);
							}

						}
					} else {
						LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
						rowList.addLast(colMap);

						LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();
						BigDecimal bd = new BigDecimal(sizeDatasOneData.get(a)
								.get("quantity").toString());

						rowMap.put(
								"Different catgory sell-out quantity_Category",
								key);
						rowMap.put("Different catgory sell-out quantity_"
								+ Month + "", key);

						rowMap.put("Different catgory sell-out quantity_"
								+ Month + "", bd.longValue());

						allDataMapEight.put(key, rowMap);

						one = bd.longValue();

						if (sizeDatasTwTotal.size() == 1) {

							LinkedHashMap<String, Object> colMapNight = sizeDatasTwTotal
									.get(0);
							if (allDataMapNight.get(key) != null) {
								LinkedHashMap<String, Object> rowMapNight = allDataMapNight
										.get(key);

								one = bd.longValue();
								bd = new BigDecimal(sizeDatasTwTotal.get(0)
										.get("quantity").toString());
								oneData = bd.longValue();
								oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapNight.put("Growth rate_" + Month + "",
										lnum + "%");
								allDataMapNight.put(key, rowMapNight);

							} else {
								LinkedList<HashMap<String, Object>> rowListNight = new LinkedList<HashMap<String, Object>>();
								rowListNight.addLast(colMapNight);
								LinkedHashMap<String, Object> rowMapNight = new LinkedHashMap<String, Object>();

								rowMapNight.put("Growth rate_Category", key);
								one = bd.longValue();
								bd = new BigDecimal(sizeDatasTwTotal.get(0)
										.get("quantity").toString());
								oneData = bd.longValue();
								oneAch = one / oneData * 100;
								long lnum = Math.round(oneAch);

								rowMapNight.put("Growth rate_" + Month + "",
										lnum + "%");
								allDataMapNight.put(key, rowMapNight);
							}

						}

					}

				}

			}

			Set<String> sizeSetEight = allDataMapEight.keySet();

			Iterator<String> sizeIterEight = sizeSetEight.iterator();
			HashMap<String, Object> totalMapEight = new HashMap<String, Object>();
			while (sizeIterEight.hasNext()) {
				String key = sizeIterEight.next();
				HashMap<String, Object> rowMap = allDataMapEight.get(key);
				dataListEight.addLast(rowMap);

				Set<String> rowSet = rowMap.keySet();

				Iterator<String> rowIter = rowSet.iterator();
				while (rowIter.hasNext()) {
					String rheader = rowIter.next();

					if (!rheader.contains("Category")) {
						int quantity = Integer.parseInt(rowMap.get(rheader)
								.toString());
						if (totalMapEight.get(rheader) == null) {

							totalMapEight.put(rheader, quantity);
						} else {
							int totalQuantity = Integer.parseInt(totalMapEight
									.get(rheader).toString());
							totalQuantity += quantity;
							totalMapEight.put(rheader, totalQuantity);
						}

					} else {
						total_header = rheader;
					}

				}

			}
			totalMapEight.put(total_header, "Total");
			dataListEight.addLast(totalMapEight);

			Set<String> sizeSetNight = allDataMapNight.keySet();

			Iterator<String> sizeIterNight = sizeSetNight.iterator();
			HashMap<String, Object> totalMapNight = new HashMap<String, Object>();
			while (sizeIterNight.hasNext()) {
				String key = sizeIterNight.next();
				HashMap<String, Object> rowMap = allDataMapNight.get(key);
				dataListNight.addLast(rowMap);

				Set<String> rowSet = rowMap.keySet();
				Iterator<String> rowIter = rowSet.iterator();

				while (rowIter.hasNext()) {
					String rheader = rowIter.next();

					if (!rheader.contains("Category")) {

						if (totalMapNight.get(rheader) == null) {

							totalMapNight.put(rheader, "100%");
						} else {
							totalMapNight.put(rheader, "100%");
						}

					} else {
						total_header = rheader;
					}

				}

			}
			totalMapNight.put("Growth rate_Category", "Total");
			dataListNight.addLast(totalMapNight);

			System.out.println("=====第7,8个查询="
					+ (System.currentTimeMillis() - start));

			LinkedList<HashMap<String, Object>> dataListTen = new LinkedList<HashMap<String, Object>>();

			int day = 0;
			long startTime = System.currentTimeMillis();

			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapTen = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
			LinkedHashMap<String, Object> rowMapTTL = new LinkedHashMap<String, Object>();
			for (int j = 1; j <= 14; j++) {
				if (j == 13) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 1);
					startDay = toYear[0] + "-01-01";
					Month = "Jan";
				} else if (j == 2) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 2);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-02-01";
					Month = "Feb";
				} else if (j == 3) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 3);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-03-01";
					Month = "Mar";
				} else if (j == 4) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 4);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-04-01";
					Month = "Apr";
				} else if (j == 5) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 5);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-05-01";
					Month = "May";
				} else if (j == 6) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 6);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-06-01";
					Month = "June";
				} else if (j == 7) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 7);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-07-01";
					Month = "July";
				} else if (j == 8) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 8);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-08-01";
					Month = "August";
				} else if (j == 9) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 9);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-09-01";
					Month = "September";
				} else if (j == 10) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 10);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-10-01";
					Month = "October";
				} else if (j == 11) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 11);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-11-01";
					Month = "November";
				} else if (j == 12) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 12);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-12-01";
					Month = "December";
				} else if (j == 1) {
					lastDay = endDateOne;
					lastDays = lastDay.split("-");
					startDay = beginDateOne;
					Month = "Total";
				} else if (j == 14) {
					String sd = beginDateOne.split("-")[2];
					String ed = endDateOne.split("-")[2];
					day = Integer.parseInt(ed) - Integer.parseInt(sd) + 1;
					Month = "Ave So per day";
					lastDay = endDateOne;
					lastDays = lastDay.split("-");
					startDay = beginDateOne;
				}

				List<LinkedHashMap<String, Object>> sumDatas = excelService
						.selectSaleDataByDEALERByAc(startDay, lastDay, searchStr,
								conditions,WebPageUtil.isHQRole());

				for (int z = 0; z < sumDatas.size(); z++) {
					LinkedHashMap<String, Object> colMap = sumDatas.get(z);
					String key = colMap.get("DEALER").toString();

					if (allDataMapTen.get(key) != null) {
						LinkedHashMap<String, Object> rowMap = allDataMapTen
								.get(key);

						BigDecimal bd = new BigDecimal(sumDatas.get(z)
								.get("saleQty").toString());
						rowMap.put(Month, bd.longValue());
						rowMap.put("DEALER", sumDatas.get(z).get("DEALER"));

						if (j == 14) {
							double qty = bd.longValue();
							rowMap.put(Month, Math.round(qty / day));
						}
						allDataMapTen.put(key, rowMap);
					} else {
						LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
						rowList.addLast(colMap);

						LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();
						BigDecimal bd = new BigDecimal(sumDatas.get(z)
								.get("saleQty").toString());
						rowMap.put("DEALER", sumDatas.get(z).get("DEALER"));
						rowMap.put(Month, key);
						rowMap.put(Month, bd.longValue());

						if (j == 14) {
							double qty = bd.longValue();
							rowMap.put(Month, Math.round(qty / day));
						}
						allDataMapTen.put(key, rowMap);

					}

				}

				List<HashMap<String, Object>> sumTTL = excelService
						.selectSaleTTLByDEALERByAc(startDay, lastDay, searchStr,
								conditions,WebPageUtil.isHQRole());
				String key = Month;
				for (int s = 0; s < sumTTL.size(); s++) {
					HashMap<String, Object> colMap = sumTTL.get(s);
					rowMapTTL.put(Month, key);
					BigDecimal bd = new BigDecimal(sumTTL.get(s).get("saleQty")
							.toString().toString());
					rowMapTTL.put(Month, bd.longValue());
					rowMapTTL.put("DEALER", "Total");
					if (j == 14) {
						double qty = bd.longValue();
						rowMapTTL.put(Month, Math.round(qty / day));
					}

				}

			}
			allDataMapTen.put("Total", rowMapTTL);

			System.out.println(allDataMapTen);

			Set<String> sizeSetTen = allDataMapTen.keySet();

			Iterator<String> sizeIterTen = sizeSetTen.iterator();
			HashMap<String, Object> totalMapTen = new HashMap<String, Object>();
			while (sizeIterTen.hasNext()) {
				String key = sizeIterTen.next();
				HashMap<String, Object> rowMap = allDataMapTen.get(key);
				dataListTen.addLast(rowMap);

				Set<String> rowSet = rowMap.keySet();
				Iterator<String> rowIter = rowSet.iterator();

			}

			// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
			Sheet sheet = wb.createSheet(Integer.parseInt(toYear[0]) - 1
					+ "  vs. " + toYear[0] + " comparative");
			sheet.setZoom(3, 4);
			// 创建字体
			Font fontinfo = wb.createFont();
			fontinfo.setFontHeightInPoints((short) 11); // 字体大小
			fontinfo.setFontName("Trebuchet MS");
			Font fonthead = wb.createFont();
			fonthead.setFontHeightInPoints((short) 12);
			fonthead.setFontName("Trebuchet MS");
			// colSplit, rowSplit,leftmostColumn, topRow,
			sheet.createFreezePane(3, 2, 3, 2);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat
					.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
														// hh:mm:ss

			CellStyle cellStylehead = wb.createCellStyle();// 表头样式
			cellStylehead.setFont(fonthead);
			cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylehead.setWrapText(true);

			CellStyle cellStyleWHITE = wb.createCellStyle();// 表头样式
			cellStyleWHITE.setFont(fonthead);
			cellStyleWHITE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleWHITE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleWHITE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyleWHITE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleWHITE.setWrapText(true);

			CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fonthead);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fonthead);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleYellow.setWrapText(true);

			CellStyle cellStylePERCENT = wb.createCellStyle();// 表头样式
			cellStylePERCENT.setFont(fonthead);
			cellStylePERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePERCENT.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePERCENT.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT
					.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStylePERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePERCENT.setWrapText(true);

			CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
			cellStyleRED.setFont(fonthead);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);

			CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			Row row = sheet.createRow((short) 0);
			// 创建这一行的一列，即创建单元格(CELL)
			Cell cell = row.createCell((short) 0);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("TCL " + partyName);// 标题
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

			// 第二行
			Row rowSec = sheet.createRow((short) 1);
			cell = rowSec.createCell((short) 0);
			cell.setCellStyle(cellStylename);
			cell.setCellValue("SELL-OUT ANALYSIS");
			// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
			CellStyle contextstyle = wb.createCellStyle();
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			contextstyle.setFont(fontinfo);
			// 从17行开始放另一个表格的表头

			int rows_max = 0; // 最大的一个项有几个子项

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map map = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 2);
				for (int i = 0; i < header.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = header[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2,
								rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (map.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (map.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < header.length; j++) {
							if (header[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 2,
								k + 2, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k
									+ 2 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerTwo.length; i++) {
				String h = headerTwo[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapTwo = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13);
				for (int i = 0; i < headerTwo.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerTwo[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13,
								rows_max + 12, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapTwo.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapTwo.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerTwo.length; j++) {
							if (headerTwo[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13,
								k + 13, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13,
									k + 13 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapTwo.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapTwo.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerThree.length; i++) {
				String h = headerThree[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapThree = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10);
				for (int i = 0; i < headerThree.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerThree[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(
								13 + dataListTwo.size() + 10, rows_max + 12
										+ dataListTwo.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapThree.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapThree.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerThree.length; j++) {
							if (headerThree[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10, k + 13
								+ dataListTwo.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10, k + 13
									+ dataListTwo.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapThree.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapThree.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerFour.length; i++) {
				String h = headerFour[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapFour = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10
						+ dataListThree.size() + 10);
				for (int i = 0; i < headerFour.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerFour[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10, rows_max + 12
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10 + 5, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapFour.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapFour.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerFour.length; j++) {
							if (headerFour[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10, k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapFour.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapFour.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerFive.length; i++) {
				String h = headerFive[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapFive = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10
						+ dataListThree.size() + 10 + dataListFour.size() + 10);
				for (int i = 0; i < headerFive.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerFive[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10, rows_max + 12
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapFive.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapFive.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerFive.length; j++) {
							if (headerFive[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10, k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapFive.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapFive.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerSix.length; i++) {
				String h = headerSix[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapSix = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10
						+ dataListThree.size() + 10 + dataListFour.size() + 10
						+ dataListFive.size() + 10);
				for (int i = 0; i < headerSix.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerSix[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10, rows_max + 12
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapSix.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapSix.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerSix.length; j++) {
							if (headerSix[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10, k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapSix.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapSix.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerEight.length; i++) {
				String h = headerEight[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapEight = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10
						+ dataListThree.size() + 10 + dataListFour.size() + 10
						+ dataListFive.size() + 10 + dataListSix.size() + 10);
				for (int i = 0; i < headerEight.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerEight[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10,

						rows_max + 12 + dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapEight.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapEight.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerEight.length; j++) {
							if (headerEight[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10, k + 13 + dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapEight.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapEight.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerNight.length; i++) {
				String h = headerNight[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map mapNight = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 13 + dataListTwo.size() + 10
						+ dataListThree.size() + 10 + dataListFour.size() + 10
						+ dataListFive.size() + 10 + dataListSix.size() + 10
						+ dataListEight.size() + 10);
				for (int i = 0; i < headerNight.length; i++) {
					cell.setCellStyle(cellStylehead);
					String headerTemp = headerNight[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10, rows_max + 12
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapNight.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapNight.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerNight.length; j++) {
							if (headerNight[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10, k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10, (num), (num
								+ cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10
									+ dataListEight.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10
									+ dataListEight.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapNight.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapNight.put(key, null);
						}
					}
				}
			}

			Map mapTen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet
						.createRow((short) k + 13 + dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10
								+ dataListNight.size() + 10);
				for (int i = 0; i < headerTen.length; i++) {
					cell.setCellStyle(cellStyleGreen);
					String headerTemp = headerTen[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10
								+ dataListNight.size() + 10, rows_max + 12
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10
								+ dataListNight.size() + 10, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapTen.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapTen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerTen.length; j++) {
							if (headerTen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数 4:终止列号
						// 参数 1:行号 参数 3:行号 参数 2:起始列号 参数4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10
								+ dataListNight.size() + 10, k + 13
								+ dataListTwo.size() + 10
								+ dataListThree.size() + 10
								+ dataListFour.size() + 10
								+ dataListFive.size() + 10 + dataListSix.size()
								+ 10 + dataListEight.size() + 10
								+ dataListNight.size() + 10, (num),
								(num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10
									+ dataListEight.size() + 10
									+ dataListNight.size() + 10, k + 13
									+ dataListTwo.size() + 10
									+ dataListThree.size() + 10
									+ dataListFour.size() + 10
									+ dataListFive.size() + 10
									+ dataListSix.size() + 10
									+ dataListEight.size() + 10
									+ dataListNight.size() + 10 + rows_max
									- s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!mapTen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapTen.put(key, null);
						}
					}
				}
			}
			for (int d = 0; d < dataList.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataList.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fields.length; c++) {

					Cell contentCell = datarow.createCell(c);

					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fields[c]) != null
							&& dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fields[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fields[c]).toString()));

						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							// contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());

						} else {

							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c])
									.toString());
						}

					} else {
						contentCell.setCellValue(0);
					}
					if (d == dataList.size() - 1) {
						cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleGreen);

					}
					/*
					 * if ( d == dataList.size() - 1 &&
					 * dataMap.get(fields[c]).toString().contains("-") ) {
					 * contentCell.setCellStyle(cellStyleRED); }
					 */

					if (d == 2) {
						contentCell.setCellStyle(cellStylePERCENT);
					}

				}

			}

			for (int d = 0; d < dataListTwo.size(); d++) {

				HashMap<String, Object> dataMap = dataListTwo.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsTwo.length; c++) {
					DataFormat df = wb.createDataFormat();
					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsTwo[c]) != null
							&& dataMap.get(fieldsTwo[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTwo[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTwo[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTwo[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsTwo[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsTwo[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsTwo[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsTwo[c])
									.toString());
						}
						if (c == 7 || c == 10 || c == 12 || c == 14) {
							contentCell.setCellStyle(cellStyleGreen);
							if (dataMap.get(fieldsTwo[c]).toString()
									.contains("-")
									&& d != dataListTwo.size() - 1) {
								contentCell.setCellStyle(cellStyleRED);
							}
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListTwo.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}

				}

			}

			for (int d = 0; d < dataListThree.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataListThree.get(d);

				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsThree.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsThree[c]) != null
							&& dataMap.get(fieldsThree[c]).toString().length() > 0

					) {
						// 判断data是否为数值型
						isNum = dataMap.get(fieldsThree[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsThree[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsThree[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsThree[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsThree[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsThree[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsThree[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsThree[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap
									.get(fieldsThree[c]).toString());
						}
						if (c == 7 || c == 10 || c == 12 || c == 14) {
							contentCell.setCellStyle(cellStyleGreen);
							if (dataMap.get(fieldsThree[c]).toString()
									.contains("-")
									&& d != dataListThree.size() - 1) {
								contentCell.setCellStyle(cellStyleRED);

							}
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListThree.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}

				}

			}

			for (int d = 0; d < dataListFour.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataListFour.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsFour.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsFour[c]) != null
							&& dataMap.get(fieldsFour[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFour[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFour[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFour[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsFour[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsFour[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsFour[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFour[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFour[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap.get(fieldsFour[c])
									.toString());
						}
						if (c == 7 || c == 10 || c == 12 || c == 14) {
							contentCell.setCellStyle(cellStyleGreen);
							if (dataMap.get(fieldsFour[c]).toString()
									.contains("-")
									&& d != dataListFour.size() - 1) {
								contentCell.setCellStyle(cellStyleRED);

							}
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListFour.size() - 1) {
						cellStyleYellow.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleYellow);
					}

				}

			}

			for (int d = 0; d < dataListFive.size(); d++) {

				HashMap<String, Object> dataMap = dataListFive.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10
						+ dataListFour.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsFive.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsFive[c]) != null
							&& dataMap.get(fieldsFive[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFive[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFive[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFive[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsFive[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsFive[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						DataFormat df = wb.createDataFormat(); // 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsFive[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFive[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsFive[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap.get(fieldsFive[c])
									.toString());
						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}

			for (int d = 0; d < dataListSix.size(); d++) {

				HashMap<String, Object> dataMap = dataListSix.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10
						+ dataListFour.size() + 10 + dataListFive.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsSix.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsSix[c]) != null
							&& dataMap.get(fieldsSix[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsSix[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsSix[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsSix[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsSix[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsSix[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						DataFormat df = wb.createDataFormat(); // 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsSix[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsSix[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsSix[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap.get(fieldsSix[c])
									.toString());
						}
						if (c > 1 && c != fieldsSix.length - 1
								&& d != dataListSix.size()) {
							if (dataMap.get(fieldsSix[c - 1]) != null) {
								if (dataMap.get(fieldsSix[c - 1]).toString()
										.contains("%")
										&& dataMap.get(fieldsSix[c]).toString()
												.contains("%")) {

									String a = dataMap.get(fieldsSix[c - 1])
											.toString();
									String b = dataMap.get(fieldsSix[c])
											.toString();
									// 去掉%
									String tempA = a.substring(0,
											a.lastIndexOf("%"));
									String tempB = b.substring(0,
											b.lastIndexOf("%"));
									// 精确表示
									Integer dataA = Integer.parseInt(tempA);
									Integer dataB = Integer.parseInt(tempB);
									// 大于为1，相同为0，小于为-1
									if (dataB.compareTo(dataA) == 0) {
										contentCell
												.setCellStyle(cellStyleYellow);
									} else if (dataB.compareTo(dataA) == 1) {
										contentCell
												.setCellStyle(cellStyleGreen);
									} else if (dataB.compareTo(dataA) == -1) {
										contentCell.setCellStyle(cellStyleRED);
									}
								}
							}

						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}

			for (int d = 0; d < dataListEight.size(); d++) {

				HashMap<String, Object> dataMap = dataListEight.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10
						+ dataListFour.size() + 10 + dataListFive.size() + 10
						+ dataListSix.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsEight.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsEight[c]) != null
							&& dataMap.get(fieldsEight[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsEight[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsEight[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsEight[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsEight[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsEight[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						DataFormat df = wb.createDataFormat(); // 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsEight[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsEight[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsEight[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap
									.get(fieldsEight[c]).toString());
						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}

			for (int d = 0; d < dataListNight.size(); d++) {

				HashMap<String, Object> dataMap = dataListNight.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10
						+ dataListFour.size() + 10 + dataListFive.size() + 10
						+ dataListSix.size() + 10 + dataListEight.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsNight.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsNight[c]) != null
							&& dataMap.get(fieldsNight[c]).toString().length() > 0

					) {
						// 判断data是否为数值型
						isNum = dataMap.get(fieldsNight[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsNight[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsNight[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsNight[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsNight[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						DataFormat df = wb.createDataFormat(); // 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsNight[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsNight[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(
									fieldsNight[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap
									.get(fieldsNight[c]).toString());
						}
						if (c > 1 && c != fieldsNight.length - 1
								&& d != dataListNight.size()) {
							if (dataMap.get(fieldsNight[c - 1]) != null) {
								if (dataMap.get(fieldsNight[c - 1]).toString()
										.contains("%")
										&& dataMap.get(fieldsNight[c])
												.toString().contains("%")) {

									String a = dataMap.get(fieldsNight[c - 1])
											.toString();
									String b = dataMap.get(fieldsNight[c])
											.toString();
									// 去掉%
									String tempA = a.substring(0,
											a.lastIndexOf("%"));
									String tempB = b.substring(0,
											b.lastIndexOf("%"));
									// 精确表示
									Integer dataA = Integer.parseInt(tempA);
									Integer dataB = Integer.parseInt(tempB);
									// 大于为1，相同为0，小于为-1
									if (dataB.compareTo(dataA) == 0) {
										contentCell
												.setCellStyle(cellStyleYellow);
									} else if (dataB.compareTo(dataA) == 1) {
										contentCell
												.setCellStyle(cellStyleGreen);
									} else if (dataB.compareTo(dataA) == -1) {
										contentCell.setCellStyle(cellStyleRED);
									}
								}
							}

						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}

			for (int d = 0; d < dataListTen.size(); d++) {

				HashMap<String, Object> dataMap = dataListTen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 12 + rows_max + 1
						+ dataListTwo.size() + 10 + dataListThree.size() + 10
						+ dataListFour.size() + 10 + dataListFive.size() + 10
						+ dataListSix.size() + 10 + dataListEight.size() + 10
						+ dataListNight.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsTen.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					boolean isGongshiOne = false;
					if (dataMap.get(fieldsTen[c]) != null
							&& dataMap.get(fieldsTen[c]).toString().length() > 0

					) {
						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTen[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTen[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTen[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsTen[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsTen[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						DataFormat df = wb.createDataFormat(); // 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsTen[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTen[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTen[c]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellValue(dataMap.get(fieldsTen[c])
									.toString());
						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void yearDealerSellout(SXSSFWorkbook wb,
			LinkedList<HashMap<String, Object>> dateList, String partyName,
			String beginDateOne, String endDateOne,String tBeginDate,String tEndDate) {

		// 表头数据
		String[] headers = { "REG", "RANK", "DEALER", };

		String[] toYear = beginDateOne.split("-");
		String[] toYearEnd = endDateOne.split("-");
		int laYear = Integer.parseInt(toYear[0].toString()) - 1;

		headers = insert(headers, "YR-" + laYear + "_" + "NO. OF SHOP");
		headers = insert(headers, "YR-" + laYear + "_" + "NO. OF FPS");
		headers = insert(headers, "YR-" + laYear + "_" + "TOTAL QTY.");
		headers = insert(headers, "YR-" + laYear + "_" + "TOTAL AMOUNT");

		headers = insert(headers, "YR-" + toYear[0] + "_" + "NO. OF SHOP");
		headers = insert(headers, "YR-" + toYear[0] + "_" + "NO. OF FPS");

		headers = insert(headers, "YR-" + toYear[0] + "_" + "TTL AIRCON SO_QTY");
		headers = insert(headers, "YR-" + toYear[0] + "_" + "TTL AIRCON SO_AMT");
	
		headers = insert(headers, "BASIC TARGET");

		headers = insert(headers, "ACH.");
		headers = insert(headers, "MARKET SHARE");
		headers = insert(headers, "GROWTH_QTY");
		headers = insert(headers, "GROWTH_AMOUNT");
		headers = insert(headers, laYear + " AVE.SO/FPS_QTY");
		headers = insert(headers, laYear + " AVE.SO/FPS_AMT");
		headers = insert(headers, toYear[0] + " AVE.SO/FPS_QTY");
		headers = insert(headers, toYear[0] + " AVE.SO/FPS_AMT");
		headers = insert(headers, "SELL-OUT EFFICIENCY_QTY");
		headers = insert(headers, "SELL-OUT EFFICIENCY_AMT");

		// 按照对应格式将表头传入
		ArrayList columns = new ArrayList();
		for (int i = 0; i < headers.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headers[i]);
			columnMap.put("field", headers[i]);
			columns.add(columnMap);
		}

		String[] header = new String[columns.size()];
		String[] fields = new String[columns.size()];
		for (int i = 0, l = columns.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columns.get(i);
			header[i] = columnMap.get("header").toString();
			fields[i] = columnMap.get("field").toString();

		}

		String searchStr = null;
		String conditions = "";
		String center = "";
		String country = "";
		String region = "";
		String office = "";

		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if (!WebPageUtil.isHAdmin()) {
			if (request.getParameter("center") != null
					&& !request.getParameter("center").equals("")
					|| request.getParameter("country") != null
					&& !request.getParameter("country").equals("")
					|| request.getParameter("region") != null
					&& !request.getParameter("region").equals("")
					|| request.getParameter("office") != null
					&& !request.getParameter("office").equals("")) {

				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")) {
					center = request.getParameter("center");
					conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
							+ center + "')   ";
				}

				if (request.getParameter("country") != null
						&& !request.getParameter("country").equals("")) {
					country = request.getParameter("country");
					conditions = "  pa.country_id= " + country + "  ";
				}
				if (request.getParameter("region") != null
						&& !request.getParameter("region").equals("")) {
					region = request.getParameter("region");
					conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
							+ region
							+ "'  OR PARTY_ID='"+region+"'))  ";
				}
				if (request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {
					office = request.getParameter("office");
					conditions = "    pa.party_id IN ('" + office + "')  ";
				}

			} else {
				if (null != userPartyIds && !"".equals(userPartyIds)) {
					conditions = "  pa.party_id in (" + userPartyIds + ")  ";
				} else {
					conditions = "  1=2  ";
				}
			}

		} else {
			conditions = " 1=1 ";

		}
		
		if (request.getParameter("level") != null
				&& !request.getParameter("level").equals("")) {
			conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
		}
		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();

		try {
			String beginOne = laYear + "-" + toYear[1] + "-" + toYear[2];
			String endOne = laYear + "-" + toYearEnd[1] + "-" + toYearEnd[2];

			String beginTwo = beginDateOne;
			String endTwo = endDateOne;
			List<HashMap<String, Object>> DealerSelloutLast = excelService
					.selectSelloutByDealerByAc(beginOne, endOne, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			List<HashMap<String, Object>> DealerSelloutInfo = excelService
					.selectSelloutByDealerInfo(searchStr, conditions);

			//conditions += "  AND  pr.head_type_id =1  ";

			List<HashMap<String, Object>> DealerSelloutTo = excelService
					.selectSelloutByDealerByAc(beginTwo, endTwo, searchStr,
							conditions,WebPageUtil.isHQRole(), tBeginDate, tEndDate);

			for (int w = 0; w < DealerSelloutInfo.size(); w++) {
				double ach = 0.0;
				double avg = 0.0;
				double tqavg = 0.0;
				double tsavg = 0.0;
				double lqavg = 0.0;
				double lsavg = 0.0;
				double lq = 0.0;
				int lfps = 0;
				int lshop = 0;
				Double lt = 0.0;
				Double ls = 0.0;
				Double ts = 0.0;
				double tq = 0.0;
				int tfps = 0;
				int tshop = 0;
				Double tt = 0.0;
				double ltq = 0.0;
				HashMap<String, Object> dataMap = new HashMap<String, Object>();

				dataMap.put("REG", DealerSelloutInfo.get(w).get("REG"));
				dataMap.put("DEALER", DealerSelloutInfo.get(w).get("DEALER"));
				// dataMap.put("RANK", w + 1);

				for (int i = 0; i < DealerSelloutTo.size(); i++) {

					if (DealerSelloutInfo.get(w).get("DEALER")
							.equals(DealerSelloutTo.get(i).get("DEALER"))) {

						int row = 7 + i;
						dataMap.put("YR-" + toYear[0] + "_" + "NO. OF SHOP",
								DealerSelloutTo.get(i).get("noOfShops"));
						dataMap.put("YR-" + toYear[0] + "_" + "NO. OF FPS",
								DealerSelloutTo.get(i).get("tvFps"));

						BigDecimal bd = new BigDecimal(DealerSelloutTo.get(i)
								.get("saleQty").toString());
						dataMap.put("YR-" + toYear[0] + "_" + "TTL AIRCON SO_QTY",
								bd.longValue());

						tq = bd.longValue();

						bd = new BigDecimal(DealerSelloutTo.get(i)
								.get("saleSum").toString());
						String am = bd.toPlainString();
						dataMap.put("YR-" + toYear[0] + "_" + "TTL AIRCON SO_AMT",
								am);

						BigDecimal td = new BigDecimal(DealerSelloutTo.get(i)
								.get("targetSum").toString());
						String tm = td.toPlainString();
						dataMap.put("BASIC TARGET", tm);

						

						tshop = Integer.parseInt(DealerSelloutTo.get(i)
								.get("noOfShops").toString());
						tfps = Integer.parseInt(DealerSelloutTo.get(i)
								.get("tvFps").toString());

						ts = Double.parseDouble(am);
						tt = Double.parseDouble(tm);
						if (tt != 0.0) {
							ach = ts / tt * 100;
							long lnum = Math.round(ach);
							dataMap.put("ACH.", lnum + "%");
						}

					

						if (tfps != 0) {
							avg = tq / tfps;
							dataMap.put(toYear[0] + " AVE.SO/FPS_QTY", Math.round(avg));
							tqavg = Math.round(avg);

							avg =  (ts / tfps);
							dataMap.put(toYear[0] + " AVE.SO/FPS_AMT", Math.round(avg));
							tsavg = Math.round(avg);
						}

					}

				}

				for (int j = 0; j < DealerSelloutLast.size(); j++) {

					if (DealerSelloutInfo.get(w).get("DEALER")
							.equals(DealerSelloutLast.get(j).get("DEALER"))) {

						dataMap.put("YR-" + laYear + "_" + "NO. OF SHOP",
								DealerSelloutLast.get(j).get("noOfShops"));
						dataMap.put("YR-" + laYear + "_" + "NO. OF FPS",
								DealerSelloutLast.get(j).get("tvFps"));

						BigDecimal lbd = new BigDecimal(DealerSelloutLast
								.get(j).get("saleQty").toString());

						dataMap.put("YR-" + laYear + "_" + "TOTAL QTY.",
								lbd.longValue());
						lq = lbd.longValue();

						lbd = new BigDecimal(DealerSelloutLast.get(j)
								.get("saleSum").toString());
						String lam = lbd.toPlainString();
						dataMap.put("YR-" + laYear + "_" + "TOTAL AMOUNT", lam);
						lshop = Integer.parseInt(DealerSelloutLast.get(j)
								.get("noOfShops").toString());
						lfps = Integer.parseInt(DealerSelloutLast.get(j)
								.get("tvFps").toString());

						ls = Double.parseDouble(lam);

						if (lfps != 0) {
							avg = lq / lfps;
							dataMap.put(laYear + " AVE.SO/FPS_QTY", Math.round(avg));
							lqavg =  Math.round(avg);

							avg = (ls / lfps);
							dataMap.put(laYear + " AVE.SO/FPS_AMT",  Math.round(avg));
							lsavg =  Math.round(avg);
						}

					}

				}

				if (tq != 0) {
					ltq = tq - lq;
					ach = ltq / tq * 100;
					long lnum = Math.round(ach);
					dataMap.put("GROWTH_QTY", lnum + "%");

				} else if (tq == 0 && lq == 0) {
					dataMap.put("GROWTH_QTY", "100%");
				} else if (tq == 0 && lq != 0) {
					dataMap.put("GROWTH_QTY", "-100%");
				}
				if (ts != 0.0) {
					ltq = ts - ls;
					ach = ltq / ts * 100;
					long lnum = Math.round(ach);
					dataMap.put("GROWTH_AMOUNT", lnum + "%");
				} else if (ts == 0.0 && ls == 0.0) {
					dataMap.put("GROWTH_AMOUNT", "100%");
				} else if (ts == 0.0 && ls != 0.0) {
					dataMap.put("GROWTH_AMOUNT", "-100%");
				}

				ach = (tqavg - lqavg);
				BigDecimal bd = new BigDecimal(ach);
				String am = bd.toPlainString();
				dataMap.put("SELL-OUT EFFICIENCY_QTY", am);

				ach = (tsavg - lsavg);
				bd = new BigDecimal(ach);
				am = bd.toPlainString();
				dataMap.put("SELL-OUT EFFICIENCY_AMT", am);

				dataList.add(dataMap);
			}
			DateUtil.Order(dataList, "GROWTH_AMOUNT");

			for (int i = 0; i < dataList.size(); i++) {
				dataList.get(i).put("RANK", i + 1);
			}
			// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
			Sheet sheet = wb.createSheet("Dealer Sellout");
			sheet.setZoom(3, 4);
			// 创建字体
			Font fontinfo = wb.createFont();
			fontinfo.setFontHeightInPoints((short) 9); // 字体大小
			fontinfo.setFontName("Trebuchet MS");
			Font fonthead = wb.createFont();
			fonthead.setFontHeightInPoints((short) 10);
			fonthead.setFontName("Trebuchet MS");

			// colSplit, rowSplit,leftmostColumn, topRow,
			sheet.createFreezePane(3, 5, 3, 5);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat
					.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
														// hh:mm:ss

			CellStyle cellStylehead = wb.createCellStyle();// 表头样式
			cellStylehead.setFont(fonthead);
			cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			CellStyle cellStyleWHITE = wb.createCellStyle();// 表头样式
			cellStyleWHITE.setFont(fonthead);
			cellStyleWHITE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleWHITE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleWHITE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyleWHITE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleWHITE.setWrapText(true);

			CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fonthead);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fonthead);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleYellow.setWrapText(true);

			CellStyle cellStylePERCENT = wb.createCellStyle();// 表头样式
			cellStylePERCENT.setFont(fonthead);
			cellStylePERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePERCENT.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePERCENT.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT
					.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStylePERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePERCENT.setWrapText(true);

			CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
			cellStyleRED.setFont(fonthead);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);

			CellStyle cellStylePE = wb.createCellStyle();// 表头样式
			cellStylePE.setFont(fonthead);
			cellStylePE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePE.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			cellStylePE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePE.setWrapText(true);

			CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			Row row = sheet.createRow((short) 0);
			// 创建这一行的一列，即创建单元格(CELL)
			Cell cell = row.createCell((short) 0);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("DEALER  SELL-OUT PERFORMANCES");// 标题
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

			CellStyle contextstyle = wb.createCellStyle();
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			contextstyle.setFont(fontinfo);
			DataFormat df = wb.createDataFormat();
			//
			int ce = DealerSelloutInfo.size() + 6;

			row = sheet.createRow((short) 5);
			// 创建这一行的一列，即创建单元格(CELL)
			cell = row.createCell((short) 3);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,D7:D" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,D7:D" + ce + ")");

			cell = row.createCell((short) 4);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,E7:E" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,E7:E" + ce + ")");

			cell = row.createCell((short) 5);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,F7:F" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,F7:F" + ce + ")");

			cell = row.createCell((short) 6);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,G7:G" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,G7:G" + ce + ")");

			cell = row.createCell((short) 7);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,H7:H" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,H7:H" + ce + ")");

			cell = row.createCell((short) 8);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,I7:I" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,I7:I" + ce + ")");

			cell = row.createCell((short) 9);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,J7:J" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,J7:J" + ce + ")");

			cell = row.createCell((short) 10);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,K7:K" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,K7:K" + ce + ")");

			cell = row.createCell((short) 11);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("SUBTOTAL(9,L7:L" + ce + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,L7:L" + ce + ")");

			cell = row.createCell((short) 12);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("TEXT(K6/L6,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT(K6/L6,\"0.00%\")");

			cell = row.createCell((short) 13);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellValue("100%");

			
			cell = row.createCell((short) 14);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("TEXT((J6-F6)/F6,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT((J6-F6)/F6,\"0.00%\")");
			
			
			cell = row.createCell((short) 15);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellValue("TEXT((K6-G6)/G6,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT((K6-G6)/G6,\"0.00%\")");
			
			
			cell = row.createCell((short) 16);
			cell.setCellValue("SUM(F6/E6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(F6/E6)");

			
			
			cell = row.createCell((short) 17);
			cell.setCellValue("SUM(G6/E6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(G6/E6)");
			
			
			cell = row.createCell((short) 18);
			cell.setCellValue("SUM(J6/I6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(J6/I6)");

			cell = row.createCell((short) 19);
			cell.setCellValue("SUM(K6/I6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(K6/I6)");
			

			

			cell = row.createCell((short) 20);
			cell.setCellValue("SUM(S6-Q6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(S6-Q6)");

			cell = row.createCell((short) 21);
			cell.setCellValue("SUM(T6-R6)");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			contextstyle.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUM(T6-R6)");

			
			// 从17行开始放另一个表格的表头

			int rows_max = 0; // 最大的一个项有几个子项

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map map = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 2);
				for (int i = 0; i < header.length; i++) {

					String headerTemp = header[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (i >= 4 && i < 8) {
						cell.setCellStyle(cellStylePERCENT);
					} else if (i >= 8 && i < 16) {
						cell.setCellStyle(cellStyleYellow);
					} else {
						cell.setCellStyle(cellStyleWHITE);
					}
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2,
								rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (map.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (map.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < header.length; j++) {
							if (header[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 2,
								k + 2, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k
									+ 2 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int d = 0; d < dataList.size(); d++) {

				HashMap<String, Object> dataMap = dataList.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 2 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fields.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fields[c]) != null
							&& dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fields[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
								cellStylePERCENT.setDataFormat(df
										.getFormat("#,##0"));
								cellStyleGreen.setDataFormat(df
										.getFormat("#,##0"));
								cellStyleWHITE.setDataFormat(df
										.getFormat("#,##0"));
								cellStylePE
										.setDataFormat(df.getFormat("#,##0"));
								cellStyleRED.setDataFormat(df
										.getFormat("#,##0"));
								cellStyleYellow.setDataFormat(df
										.getFormat("#,##0"));
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fields[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c])
									.toString());
						}
						if (c >= 3 && c < 7) {
							cellStylePERCENT.setDataFormat(df
									.getFormat("#,##0"));
							contentCell.setCellStyle(cellStylePERCENT);
							if (dataMap.get(fields[c]).toString().contains("-")) {
								cellStyleRED.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStyleRED);
							}
						} else if (c >= 7 && c < 15) {
							cellStyleYellow
									.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(cellStyleYellow);
							if (dataMap.get(fields[c]).toString().contains("-")) {
								cellStyleRED.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStyleRED);
							}
						} else if (c > 17 && c <= 19) {
							cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(cellStyleGreen);
							if (dataMap.get(fields[c]).toString().contains("-")) {
								cellStyleRED.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStyleRED);
							}
						} else if (c > 23) {
							cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(cellStyleGreen);
							if (dataMap.get(fields[c]).toString().contains("-")) {
								cellStyleRED.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStyleRED);
							}
						} else {
							cellStyleWHITE.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(cellStyleWHITE);
						}
					} else {
						contentCell.setCellValue("");
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void yearYTDsellout(SXSSFWorkbook wb, String partyName,
			String beginDateOne, String endDateOne) {

		String[] toYear = beginDateOne.split("-");
		int laYear = Integer.parseInt(toYear[0].toString()) - 1;
		String laDays = DateUtil.getLastDayOfMonth(laYear,
				Integer.parseInt(toYear[1]));
		String[] laDay = laDays.split("-");
		int lastYear = Integer.parseInt(toYear[0]) - 1;

		// 表头数据
		String[] headers = { "TCL MONTHLY SELLOUT TREND_Year",
				"TCL MONTHLY SELLOUT TREND_Month",
				"TCL MONTHLY SELLOUT TREND_Jan",
				"TCL MONTHLY SELLOUT TREND_Feb",
				"TCL MONTHLY SELLOUT TREND_Mar",
				"TCL MONTHLY SELLOUT TREND_Apr",
				"TCL MONTHLY SELLOUT TREND_May",
				"TCL MONTHLY SELLOUT TREND_June",
				"TCL MONTHLY SELLOUT TREND_July",
				"TCL MONTHLY SELLOUT TREND_August",
				"TCL MONTHLY SELLOUT TREND_September",
				"TCL MONTHLY SELLOUT TREND_October",
				"TCL MONTHLY SELLOUT TREND_November",
				"TCL MONTHLY SELLOUT TREND_December",
				"TCL MONTHLY SELLOUT TREND_TOTAL"

		};

		// 按照对应格式将表头传入
		ArrayList columns = new ArrayList();
		for (int i = 0; i < headers.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headers[i]);
			columnMap.put("field", headers[i]);
			columns.add(columnMap);
		}

		String[] header = new String[columns.size()];
		String[] fields = new String[columns.size()];
		for (int i = 0, l = columns.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columns.get(i);
			header[i] = columnMap.get("header").toString();
			fields[i] = columnMap.get("field").toString();

		}

		// 表头数据
		String[] headersTwo = {
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_Category",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_MODELS",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_Jan",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_Feb",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_Mar",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_Apr",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_May",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_June",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_July",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_August",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_September",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_October",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_November",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_December",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_TOTAL"

		};

		// 按照对应格式将表头传入
		ArrayList columnsTwo = new ArrayList();
		for (int i = 0; i < headersTwo.length; i++) {
			HashMap<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("header", headersTwo[i]);
			columnMap.put("field", headersTwo[i]);
			columnsTwo.add(columnMap);
		}

		String[] headerTwo = new String[columnsTwo.size()];
		String[] fieldsTwo = new String[columnsTwo.size()];
		for (int i = 0, l = columnsTwo.size(); i < l; i++) {

			HashMap columnMap = (HashMap) columnsTwo.get(i);
			headerTwo[i] = columnMap.get("header").toString();
			fieldsTwo[i] = columnMap.get("field").toString();

		}

		String searchStr = null;
		String conditions = "";
		String center = "";
		String country = "";
		String region = "";
		String office = "";

		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if (!WebPageUtil.isHAdmin()) {
			if (request.getParameter("center") != null
					&& !request.getParameter("center").equals("")
					|| request.getParameter("country") != null
					&& !request.getParameter("country").equals("")
					|| request.getParameter("region") != null
					&& !request.getParameter("region").equals("")
					|| request.getParameter("office") != null
					&& !request.getParameter("office").equals("")) {

				if (request.getParameter("center") != null
						&& !request.getParameter("center").equals("")) {
					center = request.getParameter("center");
					conditions = "   pa.party_id IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='"
							+ center + "')   ";
				}

				if (request.getParameter("country") != null
						&& !request.getParameter("country").equals("")) {
					country = request.getParameter("country");
					conditions = "  pa.country_id= " + country + "  ";
				}
				if (request.getParameter("region") != null
						&& !request.getParameter("region").equals("")) {
					region = request.getParameter("region");
					conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='"
							+ region
							+ "'  OR PARTY_ID='"+region+"'))  ";
				}
				if (request.getParameter("office") != null
						&& !request.getParameter("office").equals("")) {
					office = request.getParameter("office");
					conditions = "    pa.party_id IN ('" + office + "')  ";
				}

			} else {
				if (null != userPartyIds && !"".equals(userPartyIds)) {
					conditions = "  pa.party_id in (" + userPartyIds + ")  ";
				} else {
					conditions = "  1=2  ";
				}
			}

		} else {
			conditions = " 1=1 ";

		}
		
		if (request.getParameter("level") != null
				&& !request.getParameter("level").equals("")) {
			conditions+= "  AND   si.level="+request.getParameter("level")+"  ";
		}
		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
		LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapOne = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> rowMapOne = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> rowMapO = new LinkedHashMap<String, Object>();
		String lastDay = DateUtil.getLastDayOfMonth(
				Integer.parseInt(toYear[0]), 1);
		String[] lastDays = lastDay.split("-");
		String spec = "";
		double one = 0.0;
		double oneData = 0.0;
		double oneAch = 0.0;
		List<LinkedHashMap<String, Object>> sizeDatasTwTotal = null;
		String startDay = "";
		String Month = "";
		try {
			HashMap<String, Object> data = new HashMap<String, Object>();

			List<LinkedHashMap<String, Object>> sizeDatas = null;

			List<LinkedHashMap<String, Object>> sizeLa = excelService
					.selectQtyTotalBySpecTypeByAc(spec, laYear + "-01-01",
							DateUtil.getLastDayOfMonth(laYear, 12), searchStr,
							conditions,WebPageUtil.isHQRole());

			List<LinkedHashMap<String, Object>> sizeTo = excelService
					.selectQtyTotalBySpecTypeByAc(spec, beginDateOne, endDateOne,
							searchStr, conditions,WebPageUtil.isHQRole());

			if (sizeLa.size() > sizeTo.size()) {
				sizeDatas = sizeLa;
			} else if (sizeTo.size() == sizeLa.size()) {
				sizeDatas = sizeLa;
			} else if (sizeTo.size() > sizeLa.size()) {
				sizeDatas = sizeTo;
			}

			for (int j = 1; j <= 13; j++) {
				if (j == 13) {
					lastDay = DateUtil.getLastDayOfMonth(lastYear, 1);
					lastDays = lastDay.split("-");
					startDay = lastYear + "-01-01";
					Month = "Jan";
				} else if (j == 2) {
					lastDay = DateUtil.getLastDayOfMonth(lastYear, 2);
					lastDays = lastDay.split("-");
					startDay = lastYear + "-02-01";
					Month = "Feb";
				} else if (j == 3) {
					lastDay = DateUtil.getLastDayOfMonth(lastYear, 3);
					lastDays = lastDay.split("-");
					startDay = lastYear + "-03-01";
					Month = "Mar";
				} else if (j == 4) {
					lastDay = DateUtil.getLastDayOfMonth(lastYear, 4);
					lastDays = lastDay.split("-");
					startDay = lastYear + "-04-01";
					Month = "Apr";
				} else if (j == 5) {
					lastDay = DateUtil.getLastDayOfMonth(lastYear, 5);
					lastDays = lastDay.split("-");
					startDay = lastYear + "-05-01";
					Month = "May";
				} else if (j == 6) {
					lastDay = DateUtil.getLastDayOfMonth(lastYear, 6);
					lastDays = lastDay.split("-");
					startDay = lastYear + "-06-01";
					Month = "June";
				} else if (j == 7) {
					lastDay = DateUtil.getLastDayOfMonth(lastYear, 7);
					lastDays = lastDay.split("-");
					startDay = lastYear + "-07-01";
					Month = "July";
				} else if (j == 8) {
					lastDay = DateUtil.getLastDayOfMonth(lastYear, 8);
					lastDays = lastDay.split("-");
					startDay = lastYear + "-08-01";
					Month = "August";
				} else if (j == 9) {
					lastDay = DateUtil.getLastDayOfMonth(lastYear, 9);
					lastDays = lastDay.split("-");
					startDay = lastYear + "-09-01";
					Month = "September";
				} else if (j == 10) {
					lastDay = DateUtil.getLastDayOfMonth(lastYear, 10);
					lastDays = lastDay.split("-");
					startDay = lastYear + "-10-01";
					Month = "October";
				} else if (j == 11) {
					lastDay = DateUtil.getLastDayOfMonth(lastYear, 11);
					lastDays = lastDay.split("-");
					startDay = lastYear + "-11-01";
					Month = "November";
				} else if (j == 12) {
					lastDay = DateUtil.getLastDayOfMonth(lastYear, 12);
					lastDays = lastDay.split("-");
					startDay = lastYear + "-12-01";
					Month = "December";
				} else if (j == 1) {
					lastDay = lastYear + "-" + endDateOne.split("-")[1] + "-"
							+ endDateOne.split("-")[2];
					lastDays = lastDay.split("-");
					startDay = lastYear + "-" + beginDateOne.split("-")[1]
							+ "-" + beginDateOne.split("-")[2];
					Month = "TOTAL";
				}
				List<LinkedHashMap<String, Object>> sizeDatasOneData = excelService
						.selectQtyTotalBySpecTypeByAc(spec, startDay, lastDay,
								searchStr, conditions,WebPageUtil.isHQRole());

				for (int z = 0; z < sizeDatasOneData.size(); z++) {
					LinkedHashMap<String, Object> colMap = sizeDatasOneData
							.get(z);
					String key = lastYear + colMap.get("SPEC").toString();
					if (allDataMapOne.get(key) != null) {
						LinkedHashMap<String, Object> rowMap = allDataMapOne
								.get(key);

						BigDecimal bd = new BigDecimal(sizeDatasOneData.get(z)
								.get("quantity").toString());
						String am = bd.toPlainString();
						// 分成12个月份查，查一个放一个
						rowMap.put("TCL MONTHLY SELLOUT TREND_" + Month + "",
								am);

						rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
								+ lastYear);
						rowMap.put("TCL MONTHLY SELLOUT TREND_Month",
								sizeDatasOneData.get(z).get("SPEC").toString());

						allDataMapOne.put(key, rowMap);
					} else {
						LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
						rowList.addLast(colMap);

						LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();

						BigDecimal bd = new BigDecimal(sizeDatasOneData.get(z)
								.get("quantity").toString());
						String am = bd.toPlainString();
						// 分成12个月份查，查一个放一个
						rowMap.put("TCL MONTHLY SELLOUT TREND_" + Month + "",
								am);

						rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
								+ lastYear);
						rowMap.put("TCL MONTHLY SELLOUT TREND_Month",
								sizeDatasOneData.get(z).get("SPEC").toString());

						allDataMapOne.put(key, rowMap);

					}
				}
				sizeDatasTwTotal = excelService.selectSaleTotalBySizeByAc(startDay,
						lastDay, searchStr, conditions,WebPageUtil.isHQRole());
				if (sizeDatasTwTotal.size() == 1) {
					BigDecimal bd = new BigDecimal(sizeDatasTwTotal.get(0)
							.get("quantity").toString());
					String am = bd.toPlainString();

					rowMapO.put("TCL MONTHLY SELLOUT TREND_" + Month + "", am);
					rowMapO.put("TCL MONTHLY SELLOUT TREND_Month", "TOTAL");
				}

			}

			if (sizeLa.size() < sizeTo.size() && sizeLa.size() != 0) {
				for (int i = 0; i < sizeTo.size() - sizeLa.size(); i++) {
					LinkedHashMap<String, Object> rw = new LinkedHashMap<String, Object>();
					allDataMapOne.put("S" + i, rw);
				}
			}

			if (sizeLa.size() == 0) {
				for (int j = 0; j < sizeTo.size(); j++) {
					LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();

					rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
							+ lastYear);
					rowMap.put("TCL MONTHLY SELLOUT TREND_Month", sizeTo.get(j)
							.get("SPEC").toString());
					allDataMapOne.put(lastYear
							+ sizeTo.get(j).get("SPEC").toString(), rowMap);
				}
			}

			allDataMapOne.put("TotalTwo", rowMapO);
			LinkedHashMap<String, Object> rw = new LinkedHashMap<String, Object>();
			allDataMapOne.put("S", rw);
			for (int j = 1; j <= 13; j++) {
				if (j == 13) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 1);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-01-01";
					Month = "Jan";
				} else if (j == 2) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 2);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-02-01";
					Month = "Feb";
				} else if (j == 3) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 3);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-03-01";
					Month = "Mar";
				} else if (j == 4) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 4);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-04-01";
					Month = "Apr";
				} else if (j == 5) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 5);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-05-01";
					Month = "May";
				} else if (j == 6) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 6);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-06-01";
					Month = "June";
				} else if (j == 7) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 7);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-07-01";
					Month = "July";
				} else if (j == 8) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 8);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-08-01";
					Month = "August";
				} else if (j == 9) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 9);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-09-01";
					Month = "September";
				} else if (j == 10) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 10);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-10-01";
					Month = "October";
				} else if (j == 11) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 11);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-11-01";
					Month = "November";
				} else if (j == 12) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 12);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-12-01";
					Month = "December";
				} else if (j == 1) {
					lastDay = endDateOne;
					lastDays = lastDay.split("-");
					startDay = beginDateOne;
					Month = "TOTAL";
				}
				List<LinkedHashMap<String, Object>> sizeDatasOneData = excelService
						.selectQtyTotalBySpecTypeByAc(spec, startDay, lastDay,
								searchStr, conditions,WebPageUtil.isHQRole());

				for (int z = 0; z < sizeDatasOneData.size(); z++) {
					LinkedHashMap<String, Object> colMap = sizeDatasOneData
							.get(z);
					String key = toYear[0] + colMap.get("SPEC").toString();
					if (allDataMapOne.get(key) != null) {
						LinkedHashMap<String, Object> rowMap = allDataMapOne
								.get(key);

						BigDecimal bd = new BigDecimal(sizeDatasOneData.get(z)
								.get("quantity").toString());
						String am = bd.toPlainString();
						// 分成12个月份查，查一个放一个
						rowMap.put("TCL MONTHLY SELLOUT TREND_" + Month + "",
								am);

						rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
								+ toYear[0]);
						rowMap.put("TCL MONTHLY SELLOUT TREND_Month",
								sizeDatasOneData.get(z).get("SPEC").toString());

						allDataMapOne.put(key, rowMap);
					} else {
						LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
						rowList.addLast(colMap);

						LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();

						BigDecimal bd = new BigDecimal(sizeDatasOneData.get(z)
								.get("quantity").toString());
						String am = bd.toPlainString();
						// 分成12个月份查，查一个放一个
						rowMap.put("TCL MONTHLY SELLOUT TREND_" + Month + "",
								am);

						rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
								+ toYear[0]);
						rowMap.put("TCL MONTHLY SELLOUT TREND_Month",
								sizeDatasOneData.get(z).get("SPEC").toString());

						allDataMapOne.put(key, rowMap);

					}
				}
				sizeDatasTwTotal = excelService.selectSaleTotalBySizeByAc(startDay,
						lastDay, searchStr, conditions,WebPageUtil.isHQRole());
				if (sizeDatasTwTotal.size() == 1) {
					BigDecimal bd = new BigDecimal(sizeDatasTwTotal.get(0)
							.get("quantity").toString());
					String am = bd.toPlainString();

					rowMapOne
							.put("TCL MONTHLY SELLOUT TREND_" + Month + "", am);
					rowMapOne.put("TCL MONTHLY SELLOUT TREND_Month", "TOTAL");

				}

			}

			if (sizeTo.size() < sizeLa.size() && sizeTo.size() != 0) {
				for (int i = 0; i < sizeLa.size() - sizeTo.size(); i++) {
					LinkedHashMap<String, Object> r = new LinkedHashMap<String, Object>();
					allDataMapOne.put("I" + i, r);
				}
			}

			if (sizeTo.size() == 0) {
				for (int j = 0; j < sizeLa.size(); j++) {
					LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();

					rowMap.put("TCL MONTHLY SELLOUT TREND_Year", "YR"
							+ lastYear);
					rowMap.put("TCL MONTHLY SELLOUT TREND_Month", sizeLa.get(j)
							.get("SPEC").toString());
					allDataMapOne.put(toYear[0]
							+ sizeLa.get(j).get("SPEC").toString(), rowMap);
				}
			}

			allDataMapOne.put("TotalOne", rowMapOne);
			Set<String> sizeSet = allDataMapOne.keySet();

			Iterator<String> sizeIter = sizeSet.iterator();
			while (sizeIter.hasNext()) {
				String key = sizeIter.next();
				LinkedHashMap<String, Object> rowMap = allDataMapOne.get(key);
				dataList.add(rowMap);

				Set<String> rowSet = rowMap.keySet();
				Iterator<String> rowIter = rowSet.iterator();
			}

			int stRow = 13;
			int enRow = 22;

			if (sizeDatas.size() == 1) {
				stRow = 7;
				enRow = 10;
			}  else if (sizeDatas.size() == 2) {
				stRow = 8;
				enRow = 12;
			}  else if (sizeDatas.size() == 3) {
				stRow = 9;
				enRow = 14;
			} else if (sizeDatas.size() == 4) {
				stRow = 10;
				enRow = 16;
			}else if (sizeDatas.size() == 5) {
				stRow = 11;
				enRow = 18;
			}else if (sizeDatas.size() == 6) {
				stRow = 12;
				enRow = 20;
			}else if (sizeDatas.size() == 7) {
				stRow = 13;
				enRow = 22;
			} else if (sizeDatas.size() == 8) {
				stRow = 14;
				enRow = 24;
			}  
			HashMap<String, Object> dataOne = new HashMap<String, Object>();
			dataOne.put("TCL MONTHLY SELLOUT TREND_Month", "Growth vs. "
					+ lastYear);
			dataOne.put("TCL MONTHLY SELLOUT TREND_Jan", "TEXT((C" + enRow
					+ "-C" + stRow + ")/C" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_Feb", "TEXT((D" + enRow
					+ "-D" + stRow + ")/D" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_Mar", "TEXT((E" + enRow
					+ "-E" + stRow + ")/E" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_Apr", "TEXT((F" + enRow
					+ "-F" + stRow + ")/F" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_May", "TEXT((G" + enRow
					+ "-G" + stRow + ")/G" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_June", "TEXT((H" + enRow
					+ "-H" + stRow + ")/H" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_July", "TEXT((I" + enRow
					+ "-I" + stRow + ")/I" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_August", "TEXT((J" + enRow
					+ "-J" + stRow + ")/J" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_September", "TEXT((K"
					+ enRow + "-K" + stRow + ")/K" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_October", "TEXT((L" + enRow
					+ "-L" + stRow + ")/L" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_November", "TEXT((M" + enRow
					+ "-M" + stRow + ")/M" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_December", "TEXT((N" + enRow
					+ "-N" + stRow + ")/N" + enRow + ",\"0.00%\")");
			dataOne.put("TCL MONTHLY SELLOUT TREND_TOTAL", "TEXT((O" + enRow
					+ "-O" + stRow + ")/O" + enRow + ",\"0.00%\")");
			dataList.add(dataOne);

			LinkedHashMap<String, LinkedHashMap<String, Object>> allDataMapTwo = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
			LinkedHashMap<String, Object> rowMapTwo = new LinkedHashMap<String, Object>();

			LinkedList<LinkedHashMap<String, Object>> dataListTwo = new LinkedList<LinkedHashMap<String, Object>>();

			long start = System.currentTimeMillis();
			for (int j = 1; j <= 13; j++) {
				if (j == 13) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 1);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-01-01";
					Month = "Jan";
				} else if (j == 2) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 2);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-02-01";
					Month = "Feb";
				} else if (j == 3) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 3);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-03-01";
					Month = "Mar";
				} else if (j == 4) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 4);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-04-01";
					Month = "Apr";
				} else if (j == 5) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 5);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-05-01";
					Month = "May";
				} else if (j == 6) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 6);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-06-01";
					Month = "June";
				} else if (j == 7) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 7);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-07-01";
					Month = "July";
				} else if (j == 8) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 8);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-08-01";
					Month = "August";
				} else if (j == 9) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 9);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-09-01";
					Month = "September";
				} else if (j == 10) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 10);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-10-01";
					Month = "October";
				} else if (j == 11) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 11);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-11-01";
					Month = "November";
				} else if (j == 12) {
					lastDay = DateUtil.getLastDayOfMonth(
							Integer.parseInt(toYear[0]), 12);
					lastDays = lastDay.split("-");
					startDay = toYear[0] + "-12-01";
					Month = "December";
				} else if (j == 1) {
					lastDay = endDateOne;
					lastDays = lastDay.split("-");
					startDay = beginDateOne;
					Month = "TOTAL";
				}

				List<LinkedHashMap<String, Object>> selectYearDataBySpecOne = excelService
						.selectQtyTotalBySpecYearByAc(spec, startDay, lastDay,
								searchStr, conditions,WebPageUtil.isHQRole());

				for (int z = 0; z < selectYearDataBySpecOne.size(); z++) {
					LinkedHashMap<String, Object> colMap = selectYearDataBySpecOne
							.get(z);
					String key = colMap.get("model").toString();
					if (allDataMapTwo.get(key) != null) {
						LinkedHashMap<String, Object> rowMap = allDataMapTwo
								.get(key);

						rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
								+ "_Category", selectYearDataBySpecOne.get(z)
								.get("SPEC"));

						rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
								+ "_MODELS", selectYearDataBySpecOne.get(z)
								.get("model"));

						BigDecimal bd = new BigDecimal(selectYearDataBySpecOne
								.get(z).get("quantity").toString());

						String am = bd.toPlainString();
						rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
								+ "_" + Month + "", am);

						allDataMapTwo.put(key, rowMap);
					} else {
						LinkedList<HashMap<String, Object>> rowList = new LinkedList<HashMap<String, Object>>();
						rowList.addLast(colMap);

						LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();
						rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
								+ "_Category", selectYearDataBySpecOne.get(z)
								.get("SPEC"));

						rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
								+ "_MODELS", selectYearDataBySpecOne.get(z)
								.get("model"));

						BigDecimal bd = new BigDecimal(selectYearDataBySpecOne
								.get(z).get("quantity").toString());
						String am = bd.toPlainString();
						rowMap.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
								+ "_" + Month + "", am);
						allDataMapTwo.put(key, rowMap);

					}
				}
				List<HashMap<String, Object>> sizeDatasOneTotal = excelService
						.selectQtyTotalBySpecTotalYearByAc(startDay, lastDay,
								searchStr, conditions,WebPageUtil.isHQRole());
				for (int s = 0; s < sizeDatasOneTotal.size(); s++) {
					HashMap<String, Object> colMap = sizeDatasOneTotal.get(s);
					rowMapTwo.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
							+ "_Category", "GRAND TOTAL");

					BigDecimal bd = new BigDecimal(sizeDatasOneTotal.get(0)
							.get("quantity").toString());
					String am = bd.toPlainString();

					rowMapTwo.put("SELL-OUT TREND PER MODEL YR-" + toYear[0]
							+ "_" + Month + "", am);

				}

			}
			allDataMapTwo.put("TotalTwo", rowMapTwo);

			Set<String> sizeSetTen = allDataMapTwo.keySet();

			Iterator<String> sizeIterTen = sizeSetTen.iterator();
			LinkedHashMap<String, Object> totalMapTen = new LinkedHashMap<String, Object>();
			while (sizeIterTen.hasNext()) {
				String key = sizeIterTen.next();
				LinkedHashMap<String, Object> rowMap = allDataMapTwo.get(key);
				dataListTwo.add(rowMap);

				Set<String> rowSet = rowMap.keySet();
				Iterator<String> rowIter = rowSet.iterator();
			}

			// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
			Sheet sheet = wb.createSheet(toYear[0] + " YTD sellout");
			sheet.setZoom(3, 4);
			// 创建字体
			Font fontinfo = wb.createFont();
			fontinfo.setFontHeightInPoints((short) 11); // 字体大小
			fontinfo.setFontName("Trebuchet MS");
			Font fonthead = wb.createFont();
			fonthead.setFontHeightInPoints((short) 12);
			fonthead.setFontName("Trebuchet MS");

			// colSplit, rowSplit,leftmostColumn, topRow,
			sheet.createFreezePane(3, 5, 3, 5);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat
					.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
														// hh:mm:ss

			CellStyle cellStylehead = wb.createCellStyle();// 表头样式
			cellStylehead.setFont(fonthead);
			cellStylehead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylehead.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylehead.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylehead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			cellStylehead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			CellStyle cellStyleWHITE = wb.createCellStyle();// 表头样式
			cellStyleWHITE.setFont(fonthead);
			cellStyleWHITE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleWHITE.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleWHITE.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleWHITE.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleWHITE.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyleWHITE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleWHITE.setWrapText(true);

			CellStyle cellStyleGreen = wb.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fonthead);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleRED = wb.createCellStyle();// 表头样式
			cellStyleRED.setFont(fonthead);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);
			CellStyle cellStyleYellow = wb.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fonthead);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleYellow.setWrapText(true);

			CellStyle cellStylePERCENT = wb.createCellStyle();// 表头样式
			cellStylePERCENT.setFont(fonthead);
			cellStylePERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylePERCENT.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStylePERCENT.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStylePERCENT.setTopBorderColor(HSSFColor.BLACK.index);
			cellStylePERCENT
					.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStylePERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStylePERCENT.setWrapText(true);

			CellStyle cellStyle = wb.createCellStyle();// 数据单元样式
			cellStyle.setWrapText(true);// 自动换行
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			Row row = sheet.createRow((short) 0);
			// 创建这一行的一列，即创建单元格(CELL)
			Cell cell = row.createCell((short) 0);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("TCL " + partyName);// 标题

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			row = sheet.createRow((short) 1);
			// 创建这一行的一列，即创建单元格(CELL)
			cell = row.createCell((short) 1);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("CUMULATIVE SELLOUT");// 标题

			// 开始创建表头
			// int col = header.length;
			// 创建第一行
			row = sheet.createRow((short) 2);
			// 创建这一行的一列，即创建单元格(CELL)
			cell = row.createCell((short) 1);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); 3.2版本以上已经自动处理编码了
			cell.setCellStyle(cellStylename);
			cell.setCellValue("YTD- " + toYear[0]);// 标题

			CellStyle contextstyle = wb.createCellStyle();
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			contextstyle.setFont(fontinfo);
			// 从17行开始放另一个表格的表头

			int rows_max = 0; // 最大的一个项有几个子项

			for (int i = 0; i < header.length; i++) {
				String h = header[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}
			Map map = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + 3);
				for (int i = 0; i < header.length; i++) {
					cell.setCellStyle(cellStyleWHITE);
					String headerTemp = header[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(3,
								rows_max + 2, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (map.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (map.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < header.length; j++) {
							if (header[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k + 3,
								k + 3, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 3, k
									+ 3 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int i = 0; i < headerTwo.length; i++) {
				String h = headerTwo[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapTwo = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k + dataList.size() + 15);
				for (int i = 0; i < headerTwo.length; i++) {
					cell.setCellStyle(cellStyleWHITE);
					String headerTemp = headerTwo[i];
					String[] s = headerTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					//
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(dataList
								.size() + 15, rows_max + dataList.size() + 14,
								(num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						int cols = 0;
						if (mapTwo.containsKey(headerTemp)) {
							continue;
						}
						for (int d = 0; d <= k; d++) {
							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}
						if (mapTwo.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerTwo.length; j++) {
							if (headerTwo[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k
								+ dataList.size() + 15, k + dataList.size()
								+ 15, (num), (num + cols - 1)));

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k
									+ dataList.size() + 15, k + dataList.size()
									+ 15 + rows_max - s.length, num, num));
						}

					}
					if (s.length > k) {
						if (!map.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							map.put(key, null);
						}
					}
				}
			}

			for (int d = 0; d < dataList.size(); d++) {
				DataFormat df = wb.createDataFormat();

				HashMap<String, Object> dataMap = dataList.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 2 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fields.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fields[c]) != null
							&& dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fields[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fields[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c])
									.toString());

						}

						else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c])
									.toString());
						}
						if (sizeDatas.size() == 7) {
							if (d == 7 || d == 16) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 8) {
							if (d == 8 || d == 18) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 6) {
							if (d == 6 || d == 14) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 5) {
							if (d == 5 || d == 12) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 4) {
							if (d == 4 || d == 10) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 3) {
							if (d == 3 || d == 8) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 2) {
							if (d == 2 || d == 6) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						} else if (sizeDatas.size() == 1) {
							if (d == 1 || d == 4) {
								cellStylehead.setDataFormat(df
										.getFormat("#,##0"));
								contentCell.setCellStyle(cellStylehead);
							}
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataList.size() - 1) {
						cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleGreen);

					}

				}
				if (sizeDatas.size() == 7) {
					sheet.addMergedRegion(new CellRangeAddress(5, 12, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(14, 21, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(13, 13, 0, 15));

				} else if (sizeDatas.size() == 8) {
					sheet.addMergedRegion(new CellRangeAddress(5, 13, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(15, 22, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(14, 14, 0, 15));
				} else if (sizeDatas.size() == 6) {
					sheet.addMergedRegion(new CellRangeAddress(5, 11, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(13, 20, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(12, 12, 0, 15));
				} else if (sizeDatas.size() == 5) {
					sheet.addMergedRegion(new CellRangeAddress(5, 10, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(12, 19, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(11, 11, 0, 15));
				} else if (sizeDatas.size() == 4) {
					sheet.addMergedRegion(new CellRangeAddress(5, 9, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(11, 18, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(10, 10, 0, 15));
				} else if (sizeDatas.size() == 3) {
					sheet.addMergedRegion(new CellRangeAddress(5, 8, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(10, 17, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(9, 9, 0, 15));
				} else if (sizeDatas.size() == 2) {
					sheet.addMergedRegion(new CellRangeAddress(5, 7, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(9, 16, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 15));
				} else if (sizeDatas.size() == 1) {
					sheet.addMergedRegion(new CellRangeAddress(5, 6, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(8, 15, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 15));
				}

			}

			for (int d = 0; d < dataListTwo.size(); d++) {
				DataFormat df = wb.createDataFormat();
				HashMap<String, Object> dataMap = dataListTwo.get(d);

				// 创建一行
				Row datarow = sheet.createRow(d + dataList.size() + 14
						+ rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，
				Cell scell = datarow.createCell((short) 0);

				scell.setCellType(Cell.CELL_TYPE_NUMERIC);

				for (int c = 0; c < fieldsTwo.length; c++) {

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsTwo[c]) != null
							&& dataMap.get(fieldsTwo[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTwo[c]).toString()
								.matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTwo[c]).toString()
								.matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTwo[c]).toString()
								.contains("%");
						isGongshi = dataMap.get(fieldsTwo[c]).toString()
								.contains("SUM");
						isGongshiOne = dataMap.get(fieldsTwo[c]).toString()
								.contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						// 此处设置数据格式
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df
										.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap
									.get(fieldsTwo[c]).toString()));
						} else if (isGongshi) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap
									.get(fieldsTwo[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsTwo[c])
									.toString());
						}

						if (d != dataListTwo.size() - 1) {
							HashMap<String, Object> dataMapTwo = dataListTwo
									.get(d + 1);
							if (dataMap.get(fieldsTwo[c]).equals(
									dataMapTwo.get(fieldsTwo[c]))) {
								sheet.addMergedRegion(new CellRangeAddress(d
										+ dataList.size() + 14 + rows_max + 1,
										d + dataList.size() + 14 + rows_max + 1
												+ 1, 0, 0));

							}
						}

					} else {
						contentCell.setCellValue("");
					}
					if (d == dataListTwo.size() - 1) {
						cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(cellStyleGreen);
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String args[]) {
		double num = 121212.5212;
		System.out.print("请输入一个浮点数：");

		long lnum = Math.round(num);
		System.out.println(num + "四舍五入得到长整数：" + lnum);
	}

}