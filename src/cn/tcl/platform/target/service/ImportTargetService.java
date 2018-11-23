package cn.tcl.platform.target.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.customer.vo.Customer;
import cn.tcl.platform.excel.vo.Excel;
import cn.tcl.platform.excel.vo.ImportExcel;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.target.vo.Target;

public interface ImportTargetService {

	public String readExcel(File file, String fileName,String name) throws IOException;
	public String readChannel(File file) throws IOException ;
	public String readOffice(File file) throws IOException ;
	public String readReg(File file) throws IOException ;
	public String readBranch(File file) throws IOException ;
	public String readRole(File file,String role) throws IOException ;
	public List<Customer> selectCustomer(String partyId) throws Exception;
	public List<Shop> selectShop(String partyId) throws Exception;
	public List<Shop> selectSale(Map<String,Object> map) throws Exception;
	public List<Shop> selectManager(String partyId) throws Exception;
	public List<Shop> selectProduct(String partyId) throws Exception;
	public List<Target> chooseRegion(String partyId) throws Exception;
	public List<Target> chooseOffice(String partyId) throws Exception;
	
}
