package cn.tcl.platform.barcode.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.platform.barcode.dao.IBarcodeDao;
import cn.tcl.platform.barcode.service.IBarcodeService;
import cn.tcl.platform.barcode.vo.Barcode;

@Service("barcodeService")
public class IBarcodeServiceImpl implements IBarcodeService{
	
	@Autowired
	private IBarcodeDao barcodeDao;

	@Override
	public Map<String, Object> selectBarcodeData(int start, int limit, String keyword, String order, String sort) throws Exception {
		List<Barcode> list = barcodeDao.selectBarcodes(start, limit, keyword, order, sort);
		Map<String, Object> map = new HashMap<String, Object>();
		int count = barcodeDao.countBarcodes(start, limit, keyword);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}

	@Override
	public Barcode getBarcodeByid(String id) throws Exception{
		return barcodeDao.selectBarcodeById(id);
	}

	@Override
	public void editBarcode(Barcode barcode) throws Exception{
		barcodeDao.editBarcodeBean(barcode);
	}

	@Override
	public void addBarcode(Barcode barcode) throws Exception {
		barcodeDao.addBarcode(barcode);
	}

	@Override
	public void deleteBarcode(Barcode barcode) throws Exception {
		barcodeDao.deleteBarcode(barcode);
	}
	
	
	

}
