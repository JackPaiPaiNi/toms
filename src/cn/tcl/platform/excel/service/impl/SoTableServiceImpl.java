package cn.tcl.platform.excel.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.excel.actions.DateUtil;
import cn.tcl.platform.excel.dao.ISoTableDao;
import cn.tcl.platform.excel.dao.IStatementTableDao;
import cn.tcl.platform.excel.service.ISoTableService;
import cn.tcl.platform.excel.vo.Excel;

@Service("soTableService")
public class SoTableServiceImpl implements ISoTableService {
	@Autowired
	private ISoTableDao soTableDao;
	
	@Autowired
	private IStatementTableDao statementTableDao;
	
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdw = new SimpleDateFormat("E");
	static SimpleDateFormat formatEn = new SimpleDateFormat("MMMM.dd,yyyy",
			Locale.ENGLISH);

	static SimpleDateFormat mm = new SimpleDateFormat("MMMM", Locale.ENGLISH);
	static SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.ENGLISH);


	@Override
	public JSONObject selectGrandTTL(Map<String, Object> whereMap) {
		JSONObject jsonObject=new JSONObject();
		
		JSONArray totalSoldArray=new JSONArray();
		
		
		BigDecimal  soldQty=BigDecimal.ZERO;
		BigDecimal  soldAmt=BigDecimal.ZERO;
		
		//
		JSONArray  soldArraySum=new JSONArray();
		
		
		
		
		/*whereMap.put("spec", "%BASIC LED%");
		List<HashMap<String, Object>> modelMapListByBASIC = soTableDao.selectModelBySpec(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		JSONArray soldArrayHead=new JSONArray();
		for (int i = 0; i < modelMapListByBASIC.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			BigDecimal bd = new BigDecimal(modelMapListByBASIC.get(i)
					.get("price").toString());
			String am = bd.toPlainString();
			double shop = Double.parseDouble(am);
			double price = shop
					/ Integer.parseInt(modelMapListByBASIC.get(i)
							.get("shop").toString());
			long lnum = Math.round(price);
			jsonObjectSoldHead.accumulate("spec", "BASIC LED");
			jsonObjectSoldHead.accumulate("model", modelMapListByBASIC.get(i).get("model"));
			jsonObjectSoldHead.accumulate("price", lnum);
			soldArrayHead.add(jsonObjectSoldHead);
			bd = new BigDecimal(modelMapListByBASIC.get(i)
					.get("amount").toString());
			
			jsonObjectSoldHead.accumulate("qty",  modelMapListByBASIC.get(i).get("quantity"));
			jsonObjectSoldHead.accumulate("amt",  Math.round(bd.doubleValue()));
			totalSoldArray.add(jsonObjectSoldHead);
		}
		
		
		whereMap.put("spec", "%DIGITAL%");
		List<HashMap<String, Object>> modelMapListByDIGITAL = soTableDao.selectModelBySpec(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < modelMapListByDIGITAL.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			BigDecimal bd = new BigDecimal(modelMapListByDIGITAL.get(i)
					.get("price").toString());
			String am = bd.toPlainString();
			double shop = Double.parseDouble(am);
			double price = shop
					/ Integer.parseInt(modelMapListByDIGITAL.get(i)
							.get("shop").toString());
			long lnum = Math.round(price);
			jsonObjectSoldHead.accumulate("spec", "DIGITAL BASIC");
			jsonObjectSoldHead.accumulate("model", modelMapListByDIGITAL.get(i).get("model"));
			jsonObjectSoldHead.accumulate("price", lnum);
			soldArrayHead.add(jsonObjectSoldHead);
			
			bd = new BigDecimal(modelMapListByDIGITAL.get(i)
					.get("amount").toString());
			
			jsonObjectSoldHead.accumulate("qty",  modelMapListByDIGITAL.get(i).get("quantity"));
			jsonObjectSoldHead.accumulate("amt",  Math.round(bd.doubleValue()));
			 totalSoldArray.add(jsonObjectSoldHead);
			 
		}
		
		
		
		whereMap.put("spec", "%INTERNET%");
		List<HashMap<String, Object>> modelMapListByINTERNET = soTableDao.selectModelBySpec(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < modelMapListByINTERNET.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			BigDecimal bd = new BigDecimal(modelMapListByINTERNET.get(i)
					.get("price").toString());
			String am = bd.toPlainString();
			double shop = Double.parseDouble(am);
			double price = shop
					/ Integer.parseInt(modelMapListByINTERNET.get(i)
							.get("shop").toString());
			long lnum = Math.round(price);
			jsonObjectSoldHead.accumulate("spec", "DIGITAL INTERNET TV");
			jsonObjectSoldHead.accumulate("model", modelMapListByINTERNET.get(i).get("model"));
			jsonObjectSoldHead.accumulate("price", lnum);
			soldArrayHead.add(jsonObjectSoldHead);
			
			bd = new BigDecimal(modelMapListByINTERNET.get(i)
					.get("amount").toString());
			
			jsonObjectSoldHead.accumulate("qty",  modelMapListByINTERNET.get(i).get("quantity"));
			jsonObjectSoldHead.accumulate("amt", Math.round(bd.doubleValue()));
			 totalSoldArray.add(jsonObjectSoldHead);
			 
		}
		
		
		whereMap.put("spec", "%QUHD%");
		List<HashMap<String, Object>> modelMapListByQUHD = soTableDao.selectModelBySpec(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < modelMapListByQUHD.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			BigDecimal bd = new BigDecimal(modelMapListByQUHD.get(i)
					.get("price").toString());
			String am = bd.toPlainString();
			double shop = Double.parseDouble(am);
			double price = shop
					/ Integer.parseInt(modelMapListByQUHD.get(i)
							.get("shop").toString());
			long lnum = Math.round(price);
			jsonObjectSoldHead.accumulate("spec", "QUHD TV");
			jsonObjectSoldHead.accumulate("model", modelMapListByQUHD.get(i).get("model"));
			jsonObjectSoldHead.accumulate("price", lnum);
			soldArrayHead.add(jsonObjectSoldHead);
			
			bd = new BigDecimal(modelMapListByQUHD.get(i)
					.get("amount").toString());
			jsonObjectSoldHead.accumulate("qty",  modelMapListByQUHD.get(i).get("quantity"));
			jsonObjectSoldHead.accumulate("amt",Math.round(bd.doubleValue()));
			 totalSoldArray.add(jsonObjectSoldHead);
		}
		
		
		whereMap.put("spec", "%SMART%");
		List<HashMap<String, Object>> modelMapListBySMART = soTableDao.selectModelBySpec(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < modelMapListBySMART.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			BigDecimal bd = new BigDecimal(modelMapListBySMART.get(i)
					.get("price").toString());
			String am = bd.toPlainString();
			double shop = Double.parseDouble(am);
			double price = shop
					/ Integer.parseInt(modelMapListBySMART.get(i)
							.get("shop").toString());
			long lnum = Math.round(price);
			jsonObjectSoldHead.accumulate("spec", "SMART TV");
			jsonObjectSoldHead.accumulate("model", modelMapListBySMART.get(i).get("model"));
			jsonObjectSoldHead.accumulate("price", lnum);
			soldArrayHead.add(jsonObjectSoldHead);
			
			bd = new BigDecimal(modelMapListBySMART.get(i)
					.get("amount").toString());
			jsonObjectSoldHead.accumulate("qty",  modelMapListBySMART.get(i).get("quantity"));
			jsonObjectSoldHead.accumulate("amt", Math.round(bd.doubleValue()));
			 totalSoldArray.add(jsonObjectSoldHead);
		}
		
		
		whereMap.put("spec", "%UHD%");
		List<HashMap<String, Object>> modelMapListByUHD = soTableDao.selectModelBySpec(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < modelMapListByUHD.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			BigDecimal bd = new BigDecimal(modelMapListByUHD.get(i)
					.get("price").toString());
			String am = bd.toPlainString();
			double shop = Double.parseDouble(am);
			double price = shop
					/ Integer.parseInt(modelMapListByUHD.get(i)
							.get("shop").toString());
			long lnum = Math.round(price);
			jsonObjectSoldHead.accumulate("spec", "UHD TV");
			jsonObjectSoldHead.accumulate("model", modelMapListByUHD.get(i).get("model"));
			jsonObjectSoldHead.accumulate("price", lnum);
			soldArrayHead.add(jsonObjectSoldHead);
			
			bd = new BigDecimal(modelMapListByUHD.get(i)
					.get("amount").toString());
			jsonObjectSoldHead.accumulate("qty",  modelMapListByUHD.get(i).get("quantity"));
			jsonObjectSoldHead.accumulate("amt",  Math.round(bd.doubleValue()));
			 totalSoldArray.add(jsonObjectSoldHead);
		}
		
		
	
		
		
		whereMap.put("spec", "%CURVE%");
		List<HashMap<String, Object>> modelMapListByCURVE = soTableDao.selectModelBySpec(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < modelMapListByCURVE.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			BigDecimal bd = new BigDecimal(modelMapListByCURVE.get(i)
					.get("price").toString());
			String am = bd.toPlainString();
			double shop = Double.parseDouble(am);
			double price = shop
					/ Integer.parseInt(modelMapListByCURVE.get(i)
							.get("shop").toString());
			long lnum = Math.round(price);
			jsonObjectSoldHead.accumulate("spec", "CURVE TV");
			jsonObjectSoldHead.accumulate("model", modelMapListByCURVE.get(i).get("model"));
			jsonObjectSoldHead.accumulate("price", lnum);
			soldArrayHead.add(jsonObjectSoldHead);
			
			bd = new BigDecimal(modelMapListByCURVE.get(i)
					.get("amount").toString());
			jsonObjectSoldHead.accumulate("qty",  modelMapListByCURVE.get(i).get("quantity"));
			jsonObjectSoldHead.accumulate("amt",  Math.round(bd.doubleValue()));
			 totalSoldArray.add(jsonObjectSoldHead);
			 
		}
		
		jsonObject.accumulate("Sold", soldArrayHead);
		
		
		
		jsonObject.accumulate("SoldSum", totalSoldArray);
		Map<String, BigDecimal> mapResultOne = new HashMap<String, BigDecimal>();
		for (int i = 0; i < jsonObject.getJSONArray("SoldSum").size(); i++) {
			JSONObject object= jsonObject.getJSONArray("SoldSum").getJSONObject(i);
			BigDecimal numOne = mapResultOne.get(object.get("spec").toString().replaceAll(" ", "_")+"_qty");
			BigDecimal numTwo = mapResultOne.get(object.get("spec").toString().replaceAll(" ", "_")+"_amt");
			  if (numOne == null) {
				  numOne = BigDecimal.ZERO;
			  }
			  if (numTwo == null) {
				  numTwo = BigDecimal.ZERO;
			  }
			  BigDecimal b=new BigDecimal(object.get("qty").toString());
			  mapResultOne.put(object.get("spec").toString().toString().replaceAll(" ", "_")+"_qty",numOne.add(b)) ;
			  b=new BigDecimal(object.get("amt").toString());
			  mapResultOne.put(object.get("spec").toString().toString().replaceAll(" ", "_")+"_amt",numTwo.add(b));

		}
		
		
		
		
		//sold门店最大总和
				List<HashMap<String, Object>> modelTotalByspec = soTableDao
						.selectModelBySpecTotal(whereMap);
				for (int j = 0; j < modelTotalByspec.size(); j++) {
					JSONObject object=new JSONObject();
					object.accumulate("spec", "soldSum");
					object.accumulate("shop", modelTotalByspec.get(j).get("shop_id"));
					object.accumulate("qty", modelTotalByspec.get(j).get("quantity"));
					BigDecimal bd = new BigDecimal( modelTotalByspec.get(j).get("amount").toString());
					object.accumulate("amt",Math.round(bd.doubleValue()));
					soldArraySum.add(object);
					
					BigDecimal b=new BigDecimal(modelTotalByspec.get(j).get("quantity").toString());
					soldQty=soldQty.add(b);
					
					b=new BigDecimal( modelTotalByspec.get(j).get("amount").toString());
					soldAmt=soldAmt.add(b);
				}
				
				
				
				
				mapResultOne.put("soldQty", soldQty);
				mapResultOne.put("soldAmt", soldAmt);
				*/
				/*BigDecimal stocksQty=BigDecimal.ZERO;
				
				//stocks门店最大总和
				List<HashMap<String, Object>> StockTotalBySpec = soTableDao
						.selectStockByTotal(whereMap);
				for (int j = 0; j < StockTotalBySpec.size(); j++) {
					JSONObject object=new JSONObject();
					object.accumulate("spec", "stocksSum");
					object.accumulate("shop", StockTotalBySpec.get(j).get("shop_id"));
					object.accumulate("qty", StockTotalBySpec.get(j).get("quantity"));
					soldArraySum.add(object);
					
					BigDecimal b=new BigDecimal(StockTotalBySpec.get(j).get("quantity").toString());
					stocksQty=stocksQty.add(b);
				}

				
				mapResultOne.put("stocksQty", stocksQty);
				
				
				BigDecimal displayQty=BigDecimal.ZERO;
				
				List<HashMap<String, Object>> displayTotalBySpec = soTableDao
						.selectDisPlayByTotal(whereMap);

				for (int j = 0; j < displayTotalBySpec.size(); j++) {
					JSONObject object=new JSONObject();
					object.accumulate("spec", "displaySum");
					object.accumulate("shop", displayTotalBySpec.get(j).get("shop_id"));
					object.accumulate("qty", displayTotalBySpec.get(j).get("quantity"));
					soldArraySum.add(object);
					
					BigDecimal b=new BigDecimal(displayTotalBySpec.get(j).get("quantity").toString());
					displayQty=displayQty.add(b);
				}
				mapResultOne.put("displayQty", displayQty);*/
				
			/*	JSONArray soldTotal=new JSONArray();
				soldTotal.add(mapResultOne);
				
				//横向功能，sold，stocks，display合计
				jsonObject.accumulate("specSum", soldTotal);
				
				jsonObject.accumulate("sumTwo", soldArraySum);*/
				
				
		
	/*	whereMap.put("spec", "%BASIC LED%");
		List<HashMap<String, Object>> soldListByBASIC = soTableDao.selectModelListBySpec(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		JSONArray soldArrayData=new JSONArray();
		for (int i = 0; i < soldListByBASIC.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			jsonObjectSoldHead.accumulate("spec", "BASIC LED");
			jsonObjectSoldHead.accumulate("model", soldListByBASIC.get(i).get("model"));
			jsonObjectSoldHead.accumulate("shop",  soldListByBASIC.get(i).get("shop_id"));
			jsonObjectSoldHead.accumulate("qty",  soldListByBASIC.get(i).get("quantity"));
			BigDecimal bd = new BigDecimal( soldListByBASIC.get(i).get("amount").toString());
			
			jsonObjectSoldHead.accumulate("amt",Math.round(bd.doubleValue()));
			soldArrayData.add(jsonObjectSoldHead);
	
		}
		
		
		
		whereMap.put("spec", "%DIGITAL%");
		List<HashMap<String, Object>>  soldListByDIGITAL = soTableDao.selectModelListBySpec(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < soldListByDIGITAL.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			jsonObjectSoldHead.accumulate("spec", "DIGITAL BASIC");
			jsonObjectSoldHead.accumulate("model", soldListByDIGITAL.get(i).get("model"));
			jsonObjectSoldHead.accumulate("shop",  soldListByDIGITAL.get(i).get("shop_id"));
			jsonObjectSoldHead.accumulate("qty",  soldListByDIGITAL.get(i).get("quantity"));
			
			BigDecimal bd = new BigDecimal( soldListByDIGITAL.get(i).get("amount").toString());
			
			jsonObjectSoldHead.accumulate("amt",Math.round(bd.doubleValue()));
			
			soldArrayData.add(jsonObjectSoldHead);
			
			
		}
		
		
		
		whereMap.put("spec", "%INTERNET%");
		List<HashMap<String, Object>> soldListByINTERNET = soTableDao.selectModelListBySpec(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < soldListByINTERNET.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			jsonObjectSoldHead.accumulate("spec", "DIGITAL INTERNET TV");
			jsonObjectSoldHead.accumulate("model", soldListByINTERNET.get(i).get("model"));
			jsonObjectSoldHead.accumulate("shop",  soldListByINTERNET.get(i).get("shop_id"));
			jsonObjectSoldHead.accumulate("qty",  soldListByINTERNET.get(i).get("quantity"));
			
			BigDecimal bd = new BigDecimal( soldListByINTERNET.get(i).get("amount").toString());
			
			jsonObjectSoldHead.accumulate("amt",Math.round(bd.doubleValue()));
			
			soldArrayData.add(jsonObjectSoldHead);
		}
		
		whereMap.put("spec", "%QUHD%");
		List<HashMap<String, Object>> soldListByQUHD = soTableDao.selectModelListBySpec(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < soldListByQUHD.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			jsonObjectSoldHead.accumulate("spec", "QUHD TV");
			jsonObjectSoldHead.accumulate("model", soldListByQUHD.get(i).get("model"));
			jsonObjectSoldHead.accumulate("shop",  soldListByQUHD.get(i).get("shop_id"));
			jsonObjectSoldHead.accumulate("qty",  soldListByQUHD.get(i).get("quantity"));
		
			BigDecimal bd = new BigDecimal( soldListByQUHD.get(i).get("amount").toString());
			jsonObjectSoldHead.accumulate("amt",Math.round(bd.doubleValue()));
			

			soldArrayData.add(jsonObjectSoldHead);
		}
		
		
		
		
		whereMap.put("spec", "%SMART%");
		List<HashMap<String, Object>> soldListBySMART = soTableDao.selectModelListBySpec(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < soldListBySMART.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			jsonObjectSoldHead.accumulate("spec", "SMART TV");
			jsonObjectSoldHead.accumulate("model", soldListBySMART.get(i).get("model"));
			jsonObjectSoldHead.accumulate("shop",  soldListBySMART.get(i).get("shop_id"));
			jsonObjectSoldHead.accumulate("qty",  soldListBySMART.get(i).get("quantity"));
			
			BigDecimal bd = new BigDecimal( soldListBySMART.get(i).get("amount").toString());
			jsonObjectSoldHead.accumulate("amt",Math.round(bd.doubleValue()));
			
			soldArrayData.add(jsonObjectSoldHead);
		}
		
		
		whereMap.put("spec", "%UHD%");
		List<HashMap<String, Object>> soldListByUHD = soTableDao.selectModelListBySpec(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < soldListByUHD.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			jsonObjectSoldHead.accumulate("spec", "UHD TV");
			jsonObjectSoldHead.accumulate("model", soldListByUHD.get(i).get("model"));
			jsonObjectSoldHead.accumulate("shop",  soldListByUHD.get(i).get("shop_id"));
			jsonObjectSoldHead.accumulate("qty",  soldListByUHD.get(i).get("quantity"));
			
			BigDecimal bd = new BigDecimal( soldListByUHD.get(i).get("amount").toString());
			jsonObjectSoldHead.accumulate("amt",Math.round(bd.doubleValue()));
		
			
			soldArrayData.add(jsonObjectSoldHead);
		}
		
		
	
		
		
		whereMap.put("spec", "%CURVE%");
		List<HashMap<String, Object>> soldListByCURVE = soTableDao.selectModelListBySpec(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < soldListByCURVE.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			jsonObjectSoldHead.accumulate("spec", "CURVE TV");
			jsonObjectSoldHead.accumulate("model", soldListByCURVE.get(i).get("model"));
			jsonObjectSoldHead.accumulate("shop",  soldListByCURVE.get(i).get("shop_id"));
			jsonObjectSoldHead.accumulate("qty",  soldListByCURVE.get(i).get("quantity"));
			
			BigDecimal bd = new BigDecimal( soldListByCURVE.get(i).get("amount").toString());
			jsonObjectSoldHead.accumulate("amt",Math.round(bd.doubleValue()));
		
			soldArrayData.add(jsonObjectSoldHead);
		}
		
		
		
		jsonObject.accumulate("soldData",soldArrayData);
		
		*/
		
		/*JSONArray totalStocksArray=new JSONArray();

		whereMap.put("spec", "%BASIC LED%");
		List<HashMap<String, Object>> stocksModelMapListByBASIC = soTableDao.selectStockBymodel(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		JSONArray stocksArrayHead=new JSONArray();
		for (int i = 0; i < stocksModelMapListByBASIC.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			jsonObjectSoldHead.accumulate("spec", "BASIC LED");
			jsonObjectSoldHead.accumulate("model", stocksModelMapListByBASIC.get(i).get("model"));
			stocksArrayHead.add(jsonObjectSoldHead);
			
			
			jsonObjectSoldHead.accumulate("qty",  stocksModelMapListByBASIC.get(i).get("quantity"));
			 totalStocksArray.add(jsonObjectSoldHead);
			 
		}
		
		
		
		whereMap.put("spec", "%DIGITAL%");
		List<HashMap<String, Object>> stocksModelMapListByDIGITAL = soTableDao.selectStockBymodel(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < stocksModelMapListByDIGITAL.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			jsonObjectSoldHead.accumulate("spec", "DIGITAL BASIC");
			jsonObjectSoldHead.accumulate("model", stocksModelMapListByDIGITAL.get(i).get("model"));
			stocksArrayHead.add(jsonObjectSoldHead);
			
			jsonObjectSoldHead.accumulate("qty",  stocksModelMapListByDIGITAL.get(i).get("quantity"));
			totalStocksArray.add(jsonObjectSoldHead);
		}
		
		
		whereMap.put("spec", "%INTERNET%");
		List<HashMap<String, Object>> stocksMapListByINTERNET = soTableDao.selectStockBymodel(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < stocksMapListByINTERNET.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			
			jsonObjectSoldHead.accumulate("spec", "DIGITAL INTERNET TV");
			jsonObjectSoldHead.accumulate("model", stocksMapListByINTERNET.get(i).get("model"));
			stocksArrayHead.add(jsonObjectSoldHead);
			
			jsonObjectSoldHead.accumulate("qty",  stocksMapListByINTERNET.get(i).get("quantity"));
			totalStocksArray.add(jsonObjectSoldHead);
		}
		
		
		whereMap.put("spec", "%QUHD%");
		List<HashMap<String, Object>> stocksMapListByQUHD= soTableDao.selectStockBymodel(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < stocksMapListByQUHD.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			
			jsonObjectSoldHead.accumulate("spec", "QUHD TV");
			jsonObjectSoldHead.accumulate("model", stocksMapListByQUHD.get(i).get("model"));
			stocksArrayHead.add(jsonObjectSoldHead);
			
			jsonObjectSoldHead.accumulate("qty",  stocksMapListByQUHD.get(i).get("quantity"));
			totalStocksArray.add(jsonObjectSoldHead);
		}
		
		
		
		whereMap.put("spec", "%SMART%");
		List<HashMap<String, Object>> stocksMapListBySMART = soTableDao.selectStockBymodel(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < stocksMapListBySMART.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			
			jsonObjectSoldHead.accumulate("spec", "SMART TV");
			jsonObjectSoldHead.accumulate("model", stocksMapListBySMART.get(i).get("model"));
			stocksArrayHead.add(jsonObjectSoldHead);
			
			jsonObjectSoldHead.accumulate("qty",  stocksMapListBySMART.get(i).get("quantity"));
			totalStocksArray.add(jsonObjectSoldHead);
		}
		
		
		whereMap.put("spec", "%UHD%");
		List<HashMap<String, Object>> stocksMapListByUHD = soTableDao.selectStockBymodel(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < stocksMapListByUHD.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			
			jsonObjectSoldHead.accumulate("spec", "UHD TV");
			jsonObjectSoldHead.accumulate("model", stocksMapListByUHD.get(i).get("model"));
			stocksArrayHead.add(jsonObjectSoldHead);
			
			jsonObjectSoldHead.accumulate("qty",  stocksMapListByUHD.get(i).get("quantity"));
			totalStocksArray.add(jsonObjectSoldHead);
		}
		
		
	
		
		
		whereMap.put("spec", "%CURVE%");
		List<HashMap<String, Object>> stocksMapListByCURVE = soTableDao.selectStockBymodel(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < stocksMapListByCURVE.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			
			jsonObjectSoldHead.accumulate("spec", "CURVE TV");
			jsonObjectSoldHead.accumulate("model", stocksMapListByCURVE.get(i).get("model"));
			stocksArrayHead.add(jsonObjectSoldHead);
			
			jsonObjectSoldHead.accumulate("qty",  stocksMapListByCURVE.get(i).get("quantity"));
			totalStocksArray.add(jsonObjectSoldHead);
			
		}
		
		jsonObject.accumulate("Stocks", stocksArrayHead);

		
		JSONObject stocksTTL=new JSONObject();
		stocksTTL.accumulate("stocksQty", stocksQty);
		totalStocksArray.add(stocksTTL);
		
		jsonObject.accumulate("StocksSum", totalStocksArray);
		*/
		
		/*
		whereMap.put("spec", "%BASIC LED%");
		List<HashMap<String, Object>> stocksListByBASIC = soTableDao.selectStockByData(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		JSONArray stocksArrayData=new JSONArray();
		for (int i = 0; i < stocksListByBASIC.size(); i++) {
			JSONObject jsonObjectstocksHead=new JSONObject();
			jsonObjectstocksHead.accumulate("spec", "BASIC LED");
			jsonObjectstocksHead.accumulate("model", stocksListByBASIC.get(i).get("model"));
			jsonObjectstocksHead.accumulate("shop",  stocksListByBASIC.get(i).get("shop_id"));
			jsonObjectstocksHead.accumulate("qty",  stocksListByBASIC.get(i).get("quantity"));
			stocksArrayData.add(jsonObjectstocksHead);
		}
		
		
		
		whereMap.put("spec", "%DIGITAL%");
		List<HashMap<String, Object>>  stocksListByDIGITAL = soTableDao.selectStockByData(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < stocksListByDIGITAL.size(); i++) {
			JSONObject jsonObjectstocksHead=new JSONObject();
			jsonObjectstocksHead.accumulate("spec", "DIGITAL BASIC");
			jsonObjectstocksHead.accumulate("model", stocksListByDIGITAL.get(i).get("model"));
			jsonObjectstocksHead.accumulate("shop",  stocksListByDIGITAL.get(i).get("shop_id"));
			jsonObjectstocksHead.accumulate("qty",  stocksListByDIGITAL.get(i).get("quantity"));
			stocksArrayData.add(jsonObjectstocksHead);
		}
		
		
		
		whereMap.put("spec", "%INTERNET%");
		List<HashMap<String, Object>> stocksListByINTERNET = soTableDao.selectStockByData(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < stocksListByINTERNET.size(); i++) {
			JSONObject jsonObjectstocksHead=new JSONObject();
			jsonObjectstocksHead.accumulate("spec", "DIGITAL INTERNET TV");
			jsonObjectstocksHead.accumulate("model", stocksListByINTERNET.get(i).get("model"));
			jsonObjectstocksHead.accumulate("shop",  stocksListByINTERNET.get(i).get("shop_id"));
			jsonObjectstocksHead.accumulate("qty",  stocksListByINTERNET.get(i).get("quantity"));
			stocksArrayData.add(jsonObjectstocksHead);
		}
		
		

		whereMap.put("spec", "%QUHD%");
		List<HashMap<String, Object>> stocksListByQUHD = soTableDao.selectStockByData(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < stocksListByQUHD.size(); i++) {
			JSONObject jsonObjectstocksHead=new JSONObject();
			jsonObjectstocksHead.accumulate("spec", "QUHD TV");
			jsonObjectstocksHead.accumulate("model", stocksListByQUHD.get(i).get("model"));
			jsonObjectstocksHead.accumulate("shop",  stocksListByQUHD.get(i).get("shop_id"));
			jsonObjectstocksHead.accumulate("qty",  stocksListByQUHD.get(i).get("quantity"));
			stocksArrayData.add(jsonObjectstocksHead);
		}
		
		
		
		
		whereMap.put("spec", "%SMART%");
		List<HashMap<String, Object>> stocksListBySMART = soTableDao.selectStockByData(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < stocksListBySMART.size(); i++) {
			JSONObject jsonObjectstocksHead=new JSONObject();
			jsonObjectstocksHead.accumulate("spec", "SMART TV");
			jsonObjectstocksHead.accumulate("model", stocksListBySMART.get(i).get("model"));
			jsonObjectstocksHead.accumulate("shop",  stocksListBySMART.get(i).get("shop_id"));
			jsonObjectstocksHead.accumulate("qty",  stocksListBySMART.get(i).get("quantity"));
			stocksArrayData.add(jsonObjectstocksHead);
		}
		
		
		whereMap.put("spec", "%UHD%");
		List<HashMap<String, Object>> stocksListByUHD = soTableDao.selectStockByData(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < stocksListByUHD.size(); i++) {
			JSONObject jsonObjectstocksHead=new JSONObject();
			jsonObjectstocksHead.accumulate("spec", "UHD TV");
			jsonObjectstocksHead.accumulate("model", stocksListByUHD.get(i).get("model"));
			jsonObjectstocksHead.accumulate("shop",  stocksListByUHD.get(i).get("shop_id"));
			jsonObjectstocksHead.accumulate("qty",  stocksListByUHD.get(i).get("quantity"));
			stocksArrayData.add(jsonObjectstocksHead);
		}
		
		
	
		
		
		whereMap.put("spec", "%CURVE%");
		List<HashMap<String, Object>> stocksListByCURVE = soTableDao.selectStockByData(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < stocksListByCURVE.size(); i++) {
			JSONObject jsonObjectstocksHead=new JSONObject();
			jsonObjectstocksHead.accumulate("spec", "CURVE TV");
			jsonObjectstocksHead.accumulate("model", stocksListByCURVE.get(i).get("model"));
			jsonObjectstocksHead.accumulate("shop",  stocksListByCURVE.get(i).get("shop_id"));
			jsonObjectstocksHead.accumulate("qty",  stocksListByCURVE.get(i).get("quantity"));
			stocksArrayData.add(jsonObjectstocksHead);
		}
		
		
		
		jsonObject.accumulate("stocksData",stocksArrayData);
		
		
		
		
		
		JSONArray totalDisplayArray=new JSONArray();

		whereMap.put("spec", "%BASIC LED%");
		List<HashMap<String, Object>> displayModelMapListByBASIC = soTableDao.selectDisplayBymodel(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		JSONArray displayArrayHead=new JSONArray();
		for (int i = 0; i < displayModelMapListByBASIC.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			jsonObjectSoldHead.accumulate("spec", "BASIC LED");
			jsonObjectSoldHead.accumulate("model", displayModelMapListByBASIC.get(i).get("model"));
			displayArrayHead.add(jsonObjectSoldHead);
			
			jsonObjectSoldHead.accumulate("qty", displayModelMapListByBASIC.get(i).get("quantity"));
			totalDisplayArray.add(jsonObjectSoldHead);
		}
		
		
		
		whereMap.put("spec", "%DIGITAL%");
		List<HashMap<String, Object>> displayModelMapListByDIGITAL = soTableDao.selectDisplayBymodel(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < displayModelMapListByDIGITAL.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			jsonObjectSoldHead.accumulate("spec", "DIGITAL BASIC");
			jsonObjectSoldHead.accumulate("model", displayModelMapListByDIGITAL.get(i).get("model"));
			displayArrayHead.add(jsonObjectSoldHead);
			
			jsonObjectSoldHead.accumulate("qty", displayModelMapListByDIGITAL.get(i).get("quantity"));
			totalDisplayArray.add(jsonObjectSoldHead);
		}
		
		
		whereMap.put("spec", "%INTERNET%");
		List<HashMap<String, Object>> displayMapListByINTERNET = soTableDao.selectDisplayBymodel(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < displayMapListByINTERNET.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			
			jsonObjectSoldHead.accumulate("spec", "DIGITAL INTERNET TV");
			jsonObjectSoldHead.accumulate("model", displayMapListByINTERNET.get(i).get("model"));
			displayArrayHead.add(jsonObjectSoldHead);
			
			jsonObjectSoldHead.accumulate("qty", displayMapListByINTERNET.get(i).get("quantity"));
			totalDisplayArray.add(jsonObjectSoldHead);
		}
		
		
		whereMap.put("spec", "%QUHD%");
		List<HashMap<String, Object>> displayMapListByQUHD = soTableDao.selectDisplayBymodel(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < displayMapListByQUHD.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			
			jsonObjectSoldHead.accumulate("spec", "QUHD TV");
			jsonObjectSoldHead.accumulate("model", displayMapListByQUHD.get(i).get("model"));
			displayArrayHead.add(jsonObjectSoldHead);
			
			jsonObjectSoldHead.accumulate("qty", displayMapListByQUHD.get(i).get("quantity"));
			totalDisplayArray.add(jsonObjectSoldHead);
		}
		
		
		
		
		whereMap.put("spec", "%SMART%");
		List<HashMap<String, Object>> displayMapListBySMART = soTableDao.selectDisplayBymodel(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < displayMapListBySMART.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			
			jsonObjectSoldHead.accumulate("spec", "SMART TV");
			jsonObjectSoldHead.accumulate("model", displayMapListBySMART.get(i).get("model"));
			displayArrayHead.add(jsonObjectSoldHead);
			
			jsonObjectSoldHead.accumulate("qty", displayMapListBySMART.get(i).get("quantity"));
			totalDisplayArray.add(jsonObjectSoldHead);
		}
		
		
		whereMap.put("spec", "%UHD%");
		List<HashMap<String, Object>> displayMapListByUHD = soTableDao.selectDisplayBymodel(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < displayMapListByUHD.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			
			jsonObjectSoldHead.accumulate("spec", "UHD TV");
			jsonObjectSoldHead.accumulate("model", displayMapListByUHD.get(i).get("model"));
			displayArrayHead.add(jsonObjectSoldHead);
			
			jsonObjectSoldHead.accumulate("qty", displayMapListByUHD.get(i).get("quantity"));
			totalDisplayArray.add(jsonObjectSoldHead);
		}
		
		
	
		
		
		whereMap.put("spec", "%CURVE%");
		List<HashMap<String, Object>> displayMapListByCURVE = soTableDao.selectDisplayBymodel(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < displayMapListByCURVE.size(); i++) {
			JSONObject jsonObjectSoldHead=new JSONObject();
			
			jsonObjectSoldHead.accumulate("spec", "CURVE TV");
			jsonObjectSoldHead.accumulate("model", displayMapListByCURVE.get(i).get("model"));
			displayArrayHead.add(jsonObjectSoldHead);
			
			jsonObjectSoldHead.accumulate("qty", displayMapListByCURVE.get(i).get("quantity"));
			totalDisplayArray.add(jsonObjectSoldHead);
		}
		
		jsonObject.accumulate("Display", displayArrayHead);
		
		JSONObject displayTTL=new JSONObject();
		displayTTL.accumulate("displayQty", displayQty);
		totalDisplayArray.add(displayTTL);
		
		
		
		jsonObject.accumulate("DisplaySum", totalDisplayArray);
		*/
		
		/*
		
		whereMap.put("spec", "%BASIC LED%");
		List<HashMap<String, Object>> displayListByBASIC = soTableDao.selectDisplayByData(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		JSONArray displayArrayData=new JSONArray();
		for (int i = 0; i < displayListByBASIC.size(); i++) {
			JSONObject jsonObjectdisplayHead=new JSONObject();
			jsonObjectdisplayHead.accumulate("spec", "BASIC LED");
			jsonObjectdisplayHead.accumulate("model", displayListByBASIC.get(i).get("model"));
			jsonObjectdisplayHead.accumulate("shop",  displayListByBASIC.get(i).get("shop_id"));
			jsonObjectdisplayHead.accumulate("qty",  displayListByBASIC.get(i).get("quantity"));
			displayArrayData.add(jsonObjectdisplayHead);
		}
		
		
		
		whereMap.put("spec", "%DIGITAL%");
		List<HashMap<String, Object>>  displayListByDIGITAL = soTableDao.selectDisplayByData(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < displayListByDIGITAL.size(); i++) {
			JSONObject jsonObjectdisplayHead=new JSONObject();
			jsonObjectdisplayHead.accumulate("spec", "DIGITAL BASIC");
			jsonObjectdisplayHead.accumulate("model", displayListByDIGITAL.get(i).get("model"));
			jsonObjectdisplayHead.accumulate("shop",  displayListByDIGITAL.get(i).get("shop_id"));
			jsonObjectdisplayHead.accumulate("qty",  displayListByDIGITAL.get(i).get("quantity"));
			displayArrayData.add(jsonObjectdisplayHead);
		}
		
		
		
		whereMap.put("spec", "%INTERNET%");
		List<HashMap<String, Object>> displayListByINTERNET = soTableDao.selectDisplayByData(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < displayListByINTERNET.size(); i++) {
			JSONObject jsonObjectdisplayHead=new JSONObject();
			jsonObjectdisplayHead.accumulate("spec", "DIGITAL INTERNET TV");
			jsonObjectdisplayHead.accumulate("model", displayListByINTERNET.get(i).get("model"));
			jsonObjectdisplayHead.accumulate("shop",  displayListByINTERNET.get(i).get("shop_id"));
			jsonObjectdisplayHead.accumulate("qty",  displayListByINTERNET.get(i).get("quantity"));
			displayArrayData.add(jsonObjectdisplayHead);
		}
		
		whereMap.put("spec", "%QUHD%");
		List<HashMap<String, Object>> displayListByQUHD = soTableDao.selectDisplayByData(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < displayListByQUHD.size(); i++) {
			JSONObject jsonObjectdisplayHead=new JSONObject();
			jsonObjectdisplayHead.accumulate("spec", "QUHD TV");
			jsonObjectdisplayHead.accumulate("model", displayListByQUHD.get(i).get("model"));
			jsonObjectdisplayHead.accumulate("shop",  displayListByQUHD.get(i).get("shop_id"));
			jsonObjectdisplayHead.accumulate("qty",  displayListByQUHD.get(i).get("quantity"));
			displayArrayData.add(jsonObjectdisplayHead);
		}
		
		
		
		whereMap.put("spec", "%SMART%");
		List<HashMap<String, Object>> displayListBySMART = soTableDao.selectDisplayByData(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < displayListBySMART.size(); i++) {
			JSONObject jsonObjectdisplayHead=new JSONObject();
			jsonObjectdisplayHead.accumulate("spec", "SMART TV");
			jsonObjectdisplayHead.accumulate("model", displayListBySMART.get(i).get("model"));
			jsonObjectdisplayHead.accumulate("shop",  displayListBySMART.get(i).get("shop_id"));
			jsonObjectdisplayHead.accumulate("qty",  displayListBySMART.get(i).get("quantity"));
			displayArrayData.add(jsonObjectdisplayHead);
		}
		
		
		whereMap.put("spec", "%UHD%");
		List<HashMap<String, Object>> displayListByUHD = soTableDao.selectDisplayByData(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < displayListByUHD.size(); i++) {
			JSONObject jsonObjectdisplayHead=new JSONObject();
			jsonObjectdisplayHead.accumulate("spec", "UHD TV");
			jsonObjectdisplayHead.accumulate("model", displayListByUHD.get(i).get("model"));
			jsonObjectdisplayHead.accumulate("shop",  displayListByUHD.get(i).get("shop_id"));
			jsonObjectdisplayHead.accumulate("qty",  displayListByUHD.get(i).get("quantity"));
			displayArrayData.add(jsonObjectdisplayHead);
		}
		
		
	
		
		
		whereMap.put("spec", "%CURVE%");
		List<HashMap<String, Object>> displayListByCURVE = soTableDao.selectDisplayByData(whereMap);

		// 将查出来的机型销售数据放入表头，形成三级标题
		for (int i = 0; i < displayListByCURVE.size(); i++) {
			JSONObject jsonObjectdisplayHead=new JSONObject();
			jsonObjectdisplayHead.accumulate("spec", "CURVE TV");
			jsonObjectdisplayHead.accumulate("model", displayListByCURVE.get(i).get("model"));
			jsonObjectdisplayHead.accumulate("shop",  displayListByCURVE.get(i).get("shop_id"));
			jsonObjectdisplayHead.accumulate("qty",  displayListByCURVE.get(i).get("quantity"));
			displayArrayData.add(jsonObjectdisplayHead);
		}
		
		
		
		jsonObject.accumulate("displayData",displayArrayData);
		
		
		
		
		*/
		List<HashMap<String, Object>> salerList = soTableDao
				.selectSalerDatas(whereMap);

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
		
		
		
		
		List<HashMap<String, Object>> saleListTo = soTableDao
				.selectSaleDataByshopNoTarget(whereMap);
	
		HashMap<String, ArrayList<HashMap<String, Object>>> saleMapTo = new HashMap<String, ArrayList<HashMap<String, Object>>>();
		for (int m = 0; m < saleListTo.size(); m++) {
			if (saleMapTo.get(saleListTo.get(m).get("shop_id").toString()) == null) {
				ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
				modelList.add(saleListTo.get(m));
				saleMapTo.put(saleListTo.get(m).get("shop_id").toString(),
						modelList);
			} else {
				ArrayList<HashMap<String, Object>> modelList = saleMapTo
						.get(saleListTo.get(m).get("shop_id").toString());
				modelList.add(saleListTo.get(m));
			}

		}
		
		String [] daysBegin=whereMap.get("beginDate").toString().split("-");
		String [] daysEnd=whereMap.get("endDate").toString().split("-");
		int yearTo=Integer.parseInt(daysBegin[0]);
		int yearLast=Integer.parseInt(daysBegin[0])-1;
		whereMap.put("beginDate",yearLast+"-"+daysBegin[1]+"-"+daysBegin[2]);
		whereMap.put("endDate",yearLast+"-"+daysEnd[1]+"-"+daysEnd[2]);
		
		
		List<HashMap<String, Object>> saleListLast = soTableDao
				.selectSaleDataByshopNoTarget(whereMap);
	
		HashMap<String, ArrayList<HashMap<String, Object>>> saleMapLast = new HashMap<String, ArrayList<HashMap<String, Object>>>();
		for (int m = 0; m < saleListLast.size(); m++) {
			if (saleMapLast.get(saleListLast.get(m).get("shop_id").toString()) == null) {
				ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
				modelList.add(saleListLast.get(m));
				saleMapLast.put(saleListLast.get(m).get("shop_id").toString(),
						modelList);
			} else {
				ArrayList<HashMap<String, Object>> modelList = saleMapLast
						.get(saleListLast.get(m).get("shop_id").toString());
				modelList.add(saleListLast.get(m));
			}

		}
		
		
		
		
		JSONArray dataArray=new JSONArray();
		JSONArray dataArrayTwo=new JSONArray();
		
		BigDecimal TOTTLQty=BigDecimal.ZERO;
		BigDecimal TOTTLAmt=BigDecimal.ZERO;
		BigDecimal LASTTTLQty=BigDecimal.ZERO;
		BigDecimal LASTTTLAmt=BigDecimal.ZERO;
		/*BigDecimal TTLTarget=BigDecimal.ZERO;
		BigDecimal TTLCTarget =BigDecimal.ZERO;
*/

		
		List<Excel>	excels = new ArrayList<Excel>(soTableDao.selectDatas(whereMap));
		for (Excel excel : excels) {
			String shop_id = excel.getShopId()+"";
			JSONObject data=new JSONObject();
			data.put("reg",
					excel.getReg());
			data.put("type", "TV");
			data.put("dealer",
					excel.getDealer());
			data.put("branch",
					excel.getShopName());
			
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

			
			if (pro.length() > 0) {
				data.put("PROMODISER",
						pro.substring(0, pro.length() - 1));
			}
			

			data.put("shopId", excel.getShopId());
			dataArray.add(data);
		
			JSONObject dataTwo=new JSONObject();
			dataTwo.put("shopId", excel.getShopId());
			dataTwo.put("date", excel.getDateOfHired());
			dataTwo.put("area", excel.getArea());
			dataTwo.put("shopClass", excel.getLevel());
			if (sale.length() > 0) {
				dataTwo.put("SALESMAN",
						sale.substring(0, sale.length() - 1));
			}
			
			if (acfo.length() > 0) {
				dataTwo.put("ACFO",
						acfo.substring(0, acfo.length() - 1));
			}
			
			int toSaleQty=0;
			int lastSaleQty=0;
			int toSaleAmt=0;
			int lastSaleAmt=0;
			
			if (saleMapTo.get(shop_id) != null) {
				ArrayList<HashMap<String, Object>> modelList = saleMapTo
						.get(shop_id);
				for (int i = 0; i < modelList.size(); i++) {
					BigDecimal bd = new BigDecimal(modelList.get(i)
							.get("saleQty").toString());
					TOTTLQty=TOTTLQty.add(bd);
					dataTwo.put("ToTTLQty", Math.round(bd.doubleValue()));
					toSaleQty= (int) Math.round(bd.doubleValue());
					bd = new BigDecimal(modelList.get(i).get("saleSum")
							.toString());
					TOTTLAmt=TOTTLAmt.add(bd);
					toSaleAmt=(int) Math.round(bd.doubleValue());
					dataTwo.put("ToTTLAmt",
							Math.round(bd.doubleValue()));
					
				/*	bd = new BigDecimal(modelList.get(i).get("targetSum")
							.toString());
					TTLTarget=TTLTarget.add(bd);
					am = bd.toPlainString();
					dataTwo.put("basicTarget",Math.round(bd.doubleValue()));
					
					
					bd = new BigDecimal(modelList.get(i).get("basicAch")
							.toString());
					am = bd.toPlainString();
					dataTwo.put("basicAch",Math.round(bd.doubleValue())+"%");
					

					bd = new BigDecimal(modelList.get(i).get("challengeSum")
							.toString());
					TTLCTarget=TTLCTarget.add(bd);
					am = bd.toPlainString();
					dataTwo.put("challengeSum",Math.round(bd.doubleValue()));
					

					bd = new BigDecimal(modelList.get(i).get("challengeAch")
							.toString());
					am = bd.toPlainString();
					dataTwo.put("challengeAch",am+"%");*/

					
				}

			}
			
			
			
			if (saleMapLast.get(shop_id) != null) {
				ArrayList<HashMap<String, Object>> modelList = saleMapLast
						.get(shop_id);
				for (int i = 0; i < modelList.size(); i++) {
					BigDecimal bd = new BigDecimal(modelList.get(i)
							.get("saleQty").toString());
					LASTTTLQty=LASTTTLQty.add(bd);
					dataTwo.put("LastTTLQty", Math.round(bd.doubleValue()));
					lastSaleQty= (int) Math.round(bd.doubleValue());
					
					bd = new BigDecimal(modelList.get(i).get("saleSum")
							.toString());
					LASTTTLAmt=LASTTTLAmt.add(bd);
					lastSaleAmt=(int) Math.round(bd.doubleValue());
					dataTwo.put("LastTTLAmt",
							Math.round(bd.doubleValue()));
					
				/*	bd = new BigDecimal(modelList.get(i).get("targetSum")
							.toString());
					TTLTarget=TTLTarget.add(bd);
					am = bd.toPlainString();
					dataTwo.put("basicTarget",Math.round(bd.doubleValue()));
					
					
					bd = new BigDecimal(modelList.get(i).get("basicAch")
							.toString());
					am = bd.toPlainString();
					dataTwo.put("basicAch",Math.round(bd.doubleValue())+"%");
					

					bd = new BigDecimal(modelList.get(i).get("challengeSum")
							.toString());
					TTLCTarget=TTLCTarget.add(bd);
					am = bd.toPlainString();
					dataTwo.put("challengeSum",Math.round(bd.doubleValue()));
					

					bd = new BigDecimal(modelList.get(i).get("challengeAch")
							.toString());
					am = bd.toPlainString();
					dataTwo.put("challengeAch",am+"%");*/

					
				}

			}
			
			
			double l=0;
			double ach=0.0;
			if (lastSaleQty != 0) {
				l=toSaleQty-lastSaleQty;
				ach=(l/lastSaleQty)  *100;
				long lnum = Math.round(ach);
				dataTwo.put("GROWTH_QTY", lnum + "%");

			} else if (toSaleQty == 0 && lastSaleQty == 0) {
				dataTwo.put("GROWTH_QTY", "100%");
			} else if (toSaleQty == 0 && lastSaleQty != 0) {
				dataTwo.put("GROWTH_QTY", "-100%");
			}
			
			
			if (lastSaleAmt != 0) {
				l=toSaleAmt-lastSaleAmt;
				ach=l/lastSaleAmt*100;
				long lnum = Math.round(ach);
				dataTwo.put("GROWTH_AMT", lnum + "%");

			} else if (toSaleAmt == 0 && lastSaleAmt == 0) {
				dataTwo.put("GROWTH_AMT", "100%");
			} else if (toSaleAmt == 0 && lastSaleAmt != 0) {
				dataTwo.put("GROWTH_AMT", "-100%");
			}
			
		
			
			dataArrayTwo.add(dataTwo);
			
		}
	
		JSONArray jsonArrayTTL=new JSONArray();
		JSONObject jsoObjectTTL=new JSONObject();
		jsoObjectTTL.accumulate("TOTTLQty", Math.round(TOTTLQty.doubleValue()));
		jsoObjectTTL.accumulate("TOTTLAmt", Math.round(TOTTLAmt.doubleValue()));
		jsoObjectTTL.accumulate("LASTTTLQty", Math.round(LASTTTLQty.doubleValue()));
		jsoObjectTTL.accumulate("LASTTTLAmt", Math.round(LASTTTLAmt.doubleValue()));
		
		
		if (Math.round(LASTTTLQty.doubleValue()) != 0) {
			double l=(Math.round(TOTTLQty.doubleValue())- Math.round(LASTTTLQty.doubleValue()));
			double ach=l/Math.round(LASTTTLQty.doubleValue())*100;
			long lnum = Math.round(ach);
			jsoObjectTTL.put("GROWTH_QTY", lnum + "%");

		} else if (Math.round(TOTTLQty.doubleValue())== 0 && Math.round(LASTTTLAmt.doubleValue()) == 0) {
			jsoObjectTTL.put("GROWTH_QTY", "100%");
		} else if (Math.round(TOTTLQty.doubleValue()) == 0 && Math.round(LASTTTLAmt.doubleValue())!= 0) {
			jsoObjectTTL.put("GROWTH_QTY", "-100%");
		}
		
		
		if (Math.round(LASTTTLAmt.doubleValue()) != 0) {
			double l=(Math.round(TOTTLAmt.doubleValue())-Math.round(LASTTTLAmt.doubleValue()));
			double ach=l/Math.round(LASTTTLAmt.doubleValue())*100;
			long lnum = Math.round(ach);
			jsoObjectTTL.put("GROWTH_AMT", lnum + "%");

		} else if (Math.round(TOTTLAmt.doubleValue()) == 0 && Math.round(LASTTTLAmt.doubleValue())== 0) {
			jsoObjectTTL.put("GROWTH_AMT", "100%");
		} else if (Math.round(TOTTLAmt.doubleValue())== 0 && Math.round(LASTTTLAmt.doubleValue()) != 0) {
			jsoObjectTTL.put("GROWTH_AMT", "-100%");
		}
		
		
	

		
		/*jsoObjectTTL.accumulate("basicTarget", TTLTarget);
		jsoObjectTTL.accumulate("challengeSum", TTLCTarget);
		jsoObjectTTL.accumulate("basicAch", Math.round(TTLAmt.doubleValue()/TTLTarget.doubleValue()*100)+"%");
		jsoObjectTTL.accumulate("challengeAch", Math.round(TTLAmt.doubleValue()/TTLCTarget.doubleValue()*100)+"%");
	*/	
		jsonArrayTTL.add(jsoObjectTTL);
		
		jsonObject.put("data", dataArray);
		jsonObject.put("dataTwo", dataArrayTwo);
		jsonObject.put("TTL", jsonArrayTTL);
		
		
		
		
		
		
		return jsonObject;
	}


	@Override
	public JSONObject selectMonthByACFO(Map<String, Object> whereMap) {
		LinkedList<HashMap<String, Object>> dataListFour = new LinkedList<HashMap<String, Object>>();
		String [] be=whereMap.get("beginDate").toString().split("-");
		String [] end=whereMap.get("endDate").toString().split("-");
		whereMap.put("tbeginDate", be[0]+"-"+be[1]+"-01");
		whereMap.put("tendDate",  end[0]+"-"+end[1]+"-31");
		

		List<HashMap<String, Object>> acfoDatas = soTableDao
				.selectTargetByAcfo(whereMap);
		
		
		
		List<HashMap<String, Object>> Area = soTableDao.selectAreaByUser(whereMap);

		List<HashMap<String, Object>> acfoFps = soTableDao
				.selectFpsNameByShop(whereMap);

		for (int i = 0; i < acfoDatas.size(); i++) {
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("Acfo", acfoDatas.get(i).get("userName"));

			if (Area.size() > 1) {
				String a = "";
				for (int k = 0; k < Area.size(); k++) {
					if (acfoDatas.get(i).get("userId").toString()
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
						.equals(acfoDatas.get(i).get("userId"))) {
					tvFps = Integer.parseInt(acfoFps.get(j).get("TVFPS")
							.toString());
					dataMap.put("FPS", tvFps);

				}
			}

			dataMap.put("Shop", acfoDatas.get(i).get("noOfShops"));

			BigDecimal a = new BigDecimal(acfoDatas.get(i).get("saleQty")
					.toString());
			dataMap.put("SoQty", Math.round(a.doubleValue()));
			a = new BigDecimal(acfoDatas.get(i).get("saleSum").toString());
			String saleSumOne = a.toPlainString();
			dataMap.put("SoAmt", Math.round(a.doubleValue()));
			Double saleSum = Double.parseDouble(saleSumOne);

			BigDecimal b = new BigDecimal(acfoDatas.get(i).get("targetSum")
					.toString());
			dataMap.put("TargetAmt", Math.round(b.doubleValue()));
			Double targetSum =  (double) Math.round(b.doubleValue());
			double ach=0.0;
			long lnum=0;
			if(targetSum!=0.0 && targetSum!=0){
				ach = saleSum / targetSum * 100;
				lnum = Math.round(ach);
				dataMap.put("Ach", Math.round(ach)+"%" );
			}else{
				dataMap.put("Ach", "100%" );
			}
		

			b = new BigDecimal(acfoDatas.get(i).get("challengeSum")
					.toString());
			dataMap.put("CTarget", Math.round(b.doubleValue()));
			Double challenge = (double) Math.round(b.doubleValue());
			if(challenge!=0.0 && challenge!=0){
				ach = saleSum / challenge * 100;
				lnum = Math.round(ach);
				dataMap.put("CAch", Math.round(ach) +"%");
			}else{
				dataMap.put("CAch", "100%");
			}
			
		

			b = new BigDecimal(acfoDatas.get(i).get("saleQty").toString());

			double qty = Math.round(b.doubleValue());
			if (tvFps != 0) {
				ach = saleSum / tvFps;
				lnum = Math.round(ach);
				double fpsQty = qty / tvFps;
				dataMap.put("Ave_FPS_Qty",  Math.round(fpsQty));
				dataMap.put("Ave_FPS_Amount",lnum);
			}

			
			
			
			
		
			dataListFour.add(dataMap);

		}
		DateUtil.Order(dataListFour, "Ach");
		for (int i = 0; i < dataListFour.size(); i++) {
			dataListFour.get(i).put("Rank", i + 1);
		}
		JSONObject jsonObject=new JSONObject();
		jsonObject.accumulate("data", dataListFour);
		return jsonObject;
	}

	@Override
	public JSONObject selectGrowthBySalesman(Map<String, Object> whereMap) throws Exception {
		LinkedList<HashMap<String, Object>> dataListThree = new LinkedList<HashMap<String, Object>>();
		String[] toYearBegin = whereMap.get("beginDate").toString().split("-");
		String[] toYearEnd = whereMap.get("endDate").toString().split("-");

		int laYear = Integer.parseInt(toYearBegin[0].toString()) - 1;
		Date timec = format.parse(whereMap.get("beginDate").toString());
		Calendar rightNowc = Calendar.getInstance();
		rightNowc.setTime(timec);
		
		Date toYearOne = format.parse(format.format(rightNowc.getTime()));
		
		String laDays = DateUtil.getLastDayOfMonth(laYear,
				Integer.parseInt(toYearBegin[1]));
		String[] laDay = laDays.split("-");
		if(whereMap.get("type")!=null && whereMap.get("type")!=""){
			if(Integer.parseInt(toYearBegin[0].toString())>=2018){
				whereMap.put("searchLine", "AND  pr.`size`>55");
			}else{
				whereMap.put("searchLine", "AND  pr.`size`>48");
			}
		}
		
		//o.line="AND  pr.`size`>"+$("#SaleBigSizeByYear").val();
		List<HashMap<String, Object>> toDataBySalesInfo = soTableDao
				.selectTargetBySalemanInfo(whereMap);

		List<HashMap<String, Object>> toDataBySales = soTableDao
				.selectTargetBySaleman(whereMap);
		List<HashMap<String, Object>> toFps = soTableDao
				.selectFpsNameByShop(whereMap);
		
		
		whereMap.put("beginDate", laYear + "-" + toYearBegin[1] + "-"+ toYearBegin[2]);
		whereMap.put("endDate", laYear + "-" + toYearEnd[1] + "-"+ toYearEnd[2]);
		if(whereMap.get("type")!=null && whereMap.get("type")!=""){
			if(laYear>=2018){
				whereMap.put("searchLine", "AND  pr.`size`>55");
			}else{
				whereMap.put("searchLine", "AND  pr.`size`>48");
			}
			
		}
	
		List<HashMap<String, Object>> laDataBysales = soTableDao
				.selectTargetBySaleman(whereMap);

		List<HashMap<String, Object>> Account = soTableDao
				.selectPartyNameByuser(whereMap);


		List<HashMap<String, Object>> laFps = soTableDao
				.selectFpsNameByShop(whereMap);

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
			dataMap.put("performances_SALESMAN",
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
							dataMap.put("toYear_FPS", tvFps);
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
							"toYear_Qty",
							Math.round(bd.doubleValue()));

					tq = Math.round(bd.doubleValue());

					bd = new BigDecimal(toDataBySales.get(j).get("saleSum")
							.toString());
					am = bd.toPlainString();
					dataMap.put("toYear_Amt",  Math.round(bd.doubleValue()));
					ts = Math.round(bd.doubleValue());

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
							dataMap.put("laYear_FPS"  , tvFps);

							lfps = tvFps;

						}
					}

					bd = new BigDecimal(laDataBysales.get(s).get("saleQty")
							.toString());

					dataMap.put(
							"laYear_Qty" ,
							Math.round(bd.doubleValue()));

					lq = Math.round(bd.doubleValue());
					bd = new BigDecimal(laDataBysales.get(s).get("saleSum")
							.toString());
					am = bd.toPlainString();
					dataMap.put("laYear_Amt", Math.round(bd.doubleValue()));
					ls =  Math.round(bd.doubleValue());
				}

			}

			if (lq != 0) {
				co = (tq - lq);
				double qg = co / lq * 100;
				long lnum = Math.round(qg);
				dataMap.put("Qty_Growth",
						lnum + "%");
			}else{
				dataMap.put("Qty_Growth","100%");
			}
			
			if (ls != 0.0) {
				co = (ts - ls);
				double re = co / ls * 100;
				long lnum = Math.round(re);
				dataMap.put("Amt_Growth", lnum + "%");
			}else{
				dataMap.put("Amt_Growth","100%");
			}

			if (lfps != 0) {
				double lf = (lq / lfps);
				dataMap.put("laYear_Aveqty_fps",Math.round(lf));
			}
			if (tfps != 0) {
				double tf = (tq / tfps);
				dataMap.put("toYear_Aveqty_fps", Math.round(tf));
			}
			if (lfps != 0) {
				double lff = (ls / lfps);
				dataMap.put("laYear_Aveamt_fps", Math.round(lff));
			}
			if (tfps != 0) {
				double tff = (ts / tfps);
				dataMap.put("toYear_Aveamt_fps", Math.round(tff));
			}

			

			dataListThree.add(dataMap);
		}

		DateUtil.Order(dataListThree, "Amt_Growth");
		for (int i = 0; i < dataListThree.size(); i++) {
			dataListThree.get(i).put("RANK",
					i + 1);
		}
		JSONObject jsonObject =new JSONObject();
		jsonObject.accumulate("data", dataListThree);
		return jsonObject;
	}

	@Override
	public JSONObject selectMonthByBigRegional(Map<String, Object> whereMap) {
		LinkedList<HashMap<String, Object>> dataListTwo = new LinkedList<HashMap<String, Object>>();

		

		List<HashMap<String, Object>> bigReg = soTableDao
				.selectBigReg(whereMap);
		
		List<HashMap<String, Object>> re = soTableDao
				.selectRegionalHeadByParty(whereMap);
		
		List<HashMap<String, Object>> areaDatas = soTableDao
				.selectDataByArea(whereMap);
		
		List<HashMap<String, Object>> areaFPS = soTableDao
				.selectFpsNameByParty(whereMap);
		
		whereMap.put("type",1);
		String [] be=whereMap.get("beginDate").toString().split("-");
		String [] end=whereMap.get("endDate").toString().split("-");
		whereMap.put("tbeginDate", be[0]+"-"+be[1]+"-01");
		whereMap.put("tendDate",  end[0]+"-"+end[1]+"-31");
		List<HashMap<String, Object>> targetDatas = soTableDao
				.selectTargetByArea(whereMap);
		
		for (int a = 0; a < bigReg.size(); a++) {
			BigDecimal Qty=BigDecimal.ZERO;
			BigDecimal Amt=BigDecimal.ZERO;
			
			BigDecimal Shop=BigDecimal.ZERO;
			BigDecimal Fps=BigDecimal.ZERO;
			
			BigDecimal TargetAmt=BigDecimal.ZERO;
			BigDecimal CTargetAmt=BigDecimal.ZERO;
			BigDecimal bd =null;
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			for (int i = 0; i < re.size(); i++) {
				if (bigReg.get(a).get("PARTY_ID")
						.equals(re.get(i).get("PARTY_ID"))) {
					dataMap.put("RegionalHead", re.get(i).get("userName"));
				}
			}
			
			dataMap.put("AREA", bigReg.get(a).get("PARTY_NAME"));
			
			for (int j = 0; j < areaFPS.size(); j++) {
				String party=","+areaFPS.get(j).get("PARTY_ID")+",";
				
				if(bigReg.get(a).get("small").toString().contains(party)){
					
					bd = new BigDecimal(areaFPS.get(j).get("tvFps").toString());
					Fps=Fps.add(bd);
				
				}
				
			}
			
			
			
			for (int j = 0; j < areaDatas.size(); j++) {
				String party=","+areaDatas.get(j).get("PARTY_ID")+",";
				
				if(bigReg.get(a).get("small").toString().contains(party)){
					bd= new BigDecimal(areaDatas.get(j).get("saleQty")
							.toString());
					Qty=Qty.add(bd);
					bd = new BigDecimal(areaDatas.get(j).get("saleSum").toString());
					Amt=Amt.add(bd);
				
					bd = new BigDecimal(areaDatas.get(j).get("noOfShops").toString());
					Shop=Shop.add(bd);
				
				
				}
				
			}
			
			for (HashMap<String, Object> hashMap : targetDatas) {
				String party=","+hashMap.get("PARTY_ID")+",";
				
				if(bigReg.get(a).get("small").toString().contains(party)){
					BigDecimal c = new BigDecimal(hashMap.get("targetSum")
							.toString());
					TargetAmt=TargetAmt.add(c);

					c = new BigDecimal(hashMap.get("challengeSum").toString());

					CTargetAmt=CTargetAmt.add(c);
					
				}
				
			}
			
			
			dataMap.put("SoQty", Math.round(Qty.doubleValue()));
			dataMap.put("SoAmt", Math.round(Amt.doubleValue()));
			dataMap.put("CTargetAmt", Math.round(CTargetAmt.doubleValue()));
			dataMap.put("TargetAmt", Math.round(TargetAmt.doubleValue()));
			dataMap.put("Shop", Math.round(Shop.doubleValue()));
			dataMap.put("Fps", Math.round(Fps.doubleValue()));
			
			if(Math.round(TargetAmt.doubleValue())!=0){
				double ach =  Amt.doubleValue() / TargetAmt.doubleValue() * 100;
				dataMap.put("Ach", Math.round(ach)+"%");
			}else{
				dataMap.put("Ach", "100%");
			}
			

			if(Math.round(CTargetAmt.doubleValue() )!=0){

				double ach =  Amt.doubleValue() / CTargetAmt.doubleValue() * 100;
				dataMap.put("CAch", Math.round(ach)+"%");
			}else{
				dataMap.put("CAch", "0%");
			}
			
			
			if (Math.round(Fps.doubleValue())!=0) {
				
				double ach = Amt.doubleValue() /  Fps.doubleValue();
				
				dataMap.put("Ave_FPS_Amount", Math.round(ach));

				double fpsQty =  Qty.doubleValue() / Fps.doubleValue();
				dataMap.put("Ave_FPS_Qty", Math.round(fpsQty));
			}else{
				dataMap.put("Ave_FPS_Amount", "-");

				dataMap.put("Ave_FPS_Qty","-");
			}

			dataListTwo.add(dataMap);

		}
		
			

		DateUtil.Order(dataListTwo, "Ach");
		for (int i = 0; i < dataListTwo.size(); i++) {
			dataListTwo.get(i).put("Rank", i + 1);
		}
		System.out.println(dataListTwo);
		JSONObject jsonObject=new JSONObject();
		jsonObject.accumulate("data", dataListTwo);
		return jsonObject;
	}
	
	public String getDiffBigSql (int Year){
		if(Year >= 2018){
			return " and pr.`size` > CAST('55' as SIGNED) ";
		}else{
			return " and pr.`size` > CAST('48' as SIGNED) ";
		}
	}

	@Override
	public JSONObject selectMonthByDEALER(Map<String, Object> whereMap) {
		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
		
		List<HashMap<String, Object>> DealerSelloutInfo = statementTableDao
				.selectSelloutByDealerInfo(whereMap);
		
		int thisYear=Integer.parseInt(whereMap.get("endDate").toString().split("-")[0]);
		if(Contents.BIG_TAB.equals(whereMap.get("tab"))){
			whereMap.put("filter", getDiffBigSql(thisYear));
		}
		List<HashMap<String, Object>> DealerSelloutTo = statementTableDao
				.selectSelloutByDealer(whereMap);

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
		
		if(Contents.BIG_TAB.equals(whereMap.get("tab"))){
			whereMap.put("filter", getDiffBigSql(thisYear - 1));
		}
		
		String [] daysBegin=whereMap.get("beginDate").toString().split("-");
		String [] daysEnd=whereMap.get("endDate").toString().split("-");
		int yearTo=Integer.parseInt(daysBegin[0]);
		int yearLast=Integer.parseInt(daysBegin[0])-1;
		whereMap.put("beginDate",yearLast+"-"+daysBegin[1]+"-"+daysBegin[2]);
		whereMap.put("endDate",yearLast+"-"+daysEnd[1]+"-"+daysEnd[2]);
		
		List<HashMap<String, Object>> DealerSelloutLast = statementTableDao
				.selectSelloutByDealer(whereMap);

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

			BigDecimal bd = null;
			String am = "";
			String aq = "";
			if (selloutToMap.get(DealerSelloutInfo.get(w).get("DEALER")) != null) {

				ArrayList<HashMap<String, Object>> list = selloutToMap
						.get(DealerSelloutInfo.get(w).get("DEALER"));
				for (int i = 0; i < list.size(); i++) {

					dataMap.put("toYear_SHOP",
							list.get(i).get("noOfShops"));
					dataMap.put("toYear_FPS", list.get(i).get("tvFps"));

					BigDecimal bq = new BigDecimal(list.get(i)
							.get("saleQty").toString());
					aq = bq.toPlainString();
					tq =  (int) Math.round(bq.doubleValue());
					dataMap.put("toYear_QTY", Math.round(bq.doubleValue()));

					bd = new BigDecimal(list.get(i).get("saleSum")
							.toString());
					am = bd.toPlainString();

					dataMap.put("toYear_AMT", Math.round(bd.doubleValue()));
					ts =  (double) Math.round(bd.doubleValue());
				
					

					tshop = Integer.parseInt(list.get(i).get("noOfShops")
							.toString());
					tfps = Integer.parseInt(list.get(i).get("tvFps")
							.toString());
					

					
					

				

					if (tfps != 0) {
						avg = tq / tfps;
						dataMap.put("toYear_AVE_FPS_QTY",Math.round( avg));
						tqavg = Math.round( avg);

						avg =  (ts / tfps);
						dataMap.put("toYear_AVE_FPS_AMT", Math.round( avg));
						tsavg = Math.round( avg);
					}

				}

			}
			if (selloutLastMap.get(DealerSelloutInfo.get(w).get("DEALER")) != null) {

				ArrayList<HashMap<String, Object>> list = selloutLastMap
						.get(DealerSelloutInfo.get(w).get("DEALER"));
				for (int j = 0; j < list.size(); j++) {
					BigDecimal lbd = new BigDecimal(list.get(j).get("saleQty").toString());
					
					dataMap.put("lastYear_SHOP", list.get(j).get("noOfShops"));
					dataMap.put("lastYear_FPS", list.get(j).get("tvFps"));
					dataMap.put("lastYear_QTY",Math.round(lbd.doubleValue()) );
					lq =  (int) Math.round(lbd.doubleValue());
					String lam = lbd.toPlainString();
					lbd = new BigDecimal(list.get(j)
							.get("saleSum").toString());
					ls =(double) Math.round(lbd.doubleValue());
					dataMap.put("lastYear_AMT", Math.round(lbd.doubleValue()));
					lshop = Integer.parseInt(list.get(j).get("noOfShops")
							.toString());

					lfps = Integer.parseInt(list.get(j).get("tvFps")
							.toString());

					
					

					if (lfps != 0) {
						avg =  (lq / lfps);
						dataMap.put("lastYear_AVE_FPS_QTY", Math.round( avg));
						lqavg = Math.round( avg);

						avg =  (ls / lfps);
						dataMap.put("lastYear_AVE_FPS_AMT", Math.round( avg));
						lsavg = Math.round( avg);
					}

				}
			}

			if (lq != 0) {
				ltq = tq - lq;
				ach = ltq / lq * 100;
				long lnum = Math.round(ach);
				dataMap.put("GROWTH_QTY", lnum + "%");

			} else if (tq == 0 && lq == 0) {
				dataMap.put("GROWTH_QTY", "0%");
			} else if (tq == 0 && lq != 0) {
				dataMap.put("GROWTH_QTY", "-100%");
			}
			if (ls != 0.0) {
				ltq = ts - ls;
				ach = ltq / ls * 100;
				long lnum = Math.round(ach);
				dataMap.put("GROWTH_AMOUNT", lnum + "%");
			} else if (ts == 0.0 && ls == 0.0) {
				dataMap.put("GROWTH_AMOUNT", "0%");
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

		DateUtil.Order(dataList, "GROWTH_AMOUNT");
		}
		for (int i = 0; i < dataList.size(); i++) {
			dataList.get(i).put("RANK", i + 1);
		}
		JSONObject jsonObject=new JSONObject();
		jsonObject.accumulate("data",dataList);
		return jsonObject;
	}

	@Override
	public JSONObject selectSpecPro(Map<String, Object> whereMap) {
		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
		
		List<HashMap<String, Object>> bigReg = soTableDao
				.selectBigReg(whereMap);
		
		whereMap.put("spec","%BASIC LED%" );
		List<HashMap<String, Object>> specyByParty = soTableDao.selectModelListBySpecParty(whereMap);

		for (int a = 0; a < bigReg.size(); a++) {
			BigDecimal Qty=BigDecimal.ZERO;
			
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("reg",bigReg.get(a).get("PARTY_NAME") );
			dataMap.put("spec","BASIC LED");
			for (int i = 0; i < specyByParty.size(); i++) {
				String party=","+specyByParty.get(i).get("PARTY_ID")+",";
				if(bigReg.get(a).get("small").toString().contains(party)){
					BigDecimal bd=new BigDecimal(specyByParty.get(i).get("quantity").toString());
					Qty=Qty.add(bd);
				}
			}
			dataMap.put("qty",Math.round(Qty.doubleValue()));
			dataList.add(dataMap);
		}
			
		
		
		
		whereMap.put("spec","%DIGITAL%" );
		specyByParty = soTableDao.selectModelListBySpecParty(whereMap);

		for (int a = 0; a < bigReg.size(); a++) {
			BigDecimal Qty=BigDecimal.ZERO;
			
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("reg",bigReg.get(a).get("PARTY_NAME") );
			dataMap.put("spec","DIGITAL");
			for (int i = 0; i < specyByParty.size(); i++) {
				String party=","+specyByParty.get(i).get("PARTY_ID")+",";
				if(bigReg.get(a).get("small").toString().contains(party)){
					BigDecimal bd=new BigDecimal(specyByParty.get(i).get("quantity").toString());
					Qty=Qty.add(bd);
				}
			}
			dataMap.put("qty",Math.round(Qty.doubleValue()));
			dataList.add(dataMap);
		}
			
		
		whereMap.put("spec","%INTERNET%" );
		specyByParty = soTableDao.selectModelListBySpecParty(whereMap);

		for (int a = 0; a < bigReg.size(); a++) {
			BigDecimal Qty=BigDecimal.ZERO;
			
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("reg",bigReg.get(a).get("PARTY_NAME") );
			dataMap.put("spec","INTERNET");
			for (int i = 0; i < specyByParty.size(); i++) {
				String party=","+specyByParty.get(i).get("PARTY_ID")+",";
				if(bigReg.get(a).get("small").toString().contains(party)){
					BigDecimal bd=new BigDecimal(specyByParty.get(i).get("quantity").toString());
					Qty=Qty.add(bd);
				}
			}
			dataMap.put("qty",Math.round(Qty.doubleValue()));
			dataList.add(dataMap);
		}
			
		
		whereMap.put("spec","%SMART%" );
		specyByParty = soTableDao.selectModelListBySpecParty(whereMap);

		for (int a = 0; a < bigReg.size(); a++) {
			BigDecimal Qty=BigDecimal.ZERO;
			
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("reg",bigReg.get(a).get("PARTY_NAME") );
			dataMap.put("spec","SMART");
			for (int i = 0; i < specyByParty.size(); i++) {
				String party=","+specyByParty.get(i).get("PARTY_ID")+",";
				if(bigReg.get(a).get("small").toString().contains(party)){
					BigDecimal bd=new BigDecimal(specyByParty.get(i).get("quantity").toString());
					Qty=Qty.add(bd);
				}
			}
			dataMap.put("qty",Math.round(Qty.doubleValue()));
			dataList.add(dataMap);
		}
			
		
		whereMap.put("spec","%UHD%" );
		specyByParty = soTableDao.selectModelListBySpecParty(whereMap);

		for (int a = 0; a < bigReg.size(); a++) {
			BigDecimal Qty=BigDecimal.ZERO;
			
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("reg",bigReg.get(a).get("PARTY_NAME") );
			dataMap.put("spec","UHD");
			for (int i = 0; i < specyByParty.size(); i++) {
				String party=","+specyByParty.get(i).get("PARTY_ID")+",";
				if(bigReg.get(a).get("small").toString().contains(party)){
					BigDecimal bd=new BigDecimal(specyByParty.get(i).get("quantity").toString());
					Qty=Qty.add(bd);
				}
			}
			dataMap.put("qty",Math.round(Qty.doubleValue()));
			dataList.add(dataMap);
		}
			
		
		whereMap.put("spec","%CURVED%" );
		specyByParty = soTableDao.selectModelListBySpecParty(whereMap);

		for (int a = 0; a < bigReg.size(); a++) {
			BigDecimal Qty=BigDecimal.ZERO;
			
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("reg",bigReg.get(a).get("PARTY_NAME") );
			dataMap.put("spec","CURVED");
			for (int i = 0; i < specyByParty.size(); i++) {
				String party=","+specyByParty.get(i).get("PARTY_ID")+",";
				if(bigReg.get(a).get("small").toString().contains(party)){
					BigDecimal bd=new BigDecimal(specyByParty.get(i).get("quantity").toString());
					Qty=Qty.add(bd);
				}
			}
			dataMap.put("qty",Math.round(Qty.doubleValue()));
			dataList.add(dataMap);
		}
			
		
		//查询DIGITAL BASIC LED
		Map<String, BigDecimal> mapResultDBL = new HashMap<String, BigDecimal>();
		for (int i = 0; i < dataList.size(); i++) {
			HashMap<String, Object> dataMap = dataList.get(i);
			if(dataMap.get("spec").equals("BASIC LED") || dataMap.get("spec").equals("DIGITAL")){
				BigDecimal numOne = mapResultDBL.get(dataMap.get("PARTY_NAME").toString());
				 if (numOne == null) {
					  numOne = BigDecimal.ZERO;
				  }
				  BigDecimal b=new BigDecimal(dataMap.get("qty").toString());
				  mapResultDBL.put(dataMap.get("PARTY_NAME").toString(),numOne.add(b)) ;
			}
				 
		}
			
		
		//查询Smart Tv
		Map<String, BigDecimal> mapResultSmart = new HashMap<String, BigDecimal>();
		for (int i = 0; i < dataList.size(); i++) {
			HashMap<String, Object> dataMap = dataList.get(i);
			if(dataMap.get("spec").equals("SMART") || dataMap.get("spec").equals("UHD")
					|| dataMap.get("spec").equals("CURVED")){
				BigDecimal numOne = mapResultSmart.get(dataMap.get("PARTY_NAME").toString());
				 if (numOne == null) {
					  numOne = BigDecimal.ZERO;
				  }
				  BigDecimal b=new BigDecimal(dataMap.get("qty").toString());
				  mapResultSmart.put(dataMap.get("PARTY_NAME").toString(),numOne.add(b)) ;
			}
				 
		}
			
		//查询Flat UHD
		Map<String, BigDecimal> mapResultFlatUHD = new HashMap<String, BigDecimal>();
		for (int i = 0; i < dataList.size(); i++) {
			HashMap<String, Object> dataMap = dataList.get(i);
			if(dataMap.get("spec").equals("SMART") || dataMap.get("spec").equals("UHD")
					|| dataMap.get("spec").equals("CURVED")){
				BigDecimal numOne = mapResultFlatUHD.get(dataMap.get("PARTY_NAME").toString());
				 if (numOne == null) {
					  numOne = BigDecimal.ZERO;
				  }
				  BigDecimal b=new BigDecimal(dataMap.get("qty").toString());
				  mapResultFlatUHD.put(dataMap.get("PARTY_NAME").toString(),numOne.add(b)) ;
			}
				 
		}
			
		
		
		//查询CURVED FHD
		Map<String, BigDecimal> mapResultCURVEDFHD = new HashMap<String, BigDecimal>();
		for (int i = 0; i < dataList.size(); i++) {
			HashMap<String, Object> dataMap = dataList.get(i);
			if(dataMap.get("spec").equals("SMART") 
					|| dataMap.get("spec").equals("CURVED")){
				BigDecimal numOne = mapResultCURVEDFHD.get(dataMap.get("PARTY_NAME").toString());
				 if (numOne == null) {
					  numOne = BigDecimal.ZERO;
				  }
				  BigDecimal b=new BigDecimal(dataMap.get("qty").toString());
				  mapResultCURVEDFHD.put(dataMap.get("PARTY_NAME").toString(),numOne.add(b)) ;
			}
				 
		}
			
		
		
		//查询CURVED UHD
		Map<String, BigDecimal> mapResultCURVEDUHD = new HashMap<String, BigDecimal>();
		for (int i = 0; i < dataList.size(); i++) {
			HashMap<String, Object> dataMap = dataList.get(i);
			if(dataMap.get("spec").equals("SMART") || dataMap.get("spec").equals("UHD")
					|| dataMap.get("spec").equals("CURVED")){
				BigDecimal numOne = mapResultCURVEDUHD.get(dataMap.get("PARTY_NAME").toString());
				 if (numOne == null) {
					  numOne = BigDecimal.ZERO;
				  }
				  BigDecimal b=new BigDecimal(dataMap.get("qty").toString());
				  mapResultCURVEDUHD.put(dataMap.get("PARTY_NAME").toString(),numOne.add(b)) ;
			}
				 
		}
			
		
		

		return null;
	}

	@Override
	public JSONObject selectModelByMonth(Map<String, Object> whereMap) {
		List<HashMap<String, Object>> modelList = soTableDao.selectModelByMonth(whereMap);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		
		String model="";
		if(modelList.size()>=1){
			model=modelList.get(0).get("model").toString();
		}
		String month=whereMap.get("endDate").toString().split("-")[1];
		int [] val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		for (int i = 0; i < modelList.size(); i++) {
			if(WebPageUtil.isStringNullAvaliable(model) && model.equals(modelList.get(i).get("model").toString())){
				String date=modelList.get(i).get("month").toString();
				int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
				BigDecimal bd=new BigDecimal(modelList.get(i).get("qty").toString());
				int qty=(int)Math.round(bd.doubleValue());
				
				val[dte -1] = qty;
				
				if(modelList.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("model",model);
					jsonObject.accumulate("arr", val);
					array.add(jsonObject);
				}
				
			}else{
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("model",model);
					jsonObject.accumulate("arr", val);
					array.add(jsonObject);
					model = modelList.get(i).get("model").toString();
					val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					i--;

				
			}
		}
		object.accumulate("data", array);
		return object;
	}

	public int []  getQty(int length){
		int[] a= new int[length];
		for (int i = 0; i < a.length; i++) {
			a[i] = 0;
		}
		return a;
	}

	
	public int []  getAmt(int length){
		int[] a= new int[length];
		for (int i = 0; i < a.length; i++) {
			a[i] = 0;
		}
		return a;
	}

	@Override
	public List<HashMap<String, Object>> selectPartyCus(
			Map<String, Object> whereMap) {
		return soTableDao.selectPartyCus(whereMap);
	}

	@Override
	public JSONObject selectYearBySale(Map<String, Object> whereMap) {
		LinkedList<HashMap<String, Object>> dataListThree = new LinkedList<HashMap<String, Object>>();

		
		List<HashMap<String, Object>> salemanDatas = soTableDao
				.selectTargetBySaleman(whereMap);
		
	
		List<HashMap<String, Object>> Account = soTableDao
				.selectPartyNameByuser(whereMap);

		List<HashMap<String, Object>> saleFps = soTableDao
				.selectFpsNameByShop(whereMap);

		for (int i = 0; i < salemanDatas.size(); i++) {
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("Saleman", salemanDatas.get(i).get("userName"));

			if (Account.size() > 1) {
				String a = "";
				for (int k = 0; k < Account.size(); k++) {
					if (salemanDatas.get(i).get("userId")
							.equals(Account.get(k).get("userId"))) {
						a += Account.get(k).get("PARTY_NAME") + ",";
					}

				}
				dataMap.put("Account", a.substring(0, a.length() - 1));
			}
			int tvFps = 0;
			for (int j = 0; j < saleFps.size(); j++) {
				if (saleFps.get(j).get("USER")
						.equals(salemanDatas.get(i).get("userId"))) {
					tvFps = Integer.parseInt(saleFps.get(j).get("TVFPS")
							.toString());
					dataMap.put("FPS", tvFps);

				}
			}

			dataMap.put("Shop", salemanDatas.get(i).get("noOfShops"));

			BigDecimal bd = new BigDecimal(salemanDatas.get(i)
					.get("saleQty").toString());
			dataMap.put("SO_Qty", Math.round(bd.doubleValue()));
			BigDecimal a = new BigDecimal(salemanDatas.get(i)
					.get("saleSum").toString());
			dataMap.put("SO_Amt", Math.round(a.doubleValue()));
			Double saleSum = (double) Math.round(a.doubleValue());

			BigDecimal b = new BigDecimal(salemanDatas.get(i)
					.get("targetSum").toString());
			dataMap.put("Target", Math.round(b.doubleValue()));
			Double targetSum = (double) Math.round(b.doubleValue());
			if(targetSum!=0 && targetSum!=0.0){
				double ach = saleSum / targetSum * 100;
				long lnum = Math.round(ach);
				dataMap.put("Ach", lnum + "%");
			}else{
				dataMap.put("Ach",   "100%");
			}
			

			b = new BigDecimal(salemanDatas.get(i).get("challengeSum")
					.toString());
			dataMap.put("CTarget", Math.round(b.doubleValue()));
			Double challenge = (double) Math.round(b.doubleValue());
			if(challenge!=0.0 && challenge!=0){
				double ach = saleSum / challenge * 100;
				long lnum = Math.round(ach);
				dataMap.put("CAch", lnum + "%");
			}else{
				dataMap.put("CAch",   "100%");
			}
			
			b = new BigDecimal(salemanDatas.get(i).get("saleQty")
					.toString());
			Long qty = Math.round(b.doubleValue());

			if (tvFps != 0) {
				double fpsAmt = (saleSum / tvFps);
				double fpsQty = qty / tvFps;
				dataMap.put("Ave_FPS_Qty",Math.round(fpsQty) );
				dataMap.put("Ave_FPS_Amount",Math.round(fpsAmt) );
			}
			

		
			
			dataListThree.add(dataMap);

		}

		DateUtil.Order(dataListThree, "Ach");
		for (int i = 0; i < dataListThree.size(); i++) {

			dataListThree.get(i).put("RANK", i + 1);
		}
		JSONObject jsonObject=new JSONObject();
		jsonObject.accumulate("data",dataListThree);
		return jsonObject;
	}

	@Override
	public List<HashMap<String, Object>> selectXCPLine(
			Map<String, Object> whereMap) {
		return soTableDao.selectXCPLine(whereMap);
	}


	@Override
	public JSONObject selectGrowthByCountry(Map<String, Object> whereMap) {
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();

		
		String[] toYearBegin = whereMap.get("beginDate").toString().split("-");
		String[] toYearEnd = whereMap.get("endDate").toString().split("-");
		int laYear = Integer.parseInt(toYearBegin[0].toString()) - 1;
		whereMap.put("beginDate", laYear + "-" + toYearBegin[1] + "-"+ toYearBegin[2]);
		//whereMap.put("endDate", laYear + "-" + toYearEnd[1] + "-"+ toYearEnd[2]);
	
		
		List<HashMap<String, Object>> countryToDatas = soTableDao
				.selectGrowthByCountry(whereMap);
		
		
		/*List<HashMap<String, Object>> countryLaDatas = soTableDao
				.selectGrowthByCountry(whereMap);*/
		
		
		String year="";
		if(countryToDatas.size()>=1){
			year=countryToDatas.get(0).get("year").toString();
		}
		String month=whereMap.get("endDate").toString().split("-")[1];
		int [] valQty = getQty(12);
		int [] valAmt = getAmt(12);
		for (int i = 0; i < countryToDatas.size(); i++) {
			if(WebPageUtil.isStringNullAvaliable(year) && year.equals(countryToDatas.get(i).get("year").toString())){
				String date=countryToDatas.get(i).get("month").toString();
				int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
				BigDecimal bd=new BigDecimal(countryToDatas.get(i).get("qty").toString());
				int qty=(int)Math.round(bd.doubleValue());
				bd=new BigDecimal(countryToDatas.get(i).get("amt").toString());
				int amt=(int)Math.round(bd.doubleValue());
				valQty[dte -1] = qty;
				valAmt[dte -1] = amt;
				if(countryToDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("year",year);
					jsonObject.accumulate("arrQty", valQty);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
				}
				
			}else{

				JSONObject jsonObject=new JSONObject();
				jsonObject.accumulate("year",year);
				jsonObject.accumulate("arrQty", valQty);
				jsonObject.accumulate("arrAmt", valAmt);
				array.add(jsonObject);
				year = countryToDatas.get(i).get("year").toString();
				valQty = getQty(12);
				valAmt = getAmt(12);
				i--;
			}
		}
		
		
		
		JSONObject jsonObject=new JSONObject();
		jsonObject.accumulate("data",array);
		return jsonObject;
	}


	@Override
	public JSONObject selectDataByAreaChain(Map<String, Object> whereMap) {
		

		List<HashMap<String, Object>> bigReg = soTableDao
				.selectBigReg(whereMap);
		
		List<HashMap<String, Object>> re = soTableDao
				.selectRegionalHeadByParty(whereMap);
		
		List<HashMap<String, Object>> areaDatas = soTableDao
				.selectDataByAreaChain(whereMap);
		
		
		
		

		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		JSONArray arrayTT=new JSONArray();
		String party="";
		if(areaDatas.size()>=1){
			party=areaDatas.get(0).get("PARTY_ID").toString();
		}
		String month=whereMap.get("endDate").toString().split("-")[1];
		int [] val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		int [] valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		for (int i = 0; i < areaDatas.size(); i++) {
			if(WebPageUtil.isStringNullAvaliable(party) && party.equals(areaDatas.get(i).get("PARTY_ID").toString())){
				String date=areaDatas.get(i).get("month").toString();
				int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
				BigDecimal bd=new BigDecimal(areaDatas.get(i).get("saleQty").toString());
				int qty=(int)Math.round(bd.doubleValue());
				
				bd=new BigDecimal(areaDatas.get(i).get("saleSum").toString());
				int amt=(int)Math.round(bd.doubleValue());
				
				val[dte -1] = qty;
				valAmt[dte -1] = amt;
				if(areaDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("party",party);
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
				}
				
			}else{
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("party",party);
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
					party = areaDatas.get(i).get("PARTY_ID").toString();
					val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					i--;

				
			}
		}
		
		for (int a = 0; a < bigReg.size(); a++) {
			
			
			JSONObject dataMap = new JSONObject();
			for (int i = 0; i < re.size(); i++) {
				if (bigReg.get(a).get("PARTY_ID")
						.equals(re.get(i).get("PARTY_ID"))) {
					dataMap.put("RegionalHead", re.get(i).get("userName"));
				}
			}
			
			dataMap.put("AREA", bigReg.get(a).get("PARTY_NAME"));
			dataMap.put("NUM",a+1);
		
			
			int [] arrQty = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
			int [] arrAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj=(JSONObject) array.get(i);
				String pa=","+obj.get("party")+",";
				if(bigReg.get(a).get("small").toString().contains(pa)){
					JSONArray one=(JSONArray) obj.get("arrQty");
					JSONArray two= (JSONArray) obj.get("arrAmt");
					for (int j = 0; j < one.size(); j++) {
						arrQty[j]+=one.getInt(j);
					}
					for (int j = 0; j < two.size(); j++) {
						arrAmt[j]+=two.getInt(j);
					}
					
				
				
				}
			}
			
			
			BigDecimal amt=BigDecimal.ZERO;
			for (int i = 0; i < arrAmt.length; i++) {
				amt=amt.add(new BigDecimal(arrAmt[i]));
			}
			
			dataMap.put("Total",  amt);
			dataMap.put("arrQty", arrQty);
			dataMap.put("arrAmt", arrAmt);

			arrayTT.add(dataMap);

		}
		
		DateUtil.OrderAmt(arrayTT,"Total");
		
		object.accumulate("data", arrayTT);
		return object;
	}


	@Override
	public JSONObject selectSalemanDataByChain(Map<String, Object> whereMap) {
		
		
		List<HashMap<String, Object>> saleDatas = soTableDao
				.selectSalemanDataByChain(whereMap);

		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		
		String user="";
		if(saleDatas.size()>=1){
			user=saleDatas.get(0).get("userId").toString();
		}
		String month=whereMap.get("endDate").toString().split("-")[1];
		int [] val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		int [] valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		for (int i = 0; i < saleDatas.size(); i++) {
			if(WebPageUtil.isStringNullAvaliable(user) && user.equals(saleDatas.get(i).get("userId").toString())){
				String date=saleDatas.get(i).get("month").toString();
				int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
				BigDecimal bd=new BigDecimal(saleDatas.get(i).get("saleQty").toString());
				int qty=(int)Math.round(bd.doubleValue());
				
				bd=new BigDecimal(saleDatas.get(i).get("saleSum").toString());
				int amt=(int)Math.round(bd.doubleValue());
				
				val[dte -1] = qty;
				valAmt[dte -1] = amt;
				if(saleDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("user",user);
					jsonObject.accumulate("userName",saleDatas.get(i).get("userName").toString());
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
				}
				
			}else{
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("user",user);
					jsonObject.accumulate("userName",saleDatas.get(i).get("userName").toString());
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
					user = saleDatas.get(i).get("userId").toString();
					val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					i--;

				
			}
		}
		
	
		
		
		
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject=(JSONObject) array.get(i);
			jsonObject.accumulate("num",i+1 );
			JSONArray arrAmt=(JSONArray) jsonObject.get("arrAmt");
			BigDecimal amt=BigDecimal.ZERO;
			for (int j = 0; j < arrAmt.size(); j++) {
				amt=amt.add(new BigDecimal(arrAmt.get(j).toString()));
			}
			
			jsonObject.accumulate("Total",amt);
		}
		DateUtil.OrderAmt(array, "Total");
		object.accumulate("data", array);
		return object;
	}


	@Override
	public JSONObject selectAcfoDataByChain(Map<String, Object> whereMap) {
		List<HashMap<String, Object>> saleDatas = soTableDao
				.selectAcfoDataByChain(whereMap);

		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		List<HashMap<String, Object>> Area = soTableDao.selectAreaByUser(whereMap);
		String user="";
		if(saleDatas.size()>=1){
			user=saleDatas.get(0).get("userId").toString();
		}
		String month=whereMap.get("endDate").toString().split("-")[1];
		int [] val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		int [] valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		for (int i = 0; i < saleDatas.size(); i++) {
			if(WebPageUtil.isStringNullAvaliable(user) && user.equals(saleDatas.get(i).get("userId").toString())){
				String date=saleDatas.get(i).get("month").toString();
				int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
				BigDecimal bd=new BigDecimal(saleDatas.get(i).get("saleQty").toString());
				int qty=(int)Math.round(bd.doubleValue());
				
				bd=new BigDecimal(saleDatas.get(i).get("saleSum").toString());
				int amt=(int)Math.round(bd.doubleValue());
				
				val[dte -1] = qty;
				valAmt[dte -1] = amt;
				if(saleDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("user",user);
					jsonObject.accumulate("userName",saleDatas.get(i).get("userName").toString());
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
				
					
					
					array.add(jsonObject);
				}
				
			}else{
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("user",user);
					jsonObject.accumulate("userName",saleDatas.get(i).get("userName").toString());
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
					user = saleDatas.get(i).get("userId").toString();
					val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					i--;

				
			}
		}
		
		
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject=(JSONObject) array.get(i);
			jsonObject.accumulate("num",i+1 );
			JSONArray arrAmt=(JSONArray) jsonObject.get("arrAmt");
			BigDecimal amt=BigDecimal.ZERO;
			for (int j = 0; j < arrAmt.size(); j++) {
				amt=amt.add(new BigDecimal(arrAmt.get(j).toString()));
			}
			
			jsonObject.accumulate("Total", amt);
		}
		DateUtil.OrderAmt(array, "Total");
		object.accumulate("data", array);
		
		
		return object;
	}


	@Override
	public JSONObject selectCountryDataByChain(Map<String, Object> whereMap) {
		JSONArray array=new JSONArray();

	
		
		List<HashMap<String, Object>> countryToDatas = soTableDao
				.selectGrowthByCountry(whereMap);
		
		
		String year="";
		if(countryToDatas.size()>1){
			year=countryToDatas.get(0).get("year").toString();
		}
		String month=whereMap.get("endDate").toString().split("-")[1];
		
		int [] valQty = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		int [] valAmt = getAmt(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		for (int i = 0; i < countryToDatas.size(); i++) {
			if(WebPageUtil.isStringNullAvaliable(year) && year.equals(countryToDatas.get(i).get("year").toString())){
				String date=countryToDatas.get(i).get("month").toString();
				int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
				BigDecimal bd=new BigDecimal(countryToDatas.get(i).get("qty").toString());
				int qty=(int)Math.round(bd.doubleValue());
				bd=new BigDecimal(countryToDatas.get(i).get("amt").toString());
				int amt=(int)Math.round(bd.doubleValue());
				valQty[dte -1] = qty;
				valAmt[dte -1] = amt;
				if(countryToDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("year",year);
					jsonObject.accumulate("arrQty", valQty);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
				}
				
			}else{
				JSONObject jsonObject=new JSONObject();
				jsonObject.accumulate("year",year);
				jsonObject.accumulate("arrQty", valQty);
				jsonObject.accumulate("arrAmt", valAmt);
				array.add(jsonObject);
				year = countryToDatas.get(i).get("year").toString();
				valQty = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
				valAmt = getAmt(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
				i--;
			}
		}
		
		
		JSONObject jsonObject=new JSONObject();
		jsonObject.accumulate("data",array);
		return jsonObject;
	}


	@Override
	public List<HashMap<String, Object>> selectXCPModel(
			Map<String, Object> whereMap) {
		return soTableDao.selectXCPModel(whereMap);
	}


	@Override
	public JSONObject selectCountryBigByYear(Map<String, Object> whereMap) {

		
		List<HashMap<String, Object>> saleDatas = soTableDao
				.selectModelByMonth(whereMap);

		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		
		String model="";
		if(saleDatas.size()>=1){
			model=saleDatas.get(0).get("model").toString();
		}
		String month=whereMap.get("endDate").toString().split("-")[1];
		int [] val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		int [] valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		for (int i = 0; i < saleDatas.size(); i++) {
			if(WebPageUtil.isStringNullAvaliable(model) && model.equals(saleDatas.get(i).get("model").toString())){
				String date=saleDatas.get(i).get("month").toString();
				int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
				BigDecimal bd=new BigDecimal(saleDatas.get(i).get("qty").toString());
				int qty=(int)Math.round(bd.doubleValue());
				
				bd=new BigDecimal(saleDatas.get(i).get("amt").toString());
				int amt=(int)Math.round(bd.doubleValue());
				
				val[dte -1] = qty;
				valAmt[dte -1] = amt;
				if(saleDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("model",model);
					jsonObject.accumulate("size",saleDatas.get(i).get("size").toString());
					jsonObject.accumulate("spec",saleDatas.get(i).get("spec").toString());
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
				}
				
			}else{
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("model",model);
					jsonObject.accumulate("size",saleDatas.get(i-1).get("size").toString());
					jsonObject.accumulate("spec",saleDatas.get(i-1).get("spec").toString());
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
					model = saleDatas.get(i).get("model").toString();
					val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					i--;

				
			}
		}
		
		object.accumulate("data", array);
		return object;
	}


	@Override
	public List<HashMap<String, Object>> selectCurvedModel(
			Map<String, Object> whereMap) {
		return soTableDao.selectCurvedModel(whereMap);
	}


	@Override
	public XSSFWorkbook read2007Excel(File file,
			String uploadExcelFileName) {
		
		String conditions="";
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		
		if (!WebPageUtil.isHAdmin()) {
			if (null != userPartyIds && !"".equals(userPartyIds)) {
				conditions = "  pa.party_id in (" + userPartyIds + ")  ";
			} else {
				conditions = "  1=2  ";
			}
		} else {
			conditions = " 1=1 ";

		}
		

		 
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径
		XSSFWorkbook xwb = null;
		try {
			xwb = new XSSFWorkbook(new FileInputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		// 读取第一章表格内容
		XSSFSheet sheet = xwb.getSheetAt(0);
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		 XSSFSheet exSheet = workbook.createSheet(uploadExcelFileName);
		 
		 exSheet.createFreezePane(0,2,0,2)  ;
		// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
		 exSheet.addMergedRegion(new CellRangeAddress(0, 1,0, 0));
		 exSheet.addMergedRegion(new CellRangeAddress(0, 0,1, 2));
		 exSheet.addMergedRegion(new CellRangeAddress(0, 0,3, 4));
		 exSheet.addMergedRegion(new CellRangeAddress(0, 0,5, 6));
		 exSheet.setColumnWidth(0, 32 *200);  
		 exSheet.setColumnWidth(2, 32 *180);  
		 exSheet.setColumnWidth(4, 32 *180);  
		 exSheet.setColumnWidth(6, 32 *180);  
		 XSSFRow rowhead = exSheet.createRow(0);
         //表头数据
             XSSFCell cellHeadOne = rowhead.createCell(0);    
             cellHeadOne.setCellValue("门店");    
             
         
             XSSFCell cellHeadTwo = rowhead.createCell(1);    
             cellHeadTwo.setCellValue("系统");    
             
             XSSFCell cellHeadThree = rowhead.createCell(3);    
             cellHeadThree.setCellValue("MIS"); 
             
             
             XSSFCell cellHeadFour = rowhead.createCell(5);    
             cellHeadFour.setCellValue("差异(系统-MIS)"); 
             
             
             
            XSSFRow rowheadThree = exSheet.createRow(2);
             //表头数据
                  
                 int ro=1000 ;
                
                 XSSFCell cellHeadTwos = rowheadThree.createCell(1);    
                 cellHeadTwos.setCellValue("SUBTOTAL(9,B4:B" +  ro + ")");
                 cellHeadTwos.setCellType(Cell.CELL_TYPE_FORMULA);
                 cellHeadTwos.setCellFormula("SUBTOTAL(9,B4:B" +  ro + ")");
 				
                 
                 XSSFCell cellHeadThrees = rowheadThree.createCell(2);    
                 cellHeadThrees.setCellValue("SUBTOTAL(9,C4:C" +  ro + ")");
                 cellHeadThrees.setCellType(Cell.CELL_TYPE_FORMULA);
                 cellHeadThrees.setCellFormula("SUBTOTAL(9,C4:C" +  ro + ")");
 				
                 
                 
                 XSSFCell cellHeadFours = rowheadThree.createCell(3);    
                 cellHeadFours.setCellValue("SUBTOTAL(9,D4:D" +  ro + ")");
                 cellHeadFours.setCellType(Cell.CELL_TYPE_FORMULA);
                 cellHeadFours.setCellFormula("SUBTOTAL(9,D4:D" +  ro + ")");
 				
                 
                 XSSFCell cellHeadFives = rowheadThree.createCell(4);    
                 cellHeadFives.setCellValue("SUBTOTAL(9,E4:E" +  ro + ")");
                 cellHeadFives.setCellType(Cell.CELL_TYPE_FORMULA);
                 cellHeadFives.setCellFormula("SUBTOTAL(9,E4:E" +  ro + ")");
 				
                 
                 XSSFCell cellHeadSixs = rowheadThree.createCell(5);    
                 cellHeadSixs.setCellValue("SUBTOTAL(9,F4:F" +  ro + ")");
                 cellHeadSixs.setCellType(Cell.CELL_TYPE_FORMULA);
                 cellHeadSixs.setCellFormula("SUBTOTAL(9,F4:F" +  ro + ")");
 				
                 
                 XSSFCell cellHeadSevens = rowheadThree.createCell(6);    
                 cellHeadSevens.setCellValue("SUBTOTAL(9,G4:G" +  ro + ")");
                 cellHeadSevens.setCellType(Cell.CELL_TYPE_FORMULA);
                 cellHeadSevens.setCellFormula("SUBTOTAL(9,G4:G" +  ro + ")");
 				
                 
             
             XSSFRow rowHeadTwo = exSheet.createRow(1);
             String[] excelHeader = {"QTY","AMT","QTY","AMT","QTY","AMT"};
             
             //表头数据
             for (int i = 0; i < excelHeader.length; i++) {   
                 XSSFCell cellOne = rowHeadTwo.createCell(i+1);    
                 cellOne.setCellValue(excelHeader[i]); 
                 
             }   
             
             XSSFRow rowDate =sheet.getRow(0);
            String [] date= rowDate.getCell(3).getStringCellValue().split("-");
     		Map<String,Object> whereMap = new HashMap<String,Object>();
     		whereMap.put("beginDate", date[0]+"-"+date[1]+"-01");
     		whereMap.put("endDate", date[0]+"-"+date[1]+"-31");
     		whereMap.put("conditions",conditions);
     		//List<HashMap<String, Object>> data =soTableDao.selectSysByMis(whereMap);
     		
     		
     		XSSFCell cellHeadFive = rowhead.createCell(7);    
     		cellHeadFive.setCellValue(date[0]+"-"+date[1]); 
            
     		
    		List<HashMap<String, Object>> salerList =soTableDao.selectSysByMis(whereMap);

			HashMap<String, ArrayList<HashMap<String, Object>>> salerDatas = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < salerList.size(); m++) {
				if (salerDatas.get(salerList.get(m).get("shopName")
						.toString().toUpperCase()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(salerList.get(m));
					salerDatas.put(salerList.get(m).get("shopName")
							.toString().toUpperCase(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = salerDatas
							.get(salerList.get(m).get("shopName").toString().toUpperCase());
					modelList.add(salerList.get(m));
				}

			}
			LinkedList<HashMap<String, Object>> dataListTwo = new LinkedList<HashMap<String, Object>>();

			/*HashMap<String, ArrayList<*/HashMap<String, Object>/*>> */map = new /*HashMap<String, ArrayList<*/HashMap<String, Object>/*>>*/();
			XSSFRow rowOne =sheet.getRow(1);
			BigDecimal misQtySum=BigDecimal.ZERO;
			BigDecimal misAmtSum=BigDecimal.ZERO;
			String shopNames="";
			for (int i =1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row =sheet.getRow(i);
				
				
				if(row.getCell(0)!=null &&
						row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK 
						){
				
					shopNames=row.getCell(0).getStringCellValue().toUpperCase();
									

					BigDecimal misQty=null;
					BigDecimal misAmt=null;
				
					
					if(row.getCell(1)!=null &&
							row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						misQty=new BigDecimal(row.getCell(1).getNumericCellValue());

					}else{
						misQty=BigDecimal.ZERO;

					}
					
					
					if(row.getCell(2)!=null &&
							row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						 misAmt=new BigDecimal(row.getCell(2).getNumericCellValue());

					}else{
							misAmt=BigDecimal.ZERO;

					}
					
					
					if (map.get(shopNames) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						HashMap<String, Object> dataMap=new HashMap<String, Object> ();
						dataMap.put("shopName",shopNames);
						dataMap.put("qty", misQty);
						dataMap.put("amt", misAmt);
						modelList.add(dataMap);
						map.put(shopNames, modelList);
						dataListTwo.add(dataMap);
						misQtySum=BigDecimal.ZERO;
						misAmtSum=BigDecimal.ZERO;
					
					} else {
						ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) map
									.get(shopNames);
						
						for (int j = 0; j < data.size(); j++) {
							misQtySum=BigDecimal.ZERO;
							misAmtSum=BigDecimal.ZERO;
							BigDecimal sysQty=new BigDecimal(data.get(j).get("qty").toString());
							BigDecimal sysAmt=new BigDecimal(data.get(j).get("amt").toString());
							misQtySum=misQtySum.add(sysQty);
							misAmtSum=misAmtSum.add(sysAmt);
						}
						misQtySum=misQtySum.add(misQty);
						misAmtSum=misAmtSum.add(misAmt);
						
						for (int j = 0; j < dataListTwo.size(); j++) {
							HashMap<String, Object> dataMap=dataListTwo.get(j);
							if(dataMap.get("shopName").toString().equals(shopNames)){
								dataMap.put("qty", misQtySum);
								dataMap.put("amt", misAmtSum);

							}
							
						}
						/*HashMap<String, Object> dataMap=new HashMap<String, Object> ();
						dataMap.put("shopName",shopNames);
						
						dataMap.put("qty", misQtySum);
						dataMap.put("amt", misAmtSum);
						dataListTwo.add(dataMap);*/
					}
					
				}/*else{
					HashMap<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("qty", misQtySum);
					dataMap.put("amt", misAmtSum);
					dataMap.put("shopName", shopNames);
					dataListTwo.add(dataMap);
					rowOne =sheet.getRow(i);
					misQtySum=BigDecimal.ZERO;
					misAmtSum=BigDecimal.ZERO;
					i--;
					
				}*/
				
		        }

			
			System.out.println("==============="+dataListTwo);
			
			HashMap<String, ArrayList<HashMap<String, Object>>> salerDatasMIS = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < dataListTwo.size(); m++) {
				if (salerDatasMIS.get(dataListTwo.get(m).get("shopName")
						.toString().toUpperCase()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(dataListTwo.get(m));
					salerDatasMIS.put(dataListTwo.get(m).get("shopName")
							.toString().toUpperCase(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = salerDatasMIS
							.get(dataListTwo.get(m).get("shopName").toString().toUpperCase());
					modelList.add(dataListTwo.get(m));
				}

			}
			
		
			
			for (int i =0; i <dataListTwo.size(); i++) {
			HashMap<String, Object> dataMaps=dataListTwo.get(i);

					String shopName=dataMaps.get("shopName").toString().toUpperCase();
				
					
					if (salerDatas.get(shopName) != null) {
						ArrayList<HashMap<String, Object>> data = salerDatas
								.get(shopName);
						for (int j = 0; j < data.size(); j++) {
							BigDecimal sysQty=new BigDecimal(data.get(j).get("qty").toString());
							BigDecimal sysAmt=new BigDecimal(data.get(j).get("amt").toString());
							BigDecimal misQty=new BigDecimal(dataMaps.get("qty").toString());
							BigDecimal misAmt=new BigDecimal(dataMaps.get("amt").toString());

							
							
							XSSFRow exRow= exSheet.createRow(i+3); 
							 
				             XSSFCell cell = exRow.createCell(0);
				             cell.setCellValue(shopName);    
				             
				             XSSFCell cellTwo = exRow.createCell(1);
				             cellTwo.setCellValue(Math.round(sysQty.doubleValue()));
				             
				             XSSFCell cellThree = exRow.createCell(2);
				             cellThree.setCellValue(Math.round(sysAmt.doubleValue()));
				             
				             XSSFCell cellFour = exRow.createCell(3);
				             cellFour.setCellValue(Math.round(misQty.doubleValue()));
				             
				             
				             XSSFCell cellFive = exRow.createCell(4);
				             cellFive.setCellValue(Math.round(misAmt.doubleValue()));
				             
				             XSSFCell cellSix = exRow.createCell(5);
				             cellSix.setCellValue(Math.round(sysQty.doubleValue())-Math.round(misQty.doubleValue()));
				             
				             XSSFCell cellSeven = exRow.createCell(6);
				             cellSeven.setCellValue(Math.round(sysAmt.doubleValue())-Math.round(misAmt.doubleValue()));
						}

					}else  {
						BigDecimal misQty=new BigDecimal(dataMaps.get("qty").toString());
						BigDecimal misAmt=new BigDecimal(dataMaps.get("amt").toString());

						
						XSSFRow exRows= exSheet.createRow(i+3); 
			            XSSFCell cell = exRows.createCell(0);
			            cell.setCellValue(shopName);
			            
			            XSSFCell cellTwo = exRows.createCell(1);
			             cellTwo.setCellValue(0);
			             
			             
			             XSSFCell cellThree = exRows.createCell(2);
			             cellThree.setCellValue(0);
			             
			             XSSFCell cellFour = exRows.createCell(3);
			             cellFour.setCellValue(Math.round(misQty.doubleValue()));
			             
			             
			             XSSFCell cellFive = exRows.createCell(4);
			             cellFive.setCellValue(Math.round(misAmt.doubleValue()));
			             
			             XSSFCell cellSix = exRows.createCell(5);
			             cellSix.setCellValue(-Math.round(misQty.doubleValue()));
			             
			             XSSFCell cellSeven = exRows.createCell(6);
			             cellSeven.setCellValue(-Math.round(misAmt.doubleValue()));
			             
					}
					

				
				
			}
			
			
			int row=0;
			for (int j = 0; j < salerList.size(); j++) {
				HashMap<String, Object> dataMap=salerList.get(j);
				String shopName=dataMap.get("shopName").toString().toUpperCase();
				
				
				if (salerDatasMIS.get(shopName) == null) {
					row=row+1;
					BigDecimal sysQty=new BigDecimal(dataMap.get("qty").toString());
					BigDecimal sysAmt=new BigDecimal(dataMap.get("amt").toString());
					XSSFRow exRows= exSheet.createRow(dataListTwo.size()+3+row); 
		            XSSFCell cell = exRows.createCell(0);
		            cell.setCellValue(shopName);
		            
		            XSSFCell cellTwo = exRows.createCell(1);
		             cellTwo.setCellValue(Math.round(sysQty.doubleValue()));
		             
		             
		             XSSFCell cellThree = exRows.createCell(2);
		             cellThree.setCellValue(Math.round(sysAmt.doubleValue()));
		             
		             XSSFCell cellFour = exRows.createCell(3);
		             cellFour.setCellValue(0);
		             
		             
		             XSSFCell cellFive = exRows.createCell(4);
		             cellFive.setCellValue(0);
		             
		             XSSFCell cellSix = exRows.createCell(5);
		             cellSix.setCellValue(Math.round(sysQty.doubleValue()));
		             
		             XSSFCell cellSeven = exRows.createCell(6);
		             cellSeven.setCellValue(Math.round(sysAmt.doubleValue()));
					
				}
			}
			
		
		return workbook;
	}


	@Override
	public JSONObject selectMonthCountryTotal(
			Map<String, Object> whereMap) {
		JSONArray array=new JSONArray();
		String beginDate=whereMap.get("beginDate").toString();
		String endDate=whereMap.get("endDate").toString();
		
		try {
			LinkedList<HashMap<String, Object>> 	dateList=null;

			if((whereMap.get("country").toString().equals("5"))){
				dateList=DateUtil.getWeekByPH(beginDate, endDate);
			}else{
				dateList=DateUtil.getWeek(beginDate, endDate);
			}
			for (int i = 0; i < dateList.size(); i++) {
				JSONObject object=new JSONObject();
				

				whereMap.put("beginDate", dateList.get(i).get("beginDate").toString());
				whereMap.put("endDate", dateList.get(i).get("endDate").toString());
				List<HashMap<String, Object>>  data=soTableDao.selectMonthCountryTotal(whereMap);
				
				object.accumulate("week", "Week"+(i+1)+"("+ dateList.get(i).get("beginDate").toString().split("-")[2]+"-"+ dateList.get(i).get("endDate").toString().split("-")[2]+")");
				if(data.size()>0){
					BigDecimal qty=new BigDecimal(data.get(0).get("saleQty").toString());
					object.accumulate("arrQty", Math.round(qty.doubleValue()));
					qty=new BigDecimal(data.get(0).get("saleSum").toString());
					object.accumulate("arrAmt",Math.round(qty.doubleValue()));
				}else{
					object.accumulate("arrQty",0);
					object.accumulate("arrAmt", 0);
				}
				array.add(object);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

			JSONObject jsonObject=new JSONObject();
			jsonObject.accumulate("data",array);
			return jsonObject;
	}




	@Override
	public JSONObject selectAreaDataByMonth (Map<String, Object> whereMap) {
		JSONArray arrayTT=new JSONArray();
		try {
		String beginDate=whereMap.get("beginDate").toString();
		String endDate=whereMap.get("endDate").toString();
		

		List<HashMap<String, Object>> bigReg = soTableDao
				.selectBigReg(whereMap);
		
		List<HashMap<String, Object>> re = soTableDao
				.selectRegionalHeadByParty(whereMap);
		
		JSONArray array=new JSONArray();
	
		LinkedList<HashMap<String, Object>> 	dateList=null;

		if((whereMap.get("country").toString().equals("5"))){
			dateList=DateUtil.getWeekByPH(beginDate, endDate);
		}else{
			dateList=DateUtil.getWeek(beginDate, endDate);
		}
		List week=new ArrayList();

			for (int i = 0; i < dateList.size(); i++) {
				week.add("Week"+(i+1)+"("+ dateList.get(i).get("beginDate").toString().split("-")[2]+"-"+ dateList.get(i).get("endDate").toString().split("-")[2]+")");
				whereMap.put("beginDate", dateList.get(i).get("beginDate").toString());
				whereMap.put("endDate", dateList.get(i).get("endDate").toString());
				List<HashMap<String, Object>> areaDatas = soTableDao
						.selectAreaDataByMonth(whereMap);
				for (int j = 0; j < areaDatas.size(); j++) {
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("week",i+1);
					jsonObject.accumulate("party",areaDatas.get(j).get("PARTY_ID"));
					jsonObject.accumulate("partyName",areaDatas.get(j).get("AREA"));
					BigDecimal qty=new BigDecimal(areaDatas.get(j).get("saleQty").toString());
					jsonObject.accumulate("saleQty",Math.round(qty.doubleValue()) );
					qty=new BigDecimal(areaDatas.get(j).get("saleSum").toString());
					jsonObject.accumulate("saleSum", Math.round(qty.doubleValue()));
					array.add(jsonObject);
					
				}
			}
			
			DateUtil.OrderName(array, "partyName");

			
			JSONArray arrayTwo=new JSONArray();
			String party="";
			if(array.size()>=1){
				party=array.getJSONObject(0).getString("party");
			}
			int [] val = getQty(dateList.size());
			int [] valAmt = getQty(dateList.size());
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj=array.getJSONObject(i);
				if(WebPageUtil.isStringNullAvaliable(party) && party.equals(obj.get("party").toString())){
					String date=obj.getString("week");
					int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
					BigDecimal bd=new BigDecimal(obj.get("saleQty").toString());
					int qty=(int)Math.round(bd.doubleValue());
					
					bd=new BigDecimal(obj.get("saleSum").toString());
					int amt=(int)Math.round(bd.doubleValue());
					
					val[dte -1] = qty;
					valAmt[dte -1] = amt;
					if(array.size() - i == 1){
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("party",party);
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						arrayTwo.add(jsonObject);
					}
					
				}else{
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("party",party);
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						arrayTwo.add(jsonObject);
						party =obj.get("party").toString();
						val = getQty(dateList.size());
						valAmt = getQty(dateList.size());
						i--;

					
				}
			}
			
			
			


		
		
		for (int a = 0; a < bigReg.size(); a++) {
			
			
			JSONObject dataMap = new JSONObject();
			for (int i = 0; i < re.size(); i++) {
				if (bigReg.get(a).get("PARTY_ID")
						.equals(re.get(i).get("PARTY_ID"))) {
					dataMap.put("RegionalHead", re.get(i).get("userName"));
				}
			}
			
			dataMap.put("AREA", bigReg.get(a).get("PARTY_NAME"));
			dataMap.put("NUM",a+1);
		
			
			
			int [] qty = getQty(dateList.size());
			int [] amt = getQty(dateList.size());
			for (int i = 0; i < arrayTwo.size(); i++) {
				JSONObject obj=(JSONObject) arrayTwo.get(i);
				String pa=","+obj.get("party")+",";
				if(bigReg.get(a).get("small").toString().contains(pa)){
					JSONArray one=(JSONArray) obj.get("arrQty");
					JSONArray two= (JSONArray) obj.get("arrAmt");
					for (int j = 0; j < one.size(); j++) {
							qty[j]+=one.getInt(j);
					}
					for (int j = 0; j < two.size(); j++) {
						
						amt[j]+=two.getInt(j);
					}
					
				
				
				}
			}
			
			
			BigDecimal amts=BigDecimal.ZERO;
			for (int i = 0; i < amt.length; i++) {
				amts=amts.add(new BigDecimal(amt[i]));
			}
			
			dataMap.put("Total", amts);
		
			dataMap.put("week", week);
			
			dataMap.put("arrQty", qty);
			dataMap.put("arrAmt", amt);

			arrayTT.add(dataMap);

		}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject object=new JSONObject();
		DateUtil.OrderAmt(arrayTT, "Total");
		object.accumulate("data", arrayTT);
		return object;
		
	}


	@Override
	public JSONObject selectMonthCountryXCP(Map<String, Object> whereMap) {
		JSONArray arrayTwo=new JSONArray();
		try {
		String beginDate=whereMap.get("beginDate").toString();
		String endDate=whereMap.get("endDate").toString();


		JSONArray array=new JSONArray();
	
		LinkedList<HashMap<String, Object>> 	dateList=null;

		if((whereMap.get("country").toString().equals("5"))){
			dateList=DateUtil.getWeekByPH(beginDate, endDate);
		}else{
			dateList=DateUtil.getWeek(beginDate, endDate);
		}

		
		List week=new ArrayList();
			for (int i = 0; i < dateList.size(); i++) {
				week.add( "Week"+(i+1)+"("+ dateList.get(i).get("beginDate").toString().split("-")[2]+"-"+ dateList.get(i).get("endDate").toString().split("-")[2]+")");

				whereMap.put("beginDate", dateList.get(i).get("beginDate").toString());
				whereMap.put("endDate", dateList.get(i).get("endDate").toString());
				List<HashMap<String, Object>> areaDatas = soTableDao
						.selectXCPModelByMonth(whereMap);
				for (int j = 0; j < areaDatas.size(); j++) {
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("week",i+1);
					jsonObject.accumulate("model",areaDatas.get(j).get("model"));
					BigDecimal qty=new BigDecimal(areaDatas.get(j).get("qty").toString());
					jsonObject.accumulate("saleQty",Math.round(qty.doubleValue()) );
					qty=new BigDecimal(areaDatas.get(j).get("amt").toString());
					jsonObject.accumulate("saleSum", Math.round(qty.doubleValue()));
					jsonObject.accumulate("spec",areaDatas.get(j).get("spec"));
					jsonObject.accumulate("size",areaDatas.get(j).get("size"));
					array.add(jsonObject);
					
				}
			}
			
			DateUtil.OrderName(array, "model");

			
			String model="";
			if(array.size()>=1){
				model=array.getJSONObject(0).getString("model");
			}
			int [] val = getQty(dateList.size());
			int [] valAmt = getQty(dateList.size());
		
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj=array.getJSONObject(i);
				if(WebPageUtil.isStringNullAvaliable(model) && model.equals(obj.get("model").toString())){
					String date=obj.getString("week");
					int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
					BigDecimal bd=new BigDecimal(obj.get("saleQty").toString());
					int qty=(int)Math.round(bd.doubleValue());
					
					bd=new BigDecimal(obj.get("saleSum").toString());
					int amt=(int)Math.round(bd.doubleValue());
					val[dte -1] = qty;
					valAmt[dte -1] = amt;
					if(array.size() - i == 1){
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("model",model);
						jsonObject.accumulate("spec",obj.get("spec"));
						jsonObject.accumulate("size",obj.get("size"));
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						jsonObject.accumulate("week", week);
						arrayTwo.add(jsonObject);
					}
					
				}else{
						obj=array.getJSONObject(i-1);
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("model",model);
						jsonObject.accumulate("spec",obj.get("spec"));
						jsonObject.accumulate("size",obj.get("size"));
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						jsonObject.accumulate("week", week);
						arrayTwo.add(jsonObject);
						obj=array.getJSONObject(i);
						model =obj.get("model").toString();
						val = getQty(dateList.size());
						valAmt = getQty(dateList.size());
						
						i--;

					
				}
			}
			
			
			


		
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject object=new JSONObject();
		object.accumulate("data", arrayTwo);
		return object;
		
	}


	@Override
	public JSONObject selectSalemanDataByMonth(Map<String, Object> whereMap) {
		JSONArray arrayTwo=new JSONArray();
		try {
		String beginDate=whereMap.get("beginDate").toString();
		String endDate=whereMap.get("endDate").toString();


		JSONArray array=new JSONArray();
	
		LinkedList<HashMap<String, Object>> 	dateList=null;

		if((whereMap.get("country").toString().equals("5"))){
			dateList=DateUtil.getWeekByPH(beginDate, endDate);
		}else{
			dateList=DateUtil.getWeek(beginDate, endDate);
		}

		
		List week=new ArrayList();
			for (int i = 0; i < dateList.size(); i++) {
				week.add( "Week"+(i+1)+"("+ dateList.get(i).get("beginDate").toString().split("-")[2]+"-"+ dateList.get(i).get("endDate").toString().split("-")[2]+")");

				whereMap.put("beginDate", dateList.get(i).get("beginDate").toString());
				whereMap.put("endDate", dateList.get(i).get("endDate").toString());
				List<HashMap<String, Object>> areaDatas = soTableDao
						.selectSalemanDataByMonth(whereMap);
				for (int j = 0; j < areaDatas.size(); j++) {
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("week",i+1);
					jsonObject.accumulate("userId",areaDatas.get(j).get("userId"));
					jsonObject.accumulate("userName",areaDatas.get(j).get("userName"));
					BigDecimal qty=new BigDecimal(areaDatas.get(j).get("saleQty").toString());
					jsonObject.accumulate("saleQty",Math.round(qty.doubleValue()) );
					qty=new BigDecimal(areaDatas.get(j).get("saleSum").toString());
					jsonObject.accumulate("saleSum", Math.round(qty.doubleValue()));
					
					array.add(jsonObject);
					
				}
			}
			
			DateUtil.OrderName(array, "userName");

			
			String user="";
			if(array.size()>=1){
				user=array.getJSONObject(0).getString("userId");
			}
			int [] val = getQty(dateList.size());
			int [] valAmt = getQty(dateList.size());
			System.out.println(array);
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj=array.getJSONObject(i);
				if(WebPageUtil.isStringNullAvaliable(user) && user.equals(obj.get("userId").toString())){
					String date=obj.getString("week");
					int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
					BigDecimal bd=new BigDecimal(obj.get("saleQty").toString());
					int qty=(int)Math.round(bd.doubleValue());
					
					bd=new BigDecimal(obj.get("saleSum").toString());
					int amt=(int)Math.round(bd.doubleValue());
					val[dte -1] = qty;
					valAmt[dte -1] = amt;
					if(array.size() - i == 1){
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("userName",obj.get("userName"));
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						jsonObject.accumulate("week", week);
						arrayTwo.add(jsonObject);
					}
					
				}else{
						obj=array.getJSONObject(i-1);
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("userName",obj.get("userName"));
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						jsonObject.accumulate("week", week);
						arrayTwo.add(jsonObject);
						obj=array.getJSONObject(i);
						user =obj.get("userId").toString();
						val = getQty(dateList.size());
						valAmt = getQty(dateList.size());
						
						i--;
						

					
				}
			}
			
			
			


		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < arrayTwo.size(); i++) {
			JSONObject jsonObject=(JSONObject) arrayTwo.get(i);
			jsonObject.accumulate("NUM",i+1 );
			JSONArray arrAmt=(JSONArray) jsonObject.get("arrAmt");
			BigDecimal amt=BigDecimal.ZERO;
			for (int j = 0; j < arrAmt.size(); j++) {
				amt=amt.add(new BigDecimal(arrAmt.get(j).toString()));
			}
			jsonObject.accumulate("Total", amt);

		}
		JSONObject object=new JSONObject();
		DateUtil.OrderAmt(arrayTwo, "Total");
		object.accumulate("data", arrayTwo);
	
		return object;
	}


	@Override
	public JSONObject selectAcfoDataByMonth(Map<String, Object> whereMap) {
		JSONArray arrayTwo=new JSONArray();
		try {
		String beginDate=whereMap.get("beginDate").toString();
		String endDate=whereMap.get("endDate").toString();


		JSONArray array=new JSONArray();
	
		LinkedList<HashMap<String, Object>> 	dateList=null;
	
		if((whereMap.get("country").toString().equals("5"))){
			dateList=DateUtil.getWeekByPH(beginDate, endDate);
		}else{
			dateList=DateUtil.getWeek(beginDate, endDate);
		}

		
		List week=new ArrayList();
			for (int i = 0; i < dateList.size(); i++) {
				week.add( "Week"+(i+1)+"("+ dateList.get(i).get("beginDate").toString().split("-")[2]+"-"+ dateList.get(i).get("endDate").toString().split("-")[2]+")");

				whereMap.put("beginDate", dateList.get(i).get("beginDate").toString());
				whereMap.put("endDate", dateList.get(i).get("endDate").toString());
				List<HashMap<String, Object>> areaDatas = soTableDao
						.selectAcfoDataByMonth(whereMap);
				for (int j = 0; j < areaDatas.size(); j++) {
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("week",i+1);
					jsonObject.accumulate("userId",areaDatas.get(j).get("userId"));
					jsonObject.accumulate("userName",areaDatas.get(j).get("userName"));
					BigDecimal qty=new BigDecimal(areaDatas.get(j).get("saleQty").toString());
					jsonObject.accumulate("saleQty",Math.round(qty.doubleValue()) );
					qty=new BigDecimal(areaDatas.get(j).get("saleSum").toString());
					jsonObject.accumulate("saleSum", Math.round(qty.doubleValue()));
					
					array.add(jsonObject);
					
				}
			}
			
			DateUtil.OrderName(array, "userName");

			
			String user="";
			if(array.size()>=1){
				user=array.getJSONObject(0).getString("userId");
			}
			int [] val = getQty(dateList.size());
			int [] valAmt = getQty(dateList.size());
		
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj=array.getJSONObject(i);
				if(WebPageUtil.isStringNullAvaliable(user) && user.equals(obj.get("userId").toString())){
					String date=obj.getString("week");
					int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
					BigDecimal bd=new BigDecimal(obj.get("saleQty").toString());
					int qty=(int)Math.round(bd.doubleValue());
					
					bd=new BigDecimal(obj.get("saleSum").toString());
					int amt=(int)Math.round(bd.doubleValue());
					val[dte -1] = qty;
					valAmt[dte -1] = amt;
					if(array.size() - i == 1){
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("userName",obj.get("userName"));
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						jsonObject.accumulate("week", week);
						arrayTwo.add(jsonObject);
					}
					
				}else{
						obj=array.getJSONObject(i-1);
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("userName",obj.get("userName"));
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						jsonObject.accumulate("week", week);
						arrayTwo.add(jsonObject);
						obj=array.getJSONObject(i);
						user =obj.get("userId").toString();
						val = getQty(dateList.size());
						valAmt = getQty(dateList.size());
						
						i--;

					
				}
			}
			
			
			


		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	
		
		
		for (int i = 0; i < arrayTwo.size(); i++) {
			JSONObject jsonObject=(JSONObject) arrayTwo.get(i);
			jsonObject.accumulate("NUM",i+1 );
			JSONArray arrAmt=(JSONArray) jsonObject.get("arrAmt");
			BigDecimal amt=BigDecimal.ZERO;
			for (int j = 0; j < arrAmt.size(); j++) {
				amt=amt.add(new BigDecimal(arrAmt.get(j).toString()));
			}
			jsonObject.accumulate("Total", amt);

		}
		JSONObject object=new JSONObject();
		DateUtil.OrderAmt(arrayTwo, "Total");
		object.accumulate("data", arrayTwo);
		return object;
	}


	@Override
	public JSONObject selectHQDataByYear(Map<String, Object> whereMap) {
		List<HashMap<String, Object>> hqDatas = soTableDao.selectCountryDataByHQ(whereMap);

		List<HashMap<String, Object>> targetDatas = soTableDao.selectTargetDataByHQ(whereMap);

		
		for (int i = 0; i < hqDatas.size(); i++) {
			for (int j = 0; j < targetDatas.size(); j++) {
				if(hqDatas.get(i).get("country_id").toString().equals(targetDatas.get(j).get("country").toString())
						&& hqDatas.get(i).get("datadate").toString().equals(targetDatas.get(j).get("DATE").toString())
						){
					hqDatas.get(i).put("targetQty", targetDatas.get(j).get("qty"));
				}
			}
		}
		
		JSONArray array=new JSONArray();
		String country="";
		if(hqDatas.size()>=1){
			country=hqDatas.get(0).get("country").toString();
		}
		BigDecimal saleQtyYear=BigDecimal.ZERO;
		BigDecimal saleQtyH1=BigDecimal.ZERO;
		BigDecimal saleQtyH2=BigDecimal.ZERO;
		
		BigDecimal TargetQtyYear=BigDecimal.ZERO;
		BigDecimal TargetQtyH1=BigDecimal.ZERO;
		BigDecimal TargetQtyH2=BigDecimal.ZERO;
		
		
		for (int i = 0; i < hqDatas.size(); i++) {
			HashMap<String, Object> obj=hqDatas.get(i);
			if(WebPageUtil.isStringNullAvaliable(country) && country.equals(obj.get("country").toString())){
				
				int month =Integer.parseInt(obj.get("datadate").toString().split("-")[1]);
				
				BigDecimal bdQty=new BigDecimal(obj.get("saleQty").toString());
				BigDecimal bdTarget=new BigDecimal(obj.get("targetQty").toString());
				saleQtyYear=saleQtyYear.add(bdQty);
				TargetQtyYear=TargetQtyYear.add(bdTarget);
				if(month<=6){
					saleQtyH1=saleQtyH1.add(bdQty);
					TargetQtyH1=TargetQtyH1.add(bdTarget);
				}else{
					saleQtyH2=saleQtyH2.add(bdQty);
					TargetQtyH2=TargetQtyH2.add(bdTarget);
				}
				
				if(hqDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQtyYear", Math.round(saleQtyYear.doubleValue()));
					jsonObject.accumulate("TargetQtyYear", Math.round(TargetQtyYear.doubleValue()));
					if(Math.round(TargetQtyYear.doubleValue())==0){
						jsonObject.accumulate("achYear",100+"%");
						
					}else{
						jsonObject.accumulate("achYear", Math.round(saleQtyYear.doubleValue()/TargetQtyYear.doubleValue()*100)+"%");

					}
					
					jsonObject.accumulate("saleQtyH1", Math.round(saleQtyH1.doubleValue()));
					jsonObject.accumulate("TargetQtyH1", Math.round(TargetQtyH1.doubleValue()));

					if(Math.round(TargetQtyH1.doubleValue())==0){
						jsonObject.accumulate("achH1",100+"%");
						
					}else{
						jsonObject.accumulate("achH1", Math.round(saleQtyH1.doubleValue()/TargetQtyH1.doubleValue()*100)+"%");

					}
					
					jsonObject.accumulate("saleQtyH2", Math.round(saleQtyH2.doubleValue()));
					jsonObject.accumulate("TargetQtyH2", Math.round(TargetQtyH2.doubleValue()));

					if(Math.round(TargetQtyH2.doubleValue())==0){
						jsonObject.accumulate("achH2", 100+"%");
						
					}else{
						jsonObject.accumulate("achH2", Math.round(saleQtyH2.doubleValue()/TargetQtyH2.doubleValue()*100)+"%");

					}
					
					array.add(jsonObject);
				}
				
			}else{
					obj=hqDatas.get(i-1);
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQtyYear", Math.round(saleQtyYear.doubleValue()));
					jsonObject.accumulate("TargetQtyYear", Math.round(TargetQtyYear.doubleValue()));
					if(Math.round(TargetQtyYear.doubleValue())==0){
						jsonObject.accumulate("achYear",100+"%");
						
					}else{
						jsonObject.accumulate("achYear", Math.round(saleQtyYear.doubleValue()/TargetQtyYear.doubleValue()*100)+"%");

					}
					
					jsonObject.accumulate("saleQtyH1", Math.round(saleQtyH1.doubleValue()));
					jsonObject.accumulate("TargetQtyH1", Math.round(TargetQtyH1.doubleValue()));

					if(Math.round(TargetQtyH1.doubleValue())==0){
						jsonObject.accumulate("achH1",100+"%");
						
					}else{
						jsonObject.accumulate("achH1", Math.round(saleQtyH1.doubleValue()/TargetQtyH1.doubleValue()*100)+"%");

					}
					
					jsonObject.accumulate("saleQtyH2", Math.round(saleQtyH2.doubleValue()));
					jsonObject.accumulate("TargetQtyH2", Math.round(TargetQtyH2.doubleValue()));

					if(Math.round(TargetQtyH2.doubleValue())==0){
						jsonObject.accumulate("achH2", 100+"%");
						
					}else{
						jsonObject.accumulate("achH2", Math.round(saleQtyH2.doubleValue()/TargetQtyH2.doubleValue()*100)+"%");

					}
					array.add(jsonObject);
					obj=hqDatas.get(i);
					country =obj.get("country").toString();
					saleQtyYear=BigDecimal.ZERO;
					saleQtyH1=BigDecimal.ZERO;
					saleQtyH2=BigDecimal.ZERO;
					
					TargetQtyYear=BigDecimal.ZERO;
					TargetQtyH1=BigDecimal.ZERO;
					TargetQtyH2=BigDecimal.ZERO;
					
					i--;

				
			}
		}

		JSONObject object=new JSONObject();
		DateUtil.OrderName(array, "center");
		object.accumulate("data",  array);
		return object;
	}


	@Override
	public JSONObject selectHQDataByHalf(Map<String, Object> whereMap) {

		String [] begin=whereMap.get("beginDate").toString().split("-");
		String hlaf="";
		if(Integer.parseInt(begin[1])==1){
			hlaf="H1";
		}else{
			hlaf="H2";
		}
		
		List<HashMap<String, Object>> hqDatas = soTableDao.selectCountryDataByHQ(whereMap);
		List<HashMap<String, Object>> targetDatas = soTableDao.selectTargetDataByHQ(whereMap);

		
		for (int i = 0; i < hqDatas.size(); i++) {
			for (int j = 0; j < targetDatas.size(); j++) {
				if(hqDatas.get(i).get("country_id").toString().equals(targetDatas.get(j).get("country").toString())
						&& hqDatas.get(i).get("datadate").toString().equals(targetDatas.get(j).get("DATE").toString())
						){
					hqDatas.get(i).put("targetQty", targetDatas.get(j).get("qty"));
				}
			}
		}
	
		JSONArray array=new JSONArray();
		String country="";
		if(hqDatas.size()>=1){
			country=hqDatas.get(0).get("country").toString();
		}
		BigDecimal saleQtyYear=BigDecimal.ZERO;
		BigDecimal saleQtyH1=BigDecimal.ZERO;
		BigDecimal saleQtyH2=BigDecimal.ZERO;
		
		BigDecimal TargetQtyYear=BigDecimal.ZERO;
		BigDecimal TargetQtyH1=BigDecimal.ZERO;
		BigDecimal TargetQtyH2=BigDecimal.ZERO;
		
		
		for (int i = 0; i < hqDatas.size(); i++) {
			HashMap<String, Object> obj=hqDatas.get(i);
			if(WebPageUtil.isStringNullAvaliable(country) && country.equals(obj.get("country").toString())){
				
				int month =Integer.parseInt(obj.get("datadate").toString().split("-")[1]);
				
				BigDecimal bdQty=new BigDecimal(obj.get("saleQty").toString());
				BigDecimal bdTarget=new BigDecimal(obj.get("targetQty").toString());
				saleQtyYear=saleQtyYear.add(bdQty);
				TargetQtyYear=TargetQtyYear.add(bdTarget);
				if(hlaf.equals("H1")){
					if(month<=3){
						saleQtyH1=saleQtyH1.add(bdQty);
						TargetQtyH1=TargetQtyH1.add(bdTarget);
					}else{
						saleQtyH2=saleQtyH2.add(bdQty);
						TargetQtyH2=TargetQtyH2.add(bdTarget);
					}
				}else{
					if(month<=9){
						saleQtyH1=saleQtyH1.add(bdQty);
						TargetQtyH1=TargetQtyH1.add(bdTarget);
					}else{
						saleQtyH2=saleQtyH2.add(bdQty);
						TargetQtyH2=TargetQtyH2.add(bdTarget);
					}
				}
				
				
				if(hqDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQtyYear", Math.round(saleQtyYear.doubleValue()));
					jsonObject.accumulate("TargetQtyYear", Math.round(TargetQtyYear.doubleValue()));
					if(Math.round(TargetQtyYear.doubleValue())==0){
						jsonObject.accumulate("achYear",100+"%");
						
					}else{
						jsonObject.accumulate("achYear", Math.round(saleQtyYear.doubleValue()/TargetQtyYear.doubleValue()*100)+"%");

					}
					
					jsonObject.accumulate("saleQtyH1", Math.round(saleQtyH1.doubleValue()));
					jsonObject.accumulate("TargetQtyH1", Math.round(TargetQtyH1.doubleValue()));

					if(Math.round(TargetQtyH1.doubleValue())==0){
						jsonObject.accumulate("achH1",100+"%");
						
					}else{
						jsonObject.accumulate("achH1", Math.round(saleQtyH1.doubleValue()/TargetQtyH1.doubleValue()*100)+"%");

					}
					
					jsonObject.accumulate("saleQtyH2", Math.round(saleQtyH2.doubleValue()));
					jsonObject.accumulate("TargetQtyH2", Math.round(TargetQtyH2.doubleValue()));

					if(Math.round(TargetQtyH2.doubleValue())==0){
						jsonObject.accumulate("achH2", 100+"%");
						
					}else{
						jsonObject.accumulate("achH2", Math.round(saleQtyH2.doubleValue()/TargetQtyH2.doubleValue()*100)+"%");

					}
			
					array.add(jsonObject);
				}
				
			}else{
					obj=hqDatas.get(i-1);
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQtyYear", Math.round(saleQtyYear.doubleValue()));
					jsonObject.accumulate("TargetQtyYear", Math.round(TargetQtyYear.doubleValue()));
					if(Math.round(TargetQtyYear.doubleValue())==0){
						jsonObject.accumulate("achYear",100+"%");
						
					}else{
						jsonObject.accumulate("achYear", Math.round(saleQtyYear.doubleValue()/TargetQtyYear.doubleValue()*100)+"%");

					}
					
					jsonObject.accumulate("saleQtyH1", Math.round(saleQtyH1.doubleValue()));
					jsonObject.accumulate("TargetQtyH1", Math.round(TargetQtyH1.doubleValue()));

					if(Math.round(TargetQtyH1.doubleValue())==0){
						jsonObject.accumulate("achH1",100+"%");
						
					}else{
						jsonObject.accumulate("achH1", Math.round(saleQtyH1.doubleValue()/TargetQtyH1.doubleValue()*100)+"%");

					}
					
					jsonObject.accumulate("saleQtyH2", Math.round(saleQtyH2.doubleValue()));
					jsonObject.accumulate("TargetQtyH2", Math.round(TargetQtyH2.doubleValue()));

					if(Math.round(TargetQtyH2.doubleValue())==0){
						jsonObject.accumulate("achH2", 100+"%");
						
					}else{
						jsonObject.accumulate("achH2", Math.round(saleQtyH2.doubleValue()/TargetQtyH2.doubleValue()*100)+"%");

					}
					array.add(jsonObject);
					obj=hqDatas.get(i);
					country =obj.get("country").toString();
					saleQtyYear=BigDecimal.ZERO;
					saleQtyH1=BigDecimal.ZERO;
					saleQtyH2=BigDecimal.ZERO;
					
					TargetQtyYear=BigDecimal.ZERO;
					TargetQtyH1=BigDecimal.ZERO;
					TargetQtyH2=BigDecimal.ZERO;
					
					i--;

				
			}
		}

		JSONObject object=new JSONObject();
		DateUtil.OrderName(array, "center");
		object.accumulate("data",  array);
		return object;
	}


	@Override
	public JSONObject selectHQDataByQuarter(Map<String, Object> whereMap) {
		List<HashMap<String, Object>> hqDatas = soTableDao.selectCountryDataByHQ(whereMap);
		List<HashMap<String, Object>> targetDatas = soTableDao.selectTargetDataByHQ(whereMap);

		
		for (int i = 0; i < hqDatas.size(); i++) {
			for (int j = 0; j < targetDatas.size(); j++) {
				if(hqDatas.get(i).get("country_id").toString().equals(targetDatas.get(j).get("country").toString())
						&& hqDatas.get(i).get("datadate").toString().equals(targetDatas.get(j).get("DATE").toString())
						){
					hqDatas.get(i).put("targetQty", targetDatas.get(j).get("qty"));
				}
			}
		}
		JSONArray array=new JSONArray();
		
		BigDecimal saleQty=BigDecimal.ZERO;
		BigDecimal saleQty1=BigDecimal.ZERO;
		BigDecimal saleQty2=BigDecimal.ZERO;
		BigDecimal saleQty3=BigDecimal.ZERO;
		
		BigDecimal TargetQty=BigDecimal.ZERO;
		BigDecimal TargetQty1=BigDecimal.ZERO;
		BigDecimal TargetQty2=BigDecimal.ZERO;
		BigDecimal TargetQty3=BigDecimal.ZERO;
		
		String country="";
		if(hqDatas.size()>=1){
			country=hqDatas.get(0).get("country").toString();
		}
	
		
		
		for (int i = 0; i < hqDatas.size(); i++) {
			HashMap<String, Object> obj=hqDatas.get(i);
			if(WebPageUtil.isStringNullAvaliable(country) && country.equals(obj.get("country").toString())){
				
				int month =Integer.parseInt(obj.get("datadate").toString().split("-")[1]);
				
				BigDecimal bdQty=new BigDecimal(obj.get("saleQty").toString());
				BigDecimal bdTarget=new BigDecimal(obj.get("targetQty").toString());
				saleQty=saleQty.add(bdQty);
				TargetQty=TargetQty.add(bdTarget);
				if(month==1 || month==4 || month==7 || month==10 ){
						saleQty1=saleQty1.add(bdQty);
						TargetQty1=TargetQty1.add(bdTarget);

				}else if(month==2 || month==5 || month==8 || month==11 ){
					saleQty2=saleQty2.add(bdQty);
					TargetQty2=TargetQty2.add(bdTarget);

				}else if(month==3 || month==6 || month==9 || month==12 ){
					saleQty3=saleQty3.add(bdQty);
					TargetQty3=TargetQty3.add(bdTarget);

				}
				
				
				if(hqDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQty", Math.round(saleQty.doubleValue()));
					jsonObject.accumulate("TargetQty", Math.round(TargetQty.doubleValue()));
					if(Math.round(TargetQty.doubleValue())==0){
						jsonObject.accumulate("ach", 100+"%");
					}else{
						jsonObject.accumulate("ach", Math.round(saleQty.doubleValue()/TargetQty.doubleValue()*100)+"%");
					}

					jsonObject.accumulate("saleQty1", Math.round(saleQty1.doubleValue()));
					jsonObject.accumulate("TargetQty1", Math.round(TargetQty1.doubleValue()));

					
					if(Math.round(TargetQty1.doubleValue())==0){
						jsonObject.accumulate("ach1",100+"%");
					}else{
						jsonObject.accumulate("ach1", Math.round(saleQty1.doubleValue()/TargetQty1.doubleValue()*100)+"%");
					}
					
					
					jsonObject.accumulate("saleQty2", Math.round(saleQty2.doubleValue()));
					jsonObject.accumulate("TargetQty2", Math.round(TargetQty2.doubleValue()));
					if(Math.round(TargetQty2.doubleValue())==0){
						jsonObject.accumulate("ach2", 100+"%");
					}else{
						jsonObject.accumulate("ach2", Math.round(saleQty2.doubleValue()/TargetQty2.doubleValue()*100)+"%");
					}
					
					
					jsonObject.accumulate("saleQty3", Math.round(saleQty3.doubleValue()));
					jsonObject.accumulate("TargetQty3", Math.round(TargetQty3.doubleValue()));

					if(Math.round(TargetQty3.doubleValue())==0){
						jsonObject.accumulate("ach3", 100+"%");
					}else{
						jsonObject.accumulate("ach3", Math.round(saleQty3.doubleValue()/TargetQty3.doubleValue()*100)+"%");
					}
			
					array.add(jsonObject);
				}
				
			}else{
					obj=hqDatas.get(i-1);
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQty", Math.round(saleQty.doubleValue()));
					jsonObject.accumulate("TargetQty", Math.round(TargetQty.doubleValue()));
					if(Math.round(TargetQty.doubleValue())==0){
						jsonObject.accumulate("ach", 100+"%");
					}else{
						jsonObject.accumulate("ach", Math.round(saleQty.doubleValue()/TargetQty.doubleValue()*100)+"%");
					}

					jsonObject.accumulate("saleQty1", Math.round(saleQty1.doubleValue()));
					jsonObject.accumulate("TargetQty1", Math.round(TargetQty1.doubleValue()));

					
					if(Math.round(TargetQty1.doubleValue())==0){
						jsonObject.accumulate("ach1",100+"%");
					}else{
						jsonObject.accumulate("ach1", Math.round(saleQty1.doubleValue()/TargetQty1.doubleValue()*100)+"%");
					}
					
					
					jsonObject.accumulate("saleQty2", Math.round(saleQty2.doubleValue()));
					jsonObject.accumulate("TargetQty2", Math.round(TargetQty2.doubleValue()));
					if(Math.round(TargetQty2.doubleValue())==0){
						jsonObject.accumulate("ach2", 100+"%");
					}else{
						jsonObject.accumulate("ach2", Math.round(saleQty2.doubleValue()/TargetQty2.doubleValue()*100)+"%");
					}
					
					
					jsonObject.accumulate("saleQty3", Math.round(saleQty3.doubleValue()));
					jsonObject.accumulate("TargetQty3", Math.round(TargetQty3.doubleValue()));

					if(Math.round(TargetQty3.doubleValue())==0){
						jsonObject.accumulate("ach3", 100+"%");
					}else{
						jsonObject.accumulate("ach3", Math.round(saleQty3.doubleValue()/TargetQty3.doubleValue()*100)+"%");
					}
			
					
					array.add(jsonObject);
					obj=hqDatas.get(i);
					country =obj.get("country").toString();
					saleQty=BigDecimal.ZERO;
					saleQty1=BigDecimal.ZERO;
					saleQty2=BigDecimal.ZERO;
					saleQty3=BigDecimal.ZERO;
					
					TargetQty=BigDecimal.ZERO;
					TargetQty1=BigDecimal.ZERO;
					TargetQty2=BigDecimal.ZERO;
					TargetQty3=BigDecimal.ZERO;
					
					i--;

				
			}
		}

		JSONObject object=new JSONObject();
		DateUtil.OrderName(array, "center");
		object.accumulate("data",  array);
		return object;
	}


	@Override
	public JSONObject selectHQChainDataByYear(Map<String, Object> whereMap) {
List<HashMap<String, Object>> hqDatas = soTableDao.selectCountryDataByHQ(whereMap);


		
	
		
		JSONArray array=new JSONArray();
		String country="";
		if(hqDatas.size()>=1){
			country=hqDatas.get(0).get("country").toString();
		}
		BigDecimal saleQtyYear=BigDecimal.ZERO;
		BigDecimal saleQtyH1=BigDecimal.ZERO;
		BigDecimal saleQtyH2=BigDecimal.ZERO;
		
		
		
		
		for (int i = 0; i < hqDatas.size(); i++) {
			HashMap<String, Object> obj=hqDatas.get(i);
			if(WebPageUtil.isStringNullAvaliable(country) && country.equals(obj.get("country").toString())){
				
				int month =Integer.parseInt(obj.get("datadate").toString().split("-")[1]);
				
				BigDecimal bdQty=new BigDecimal(obj.get("saleQty").toString());
				saleQtyYear=saleQtyYear.add(bdQty);
				if(month<=6){
					saleQtyH1=saleQtyH1.add(bdQty);
				}else{
					saleQtyH2=saleQtyH2.add(bdQty);
				}
				
				if(hqDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQtyYear", Math.round(saleQtyYear.doubleValue()));
				

					jsonObject.accumulate("saleQtyH1", Math.round(saleQtyH1.doubleValue()));
					jsonObject.accumulate("saleQtyH2", Math.round(saleQtyH2.doubleValue()));
					
					jsonObject.accumulate("gro1","");

					if( Math.round(saleQtyH1.doubleValue())==0){
						jsonObject.accumulate("gro2", "100%");

					}else if( Math.round(saleQtyH2.doubleValue())==0){
						jsonObject.accumulate("gro2", "-100%");
					}else{
						double gro=(saleQtyH2.doubleValue()-saleQtyH1.doubleValue())/saleQtyH1.doubleValue();
						jsonObject.accumulate("gro2", Math.round(gro*100)+"%");
						
					}
					
					
					array.add(jsonObject);
				}
				
			}else{
					obj=hqDatas.get(i-1);
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQtyYear", Math.round(saleQtyYear.doubleValue()));
					
					jsonObject.accumulate("saleQtyH1", Math.round(saleQtyH1.doubleValue()));
				
					jsonObject.accumulate("saleQtyH2", Math.round(saleQtyH2.doubleValue()));
					
					
					jsonObject.accumulate("gro1","");

					if( Math.round(saleQtyH1.doubleValue())==0){
						jsonObject.accumulate("gro2", "100%");

					}else if( Math.round(saleQtyH2.doubleValue())==0){
						jsonObject.accumulate("gro2", "-100%");
					}else{
						double gro=(saleQtyH2.doubleValue()-saleQtyH1.doubleValue())/saleQtyH1.doubleValue();
						jsonObject.accumulate("gro2", Math.round(gro*100)+"%");
						
					}
					
				
					array.add(jsonObject);
					obj=hqDatas.get(i);
					country =obj.get("country").toString();
					saleQtyYear=BigDecimal.ZERO;
					saleQtyH1=BigDecimal.ZERO;
					saleQtyH2=BigDecimal.ZERO;
					
					
					i--;

				
			}
		}

		JSONObject object=new JSONObject();
		DateUtil.OrderName(array, "center");
		object.accumulate("data",  array);
		return object;
	}


	@Override
	public JSONObject selectHQChainDataByHalf(Map<String, Object> whereMap) {
		String [] begin=whereMap.get("beginDate").toString().split("-");
		String hlaf="";
		if(Integer.parseInt(begin[1])==1){
			hlaf="H1";
		}else{
			whereMap.put("beginDate", "2017-04-01");
			hlaf="H2";
		}
		
		List<HashMap<String, Object>> hqDatas = soTableDao.selectCountryDataByHQ(whereMap);

		
	
	
		JSONArray array=new JSONArray();
		String country="";
		if(hqDatas.size()>=1){
			country=hqDatas.get(0).get("country").toString();
		}
		BigDecimal saleQtyYear=BigDecimal.ZERO;
		BigDecimal saleQtyH1=BigDecimal.ZERO;
		BigDecimal saleQtyH2=BigDecimal.ZERO;
		
		
		BigDecimal saleQtylast=BigDecimal.ZERO;
		
		for (int i = 0; i < hqDatas.size(); i++) {
			HashMap<String, Object> obj=hqDatas.get(i);
			if(WebPageUtil.isStringNullAvaliable(country) && country.equals(obj.get("country").toString())){
				
				int month =Integer.parseInt(obj.get("datadate").toString().split("-")[1]);
				
				BigDecimal bdQty=new BigDecimal(obj.get("saleQty").toString());
				saleQtyYear=saleQtyYear.add(bdQty);
				if(hlaf.equals("H1")){
					if(month<=3){
						saleQtyH1=saleQtyH1.add(bdQty);
					}else{
						saleQtyH2=saleQtyH2.add(bdQty);
					}
				}else{
					if(month<=6){
						saleQtylast=saleQtylast.add(bdQty);
					}else if(month>6 &&month<=9){
						saleQtyH1=saleQtyH1.add(bdQty);
					}else{
						saleQtyH2=saleQtyH2.add(bdQty);
					}
				}
				
				
				if(hqDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQtyYear", Math.round(saleQtyYear.doubleValue()));
					
					jsonObject.accumulate("saleQtyH1", Math.round(saleQtyH1.doubleValue()));
					jsonObject.accumulate("saleQtyH2", Math.round(saleQtyH2.doubleValue()));
					
					jsonObject.accumulate("saleQtylast", Math.round(saleQtylast.doubleValue()));
					
					double gro=(saleQtyH2.doubleValue()-saleQtyH1.doubleValue())/saleQtyH1.doubleValue();

					if(hlaf.equals("H1")){
						jsonObject.accumulate("gro1","");
						
					}else{
						double gro2=(saleQtyH1.doubleValue()-saleQtylast.doubleValue())/saleQtylast.doubleValue();
						if(Math.round(saleQtyH1.doubleValue())==0){
							jsonObject.accumulate("gro1","-100%");
						}else if(Math.round(saleQtylast.doubleValue())==0){
							jsonObject.accumulate("gro1","100%");
						}else{
							jsonObject.accumulate("gro1",Math.round(gro2*100)+"%");
						}
						
						
					}
					
			
					if(Math.round(saleQtyH1.doubleValue())==0){
						jsonObject.accumulate("gro2","100%");
					}else if(Math.round(saleQtyH2.doubleValue())==0){
						jsonObject.accumulate("gro2","-100%");
					}else{
						jsonObject.accumulate("gro2",Math.round(gro*100)+"%");
					}
					
					array.add(jsonObject);
				}
				
			}else{
					obj=hqDatas.get(i-1);
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQtyYear", Math.round(saleQtyYear.doubleValue()));
					jsonObject.accumulate("saleQtyH1", Math.round(saleQtyH1.doubleValue()));
					jsonObject.accumulate("saleQtyH2", Math.round(saleQtyH2.doubleValue()));
					jsonObject.accumulate("saleQtylast", Math.round(saleQtylast.doubleValue()));

					double gro=(saleQtyH2.doubleValue()-saleQtyH1.doubleValue())/saleQtyH1.doubleValue();

					if(hlaf.equals("H1")){
						jsonObject.accumulate("gro1","");
						
					}else{
						double gro2=(saleQtyH1.doubleValue()-saleQtylast.doubleValue())/saleQtylast.doubleValue();
						if(Math.round(saleQtyH1.doubleValue())==0){
							jsonObject.accumulate("gro1","-100%");
						}else if(Math.round(saleQtylast.doubleValue())==0){
							jsonObject.accumulate("gro1","100%");
						}else{
							jsonObject.accumulate("gro1",Math.round(gro2*100)+"%");
						}
						
						
					}
					
			
					if(Math.round(saleQtyH1.doubleValue())==0){
						jsonObject.accumulate("gro2","100%");
					}else if(Math.round(saleQtyH2.doubleValue())==0){
						jsonObject.accumulate("gro2","-100%");
					}else{
						jsonObject.accumulate("gro2",Math.round(gro*100)+"%");
					}
					
					array.add(jsonObject);
					obj=hqDatas.get(i);
					country =obj.get("country").toString();
					saleQtyYear=BigDecimal.ZERO;
					saleQtyH1=BigDecimal.ZERO;
					saleQtyH2=BigDecimal.ZERO;
					saleQtylast=BigDecimal.ZERO;
					
					i--;

				
			}
		}

		JSONObject object=new JSONObject();
		DateUtil.OrderName(array, "center");
		object.accumulate("data",  array);
		return object;
	}


	@Override
	public JSONObject selectHQChainDataByQuarter(Map<String, Object> whereMap) {

		String [] beg=whereMap.get("beginDate").toString().split("-");
		
		String [] end=whereMap.get("endDate").toString().split("-");


		if(Integer.parseInt(beg[1].toString())==4){
			whereMap.put("beginDate", "2017-03-01");
		}else if(Integer.parseInt(beg[1].toString())==7){
			whereMap.put("beginDate", "2017-06-01");
		}else if(Integer.parseInt(beg[1].toString())==10){
			whereMap.put("beginDate", "2017-09-01");
		}
		List<HashMap<String, Object>> hqDatas = soTableDao.selectCountryDataByHQ(whereMap);

	
		JSONArray array=new JSONArray();
		
		BigDecimal saleQty=BigDecimal.ZERO;
		BigDecimal saleQty1=BigDecimal.ZERO;
		BigDecimal saleQty2=BigDecimal.ZERO;
		BigDecimal saleQty3=BigDecimal.ZERO;
		
		BigDecimal saleQtylast=BigDecimal.ZERO;
		
		String country="";
		if(hqDatas.size()>=1){
			country=hqDatas.get(0).get("country").toString();
		}
	
		
		
		for (int i = 0; i < hqDatas.size(); i++) {
			HashMap<String, Object> obj=hqDatas.get(i);
			if(WebPageUtil.isStringNullAvaliable(country) && country.equals(obj.get("country").toString())){
				
				int month =Integer.parseInt(obj.get("datadate").toString().split("-")[1]);
				
				BigDecimal bdQty=new BigDecimal(obj.get("saleQty").toString());
				saleQty=saleQty.add(bdQty);
				
					
				if(Integer.parseInt(end[1].toString())==3){
					if(month==1){
							saleQty1=saleQty1.add(bdQty);
	
					}else if(month==2){
						saleQty2=saleQty2.add(bdQty);
					}else if(month==3){
						saleQty3=saleQty3.add(bdQty);
	
					}
						
				}else if(Integer.parseInt(end[1].toString())==6){
					if(month==4){
						saleQty1=saleQty1.add(bdQty);
					}else if(month==5){
						saleQty2=saleQty2.add(bdQty);
					}else if(month==6){
						saleQty3=saleQty3.add(bdQty);
	
					}else if(month==3){
						saleQtylast=saleQtylast.add(bdQty);
	
					}
					
				}else if(Integer.parseInt(end[1].toString())==9){
					
					if(month==7){
						saleQty1=saleQty1.add(bdQty);
					}else if(month==8){
						saleQty2=saleQty2.add(bdQty);
					}else if(month==9){
						saleQty3=saleQty3.add(bdQty);
	
					}else if(month==6){
						saleQtylast=saleQtylast.add(bdQty);
	
					}
					
					
				}else if(Integer.parseInt(end[1].toString())==12){

					if(month==10){
						saleQty1=saleQty1.add(bdQty);
					}else if(month==11){
						saleQty2=saleQty2.add(bdQty);
					}else if(month==12){
						saleQty3=saleQty3.add(bdQty);
	
					}else if(month==9){
						saleQtylast=saleQtylast.add(bdQty);
	
					}
				}
				
				
				if(hqDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQty", Math.round(saleQty.doubleValue()));
					
					jsonObject.accumulate("saleQty1", Math.round(saleQty1.doubleValue()));
					jsonObject.accumulate("saleQty2", Math.round(saleQty2.doubleValue()));
					jsonObject.accumulate("saleQty3", Math.round(saleQty3.doubleValue()));
					
					jsonObject.accumulate("saleQtylast", Math.round(saleQtylast.doubleValue()));
					
					double gro2=(saleQty2.doubleValue()-saleQty1.doubleValue())/saleQty1.doubleValue();
					double gro3=(saleQty3.doubleValue()-saleQty2.doubleValue())/saleQty2.doubleValue();

					
					if(Integer.parseInt(end[1].toString())==3){
						jsonObject.accumulate("gro1", "");
						
					}else{
						double gro1=(saleQty1.doubleValue()-saleQtylast.doubleValue())/saleQtylast.doubleValue();
						
						if( Math.round(saleQty1.doubleValue())==0){
							jsonObject.accumulate("gro1", "-100%");
						}else if( Math.round(saleQtylast.doubleValue())==0){
							jsonObject.accumulate("gro1", "100%");
						}else{
							jsonObject.accumulate("gro1", Math.round(gro1*100)+"%");

						}
						
					}
					

					
					if( Math.round(saleQty2.doubleValue())==0){
						jsonObject.accumulate("gro2", "-100%");
					}else if( Math.round(saleQty1.doubleValue())==0){
						jsonObject.accumulate("gro1", "100%");
					}else{
						jsonObject.accumulate("gro2", Math.round(gro2*100)+"%");

					}
					
					if( Math.round(saleQty2.doubleValue())==0){
						jsonObject.accumulate("gro3", "100%");
					}else if( Math.round(saleQty3.doubleValue())==0){
						jsonObject.accumulate("gro3", "-100%");
					}else{
						jsonObject.accumulate("gro3", Math.round(gro3*100)+"%");

					}
					
					
					array.add(jsonObject);
				}
				
			}else{
					obj=hqDatas.get(i-1);
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQty", Math.round(saleQty.doubleValue()));
					
					jsonObject.accumulate("saleQty1", Math.round(saleQty1.doubleValue()));
					jsonObject.accumulate("saleQty2", Math.round(saleQty2.doubleValue()));
					jsonObject.accumulate("saleQty3", Math.round(saleQty3.doubleValue()));
					jsonObject.accumulate("saleQtylast", Math.round(saleQtylast.doubleValue()));

					double gro2=(saleQty2.doubleValue()-saleQty1.doubleValue())/saleQty1.doubleValue();
					double gro3=(saleQty3.doubleValue()-saleQty2.doubleValue())/saleQty2.doubleValue();

					
					
					if(Integer.parseInt(end[1].toString())==3){
						jsonObject.accumulate("gro1", "");
						
					}else{
						double gro1=(saleQty1.doubleValue()-saleQtylast.doubleValue())/saleQtylast.doubleValue();
						
						if( Math.round(saleQty1.doubleValue())==0){
							jsonObject.accumulate("gro1", "-100%");
						}else if( Math.round(saleQtylast.doubleValue())==0){
							jsonObject.accumulate("gro1", "100%");
						}else{
							jsonObject.accumulate("gro1", Math.round(gro1*100)+"%");

						}
						
					}
					

					
					if( Math.round(saleQty2.doubleValue())==0){
						jsonObject.accumulate("gro2", "-100%");
					}else if( Math.round(saleQty1.doubleValue())==0){
						jsonObject.accumulate("gro1", "100%");
					}else{
						jsonObject.accumulate("gro2", Math.round(gro2*100)+"%");

					}
					
					if( Math.round(saleQty2.doubleValue())==0){
						jsonObject.accumulate("gro3", "100%");
					}else if( Math.round(saleQty3.doubleValue())==0){
						jsonObject.accumulate("gro3", "-100%");
					}else{
						jsonObject.accumulate("gro3", Math.round(gro3*100)+"%");

					}
					
					
					
					
					array.add(jsonObject);
					obj=hqDatas.get(i);
					country =obj.get("country").toString();
					saleQty=BigDecimal.ZERO;
					saleQty1=BigDecimal.ZERO;
					saleQty2=BigDecimal.ZERO;
					saleQty3=BigDecimal.ZERO;
					saleQtylast=BigDecimal.ZERO;
					
					i--;

				
			}
		}

		JSONObject object=new JSONObject();
		DateUtil.OrderName(array, "center");
		object.accumulate("data",  array);
		return object;
	}

	
	
}


