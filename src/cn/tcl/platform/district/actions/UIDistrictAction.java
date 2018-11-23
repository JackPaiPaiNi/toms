package cn.tcl.platform.district.actions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.district.service.IDistrictService;
import cn.tcl.platform.district.vo.City;
import cn.tcl.platform.district.vo.Country;
import cn.tcl.platform.district.vo.County;
import cn.tcl.platform.district.vo.Province;
import cn.tcl.platform.district.vo.Town;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("serial")
public class UIDistrictAction extends BaseAction {
	
	@Autowired(required = false)
	@Qualifier("districtService")
	private IDistrictService districtService;
	
	
	public void onloadCountry(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try {
			request.setCharacterEncoding("UTF-8");
			String conSt=request.getParameter("id");
			List<Country> contries = districtService.selectCountry(conSt);
			String rows=JSONArray.fromObject(contries).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//获取国家数据
	public void getCountryList(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try {
			request.setCharacterEncoding("utf-8");
			String conStr = request.getParameter("_searchFlag");
			System.out.println(conStr);
			List<Country> countries=districtService.getCountryList(conStr);
			result = JSONArray.fromObject(countries);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	//获取省数据
	public void selectProvince(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("countryId");
		try {
			request.setCharacterEncoding("utf-8");
			List<Province> provinces=districtService.selectProvince(partyId);
			String rows = JSONArray.fromObject(provinces).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			
		}
		WebPageUtil.writeBack(result.toString());
	}
	public void getProvinceList(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try {
			request.setCharacterEncoding("utf-8");
			String conStr = request.getParameter("_searchFlag");
			String countryId=request.getParameter("countryId");
			List<Province> provinces=districtService.getProvinceList(countryId,conStr);
			result = JSONArray.fromObject(provinces);
		} catch (Exception e) {
		}
		WebPageUtil.writeBack(result.toString());
	}
	//获取市数据
	public void getCityList(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try {
			request.setCharacterEncoding("utf-8");
			String conStr = request.getParameter("_searchFlag");
			String provinceId=request.getParameter("provinceId");
			List<City> cities=districtService.getCityList(provinceId,conStr);
			result = JSONArray.fromObject(cities);
		} catch (Exception e) {
		}
		WebPageUtil.writeBack(result.toString());
	}
	//获取县数据
	public void getCountyList(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try {
			request.setCharacterEncoding("utf-8");
			String conStr = request.getParameter("_searchFlag");
			String cityId=request.getParameter("cityId");
			List<County> counties=districtService.getCountyList(cityId,conStr);
			result = JSONArray.fromObject(counties);
		} catch (Exception e) {
		}
		WebPageUtil.writeBack(result.toString());
	}
	//获取乡数据
	public void getTownList(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try {
			request.setCharacterEncoding("utf-8");
			String conStr = request.getParameter("_searchFlag");
			String countyId=request.getParameter("countyId");
			List<Town> towns=districtService.getTownList(countyId,conStr);
			result = JSONArray.fromObject(towns);
		} catch (Exception e) {
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void loadCityList(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		String countryId = request.getParameter("countryId");
		
		try {
			List<City> list = districtService.loadCityList(countryId);
			result = JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
}
