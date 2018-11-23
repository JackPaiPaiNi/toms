package cn.tcl.platform.customer.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.platform.customer.dao.ICustomerDao;
import cn.tcl.platform.customer.service.ICustomerService;
import cn.tcl.platform.customer.vo.Customer;
import cn.tcl.platform.customer.vo.CustomerUser;
import cn.tcl.platform.party.dao.IPartyDAO;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.user.vo.UserLogin;
@Service("customerService")
public class CustomerServiceImpl implements ICustomerService {
	@Autowired
	private ICustomerDao customerDao;
	@Autowired
	private IPartyDAO partyDao;
	
	@Override
	public Map<String, Object> selectCustomersData(int start, int limit, 
			String searchStr,String countryId,String provinceId,
			String order, String sort,String conditions,String bt) throws Exception {
		
		String _countryId = countryId == "" ? null : countryId;
		String _provinceId = provinceId == "" ? null : provinceId;
		
		List<Customer> list=customerDao.selectCustomers(start, limit, searchStr,_countryId,_provinceId,order, sort,conditions,bt);
		Map<String, Object> map=new HashMap<String, Object>();
		int count=customerDao.countCustomers(start, limit, searchStr,_countryId,_provinceId,conditions);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}

//	@Override
//	public void saveCustomer(Customer customer) throws Exception {
//		customerDao.saveCustomer(customer);
//	}

	@Override
	public Customer getCustomer(String cid) throws Exception {
		return customerDao.getCustomer(cid);
	}

//	@Override
//	public void editCustomer(Customer customer) throws Exception {
//		customerDao.editCustomer(customer);
//	}

	@Override
	public void importCustomer(List<Customer> customers) throws Exception {
		if(customers!=null){
			for (Customer customer : customers) {
				//处理表的关联关系
				
				//客户所属机构
				String partyName=customer.getPartyId();//导入时实际上输入的是机构的名字，这里根据名字找到对应的ID
				Party party=partyDao.getOnePartyByName(partyName);
				customer.setPartyId(party==null?null:party.getPartyId());
				
				//国家省市县镇
				String countryName=customer.getCountryId();
				String country = customerDao.getCountryByName(countryName);
				
				String provinceName=customer.getProvinceId();
				String province = customerDao.getProvinceByName(provinceName);
				
//				String cityName=customer.getCityId();
//				String city = customerDao.getCityByName(cityName);
				
//				String countyName = customer.getCountyId();
//				String county = customerDao.getCountyByName(countyName);
				
//				String townName = customer.getTownId();
//				String town = customerDao.getTownByName(townName);
				
				//渠道类型
				String channelTypeName = customer.getChannelType();
				String channelType = customerDao.getChannelByName(channelTypeName);

				customer.setCountryId(country == null ? null:country);
				customer.setProvinceId(province == null?null:province);
//				customer.setCityId(city == null?null:city);
//				customer.setCountyId(county == null?null:county);
//				customer.setTownId(town == null?null:town);
				customer.setChannelType(channelType == null?null:channelType);
				
				customer.setCreateDate(new Date());
				customer.setStatus("1");
				customerDao.saveCustomer(customer);
			}
		}
	}

	@Override
	public void deleteCustomer(Customer customer) throws Exception {
		customerDao.deleteCustomer(customer);
	}

	@Override
	public Customer getRepeatByCustomerCode(String code) throws Exception {
		return customerDao.getRepeatByCustomerCode(code);
	}

	@Override
	public Party getOnePartyByName(String partyName) throws Exception {
		return partyDao.getOnePartyByName(partyName);
	}

	@Override
	public int getCountByChannel(String channelType) throws Exception {
		return customerDao.getCountByChannel(channelType);
	}

	@Override
	public int getCountryByName(String searchName) throws Exception {
		return customerDao.getCountryByNameCount(searchName);
	}
	
	@Override
	public String getCountryByNames(String searchName) throws Exception {
		return customerDao.getCountryByName(searchName);
	}

	@Override
	public int getProvinceByName(String searchName) throws Exception {
		return customerDao.getProvinceByNameCount(searchName);
	}

	@Override
	public int getCityByName(String searchName) throws Exception {
		return customerDao.getCityByNameCount(searchName);
	}

	@Override
	public int getCountyByName(String searchName) throws Exception {
		return customerDao.getCountyByNameCount(searchName);
	}

	@Override
	public int getTownByName(String searchName) throws Exception {
		return customerDao.getTownByNameCount(searchName);
	}

	@Override
	public XSSFWorkbook exporDealerName(String conditions,
			String[] excelHeader, String title) throws Exception {
		//先查询出 导出的数据内容
		String key=null;
		List<Customer> list=customerDao.selectCustomerName(conditions,null);
		//设置 表头宽度
		int[] excelWidth = {120,120,120,120,130,120,200,130,130,120};
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		 XSSFSheet sheet = workbook.createSheet(title);
		 XSSFRow headerRow = sheet.createRow(0);
		 
		 //导出字体样式
		 XSSFFont font = workbook.createFont();
		 font.setFontHeightInPoints((short) 12); // 字体大小
		 font.setFontName("Times New Roman");
		 font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 
		 //导出样式
         XSSFCellStyle style = workbook.createCellStyle();    
         style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         style.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
         style.setBottomBorderColor(HSSFColor.BLACK.index);
         style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
         style.setLeftBorderColor(HSSFColor.BLACK.index);
         style.setBorderRight(HSSFCellStyle.BORDER_THIN);
         style.setRightBorderColor(HSSFColor.BLACK.index);
         style.setBorderTop(HSSFCellStyle.BORDER_THIN);
         style.setTopBorderColor(HSSFColor.BLACK.index);
         style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
         style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         style.setFont(font);
         
         
         //导出字体样式
		 XSSFFont fontTwo = workbook.createFont();
		 fontTwo.setFontHeightInPoints((short) 10); // 字体大小
		 fontTwo.setFontName("Times New Roman");

		 
		 //导出样式
         XSSFCellStyle styleTwo = workbook.createCellStyle();    
         styleTwo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleTwo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         
         styleTwo.setFont(fontTwo);
		 
		 
		 
         //--------------------------------------------------------------
		 for (int i = 0; i < excelWidth.length; i++) {  
			    sheet.setColumnWidth(i, 32 * excelWidth[i]);  
		 } 
		 
         XSSFRow row = sheet.createRow(0);
         //表头数据
         for (int i = 0; i < excelHeader.length; i++) {   
             XSSFCell cell = row.createCell(i);    
             cell.setCellValue(excelHeader[i]);    
             cell.setCellStyle(style);  
             
         }   
         
         //表体数据
         for (int i = 0; i < list.size(); i++) {    
             row = sheet.createRow(i + 1);    
             Customer customer = list.get(i);
             
             //-------------单元格-------------------
             
            
             if(customer.getCustomerName()!=null  && !customer.getCustomerName().equals("")){
            	 XSSFCell cell0 = row.createCell(0);
                 cell0.setCellValue(customer.getCustomerName());    
                 cell0.setCellStyle(styleTwo);
             }
            
             if(customer.getCustomerCode()!=null  && !customer.getCustomerCode().equals("")){
            	 XSSFCell cell0 = row.createCell(1);
                 cell0.setCellValue(customer.getCustomerCode());    
                 cell0.setCellStyle(styleTwo);
             }
            
            
             
            
         }    
         
         return workbook;
}

	@Override
	public List<Party> selectParty(String countryId) throws Exception {		
		return customerDao.selectParty(countryId);
	}

	@Override
	public List<UserLogin> selectCustomerSaler(String conditions, int type,
			String customerId) throws Exception {
		return customerDao.selectCustomerSaler(conditions, type,customerId);
	}

	@Override
	public void saveCustomer(Customer customer, String salers,String pros,
			String supers) throws Exception {
		customerDao.saveCustomer(customer);
//		customerDao.deleteRelation(customer.getCustomerId());
		
		if(salers!=null && !"".equals(salers)){
			String[] lists = salers.split(";");
			for (String list : lists) {
				customerDao.insertSalers(customer.getCustomerId(), list, 0);
				
			}
		}
		if(pros!=null && !"".equals(pros)){
			String[] lists2 = pros.split(";");
			for (String list : lists2) {
				customerDao.insertSalers(customer.getCustomerId(), list, 1);
			}
		}
		if(supers!=null && !"".equals(supers)){
			String[] lists1 = supers.split(";");
			for (String list1 : lists1) {
				customerDao.insertSalers(customer.getCustomerId(), list1, 2);
			}
		}
		
	}

	@Override
	public List<CustomerUser> getCustomerUserRelations(String partyId,String customerId)
			throws Exception {
		return customerDao.getCustomerUserRelations(partyId,customerId);
	}

	@Override
	public void editCustomer(Customer customer, String salers,String pros, String supers)
			throws Exception {
		customerDao.editCustomer(customer);
		customerDao.deleteRelation(customer.getCustomerId());
		
		if(salers!=null && !"".equals(salers)){
			String[] lists = salers.split(";");
			for (String list : lists) {
				customerDao.insertSalers(customer.getCustomerId(), list, 0);
				
			}
		}
		
		if(pros!=null && !"".equals(pros)){
			String[] lists2 = pros.split(";");
			System.out.println(pros+"------------------lists2---------");
			for (String list : lists2) {
				customerDao.insertSalers(customer.getCustomerId(), list, 1);
			}
		}
		
		if(supers!=null && !"".equals(supers)){
			String[] lists1 = supers.split(";");
			for (String list1 : lists1) {
				customerDao.insertSalers(customer.getCustomerId(), list1, 2);
			}
		}
		
	}
	
	/**
	 * 根据国家ID获取所属渠道
	 * @param countryId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<CustomerUser> getCustomerByCountry(String countryId) throws Exception {
		return customerDao.getCustomerByCountry(countryId);
	}

	@Override
	public List<UserLogin> selectCustomerSalerData(String conditions, int type,
			String customerId) throws Exception {
		return customerDao.selectCustomerSalerData(conditions, type, customerId);
	}


}
