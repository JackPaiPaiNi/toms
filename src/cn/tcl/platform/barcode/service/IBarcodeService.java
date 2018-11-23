package cn.tcl.platform.barcode.service;

import java.util.Map;

import cn.tcl.platform.barcode.vo.Barcode;

public interface IBarcodeService {
	
	public Map<String,Object> selectBarcodeData(int start,int  limit,String keyword,String order,String sort) throws Exception;

	public Barcode getBarcodeByid(String id) throws Exception;
	
	public void editBarcode(Barcode barcode) throws Exception;
	
	public void addBarcode(Barcode barcode) throws Exception;
	
	public void deleteBarcode(Barcode barcode) throws Exception;
}
