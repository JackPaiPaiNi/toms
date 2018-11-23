package cn.tcl.platform.district.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.platform.district.dao.IDistrictDao;
import cn.tcl.platform.district.vo.City;
import cn.tcl.platform.district.vo.Country;
import cn.tcl.platform.district.vo.County;
import cn.tcl.platform.district.vo.Province;
import cn.tcl.platform.district.vo.Town;

@Service("districtService")
public class DistrictServiceImpl implements IDistrictService {
	@Autowired
	private IDistrictDao districtDao;
	
	@Override
	public List<Country> getCountryList(String conStr) throws Exception {
		return districtDao.getCountryList(conStr);
	}

	@Override
	public  List<Province> getProvinceList(String countryId,String conStr) throws Exception {
		return districtDao.getProvinceList(countryId,conStr);
	}

	@Override
	public List<City> getCityList(String provinceId,String conStr) throws Exception {
		return districtDao.getCityList(provinceId,conStr);
	}

	@Override
	public List<County> getCountyList(String cityId,String conStr) throws Exception {
		return districtDao.getCountyList(cityId,conStr);
	}

	@Override
	public List<Town> getTownList(String countyId,String conStr) throws Exception {
		return districtDao.getTownList(countyId,conStr);
	}

	@Override
	public List<Country> selectCountry(String conSt) throws Exception {
		return districtDao.selectCountry(conSt);
	}

	@Override
	public List<Province> selectProvince(String partyId)
			throws Exception {
		return districtDao.selectProvince(partyId);
	}

	@Override
	public List<City> loadCityList(String countryId) throws Exception {
		return districtDao.loadCityList(countryId);
	}

}
