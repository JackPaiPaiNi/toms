package cn.tcl.platform.BDExcel.action;

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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.processors.JsonBeanProcessor;
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
import cn.tcl.platform.BDExcel.service.IBDExcelService;
import cn.tcl.platform.BDExcel.service.impl.BDDateUtil;
import cn.tcl.platform.excel.actions.ExportExcelAction;

/**
 * 报表导出
 * 
 * @author 陈婕
 * 
 */
public class ExportBDExcelAction extends BaseAction {

	@Autowired
	@Qualifier("BDExcelService")
	private IBDExcelService excelService;

	String user;
	String countryId;
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdw = new SimpleDateFormat("E");
	static SimpleDateFormat formatEn = new SimpleDateFormat("MMMM.dd,yyyy", Locale.ENGLISH);
	static SimpleDateFormat monthEn = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
	static SimpleDateFormat monthCh = new SimpleDateFormat("MM yyyy");

	static SimpleDateFormat mm = new SimpleDateFormat("MMMM", Locale.ENGLISH);
	static SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.ENGLISH);

	public String loadBDSCTargetPage() {
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public void importBDSCTarget() {
		try {

			String what = request.getParameter("what");
			String errorMsg = excelService.readExcelByTarget(uploadExcel, uploadExcelFileName, what);

			if ("".equals(errorMsg)) {
				WebPageUtil.writeBack("success");
			} else {
				WebPageUtil.writeBack(errorMsg);
			}
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			if (null == errorMsg || "".equals(errorMsg)) {
				errorMsg = this.getText("import.error.exist");
			}
			log.error(e.getStackTrace());
			e.printStackTrace();
			WebPageUtil.writeBack(errorMsg);
		}
	}

	/**
	 * 月报 4.7052668
	 * 
	 * @throws Exception
	 */
	public void exportMonthlyByBDSC() throws Exception {
		String searchStr = null;
		String conditions = "";
		String center = "";
		String country = "";
		String region = "";
		String office = "";

		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if (!WebPageUtil.isHAdmin()) {
			if (request.getParameter("center") != null && !request.getParameter("center").equals("")
					|| request.getParameter("country") != null && !request.getParameter("country").equals("")
					|| request.getParameter("region") != null && !request.getParameter("region").equals("")
					|| request.getParameter("office") != null && !request.getParameter("office").equals("")) {

				if (request.getParameter("center") != null && !request.getParameter("center").equals("")) {
					center = request.getParameter("center");
					conditions = "   s.`PARTY_ID` IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='" + center
							+ "')   ";
				}

				if (request.getParameter("country") != null && !request.getParameter("country").equals("")) {
					country = request.getParameter("country");
					conditions = "  pa.country_id= " + country + "  ";
				}
				if (request.getParameter("region") != null && !request.getParameter("region").equals("")) {
					region = request.getParameter("region");
					conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='" + region
							+ "'  OR PARTY_ID='" + region + "'))  ";
				}
				if (request.getParameter("office") != null && !request.getParameter("office").equals("")) {
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

		long time = System.currentTimeMillis();
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		
		SXSSFWorkbook wb = new SXSSFWorkbook();
		/*
		 * if (WebPageUtil.getLoginedUserId() != null) { user =
		 * WebPageUtil.getLoginedUserId(); countryId =
		 * excelService.selectCountryByUser(user); }
		 * 
		 * String partyName = excelService.selectPartyByUser(user);
		 */

		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		LinkedList<HashMap<String, Object>> dateList = new LinkedList<HashMap<String, Object>>();
		List<KeyValueForDate> list = getKeyValueForDate(beginDate, endDate);
		for (KeyValueForDate keyValueForDate : list) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("beginDate", keyValueForDate.getStartDate());
			map.put("endDate", keyValueForDate.getEndDate());
			dateList.add(map);
		}
		String tBeginDate = beginDate;
		String tEndDate = endDate;
		String head=monthEn.format(monthCh.parse(tBeginDate.split("-")[1]+" "+tBeginDate.split("-")[0]));
		//excelService.common("", wb, dateList, "Quarterly Reports", beginDate, endDate, searchStr, conditions);
		excelService.commonByHq(wb, dateList, "Monthly Reports", tBeginDate, tEndDate, searchStr, conditions);// 8.978 S
		excelService.comparativeByHq(wb, "", beginDate, endDate, tBeginDate, tEndDate, searchStr, conditions);// 130.147
																												// S
		// 13.318
		// S
		excelService.monthYTDsellout(wb, beginDate, endDate, searchStr, conditions);// 114.281 S
		System.out.println("耗时：" + (System.currentTimeMillis() - time) + "毫秒");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + "BDSC "+head+" Monthly Reports" + ".xlsx");
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
	public void exportQYCByBDSC() throws Exception {
		String searchStr = null;
		String conditions = "";
		String center = "";
		String country = "";
		String region = "";
		String office = "";

		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if (!WebPageUtil.isHAdmin()) {
			if (request.getParameter("center") != null && !request.getParameter("center").equals("")
					|| request.getParameter("country") != null && !request.getParameter("country").equals("")
					|| request.getParameter("region") != null && !request.getParameter("region").equals("")
					|| request.getParameter("office") != null && !request.getParameter("office").equals("")) {

				if (request.getParameter("center") != null && !request.getParameter("center").equals("")) {
					center = request.getParameter("center");
					conditions = "   s.`PARTY_ID` IN(SELECT  `COUNTRY_ID` FROM  party WHERE PARENT_PARTY_ID='" + center
							+ "')   ";
				}

				if (request.getParameter("country") != null && !request.getParameter("country").equals("")) {
					country = request.getParameter("country");
					conditions = "  pa.country_id= " + country + "  ";
				}
				if (request.getParameter("region") != null && !request.getParameter("region").equals("")) {
					region = request.getParameter("region");
					conditions = "  pa.party_id in ( (SELECT  party_id FROM party WHERE PARENT_PARTY_ID='" + region
							+ "'  OR PARTY_ID='" + region + "'))  ";
				}
				if (request.getParameter("office") != null && !request.getParameter("office").equals("")) {
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
		long time = System.currentTimeMillis();
		String what = request.getParameter("YQC");
		SXSSFWorkbook wb = new SXSSFWorkbook();
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");

		String q = request.getParameter("qua");
		
		response.setContentType("application/vnd.ms-excel");
		if (what.equals("quar")) {
			response.setHeader("Content-Disposition", "attachment; filename=\"" +q+" "+beginDate.split("-")[0]+ " Quarterly Reports" + ".xlsx");
		} else if (what.equals("custom")) {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + "Custom Reports" + ".xlsx");
		} else if (what.equals("year")) {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + beginDate.split("-")[0]+" Annual Reports" + ".xlsx");

		}

		

		LinkedList<HashMap<String, Object>> dataList = BDDateUtil.splitSeason(beginDate, endDate);
		String tBeginDate = beginDate;
		String tEndDate = endDate;
		//excelService.common(what, wb, dataList, "Quarterly Reports", beginDate, endDate, searchStr, conditions);
		// 累计数据报表
		if (what.equals("custom")) {
			excelService.commonByHqQuaCus(what, wb, dataList, "Quarterly Reports", beginDate, endDate, searchStr, conditions);

		}else {
			excelService.commonByHqQua(what, wb, dataList, "Quarterly Reports", beginDate, endDate, searchStr, conditions);

		}

		LinkedList<HashMap<String, Object>> dateList = new LinkedList<HashMap<String, Object>>();
		List<KeyValueForDate> list = getKeyValueForDate(beginDate, endDate);
		for (KeyValueForDate keyValueForDate : list) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("beginDate", keyValueForDate.getStartDate());
			map.put("endDate", keyValueForDate.getEndDate());
			dateList.add(map);
		}
		excelService.comparativeByHqQua(what, wb, "", beginDate, endDate, tBeginDate, tEndDate, searchStr, conditions);// 130.147
																														// S
		// 13.318
		// S
		if (what.equals("custom")) {
			excelService.monthYTDselloutQuaS(what, wb, beginDate, endDate, searchStr, conditions);// 114.281 S
		} else {
			excelService.monthYTDsellout(wb, beginDate, endDate, searchStr, conditions);
		}

		System.out.println("耗时：" + (System.currentTimeMillis() - time) + "毫秒");

		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	public static List<KeyValueForDate> getKeyValueForDate(String startDate, String endDate) {
		List<KeyValueForDate> list = null;
		try {
			list = new ArrayList<KeyValueForDate>();

			String firstDay = "";
			String lastDay = "";
			Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);// 定义起始日期

			Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);// 定义结束日期

			Calendar dd = Calendar.getInstance();// 定义日期实例
			dd.setTime(d1);// 设置日期起始时间
			Calendar cale = Calendar.getInstance();

			Calendar c = Calendar.getInstance();
			c.setTime(d2);

			int startDay = d1.getDate();
			int endDay = d2.getDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			KeyValueForDate keyValueForDate = null;

			while (dd.getTime().before(d2)) {// 判断是否到结束日期
				keyValueForDate = new KeyValueForDate();
				cale.setTime(dd.getTime());

				if (dd.getTime().equals(d1)) {
					cale.set(Calendar.DAY_OF_MONTH, dd.getActualMaximum(Calendar.DAY_OF_MONTH));
					lastDay = sdf.format(cale.getTime());
					keyValueForDate.setStartDate(sdf.format(d1));
					keyValueForDate.setEndDate(lastDay);

				} else if (dd.get(Calendar.MONTH) == d2.getMonth() && dd.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
					cale.set(Calendar.DAY_OF_MONTH, 1);// 取第一天
					firstDay = sdf.format(cale.getTime());

					keyValueForDate.setStartDate(firstDay);
					keyValueForDate.setEndDate(sdf.format(d2));

				} else {
					cale.set(Calendar.DAY_OF_MONTH, 1);// 取第一天
					firstDay = sdf.format(cale.getTime());

					cale.set(Calendar.DAY_OF_MONTH, dd.getActualMaximum(Calendar.DAY_OF_MONTH));
					lastDay = sdf.format(cale.getTime());

					keyValueForDate.setStartDate(firstDay);
					keyValueForDate.setEndDate(lastDay);

				}
				list.add(keyValueForDate);
				dd.add(Calendar.MONTH, 1);// 进行当前日期月份加1

			}

			if (endDay < startDay) {
				keyValueForDate = new KeyValueForDate();

				cale.setTime(d2);
				cale.set(Calendar.DAY_OF_MONTH, 1);// 取第一天
				firstDay = sdf.format(cale.getTime());

				keyValueForDate.setStartDate(firstDay);
				keyValueForDate.setEndDate(sdf.format(d2));
				list.add(keyValueForDate);
			}
		} catch (Exception e) {
			return null;
		}

		return list;
	}

	public void selectBDSCTarget() {
		response.setHeader("Content-Type", "application/json");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String date = request.getParameter("date");
		String type = request.getParameter("type");
		String what = request.getParameter("what");
		String searchStr = null;
		String conditions = "";

		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if (!WebPageUtil.isHAdmin()) {
			conditions = "   pa.party_id in (" + userPartyIds + ")  ";
		} else {
			conditions = "  1=1";
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		String yearStr = calendar.get(Calendar.YEAR) + "";
		int month = calendar.get(Calendar.MONTH) + 1;// 获取月份
		String monthStr = month < 10 ? "0" + month : month + "";
		int dayy = calendar.get(Calendar.DATE);// 获取日
		String dayStr = dayy < 10 ? "0" + dayy : dayy + "";
		JSONObject jsonObjec=null;
		JSONObject jsonObject;
		if(what.equals("month")) {
			if (beginDate == null || beginDate.equals("")) {
				beginDate = yearStr + "-" + monthStr + "-01";
				endDate = yearStr + "-" + monthStr + "-" + dayStr;
		}
			 jsonObject = excelService.selectBDSCTarger(beginDate, endDate, searchStr, conditions,type);
		}else {
			if (date == null || date.equals("")) {
				date = yearStr;
		}
			jsonObject = excelService.selectBDSCTargerYear(date, searchStr, conditions,type);
		}
		
		
		JSONArray array = JSONArray.fromObject(jsonObject);
		System.out.println("========data===========" + array.toString());
		WebPageUtil.writeBack(array.toString());
	}

}

class KeyValueForDate {
	private String startDate;
	private String endDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
