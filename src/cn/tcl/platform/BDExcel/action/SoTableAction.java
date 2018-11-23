package cn.tcl.platform.BDExcel.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.tcl.common.BaseAction;
import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.BDExcel.service.ISoTableService;

/**
 * @author Fay 2017年11月7日10:14:20
 */
public class SoTableAction extends BaseAction {

	@Autowired(required = false)
	@Qualifier("BDSCsoTableService")
	private ISoTableService soTableService;

	public String loadGrandTTLPage() {
		try {
			if (WebPageUtil.isHQRole()) {
				return "successhq";
			} else {
				return SUCCESS;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String loadSoGrowthPage() {
		try {
			if (WebPageUtil.isHQRole()) {
				return "successhq";
			} else {
				return SUCCESS;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String loadSoHuanBiPage() {
		try {
			if (WebPageUtil.isHQRole()) {
				return "successhq";
			} else {
				return SUCCESS;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public void selectGrandTTL() {
		response.setHeader("Content-Type", "application/json");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");

		String what = request.getParameter("what");
		String line = request.getParameter("line");
		String model = request.getParameter("model");

		String check = request.getParameter("check");

		String countryUDModelByYear = request.getParameter("countryUDModelByYear");
		String countryCurvedModelByYear = request.getParameter("countryCurvedModelByYear");
		String RegUDModelByYear = request.getParameter("RegUDModelByYear");
		String CurvedModelRegYear = request.getParameter("CurvedModelRegYear");

		String SaleUDModelByYear = request.getParameter("SaleUDModelByYear");
		String CurvedModelSaleYear = request.getParameter("CurvedModelSaleYear");

		String AcfoUDModelByYear = request.getParameter("AcfoUDModelByYear");
		String CurvedModelAcfoYear = request.getParameter("CurvedModelAcfoYear");

		String countryUDModelByMonth = request.getParameter("countryUDModelByMonth");
		String countryCurvedModelByMonth = request.getParameter("countryCurvedModelByMonth");
		String RegUDModelByMonth = request.getParameter("RegUDModelByMonth");
		String CurvedModelRegMonth = request.getParameter("CurvedModelRegMonth");

		String SaleUDModelByMonth = request.getParameter("SaleUDModelByMonth");
		String CurvedModelSaleMonth = request.getParameter("CurvedModelSaleMonth");

		String AcfoUDModelByMonth = request.getParameter("AcfoUDModelByMonth");
		String CurvedModelAcfoMonth = request.getParameter("CurvedModelAcfoMonth");

		String SaleUDModelByGroMonth = request.getParameter("SaleUDModelByGroMonth");
		String CurvedModelSaleGroMonth = request.getParameter("CurvedModelSaleGroMonth");

		String CountryUDModelByGroMonth = request.getParameter("CountryUDModelByGroMonth");
		String CurvedModelCountryGroMonth = request.getParameter("CurvedModelCountryGroMonth");

		String conditions = "";
		String conditionsTarget = "";
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		Map<String, Object> whereMap = new HashMap<String, Object>();

		if (!WebPageUtil.isHAdmin()) {
			if (null != userPartyIds && !"".equals(userPartyIds)) {
				whereMap.put("country", WebPageUtil.getLoginedUser().getPartyId());
				conditions = "  pa.party_id in (" + userPartyIds + ")  ";
			} else {
				conditions = "  1=2  ";
			}
		} else {
			conditions = " 1=1 ";

		}
		System.out.println("===========userPartyIds===========" + conditions);
		whereMap.put("beginDate", beginDate);
		whereMap.put("endDate", endDate);
		whereMap.put("conditions", conditions);
		whereMap.put("isHq", WebPageUtil.isHQRole());
		if (check != null && !check.equals("")) {
			whereMap.put("check", check.toString());

		} else {
			whereMap.put("check", "true");

		}
		whereMap.put("year", beginDate.split("-")[0]);

		if (what.contains("HQ")) {

			if (null != userPartyIds && !"".equals(userPartyIds)) {
				conditions = "  pa.party_id in (" + userPartyIds + ")  ";
				conditionsTarget = " WHERE pa.`PARTY_ID` in (" + userPartyIds + ")  ";
			} else {
				conditions = "  1=2  ";
				conditionsTarget = " WHERE 1=2  ";
			}

			whereMap.put("conditions", conditions);

			whereMap.put("conditionsTarget", conditionsTarget);
		}

		whereMap.put("what", what);
		JSONObject object = null;
		try {
			if (what.equals("yearXCP")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchStr", "AND pt.product_line like'%" + line + "%'");
				} else {
					whereMap.put("searchStr",
							"AND (pt.product_line  LIKE 'X%' OR pt.product_line  LIKE 'C%' OR pt.product_line  LIKE 'P%')");
				}
				object = soTableService.selectModelByMonth(whereMap);
			} else if (what.equals("countryGrowth")) {
				object = soTableService.selectGrowthByCountry(whereMap);
			} else if (what.equals("yearRegTotal")) {
				object = soTableService.selectDataByAreaChain(whereMap);
			} else if (what.equals("yearSaleTotal")) {
				object = soTableService.selectSalemanDataByChain(whereMap);
			} else if (what.equals("yearAcfoTotal")) {
				object = soTableService.selectAcfoDataByChain(whereMap);
			} else if (what.equals("yearCountryTotal")) {
				object = soTableService.selectCountryDataByChain(whereMap);
			} else if (what.equals("yearCountryUD")) {
				if (countryUDModelByYear != null && !countryUDModelByYear.equals("")) {
					whereMap.put("searchModel", countryUDModelByYear);
				} else {
					whereMap.put("searchLine",
							"AND (pt.product_line LIKE 'X%' OR pt.product_line LIKE 'C%' OR pt.product_line LIKE 'P%')");

				}
				object = soTableService.selectCountryDataByChain(whereMap);
			} else if (what.equals("yearCountrySmart")) {
				whereMap.put("searchLine", "AND pt.`PRODUCT_SPEC_ID` LIKE '%Smart%'");
				object = soTableService.selectCountryDataByChain(whereMap);
			} else if (what.equals("yearCountryBig")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchStr", line);
				}
				object = soTableService.selectCountryBigByYear(whereMap);
			} else if (what.equals("yearCountryCurved")) {
				if (countryCurvedModelByYear != null && !countryCurvedModelByYear.equals("")) {
					whereMap.put("searchModel", countryCurvedModelByYear);
				} else {
					whereMap.put("searchLine", "AND pt.`PRODUCT_SPEC_ID` LIKE 'Curved%'");
				}
				object = soTableService.selectCountryDataByChain(whereMap);
			} else if (what.equals("yearRegUD")) {
				if (RegUDModelByYear != null && !RegUDModelByYear.equals("")) {
					whereMap.put("searchModel", RegUDModelByYear);
				} else {
					whereMap.put("searchLine",
							"AND (pr.product_line LIKE 'X%' OR pr.product_line LIKE 'C%' OR pr.product_line LIKE 'P%')");
				}
				object = soTableService.selectDataByAreaChain(whereMap);
			} else if (what.equals("yearRegXCP")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchLine", "AND pr.product_line like'%" + line + "%'");
				} else {
					whereMap.put("searchLine",
							"AND (pr.product_line  LIKE 'X%' OR pr.product_line  LIKE 'C%' OR pr.product_line  LIKE 'P%')");
				}
				object = soTableService.selectDataByAreaChain(whereMap);
			} else if (what.equals("yearRegSmart")) {
				whereMap.put("searchLine", "AND pr.`PRODUCT_SPEC_ID` LIKE '%Smart%'");
				object = soTableService.selectDataByAreaChain(whereMap);
			} else if (what.equals("yearRegBig")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchLine", line);
				}
				object = soTableService.selectDataByAreaChain(whereMap);
			} else if (what.equals("yearRegCurved")) {
				if (CurvedModelRegYear != null && !CurvedModelRegYear.equals("")) {
					whereMap.put("searchModel", CurvedModelRegYear);
				} else {
					whereMap.put("searchLine", "AND pr.`PRODUCT_SPEC_ID` LIKE 'Curved%'");
				}
				object = soTableService.selectDataByAreaChain(whereMap);
			} else if (what.equals("yearSaleUD")) {
				if (SaleUDModelByYear != null && !SaleUDModelByYear.equals("")) {
					whereMap.put("searchModel", SaleUDModelByYear);
				} else {
					whereMap.put("searchLine",
							"AND (pr.product_line LIKE 'X%' OR pr.product_line LIKE 'C%' OR pr.product_line LIKE 'P%')");
				}
				object = soTableService.selectSalemanDataByChain(whereMap);
			} else if (what.equals("yearSaleXCP")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchLine", "AND pr.product_line like'%" + line + "%'");
				} else {
					whereMap.put("searchLine",
							"AND (pr.product_line  LIKE 'X%' OR pr.product_line  LIKE 'C%' OR pr.product_line  LIKE 'P%')");
				}
				object = soTableService.selectSalemanDataByChain(whereMap);
			} else if (what.equals("yearSaleSmart")) {
				whereMap.put("searchLine", "AND pr.`PRODUCT_SPEC_ID` LIKE '%Smart%'");
				object = soTableService.selectSalemanDataByChain(whereMap);
			} else if (what.equals("yearSaleBig")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchLine", line);
				}
				object = soTableService.selectSalemanDataByChain(whereMap);
			} else if (what.equals("yearSaleCurved")) {
				if (CurvedModelSaleYear != null && !CurvedModelSaleYear.equals("")) {
					whereMap.put("searchModel", CurvedModelSaleYear);
				} else {
					whereMap.put("searchLine", "AND pr.`PRODUCT_SPEC_ID` LIKE 'Curved%'");
				}
				object = soTableService.selectSalemanDataByChain(whereMap);
			} else if (what.equals("yearAcfoUD")) {
				if (AcfoUDModelByYear != null && !AcfoUDModelByYear.equals("")) {
					whereMap.put("searchModel", AcfoUDModelByYear);
				} else {
					whereMap.put("searchLine",
							"AND (pr.product_line LIKE 'X%' OR pr.product_line LIKE 'C%' OR pr.product_line LIKE 'P%')");
				}
				object = soTableService.selectAcfoDataByChain(whereMap);
			} else if (what.equals("yearAcfoXCP")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchLine", "AND pr.product_line like'%" + line + "%'");
				} else {
					whereMap.put("searchLine",
							"AND (pr.product_line  LIKE 'X%' OR pr.product_line  LIKE 'C%' OR pr.product_line  LIKE 'P%')");
				}
				object = soTableService.selectAcfoDataByChain(whereMap);
			} else if (what.equals("yearAcfoSmart")) {
				whereMap.put("searchLine", "AND pr.`PRODUCT_SPEC_ID` LIKE '%Smart%'");
				object = soTableService.selectAcfoDataByChain(whereMap);
			} else if (what.equals("yearAcfoBig")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchLine", line);
				}
				object = soTableService.selectAcfoDataByChain(whereMap);
			} else if (what.equals("yearAcfoCurved")) {
				if (CurvedModelAcfoYear != null && !CurvedModelAcfoYear.equals("")) {
					whereMap.put("searchModel", CurvedModelAcfoYear);
				} else {
					whereMap.put("searchLine", "AND pr.`PRODUCT_SPEC_ID` LIKE 'Curved%'");
				}
				object = soTableService.selectAcfoDataByChain(whereMap);
			} else if (what.equals("monthCountryTotal")) {
				object = soTableService.selectMonthCountryTotal(whereMap);
			} else if (what.equals("monthCountryUD")) {
				if (countryUDModelByMonth != null && !countryUDModelByMonth.equals("")) {
					whereMap.put("searchModel", countryUDModelByMonth);
				} else {
					whereMap.put("searchLine",
							"AND (pr.product_line LIKE 'X%' OR pr.product_line LIKE 'C%' OR pr.product_line LIKE 'P%')");
				}
				object = soTableService.selectMonthCountryTotal(whereMap);
			} else if (what.equals("monthCountryXCP")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchStr", "AND pr.product_line like'%" + line + "%'");
				} else {
					whereMap.put("searchStr",
							"AND (pr.product_line  LIKE 'X%' OR pr.product_line  LIKE 'C%' OR pr.product_line  LIKE 'P%')");
				}
				object = soTableService.selectMonthCountryXCP(whereMap);
			} else if (what.equals("monthCountrySmart")) {
				whereMap.put("searchLine", "AND pr.`PRODUCT_SPEC_ID` LIKE '%Smart%'");
				object = soTableService.selectMonthCountryTotal(whereMap);
			} else if (what.equals("monthCountryBig")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchStr", line);
				}
				object = soTableService.selectMonthCountryXCP(whereMap);
			} else if (what.equals("monthCountryCurved")) {
				if (countryCurvedModelByMonth != null && !countryCurvedModelByMonth.equals("")) {
					whereMap.put("searchModel", countryCurvedModelByMonth);
				} else {
					whereMap.put("searchLine", "AND pr.`PRODUCT_SPEC_ID` LIKE 'Curved%'");
				}
				object = soTableService.selectMonthCountryTotal(whereMap);
			} else if (what.equals("monthRegTotal")) {
				object = soTableService.selectAreaDataByMonth(whereMap);
			} else if (what.equals("monthRegUD")) {
				if (RegUDModelByMonth != null && !RegUDModelByMonth.equals("")) {
					whereMap.put("searchModel", RegUDModelByMonth);
				} else {
					whereMap.put("searchLine",
							"AND (pr.product_line LIKE 'X%' OR pr.product_line LIKE 'C%' OR pr.product_line LIKE 'P%')");
				}
				object = soTableService.selectAreaDataByMonth(whereMap);
			} else if (what.equals("monthRegXCP")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchLine", "AND pr.product_line like'%" + line + "%'");
				} else {
					whereMap.put("searchLine",
							"AND (pr.product_line  LIKE 'X%' OR pr.product_line  LIKE 'C%' OR pr.product_line  LIKE 'P%')");
				}
				object = soTableService.selectAreaDataByMonth(whereMap);
			} else if (what.equals("monthRegSmart")) {
				whereMap.put("searchLine", "AND pr.`PRODUCT_SPEC_ID` LIKE '%Smart%'");
				object = soTableService.selectAreaDataByMonth(whereMap);
			} else if (what.equals("monthRegBig")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchLine", line);
				}
				object = soTableService.selectAreaDataByMonth(whereMap);
			} else if (what.equals("monthRegCurved")) {
				if (CurvedModelRegMonth != null && !CurvedModelRegMonth.equals("")) {
					whereMap.put("searchModel", CurvedModelRegMonth);
				} else {
					whereMap.put("searchLine", "AND pr.`PRODUCT_SPEC_ID` LIKE 'Curved%'");
				}
				object = soTableService.selectAreaDataByMonth(whereMap);
			} else if (what.equals("monthSaleTotal")) {
				object = soTableService.selectSalemanDataByMonth(whereMap);
			} else if (what.equals("monthSaleUD")) {
				if (SaleUDModelByMonth != null && !SaleUDModelByMonth.equals("")) {
					whereMap.put("searchModel", SaleUDModelByMonth);
				} else {
					whereMap.put("searchLine",
							"AND (pr.product_line LIKE 'X%' OR pr.product_line LIKE 'C%' OR pr.product_line LIKE 'P%')");
				}
				object = soTableService.selectSalemanDataByMonth(whereMap);
			} else if (what.equals("monthSaleXCP")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchLine", "AND pr.product_line like'%" + line + "%'");
				} else {
					whereMap.put("searchLine",
							"AND (pr.product_line  LIKE 'X%' OR pr.product_line  LIKE 'C%' OR pr.product_line  LIKE 'P%')");
				}
				object = soTableService.selectSalemanDataByMonth(whereMap);
			} else if (what.equals("monthSaleSmart")) {
				whereMap.put("searchLine", "AND pr.`PRODUCT_SPEC_ID` LIKE '%Smart%'");
				object = soTableService.selectSalemanDataByMonth(whereMap);
			} else if (what.equals("monthSaleBig")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchLine", line);
				}
				object = soTableService.selectSalemanDataByMonth(whereMap);
			} else if (what.equals("monthSaleCurved")) {
				if (CurvedModelSaleMonth != null && !CurvedModelSaleMonth.equals("")) {
					whereMap.put("searchModel", CurvedModelSaleMonth);
				} else {
					whereMap.put("searchLine", "AND pr.`PRODUCT_SPEC_ID` LIKE 'Curved%'");
				}
				object = soTableService.selectSalemanDataByMonth(whereMap);
			} else if (what.equals("monthAcfoTotal")) {
				object = soTableService.selectAcfoDataByMonth(whereMap);
			} else if (what.equals("monthAcfoUD")) {
				if (AcfoUDModelByMonth != null && !AcfoUDModelByMonth.equals("")) {
					whereMap.put("searchModel", AcfoUDModelByMonth);
				} else {
					whereMap.put("searchLine",
							"AND (pr.product_line LIKE 'X%' OR pr.product_line LIKE 'C%' OR pr.product_line LIKE 'P%')");
				}
				object = soTableService.selectAcfoDataByMonth(whereMap);
			} else if (what.equals("monthAcfoXCP")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchLine", "AND pr.product_line like'%" + line + "%'");
				} else {
					whereMap.put("searchLine",
							"AND (pr.product_line  LIKE 'X%' OR pr.product_line  LIKE 'C%' OR pr.product_line  LIKE 'P%')");
				}
				object = soTableService.selectAcfoDataByMonth(whereMap);
			} else if (what.equals("monthAcfoSmart")) {
				whereMap.put("searchLine", "AND pr.`PRODUCT_SPEC_ID` LIKE '%Smart%'");
				object = soTableService.selectAcfoDataByMonth(whereMap);
			} else if (what.equals("monthAcfoBig")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchLine", line);
				}
				object = soTableService.selectAcfoDataByMonth(whereMap);
			} else if (what.equals("monthAcfoCurved")) {
				if (CurvedModelAcfoMonth != null && !CurvedModelAcfoMonth.equals("")) {
					whereMap.put("searchModel", CurvedModelAcfoMonth);
				} else {
					whereMap.put("searchLine", "AND pr.`PRODUCT_SPEC_ID` LIKE 'Curved%'");
				}
				object = soTableService.selectAcfoDataByMonth(whereMap);
			} else if (what.equals("monthCountryGrowthUD")) {
				if (CountryUDModelByGroMonth != null && !CountryUDModelByGroMonth.equals("")) {
					whereMap.put("searchModel", CountryUDModelByGroMonth);
				} else {
					whereMap.put("searchLine",
							"AND (pt.product_line LIKE 'X%' OR pt.product_line LIKE 'C%' OR pt.product_line LIKE 'P%')");
				}
				object = soTableService.selectGrowthByCountry(whereMap);
			} else if (what.equals("monthCountryGrowthXCP")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchLine", "AND pt.product_line like'%" + line + "%'");
				} else {
					whereMap.put("searchLine",
							"AND (pt.product_line  LIKE 'X%' OR pt.product_line  LIKE 'C%' OR pt.product_line  LIKE 'P%')");
				}
				object = soTableService.selectGrowthByCountry(whereMap);
			} else if (what.equals("monthCountryGrowthSmart")) {
				whereMap.put("searchLine", "AND pt.`PRODUCT_SPEC_ID` LIKE '%Smart%'");
				object = soTableService.selectGrowthByCountry(whereMap);
			} else if (what.equals("monthCountryGrowthBig")) {
				// if(line!=null && !line.equals("")){
				whereMap.put("type", "big");
				// }
				object = soTableService.selectGrowthByCountry(whereMap);
			} else if (what.equals("monthCountryGrowthCurved")) {
				if (CurvedModelCountryGroMonth != null && !CurvedModelCountryGroMonth.equals("")) {
					whereMap.put("searchModel", CurvedModelCountryGroMonth);
				} else {
					whereMap.put("searchLine", "AND pt.`PRODUCT_SPEC_ID` LIKE 'Curved%'");
				}
				object = soTableService.selectGrowthByCountry(whereMap);
			} else if (what.equals("yearTotalByHQ")) {
				object = soTableService.selectHQDataByYear(whereMap);
			} else if (what.equals("yearUDByHQ")) {
				whereMap.put("searchLine",
						"AND (pt.product_line LIKE 'X%' OR pt.product_line LIKE 'C%' OR pt.product_line LIKE 'P%')");
				whereMap.put("searchLineTarget",
						" AND (tr.`product_line` LIKE 'X%' OR tr.`product_line`  LIKE 'C%' OR tr.`product_line` LIKE 'P%' )");

				object = soTableService.selectHQDataByYear(whereMap);
			} else if (what.equals("yearXCPByHQ")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchStr", "AND pt.product_line  like'%" + line + "%'");
				} else {
					whereMap.put("searchStr",
							"AND (pt.product_line   LIKE 'X%' OR pt.product_line  LIKE 'C%' OR pt.product_line   LIKE 'P%')");
				}
				object = soTableService.selectHQDataByYear(whereMap);
			} else if (what.equals("yearSmartByHQ")) {
				whereMap.put("searchLine", "AND pt.PRODUCT_SPEC_ID LIKE '%Smart%'");
				object = soTableService.selectHQDataByYear(whereMap);
			} else if (what.equals("yearBigByHQ")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchStr", line);
				}
				object = soTableService.selectHQDataByYear(whereMap);
			} else if (what.equals("yearCurvedByHQ")) {
				whereMap.put("searchLine", "AND pt.PRODUCT_SPEC_ID LIKE 'Curved%'");

				object = soTableService.selectHQDataByYear(whereMap);
			} else if (what.equals("QuarterTotalByHQ")) {
				object = soTableService.selectHQDataByQuarter(whereMap);
			} else if (what.equals("QuarterUDByHQ")) {
				whereMap.put("searchLine",
						"AND (pt.product_line LIKE 'X%' OR pt.product_line LIKE 'C%' OR pt.product_line LIKE 'P%')");
				whereMap.put("searchLineTarget",
						" AND (tr.`product_line` LIKE 'X%' OR tr.`product_line`  LIKE 'C%' OR tr.`product_line` LIKE 'P%' )");

				object = soTableService.selectHQDataByQuarter(whereMap);
			} else if (what.equals("QuarterXCPByHQ")) {
				if (model != null && !model.equals("")) {
					if (WebPageUtil.isHQRole()) {
						whereMap.put("searchStr", "AND tm.hq_model  like'%" + model + "%'");
					} else {
						whereMap.put("searchStr", "AND tm.branch_model  like'%" + model + "%'");
					}
					whereMap.put("searchLineTarget", " AND tr.`product_line` LIKE '" + line + "%' ");
				} else if (line != null && !line.equals("")) {
					whereMap.put("searchStr", "AND pt.product_line  like'%" + line + "%'");

					whereMap.put("searchLineTarget", " AND tr.`product_line` LIKE '" + line + "%' ");

				} else {
					whereMap.put("searchLine",
							"AND (pt.product_line LIKE 'X%' OR pt.product_line LIKE 'C%' OR pt.product_line LIKE 'P%')");

					whereMap.put("searchLineTarget",
							" AND (tr.`product_line` LIKE 'X%' OR tr.`product_line`  LIKE 'C%' OR tr.`product_line` LIKE 'P%' )");

				}
				object = soTableService.selectHQDataByQuarter(whereMap);
			} else if (what.equals("QuarterSmartByHQ")) {
				whereMap.put("searchLine", "AND pt.PRODUCT_SPEC_ID LIKE '%Smart%'");
				object = soTableService.selectHQDataByQuarter(whereMap);
			} else if (what.equals("QuarterBigByHQ")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchStr", line);
				}
				object = soTableService.selectHQDataByQuarter(whereMap);
			} else if (what.equals("QuarterCurvedByHQ")) {
				whereMap.put("searchLine", "AND pt.PRODUCT_SPEC_ID LIKE 'Curved%'");

				object = soTableService.selectHQDataByQuarter(whereMap);
			} else if (what.equals("yearTotalByHQChain")) {
				object = soTableService.selectHQChainDataByYear(whereMap);
			} else if (what.equals("yearUDByHQChain")) {
				whereMap.put("searchLine",
						"AND (pt.product_line LIKE 'X%' OR pt.product_line LIKE 'C%' OR pt.product_line LIKE 'P%')");
				object = soTableService.selectHQChainDataByYear(whereMap);
			} else if (what.equals("yearXCPByHQChain")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchStr", "AND pt.product_line  like'%" + line + "%'");
				} else {
					whereMap.put("searchStr",
							"AND (pt.product_line   LIKE 'X%' OR pt.product_line  LIKE 'C%' OR pt.product_line   LIKE 'P%')");
				}
				object = soTableService.selectHQChainDataByYear(whereMap);
			} else if (what.equals("yearSmartByHQChain")) {
				whereMap.put("searchLine", "AND pt.PRODUCT_SPEC_ID LIKE '%Smart%'");
				object = soTableService.selectHQChainDataByYear(whereMap);
			} else if (what.equals("yearBigByHQChain")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchStr", line);
				}
				object = soTableService.selectHQChainDataByYear(whereMap);
			} else if (what.equals("yearCurvedByHQChain")) {
				whereMap.put("searchLine", "AND pt.PRODUCT_SPEC_ID LIKE 'Curved%'");

				object = soTableService.selectHQChainDataByYear(whereMap);
			} else if (what.equals("HalfTotalByHQChain")) {
				object = soTableService.selectHQChainDataByHalf(whereMap);
			} else if (what.equals("HalfUDByHQChain")) {
				whereMap.put("searchLine",
						"AND (pt.product_line LIKE 'X%' OR pt.product_line LIKE 'C%' OR pt.product_line LIKE 'P%')");
				object = soTableService.selectHQChainDataByHalf(whereMap);
			} else if (what.equals("HalfXCPByHQChain")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchStr", "AND pt.product_line  like'%" + line + "%'");
				} else {
					whereMap.put("searchStr",
							"AND (pt.product_line   LIKE 'X%' OR pt.product_line  LIKE 'C%' OR pt.product_line   LIKE 'P%')");
				}
				object = soTableService.selectHQChainDataByHalf(whereMap);
			} else if (what.equals("HalfSmartByHQChain")) {
				whereMap.put("searchLine", "AND pt.PRODUCT_SPEC_ID LIKE '%Smart%'");
				object = soTableService.selectHQChainDataByHalf(whereMap);
			} else if (what.equals("HalfBigByHQChain")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchStr", line);
				}
				object = soTableService.selectHQChainDataByHalf(whereMap);
			} else if (what.equals("HalfCurvedByHQChain")) {
				whereMap.put("searchLine", "AND pt.PRODUCT_SPEC_ID LIKE 'Curved%'");

				object = soTableService.selectHQChainDataByHalf(whereMap);
			} else if (what.equals("QuarterTotalByHQChain")) {
				object = soTableService.selectHQChainDataByQuarter(whereMap);
			} else if (what.equals("QuarterUDByHQChain")) {
				whereMap.put("searchLine",
						"AND (pt.product_line LIKE 'X%' OR pt.product_line LIKE 'C%' OR pt.product_line LIKE 'P%')");
				object = soTableService.selectHQChainDataByQuarter(whereMap);
			} else if (what.equals("QuarterXCPByHQChain")) {
				if (model != null && !model.equals("")) {
					if (WebPageUtil.isHQRole()) {
						whereMap.put("searchStr", "AND tm.hq_model  like'%" + model + "%'");
					} else {
						whereMap.put("searchStr", "AND tm.branch_model  like'%" + model + "%'");
					}

				} else if (line != null && !line.equals("")) {
					whereMap.put("searchStr", "AND pt.product_line  like'%" + line + "%'");
				} else {
					whereMap.put("searchStr",
							"AND (pt.product_line   LIKE 'X%' OR pt.product_line  LIKE 'C%' OR pt.product_line   LIKE 'P%')");
				}
				object = soTableService.selectHQChainDataByQuarter(whereMap);
			} else if (what.equals("QuarterSmartByHQChain")) {
				whereMap.put("searchLine", "AND pt.PRODUCT_SPEC_ID LIKE '%Smart%'");
				object = soTableService.selectHQChainDataByQuarter(whereMap);
			} else if (what.equals("QuarterBigByHQChain")) {
				if (line != null && !line.equals("")) {
					whereMap.put("searchStr", line);
				}
				object = soTableService.selectHQChainDataByQuarter(whereMap);
			} else if (what.equals("QuarterCurvedByHQChain")) {
				whereMap.put("searchLine", "AND pt.PRODUCT_SPEC_ID LIKE 'Curved%'");

				object = soTableService.selectHQChainDataByQuarter(whereMap);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("=======sumTwo================" + object);

		WebPageUtil.writeBack(object.toString());
	}

	public void selectPartyCus() {
		JSONArray array = new JSONArray();
		String conditions = "";
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
		Map<String, Object> whereMap = new HashMap<String, Object>();

		whereMap.put("conditions", conditions);
		whereMap.put("isHq", WebPageUtil.isHQRole());

		List<HashMap<String, Object>> object = null;
		try {

			object = soTableService.selectPartyCus(whereMap);

			array = JSONArray.fromObject(object);

		} catch (Exception e) {
			e.printStackTrace();
		}

		WebPageUtil.writeBack(array.toString());
	}

	public void selectXCPLine() {
		JSONArray array = new JSONArray();
		Map<String, Object> whereMap = new HashMap<String, Object>();
		String line = request.getParameter("line");
		if (line == null || line.equals("")) {
			whereMap.put("searchStr", "AND (cfg.PVALUE LIKE 'X%' OR cfg.PVALUE LIKE 'C%' OR cfg.PVALUE LIKE 'P%')");
		} else {
			whereMap.put("searchStr", "AND cfg.PVALUE LIKE '%" + line + "%'");

		} // AND cfg.PVALUE LIKE '%AND cfg.`PVALUE` LIKE 'X%'%'

		List<HashMap<String, Object>> object = null;
		try {

			object = soTableService.selectXCPLine(whereMap);

			array = JSONArray.fromObject(object);

		} catch (Exception e) {
			e.printStackTrace();
		}

		WebPageUtil.writeBack(array.toString());
	}

	public void selectCurvedModel() {
		JSONArray array = new JSONArray();
		Map<String, Object> whereMap = new HashMap<String, Object>();
		String conditions = "";
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
		whereMap.put("conditions", conditions);

		List<HashMap<String, Object>> object = null;
		try {

			object = soTableService.selectCurvedModel(whereMap);

			array = JSONArray.fromObject(object);

		} catch (Exception e) {
			e.printStackTrace();
		}

		WebPageUtil.writeBack(array.toString());
	}

	public void queryStateTimeSalesByBDSC() {
		response.setHeader("Content-Type", "application/json");

		String subXcp = request.getParameter("subXcp");
		String seriesXcp = request.getParameter("seriesXcp");
		String model = request.getParameter("model");

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String timeType = request.getParameter("timeType");
		// String xcpWhere = request.getParameter("xcpWhere");
		String tab = request.getParameter("tab");
		String coeff = request.getParameter("coeff");
		String halfAYear = request.getParameter("selectHAY");

		Map<String, Object> whereMap = new HashMap<String, Object>();
		String sqlWhere = "";

		if (Contents.XCP_TAB.equals(tab)) {
			sqlWhere += getXCPWhereSql(subXcp, seriesXcp, model);
		} else if (Contents.BIG_TAB.equals(tab)) {
		} else {
			sqlWhere += getHqWhereSql(tab);
		}

		String conditions = "";
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
		whereMap.put("conditions", conditions);

		whereMap.put("startDate", startDate);
		whereMap.put("endDate", endDate);
		whereMap.put("timeType", timeType);
		whereMap.put("tab", tab);
		whereMap.put("searchStr", sqlWhere);
		whereMap.put("coeff", coeff);
		whereMap.put("halfAYear", halfAYear);

		List<HashMap<String, Object>> l = null;
		try {
			l = soTableService.queryStateTimeSalesBycountry(whereMap);

		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONObject j = new JSONObject();
		j.accumulate("data", l);
		System.out.println("result=============" + j.toString());
		WebPageUtil.writeBack(j.toString());
	}

	public String getHqWhereSql(String tab) {
		if (Contents.UD_TAB.equals(tab)) {
			return " and (pt.product_line like 'X%' or pt.product_line like 'C%' or pt.product_line like 'P%' ) ";
		} else if (Contents.SMART_TAB.equals(tab)) {
			return " and pt.`PRODUCT_SPEC_ID` like '%smart%' ";
		} else if (Contents.CURVED_TAB.equals(tab)) {
			return " and pt.`PRODUCT_SPEC_ID` like 'Curved%' ";
		}
		return " and 1=1";
	};

	public String getXCPWhereSql(String subXcp, String seriesXcp, String model) {
		String xcpWhere = "";
		if (model != null && !model.equals("")) {
			if (WebPageUtil.isHQRole()) {
				xcpWhere = "AND tm.hq_model  like'%" + model + "%'";
			} else {
				xcpWhere = "AND tm.branch_model  like'%" + model + "%'";
			}

		} else if (WebPageUtil.isStringNullAvaliable(subXcp)) {
			xcpWhere = " and pt.product_line like '" + subXcp + "%'";
		} else {
			if (WebPageUtil.isStringNullAvaliable(seriesXcp)) {
				xcpWhere = " and pt.product_line like '" + seriesXcp + "%'";
			} else {
				xcpWhere = " and (pt.product_line like 'X%' or pt.product_line like 'C%' or pt.product_line like 'P%' ) ";
			}
		}
		return xcpWhere;
	};

	public void selectXCPModelTwo() {
		JSONArray array = new JSONArray();
		Map<String, Object> whereMap = new HashMap<String, Object>();
		String line = request.getParameter("line");

		String conditions = "";
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
		whereMap.put("conditions", conditions);
		whereMap.put("isHq", WebPageUtil.isHQRole());
		if (line == null || line.equals("")) {
			whereMap.put("searchStr",
					"AND (pt.product_line  LIKE 'X%' OR pt.product_line  LIKE 'C%' OR pt.product_line  LIKE 'P%')");
		} else {
			whereMap.put("searchStr", "AND pt.product_line  LIKE '%" + line + "%'");

		}

		List<HashMap<String, Object>> object = null;
		try {
			object = soTableService.selectXCPModel(whereMap);

			array = JSONArray.fromObject(object);

		} catch (Exception e) {
			e.printStackTrace();
		}

		WebPageUtil.writeBack(array.toString());
	}

}
