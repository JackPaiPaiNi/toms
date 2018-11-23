package cn.tcl.platform.barcode.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.barcode.vo.Barcode;

public interface IBarcodeDao {
	/**
	 * 列表数据
	 * @return
	 */
	public List<Barcode> selectBarcodes(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="keyword") String keyword,
			@Param(value="order") String order,
			@Param(value="sort") String sort) throws Exception;
	
	/**
	 *总数
	 * @return
	 */
	public int countBarcodes(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="keyword") String keyword) throws Exception;
	
	public Barcode selectBarcodeById(String id) throws Exception;
	
	public void editBarcodeBean(Barcode barcode) throws Exception;
	
	public void addBarcode(Barcode barcode) throws Exception;

	public void deleteBarcode(Barcode barcode) throws Exception;
}
