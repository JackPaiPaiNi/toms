package cn.tcl.platform.BDExcel.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.BDExcel.vo.BDSCImportExcel;


public interface IBDExcelDao {
	public   List<HashMap<String, Object>>  selectDatas(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "isHq") boolean isHq) throws Exception;
	
	public List<HashMap<String, Object>> selectModelListByHq(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	

	public List<HashMap<String, Object>> selectModelByHead(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public  List<HashMap<String, Object>>  selectTargetByshop(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate,
			@Param(value = "type") String type,
			@Param(value = "isHq") boolean isHq
			);
	public  List<HashMap<String, Object>>  selectTargetByYear(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate,
			@Param(value = "type") String type,
			@Param(value = "isHq") boolean isHq
			);

	
	

	
	
	public List<HashMap<String, Object>>  selectSaleDataBySize(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	public List<HashMap<String, Object>>  selectSaleDataBySpec(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	public List<HashMap<String, Object>>  selectSaleTotalBySpec(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	
	public List<HashMap<String, Object>>  selectSaleDataBySpecYTD(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	public List<HashMap<String, Object>>  selectSaleDataBySpecYTDCUSTOM(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	
	
	
	public List<HashMap<String, Object>>  selectSaleTotalBySpecYTD(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	public List<HashMap<String, Object>>  selectSaleTotalBySpecYTDCUSTOM(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	
	
	public LinkedList<LinkedHashMap<String, Object>>  selectSaleDataByModelYTD(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	
	public LinkedList<LinkedHashMap<String, Object>>  selectSaleDataByModelYTDCUSTOM(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	public List<HashMap<String, Object>>  selectSaleDataByXCP(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	

	
	
	public List<HashMap<String, Object>>  selectSaleTotalByXCP(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	
	
	public int saveCountryTarget(List<BDSCImportExcel > reddemCodeList) throws Exception;
	public int deleteCountryLineTarget(List<BDSCImportExcel > reddemCodeList) throws Exception;
	public int deleteCountryTarget(List<BDSCImportExcel > reddemCodeList) throws Exception;
	public int deleteCountryUDTargetYear(List<BDSCImportExcel > reddemCodeList) throws Exception;

	
	public int deleteCountryTargetYear(List<BDSCImportExcel > reddemCodeList) throws Exception;

	public List<HashMap<String,Object>> selectSaleTotalBySize(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);


	public List<HashMap<String, Object>> selectPartyNameByuser(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);


	

	public String selectPartyByUser(@Param(value = "userId") String userId);

	
	public String selectCountryByUser(@Param(value = "userId") String userId);
	public int selectCountByLine(@Param(value="line") String line) throws Exception;
	public String selectCountry(@Param(value = "country") String country)
			throws Exception;
	
	public List<HashMap<String,Object>> selectBDSCTarget(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "type") String type
			);
	public List<HashMap<String,Object>> selectBDSCTargetYear(
			@Param(value = "date") String date,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "type") String type
			);

	
}
