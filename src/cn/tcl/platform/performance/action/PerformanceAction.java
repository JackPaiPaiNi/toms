package cn.tcl.platform.performance.action;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.BDExcel.service.IBDExcelService;
import cn.tcl.platform.excel.actions.DateUtil;
import cn.tcl.platform.performance.service.IPerformanceService;
import cn.tcl.platform.sale.vo.Sale;

/**
 * 报表导出
 * 
 * @author 陈婕
 * 
 */
public class PerformanceAction extends BaseAction {

	@Autowired
	@Qualifier("performanceService")
	private IPerformanceService performanceService;

	String user;
	String countryId;
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdw = new SimpleDateFormat("E");
	static SimpleDateFormat formatEn = new SimpleDateFormat("MMMM.dd,yyyy", Locale.ENGLISH);

	static SimpleDateFormat mm = new SimpleDateFormat("MMMM", Locale.ENGLISH);
	static SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.ENGLISH);

	public String loadPCTargetPage() {
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return ERROR;
		}
	}

	
	public String loadINPOPage() {
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return ERROR;
		}
	}

	
	
	public void importPCTarget() {
		try {

			String what = request.getParameter("what");
			String errorMsg = performanceService.readExcelByTarget(uploadExcel, uploadExcelFileName, what);

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

	public void loadPCTargetData(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try{
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			String pageStr=request.getParameter("page");
			int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
			String rowStr=request.getParameter("rows");
			int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
			int start = (page-1)*limit;
			
			String searchStr =null;
			String beginDate="";
			String endDate="";
			String date = request.getParameter("date");
			if(!"".equals(date) && date != null){
				beginDate =date + "-01";
				endDate =date+"-31";
			}else {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				String yearStr = calendar.get(Calendar.YEAR) + "";
				int month = calendar.get(Calendar.MONTH) + 1;// 获取月份
				String monthStr = month < 10 ? "0" + month : month + "";
				int dayy = calendar.get(Calendar.DATE);// 获取日
				String dayStr = dayy < 10 ? "0" + dayy : dayy + "";

				if (beginDate == null || beginDate.equals("")) {
					beginDate = yearStr + "-" + monthStr + "-01";
					endDate = yearStr + "-" + monthStr + "-" + dayStr;
				}

			}
			searchStr=" AND t.datadate BETWEEN '"+beginDate+"'  AND '"+endDate+"' ";
		 	

		 	
			String countryName = request.getParameter("countryName");
			if(!"".equals(countryName) && countryName != null){
				searchStr += " and  p.PARTY_NAME like CONCAT('%','"+countryName+"','%')";
			}
			String customerName = request.getParameter("customerName");
			if(!"".equals(customerName) && customerName != null){
				searchStr += " and  ci.customer_name like CONCAT('%','"+customerName+"','%')";
			}
			
			String userName = request.getParameter("userName");
			if(!"".equals(userName) && userName != null){
				searchStr += " and ul.user_name like CONCAT('%','"+userName+"','%')";
			}
			String shopName = request.getParameter("shopName");
			if(!"".equals(shopName) && shopName != null){
				searchStr += " and si.shop_name like CONCAT('%','"+shopName+"','%')";
			}
		
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += " pa.party_id in ("+userPartyIds+")";
				}
				else
				{
					conditions += " 1=2 ";
				}
			}
			else
			{
				conditions += " 1=1 ";
			}
			
			Map<String, Object> map = performanceService.selectPCTarget(start, limit, searchStr, order, sort, conditions);
			int total = (Integer)map.get("total");
			List<Sale> list = (ArrayList<Sale>)map.get("rows");
			JSONArray jsonArray = JSONArray.fromObject(list);
			String rows = jsonArray.toString();
			result.accumulate("rows", rows);
			result.accumulate("total", total);
			result.accumulate("success", true);
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
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
