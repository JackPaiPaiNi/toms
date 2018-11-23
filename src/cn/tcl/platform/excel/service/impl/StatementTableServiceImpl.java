package cn.tcl.platform.excel.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.excel.actions.DateUtil;
import cn.tcl.platform.excel.dao.IStatementTableDao;
import cn.tcl.platform.excel.service.IStatementTableService;

@Service("statementTableService")
public class StatementTableServiceImpl implements IStatementTableService{
	@Autowired
	private IStatementTableDao dao;

	@Override
	public JSONObject selectDealerSellout(Map<String, Object> whereMap) {
		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
			
			

			List<HashMap<String, Object>> DealerSelloutInfo = dao
					.selectSelloutByDealerInfo(whereMap);


			List<HashMap<String, Object>> DealerSelloutTo = dao
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
		
			String [] daysBegin=whereMap.get("beginDate").toString().split("-");
			String [] daysEnd=whereMap.get("endDate").toString().split("-");
			int yearTo=Integer.parseInt(daysBegin[0]);
			int yearLast=Integer.parseInt(daysBegin[0])-1;
			whereMap.put("beginDate",yearLast+"-"+daysBegin[1]+"-"+daysBegin[2]);
			whereMap.put("endDate",yearLast+"-"+daysEnd[1]+"-"+daysEnd[2]);
			
			List<HashMap<String, Object>> DealerSelloutLast = dao
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

						dataMap.put("toYear_NO_OF_SHOP",
								list.get(i).get("noOfShops"));
						dataMap.put("toYear_NO_OF_FPS", list.get(i).get("tvFps"));

						BigDecimal bq = new BigDecimal(list.get(i)
								.get("saleQty").toString());
						aq = bq.toPlainString();
						tq =  bq.intValue();
						dataMap.put("toYear_TTL_TV_SO_QTY", bq.longValue());

						bd = new BigDecimal(list.get(i).get("saleSum")
								.toString());
						am = bd.toPlainString();

						dataMap.put("toYear_TTL_TV_SO_AMT", am);

						BigDecimal td = new BigDecimal(list.get(i)
								.get("targetSum").toString());
						String tm = td.toPlainString();
						dataMap.put("BASIC_TARGET", tm);

						

						tshop = Integer.parseInt(list.get(i).get("noOfShops")
								.toString());
						tfps = Integer.parseInt(list.get(i).get("tvFps")
								.toString());
						

						ts = Double.parseDouble(am);
						tt = Double.parseDouble(tm);
						if (tt != 0.0) {
							ach = ts / tt * 100;
							long lnum = Math.round(ach);
							dataMap.put("ACH_", lnum + "");
						}

						dataMap.put("toYear_TOTAL_QTY_", tq);

						bd = new BigDecimal(ts);
						am = bd.toPlainString();

						dataMap.put("toYear_TOTAL_AMOUNT", am);

						if (tfps != 0) {
							avg = tq / tfps;
							dataMap.put("toYear_AVE_SO_FPS_QTY",Math.round( avg));
							tqavg = Math.round( avg);

							avg =  (ts / tfps);
							dataMap.put("toYear_AVE_SO_FPS_AMT", Math.round( avg));
							tsavg = Math.round( avg);
						}

					}

				}
				if (selloutLastMap.get(DealerSelloutInfo.get(w).get("DEALER")) != null) {

					ArrayList<HashMap<String, Object>> list = selloutLastMap
							.get(DealerSelloutInfo.get(w).get("DEALER"));
					for (int j = 0; j < list.size(); j++) {
						dataMap.put("lastYear_NO_OF_SHOP", list.get(j).get("noOfShops"));
						dataMap.put("lastYear_NO_OF_FPS", list.get(j).get("tvFps"));
						dataMap.put("lastYear_TOTAL_QTY_", list.get(j).get("saleQty"));
						BigDecimal lbd = new BigDecimal(list.get(j)
								.get("saleSum").toString());
						String lam = lbd.toPlainString();
						dataMap.put("lastYear_TOTAL_AMOUNT", lam);
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
							dataMap.put("lastYear_AVE_SO_FPS_QTY", Math.round( avg));
							lqavg = Math.round( avg);

							avg =  (ls / lfps);
							dataMap.put("lastYear_AVE_SO_FPS_AMT", Math.round( avg));
							lsavg = Math.round( avg);
						}

					}
				}

				if (tq != 0) {
					ltq = tq - lq;
					ach = ltq / tq * 100;
					long lnum = Math.round(ach);
					dataMap.put("GROWTH_QTY", lnum + "");

				} else if (tq == 0 && lq == 0) {
					dataMap.put("GROWTH_QTY", "100");
				} else if (tq == 0 && lq != 0) {
					dataMap.put("GROWTH_QTY", "-100");
				}
				if (ts != 0.0) {
					ltq = ts - ls;
					ach = ltq / ts * 100;
					long lnum = Math.round(ach);
					dataMap.put("GROWTH_AMOUNT", lnum + "");
				} else if (ts == 0.0 && ls == 0.0) {
					dataMap.put("GROWTH_AMOUNT", "100");
				} else if (ts == 0.0 && ls != 0.0) {
					dataMap.put("GROWTH_AMOUNT", "-100");
				}

				ach = (tqavg - lqavg);
				bd = new BigDecimal(ach);
				am = bd.toPlainString();
				dataMap.put("SELL_OUT_EFFICIENCY_QTY", am);

				ach = (tsavg - lsavg);
				bd = new BigDecimal(ach);
				am = bd.toPlainString();
				dataMap.put("SELL_OUT_EFFICIENCY_AMT", am);

				dataList.add(dataMap);

			DateUtil.Order(dataList, "GROWTH_AMOUNT");
			}
			JSONObject jsonObject=new JSONObject();
			jsonObject.accumulate("data",dataList);
		return jsonObject;
	}
	
}
