package cn.tcl.platform.BDExcel.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.contrib.RegionUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.BDExcel.dao.IBDExcelDao;
import cn.tcl.platform.BDExcel.dao.IBDSCSoTableDao;
import cn.tcl.platform.BDExcel.service.IBDExcelService;
import cn.tcl.platform.BDExcel.dao.IBDExcelDao;
import cn.tcl.platform.BDExcel.vo.BDExcel;
import cn.tcl.platform.BDExcel.vo.BDSCImportExcel;
import cn.tcl.platform.excel.actions.DateUtil;
import cn.tcl.platform.excel.service.IExcelService;
import cn.tcl.platform.excel.vo.Excel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("BDExcelService")
public class BDExcelServiceImpl extends BaseAction implements IBDExcelService {
	@Autowired
	private IBDExcelDao excelDao;
	@Autowired
	private IBDSCSoTableDao soTableDao;
	

	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdw = new SimpleDateFormat("E");
	static SimpleDateFormat formatEn = new SimpleDateFormat("MMMM.dd,yyyy", Locale.ENGLISH);
	static SimpleDateFormat monthEn = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
	static SimpleDateFormat monthCh = new SimpleDateFormat("MM yyyy");

	static SimpleDateFormat mm = new SimpleDateFormat("MMMM", Locale.ENGLISH);
	static SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.ENGLISH);
	@Autowired
	@Qualifier("excelService")
	private IExcelService excelService;

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

	@Override
	public void commonByHq(SXSSFWorkbook wb, LinkedList<HashMap<String, Object>> list, String wbName, String tBeginDate,
			String tEndDate, String searchStr, String conditions) {

		try {
			String user = "";
			if (WebPageUtil.getLoginedUserId() != null) {
				user = WebPageUtil.getLoginedUserId();
			}

			String sheetName = "";

			String beginDate = tBeginDate;
			String endDate = tEndDate;
			String yearBegin = tBeginDate.split("-")[0];
			String yearEnd = tEndDate.split("-")[0];
			String qHead=BDDateUtil.getQ(Integer.parseInt(tBeginDate.split("-")[1]) )+" "+yearBegin;
			String head=monthEn.format(monthCh.parse(tBeginDate.split("-")[1]+" "+yearBegin));
			sheetName = head ;
			// 表头数据
			String[] headers = { "SELL-OUT INFORMATION SHEET_REG.", "SELL-OUT INFORMATION SHEET_Country.",
					"SELL-OUT INFORMATION SHEET_TYPE", "Admin Name", "DATE OF Upload",
					"TV SELL-OUT and TARGET_TTL TV SO_QTY", "TV SELL-OUT and TARGET_TTL TV SO_ASP",
					"TV SELL-OUT and TARGET_"+head+" TARGET", "TV SELL-OUT and TARGET_"+head+" Ach.",
					"TV SELL-OUT and TARGET_"+qHead+" Qty", 
					"TV SELL-OUT and TARGET_"+qHead+" TARGET", "TV SELL-OUT and TARGET_Quarter Ach.",
					"TV SELL-OUT and TARGET_Year Qty",
					"TV SELL-OUT and TARGET_Year TARGET", "TV SELL-OUT and TARGET_Year Ach." };
			String[] headersBASIC = {};
			String[] headersDIGITAL = {};
			String[] headersINTERNET = {};
			String[] headersQUHD = {};
			String[] headersSMART = {};
			String[] headersUHD = {};
			String[] headersCURVE = {};
			// 查询机型销售数据
			List<HashMap<String, Object>> modelMapList = excelDao.selectModelByHead(beginDate, endDate, searchStr,
					conditions, WebPageUtil.isHQRole());
			// 将查出来的机型销售数据放入表头，形成三级标题
			int modelSize = 0;
			for (int i = 0; i < modelMapList.size(); i++) {
				BigDecimal bd = new BigDecimal(modelMapList.get(i).get("price").toString());
				String am = bd.toPlainString();
				double shop = Double.parseDouble(am);
				double price = shop / Integer.parseInt(modelMapList.get(i).get("country").toString());

				long lnum = Math.round(price);
				String m = "";

				if (modelMapList.get(i).get("spec").toString().contains("BASIC")) {
					m = "BASIC LED" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					modelSize++;
					headersBASIC = insert(headersBASIC, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("DIGITAL")) {
					m = "DIGITAL BASIC" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersDIGITAL = insert(headersDIGITAL, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("INTERNET")) {
					m = "DIGITAL INTERNET" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersINTERNET = insert(headersINTERNET, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("QUHD")) {
					m = "(QUHD) TV" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersQUHD = insert(headersQUHD, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("SMART")) {
					m = "SMART TV" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersSMART = insert(headersSMART, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("UHD")) {
					m = "UHD TV" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersUHD = insert(headersUHD, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("CURVE")) {
					m = "CURVE TV" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersCURVE = insert(headersCURVE, m);
				}

			}

			headersBASIC = insert(headersBASIC, "LED SUB-TOTAL_" + "QTY");
			headersBASIC = insert(headersBASIC, "LED SUB-TOTAL_" + "AMOUNT");
			headersDIGITAL = insert(headersDIGITAL, "DIGITAL SUB-TOTAL_" + "QTY");
			headersDIGITAL = insert(headersDIGITAL, "DIGITAL SUB-TOTAL_" + "AMOUNT");
			headersINTERNET = insert(headersINTERNET, "INTERNET SUB-TOTAL_" + "QTY");
			headersINTERNET = insert(headersINTERNET, "INTERNET SUB-TOTAL_" + "AMOUNT");
			headersQUHD = insert(headersQUHD, "(QUHD) SUB-TOTAL_" + "QTY");
			headersQUHD = insert(headersQUHD, "(QUHD) SUB-TOTAL_" + "AMOUNT");
			headersSMART = insert(headersSMART, "SMART SUB-TOTAL_" + "QTY");
			headersSMART = insert(headersSMART, "SMART SUB-TOTAL_" + "AMOUNT");
			headersUHD = insert(headersUHD, "UHD SUB-TOTAL_" + "QTY");
			headersUHD = insert(headersUHD, "UHD SUB-TOTAL_" + "AMOUNT");
			headersCURVE = insert(headersCURVE, "CURVE SUB-TOTAL_" + "QTY");
			headersCURVE = insert(headersCURVE, "CURVE SUB-TOTAL_" + "AMOUNT");

			headers = BDDateUtil.list(headers, headersBASIC);
			headers = BDDateUtil.list(headers, headersDIGITAL);
			headers = BDDateUtil.list(headers, headersINTERNET);
			headers = BDDateUtil.list(headers, headersQUHD);

			headers = BDDateUtil.list(headers, headersSMART);
			headers = BDDateUtil.list(headers, headersUHD);
			headers = BDDateUtil.list(headers, headersCURVE);

			HashMap<String, ArrayList<HashMap<String, Object>>> priceMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelMapList.size(); m++) {
				if (priceMap.get(modelMapList.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelMapList.get(m));
					priceMap.put(modelMapList.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = priceMap
							.get(modelMapList.get(m).get("model").toString());
					modelList.add(modelMapList.get(m));
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
			
			String[] headersTwo = {
					"All Products_REG.", 	"All Products_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersTwo = insert(headersTwo,getMonth(i+"")+ "_Target");
				headersTwo = insert(headersTwo,getMonth(i+"")+ "_Achieved");
				headersTwo = insert(headersTwo,getMonth(i+"")+ "_Rate%");
			}
			
			ArrayList columnsTwo = new ArrayList();
			for (int i = 0; i < headersTwo.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersTwo[i]);
				columnMap.put("field", headersTwo[i]);
				columnsTwo.add(columnMap);
			}
			
			
			String[] headersThree = {
					"XCP UHD_REG.", 	"XCP UHD_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersThree = insert(headersThree,getMonth(i+"")+ "_Target");
				headersThree = insert(headersThree,getMonth(i+"")+ "_Achieved");
				headersThree = insert(headersThree,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsThree = new ArrayList();
			for (int i = 0; i < headersThree.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersThree[i]);
				columnMap.put("field", headersThree[i]);
				columnsThree.add(columnMap);
			}
			String[] headersFour = {
					"CX UHD_REG.", 	"CX UHD_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersFour = insert(headersFour,getMonth(i+"")+ "_Target");
				headersFour = insert(headersFour,getMonth(i+"")+ "_Achieved");
				headersFour = insert(headersFour,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsFour = new ArrayList();
			for (int i = 0; i < headersFour.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersFour[i]);
				columnMap.put("field", headersFour[i]);
				columnsFour.add(columnMap);
			}
			
			
			String[] headersFive = {
					"P UHD_REG.", 	"P UHD_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersFive = insert(headersFive,getMonth(i+"")+ "_Target");
				headersFive = insert(headersFive,getMonth(i+"")+ "_Achieved");
				headersFive = insert(headersFive,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsFive = new ArrayList();
			for (int i = 0; i < headersFive.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersFive[i]);
				columnMap.put("field", headersFive[i]);
				columnsFive.add(columnMap);
			}
			String[] headersSix = {
					"65\"↑ UHD_REG.", 	"65\"↑ UHD_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersSix = insert(headersSix,getMonth(i+"")+ "_Target");
				headersSix = insert(headersSix,getMonth(i+"")+ "_Achieved");
				headersSix = insert(headersSix,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsSix = new ArrayList();
			for (int i = 0; i < headersSix.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersSix[i]);
				columnMap.put("field", headersSix[i]);
				columnsSix.add(columnMap);
			}
			
			
			String[] headersSeven = {
					"X2X3 UHD_REG.", 	"X2X3 UHD_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersSeven = insert(headersSeven,getMonth(i+"")+ "_Target");
				headersSeven = insert(headersSeven,getMonth(i+"")+ "_Achieved");
				headersSeven = insert(headersSeven,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsSeven = new ArrayList();
			for (int i = 0; i < headersSeven.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersSeven[i]);
				columnMap.put("field", headersSeven[i]);
				columnsSeven.add(columnMap);
			}
			
			
			
			String[] headersEight = {
					"C2C2L UHD_REG.", 	"C2C2L UHD_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersEight = insert(headersEight,getMonth(i+"")+ "_Target");
				headersEight = insert(headersEight,getMonth(i+"")+ "_Achieved");
				headersEight = insert(headersEight,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsEight = new ArrayList();
			for (int i = 0; i < headersEight.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersEight[i]);
				columnMap.put("field", headersEight[i]);
				columnsEight.add(columnMap);
			}
			
			
			
			String[] headersNight = {
					"C4C6 UHD_REG.", 	"C4C6 UHD_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersNight = insert(headersNight,getMonth(i+"")+ "_Target");
				headersNight = insert(headersNight,getMonth(i+"")+ "_Achieved");
				headersNight = insert(headersNight,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsNight = new ArrayList();
			for (int i = 0; i < headersNight.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersNight[i]);
				columnMap.put("field", headersNight[i]);
				columnsNight.add(columnMap);
			}
			
			
			
			String[] headersTen = {
					"P2P21 UHD_REG.", 	"P2P21 UHD_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersTen = insert(headersTen,getMonth(i+"")+ "_Target");
				headersTen = insert(headersTen,getMonth(i+"")+ "_Achieved");
				headersTen = insert(headersTen,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsTen = new ArrayList();
			for (int i = 0; i < headersTen.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersTen[i]);
				columnMap.put("field", headersTen[i]);
				columnsTen.add(columnMap);
			}
			
			
			
			
			String[] headersEleven = {
					"P6 UHD_REG.", 	"P6 UHD_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersEleven = insert(headersEleven,getMonth(i+"")+ "_Target");
				headersEleven = insert(headersEleven,getMonth(i+"")+ "_Achieved");
				headersEleven = insert(headersEleven,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsEleven = new ArrayList();
			for (int i = 0; i < headersEleven.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersEleven[i]);
				columnMap.put("field", headersEleven[i]);
				columnsEleven.add(columnMap);
			}
			
			String[] headerEleven = new String[columnsEleven.size()];
			String[] fieldsEleven = new String[columnsEleven.size()];
			for (int i = 0, l = columnsEleven.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsEleven.get(i);
				headerEleven[i] = columnMap.get("header").toString();
				fieldsEleven[i] = columnMap.get("field").toString();

			}
			
			
			
			
			
			String[] headersTwelve = {
					"P62 UHD_REG.", 	"P62 UHD_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersTwelve = insert(headersTwelve,getMonth(i+"")+ "_Target");
				headersTwelve = insert(headersTwelve,getMonth(i+"")+ "_Achieved");
				headersTwelve = insert(headersTwelve,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsTwelve = new ArrayList();
			for (int i = 0; i < headersTwelve.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersTwelve[i]);
				columnMap.put("field", headersTwelve[i]);
				columnsTwelve.add(columnMap);
			}
			
			String[] headerTwelve = new String[columnsTwelve.size()];
			String[] fieldsTwelve = new String[columnsTwelve.size()];
			for (int i = 0, l = columnsTwelve.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsTwelve.get(i);
				headerTwelve[i] = columnMap.get("header").toString();
				fieldsTwelve[i] = columnMap.get("field").toString();

			}
			
			
			
			String[] headersThirteen = {
					"P65 UHD_REG.", 	"P65 UHD_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersThirteen = insert(headersThirteen,getMonth(i+"")+ "_Target");
				headersThirteen = insert(headersThirteen,getMonth(i+"")+ "_Achieved");
				headersThirteen = insert(headersThirteen,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsThirteen = new ArrayList();
			for (int i = 0; i < headersThirteen.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersThirteen[i]);
				columnMap.put("field", headersThirteen[i]);
				columnsThirteen.add(columnMap);
			}
			
			String[] headerThirteen = new String[columnsThirteen.size()];
			String[] fieldsThirteen = new String[columnsThirteen.size()];
			for (int i = 0, l = columnsThirteen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsThirteen.get(i);
				headerThirteen[i] = columnMap.get("header").toString();
				fieldsThirteen[i] = columnMap.get("field").toString();

			}
			
			
			
			String[] headersFourteen = {
					"P5_REG.", 	"P5_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersFourteen = insert(headersFourteen,getMonth(i+"")+ "_Target");
				headersFourteen = insert(headersFourteen,getMonth(i+"")+ "_Achieved");
				headersFourteen = insert(headersFourteen,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsFourteen = new ArrayList();
			for (int i = 0; i < headersFourteen.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersFourteen[i]);
				columnMap.put("field", headersFourteen[i]);
				columnsFourteen.add(columnMap);
			}
			
			String[] headerFourteen = new String[columnsFourteen.size()];
			String[] fieldsFourteen = new String[columnsFourteen.size()];
			for (int i = 0, l = columnsFourteen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsFourteen.get(i);
				headerFourteen[i] = columnMap.get("header").toString();
				fieldsFourteen[i] = columnMap.get("field").toString();

			}
			
			
			

			
			String[] headersFifteen = {
					"P3_REG.", 	"P3_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersFifteen = insert(headersFifteen,getMonth(i+"")+ "_Target");
				headersFifteen = insert(headersFifteen,getMonth(i+"")+ "_Achieved");
				headersFifteen = insert(headersFifteen,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsFifteen = new ArrayList();
			for (int i = 0; i < headersFifteen.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersFifteen[i]);
				columnMap.put("field", headersFifteen[i]);
				columnsFifteen.add(columnMap);
			}
			
			String[] headerFifteen = new String[columnsFifteen.size()];
			String[] fieldsFifteen = new String[columnsFifteen.size()];
			for (int i = 0, l = columnsFifteen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsFifteen.get(i);
				headerFifteen[i] = columnMap.get("header").toString();
				fieldsFifteen[i] = columnMap.get("field").toString();

			}
			
			
			String[] headersSixteen = {
					"S FHD Smart_REG.", 	"S FHD Smart_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersSixteen = insert(headersSixteen,getMonth(i+"")+ "_Target");
				headersSixteen = insert(headersSixteen,getMonth(i+"")+ "_Achieved");
				headersSixteen = insert(headersSixteen,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsSixteen = new ArrayList();
			for (int i = 0; i < headersSixteen.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersSixteen[i]);
				columnMap.put("field", headersSixteen[i]);
				columnsSixteen.add(columnMap);
			}
			
			String[] headerSixteen = new String[columnsSixteen.size()];
			String[] fieldsSixteen = new String[columnsSixteen.size()];
			for (int i = 0, l = columnsSixteen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsSixteen.get(i);
				headerSixteen[i] = columnMap.get("header").toString();
				fieldsSixteen[i] = columnMap.get("field").toString();

			}
			
			
			
			String[] headersSeventeen = {
					"S FHD Android_REG.", 	"S FHD Android_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersSeventeen = insert(headersSeventeen,getMonth(i+"")+ "_Target");
				headersSeventeen = insert(headersSeventeen,getMonth(i+"")+ "_Achieved");
				headersSeventeen = insert(headersSeventeen,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsSeventeen = new ArrayList();
			for (int i = 0; i < headersSeventeen.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersSeventeen[i]);
				columnMap.put("field", headersSeventeen[i]);
				columnsSeventeen.add(columnMap);
			}
			
			String[] headerSeventeen = new String[columnsSeventeen.size()];
			String[] fieldsSeventeen = new String[columnsSeventeen.size()];
			for (int i = 0, l = columnsSeventeen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsSeventeen.get(i);
				headerSeventeen[i] = columnMap.get("header").toString();
				fieldsSeventeen[i] = columnMap.get("field").toString();

			}
			
			
			
			String[] headersEightteen = {
					"DTV_REG.", 	"DTV_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersEightteen = insert(headersEightteen,getMonth(i+"")+ "_Target");
				headersEightteen = insert(headersEightteen,getMonth(i+"")+ "_Achieved");
				headersEightteen = insert(headersEightteen,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsEightteen = new ArrayList();
			for (int i = 0; i < headersEightteen.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersEightteen[i]);
				columnMap.put("field", headersEightteen[i]);
				columnsEightteen.add(columnMap);
			}
			
			String[] headerEightteen = new String[columnsEightteen.size()];
			String[] fieldsEightteen = new String[columnsEightteen.size()];
			for (int i = 0, l = columnsEightteen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsEightteen.get(i);
				headerEightteen[i] = columnMap.get("header").toString();
				fieldsEightteen[i] = columnMap.get("field").toString();

			}
			
			
			
			String[] headersNineteen = {
					"ATV_REG.", 	"ATV_Country.", 
					yearBegin +" full year_Target", 
					yearBegin +" full year_Achieved",
					yearBegin +" full year_Rate%",
					yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
					yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
					yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
					yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
					 };
			for (int i = 1; i <=12; i++) {
				headersNineteen = insert(headersNineteen,getMonth(i+"")+ "_Target");
				headersNineteen = insert(headersNineteen,getMonth(i+"")+ "_Achieved");
				headersNineteen = insert(headersNineteen,getMonth(i+"")+ "_Rate%");
			}
			ArrayList columnsNineteen = new ArrayList();
			for (int i = 0; i < headersNineteen.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headersNineteen[i]);
				columnMap.put("field", headersNineteen[i]);
				columnsNineteen.add(columnMap);
			}
			
			String[] headerNineteen = new String[columnsNineteen.size()];
			String[] fieldsNineteen = new String[columnsNineteen.size()];
			for (int i = 0, l = columnsNineteen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsNineteen.get(i);
				headerNineteen[i] = columnMap.get("header").toString();
				fieldsNineteen[i] = columnMap.get("field").toString();

			}
			
			
			
			String[] header = new String[columns.size()];
			String[] fields = new String[columns.size()];
			for (int i = 0, l = columns.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columns.get(i);
				header[i] = columnMap.get("header").toString();
				fields[i] = columnMap.get("field").toString();

			}
			String[] headerTwo = new String[columnsTwo.size()];
			String[] fieldsTwo = new String[columnsTwo.size()];
			for (int i = 0, l = columnsTwo.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsTwo.get(i);
				headerTwo[i] = columnMap.get("header").toString();
				fieldsTwo[i] = columnMap.get("field").toString();

			}
			
			
			
			String[] headerThree = new String[columnsThree.size()];
			String[] fieldsThree = new String[columnsThree.size()];
			for (int i = 0, l = columnsThree.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsThree.get(i);
				headerThree[i] = columnMap.get("header").toString();
				fieldsThree[i] = columnMap.get("field").toString();

			}
			String[] headerFour = new String[columnsFour.size()];
			String[] fieldsFour = new String[columnsFour.size()];
			for (int i = 0, l = columnsFour.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsFour.get(i);
				headerFour[i] = columnMap.get("header").toString();
				fieldsFour[i] = columnMap.get("field").toString();

			}
			
			
			String[] headerFive = new String[columnsFive.size()];
			String[] fieldsFive = new String[columnsFive.size()];
			for (int i = 0, l = columnsFive.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsFive.get(i);
				headerFive[i] = columnMap.get("header").toString();
				fieldsFive[i] = columnMap.get("field").toString();

			}
			
			
			String[] headerSix = new String[columnsSix.size()];
			String[] fieldsSix = new String[columnsSix.size()];
			for (int i = 0, l = columnsSix.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsSix.get(i);
				headerSix[i] = columnMap.get("header").toString();
				fieldsSix[i] = columnMap.get("field").toString();

			}
			
			
			
			String[] headerSeven = new String[columnsSeven.size()];
			String[] fieldsSeven = new String[columnsSeven.size()];
			for (int i = 0, l = columnsSeven.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsSeven.get(i);
				headerSeven[i] = columnMap.get("header").toString();
				fieldsSeven[i] = columnMap.get("field").toString();

			}
			
			
			
			String[] headerEight = new String[columnsEight.size()];
			String[] fieldsEight = new String[columnsEight.size()];
			for (int i = 0, l = columnsEight.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsEight.get(i);
				headerEight[i] = columnMap.get("header").toString();
				fieldsEight[i] = columnMap.get("field").toString();

			}
			
			
			
			
			String[] headerNight = new String[columnsNight.size()];
			String[] fieldsNight = new String[columnsNight.size()];
			for (int i = 0, l = columnsNight.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsNight.get(i);
				headerNight[i] = columnMap.get("header").toString();
				fieldsNight[i] = columnMap.get("field").toString();

			}
			
			
			String[] headerTen = new String[columnsTen.size()];
			String[] fieldsTen = new String[columnsTen.size()];
			for (int i = 0, l = columnsTen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsTen.get(i);
				headerTen[i] = columnMap.get("header").toString();
				fieldsTen[i] = columnMap.get("field").toString();

			}
			
			// 查询门店机型销售数据
			List<HashMap<String, Object>> modeldataList = excelDao.selectModelListByHq(beginDate, endDate, searchStr,
					conditions, WebPageUtil.isHQRole());

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> countryMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modeldataList.size(); m++) {
				if (countryMap.get(modeldataList.get(m).get("country_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modeldataList.get(m));
					countryMap.put(modeldataList.get(m).get("country_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = countryMap
							.get(modeldataList.get(m).get("country_id").toString());
					modelList.add(modeldataList.get(m));
				}

			}

			// 根据门店取得对应销售数据与目标数据
			List<HashMap<String, Object>> targetList = excelDao.selectTargetByshop(searchStr, conditions, tBeginDate,
					tEndDate, "1", WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> targetMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < targetList.size(); m++) {
				if (targetMap.get(targetList.get(m).get("country_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(targetList.get(m));
					targetMap.put(targetList.get(m).get("country_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = targetMap
							.get(targetList.get(m).get("country_id").toString());
					modelList.add(targetList.get(m));
				}

			}

			HashMap<String, String> date = BDDateUtil.getQua(tBeginDate);
			List<HashMap<String, Object>> targetListQua = excelDao.selectTargetByshop(searchStr, conditions,
					date.get("beginDate"), date.get("endDate"), "1", WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> targetMapQua = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < targetListQua.size(); m++) {
				if (targetMapQua.get(targetListQua.get(m).get("country_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(targetListQua.get(m));
					targetMapQua.put(targetListQua.get(m).get("country_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = targetMapQua
							.get(targetListQua.get(m).get("country_id").toString());
					modelList.add(targetListQua.get(m));
				}

			}
			List<HashMap<String, Object>> targetListYear = excelDao.selectTargetByYear(searchStr, conditions, yearBegin,
					yearEnd, "1", WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> targetMapYear = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < targetListYear.size(); m++) {
				if (targetMapYear.get(targetListYear.get(m).get("country_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(targetListYear.get(m));
					targetMapYear.put(targetListYear.get(m).get("country_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = targetMapYear
							.get(targetListYear.get(m).get("country_id").toString());
					modelList.add(targetListYear.get(m));
				}

			}

			// 用于放置表格数据
			LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
			HashMap<String, String> QuaMap=BDDateUtil.getQua(beginDate);
			String quaBegin=QuaMap.get("beginDate");
			List<HashMap<String, Object>> QuaExcels = excelDao.selectDatas(searchStr, conditions, quaBegin, endDate,
					WebPageUtil.isHQRole());
		
			
			String yearBegins=beginDate.split("-")[0]+"-01-01";
			List<HashMap<String, Object>> YearExcels = excelDao.selectDatas(searchStr, conditions, yearBegins, endDate,
					WebPageUtil.isHQRole());
			// 查询门店所有数据
			List<HashMap<String, Object>> excels = excelDao.selectDatas(searchStr, conditions, beginDate, endDate,
					WebPageUtil.isHQRole());
			int rowOne = 7;
			for (int j = 0; j < excels.size(); j++) {
				BigDecimal  monthQty=BigDecimal.ZERO;
				BigDecimal monthTarget=BigDecimal.ZERO;
				BigDecimal quaQty=BigDecimal.ZERO;
				BigDecimal quaTarget=BigDecimal.ZERO;
				BigDecimal yearQty=BigDecimal.ZERO;
				BigDecimal yearTarget=BigDecimal.ZERO;
				Double ach=0.0;
				// 用于放置表格数据
				HashMap<String, Object> dataMap = new HashMap<String, Object>();

				String shop_id = excels.get(j).get("country_id").toString();
				dataMap.put("SELL-OUT INFORMATION SHEET_REG.", excels.get(j).get("reg"));
				dataMap.put("SELL-OUT INFORMATION SHEET_Country.", excels.get(j).get("country_name"));
				dataMap.put("SELL-OUT INFORMATION SHEET_TYPE", "TV");
				dataMap.put("Admin Name",excels.get(j).get("user_name") );
				dataMap.put("DATE OF Upload", excels.get(j).get("datadate"));
				BigDecimal bd = null;
				bd = new BigDecimal(excels.get(j).get("saleQty").toString());
				dataMap.put("TV SELL-OUT and TARGET_TTL TV SO_QTY", Math.round(bd.doubleValue()));
				bd = new BigDecimal(excels.get(j).get("saleAmt").toString());
				//dataMap.put("TV SELL-OUT and TARGET_TTL TV SO_ASP", Math.round(bd.doubleValue()));

				Double saleSum = 0.0;
				
		
				if (targetMap.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = targetMap.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {
						bd = new BigDecimal(modelList.get(i).get("targetQty").toString());
						dataMap.put("TV SELL-OUT and TARGET_"+head+" TARGET", Math.round(bd.doubleValue()));
						monthTarget=bd;
					}

				}
				
				BigDecimal   b   =   null;  
				
				monthQty=new BigDecimal(excels.get(j).get("saleQty").toString());
				if(monthTarget.doubleValue()==0.0 || monthTarget.doubleValue()==0) {
					dataMap.put("TV SELL-OUT and TARGET_"+head+" Ach.", "100%");
				}else if(monthQty.doubleValue()==0.0 || monthQty.doubleValue()==0) {
					dataMap.put("TV SELL-OUT and TARGET_"+head+" Ach.", "0%");
				}else {
					ach=monthQty.doubleValue()/monthTarget.doubleValue()*100;
					b=new BigDecimal(ach);
					dataMap.put("TV SELL-OUT and TARGET_"+head+" Ach."
							,b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
				}
				
				
				for (int m = 0; m < QuaExcels.size(); m++) {
					if(QuaExcels.get(m).get("country_id").equals(shop_id)) {
						bd = new BigDecimal(QuaExcels.get(m).get("saleQty").toString());
						dataMap.put("TV SELL-OUT and TARGET_"+qHead+" Qty", Math.round(bd.doubleValue()));
						quaQty=bd;
					}
				}
				
				if (targetMapQua.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = targetMapQua.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {
						bd = new BigDecimal(modelList.get(i).get("targetQty").toString());
						dataMap.put("TV SELL-OUT and TARGET_"+qHead+" TARGET", Math.round(bd.doubleValue()));
						quaTarget=bd;
					}

				}
				
				
				if(quaTarget.doubleValue()==0.0 || quaTarget.doubleValue()==0) {
					dataMap.put("TV SELL-OUT and TARGET_Quarter Ach.", "100%");
				}else if(quaQty.doubleValue()==0.0 || quaQty.doubleValue()==0) {
					dataMap.put("TV SELL-OUT and TARGET_Quarter Ach.","0%");
				}else {
					ach=quaQty.doubleValue()/quaTarget.doubleValue()*100;
					b=new BigDecimal(ach);
					dataMap.put("TV SELL-OUT and TARGET_Quarter Ach.", 
							b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
				}
				
				
				for (int m = 0; m < YearExcels.size(); m++) {
					if(YearExcels.get(m).get("country_id").equals(shop_id)) {
						bd = new BigDecimal(YearExcels.get(m).get("saleQty").toString());
						dataMap.put("TV SELL-OUT and TARGET_Year Qty", Math.round(bd.doubleValue()));
						yearQty=bd;
					}
				}
				
				if (targetMapYear.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = targetMapYear.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {
						bd = new BigDecimal(modelList.get(i).get("targetQty").toString());
						dataMap.put("TV SELL-OUT and TARGET_Year TARGET", Math.round(bd.doubleValue()));
						yearTarget=bd;
					}

				}
				if(yearTarget.doubleValue()==0.0 || yearTarget.doubleValue()==0) {
					dataMap.put("TV SELL-OUT and TARGET_Year Ach.", "100%");
				}else if(yearQty.doubleValue()==0.0 || yearQty.doubleValue()==0) {
					dataMap.put("TV SELL-OUT and TARGET_Year Ach.", "0%");
				}else {
					ach=yearQty.doubleValue()/yearTarget.doubleValue()*100;
					b=new BigDecimal(ach);
					dataMap.put("TV SELL-OUT and TARGET_Year Ach.", 
							b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
				}
				

				

				rowOne++;

		
				
				BigDecimal totalBASICQTY = BigDecimal.ZERO;
				BigDecimal totalDIGITALQTY = BigDecimal.ZERO;
				BigDecimal totalINTERNETQTY = BigDecimal.ZERO;
				BigDecimal totalQUHDQTY = BigDecimal.ZERO;
				BigDecimal totalSMARTQTY = BigDecimal.ZERO;
				BigDecimal totalUHDQTY = BigDecimal.ZERO;
				BigDecimal totalCURVEQTY = BigDecimal.ZERO;

				BigDecimal totalBASICAMT = BigDecimal.ZERO;
				BigDecimal totalDIGITALAMT = BigDecimal.ZERO;
				BigDecimal totalINTERNETAMT = BigDecimal.ZERO;
				BigDecimal totalQUHDAMT = BigDecimal.ZERO;
				BigDecimal totalSMARTAMT = BigDecimal.ZERO;
				BigDecimal totalUHDAMT = BigDecimal.ZERO;
				BigDecimal totalCURVEAMT = BigDecimal.ZERO;

				// 根据门店取得对应机型销售数据
				if (countryMap.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = countryMap.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {
						if (priceMap.get(modelList.get(i).get("model")) != null) {
							ArrayList<HashMap<String, Object>> priceList = priceMap.get(modelList.get(i).get("model"));
							for (int p = 0; p < priceList.size(); p++) {
								bd = new BigDecimal(priceList.get(p).get("price").toString());
								String am = bd.toPlainString();
								double shop = Double.parseDouble(am);
								double price = shop / Integer.parseInt(priceList.get(p).get("country").toString());

								long lnum = Math.round(price);
								String key = "";
								if (modelList.get(i).get("spec").toString().contains("BASIC")) {
									totalBASICQTY = totalBASICQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalBASICAMT = totalBASICAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "BASIC LED" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";
								} else if (modelList.get(i).get("spec").toString().contains("DIGITAL")) {
									totalDIGITALQTY = totalDIGITALQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalDIGITALAMT = totalDIGITALAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "DIGITAL BASIC" + "_" + modelList.get(i).get("model") + "_" + lnum + "_"
											+ "sold";
								} else if (modelList.get(i).get("spec").toString().contains("INTERNET")) {
									totalINTERNETQTY = totalINTERNETQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalINTERNETAMT = totalINTERNETAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "DIGITAL INTERNET" + "_" + modelList.get(i).get("model") + "_" + lnum + "_"
											+ "sold";
								} else if (modelList.get(i).get("spec").toString().contains("QUHD")) {
									totalQUHDQTY = totalQUHDQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalQUHDAMT = totalQUHDAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "(QUHD) TV" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";
								} else if (modelList.get(i).get("spec").toString().contains("SMART")) {
									totalSMARTQTY = totalSMARTQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalSMARTAMT = totalSMARTAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "SMART TV" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";
								} else if (modelList.get(i).get("spec").toString().contains("UHD")) {
									totalUHDQTY = totalUHDQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalUHDAMT = totalUHDAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "UHD TV" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";
								} else if (modelList.get(i).get("spec").toString().contains("CURVE")) {
									totalCURVEQTY = totalCURVEQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalCURVEAMT = totalCURVEAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "CURVE TV" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";
								}
								bd = new BigDecimal(modelList.get(i).get("saleQty").toString());

								dataMap.put(key, Math.round(bd.doubleValue()));
							}

						}

					}

				}

				dataMap.put("LED SUB-TOTAL_" + "QTY", Math.round(totalBASICQTY.doubleValue()));
				dataMap.put("LED SUB-TOTAL_" + "AMOUNT", Math.round(totalBASICAMT.doubleValue()));
				dataMap.put("DIGITAL SUB-TOTAL_" + "QTY", Math.round(totalDIGITALQTY.doubleValue()));
				dataMap.put("DIGITAL SUB-TOTAL_" + "AMOUNT", Math.round(totalDIGITALAMT.doubleValue()));
				dataMap.put("INTERNET SUB-TOTAL_" + "QTY", Math.round(totalINTERNETQTY.doubleValue()));
				dataMap.put("INTERNET SUB-TOTAL_" + "AMOUNT", Math.round(totalINTERNETAMT.doubleValue()));
				dataMap.put("(QUHD) SUB-TOTAL_" + "QTY", Math.round(totalQUHDQTY.doubleValue()));
				dataMap.put("(QUHD) SUB-TOTAL_" + "AMOUNT", Math.round(totalQUHDAMT.doubleValue()));
				dataMap.put("SMART SUB-TOTAL_" + "QTY", Math.round(totalSMARTQTY.doubleValue()));
				dataMap.put("SMART SUB-TOTAL_" + "AMOUNT", Math.round(totalBASICAMT.doubleValue()));
				dataMap.put("UHD SUB-TOTAL_" + "QTY", Math.round(totalUHDQTY.doubleValue()));
				dataMap.put("UHD SUB-TOTAL_" + "AMOUNT", Math.round(totalUHDAMT.doubleValue()));
				dataMap.put("CURVE SUB-TOTAL_" + "QTY", Math.round(totalCURVEQTY.doubleValue()));
				dataMap.put("CURVE SUB-TOTAL_" + "AMOUNT", Math.round(totalCURVEAMT.doubleValue()));

				dataList.add(dataMap);
			}

			
			HashMap<String, Object> whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","Total");
			
			LinkedList<HashMap<String, Object>> dataListTwo=getList(whereMap, yearBegin, "All Products");
			

			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			String searchLine=" AND (pt.product_line  LIKE 'X%' OR pt.product_line  LIKE 'C%' OR pt.product_line  LIKE 'P%')"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (tr.`product_line` LIKE 'X%' OR tr.`product_line`  LIKE 'C%' OR tr.`product_line` LIKE 'P%' ) ");

			whereMap.put("searchLine",searchLine);
			
			LinkedList<HashMap<String, Object>> dataListThree=getList(whereMap, yearBegin, "XCP UHD");
		

			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line  LIKE 'X%' OR pt.product_line  LIKE 'C%'  )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (tr.`product_line` LIKE 'X%' OR tr.`product_line`  LIKE 'C%'  ) ");

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListFour =getList(whereMap, yearBegin, "CX UHD");
			

			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line  LIKE 'P%' )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line`  LIKE 'P%'  ) ");

			whereMap.put("searchLine",searchLine);
			
			LinkedList<HashMap<String, Object>> dataListFive=getList(whereMap, yearBegin, "P UHD");
			
			
	
			
			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","Total");
			searchLine=" AND (pt.Size>65 )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			

			whereMap.put("searchLine",searchLine);
			
			LinkedList<HashMap<String, Object>> dataListSix =getList(whereMap, yearBegin, "65\"↑ UHD");
			
			
			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%X2%' OR  pt.product_line like '%X3%' )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%X2%' OR  tr.`product_line`  like '%X3%'  ) ");
			
			

			whereMap.put("searchLine",searchLine);
			
			LinkedList<HashMap<String, Object>> dataListSeven=getList(whereMap, yearBegin, "X2X3 UHD");
			
	
		
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%C2%' OR  pt.product_line like '%C2L%' )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%C2%' OR  tr.`product_line`  like '%C2L%'  ) ");
			
			

			whereMap.put("searchLine",searchLine);
			
			LinkedList<HashMap<String, Object>> dataListEight=getList(whereMap, yearBegin, "C2C2L UHD");
			
			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%C4%' OR  pt.product_line like '%C6%' )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%C4%' OR  tr.`product_line`  like '%C6%'  ) ");
			
			

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListNight=getList(whereMap, yearBegin, "C4C6 UHD");
			
			
			
			
			
			
			
	
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%P2%' OR  pt.product_line like '%P21%' )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%P2%' OR  tr.`product_line`  like '%P21%'  ) ");
			
			

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListTen =getList(whereMap, yearBegin, "P2P21 UHD");
			
			
			

			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%P6%' )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%P6%') ");


			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListEleven =getList(whereMap, yearBegin, "P6 UHD");
		
			

			
		
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%P62%'  )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%P62%'  ) ");
			
			

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListTwelve  =getList(whereMap, yearBegin, "P62 UHD");
			
			
		
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%P65%'  )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%P65%'   ) ");
			
			

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListThirteen =getList(whereMap, yearBegin, "P65 UHD");
			
			

			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%P5%'  )";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%P5%'   ) ");
			
			

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListFourteen =getList(whereMap, yearBegin, "P5 UHD");
			
			
			
			
	

			
		
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%P3%'   )";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%P3%'  ) ");
			
			

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListFifteen  =getList(whereMap, yearBegin, "P3");
			
			

			
		
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");

			searchLine=" AND (pt.product_line like 'S%'   )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%FHD%'   AND pt.`PRODUCT_SPEC_ID` LIKE '%Smart%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like 'S%'   ) ");
			
			

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListSixteen =getList(whereMap, yearBegin, "S FHD Smart");
			
			

			
		
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like 'S%' )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%FHD%'   AND PT.OS='Android' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like 'S%'  ) ");

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListSeventeen=getList(whereMap, yearBegin, "S FHD Android");
		

			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","Total");
			searchLine= " AND pt.`PRODUCT_SPEC_ID` LIKE '%DIGITAL%' ";
			


			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListEightteen =getList(whereMap, yearBegin, "DTV");

			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","Total");
			searchLine= " AND pt.`PRODUCT_SPEC_ID` NOT LIKE '%DIGITAL%'  ";
	
			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListNineteen   =getList(whereMap, yearBegin, "ATV");
			
		
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
			sheet.createFreezePane(7, 9, 9, 10);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
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
			cellStyleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
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
			cellStylePERCENT.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
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
			cell.setCellValue("TCL  BDSC "+head);// 标题
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

			/*
			 * cell = rowSix.createCell((short) 4); cell.setCellValue("SUBTOTAL(9,E8:E" +
			 * size + ")"); cell.setCellType(Cell.CELL_TYPE_FORMULA);
			 * cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			 * cell.setCellStyle(cellStylePERCENT); cell.setCellFormula("SUBTOTAL(9,E8:E" +
			 * size + ")");
			 */
			cell = rowSix.createCell((short) 0);
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellValue("BDSC");
			// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
			sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 1));
			

			// 第二行
			cell = rowSix.createCell((short) 5);
			cell.setCellValue("SUBTOTAL(9,F8:F" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,F8:F" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 6);
			cell.setCellValue("SUBTOTAL(9,G8:G" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,G8:G" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 7);
			cell.setCellValue("SUBTOTAL(9,H8:H" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,H8:H" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 8);
			cell.setCellValue("TEXT(F7/H7,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT(F7/H7,\"0.00%\")");

			// 第二行
			cell = rowSix.createCell((short) 9);
			cell.setCellValue("SUBTOTAL(9,J8:J" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,J8:J" + size + ")");
			
			// 第二行
			cell = rowSix.createCell((short) 10);
			cell.setCellValue("SUBTOTAL(9,K8:K" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,K8:K" + size + ")");
			

			// 第二行
			cell = rowSix.createCell((short) 11);
			cell.setCellValue("TEXT(J7/K7,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT(J7/K7,\"0.00%\")");

			// 第二行
			cell = rowSix.createCell((short) 12);
			cell.setCellValue("SUBTOTAL(9,M8:M" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,M8:M" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 13);
			cell.setCellValue("SUBTOTAL(9,N8:N" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,N8:N" + size + ")");
			
			
			// 第二行
			cell = rowSix.createCell((short) 14);
			cell.setCellValue("TEXT(M7/N7,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT(M7/N7,\"0.00%\")");


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
			cell.setCellValue("SUBTOTAL(9,R8:R" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,R8:R" + size + ")");

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
			String[] line = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
					"S", "T", "U", "V", "W", "X", "Y", "Z" };

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
				cell.setCellValue("SUBTOTAL(9," + big[i] + "8:" + big[i] + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9," + big[i] + "8:" + big[i] + size + ")");

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

						if (i < 5) {
							cell.setCellStyle(cellStyleWHITE);
						} else if (i > 4 && i < 15) {
							cell.setCellStyle(cellStylehead);
						} else if (i > 14) {
							cell.setCellStyle(cellStyleYellow);
							if (s[0].contains("SUB-TOTAL") || s[0].contains("TTL")) {
								cell.setCellStyle(cellStyleRED);
							}
						}
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2, rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						if (i < 5) {
							cell.setCellStyle(cellStyleWHITE);
						} else if (i > 4 && i < 15) {
							cell.setCellStyle(cellStylehead);
						} else if (i > 14) {
							cell.setCellStyle(cellStyleYellow);
							if (s[0].contains("SUB-TOTAL") || s[0].contains("TTL")) {
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
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						if ((i > 14 && k + 2 == 2) || i < 15) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k + 2, (num), (num + cols - 1)));
						}

						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k + 2 + rows_max - s.length, num, num));
						}
						// }

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
					if (dataMap.get(fields[c]) != null && dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString().contains("%");
						isGongshi = dataMap.get(fields[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fields[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c]).toString());
							int s = dataMap.get(fields[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c]).toString());

						} 
						if(c==8 || c==11 || c==14) {
							BDDateUtil.setColor(dataMap.get(fields[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						}
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
				
			}
			rows_max = 0;
			for (int i = 0; i < headerTwo.length; i++) {
				String h = headerTwo[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapTwo = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7 );

				for (int i = 0; i < headerTwo.length; i++) {

					String headerTwoTemp = headerTwo[i];
					String[] s = headerTwoTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7 , rows_max+7+excels.size()+7 + 1, (num), (num)));
						sk = headerTwoTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapTwo.containsKey(headerTwoTemp)) {
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
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7 , k +7+excels.size()+7 , (num), (num + cols - 1)));

						if (sk.equals(headerTwoTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7 ,k +7+excels.size()+7 + rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListTwo.size(); d++) {

				HashMap<String, Object> dataMap = dataListTwo.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsTwo.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsTwo[c]) != null && dataMap.get(fieldsTwo[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTwo[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTwo[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTwo[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsTwo[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsTwo[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsTwo[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsTwo[c]).toString());
							int s = dataMap.get(fieldsTwo[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsTwo[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsTwo[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsTwo[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
				
			}
			
			rows_max = 0;
			for (int i = 0; i < headerThree.length; i++) {
				String h = headerThree[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapThree = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5);

				for (int i = 0; i < headerThree.length; i++) {

					String headerThreeTemp = headerThree[i];
					String[] s = headerThreeTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5, rows_max+7+excels.size()+7+dataListTwo.size()+5 + 1, (num), (num)));
						sk = headerThreeTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapThree.containsKey(headerThreeTemp)) {
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
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 , k +7+excels.size()+7+dataListTwo.size()+5 , (num), (num + cols - 1)));

						if (sk.equals(headerThreeTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 ,k +7+excels.size()+7+dataListTwo.size()+5 + rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListThree.size(); d++) {

				HashMap<String, Object> dataMap = dataListThree.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsThree.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsThree[c]) != null && dataMap.get(fieldsThree[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsThree[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsThree[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsThree[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsThree[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsThree[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsThree[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsThree[c]).toString());
							int s = dataMap.get(fieldsThree[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsThree[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsThree[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsThree[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerFour.length; i++) {
				String h = headerFour[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapFour = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5);

				for (int i = 0; i < headerFour.length; i++) {

					String headerFourTemp = headerFour[i];
					String[] s = headerFourTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+ 1, (num), (num)));
						sk = headerFourTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapFour.containsKey(headerFourTemp)) {
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
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5 , (num), (num + cols - 1)));

						if (sk.equals(headerFourTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+ rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListFour.size(); d++) {

				HashMap<String, Object> dataMap = dataListFour.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5+dataListThree.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsFour.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsFour[c]) != null && dataMap.get(fieldsFour[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFour[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFour[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFour[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsFour[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsFour[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsFour[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsFour[c]).toString());
							int s = dataMap.get(fieldsFour[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsFour[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsFour[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsFour[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerFive.length; i++) {
				String h = headerFive[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapFive = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5);

				for (int i = 0; i < headerFive.length; i++) {

					String headerFiveTemp = headerFive[i];
					String[] s = headerFiveTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+ 1, (num), (num)));
						sk = headerFiveTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapFive.containsKey(headerFiveTemp)) {
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

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 , (num), (num + cols - 1)));

						if (sk.equals(headerFiveTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+ rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListFive.size(); d++) {

				HashMap<String, Object> dataMap = dataListFive.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsFive.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsFive[c]) != null && dataMap.get(fieldsFive[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFive[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFive[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFive[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsFive[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsFive[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsFive[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsFive[c]).toString());
							int s = dataMap.get(fieldsFive[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsFive[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsFive[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsFive[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerSix.length; i++) {
				String h = headerSix[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapSix = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5);

				for (int i = 0; i < headerSix.length; i++) {

					String headerSixTemp = headerSix[i];
					String[] s = headerSixTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+ 1, (num), (num)));
						sk = headerSixTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapSix.containsKey(headerSixTemp)) {
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

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerSixTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+ rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListSix.size(); d++) {

				HashMap<String, Object> dataMap = dataListSix.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsSix.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsSix[c]) != null && dataMap.get(fieldsSix[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsSix[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsSix[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsSix[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsSix[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsSix[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsSix[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsSix[c]).toString());
							int s = dataMap.get(fieldsSix[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsSix[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsSix[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsSix[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerSeven.length; i++) {
				String h = headerSeven[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapSeven = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5);

				for (int i = 0; i < headerSeven.length; i++) {

					String headerSevenTemp = headerSeven[i];
					String[] s = headerSevenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+ 1, (num), (num)));
						sk = headerSevenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapSeven.containsKey(headerSevenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapSeven.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerSeven.length; j++) {

							if (headerSeven[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerSevenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapSeven.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapSeven.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListSeven.size(); d++) {

				HashMap<String, Object> dataMap = dataListSeven.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsSeven.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsSeven[c]) != null && dataMap.get(fieldsSeven[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsSeven[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsSeven[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsSeven[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsSeven[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsSeven[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsSeven[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsSeven[c]).toString());
							int s = dataMap.get(fieldsSeven[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsSeven[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsSeven[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsSeven[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			
			
			
			
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerEight.length; i++) {
				String h = headerEight[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapEight = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5);

				for (int i = 0; i < headerEight.length; i++) {

					String headerEightTemp = headerEight[i];
					String[] s = headerEightTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+ 1, (num), (num)));
						sk = headerEightTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapEight.containsKey(headerEightTemp)) {
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

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerEightTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+ rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListEight.size(); d++) {

				HashMap<String, Object> dataMap = dataListEight.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsEight.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsEight[c]) != null && dataMap.get(fieldsEight[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsEight[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsEight[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsEight[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsEight[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsEight[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsEight[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsEight[c]).toString());
							int s = dataMap.get(fieldsEight[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsEight[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsEight[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsEight[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerNight.length; i++) {
				String h = headerNight[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapNight = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5);

				for (int i = 0; i < headerNight.length; i++) {

					String headerNightTemp = headerNight[i];
					String[] s = headerNightTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+ 1, (num), (num)));
						sk = headerNightTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapNight.containsKey(headerNightTemp)) {
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

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerNightTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+ rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListNight.size(); d++) {

				HashMap<String, Object> dataMap = dataListNight.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsNight.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsNight[c]) != null && dataMap.get(fieldsNight[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsNight[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsNight[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsNight[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsNight[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsNight[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsNight[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsNight[c]).toString());
							int s = dataMap.get(fieldsNight[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsNight[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsNight[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsNight[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerTen.length; i++) {
				String h = headerTen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapTen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5);

				for (int i = 0; i < headerTen.length; i++) {

					String headerTenTemp = headerTen[i];
					String[] s = headerTenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+ 1, (num), (num)));
						sk = headerTenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapTen.containsKey(headerTenTemp)) {
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

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerTenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+ rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListTen.size(); d++) {

				HashMap<String, Object> dataMap = dataListTen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsTen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsTen[c]) != null && dataMap.get(fieldsTen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsTen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsTen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsTen[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsTen[c]).toString());
							int s = dataMap.get(fieldsTen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsTen[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsTen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsTen[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			rows_max = 0;
			for (int i = 0; i < headerEleven.length; i++) {
				String h = headerEleven[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapEleven = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5);

				for (int i = 0; i < headerEleven.length; i++) {

					String headerElevenTemp = headerEleven[i];
					String[] s = headerElevenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+ 1, (num), (num)));
						sk = headerElevenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapEleven.containsKey(headerElevenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapEleven.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerEleven.length; j++) {

							if (headerEleven[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerElevenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapEleven.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapEleven.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListEleven.size(); d++) {

				HashMap<String, Object> dataMap = dataListEleven.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsEleven.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conEleventCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsEleven[c]) != null && dataMap.get(fieldsEleven[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsEleven[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsEleven[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsEleven[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsEleven[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsEleven[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conEleventCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conEleventCell.setCellValue(Double.parseDouble(dataMap.get(fieldsEleven[c]).toString()));
						} else if (isGongshi) {

							conEleventCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conEleventCell.setCellStyle(contextstyle);
							conEleventCell.setCellFormula(dataMap.get(fieldsEleven[c]).toString());
							int s = dataMap.get(fieldsEleven[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conEleventCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conEleventCell.setCellStyle(contextstyle);
							conEleventCell.setCellFormula(dataMap.get(fieldsEleven[c]).toString());

						} else {
							conEleventCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conEleventCell.setCellValue(dataMap.get(fieldsEleven[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsEleven[c]).toString(), conEleventCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conEleventCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerTwelve.length; i++) {
				String h = headerTwelve[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapTwelve = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5);

				for (int i = 0; i < headerTwelve.length; i++) {

					String headerTwelveTemp = headerTwelve[i];
					String[] s = headerTwelveTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+ 1, (num), (num)));
						sk = headerTwelveTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapTwelve.containsKey(headerTwelveTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapTwelve.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerTwelve.length; j++) {

							if (headerTwelve[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerTwelveTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapTwelve.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapTwelve.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListTwelve.size(); d++) {

				HashMap<String, Object> dataMap = dataListTwelve.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsTwelve.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conTwelvetCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsTwelve[c]) != null && dataMap.get(fieldsTwelve[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTwelve[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTwelve[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTwelve[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsTwelve[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsTwelve[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conTwelvetCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conTwelvetCell.setCellValue(Double.parseDouble(dataMap.get(fieldsTwelve[c]).toString()));
						} else if (isGongshi) {

							conTwelvetCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conTwelvetCell.setCellStyle(contextstyle);
							conTwelvetCell.setCellFormula(dataMap.get(fieldsTwelve[c]).toString());
							int s = dataMap.get(fieldsTwelve[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conTwelvetCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conTwelvetCell.setCellStyle(contextstyle);
							conTwelvetCell.setCellFormula(dataMap.get(fieldsTwelve[c]).toString());

						} else {
							conTwelvetCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conTwelvetCell.setCellValue(dataMap.get(fieldsTwelve[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsTwelve[c]).toString(), conTwelvetCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conTwelvetCell.setCellValue("");
					}

					
				}
			}
			
			
			rows_max = 0;
			for (int i = 0; i < headerThirteen.length; i++) {
				String h = headerThirteen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapThirteen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5);

				for (int i = 0; i < headerThirteen.length; i++) {

					String headerThirteenTemp = headerThirteen[i];
					String[] s = headerThirteenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+ 1, (num), (num)));
						sk = headerThirteenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapThirteen.containsKey(headerThirteenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapThirteen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerThirteen.length; j++) {

							if (headerThirteen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerThirteenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapThirteen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapThirteen.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListThirteen.size(); d++) {

				HashMap<String, Object> dataMap = dataListThirteen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsThirteen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conThirteentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsThirteen[c]) != null && dataMap.get(fieldsThirteen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsThirteen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsThirteen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsThirteen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsThirteen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsThirteen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conThirteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conThirteentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsThirteen[c]).toString()));
						} else if (isGongshi) {

							conThirteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conThirteentCell.setCellStyle(contextstyle);
							conThirteentCell.setCellFormula(dataMap.get(fieldsThirteen[c]).toString());
							int s = dataMap.get(fieldsThirteen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conThirteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conThirteentCell.setCellStyle(contextstyle);
							conThirteentCell.setCellFormula(dataMap.get(fieldsThirteen[c]).toString());

						} else {
							conThirteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conThirteentCell.setCellValue(dataMap.get(fieldsThirteen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsThirteen[c]).toString(), conThirteentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conThirteentCell.setCellValue("");
					}

					
				}
			}
			
			
			rows_max = 0;
			for (int i = 0; i < headerFourteen.length; i++) {
				String h = headerFourteen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapFourteen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5);

				for (int i = 0; i < headerFourteen.length; i++) {

					String headerFourteenTemp = headerFourteen[i];
					String[] s = headerFourteenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+ 1, (num), (num)));
						sk = headerFourteenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapFourteen.containsKey(headerFourteenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapFourteen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerFourteen.length; j++) {

							if (headerFourteen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerFourteenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapFourteen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapFourteen.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListFourteen.size(); d++) {

				HashMap<String, Object> dataMap = dataListFourteen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsFourteen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conFourteentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsFourteen[c]) != null && dataMap.get(fieldsFourteen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFourteen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFourteen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFourteen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsFourteen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsFourteen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conFourteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conFourteentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsFourteen[c]).toString()));
						} else if (isGongshi) {

							conFourteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conFourteentCell.setCellStyle(contextstyle);
							conFourteentCell.setCellFormula(dataMap.get(fieldsFourteen[c]).toString());
							int s = dataMap.get(fieldsFourteen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conFourteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conFourteentCell.setCellStyle(contextstyle);
							conFourteentCell.setCellFormula(dataMap.get(fieldsFourteen[c]).toString());

						} else {
							conFourteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conFourteentCell.setCellValue(dataMap.get(fieldsFourteen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsFourteen[c]).toString(), conFourteentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conFourteentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerFifteen.length; i++) {
				String h = headerFifteen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapFifteen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5);

				for (int i = 0; i < headerFifteen.length; i++) {

					String headerFifteenTemp = headerFifteen[i];
					String[] s = headerFifteenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+ 1, (num), (num)));
						sk = headerFifteenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapFifteen.containsKey(headerFifteenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapFifteen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerFifteen.length; j++) {

							if (headerFifteen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerFifteenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapFifteen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapFifteen.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListFifteen.size(); d++) {

				HashMap<String, Object> dataMap = dataListFifteen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsFifteen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conFifteentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsFifteen[c]) != null && dataMap.get(fieldsFifteen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFifteen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFifteen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFifteen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsFifteen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsFifteen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conFifteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conFifteentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsFifteen[c]).toString()));
						} else if (isGongshi) {

							conFifteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conFifteentCell.setCellStyle(contextstyle);
							conFifteentCell.setCellFormula(dataMap.get(fieldsFifteen[c]).toString());
							int s = dataMap.get(fieldsFifteen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conFifteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conFifteentCell.setCellStyle(contextstyle);
							conFifteentCell.setCellFormula(dataMap.get(fieldsFifteen[c]).toString());

						} else {
							conFifteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conFifteentCell.setCellValue(dataMap.get(fieldsFifteen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsFifteen[c]).toString(), conFifteentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conFifteentCell.setCellValue("");
					}

					
				}
			}
			
			rows_max = 0;
			for (int i = 0; i < headerSixteen.length; i++) {
				String h = headerSixteen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapSixteen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5);

				for (int i = 0; i < headerSixteen.length; i++) {

					String headerSixteenTemp = headerSixteen[i];
					String[] s = headerSixteenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+ 1, (num), (num)));
						sk = headerSixteenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapSixteen.containsKey(headerSixteenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapSixteen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerSixteen.length; j++) {

							if (headerSixteen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerSixteenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapSixteen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapSixteen.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListSixteen.size(); d++) {

				HashMap<String, Object> dataMap = dataListSixteen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsSixteen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conSixteentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsSixteen[c]) != null && dataMap.get(fieldsSixteen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsSixteen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsSixteen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsSixteen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsSixteen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsSixteen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conSixteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conSixteentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsSixteen[c]).toString()));
						} else if (isGongshi) {

							conSixteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conSixteentCell.setCellStyle(contextstyle);
							conSixteentCell.setCellFormula(dataMap.get(fieldsSixteen[c]).toString());
							int s = dataMap.get(fieldsSixteen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conSixteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conSixteentCell.setCellStyle(contextstyle);
							conSixteentCell.setCellFormula(dataMap.get(fieldsSixteen[c]).toString());

						} else {
							conSixteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conSixteentCell.setCellValue(dataMap.get(fieldsSixteen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsSixteen[c]).toString(), conSixteentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conSixteentCell.setCellValue("");
					}

					
				}
			}
			
			
			rows_max = 0;
			for (int i = 0; i < headerSeventeen.length; i++) {
				String h = headerSeventeen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapSeventeen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5);

				for (int i = 0; i < headerSeventeen.length; i++) {

					String headerSeventeenTemp = headerSeventeen[i];
					String[] s = headerSeventeenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+ 1, (num), (num)));
						sk = headerSeventeenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapSeventeen.containsKey(headerSeventeenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapSeventeen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerSeventeen.length; j++) {

							if (headerSeventeen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerSeventeenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapSeventeen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapSeventeen.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListSeventeen.size(); d++) {

				HashMap<String, Object> dataMap = dataListSeventeen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsSeventeen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conSeventeentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsSeventeen[c]) != null && dataMap.get(fieldsSeventeen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsSeventeen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsSeventeen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsSeventeen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsSeventeen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsSeventeen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conSeventeentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conSeventeentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsSeventeen[c]).toString()));
						} else if (isGongshi) {

							conSeventeentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conSeventeentCell.setCellStyle(contextstyle);
							conSeventeentCell.setCellFormula(dataMap.get(fieldsSeventeen[c]).toString());
							int s = dataMap.get(fieldsSeventeen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conSeventeentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conSeventeentCell.setCellStyle(contextstyle);
							conSeventeentCell.setCellFormula(dataMap.get(fieldsSeventeen[c]).toString());

						} else {
							conSeventeentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conSeventeentCell.setCellValue(dataMap.get(fieldsSeventeen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsSeventeen[c]).toString(), conSeventeentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conSeventeentCell.setCellValue("");
					}

					
				}
			}
			
			
			rows_max = 0;
			for (int i = 0; i < headerEightteen.length; i++) {
				String h = headerEightteen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapEightteen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5);

				for (int i = 0; i < headerEightteen.length; i++) {

					String headerEightteenTemp = headerEightteen[i];
					String[] s = headerEightteenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+ 1, (num), (num)));
						sk = headerEightteenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapEightteen.containsKey(headerEightteenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapEightteen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerEightteen.length; j++) {

							if (headerEightteen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerEightteenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapEightteen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapEightteen.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListEightteen.size(); d++) {

				HashMap<String, Object> dataMap = dataListEightteen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsEightteen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conEightteentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsEightteen[c]) != null && dataMap.get(fieldsEightteen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsEightteen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsEightteen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsEightteen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsEightteen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsEightteen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conEightteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conEightteentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsEightteen[c]).toString()));
						} else if (isGongshi) {

							conEightteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conEightteentCell.setCellStyle(contextstyle);
							conEightteentCell.setCellFormula(dataMap.get(fieldsEightteen[c]).toString());
							int s = dataMap.get(fieldsEightteen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conEightteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conEightteentCell.setCellStyle(contextstyle);
							conEightteentCell.setCellFormula(dataMap.get(fieldsEightteen[c]).toString());

						} else {
							conEightteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conEightteentCell.setCellValue(dataMap.get(fieldsEightteen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsEightteen[c]).toString(), conEightteentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conEightteentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerNineteen.length; i++) {
				String h = headerNineteen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapNineteen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5);

				for (int i = 0; i < headerNineteen.length; i++) {

					String headerNineteenTemp = headerNineteen[i];
					String[] s = headerNineteenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5+ 1, (num), (num)));
						sk = headerNineteenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapNineteen.containsKey(headerNineteenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapNineteen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerNineteen.length; j++) {

							if (headerNineteen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerNineteenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapNineteen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapNineteen.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListNineteen.size(); d++) {

				HashMap<String, Object> dataMap = dataListNineteen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsNineteen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conNineteentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsNineteen[c]) != null && dataMap.get(fieldsNineteen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsNineteen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsNineteen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsNineteen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsNineteen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsNineteen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conNineteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conNineteentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsNineteen[c]).toString()));
						} else if (isGongshi) {

							conNineteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conNineteentCell.setCellStyle(contextstyle);
							conNineteentCell.setCellFormula(dataMap.get(fieldsNineteen[c]).toString());
							int s = dataMap.get(fieldsNineteen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conNineteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conNineteentCell.setCellStyle(contextstyle);
							conNineteentCell.setCellFormula(dataMap.get(fieldsNineteen[c]).toString());

						} else {
							conNineteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conNineteentCell.setCellValue(dataMap.get(fieldsNineteen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsNineteen[c]).toString(), conNineteentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conNineteentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	String[] countryList = {"Russia","Brazil","Argentina","Saudi Arabia","UAE","Iran","Ecuador","Costa Rica"};
	String[] specList = { "BASIC LED", "CURVED TV", "DIGITAL BASIC LED TV", "INTERNET TV", "QUHD", "SMART TV", "UHD" };
	private JSONObject obj;
	
	

	@Override
	public void comparativeByHq(SXSSFWorkbook wb, String partyName, String beginDateOne, String endDateOne,
			String tBeginDate, String tEndDate, String searchStr, String conditions) throws Exception {
		String beginDate = beginDateOne;
		String endDate = endDateOne;
		String[] days = beginDateOne.split("-");
		Date timeb = format.parse(beginDateOne);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(timeb);
		Date daysOne = format.parse(format.format(rightNow.getTime()));
		String[] toYear = endDateOne.split("-");

		Date timec = format.parse(beginDateOne);
		Calendar rightNowc = Calendar.getInstance();
		rightNowc.setTime(timec);
		Date toYearOne = format.parse(format.format(rightNowc.getTime()));

		int laYear = Integer.parseInt(toYear[0].toString()) - 1;
		String laDays = BDDateUtil.getLastDayOfMonth(laYear, Integer.parseInt(toYear[1]));
		String[] laDay = laDays.split("-");
		// 表头数据
		String[] headers = { sdf.format(daysOne) + "." + laYear };
		// 用于放置表格数据

		BigDecimal bds = null;
		List<HashMap<String, Object>> excels = excelDao.selectDatas(searchStr, conditions, beginDate, endDate,
				WebPageUtil.isHQRole());
		Map<String, BigDecimal> mapAvg = new HashMap<String, BigDecimal>();

	
		HashMap<String, Object> dataThrees = new HashMap<String, Object>();
		HashMap<String, Object> datFours = new HashMap<String, Object>();
		HashMap<String, Object> dataFives = new HashMap<String, Object>();
		BigDecimal qtyTotal = BigDecimal.ZERO;
		BigDecimal amtTotal = BigDecimal.ZERO;
		dataThrees.put(sdf.format(daysOne) + "." + laYear, sdf.format(daysOne) + "." + days[0]);
		datFours.put(sdf.format(daysOne) + "." + laYear, "TTL QTY");
		dataFives.put(sdf.format(daysOne) + "." + laYear, "Ave/day");
		String lastDayt[] = BDDateUtil.getLastDayOfMonth(Integer.parseInt(days[0]), Integer.parseInt(days[1]))
				.split("-");
		int day = Integer.parseInt(lastDayt[2]);
		qtyTotal = BigDecimal.ZERO;
		amtTotal = BigDecimal.ZERO;
		for (int i = 0; i < excels.size(); i++) {

			bds = new BigDecimal(excels.get(i).get("saleQty").toString());
			datFours.put(excels.get(i).get("country_name").toString(), Math.round(bds.doubleValue()));
			dataFives.put(excels.get(i).get("country_name").toString(), Math.round(bds.doubleValue() / day));

			qtyTotal = qtyTotal.add(new BigDecimal(excels.get(i).get("saleQty").toString()));
			amtTotal = amtTotal.add(new BigDecimal(excels.get(i).get("saleAmt").toString()));
			
			BigDecimal numOne = mapAvg.get(excels.get(i).get("country_name")+"_Now");
			if (numOne == null) {
				numOne = BigDecimal.ZERO;
			}
			
			mapAvg.put(excels.get(i).get("country_name")+"_Now", numOne.add( new BigDecimal(Math.round(bds.doubleValue() / day))));
			
		}
		dataThrees.put("Total", "Total");
		dataThrees.put("Amount", "Amount");

		datFours.put("Total", Math.round(qtyTotal.doubleValue()));
		datFours.put("Amount", amtTotal.doubleValue());
		dataFives.put("Total", Math.round(qtyTotal.doubleValue() / day));
		dataFives.put("Amount", Math.round(amtTotal.doubleValue() / day));

		String[] headersFive = { "YTD-" + toYear[0] + "  Monthly sellout trend per size_Country",
				"YTD-" + toYear[0] + "  Monthly sellout trend per size_Category"
				// "YTD-" + toYear[0] + " Monthly sellout trend per size_TTL"
		};

		if (toYear[1].equals("01")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Jan." + laYear);
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Jan." + toYear[0]);

		} else if (toYear[1].equals("02")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Feb." + laYear);
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Feb." + toYear[0]);

		} else if (toYear[1].equals("03")) {

			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Mar." + laYear);
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Mar." + toYear[0]);
		} else if (toYear[1].equals("04")) {

			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Apr." + laYear);
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Apr." + toYear[0]);
		} else if (toYear[1].equals("05")) {

			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_May." + laYear);
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_May." + toYear[0]);
		} else if (toYear[1].equals("06")) {

			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_June." + laYear);
			headersFive = insert(headersFive,
					"YTD-" + toYear[0] + "  Monthly sellout trend per size_June." + toYear[0]);
		} else if (toYear[1].equals("07")) {

			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_July." + laYear);
			headersFive = insert(headersFive,
					"YTD-" + toYear[0] + "  Monthly sellout trend per size_July." + toYear[0]);
		} else if (toYear[1].equals("08")) {

			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Aug." + laYear);
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Aug." + toYear[0]);
		} else if (toYear[1].equals("09")) {

			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Sept." + laYear);
			headersFive = insert(headersFive,
					"YTD-" + toYear[0] + "  Monthly sellout trend per size_Sept." + toYear[0]);
		} else if (toYear[1].equals("10")) {

			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Oct." + laYear);
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Oct." + toYear[0]);
		} else if (toYear[1].equals("11")) {

			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Nov." + laYear);
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Nov." + toYear[0]);
		} else if (toYear[1].equals("12")) {

			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Dec." + laYear);
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Dec." + toYear[0]);
		}
		headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth");

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

		String[] headersSix = { "Market Share_Country", "Market Share_Category"
				// "Market Share_TTL"

		};

		if (toYear[1].equals("01")) {

			headersSix = insert(headersSix, "Market Share_Jan." + laYear);
			headersSix = insert(headersSix, "Market Share_Jan." + toYear[0]);
		} else if (toYear[1].equals("02")) {

			headersSix = insert(headersSix, "Market Share_Feb." + laYear);
			headersSix = insert(headersSix, "Market Share_Feb." + toYear[0]);
		} else if (toYear[1].equals("03")) {

			headersSix = insert(headersSix, "Market Share_Mar." + laYear);
			headersSix = insert(headersSix, "Market Share_Mar." + toYear[0]);
		} else if (toYear[1].equals("04")) {
			headersSix = insert(headersSix, "Market Share_Apr." + laYear);
			headersSix = insert(headersSix, "Market Share_Apr." + toYear[0]);
		} else if (toYear[1].equals("05")) {
			headersSix = insert(headersSix, "Market Share_May." + laYear);
			headersSix = insert(headersSix, "Market Share_May." + toYear[0]);
		} else if (toYear[1].equals("06")) {
			headersSix = insert(headersSix, "Market Share_June." + laYear);
			headersSix = insert(headersSix, "Market Share_June." + toYear[0]);
		} else if (toYear[1].equals("07")) {
			headersSix = insert(headersSix, "Market Share_July." + laYear);
			headersSix = insert(headersSix, "Market Share_July." + toYear[0]);
		} else if (toYear[1].equals("08")) {
			headersSix = insert(headersSix, "Market Share_Aug." + laYear);
			headersSix = insert(headersSix, "Market Share_Aug." + toYear[0]);
		} else if (toYear[1].equals("09")) {
			headersSix = insert(headersSix, "Market Share_Sept." + laYear);
			headersSix = insert(headersSix, "Market Share_Sept." + toYear[0]);
		} else if (toYear[1].equals("10")) {
			headersSix = insert(headersSix, "Market Share_Oct." + laYear);
			headersSix = insert(headersSix, "Market Share_Oct." + toYear[0]);
		} else if (toYear[1].equals("11")) {
			headersSix = insert(headersSix, "Market Share_Nov." + laYear);
			headersSix = insert(headersSix, "Market Share_Nov." + toYear[0]);
		} else if (toYear[1].equals("12")) {
			headersSix = insert(headersSix, "Market Share_Dec." + laYear);
			headersSix = insert(headersSix, "Market Share_Dec." + toYear[0]);
		}

		headersSix = insert(headersSix, "Market Share_Growth");

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

		String[] headersEight = { "Different catgory sell-out quantity_Country",
				"Different catgory sell-out quantity_Category"

		};

		if (toYear[1].equals("01")) {

			headersEight = insert(headersEight, "Different catgory sell-out quantity_Jan." + laYear);
			headersEight = insert(headersEight, "Different catgory sell-out quantity_Jan." + toYear[0]);
		} else if (toYear[1].equals("02")) {

			headersEight = insert(headersEight, "Different catgory sell-out quantity_Feb." + laYear);
			headersEight = insert(headersEight, "Different catgory sell-out quantity_Feb." + toYear[0]);
		} else if (toYear[1].equals("03")) {

			headersEight = insert(headersEight, "Different catgory sell-out quantity_Mar." + laYear);
			headersEight = insert(headersEight, "Different catgory sell-out quantity_Mar." + toYear[0]);
		} else if (toYear[1].equals("04")) {
			headersEight = insert(headersEight, "Different catgory sell-out quantity_Apr." + laYear);
			headersEight = insert(headersEight, "Different catgory sell-out quantity_Apr." + toYear[0]);
		} else if (toYear[1].equals("05")) {
			headersEight = insert(headersEight, "Different catgory sell-out quantity_May." + laYear);
			headersEight = insert(headersEight, "Different catgory sell-out quantity_May." + toYear[0]);
		} else if (toYear[1].equals("06")) {

			headersEight = insert(headersEight, "Different catgory sell-out quantity_June." + laYear);
			headersEight = insert(headersEight, "Different catgory sell-out quantity_June." + toYear[0]);
		} else if (toYear[1].equals("07")) {
			headersEight = insert(headersEight, "Different catgory sell-out quantity_July." + laYear);
			headersEight = insert(headersEight, "Different catgory sell-out quantity_July." + toYear[0]);
		} else if (toYear[1].equals("08")) {
			headersEight = insert(headersEight, "Different catgory sell-out quantity_Aug." + laYear);
			headersEight = insert(headersEight, "Different catgory sell-out quantity_Aug." + toYear[0]);
		} else if (toYear[1].equals("09")) {

			headersEight = insert(headersEight, "Different catgory sell-out quantity_Sept." + laYear);
			headersEight = insert(headersEight, "Different catgory sell-out quantity_Sept." + toYear[0]);
		} else if (toYear[1].equals("10")) {

			headersEight = insert(headersEight, "Different catgory sell-out quantity_Oct." + laYear);
			headersEight = insert(headersEight, "Different catgory sell-out quantity_Oct." + toYear[0]);
		} else if (toYear[1].equals("11")) {

			headersEight = insert(headersEight, "Different catgory sell-out quantity_Nov." + laYear);
			headersEight = insert(headersEight, "Different catgory sell-out quantity_Nov." + toYear[0]);
		} else if (toYear[1].equals("12")) {

			headersEight = insert(headersEight, "Different catgory sell-out quantity_Dec." + laYear);
			headersEight = insert(headersEight, "Different catgory sell-out quantity_Dec." + toYear[0]);
		}

		headersEight = insert(headersEight, "Different catgory sell-out quantity_Growth");

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

		String[] headersNight = { "Growth rate_Country", "Growth rate_Category"

				// "Growth rate_TTL"

		};

		if (toYear[1].equals("01")) {
			headersNight = insert(headersNight, "Growth rate_Jan." + laYear);
			headersNight = insert(headersNight, "Growth rate_Jan." + toYear[0]);
		} else if (toYear[1].equals("02")) {
			headersNight = insert(headersNight, "Growth rate_Feb." + laYear);
			headersNight = insert(headersNight, "Growth rate_Feb." + toYear[0]);
		} else if (toYear[1].equals("03")) {
			headersNight = insert(headersNight, "Growth rate_Mar." + laYear);
			headersNight = insert(headersNight, "Growth rate_Mar." + toYear[0]);
		} else if (toYear[1].equals("04")) {
			headersNight = insert(headersNight, "Growth rate_Apr." + laYear);
			headersNight = insert(headersNight, "Growth rate_Apr." + toYear[0]);
		} else if (toYear[1].equals("05")) {
			headersNight = insert(headersNight, "Growth rate_May." + laYear);
			headersNight = insert(headersNight, "Growth rate_May." + toYear[0]);
		} else if (toYear[1].equals("06")) {
			headersNight = insert(headersNight, "Growth rate_June." + laYear);
			headersNight = insert(headersNight, "Growth rate_June." + toYear[0]);
		} else if (toYear[1].equals("07")) {
			headersNight = insert(headersNight, "Growth rate_July." + laYear);
			headersNight = insert(headersNight, "Growth rate_July." + toYear[0]);
		} else if (toYear[1].equals("08")) {
			headersNight = insert(headersNight, "Growth rate_Aug." + laYear);
			headersNight = insert(headersNight, "Growth rate_Aug." + toYear[0]);
		} else if (toYear[1].equals("09")) {
			headersNight = insert(headersNight, "Growth rate_Sept." + laYear);
			headersNight = insert(headersNight, "Growth rate_Sept." + toYear[0]);
		} else if (toYear[1].equals("10")) {
			headersNight = insert(headersNight, "Growth rate_Oct." + laYear);
			headersNight = insert(headersNight, "Growth rate_Oct." + toYear[0]);
		} else if (toYear[1].equals("11")) {
			headersNight = insert(headersNight, "Growth rate_Nov." + laYear);
			headersNight = insert(headersNight, "Growth rate_Nov." + toYear[0]);
		} else if (toYear[1].equals("12")) {
			headersNight = insert(headersNight, "Growth rate_Dec." + laYear);
			headersNight = insert(headersNight, "Growth rate_Dec." + toYear[0]);
		}

		headersNight = insert(headersNight, "Growth rate_Growth");

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
		String[] headersTen = { "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Country",
				"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series",
				"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Model"

		};

		if (toYear[1].equals("01")) {
			headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Jan." + laYear);
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Jan." + toYear[0]);
		} else if (toYear[1].equals("02")) {
			headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Feb." + laYear);
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Feb." + toYear[0]);
		} else if (toYear[1].equals("03")) {
			headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Mar." + laYear);
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Mar." + toYear[0]);
		} else if (toYear[1].equals("04")) {
			headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Apr." + laYear);
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Apr." + toYear[0]);
		} else if (toYear[1].equals("05")) {
			headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_May." + laYear);
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_May." + toYear[0]);
		} else if (toYear[1].equals("06")) {
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_June." + laYear);
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_June." + toYear[0]);
		} else if (toYear[1].equals("07")) {
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_July." + laYear);
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_July." + toYear[0]);
		} else if (toYear[1].equals("08")) {
			headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Aug." + laYear);
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Aug." + toYear[0]);
		} else if (toYear[1].equals("09")) {
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Sept." + laYear);
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Sept." + toYear[0]);
		} else if (toYear[1].equals("10")) {
			headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Oct." + laYear);
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Oct." + toYear[0]);
		} else if (toYear[1].equals("11")) {

			headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Nov." + laYear);
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Nov." + toYear[0]);
		} else if (toYear[1].equals("12")) {
			headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Dec." + laYear);
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Dec." + toYear[0]);
		}

		headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth");

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
		BigDecimal bd = BigDecimal.ZERO;

		HashMap<String, Object> datas = new HashMap<String, Object>();
		HashMap<String, Object> dataTwos = new HashMap<String, Object>();
		HashMap<String, Object> dataSixs = new HashMap<String, Object>();

		BDDateUtil.year = Integer.parseInt(days[0]) - 1;
		BDDateUtil.month = Integer.parseInt(days[1]);

		beginDate = BDDateUtil.year + "-" + days[1] + "-01";
		endDate = BDDateUtil.getLastDayOfMonth(BDDateUtil.year, BDDateUtil.month);
		List<HashMap<String, Object>> laExcels = excelDao.selectDatas(searchStr, conditions, beginDate, endDate,
				WebPageUtil.isHQRole());
		lastDayt = BDDateUtil.getLastDayOfMonth(BDDateUtil.year, BDDateUtil.month).split("-");
		day = Integer.parseInt(lastDayt[2]);
		String key = "";
		BigDecimal qtyTotalLast = BigDecimal.ZERO;
		BigDecimal amtTotalLast = BigDecimal.ZERO;
		for (int i = 0; i < laExcels.size(); i++) {
			bd = new BigDecimal(laExcels.get(i).get("saleQty").toString());
			qtyTotalLast = qtyTotalLast.add(new BigDecimal(laExcels.get(i).get("saleQty").toString()));
			amtTotalLast = amtTotalLast.add(new BigDecimal(laExcels.get(i).get("saleAmt").toString()));
			key = laExcels.get(i).get("country_name").toString();
			datas.put(key, Math.round(bd.doubleValue()));
			dataTwos.put(key, Math.round(bd.doubleValue() / day));
			
			BigDecimal numOne = mapAvg.get(laExcels.get(i).get("country_name")+"_Last");
			if (numOne == null) {
				numOne = BigDecimal.ZERO;
			}
			
			mapAvg.put(laExcels.get(i).get("country_name")+"_Last", numOne.add( new BigDecimal(Math.round(bd.doubleValue() / day))));

		}
		datas.put("Total", Math.round(qtyTotalLast.doubleValue()));
		datas.put("Amount", Math.round(amtTotalLast.doubleValue()));
		dataTwos.put("Total", Math.round(qtyTotalLast.doubleValue()/ day));
		dataTwos.put("Amount", Math.round(amtTotalLast.doubleValue()/ day));
		
		
		String[] sizeList = { "20-28", "32", "39-43", "48-50", "55", ">=60" };

		String[] str = {};
		countryList = str;
		double gro=0.0;
		if (laExcels.size() > excels.size()) {
			for (int i = 0; i < laExcels.size(); i++) {
				key = laExcels.get(i).get("country_name").toString();
				if(mapAvg.get(key+"_Last")!=null) {
					if(mapAvg.get(key+"_Now")!=null) {
						BigDecimal now=mapAvg.get(key+"_Now");
						BigDecimal last=mapAvg.get(key+"_Last");
						gro=(now.doubleValue()-last.doubleValue())/last.doubleValue()*100;
						if(last.doubleValue()==0.0 || last.doubleValue()==0) {
							dataSixs.put(key, "100%");
						}else {
							dataSixs.put(key, Math.round(gro)+"%");
						}
					}
				}else {
					dataSixs.put(key, "100%");
				}

				headers = insert(headers, laExcels.get(i).get("country_name").toString());
				countryList = insert(countryList, laExcels.get(i).get("country_name").toString());
				dataThrees.put(laExcels.get(i).get("country_name").toString(),
						laExcels.get(i).get("country_name").toString());
			}
			
			if(qtyTotalLast.doubleValue() / day!=0.0 && qtyTotalLast.doubleValue() / day!=0) {
			double c=	(qtyTotal.doubleValue() / day) -(qtyTotalLast.doubleValue() / day);
			gro=c/(qtyTotalLast.doubleValue() / day)*100;
			dataSixs.put("Total",Math.round(gro)+"%");
			}else {
				dataSixs.put("Total", "100%");
			}
			
			if(amtTotalLast.doubleValue() / day!=0.0 && amtTotalLast.doubleValue() / day!=0) {
				double c=	(amtTotal.doubleValue() / day) -(amtTotalLast.doubleValue() / day);
				gro=c/(amtTotalLast.doubleValue() / day)*100;
				dataSixs.put("Amount",Math.round(gro)+"%");
				}else {
					dataSixs.put("Amount", "100%");
				}
			

		} else {
			for (int i = 0; i < excels.size(); i++) {
				key = excels.get(i).get("country_name").toString();
				if(mapAvg.get(key+"_Last")!=null) {
					if(mapAvg.get(key+"_Now")!=null) {
						BigDecimal now=mapAvg.get(key+"_Now");
						BigDecimal last=mapAvg.get(key+"_Last");
						gro=(now.doubleValue()-last.doubleValue())/last.doubleValue()*100;
						if(last.doubleValue()==0.0 || last.doubleValue()==0) {
							dataSixs.put(key, "100%");
						}else {
							dataSixs.put(key, Math.round(gro)+"%");
						}
					}
				}else {
					dataSixs.put(key, "100%");
				}
				
				headers = insert(headers, excels.get(i).get("country_name").toString());
				countryList = insert(countryList, excels.get(i).get("country_name").toString());
				dataThrees.put(excels.get(i).get("country_name").toString(),
						excels.get(i).get("country_name").toString());
			}
			if(qtyTotalLast.doubleValue() / day!=0.0 && qtyTotalLast.doubleValue() / day!=0) {
				double c=	(qtyTotal.doubleValue() / day) -(qtyTotalLast.doubleValue() / day);
				gro=c/(qtyTotalLast.doubleValue() / day)*100;
				dataSixs.put("Total",Math.round(gro)+"%");
				}else {
					dataSixs.put("Total", "100%");
				}
				
				if(amtTotalLast.doubleValue() / day!=0.0 && amtTotalLast.doubleValue() / day!=0) {
					double c=	(amtTotal.doubleValue() / day) -(amtTotalLast.doubleValue() / day);
					gro=c/(amtTotalLast.doubleValue() / day)*100;
					dataSixs.put("Amount",Math.round(gro)+"%");
					}else {
						dataSixs.put("Amount", "100%");
					}
				

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



		start = System.currentTimeMillis();

		datas.put(sdf.format(daysOne) + "." + laYear, "TTL QTY");
		dataTwos.put(sdf.format(daysOne) + "." + laYear, "Ave/day");
		dataSixs.put(sdf.format(daysOne) + "." + laYear, "Sellout Growth per day");

		dataList.add(datas);
		dataList.add(dataTwos);
		dataList.add(dataThrees);
		dataList.add(datFours);
		dataList.add(dataFives);
		dataList.add(dataSixs);

		System.out.println("=====第4个查询=" + (System.currentTimeMillis() - start));

		LinkedList<HashMap<String, Object>> dataListFive = new LinkedList<HashMap<String, Object>>();
		LinkedList<HashMap<String, Object>> dataListSix = new LinkedList<HashMap<String, Object>>();
		start = System.currentTimeMillis();

		String Month = "";

		if (toYear[1].equals("01")) {
			Month = "Jan.";
		} else if (toYear[1].equals("02")) {
			Month = "Feb.";
		} else if (toYear[1].equals("03")) {
			Month = "Mar.";
		} else if (toYear[1].equals("04")) {
			Month = "Apr.";
		} else if (toYear[1].equals("05")) {
			Month = "May.";
		} else if (toYear[1].equals("06")) {
			Month = "June.";
		} else if (toYear[1].equals("07")) {
			Month = "July.";
		} else if (toYear[1].equals("08")) {
			Month = "Aug.";
		} else if (toYear[1].equals("09")) {
			Month = "Sept.";
		} else if (toYear[1].equals("10")) {
			Month = "Oct.";
		} else if (toYear[1].equals("11")) {
			Month = "Nov.";
		} else if (toYear[1].equals("12")) {
			Month = "Dec.";
		}

		List<HashMap<String, Object>> sizeDatasOne = excelDao.selectSaleDataBySize(beginDateOne, endDateOne, searchStr,
				conditions, WebPageUtil.isHQRole());

		List<HashMap<String, Object>> sizeDatasOneData = excelDao.selectSaleTotalBySize(beginDateOne, endDateOne,
				searchStr, conditions, WebPageUtil.isHQRole());

		beginDate = laYear + "-" + days[1] + "-01";
		endDate = BDDateUtil.getLastDayOfMonth(laYear, BDDateUtil.month);

		List<HashMap<String, Object>> sizeDatasOneLast = excelDao.selectSaleDataBySize(beginDate, endDate, searchStr,
				conditions, WebPageUtil.isHQRole());

		List<HashMap<String, Object>> sizeDatasOneDataLast = excelDao.selectSaleTotalBySize(beginDate, endDate,
				searchStr, conditions, WebPageUtil.isHQRole());

		BigDecimal lastQty = BigDecimal.ZERO;

		String nowKey = "YTD-" + toYear[0] + "  Monthly sellout trend per size_" + Month + "" + toYear[0];
		String lastKey = "YTD-" + toYear[0] + "  Monthly sellout trend per size_" + Month + "" + laYear;

		String proNow = "Market Share_" + Month + "" + toYear[0];

		String proLast = "Market Share_" + Month + "" + laYear;

		Map<String, BigDecimal> mapResultOne = new HashMap<String, BigDecimal>();

		for (int i = 0; i < countryList.length; i++) {
			BigDecimal bdTotal = BigDecimal.ZERO;
			BigDecimal lastQtyTotal = BigDecimal.ZERO;
			HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();
			HashMap<String, Object> dataSixTotal = new HashMap<String, Object>();
			for (int j = 0; j < sizeList.length; j++) {
				bd = BigDecimal.ZERO;
				lastQty = BigDecimal.ZERO;
				bdTotal = BigDecimal.ZERO;
				lastQtyTotal = BigDecimal.ZERO;
				HashMap<String, Object> datasix = new HashMap<String, Object>();
				HashMap<String, Object> dataFive = new HashMap<String, Object>();
				dataFive.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Country", countryList[i]);
				dataFive.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Category", sizeList[j]);

				datasix.put("Market Share_Country", countryList[i]);
				datasix.put("Market Share_Category", sizeList[j]);
				if (sizeDatasOneData.size() > 0) {
					for (int k = 0; k < sizeDatasOneData.size(); k++) {
						if (sizeDatasOneData.get(k).get("party_name").toString().equals(countryList[i])) {
							bdTotal = new BigDecimal(sizeDatasOneData.get(k).get("saleQty").toString());
						}
					}
				} else {
					bdTotal = BigDecimal.ZERO;
				}

				for (int c = 0; c < sizeDatasOne.size(); c++) {
					
					if (sizeDatasOne.get(c).get("party_name").toString().equals(countryList[i])
							&& sizeDatasOne.get(c).get("sizeT").toString().equals(sizeList[j])) {
						bd = new BigDecimal(sizeDatasOne.get(c).get("saleQty").toString());
						dataFive.put(nowKey, Math.round(bd.doubleValue()));
						datasix.put(proNow, Math.round(bd.doubleValue() / bdTotal.doubleValue() * 100) + "%");
						BigDecimal numOne = mapResultOne.get(sizeList[j] + "_" + nowKey);
						BigDecimal numOneSize = mapResultOne.get(nowKey);
						if (numOne == null) {
							numOne = BigDecimal.ZERO;
						}
						if (numOneSize == null) {
							numOneSize = BigDecimal.ZERO;
						}
						mapResultOne.put(sizeList[j] + "_" + nowKey, numOne.add(bd));
						mapResultOne.put(nowKey, numOneSize.add(bd));

					}
				}
				if (sizeDatasOneDataLast.size() > 0) {
					for (int k = 0; k < sizeDatasOneDataLast.size(); k++) {
						if (sizeDatasOneDataLast.get(k).get("party_name").toString().equals(countryList[i])) {
							lastQtyTotal = new BigDecimal(sizeDatasOneDataLast.get(k).get("saleQty").toString());
						}
					}
				} else {
					lastQtyTotal = BigDecimal.ZERO;
				}

				dataFiveTotal.put(nowKey, Math.round(bdTotal.doubleValue()));
				for (int c = 0; c < sizeDatasOneLast.size(); c++) {
					

					if (sizeDatasOneLast.get(c).get("party_name").toString().equals(countryList[i])
							&& sizeDatasOneLast.get(c).get("sizeT").toString().equals(sizeList[j])) {
						lastQty = new BigDecimal(sizeDatasOneLast.get(c).get("saleQty").toString());
						dataFive.put(lastKey, Math.round(lastQty.doubleValue()));
						datasix.put(proNow, Math.round(lastQty.doubleValue() / lastQtyTotal.doubleValue() * 100) + "%");
						BigDecimal numOne = mapResultOne.get(sizeList[j] + "_" + lastKey);
						BigDecimal numOneSize = mapResultOne.get(lastKey);
						if (numOne == null) {
							numOne = BigDecimal.ZERO;
						}
						if (numOneSize == null) {
							numOneSize = BigDecimal.ZERO;
						}
						mapResultOne.put(sizeList[j] + "_" + lastKey, numOne.add(lastQty));
						mapResultOne.put(lastKey, numOneSize.add(lastQty));

					}
				}
				dataFiveTotal.put(lastKey, Math.round(lastQtyTotal.doubleValue()));
				// 同比增长率=（本期数－同期数）÷同期数×100%
				if (Math.round(bd.doubleValue()) == 0) {
					dataFive.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "-100%");
					datasix.put("Market Share_Growth", "-100%");
				} else if (Math.round(lastQty.doubleValue()) == 0) {
					dataFive.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "100%");
					datasix.put("Market Share_Growth", "100%");
				} else {
					double poor = (bd.doubleValue() - lastQty.doubleValue()) / lastQty.doubleValue() * 100;
					dataFive.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth",
							Math.round(poor) + "%");
					datasix.put("Market Share_Growth", Math.round(poor) + "%");
				}

				dataListFive.add(dataFive);
				dataListSix.add(datasix);
			}
			dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Category", "Total");
			if(lastQtyTotal.doubleValue()==0.0) {
				dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "100%");
				
			}else {
				gro=(bdTotal.doubleValue()-lastQtyTotal.doubleValue())/lastQtyTotal.doubleValue()*100;
				dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", Math.round(gro)+"%");
			}
			
			dataListFive.add(dataFiveTotal);

			dataSixTotal.put("Market Share_Category", "Total");
			dataSixTotal.put(proNow, "100%");
			dataSixTotal.put(proLast, "100%");
			dataSixTotal.put("Market Share_Growth", "100%");
			dataListSix.add(dataSixTotal);

			if (WebPageUtil.isHQRole()) {
				dataFiveTotal = new HashMap<String, Object>();
				dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Country", "Country");
				dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Category", "Category");
				dataFiveTotal.put(nowKey, Month + "" + toYear[0]);
				dataFiveTotal.put(lastKey, Month + "" + laYear);
				dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "Growth");
				dataListFive.add(dataFiveTotal);
				dataSixTotal = new HashMap<String, Object>();
				dataSixTotal.put("Market Share_Category", "Category");
				dataSixTotal.put("Market Share_Country", "Country");
				dataSixTotal.put(proNow, Month + "" + toYear[0]);
				dataSixTotal.put(proLast, Month + "" + laYear);
				dataSixTotal.put("Market Share_Growth", "Growth");
				dataListSix.add(dataSixTotal);
			} else {
				if (countryList.length - 1 != i) {
					dataFiveTotal = new HashMap<String, Object>();
					dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Country", "Country");
					dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Category", "Category");
					dataFiveTotal.put(nowKey, Month + "" + toYear[0]);
					dataFiveTotal.put(lastKey, Month + "" + laYear);
					dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "Growth");
					dataListFive.add(dataFiveTotal);
					dataSixTotal = new HashMap<String, Object>();
					dataSixTotal.put("Market Share_Country", "Country");
					dataSixTotal.put("Market Share_Category", "Category");
					dataSixTotal.put(proNow, Month + "" + toYear[0]);
					dataSixTotal.put(proLast, Month + "" + laYear);
					dataSixTotal.put("Market Share_Growth", "Growth");
					dataListSix.add(dataSixTotal);
				}
			}

		}

		if (WebPageUtil.isHQRole()) {
			BigDecimal bdTotal = BigDecimal.ZERO;
			BigDecimal lastTotal = BigDecimal.ZERO;

			if (mapResultOne.get(nowKey) != null) {
				bdTotal = mapResultOne.get(nowKey);
			}
			if (mapResultOne.get(lastKey) != null) {
				lastTotal = mapResultOne.get(lastKey);
			}
			for (int k = 0; k < sizeList.length; k++) {
				HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();
				HashMap<String, Object> dataSixTotal = new HashMap<String, Object>();
				dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Country", "BDSC");
				dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Category", sizeList[k]);

				dataSixTotal.put("Market Share_Country", "BDSC");
				dataSixTotal.put("Market Share_Category", sizeList[k]);

				if (mapResultOne.get(sizeList[k] + "_" + nowKey) != null) {
					bd = mapResultOne.get(sizeList[k] + "_" + nowKey);
				} else {
					bd = BigDecimal.ZERO;
				}
				if (mapResultOne.get(sizeList[k] + "_" + lastKey) != null) {
					lastQty = mapResultOne.get(sizeList[k] + "_" + lastKey);
				} else {
					lastQty = BigDecimal.ZERO;
				}

				dataFiveTotal.put(lastKey, Math.round(lastQty.doubleValue()));
				dataFiveTotal.put(nowKey, Math.round(bd.doubleValue()));
				if (Math.round(bd.doubleValue()) == 0) {
					dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "-100%");
					dataSixTotal.put("Market Share_Growth", "-100%");
				} else if (Math.round(lastQty.doubleValue()) == 0) {
					dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "100%");
					dataSixTotal.put("Market Share_Growth", "100%");
				} else {
					double poor = (bd.doubleValue() - lastQty.doubleValue()) / lastQty.doubleValue() * 100;
					dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth",
							Math.round(poor) + "%");
					dataSixTotal.put("Market Share_Growth", Math.round(poor) + "%");
				}
				dataSixTotal.put(proNow, Math.round(bd.doubleValue() / bdTotal.doubleValue() * 100) + "%");
				dataSixTotal.put(proLast, Math.round(lastQty.doubleValue() / lastTotal.doubleValue() * 100) + "%");
				dataListFive.add(dataFiveTotal);
				dataListSix.add(dataSixTotal);
			}

			HashMap<String, Object> dataFiveTotals = new HashMap<String, Object>();
			HashMap<String, Object> dataSixTotals = new HashMap<String, Object>();
			dataFiveTotals.put(nowKey, Math.round(bdTotal.doubleValue()));
			dataFiveTotals.put(lastKey, Math.round(lastTotal.doubleValue()));
			dataFiveTotals.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Category", "Total");
			dataFiveTotals.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "100%");
			dataListFive.add(dataFiveTotals);

			dataSixTotals.put("Market Share_Category", "Total");
			dataSixTotals.put(proNow, "100%");
			dataSixTotals.put(proLast, "100%");
			dataSixTotals.put("Market Share_Growth", "100%");
			dataListSix.add(dataSixTotals);

		}

		/*
		 * dataFiveTotals= new HashMap<String, Object>(); dataFiveTotals.put("YTD-" +
		 * toYear[0] + "  Monthly sellout trend per size_Category", "Category");
		 * dataFiveTotals.put(nowKey, Month+""+toYear[0]); dataFiveTotals.put(lastKey,
		 * Month+""+laYear); dataFiveTotals.put("YTD-" + toYear[0] +
		 * "  Monthly sellout trend per size_Growth", "Growth");
		 * dataListFive.add(dataFiveTotals); dataSixTotals= new HashMap<String,
		 * Object>(); dataSixTotals.put("Market Share_Category", "Category");
		 * dataSixTotals.put(proNow, Month+""+toYear[0]); dataSixTotals.put(proLast,
		 * Month+""+laYear); dataSixTotals.put("Market Share_Growth", "Growth");
		 * dataListSix.add(dataSixTotals);
		 * 
		 */

		LinkedList<HashMap<String, Object>> dataListEight = new LinkedList<HashMap<String, Object>>();

		LinkedList<HashMap<String, Object>> dataListNight = new LinkedList<HashMap<String, Object>>();

		List<HashMap<String, Object>> specDatasOne = excelDao.selectSaleDataBySpec(beginDateOne, endDateOne, searchStr,
				conditions, WebPageUtil.isHQRole());

		List<HashMap<String, Object>> specDatasOneData = excelDao.selectSaleTotalBySpec(beginDateOne, endDateOne,
				searchStr, conditions, WebPageUtil.isHQRole());

		beginDate = laYear + "-" + days[1] + "-01";
		endDate = BDDateUtil.getLastDayOfMonth(laYear, BDDateUtil.month);

		List<HashMap<String, Object>> specDatasOneLast = excelDao.selectSaleDataBySpec(beginDate, endDate, searchStr,
				conditions, WebPageUtil.isHQRole());

		List<HashMap<String, Object>> specDatasOneDataLast = excelDao.selectSaleTotalBySpec(beginDate, endDate,
				searchStr, conditions, WebPageUtil.isHQRole());

		lastQty = BigDecimal.ZERO;

		nowKey = "Different catgory sell-out quantity_" + Month + "" + toYear[0];
		lastKey = "Different catgory sell-out quantity_" + Month + "" + laYear;

		proNow = "Growth rate_" + Month + "" + toYear[0];

		proLast = "Growth rate_" + Month + "" + laYear;
		Map<String, BigDecimal> mapResultTwo = new HashMap<String, BigDecimal>();

		for (int i = 0; i < countryList.length; i++) {
			BigDecimal bdTotal = BigDecimal.ZERO;
			BigDecimal lastQtyTotal = BigDecimal.ZERO;
			HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();
			HashMap<String, Object> dataSixTotal = new HashMap<String, Object>();
			for (int j = 0; j < specList.length; j++) {
				bd = BigDecimal.ZERO;
				lastQty = BigDecimal.ZERO;
				bdTotal = BigDecimal.ZERO;
				lastQtyTotal = BigDecimal.ZERO;
				HashMap<String, Object> datasix = new HashMap<String, Object>();
				HashMap<String, Object> dataFive = new HashMap<String, Object>();
				dataFive.put("Different catgory sell-out quantity_Country", countryList[i]);
				dataFive.put("Different catgory sell-out quantity_Category", specList[j]);

				datasix.put("Growth rate_Country", countryList[i]);
				datasix.put("Growth rate_Category", specList[j]);
				if (specDatasOneData.size() > 0) {
					for (int k = 0; k < specDatasOneData.size(); k++) {
						if (specDatasOneData.get(k).get("party_name").toString().equals(countryList[i])) {
							bdTotal = new BigDecimal(specDatasOneData.get(k).get("saleQty").toString());
						}
					}
				} else {
					bdTotal = BigDecimal.ZERO;
				}

				for (int c = 0; c < specDatasOne.size(); c++) {
				

					if (specDatasOne.get(c).get("party_name").toString().equals(countryList[i])
							&& specDatasOne.get(c).get("SPEC").toString().equals(specList[j])) {
						bd = new BigDecimal(specDatasOne.get(c).get("saleQty").toString());
						dataFive.put(nowKey, Math.round(bd.doubleValue()));
						datasix.put(proNow, Math.round(bd.doubleValue() / bdTotal.doubleValue() * 100) + "%");
						BigDecimal numOne = mapResultTwo.get(specList[j] + "_" + nowKey);
						BigDecimal numOneSize = mapResultTwo.get(nowKey);
						if (numOne == null) {
							numOne = BigDecimal.ZERO;
						}
						if (numOneSize == null) {
							numOneSize = BigDecimal.ZERO;
						}
						mapResultTwo.put(specList[j] + "_" + nowKey, numOne.add(bd));
						mapResultTwo.put(nowKey, numOneSize.add(bd));

					}
				}
				if (specDatasOneDataLast.size() > 0) {
					for (int k = 0; k < specDatasOneDataLast.size(); k++) {
						if (specDatasOneDataLast.get(k).get("party_name").toString().equals(countryList[i])) {
							lastQtyTotal = new BigDecimal(specDatasOneDataLast.get(k).get("saleQty").toString());
						}
					}
				} else {
					lastQtyTotal = BigDecimal.ZERO;
				}

				dataFiveTotal.put(nowKey, Math.round(bdTotal.doubleValue()));
				for (int c = 0; c < specDatasOneLast.size(); c++) {
					

					if (specDatasOneLast.get(c).get("party_name").toString().equals(countryList[i])
							&& specDatasOneLast.get(c).get("SPEC").toString().equals(specList[j])) {
						lastQty = new BigDecimal(specDatasOneLast.get(c).get("saleQty").toString());
						dataFive.put(lastKey, Math.round(lastQty.doubleValue()));
						datasix.put(proLast,
								Math.round(lastQty.doubleValue() / lastQtyTotal.doubleValue() * 100) + "%");

						BigDecimal numOne = mapResultTwo.get(specList[j] + "_" + lastKey);
						BigDecimal numOneSize = mapResultTwo.get(lastKey);
						if (numOne == null) {
							numOne = BigDecimal.ZERO;
						}
						if (numOneSize == null) {
							numOneSize = BigDecimal.ZERO;
						}
						mapResultTwo.put(specList[j] + "_" + lastKey, numOne.add(lastQty));
						mapResultTwo.put(lastKey, numOneSize.add(lastQty));

					}
				}

				dataFiveTotal.put(lastKey, Math.round(lastQtyTotal.doubleValue()));
				// 同比增长率=（本期数－同期数）÷同期数×100%
				if (Math.round(bd.doubleValue()) == 0) {
					dataFive.put("Different catgory sell-out quantity_Growth", "-100%");
					datasix.put("Growth rate_Growth", "-100%");
				} else if (Math.round(lastQty.doubleValue()) == 0) {
					dataFive.put("Different catgory sell-out quantity_Growth", "100%");
					datasix.put("Growth rate_Growth", "100%");
				} else {
					double poor = (bd.doubleValue() - lastQty.doubleValue()) / lastQty.doubleValue() * 100;
					dataFive.put("Different catgory sell-out quantity_Growth", Math.round(poor) + "%");
					datasix.put("Growth rate_Growth", Math.round(poor) + "%");
				}

				dataListEight.add(dataFive);
				dataListNight.add(datasix);
			}
			dataFiveTotal.put("Different catgory sell-out quantity_Category", "Total");

			dataFiveTotal.put("Different catgory sell-out quantity_Growth", "100%");
			dataListEight.add(dataFiveTotal);

			dataSixTotal.put("Growth rate_Category", "Total");
			dataSixTotal.put(proNow, "100%");
			dataSixTotal.put(proLast, "100%");
			dataSixTotal.put("Growth rate_Growth", "100%");
			dataListNight.add(dataSixTotal);

			if (WebPageUtil.isHQRole()) {
				dataFiveTotal = new HashMap<String, Object>();
				dataFiveTotal.put("Different catgory sell-out quantity_Country", "Country");
				dataFiveTotal.put("Different catgory sell-out quantity_Category", "Category");
				dataFiveTotal.put(nowKey, Month + "" + toYear[0]);
				dataFiveTotal.put(lastKey, Month + "" + laYear);
				dataFiveTotal.put("Different catgory sell-out quantity_Growth", "Growth");
				dataListEight.add(dataFiveTotal);
				dataSixTotal = new HashMap<String, Object>();
				dataSixTotal.put("Growth rate_Country", "Country");
				dataSixTotal.put("Growth rate_Category", "Category");
				dataSixTotal.put(proNow, Month + "" + toYear[0]);
				dataSixTotal.put(proLast, Month + "" + laYear);
				dataSixTotal.put("Growth rate_Growth", "Growth");
				dataListNight.add(dataSixTotal);
			} else {
				if (countryList.length - 1 != i) {
					dataFiveTotal = new HashMap<String, Object>();
					dataFiveTotal.put("Different catgory sell-out quantity_Country", "Country");
					dataFiveTotal.put("Different catgory sell-out quantity_Category", "Category");
					dataFiveTotal.put(nowKey, Month + "" + toYear[0]);
					dataFiveTotal.put(lastKey, Month + "" + laYear);
					dataFiveTotal.put("Different catgory sell-out quantity_Growth", "Growth");
					dataListEight.add(dataFiveTotal);
					dataSixTotal = new HashMap<String, Object>();
					dataSixTotal.put("Growth rate_Country", "Country");
					dataSixTotal.put("Growth rate_Category", "Category");
					dataSixTotal.put(proNow, Month + "" + toYear[0]);
					dataSixTotal.put(proLast, Month + "" + laYear);
					dataSixTotal.put("Growth rate_Growth", "Growth");
					dataListNight.add(dataSixTotal);
				}
			}

		}
		if (WebPageUtil.isHQRole()) {
			BigDecimal bdTotal = BigDecimal.ZERO;
			BigDecimal lastTotal = BigDecimal.ZERO;

			if (mapResultTwo.get(nowKey) != null) {
				bdTotal = mapResultTwo.get(nowKey);
			}
			if (mapResultTwo.get(lastKey) != null) {
				lastTotal = mapResultTwo.get(lastKey);
			}
			for (int k = 0; k < specList.length; k++) {
				HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();
				HashMap<String, Object> dataSixTotal = new HashMap<String, Object>();

				dataFiveTotal.put("Different catgory sell-out quantity_Country", "BDSC");
				dataFiveTotal.put("Different catgory sell-out quantity_Category", specList[k]);

				dataSixTotal.put("Growth rate_Country", "BDSC");
				dataSixTotal.put("Growth rate_Category", specList[k]);

				if (mapResultTwo.get(specList[k] + "_" + nowKey) != null) {
					bd = mapResultTwo.get(specList[k] + "_" + nowKey);
				} else {
					bd = BigDecimal.ZERO;
				}
				if (mapResultTwo.get(specList[k] + "_" + lastKey) != null) {
					lastQty = mapResultTwo.get(specList[k] + "_" + lastKey);
				} else {
					lastQty = BigDecimal.ZERO;
				}

				dataFiveTotal.put(lastKey, Math.round(lastQty.doubleValue()));
				dataFiveTotal.put(nowKey, Math.round(bd.doubleValue()));
				if (Math.round(bd.doubleValue()) == 0) {
					dataFiveTotal.put("Different catgory sell-out quantity_Growth", "-100%");
					dataSixTotal.put("Growth rate_Growth", "-100%");
				} else if (Math.round(lastQty.doubleValue()) == 0) {
					dataFiveTotal.put("Different catgory sell-out quantity_Growth", "100%");
					dataSixTotal.put("Growth rate_Growth", "100%");
				} else {
					double poor = (bd.doubleValue() - lastQty.doubleValue()) / lastQty.doubleValue() * 100;
					dataFiveTotal.put("Different catgory sell-out quantity_Growth", Math.round(poor) + "%");
					dataSixTotal.put("Growth rate_Growth", Math.round(poor) + "%");
				}
				dataSixTotal.put(proNow, Math.round(bd.doubleValue() / bdTotal.doubleValue() * 100) + "%");
				dataSixTotal.put(proLast, Math.round(lastQty.doubleValue() / lastTotal.doubleValue() * 100) + "%");
				dataListEight.add(dataFiveTotal);
				dataListNight.add(dataSixTotal);
			}

			HashMap<String, Object> dataFiveTotals = new HashMap<String, Object>();
			HashMap<String, Object> dataSixTotals = new HashMap<String, Object>();
			dataFiveTotals.put(nowKey, Math.round(bdTotal.doubleValue()));
			dataFiveTotals.put(lastKey, Math.round(lastTotal.doubleValue()));
			dataFiveTotals.put("Different catgory sell-out quantity_Category", "Total");
			dataFiveTotals.put("Different catgory sell-out quantity_Growth", "100%");
			dataListEight.add(dataFiveTotals);

			dataSixTotals.put("Growth rate_Category", "Total");
			dataSixTotals.put(proNow, "100%");
			dataSixTotals.put(proLast, "100%");
			dataSixTotals.put("Growth rate_Growth", "100%");
			dataListNight.add(dataSixTotals);

		}

		LinkedList<HashMap<String, Object>> dataListTen = new LinkedList<HashMap<String, Object>>();

		List<HashMap<String, Object>> XCPDatasOne = excelDao.selectSaleDataByXCP(beginDateOne, endDateOne, searchStr,
				conditions, WebPageUtil.isHQRole());

		List<HashMap<String, Object>> XCPDatasOneData = excelDao.selectSaleTotalByXCP(beginDateOne, endDateOne,
				searchStr, conditions, WebPageUtil.isHQRole());

		HashMap<String, ArrayList<HashMap<String, Object>>> nowMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
		for (int m = 0; m < XCPDatasOne.size(); m++) {
			if (nowMap.get(XCPDatasOne.get(m).get("model").toString()) == null) {
				ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
				modelList.add(XCPDatasOne.get(m));
				nowMap.put(XCPDatasOne.get(m).get("model").toString(), modelList);
			} else {
				ArrayList<HashMap<String, Object>> modelList = nowMap.get(XCPDatasOne.get(m).get("model").toString());
				modelList.add(XCPDatasOne.get(m));
			}

		}

		beginDate = laYear + "-" + days[1] + "-01";
		endDate = BDDateUtil.getLastDayOfMonth(laYear, BDDateUtil.month);

		List<HashMap<String, Object>> XCPDatasOneLast = excelDao.selectSaleDataByXCP(beginDate, endDate, searchStr,
				conditions, WebPageUtil.isHQRole());

		List<HashMap<String, Object>> XCPDatasOneDataLast = excelDao.selectSaleTotalByXCP(beginDate, endDate, searchStr,
				conditions, WebPageUtil.isHQRole());

		HashMap<String, ArrayList<HashMap<String, Object>>> lastMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
		for (int m = 0; m < XCPDatasOneLast.size(); m++) {
			if (lastMap.get(XCPDatasOneLast.get(m).get("model").toString()) == null) {
				ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
				modelList.add(XCPDatasOneLast.get(m));
				lastMap.put(XCPDatasOneLast.get(m).get("model").toString(), modelList);
			} else {
				ArrayList<HashMap<String, Object>> modelList = lastMap
						.get(XCPDatasOneLast.get(m).get("model").toString());
				modelList.add(XCPDatasOneLast.get(m));
			}

		}

		lastQty = BigDecimal.ZERO;

		nowKey = "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_" + Month + "" + toYear[0];
		lastKey = "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_" + Month + "" + laYear;

		BigDecimal bdTotal = BigDecimal.ZERO;
		BigDecimal lastQtyTotal = BigDecimal.ZERO;

		bd = BigDecimal.ZERO;
		lastQty = BigDecimal.ZERO;

		String partysNameLast = "";
		String partysNameNow = "";
		for (int c = 0; c < XCPDatasOne.size(); c++) {
			HashMap<String, Object> dataFive = new HashMap<String, Object>();

			dataFive.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Country",
					XCPDatasOne.get(c).get("party_name").toString());
			bd = new BigDecimal(XCPDatasOne.get(c).get("saleQty").toString());

			dataFive.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series",
					XCPDatasOne.get(c).get("LINE"));
			dataFive.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Model",
					XCPDatasOne.get(c).get("model"));
			dataFive.put(nowKey, Math.round(bd.doubleValue()));

			for (int s = 0; s < XCPDatasOneLast.size(); s++) {
			
				if (XCPDatasOneLast.get(s).get("party_name").toString()
						.equals(XCPDatasOne.get(c).get("party_name").toString())
						&& XCPDatasOneLast.get(s).get("model").toString().equals(XCPDatasOne.get(c).get("model"))) {
					lastQty = new BigDecimal(XCPDatasOneLast.get(s).get("saleQty").toString());
					dataFive.put(lastKey, Math.round(lastQty.doubleValue()));

				}
			}

			// 同比增长率=（本期数－同期数）÷同期数×100%
			if (Math.round(bd.doubleValue()) == 0) {
				dataFive.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "-100%");
			} else if (Math.round(lastQty.doubleValue()) == 0) {
				dataFive.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "100%");
			} else {
				double poor = (bd.doubleValue() - lastQty.doubleValue()) / lastQty.doubleValue() * 100;
				dataFive.put("YTD-" + toYear[0] + " Monthly sellout trend per size_Growth", Math.round(poor) + "%");
			}

			dataListTen.add(dataFive);

			partysNameNow = XCPDatasOne.get(c).get("party_name").toString();
			if (XCPDatasOne.size() - 1 != c) {
				partysNameLast = XCPDatasOne.get(c + 1).get("party_name").toString();

				if (!partysNameNow.equals(partysNameLast)) {
					HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();
					if (XCPDatasOneData.size() > 0) {
						for (int k = 0; k < XCPDatasOneData.size(); k++) {
							if (XCPDatasOneData.get(k).get("party_name").toString().equals(partysNameNow)) {
								bdTotal = new BigDecimal(XCPDatasOneData.get(k).get("saleQty").toString());
							}
						}
					} else {
						bdTotal = BigDecimal.ZERO;
					}

					if (XCPDatasOneDataLast.size() > 0) {
						for (int k = 0; k < XCPDatasOneDataLast.size(); k++) {
							if (XCPDatasOneDataLast.get(k).get("party_name").toString().equals(partysNameNow)) {
								lastQtyTotal = new BigDecimal(XCPDatasOneDataLast.get(k).get("saleQty").toString());
							}
						}
					} else {
						lastQtyTotal = BigDecimal.ZERO;
					}

					if (Math.round(bdTotal.doubleValue()) == 0) {
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "-100%");
					} else if (Math.round(lastQtyTotal.doubleValue()) == 0) {
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "100%");
					} else {
						double poor = (bdTotal.doubleValue() - lastQtyTotal.doubleValue()) / lastQtyTotal.doubleValue()
								* 100;
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout trend per size_Growth",
								Math.round(poor) + "%");
					}

					dataFiveTotal.put(nowKey, Math.round(bdTotal.doubleValue()));
					dataFiveTotal.put(lastKey, Math.round(lastQtyTotal.doubleValue()));
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series", "Total");
					dataListTen.add(dataFiveTotal);
					dataFiveTotal = new HashMap<String, Object>();
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Country", "Country");
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series", "Series");
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Model", "Model");
					dataFiveTotal.put(nowKey, Month + "" + toYear[0]);
					dataFiveTotal.put(lastKey, Month + "" + laYear);
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "Growth");
					dataListTen.add(dataFiveTotal);

				}
			} else {

				if (XCPDatasOneData.size() > 0) {
					for (int k = 0; k < XCPDatasOneData.size(); k++) {
						if (XCPDatasOneData.get(k).get("party_name").toString().equals(partysNameNow)) {
							bdTotal = new BigDecimal(XCPDatasOneData.get(k).get("saleQty").toString());
						}
					}
				} else {
					bdTotal = BigDecimal.ZERO;
				}

				if (XCPDatasOneDataLast.size() > 0) {
					for (int k = 0; k < XCPDatasOneDataLast.size(); k++) {
						if (XCPDatasOneDataLast.get(k).get("party_name").toString().equals(partysNameNow)) {
							lastQtyTotal = new BigDecimal(XCPDatasOneDataLast.get(k).get("saleQty").toString());
						}
					}
				} else {
					lastQtyTotal = BigDecimal.ZERO;
				}
				if (WebPageUtil.isHQRole()) {

					HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();

					if (Math.round(bdTotal.doubleValue()) == 0) {
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "-100%");
					} else if (Math.round(lastQtyTotal.doubleValue()) == 0) {
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "100%");
					} else {
						double poor = (bdTotal.doubleValue() - lastQtyTotal.doubleValue()) / lastQtyTotal.doubleValue()
								* 100;
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout trend per size_Growth",
								Math.round(poor) + "%");
					}

					dataFiveTotal.put(nowKey, Math.round(bdTotal.doubleValue()));
					dataFiveTotal.put(lastKey, Math.round(lastQtyTotal.doubleValue()));
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series", "Total");
					dataListTen.add(dataFiveTotal);

					dataFiveTotal = new HashMap<String, Object>();
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Country", "Country");
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series", "Series");
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Model", "Model");
					dataFiveTotal.put(nowKey, Month + "" + toYear[0]);
					dataFiveTotal.put(lastKey, Month + "" + laYear);
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "Growth");
					dataListTen.add(dataFiveTotal);

				} else {

					HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();

					if (Math.round(bdTotal.doubleValue()) == 0) {
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "-100%");
					} else if (Math.round(lastQtyTotal.doubleValue()) == 0) {
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "100%");
					} else {
						double poor = (bdTotal.doubleValue() - lastQtyTotal.doubleValue()) / lastQtyTotal.doubleValue()
								* 100;
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout trend per size_Growth",
								Math.round(poor) + "%");
					}

					dataFiveTotal.put(nowKey, Math.round(bdTotal.doubleValue()));
					dataFiveTotal.put(lastKey, Math.round(lastQtyTotal.doubleValue()));
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series", "Total");
					dataListTen.add(dataFiveTotal);

				}

			}

		}
		if (WebPageUtil.isHQRole()) {
			LinkedList<HashMap<String, Object>> dataListTenTwo = new LinkedList<HashMap<String, Object>>();
			bdTotal = BigDecimal.ZERO;
			lastQtyTotal = BigDecimal.ZERO;
			Collection keysNow = nowMap.keySet();
			for (Iterator iterator = keysNow.iterator(); iterator.hasNext();) {
				bd = BigDecimal.ZERO;
				lastQty = BigDecimal.ZERO;

				HashMap<String, Object> dataTwo = new HashMap<String, Object>();
				Object keyNow = iterator.next();
				ArrayList<HashMap<String, Object>> monthList = nowMap.get(keyNow);
				dataTwo.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Country", "BDSC");
				dataTwo.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Model", keyNow);

				for (int i = 0; i < monthList.size(); i++) {
					bd = bd.add(new BigDecimal(monthList.get(i).get("saleQty").toString()));

					dataTwo.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series",
							monthList.get(i).get("LINE"));
				}
				bdTotal = bdTotal.add(bd);
				dataTwo.put(nowKey, Math.round(bd.doubleValue()));
				if (lastMap.get(keyNow) != null) {
					monthList = nowMap.get(keyNow);
					for (int i = 0; i < monthList.size(); i++) {
						lastQty = lastQty.add(new BigDecimal(monthList.get(i).get("saleQty").toString()));

					}
					lastQtyTotal = lastQtyTotal.add(lastQty);
					dataTwo.put(lastKey, Math.round(lastQty.doubleValue()));
				}

				// 同比增长率=（本期数－同期数）÷同期数×100%
				if (Math.round(bd.doubleValue()) == 0) {
					dataTwo.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "-100%");
				} else if (Math.round(lastQty.doubleValue()) == 0) {
					dataTwo.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "100%");
				} else {
					double poor = (bd.doubleValue() - lastQty.doubleValue()) / lastQty.doubleValue() * 100;
					dataTwo.put("YTD-" + toYear[0] + " Monthly sellout trend per size_Growth", Math.round(poor) + "%");
				}

				dataListTenTwo.add(dataTwo);
			}
			BDDateUtil.Order(dataListTenTwo, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series");
			for (int i = 0; i < dataListTenTwo.size(); i++) {
				dataListTen.add(dataListTenTwo.get(i));
			}
			HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();

			if (Math.round(bdTotal.doubleValue()) == 0) {
				dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "-100%");
			} else if (Math.round(lastQtyTotal.doubleValue()) == 0) {
				dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "100%");
			} else {
				double poor = (bdTotal.doubleValue() - lastQtyTotal.doubleValue()) / lastQtyTotal.doubleValue() * 100;
				dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout trend per size_Growth",
						Math.round(poor) + "%");
			}

			dataFiveTotal.put(nowKey, Math.round(bdTotal.doubleValue()));
			dataFiveTotal.put(lastKey, Math.round(lastQtyTotal.doubleValue()));
			dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series", "Total");
			dataListTen.add(dataFiveTotal);

		}

		long startTime = System.currentTimeMillis();

		System.out.println("=====第9个=" + (System.currentTimeMillis() - startTime));
		// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
		Sheet sheet = wb.createSheet(Integer.parseInt(toYear[0]) - 1 + "  vs. " + toYear[0] + " comparative");
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

		cellStyleDate.setDataFormat(dataFormat.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
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
		cellStyleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
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
		cellStylePERCENT.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
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
		cell.setCellValue("TCL BDSC "+monthEn.format(monthCh.parse(tBeginDate.split("-")[1]+" "+tBeginDate.split("-")[0])));// 标题
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
				int num = i;
				sheet.setColumnWidth(num, s.toString().length() * 156);
				//
				if (s.length == 1) { // 如果是简单表头项
					cell = row.createCell((short) (num));
					// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
					sheet.addMergedRegion(new CellRangeAddress(2, rows_max + 1, (num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(k + 2, k + 2, (num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(k + 2, k + 2 + rows_max - s.length, num, num));
					}

				}
				if (s.length > k) {
					if (!map.containsKey(sk)) {
						key = "";
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

		for (int i = 0; i < headerFive.length; i++) {
			String h = headerFive[i];

			if (h.split("_").length > rows_max) {
				rows_max = h.split("_").length;
			}
		}
		Map mapFive = new HashMap();
		for (int k = 0; k < rows_max; k++) {
			row = sheet.createRow((short) k + 13);
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
					sheet.addMergedRegion(new CellRangeAddress(13, rows_max + 12, (num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(k + 13, k + 13, (num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(k + 13, k + 13 + rows_max - s.length, num, num));
					}

				}
				if (s.length > k) {
					if (!mapFive.containsKey(sk)) {
						key = "";
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
			row = sheet.createRow((short) k + 13 + dataListFive.size() + 10);
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
					sheet.addMergedRegion(new CellRangeAddress(13 + dataListFive.size() + 10,
							rows_max + 12 + dataListFive.size() + 10, (num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(k + 13 + dataListFive.size() + 10,
							k + 13 + dataListFive.size() + 10, (num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(k + 13 + +dataListFive.size() + 10,
								k + 13 + dataListFive.size() + 10 + rows_max - s.length, num, num));
					}

				}
				if (s.length > k) {
					if (!mapSix.containsKey(sk)) {
						key = "";
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
			row = sheet.createRow((short) k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10);
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
					sheet.addMergedRegion(new CellRangeAddress(13 + dataListFive.size() + 10 + dataListSix.size() + 10,

							rows_max + 12 + dataListFive.size() + 10 + dataListSix.size() + 10, (num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(
							k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10,
							k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10, (num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(
								k + 13 + +dataListFive.size() + 10 + dataListSix.size() + 10,
								k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + rows_max - s.length, num,
								num));
					}

				}
				if (s.length > k) {
					if (!mapEight.containsKey(sk)) {
						key = "";
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
			row = sheet.createRow(
					(short) k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10);
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
					sheet.addMergedRegion(new CellRangeAddress(
							13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10,
							rows_max + 12 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size()
									+ 10,
							(num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(
							k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10,
							k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10,
							(num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(
								k + 13 + 10 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size()
										+ 10,
								k + 13 + 10 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size()
										+ 10 + rows_max - s.length,
								num, num));
					}

				}
				if (s.length > k) {
					if (!mapNight.containsKey(sk)) {
						key = "";
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
			row = sheet.createRow((short) k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10
					+ dataListEight.size() + 10 + dataListNight.size() + 10);
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
					sheet.addMergedRegion(new CellRangeAddress(
							13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10
									+ dataListNight.size() + 10,
							rows_max + 12 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size()
									+ 10 + dataListNight.size() + 10,
							(num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(
							k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10
									+ dataListNight.size() + 10,
							k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10
									+ dataListNight.size() + 10,
							(num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(
								k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10
										+ dataListNight.size() + 10,
								k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10
										+ dataListNight.size() + 10 + rows_max - s.length,
								num, num));
					}

				}
				if (s.length > k) {
					if (!mapTen.containsKey(sk)) {
						key = "";
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

		System.out.println("=====create header  cost time=" + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();
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
				if (dataMap.get(fields[c]) != null && dataMap.get(fields[c]).toString().length() > 0

				) {

					// 判断data是否为数值型
					isNum = dataMap.get(fields[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fields[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fields[c]).toString().contains("%");
					isGongshi = dataMap.get(fields[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fields[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					// 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fields[c]).toString()));

					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fields[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						// contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fields[c]).toString());

					} else {

						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为字符型
						contentCell.setCellValue(dataMap.get(fields[c]).toString());
					}
					if (d == dataList.size() - 1) {
						if(dataMap.get(fields[c]).toString().contains("-")) {
							contentCell.setCellStyle(cellStyleRED);

						}else {
							contentCell.setCellStyle(cellStyleGreen);
						}
						

					}
				} else {
					contentCell.setCellValue(0);
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

		System.out.println("=====create dataListFirst  cost time=" + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();

		System.out.println("=====create dataList4 cost time=" + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();

		int starts=12 + rows_max + 1;
		int end=12 + rows_max + 1;
		for (int d = 0; d < dataListFive.size(); d++) {

			HashMap<String, Object> dataMap = dataListFive.get(d);
			// 创建一行
			Row datarow = sheet.createRow(d + 12 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

			Cell scell = datarow.createCell((short) 0);

			scell.setCellType(Cell.CELL_TYPE_NUMERIC);
			
			for (int c = 0; c < fieldsFive.length; c++) {

				Cell contentCell = datarow.createCell(c);
				Boolean isNum = false;// data是否为数值型
				Boolean isInteger = false;// data是否为整数
				Boolean isPercent = false;// data是否为百分数
				Boolean isGongshi = false;// data是否为百分数
				boolean isGongshiOne = false;
				if (dataMap.get(fieldsFive[c]) != null && dataMap.get(fieldsFive[c]).toString().length() > 0

				) {

					// 判断data是否为数值型
					isNum = dataMap.get(fieldsFive[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fieldsFive[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fieldsFive[c]).toString().contains("%");
					isGongshi = dataMap.get(fieldsFive[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fieldsFive[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					DataFormat df = wb.createDataFormat(); // 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsFive[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsFive[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsFive[c]).toString());
					} else if (isPercent && c == fieldsFive.length - 1) {
						if (dataMap.get(fieldsFive[c]).toString().contains("-")) {
							contentCell.setCellStyle(cellStyleRED);
						} else {
							contentCell.setCellStyle(cellStyleGreen);
						}
						contentCell.setCellValue(dataMap.get(fieldsFive[c]).toString());
					} else {
						if (dataMap.get(fieldsFive[c]).toString().contains("Growth")) {
							contentCell.setCellStyle(cellStylehead);
							datarow.getCell(c - 1).setCellStyle(cellStylehead);
							datarow.getCell(c - 2).setCellStyle(cellStylehead);
							datarow.getCell(c - 3).setCellStyle(cellStylehead);
							datarow.getCell(c - 4).setCellStyle(cellStylehead);
							datarow.getCell(c - 1).setCellValue(dataMap.get(fieldsFive[c - 1]).toString());
							datarow.getCell(c - 2).setCellValue(dataMap.get(fieldsFive[c - 2]).toString());
							datarow.getCell(c - 3).setCellValue(dataMap.get(fieldsFive[c - 3]).toString());
							datarow.getCell(c - 4).setCellValue(dataMap.get(fieldsFive[c - 4]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
						}
						contentCell.setCellValue(dataMap.get(fieldsFive[c]).toString());
					}
		
				} else {
					contentCell.setCellValue("");
				}

			}
			
			if (d != dataListFive.size() - 1) {
				String keyMap="YTD-" + toYear[0] + "  Monthly sellout trend per size_Country";
					HashMap<String, Object> dataMapTwo = dataListFive.get(d + 1);
					if(dataMap.get(keyMap)!=null &&dataMapTwo.get(keyMap)!=null ) {
						if (dataMap.get(keyMap).equals(dataMapTwo.get(keyMap))) {
						}else {
							sheet.addMergedRegion(
									new CellRangeAddress(starts, d +end-1, 0, 0));
							starts=d +end+1;
						}
					}
				
				}else {
					sheet.addMergedRegion(
							new CellRangeAddress(starts, d +end, 0, 0));
				}

		}

		System.out.println("=====create dataList5  cost time=" + (System.currentTimeMillis() - startTime));
		starts=12 + rows_max + 1 + dataListFive.size() + 10;
		end=12 + rows_max + 1 + dataListFive.size() + 10;
		
		startTime = System.currentTimeMillis();
		for (int d = 0; d < dataListSix.size(); d++) {

			HashMap<String, Object> dataMap = dataListSix.get(d);
			// 创建一行
			Row datarow = sheet.createRow(d + 12 + rows_max + 1 + dataListFive.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

			Cell scell = datarow.createCell((short) 0);

			scell.setCellType(Cell.CELL_TYPE_NUMERIC);

			for (int c = 0; c < fieldsSix.length; c++) {

				Cell contentCell = datarow.createCell(c);
				Boolean isNum = false;// data是否为数值型
				Boolean isInteger = false;// data是否为整数
				Boolean isPercent = false;// data是否为百分数
				Boolean isGongshi = false;// data是否为百分数
				boolean isGongshiOne = false;
				if (dataMap.get(fieldsSix[c]) != null && dataMap.get(fieldsSix[c]).toString().length() > 0

				) {

					// 判断data是否为数值型
					isNum = dataMap.get(fieldsSix[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fieldsSix[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fieldsSix[c]).toString().contains("%");
					isGongshi = dataMap.get(fieldsSix[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fieldsSix[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					DataFormat df = wb.createDataFormat(); // 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsSix[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsSix[c]).toString());
					} else if (isPercent && c == fieldsSix.length - 1) {
						if (dataMap.get(fieldsSix[c]).toString().contains("-")) {
							contentCell.setCellStyle(cellStyleRED);
						} else {
							contentCell.setCellStyle(cellStyleGreen);
						}
						contentCell.setCellValue(dataMap.get(fieldsSix[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsSix[c]).toString());
					} else {
						if (dataMap.get(fieldsSix[c]).toString().contains("Growth")) {
							contentCell.setCellStyle(cellStylehead);
							datarow.getCell(c - 1).setCellStyle(cellStylehead);
							datarow.getCell(c - 2).setCellStyle(cellStylehead);
							datarow.getCell(c - 3).setCellStyle(cellStylehead);
							datarow.getCell(c - 4).setCellStyle(cellStylehead);
							datarow.getCell(c - 1).setCellValue(dataMap.get(fieldsSix[c - 1]).toString());
							datarow.getCell(c - 2).setCellValue(dataMap.get(fieldsSix[c - 2]).toString());
							datarow.getCell(c - 3).setCellValue(dataMap.get(fieldsSix[c - 3]).toString());
							datarow.getCell(c - 4).setCellValue(dataMap.get(fieldsSix[c - 4]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
						}
						contentCell.setCellValue(dataMap.get(fieldsSix[c]).toString());
					}

				
				} else {
					contentCell.setCellValue("");
				}
			
			}
			if (d != dataListSix.size() - 1) {
				String keyMap="Market Share_Country";
					HashMap<String, Object> dataMapTwo = dataListSix.get(d + 1);
					if(dataMap.get(keyMap)!=null &&dataMapTwo.get(keyMap)!=null ) {
						if (dataMap.get(keyMap).equals(dataMapTwo.get(keyMap))) {
						}else {
							sheet.addMergedRegion(
									new CellRangeAddress(starts, d +end-1, 0, 0));
							starts=d +end+1;
						}
					}
				
				}else {
					sheet.addMergedRegion(
							new CellRangeAddress(starts, d +end, 0, 0));
				}
		}

		System.out.println("=====create dataList7  cost time=" + (System.currentTimeMillis() - startTime));
		starts= 12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10;
		end= 12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10;
		startTime = System.currentTimeMillis();
		for (int d = 0; d < dataListEight.size(); d++) {

			HashMap<String, Object> dataMap = dataListEight.get(d);
			// 创建一行
			Row datarow = sheet.createRow(d + 12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

			Cell scell = datarow.createCell((short) 0);

			scell.setCellType(Cell.CELL_TYPE_NUMERIC);

			for (int c = 0; c < fieldsEight.length; c++) {

				Cell contentCell = datarow.createCell(c);
				Boolean isNum = false;// data是否为数值型
				Boolean isInteger = false;// data是否为整数
				Boolean isPercent = false;// data是否为百分数
				Boolean isGongshi = false;// data是否为百分数
				boolean isGongshiOne = false;
				if (dataMap.get(fieldsEight[c]) != null && dataMap.get(fieldsEight[c]).toString().length() > 0

				) {

					// 判断data是否为数值型
					isNum = dataMap.get(fieldsEight[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fieldsEight[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fieldsEight[c]).toString().contains("%");
					isGongshi = dataMap.get(fieldsEight[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fieldsEight[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					DataFormat df = wb.createDataFormat(); // 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsEight[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsEight[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsEight[c]).toString());
					} else if (isPercent && c == fieldsEight.length - 1) {
						if (dataMap.get(fieldsEight[c]).toString().contains("-")) {
							contentCell.setCellStyle(cellStyleRED);
						} else {
							contentCell.setCellStyle(cellStyleGreen);
						}
						contentCell.setCellValue(dataMap.get(fieldsEight[c]).toString());
					} else {
						if (dataMap.get(fieldsEight[c]).toString().contains("Growth")) {

							contentCell.setCellStyle(cellStylehead);
							datarow.getCell(c - 1).setCellStyle(cellStylehead);
							datarow.getCell(c - 2).setCellStyle(cellStylehead);
							datarow.getCell(c - 3).setCellStyle(cellStylehead);
							datarow.getCell(c - 4).setCellStyle(cellStylehead);

							datarow.getCell(c - 1).setCellValue(dataMap.get(fieldsEight[c - 1]).toString());
							datarow.getCell(c - 2).setCellValue(dataMap.get(fieldsEight[c - 2]).toString());
							datarow.getCell(c - 3).setCellValue(dataMap.get(fieldsEight[c - 3]).toString());
							datarow.getCell(c - 4).setCellValue(dataMap.get(fieldsEight[c - 4]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
						}
						contentCell.setCellValue(dataMap.get(fieldsEight[c]).toString());
					}

	
				} else {
					contentCell.setCellValue("");
				}

			}
			if (d != dataListEight.size() - 1) {
				String keyMap="Different catgory sell-out quantity_Country";
					HashMap<String, Object> dataMapTwo = dataListEight.get(d + 1);
					if(dataMap.get(keyMap)!=null &&dataMapTwo.get(keyMap)!=null ) {
						if (dataMap.get(keyMap).equals(dataMapTwo.get(keyMap))) {
						}else {
							sheet.addMergedRegion(
									new CellRangeAddress(starts, d +end-1, 0, 0));
							starts=d +end+1;
						}
					}
				
				}else {
					sheet.addMergedRegion(
							new CellRangeAddress(starts, d +end, 0, 0));
				}

		}
		System.out.println("=====create dataList8  cost time=" + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();
		starts=12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
				+ dataListEight.size() + 10;
		end=12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
				+ dataListEight.size() + 10;
		for (int d = 0; d < dataListNight.size(); d++) {

			HashMap<String, Object> dataMap = dataListNight.get(d);
			// 创建一行
			Row datarow = sheet.createRow(d + 12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
					+ dataListEight.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

			Cell scell = datarow.createCell((short) 0);

			scell.setCellType(Cell.CELL_TYPE_NUMERIC);

			for (int c = 0; c < fieldsNight.length; c++) {

				Cell contentCell = datarow.createCell(c);
				Boolean isNum = false;// data是否为数值型
				Boolean isInteger = false;// data是否为整数
				Boolean isPercent = false;// data是否为百分数
				Boolean isGongshi = false;// data是否为百分数
				boolean isGongshiOne = false;
				if (dataMap.get(fieldsNight[c]) != null && dataMap.get(fieldsNight[c]).toString().length() > 0

				) {
					// 判断data是否为数值型
					isNum = dataMap.get(fieldsNight[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fieldsNight[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fieldsNight[c]).toString().contains("%");
					isGongshi = dataMap.get(fieldsNight[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fieldsNight[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					DataFormat df = wb.createDataFormat(); // 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsNight[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsNight[c]).toString());
					} else if (isPercent && c == fieldsNight.length - 1) {
						if (dataMap.get(fieldsNight[c]).toString().contains("-")) {
							contentCell.setCellStyle(cellStyleRED);
						} else {
							contentCell.setCellStyle(cellStyleGreen);
						}
						contentCell.setCellValue(dataMap.get(fieldsNight[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsNight[c]).toString());
					} else {
						if (dataMap.get(fieldsNight[c]).toString().contains("Growth")) {
							contentCell.setCellStyle(cellStylehead);
							datarow.getCell(c - 1).setCellStyle(cellStylehead);
							datarow.getCell(c - 2).setCellStyle(cellStylehead);
							datarow.getCell(c - 3).setCellStyle(cellStylehead);
							datarow.getCell(c - 4).setCellStyle(cellStylehead);

							datarow.getCell(c - 1).setCellValue(dataMap.get(fieldsNight[c - 1]).toString());
							datarow.getCell(c - 2).setCellValue(dataMap.get(fieldsNight[c - 2]).toString());
							datarow.getCell(c - 3).setCellValue(dataMap.get(fieldsNight[c - 3]).toString());
							datarow.getCell(c - 4).setCellValue(dataMap.get(fieldsNight[c - 4]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
						}
						contentCell.setCellValue(dataMap.get(fieldsNight[c]).toString());
					}

				} else {
					contentCell.setCellValue("");
				}

			}
			if (d != dataListNight.size() - 1) {
				String keyMap="Growth rate_Country";
					HashMap<String, Object> dataMapTwo = dataListNight.get(d + 1);
					if(dataMap.get(keyMap)!=null &&dataMapTwo.get(keyMap)!=null ) {
						if (dataMap.get(keyMap).equals(dataMapTwo.get(keyMap))) {
						}else {
							sheet.addMergedRegion(
									new CellRangeAddress(starts, d +end-1, 0, 0));
							starts=d +end+1;
						}
					}
				
				}else {
					sheet.addMergedRegion(
							new CellRangeAddress(starts, d +end, 0, 0));
				}
		}
		System.out.println("=====create dataList8  cost time=" + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();
		starts=12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
				+ dataListEight.size() + 10 + dataListNight.size() + 10;
		end=12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
				+ dataListEight.size() + 10 + dataListNight.size() + 10;
		
		int startsTwo=12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
				+ dataListEight.size() + 10 + dataListNight.size() + 10;
		int endTwo=12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
				+ dataListEight.size() + 10 + dataListNight.size() + 10;
		
		for (int d = 0; d < dataListTen.size(); d++) {

			HashMap<String, Object> dataMap = dataListTen.get(d);
			// 创建一行
			Row datarow = sheet.createRow(d + 12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
					+ dataListEight.size() + 10 + dataListNight.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

			Cell scell = datarow.createCell((short) 0);

			scell.setCellType(Cell.CELL_TYPE_NUMERIC);

			for (int c = 0; c < fieldsTen.length; c++) {

				Cell contentCell = datarow.createCell(c);
				Boolean isNum = false;// data是否为数值型
				Boolean isInteger = false;// data是否为整数
				Boolean isPercent = false;// data是否为百分数
				Boolean isGongshi = false;// data是否为百分数
				boolean isGongshiOne = false;
				if (dataMap.get(fieldsTen[c]) != null && dataMap.get(fieldsTen[c]).toString().length() > 0

				) {
					// 判断data是否为数值型
					isNum = dataMap.get(fieldsTen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fieldsTen[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fieldsTen[c]).toString().contains("%");
					isGongshi = dataMap.get(fieldsTen[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fieldsTen[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					DataFormat df = wb.createDataFormat(); // 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsTen[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsTen[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsTen[c]).toString());
					} else if (isPercent && c == fieldsTen.length - 1) {
						if (dataMap.get(fieldsTen[c]).toString().contains("-")) {
							contentCell.setCellStyle(cellStyleRED);
						} else {
							contentCell.setCellStyle(cellStyleGreen);
						}
						contentCell.setCellValue(dataMap.get(fieldsTen[c]).toString());
					} else {
						if (dataMap.get(fieldsTen[c]).toString().contains("Growth")) {
							contentCell.setCellStyle(cellStylehead);
							datarow.getCell(c - 1).setCellStyle(cellStylehead);
							datarow.getCell(c - 2).setCellStyle(cellStylehead);
							datarow.getCell(c - 3).setCellStyle(cellStylehead);
							datarow.getCell(c - 4).setCellStyle(cellStylehead);
							datarow.getCell(c - 5).setCellStyle(cellStylehead);

							datarow.getCell(c - 1).setCellValue(dataMap.get(fieldsTen[c - 1]).toString());
							datarow.getCell(c - 2).setCellValue(dataMap.get(fieldsTen[c - 2]).toString());
							datarow.getCell(c - 3).setCellValue(dataMap.get(fieldsTen[c - 3]).toString());
							datarow.getCell(c - 4).setCellValue(dataMap.get(fieldsTen[c - 4]).toString());
							datarow.getCell(c - 5).setCellValue(dataMap.get(fieldsTen[c - 5]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
						}
						contentCell.setCellValue(dataMap.get(fieldsTen[c]).toString());
					}


				} else {
					contentCell.setCellValue("");
				}

			}
			
			if (d != dataListTen.size() - 1) {
				String keyMap="YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Country";
					HashMap<String, Object> dataMapTwo = dataListTen.get(d + 1);
					if(dataMap.get(keyMap)!=null &&dataMapTwo.get(keyMap)!=null ) {
						if (dataMap.get(keyMap).equals(dataMapTwo.get(keyMap))) {
						}else {
							sheet.addMergedRegion(
									new CellRangeAddress(starts, d +end-1, 0, 0));
							starts=d +end+1;
						}
					}
				
				}else {
					sheet.addMergedRegion(
							new CellRangeAddress(starts, d +end, 0, 0));
				}
			
			
			if (d != dataListTen.size() - 1) {
				String keyMap="YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series";
					HashMap<String, Object> dataMapTwo = dataListTen.get(d + 1);
					if(dataMap.get(keyMap)!=null &&dataMapTwo.get(keyMap)!=null ) {
						if (dataMap.get(keyMap).equals(dataMapTwo.get(keyMap))) {
						}else {
							sheet.addMergedRegion(
									new CellRangeAddress(startsTwo, d +endTwo, 1, 1));
							startsTwo=d +endTwo+1;
						}
					}
				
				}else {
					sheet.addMergedRegion(
							new CellRangeAddress(startsTwo, d +endTwo, 1, 1));
				}

		

		}

		System.out.println("=====create dataList9 cost time=" + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();

	}

	@Override
	public void monthYTDsellout(SXSSFWorkbook wb, String beginDateOne, String endDateOne, String searchStr,
			String conditions) throws Exception {

		String[] toYear = endDateOne.split("-");
		int laYear = Integer.parseInt(toYear[0].toString()) - 1;
		String laDays = BDDateUtil.getLastDayOfMonth(laYear, Integer.parseInt(toYear[1]));
		String[] laDay = laDays.split("-");
		int lastYear = Integer.parseInt(toYear[0]) - 1;
		String lastBegin=laYear+"-12-01";
		String lastEnd=laYear+"12-31";
		String YTDBegin = toYear[0] + "-01-01";
		// 表头数据
		String[] headers = { "Country", "YTD- " + toYear[0] + "_ _Category", 
				"YTD- " + toYear[0] + "_SUBTOTAL_Qty",
				"YTD- " + toYear[0] + "_SUBTOTAL_Share"

		};
		String Month = "";

		for (int i = 1; i <= Integer.parseInt(toYear[1]); i++) {
			headers = insert(headers, "YTD- " + toYear[0] + "_" + getMonth(i + "")+"_Qty");
			headers = insert(headers, "YTD- " + toYear[0] + "_" + getMonth(i + "")+"_Share");
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

		// 表头数据
		String[] headersTwo = {"",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_ _MODELS",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_SUBTOTAL_Qty",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_SUBTOTAL_Share"

		};

		for (int i = 1; i <= Integer.parseInt(toYear[1]); i++) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_" + getMonth(i + "")+"_Qty");
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_" + getMonth(i + "")+"_Share");

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

		long start = System.currentTimeMillis();
		Map<String, BigDecimal> mapResultTwo = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> countrySpec = new HashMap<String, BigDecimal>();

		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();

		List<HashMap<String, Object>> specDatasOneData = excelDao.selectSaleDataBySpecYTD(YTDBegin, endDateOne,
				searchStr, conditions, WebPageUtil.isHQRole());

		List<HashMap<String, Object>> specTotalOneData = excelDao.selectSaleTotalBySpecYTD(YTDBegin, endDateOne,
				searchStr, conditions, WebPageUtil.isHQRole());
		
		List<HashMap<String, Object>> specTotalLast = excelDao.selectSaleTotalBySpecYTD(lastBegin, lastEnd,
				searchStr, conditions, WebPageUtil.isHQRole());

		int beginNow = 6;// 每次+8
		int endNow = 12;
		BigDecimal totalLast=BigDecimal.ZERO;
		for (int i = 0; i < countryList.length; i++) {
			BigDecimal bdTotal = BigDecimal.ZERO;
			for (int j = 0; j < specList.length; j++) {
				BigDecimal bd = BigDecimal.ZERO;
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("Country", countryList[i]);
				data.put("YTD- " + toYear[0] + "_ _Category", specList[j]);
				data.put("YTD- " + toYear[0] + "_SUBTOTAL_Qty",
						"SUM(D" + (6 + dataList.size()) + ":"
								+ BDDateUtil.getExcelColumnLabel(Integer.parseInt(toYear[1]) + 3)
								+ (6 + dataList.size()) + ")");
				for (int k = 0; k < specDatasOneData.size(); k++) {
					bd = new BigDecimal(specDatasOneData.get(k).get("saleQty").toString());
					if (specDatasOneData.get(k).get("party_name").equals(countryList[i])
							&& specDatasOneData.get(k).get("SPEC").equals(specList[j])) {
						String mon = specDatasOneData.get(k).get("date").toString();
						String key = "YTD- " + toYear[0] + "_" + getMonth(mon)+"_Qty";
						data.put(key, Math.round(bd.doubleValue()));
						BigDecimal   monthTTL=BigDecimal.ZERO;
						for (int s = 0; s< specTotalOneData.size(); s++) {
							
							if (specTotalOneData.get(s).get("party_name").equals(countryList[i])
									&& specTotalOneData.get(s).get("date").toString().equals(mon)) {
								monthTTL = new BigDecimal(specTotalOneData.get(s).get("saleQty").toString());
								data.put( "YTD- " + toYear[0] + "_" + getMonth(mon)+"_Share", 
										Math.round(bd.doubleValue()/monthTTL.doubleValue()*100)+"%");
							}
						}
					

						BigDecimal numOne = mapResultTwo.get(specList[j] + "_" + key);
						BigDecimal numOneSize = mapResultTwo.get(key);
						if (numOne == null) {
							numOne = BigDecimal.ZERO;
						}
						if (numOneSize == null) {
							numOneSize = BigDecimal.ZERO;
						}
						mapResultTwo.put(specList[j] + "_" + key, numOne.add(bd));
						mapResultTwo.put(key, numOneSize.add(bd));
						
						
						
						BigDecimal numTwo = countrySpec.get(specList[j] + "_" + countryList[i]);
						BigDecimal numTwoSize = countrySpec.get(specList[j]);
						BigDecimal numTwoMonth = countrySpec.get( countryList[i]);
						BigDecimal numTwoTotal = countrySpec.get("BDSC");
						if (numTwo == null) {
							numTwo = BigDecimal.ZERO;
						}
						if (numTwoSize == null) {
							numTwoSize = BigDecimal.ZERO;
						}
						if (numTwoMonth == null) {
							numTwoMonth = BigDecimal.ZERO;
						}
						if (numTwoTotal == null) {
							numTwoTotal = BigDecimal.ZERO;
						}
						countrySpec.put(specList[j] + "_" + countryList[i], numTwo.add(bd));
						countrySpec.put(specList[j], numTwoSize.add(bd));
						countrySpec.put( countryList[i], numTwoMonth.add(bd));
						countrySpec.put("BDSC", numTwoTotal.add(bd));
					}
				}
				dataList.add(data);
			}
			LinkedList<HashMap<String, Object>> dataListLast = new LinkedList<HashMap<String, Object>>();

			for (int k = 0; k < dataList.size(); k++) {
				HashMap<String, Object>  obj=dataList.get(k);
				BigDecimal 	bd=BigDecimal.ZERO;
				BigDecimal 	bdTTL=BigDecimal.ZERO;
				if (countrySpec.get(obj.get("YTD- " + toYear[0] + "_ _Category")+ "_"
				+ obj.get("Country")) != null) {
					
				bd = countrySpec.get(obj.get("YTD- " + toYear[0] + "_ _Category")+ "_"
							+ obj.get("Country"));
				obj.put("YTD- " + toYear[0] + "_SUBTOTAL_Qty",Math.round(bd.doubleValue()));
						
				}
				if (countrySpec.get(obj.get("Country")) != null) {
						bdTTL = countrySpec.get(obj.get("Country")) ;
						if(bdTTL.doubleValue()==0.0 || bdTTL.doubleValue()==0) {
							obj.put("YTD- " + toYear[0] + "_SUBTOTAL_Share","100%");
						}else {
							double ach=bd.doubleValue()/bdTTL.doubleValue()*100;
							obj.put("YTD- " + toYear[0] + "_SUBTOTAL_Share",Math.round(ach)+"%");
						}
				}
				
			
				
				dataListLast.add(obj);
				
			}
			dataList=dataListLast;
			HashMap<String, Object> dataTotal = new HashMap<String, Object>();
			HashMap<String, Object> dataTotalLast = new HashMap<String, Object>();
			dataTotalLast.put("YTD- " + toYear[0] + "_ _Category", "Growth vs. Monthly");
			String key = "YTD- " + toYear[0] + "_ _Category";
			dataTotal.put(key, "TOTAL");
			for (int j = 0; j < specTotalOneData.size(); j++) {
				bdTotal = new BigDecimal(specTotalOneData.get(j).get("saleQty").toString());
				if (specTotalOneData.get(j).get("party_name").equals(countryList[i])) {
					String mon = specTotalOneData.get(j).get("date").toString();
					key = "YTD- " + toYear[0] + "_" + getMonth(mon)+"_Qty";
					dataTotal.put(key, Math.round(bdTotal.doubleValue()));
				}
				BigDecimal 	bd =BigDecimal.ZERO;
				if (countrySpec.get(countryList[i]) != null) {
					 bd = countrySpec.get(countryList[i]) ;
					dataTotal.put("YTD- " + toYear[0] + "_SUBTOTAL_Qty",Math.round(bd.doubleValue()));
			}
				dataTotalLast.put("YTD- " + toYear[0] + "_SUBTOTAL_Qty", Math.round(bd.doubleValue()/Integer.parseInt(toYear[1])));
				dataTotal.put("YTD- " + toYear[0] + "_SUBTOTAL_Share","100%");
			}
		
			for (int j =1; j <=Integer.parseInt(toYear[1]); j++) {
				// 单元格行
				int r = endNow + 1;
				// 本期列
				String bl = BDDateUtil.getExcelColumnLabel(j + 3);
				// 同期列
				String el = BDDateUtil.getExcelColumnLabel(j + 2);
				key = "YTD- " + toYear[0] + "_" + getMonth(j + "")+"_Qty";
				BigDecimal tNow=	BigDecimal.ZERO;
				if(dataTotal.get(key)!=null) {
					 tNow=	new BigDecimal(dataTotal.get(key).toString());
				}
				if(j==1) {
					if(specTotalLast.size()>0) {
						for (int k = 0; k < specTotalLast.size(); k++) {
							if(countryList[i].equals(specTotalLast.get(k).get("party_name"))) {
								BigDecimal bdLast=new BigDecimal(specTotalLast.get(k).get("saleQty").toString());
								totalLast=totalLast.add(bdLast);
								if(bdLast.doubleValue()==0.0 || bdLast.doubleValue()==0) {
									dataTotalLast.put(key, "100%");
								}else {
									double gro=(tNow.doubleValue()-bdLast.doubleValue())/bdLast.doubleValue()*100;
									dataTotalLast.put(key,Math.round(gro)+"%");
								}
							}
						}
					}else {
						dataTotalLast.put(key, "100%");
					}
				
					
				}else {
				
				BigDecimal tLast=	BigDecimal.ZERO;;
			
				if(dataTotal.get( "YTD- " + toYear[0] + "_" + getMonth((j-1) + "")+"_Qty")!=null) {
					tLast=	new BigDecimal(dataTotal.get( "YTD- " + toYear[0] + "_" + getMonth((j-1) + "")+"_Qty").toString());
				}
				if(tLast.doubleValue()==0.0 || tLast.doubleValue()==0) {
					dataTotalLast.put(key, "100%");
				}else {
					double gro=(tNow.doubleValue()-tLast.doubleValue())/tLast.doubleValue()*100;
					dataTotalLast.put(key,Math.round(gro)+"%");
				}
			
				}
				dataTotal.put( "YTD- " + toYear[0] + "_" + getMonth(j + "")+"_Share", "100%");
				// 本期数－同期数）÷同期数×100%
			}
			
		
			dataList.add(dataTotal);
			dataList.add(dataTotalLast);
			HashMap<String, Object> dataT = new HashMap<String, Object>();
			dataList.add(dataT);
		}

		if (WebPageUtil.isHQRole()) {
			for (int j = 0; j < specList.length; j++) {
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("Country", "BDSC");
				data.put("YTD- " + toYear[0] + "_ _Category", specList[j]);
				double gro =0.0;
					if (countrySpec.get(specList[j]) != null) {
						BigDecimal 	bd = countrySpec.get(specList[j]);
						data.put("YTD- " + toYear[0] + "_SUBTOTAL_Qty",Math.round(bd.doubleValue()));
						if(countrySpec.get("BDSC")!=null) {
							gro=bd.doubleValue()/countrySpec.get("BDSC").doubleValue()*100;
							data.put("YTD- " + toYear[0] + "_SUBTOTAL_Share",Math.round(gro)+"%");
						}else {
							data.put("YTD- " + toYear[0] + "_SUBTOTAL_Share","100%");
						}
						}
				
				for (int k = 1; k <= Integer.parseInt(toYear[1]); k++) {
					String key = "YTD- " + toYear[0] + "_" + getMonth(k + "")+"_Qty";
					BigDecimal bd = BigDecimal.ZERO;
					if (mapResultTwo.get(specList[j] + "_" + key) != null) {
						bd = mapResultTwo.get(specList[j] + "_" + key);
					
						if (mapResultTwo.get(key) != null) {
							gro=bd.doubleValue()/mapResultTwo.get(key).doubleValue()*100;
							data.put( "YTD- " + toYear[0] + "_" + getMonth(k + "")+"_Share", Math.round(gro)+"%");
						}else {
							data.put( "YTD- " + toYear[0] + "_" + getMonth(k + "")+"_Share", "100%");
						}
						
					}

					data.put(key, Math.round(bd.doubleValue()));
				}

			
				dataList.add(data);
			}
			HashMap<String, Object> dataTotal = new HashMap<String, Object>();
			HashMap<String, Object> dataTotalTwo = new HashMap<String, Object>();
			dataTotalTwo.put("YTD- " + toYear[0] + "_ _Category", "Growth vs. Monthly");
			dataTotal.put("YTD- " + toYear[0] + "_ _Category", "TOTAL");

			if(countrySpec.get("BDSC")!=null) {
				dataTotal.put("YTD- " + toYear[0] + "_SUBTOTAL_Qty",Math.round(countrySpec.get("BDSC").doubleValue()));
				dataTotalTwo.put("YTD- " + toYear[0] + "_SUBTOTAL_Qty", Math.round(Math.round(countrySpec.get("BDSC").doubleValue())/ Integer.parseInt(toYear[1])));

			}
			dataTotal.put("YTD- " + toYear[0] + "_SUBTOTAL_Share","100%");
			
			for (int k = 1; k <= Integer.parseInt(toYear[1]); k++) {
				String key = "YTD- " + toYear[0] + "_" + getMonth(k + "")+"_Qty";
				BigDecimal bd = BigDecimal.ZERO;
				if (mapResultTwo.get(key) != null) {
					bd = mapResultTwo.get(key);
					
					if (k != 1) {
						String	 keyLast = "YTD- " + toYear[0] + "_" + getMonth(k-1 + "")+"_Qty";
								if( mapResultTwo.get(keyLast)!=null ) {
									double ach=( mapResultTwo.get(key).doubleValue()- mapResultTwo.get(keyLast).doubleValue())
											/ mapResultTwo.get(keyLast).doubleValue()
											*100;
									dataTotalTwo.put(key, Math.round(ach)+"%");
								}else {
									dataTotalTwo.put(key, "100%");
								}
						
						dataTotal.put("YTD- " + toYear[0] + "_" + getMonth(k-1 + "")+"_Share", "100%");
						// 本期数－同期数）÷同期数×100%
					}else {
						if(totalLast.doubleValue()==0.0 || totalLast.doubleValue()==0) {
							dataTotalTwo.put(key, "100%");
						}else {
							double gro=(bd.doubleValue()-totalLast.doubleValue())/totalLast.doubleValue()*100;
							dataTotalTwo.put(key,Math.round(gro)+"%");
						}
					}
				}
				dataTotal.put(key, Math.round(bd.doubleValue()));
				// 单元格行
				
				


			}
			dataList.add(dataTotal);

			dataList.add(dataTotalTwo);
		}
		System.out.println(dataList);
		// headers = insert(headers, "YTD- "+toYear[0]+"_"+getMonth(i+""));
		System.out.println("+++++++++++++++YTD 第一个++++++++++++++++++" + (System.currentTimeMillis() - start));

		LinkedList<HashMap<String, Object>> dataListTwo = new LinkedList<HashMap<String, Object>>();

		LinkedList<LinkedHashMap<String, Object>> modelTotalData = excelDao.selectSaleDataByModelYTD(YTDBegin,
				endDateOne, searchStr, conditions, WebPageUtil.isHQRole());

		LinkedHashMap<String, LinkedList<LinkedHashMap<String, Object>>> modelMap = new LinkedHashMap<String, LinkedList<LinkedHashMap<String, Object>>>();
		for (int m = 0; m < modelTotalData.size(); m++) {
			if (modelMap.get(modelTotalData.get(m).get("model").toString()) == null) {
				LinkedList<LinkedHashMap<String, Object>> modelList = new LinkedList<LinkedHashMap<String, Object>>();
				modelList.add(modelTotalData.get(m));
				modelMap.put(modelTotalData.get(m).get("model").toString(), modelList);
			} else {
				LinkedList<LinkedHashMap<String, Object>> modelList = modelMap
						.get(modelTotalData.get(m).get("model").toString());
				modelList.add(modelTotalData.get(m));
			}

		}

		BigDecimal bdTotal = BigDecimal.ZERO;
		Map<String, BigDecimal> mapResultOne = new HashMap<String, BigDecimal>();
		Collection keys = modelMap.keySet();
		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			BigDecimal modelTotal = BigDecimal.ZERO;
			HashMap<String, Object> dataTwo = new HashMap<String, Object>();
			Object key = iterator.next();
			LinkedList<LinkedHashMap<String, Object>> monthList = modelMap.get(key);
			String keyCell = "SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_ _MODELS";
			dataTwo.put(keyCell, key);
			for (int j = 0; j < monthList.size(); j++) {
				BigDecimal bd = new BigDecimal(monthList.get(j).get("saleQty").toString());
				keyCell = "SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_"
						+ getMonth(monthList.get(j).get("date").toString())+"_Qty";
				dataTwo.put(keyCell, Math.round(bd.doubleValue()));
				bdTotal = bdTotal.add(bd);
				modelTotal = modelTotal.add(bd);
				BigDecimal numOne = mapResultOne.get(getMonth(monthList.get(j).get("date").toString()));
				if (numOne == null) {
					numOne = BigDecimal.ZERO;
				}
				mapResultOne.put(getMonth(monthList.get(j).get("date").toString()), numOne.add(bd));

			}
			
			dataTwo.put("SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_SUBTOTAL_Qty", Math.round(modelTotal.doubleValue()));
			dataListTwo.add(dataTwo);
		}
		
		LinkedList<HashMap<String, Object>> dataListTwoLast = new LinkedList<HashMap<String, Object>>();
		for (int c = 0; c < dataListTwo.size(); c++) {
			HashMap<String, Object> obj=dataListTwo.get(c);
			if(obj.get("SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_SUBTOTAL_Qty")!=null) {
				BigDecimal modelTotal = new BigDecimal(obj.get("SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_SUBTOTAL_Qty").toString());
				double gro=modelTotal.doubleValue()/bdTotal.doubleValue()*100;
				obj.put("SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_SUBTOTAL_Share", 
						Math.round(gro)+"%");
			}

			
			for (int i = 1; i <= Integer.parseInt(toYear[1]); i++) {
				BigDecimal bd = BigDecimal.ZERO;
				BigDecimal bdQty = BigDecimal.ZERO;
				String key = "SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_" + getMonth(i + "")+"_Share";
				String keyQty = "SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_" + getMonth(i + "")+"_Qty";
				if (mapResultOne.get(getMonth(i + "")) != null) {
					bd = mapResultOne.get(getMonth(i + ""));
					if(obj.get(keyQty)!=null) {
						bdQty=new BigDecimal(obj.get(keyQty).toString());
						double gro=bdQty.doubleValue()/bd.doubleValue()*100;
						obj.put(key, Math.round(gro)+"%");
					}else {
						obj.put(key, "0%");
					}
				}else {
					obj.put(key, "100%");
				}
		
				

			}
			dataListTwoLast.add(obj);
		}
		dataListTwo=dataListTwoLast;
		HashMap<String, Object> dataTwoTotal = new HashMap<String, Object>();
		dataTwoTotal.put("SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_ _MODELS", "TOTAL");
		for (int i = 1; i <= Integer.parseInt(toYear[1]); i++) {
			BigDecimal bd = BigDecimal.ZERO;
			if (mapResultOne.get(getMonth(i + "")) != null) {
				bd = mapResultOne.get(getMonth(i + ""));
			}
			String key = "SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_" + getMonth(i + "")+"_Qty";
			dataTwoTotal.put(key, Math.round(bd.doubleValue()));
			dataTwoTotal.put("SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_" + getMonth(i + "")+"_Share", "100%");

		}
		dataTwoTotal.put("SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_SUBTOTAL_Qty", Math.round(bdTotal.doubleValue()));
		dataTwoTotal.put("SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_SUBTOTAL_Share", "100%");

		dataListTwo.add(dataTwoTotal);
		start = System.currentTimeMillis();

		System.out.println("+++++++++++++++YTD 第二个++++++++++++++++++" + (System.currentTimeMillis() - start));

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
		sheet.createFreezePane(3, 6, 3, 6);
		// sheet.createFreezePane(3, 88, 3, 89);
		CellStyle cellStylename = wb.createCellStyle();// 表名样式
		cellStylename.setFont(fonthead);

		CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
		cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
		cellStyleinfo.setFont(fontinfo);

		CellStyle cellStyleDate = wb.createCellStyle();

		DataFormat dataFormat = wb.createDataFormat();

		cellStyleDate.setDataFormat(dataFormat.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
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
		cellStyleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
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
		cellStylePERCENT.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
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
		cell.setCellValue("TCL BDSC");// 标题

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
		// cell.setCellValue("YTD- " + toYear[0]);// 标题

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
					sheet.addMergedRegion(new CellRangeAddress(3, rows_max + 2, (num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(k + 3, k + 3, (num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(k + 3, k + 3 + rows_max - s.length, num, num));
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
		rows_max=0;
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
					sheet.addMergedRegion(
							new CellRangeAddress(dataList.size() + 15, rows_max + dataList.size() + 14, (num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(k + dataList.size() + 15, k + dataList.size() + 15,
							(num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(k + dataList.size() + 15,
								k + dataList.size() + 15 + rows_max - s.length, num, num));
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
		int starts =2+rows_max+1;
		int end=2+rows_max+1;
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
				if (dataMap.get(fields[c]) != null && dataMap.get(fields[c]).toString().length() > 0

				) {

					// 判断data是否为数值型
					isNum = dataMap.get(fields[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fields[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fields[c]).toString().contains("%");
					isGongshi = dataMap.get(fields[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fields[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					// 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fields[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fields[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fields[c]).toString());

					}

					else {
						if (dataMap.get(fields[c]).toString().contains("TOTAL")) {
							contentCell.setCellStyle(cellStylehead);
							datarow.createCell(c + 1).setCellStyle(cellStylehead);
							for (int i = 1; i <= Integer.parseInt(toYear[1]); i++) {
								datarow.createCell(c + 1 + i).setCellStyle(cellStylehead);
							}
						} else if (dataMap.get(fields[c]).toString().contains("Growth vs. Monthly")) {
							contentCell.setCellStyle(cellStylehead);
							datarow.createCell(c + 1).setCellStyle(cellStylehead);
							for (int i = 1; i <= Integer.parseInt(toYear[1]); i++) {
								datarow.createCell(c + 1 + i).setCellStyle(cellStylehead);
							}

						} else {
							contentCell.setCellStyle(contextstyle);
						}

						// 设置单元格内容为字符型
						contentCell.setCellValue(dataMap.get(fields[c]).toString());
					}
				
					if(c%2==0 &&  dataMap.get(fields[c]).toString().contains("%"))
					{ 
						if(dataMap.get(fields[c]).toString().contains("-")) {
							contentCell.setCellStyle(cellStyleRED);
						}else {
							contentCell.setCellStyle(cellStyleGreen);
						}
					}
				} else {
					contentCell.setCellValue("");
				}
				

			}
			if (d != dataList.size() - 1) {
				String keyMap="Country";
					HashMap<String, Object> dataMapTwo = dataList.get(d + 1);
					if(dataMap.get(keyMap)!=null &&dataMapTwo.get(keyMap)!=null ) {
						if (dataMap.get(keyMap).equals(dataMapTwo.get(keyMap))) {
							
						}else {
							sheet.addMergedRegion(
									new CellRangeAddress(starts, d +end+2, 0, 0));
							starts=d +end+4;
						}
					}else if(dataMap.get(keyMap)!=null && dataMapTwo.get(keyMap)==null ){
						sheet.addMergedRegion(
								new CellRangeAddress(starts, d +end+2, 0, 0));
						starts=d +end+4;
					}
				
				}else {
					sheet.addMergedRegion(
							new CellRangeAddress(starts, d +end, 0, 0));
				}
		}

		for (int d = 0; d < dataListTwo.size(); d++) {
			DataFormat df = wb.createDataFormat();
			HashMap<String, Object> dataMap = dataListTwo.get(d);

			// 创建一行
			Row datarow = sheet.createRow(d + dataList.size() + 14 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，
			Cell scell = datarow.createCell((short) 0);

			scell.setCellType(Cell.CELL_TYPE_NUMERIC);

			for (int c = 0; c < fieldsTwo.length; c++) {

				Cell contentCell = datarow.createCell(c);
				Boolean isNum = false;// data是否为数值型
				Boolean isInteger = false;// data是否为整数
				Boolean isPercent = false;// data是否为百分数
				Boolean isGongshi = false;// data是否为百分数
				Boolean isGongshiOne = false;
				if (dataMap.get(fieldsTwo[c]) != null && dataMap.get(fieldsTwo[c]).toString().length() > 0

				) {

					// 判断data是否为数值型
					isNum = dataMap.get(fieldsTwo[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fieldsTwo[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fieldsTwo[c]).toString().contains("%");
					isGongshi = dataMap.get(fieldsTwo[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fieldsTwo[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					// 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsTwo[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsTwo[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsTwo[c]).toString());

					} else {
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为字符型
						contentCell.setCellValue(dataMap.get(fieldsTwo[c]).toString());
					}

					/*
					 * if (d != dataListTwo.size() - 1) { HashMap<String, Object> dataMapTwo =
					 * dataListTwo.get(d + 1); if
					 * (dataMap.get(fieldsTwo[c]).equals(dataMapTwo.get(fieldsTwo[c]))) {
					 * sheet.addMergedRegion(new CellRangeAddress(d + dataList.size() + 14 +
					 * rows_max + 1, d + dataList.size() + 14 + rows_max + 1 + 1, 0, 0));
					 * 
					 * } }
					 */

				} else {
					contentCell.setCellValue("");
				}
				if (d == dataListTwo.size() - 1) {
					cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
					contentCell.setCellStyle(cellStyleGreen);
				}

			}

		}

	}

	@Override
	public void commonByHqQua(String what, SXSSFWorkbook wb, LinkedList<HashMap<String, Object>> list, String wbName,
			String beginDate, String endDate, String searchStr, String conditions) {

		try {
			String user = "";
			if (WebPageUtil.getLoginedUserId() != null) {
				user = WebPageUtil.getLoginedUserId();
			}

			String sheetName = "";

			String[] day = beginDate.split("-");
			if (what.equals("custom")) {
				sheetName = beginDate + " — " + endDate + "";
			} else if (what.equals("quar")) {

				if (day[1].equals("01") || day[1].equals("1")) {
					sheetName = day[0] + " -  Q1";
				} else if (day[1].equals("04") || day[1].equals("4")) {
					sheetName = day[0] + " -  Q2";
				} else if (day[1].equals("07") || day[1].equals("7")) {
					sheetName = day[0] + " -  Q3";
				} else if (day[1].equals("10")) {
					sheetName = day[0] + " -  Q4";
				}

			} else if (what.equals("year")) {
				sheetName = "YR-" + day[0];
			}
		String tBeginDate=beginDate;
		String tEndDate=endDate;
			String yearBegin = tBeginDate.split("-")[0];
			String yearEnd = tEndDate.split("-")[0];
			String qHead=BDDateUtil.getQ(Integer.parseInt(tBeginDate.split("-")[1]) )+" "+yearBegin;
			String head=monthEn.format(monthCh.parse(tBeginDate.split("-")[1]+" "+yearBegin));
			// 表头数据
			String[] headers = { "SELL-OUT INFORMATION SHEET_REG.", "SELL-OUT INFORMATION SHEET_Country.",
					"SELL-OUT INFORMATION SHEET_TYPE", "Admin Name", "DATE OF Upload",
					"TV SELL-OUT and TARGET_TTL TV SO_"+qHead+" Qty", "TV SELL-OUT and TARGET_TTL TV SO_ASP",
					"TV SELL-OUT and TARGET_"+qHead+" TARGET", "TV SELL-OUT and TARGET_Quarter Ach.",
					"TV SELL-OUT and TARGET_Year TARGET", "TV SELL-OUT and TARGET_Year Ach." };
			String[] headersBASIC = {};
			String[] headersDIGITAL = {};
			String[] headersINTERNET = {};
			String[] headersQUHD = {};
			String[] headersSMART = {};
			String[] headersUHD = {};
			String[] headersCURVE = {};
			// 查询机型销售数据
			List<HashMap<String, Object>> modelMapList = excelDao.selectModelByHead(beginDate, endDate, searchStr,
					conditions, WebPageUtil.isHQRole());
			// 将查出来的机型销售数据放入表头，形成三级标题
			int modelSize = 0;
			for (int i = 0; i < modelMapList.size(); i++) {
				BigDecimal bd = new BigDecimal(modelMapList.get(i).get("price").toString());
				String am = bd.toPlainString();
				double shop = Double.parseDouble(am);
				double price = shop / Integer.parseInt(modelMapList.get(i).get("country").toString());

				long lnum = Math.round(price);
				String m = "";

				if (modelMapList.get(i).get("spec").toString().contains("BASIC")) {
					m = "BASIC LED" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersBASIC = insert(headersBASIC, m);
					modelSize++;
				} else if (modelMapList.get(i).get("spec").toString().contains("DIGITAL")) {
					m = "DIGITAL BASIC" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersDIGITAL = insert(headersDIGITAL, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("INTERNET")) {
					m = "DIGITAL INTERNET" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersINTERNET = insert(headersINTERNET, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("QUHD")) {
					m = "(QUHD) TV" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersQUHD = insert(headersQUHD, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("SMART")) {
					m = "SMART TV" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersSMART = insert(headersSMART, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("UHD")) {
					m = "UHD TV" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersUHD = insert(headersUHD, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("CURVE")) {
					m = "CURVE TV" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersCURVE = insert(headersCURVE, m);
				}

			}

			headersBASIC = insert(headersBASIC, "LED SUB-TOTAL_" + "QTY");
			headersBASIC = insert(headersBASIC, "LED SUB-TOTAL_" + "AMOUNT");
			headersDIGITAL = insert(headersDIGITAL, "DIGITAL SUB-TOTAL_" + "QTY");
			headersDIGITAL = insert(headersDIGITAL, "DIGITAL SUB-TOTAL_" + "AMOUNT");
			headersINTERNET = insert(headersINTERNET, "INTERNET SUB-TOTAL_" + "QTY");
			headersINTERNET = insert(headersINTERNET, "INTERNET SUB-TOTAL_" + "AMOUNT");
			headersQUHD = insert(headersQUHD, "(QUHD) SUB-TOTAL_" + "QTY");
			headersQUHD = insert(headersQUHD, "(QUHD) SUB-TOTAL_" + "AMOUNT");
			headersSMART = insert(headersSMART, "SMART SUB-TOTAL_" + "QTY");
			headersSMART = insert(headersSMART, "SMART SUB-TOTAL_" + "AMOUNT");
			headersUHD = insert(headersUHD, "UHD SUB-TOTAL_" + "QTY");
			headersUHD = insert(headersUHD, "UHD SUB-TOTAL_" + "AMOUNT");
			headersCURVE = insert(headersCURVE, "CURVE SUB-TOTAL_" + "QTY");
			headersCURVE = insert(headersCURVE, "CURVE SUB-TOTAL_" + "AMOUNT");

			headers = BDDateUtil.list(headers, headersBASIC);
			headers = BDDateUtil.list(headers, headersDIGITAL);
			headers = BDDateUtil.list(headers, headersINTERNET);
			headers = BDDateUtil.list(headers, headersQUHD);

			headers = BDDateUtil.list(headers, headersSMART);
			headers = BDDateUtil.list(headers, headersUHD);
			headers = BDDateUtil.list(headers, headersCURVE);

			HashMap<String, ArrayList<HashMap<String, Object>>> priceMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelMapList.size(); m++) {
				if (priceMap.get(modelMapList.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelMapList.get(m));
					priceMap.put(modelMapList.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = priceMap
							.get(modelMapList.get(m).get("model").toString());
					modelList.add(modelMapList.get(m));
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
			yearBegin=beginDate.split("-")[0];
			
			String[] headersTwo = getHead("All Products", yearBegin);
			
			ArrayList columnsTwo =getArray(headersTwo);
			
			
			String[] headersThree = getHead("XCP UHD", yearBegin);
			
			ArrayList columnsThree =getArray(headersThree);
			
			
			String[] headersFour = getHead("CX UHD", yearBegin);
			
			ArrayList columnsFour =getArray(headersFour);
			
			String[] headersFive = getHead("P UHD", yearBegin);
			
			ArrayList columnsFive =getArray(headersFive);
			
			String[] headersSix = getHead("65\"↑ UHD", yearBegin);
			
			ArrayList columnsSix =getArray(headersSix);
			
			
			String[] headersSeven = getHead("X2X3 UHD", yearBegin);
			
			ArrayList columnsSeven =getArray(headersSeven);
			
			String[] headersEight = getHead("C2C2L UHD", yearBegin);
			
			ArrayList columnsEight =getArray(headersEight);
			
			String[] headersNine = getHead("C4C6 UHD", yearBegin);
			
			ArrayList columnsNine =getArray(headersNine);
			
			String[] headersTen = getHead("P2P21 UHD", yearBegin);
			
			ArrayList columnsTen =getArray(headersTen);
			
			String[] headersEleven = getHead("P6 UHD", yearBegin);
			
			ArrayList columnsEleven =getArray(headersEleven);
			
			String[] headersTwelve = getHead("P62 UHD", yearBegin);
			
			ArrayList columnsTwelve =getArray(headersTwelve);
			
			String[] headersThirteen = getHead("P65 UHD", yearBegin);
			
			ArrayList columnsThirteen=getArray(headersThirteen);
			
			String[] headersFourteen = getHead("P5", yearBegin);
			
			ArrayList columnsFourteen =getArray(headersFourteen);
			
			
			String[] headersFifteen = getHead("P3", yearBegin);
			
			ArrayList columnsFifteen =getArray(headersFifteen);
			
			String[] headersSixteen = getHead("S FHD Smart", yearBegin);
			
			ArrayList columnsSixteen =getArray(headersSixteen);	
			
			
			String[] headersSeventeen = getHead("S FHD Android", yearBegin);
			
			ArrayList columnsSeventeen =getArray(headersSeventeen);
			
			
			String[] headersEightteen = getHead("DTV", yearBegin);
			
			ArrayList columnsEightteen =getArray(headersEightteen);
			
			
			String[] headersNineteen = getHead("ATV", yearBegin);
			
			ArrayList columnsNineteen =getArray(headersNineteen);
			
			
			// 查询门店机型销售数据
			List<HashMap<String, Object>> modeldataList = excelDao.selectModelListByHq(beginDate, endDate, searchStr,
					conditions, WebPageUtil.isHQRole());

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> countryMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modeldataList.size(); m++) {
				if (countryMap.get(modeldataList.get(m).get("country_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modeldataList.get(m));
					countryMap.put(modeldataList.get(m).get("country_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = countryMap
							.get(modeldataList.get(m).get("country_id").toString());
					modelList.add(modeldataList.get(m));
				}

			}

			 tBeginDate = beginDate.split("-")[0] + "-" + beginDate.split("-")[1] + "-01";
			 tEndDate = BDDateUtil.getLastDayOfMonth(Integer.parseInt(endDate.split("-")[0]),
					Integer.parseInt(endDate.split("-")[1]));
			// 根据门店取得对应销售数据与目标数据
			List<HashMap<String, Object>> targetList = excelDao.selectTargetByshop(searchStr, conditions, tBeginDate,
					tEndDate, "1", WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> targetMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < targetList.size(); m++) {
				if (targetMap.get(targetList.get(m).get("country_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(targetList.get(m));
					targetMap.put(targetList.get(m).get("country_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = targetMap
							.get(targetList.get(m).get("country_id").toString());
					modelList.add(targetList.get(m));
				}

			}

			yearBegin = beginDate.split("-")[0];
			yearEnd = endDate.split("-")[0];
			// 根据门店取得对应销售数据与目标数据
			List<HashMap<String, Object>> targetListYear = excelDao.selectTargetByYear(searchStr, conditions, yearBegin,
					yearEnd, "1", WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> targetMapYear = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < targetListYear.size(); m++) {
				if (targetMapYear.get(targetListYear.get(m).get("country_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(targetListYear.get(m));
					targetMapYear.put(targetListYear.get(m).get("country_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = targetMapYear
							.get(targetListYear.get(m).get("country_id").toString());
					modelList.add(targetListYear.get(m));
				}

			}

			// 用于放置表格数据
			LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
			// 查询门店所有数据
			List<HashMap<String, Object>> excels = excelDao.selectDatas(searchStr, conditions, beginDate, endDate,
					WebPageUtil.isHQRole());
			int rowOne = 7;
			for (int j = 0; j < excels.size(); j++) {
				BigDecimal  monthQty=BigDecimal.ZERO;
				BigDecimal monthTarget=BigDecimal.ZERO;
				BigDecimal quaQty=BigDecimal.ZERO;
				BigDecimal quaTarget=BigDecimal.ZERO;
				BigDecimal yearQty=BigDecimal.ZERO;
				BigDecimal yearTarget=BigDecimal.ZERO;
				Double ach=0.0;
				// 用于放置表格数据
				HashMap<String, Object> dataMap = new HashMap<String, Object>();

				String shop_id = excels.get(j).get("country_id").toString();
				dataMap.put("SELL-OUT INFORMATION SHEET_REG.", excels.get(j).get("reg"));
				dataMap.put("SELL-OUT INFORMATION SHEET_Country.", excels.get(j).get("country_name"));
				dataMap.put("SELL-OUT INFORMATION SHEET_TYPE", "TV");
				dataMap.put("Admin Name",excels.get(j).get("user_name") );
				dataMap.put("DATE OF Upload", excels.get(j).get("datadate"));
				BigDecimal bd = null;
				bd = new BigDecimal(excels.get(j).get("saleQty").toString());
				dataMap.put("TV SELL-OUT and TARGET_TTL TV SO_"+qHead+" Qty", Math.round(bd.doubleValue()));

				//dataMap.put("TV SELL-OUT and TARGET_TTL TV SO_ASP", Math.round(bd.doubleValue()));
				quaQty=bd;
				yearQty=bd;
				Double saleSum = 0.0;

				if (targetMap.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = targetMap.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {
						bd = new BigDecimal(modelList.get(i).get("targetQty").toString());
						dataMap.put("TV SELL-OUT and TARGET_"+qHead+" TARGET", Math.round(bd.doubleValue()));
						quaTarget=bd;
					}

				}
				BigDecimal   b   =   null;  
				if(quaTarget.doubleValue()==0.0 || quaTarget.doubleValue()==0) {
					dataMap.put("TV SELL-OUT and TARGET_Quarter Ach.", "100%");
				}else if(quaQty.doubleValue()==0.0 || quaQty.doubleValue()==0) {
					dataMap.put("TV SELL-OUT and TARGET_Quarter Ach.", "0%");
				}else {
					ach=quaQty.doubleValue()/quaTarget.doubleValue()*100;
					b=new BigDecimal(ach);
					dataMap.put("TV SELL-OUT and TARGET_Quarter Ach.", 
							b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
				}
				

				
				if (targetMapYear.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = targetMapYear.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {
						bd = new BigDecimal(modelList.get(i).get("targetQty").toString());
						dataMap.put("TV SELL-OUT and TARGET_Year TARGET", Math.round(bd.doubleValue()));
						yearTarget=bd;
					}

				}
				if(yearTarget.doubleValue()==0.0 || yearTarget.doubleValue()==0) {
					dataMap.put("TV SELL-OUT and TARGET_Year Ach.",  "100%");
				}else if(yearQty.doubleValue()==0.0 || yearQty.doubleValue()==0) {
					dataMap.put("TV SELL-OUT and TARGET_Year Ach.",  "0%");
				}else {
					ach=yearQty.doubleValue()/yearTarget.doubleValue()*100;
					b=new BigDecimal(ach);
					dataMap.put("TV SELL-OUT and TARGET_Year Ach.", 
							b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
				}
				

				

				BigDecimal totalBASICQTY = BigDecimal.ZERO;
				BigDecimal totalDIGITALQTY = BigDecimal.ZERO;
				BigDecimal totalINTERNETQTY = BigDecimal.ZERO;
				BigDecimal totalQUHDQTY = BigDecimal.ZERO;
				BigDecimal totalSMARTQTY = BigDecimal.ZERO;
				BigDecimal totalUHDQTY = BigDecimal.ZERO;
				BigDecimal totalCURVEQTY = BigDecimal.ZERO;

				BigDecimal totalBASICAMT = BigDecimal.ZERO;
				BigDecimal totalDIGITALAMT = BigDecimal.ZERO;
				BigDecimal totalINTERNETAMT = BigDecimal.ZERO;
				BigDecimal totalQUHDAMT = BigDecimal.ZERO;
				BigDecimal totalSMARTAMT = BigDecimal.ZERO;
				BigDecimal totalUHDAMT = BigDecimal.ZERO;
				BigDecimal totalCURVEAMT = BigDecimal.ZERO;

				// 根据门店取得对应机型销售数据
				if (countryMap.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = countryMap.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {
						if (priceMap.get(modelList.get(i).get("model")) != null) {
							ArrayList<HashMap<String, Object>> priceList = priceMap.get(modelList.get(i).get("model"));
							for (int p = 0; p < priceList.size(); p++) {
								bd = new BigDecimal(priceList.get(p).get("price").toString());
								String am = bd.toPlainString();
								double shop = Double.parseDouble(am);
								double price = shop / Integer.parseInt(priceList.get(p).get("country").toString());

								long lnum = Math.round(price);
								String key = "";
								if (modelList.get(i).get("spec").toString().contains("BASIC")) {
									totalBASICQTY = totalBASICQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalBASICAMT = totalBASICAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "BASIC LED" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";
								} else if (modelList.get(i).get("spec").toString().contains("DIGITAL")) {
									totalDIGITALQTY = totalDIGITALQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalDIGITALAMT = totalDIGITALAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "DIGITAL BASIC" + "_" + modelList.get(i).get("model") + "_" + lnum + "_"
											+ "sold";
								} else if (modelList.get(i).get("spec").toString().contains("INTERNET")) {
									totalINTERNETQTY = totalINTERNETQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalINTERNETAMT = totalINTERNETAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "DIGITAL INTERNET" + "_" + modelList.get(i).get("model") + "_" + lnum + "_"
											+ "sold";
								} else if (modelList.get(i).get("spec").toString().contains("QUHD")) {
									totalQUHDQTY = totalQUHDQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalQUHDAMT = totalQUHDAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "(QUHD) TV" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";
								} else if (modelList.get(i).get("spec").toString().contains("SMART")) {
									totalSMARTQTY = totalSMARTQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalSMARTAMT = totalSMARTAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "SMART TV" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";
								} else if (modelList.get(i).get("spec").toString().contains("UHD")) {
									totalUHDQTY = totalUHDQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalUHDAMT = totalUHDAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "UHD TV" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";
								} else if (modelList.get(i).get("spec").toString().contains("CURVE")) {
									totalCURVEQTY = totalCURVEQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalCURVEAMT = totalCURVEAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "CURVE TV" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";
								}
								bd = new BigDecimal(modelList.get(i).get("saleQty").toString());

								dataMap.put(key, Math.round(bd.doubleValue()));
							}

						}

					}

				}

				dataMap.put("LED SUB-TOTAL_" + "QTY", Math.round(totalBASICQTY.doubleValue()));
				dataMap.put("LED SUB-TOTAL_" + "AMOUNT", Math.round(totalBASICAMT.doubleValue()));
				dataMap.put("DIGITAL SUB-TOTAL_" + "QTY", Math.round(totalDIGITALQTY.doubleValue()));
				dataMap.put("DIGITAL SUB-TOTAL_" + "AMOUNT", Math.round(totalDIGITALAMT.doubleValue()));
				dataMap.put("INTERNET SUB-TOTAL_" + "QTY", Math.round(totalINTERNETQTY.doubleValue()));
				dataMap.put("INTERNET SUB-TOTAL_" + "AMOUNT", Math.round(totalINTERNETAMT.doubleValue()));
				dataMap.put("(QUHD) SUB-TOTAL_" + "QTY", Math.round(totalQUHDQTY.doubleValue()));
				dataMap.put("(QUHD) SUB-TOTAL_" + "AMOUNT", Math.round(totalQUHDAMT.doubleValue()));
				dataMap.put("SMART SUB-TOTAL_" + "QTY", Math.round(totalSMARTQTY.doubleValue()));
				dataMap.put("SMART SUB-TOTAL_" + "AMOUNT", Math.round(totalBASICAMT.doubleValue()));
				dataMap.put("UHD SUB-TOTAL_" + "QTY", Math.round(totalUHDQTY.doubleValue()));
				dataMap.put("UHD SUB-TOTAL_" + "AMOUNT", Math.round(totalUHDAMT.doubleValue()));
				dataMap.put("CURVE SUB-TOTAL_" + "QTY", Math.round(totalCURVEQTY.doubleValue()));
				dataMap.put("CURVE SUB-TOTAL_" + "AMOUNT", Math.round(totalCURVEAMT.doubleValue()));

				dataList.add(dataMap);
			}

			
			
			HashMap<String, Object> whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","Total");
			
			LinkedList<HashMap<String, Object>> dataListTwo=getList(whereMap, yearBegin, "All Products");
			

			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			String searchLine=" AND (pt.product_line  LIKE 'X%' OR pt.product_line  LIKE 'C%' OR pt.product_line  LIKE 'P%')"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (tr.`product_line` LIKE 'X%' OR tr.`product_line`  LIKE 'C%' OR tr.`product_line` LIKE 'P%' ) ");

			whereMap.put("searchLine",searchLine);
			
			LinkedList<HashMap<String, Object>> dataListThree=getList(whereMap, yearBegin, "XCP UHD");
		

			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line  LIKE 'X%' OR pt.product_line  LIKE 'C%'  )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (tr.`product_line` LIKE 'X%' OR tr.`product_line`  LIKE 'C%'  ) ");

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListFour =getList(whereMap, yearBegin, "CX UHD");
			

			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line  LIKE 'P%' )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line`  LIKE 'P%'  ) ");

			whereMap.put("searchLine",searchLine);
			
			LinkedList<HashMap<String, Object>> dataListFive=getList(whereMap, yearBegin, "P UHD");
			
			
	
			
			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","Total");
			searchLine=" AND (pt.Size>65 )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			

			whereMap.put("searchLine",searchLine);
			
			LinkedList<HashMap<String, Object>> dataListSix =getList(whereMap, yearBegin, "65\"↑ UHD");
			
			
			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%X2%' OR  pt.product_line like '%X3%' )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%X2%' OR  tr.`product_line`  like '%X3%'  ) ");
			
			

			whereMap.put("searchLine",searchLine);
			
			LinkedList<HashMap<String, Object>> dataListSeven=getList(whereMap, yearBegin, "X2X3 UHD");
			
	
		
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%C2%' OR  pt.product_line like '%C2L%' )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%C2%' OR  tr.`product_line`  like '%C2L%'  ) ");
			
			

			whereMap.put("searchLine",searchLine);
			
			LinkedList<HashMap<String, Object>> dataListEight=getList(whereMap, yearBegin, "C2C2L UHD");
			
			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%C4%' OR  pt.product_line like '%C6%' )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%C4%' OR  tr.`product_line`  like '%C6%'  ) ");
			
			

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListNight=getList(whereMap, yearBegin, "C4C6 UHD");
			
			
			
			
			
			
			
	
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%P2%' OR  pt.product_line like '%P21%' )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%P2%' OR  tr.`product_line`  like '%P21%'  ) ");
			
			

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListTen =getList(whereMap, yearBegin, "P2P21 UHD");
			
			
			

			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%P6%' )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%P6%') ");


			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListEleven =getList(whereMap, yearBegin, "P6 UHD");
		
			

			
		
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%P62%'  )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%P62%'  ) ");
			
			

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListTwelve  =getList(whereMap, yearBegin, "P62 UHD");
			
			
		
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%P65%'  )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%UHD%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%P65%'   ) ");
			
			

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListThirteen =getList(whereMap, yearBegin, "P65 UHD");
			
			

			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%P5%'  )";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%P5%'   ) ");
			
			

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListFourteen =getList(whereMap, yearBegin, "P5 UHD");
			
			
			
			
	

			
		
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like '%P3%'   )";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like '%P3%'  ) ");
			
			

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListFifteen  =getList(whereMap, yearBegin, "P3 UHD");
			
			

			
		
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");

			searchLine=" AND (pt.product_line like 'S%'   )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%FHD%'   AND pt.`PRODUCT_SPEC_ID` LIKE '%Smart%' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like 'S%'   ) ");
			
			

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListSixteen =getList(whereMap, yearBegin, "S FHD Smart");
			
			

			
		
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","XCP");
			searchLine=" AND (pt.product_line like 'S%' )"
					+ " AND pt.`PRODUCT_SPEC_ID` LIKE '%FHD%'   AND PT.OS='Android' ";
			
			whereMap.put("searchLineTarget",
					" AND (  tr.`product_line` like 'S%'  ) ");

			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListSeventeen=getList(whereMap, yearBegin, "S FHD Android");
		

			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","Total");
			searchLine= " AND pt.`PRODUCT_SPEC_ID` LIKE '%DIGITAL%' ";
			


			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListEightteen =getList(whereMap, yearBegin, "DTV");

			
			
			whereMap=new HashMap<>();
			whereMap.put("searchStr", searchStr);
			whereMap.put("conditions", conditions);
			whereMap.put("beginDate", beginDate.split("-")[0]+"-01-01");
			whereMap.put("endDate",  beginDate.split("-")[0]+"-12-31");
			whereMap.put("what","Total");
			searchLine= " AND pt.`PRODUCT_SPEC_ID` NOT LIKE '%DIGITAL%'  ";
	
			whereMap.put("searchLine",searchLine);
			LinkedList<HashMap<String, Object>> dataListNineteen   =getList(whereMap, yearBegin, "ATV");
			
			
			String[] header = new String[columns.size()];
			String[] fields = new String[columns.size()];
			for (int i = 0, l = columns.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columns.get(i);
				header[i] = columnMap.get("header").toString();
				fields[i] = columnMap.get("field").toString();

			}
			
			String[] headerTwo = new String[columnsTwo.size()];
			String[] fieldsTwo = new String[columnsTwo.size()];
			for (int i = 0, l = columnsTwo.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsTwo.get(i);
				headerTwo[i] = columnMap.get("header").toString();
				fieldsTwo[i] = columnMap.get("field").toString();

			}

			
			String[] headerThree = new String[columnsThree.size()];
			String[] fieldsThree = new String[columnsThree.size()];
			for (int i = 0, l = columnsThree.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsThree.get(i);
				headerThree[i] = columnMap.get("header").toString();
				fieldsThree[i] = columnMap.get("field").toString();

			}
			
			
			
			String[] headerFour = new String[columnsFour.size()];
			String[] fieldsFour = new String[columnsFour.size()];
			for (int i = 0, l = columnsFour.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsFour.get(i);
				headerFour[i] = columnMap.get("header").toString();
				fieldsFour[i] = columnMap.get("field").toString();

			}
			String[] headerFive = new String[columnsFive.size()];
			String[] fieldsFive = new String[columnsFive.size()];
			for (int i = 0, l = columnsFive.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsFive.get(i);
				headerFive[i] = columnMap.get("header").toString();
				fieldsFive[i] = columnMap.get("field").toString();

			}
			String[] headerSix = new String[columnsSix.size()];
			String[] fieldsSix = new String[columnsSix.size()];
			for (int i = 0, l = columnsSix.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsSix.get(i);
				headerSix[i] = columnMap.get("header").toString();
				fieldsSix[i] = columnMap.get("field").toString();

			}
			String[] headerSeven = new String[columnsSeven.size()];
			String[] fieldsSeven = new String[columnsSeven.size()];
			for (int i = 0, l = columnsSeven.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsSeven.get(i);
				headerSeven[i] = columnMap.get("header").toString();
				fieldsSeven[i] = columnMap.get("field").toString();

			}
			String[] headerEight = new String[columnsEight.size()];
			String[] fieldsEight = new String[columnsEight.size()];
			for (int i = 0, l = columnsEight.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsEight.get(i);
				headerEight[i] = columnMap.get("header").toString();
				fieldsEight[i] = columnMap.get("field").toString();

			}
			String[] headerNine = new String[columnsNine.size()];
			String[] fieldsNine = new String[columnsNine.size()];
			for (int i = 0, l = columnsNine.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsNine.get(i);
				headerNine[i] = columnMap.get("header").toString();
				fieldsNine[i] = columnMap.get("field").toString();

			}
			String[] headerTen = new String[columnsTen.size()];
			String[] fieldsTen = new String[columnsTen.size()];
			for (int i = 0, l = columnsTen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsTen.get(i);
				headerTen[i] = columnMap.get("header").toString();
				fieldsTen[i] = columnMap.get("field").toString();

			}
			String[] headerEleven = new String[columnsEleven.size()];
			String[] fieldsEleven = new String[columnsEleven.size()];
			for (int i = 0, l = columnsEleven.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsEleven.get(i);
				headerEleven[i] = columnMap.get("header").toString();
				fieldsEleven[i] = columnMap.get("field").toString();

			}
			String[] headerTwelve = new String[columnsTwelve.size()];
			String[] fieldsTwelve = new String[columnsTwelve.size()];
			for (int i = 0, l = columnsTwelve.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsTwelve.get(i);
				headerTwelve[i] = columnMap.get("header").toString();
				fieldsTwelve[i] = columnMap.get("field").toString();

			}
			String[] headerThirteen = new String[columnsThirteen.size()];
			String[] fieldsThirteen = new String[columnsThirteen.size()];
			for (int i = 0, l = columnsThirteen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsThirteen.get(i);
				headerThirteen[i] = columnMap.get("header").toString();
				fieldsThirteen[i] = columnMap.get("field").toString();

			}
			String[] headerFourteen = new String[columnsFourteen.size()];
			String[] fieldsFourteen = new String[columnsFourteen.size()];
			for (int i = 0, l = columnsFourteen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsFourteen.get(i);
				headerFourteen[i] = columnMap.get("header").toString();
				fieldsFourteen[i] = columnMap.get("field").toString();

			}
			String[] headerFifteen = new String[columnsFifteen.size()];
			String[] fieldsFifteen = new String[columnsFifteen.size()];
			for (int i = 0, l = columnsFifteen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsFifteen.get(i);
				headerFifteen[i] = columnMap.get("header").toString();
				fieldsFifteen[i] = columnMap.get("field").toString();

			}
			String[] headerSixteen = new String[columnsSixteen.size()];
			String[] fieldsSixteen = new String[columnsSixteen.size()];
			for (int i = 0, l = columnsSixteen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsSixteen.get(i);
				headerSixteen[i] = columnMap.get("header").toString();
				fieldsSixteen[i] = columnMap.get("field").toString();

			}
			
			String[] headerSeventeen = new String[columnsSeventeen.size()];
			String[] fieldsSeventeen = new String[columnsSeventeen.size()];
			for (int i = 0, l = columnsSeventeen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsSeventeen.get(i);
				headerSeventeen[i] = columnMap.get("header").toString();
				fieldsSeventeen[i] = columnMap.get("field").toString();

			}
			String[] headerEightteen = new String[columnsEightteen.size()];
			String[] fieldsEightteen = new String[columnsEightteen.size()];
			for (int i = 0, l = columnsEightteen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsEightteen.get(i);
				headerEightteen[i] = columnMap.get("header").toString();
				fieldsEightteen[i] = columnMap.get("field").toString();

			}
			String[] headerNineteen = new String[columnsNineteen.size()];
			String[] fieldsNineteen = new String[columnsNineteen.size()];
			for (int i = 0, l = columnsNineteen.size(); i < l; i++) {

				HashMap columnMap = (HashMap) columnsNineteen.get(i);
				headerNineteen[i] = columnMap.get("header").toString();
				fieldsNineteen[i] = columnMap.get("field").toString();

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
			sheet.createFreezePane(7, 9, 9, 10);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
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
			cellStyleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
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
			cellStylePERCENT.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
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
			cell.setCellValue("TCL  BDSC");// 标题
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

			// 第二行
			cell = rowSix.createCell((short) 5);
			cell.setCellValue("SUBTOTAL(9,F8:F" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,F8:F" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 6);
			cell.setCellValue("SUBTOTAL(9,G8:G" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,G8:G" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 7);
			cell.setCellValue("SUBTOTAL(9,H8:H" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,H8:H" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 8);
			cell.setCellValue("TEXT(F7/H7,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT(F7/H7,\"0.00%\")");

			// 第二行

			// 第二行
			cell = rowSix.createCell((short) 9);
			cell.setCellValue("SUBTOTAL(9,J8:J" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,J8:J" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 10);
			cell.setCellValue("TEXT(F7/J7,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT(F7/J7,\"0.00%\")");

			// 第二行
			cell = rowSix.createCell((short) 11);
			cell.setCellValue("SUBTOTAL(9,L8:L" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,L8:L" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 12);
			cell.setCellValue("SUBTOTAL(9,M8:M" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,M8:M" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 13);
			cell.setCellValue("SUBTOTAL(9,N8:N" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,N8:N" + size + ")");

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
			cell.setCellValue("SUBTOTAL(9,R8:R" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,R8:R" + size + ")");

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
			String[] line = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
					"S", "T", "U", "V", "W", "X", "Y", "Z" };

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
				cell.setCellValue("SUBTOTAL(9," + big[i] + "8:" + big[i] + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9," + big[i] + "8:" + big[i] + size + ")");

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

						if (i < 5) {
							cell.setCellStyle(cellStyleWHITE);
						} else if (i > 4 && i < 11) {
							cell.setCellStyle(cellStylehead);
						} else if (i > 10) {
							cell.setCellStyle(cellStyleYellow);
							if (s[0].contains("SUB-TOTAL") || s[0].contains("TTL")) {
								cell.setCellStyle(cellStyleRED);
							}
						}
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2, rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						if (i < 5) {
							cell.setCellStyle(cellStyleWHITE);
						} else if (i > 4 && i < 11) {
							cell.setCellStyle(cellStylehead);
						} else if (i > 10) {
							cell.setCellStyle(cellStyleYellow);
							if (s[0].contains("SUB-TOTAL") || s[0].contains("TTL")) {
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
						if (i < 11 || (i > 10 && k + 2 == 2)) {
							// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号

							sheet.addMergedRegion(new CellRangeAddress(k + 2, k + 2, (num), (num + cols - 1)));

						}
						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k + 2 + rows_max - s.length, num, num));
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
					if (dataMap.get(fields[c]) != null && dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString().contains("%");
						isGongshi = dataMap.get(fields[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fields[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c]).toString());
							int s = dataMap.get(fields[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c]).toString());

						}
						if(c==8 || c==10 ) {
							BDDateUtil.setColor(dataMap.get(fields[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						}
					} else {
						contentCell.setCellValue("");
					}

					/*
					 * if (dataMap.get(fields[c]) != null &&
					 * dataMap.get(fields[c]).toString().length() > 0) {
					 * cell.setCellValue(dataMap.get(fields[c]).toString()); // 信息 } else {
					 * cell.setCellValue(""); // 信息
					 * 
					 * }
					 */
					// sheet.addMergedRegion(new CellRangeAddress(0, (short)
					// 2, 0,
					// (short)
					// c));
				}
			}
			
			rows_max = 0;
			for (int i = 0; i < headerTwo.length; i++) {
				String h = headerTwo[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapTwo = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7 );

				for (int i = 0; i < headerTwo.length; i++) {

					String headerTwoTemp = headerTwo[i];
					String[] s = headerTwoTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7 , rows_max+7+excels.size()+7 + 1, (num), (num)));
						sk = headerTwoTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapTwo.containsKey(headerTwoTemp)) {
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
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7 , k +7+excels.size()+7 , (num), (num + cols - 1)));

						if (sk.equals(headerTwoTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7 ,k +7+excels.size()+7 + rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListTwo.size(); d++) {

				HashMap<String, Object> dataMap = dataListTwo.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsTwo.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsTwo[c]) != null && dataMap.get(fieldsTwo[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTwo[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTwo[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTwo[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsTwo[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsTwo[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsTwo[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsTwo[c]).toString());
							int s = dataMap.get(fieldsTwo[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsTwo[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsTwo[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsTwo[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
				
			}
			
			rows_max = 0;
			for (int i = 0; i < headerThree.length; i++) {
				String h = headerThree[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapThree = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5);

				for (int i = 0; i < headerThree.length; i++) {

					String headerThreeTemp = headerThree[i];
					String[] s = headerThreeTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5, rows_max+7+excels.size()+7+dataListTwo.size()+5 + 1, (num), (num)));
						sk = headerThreeTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapThree.containsKey(headerThreeTemp)) {
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
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 , k +7+excels.size()+7+dataListTwo.size()+5 , (num), (num + cols - 1)));

						if (sk.equals(headerThreeTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 ,k +7+excels.size()+7+dataListTwo.size()+5 + rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListThree.size(); d++) {

				HashMap<String, Object> dataMap = dataListThree.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsThree.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsThree[c]) != null && dataMap.get(fieldsThree[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsThree[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsThree[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsThree[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsThree[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsThree[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsThree[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsThree[c]).toString());
							int s = dataMap.get(fieldsThree[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsThree[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsThree[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsThree[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerFour.length; i++) {
				String h = headerFour[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapFour = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5);

				for (int i = 0; i < headerFour.length; i++) {

					String headerFourTemp = headerFour[i];
					String[] s = headerFourTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+ 1, (num), (num)));
						sk = headerFourTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapFour.containsKey(headerFourTemp)) {
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
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5 , (num), (num + cols - 1)));

						if (sk.equals(headerFourTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+ rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListFour.size(); d++) {

				HashMap<String, Object> dataMap = dataListFour.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5+dataListThree.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsFour.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsFour[c]) != null && dataMap.get(fieldsFour[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFour[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFour[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFour[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsFour[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsFour[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsFour[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsFour[c]).toString());
							int s = dataMap.get(fieldsFour[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsFour[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsFour[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsFour[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerFive.length; i++) {
				String h = headerFive[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapFive = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5);

				for (int i = 0; i < headerFive.length; i++) {

					String headerFiveTemp = headerFive[i];
					String[] s = headerFiveTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+ 1, (num), (num)));
						sk = headerFiveTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapFive.containsKey(headerFiveTemp)) {
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

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 , (num), (num + cols - 1)));

						if (sk.equals(headerFiveTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+ rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListFive.size(); d++) {

				HashMap<String, Object> dataMap = dataListFive.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsFive.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsFive[c]) != null && dataMap.get(fieldsFive[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFive[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFive[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFive[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsFive[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsFive[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsFive[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsFive[c]).toString());
							int s = dataMap.get(fieldsFive[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsFive[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsFive[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsFive[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerSix.length; i++) {
				String h = headerSix[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapSix = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5);

				for (int i = 0; i < headerSix.length; i++) {

					String headerSixTemp = headerSix[i];
					String[] s = headerSixTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+ 1, (num), (num)));
						sk = headerSixTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapSix.containsKey(headerSixTemp)) {
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

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerSixTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+ rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListSix.size(); d++) {

				HashMap<String, Object> dataMap = dataListSix.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsSix.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsSix[c]) != null && dataMap.get(fieldsSix[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsSix[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsSix[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsSix[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsSix[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsSix[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsSix[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsSix[c]).toString());
							int s = dataMap.get(fieldsSix[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsSix[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsSix[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsSix[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerSeven.length; i++) {
				String h = headerSeven[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapSeven = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5);

				for (int i = 0; i < headerSeven.length; i++) {

					String headerSevenTemp = headerSeven[i];
					String[] s = headerSevenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+ 1, (num), (num)));
						sk = headerSevenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapSeven.containsKey(headerSevenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapSeven.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerSeven.length; j++) {

							if (headerSeven[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerSevenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapSeven.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapSeven.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListSeven.size(); d++) {

				HashMap<String, Object> dataMap = dataListSeven.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsSeven.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsSeven[c]) != null && dataMap.get(fieldsSeven[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsSeven[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsSeven[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsSeven[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsSeven[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsSeven[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsSeven[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsSeven[c]).toString());
							int s = dataMap.get(fieldsSeven[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsSeven[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsSeven[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsSeven[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			
			
			
			
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerEight.length; i++) {
				String h = headerEight[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapEight = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5);

				for (int i = 0; i < headerEight.length; i++) {

					String headerEightTemp = headerEight[i];
					String[] s = headerEightTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+ 1, (num), (num)));
						sk = headerEightTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapEight.containsKey(headerEightTemp)) {
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

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerEightTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+ rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListEight.size(); d++) {

				HashMap<String, Object> dataMap = dataListEight.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsEight.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsEight[c]) != null && dataMap.get(fieldsEight[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsEight[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsEight[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsEight[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsEight[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsEight[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsEight[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsEight[c]).toString());
							int s = dataMap.get(fieldsEight[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsEight[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsEight[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsEight[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerNine.length; i++) {
				String h = headerNine[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapNight = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5);

				for (int i = 0; i < headerNine.length; i++) {

					String headerNightTemp = headerNine[i];
					String[] s = headerNightTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+ 1, (num), (num)));
						sk = headerNightTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapNight.containsKey(headerNightTemp)) {
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
						for (int j = 0; j < headerNine.length; j++) {

							if (headerNine[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerNightTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+ rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListNight.size(); d++) {

				HashMap<String, Object> dataMap = dataListNight.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsNine.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsNine[c]) != null && dataMap.get(fieldsNine[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsNine[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsNine[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsNine[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsNine[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsNine[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsNine[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsNine[c]).toString());
							int s = dataMap.get(fieldsNine[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsNine[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsNine[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsNine[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerTen.length; i++) {
				String h = headerTen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapTen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5);

				for (int i = 0; i < headerTen.length; i++) {

					String headerTenTemp = headerTen[i];
					String[] s = headerTenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+ 1, (num), (num)));
						sk = headerTenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapTen.containsKey(headerTenTemp)) {
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

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerTenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+ rows_max - s.length, num, num));
						}
						// }

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

			
			for (int d = 0; d < dataListTen.size(); d++) {

				HashMap<String, Object> dataMap = dataListTen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsTen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell contentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsTen[c]) != null && dataMap.get(fieldsTen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsTen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsTen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsTen[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsTen[c]).toString());
							int s = dataMap.get(fieldsTen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fieldsTen[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fieldsTen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsTen[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						contentCell.setCellValue("");
					}

					
				}
			}
			
			
			rows_max = 0;
			for (int i = 0; i < headerEleven.length; i++) {
				String h = headerEleven[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapEleven = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5);

				for (int i = 0; i < headerEleven.length; i++) {

					String headerElevenTemp = headerEleven[i];
					String[] s = headerElevenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+ 1, (num), (num)));
						sk = headerElevenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapEleven.containsKey(headerElevenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapEleven.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerEleven.length; j++) {

							if (headerEleven[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerElevenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapEleven.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapEleven.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListEleven.size(); d++) {

				HashMap<String, Object> dataMap = dataListEleven.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsEleven.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conEleventCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsEleven[c]) != null && dataMap.get(fieldsEleven[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsEleven[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsEleven[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsEleven[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsEleven[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsEleven[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conEleventCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conEleventCell.setCellValue(Double.parseDouble(dataMap.get(fieldsEleven[c]).toString()));
						} else if (isGongshi) {

							conEleventCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conEleventCell.setCellStyle(contextstyle);
							conEleventCell.setCellFormula(dataMap.get(fieldsEleven[c]).toString());
							int s = dataMap.get(fieldsEleven[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conEleventCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conEleventCell.setCellStyle(contextstyle);
							conEleventCell.setCellFormula(dataMap.get(fieldsEleven[c]).toString());

						} else {
							conEleventCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conEleventCell.setCellValue(dataMap.get(fieldsEleven[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsEleven[c]).toString(), conEleventCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conEleventCell.setCellValue("");
					}

					
				}
			}
			
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerTwelve.length; i++) {
				String h = headerTwelve[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapTwelve = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5);

				for (int i = 0; i < headerTwelve.length; i++) {

					String headerTwelveTemp = headerTwelve[i];
					String[] s = headerTwelveTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+ 1, (num), (num)));
						sk = headerTwelveTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapTwelve.containsKey(headerTwelveTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapTwelve.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerTwelve.length; j++) {

							if (headerTwelve[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerTwelveTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapTwelve.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapTwelve.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListTwelve.size(); d++) {

				HashMap<String, Object> dataMap = dataListTwelve.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsTwelve.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conTwelvetCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsTwelve[c]) != null && dataMap.get(fieldsTwelve[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsTwelve[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsTwelve[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsTwelve[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsTwelve[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsTwelve[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conTwelvetCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conTwelvetCell.setCellValue(Double.parseDouble(dataMap.get(fieldsTwelve[c]).toString()));
						} else if (isGongshi) {

							conTwelvetCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conTwelvetCell.setCellStyle(contextstyle);
							conTwelvetCell.setCellFormula(dataMap.get(fieldsTwelve[c]).toString());
							int s = dataMap.get(fieldsTwelve[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conTwelvetCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conTwelvetCell.setCellStyle(contextstyle);
							conTwelvetCell.setCellFormula(dataMap.get(fieldsTwelve[c]).toString());

						} else {
							conTwelvetCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conTwelvetCell.setCellValue(dataMap.get(fieldsTwelve[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsTwelve[c]).toString(), conTwelvetCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conTwelvetCell.setCellValue("");
					}

					
				}
			}
			
			
			rows_max = 0;
			for (int i = 0; i < headerThirteen.length; i++) {
				String h = headerThirteen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapThirteen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5);

				for (int i = 0; i < headerThirteen.length; i++) {

					String headerThirteenTemp = headerThirteen[i];
					String[] s = headerThirteenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+ 1, (num), (num)));
						sk = headerThirteenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapThirteen.containsKey(headerThirteenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapThirteen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerThirteen.length; j++) {

							if (headerThirteen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerThirteenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapThirteen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapThirteen.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListThirteen.size(); d++) {

				HashMap<String, Object> dataMap = dataListThirteen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsThirteen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conThirteentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsThirteen[c]) != null && dataMap.get(fieldsThirteen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsThirteen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsThirteen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsThirteen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsThirteen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsThirteen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conThirteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conThirteentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsThirteen[c]).toString()));
						} else if (isGongshi) {

							conThirteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conThirteentCell.setCellStyle(contextstyle);
							conThirteentCell.setCellFormula(dataMap.get(fieldsThirteen[c]).toString());
							int s = dataMap.get(fieldsThirteen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conThirteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conThirteentCell.setCellStyle(contextstyle);
							conThirteentCell.setCellFormula(dataMap.get(fieldsThirteen[c]).toString());

						} else {
							conThirteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conThirteentCell.setCellValue(dataMap.get(fieldsThirteen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsThirteen[c]).toString(), conThirteentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conThirteentCell.setCellValue("");
					}

					
				}
			}
			
			
			rows_max = 0;
			for (int i = 0; i < headerFourteen.length; i++) {
				String h = headerFourteen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapFourteen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5);

				for (int i = 0; i < headerFourteen.length; i++) {

					String headerFourteenTemp = headerFourteen[i];
					String[] s = headerFourteenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+ 1, (num), (num)));
						sk = headerFourteenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapFourteen.containsKey(headerFourteenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapFourteen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerFourteen.length; j++) {

							if (headerFourteen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerFourteenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapFourteen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapFourteen.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListFourteen.size(); d++) {

				HashMap<String, Object> dataMap = dataListFourteen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsFourteen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conFourteentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsFourteen[c]) != null && dataMap.get(fieldsFourteen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFourteen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFourteen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFourteen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsFourteen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsFourteen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conFourteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conFourteentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsFourteen[c]).toString()));
						} else if (isGongshi) {

							conFourteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conFourteentCell.setCellStyle(contextstyle);
							conFourteentCell.setCellFormula(dataMap.get(fieldsFourteen[c]).toString());
							int s = dataMap.get(fieldsFourteen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conFourteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conFourteentCell.setCellStyle(contextstyle);
							conFourteentCell.setCellFormula(dataMap.get(fieldsFourteen[c]).toString());

						} else {
							conFourteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conFourteentCell.setCellValue(dataMap.get(fieldsFourteen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsFourteen[c]).toString(), conFourteentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conFourteentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerFifteen.length; i++) {
				String h = headerFifteen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapFifteen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5);

				for (int i = 0; i < headerFifteen.length; i++) {

					String headerFifteenTemp = headerFifteen[i];
					String[] s = headerFifteenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+ 1, (num), (num)));
						sk = headerFifteenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapFifteen.containsKey(headerFifteenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapFifteen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerFifteen.length; j++) {

							if (headerFifteen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerFifteenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapFifteen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapFifteen.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListFifteen.size(); d++) {

				HashMap<String, Object> dataMap = dataListFifteen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsFifteen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conFifteentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsFifteen[c]) != null && dataMap.get(fieldsFifteen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsFifteen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsFifteen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsFifteen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsFifteen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsFifteen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conFifteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conFifteentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsFifteen[c]).toString()));
						} else if (isGongshi) {

							conFifteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conFifteentCell.setCellStyle(contextstyle);
							conFifteentCell.setCellFormula(dataMap.get(fieldsFifteen[c]).toString());
							int s = dataMap.get(fieldsFifteen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conFifteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conFifteentCell.setCellStyle(contextstyle);
							conFifteentCell.setCellFormula(dataMap.get(fieldsFifteen[c]).toString());

						} else {
							conFifteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conFifteentCell.setCellValue(dataMap.get(fieldsFifteen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsFifteen[c]).toString(), conFifteentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conFifteentCell.setCellValue("");
					}

					
				}
			}
			
			rows_max = 0;
			for (int i = 0; i < headerSixteen.length; i++) {
				String h = headerSixteen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapSixteen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5);

				for (int i = 0; i < headerSixteen.length; i++) {

					String headerSixteenTemp = headerSixteen[i];
					String[] s = headerSixteenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+ 1, (num), (num)));
						sk = headerSixteenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapSixteen.containsKey(headerSixteenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapSixteen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerSixteen.length; j++) {

							if (headerSixteen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerSixteenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapSixteen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapSixteen.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListSixteen.size(); d++) {

				HashMap<String, Object> dataMap = dataListSixteen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsSixteen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conSixteentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsSixteen[c]) != null && dataMap.get(fieldsSixteen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsSixteen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsSixteen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsSixteen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsSixteen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsSixteen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conSixteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conSixteentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsSixteen[c]).toString()));
						} else if (isGongshi) {

							conSixteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conSixteentCell.setCellStyle(contextstyle);
							conSixteentCell.setCellFormula(dataMap.get(fieldsSixteen[c]).toString());
							int s = dataMap.get(fieldsSixteen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conSixteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conSixteentCell.setCellStyle(contextstyle);
							conSixteentCell.setCellFormula(dataMap.get(fieldsSixteen[c]).toString());

						} else {
							conSixteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conSixteentCell.setCellValue(dataMap.get(fieldsSixteen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsSixteen[c]).toString(), conSixteentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conSixteentCell.setCellValue("");
					}

					
				}
			}
			
			
			rows_max = 0;
			for (int i = 0; i < headerSeventeen.length; i++) {
				String h = headerSeventeen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapSeventeen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5);

				for (int i = 0; i < headerSeventeen.length; i++) {

					String headerSeventeenTemp = headerSeventeen[i];
					String[] s = headerSeventeenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+ 1, (num), (num)));
						sk = headerSeventeenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapSeventeen.containsKey(headerSeventeenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapSeventeen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerSeventeen.length; j++) {

							if (headerSeventeen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerSeventeenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapSeventeen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapSeventeen.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListSeventeen.size(); d++) {

				HashMap<String, Object> dataMap = dataListSeventeen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsSeventeen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conSeventeentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsSeventeen[c]) != null && dataMap.get(fieldsSeventeen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsSeventeen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsSeventeen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsSeventeen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsSeventeen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsSeventeen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conSeventeentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conSeventeentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsSeventeen[c]).toString()));
						} else if (isGongshi) {

							conSeventeentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conSeventeentCell.setCellStyle(contextstyle);
							conSeventeentCell.setCellFormula(dataMap.get(fieldsSeventeen[c]).toString());
							int s = dataMap.get(fieldsSeventeen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conSeventeentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conSeventeentCell.setCellStyle(contextstyle);
							conSeventeentCell.setCellFormula(dataMap.get(fieldsSeventeen[c]).toString());

						} else {
							conSeventeentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conSeventeentCell.setCellValue(dataMap.get(fieldsSeventeen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsSeventeen[c]).toString(), conSeventeentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conSeventeentCell.setCellValue("");
					}

					
				}
			}
			
			
			rows_max = 0;
			for (int i = 0; i < headerEightteen.length; i++) {
				String h = headerEightteen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapEightteen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5);

				for (int i = 0; i < headerEightteen.length; i++) {

					String headerEightteenTemp = headerEightteen[i];
					String[] s = headerEightteenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+ 1, (num), (num)));
						sk = headerEightteenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapEightteen.containsKey(headerEightteenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapEightteen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerEightteen.length; j++) {

							if (headerEightteen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerEightteenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapEightteen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapEightteen.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListEightteen.size(); d++) {

				HashMap<String, Object> dataMap = dataListEightteen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsEightteen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conEightteentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsEightteen[c]) != null && dataMap.get(fieldsEightteen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsEightteen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsEightteen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsEightteen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsEightteen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsEightteen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conEightteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conEightteentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsEightteen[c]).toString()));
						} else if (isGongshi) {

							conEightteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conEightteentCell.setCellStyle(contextstyle);
							conEightteentCell.setCellFormula(dataMap.get(fieldsEightteen[c]).toString());
							int s = dataMap.get(fieldsEightteen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conEightteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conEightteentCell.setCellStyle(contextstyle);
							conEightteentCell.setCellFormula(dataMap.get(fieldsEightteen[c]).toString());

						} else {
							conEightteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conEightteentCell.setCellValue(dataMap.get(fieldsEightteen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsEightteen[c]).toString(), conEightteentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conEightteentCell.setCellValue("");
					}

					
				}
			}
			
			
			
			rows_max = 0;
			for (int i = 0; i < headerNineteen.length; i++) {
				String h = headerNineteen[i];

				if (h.split("_").length > rows_max) {
					rows_max = h.split("_").length;
				}
			}

			Map mapNineteen = new HashMap();
			for (int k = 0; k < rows_max; k++) {
				row = sheet.createRow((short) k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5);

				for (int i = 0; i < headerNineteen.length; i++) {

					String headerNineteenTemp = headerNineteen[i];
					String[] s = headerNineteenTemp.split("_");
					String sk = "";
					int num = i;
					sheet.setColumnWidth(num, s.toString().length() * 156);
					if (s.length == 1) { // 如果是简单表头项
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
				
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5
								+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5,
								rows_max+7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5+ 1, (num), (num)));
						sk = headerNineteenTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
					
							cell.setCellStyle(cellStylehead);
						
						int cols = 0;
						if (mapNineteen.containsKey(headerNineteenTemp)) {
							continue;
						}

						for (int d = 0; d <= k; d++) {

							if (d != k) {
								sk += s[d] + "_";
							} else {
								sk += s[d];
							}
						}

						if (mapNineteen.containsKey(sk)) {
							continue;
						}
						for (int j = 0; j < headerNineteen.length; j++) {

							if (headerNineteen[j].indexOf(sk) != -1) {
								cols++;
							}
						}
						cell.setCellValue(s[k]);

						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						// if (i < 13 || i >= 13 + modelSize) {
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
								+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5, 
								k +7+excels.size()+7+dataListTwo.size()+5+dataListThree.size()+5+dataListFour.size()+5 +dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5, (num), (num + cols - 1)));

						if (sk.equals(headerNineteenTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k +7+excels.size()+7+dataListTwo.size()+5 
									+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5,
									k +7+excels.size()+7+dataListTwo.size()+5 +dataListThree.size()+5+dataListFour.size()+5
									+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5+ rows_max - s.length, num, num));
						}
						// }

					}
					if (s.length > k) {
						if (!mapNineteen.containsKey(sk)) {
							String key = "";
							if (k > 0) {
								key = sk;
							} else {
								key = s[k];
							}
							mapNineteen.put(key, null);
						}
					}
				}
			}

			
			for (int d = 0; d < dataListNineteen.size(); d++) {

				HashMap<String, Object> dataMap = dataListNineteen.get(d);
				// 创建一行
				Row datarow = sheet.createRow(d + 7+excels.size()+7 + rows_max+dataListTwo.size()+5
						+dataListThree.size()+5+dataListFour.size()+5+dataListFive.size()+5+dataListSix.size()+5+dataListSeven.size()+5+dataListEight.size()+5+dataListNight.size()+5+dataListTen.size()+5+dataListEleven.size()+5+dataListTwelve.size()+5+dataListThirteen.size()+5+dataListFourteen.size()+5+dataListFifteen.size()+5+dataListSixteen.size()+5+dataListSeventeen.size()+5+dataListEightteen.size()+5);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

				Cell scell = datarow.createCell((short) 0);
				// 创建列
				for (int c = 0; c < fieldsNineteen.length; c++) {

					cell = datarow.createCell((short) (c));

					Cell conNineteentCell = datarow.createCell(c);
					Boolean isNum = false;// data是否为数值型
					Boolean isInteger = false;// data是否为整数
					Boolean isPercent = false;// data是否为百分数
					Boolean isGongshi = false;// data是否为百分数
					Boolean isGongshiOne = false;
					if (dataMap.get(fieldsNineteen[c]) != null && dataMap.get(fieldsNineteen[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fieldsNineteen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fieldsNineteen[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fieldsNineteen[c]).toString().contains("%");
						isGongshi = dataMap.get(fieldsNineteen[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fieldsNineteen[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							conNineteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							conNineteentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsNineteen[c]).toString()));
						} else if (isGongshi) {

							conNineteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							conNineteentCell.setCellStyle(contextstyle);
							conNineteentCell.setCellFormula(dataMap.get(fieldsNineteen[c]).toString());
							int s = dataMap.get(fieldsNineteen[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							conNineteentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							conNineteentCell.setCellStyle(contextstyle);
							conNineteentCell.setCellFormula(dataMap.get(fieldsNineteen[c]).toString());

						} else {
							conNineteentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							conNineteentCell.setCellValue(dataMap.get(fieldsNineteen[c]).toString());

						} 
							BDDateUtil.setColor(dataMap.get(fieldsNineteen[c]).toString(), conNineteentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						
					} else {
						conNineteentCell.setCellValue("");
					}

					
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	
	@Override
	public void commonByHqQuaCus(String what, SXSSFWorkbook wb, LinkedList<HashMap<String, Object>> list, String wbName,
			String beginDate, String endDate, String searchStr, String conditions) {

		try {
			String user = "";
			if (WebPageUtil.getLoginedUserId() != null) {
				user = WebPageUtil.getLoginedUserId();
			}
			String tBeginDate=beginDate;
			String tEndDate=endDate;
				String yearBegin = tBeginDate.split("-")[0];
				String yearEnd = tEndDate.split("-")[0];
				String qHead=BDDateUtil.getQ(Integer.parseInt(tBeginDate.split("-")[1]) )+" "+yearBegin;
				String head=monthEn.format(monthCh.parse(tBeginDate.split("-")[1]+" "+yearBegin));
			String sheetName = "";

			String[] day = beginDate.split("-");
			if (what.equals("custom")) {
				sheetName = beginDate + " — " + endDate + "";
			} else if (what.equals("quar")) {

				if (day[1].equals("01") || day[1].equals("1")) {
					sheetName = day[0] + " -  Q1";
				} else if (day[1].equals("04") || day[1].equals("4")) {
					sheetName = day[0] + " -  Q2";
				} else if (day[1].equals("07") || day[1].equals("7")) {
					sheetName = day[0] + " -  Q3";
				} else if (day[1].equals("10")) {
					sheetName = day[0] + " -  Q4";
				}

			} else if (what.equals("year")) {
				sheetName = "YR-" + day[0];
			}

			// 表头数据
			String[] headers = { "SELL-OUT INFORMATION SHEET_REG.", "SELL-OUT INFORMATION SHEET_Country.",
					"SELL-OUT INFORMATION SHEET_TYPE", "Admin Name", "DATE OF Upload",
					"TV SELL-OUT and TARGET_TTL TV SO_"+qHead+" Qty", "TV SELL-OUT and TARGET_TTL TV SO_ASP",
					"TV SELL-OUT and TARGET_"+qHead+" TARGET", "TV SELL-OUT and TARGET_Quarter Ach.",
					"TV SELL-OUT and TARGET_Year TARGET", "TV SELL-OUT and TARGET_Year Ach." };
			String[] headersBASIC = {};
			String[] headersDIGITAL = {};
			String[] headersINTERNET = {};
			String[] headersQUHD = {};
			String[] headersSMART = {};
			String[] headersUHD = {};
			String[] headersCURVE = {};
			// 查询机型销售数据
			List<HashMap<String, Object>> modelMapList = excelDao.selectModelByHead(beginDate, endDate, searchStr,
					conditions, WebPageUtil.isHQRole());
			// 将查出来的机型销售数据放入表头，形成三级标题
			int modelSize = 0;
			for (int i = 0; i < modelMapList.size(); i++) {
				BigDecimal bd = new BigDecimal(modelMapList.get(i).get("price").toString());
				String am = bd.toPlainString();
				double shop = Double.parseDouble(am);
				double price = shop / Integer.parseInt(modelMapList.get(i).get("country").toString());

				long lnum = Math.round(price);
				String m = "";

				if (modelMapList.get(i).get("spec").toString().contains("BASIC")) {
					m = "BASIC LED" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersBASIC = insert(headersBASIC, m);
					modelSize++;
				} else if (modelMapList.get(i).get("spec").toString().contains("DIGITAL")) {
					m = "DIGITAL BASIC" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersDIGITAL = insert(headersDIGITAL, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("INTERNET")) {
					m = "DIGITAL INTERNET" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersINTERNET = insert(headersINTERNET, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("QUHD")) {
					m = "(QUHD) TV" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersQUHD = insert(headersQUHD, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("SMART")) {
					m = "SMART TV" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersSMART = insert(headersSMART, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("UHD")) {
					m = "UHD TV" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersUHD = insert(headersUHD, m);
				} else if (modelMapList.get(i).get("spec").toString().contains("CURVE")) {
					m = "CURVE TV" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
					headersCURVE = insert(headersCURVE, m);
				}

			}

			headersBASIC = insert(headersBASIC, "LED SUB-TOTAL_" + "QTY");
			headersBASIC = insert(headersBASIC, "LED SUB-TOTAL_" + "AMOUNT");
			headersDIGITAL = insert(headersDIGITAL, "DIGITAL SUB-TOTAL_" + "QTY");
			headersDIGITAL = insert(headersDIGITAL, "DIGITAL SUB-TOTAL_" + "AMOUNT");
			headersINTERNET = insert(headersINTERNET, "INTERNET SUB-TOTAL_" + "QTY");
			headersINTERNET = insert(headersINTERNET, "INTERNET SUB-TOTAL_" + "AMOUNT");
			headersQUHD = insert(headersQUHD, "(QUHD) SUB-TOTAL_" + "QTY");
			headersQUHD = insert(headersQUHD, "(QUHD) SUB-TOTAL_" + "AMOUNT");
			headersSMART = insert(headersSMART, "SMART SUB-TOTAL_" + "QTY");
			headersSMART = insert(headersSMART, "SMART SUB-TOTAL_" + "AMOUNT");
			headersUHD = insert(headersUHD, "UHD SUB-TOTAL_" + "QTY");
			headersUHD = insert(headersUHD, "UHD SUB-TOTAL_" + "AMOUNT");
			headersCURVE = insert(headersCURVE, "CURVE SUB-TOTAL_" + "QTY");
			headersCURVE = insert(headersCURVE, "CURVE SUB-TOTAL_" + "AMOUNT");

			headers = BDDateUtil.list(headers, headersBASIC);
			headers = BDDateUtil.list(headers, headersDIGITAL);
			headers = BDDateUtil.list(headers, headersINTERNET);
			headers = BDDateUtil.list(headers, headersQUHD);

			headers = BDDateUtil.list(headers, headersSMART);
			headers = BDDateUtil.list(headers, headersUHD);
			headers = BDDateUtil.list(headers, headersCURVE);

			HashMap<String, ArrayList<HashMap<String, Object>>> priceMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelMapList.size(); m++) {
				if (priceMap.get(modelMapList.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelMapList.get(m));
					priceMap.put(modelMapList.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = priceMap
							.get(modelMapList.get(m).get("model").toString());
					modelList.add(modelMapList.get(m));
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

			
			// 查询门店机型销售数据
			List<HashMap<String, Object>> modeldataList = excelDao.selectModelListByHq(beginDate, endDate, searchStr,
					conditions, WebPageUtil.isHQRole());

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> countryMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modeldataList.size(); m++) {
				if (countryMap.get(modeldataList.get(m).get("country_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modeldataList.get(m));
					countryMap.put(modeldataList.get(m).get("country_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = countryMap
							.get(modeldataList.get(m).get("country_id").toString());
					modelList.add(modeldataList.get(m));
				}

			}

			 tBeginDate = beginDate.split("-")[0] + "-" + beginDate.split("-")[1] + "-01";
			 tEndDate = BDDateUtil.getLastDayOfMonth(Integer.parseInt(endDate.split("-")[0]),
					Integer.parseInt(endDate.split("-")[1]));
			// 根据门店取得对应销售数据与目标数据
			List<HashMap<String, Object>> targetList = excelDao.selectTargetByshop(searchStr, conditions, tBeginDate,
					tEndDate, "1", WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> targetMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < targetList.size(); m++) {
				if (targetMap.get(targetList.get(m).get("country_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(targetList.get(m));
					targetMap.put(targetList.get(m).get("country_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = targetMap
							.get(targetList.get(m).get("country_id").toString());
					modelList.add(targetList.get(m));
				}

			}

			 yearBegin = beginDate.split("-")[0];
			 yearEnd = endDate.split("-")[0];
			// 根据门店取得对应销售数据与目标数据
			List<HashMap<String, Object>> targetListYear = excelDao.selectTargetByYear(searchStr, conditions, yearBegin,
					yearEnd, "1", WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> targetMapYear = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < targetListYear.size(); m++) {
				if (targetMapYear.get(targetListYear.get(m).get("country_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(targetListYear.get(m));
					targetMapYear.put(targetListYear.get(m).get("country_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = targetMapYear
							.get(targetListYear.get(m).get("country_id").toString());
					modelList.add(targetListYear.get(m));
				}

			}

			// 用于放置表格数据
			LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
			// 查询门店所有数据
			List<HashMap<String, Object>> excels = excelDao.selectDatas(searchStr, conditions, beginDate, endDate,
					WebPageUtil.isHQRole());
			int rowOne = 7;
			for (int j = 0; j < excels.size(); j++) {
				BigDecimal  monthQty=BigDecimal.ZERO;
				BigDecimal monthTarget=BigDecimal.ZERO;
				BigDecimal quaQty=BigDecimal.ZERO;
				BigDecimal quaTarget=BigDecimal.ZERO;
				BigDecimal yearQty=BigDecimal.ZERO;
				BigDecimal yearTarget=BigDecimal.ZERO;
				Double ach=0.0;
				// 用于放置表格数据
				HashMap<String, Object> dataMap = new HashMap<String, Object>();

				String shop_id = excels.get(j).get("country_id").toString();
				dataMap.put("SELL-OUT INFORMATION SHEET_REG.", excels.get(j).get("reg"));
				dataMap.put("SELL-OUT INFORMATION SHEET_Country.", excels.get(j).get("country_name"));
				dataMap.put("SELL-OUT INFORMATION SHEET_TYPE", "TV");
				dataMap.put("Admin Name",excels.get(j).get("user_name") );
				dataMap.put("DATE OF Upload", excels.get(j).get("datadate"));
				BigDecimal bd = null;
				bd = new BigDecimal(excels.get(j).get("saleQty").toString());
				dataMap.put("TV SELL-OUT and TARGET_TTL TV SO_"+qHead+" Qty", Math.round(bd.doubleValue()));

				//dataMap.put("TV SELL-OUT and TARGET_TTL TV SO_ASP", Math.round(bd.doubleValue()));
				quaQty=bd;
				yearQty=bd;
				Double saleSum = 0.0;

				if (targetMap.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = targetMap.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {
						bd = new BigDecimal(modelList.get(i).get("targetQty").toString());
						dataMap.put("TV SELL-OUT and TARGET_"+qHead+" TARGET", Math.round(bd.doubleValue()));
						quaTarget=bd;
					}

				}
				
				BigDecimal   b   =   null;  
				
				
	
				if(quaTarget.doubleValue()==0.0 || quaTarget.doubleValue()==0) {
					dataMap.put("TV SELL-OUT and TARGET_Quarter Ach.", "100%");
				}else if(quaQty.doubleValue()==0.0 || quaQty.doubleValue()==0) {
					dataMap.put("TV SELL-OUT and TARGET_Quarter Ach.", "0%");
				}else {
					ach=quaQty.doubleValue()/quaTarget.doubleValue()*100;
					b=new BigDecimal(ach);
					dataMap.put("TV SELL-OUT and TARGET_Quarter Ach.", 
							b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
				}
				

				
				if (targetMapYear.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = targetMapYear.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {
						bd = new BigDecimal(modelList.get(i).get("targetQty").toString());
						dataMap.put("TV SELL-OUT and TARGET_Year TARGET", Math.round(bd.doubleValue()));
						yearTarget=bd;
					}

				}
				if(yearTarget.doubleValue()==0.0 || yearTarget.doubleValue()==0) {
					dataMap.put("TV SELL-OUT and TARGET_Year Ach.",  "100%");
				}else if(yearQty.doubleValue()==0.0 || yearQty.doubleValue()==0) {
					dataMap.put("TV SELL-OUT and TARGET_Year Ach.",  "0%");
				}else {
					ach=yearQty.doubleValue()/yearTarget.doubleValue()*100;
					b=new BigDecimal(ach);
					dataMap.put("TV SELL-OUT and TARGET_Year Ach.", 
							b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
				}
				

				

				BigDecimal totalBASICQTY = BigDecimal.ZERO;
				BigDecimal totalDIGITALQTY = BigDecimal.ZERO;
				BigDecimal totalINTERNETQTY = BigDecimal.ZERO;
				BigDecimal totalQUHDQTY = BigDecimal.ZERO;
				BigDecimal totalSMARTQTY = BigDecimal.ZERO;
				BigDecimal totalUHDQTY = BigDecimal.ZERO;
				BigDecimal totalCURVEQTY = BigDecimal.ZERO;

				BigDecimal totalBASICAMT = BigDecimal.ZERO;
				BigDecimal totalDIGITALAMT = BigDecimal.ZERO;
				BigDecimal totalINTERNETAMT = BigDecimal.ZERO;
				BigDecimal totalQUHDAMT = BigDecimal.ZERO;
				BigDecimal totalSMARTAMT = BigDecimal.ZERO;
				BigDecimal totalUHDAMT = BigDecimal.ZERO;
				BigDecimal totalCURVEAMT = BigDecimal.ZERO;

				// 根据门店取得对应机型销售数据
				if (countryMap.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = countryMap.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {
						if (priceMap.get(modelList.get(i).get("model")) != null) {
							ArrayList<HashMap<String, Object>> priceList = priceMap.get(modelList.get(i).get("model"));
							for (int p = 0; p < priceList.size(); p++) {
								bd = new BigDecimal(priceList.get(p).get("price").toString());
								String am = bd.toPlainString();
								double shop = Double.parseDouble(am);
								double price = shop / Integer.parseInt(priceList.get(p).get("country").toString());

								long lnum = Math.round(price);
								String key = "";
								if (modelList.get(i).get("spec").toString().contains("BASIC")) {
									totalBASICQTY = totalBASICQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalBASICAMT = totalBASICAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "BASIC LED" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";
								} else if (modelList.get(i).get("spec").toString().contains("DIGITAL")) {
									totalDIGITALQTY = totalDIGITALQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalDIGITALAMT = totalDIGITALAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "DIGITAL BASIC" + "_" + modelList.get(i).get("model") + "_" + lnum + "_"
											+ "sold";
								} else if (modelList.get(i).get("spec").toString().contains("INTERNET")) {
									totalINTERNETQTY = totalINTERNETQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalINTERNETAMT = totalINTERNETAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "DIGITAL INTERNET" + "_" + modelList.get(i).get("model") + "_" + lnum + "_"
											+ "sold";
								} else if (modelList.get(i).get("spec").toString().contains("QUHD")) {
									totalQUHDQTY = totalQUHDQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalQUHDAMT = totalQUHDAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "(QUHD) TV" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";
								} else if (modelList.get(i).get("spec").toString().contains("SMART")) {
									totalSMARTQTY = totalSMARTQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalSMARTAMT = totalSMARTAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "SMART TV" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";
								} else if (modelList.get(i).get("spec").toString().contains("UHD")) {
									totalUHDQTY = totalUHDQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalUHDAMT = totalUHDAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "UHD TV" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";
								} else if (modelList.get(i).get("spec").toString().contains("CURVE")) {
									totalCURVEQTY = totalCURVEQTY
											.add(new BigDecimal(modelList.get(i).get("saleQty").toString()));
									totalCURVEAMT = totalCURVEAMT
											.add(new BigDecimal(modelList.get(i).get("saleAmt").toString()));
									key = "CURVE TV" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";
								}
								bd = new BigDecimal(modelList.get(i).get("saleQty").toString());

								dataMap.put(key, Math.round(bd.doubleValue()));
							}

						}

					}

				}

				dataMap.put("LED SUB-TOTAL_" + "QTY", Math.round(totalBASICQTY.doubleValue()));
				dataMap.put("LED SUB-TOTAL_" + "AMOUNT", Math.round(totalBASICAMT.doubleValue()));
				dataMap.put("DIGITAL SUB-TOTAL_" + "QTY", Math.round(totalDIGITALQTY.doubleValue()));
				dataMap.put("DIGITAL SUB-TOTAL_" + "AMOUNT", Math.round(totalDIGITALAMT.doubleValue()));
				dataMap.put("INTERNET SUB-TOTAL_" + "QTY", Math.round(totalINTERNETQTY.doubleValue()));
				dataMap.put("INTERNET SUB-TOTAL_" + "AMOUNT", Math.round(totalINTERNETAMT.doubleValue()));
				dataMap.put("(QUHD) SUB-TOTAL_" + "QTY", Math.round(totalQUHDQTY.doubleValue()));
				dataMap.put("(QUHD) SUB-TOTAL_" + "AMOUNT", Math.round(totalQUHDAMT.doubleValue()));
				dataMap.put("SMART SUB-TOTAL_" + "QTY", Math.round(totalSMARTQTY.doubleValue()));
				dataMap.put("SMART SUB-TOTAL_" + "AMOUNT", Math.round(totalBASICAMT.doubleValue()));
				dataMap.put("UHD SUB-TOTAL_" + "QTY", Math.round(totalUHDQTY.doubleValue()));
				dataMap.put("UHD SUB-TOTAL_" + "AMOUNT", Math.round(totalUHDAMT.doubleValue()));
				dataMap.put("CURVE SUB-TOTAL_" + "QTY", Math.round(totalCURVEQTY.doubleValue()));
				dataMap.put("CURVE SUB-TOTAL_" + "AMOUNT", Math.round(totalCURVEAMT.doubleValue()));

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
			sheet.createFreezePane(7, 9, 9, 10);
			CellStyle cellStylename = wb.createCellStyle();// 表名样式
			cellStylename.setFont(fonthead);

			CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
			cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
			cellStyleinfo.setFont(fontinfo);

			CellStyle cellStyleDate = wb.createCellStyle();

			DataFormat dataFormat = wb.createDataFormat();

			cellStyleDate.setDataFormat(dataFormat.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
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
			cellStyleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
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
			cellStylePERCENT.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
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
			cell.setCellValue("TCL  BDSC ");// 标题
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

			// 第二行
			cell = rowSix.createCell((short) 5);
			cell.setCellValue("SUBTOTAL(9,F8:F" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,F8:F" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 6);
			cell.setCellValue("SUBTOTAL(9,G8:G" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,G8:G" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 7);
			cell.setCellValue("SUBTOTAL(9,H8:H" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,H8:H" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 8);
			cell.setCellValue("TEXT(F7/H7,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT(F7/H7,\"0.00%\")");

			// 第二行

			// 第二行
			cell = rowSix.createCell((short) 9);
			cell.setCellValue("SUBTOTAL(9,J8:J" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,J8:J" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 10);
			cell.setCellValue("TEXT(F7/J7,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT(F7/J7,\"0.00%\")");

			// 第二行
			cell = rowSix.createCell((short) 11);
			cell.setCellValue("SUBTOTAL(9,L8:L" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,L8:L" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 12);
			cell.setCellValue("SUBTOTAL(9,M8:M" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,M8:M" + size + ")");

			// 第二行
			cell = rowSix.createCell((short) 13);
			cell.setCellValue("SUBTOTAL(9,N8:N" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,N8:N" + size + ")");

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
			cell.setCellValue("SUBTOTAL(9,R8:R" + size + ")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("SUBTOTAL(9,R8:R" + size + ")");

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
			String[] line = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
					"S", "T", "U", "V", "W", "X", "Y", "Z" };

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
				cell.setCellValue("SUBTOTAL(9," + big[i] + "8:" + big[i] + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9," + big[i] + "8:" + big[i] + size + ")");

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

						if (i < 5) {
							cell.setCellStyle(cellStyleWHITE);
						} else if (i > 4 && i < 11) {
							cell.setCellStyle(cellStylehead);
						} else if (i > 10) {
							cell.setCellStyle(cellStyleYellow);
							if (s[0].contains("SUB-TOTAL") || s[0].contains("TTL")) {
								cell.setCellStyle(cellStyleRED);
							}
						}
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2, rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						if (i < 5) {
							cell.setCellStyle(cellStyleWHITE);
						} else if (i > 4 && i < 11) {
							cell.setCellStyle(cellStylehead);
						} else if (i > 10) {
							cell.setCellStyle(cellStyleYellow);
							if (s[0].contains("SUB-TOTAL") || s[0].contains("TTL")) {
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
						if (i < 11 || (i > 10 && k + 2 == 2)) {
							// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号

							sheet.addMergedRegion(new CellRangeAddress(k + 2, k + 2, (num), (num + cols - 1)));

						}
						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k + 2 + rows_max - s.length, num, num));
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
					if (dataMap.get(fields[c]) != null && dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString().contains("%");
						isGongshi = dataMap.get(fields[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fields[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c]).toString());
							int s = dataMap.get(fields[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c]).toString());

						}
						if(c==8 || c==10 ) {
							BDDateUtil.setColor(dataMap.get(fields[c]).toString(), contentCell, cellStyleRED, cellStyleGreen, cellStyleYellow);
						}
						
						
						
						
					} else {
						contentCell.setCellValue("");
					}

					/*
					 * if (dataMap.get(fields[c]) != null &&
					 * dataMap.get(fields[c]).toString().length() > 0) {
					 * cell.setCellValue(dataMap.get(fields[c]).toString()); // 信息 } else {
					 * cell.setCellValue(""); // 信息
					 * 
					 * }
					 */
					// sheet.addMergedRegion(new CellRangeAddress(0, (short)
					// 2, 0,
					// (short)
					// c));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void comparativeByHqQua(String what, SXSSFWorkbook wb, String partyName, String beginDateOne,
			String endDateOne, String tBeginDate, String tEndDate, String searchStr, String conditions)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		String yearStr = calendar.get(Calendar.YEAR) + "";
		String beginDate = beginDateOne;
		String endDate = endDateOne;
		String[] days = beginDateOne.split("-");

		int month = calendar.get(Calendar.MONTH) + 1;// 获取月份
		String monthStr = month < 10 ? "0" + month : month + "";
		int dayy = calendar.get(Calendar.DATE);// 获取日
		String dayStr = dayy < 10 ? "0" + dayy : dayy + "";
		if (what.equals("year")) {
			if (yearStr.equals(days[0])) {
				endDate = yearStr + "-" + monthStr + "-" + dayStr;
				endDateOne = yearStr + "-" + monthStr + "-" + dayStr;
			}
		}

		String[] daysEnd = endDateOne.split("-");
		Date timeb = format.parse(beginDateOne);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(timeb);
		Date daysOne = format.parse(format.format(rightNow.getTime()));
		String[] toYear = endDateOne.split("-");

		Date timec = format.parse(beginDateOne);
		Calendar rightNowc = Calendar.getInstance();
		rightNowc.setTime(timec);
		Date toYearOne = format.parse(format.format(rightNowc.getTime()));

		int laYear = Integer.parseInt(toYear[0].toString()) - 1;
		String laDays = BDDateUtil.getLastDayOfMonth(laYear, Integer.parseInt(toYear[1]));
		String[] laDay = laDays.split("-");
		// 表头数据

		// 用于放置表格数据

		BigDecimal bds = null;
		List<HashMap<String, Object>> excels = excelDao.selectDatas(searchStr, conditions, beginDate, endDate,
				WebPageUtil.isHQRole());
		HashMap<String, Object> dataThrees = new HashMap<String, Object>();
		HashMap<String, Object> datFours = new HashMap<String, Object>();
		HashMap<String, Object> dataFives = new HashMap<String, Object>();
		BigDecimal qtyTotal = BigDecimal.ZERO;
		BigDecimal amtTotal = BigDecimal.ZERO;
		String headKey = "";
		String headKeyNow = "";
		String AveKey = "";
		String lastDayt[] = BDDateUtil.getLastDayOfMonth(Integer.parseInt(days[0]), Integer.parseInt(days[1]))
				.split("-");
		int dayNow = Integer.parseInt(lastDayt[2]);
		int dayLast = Integer.parseInt(lastDayt[2]);

		if (what.equals("custom")) {
			headKey = laYear + "";
			headKeyNow = toYear[0] + "";
			Calendar a = Calendar.getInstance(), b = Calendar.getInstance();
			a.set(Integer.parseInt(days[0]), Integer.parseInt(days[1]), Integer.parseInt(days[2]));
			b.set(Integer.parseInt(toYear[0]), Integer.parseInt(toYear[1]), Integer.parseInt(toYear[2]));
			long diffDays = (b.getTimeInMillis() - a.getTimeInMillis()) / (1000 * 60 * 60 * 24);
			dayNow = (int) diffDays;
			dayLast = (int) diffDays;
			AveKey = "Ave/day";
		} else if (what.equals("quar")) {

			if (days[1].equals("01") || days[1].equals("1")) {
				headKey = "Q1." + laYear;
				headKeyNow = "Q1." + toYear[0];
			} else if (days[1].equals("04") || days[1].equals("4")) {
				headKey = "Q2." + laYear;
				headKeyNow = "Q2." + toYear[0];
			} else if (days[1].equals("07") || days[1].equals("7")) {
				headKey = "Q3." + laYear;
				headKeyNow = "Q3." + toYear[0];
			} else if (days[1].equals("10")) {
				headKey = "Q4." + laYear;
				headKeyNow = "Q4." + toYear[0];
			}
			dayNow = 3;
			dayLast = 3;
			AveKey = "Ave/Month";
		} else if (what.equals("year")) {
			headKey = laYear + "";
			headKeyNow = toYear[0] + "";
			dayNow = Integer.parseInt(daysEnd[1]);
			;
			dayLast = 12;
			AveKey = "Ave/Month";
		}
		String[] headers = { headKey };
		dataThrees.put(headKey, headKeyNow);
		datFours.put(headKey, "TTL QTY");
		dataFives.put(headKey, AveKey);

		qtyTotal = BigDecimal.ZERO;
		amtTotal = BigDecimal.ZERO;
		Map<String, BigDecimal> mapTotal = new HashMap<String, BigDecimal>();
		
		for (int i = 0; i < excels.size(); i++) {

			bds = new BigDecimal(excels.get(i).get("saleQty").toString());
			datFours.put(excels.get(i).get("country_name").toString(), Math.round(bds.doubleValue()));
			dataFives.put(excels.get(i).get("country_name").toString(), Math.round(bds.doubleValue() / dayNow));

			qtyTotal = qtyTotal.add(new BigDecimal(excels.get(i).get("saleQty").toString()));
			amtTotal = amtTotal.add(new BigDecimal(excels.get(i).get("saleAmt").toString()));
			
			BigDecimal numOne = mapTotal.get(excels.get(i).get("country_name").toString()+"_Now");
			if (numOne == null) {
				numOne = BigDecimal.ZERO;
			}
			
			mapTotal.put(excels.get(i).get("country_name").toString()+"_Now", numOne.add(new BigDecimal(excels.get(i).get("saleQty").toString())));
		
		}
		dataThrees.put("Total", "Total");
		dataThrees.put("Amount", "Amount");

		datFours.put("Total", Math.round(qtyTotal.doubleValue()));
		datFours.put("Amount", Math.round(amtTotal.doubleValue()));
		dataFives.put("Total", Math.round(qtyTotal.doubleValue() / dayNow));
		dataFives.put("Amount", Math.round(amtTotal.doubleValue() / dayNow));

		String[] headersFive = { "YTD-" + toYear[0] + "  Monthly sellout trend per size_Country",
				"YTD-" + toYear[0] + "  Monthly sellout trend per size_Category"
		};
		String fiveNowKey = "";
		String fiveLastKey = "";
		if (what.equals("custom")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_" + laYear);
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_" + toYear[0]);
			fiveNowKey = "YTD-" + toYear[0] + "  Monthly sellout trend per size_" + toYear[0];
			fiveLastKey = "YTD-" + toYear[0] + "  Monthly sellout trend per size_" + laYear;
		} else if (what.equals("quar")) {
			String q = "";
			if (days[1].equals("01") || days[1].equals("1")) {
				q = "Q1.";
			} else if (days[1].equals("04") || days[1].equals("4")) {
				q = "Q2.";

			} else if (days[1].equals("07") || days[1].equals("7")) {
				q = "Q3.";
			} else if (days[1].equals("10")) {
				q = "Q4.";
			}
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_" + q + laYear);
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_" + q + toYear[0]);

			fiveNowKey = "YTD-" + toYear[0] + "  Monthly sellout trend per size_" + q + toYear[0];
			fiveLastKey = "YTD-" + toYear[0] + "  Monthly sellout trend per size_" + q + laYear;

		} else if (what.equals("year")) {
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_" + laYear);
			headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_" + toYear[0]);
			fiveNowKey = "YTD-" + toYear[0] + "  Monthly sellout trend per size_" + toYear[0];
			fiveLastKey = "YTD-" + toYear[0] + "  Monthly sellout trend per size_" + laYear;
		}

		headersFive = insert(headersFive, "YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth");

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

		String[] headersSix = { "Market Share_Country", "Market Share_Category"
				// "Market Share_TTL"

		};
		String sixNowKey = "";
		String sixLastKey = "";
		if (what.equals("custom")) {
			headersSix = insert(headersSix, "Market Share_" + laYear);
			headersSix = insert(headersSix, "Market Share_" + toYear[0]);
			sixNowKey = "Market Share_" + toYear[0];
			sixLastKey = "Market Share_" + laYear;
		} else if (what.equals("quar")) {
			String q = "";
			if (days[1].equals("01") || days[1].equals("1")) {
				q = "Q1.";
			} else if (days[1].equals("04") || days[1].equals("4")) {
				q = "Q2.";

			} else if (days[1].equals("07") || days[1].equals("7")) {
				q = "Q3.";
			} else if (days[1].equals("10")) {
				q = "Q4.";
			}
			headersSix = insert(headersSix, "Market Share_" + q + laYear);
			headersSix = insert(headersSix, "Market Share_" + q + toYear[0]);

			sixNowKey = "Market Share_" + q + toYear[0];
			sixLastKey = "Market Share_" + q + laYear;

		} else if (what.equals("year")) {
			headersSix = insert(headersSix, "Market Share_" + laYear);
			headersSix = insert(headersSix, "Market Share_" + toYear[0]);
			sixNowKey = "Market Share_" + toYear[0];
			sixLastKey = "Market Share_" + laYear;
		}

		headersSix = insert(headersSix, "Market Share_Growth");

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

		String[] headersEight = { "Different catgory sell-out quantity_Country",
				"Different catgory sell-out quantity_Category"

		};

		String eightNowKey = "";
		String eightLastKey = "";
		if (what.equals("custom")) {
			headersEight = insert(headersEight, "Different catgory sell-out quantity_" + laYear);
			headersEight = insert(headersEight, "Different catgory sell-out quantity_" + toYear[0]);
			eightNowKey = "Different catgory sell-out quantity_" + toYear[0];
			eightLastKey = "Different catgory sell-out quantity_" + laYear;
		} else if (what.equals("quar")) {
			String q = "";
			if (days[1].equals("01") || days[1].equals("1")) {
				q = "Q1.";
			} else if (days[1].equals("04") || days[1].equals("4")) {
				q = "Q2.";

			} else if (days[1].equals("07") || days[1].equals("7")) {
				q = "Q3.";
			} else if (days[1].equals("10")) {
				q = "Q4.";
			}
			headersEight = insert(headersEight, "Different catgory sell-out quantity_" + q + laYear);
			headersEight = insert(headersEight, "Different catgory sell-out quantity_" + q + toYear[0]);

			eightNowKey = "Different catgory sell-out quantity_" + q + toYear[0];
			eightLastKey = "Different catgory sell-out quantity_" + q + laYear;

		} else if (what.equals("year")) {
			headersEight = insert(headersEight, "Different catgory sell-out quantity_" + laYear);
			headersEight = insert(headersEight, "Different catgory sell-out quantity_" + toYear[0]);
			eightNowKey = "Different catgory sell-out quantity_" + toYear[0];
			eightLastKey = "Different catgory sell-out quantity_" + laYear;
		}

		headersEight = insert(headersEight, "Different catgory sell-out quantity_Growth");

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

		String[] headersNight = { "Growth rate_Country", "Growth rate_Category"

				// "Growth rate_TTL"

		};

		String NightNowKey = "";
		String NightLastKey = "";
		if (what.equals("custom")) {
			headersNight = insert(headersNight, "Growth rate_" + laYear);
			headersNight = insert(headersNight, "Growth rate_" + toYear[0]);
			NightNowKey = "Growth rate_" + toYear[0];
			NightLastKey = "Growth rate_" + laYear;
		} else if (what.equals("quar")) {
			String q = "";
			if (days[1].equals("01") || days[1].equals("1")) {
				q = "Q1.";
			} else if (days[1].equals("04") || days[1].equals("4")) {
				q = "Q2.";

			} else if (days[1].equals("07") || days[1].equals("7")) {
				q = "Q3.";
			} else if (days[1].equals("10")) {
				q = "Q4.";
			}
			headersNight = insert(headersNight, "Growth rate_" + q + laYear);
			headersNight = insert(headersNight, "Growth rate_" + q + toYear[0]);

			NightNowKey = "Growth rate_" + q + toYear[0];
			NightLastKey = "Growth rate_" + q + laYear;

		} else if (what.equals("year")) {
			headersNight = insert(headersNight, "Growth rate_" + laYear);
			headersNight = insert(headersNight, "Growth rate_" + toYear[0]);
			NightNowKey = "Growth rate_" + toYear[0];
			NightLastKey = "Growth rate_" + laYear;
		}

		headersNight = insert(headersNight, "Growth rate_Growth");

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
		String[] headersTen = { "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Country",
				"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series",
				"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Model"

		};

		String TenNowKey = "";
		String TenLastKey = "";
		if (what.equals("custom")) {
			headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_" + laYear);
			headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_" + toYear[0]);
			TenNowKey = "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_" + toYear[0];
			TenLastKey = "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_" + laYear;
		} else if (what.equals("quar")) {
			String q = "";
			if (days[1].equals("01") || days[1].equals("1")) {
				q = "Q1.";
			} else if (days[1].equals("04") || days[1].equals("4")) {
				q = "Q2.";

			} else if (days[1].equals("07") || days[1].equals("7")) {
				q = "Q3.";
			} else if (days[1].equals("10")) {
				q = "Q4.";
			}
			headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_" + q + laYear);
			headersTen = insert(headersTen,
					"YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_" + q + toYear[0]);

			TenNowKey = "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_" + q + toYear[0];
			TenLastKey = "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_" + q + laYear;

		} else if (what.equals("year")) {
			headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_" + laYear);
			headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_" + toYear[0]);
			TenNowKey = "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_" + toYear[0];
			TenLastKey = "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_" + laYear;
		}

		headersTen = insert(headersTen, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth");

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
		BigDecimal bd = BigDecimal.ZERO;

		HashMap<String, Object> datas = new HashMap<String, Object>();
		HashMap<String, Object> dataTwos = new HashMap<String, Object>();
		HashMap<String, Object> dataSixs = new HashMap<String, Object>();

		BDDateUtil.year = Integer.parseInt(days[0]) - 1;
		BDDateUtil.month = Integer.parseInt(days[1]);

		beginDate = BDDateUtil.year + "-" + days[1] + "-" + days[2];
		endDate = BDDateUtil.year + "-" + daysEnd[1] + "-" + daysEnd[2];
		List<HashMap<String, Object>> laExcels = excelDao.selectDatas(searchStr, conditions, beginDate, endDate,
				WebPageUtil.isHQRole());
		lastDayt = BDDateUtil.getLastDayOfMonth(BDDateUtil.year, BDDateUtil.month).split("-");
		String key = "";
		BigDecimal qtyTotalLast = BigDecimal.ZERO;
		BigDecimal amtTotalLast = BigDecimal.ZERO;
		for (int i = 0; i < laExcels.size(); i++) {
			bd = new BigDecimal(laExcels.get(i).get("saleQty").toString());
			qtyTotalLast = qtyTotalLast.add(new BigDecimal(laExcels.get(i).get("saleQty").toString()));
			amtTotalLast = amtTotalLast.add(new BigDecimal(laExcels.get(i).get("saleAmt").toString()));
			key = laExcels.get(i).get("country_name").toString();
			datas.put(key, Math.round(bd.doubleValue()));
			dataTwos.put(key, Math.round(bd.doubleValue() / dayLast));
			BigDecimal numOne = mapTotal.get(laExcels.get(i).get("country_name").toString()+"_Last");
			if (numOne == null) {
				numOne = BigDecimal.ZERO;
			}
			
			mapTotal.put(laExcels.get(i).get("country_name").toString()+"_Last", numOne.add(bd));
		
		}
		
		datas.put("Total", Math.round(qtyTotalLast.doubleValue()));
		datas.put("Amount", Math.round(amtTotalLast.doubleValue()));
		dataTwos.put("Total", Math.round(qtyTotalLast.doubleValue() / dayLast));
		dataTwos.put("Amount", Math.round(amtTotalLast.doubleValue() / dayLast));
		double gro=0.0;
		String[] sizeList = { "20-28", "32", "39-43", "48-50", "55", ">=60" };

		String[] str = {};
		countryList = str;
		if (laExcels.size() > excels.size()) {
			for (int i = 0; i < laExcels.size(); i++) {
				key = laExcels.get(i).get("country_name").toString();

				if(mapTotal.get(key+"_Last")!=null) {
					if(mapTotal.get(key+"_Now")!=null) {
						BigDecimal now=mapTotal.get(key+"_Now");
						BigDecimal last=mapTotal.get(key+"_Last");
						gro=(now.doubleValue()-last.doubleValue())/last.doubleValue()*100;
						if(last.doubleValue()==0.0 || last.doubleValue()==0) {
							dataSixs.put(key, "100%");
						}else {
							dataSixs.put(key, Math.round(gro)+"%");
						}
					}
				}else {
					dataSixs.put(key, "100%");
				}
				
				headers = insert(headers, laExcels.get(i).get("country_name").toString());
				countryList = insert(countryList, laExcels.get(i).get("country_name").toString());
				dataThrees.put(laExcels.get(i).get("country_name").toString(),
						laExcels.get(i).get("country_name").toString());
			}
			
		
			if(qtyTotalLast.doubleValue() / dayLast!=0.0 && qtyTotalLast.doubleValue() / dayLast!=0) {
				double c=	(qtyTotal.doubleValue() / dayNow) -(qtyTotalLast.doubleValue() / dayLast);
				gro=c/(qtyTotalLast.doubleValue() / dayLast)*100;
				dataSixs.put("Total",Math.round(gro)+"%");
				}else {
					dataSixs.put("Total", "100%");
				}
				
				if(amtTotalLast.doubleValue() / dayLast!=0.0 && amtTotalLast.doubleValue() / dayLast!=0) {
					double c=	(amtTotal.doubleValue() / dayNow) -(amtTotalLast.doubleValue() / dayLast);
					gro=c/(amtTotalLast.doubleValue() / dayLast)*100;
					dataSixs.put("Amount",Math.round(gro)+"%");
					}else {
						dataSixs.put("Amount", "100%");
					}
				

		} else {
			for (int i = 0; i < excels.size(); i++) {

				key = excels.get(i).get("country_name").toString();


				if(mapTotal.get(key+"_Last")!=null) {
					if(mapTotal.get(key+"_Now")!=null) {
						BigDecimal now=mapTotal.get(key+"_Now");
						BigDecimal last=mapTotal.get(key+"_Last");
						gro=(now.doubleValue()-last.doubleValue())/last.doubleValue()*100;
						if(last.doubleValue()==0.0 || last.doubleValue()==0) {
							dataSixs.put(key, "100%");
						}else {
							dataSixs.put(key, Math.round(gro)+"%");
						}
					}
				}else {
					dataSixs.put(key, "100%");
				}
				
				headers = insert(headers, excels.get(i).get("country_name").toString());
				countryList = insert(countryList, excels.get(i).get("country_name").toString());
				dataThrees.put(excels.get(i).get("country_name").toString(),
						excels.get(i).get("country_name").toString());
			}

			if(qtyTotalLast.doubleValue() / dayLast!=0.0 && qtyTotalLast.doubleValue() / dayLast!=0) {
				double c=	(qtyTotal.doubleValue() / dayNow) -(qtyTotalLast.doubleValue() / dayLast);
				gro=c/(qtyTotalLast.doubleValue() / dayLast)*100;
				dataSixs.put("Total",Math.round(gro)+"%");
				}else {
					dataSixs.put("Total", "100%");
				}
				
				if(amtTotalLast.doubleValue() / dayLast!=0.0 && amtTotalLast.doubleValue() / dayLast!=0) {
					double c=	(amtTotal.doubleValue() / dayNow) -(amtTotalLast.doubleValue() / dayLast);
					gro=c/(amtTotalLast.doubleValue() / dayLast)*100;
					dataSixs.put("Amount",Math.round(gro)+"%");
					}else {
						dataSixs.put("Amount", "100%");
					}
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

		

		start = System.currentTimeMillis();

		datas.put(headKey, "TTL QTY");
		dataTwos.put(headKey, AveKey);
		if (what.equals("custom")) {
			dataSixs.put(headKey, "Sellout Growth per day");
		} else {
			dataSixs.put(headKey, "Sellout Growth per month");
		}

		dataList.add(datas);
		dataList.add(dataTwos);
		dataList.add(dataThrees);
		dataList.add(datFours);
		dataList.add(dataFives);
		dataList.add(dataSixs);

		System.out.println("=====第4个查询=" + (System.currentTimeMillis() - start));

		LinkedList<HashMap<String, Object>> dataListFive = new LinkedList<HashMap<String, Object>>();
		LinkedList<HashMap<String, Object>> dataListSix = new LinkedList<HashMap<String, Object>>();
		start = System.currentTimeMillis();

		String Month = "";

		if (toYear[1].equals("01")) {
			Month = "Jan.";
		} else if (toYear[1].equals("02")) {
			Month = "Feb.";
		} else if (toYear[1].equals("03")) {
			Month = "Mar.";
		} else if (toYear[1].equals("04")) {
			Month = "Apr.";
		} else if (toYear[1].equals("05")) {
			Month = "May.";
		} else if (toYear[1].equals("06")) {
			Month = "June.";
		} else if (toYear[1].equals("07")) {
			Month = "July.";
		} else if (toYear[1].equals("08")) {
			Month = "Aug.";
		} else if (toYear[1].equals("09")) {
			Month = "Sept.";
		} else if (toYear[1].equals("10")) {
			Month = "Oct.";
		} else if (toYear[1].equals("11")) {
			Month = "Nov.";
		} else if (toYear[1].equals("12")) {
			Month = "Dec.";
		}

		List<HashMap<String, Object>> sizeDatasOne = excelDao.selectSaleDataBySize(beginDateOne, endDateOne, searchStr,
				conditions, WebPageUtil.isHQRole());

		List<HashMap<String, Object>> sizeDatasOneData = excelDao.selectSaleTotalBySize(beginDateOne, endDateOne,
				searchStr, conditions, WebPageUtil.isHQRole());

		beginDate = laYear + "-" + days[1] + "-" + days[2];
		endDate = laYear + "-" + daysEnd[1] + "-" + daysEnd[2];

		List<HashMap<String, Object>> sizeDatasOneLast = excelDao.selectSaleDataBySize(beginDate, endDate, searchStr,
				conditions, WebPageUtil.isHQRole());

		List<HashMap<String, Object>> sizeDatasOneDataLast = excelDao.selectSaleTotalBySize(beginDate, endDate,
				searchStr, conditions, WebPageUtil.isHQRole());

		BigDecimal lastQty = BigDecimal.ZERO;

		String nowKey = fiveNowKey;
		String lastKey = fiveLastKey;

		String proNow = sixNowKey;

		String proLast = sixLastKey;

		Map<String, BigDecimal> mapResultOne = new HashMap<String, BigDecimal>();

		for (int i = 0; i < countryList.length; i++) {
			BigDecimal bdTotal = BigDecimal.ZERO;
			BigDecimal lastQtyTotal = BigDecimal.ZERO;
			HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();
			HashMap<String, Object> dataSixTotal = new HashMap<String, Object>();
			for (int j = 0; j < sizeList.length; j++) {
				bd = BigDecimal.ZERO;
				lastQty = BigDecimal.ZERO;
				bdTotal = BigDecimal.ZERO;
				lastQtyTotal = BigDecimal.ZERO;
				HashMap<String, Object> datasix = new HashMap<String, Object>();
				HashMap<String, Object> dataFive = new HashMap<String, Object>();
				dataFive.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Country", countryList[i]);
				dataFive.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Category", sizeList[j]);

				datasix.put("Market Share_Country", countryList[i]);
				datasix.put("Market Share_Category", sizeList[j]);
				if (sizeDatasOneData.size() > 0) {
					for (int k = 0; k < sizeDatasOneData.size(); k++) {
						if (sizeDatasOneData.get(k).get("party_name").toString().equals(countryList[i])) {
							bdTotal = new BigDecimal(sizeDatasOneData.get(k).get("saleQty").toString());
						}
					}
				} else {
					bdTotal = BigDecimal.ZERO;
				}

				for (int c = 0; c < sizeDatasOne.size(); c++) {
					
					if (sizeDatasOne.get(c).get("party_name").toString().equals(countryList[i])
							&& sizeDatasOne.get(c).get("sizeT").toString().equals(sizeList[j])) {
						bd = new BigDecimal(sizeDatasOne.get(c).get("saleQty").toString());
						dataFive.put(nowKey, Math.round(bd.doubleValue()));
						datasix.put(proNow, Math.round(bd.doubleValue() / bdTotal.doubleValue() * 100) + "%");
						BigDecimal numOne = mapResultOne.get(sizeList[j] + "_" + nowKey);
						BigDecimal numOneSize = mapResultOne.get(nowKey);
						if (numOne == null) {
							numOne = BigDecimal.ZERO;
						}
						if (numOneSize == null) {
							numOneSize = BigDecimal.ZERO;
						}
						mapResultOne.put(sizeList[j] + "_" + nowKey, numOne.add(bd));
						mapResultOne.put(nowKey, numOneSize.add(bd));

					}
				}
				if (sizeDatasOneDataLast.size() > 0) {
					for (int k = 0; k < sizeDatasOneDataLast.size(); k++) {
						if (sizeDatasOneDataLast.get(k).get("party_name").toString().equals(countryList[i])) {
							lastQtyTotal = new BigDecimal(sizeDatasOneDataLast.get(k).get("saleQty").toString());
						}
					}
				} else {
					lastQtyTotal = BigDecimal.ZERO;
				}

				dataFiveTotal.put(nowKey, Math.round(bdTotal.doubleValue()));
				for (int c = 0; c < sizeDatasOneLast.size(); c++) {
				
					if (sizeDatasOneLast.get(c).get("party_name").toString().equals(countryList[i])
							&& sizeDatasOneLast.get(c).get("sizeT").toString().equals(sizeList[j])) {
						lastQty = new BigDecimal(sizeDatasOneLast.get(c).get("saleQty").toString());

						dataFive.put(lastKey, Math.round(lastQty.doubleValue()));
						datasix.put(proLast,
								Math.round(lastQty.doubleValue() / lastQtyTotal.doubleValue() * 100) + "%");
						BigDecimal numOne = mapResultOne.get(sizeList[j] + "_" + lastKey);
						BigDecimal numOneSize = mapResultOne.get(lastKey);
						if (numOne == null) {
							numOne = BigDecimal.ZERO;
						}
						if (numOneSize == null) {
							numOneSize = BigDecimal.ZERO;
						}
						mapResultOne.put(sizeList[j] + "_" + lastKey, numOne.add(bd));
						mapResultOne.put(lastKey, numOneSize.add(bd));

					}
				}
				dataFiveTotal.put(lastKey, Math.round(lastQtyTotal.doubleValue()));
				// 同比增长率=（本期数－同期数）÷同期数×100%
				if (Math.round(bd.doubleValue()) == 0) {
					dataFive.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "-100%");
					datasix.put("Market Share_Growth", "-100%");
				} else if (Math.round(lastQty.doubleValue()) == 0) {
					dataFive.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "100%");
					datasix.put("Market Share_Growth", "100%");
				} else {
					double poor = (bd.doubleValue() - lastQty.doubleValue()) / lastQty.doubleValue() * 100;
					dataFive.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth",
							Math.round(poor) + "%");
					datasix.put("Market Share_Growth", Math.round(poor) + "%");
				}

				dataListFive.add(dataFive);
				dataListSix.add(datasix);
			}
			dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Category", "Total");
			
			if(lastQtyTotal.doubleValue()==0.0) {
				dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "100%");
				
			}else {
				gro=(bdTotal.doubleValue()-lastQtyTotal.doubleValue())/lastQtyTotal.doubleValue()*100;
				dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", Math.round(gro)+"%");
			}
			
			
			dataListFive.add(dataFiveTotal);

			dataSixTotal.put("Market Share_Category", "Total");
			dataSixTotal.put(proNow, "100%");
			dataSixTotal.put(proLast, "100%");
			dataSixTotal.put("Market Share_Growth", "100%");
			dataListSix.add(dataSixTotal);

			if (WebPageUtil.isHQRole()) {
				dataFiveTotal = new HashMap<String, Object>();
				dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Country", "Country");
				dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Category", "Category");
				dataFiveTotal.put(nowKey, nowKey.split("_")[1]);
				dataFiveTotal.put(lastKey, lastKey.split("_")[1]);
				dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "Growth");
				dataListFive.add(dataFiveTotal);
				dataSixTotal = new HashMap<String, Object>();
				dataSixTotal.put("Market Share_Country", "Country");
				dataSixTotal.put("Market Share_Category", "Category");
				dataSixTotal.put(proNow, proNow.split("_")[1]);
				dataSixTotal.put(proLast, proLast.split("_")[1]);
				dataSixTotal.put("Market Share_Growth", "Growth");
				dataListSix.add(dataSixTotal);
			} else {
				if (countryList.length - 1 != i) {
					dataFiveTotal = new HashMap<String, Object>();
					dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Country", "Country");
					dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Category", "Category");
					dataFiveTotal.put(nowKey, nowKey.split("_")[1]);
					dataFiveTotal.put(lastKey, lastKey.split("_")[1]);
					dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "Growth");
					dataListFive.add(dataFiveTotal);
					dataSixTotal = new HashMap<String, Object>();
					dataSixTotal.put("Market Share_Country", "Country");
					dataSixTotal.put("Market Share_Category", "Category");
					dataSixTotal.put(proNow, proNow.split("_")[1]);
					dataSixTotal.put(proLast, proLast.split("_")[1]);
					dataSixTotal.put("Market Share_Growth", "Growth");
					dataListSix.add(dataSixTotal);
				}
			}

		}

		if (WebPageUtil.isHQRole()) {
			BigDecimal bdTotal = BigDecimal.ZERO;
			BigDecimal lastTotal = BigDecimal.ZERO;

			if (mapResultOne.get(nowKey) != null) {
				bdTotal = mapResultOne.get(nowKey);
			}
			if (mapResultOne.get(lastKey) != null) {
				lastTotal = mapResultOne.get(lastKey);
			}
			for (int k = 0; k < sizeList.length; k++) {
				HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();
				HashMap<String, Object> dataSixTotal = new HashMap<String, Object>();
				dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Country", "BDSC");
				dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Category", sizeList[k]);

				dataSixTotal.put("Market Share_Country", "BDSC");
				dataSixTotal.put("Market Share_Category", sizeList[k]);

				if (mapResultOne.get(sizeList[k] + "_" + nowKey) != null) {
					bd = mapResultOne.get(sizeList[k] + "_" + nowKey);
				} else {
					bd = BigDecimal.ZERO;
				}
				if (mapResultOne.get(sizeList[k] + "_" + lastKey) != null) {
					lastQty = mapResultOne.get(sizeList[k] + "_" + lastKey);
				} else {
					lastQty = BigDecimal.ZERO;
				}

				dataFiveTotal.put(lastKey, Math.round(lastQty.doubleValue()));
				dataFiveTotal.put(nowKey, Math.round(bd.doubleValue()));
				if (Math.round(bd.doubleValue()) == 0) {
					dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "-100%");
					dataSixTotal.put("Market Share_Growth", "-100%");
				} else if (Math.round(lastQty.doubleValue()) == 0) {
					dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "100%");
					dataSixTotal.put("Market Share_Growth", "100%");
				} else {
					double poor = (bd.doubleValue() - lastQty.doubleValue()) / lastQty.doubleValue() * 100;
					dataFiveTotal.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth",
							Math.round(poor) + "%");
					dataSixTotal.put("Market Share_Growth", Math.round(poor) + "%");
				}
				dataSixTotal.put(proNow, Math.round(bd.doubleValue() / bdTotal.doubleValue() * 100) + "%");
				dataSixTotal.put(proLast, Math.round(lastQty.doubleValue() / lastTotal.doubleValue() * 100) + "%");
				dataListFive.add(dataFiveTotal);
				dataListSix.add(dataSixTotal);
			}

			HashMap<String, Object> dataFiveTotals = new HashMap<String, Object>();
			HashMap<String, Object> dataSixTotals = new HashMap<String, Object>();
			dataFiveTotals.put(nowKey, Math.round(bdTotal.doubleValue()));
			dataFiveTotals.put(lastKey, Math.round(lastTotal.doubleValue()));
			dataFiveTotals.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Category", "Total");
			dataFiveTotals.put("YTD-" + toYear[0] + "  Monthly sellout trend per size_Growth", "100%");
			dataListFive.add(dataFiveTotals);

			dataSixTotals.put("Market Share_Category", "Total");
			dataSixTotals.put(proNow, "100%");
			dataSixTotals.put(proLast, "100%");
			dataSixTotals.put("Market Share_Growth", "100%");
			dataListSix.add(dataSixTotals);

		}

		/*
		 * dataFiveTotals= new HashMap<String, Object>(); dataFiveTotals.put("YTD-" +
		 * toYear[0] + "  Monthly sellout trend per size_Category", "Category");
		 * dataFiveTotals.put(nowKey, Month+""+toYear[0]); dataFiveTotals.put(lastKey,
		 * Month+""+laYear); dataFiveTotals.put("YTD-" + toYear[0] +
		 * "  Monthly sellout trend per size_Growth", "Growth");
		 * dataListFive.add(dataFiveTotals); dataSixTotals= new HashMap<String,
		 * Object>(); dataSixTotals.put("Market Share_Category", "Category");
		 * dataSixTotals.put(proNow, Month+""+toYear[0]); dataSixTotals.put(proLast,
		 * Month+""+laYear); dataSixTotals.put("Market Share_Growth", "Growth");
		 * dataListSix.add(dataSixTotals);
		 * 
		 */

		LinkedList<HashMap<String, Object>> dataListEight = new LinkedList<HashMap<String, Object>>();

		LinkedList<HashMap<String, Object>> dataListNight = new LinkedList<HashMap<String, Object>>();

		List<HashMap<String, Object>> specDatasOne = excelDao.selectSaleDataBySpec(beginDateOne, endDateOne, searchStr,
				conditions, WebPageUtil.isHQRole());

		List<HashMap<String, Object>> specDatasOneData = excelDao.selectSaleTotalBySpec(beginDateOne, endDateOne,
				searchStr, conditions, WebPageUtil.isHQRole());

		beginDate = BDDateUtil.year + "-" + days[1] + "-" + days[2];
		endDate = BDDateUtil.year + "-" + daysEnd[1] + "-" + daysEnd[2];

		List<HashMap<String, Object>> specDatasOneLast = excelDao.selectSaleDataBySpec(beginDate, endDate, searchStr,
				conditions, WebPageUtil.isHQRole());

		List<HashMap<String, Object>> specDatasOneDataLast = excelDao.selectSaleTotalBySpec(beginDate, endDate,
				searchStr, conditions, WebPageUtil.isHQRole());

		lastQty = BigDecimal.ZERO;

		nowKey = eightNowKey;
		lastKey = eightLastKey;

		proNow = NightNowKey;

		proLast = NightLastKey;
		Map<String, BigDecimal> mapResultTwo = new HashMap<String, BigDecimal>();

		for (int i = 0; i < countryList.length; i++) {
			BigDecimal bdTotal = BigDecimal.ZERO;
			BigDecimal lastQtyTotal = BigDecimal.ZERO;
			HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();
			HashMap<String, Object> dataSixTotal = new HashMap<String, Object>();
			for (int j = 0; j < specList.length; j++) {
				bd = BigDecimal.ZERO;
				lastQty = BigDecimal.ZERO;
				bdTotal = BigDecimal.ZERO;
				lastQtyTotal = BigDecimal.ZERO;
				HashMap<String, Object> datasix = new HashMap<String, Object>();
				HashMap<String, Object> dataFive = new HashMap<String, Object>();
				dataFive.put("Different catgory sell-out quantity_Country", countryList[i]);
				dataFive.put("Different catgory sell-out quantity_Category", specList[j]);

				datasix.put("Growth rate_Country", countryList[i]);
				datasix.put("Growth rate_Category", specList[j]);
				if (specDatasOneData.size() > 0) {
					for (int k = 0; k < specDatasOneData.size(); k++) {
						if (specDatasOneData.get(k).get("party_name").toString().equals(countryList[i])) {
							bdTotal = new BigDecimal(specDatasOneData.get(k).get("saleQty").toString());
						}
					}
				} else {
					bdTotal = BigDecimal.ZERO;
				}

				for (int c = 0; c < specDatasOne.size(); c++) {
					bd = new BigDecimal(specDatasOne.get(c).get("saleQty").toString());

					if (specDatasOne.get(c).get("party_name").toString().equals(countryList[i])
							&& specDatasOne.get(c).get("SPEC").toString().equals(specList[j])) {
						dataFive.put(nowKey, Math.round(bd.doubleValue()));
						datasix.put(proNow, Math.round(bd.doubleValue() / bdTotal.doubleValue() * 100) + "%");
						BigDecimal numOne = mapResultTwo.get(specList[j] + "_" + nowKey);
						BigDecimal numOneSize = mapResultTwo.get(nowKey);
						if (numOne == null) {
							numOne = BigDecimal.ZERO;
						}
						if (numOneSize == null) {
							numOneSize = BigDecimal.ZERO;
						}
						mapResultTwo.put(specList[j] + "_" + nowKey, numOne.add(bd));
						mapResultTwo.put(nowKey, numOneSize.add(bd));

					}
				}
				if (specDatasOneDataLast.size() > 0) {
					for (int k = 0; k < specDatasOneDataLast.size(); k++) {
						if (specDatasOneDataLast.get(k).get("party_name").toString().equals(countryList[i])) {
							lastQtyTotal = new BigDecimal(specDatasOneDataLast.get(k).get("saleQty").toString());
						}
					}
				} else {
					lastQtyTotal = BigDecimal.ZERO;
				}

				dataFiveTotal.put(nowKey, Math.round(bdTotal.doubleValue()));
				for (int c = 0; c < specDatasOneLast.size(); c++) {
					lastQty = new BigDecimal(specDatasOneLast.get(c).get("saleQty").toString());

					if (specDatasOneLast.get(c).get("party_name").toString().equals(countryList[i])
							&& specDatasOneLast.get(c).get("SPEC").toString().equals(specList[j])) {
						dataFive.put(lastKey, Math.round(lastQty.doubleValue()));
						datasix.put(proLast,
								Math.round(lastQty.doubleValue() / lastQtyTotal.doubleValue() * 100) + "%");

						BigDecimal numOne = mapResultTwo.get(specList[j] + "_" + lastKey);
						BigDecimal numOneSize = mapResultTwo.get(lastKey);
						if (numOne == null) {
							numOne = BigDecimal.ZERO;
						}
						if (numOneSize == null) {
							numOneSize = BigDecimal.ZERO;
						}
						mapResultTwo.put(specList[j] + "_" + lastKey, numOne.add(lastQty));
						mapResultTwo.put(lastKey, numOneSize.add(lastQty));

					}
				}

				dataFiveTotal.put(lastKey, Math.round(lastQtyTotal.doubleValue()));
				// 同比增长率=（本期数－同期数）÷同期数×100%
				if (Math.round(bd.doubleValue()) == 0) {
					dataFive.put("Different catgory sell-out quantity_Growth", "-100%");
					datasix.put("Growth rate_Growth", "-100%");
				} else if (Math.round(lastQty.doubleValue()) == 0) {
					dataFive.put("Different catgory sell-out quantity_Growth", "100%");
					datasix.put("Growth rate_Growth", "100%");
				} else {
					double poor = (bd.doubleValue() - lastQty.doubleValue()) / lastQty.doubleValue() * 100;
					dataFive.put("Different catgory sell-out quantity_Growth", Math.round(poor) + "%");
					datasix.put("Growth rate_Growth", Math.round(poor) + "%");
				}

				dataListEight.add(dataFive);
				dataListNight.add(datasix);
			}
			dataFiveTotal.put("Different catgory sell-out quantity_Category", "Total");

			dataFiveTotal.put("Different catgory sell-out quantity_Growth", "100%");
			dataListEight.add(dataFiveTotal);

			dataSixTotal.put("Growth rate_Category", "Total");
			dataSixTotal.put(proNow, "100%");
			dataSixTotal.put(proLast, "100%");
			dataSixTotal.put("Growth rate_Growth", "100%");
			dataListNight.add(dataSixTotal);

			if (WebPageUtil.isHQRole()) {
				dataFiveTotal = new HashMap<String, Object>();
				dataFiveTotal.put("Different catgory sell-out quantity_Country", "Country");
				dataFiveTotal.put("Different catgory sell-out quantity_Category", "Category");
				dataFiveTotal.put(nowKey, nowKey.split("_")[1]);
				dataFiveTotal.put(lastKey, lastKey.split("_")[1]);
				dataFiveTotal.put("Different catgory sell-out quantity_Growth", "Growth");
				dataListEight.add(dataFiveTotal);
				dataSixTotal = new HashMap<String, Object>();
				dataSixTotal.put("Growth rate_Country", "Country");
				dataSixTotal.put("Growth rate_Category", "Category");
				dataSixTotal.put(proNow, proNow.split("_")[1]);
				dataSixTotal.put(proLast, proLast.split("_")[1]);
				dataSixTotal.put("Growth rate_Growth", "Growth");
				dataListNight.add(dataSixTotal);
			} else {
				if (countryList.length - 1 != i) {
					dataFiveTotal = new HashMap<String, Object>();
					dataFiveTotal.put("Different catgory sell-out quantity_Country", "Country");
					dataFiveTotal.put("Different catgory sell-out quantity_Category", "Category");
					dataFiveTotal.put(nowKey, nowKey.split("_")[1]);
					dataFiveTotal.put(lastKey, lastKey.split("_")[1]);
					dataFiveTotal.put("Different catgory sell-out quantity_Growth", "Growth");
					dataListEight.add(dataFiveTotal);
					dataSixTotal = new HashMap<String, Object>();
					dataSixTotal.put("Growth rate_Country", "Country");
					dataSixTotal.put("Growth rate_Category", "Category");
					dataSixTotal.put(proNow, proNow.split("_")[1]);
					dataSixTotal.put(proLast, proLast.split("_")[1]);
					dataSixTotal.put("Growth rate_Growth", "Growth");
					dataListNight.add(dataSixTotal);
				}
			}

		}
		if (WebPageUtil.isHQRole()) {
			BigDecimal bdTotal = BigDecimal.ZERO;
			BigDecimal lastTotal = BigDecimal.ZERO;

			if (mapResultTwo.get(nowKey) != null) {
				bdTotal = mapResultTwo.get(nowKey);
			}
			if (mapResultTwo.get(lastKey) != null) {
				lastTotal = mapResultTwo.get(lastKey);
			}
			for (int k = 0; k < specList.length; k++) {
				HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();
				HashMap<String, Object> dataSixTotal = new HashMap<String, Object>();

				dataFiveTotal.put("Different catgory sell-out quantity_Country", "BDSC");
				dataFiveTotal.put("Different catgory sell-out quantity_Category", specList[k]);

				dataSixTotal.put("Growth rate_Country", "BDSC");
				dataSixTotal.put("Growth rate_Category", specList[k]);

				if (mapResultTwo.get(specList[k] + "_" + nowKey) != null) {
					bd = mapResultTwo.get(specList[k] + "_" + nowKey);
				} else {
					bd = BigDecimal.ZERO;
				}
				if (mapResultTwo.get(specList[k] + "_" + lastKey) != null) {
					lastQty = mapResultTwo.get(specList[k] + "_" + lastKey);
				} else {
					lastQty = BigDecimal.ZERO;
				}

				dataFiveTotal.put(lastKey, Math.round(lastQty.doubleValue()));
				dataFiveTotal.put(nowKey, Math.round(bd.doubleValue()));
				if (Math.round(bd.doubleValue()) == 0) {
					dataFiveTotal.put("Different catgory sell-out quantity_Growth", "-100%");
					dataSixTotal.put("Growth rate_Growth", "-100%");
				} else if (Math.round(lastQty.doubleValue()) == 0) {
					dataFiveTotal.put("Different catgory sell-out quantity_Growth", "100%");
					dataSixTotal.put("Growth rate_Growth", "100%");
				} else {
					double poor = (bd.doubleValue() - lastQty.doubleValue()) / lastQty.doubleValue() * 100;
					dataFiveTotal.put("Different catgory sell-out quantity_Growth", Math.round(poor) + "%");
					dataSixTotal.put("Growth rate_Growth", Math.round(poor) + "%");
				}
				dataSixTotal.put(proNow, Math.round(bd.doubleValue() / bdTotal.doubleValue() * 100) + "%");
				dataSixTotal.put(proLast, Math.round(lastQty.doubleValue() / lastTotal.doubleValue() * 100) + "%");
				dataListEight.add(dataFiveTotal);
				dataListNight.add(dataSixTotal);
			}

			HashMap<String, Object> dataFiveTotals = new HashMap<String, Object>();
			HashMap<String, Object> dataSixTotals = new HashMap<String, Object>();
			dataFiveTotals.put(nowKey, Math.round(bdTotal.doubleValue()));
			dataFiveTotals.put(lastKey, Math.round(lastTotal.doubleValue()));
			dataFiveTotals.put("Different catgory sell-out quantity_Category", "Total");
			dataFiveTotals.put("Different catgory sell-out quantity_Growth", "100%");
			dataListEight.add(dataFiveTotals);

			dataSixTotals.put("Growth rate_Category", "Total");
			dataSixTotals.put(proNow, "100%");
			dataSixTotals.put(proLast, "100%");
			dataSixTotals.put("Growth rate_Growth", "100%");
			dataListNight.add(dataSixTotals);

		}

		LinkedList<HashMap<String, Object>> dataListTen = new LinkedList<HashMap<String, Object>>();

		List<HashMap<String, Object>> XCPDatasOne = excelDao.selectSaleDataByXCP(beginDateOne, endDateOne, searchStr,
				conditions, WebPageUtil.isHQRole());

		List<HashMap<String, Object>> XCPDatasOneData = excelDao.selectSaleTotalByXCP(beginDateOne, endDateOne,
				searchStr, conditions, WebPageUtil.isHQRole());

		HashMap<String, ArrayList<HashMap<String, Object>>> nowMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
		for (int m = 0; m < XCPDatasOne.size(); m++) {
			if (nowMap.get(XCPDatasOne.get(m).get("model").toString()) == null) {
				ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
				modelList.add(XCPDatasOne.get(m));
				nowMap.put(XCPDatasOne.get(m).get("model").toString(), modelList);
			} else {
				ArrayList<HashMap<String, Object>> modelList = nowMap.get(XCPDatasOne.get(m).get("model").toString());
				modelList.add(XCPDatasOne.get(m));
			}

		}

		beginDate = BDDateUtil.year + "-" + days[1] + "-" + days[2];
		endDate = BDDateUtil.year + "-" + daysEnd[1] + "-" + daysEnd[2];

		List<HashMap<String, Object>> XCPDatasOneLast = excelDao.selectSaleDataByXCP(beginDate, endDate, searchStr,
				conditions, WebPageUtil.isHQRole());

		List<HashMap<String, Object>> XCPDatasOneDataLast = excelDao.selectSaleTotalByXCP(beginDate, endDate, searchStr,
				conditions, WebPageUtil.isHQRole());

		HashMap<String, ArrayList<HashMap<String, Object>>> lastMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
		for (int m = 0; m < XCPDatasOneLast.size(); m++) {
			if (lastMap.get(XCPDatasOneLast.get(m).get("model").toString()) == null) {
				ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
				modelList.add(XCPDatasOneLast.get(m));
				lastMap.put(XCPDatasOneLast.get(m).get("model").toString(), modelList);
			} else {
				ArrayList<HashMap<String, Object>> modelList = lastMap
						.get(XCPDatasOneLast.get(m).get("model").toString());
				modelList.add(XCPDatasOneLast.get(m));
			}

		}

		lastQty = BigDecimal.ZERO;

		nowKey = TenNowKey;
		lastKey = TenLastKey;

		BigDecimal bdTotal = BigDecimal.ZERO;
		BigDecimal lastQtyTotal = BigDecimal.ZERO;

		bd = BigDecimal.ZERO;
		lastQty = BigDecimal.ZERO;

		String partysNameLast = "";
		String partysNameNow = "";
		for (int c = 0; c < XCPDatasOne.size(); c++) {
			HashMap<String, Object> dataFive = new HashMap<String, Object>();

			dataFive.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Country",
					XCPDatasOne.get(c).get("party_name").toString());
			bd = new BigDecimal(XCPDatasOne.get(c).get("saleQty").toString());

			dataFive.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series",
					XCPDatasOne.get(c).get("LINE"));
			dataFive.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Model",
					XCPDatasOne.get(c).get("model"));
			dataFive.put(nowKey, Math.round(bd.doubleValue()));

			for (int s = 0; s < XCPDatasOneLast.size(); s++) {
				lastQty = new BigDecimal(XCPDatasOneLast.get(s).get("saleQty").toString());
				if (XCPDatasOneLast.get(s).get("party_name").toString()
						.equals(XCPDatasOne.get(c).get("party_name").toString())
						&& XCPDatasOneLast.get(s).get("model").toString().equals(XCPDatasOne.get(c).get("model"))) {
					dataFive.put(lastKey, Math.round(lastQty.doubleValue()));

				}
			}

			// 同比增长率=（本期数－同期数）÷同期数×100%
			if (Math.round(bd.doubleValue()) == 0) {
				dataFive.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "-100%");
			} else if (Math.round(lastQty.doubleValue()) == 0) {
				dataFive.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "100%");
			} else {
				double poor = (bd.doubleValue() - lastQty.doubleValue()) / lastQty.doubleValue() * 100;
				dataFive.put("YTD-" + toYear[0] + " Monthly sellout trend per size_Growth", Math.round(poor) + "%");
			}

			dataListTen.add(dataFive);

			partysNameNow = XCPDatasOne.get(c).get("party_name").toString();
			if (XCPDatasOne.size() - 1 != c) {
				partysNameLast = XCPDatasOne.get(c + 1).get("party_name").toString();

				if (!partysNameNow.equals(partysNameLast)) {
					HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();
					if (XCPDatasOneData.size() > 0) {
						for (int k = 0; k < XCPDatasOneData.size(); k++) {
							if (XCPDatasOneData.get(k).get("party_name").toString().equals(partysNameNow)) {
								bdTotal = new BigDecimal(XCPDatasOneData.get(k).get("saleQty").toString());
							}
						}
					} else {
						bdTotal = BigDecimal.ZERO;
					}

					if (XCPDatasOneDataLast.size() > 0) {
						for (int k = 0; k < XCPDatasOneDataLast.size(); k++) {
							if (XCPDatasOneDataLast.get(k).get("party_name").toString().equals(partysNameNow)) {
								lastQtyTotal = new BigDecimal(XCPDatasOneDataLast.get(k).get("saleQty").toString());
							}
						}
					} else {
						lastQtyTotal = BigDecimal.ZERO;
					}

					if (Math.round(bdTotal.doubleValue()) == 0) {
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "-100%");
					} else if (Math.round(lastQtyTotal.doubleValue()) == 0) {
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "100%");
					} else {
						double poor = (bdTotal.doubleValue() - lastQtyTotal.doubleValue()) / lastQtyTotal.doubleValue()
								* 100;
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout trend per size_Growth",
								Math.round(poor) + "%");
					}

					dataFiveTotal.put(nowKey, Math.round(bdTotal.doubleValue()));
					dataFiveTotal.put(lastKey, Math.round(lastQtyTotal.doubleValue()));
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series", "Total");
					dataListTen.add(dataFiveTotal);
					dataFiveTotal = new HashMap<String, Object>();
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Country", "Country");
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series", "Series");
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Model", "Model");
					dataFiveTotal.put(nowKey, nowKey.split("_")[1]);
					dataFiveTotal.put(lastKey, lastKey.split("_")[1]);
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "Growth");
					dataListTen.add(dataFiveTotal);

				}
			} else {

				if (XCPDatasOneData.size() > 0) {
					for (int k = 0; k < XCPDatasOneData.size(); k++) {
						if (XCPDatasOneData.get(k).get("party_name").toString().equals(partysNameNow)) {
							bdTotal = new BigDecimal(XCPDatasOneData.get(k).get("saleQty").toString());
						}
					}
				} else {
					bdTotal = BigDecimal.ZERO;
				}

				if (XCPDatasOneDataLast.size() > 0) {
					for (int k = 0; k < XCPDatasOneDataLast.size(); k++) {
						if (XCPDatasOneDataLast.get(k).get("party_name").toString().equals(partysNameNow)) {
							lastQtyTotal = new BigDecimal(XCPDatasOneDataLast.get(k).get("saleQty").toString());
						}
					}
				} else {
					lastQtyTotal = BigDecimal.ZERO;
				}
				if (WebPageUtil.isHQRole()) {

					HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();

					if (Math.round(bdTotal.doubleValue()) == 0) {
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "-100%");
					} else if (Math.round(lastQtyTotal.doubleValue()) == 0) {
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "100%");
					} else {
						double poor = (bdTotal.doubleValue() - lastQtyTotal.doubleValue()) / lastQtyTotal.doubleValue()
								* 100;
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout trend per size_Growth",
								Math.round(poor) + "%");
					}

					dataFiveTotal.put(nowKey, Math.round(bdTotal.doubleValue()));
					dataFiveTotal.put(lastKey, Math.round(lastQtyTotal.doubleValue()));
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series", "Total");
					dataListTen.add(dataFiveTotal);

					dataFiveTotal = new HashMap<String, Object>();
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Country", "Country");
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series", "Series");
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Model", "Model");
					dataFiveTotal.put(nowKey, nowKey.split("_")[1]);
					dataFiveTotal.put(lastKey, lastKey.split("_")[1]);
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "Growth");
					dataListTen.add(dataFiveTotal);

				} else {

					HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();

					if (Math.round(bdTotal.doubleValue()) == 0) {
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "-100%");
					} else if (Math.round(lastQtyTotal.doubleValue()) == 0) {
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "100%");
					} else {
						double poor = (bdTotal.doubleValue() - lastQtyTotal.doubleValue()) / lastQtyTotal.doubleValue()
								* 100;
						dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout trend per size_Growth",
								Math.round(poor) + "%");
					}

					dataFiveTotal.put(nowKey, Math.round(bdTotal.doubleValue()));
					dataFiveTotal.put(lastKey, Math.round(lastQtyTotal.doubleValue()));
					dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series", "Total");
					dataListTen.add(dataFiveTotal);

				}

			}

		}
		if (WebPageUtil.isHQRole()) {
			LinkedList<HashMap<String, Object>> dataListTenTwo = new LinkedList<HashMap<String, Object>>();
			bdTotal = BigDecimal.ZERO;
			lastQtyTotal = BigDecimal.ZERO;
			Collection keysNow = nowMap.keySet();
			for (Iterator iterator = keysNow.iterator(); iterator.hasNext();) {
				bd = BigDecimal.ZERO;
				lastQty = BigDecimal.ZERO;

				HashMap<String, Object> dataTwo = new HashMap<String, Object>();
				Object keyNow = iterator.next();
				ArrayList<HashMap<String, Object>> monthList = nowMap.get(keyNow);
				dataTwo.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Country", "BDSC");
				dataTwo.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Model", keyNow);

				for (int i = 0; i < monthList.size(); i++) {
					bd = bd.add(new BigDecimal(monthList.get(i).get("saleQty").toString()));

					dataTwo.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series",
							monthList.get(i).get("LINE"));
				}
				bdTotal = bdTotal.add(bd);
				dataTwo.put(nowKey, Math.round(bd.doubleValue()));
				if (lastMap.get(keyNow) != null) {
					monthList = nowMap.get(keyNow);
					for (int i = 0; i < monthList.size(); i++) {
						lastQty = lastQty.add(new BigDecimal(monthList.get(i).get("saleQty").toString()));

					}
					lastQtyTotal = lastQtyTotal.add(lastQty);
					dataTwo.put(lastKey, Math.round(lastQty.doubleValue()));
				}

				// 同比增长率=（本期数－同期数）÷同期数×100%
				if (Math.round(bd.doubleValue()) == 0) {
					dataTwo.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "-100%");
				} else if (Math.round(lastQty.doubleValue()) == 0) {
					dataTwo.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "100%");
				} else {
					double poor = (bd.doubleValue() - lastQty.doubleValue()) / lastQty.doubleValue() * 100;
					dataTwo.put("YTD-" + toYear[0] + " Monthly sellout trend per size_Growth", Math.round(poor) + "%");
				}

				dataListTenTwo.add(dataTwo);
			}
			BDDateUtil.Order(dataListTenTwo, "YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series");
			for (int i = 0; i < dataListTenTwo.size(); i++) {
				dataListTen.add(dataListTenTwo.get(i));
			}
			HashMap<String, Object> dataFiveTotal = new HashMap<String, Object>();

			if (Math.round(bdTotal.doubleValue()) == 0) {
				dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "-100%");
			} else if (Math.round(lastQtyTotal.doubleValue()) == 0) {
				dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Growth", "100%");
			} else {
				double poor = (bdTotal.doubleValue() - lastQtyTotal.doubleValue()) / lastQtyTotal.doubleValue() * 100;
				dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout trend per size_Growth",
						Math.round(poor) + "%");
			}

			dataFiveTotal.put(nowKey, Math.round(bdTotal.doubleValue()));
			dataFiveTotal.put(lastKey, Math.round(lastQtyTotal.doubleValue()));
			dataFiveTotal.put("YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series", "Total");
			dataListTen.add(dataFiveTotal);

		}

		long startTime = System.currentTimeMillis();

		System.out.println("=====第9个=" + (System.currentTimeMillis() - startTime));
		// 创建工作表（SHEET） 此处sheet名字应根据数据的时间
		Sheet sheet = wb.createSheet(Integer.parseInt(toYear[0]) - 1 + "  vs. " + toYear[0] + " comparative");
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

		cellStyleDate.setDataFormat(dataFormat.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
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
		cellStyleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
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
		cellStylePERCENT.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
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
		cell.setCellValue("TCL BDSC");// 标题
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
				int num = i;
				sheet.setColumnWidth(num, s.toString().length() * 156);
				//
				if (s.length == 1) { // 如果是简单表头项
					cell = row.createCell((short) (num));
					// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
					sheet.addMergedRegion(new CellRangeAddress(2, rows_max + 1, (num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(k + 2, k + 2, (num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(k + 2, k + 2 + rows_max - s.length, num, num));
					}

				}
				if (s.length > k) {
					if (!map.containsKey(sk)) {
						key = "";
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

		for (int i = 0; i < headerFive.length; i++) {
			String h = headerFive[i];

			if (h.split("_").length > rows_max) {
				rows_max = h.split("_").length;
			}
		}
		Map mapFive = new HashMap();
		for (int k = 0; k < rows_max; k++) {
			row = sheet.createRow((short) k + 13);
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
					sheet.addMergedRegion(new CellRangeAddress(13, rows_max + 12, (num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(k + 13, k + 13, (num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(k + 13, k + 13 + rows_max - s.length, num, num));
					}

				}
				if (s.length > k) {
					if (!mapFive.containsKey(sk)) {
						key = "";
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
			row = sheet.createRow((short) k + 13 + dataListFive.size() + 10);
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
					sheet.addMergedRegion(new CellRangeAddress(13 + dataListFive.size() + 10,
							rows_max + 12 + dataListFive.size() + 10, (num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(k + 13 + dataListFive.size() + 10,
							k + 13 + dataListFive.size() + 10, (num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(k + 13 + +dataListFive.size() + 10,
								k + 13 + dataListFive.size() + 10 + rows_max - s.length, num, num));
					}

				}
				if (s.length > k) {
					if (!mapSix.containsKey(sk)) {
						key = "";
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
			row = sheet.createRow((short) k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10);
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
					sheet.addMergedRegion(new CellRangeAddress(13 + dataListFive.size() + 10 + dataListSix.size() + 10,

							rows_max + 12 + dataListFive.size() + 10 + dataListSix.size() + 10, (num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(
							k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10,
							k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10, (num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(
								k + 13 + +dataListFive.size() + 10 + dataListSix.size() + 10,
								k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + rows_max - s.length, num,
								num));
					}

				}
				if (s.length > k) {
					if (!mapEight.containsKey(sk)) {
						key = "";
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
			row = sheet.createRow(
					(short) k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10);
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
					sheet.addMergedRegion(new CellRangeAddress(
							13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10,
							rows_max + 12 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size()
									+ 10,
							(num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(
							k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10,
							k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10,
							(num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(
								k + 13 + 10 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size()
										+ 10,
								k + 13 + 10 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size()
										+ 10 + rows_max - s.length,
								num, num));
					}

				}
				if (s.length > k) {
					if (!mapNight.containsKey(sk)) {
						key = "";
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
			row = sheet.createRow((short) k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10
					+ dataListEight.size() + 10 + dataListNight.size() + 10);
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
					sheet.addMergedRegion(new CellRangeAddress(
							13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10
									+ dataListNight.size() + 10,
							rows_max + 12 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size()
									+ 10 + dataListNight.size() + 10,
							(num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(
							k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10
									+ dataListNight.size() + 10,
							k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10
									+ dataListNight.size() + 10,
							(num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(
								k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10
										+ dataListNight.size() + 10,
								k + 13 + dataListFive.size() + 10 + dataListSix.size() + 10 + dataListEight.size() + 10
										+ dataListNight.size() + 10 + rows_max - s.length,
								num, num));
					}

				}
				if (s.length > k) {
					if (!mapTen.containsKey(sk)) {
						key = "";
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

		System.out.println("=====create header  cost time=" + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();
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
				if (dataMap.get(fields[c]) != null && dataMap.get(fields[c]).toString().length() > 0

				) {

					// 判断data是否为数值型
					isNum = dataMap.get(fields[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fields[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fields[c]).toString().contains("%");
					isGongshi = dataMap.get(fields[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fields[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					// 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fields[c]).toString()));

					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fields[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						// contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fields[c]).toString());

					} else {

						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为字符型
						contentCell.setCellValue(dataMap.get(fields[c]).toString());
					}
					if (d == dataList.size() - 1) {
						if(dataMap.get(fields[c]).toString().contains("-")) {
							contentCell.setCellStyle(cellStyleRED);

						}else {
							contentCell.setCellStyle(cellStyleGreen);
						}
						

					}
				} else {
					contentCell.setCellValue(0);
				}
			

				if (d == 2) {
					contentCell.setCellStyle(cellStylePERCENT);
				}

			}

		}

		System.out.println("=====create dataListFirst  cost time=" + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();

		System.out.println("=====create dataList4 cost time=" + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();
		int starts=12 + rows_max + 1;
		int end=12 + rows_max + 1;
		for (int d = 0; d < dataListFive.size(); d++) {

			HashMap<String, Object> dataMap = dataListFive.get(d);
			// 创建一行
			Row datarow = sheet.createRow(d + 12 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

			Cell scell = datarow.createCell((short) 0);

			scell.setCellType(Cell.CELL_TYPE_NUMERIC);
			
			for (int c = 0; c < fieldsFive.length; c++) {

				Cell contentCell = datarow.createCell(c);
				Boolean isNum = false;// data是否为数值型
				Boolean isInteger = false;// data是否为整数
				Boolean isPercent = false;// data是否为百分数
				Boolean isGongshi = false;// data是否为百分数
				boolean isGongshiOne = false;
				if (dataMap.get(fieldsFive[c]) != null && dataMap.get(fieldsFive[c]).toString().length() > 0

				) {

					// 判断data是否为数值型
					isNum = dataMap.get(fieldsFive[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fieldsFive[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fieldsFive[c]).toString().contains("%");
					isGongshi = dataMap.get(fieldsFive[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fieldsFive[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					DataFormat df = wb.createDataFormat(); // 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsFive[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsFive[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsFive[c]).toString());
					} else if (isPercent && c == fieldsFive.length - 1) {
						if (dataMap.get(fieldsFive[c]).toString().contains("-")) {
							contentCell.setCellStyle(cellStyleRED);
						} else {
							contentCell.setCellStyle(cellStyleGreen);
						}
						contentCell.setCellValue(dataMap.get(fieldsFive[c]).toString());
					} else {
						if (dataMap.get(fieldsFive[c]).toString().contains("Growth")) {
							contentCell.setCellStyle(cellStylehead);
							datarow.getCell(c - 1).setCellStyle(cellStylehead);
							datarow.getCell(c - 2).setCellStyle(cellStylehead);
							datarow.getCell(c - 3).setCellStyle(cellStylehead);
							datarow.getCell(c - 4).setCellStyle(cellStylehead);
							datarow.getCell(c - 1).setCellValue(dataMap.get(fieldsFive[c - 1]).toString());
							datarow.getCell(c - 2).setCellValue(dataMap.get(fieldsFive[c - 2]).toString());
							datarow.getCell(c - 3).setCellValue(dataMap.get(fieldsFive[c - 3]).toString());
							datarow.getCell(c - 4).setCellValue(dataMap.get(fieldsFive[c - 4]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
						}
						contentCell.setCellValue(dataMap.get(fieldsFive[c]).toString());
					}
		
				} else {
					contentCell.setCellValue("");
				}

			}
			
			if (d != dataListFive.size() - 1) {
				String keyMap="YTD-" + toYear[0] + "  Monthly sellout trend per size_Country";
					HashMap<String, Object> dataMapTwo = dataListFive.get(d + 1);
					if(dataMap.get(keyMap)!=null &&dataMapTwo.get(keyMap)!=null ) {
						if (dataMap.get(keyMap).equals(dataMapTwo.get(keyMap))) {
						}else {
							sheet.addMergedRegion(
									new CellRangeAddress(starts, d +end-1, 0, 0));
							starts=d +end+1;
						}
					}
				
				}else {
					sheet.addMergedRegion(
							new CellRangeAddress(starts, d +end, 0, 0));
				}

		}

		System.out.println("=====create dataList5  cost time=" + (System.currentTimeMillis() - startTime));
		starts=12 + rows_max + 1 + dataListFive.size() + 10;
		end=12 + rows_max + 1 + dataListFive.size() + 10;
		
		startTime = System.currentTimeMillis();
		for (int d = 0; d < dataListSix.size(); d++) {

			HashMap<String, Object> dataMap = dataListSix.get(d);
			// 创建一行
			Row datarow = sheet.createRow(d + 12 + rows_max + 1 + dataListFive.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

			Cell scell = datarow.createCell((short) 0);

			scell.setCellType(Cell.CELL_TYPE_NUMERIC);

			for (int c = 0; c < fieldsSix.length; c++) {

				Cell contentCell = datarow.createCell(c);
				Boolean isNum = false;// data是否为数值型
				Boolean isInteger = false;// data是否为整数
				Boolean isPercent = false;// data是否为百分数
				Boolean isGongshi = false;// data是否为百分数
				boolean isGongshiOne = false;
				if (dataMap.get(fieldsSix[c]) != null && dataMap.get(fieldsSix[c]).toString().length() > 0

				) {

					// 判断data是否为数值型
					isNum = dataMap.get(fieldsSix[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fieldsSix[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fieldsSix[c]).toString().contains("%");
					isGongshi = dataMap.get(fieldsSix[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fieldsSix[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					DataFormat df = wb.createDataFormat(); // 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsSix[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsSix[c]).toString());
					} else if (isPercent && c == fieldsSix.length - 1) {
						if (dataMap.get(fieldsSix[c]).toString().contains("-")) {
							contentCell.setCellStyle(cellStyleRED);
						} else {
							contentCell.setCellStyle(cellStyleGreen);
						}
						contentCell.setCellValue(dataMap.get(fieldsSix[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsSix[c]).toString());
					} else {
						if (dataMap.get(fieldsSix[c]).toString().contains("Growth")) {
							contentCell.setCellStyle(cellStylehead);
							datarow.getCell(c - 1).setCellStyle(cellStylehead);
							datarow.getCell(c - 2).setCellStyle(cellStylehead);
							datarow.getCell(c - 3).setCellStyle(cellStylehead);
							datarow.getCell(c - 4).setCellStyle(cellStylehead);
							datarow.getCell(c - 1).setCellValue(dataMap.get(fieldsSix[c - 1]).toString());
							datarow.getCell(c - 2).setCellValue(dataMap.get(fieldsSix[c - 2]).toString());
							datarow.getCell(c - 3).setCellValue(dataMap.get(fieldsSix[c - 3]).toString());
							datarow.getCell(c - 4).setCellValue(dataMap.get(fieldsSix[c - 4]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
						}
						contentCell.setCellValue(dataMap.get(fieldsSix[c]).toString());
					}

				
				} else {
					contentCell.setCellValue("");
				}
			
			}
			if (d != dataListSix.size() - 1) {
				String keyMap="Market Share_Country";
					HashMap<String, Object> dataMapTwo = dataListSix.get(d + 1);
					if(dataMap.get(keyMap)!=null &&dataMapTwo.get(keyMap)!=null ) {
						if (dataMap.get(keyMap).equals(dataMapTwo.get(keyMap))) {
						}else {
							sheet.addMergedRegion(
									new CellRangeAddress(starts, d +end-1, 0, 0));
							starts=d +end+1;
						}
					}
				
				}else {
					sheet.addMergedRegion(
							new CellRangeAddress(starts, d +end, 0, 0));
				}
		}

		System.out.println("=====create dataList7  cost time=" + (System.currentTimeMillis() - startTime));
		starts= 12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10;
		end= 12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10;
		startTime = System.currentTimeMillis();
		for (int d = 0; d < dataListEight.size(); d++) {

			HashMap<String, Object> dataMap = dataListEight.get(d);
			// 创建一行
			Row datarow = sheet.createRow(d + 12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

			Cell scell = datarow.createCell((short) 0);

			scell.setCellType(Cell.CELL_TYPE_NUMERIC);

			for (int c = 0; c < fieldsEight.length; c++) {

				Cell contentCell = datarow.createCell(c);
				Boolean isNum = false;// data是否为数值型
				Boolean isInteger = false;// data是否为整数
				Boolean isPercent = false;// data是否为百分数
				Boolean isGongshi = false;// data是否为百分数
				boolean isGongshiOne = false;
				if (dataMap.get(fieldsEight[c]) != null && dataMap.get(fieldsEight[c]).toString().length() > 0

				) {

					// 判断data是否为数值型
					isNum = dataMap.get(fieldsEight[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fieldsEight[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fieldsEight[c]).toString().contains("%");
					isGongshi = dataMap.get(fieldsEight[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fieldsEight[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					DataFormat df = wb.createDataFormat(); // 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsEight[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsEight[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsEight[c]).toString());
					} else if (isPercent && c == fieldsEight.length - 1) {
						if (dataMap.get(fieldsEight[c]).toString().contains("-")) {
							contentCell.setCellStyle(cellStyleRED);
						} else {
							contentCell.setCellStyle(cellStyleGreen);
						}
						contentCell.setCellValue(dataMap.get(fieldsEight[c]).toString());
					} else {
						if (dataMap.get(fieldsEight[c]).toString().contains("Growth")) {

							contentCell.setCellStyle(cellStylehead);
							datarow.getCell(c - 1).setCellStyle(cellStylehead);
							datarow.getCell(c - 2).setCellStyle(cellStylehead);
							datarow.getCell(c - 3).setCellStyle(cellStylehead);
							datarow.getCell(c - 4).setCellStyle(cellStylehead);

							datarow.getCell(c - 1).setCellValue(dataMap.get(fieldsEight[c - 1]).toString());
							datarow.getCell(c - 2).setCellValue(dataMap.get(fieldsEight[c - 2]).toString());
							datarow.getCell(c - 3).setCellValue(dataMap.get(fieldsEight[c - 3]).toString());
							datarow.getCell(c - 4).setCellValue(dataMap.get(fieldsEight[c - 4]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
						}
						contentCell.setCellValue(dataMap.get(fieldsEight[c]).toString());
					}

	
				} else {
					contentCell.setCellValue("");
				}

			}
			if (d != dataListEight.size() - 1) {
				String keyMap="Different catgory sell-out quantity_Country";
					HashMap<String, Object> dataMapTwo = dataListEight.get(d + 1);
					if(dataMap.get(keyMap)!=null &&dataMapTwo.get(keyMap)!=null ) {
						if (dataMap.get(keyMap).equals(dataMapTwo.get(keyMap))) {
						}else {
							sheet.addMergedRegion(
									new CellRangeAddress(starts, d +end-1, 0, 0));
							starts=d +end+1;
						}
					}
				
				}else {
					sheet.addMergedRegion(
							new CellRangeAddress(starts, d +end, 0, 0));
				}

		}
		System.out.println("=====create dataList8  cost time=" + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();
		starts=12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
				+ dataListEight.size() + 10;
		end=12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
				+ dataListEight.size() + 10;
		for (int d = 0; d < dataListNight.size(); d++) {

			HashMap<String, Object> dataMap = dataListNight.get(d);
			// 创建一行
			Row datarow = sheet.createRow(d + 12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
					+ dataListEight.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

			Cell scell = datarow.createCell((short) 0);

			scell.setCellType(Cell.CELL_TYPE_NUMERIC);

			for (int c = 0; c < fieldsNight.length; c++) {

				Cell contentCell = datarow.createCell(c);
				Boolean isNum = false;// data是否为数值型
				Boolean isInteger = false;// data是否为整数
				Boolean isPercent = false;// data是否为百分数
				Boolean isGongshi = false;// data是否为百分数
				boolean isGongshiOne = false;
				if (dataMap.get(fieldsNight[c]) != null && dataMap.get(fieldsNight[c]).toString().length() > 0

				) {
					// 判断data是否为数值型
					isNum = dataMap.get(fieldsNight[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fieldsNight[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fieldsNight[c]).toString().contains("%");
					isGongshi = dataMap.get(fieldsNight[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fieldsNight[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					DataFormat df = wb.createDataFormat(); // 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsNight[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsNight[c]).toString());
					} else if (isPercent && c == fieldsNight.length - 1) {
						if (dataMap.get(fieldsNight[c]).toString().contains("-")) {
							contentCell.setCellStyle(cellStyleRED);
						} else {
							contentCell.setCellStyle(cellStyleGreen);
						}
						contentCell.setCellValue(dataMap.get(fieldsNight[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsNight[c]).toString());
					} else {
						if (dataMap.get(fieldsNight[c]).toString().contains("Growth")) {
							contentCell.setCellStyle(cellStylehead);
							datarow.getCell(c - 1).setCellStyle(cellStylehead);
							datarow.getCell(c - 2).setCellStyle(cellStylehead);
							datarow.getCell(c - 3).setCellStyle(cellStylehead);
							datarow.getCell(c - 4).setCellStyle(cellStylehead);

							datarow.getCell(c - 1).setCellValue(dataMap.get(fieldsNight[c - 1]).toString());
							datarow.getCell(c - 2).setCellValue(dataMap.get(fieldsNight[c - 2]).toString());
							datarow.getCell(c - 3).setCellValue(dataMap.get(fieldsNight[c - 3]).toString());
							datarow.getCell(c - 4).setCellValue(dataMap.get(fieldsNight[c - 4]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
						}
						contentCell.setCellValue(dataMap.get(fieldsNight[c]).toString());
					}

				} else {
					contentCell.setCellValue("");
				}

			}
			if (d != dataListNight.size() - 1) {
				String keyMap="Growth rate_Country";
					HashMap<String, Object> dataMapTwo = dataListNight.get(d + 1);
					if(dataMap.get(keyMap)!=null &&dataMapTwo.get(keyMap)!=null ) {
						if (dataMap.get(keyMap).equals(dataMapTwo.get(keyMap))) {
						}else {
							sheet.addMergedRegion(
									new CellRangeAddress(starts, d +end-1, 0, 0));
							starts=d +end+1;
						}
					}
				
				}else {
					sheet.addMergedRegion(
							new CellRangeAddress(starts, d +end, 0, 0));
				}
		}
		System.out.println("=====create dataList8  cost time=" + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();
		starts=12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
				+ dataListEight.size() + 10 + dataListNight.size() + 10;
		end=12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
				+ dataListEight.size() + 10 + dataListNight.size() + 10;
		
		int startsTwo=12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
				+ dataListEight.size() + 10 + dataListNight.size() + 10;
		int endTwo=12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
				+ dataListEight.size() + 10 + dataListNight.size() + 10;
		
		for (int d = 0; d < dataListTen.size(); d++) {

			HashMap<String, Object> dataMap = dataListTen.get(d);
			// 创建一行
			Row datarow = sheet.createRow(d + 12 + rows_max + 1 + dataListFive.size() + 10 + dataListSix.size() + 10
					+ dataListEight.size() + 10 + dataListNight.size() + 10);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，

			Cell scell = datarow.createCell((short) 0);

			scell.setCellType(Cell.CELL_TYPE_NUMERIC);

			for (int c = 0; c < fieldsTen.length; c++) {

				Cell contentCell = datarow.createCell(c);
				Boolean isNum = false;// data是否为数值型
				Boolean isInteger = false;// data是否为整数
				Boolean isPercent = false;// data是否为百分数
				Boolean isGongshi = false;// data是否为百分数
				boolean isGongshiOne = false;
				if (dataMap.get(fieldsTen[c]) != null && dataMap.get(fieldsTen[c]).toString().length() > 0

				) {
					// 判断data是否为数值型
					isNum = dataMap.get(fieldsTen[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fieldsTen[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fieldsTen[c]).toString().contains("%");
					isGongshi = dataMap.get(fieldsTen[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fieldsTen[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					DataFormat df = wb.createDataFormat(); // 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsTen[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsTen[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);

						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsTen[c]).toString());
					} else if (isPercent && c == fieldsTen.length - 1) {
						if (dataMap.get(fieldsTen[c]).toString().contains("-")) {
							contentCell.setCellStyle(cellStyleRED);
						} else {
							contentCell.setCellStyle(cellStyleGreen);
						}
						contentCell.setCellValue(dataMap.get(fieldsTen[c]).toString());
					} else {
						if (dataMap.get(fieldsTen[c]).toString().contains("Growth")) {
							contentCell.setCellStyle(cellStylehead);
							datarow.getCell(c - 1).setCellStyle(cellStylehead);
							datarow.getCell(c - 2).setCellStyle(cellStylehead);
							datarow.getCell(c - 3).setCellStyle(cellStylehead);
							datarow.getCell(c - 4).setCellStyle(cellStylehead);
							datarow.getCell(c - 5).setCellStyle(cellStylehead);

							datarow.getCell(c - 1).setCellValue(dataMap.get(fieldsTen[c - 1]).toString());
							datarow.getCell(c - 2).setCellValue(dataMap.get(fieldsTen[c - 2]).toString());
							datarow.getCell(c - 3).setCellValue(dataMap.get(fieldsTen[c - 3]).toString());
							datarow.getCell(c - 4).setCellValue(dataMap.get(fieldsTen[c - 4]).toString());
							datarow.getCell(c - 5).setCellValue(dataMap.get(fieldsTen[c - 5]).toString());
						} else {
							contentCell.setCellStyle(contextstyle);
						}
						contentCell.setCellValue(dataMap.get(fieldsTen[c]).toString());
					}


				} else {
					contentCell.setCellValue("");
				}

			}
			
			if (d != dataListTen.size() - 1) {
				String keyMap="YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Country";
					HashMap<String, Object> dataMapTwo = dataListTen.get(d + 1);
					if(dataMap.get(keyMap)!=null &&dataMapTwo.get(keyMap)!=null ) {
						if (dataMap.get(keyMap).equals(dataMapTwo.get(keyMap))) {
						}else {
							sheet.addMergedRegion(
									new CellRangeAddress(starts, d +end-1, 0, 0));
							starts=d +end+1;
						}
					}
				
				}else {
					sheet.addMergedRegion(
							new CellRangeAddress(starts, d +end, 0, 0));
				}
			
			
			if (d != dataListTen.size() - 1) {
				String keyMap="YTD-" + toYear[0] + " Monthly sellout X/C/P trend per size_Series";
					HashMap<String, Object> dataMapTwo = dataListTen.get(d + 1);
					if(dataMap.get(keyMap)!=null &&dataMapTwo.get(keyMap)!=null ) {
						if (dataMap.get(keyMap).equals(dataMapTwo.get(keyMap))) {
						}else {
							sheet.addMergedRegion(
									new CellRangeAddress(startsTwo, d +endTwo, 1, 1));
							startsTwo=d +endTwo+1;
						}
					}
				
				}else {
					sheet.addMergedRegion(
							new CellRangeAddress(startsTwo, d +endTwo, 1, 1));
				}

		}

		System.out.println("=====create dataList9 cost time=" + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();

	}

	@Override
	public void monthYTDselloutQua(String what, SXSSFWorkbook wb, String beginDateOne, String endDateOne,
			String searchStr, String conditions) throws Exception {

		String[] toYear = beginDateOne.split("-");
		String[] toYearEnd = endDateOne.split("-");
		int laYear = Integer.parseInt(toYear[0].toString()) - 1;
		String laDays = BDDateUtil.getLastDayOfMonth(laYear, Integer.parseInt(toYear[1]));
		String[] laDay = laDays.split("-");
		int lastYear = Integer.parseInt(toYear[0]) - 1;
		String YTDBegin = toYear[0] + "-01-01";
		int maxMonth = Integer.parseInt(toYear[1]);

		YTDBegin = toYear[0] + "-01-01";
		maxMonth = Integer.parseInt(toYearEnd[1]);

		// 表头数据
		String[] headers = { "Country", "YTD- " + toYear[0] + "_Category", "YTD- " + toYear[0] + "_SUBTOTAL"

		};
		String Month = "";

		for (int i = 1; i <= maxMonth; i++) {
			headers = insert(headers, "YTD- " + toYear[0] + "_" + getMonth(i + ""));
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

		// 表头数据
		String[] headersTwo = { "SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_MODELS",
				"SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_SUBTOTAL"

		};

		for (int i = 1; i <= maxMonth; i++) {
			headersTwo = insert(headersTwo, "SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_" + getMonth(i + ""));

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

		long start = System.currentTimeMillis();
		Map<String, BigDecimal> mapResultTwo = new HashMap<String, BigDecimal>();

		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();

		List<HashMap<String, Object>> specDatasOneData = excelDao.selectSaleDataBySpecYTD(YTDBegin, endDateOne,
				searchStr, conditions, WebPageUtil.isHQRole());

		List<HashMap<String, Object>> specTotalOneData = excelDao.selectSaleTotalBySpecYTD(YTDBegin, endDateOne,
				searchStr, conditions, WebPageUtil.isHQRole());

		int beginNow = 6;// 每次+8
		int endNow = 12;
		for (int i = 0; i < countryList.length; i++) {
			BigDecimal bdTotal = BigDecimal.ZERO;
			for (int j = 0; j < specList.length; j++) {
				BigDecimal bd = BigDecimal.ZERO;
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("Country", countryList[i]);
				data.put("YTD- " + toYear[0] + "_Category", specList[j]);
				data.put("YTD- " + toYear[0] + "_SUBTOTAL", "SUM(D" + (6 + dataList.size()) + ":"
						+ BDDateUtil.getExcelColumnLabel(maxMonth + 3) + (6 + dataList.size()) + ")");
				for (int k = 0; k < specDatasOneData.size(); k++) {
					bd = new BigDecimal(specDatasOneData.get(k).get("saleQty").toString());
					if (specDatasOneData.get(k).get("party_name").equals(countryList[i])
							&& specDatasOneData.get(k).get("SPEC").equals(specList[j])) {
						String mon = specDatasOneData.get(k).get("date").toString();
						String key = "YTD- " + toYear[0] + "_" + getMonth(mon);
						data.put(key, Math.round(bd.doubleValue()));

						BigDecimal numOne = mapResultTwo.get(specList[j] + "_" + key);
						BigDecimal numOneSize = mapResultTwo.get(key);
						if (numOne == null) {
							numOne = BigDecimal.ZERO;
						}
						if (numOneSize == null) {
							numOneSize = BigDecimal.ZERO;
						}
						mapResultTwo.put(specList[j] + "_" + key, numOne.add(bd));
						mapResultTwo.put(key, numOneSize.add(bd));

					}
				}
				dataList.add(data);
			}

			HashMap<String, Object> dataTotal = new HashMap<String, Object>();
			String key = "YTD- " + toYear[0] + "_Category";
			dataTotal.put(key, "TOTAL");
			for (int j = 0; j < specTotalOneData.size(); j++) {
				bdTotal = new BigDecimal(specTotalOneData.get(j).get("saleQty").toString());
				if (specTotalOneData.get(j).get("party_name").equals(countryList[i])) {
					String mon = specTotalOneData.get(j).get("date").toString();
					key = "YTD- " + toYear[0] + "_" + getMonth(mon);
					dataTotal.put(key, Math.round(bdTotal.doubleValue()));
				}
			}
			for (int j = 1; j <= maxMonth; j++) {
				key = "YTD- " + toYear[0] + "_" + getMonth(j + "");
				if (!dataTotal.containsKey(key)) {
					dataTotal.put(key, 0);
				}
			}
			dataTotal.put("YTD- " + toYear[0] + "_SUBTOTAL", "SUM(C" + beginNow + ":C" + endNow + ")");
			dataList.add(dataTotal);
			dataTotal = new HashMap<String, Object>();
			dataTotal.put("YTD- " + toYear[0] + "_Category", "Growth vs. Monthly");
			for (int j = 1; j <= maxMonth; j++) {
				// 单元格行
				int r = endNow + 1;
				// 本期列
				String bl = BDDateUtil.getExcelColumnLabel(j + 3);
				// 同期列
				String el = BDDateUtil.getExcelColumnLabel(j + 2);
				if (j > 1) {
					key = "YTD- " + toYear[0] + "_" + getMonth(j + "");
					dataTotal.put(key, "TEXT((" + bl + "" + r + "-" + el + r + ")/" + el + r + ",\"0.00%\")");

				}

				// 本期数－同期数）÷同期数×100%

			}
			dataList.add(dataTotal);
			beginNow += 9;
			endNow += 9;

		}

		if (WebPageUtil.isHQRole()) {
			for (int j = 0; j < specList.length; j++) {
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("Country", "BDSC");
				data.put("YTD- " + toYear[0] + "_Category", specList[j]);
				data.put("YTD- " + toYear[0] + "_SUBTOTAL", "SUM(D" + (6 + dataList.size()) + ":"
						+ BDDateUtil.getExcelColumnLabel(maxMonth + 3) + (6 + dataList.size()) + ")");
				for (int k = 1; k <= maxMonth; k++) {
					String key = "YTD- " + toYear[0] + "_" + getMonth(k + "");
					BigDecimal bd = BigDecimal.ZERO;
					if (mapResultTwo.get(specList[j] + "_" + key) != null) {
						bd = mapResultTwo.get(specList[j] + "_" + key);
					}

					data.put(key, Math.round(bd.doubleValue()));
				}

				dataList.add(data);
			}
			HashMap<String, Object> dataTotal = new HashMap<String, Object>();
			dataTotal.put("YTD- " + toYear[0] + "_Category", "TOTAL");
			dataTotal.put("YTD- " + toYear[0] + "_SUBTOTAL", "SUM(C" + beginNow + ":C" + endNow + ")");
			HashMap<String, Object> dataTotalTwo = new HashMap<String, Object>();

			dataTotalTwo.put("YTD- " + toYear[0] + "_Category", "Growth vs. Monthly");
			for (int k = 1; k <= maxMonth; k++) {
				String key = "YTD- " + toYear[0] + "_" + getMonth(k + "");
				BigDecimal bd = BigDecimal.ZERO;
				if (mapResultTwo.get(key) != null) {
					bd = mapResultTwo.get(key);
				}
				dataTotal.put(key, Math.round(bd.doubleValue()));
				// 单元格行
				int r = endNow + 1;
				if (k > 1) {
					// 本期列
					String bl = BDDateUtil.getExcelColumnLabel(k + 3);
					// 同期列
					String el = BDDateUtil.getExcelColumnLabel(k + 2);
					key = "YTD- " + toYear[0] + "_" + getMonth(k + "");
					dataTotalTwo.put(key, "TEXT((" + bl + "" + r + "-" + el + r + ")/" + el + r + ",\"0.00%\")");
					// 本期数－同期数）÷同期数×100%
				}

			}
			dataList.add(dataTotal);

			dataList.add(dataTotalTwo);
		}

		// headers = insert(headers, "YTD- "+toYear[0]+"_"+getMonth(i+""));
		System.out.println("+++++++++++++++YTD 第一个++++++++++++++++++" + (System.currentTimeMillis() - start));

		LinkedList<HashMap<String, Object>> dataListTwo = new LinkedList<HashMap<String, Object>>();

		LinkedList<LinkedHashMap<String, Object>> modelTotalData = excelDao.selectSaleDataByModelYTD(YTDBegin,
				endDateOne, searchStr, conditions, WebPageUtil.isHQRole());

		LinkedHashMap<String, LinkedList<LinkedHashMap<String, Object>>> modelMap = new LinkedHashMap<String, LinkedList<LinkedHashMap<String, Object>>>();
		for (int m = 0; m < modelTotalData.size(); m++) {
			if (modelMap.get(modelTotalData.get(m).get("model").toString()) == null) {
				LinkedList<LinkedHashMap<String, Object>> modelList = new LinkedList<LinkedHashMap<String, Object>>();
				modelList.add(modelTotalData.get(m));
				modelMap.put(modelTotalData.get(m).get("model").toString(), modelList);
			} else {
				LinkedList<LinkedHashMap<String, Object>> modelList = modelMap
						.get(modelTotalData.get(m).get("model").toString());
				modelList.add(modelTotalData.get(m));
			}

		}

		BigDecimal bdTotal = BigDecimal.ZERO;
		Map<String, BigDecimal> mapResultOne = new HashMap<String, BigDecimal>();
		Collection keys = modelMap.keySet();
		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			BigDecimal modelTotal = BigDecimal.ZERO;
			HashMap<String, Object> dataTwo = new HashMap<String, Object>();
			Object key = iterator.next();
			LinkedList<LinkedHashMap<String, Object>> monthList = modelMap.get(key);
			String keyCell = "SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_MODELS";
			dataTwo.put(keyCell, key);
			for (int j = 0; j < monthList.size(); j++) {
				BigDecimal bd = new BigDecimal(monthList.get(j).get("saleQty").toString());
				keyCell = "SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_"
						+ getMonth(monthList.get(j).get("date").toString());
				dataTwo.put(keyCell, Math.round(bd.doubleValue()));
				bdTotal = bdTotal.add(bd);
				modelTotal = modelTotal.add(bd);
				BigDecimal numOne = mapResultOne.get(getMonth(monthList.get(j).get("date").toString()));
				if (numOne == null) {
					numOne = BigDecimal.ZERO;
				}
				mapResultOne.put(getMonth(monthList.get(j).get("date").toString()), numOne.add(bd));

			}

			dataTwo.put("SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_SUBTOTAL", Math.round(modelTotal.doubleValue()));
			dataListTwo.add(dataTwo);
		}

	
		HashMap<String, Object> dataTwoTotal = new HashMap<String, Object>();
		dataTwoTotal.put("SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_MODELS", "TOTAL");
		for (int i = 1; i <= maxMonth; i++) {
			BigDecimal bd = BigDecimal.ZERO;
			if (mapResultOne.get(getMonth(i + "")) != null) {
				bd = mapResultOne.get(getMonth(i + ""));
			}
			String key = "SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_" + getMonth(i + "");
			dataTwoTotal.put(key, Math.round(bd.doubleValue()));

		}
		dataTwoTotal.put("SELL-OUT TREND PER MODEL YR-" + toYear[0] + "_SUBTOTAL", Math.round(bdTotal.doubleValue()));

		dataListTwo.add(dataTwoTotal);
		start = System.currentTimeMillis();

		System.out.println("+++++++++++++++YTD 第二个++++++++++++++++++" + (System.currentTimeMillis() - start));
		
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
		// sheet.createFreezePane(3, 88, 3, 89);
		CellStyle cellStylename = wb.createCellStyle();// 表名样式
		cellStylename.setFont(fonthead);

		CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
		cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
		cellStyleinfo.setFont(fontinfo);

		CellStyle cellStyleDate = wb.createCellStyle();

		DataFormat dataFormat = wb.createDataFormat();

		cellStyleDate.setDataFormat(dataFormat.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
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
		cellStyleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
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
		cellStylePERCENT.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
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
		cell.setCellValue("TCL BDSC");// 标题

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
		// cell.setCellValue("YTD- " + toYear[0]);// 标题

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
					sheet.addMergedRegion(new CellRangeAddress(3, rows_max + 2, (num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(k + 3, k + 3, (num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(k + 3, k + 3 + rows_max - s.length, num, num));
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
					sheet.addMergedRegion(
							new CellRangeAddress(dataList.size() + 15, rows_max + dataList.size() + 14, (num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(k + dataList.size() + 15, k + dataList.size() + 15,
							(num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(k + dataList.size() + 15,
								k + dataList.size() + 15 + rows_max - s.length, num, num));
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
				if (dataMap.get(fields[c]) != null && dataMap.get(fields[c]).toString().length() > 0

				) {

					// 判断data是否为数值型
					isNum = dataMap.get(fields[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fields[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fields[c]).toString().contains("%");
					isGongshi = dataMap.get(fields[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fields[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					// 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fields[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fields[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fields[c]).toString());

					}

					else {
						if (dataMap.get(fields[c]).toString().contains("TOTAL")) {
							contentCell.setCellStyle(cellStylehead);
							datarow.createCell(c + 1).setCellStyle(cellStylehead);
							for (int i = 1; i <= maxMonth; i++) {
								datarow.createCell(c + 1 + i).setCellStyle(cellStylehead);
							}
						} else if (dataMap.get(fields[c]).toString().contains("Growth vs. Monthly")) {
							contentCell.setCellStyle(cellStylehead);
							datarow.createCell(c + 1).setCellStyle(cellStylehead);
							for (int i = 1; i <= maxMonth; i++) {
								datarow.createCell(c + 1 + i).setCellStyle(cellStylehead);
							}

						} else {
							contentCell.setCellStyle(contextstyle);
						}

						// 设置单元格内容为字符型
						contentCell.setCellValue(dataMap.get(fields[c]).toString());
					}
					if (d != dataList.size() - 1) {
						HashMap<String, Object> dataMapTwo = dataList.get(d + 1);
						if (dataMap.get(fields[c]).equals(dataMapTwo.get(fields[c]))) {
							sheet.addMergedRegion(
									new CellRangeAddress(d + 2 + rows_max + 1, d + 2 + rows_max + 1 + 1, 0, 0));

						}
					}
				} else {
					contentCell.setCellValue("");
				}
				/*
				 * if (d == dataList.size() - 1) {
				 * cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
				 * contentCell.setCellStyle(cellStyleGreen);
				 * 
				 * }
				 */

			}

		}

		for (int d = 0; d < dataListTwo.size(); d++) {
			DataFormat df = wb.createDataFormat();
			HashMap<String, Object> dataMap = dataListTwo.get(d);

			// 创建一行
			Row datarow = sheet.createRow(d + dataList.size() + 14 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，
			Cell scell = datarow.createCell((short) 0);

			scell.setCellType(Cell.CELL_TYPE_NUMERIC);

			for (int c = 0; c < fieldsTwo.length; c++) {

				Cell contentCell = datarow.createCell(c);
				Boolean isNum = false;// data是否为数值型
				Boolean isInteger = false;// data是否为整数
				Boolean isPercent = false;// data是否为百分数
				Boolean isGongshi = false;// data是否为百分数
				Boolean isGongshiOne = false;
				if (dataMap.get(fieldsTwo[c]) != null && dataMap.get(fieldsTwo[c]).toString().length() > 0

				) {

					// 判断data是否为数值型
					isNum = dataMap.get(fieldsTwo[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fieldsTwo[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fieldsTwo[c]).toString().contains("%");
					isGongshi = dataMap.get(fieldsTwo[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fieldsTwo[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					// 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsTwo[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsTwo[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsTwo[c]).toString());

					} else {
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为字符型
						contentCell.setCellValue(dataMap.get(fieldsTwo[c]).toString());
					}

					/*
					 * if (d != dataListTwo.size() - 1) { HashMap<String, Object> dataMapTwo =
					 * dataListTwo.get(d + 1); if
					 * (dataMap.get(fieldsTwo[c]).equals(dataMapTwo.get(fieldsTwo[c]))) {
					 * sheet.addMergedRegion(new CellRangeAddress(d + dataList.size() + 14 +
					 * rows_max + 1, d + dataList.size() + 14 + rows_max + 1 + 1, 0, 0));
					 * 
					 * } }
					 */

				} else {
					contentCell.setCellValue("");
				}
				if (d == dataListTwo.size() - 1) {
					cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
					contentCell.setCellStyle(cellStyleGreen);
				}

			}

		}

	}

	@Override
	public void monthYTDselloutQuaS(String what, SXSSFWorkbook wb, String beginDateOne, String endDateOne,
			String searchStr, String conditions) throws Exception {

		String[] toYear = beginDateOne.split("-");
		String[] toYearEnd = endDateOne.split("-");
		int laYear = Integer.parseInt(toYear[0].toString()) - 1;
		String laDays = BDDateUtil.getLastDayOfMonth(laYear, Integer.parseInt(toYear[1]));
		String[] laDay = laDays.split("-");
		int lastYear = Integer.parseInt(toYear[0]) - 1;
		String YTDBegin = toYear[0] + "-01-01";
		int maxMonth = Integer.parseInt(toYear[1]);
		String lastBegin=laYear+"-12-01";
		String lastEnd=laYear+"12-31";
		if (what.equals("custom")) {
			YTDBegin = beginDateOne;
			maxMonth = BDDateUtil.getBetweenMonth(toYear[0] + "-" + toYear[1], toYearEnd[0] + "-" + toYearEnd[1]);
		} else {
			YTDBegin = toYear[0] + "-01-01";
			maxMonth = Integer.parseInt(toYear[1]);
		}

		// 表头数据
		String[] headers = { 
				beginDateOne + " - " + endDateOne + "_ _Country", 
				beginDateOne + " - " + endDateOne + "_ _Category",
				beginDateOne + " - " + endDateOne + "_SUBTOTAL_Qty",
				beginDateOne + " - " + endDateOne + "_SUBTOTAL_Share"

		};
		String Month = "";
		String[] month = BDDateUtil.getBetMonth(beginDateOne, toYearEnd[0] + "-" + toYearEnd[1] + "-" + toYear[2]);
		for (int i = 0; i < month.length; i++) {
			headers = insert(headers, beginDateOne + " - " + endDateOne + "_" + month[i]+"_Qty");
			headers = insert(headers, beginDateOne + " - " + endDateOne + "_" + month[i]+"_Share");
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

		// 表头数据
		String[] headersTwo = { "",
				"SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_ _MODELS",
				"SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_SUBTOTAL_Qty",
						"SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_SUBTOTAL_Share"

		};

		for (int i = 0; i < month.length; i++) {
			headersTwo = insert(headersTwo,
					"SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_" + month[i]+"_Qty");
			headersTwo = insert(headersTwo,
					"SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_" + month[i]+"_Share");
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

		long start = System.currentTimeMillis();
		Map<String, BigDecimal> mapResultTwo = new HashMap<String, BigDecimal>();

		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();

		List<HashMap<String, Object>> specDatasOneData = excelDao.selectSaleDataBySpecYTDCUSTOM(YTDBegin, endDateOne,
				searchStr, conditions, WebPageUtil.isHQRole());

		List<HashMap<String, Object>> specTotalOneData = excelDao.selectSaleTotalBySpecYTDCUSTOM(YTDBegin, endDateOne,
				searchStr, conditions, WebPageUtil.isHQRole());
		List<HashMap<String, Object>> specTotalLast = excelDao.selectSaleTotalBySpecYTD(lastBegin, lastEnd,
				searchStr, conditions, WebPageUtil.isHQRole());
		
		int beginNow = 6;// 每次+8
		int endNow = 12;
		BigDecimal totalLast=BigDecimal.ZERO;
		for (int i = 0; i < countryList.length; i++) {
			BigDecimal bdTotal = BigDecimal.ZERO;
			for (int j = 0; j < specList.length; j++) {
				BigDecimal bd = BigDecimal.ZERO;
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put(beginDateOne + " - " + endDateOne + "_ _Country", countryList[i]);
				data.put(beginDateOne + " - " + endDateOne + "_ _Category", specList[j]);
				data.put(beginDateOne + " - " + endDateOne + "_SUBTOTAL_Qty", "SUM(D" + (6 + dataList.size()) + ":"
						+ BDDateUtil.getExcelColumnLabel(maxMonth + 3) + (6 + dataList.size()) + ")");
				for (int k = 0; k < specDatasOneData.size(); k++) {
					bd = new BigDecimal(specDatasOneData.get(k).get("saleQty").toString());
					if (specDatasOneData.get(k).get("party_name").equals(countryList[i])
							&& specDatasOneData.get(k).get("SPEC").equals(specList[j])) {
						String mon = specDatasOneData.get(k).get("date").toString();
						String key = beginDateOne + " - " + endDateOne + "_" + mon+"_Qty";
						data.put(key, Math.round(bd.doubleValue()));

						BigDecimal   monthTTL=BigDecimal.ZERO;
						for (int s = 0; s< specTotalOneData.size(); s++) {
							
							if (specTotalOneData.get(s).get("party_name").equals(countryList[i])
									&& specTotalOneData.get(s).get("date").toString().equals(mon)) {
								monthTTL = new BigDecimal(specTotalOneData.get(s).get("saleQty").toString());
								data.put( beginDateOne + " - " + endDateOne + "_" + mon+"_Share", 
										Math.round(bd.doubleValue()/monthTTL.doubleValue()*100)+"%");
							}
						}
					
						
						
						BigDecimal numOne = mapResultTwo.get(specList[j] + "_" + key);
						BigDecimal numOneSize = mapResultTwo.get(key);
						BigDecimal numOneCountrySpec = mapResultTwo.get(specList[j] + "_" + countryList[i]);
						BigDecimal numOneCountry = mapResultTwo.get(countryList[i]);
						BigDecimal numOneBDSC = mapResultTwo.get("BDSC");
						BigDecimal numOneBDSCSpec = mapResultTwo.get(specList[j] );
						if (numOne == null) {
							numOne = BigDecimal.ZERO;
						}
						if (numOneSize == null) {
							numOneSize = BigDecimal.ZERO;
						}
						if (numOneCountry == null) {
							numOneCountry = BigDecimal.ZERO;
						}
						
						if (numOneCountrySpec == null) {
							numOneCountrySpec = BigDecimal.ZERO;
						}
						
						if (numOneBDSC == null) {
							numOneBDSC = BigDecimal.ZERO;
						}
						if (numOneBDSCSpec == null) {
							numOneBDSCSpec = BigDecimal.ZERO;
						}
						mapResultTwo.put(specList[j] + "_" + key, numOne.add(bd));
						mapResultTwo.put(key, numOneSize.add(bd));
						mapResultTwo.put(specList[j] + "_" + countryList[i], numOneCountrySpec.add(bd));
						mapResultTwo.put(countryList[i], numOneCountry.add(bd));
						mapResultTwo.put("BDSC", numOneBDSC.add(bd));
						mapResultTwo.put(specList[j] , numOneBDSCSpec.add(bd));
						
					}
				}
				dataList.add(data);
			}

			LinkedList<HashMap<String, Object>> dataListLast = new LinkedList<HashMap<String, Object>>();

			for (int k = 0; k < dataList.size(); k++) {
				HashMap<String, Object>  obj=dataList.get(k);
				BigDecimal 	bd=BigDecimal.ZERO;
				BigDecimal 	bdTTL=BigDecimal.ZERO;
				
				if (mapResultTwo.get(obj.get(beginDateOne + " - " + endDateOne +  "_ _Category")+ "_"
				+ obj.get(beginDateOne + " - " + endDateOne + "_ _Country")) != null) {
					
				bd = mapResultTwo.get(obj.get(beginDateOne + " - " + endDateOne +  "_ _Category")+ "_"
							+ obj.get(beginDateOne + " - " + endDateOne + "_ _Country"));
				obj.put(beginDateOne + " - " + endDateOne + "_SUBTOTAL_Qty",Math.round(bd.doubleValue()));
						
				}
				if (mapResultTwo.get(obj.get(beginDateOne + " - " + endDateOne + "_ _Country")) != null) {
						bdTTL = mapResultTwo.get(obj.get(beginDateOne + " - " + endDateOne + "_ _Country")) ;
						if(bdTTL.doubleValue()==0.0 || bdTTL.doubleValue()==0) {
							obj.put(beginDateOne + " - " + endDateOne +  "_SUBTOTAL_Share","100%");
						}else {
							double ach=bd.doubleValue()/bdTTL.doubleValue()*100;
							obj.put(beginDateOne + " - " + endDateOne +  "_SUBTOTAL_Share",Math.round(ach)+"%");
						}
				}
				
			
				
				dataListLast.add(obj);
				
			}
			dataList=dataListLast;
			
			HashMap<String, Object> dataTotal = new HashMap<String, Object>();
			HashMap<String, Object> dataTotalLast = new HashMap<String, Object>();
			dataTotalLast.put(beginDateOne + " - " + endDateOne + "_ _Category", "Growth vs. Monthly");
			String key = beginDateOne + " - " + endDateOne +"_ _Category";
			dataTotal.put(key, "TOTAL");
			for (int j = 0; j < specTotalOneData.size(); j++) {
				bdTotal = new BigDecimal(specTotalOneData.get(j).get("saleQty").toString());
				if (specTotalOneData.get(j).get("party_name").equals(countryList[i])) {
					String mon = specTotalOneData.get(j).get("date").toString();
					key = beginDateOne + " - " + endDateOne+ "_"  + mon+"_Qty";
					dataTotal.put(key, Math.round(bdTotal.doubleValue()));
				}
				BigDecimal 	bd =BigDecimal.ZERO;
				if (mapResultTwo.get(countryList[i]) != null) {
					 bd = mapResultTwo.get(countryList[i]) ;
					dataTotal.put(beginDateOne + " - " + endDateOne + "_SUBTOTAL_Qty",Math.round(bd.doubleValue()));
			}
				dataTotalLast.put(beginDateOne + " - " + endDateOne + "_SUBTOTAL_Qty", Math.round(bd.doubleValue()/month.length));
				dataTotal.put(beginDateOne + " - " + endDateOne + "_SUBTOTAL_Share","100%");
			}

			for (int j =0; j <month.length; j++) {
				String whichMonth=month[j];
			
				key = beginDateOne + " - " + endDateOne + "_" + whichMonth+"_Qty";
				BigDecimal tNow=	BigDecimal.ZERO;
				BigDecimal tLast=	BigDecimal.ZERO;;
				if(dataTotal.get(key)!=null) {
					 tNow=	new BigDecimal(dataTotal.get(key).toString());
				}
				if(j==0) {
					if(specTotalLast.size()>0) {
						for (int k = 0; k < specTotalLast.size(); k++) {
							if(countryList[i].equals(specTotalLast.get(k).get("party_name"))) {
								BigDecimal bdLast=new BigDecimal(specTotalLast.get(k).get("saleQty").toString());
								totalLast=totalLast.add(bdLast);
								if(bdLast.doubleValue()==0.0 || bdLast.doubleValue()==0) {
									dataTotalLast.put(key, "100%");
								}else {
									double gro=(tNow.doubleValue()-bdLast.doubleValue())/bdLast.doubleValue()*100;
									dataTotalLast.put(key,Math.round(gro)+"%");
								}
							}
						}
					}else {
						dataTotalLast.put(key, "100%");
					}
				
				}else {
			
				String d=(Integer.parseInt(whichMonth.split("-")[1])-1) < 10 ? "0" + (Integer.parseInt(whichMonth.split("-")[1])-1) : (Integer.parseInt(whichMonth.split("-")[1])-1) + "";
				String  lastMonth=whichMonth.split("-")[0]+"-"+ d;
				if(dataTotal.get( beginDateOne + " - " + endDateOne + "_" + lastMonth+"_Qty")!=null) {
					tLast=	new BigDecimal(dataTotal.get( beginDateOne + " - " + endDateOne +"_" + lastMonth+"_Qty").toString());
				}
				if(tLast.doubleValue()==0.0 || tLast.doubleValue()==0) {
					dataTotalLast.put(key, "100%");
				}else {
					double gro=(tNow.doubleValue()-tLast.doubleValue())/tLast.doubleValue()*100;
					dataTotalLast.put(key,Math.round(gro)+"%");
				}
			
				}
				dataTotal.put(beginDateOne + " - " + endDateOne + "_" + whichMonth+"_Share", "100%");
				// 本期数－同期数）÷同期数×100%
			}
			
		
			dataList.add(dataTotal);
			dataList.add(dataTotalLast);
			HashMap<String, Object> dataT = new HashMap<String, Object>();
			dataList.add(dataT);
		}

		if (WebPageUtil.isHQRole()) {
			for (int j = 0; j < specList.length; j++) {
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put(beginDateOne + " - " + endDateOne + "_ _Country", "BDSC");
				data.put(beginDateOne + " - " + endDateOne + "_ _Category", specList[j]);
				double gro =0.0;
					if (mapResultTwo.get(specList[j]) != null) {
						BigDecimal 	bd = mapResultTwo.get(specList[j]);
						data.put(beginDateOne + " - " + endDateOne + "_SUBTOTAL_Qty",Math.round(bd.doubleValue()));
						if(mapResultTwo.get("BDSC")!=null) {
							gro=bd.doubleValue()/mapResultTwo.get("BDSC").doubleValue()*100;
							data.put(beginDateOne + " - " + endDateOne + "_SUBTOTAL_Share",Math.round(gro)+"%");
						}else {
							data.put(beginDateOne + " - " + endDateOne +"_SUBTOTAL_Share","100%");
						}
						}
				
				for (int k = 0; k <month.length; k++) {
					String whichMonth=month[k];
					String key = beginDateOne + " - " + endDateOne + "_" + whichMonth+"_Qty";
					BigDecimal bd = BigDecimal.ZERO;
					if (mapResultTwo.get(specList[j] + "_" + key) != null) {
						bd = mapResultTwo.get(specList[j] + "_" + key);
					
						if (mapResultTwo.get(key) != null) {
							gro=bd.doubleValue()/mapResultTwo.get(key).doubleValue()*100;
							data.put(beginDateOne + " - " + endDateOne + "_" + whichMonth+"_Share", Math.round(gro)+"%");
						}else {
							data.put(beginDateOne + " - " + endDateOne + "_" +whichMonth+"_Share", "100%");
						}
						
					}

					data.put(key, Math.round(bd.doubleValue()));
				}

			
				dataList.add(data);
			}
			HashMap<String, Object> dataTotal = new HashMap<String, Object>();
			HashMap<String, Object> dataTotalTwo = new HashMap<String, Object>();
			dataTotalTwo.put(beginDateOne + " - " + endDateOne + "_ _Category", "Growth vs. Monthly");
			dataTotal.put(beginDateOne + " - " + endDateOne + "_ _Category", "TOTAL");

			if(mapResultTwo.get("BDSC")!=null) {
				dataTotal.put(beginDateOne + " - " + endDateOne + "_SUBTOTAL_Qty",Math.round(mapResultTwo.get("BDSC").doubleValue()));
				dataTotalTwo.put(beginDateOne + " - " + endDateOne + "_SUBTOTAL_Qty", Math.round(Math.round(mapResultTwo.get("BDSC").doubleValue())/ Integer.parseInt(toYear[1])));

			}
			dataTotal.put(beginDateOne + " - " + endDateOne + "_SUBTOTAL_Share","100%");
			
			for (int k = 0; k <month.length; k++) {
				String whichMonth=month[k];
				String key = beginDateOne + " - " + endDateOne + "_" + whichMonth+"_Qty";
				BigDecimal bd = BigDecimal.ZERO;
				if (mapResultTwo.get(key) != null) {
					bd = mapResultTwo.get(key);
				}
				dataTotal.put(key, Math.round(bd.doubleValue()));
				// 单元格行
				
				if (k != 0) {
					String d=(Integer.parseInt(whichMonth.split("-")[1])-1) < 10 ? "0" + (Integer.parseInt(whichMonth.split("-")[1])-1) : (Integer.parseInt(whichMonth.split("-")[1])-1) + "";

					String  lastMonth=whichMonth.split("-")[0]+"-"+d;
					String	 keyLast =beginDateOne + " - " + endDateOne + "_" + lastMonth+"_Qty";
					if( mapResultTwo.get(key)!=null ) {
							if( mapResultTwo.get(keyLast)!=null ) {
								double ach=( mapResultTwo.get(key).doubleValue()- mapResultTwo.get(keyLast).doubleValue())
										/ mapResultTwo.get(keyLast).doubleValue()
										*100;
								dataTotalTwo.put(key, Math.round(ach)+"%");
							}else {
								dataTotalTwo.put(key, "100%");
							}
					}
					
					dataTotal.put(beginDateOne + " - " + endDateOne + "_" + whichMonth+"_Share", "100%");
					// 本期数－同期数）÷同期数×100%
				}else {
					if(totalLast.doubleValue()==0.0 || totalLast.doubleValue()==0) {
						dataTotalTwo.put(key, "100%");
					}else {
						double gro=(bd.doubleValue()-totalLast.doubleValue())/totalLast.doubleValue()*100;
						dataTotalTwo.put(key,Math.round(gro)+"%");
					}
				}

			}
			dataList.add(dataTotal);

			dataList.add(dataTotalTwo);
		}

		
		LinkedList<HashMap<String, Object>> dataListTwo = new LinkedList<HashMap<String, Object>>();

		LinkedList<LinkedHashMap<String, Object>> modelTotalData = excelDao.selectSaleDataByModelYTDCUSTOM(YTDBegin,
				endDateOne, searchStr, conditions, WebPageUtil.isHQRole());

		LinkedHashMap<String, LinkedList<LinkedHashMap<String, Object>>> modelMap = new LinkedHashMap<String, LinkedList<LinkedHashMap<String, Object>>>();
		for (int m = 0; m < modelTotalData.size(); m++) {
			if (modelMap.get(modelTotalData.get(m).get("model").toString()) == null) {
				LinkedList<LinkedHashMap<String, Object>> modelList = new LinkedList<LinkedHashMap<String, Object>>();
				modelList.add(modelTotalData.get(m));
				modelMap.put(modelTotalData.get(m).get("model").toString(), modelList);
			} else {
				LinkedList<LinkedHashMap<String, Object>> modelList = modelMap
						.get(modelTotalData.get(m).get("model").toString());
				modelList.add(modelTotalData.get(m));
			}

		}

		BigDecimal bdTotal = BigDecimal.ZERO;
		Map<String, BigDecimal> mapResultOne = new HashMap<String, BigDecimal>();
		Collection keys = modelMap.keySet();
		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			BigDecimal modelTotal = BigDecimal.ZERO;
			HashMap<String, Object> dataTwo = new HashMap<String, Object>();
			Object key = iterator.next();
			LinkedList<LinkedHashMap<String, Object>> monthList = modelMap.get(key);
			String keyCell =  "SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_ _MODELS";
			dataTwo.put(keyCell, key);
			for (int j = 0; j < monthList.size(); j++) {
				BigDecimal bd = new BigDecimal(monthList.get(j).get("saleQty").toString());
				keyCell =  "SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_"
						+ monthList.get(j).get("date").toString()+"_Qty";
				dataTwo.put(keyCell, Math.round(bd.doubleValue()));
				bdTotal = bdTotal.add(bd);
				modelTotal = modelTotal.add(bd);
				BigDecimal numOne = mapResultOne.get(monthList.get(j).get("date").toString());
				if (numOne == null) {
					numOne = BigDecimal.ZERO;
				}
				mapResultOne.put(monthList.get(j).get("date").toString(), numOne.add(bd));

			}
			
			dataTwo.put( "SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_SUBTOTAL_Qty", Math.round(modelTotal.doubleValue()));
			dataListTwo.add(dataTwo);
		}
		
		LinkedList<HashMap<String, Object>> dataListTwoLast = new LinkedList<HashMap<String, Object>>();
		for (int c = 0; c < dataListTwo.size(); c++) {
			HashMap<String, Object> obj=dataListTwo.get(c);
			if(obj.get( "SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_SUBTOTAL_Qty")!=null) {
				BigDecimal modelTotal = new BigDecimal(obj.get( "SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_SUBTOTAL_Qty").toString());
				double gro=modelTotal.doubleValue()/bdTotal.doubleValue()*100;
				obj.put( "SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_SUBTOTAL_Share", 
						Math.round(gro)+"%");
			}

			
			for (int i = 0; i <month.length; i++) {
				BigDecimal bd = BigDecimal.ZERO;
				BigDecimal bdQty = BigDecimal.ZERO;
				String whichMonth=month[i];
				String key =  "SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_" + whichMonth+"_Share";
				String keyQty =  "SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_" +whichMonth+"_Qty";
				if (mapResultOne.get(whichMonth) != null) {
					bd = mapResultOne.get(whichMonth);
					if(obj.get(keyQty)!=null) {
						bdQty=new BigDecimal(obj.get(keyQty).toString());
						double gro=bdQty.doubleValue()/bd.doubleValue()*100;
						obj.put(key, Math.round(gro)+"%");
					}else {
						obj.put(key, "0%");
					}
				}else {
					obj.put(key, "100%");
				}
		
				

			}
			dataListTwoLast.add(obj);
		}
		dataListTwo=dataListTwoLast;
		HashMap<String, Object> dataTwoTotal = new HashMap<String, Object>();
		dataTwoTotal.put( "SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_ _MODELS", "TOTAL");
		for (int i = 0; i <month.length; i++) {
			String whichMonth=month[i];
			BigDecimal bd = BigDecimal.ZERO;
			if (mapResultOne.get(whichMonth) != null) {
				bd = mapResultOne.get(whichMonth);
			}
			String key =  "SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_" + whichMonth+"_Qty";
			dataTwoTotal.put(key, Math.round(bd.doubleValue()));
			dataTwoTotal.put( "SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_" + whichMonth+"_Share", "100%");

		}
		dataTwoTotal.put( "SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_SUBTOTAL_Qty", Math.round(bdTotal.doubleValue()));
		dataTwoTotal.put( "SELL-OUT TREND PER MODEL Date-" + beginDateOne + " - " + endDateOne + "_SUBTOTAL_Share", "100%");
		
		// headers = insert(headers, "YTD- "+toYear[0]+"_"+getMonth(i+""));
		System.out.println("+++++++++++++++YTD 第一个++++++++++++++++++" + (System.currentTimeMillis() - start));

		

		dataListTwo.add(dataTwoTotal);
		start = System.currentTimeMillis();

		System.out.println("+++++++++++++++YTD 第二个++++++++++++++++++" + (System.currentTimeMillis() - start));

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
		sheet.createFreezePane(3, 6, 3, 6);
		// sheet.createFreezePane(3, 88, 3, 89);
		CellStyle cellStylename = wb.createCellStyle();// 表名样式
		cellStylename.setFont(fonthead);

		CellStyle cellStyleinfo = wb.createCellStyle();// 表信息样式
		cellStyleinfo.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐
		cellStyleinfo.setFont(fontinfo);

		CellStyle cellStyleDate = wb.createCellStyle();

		DataFormat dataFormat = wb.createDataFormat();

		cellStyleDate.setDataFormat(dataFormat.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
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
		cellStyleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
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
		cellStylePERCENT.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
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
		cell.setCellValue("TCL BDSC");// 标题

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
		// cell.setCellValue("YTD- " + toYear[0]);// 标题

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
					sheet.addMergedRegion(new CellRangeAddress(3, rows_max + 2, (num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(k + 3, k + 3, (num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(k + 3, k + 3 + rows_max - s.length, num, num));
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

		rows_max=0;
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
					sheet.addMergedRegion(
							new CellRangeAddress(dataList.size() + 15, rows_max + dataList.size() + 14, (num), (num)));
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
					sheet.addMergedRegion(new CellRangeAddress(k + dataList.size() + 15, k + dataList.size() + 15,
							(num), (num + cols - 1)));

					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(k + dataList.size() + 15,
								k + dataList.size() + 15 + rows_max - s.length, num, num));
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
		int starts=2+rows_max+1;
		int end=starts;
				
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
				if (dataMap.get(fields[c]) != null && dataMap.get(fields[c]).toString().length() > 0

				) {

					// 判断data是否为数值型
					isNum = dataMap.get(fields[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fields[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fields[c]).toString().contains("%");
					isGongshi = dataMap.get(fields[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fields[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					// 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fields[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fields[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fields[c]).toString());

					}

					else {
						if (dataMap.get(fields[c]).toString().contains("TOTAL")) {
							contentCell.setCellStyle(cellStylehead);
							datarow.createCell(c + 1).setCellStyle(cellStylehead);
							for (int i = 1; i <= maxMonth; i++) {
								datarow.createCell(c + 1 + i).setCellStyle(cellStylehead);
							}
						} else if (dataMap.get(fields[c]).toString().contains("Growth vs. Monthly")) {
							contentCell.setCellStyle(cellStylehead);
							datarow.createCell(c + 1).setCellStyle(cellStylehead);
							for (int i = 1; i <= maxMonth; i++) {
								datarow.createCell(c + 1 + i).setCellStyle(cellStylehead);
							}

						} else {
							contentCell.setCellStyle(contextstyle);
						}

						// 设置单元格内容为字符型
						contentCell.setCellValue(dataMap.get(fields[c]).toString());
					}
					if(c%2==0 &&  dataMap.get(fields[c]).toString().contains("%"))
					{ 
						if(dataMap.get(fields[c]).toString().contains("-")) {
							contentCell.setCellStyle(cellStyleRED);
						}else {
							contentCell.setCellStyle(cellStyleGreen);
						}
					}
		
				} else {
					contentCell.setCellValue("");
				}
				

			}
			if (d != dataList.size() - 1) {
				String keyMap=beginDateOne + " - " + endDateOne + "_ _Country";
				HashMap<String, Object> dataMapTwo = dataList.get(d + 1);
				if(dataMap.get(keyMap)!=null &&dataMapTwo.get(keyMap)!=null ) {
					if (dataMap.get(keyMap).equals(dataMapTwo.get(keyMap))) {
						
					}else {
						sheet.addMergedRegion(
								new CellRangeAddress(starts, d +end+2, 0, 0));
						starts=d +end+4;
					}
				}else if(dataMap.get(keyMap)!=null && dataMapTwo.get(keyMap)==null ){
					sheet.addMergedRegion(
							new CellRangeAddress(starts, d +end+2, 0, 0));
					starts=d +end+4;
				}
			
			}else {
				sheet.addMergedRegion(
						new CellRangeAddress(starts, d +end, 0, 0));
			}
		}

		for (int d = 0; d < dataListTwo.size(); d++) {
			DataFormat df = wb.createDataFormat();
			HashMap<String, Object> dataMap = dataListTwo.get(d);

			// 创建一行
			Row datarow = sheet.createRow(d + dataList.size() + 14 + rows_max + 1);// d+row_max+1是指行号，行号和列号都从0开始计的，这个1是同于标题占了一行，另外rows_max是指表头所占行数，
			Cell scell = datarow.createCell((short) 0);

			scell.setCellType(Cell.CELL_TYPE_NUMERIC);

			for (int c = 0; c < fieldsTwo.length; c++) {

				Cell contentCell = datarow.createCell(c);
				Boolean isNum = false;// data是否为数值型
				Boolean isInteger = false;// data是否为整数
				Boolean isPercent = false;// data是否为百分数
				Boolean isGongshi = false;// data是否为百分数
				Boolean isGongshiOne = false;
				if (dataMap.get(fieldsTwo[c]) != null && dataMap.get(fieldsTwo[c]).toString().length() > 0

				) {

					// 判断data是否为数值型
					isNum = dataMap.get(fieldsTwo[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
					// 判断data是否为整数（小数部分是否为0）
					isInteger = dataMap.get(fieldsTwo[c]).toString().matches("^[-\\+]?[\\d]*$");
					// 判断data是否为百分数（是否包含“%”）
					isPercent = dataMap.get(fieldsTwo[c]).toString().contains("%");
					isGongshi = dataMap.get(fieldsTwo[c]).toString().contains("SUM");
					isGongshiOne = dataMap.get(fieldsTwo[c]).toString().contains("TEXT");
					// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
					// 此处设置数据格式
					if (isNum && !isPercent) {
						if (isInteger) {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
						} else {
							contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
						}
						// 设置单元格格式
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为double类型
						contentCell.setCellValue(Double.parseDouble(dataMap.get(fieldsTwo[c]).toString()));
					} else if (isGongshi) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						contextstyle.setDataFormat(df.getFormat("#,##0"));
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsTwo[c]).toString());
					} else if (isGongshiOne) {
						contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
						contentCell.setCellStyle(contextstyle);
						contentCell.setCellFormula(dataMap.get(fieldsTwo[c]).toString());

					} else {
						contentCell.setCellStyle(contextstyle);
						// 设置单元格内容为字符型
						contentCell.setCellValue(dataMap.get(fieldsTwo[c]).toString());
					}

					/*
					 * if (d != dataListTwo.size() - 1) { HashMap<String, Object> dataMapTwo =
					 * dataListTwo.get(d + 1); if
					 * (dataMap.get(fieldsTwo[c]).equals(dataMapTwo.get(fieldsTwo[c]))) {
					 * sheet.addMergedRegion(new CellRangeAddress(d + dataList.size() + 14 +
					 * rows_max + 1, d + dataList.size() + 14 + rows_max + 1 + 1, 0, 0));
					 * 
					 * } }
					 */

				} else {
					contentCell.setCellValue("");
				}
				if (d == dataListTwo.size() - 1) {
					cellStyleGreen.setDataFormat(df.getFormat("#,##0"));
					contentCell.setCellStyle(cellStyleGreen);
				}

			}

		}

	}

	@Override
	public String selectPartyByUser(String userId) throws Exception {
		return excelDao.selectPartyByUser(userId);
	}

	@Override
	public String selectCountryByUser(String userId) {
		return excelDao.selectCountryByUser(userId);
	}

	public String getMonth(String month) {
		String Month = "";
		if (month.equals("01") || month.equals("1")) {
			Month = "Jan.";
		} else if (month.equals("02") || month.equals("2")) {
			Month = "Feb.";
		} else if (month.equals("03") || month.equals("3")) {
			Month = "Mar.";
		} else if (month.equals("04") || month.equals("4")) {
			Month = "Apr.";
		} else if (month.equals("05") || month.equals("5")) {
			Month = "May.";
		} else if (month.equals("06") || month.equals("6")) {
			Month = "June.";
		} else if (month.equals("07") || month.equals("7")) {
			Month = "July.";
		} else if (month.equals("08") || month.equals("8")) {
			Month = "Aug.";
		} else if (month.equals("09") || month.equals("9")) {
			Month = "Sept.";
		} else if (month.equals("10")) {
			Month = "Oct.";
		} else if (month.equals("11")) {
			Month = "Nov.";
		} else if (month.equals("12")) {
			Month = "Dec.";
		}
		return Month;
	}

	@Override
	public void common(String what, SXSSFWorkbook wb, LinkedList<HashMap<String, Object>> list, String wbName,
			String beginDate, String endDate, String searchStr, String conditions) {

		try {

			List<Excel> excels = null;
			String sheetName = "";

			sheetName = beginDate + " — " + endDate + "(Shop)";
			// 表头数据
			String[] headers = { "SELL-OUT INFORMATION SHEET_REG.", "SELL-OUT INFORMATION SHEET_TYPE",
					"SELL-OUT INFORMATION SHEET_NO OF SHOP", "SELL-OUT INFORMATION SHEET_DEALER",
					"SELL-OUT INFORMATION SHEET_STORE", "TV FPS", "PROMODISER NAME", "DATE OF HIRED", "SHOP ID", "ACFO",
					"AREA", "SALESMAN", "AGENCY", "SHOP CLASS", "TV SELL-OUT and TARGET_TTL TV SO_QTY",
					"TV SELL-OUT and TARGET_TTL TV SO_ASP", "TV SELL-OUT and TARGET_BASIC TARGET",
					"TV SELL-OUT and TARGET_Ach.", "TV SELL-OUT and TARGET_CHALLENGE TARGET",
					"TV SELL-OUT and TARGET_CHALLENGE Ach." };

			// 查询机型销售数据
			List<HashMap<String, Object>> modelMapList = excelService.selectModel(beginDate, endDate, searchStr,
					conditions, WebPageUtil.isHQRole());
			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < modelMapList.size(); i++) {
				BigDecimal bd = new BigDecimal(modelMapList.get(i).get("price").toString());
				String am = bd.toPlainString();
				double shop = Double.parseDouble(am);
				double price = shop / Integer.parseInt(modelMapList.get(i).get("shop").toString());

				long lnum = Math.round(price);

				String m = "" + "_" + modelMapList.get(i).get("model") + "_" + lnum + "_" + "sold";
				headers = insert(headers, m);
				if (i == modelMapList.size() - 1) {
					headers = insert(headers, "LED SUB-TOTAL_" + "QTY");
					headers = insert(headers, "LED SUB-TOTAL_" + "AMOUNT");
				}
			}
			HashMap<String, ArrayList<HashMap<String, Object>>> priceMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelMapList.size(); m++) {
				if (priceMap.get(modelMapList.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelMapList.get(m));
					priceMap.put(modelMapList.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = priceMap
							.get(modelMapList.get(m).get("model").toString());
					modelList.add(modelMapList.get(m));
				}

			}

			List<HashMap<String, Object>> modelMapListByDigital = excelService.selectModelBySpec(beginDate, endDate,
					"%DIGITAL%", searchStr, conditions, WebPageUtil.isHQRole());

			for (int i = 0; i < modelMapListByDigital.size(); i++) {
				BigDecimal bd = new BigDecimal(modelMapListByDigital.get(i).get("price").toString());
				String am = bd.toPlainString();
				double shop = Double.parseDouble(am);
				double price = shop / Integer.parseInt(modelMapListByDigital.get(i).get("shop").toString());
				long lnum = Math.round(price);
				String m = "DIGITAL BASIC" + "_" + modelMapListByDigital.get(i).get("model") + "_" + lnum + "_"
						+ "sold";
				headers = insert(headers, m);
				if (i == modelMapListByDigital.size() - 1) {

					headers = insert(headers, "DIGITAL SUB-TOTAL_" + "QTY");
					headers = insert(headers, "DIGITAL SUB-TOTAL_" + "AMOUNT");

				}

			}
			HashMap<String, ArrayList<HashMap<String, Object>>> DIGITALMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelMapListByDigital.size(); m++) {
				if (DIGITALMap.get(modelMapListByDigital.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelMapListByDigital.get(m));
					DIGITALMap.put(modelMapListByDigital.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = DIGITALMap
							.get(modelMapListByDigital.get(m).get("model").toString());
					modelList.add(modelMapListByDigital.get(m));
				}

			}

			List<HashMap<String, Object>> modelMapListByINTERNET = excelService.selectModelBySpec(beginDate, endDate,
					"%INTERNET%", searchStr, conditions, WebPageUtil.isHQRole());

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < modelMapListByINTERNET.size(); i++) {
				BigDecimal bd = new BigDecimal(modelMapListByINTERNET.get(i).get("price").toString());
				String am = bd.toPlainString();
				double shop = Double.parseDouble(am);
				double price = shop / Integer.parseInt(modelMapListByINTERNET.get(i).get("shop").toString());

				long lnum = Math.round(price);
				String m = "DIGITAL INTERNET TV" + "_" + modelMapListByINTERNET.get(i).get("model") + "_" + lnum + "_"
						+ "sold";
				headers = insert(headers, m);
				if (i == modelMapListByINTERNET.size() - 1) {
					headers = insert(headers, "INTERNET SUB-TOTAL_QTY");
					headers = insert(headers, "INTERNET SUB-TOTAL_AMOUNT");
				}

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> INTERNETMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelMapListByINTERNET.size(); m++) {
				if (INTERNETMap.get(modelMapListByINTERNET.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelMapListByINTERNET.get(m));
					INTERNETMap.put(modelMapListByINTERNET.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = INTERNETMap
							.get(modelMapListByINTERNET.get(m).get("model").toString());
					modelList.add(modelMapListByINTERNET.get(m));
				}

			}

			List<HashMap<String, Object>> modelMapListByQUHD = excelService.selectModelBySpec(beginDate, endDate,
					"%QUHD%", searchStr, conditions, WebPageUtil.isHQRole());

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < modelMapListByQUHD.size(); i++) {
				BigDecimal bd = new BigDecimal(modelMapListByQUHD.get(i).get("price").toString());
				String am = bd.toPlainString();
				double shop = Double.parseDouble(am);
				double price = shop / Integer.parseInt(modelMapListByQUHD.get(i).get("shop").toString());

				long lnum = Math.round(price);
				String m = "(QUHD) TV" + "_" + modelMapListByQUHD.get(i).get("model") + "_" + lnum + "_" + "sold";
				headers = insert(headers, m);
				if (i == modelMapListByQUHD.size() - 1) {
					headers = insert(headers, "(QUHD) SUB-TOTAL_QTY");
					headers = insert(headers, "(QUHD) SUB-TOTAL_AMOUNT");
				}

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> QUHDMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelMapListByQUHD.size(); m++) {
				if (QUHDMap.get(modelMapListByQUHD.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelMapListByQUHD.get(m));
					QUHDMap.put(modelMapListByQUHD.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = QUHDMap
							.get(modelMapListByQUHD.get(m).get("model").toString());
					modelList.add(modelMapListByQUHD.get(m));
				}

			}

			List<HashMap<String, Object>> modelMapListBySMART = excelService.selectModelBySpec(beginDate, endDate,
					"%SMART%", searchStr, conditions, WebPageUtil.isHQRole());

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < modelMapListBySMART.size(); i++) {
				BigDecimal bd = new BigDecimal(modelMapListBySMART.get(i).get("price").toString());
				String am = bd.toPlainString();
				double shop = Double.parseDouble(am);
				double price = shop / Integer.parseInt(modelMapListBySMART.get(i).get("shop").toString());

				long lnum = Math.round(price);

				String m = "SMART TV" + "_" + modelMapListBySMART.get(i).get("model") + "_" + lnum + "_" + "sold";
				headers = insert(headers, m);
				if (i == modelMapListBySMART.size() - 1) {

					headers = insert(headers, "SMART SUB-TOTAL_" + "QTY");
					headers = insert(headers, "SMART SUB-TOTAL_" + "AMOUNT");

				}

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> SMARTMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelMapListBySMART.size(); m++) {
				if (SMARTMap.get(modelMapListBySMART.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelMapListBySMART.get(m));
					SMARTMap.put(modelMapListBySMART.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = SMARTMap
							.get(modelMapListBySMART.get(m).get("model").toString());
					modelList.add(modelMapListBySMART.get(m));
				}

			}

			List<HashMap<String, Object>> modelMapListByUHD = excelService.selectModelBySpec(beginDate, endDate,
					"%UHD%", searchStr, conditions, WebPageUtil.isHQRole());

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < modelMapListByUHD.size(); i++) {
				BigDecimal bd = new BigDecimal(modelMapListByUHD.get(i).get("price").toString());
				String am = bd.toPlainString();
				double shop = Double.parseDouble(am);
				double price = shop / Integer.parseInt(modelMapListByUHD.get(i).get("shop").toString());

				long lnum = Math.round(price);
				String m = "UHD TV" + "_" + modelMapListByUHD.get(i).get("model") + "_" + lnum + "_" + "sold";
				headers = insert(headers, m);

				if (i == modelMapListByUHD.size() - 1) {
					headers = insert(headers, "UHD SUB-TOTAL_QTY");
					headers = insert(headers, "UHD SUB-TOTAL_AMOUNT");

				}

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> UHDMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelMapListByUHD.size(); m++) {
				if (UHDMap.get(modelMapListByUHD.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelMapListByUHD.get(m));
					UHDMap.put(modelMapListByUHD.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = UHDMap
							.get(modelMapListByUHD.get(m).get("model").toString());
					modelList.add(modelMapListByUHD.get(m));
				}

			}

			List<HashMap<String, Object>> modelMapListByCURVE = excelService.selectModelBySpec(beginDate, endDate,
					"%CURVE%", searchStr, conditions, WebPageUtil.isHQRole());

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < modelMapListByCURVE.size(); i++) {
				BigDecimal bd = new BigDecimal(modelMapListByCURVE.get(i).get("price").toString());
				String am = bd.toPlainString();
				double shop = Double.parseDouble(am);
				double price = shop / Integer.parseInt(modelMapListByCURVE.get(i).get("shop").toString());
				long lnum = Math.round(price);
				String m = "CURVE TV" + "_" + modelMapListByCURVE.get(i).get("model") + "_" + lnum + "_" + "sold";
				headers = insert(headers, m);
				if (i == modelMapListByCURVE.size() - 1) {
					headers = insert(headers, "CURVE SUB-TOTAL_QTY");
					headers = insert(headers, "CURVE SUB-TOTAL_AMOUNT");

				}

			}

			headers = insert(headers, "TV SUB-TOTAL_QTY");
			headers = insert(headers, "TV SUB-TOTAL_AMOUNT");

			HashMap<String, ArrayList<HashMap<String, Object>>> CURVEMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelMapListByCURVE.size(); m++) {
				if (CURVEMap.get(modelMapListByCURVE.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelMapListByCURVE.get(m));
					CURVEMap.put(modelMapListByCURVE.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = CURVEMap
							.get(modelMapListByCURVE.get(m).get("model").toString());
					modelList.add(modelMapListByCURVE.get(m));
				}

			}

			List<HashMap<String, Object>> stockMapListByDigital = excelService.selectStockBymodel("%DIGITAL%",
					searchStr, conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < stockMapListByDigital.size(); i++) {
				String m = "DIGITAL STOCKS" + "_" + stockMapListByDigital.get(i).get("model") + "_" + " " + "_"
						+ "Stocks";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> stockDIGITALMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockMapListByDigital.size(); m++) {
				if (stockDIGITALMap.get(stockMapListByDigital.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockMapListByDigital.get(m));
					stockDIGITALMap.put(stockMapListByDigital.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockDIGITALMap
							.get(stockMapListByDigital.get(m).get("model").toString());
					modelList.add(stockMapListByDigital.get(m));
				}

			}

			List<HashMap<String, Object>> stockMapListByINTERNET = excelService.selectStockBymodel("%INTERNET%",
					searchStr, conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < stockMapListByINTERNET.size(); i++) {
				String m = "INTERNET STOCKS" + "_" + stockMapListByINTERNET.get(i).get("model") + "_" + " " + "_"
						+ "Stocks";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> stockINTERNETMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockMapListByINTERNET.size(); m++) {
				if (stockINTERNETMap.get(stockMapListByINTERNET.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockMapListByINTERNET.get(m));
					stockINTERNETMap.put(stockMapListByINTERNET.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockINTERNETMap
							.get(stockMapListByINTERNET.get(m).get("model").toString());
					modelList.add(stockMapListByINTERNET.get(m));
				}

			}

			List<HashMap<String, Object>> stockMapListByQUHD = excelService.selectStockBymodel("%QUHD%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < stockMapListByQUHD.size(); i++) {
				String m = "(QUHD) STOCKS" + "_" + stockMapListByQUHD.get(i).get("model") + "_" + " " + "_" + "Stocks";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> stockQUHDMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockMapListByQUHD.size(); m++) {
				if (stockQUHDMap.get(stockMapListByQUHD.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockMapListByQUHD.get(m));
					stockQUHDMap.put(stockMapListByQUHD.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockQUHDMap
							.get(stockMapListByQUHD.get(m).get("model").toString());
					modelList.add(stockMapListByQUHD.get(m));
				}

			}

			List<HashMap<String, Object>> stockMapListBySMART = excelService.selectStockBymodel("%SMART%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < stockMapListBySMART.size(); i++) {
				String m = "SMART STOCKS" + "_" + stockMapListBySMART.get(i).get("model") + "_" + " " + "_" + "Stocks";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> stockSMARTMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockMapListBySMART.size(); m++) {
				if (stockSMARTMap.get(stockMapListBySMART.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockMapListBySMART.get(m));
					stockSMARTMap.put(stockMapListBySMART.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockSMARTMap
							.get(stockMapListBySMART.get(m).get("model").toString());
					modelList.add(stockMapListBySMART.get(m));
				}

			}

			List<HashMap<String, Object>> stockMapListByUHD = excelService.selectStockBymodel("%UHD%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < stockMapListByUHD.size(); i++) {
				String m = "UHD STOCKS" + "_" + stockMapListByUHD.get(i).get("model") + "_" + " " + "_" + "Stocks";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> stockUHDMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockMapListByUHD.size(); m++) {
				if (stockUHDMap.get(stockMapListByUHD.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockMapListByUHD.get(m));
					stockUHDMap.put(stockMapListByUHD.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockUHDMap
							.get(stockMapListByUHD.get(m).get("model").toString());
					modelList.add(stockMapListByUHD.get(m));
				}

			}

			List<HashMap<String, Object>> stockMapListByCURVE = excelService.selectStockBymodel("%CURVE%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < stockMapListByCURVE.size(); i++) {
				String m = "CURVE STOCKS" + "_" + stockMapListByCURVE.get(i).get("model") + "_" + " " + "_" + "Stocks";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> stockCURVEMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockMapListByCURVE.size(); m++) {
				if (stockCURVEMap.get(stockMapListByCURVE.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockMapListByCURVE.get(m));
					stockCURVEMap.put(stockMapListByCURVE.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockCURVEMap
							.get(stockMapListByCURVE.get(m).get("model").toString());
					modelList.add(stockMapListByCURVE.get(m));
				}

			}

			headers = insert(headers, "FLAT PANEL TV TTL INVENTORY_Stocks");

			List<HashMap<String, Object>> disMapListByDigital = excelService.selectDisplayBymodel("%DIGITAL%",
					searchStr, conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < disMapListByDigital.size(); i++) {
				String m = "DIGITAL DisPlay" + "_" + disMapListByDigital.get(i).get("model") + "_" + " " + "_"
						+ "DisPlay";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> disDIGITALMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disMapListByDigital.size(); m++) {
				if (disDIGITALMap.get(disMapListByDigital.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disMapListByDigital.get(m));
					disDIGITALMap.put(disMapListByDigital.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disDIGITALMap
							.get(disMapListByDigital.get(m).get("model").toString());
					modelList.add(disMapListByDigital.get(m));
				}

			}

			List<HashMap<String, Object>> disMapListByINTERNET = excelService.selectDisplayBymodel("%INTERNET%",
					searchStr, conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < disMapListByINTERNET.size(); i++) {
				String m = "INTERNET DisPlay" + "_" + disMapListByINTERNET.get(i).get("model") + "_" + " " + "_"
						+ "DisPlay";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> disINTERNETMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disMapListByINTERNET.size(); m++) {
				if (disINTERNETMap.get(disMapListByINTERNET.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disMapListByINTERNET.get(m));
					disINTERNETMap.put(disMapListByINTERNET.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disINTERNETMap
							.get(disMapListByINTERNET.get(m).get("model").toString());
					modelList.add(disMapListByINTERNET.get(m));
				}

			}

			List<HashMap<String, Object>> disMapListByQUHD = excelService.selectDisplayBymodel("%QUHD%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < disMapListByQUHD.size(); i++) {
				String m = "(QUHD) DisPlay" + "_" + disMapListByQUHD.get(i).get("model") + "_" + " " + "_" + "DisPlay";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> disQUHDMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disMapListByQUHD.size(); m++) {
				if (disQUHDMap.get(disMapListByQUHD.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disMapListByQUHD.get(m));
					disQUHDMap.put(disMapListByQUHD.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disQUHDMap
							.get(disMapListByQUHD.get(m).get("model").toString());
					modelList.add(disMapListByQUHD.get(m));
				}

			}

			List<HashMap<String, Object>> disMapListBySMART = excelService.selectDisplayBymodel("%SMART%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < disMapListBySMART.size(); i++) {
				String m = "SMART DisPlay" + "_" + disMapListBySMART.get(i).get("model") + "_" + " " + "_" + "DisPlay";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> disSMARTMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disMapListBySMART.size(); m++) {
				if (disSMARTMap.get(disMapListBySMART.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disMapListBySMART.get(m));
					disSMARTMap.put(disMapListBySMART.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disSMARTMap
							.get(disMapListBySMART.get(m).get("model").toString());
					modelList.add(disMapListBySMART.get(m));
				}

			}

			List<HashMap<String, Object>> disMapListByUHD = excelService.selectDisplayBymodel("%UHD%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < disMapListByUHD.size(); i++) {
				String m = "UHD DisPlay" + "_" + disMapListByUHD.get(i).get("model") + "_" + "" + "_" + "DisPlay";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> disUHDMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disMapListByUHD.size(); m++) {
				if (disUHDMap.get(disMapListByUHD.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disMapListByUHD.get(m));
					disUHDMap.put(disMapListByUHD.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disUHDMap
							.get(disMapListByUHD.get(m).get("model").toString());
					modelList.add(disMapListByUHD.get(m));
				}

			}

			List<HashMap<String, Object>> disMapListByCURVE = excelService.selectDisplayBymodel("%CURVE%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 将查出来的机型销售数据放入表头，形成三级标题
			for (int i = 0; i < disMapListByCURVE.size(); i++) {
				String m = "CURVE DisPlay" + "_" + disMapListByCURVE.get(i).get("model") + "_" + " " + "_" + "DisPlay";
				headers = insert(headers, m);

			}

			HashMap<String, ArrayList<HashMap<String, Object>>> disCURVEMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disMapListByCURVE.size(); m++) {
				if (disCURVEMap.get(disMapListByCURVE.get(m).get("model").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disMapListByCURVE.get(m));
					disCURVEMap.put(disMapListByCURVE.get(m).get("model").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disCURVEMap
							.get(disMapListByCURVE.get(m).get("model").toString());
					modelList.add(disMapListByCURVE.get(m));
				}

			}

			headers = insert(headers, "FLAT PANEL TV TTL DISPLAY_Display");

			// 按照对应格式将表头传入
			ArrayList columns = new ArrayList();
			for (int i = 0; i < headers.length; i++) {
				HashMap<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("header", headers[i]);
				columnMap.put("field", headers[i]);
				columns.add(columnMap);
			}

			// 查询门店机型销售数据
			List<HashMap<String, Object>> modeldataList = excelService.selectModelList(beginDate, endDate, searchStr,
					conditions, WebPageUtil.isHQRole());

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> shopMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modeldataList.size(); m++) {
				if (shopMap.get(modeldataList.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modeldataList.get(m));
					shopMap.put(modeldataList.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = shopMap
							.get(modeldataList.get(m).get("shop_id").toString());
					modelList.add(modeldataList.get(m));
				}

			}

			// 查询门店机型销售数据
			List<HashMap<String, Object>> modeldataListByDIGITAL = excelService.selectModelListBySpec("%DIGITAL%",
					beginDate, endDate, searchStr, conditions, WebPageUtil.isHQRole());

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> shopMapByDIGITAL = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modeldataListByDIGITAL.size(); m++) {
				if (shopMapByDIGITAL.get(modeldataListByDIGITAL.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modeldataListByDIGITAL.get(m));
					shopMapByDIGITAL.put(modeldataListByDIGITAL.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = shopMapByDIGITAL
							.get(modeldataListByDIGITAL.get(m).get("shop_id").toString());
					modelList.add(modeldataListByDIGITAL.get(m));
				}

			}

			// 查询门店机型销售数据
			List<HashMap<String, Object>> modeldataListByINTERNET = excelService.selectModelListBySpec("%INTERNET%",
					beginDate, endDate, searchStr, conditions, WebPageUtil.isHQRole());

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> shopMapByINTERNET = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modeldataListByINTERNET.size(); m++) {
				if (shopMapByINTERNET.get(modeldataListByINTERNET.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modeldataListByINTERNET.get(m));
					shopMapByINTERNET.put(modeldataListByINTERNET.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = shopMapByINTERNET
							.get(modeldataListByINTERNET.get(m).get("shop_id").toString());
					modelList.add(modeldataListByINTERNET.get(m));
				}

			}

			// 查询门店机型销售数据
			List<HashMap<String, Object>> modeldataListByQUHD = excelService.selectModelListBySpec("%QUHD%", beginDate,
					endDate, searchStr, conditions, WebPageUtil.isHQRole());

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> shopMapByQUHD = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modeldataListByQUHD.size(); m++) {
				if (shopMapByQUHD.get(modeldataListByQUHD.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modeldataListByQUHD.get(m));
					shopMapByQUHD.put(modeldataListByQUHD.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = shopMapByQUHD
							.get(modeldataListByQUHD.get(m).get("shop_id").toString());
					modelList.add(modeldataListByQUHD.get(m));
				}

			}

			// 查询门店机型销售数据
			List<HashMap<String, Object>> modeldataListBySMART = excelService.selectModelListBySpec("%SMART%",
					beginDate, endDate, searchStr, conditions, WebPageUtil.isHQRole());

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> shopMapBySMART = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modeldataListBySMART.size(); m++) {
				if (shopMapBySMART.get(modeldataListBySMART.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modeldataListBySMART.get(m));
					shopMapBySMART.put(modeldataListBySMART.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = shopMapBySMART
							.get(modeldataListBySMART.get(m).get("shop_id").toString());
					modelList.add(modeldataListBySMART.get(m));
				}

			}

			// 查询门店机型销售数据
			List<HashMap<String, Object>> modeldataListByUHD = excelService.selectModelListBySpec("%UHD%", beginDate,
					endDate, searchStr, conditions, WebPageUtil.isHQRole());

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> shopMapByUHD = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modeldataListByUHD.size(); m++) {
				if (shopMapByUHD.get(modeldataListByUHD.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modeldataListByUHD.get(m));
					shopMapByUHD.put(modeldataListByUHD.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = shopMapByUHD
							.get(modeldataListByUHD.get(m).get("shop_id").toString());
					modelList.add(modeldataListByUHD.get(m));
				}

			}

			/*
			 * DIGITAL BASIC ---基本数字电视 DIGITAL INTERNET TV ---数字网络电视 SMART TV ----智能电视 UHD
			 * ---高清电视 CURVE TV 曲面电视
			 */
			// 查询门店机型销售数据
			List<HashMap<String, Object>> modeldataListByCURVE = excelService.selectModelListBySpec("%CURVE%",
					beginDate, endDate, searchStr, conditions, WebPageUtil.isHQRole());

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> shopMapByCURVE = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modeldataListByCURVE.size(); m++) {
				if (shopMapByCURVE.get(modeldataListByCURVE.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modeldataListByCURVE.get(m));
					shopMapByCURVE.put(modeldataListByCURVE.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = shopMapByCURVE
							.get(modeldataListByCURVE.get(m).get("shop_id").toString());
					modelList.add(modeldataListByCURVE.get(m));
				}

			}

			List<HashMap<String, Object>> stockDataListByDIGITAL = excelService.selectStockByData("%DIGITAL%",
					searchStr, conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> stockMapByDIGITAL = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockDataListByDIGITAL.size(); m++) {
				if (stockMapByDIGITAL.get(stockDataListByDIGITAL.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockDataListByDIGITAL.get(m));
					stockMapByDIGITAL.put(stockDataListByDIGITAL.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockMapByDIGITAL
							.get(stockDataListByDIGITAL.get(m).get("shop_id").toString());
					modelList.add(stockDataListByDIGITAL.get(m));
				}

			}

			List<HashMap<String, Object>> stockDataListByINTERNET = excelService.selectStockByData("%INTERNET%",
					searchStr, conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> stockMapByINTERNET = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockDataListByINTERNET.size(); m++) {
				if (stockMapByINTERNET.get(stockDataListByINTERNET.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockDataListByINTERNET.get(m));
					stockMapByINTERNET.put(stockDataListByINTERNET.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockMapByINTERNET
							.get(stockDataListByINTERNET.get(m).get("shop_id").toString());
					modelList.add(stockDataListByINTERNET.get(m));
				}

			}

			List<HashMap<String, Object>> stockDataListByQUHD = excelService.selectStockByData("%QUHD%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> stockMapByQUHD = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockDataListByQUHD.size(); m++) {
				if (stockMapByQUHD.get(stockDataListByQUHD.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockDataListByQUHD.get(m));
					stockMapByQUHD.put(stockDataListByQUHD.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockMapByQUHD
							.get(stockDataListByQUHD.get(m).get("shop_id").toString());
					modelList.add(stockDataListByQUHD.get(m));
				}

			}

			List<HashMap<String, Object>> stockDataListBySMART = excelService.selectStockByData("%SMART%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);
			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> stockMapBySMART = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockDataListBySMART.size(); m++) {
				if (stockMapBySMART.get(stockDataListBySMART.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockDataListBySMART.get(m));
					stockMapBySMART.put(stockDataListBySMART.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockMapBySMART
							.get(stockDataListBySMART.get(m).get("shop_id").toString());
					modelList.add(stockDataListBySMART.get(m));
				}

			}

			List<HashMap<String, Object>> stockDataListByUHD = excelService.selectStockByData("%UHD%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);
			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> stockMapByUHD = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockDataListByUHD.size(); m++) {
				if (stockMapByUHD.get(stockDataListByUHD.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockDataListByUHD.get(m));
					stockMapByUHD.put(stockDataListByUHD.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockMapByUHD
							.get(stockDataListByUHD.get(m).get("shop_id").toString());
					modelList.add(stockDataListByUHD.get(m));
				}

			}
			List<HashMap<String, Object>> stockDataListByCURVE = excelService.selectStockByData("%CURVE%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);
			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> stockMapByCURVE = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < stockDataListByCURVE.size(); m++) {
				if (stockMapByCURVE.get(stockDataListByCURVE.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(stockDataListByCURVE.get(m));
					stockMapByCURVE.put(stockDataListByCURVE.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockMapByCURVE
							.get(stockDataListByCURVE.get(m).get("shop_id").toString());
					modelList.add(stockDataListByCURVE.get(m));
				}

			}

			List<HashMap<String, Object>> disDataListByDIGITAL = excelService.selectDisplayByData("%DIGITAL%",
					searchStr, conditions, WebPageUtil.isHQRole(), beginDate, endDate);
			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> disMapByDIGITAL = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disDataListByDIGITAL.size(); m++) {
				if (disMapByDIGITAL.get(disDataListByDIGITAL.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disDataListByDIGITAL.get(m));
					disMapByDIGITAL.put(disDataListByDIGITAL.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disMapByDIGITAL
							.get(disDataListByDIGITAL.get(m).get("shop_id").toString());
					modelList.add(disDataListByDIGITAL.get(m));
				}

			}

			List<HashMap<String, Object>> disDataListByINTERNET = excelService.selectDisplayByData("%INTERNET%",
					searchStr, conditions, WebPageUtil.isHQRole(), beginDate, endDate);
			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> disMapByINTERNET = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disDataListByINTERNET.size(); m++) {
				if (disMapByINTERNET.get(disDataListByINTERNET.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disDataListByINTERNET.get(m));
					disMapByINTERNET.put(disDataListByINTERNET.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disMapByINTERNET
							.get(disDataListByINTERNET.get(m).get("shop_id").toString());
					modelList.add(disDataListByINTERNET.get(m));
				}

			}

			List<HashMap<String, Object>> disDataListByQUHD = excelService.selectDisplayByData("%QUHD%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);
			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> disMapByQUHD = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disDataListByQUHD.size(); m++) {
				if (disMapByQUHD.get(disDataListByQUHD.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disDataListByQUHD.get(m));
					disMapByQUHD.put(disDataListByQUHD.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disMapByQUHD
							.get(disDataListByQUHD.get(m).get("shop_id").toString());
					modelList.add(disDataListByQUHD.get(m));
				}

			}

			List<HashMap<String, Object>> disDataListBySMART = excelService.selectDisplayByData("%SMART%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);
			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> disMapBySMART = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disDataListBySMART.size(); m++) {
				if (disMapBySMART.get(disDataListBySMART.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disDataListBySMART.get(m));
					disMapBySMART.put(disDataListBySMART.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disMapBySMART
							.get(disDataListBySMART.get(m).get("shop_id").toString());
					modelList.add(disDataListBySMART.get(m));
				}

			}
			List<HashMap<String, Object>> disDataListByUHD = excelService.selectDisplayByData("%UHD%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);
			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> disMapByUHD = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disDataListByUHD.size(); m++) {
				if (disMapByUHD.get(disDataListByUHD.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disDataListByUHD.get(m));
					disMapByUHD.put(disDataListByUHD.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disMapByUHD
							.get(disDataListByUHD.get(m).get("shop_id").toString());
					modelList.add(disDataListByUHD.get(m));
				}

			}

			List<HashMap<String, Object>> disDataListByCURVE = excelService.selectDisplayByData("%CURVE%", searchStr,
					conditions, WebPageUtil.isHQRole(), beginDate, endDate);

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> disMapByCURVE = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disDataListByCURVE.size(); m++) {
				if (disMapByCURVE.get(disDataListByCURVE.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disDataListByCURVE.get(m));
					disMapByCURVE.put(disDataListByCURVE.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disMapByCURVE
							.get(disDataListByCURVE.get(m).get("shop_id").toString());
					modelList.add(disDataListByCURVE.get(m));
				}

			}

			List<HashMap<String, Object>> StockTotalBySpec = excelService.selectStockByTotal(searchStr, conditions,
					beginDate, endDate);
			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> stockMapByTotal = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < StockTotalBySpec.size(); m++) {
				if (stockMapByTotal.get(StockTotalBySpec.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(StockTotalBySpec.get(m));
					stockMapByTotal.put(StockTotalBySpec.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = stockMapByTotal
							.get(StockTotalBySpec.get(m).get("shop_id").toString());
					modelList.add(StockTotalBySpec.get(m));
				}

			}

			List<HashMap<String, Object>> disTotalBySpec = excelService.selectDisPlayByTotal(searchStr, conditions,
					beginDate, endDate);

			// 按照门店进行销售数据分组
			HashMap<String, ArrayList<HashMap<String, Object>>> disPlayMapByTotal = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < disTotalBySpec.size(); m++) {
				if (disPlayMapByTotal.get(disTotalBySpec.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(disTotalBySpec.get(m));
					disPlayMapByTotal.put(disTotalBySpec.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = disPlayMapByTotal
							.get(disTotalBySpec.get(m).get("shop_id").toString());
					modelList.add(disTotalBySpec.get(m));
				}

			}
			// 查询所有导购员，督导与业务员
			List<HashMap<String, Object>> salerList = excelService.selectSalerDatas(searchStr, conditions);

			HashMap<String, ArrayList<HashMap<String, Object>>> salerDatas = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < salerList.size(); m++) {
				if (salerDatas.get(salerList.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(salerList.get(m));
					salerDatas.put(salerList.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = salerDatas
							.get(salerList.get(m).get("shop_id").toString());
					modelList.add(salerList.get(m));
				}

			}

			String tBeginDate = beginDate.split("-")[0] + "-" + beginDate.split("-")[1] + "-01";
			String tEndDate = BDDateUtil.getLastDayOfMonth(Integer.parseInt(endDate.split("-")[0]),
					Integer.parseInt(endDate.split("-")[1]));

			// 根据门店取得对应销售数据与目标数据
			List<HashMap<String, Object>> targetList = excelService.selectTargetByshop(searchStr, conditions,
					tBeginDate, tEndDate, WebPageUtil.isHQRole(), 1);

			HashMap<String, ArrayList<HashMap<String, Object>>> targetMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < targetList.size(); m++) {
				if (targetMap.get(targetList.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(targetList.get(m));
					targetMap.put(targetList.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = targetMap
							.get(targetList.get(m).get("shop_id").toString());
					modelList.add(targetList.get(m));
				}

			}

			List<HashMap<String, Object>> saleList = excelService.selectSaleDataByshop(beginDate, endDate, searchStr,
					conditions, WebPageUtil.isHQRole());
			HashMap<String, ArrayList<HashMap<String, Object>>> saleMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < saleList.size(); m++) {
				if (saleMap.get(saleList.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(saleList.get(m));
					saleMap.put(saleList.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = saleMap
							.get(saleList.get(m).get("shop_id").toString());
					modelList.add(saleList.get(m));
				}

			}

			List<HashMap<String, Object>> modelTotalByDIGITAL = excelService.selectModelTotalBySpec("%DIGITAL%",
					beginDate, endDate, searchStr, conditions, WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> modelSpecByDIGITAL = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelTotalByDIGITAL.size(); m++) {
				if (modelSpecByDIGITAL.get(modelTotalByDIGITAL.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelTotalByDIGITAL.get(m));
					modelSpecByDIGITAL.put(modelTotalByDIGITAL.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = modelSpecByDIGITAL
							.get(modelTotalByDIGITAL.get(m).get("shop_id").toString());
					modelList.add(modelTotalByDIGITAL.get(m));
				}

			}

			List<HashMap<String, Object>> modelTotalByINTERNET = excelService.selectModelTotalBySpec("%INTERNET%",
					beginDate, endDate, searchStr, conditions, WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> modelSpecByINTERNET = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelTotalByINTERNET.size(); m++) {
				if (modelSpecByINTERNET.get(modelTotalByINTERNET.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelTotalByINTERNET.get(m));
					modelSpecByINTERNET.put(modelTotalByINTERNET.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = modelSpecByINTERNET
							.get(modelTotalByINTERNET.get(m).get("shop_id").toString());
					modelList.add(modelTotalByINTERNET.get(m));
				}

			}

			List<HashMap<String, Object>> modelTotalByQUHD = excelService.selectModelTotalBySpec("%QUHD%", beginDate,
					endDate, searchStr, conditions, WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> modelSpecByQUHD = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < modelTotalByQUHD.size(); m++) {
				if (modelSpecByQUHD.get(modelTotalByQUHD.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelTotalByQUHD.get(m));
					modelSpecByQUHD.put(modelTotalByQUHD.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = modelSpecByQUHD
							.get(modelTotalByQUHD.get(m).get("shop_id").toString());
					modelList.add(modelTotalByQUHD.get(m));
				}

			}

			List<HashMap<String, Object>> modelTotalBySMART = excelService.selectModelTotalBySpec("%SMART%", beginDate,
					endDate, searchStr, conditions, WebPageUtil.isHQRole());
			HashMap<String, ArrayList<HashMap<String, Object>>> modelSpecBySMART = new HashMap<String, ArrayList<HashMap<String, Object>>>();

			for (int m = 0; m < modelTotalBySMART.size(); m++) {
				if (modelSpecBySMART.get(modelTotalBySMART.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelTotalBySMART.get(m));
					modelSpecBySMART.put(modelTotalBySMART.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = modelSpecBySMART
							.get(modelTotalBySMART.get(m).get("shop_id").toString());
					modelList.add(modelTotalBySMART.get(m));
				}

			}

			List<HashMap<String, Object>> modelTotalByUHD = excelService.selectModelTotalBySpec("%UHD%", beginDate,
					endDate, searchStr, conditions, WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> modelSpecByUHD = new HashMap<String, ArrayList<HashMap<String, Object>>>();

			for (int m = 0; m < modelTotalByUHD.size(); m++) {
				if (modelSpecByUHD.get(modelTotalByUHD.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelTotalByUHD.get(m));
					modelSpecByUHD.put(modelTotalByUHD.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = modelSpecByUHD
							.get(modelTotalByUHD.get(m).get("shop_id").toString());
					modelList.add(modelTotalByUHD.get(m));
				}

			}

			List<HashMap<String, Object>> modelTotalByCURVE = excelService.selectModelTotalBySpec("%CURVE%", beginDate,
					endDate, searchStr, conditions, WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> modelSpecByCURVE = new HashMap<String, ArrayList<HashMap<String, Object>>>();

			for (int m = 0; m < modelTotalByCURVE.size(); m++) {
				if (modelSpecByCURVE.get(modelTotalByCURVE.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelTotalByCURVE.get(m));
					modelSpecByCURVE.put(modelTotalByCURVE.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = modelSpecByCURVE
							.get(modelTotalByCURVE.get(m).get("shop_id").toString());
					modelList.add(modelTotalByCURVE.get(m));
				}

			}

			List<HashMap<String, Object>> modelTotalByspec = excelService.selectModelBySpecTotal(beginDate, endDate,
					searchStr, conditions, WebPageUtil.isHQRole());

			HashMap<String, ArrayList<HashMap<String, Object>>> modelSpecByTotal = new HashMap<String, ArrayList<HashMap<String, Object>>>();

			for (int m = 0; m < modelTotalByspec.size(); m++) {
				if (modelSpecByTotal.get(modelTotalByspec.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelTotalByspec.get(m));
					modelSpecByTotal.put(modelTotalByspec.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = modelSpecByTotal
							.get(modelTotalByspec.get(m).get("shop_id").toString());
					modelList.add(modelTotalByspec.get(m));
				}

			}

			List<HashMap<String, Object>> modelTotal = excelService.selectModelTotal(beginDate, endDate, searchStr,
					conditions, WebPageUtil.isHQRole());
			HashMap<String, ArrayList<HashMap<String, Object>>> modelTotalMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();

			for (int m = 0; m < modelTotal.size(); m++) {
				if (modelTotalMap.get(modelTotal.get(m).get("shop_id").toString()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(modelTotal.get(m));
					modelTotalMap.put(modelTotal.get(m).get("shop_id").toString(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = modelTotalMap
							.get(modelTotal.get(m).get("shop_id").toString());
					modelList.add(modelTotal.get(m));
				}

			}

			// 用于放置表格数据
			LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
			// 查询门店所有数据
			excels = new ArrayList<Excel>(excelService.selectDatas(searchStr, conditions));
			int rowOne = 7;
			for (Excel excel : excels) {

				// 用于放置表格数据
				HashMap<String, Object> dataMap = new HashMap<String, Object>();

				String shop_id = excel.getShopId();
				dataMap.put("SELL-OUT INFORMATION SHEET_REG.", excel.getReg());
				dataMap.put("SELL-OUT INFORMATION SHEET_TYPE", "TV");
				dataMap.put("SELL-OUT INFORMATION SHEET_NO OF SHOP", 1);
				dataMap.put("SELL-OUT INFORMATION SHEET_DEALER", excel.getDealer());
				dataMap.put("SELL-OUT INFORMATION SHEET_STORE", excel.getShopName());
				dataMap.put("TV FPS", 1);
				dataMap.put("AC FPS", "");

				// AS SALESMAN 0
				// AS PROM_NAME 1
				// AS ACFO 2

				String sale = "";
				String pro = "";
				String acfo = "";
				if (salerDatas.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = salerDatas.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {
						if (modelList.get(i).get("salerType").equals(0)
								|| modelList.get(i).get("salerType").equals("0")) {
							sale += modelList.get(i).get("userId") + ",";

						} else if (modelList.get(i).get("salerType").equals(1)
								|| modelList.get(i).get("salerType").equals("1")) {
							pro += modelList.get(i).get("userId") + ",";

						} else if (modelList.get(i).get("salerType").equals(2)
								|| modelList.get(i).get("salerType").equals("2")) {
							acfo += modelList.get(i).get("userId") + ",";
						}

					}

				}

				if (sale.length() > 0) {
					dataMap.put("SALESMAN", sale.substring(0, sale.length() - 1));
				}
				if (pro.length() > 0) {
					dataMap.put("PROMODISER NAME", pro.substring(0, pro.length() - 1));
				}
				if (acfo.length() > 0) {
					dataMap.put("ACFO", acfo.substring(0, acfo.length() - 1));
				}

				dataMap.put("DATE OF HIRED", excel.getDateOfHired());
				dataMap.put("SHOP ID", excel.getShopId());
				dataMap.put("AREA", excel.getArea());
				dataMap.put("AGENCY", "AGENCY");
				dataMap.put("SHOP CLASS", excel.getLevel());

				if (saleMap.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = saleMap.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {

						BigDecimal bd = new BigDecimal(modelList.get(i).get("saleQty").toString());
						String am = bd.toPlainString();
						dataMap.put("TV SELL-OUT and TARGET_TTL TV SO_QTY", am);
						bd = new BigDecimal(modelList.get(i).get("saleSum").toString());
						am = bd.toPlainString();
						//dataMap.put("TV SELL-OUT and TARGET_TTL TV SO_ASP", am);
					}

				}

				Double saleSum = 0.0;

				if (targetMap.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = targetMap.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {
						BigDecimal bd = new BigDecimal(modelList.get(i).get("targetSum").toString());
						String am = bd.toPlainString();
						dataMap.put("TV SELL-OUT and TARGET_BASIC TARGET", am);
						bd = new BigDecimal(modelList.get(i).get("challengeSum").toString());
						am = bd.toPlainString();
						dataMap.put("TV SELL-OUT and TARGET_CHALLENGE TARGET", am);
					}

				}

				rowOne++;

				dataMap.put("TV SELL-OUT and TARGET_Ach.", "TEXT(P" + rowOne + "/Q" + rowOne + ",\"0.00%\")");

				dataMap.put("TV SELL-OUT and TARGET_CHALLENGE Ach.", "TEXT(P" + rowOne + "/S" + rowOne + ",\"0.00%\")");

				// 根据门店取得对应机型销售数据
				if (shopMap.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelList = shopMap.get(shop_id);
					for (int i = 0; i < modelList.size(); i++) {
						if (priceMap.get(modelList.get(i).get("model")) != null) {
							ArrayList<HashMap<String, Object>> priceList = priceMap.get(modelList.get(i).get("model"));
							for (int j = 0; j < priceList.size(); j++) {
								BigDecimal bd = new BigDecimal(priceList.get(j).get("price").toString());
								String am = bd.toPlainString();
								double shop = Double.parseDouble(am);
								double price = shop / Integer.parseInt(priceList.get(j).get("shop").toString());

								long lnum = Math.round(price);

								String key = "" + "_" + modelList.get(i).get("model") + "_" + lnum + "_" + "sold";

								bd = new BigDecimal(modelList.get(i).get("quantity").toString());

								dataMap.put(key, bd.longValue());
							}

						}

					}

				}

				if (shopMapByDIGITAL.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelListByDIGITAL = shopMapByDIGITAL.get(shop_id);
					for (int i = 0; i < modelListByDIGITAL.size(); i++) {
						if (DIGITALMap.get(modelListByDIGITAL.get(i).get("model")) != null) {
							ArrayList<HashMap<String, Object>> priceList = DIGITALMap
									.get(modelListByDIGITAL.get(i).get("model"));
							for (int j = 0; j < priceList.size(); j++) {
								BigDecimal bd = new BigDecimal(priceList.get(j).get("price").toString());
								String am = bd.toPlainString();
								double shop = Double.parseDouble(am);
								double price = shop / Integer.parseInt(priceList.get(j).get("shop").toString());

								long lnum = Math.round(price);

								String key = "DIGITAL BASIC" + "_" + modelListByDIGITAL.get(i).get("model") + "_" + lnum
										+ "_" + "sold";

								bd = new BigDecimal(modelListByDIGITAL.get(i).get("quantity").toString());

								dataMap.put(key, bd.longValue());
							}

						}

					}

				}

				if (shopMapByINTERNET.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelListByINTERNET = shopMapByINTERNET.get(shop_id);
					for (int i = 0; i < modelListByINTERNET.size(); i++) {
						if (INTERNETMap.get(modelListByINTERNET.get(i).get("model")) != null) {
							ArrayList<HashMap<String, Object>> priceList = INTERNETMap
									.get(modelListByINTERNET.get(i).get("model"));
							for (int j = 0; j < priceList.size(); j++) {
								BigDecimal bd = new BigDecimal(priceList.get(j).get("price").toString());
								String am = bd.toPlainString();
								double shop = Double.parseDouble(am);
								double price = shop / Integer.parseInt(priceList.get(j).get("shop").toString());

								long lnum = Math.round(price);

								String key = "DIGITAL INTERNET TV" + "_" + modelListByINTERNET.get(i).get("model") + "_"
										+ lnum + "_" + "sold";

								bd = new BigDecimal(modelListByINTERNET.get(i).get("quantity").toString());

								dataMap.put(key, bd.longValue());
							}

						}

					}
				}

				if (shopMapByQUHD.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelListByQUHD = shopMapByQUHD.get(shop_id);
					for (int i = 0; i < modelListByQUHD.size(); i++) {
						if (QUHDMap.get(modelListByQUHD.get(i).get("model")) != null) {
							ArrayList<HashMap<String, Object>> priceList = QUHDMap
									.get(modelListByQUHD.get(i).get("model"));
							for (int j = 0; j < priceList.size(); j++) {
								BigDecimal bd = new BigDecimal(priceList.get(j).get("price").toString());
								String am = bd.toPlainString();
								double shop = Double.parseDouble(am);
								double price = shop / Integer.parseInt(priceList.get(j).get("shop").toString());

								long lnum = Math.round(price);

								String key = "(QUHD) TV" + "_" + modelListByQUHD.get(i).get("model") + "_" + lnum + "_"
										+ "sold";

								bd = new BigDecimal(modelListByQUHD.get(i).get("quantity").toString());

								dataMap.put(key, bd.longValue());
							}

						}

					}
				}

				if (shopMapBySMART.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelListBySMART = shopMapBySMART.get(shop_id);
					for (int i = 0; i < modelListBySMART.size(); i++) {
						if (SMARTMap.get(modelListBySMART.get(i).get("model")) != null) {
							ArrayList<HashMap<String, Object>> priceList = SMARTMap
									.get(modelListBySMART.get(i).get("model"));
							for (int j = 0; j < priceList.size(); j++) {
								BigDecimal bd = new BigDecimal(priceList.get(j).get("price").toString());
								String am = bd.toPlainString();
								double shop = Double.parseDouble(am);
								double price = shop / Integer.parseInt(priceList.get(j).get("shop").toString());

								long lnum = Math.round(price);

								String key = "SMART TV" + "_" + modelListBySMART.get(i).get("model") + "_" + lnum + "_"
										+ "sold";

								bd = new BigDecimal(modelListBySMART.get(i).get("quantity").toString());

								dataMap.put(key, bd.longValue());
							}

						}

					}

				}

				if (shopMapByUHD.get(shop_id) != null) {

					ArrayList<HashMap<String, Object>> modelListByUHD = shopMapByUHD.get(shop_id);
					for (int i = 0; i < modelListByUHD.size(); i++) {
						if (UHDMap.get(modelListByUHD.get(i).get("model")) != null) {
							ArrayList<HashMap<String, Object>> priceList = UHDMap
									.get(modelListByUHD.get(i).get("model"));
							for (int j = 0; j < priceList.size(); j++) {
								BigDecimal bd = new BigDecimal(priceList.get(j).get("price").toString());
								String am = bd.toPlainString();
								double shop = Double.parseDouble(am);
								double price = shop / Integer.parseInt(priceList.get(j).get("shop").toString());

								long lnum = Math.round(price);

								String key = "UHD TV" + "_" + modelListByUHD.get(i).get("model") + "_" + lnum + "_"
										+ "sold";

								bd = new BigDecimal(modelListByUHD.get(i).get("quantity").toString());

								dataMap.put(key, bd.longValue());
							}

						}

					}
				}

				/*
				 * DIGITAL BASIC ---基本数字电视 DIGITAL INTERNET TV ---数字网络电视 SMART TV ----智能电视 UHD
				 * ---高清电视 CURVE TV 曲面电视
				 */
				if (shopMapByCURVE.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelListByCURVE = shopMapByCURVE.get(shop_id);
					for (int i = 0; i < modelListByCURVE.size(); i++) {
						if (CURVEMap.get(modelListByCURVE.get(i).get("model")) != null) {
							ArrayList<HashMap<String, Object>> priceList = CURVEMap
									.get(modelListByCURVE.get(i).get("model"));
							for (int j = 0; j < priceList.size(); j++) {
								BigDecimal bd = new BigDecimal(priceList.get(j).get("price").toString());
								String am = bd.toPlainString();
								double shop = Double.parseDouble(am);
								double price = shop / Integer.parseInt(priceList.get(j)

										.get("shop").toString());

								long lnum = Math.round(price);

								String key = "CURVE TV" + "_" + modelListByCURVE.get(i).get("model") + "_" + lnum + "_"
										+ "sold";

								bd = new BigDecimal(modelListByCURVE.get(i).get("quantity").toString());

								dataMap.put(key, bd.longValue());
							}

						}

					}

				}

				if (stockMapByDIGITAL.get(shop_id) != null) {

					ArrayList<HashMap<String, Object>> stockListByDIGITAL = stockMapByDIGITAL.get(shop_id);
					for (int i = 0; i < stockListByDIGITAL.size(); i++) {

						String key = "DIGITAL STOCKS" + "_" + stockListByDIGITAL.get(i).get("model") + "_" + " " + "_"
								+ "Stocks";
						BigDecimal bd = new BigDecimal(stockListByDIGITAL.get(i).get("quantity").toString());

						dataMap.put(key, bd.longValue());
					}

				}

				if (stockMapByINTERNET.get(shop_id) != null) {

					ArrayList<HashMap<String, Object>> modelListByINTERNET = stockMapByINTERNET.get(shop_id);
					for (int i = 0; i < modelListByINTERNET.size(); i++) {

						String key = "INTERNET STOCKS" + "_" + modelListByINTERNET.get(i).get("model") + "_" + " " + "_"
								+ "Stocks";

						BigDecimal bd = new BigDecimal(modelListByINTERNET.get(i).get("quantity").toString());

						dataMap.put(key, bd.longValue());
					}

				}

				if (stockMapByQUHD.get(shop_id) != null) {

					ArrayList<HashMap<String, Object>> modelListByQUHD = stockMapByQUHD.get(shop_id);
					for (int i = 0; i < modelListByQUHD.size(); i++) {

						String key = "(QUHD) STOCKS" + "_" + modelListByQUHD.get(i).get("model") + "_" + " " + "_"
								+ "Stocks";

						BigDecimal bd = new BigDecimal(modelListByQUHD.get(i).get("quantity").toString());

						dataMap.put(key, bd.longValue());
					}

				}

				if (stockMapBySMART.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelListBySMART = stockMapBySMART.get(shop_id);
					for (int i = 0; i < modelListBySMART.size(); i++) {

						String key = "SMART STOCKS" + "_" + modelListBySMART.get(i).get("model") + "_" + " " + "_"
								+ "Stocks";

						BigDecimal bd = new BigDecimal(modelListBySMART.get(i).get("quantity").toString());

						dataMap.put(key, bd.longValue());
					}

				}

				if (stockMapByUHD.get(shop_id) != null) {

					ArrayList<HashMap<String, Object>> modelListByUHD = stockMapByUHD.get(shop_id);
					for (int i = 0; i < modelListByUHD.size(); i++) {

						String key = "UHD STOCKS" + "_" + modelListByUHD.get(i).get("model") + "_" + " " + "_"
								+ "Stocks";

						BigDecimal bd = new BigDecimal(modelListByUHD.get(i).get("quantity").toString());

						dataMap.put(key, bd.longValue());
					}

				}

				/*
				 * DIGITAL BASIC ---基本数字电视 DIGITAL INTERNET TV ---数字网络电视 SMART TV ----智能电视 UHD
				 * ---高清电视 CURVE TV 曲面电视
				 */
				if (stockMapByCURVE.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> modelListByCURVE = stockMapByCURVE.get(shop_id);
					for (int i = 0; i < modelListByCURVE.size(); i++) {

						String key = "CURVE STOCKS" + "_" + modelListByCURVE.get(i).get("model") + "_" + " " + "_"
								+ "Stocks";

						BigDecimal bd = new BigDecimal(modelListByCURVE.get(i).get("quantity").toString());

						dataMap.put(key, bd.longValue());
					}

				}

				if (disMapByDIGITAL.get(shop_id) != null) {

					ArrayList<HashMap<String, Object>> disListByDIGITAL = disMapByDIGITAL.get(shop_id);
					for (int i = 0; i < disListByDIGITAL.size(); i++) {

						String key = "DIGITAL DisPlay" + "_" + disListByDIGITAL.get(i).get("model") + "_" + "" + "_"
								+ "DisPlay";
						BigDecimal bd = new BigDecimal(disListByDIGITAL.get(i).get("quantity").toString());

						dataMap.put(key, bd.longValue());
					}

				}

				if (disMapByINTERNET.get(shop_id) != null) {

					ArrayList<HashMap<String, Object>> disListByINTERNET = disMapByINTERNET.get(shop_id);
					for (int i = 0; i < disListByINTERNET.size(); i++) {

						String key = "INTERNET DisPlay" + "_" + disListByINTERNET.get(i).get("model") + "_" + " " + "_"
								+ "DisPlay";

						BigDecimal bd = new BigDecimal(disListByINTERNET.get(i).get("quantity").toString());

						dataMap.put(key, bd.longValue());
					}

				}

				if (disMapByQUHD.get(shop_id) != null) {

					ArrayList<HashMap<String, Object>> disListByQUHD = disMapByQUHD.get(shop_id);
					for (int i = 0; i < disListByQUHD.size(); i++) {

						String key = "(QUHD) DisPlay" + "_" + disListByQUHD.get(i).get("model") + "_" + " " + "_"
								+ "DisPlay";

						BigDecimal bd = new BigDecimal(disListByQUHD.get(i).get("quantity").toString());

						dataMap.put(key, bd.longValue());
					}

				}

				if (disMapBySMART.get(shop_id) != null) {

					ArrayList<HashMap<String, Object>> disListBySMART = disMapBySMART.get(shop_id);

					for (int j = 0; j < disListBySMART.size(); j++) {
						String key = "SMART DisPlay" + "_" + disListBySMART.get(j).get("model") + "_" + " " + "_"
								+ "DisPlay";

						BigDecimal bd = new BigDecimal(disListBySMART.get(j).get("quantity").toString());

						dataMap.put(key, bd.longValue());
					}

				}

				if (disMapByUHD.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> disListByUHD = disMapByUHD.get(shop_id);
					for (int i = 0; i < disListByUHD.size(); i++) {
						String key = "UHD DisPlay" + "_" + disListByUHD.get(i).get("model") + "_" + " " + "_"
								+ "DisPlay";

						BigDecimal bd = new BigDecimal(disListByUHD.get(i).get("quantity").toString());

						dataMap.put(key, bd.longValue());

					}

				}

				/*
				 * DIGITAL BASIC ---基本数字电视 DIGITAL INTERNET TV ---数字网络电视 SMART TV ----智能电视 UHD
				 * ---高清电视 CURVE TV 曲面电视
				 */
				if (disMapByCURVE.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> disListByCURVE = disMapByCURVE.get(shop_id);
					for (int i = 0; i < disListByCURVE.size(); i++) {

						String key = "CURVE DisPlay" + "_" + disListByCURVE.get(i).get("model") + "_" + " " + "_"
								+ "DisPlay";

						dataMap.put(key, disListByCURVE.get(i).get("quantity"));

					}

				}

				// 查询类型DIGITAL总台数
				if (modelSpecByDIGITAL.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> disListByCURVE = modelSpecByDIGITAL.get(shop_id);
					for (int i = 0; i < disListByCURVE.size(); i++) {

						String qtyKey = "DIGITAL SUB-TOTAL_" + "QTY";
						String sumKey = "DIGITAL SUB-TOTAL_" + "AMOUNT";

						BigDecimal bd = new BigDecimal(disListByCURVE.get(i).get("quantity").toString());

						dataMap.put(qtyKey, bd.longValue());
						bd = new BigDecimal(disListByCURVE.get(i).get("amount").toString());
						String am = bd.toPlainString();
						long lnum = Math.round(bd.doubleValue());
						if (am.contains(".")) {
							dataMap.put(sumKey, lnum);
						} else {
							dataMap.put(sumKey, am);
						}

					}

				}

				// 查询类型INTERNET总台数
				if (modelSpecByINTERNET.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> disListByCURVE = modelSpecByINTERNET.get(shop_id);
					for (int i = 0; i < disListByCURVE.size(); i++) {

						String qtyKey = "INTERNET SUB-TOTAL_" + "QTY";
						String sumKey = "INTERNET SUB-TOTAL_" + "AMOUNT";
						BigDecimal bd = new BigDecimal(disListByCURVE.get(i).get("quantity").toString());

						dataMap.put(qtyKey, bd.longValue());
						bd = new BigDecimal(disListByCURVE.get(i).get("amount").toString());
						String am = bd.toPlainString();
						long lnum = Math.round(bd.doubleValue());
						if (am.contains(".")) {
							dataMap.put(sumKey, lnum);
						} else {
							dataMap.put(sumKey, am);
						}

					}

				}

				// 查询类型QUHD总台数
				if (modelSpecByQUHD.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> disListByCURVE = modelSpecByQUHD.get(shop_id);
					for (int i = 0; i < disListByCURVE.size(); i++) {

						String qtyKey = "(QUHD) SUB-TOTAL_" + "QTY";
						String sumKey = "(QUHD) SUB-TOTAL_" + "AMOUNT";
						BigDecimal bd = new BigDecimal(disListByCURVE.get(i).get("quantity").toString());

						dataMap.put(qtyKey, bd.longValue());
						bd = new BigDecimal(disListByCURVE.get(i).get("amount").toString());
						String am = bd.toPlainString();
						long lnum = Math.round(bd.doubleValue());
						if (am.contains(".")) {
							dataMap.put(sumKey, lnum);
						} else {
							dataMap.put(sumKey, am);
						}

					}

				}

				// 查询类型SMART总台数
				if (modelSpecBySMART.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> disListByCURVE = modelSpecBySMART.get(shop_id);
					for (int i = 0; i < disListByCURVE.size(); i++) {

						String qtyKey = "SMART SUB-TOTAL_" + "QTY";
						String sumKey = "SMART SUB-TOTAL_" + "AMOUNT";

						BigDecimal bd = new BigDecimal(disListByCURVE.get(i).get("quantity").toString());

						dataMap.put(qtyKey, bd.longValue());
						bd = new BigDecimal(disListByCURVE.get(i).get("amount").toString());
						String am = bd.toPlainString();
						long lnum = Math.round(bd.doubleValue());
						if (am.contains(".")) {
							dataMap.put(sumKey, lnum);
						} else {
							dataMap.put(sumKey, am);
						}

					}

				}

				// 查询类型UHD总台数
				if (modelSpecByUHD.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> disListByCURVE = modelSpecByUHD.get(shop_id);
					for (int i = 0; i < disListByCURVE.size(); i++) {

						String qtyKey = "UHD SUB-TOTAL_" + "QTY";
						String sumKey = "UHD SUB-TOTAL_" + "AMOUNT";

						BigDecimal bd = new BigDecimal(disListByCURVE.get(i).get("quantity").toString());

						dataMap.put(qtyKey, bd.longValue());
						bd = new BigDecimal(disListByCURVE.get(i).get("amount").toString());
						String am = bd.toPlainString();
						long lnum = Math.round(bd.doubleValue());
						if (am.contains(".")) {
							dataMap.put(sumKey, lnum);
						} else {
							dataMap.put(sumKey, am);
						}

					}

				}

				// 查询类型CURVE总台数
				if (modelSpecByCURVE.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> disListByCURVE = modelSpecByCURVE.get(shop_id);
					for (int i = 0; i < disListByCURVE.size(); i++) {

						String qtyKey = "CURVE SUB-TOTAL_" + "QTY";
						String sumKey = "CURVE SUB-TOTAL_" + "AMOUNT";
						BigDecimal bd = new BigDecimal(disListByCURVE.get(i).get("quantity").toString());

						dataMap.put(qtyKey, bd.longValue());
						bd = new BigDecimal(disListByCURVE.get(i).get("amount").toString());
						String am = bd.toPlainString();
						long lnum = Math.round(bd.doubleValue());
						if (am.contains(".")) {
							dataMap.put(sumKey, lnum);
						} else {
							dataMap.put(sumKey, am);
						}

					}

				}

				// 查询类型总台数
				if (modelSpecByTotal.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> disListByCURVE = modelSpecByTotal.get(shop_id);
					for (int i = 0; i < disListByCURVE.size(); i++) {

						String qtyKey = "TV SUB-TOTAL_" + "QTY";
						String sumKey = "TV SUB-TOTAL_" + "AMOUNT";

						BigDecimal bd = new BigDecimal(disListByCURVE.get(i).get("quantity").toString());

						dataMap.put(qtyKey, bd.longValue());
						bd = new BigDecimal(disListByCURVE.get(i).get("amount").toString());
						String am = bd.toPlainString();
						long lnum = Math.round(bd.doubleValue());
						if (am.contains(".")) {
							dataMap.put(sumKey, lnum);
						} else {
							dataMap.put(sumKey, am);
						}

					}

				}

				if (modelTotalMap.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> disListByCURVE = modelTotalMap.get(shop_id);
					for (int i = 0; i < disListByCURVE.size(); i++) {

						String qtyKey = "LED SUB-TOTAL_" + "QTY";
						String sumKey = "LED SUB-TOTAL_" + "AMOUNT";

						BigDecimal bd = new BigDecimal(disListByCURVE.get(i).get("quantity").toString());

						dataMap.put(qtyKey, bd.longValue());
						bd = new BigDecimal(disListByCURVE.get(i).get("amount").toString());
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
					ArrayList<HashMap<String, Object>> stockTTL = stockMapByTotal.get(shop_id);
					for (int i = 0; i < stockTTL.size(); i++) {
						String key = "FLAT PANEL TV TTL INVENTORY_Stocks";

						BigDecimal bd = new BigDecimal(stockTTL.get(i).get("quantity").toString());

						dataMap.put(key, bd.longValue());

					}
				}

				if (disPlayMapByTotal.get(shop_id) != null) {
					ArrayList<HashMap<String, Object>> disTTL = disPlayMapByTotal.get(shop_id);
					for (int i = 0; i < disTTL.size(); i++) {
						String key = "FLAT PANEL TV TTL DISPLAY_Display";
						BigDecimal bd = new BigDecimal(disTTL.get(i).get("quantity").toString());
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

			cellStyleDate.setDataFormat(dataFormat.getFormat("yyyy-m-d hh:mm:ss"));// 这个中文有问题yyyy年m月d日
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
			cellStyleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
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
			cellStylePERCENT.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
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
			cell.setCellValue("TCL  BDSC");// 标题
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
			cell.setCellValue("TEXT(P7/S7,\"0.00%\")");
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
			cell.setCellStyle(cellStylePERCENT);
			cell.setCellFormula("TEXT(P7/S7,\"0.00%\")");

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
			String[] line = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
					"S", "T", "U", "V", "W", "X", "Y", "Z" };

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
				cell.setCellValue("SUBTOTAL(9," + big[i] + "8:" + big[i] + size + ")");
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cellStylePERCENT.setDataFormat(df.getFormat("#,##0"));
				cell.setCellStyle(cellStylePERCENT);
				cell.setCellFormula("SUBTOTAL(9," + big[i] + "8:" + big[i] + size + ")");

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
						} else if (i > 13 && i < 20) {
							cell.setCellStyle(cellStylehead);
						} else if (i > 19) {
							cell.setCellStyle(cellStyleYellow);
							if (s[0].contains("SUB-TOTAL") || s[0].contains("TTL")) {
								cell.setCellStyle(cellStyleRED);
							}
						}
						// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
						sheet.addMergedRegion(new CellRangeAddress(2, rows_max + 1, (num), (num)));
						sk = headerTemp;
						cell.setCellValue(sk);

					} else {
						cell = row.createCell((short) (num));
						if (i < 14) {
							cell.setCellStyle(cellStyleWHITE);
						} else if (i > 13 && i < 20) {
							cell.setCellStyle(cellStylehead);
						} else if (i > 19) {
							cell.setCellStyle(cellStyleYellow);
							if (s[0].contains("SUB-TOTAL") || s[0].contains("TTL")) {
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
						if (i < 20 || (i > 19 && k + 2 == 2 && i >= 20 + modelMapList.size())) {
							// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k + 2, (num), (num + cols - 1)));

						}
						if (sk.equals(headerTemp)) {
							sheet.addMergedRegion(new CellRangeAddress(k + 2, k + 2 + rows_max - s.length, num, num));
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
					if (dataMap.get(fields[c]) != null && dataMap.get(fields[c]).toString().length() > 0

					) {

						// 判断data是否为数值型
						isNum = dataMap.get(fields[c]).toString().matches("^(-?\\d+)(\\.\\d+)?$");
						// 判断data是否为整数（小数部分是否为0）
						isInteger = dataMap.get(fields[c]).toString().matches("^[-\\+]?[\\d]*$");
						// 判断data是否为百分数（是否包含“%”）
						isPercent = dataMap.get(fields[c]).toString().contains("%");
						isGongshi = dataMap.get(fields[c]).toString().contains("SUM");
						isGongshiOne = dataMap.get(fields[c]).toString().contains("TEXT");
						// 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
						if (isNum && !isPercent) {
							if (isInteger) {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 数据格式只显示整数
							} else {
								contextstyle.setDataFormat(df.getFormat("#,##0"));// 保留两位小数点
							}
							// 设置单元格格式
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为double类型
							contentCell.setCellValue(Double.parseDouble(dataMap.get(fields[c]).toString()));
						} else if (isGongshi) {

							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contextstyle.setDataFormat(df.getFormat("#,##0"));
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c]).toString());
							int s = dataMap.get(fields[c]).toString().length() * 512;
							sheet.setColumnWidth(c, s);
						} else if (isGongshiOne) {
							contentCell.setCellType(Cell.CELL_TYPE_FORMULA);
							contentCell.setCellStyle(contextstyle);
							contentCell.setCellFormula(dataMap.get(fields[c]).toString());

						} else {
							contentCell.setCellStyle(contextstyle);
							// 设置单元格内容为字符型
							contentCell.setCellValue(dataMap.get(fields[c]).toString());

						}
					} else {
						contentCell.setCellValue("");
					}

					/*
					 * if (dataMap.get(fields[c]) != null &&
					 * dataMap.get(fields[c]).toString().length() > 0) {
					 * cell.setCellValue(dataMap.get(fields[c]).toString()); // 信息 } else {
					 * cell.setCellValue(""); // 信息
					 * 
					 * }
					 */
					// sheet.addMergedRegion(new CellRangeAddress(0, (short)
					// 2, 0,
					// (short)
					// c));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String readBDSCTarget(File file) throws IOException {

		System.out.println("=======read2007ExcelByPK========");
		String countrySale = "";
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
			XSSFRow ro = sheet.getRow(1);
			XSSFCell cells = null;

			List<HashMap<String, Object>> modelList = new LinkedList<HashMap<String, Object>>();
			Set<String> setLine = new HashSet<String>();
			for (int j = 4; j < ro.getLastCellNum(); j++) {
				HashMap<String, Object> hashMap = new HashMap<>();
				cells = ro.getCell(j);
				if (cells == null) {
					continue;
				}

				if (cells != null && cells.getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& getCellValueByCell(cells) != null && !getCellValueByCell(cells).equals("")) {

					int model = excelDao.selectCountByLine(getCellValueByCell(cells));
					if (model >= 1) {
						boolean one = setLine.add(getCellValueByCell(cells));
						if (!one) {
							msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1) + " "
									+ getText("excel.error.lineRE") + "  (" + getCellValueByCell(cells) + ") "
									+ "<br/>");
						}
						hashMap.put("line", getCellValueByCell(cells));

					} else {
						msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1) + " "
								+ getText("excel.error.lineNo") + "  (" + getCellValueByCell(cells) + ") " + "<br/>");
					}

					modelList.add(hashMap);
				}
			}

			System.out.println("========lineList========" + modelList);

			String userCountry = WebPageUtil.getLoginedUser().getPartyId();

			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			List<HashMap<String, Object>> allCountryList = new LinkedList<HashMap<String, Object>>();
			for (int i = 2; i <= sheet.getLastRowNum(); i++) {
				HashMap<String, Object> countryTargetMap = new HashMap<String, Object>();
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				String countryName = "";
				String countryId = "";
				if (row.getCell(0) != null && row.getCell(0).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
					countryName = getCellValueByCell(row.getCell(0));
					countryId = excelDao.selectCountry(countryName);
					if (countryId == null) {
						msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.country") + "("
								+ getCellValueByCell(row.getCell(0)) + ")" + "<br/>");
					} else if (!userCountry.equals("999") && !userCountry.equals(countryId)) {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.userCountry") + "("
								+ getCellValueByCell(row.getCell(0)) + ")" + "<br/>");

					} else {
						countryTargetMap.put("Country", countryId);
					}

				} else {
					if ((row.getCell(1) != null && row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK)) {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.line")
								+ DateUtil.getExcelColumnLabel(1) + " " + getText("excel.error.null") + "<br/>");

					}
				}

				SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				String dt = dfd.format(date);
				Date dt1 = dfd.parse(dt);
				Date dt2;
				String rowDate = "";
				Float rowExchange = (float) 0;

				if (row.getCell(0) != null && row.getCell(0).getCellType() != HSSFCell.CELL_TYPE_BLANK) {

					if (row.getCell(1) != null && row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
						try {
							format.setLenient(false);
							date = format.parse(getCellValueByCell(row.getCell(1)));// 有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);// 有异常要捕获
							dt2 = dfd.parse(newD);
							rowDate = newD;
							countryTargetMap.put("DataDate", rowDate);
						} catch (Exception e) {
							msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(2) + " " + getText("excel.error.date") + "<br/>");
						}

					} else {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(2) + " " + getText("excel.error.dateNo") + "<br/>");
					}

				}

				String type = "";
				if (row.getCell(0) != null && row.getCell(0).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& row.getCell(1) != null && row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
					if (row.getCell(2) != null && row.getCell(2).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
						type = getCellValueByCell(row.getCell(2));

						if (type.equals("TV")) {
							countryTargetMap.put("Type", 1);
							type = 1 + "";
						} else if (type.equals("AC")) {
							countryTargetMap.put("Type", 2);
							type = 2 + "";
						} else {
							msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(3) + " " + getText("excel.error.type") + "<br/>");
						}
					} else {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(3) + " " + getText("excel.error.typeNo") + "<br/>");
					}

				}

				if (row.getCell(0) != null && row.getCell(0).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& row.getCell(1) != null && row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
					if (row.getCell(3) != null && row.getCell(3).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
						switch (row.getCell(3).getCellType()) {

						case HSSFCell.CELL_TYPE_STRING:
							msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(4) + " " + getText("excel.error.num") + "<br/>");
							break;

						case HSSFCell.CELL_TYPE_FORMULA:

							msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(4) + " " + getText("excel.error.num") + "<br/>");

							break;

						case HSSFCell.CELL_TYPE_NUMERIC:

								countryTargetMap.put("targetQty", (int) row.getCell(3).getNumericCellValue());

							break;

						case HSSFCell.CELL_TYPE_ERROR:

							break;

						}
					} else {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(4) + " " + getText("excel.error.totalNull") + "<br/>");

					}
				}
				/*
				 * if (row.getCell(0) != null && row.getCell(0).getCellType() !=
				 * HSSFCell.CELL_TYPE_BLANK && row.getCell(1) != null &&
				 * row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK && row.getCell(4) !=
				 * null && row.getCell(4).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
				 * 
				 * switch (row.getCell(4).getCellType()) {
				 * 
				 * case HSSFCell.CELL_TYPE_STRING: msg.append(getText("excel.error.row") + (i +
				 * 1) + " " + getText("excel.error.cell") + DateUtil.getExcelColumnLabel(5) +
				 * " " + getText("excel.error.num") + "<br/>"); break;
				 * 
				 * case HSSFCell.CELL_TYPE_FORMULA:
				 * 
				 * msg.append(getText("excel.error.row") + (i + 1) + " " +
				 * getText("excel.error.cell") + DateUtil.getExcelColumnLabel(5) + " " +
				 * getText("excel.error.num") + "<br/>");
				 * 
				 * break;
				 * 
				 * case HSSFCell.CELL_TYPE_NUMERIC:
				 * 
				 * if (row.getCell(4).getNumericCellValue() != 0) {
				 * countryTargetMap.put("ChallengeTarget", (int)
				 * row.getCell(4).getNumericCellValue()); }
				 * 
				 * break;
				 * 
				 * case HSSFCell.CELL_TYPE_ERROR:
				 * 
				 * break;
				 * 
				 * }
				 * 
				 * }
				 */

				List<Object> linked = new LinkedList<Object>();
				for (int m = 0; m < modelList.size(); m++) {

					if (row.getCell(m + 4) != null && row.getCell(m + 4).getCellType() != HSSFCell.CELL_TYPE_BLANK

					) {
						switch (row.getCell(m + 4).getCellType()) {

						case HSSFCell.CELL_TYPE_STRING:
							msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(m + 4 + 1) + " " + getText("excel.error.num")
									+ "<br/>");
							break;

						case HSSFCell.CELL_TYPE_FORMULA:

							msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(m + 4 + 1) + " " + getText("excel.error.num")
									+ "<br/>");

							break;

						case HSSFCell.CELL_TYPE_NUMERIC:

								HashMap<String, Object> modelMap = new HashMap<String, Object>();
								modelMap.put("Line", modelList.get(m).get("line"));
								if (countryId != null) {
									countrySale = countryId;
									modelMap.put("Country", countryId == null ? null : countryId);
									modelMap.put("CountryName", countryName);
								}

								if (rowDate != null && !rowDate.equals("")) {

									modelMap.put("DataDate", rowDate);

								}

								if (type != null && !type.equals("")) {

									modelMap.put("Type", type);

								}
								modelMap.put("lineTrager", (int) row.getCell(m + 4).getNumericCellValue());
								allModelList.add(modelMap);


							break;

						case HSSFCell.CELL_TYPE_ERROR:

							break;

						}

					}else {
						HashMap<String, Object> modelMap = new HashMap<String, Object>();
						modelMap.put("Line", modelList.get(m).get("line"));
						if (countryId != null) {
							countrySale = countryId;
							modelMap.put("Country", countryId == null ? null : countryId);
							modelMap.put("CountryName", countryName);
						}

						if (rowDate != null && !rowDate.equals("")) {

							modelMap.put("DataDate", rowDate);

						}

						if (type != null && !type.equals("")) {

							modelMap.put("Type", type);

						}
						modelMap.put("lineTrager", 0);
						allModelList.add(modelMap);
					}

				}
				allCountryList.add(countryTargetMap);
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
					cell = row.getCell(j);
					if (cell == null) {
						continue;
					}
					DecimalFormat df = new DecimalFormat("0");// 格式化 number
																// String 字符
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
					DecimalFormat nf = new DecimalFormat("0");// 格式化数字
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						if ("@".equals(cell.getCellStyle().getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
						} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
							value = nf.format(cell.getNumericCellValue());
						} else {
							value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
						}
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN:
						value = cell.getBooleanCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						value = "";
						break;
					default:
						value = cell.toString();
					}
					if (value == null || "".equals(value)) {
						continue;
					}
					linked.add(value);

				}
				list.add(linked);
			}

			System.out.println("=====size=============" + allModelList.size());
			System.out.println("=====allModelList=============" + allModelList);

			List<BDSCImportExcel> countryTargetList = new ArrayList<BDSCImportExcel>();
			List<BDSCImportExcel> countryLineTargetList = new ArrayList<BDSCImportExcel>();

			List<BDSCImportExcel> deleteCountryTargetList = new ArrayList<BDSCImportExcel>();
			List<BDSCImportExcel> deleteCountryLineTargetList = new ArrayList<BDSCImportExcel>();

			if (msg.length() <= 0) {
				for (int i = 0; i < allModelList.size(); i++) {
					if (

					allModelList.get(i).get("Line") != null && allModelList.get(i).get("Line") != ""
							&& allModelList.get(i).get("Country") != null && allModelList.get(i).get("Country") != ""
							&& allModelList.get(i).get("DataDate") != null && allModelList.get(i).get("DataDate") != ""
							&& allModelList.get(i).get("Type") != null && allModelList.get(i).get("Type") != ""
							&& allModelList.get(i).get("lineTrager") != null
							) {
						BDSCImportExcel excel = new BDSCImportExcel();
						excel.setLine(allModelList.get(i).get("Line").toString());

						excel.setDataDate(allModelList.get(i).get("DataDate").toString());

						excel.setTargetId(allModelList.get(i).get("Country").toString());

						excel.setTargetQty(Integer.parseInt(allModelList.get(i).get("lineTrager").toString()));
						excel.setClassId(6);
						excel.setType(Integer.parseInt(allModelList.get(i).get("Type").toString()));
						excel.setCountry(allModelList.get(i).get("Country").toString());
						countryLineTargetList.add(excel);
						deleteCountryLineTargetList.add(excel);

					}
					// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
				}
				for (int i = 0; i < allCountryList.size(); i++) {
					if (allCountryList.get(i).get("Country") != null && allCountryList.get(i).get("Country") != ""
							&& allCountryList.get(i).get("DataDate") != null
							&& allCountryList.get(i).get("DataDate") != "" && allCountryList.get(i).get("Type") != null
							&& allCountryList.get(i).get("Type") != "" && allCountryList.get(i).get("targetQty") != null
							) {
						BDSCImportExcel excel = new BDSCImportExcel();

						excel.setDataDate(allCountryList.get(i).get("DataDate").toString());

						excel.setTargetId(allCountryList.get(i).get("Country").toString());

						excel.setTargetQty(Integer.parseInt(allCountryList.get(i).get("targetQty").toString()));
						excel.setClassId(1);
						excel.setType(Integer.parseInt(allCountryList.get(i).get("Type").toString()));
						excel.setCountry(allCountryList.get(i).get("Country").toString());

						countryTargetList.add(excel);
						deleteCountryTargetList.add(excel);

					}
					// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
				}
			}

			if (deleteCountryTargetList.size() > 0) {
				int row = excelDao.deleteCountryTarget(deleteCountryTargetList);
				excelDao.saveCountryTarget(countryTargetList);
			}

			if (deleteCountryLineTargetList.size() > 0) {
				int row = excelDao.deleteCountryLineTarget(deleteCountryLineTargetList);
				excelDao.saveCountryTarget(countryLineTargetList);
			}
			if (msg.length() > 0) {
				return msg.toString();
			} else {

				return "";
			}

		} catch (Exception e) {
			e.printStackTrace();
			msg.append(e.getMessage());
			return msg.toString();
		}
	}

	@Override
	public String readExcelByTarget(File file, String fileName, String what) throws IOException {
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		} else if ("xlsx".equals(extension)) {
			if (what.equals("month")) {
				return readBDSCTarget(file);
			} else {
				return readBDSCTargetYear(file);
			}

		} else {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		}
	}

	private static String getCellValueByCell(XSSFCell xssfCell) {
		// 判断是否为null或空串
		if (xssfCell == null || xssfCell.toString().trim().equals("")) {
			return "";
		}
		String cellValue = "";
		int cellType = xssfCell.getCellType();
		if (cellType == HSSFCell.CELL_TYPE_FORMULA) { // 表达式类型
			FormulaEvaluator evaluator = xssfCell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
			evaluator.evaluateFormulaCell(xssfCell);
			CellValue cellValues = evaluator.evaluate(xssfCell);
			Double celldata = cellValues.getNumberValue();
			return celldata + "";
		}

		switch (cellType) {
		case HSSFCell.CELL_TYPE_STRING: // 字符串类型
			cellValue = xssfCell.getStringCellValue().trim();
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN: // 布尔类型
			cellValue = String.valueOf(xssfCell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_NUMERIC: // 数值类型
			if (HSSFDateUtil.isCellDateFormatted(xssfCell)) { // 判断日期类型
				cellValue = format.format(xssfCell.getDateCellValue());
			} else { // 否
				cellValue = new DecimalFormat("#.######").format(xssfCell.getNumericCellValue());
			}
			break;
		default: // 其它类型，取空串吧
			cellValue = "";
			break;
		}
		return cellValue;
	}

	@Override
	public JSONObject selectBDSCTarger(String beginDate, String endDate, String searchStr, String conditions,
			String type) {
		List<HashMap<String, Object>> targetList = excelDao.selectBDSCTarget(beginDate, endDate, searchStr, conditions,
				type);
		HashMap<String, ArrayList<HashMap<String, Object>>> lineMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();

		JSONObject jsonObject = new JSONObject();
		JSONArray countryArray = new JSONArray();
		JSONArray lineArray = new JSONArray();
		JSONArray dataArray = new JSONArray();
		for (int m = 0; m < targetList.size(); m++) {

			if (targetList.get(m).get("class_id").toString().equals("6")) {
				JSONObject dataObject = new JSONObject();
				dataObject.accumulate("country", targetList.get(m).get("country_name"));
				dataObject.accumulate("type", targetList.get(m).get("TYPE"));
				dataObject.accumulate("date", targetList.get(m).get("date"));
				dataObject.accumulate("classId", targetList.get(m).get("class_id"));

				dataObject.accumulate("line", targetList.get(m).get("line"));
				dataObject.accumulate("targetQty", targetList.get(m).get("targetQty"));
				dataArray.add(dataObject);
			}

			if (targetList.get(m).get("class_id").toString().equals("1")) {
				JSONObject jsonCountry = new JSONObject();
				jsonCountry.accumulate("country", targetList.get(m).get("country_name"));
				jsonCountry.accumulate("type", targetList.get(m).get("TYPE"));
				jsonCountry.accumulate("date", targetList.get(m).get("date"));
				jsonCountry.accumulate("targetQty", targetList.get(m).get("targetQty"));
				if (!countryArray.contains(jsonCountry)) {
					countryArray.add(jsonCountry);
				}
			}

			if (targetList.get(m).get("class_id").toString().equals("6")) {
				JSONObject jsonline = new JSONObject();
				jsonline.put("line", targetList.get(m).get("line"));

				if (!lineArray.contains(jsonline)) {
					lineArray.add(jsonline);
				}
			}

		}

		JSONArray array = new JSONArray();
		jsonObject.put("line", JSONArray.fromObject(lineArray));
		jsonObject.put("country", JSONArray.fromObject(countryArray));
		jsonObject.put("data", JSONArray.fromObject(dataArray));
		return jsonObject;
	}

	@Override
	public JSONObject selectBDSCTargerYear(String date, String searchStr, String conditions, String type) {
		List<HashMap<String, Object>> targetList = excelDao.selectBDSCTargetYear(date, searchStr, conditions, type);
		HashMap<String, ArrayList<HashMap<String, Object>>> lineMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();

		JSONObject jsonObject = new JSONObject();
		JSONArray countryArray = new JSONArray();
		JSONArray lineArray = new JSONArray();
		JSONArray dataArray = new JSONArray();
		for (int m = 0; m < targetList.size(); m++) {

			if (targetList.get(m).get("class_id").toString().equals("12")) {
				JSONObject dataObject = new JSONObject();
				dataObject.accumulate("country", targetList.get(m).get("country_name"));
				dataObject.accumulate("type", targetList.get(m).get("TYPE"));
				dataObject.accumulate("date", targetList.get(m).get("date"));
				dataObject.accumulate("classId", targetList.get(m).get("class_id"));
				dataObject.accumulate("targetQty", targetList.get(m).get("targetQty"));
				dataArray.add(dataObject);
			}

			if (targetList.get(m).get("class_id").toString().equals("11")) {
				JSONObject jsonCountry = new JSONObject();
				jsonCountry.accumulate("country", targetList.get(m).get("country_name"));
				jsonCountry.accumulate("type", targetList.get(m).get("TYPE"));
				jsonCountry.accumulate("date", targetList.get(m).get("date"));
				jsonCountry.accumulate("targetQty", targetList.get(m).get("targetQty"));
				if (!countryArray.contains(jsonCountry)) {
					countryArray.add(jsonCountry);
				}
			}

		}

		JSONArray array = new JSONArray();
		jsonObject.put("country", JSONArray.fromObject(countryArray));
		jsonObject.put("data", JSONArray.fromObject(dataArray));
		return jsonObject;
	}

	public String readBDSCTargetYear(File file) throws IOException {

		System.out.println("=======read2007ExcelByPK========");
		String countrySale = "";
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
			XSSFRow ro = sheet.getRow(1);
			XSSFCell cells = null;

			String userCountry = WebPageUtil.getLoginedUser().getPartyId();

			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			List<HashMap<String, Object>> allCountryList = new LinkedList<HashMap<String, Object>>();
			for (int i = 2; i <= sheet.getLastRowNum(); i++) {
				HashMap<String, Object> countryTargetMap = new HashMap<String, Object>();
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				String countryName = "";
				String countryId = "";
				if (row.getCell(0) != null && row.getCell(0).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
					countryName = getCellValueByCell(row.getCell(0));
					countryId = excelDao.selectCountry(countryName);
					if (countryId == null) {
						msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.country") + "("
								+ getCellValueByCell(row.getCell(0)) + ")" + "<br/>");
					} else if (!userCountry.equals("999") && !userCountry.equals(countryId)) {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.userCountry") + "("
								+ getCellValueByCell(row.getCell(0)) + ")" + "<br/>");

					} else {
						countryTargetMap.put("Country", countryId);
					}

				} else {
					if ((row.getCell(1) != null && row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK)) {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.line")
								+ DateUtil.getExcelColumnLabel(1) + " " + getText("excel.error.null") + "<br/>");

					}
				}

				SimpleDateFormat dfd = new SimpleDateFormat("yyyy");// 设置日期格式
				SimpleDateFormat format = new SimpleDateFormat("yyyy");
				Date date = new Date();
				String dt = dfd.format(date);
				Date dt1 = dfd.parse(dt);
				Date dt2;
				String rowDate = "";
				Float rowExchange = (float) 0;

				if (row.getCell(0) != null && row.getCell(0).getCellType() != HSSFCell.CELL_TYPE_BLANK) {

					if (row.getCell(1) != null && row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
						try {
							format.setLenient(false);
							date = format.parse(getCellValueByCell(row.getCell(1)));// 有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);// 有异常要捕获
							dt2 = dfd.parse(newD);
							rowDate = newD;
							countryTargetMap.put("DataDate", rowDate);
						} catch (Exception e) {
							msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(2) + " " + getText("excel.error.dateYear") + "<br/>");
						}

					} else {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(2) + " " + getText("excel.error.dateNo") + "<br/>");
					}

				}

				String type = "";
				if (row.getCell(0) != null && row.getCell(0).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& row.getCell(1) != null && row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
					if (row.getCell(2) != null && row.getCell(2).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
						type = getCellValueByCell(row.getCell(2));

						if (type.equals("TV")) {
							countryTargetMap.put("Type", 1);
							type = 1 + "";
						} else if (type.equals("AC")) {
							countryTargetMap.put("Type", 2);
							type = 2 + "";
						} else {
							msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(3) + " " + getText("excel.error.type") + "<br/>");
						}
					} else {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(3) + " " + getText("excel.error.typeNo") + "<br/>");
					}

				}

				if (row.getCell(0) != null && row.getCell(0).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& row.getCell(1) != null && row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
					if (row.getCell(3) != null && row.getCell(3).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
						switch (row.getCell(3).getCellType()) {

						case HSSFCell.CELL_TYPE_STRING:
							msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(4) + " " + getText("excel.error.num") + "<br/>");
							break;

						case HSSFCell.CELL_TYPE_FORMULA:

							msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(4) + " " + getText("excel.error.num") + "<br/>");

							break;

						case HSSFCell.CELL_TYPE_NUMERIC:

								countryTargetMap.put("targetQty", (int) row.getCell(3).getNumericCellValue());

							break;

						case HSSFCell.CELL_TYPE_ERROR:

							break;

						}
					} else {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(4) + " " + getText("excel.error.totalNull") + "<br/>");

					}

				}

				if (row.getCell(0) != null && row.getCell(0).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& row.getCell(1) != null && row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
					if (row.getCell(4) != null && row.getCell(4).getCellType() != HSSFCell.CELL_TYPE_BLANK) {

						switch (row.getCell(4).getCellType()) {

						case HSSFCell.CELL_TYPE_STRING:
							msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(5) + " " + getText("excel.error.num") + "<br/>");
							break;

						case HSSFCell.CELL_TYPE_FORMULA:

							msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(5) + " " + getText("excel.error.num") + "<br/>");

							break;

						case HSSFCell.CELL_TYPE_NUMERIC:

								countryTargetMap.put("UDTarget", (int) row.getCell(4).getNumericCellValue());

							break;

						case HSSFCell.CELL_TYPE_ERROR:

							break;

						}
					} else {

						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(4) + " " + getText("excel.error.udNull") + "<br/>");
					}

				}

				List<Object> linked = new LinkedList<Object>();

				allCountryList.add(countryTargetMap);
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
					cell = row.getCell(j);
					if (cell == null) {
						continue;
					}
					DecimalFormat df = new DecimalFormat("0");// 格式化 number
																// String 字符
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
					DecimalFormat nf = new DecimalFormat("0");// 格式化数字
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						if ("@".equals(cell.getCellStyle().getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
						} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
							value = nf.format(cell.getNumericCellValue());
						} else {
							value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
						}
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN:
						value = cell.getBooleanCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						value = "";
						break;
					default:
						value = cell.toString();
					}
					if (value == null || "".equals(value)) {
						continue;
					}
					linked.add(value);

				}
				list.add(linked);
			}

			System.out.println("=====size=============" + allModelList.size());
			System.out.println("=====allModelList=============" + allModelList);

			List<BDSCImportExcel> countryTargetList = new ArrayList<BDSCImportExcel>();
			List<BDSCImportExcel> countryUDTargetList = new ArrayList<BDSCImportExcel>();

			List<BDSCImportExcel> deleteCountryTargetList = new ArrayList<BDSCImportExcel>();
			List<BDSCImportExcel> deleteCountryUDTargetList = new ArrayList<BDSCImportExcel>();

			if (msg.length() <= 0) {

				for (int i = 0; i < allCountryList.size(); i++) {
					if (allCountryList.get(i).get("Country") != null && allCountryList.get(i).get("Country") != ""
							&& allCountryList.get(i).get("DataDate") != null
							&& allCountryList.get(i).get("DataDate") != "" && allCountryList.get(i).get("Type") != null

					) {
						if (allCountryList.get(i).get("Type") != "" && allCountryList.get(i).get("targetQty") != null
								) {
							BDSCImportExcel excel = new BDSCImportExcel();

							excel.setDataDate(allCountryList.get(i).get("DataDate").toString());

							excel.setTargetId(allCountryList.get(i).get("Country").toString());

							excel.setTargetQty(Integer.parseInt(allCountryList.get(i).get("targetQty").toString()));
							excel.setClassId(11);
							excel.setType(Integer.parseInt(allCountryList.get(i).get("Type").toString()));
							excel.setCountry(allCountryList.get(i).get("Country").toString());

							countryTargetList.add(excel);
							deleteCountryTargetList.add(excel);
						}

						if (allCountryList.get(i).get("UDTarget") != null
							) {
							BDSCImportExcel excelUD = new BDSCImportExcel();

							excelUD.setDataDate(allCountryList.get(i).get("DataDate").toString());

							excelUD.setTargetId(allCountryList.get(i).get("Country").toString());
							excelUD.setTargetQty(Integer.parseInt(allCountryList.get(i).get("UDTarget").toString()));
							excelUD.setClassId(12);
							excelUD.setType(Integer.parseInt(allCountryList.get(i).get("Type").toString()));
							excelUD.setCountry(allCountryList.get(i).get("Country").toString());

							countryUDTargetList.add(excelUD);
							deleteCountryUDTargetList.add(excelUD);

						}

					}
					// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
				}
			}

			if (deleteCountryTargetList.size() > 0) {
				int row = excelDao.deleteCountryTargetYear(deleteCountryTargetList);
				excelDao.saveCountryTarget(countryTargetList);
			}

			if (deleteCountryUDTargetList.size() > 0) {
				int row = excelDao.deleteCountryUDTargetYear(deleteCountryUDTargetList);
				excelDao.saveCountryTarget(countryUDTargetList);
			}

			if (msg.length() > 0) {
				return msg.toString();
			} else {

				return "";
			}

		} catch (Exception e) {
			e.printStackTrace();
			msg.append(e.getMessage());
			return msg.toString();
		}
	}

	
	public HashMap<String, Object> selectHQDataByYear(Map<String, Object> whereMap) {
		List<HashMap<String, Object>> soDatas = soTableDao.selectCountryDataByHQ(whereMap);
		List<HashMap<String, Object>> hqDatas = null;
		List<HashMap<String, Object>> targetDatas=null;
		
		String year=whereMap.get("beginDate").toString().split("-")[0];
		whereMap.put("year", year);
		List<HashMap<String, Object>> yearTargetDatas = null;
		if( (whereMap.get("what").toString().contains("UD") || whereMap.get("what").toString().contains("XCP"))) {
			yearTargetDatas = soTableDao.selectYearTargetDataByHQUD(whereMap);
			targetDatas = soTableDao.selectTargetDataByHQUD(whereMap);
		}else {
			yearTargetDatas = soTableDao.selectYearTargetDataByHQ(whereMap);
			targetDatas = soTableDao.selectTargetDataByHQ(whereMap);
		}

		 
		if(targetDatas.size()>soDatas.size()) {
			for (int j = 0; j < targetDatas.size(); j++) {
				for (int i = 0; i < soDatas.size(); i++) {
					if(soDatas.get(i).get("country_id").toString().equals(targetDatas.get(j).get("country_id").toString())
							&& soDatas.get(i).get("datadate").toString().equals(targetDatas.get(j).get("datadate").toString())
							){
						targetDatas.get(j).put("saleQty", soDatas.get(i).get("saleQty"));
					}
				}
			}
			hqDatas=targetDatas;
		}else {
			for (int i = 0; i < soDatas.size(); i++) {
				for (int j = 0; j < targetDatas.size(); j++) {
					if(soDatas.get(i).get("country_id").toString().equals(targetDatas.get(j).get("country_id").toString())
							&& soDatas.get(i).get("datadate").toString().equals(targetDatas.get(j).get("datadate").toString())
							){
						soDatas.get(i).put("targetQty", targetDatas.get(j).get("targetQty"));
					}
				}
			}
			hqDatas=soDatas;
		}
		
		
		String country="";
		if(hqDatas.size()>=1){
			country=hqDatas.get(0).get("country").toString();
		}
		BigDecimal saleQtyYear=BigDecimal.ZERO;
		BigDecimal saleQtyQ1=BigDecimal.ZERO;
		BigDecimal saleQtyQ2=BigDecimal.ZERO;
		BigDecimal saleQtyQ3=BigDecimal.ZERO;
		BigDecimal saleQtyQ4=BigDecimal.ZERO;
		
		BigDecimal TargetQtyYear=BigDecimal.ZERO;
		BigDecimal TargetQtyQ1=BigDecimal.ZERO;
		BigDecimal TargetQtyQ2=BigDecimal.ZERO;
		BigDecimal TargetQtyQ3=BigDecimal.ZERO;
		BigDecimal TargetQtyQ4=BigDecimal.ZERO;
		LinkedList<HashMap<String, Object>>  array=new LinkedList<HashMap<String, Object>> ();
		LinkedList<HashMap<String, Object>> dataList=new LinkedList<HashMap<String, Object>>();
		LinkedList<HashMap<String, Object>> arrayData=new LinkedList<HashMap<String, Object>>();
		for (int i = 0; i < hqDatas.size(); i++) {
			HashMap<String, Object> obj=hqDatas.get(i);
			BigDecimal bdQty=new BigDecimal(obj.get("saleQty").toString());
			BigDecimal bdTarget=new BigDecimal(obj.get("targetQty").toString());
			int month =Integer.parseInt(obj.get("datadate").toString().split("-")[1]);
			HashMap<String, Object>   jsonObjectData=new 	HashMap<String, Object> ();
			jsonObjectData.put("country", obj.get("country"));
			jsonObjectData.put("month", month);
			jsonObjectData.put(getMonth(month+"")+ "_Achieved" ,bdQty);
			jsonObjectData.put(getMonth(month+"")+ "_Target" ,bdTarget);
			if(bdTarget.doubleValue()==0.0 || bdTarget.doubleValue()==0) {
				jsonObjectData.put(getMonth(month+"")+ "_Rate%" ,"100%");

			}else {
				jsonObjectData.put(getMonth(month+"")+ "_Rate%" ,Math.round(bdQty.doubleValue()/bdTarget.doubleValue()*100)+"%");

			}
			arrayData.add(jsonObjectData);
		}
		for (int i = 0; i < hqDatas.size(); i++) {
			HashMap<String, Object> obj=hqDatas.get(i);
			BigDecimal bdQty=new BigDecimal(obj.get("saleQty").toString());
			BigDecimal bdTarget=new BigDecimal(obj.get("targetQty").toString());
			int month =Integer.parseInt(obj.get("datadate").toString().split("-")[1]);
			if(WebPageUtil.isStringNullAvaliable(country) && country.equals(obj.get("country").toString())){
				
				saleQtyYear=saleQtyYear.add(bdQty);
				TargetQtyYear=TargetQtyYear.add(bdTarget);
				if(month>=1 && month<=3){
					saleQtyQ1=saleQtyQ1.add(bdQty);
					TargetQtyQ1=TargetQtyQ1.add(bdTarget);
				}else if(month>=4 && month<=6){
					saleQtyQ2=saleQtyQ2.add(bdQty);
					TargetQtyQ2=TargetQtyQ2.add(bdTarget);
				}else if(month>=7 && month<=9){
					saleQtyQ3=saleQtyQ3.add(bdQty);
					TargetQtyQ3=TargetQtyQ3.add(bdTarget);
				}else if(month>=10 && month<=12){
					saleQtyQ4=saleQtyQ4.add(bdQty);
					TargetQtyQ4=TargetQtyQ4.add(bdTarget);
				}
				
				if(hqDatas.size() - i == 1){
					HashMap<String, Object> jsonObject=new HashMap<String, Object>();
					jsonObject.put("center",obj.get("center"));
					jsonObject.put("country", obj.get("country"));
					jsonObject.put(year+" full year_Achieved", Math.round(saleQtyYear.doubleValue()));
					jsonObject.put(year+" full year_Target", Math.round(TargetQtyYear.doubleValue()));
					if(Math.round(TargetQtyYear.doubleValue())==0){
						jsonObject.put(year+" full year_Rate%",100+"%");
						
					}else{
						jsonObject.put(year+" full year_Rate%", Math.round(saleQtyYear.doubleValue()/TargetQtyYear.doubleValue()*100)+"%");

					}
					
					jsonObject.put(year+" Q1_Achieved", Math.round(saleQtyQ1.doubleValue()));
					jsonObject.put(year+" Q1_Target", Math.round(TargetQtyQ1.doubleValue()));

					if(Math.round(TargetQtyQ1.doubleValue())==0){
						jsonObject.put(year+" Q1_Rate%",100+"%");
						
					}else{
						jsonObject.put(year+" Q1_Rate%", Math.round(saleQtyQ1.doubleValue()/TargetQtyQ1.doubleValue()*100)+"%");

					}
					
					
					jsonObject.put(year+" Q2_Achieved", Math.round(saleQtyQ2.doubleValue()));
					jsonObject.put(year+" Q2_Target", Math.round(TargetQtyQ2.doubleValue()));

					if(Math.round(TargetQtyQ2.doubleValue())==0){
						jsonObject.put(year+" Q2_Rate%",100+"%");
						
					}else{
						jsonObject.put(year+" Q2_Rate%", Math.round(saleQtyQ2.doubleValue()/TargetQtyQ2.doubleValue()*100)+"%");

					}
					
					

					jsonObject.put(year+" Q3_Achieved", Math.round(saleQtyQ3.doubleValue()));
					jsonObject.put(year+" Q3_Target", Math.round(TargetQtyQ3.doubleValue()));

					if(Math.round(TargetQtyQ3.doubleValue())==0){
						jsonObject.put(year+" Q3_Rate%",100+"%");
						
					}else{
						jsonObject.put(year+" Q3_Rate%",Math.round(saleQtyQ3.doubleValue()/TargetQtyQ3.doubleValue()*100)+"%");

					}
					
					
					

					jsonObject.put(year+" Q4_Achieved",Math.round(saleQtyQ4.doubleValue()));
					jsonObject.put(year+" Q4_Target", Math.round(TargetQtyQ4.doubleValue()));

					if(Math.round(TargetQtyQ4.doubleValue())==0){
						jsonObject.put(year+" Q4_Rate%",100+"%");
						
					}else{
						jsonObject.put(year+" Q4_Rate%", Math.round(saleQtyQ4.doubleValue()/TargetQtyQ4.doubleValue()*100)+"%");

					}
					
					
					
					array.add(jsonObject);
				}
				
			}else{
					obj=hqDatas.get(i-1);
					HashMap<String, Object> jsonObject=new HashMap<String, Object>();
					jsonObject.put("center",obj.get("center"));
					jsonObject.put("country", obj.get("country"));
					jsonObject.put(year+" full year_Achieved", Math.round(saleQtyYear.doubleValue()));
					jsonObject.put(year+" full year_Target", Math.round(TargetQtyYear.doubleValue()));
					if(Math.round(TargetQtyYear.doubleValue())==0){
						jsonObject.put(year+" full year_Rate%",100+"%");
						
					}else{
						jsonObject.put(year+" full year_Rate%", Math.round(saleQtyYear.doubleValue()/TargetQtyYear.doubleValue()*100)+"%");

					}
					
					jsonObject.put(year+" Q1_Achieved", Math.round(saleQtyQ1.doubleValue()));
					jsonObject.put(year+" Q1_Target", Math.round(TargetQtyQ1.doubleValue()));

					if(Math.round(TargetQtyQ1.doubleValue())==0){
						jsonObject.put(year+" Q1_Rate%",100+"%");
						
					}else{
						jsonObject.put(year+" Q1_Rate%", Math.round(saleQtyQ1.doubleValue()/TargetQtyQ1.doubleValue()*100)+"%");

					}
					
					
					jsonObject.put(year+" Q2_Achieved", Math.round(saleQtyQ2.doubleValue()));
					jsonObject.put(year+" Q2_Target", Math.round(TargetQtyQ2.doubleValue()));

					if(Math.round(TargetQtyQ2.doubleValue())==0){
						jsonObject.put(year+" Q2_Rate%",100+"%");
						
					}else{
						jsonObject.put(year+" Q2_Rate%", Math.round(saleQtyQ2.doubleValue()/TargetQtyQ2.doubleValue()*100)+"%");

					}
					
					

					jsonObject.put(year+" Q3_Achieved", Math.round(saleQtyQ3.doubleValue()));
					jsonObject.put(year+" Q3_Target", Math.round(TargetQtyQ3.doubleValue()));

					if(Math.round(TargetQtyQ3.doubleValue())==0){
						jsonObject.put(year+" Q3_Rate%",100+"%");
						
					}else{
						jsonObject.put(year+" Q3_Rate%",Math.round(saleQtyQ3.doubleValue()/TargetQtyQ3.doubleValue()*100)+"%");

					}
					
					
					

					jsonObject.put(year+" Q4_Achieved",Math.round(saleQtyQ4.doubleValue()));
					jsonObject.put(year+" Q4_Target", Math.round(TargetQtyQ4.doubleValue()));

					if(Math.round(TargetQtyQ4.doubleValue())==0){
						jsonObject.put(year+" Q4_Rate%",100+"%");
						
					}else{
						jsonObject.put(year+" Q4_Rate%", Math.round(saleQtyQ4.doubleValue()/TargetQtyQ4.doubleValue()*100)+"%");

					}
					
					
					
					array.add(jsonObject);
					obj=hqDatas.get(i);
					country =obj.get("country").toString();
					saleQtyYear=BigDecimal.ZERO;
					saleQtyQ1=BigDecimal.ZERO;
					saleQtyQ2=BigDecimal.ZERO;
					saleQtyQ3=BigDecimal.ZERO;
					saleQtyQ4=BigDecimal.ZERO;
					
					TargetQtyYear=BigDecimal.ZERO;
					TargetQtyQ1=BigDecimal.ZERO;
					TargetQtyQ2=BigDecimal.ZERO;
					TargetQtyQ3=BigDecimal.ZERO;
					TargetQtyQ4=BigDecimal.ZERO;
					
					i--;

				
			}
		}
		LinkedList<HashMap<String, Object>> arrayTwo=new LinkedList<HashMap<String, Object>>();
		for (int j = 0; j < array.size(); j++) {
			HashMap<String, Object> object=array.get(j);
			for (int k = 0; k < yearTargetDatas.size(); k++) {
				if(object.get("country").toString().equals(yearTargetDatas.get(k).get("country").toString())) {
					BigDecimal bd=new BigDecimal(yearTargetDatas.get(k).get("qty").toString());
					object.put(year+" full year_Target",Math.round(bd.doubleValue()));
					saleQtyYear=new BigDecimal(object.get(year+" full year_Achieved").toString());
					if(Math.round(bd.doubleValue())==0){
						object.put(year+" full year_Rate%",100+"%");
						
					}else{
						object.put(year+" full year_Rate%", Math.round(saleQtyYear.doubleValue()/bd.doubleValue()*100)+"%");
					}
					
					
				}
			}
			arrayTwo.add(object);
		
		}
		
		HashMap<String, Object> object=new HashMap<String, Object>();
		object.put("data",  arrayTwo);
		object.put("dataMonth",  arrayData);
		
		return object;
	}
	public LinkedList<HashMap<String, Object>>   getList(HashMap<String, Object> whereMap,String yearBegin,String head){

		
		LinkedList<HashMap<String, Object>> dataListEleven  = new LinkedList<HashMap<String, Object>>();
		
	
		HashMap<String, Object> data=selectHQDataByYear(whereMap);
		LinkedList<HashMap<String, Object>> array= (LinkedList<HashMap<String, Object>>) data.get("data");
		LinkedList<HashMap<String, Object>> arrayMonth = (LinkedList<HashMap<String, Object>>) data.get("dataMonth");
		BigDecimal saleQtyYear = BigDecimal.ZERO;
		BigDecimal saleQtyQ1=BigDecimal.ZERO;
		BigDecimal saleQtyQ2=BigDecimal.ZERO;
		BigDecimal saleQtyQ3=BigDecimal.ZERO;
		BigDecimal saleQtyQ4=BigDecimal.ZERO;
		
		BigDecimal TargetQtyYear=BigDecimal.ZERO;
		BigDecimal TargetQtyQ1=BigDecimal.ZERO;
		BigDecimal  TargetQtyQ2=BigDecimal.ZERO;
		BigDecimal  TargetQtyQ3=BigDecimal.ZERO;
		BigDecimal  TargetQtyQ4=BigDecimal.ZERO;
		
		double []  monthSale=new double[12];
		double []  monthTarget=new double[12];
		System.out.println("=====array========="+arrayMonth);
		for (int j = 0; j < array.size(); j++) {
			HashMap<String, Object> object=array.get(j);
			object.put(	head+"_REG.", object.get("center"));
			object.put(	head+"_Country.", object.get("country"));
			TargetQtyYear=TargetQtyYear.add(new BigDecimal(object.get(yearBegin +" full year_Target").toString()));
			saleQtyYear=saleQtyYear.add(new BigDecimal(object.get(yearBegin +" full year_Achieved").toString()));
			saleQtyQ1=saleQtyQ1.add(new BigDecimal(object.get(yearBegin +" Q1_Achieved").toString()));
			saleQtyQ2=saleQtyQ2.add(new BigDecimal(object.get(yearBegin +" Q2_Achieved").toString()));
			saleQtyQ3=saleQtyQ3.add(new BigDecimal(object.get(yearBegin +" Q3_Achieved").toString()));
			saleQtyQ4=saleQtyQ4.add(new BigDecimal(object.get(yearBegin +" Q4_Achieved").toString()));
			
			TargetQtyQ1=TargetQtyQ1.add(new BigDecimal(object.get(yearBegin +" Q1_Target").toString()));
			TargetQtyQ2=TargetQtyQ2.add(new BigDecimal(object.get(yearBegin +" Q2_Target").toString()));
			TargetQtyQ3=TargetQtyQ3.add(new BigDecimal(object.get(yearBegin +" Q3_Target").toString()));
			TargetQtyQ4=TargetQtyQ4.add(new BigDecimal(object.get(yearBegin +" Q4_Target").toString()));
			
			for (int k = 0; k < arrayMonth.size(); k++) {
				BigDecimal bd=BigDecimal.ZERO;
				HashMap<String, Object> objectMonth=arrayMonth.get(k);
				if(object.get("country").equals(objectMonth.get("country"))) {
					int month =Integer.parseInt(objectMonth.get("month").toString());
					object.put(getMonth((month)+"")+ "_Achieved", objectMonth.get(getMonth((month)+"")+ "_Achieved"));
					object.put(getMonth((month)+"")+ "_Target", objectMonth.get(getMonth((month)+"")+ "_Target"));
					object.put(getMonth((month)+"")+ "_Rate%", objectMonth.get(getMonth((month)+"")+ "_Rate%"));
					
					if(objectMonth.get(getMonth((month)+"")+ "_Achieved")!=null) {
						bd=new BigDecimal(objectMonth.get(getMonth((month)+"")+ "_Achieved").toString());
				/*		if(month==1) {
							System.out.println("=当前值==="+bd.doubleValue());
							System.out.println("累计值"+monthSale[month-1]);
							System.out.println("累计当前后值"+(monthSale[month-1]+bd.doubleValue()));
						}
					*/
						monthSale[month-1]+=(bd.doubleValue());
					}
					if(objectMonth.get(getMonth((month)+"")+ "_Target")!=null) {
					bd=new BigDecimal(objectMonth.get(getMonth((month)+"")+ "_Target").toString());
					 monthTarget[month-1]+=(bd.doubleValue());
				}
					
				}
			}
			
			dataListEleven .add(object);
		}
		
		
		HashMap<String, Object>  dataTotalMapEleven =new HashMap<String, Object> ();
		dataTotalMapEleven .put(	head+"_Country.", "BDSC");
		dataTotalMapEleven .put(yearBegin +" full year_Achieved", saleQtyYear);
		dataTotalMapEleven .put(yearBegin +" full year_Target",TargetQtyYear);
		double  ach=0.0;
		if(TargetQtyYear.doubleValue()==0.0) {
			dataTotalMapEleven .put(yearBegin +" full year_Rate%",100+"%");
		}else {
			ach=saleQtyYear.doubleValue()/TargetQtyYear.doubleValue()*100;
			dataTotalMapEleven .put(yearBegin +" full year_Rate%",Math.round(ach)+"%");
		}
		
		
		dataTotalMapEleven .put(yearBegin +" Q1_Target",TargetQtyQ1);
		dataTotalMapEleven .put(yearBegin +" Q1_Achieved",saleQtyQ1);

		if(TargetQtyQ1.doubleValue()==0.0) {
			dataTotalMapEleven .put(yearBegin +" Q1_Rate%",100+"%");
		}else {
			ach=saleQtyQ1.doubleValue()/TargetQtyQ1.doubleValue()*100;
			dataTotalMapEleven .put(yearBegin +" Q1_Rate%",Math.round(ach)+"%");
		}
		
		dataTotalMapEleven .put(yearBegin +" Q2_Target",TargetQtyQ2);
		dataTotalMapEleven .put(yearBegin +" Q2_Achieved",saleQtyQ2);

		if(TargetQtyQ2.doubleValue()==0.0) {
			dataTotalMapEleven .put(yearBegin +" Q2_Rate%",100+"%");
		}else {
			ach=saleQtyQ2.doubleValue()/TargetQtyQ2.doubleValue()*100;
			dataTotalMapEleven .put(yearBegin +" Q2_Rate%",Math.round(ach)+"%");
		}
		
		
		
		
		dataTotalMapEleven .put(yearBegin +" Q3_Target",TargetQtyQ3);
		dataTotalMapEleven .put(yearBegin +" Q3_Achieved",saleQtyQ3);

		if(TargetQtyQ3.doubleValue()==0.0) {
			dataTotalMapEleven .put(yearBegin +" Q3_Rate%",100+"%");
		}else {
			ach=saleQtyQ3.doubleValue()/TargetQtyQ3.doubleValue()*100;
			dataTotalMapEleven .put(yearBegin +" Q3_Rate%",Math.round(ach)+"%");
		}
		
		
		dataTotalMapEleven .put(yearBegin +" Q4_Target",TargetQtyQ4);
		dataTotalMapEleven .put(yearBegin +" Q4_Achieved",saleQtyQ4);

		if(TargetQtyQ4.doubleValue()==0.0) {
			dataTotalMapEleven .put(yearBegin +" Q4_Rate%",100+"%");
		}else {
			ach=saleQtyQ4.doubleValue()/TargetQtyQ4.doubleValue()*100;
			dataTotalMapEleven .put(yearBegin +" Q4_Rate%",Math.round(ach)+"%");
		}
		
		
		for (int i = 0; i < 12; i++) {
			dataTotalMapEleven .put(getMonth((i+1)+"")+ "_Achieved",Math.round(monthSale[i]));
			dataTotalMapEleven .put(getMonth((i+1)+"")+ "_Target",Math.round(monthTarget[i]));
			if(Math.round(monthTarget[i])==0) {
				dataTotalMapEleven .put(getMonth((i+1)+"")+ "_Rate%","100%");
			}else {
				ach=monthSale[i]/monthTarget[i]*100;
				dataTotalMapEleven .put(getMonth((i+1)+"")+ "_Rate%",Math.round(ach)+"%");
			}
		
		}
		dataListEleven .addFirst(dataTotalMapEleven );
		return dataListEleven;
	}

public String [] getHead(String head,String yearBegin) {
	String[] headersTwo = {
			head+"_REG.", 	head+"_Country.", 
			yearBegin +" full year_Target", 
			yearBegin +" full year_Achieved",
			yearBegin +" full year_Rate%",
			yearBegin +" Q1_Target",yearBegin +" Q1_Achieved",yearBegin +" Q1_Rate%",
			yearBegin +" Q2_Target",yearBegin +" Q2_Achieved",yearBegin +" Q2_Rate%",
			yearBegin +" Q3_Target",yearBegin +" Q3_Achieved",yearBegin +" Q3_Rate%",
			yearBegin +" Q4_Target",yearBegin +" Q4_Achieved",yearBegin +" Q4_Rate%"
			 };
	for (int i = 1; i <=12; i++) {
		headersTwo = insert(headersTwo,getMonth(i+"")+ "_Target");
		headersTwo = insert(headersTwo,getMonth(i+"")+ "_Achieved");
		headersTwo = insert(headersTwo,getMonth(i+"")+ "_Rate%");
	}
	

	return headersTwo;
}
public ArrayList getArray(String [] headersTwo) {
	ArrayList columnsTwo = new ArrayList();
	for (int i = 0; i < headersTwo.length; i++) {
		HashMap<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("header", headersTwo[i]);
		columnMap.put("field", headersTwo[i]);
		columnsTwo.add(columnMap);
	}
	return columnsTwo;
}
}
