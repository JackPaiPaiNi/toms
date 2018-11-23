package cn.tcl.platform.district.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.district.vo.City;
import cn.tcl.platform.district.vo.Country;
import cn.tcl.platform.district.vo.County;
import cn.tcl.platform.district.vo.Province;
import cn.tcl.platform.district.vo.Town;

public interface IDistrictDao {
	//获取国家数据
	public List<Country> getCountryList(@Param(value="conStr") String conStr) throws Exception;
	//获取省数据
	public List<Province> selectProvince(@Param("partyId")String partyId) throws Exception;
	public List<Province> getProvinceList(@Param(value="countryId") String countryId,
			@Param(value="conStr") String conStr) throws Exception;
	//获取市数据
	public List<City> getCityList(@Param(value="provinceId") String provinceId,
			@Param(value="conStr") String conStr) throws Exception;
	//获取县数据
	public List<County> getCountyList(@Param(value="cityId") String cityId,
			@Param(value="conStr") String conStr) throws Exception;
	//获取乡数据
	public List<Town> getTownList(@Param(value="countyId") String countyId,
			@Param(value="conStr") String conStr) throws Exception;
	public List<Country> selectCountry(@Param(value="conSt") String conSt) throws Exception;
	
	/**
	 * 根据国家获取市的数据
	 * @param countryId
	 * @return
	 * @throws Exception
	 */
	public List<City> loadCityList(@Param("countryId")String countryId) throws Exception;
}
