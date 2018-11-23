package cn.tcl.platform.ws.service;


import java.util.List;

import javax.jws.WebService;

import cn.tcl.platform.sale.vo.Sale;
import cn.tcl.platform.ws.vo.SaleReq;

@WebService
public interface ISaleService {
	
	public String InputSaleData(List<SaleReq> saledataList) throws Exception;
}
