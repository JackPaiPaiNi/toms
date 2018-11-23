package cn.tcl.platform.ws.service.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.customer.dao.ICustomerDao;
import cn.tcl.platform.customer.vo.Customer;
import cn.tcl.platform.modelmap.dao.IModelMapDao;
import cn.tcl.platform.modelmap.vo.ModelMap;
import cn.tcl.platform.sale.dao.ISaleDao;
import cn.tcl.platform.sale.vo.Sale;
import cn.tcl.platform.shop.dao.IShopDao;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.ws.dao.DMXProductDao;
import cn.tcl.platform.ws.dao.DMXShopDao;
import cn.tcl.platform.ws.dao.SaleDao;
import cn.tcl.platform.ws.service.ISaleService;
import cn.tcl.platform.ws.vo.DMXProduct;
import cn.tcl.platform.ws.vo.DMXShop;
import cn.tcl.platform.ws.vo.SaleReq;

@WebService(endpointInterface="cn.tcl.platform.ws.service.ISaleService")
public class ISaleServiceImpl implements ISaleService {
	
	protected Logger log = Logger.getLogger(this.getClass());
		
	@Autowired
	private SaleDao saleDaoWs;
	
	@Autowired
	private IShopDao shopDao;
	
	@Autowired
	private DMXShopDao dmxShopDao;
	
	@Autowired
	private ICustomerDao customerDao;
	
	@Autowired
	private DMXProductDao dmxProductDao;
	
	@Autowired
	private IModelMapDao modelmapDao;
			

	@Override
	public String InputSaleData(List<SaleReq> saledataList) throws Exception {
		// TODO Auto-generated method stub
		log.info("invoke service Start time:"+WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		log.info("read InputSaleData Start time:"+WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		//越南分公司的partyId
		String partyId="19";
		//世界电子渠道名称
		//String customerName="CAO PHONG";
		String customerName="DMX";
		List<Customer> clst=null;
		//验证世界电子这个渠道是否维护
		clst=customerDao.getCustomerByName(customerName);
		if(clst==null ||clst.size()==0)
		{
			log.info("the customer named '"+customerName+"' is unmaintened in the system");
			return "the customer named 'CAO PHONG' is unmaintened in the system";
		}
		//查询渠道id
		Map<String,SaleReq> saledatamap = new HashMap<String,SaleReq>();
		//数据不规范的集合
		List<SaleReq> incorrectmodelLst=new LinkedList<SaleReq>();
		List<SaleReq> incorrectshopLst =new LinkedList<SaleReq>();
		List<SaleReq> incorrectsodateLst =new LinkedList<SaleReq>();
		//遍历输入数据列表
		SaleReq inputreq=null;
		SaleReq storereq=null;
		int count=0;
		String key=null;
		log.info("InputSaleData  Size:"+saledataList.size());
		if(saledataList==null||saledataList.size()==0)
		{
			return "error,there is no data";
		}
		if(saledataList.size()>1000)
		{
			return "there is too much data,the most amount is 1000";
		}
		log.info("traverse InputData Start time:"+WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		for(int i=0;i<saledataList.size();i++)
		{
			inputreq=saledataList.get(i);
			//判断是否填写有model
			if(inputreq.getModel()==null||inputreq.getModel()=="")
			{
				incorrectmodelLst.add(inputreq);
				continue;
			}
			//判断是否填写有shopName
			if(inputreq.getShopName()==null||inputreq.getModel()=="")
			{
				incorrectshopLst.add(inputreq);
				continue;
			}
			//判断是否填写有销售日期
			if(inputreq.getSoDate()==null)
			{
				incorrectsodateLst.add(inputreq);
				continue;
			}
			//处理日期格式
			key=inputreq.getModel()+inputreq.getShopName()+WebPageUtil.dateToStr(inputreq.getSoDate());
			count=inputreq.getCount()!=0?inputreq.getCount():1;
			if(saledatamap.containsKey(key))
			{
				storereq=saledatamap.get(key);			
				storereq.setCount(storereq.getCount()+count);
				saledatamap.put(key, storereq);
			}
			else
			{
				inputreq.setCount(count);
				saledatamap.put(key, inputreq);
			};
		}
		log.info("traverse InputData End time:"+WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		//需要添加销售数据纪录的集合
		List<Sale> saledataAdd=new LinkedList<Sale>();
		//需要更新销售数据纪录的集合
		List<Sale> saledataUpdate=new LinkedList<Sale>();
		//门店不存在的集合
		List<DMXShop> lackdmxShops=new LinkedList<DMXShop>();
		//产品型号不存在的集合
		//Map<String,SaleReq> UnmaintainedModels=new HashMap<String,SaleReq>();
		List<DMXProduct> lackdmxProduct=new LinkedList<DMXProduct>();
		//临时对象
		Sale sale=null;
		DMXShop lackShop=null;
		DMXProduct lackProduct=null;
		storereq=null;
		List<Shop> solst=null;
		List<ModelMap> mlst=null;
		List<DMXShop> dshoplst=null;
		List<DMXProduct> dplst=null;
		StringBuilder remarkSb=new StringBuilder();
		log.info("traversed data size:"+saledatamap.size());
		log.info("traverse and classification InputData Start time:"+WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		for(Map.Entry<String,SaleReq> entry:saledatamap.entrySet())
		{
			storereq=entry.getValue();
			solst=shopDao.getShopBySNameAndCName(storereq.getShopName(), customerName);
			mlst=modelmapDao.getModelMapByBModel(storereq.getModel());
			if(solst==null||solst.size()==0||mlst==null || mlst.size()==0)
			{
				//判断门店是否已维护
				if(solst==null || solst.size()==0)
				{			
					dshoplst=dmxShopDao.GetShopByName(storereq.getShopName());
					if(dshoplst==null || dshoplst.size()==0)
					{
						lackShop=new DMXShop();
						//lackShop.ResetNull();
						lackShop.setShopName(storereq.getShopName());
						lackShop.setShopCode(storereq.getShopCode());
						lackShop.setCtime(WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
						lackdmxShops.add(lackShop);				
					}
					log.info("the shop named "+storereq.getShopName()+" is unmaintened !");			
				}
				//判断产品是否已经维护			
				if(mlst==null || mlst.size()==0)
				{
					dplst=dmxProductDao.getDMXProductByModel(storereq.getModel());
					if(dplst==null || dplst.size()==0)
					{
						lackProduct=new DMXProduct();
						//lackProduct.ResetNull();
						lackProduct.setModel(storereq.getModel());
						lackProduct.setCtime(WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
						lackdmxProduct.add(lackProduct);
					}
					log.info("the branch model named "+storereq.getModel()+" is unmaintened !");				
				}
				continue;
			}
			remarkSb.delete(0,remarkSb.length());
			sale=new Sale();
			//sale.ResetNull();
			sale.setShopId(solst.get(0).getShopId());
			sale.setDatadate(WebPageUtil.dateToStr(storereq.getSoDate()));
			sale.setModel(storereq.getModel());
			List<Sale> lst=saleDaoWs.selectSaleList(sale);
			//判断数据库中是否存在纪录(以shopId,Datadate,Model作为唯一性判断依据)
			if(lst==null||lst.size()==0)
			{
				sale.setQuantity(storereq.getCount());
				sale.setUserId("admin");
				if(mlst.get(0).getPrice()==0)
				{
					log.info("the price of the branch model named "+storereq.getModel()+" is unmaintened !");
				}
				sale.setPrice((double)mlst.get(0).getPrice());
				sale.setCountry("19");
				sale.setAmount((double)mlst.get(0).getPrice()*storereq.getCount());
				sale.setCtime(WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
				if(storereq.getShopCode()!=null&&storereq.getShopCode().trim()!="")
				{
					remarkSb.append("ShopCode:"+storereq.getShopCode()+";");
				}
				if(storereq.getConsumerName()!=null&&storereq.getConsumerName().trim()!="")
				{
					remarkSb.append("CustomerName:"+storereq.getConsumerName()+";");
				}
				if(storereq.getPhoneNumber()!=null&&storereq.getPhoneNumber().trim()!="")
				{
					remarkSb.append("PhoneNumber:"+storereq.getPhoneNumber()+";");
				}
				if(storereq.getSn()!=null&&storereq.getSn().trim()!="")
				{
					remarkSb.append("SN:"+storereq.getSn()+";");
				}
				sale.setRemark(remarkSb.toString());
				//添加需要新增的销售数据记录
				saledataAdd.add(sale);
				continue;
			}
			sale.setSaleId(lst.get(0).getSaleId());
			count=lst.get(0).getQuantity();
			sale.setQuantity(storereq.getCount()+count);
			if(mlst.get(0).getPrice()==0)
			{
				log.info("the price of the branch model named "+storereq.getModel()+" is unmaintened !");
			}
			sale.setPrice((double)mlst.get(0).getPrice());
			sale.setCountry("19");
			if(lst.get(0).getAmount()==null)
			{
				sale.setAmount((double)mlst.get(0).getPrice()*storereq.getCount());
			}
			else
			{
				sale.setAmount(lst.get(0).getAmount()+mlst.get(0).getPrice()*storereq.getCount());
			}
			sale.setCtime(WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
			if(storereq.getShopCode()!=null&&storereq.getShopCode().trim()!="")
			{
				remarkSb.append("ShopCode:"+storereq.getShopCode()+";");
			}
			if(storereq.getConsumerName()!=null&&storereq.getConsumerName().trim()!="")
			{
				remarkSb.append("CustomerName:"+storereq.getConsumerName()+";");
			}
			if(storereq.getPhoneNumber()!=null&&storereq.getPhoneNumber().trim()!="")
			{
				remarkSb.append("PhoneNumber:"+storereq.getPhoneNumber()+";");
			}
			if(storereq.getSn()!=null&&storereq.getSn().trim()!="")
			{
				remarkSb.append("SN:"+storereq.getSn()+";");
			}
			sale.setRemark(remarkSb.toString());
			//添加需要更新的销售数据记录
			saledataUpdate.add(sale);
		} 
		log.info("traverse and classification InputData End time:"+WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		log.info("Add SaleData into DB Start time:"+WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		log.info("Add SaleData Size:"+saledataAdd.size());
		if(saledataAdd !=null && saledataAdd.size()>0)
		{
			saleDaoWs.InsertSaleDataBatch(saledataAdd);
		}
		log.info("Add SaleData into DB End time:"+WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		//更新需修改数据至数据库
		log.info("Update SaleData into DB Start time:"+WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		log.info("Add saledataUpdate Size:"+saledataUpdate.size());
		if(saledataUpdate !=null && saledataUpdate.size()>0)
		{
			saleDaoWs.UpdateSaleDataBatch(saledataUpdate);
		}
		log.info("Update SaleData into DB End time:"+WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		//Log纪录不存在的门店,建数据库表存储起来
		log.info("Add DMX Lack Shop Data into DB Start time:"+WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		log.info("Add lackdmxShops Size:"+lackdmxShops.size());
		if(lackdmxShops !=null&&lackdmxShops.size()>0)
		{
			dmxShopDao.InsertUnMaintainedShopBatch(lackdmxShops);
		}
		log.info("Add DMX Lack Shop Data into DB End time:"+WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		//log纪录不存在的model,并存入数据库
		log.info("Add DMX Lack Model Data into DB Start time:"+WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		if(lackdmxProduct!=null&&lackdmxProduct.size()>0)
		{
			dmxProductDao.InsertUnmaintenedModel(lackdmxProduct);
		}
		log.info("Add DMX Lack Model Data into DB End time:"+WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		log.info("invoke service End time:"+WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		return "success";
	}

}
