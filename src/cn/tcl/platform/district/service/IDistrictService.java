package cn.tcl.platform.district.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.district.vo.City;
import cn.tcl.platform.district.vo.Country;
import cn.tcl.platform.district.vo.County;
import cn.tcl.platform.district.vo.Province;
import cn.tcl.platform.district.vo.Town;

public interface IDistrictService {
	//获取国家数据
	public  List<Country>  getCountryList(String conStr) throws Exception;
	//获取省数据
	public List<Province> selectProvince(String partyId) throws Exception;
	public  List<Province>  getProvinceList(String countryId,String conStr) throws Exception;
	//获取市数据
	public  List<City>  getCityList(String provinceId,String conStr) throws Exception;
	//获取县数据
	public  List<County>  getCountyList(String cityId,String conStr) throws Exception;
	//获取乡数据
	public  List<Town>  getTownList(String countyId,String conStr) throws Exception;
	public List<Country> selectCountry(String conSt) throws Exception;
	
	//根据国家获取市数据
	public List<City> loadCityList(String countryId) throws Exception;
}
