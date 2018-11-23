package cn.tcl.platform.modelmap.service.impl;

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

import cn.tcl.platform.modelmap.dao.IModelMapDao;
import cn.tcl.platform.modelmap.service.IModelMapService;
import cn.tcl.platform.modelmap.vo.ModelMap;

@Service("modelmapService")
public class IModelMapserviceImpl implements IModelMapService {
	
	@Autowired
	private IModelMapDao modelMapDao;

	@Override
	public Map<String, Object> selectModelMapData(int start, int limit,
			String keyword, String order, String sort,String conditions) throws Exception {
		List<ModelMap> list = modelMapDao.selectModelMap(start, limit, keyword, order, sort,conditions);
		Map<String, Object> map = new HashMap<String, Object>();
		int count = modelMapDao.countModelMap(start, limit, keyword,conditions);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}

	@Override
	public void editModelMap(ModelMap modelMap) throws Exception {
		modelMapDao.editModelMap(modelMap);
	}

	@Override
	public void addModelMap(ModelMap modelMap) throws Exception {
		modelMapDao.addModelMap(modelMap);
	}

	@Override
	public ModelMap getModelMapById(String modelId) throws Exception {
		return modelMapDao.getModelMapById(modelId);
	}

	@Override
	public void deleteModelMap(ModelMap modelMap) throws Exception {
		modelMapDao.deleteModelMap(modelMap);
		
	}

	@Override
	public void importModelMap(List<ModelMap> modelmaps) throws Exception {
		if(modelmaps != null){
			for (ModelMap modelMap : modelmaps) {
				modelMap.setPartyId(modelMap.getPartyId());
				modelMap.setBranchModel(modelMap.getBranchModel().toUpperCase());
				modelMap.setHqModel(modelMap.getHqModel().toUpperCase());
				modelMapDao.addModelMap(modelMap);
			}
		}
	}
	
	@Override
	public void importChannelModelMap(List<ModelMap> modelmaps) throws Exception {
		if(modelmaps != null){
			for (ModelMap modelMap : modelmaps) {
				modelMap.setCustomerId(modelMapDao.searchChannelByName(modelMap.getPartyId(), modelMap.getCustomerName()) + "");
				String party = modelMapDao.getPartyIdByName(modelMap);
				modelMap.setPartyId(party);
				modelMap.setBranchModel(modelMap.getBranchModel());
				modelMap.setChannelModel(modelMap.getChannelModel());
				modelMap.setChannelPrice(0L);//默认为0
				
				modelMapDao.insertChannelModel(modelMap);
			}
		}
	}

	@Override
	public String searchBeanVerify(ModelMap modelMap) throws Exception {
		String mc = "";
		int a = modelMapDao.searchBeanLimit(modelMap); 
		if(a > 0){
			mc = "modelError";
		}else{
			mc = "success";
		}
		return mc;
	}

	@Override
	public int getCountModelMapByBranch(ModelMap modelMap) throws Exception {
		return modelMapDao.bSearchBeanLimit(modelMap);
	}

	@Override
	public int searchHqModelMapCount(ModelMap modelMap) throws Exception {
		return modelMapDao.searchHqModelMapCount(modelMap);
	}

	@Override
	public int searchCountryByName(ModelMap modelMap) throws Exception {
		return modelMapDao.searchCountryByName(modelMap);
	}

	@Override
	public int getModelIdByParty(String conditions,String model,String partyId) throws Exception {
		return modelMapDao.getModelIdByParty(conditions,model,partyId);
	}

	@Override
	public void importModelPrice(List<ModelMap> modelmaps) throws Exception {
		if(modelmaps != null){
			for (ModelMap modelMap : modelmaps) {
				String party = modelMapDao.getPartyIdByName(modelMap);
				modelMap.setPartyId(party);
				modelMap.setBranchModel(modelMap.getBranchModel().toUpperCase());
				modelMap.setPrice(modelMap.getPrice());
				if(
						modelMap.getBranchModel()!=null && !modelMap.getBranchModel().equals("")
						&& modelMap.getPartyId()!=null && !modelMap.getPartyId().equals("")
						&& modelMap.getPrice()!=null  &&  !modelMap.getPrice().equals("")){
						modelMapDao.editModelPrice(modelMap);
				}
				
			}
		}
	}

	@Override
	public int bSearchBeanLimit(ModelMap modelMap) throws Exception {
		return modelMapDao.bSearchBeanLimit(modelMap);
	}


	@Override
	public XSSFWorkbook exporModelPrice(String conditions,
			String[] excelHeader, String title) throws Exception {
		//先查询出 导出的数据内容
		String key=null;
		List<ModelMap> modelList =modelMapDao.selectModelMap(conditions,key);
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
         for (int i = 0; i < modelList.size(); i++) {    
             row = sheet.createRow(i + 1);    
             ModelMap model = modelList.get(i);
             
             //-------------单元格-------------------
             
             /**
              * 国家
              */
             if(model.getPartyName()!=null  && !model.getPartyName().equals("")){
            	 XSSFCell cell0 = row.createCell(0);
                 cell0.setCellValue(model.getPartyName());    
                 cell0.setCellStyle(styleTwo);
             }
            
             
            
             if(model.getBranchModel()!=null && !model.getBranchModel().equals("")){
            	 XSSFCell cell1 = row.createCell(1);
                 cell1.setCellValue(model.getBranchModel());    
                 cell1.setCellStyle(styleTwo); 
             }
             
            if(model.getPrice()!=null){
            		XSSFCell cell2 = row.createCell(2);
                  cell2.setCellValue(model.getPrice());    
                  cell2.setCellStyle(styleTwo);
            }
           
            
         }    
         
         return workbook;
	}
	
	/**
	 * 渠道型号与分公司型号的关系
	 */
	@Override
	public Map<String, Object> selectChannelModelMap(Map<String,Object> m) throws Exception {
		Map<String, Object> map = new HashMap <String, Object>();
		List<ModelMap> list =  modelMapDao.selectChannelModelMap(m);
		int cou =  modelMapDao.countChannelModelMap(m);
		map.put("rows", list);
		map.put("total", cou);
		return map;
	}

//	@Override
//	public void deleteChannelModel(String id) throws Exception {
//		modelMapDao.deleteChannelModel(id);
//	}
	
	public void deleteChannelModel(ModelMap modelMap) throws Exception {
		modelMapDao.deleteChannelModel(modelMap);
	}

	@Override
	public List<ModelMap> selectChennalModelByPartyId(String partyId) throws Exception {
		return modelMapDao.selectChennalModelByPartyId(partyId);
	}

	@Override
	public void updateChannelModelById(ModelMap modelMap) throws Exception {
		modelMapDao.updateChannelModelById(modelMap);
	}

	@Override
	public void insertChannelModel(ModelMap modelMap) throws Exception {
		modelMapDao.insertChannelModel(modelMap);
	}
	
	@Override
	public String channelModelIsBeing(String customerId,String channelModel,String branchModel,String condition) throws Exception {
		int cModel = modelMapDao.channelModelIsBeing(customerId, channelModel,branchModel, condition);
		//int bModel = modelMapDao.brModelIsBeing(customerId, branchModel, condition);
		if(cModel > 0){
		/*	return "branModelError";
		}else if(bModel > 0){*/
			return "custModelError";
		}else{
			return "SUCCESS";
		}
	}

	@Override
	public int branchModelIsBeing(ModelMap modelMap) throws Exception {
		return modelMapDao.branchModelIsBeing(modelMap);
	}

	@Override
	public int searchChannelByName(String partyId, String customerName) throws Exception {
		return modelMapDao.searchChannelByName(partyId,customerName);
	}
	
	/**
	 * 是否存在分公司、渠道且分公司型号一致的数据
	 */
	@Override
	public int isPartyAndCustAndPaModelUnan(String partyId, String customerId, String branchModel) throws Exception {
		return modelMapDao.isPartyAndCustAndPaModelUnan(partyId, customerId, branchModel);
	}

	@Override
	public int selectSaleMappingBybranchModel(String branchModel) throws Exception {
		return modelMapDao.selectSaleMappingBybranchModel(branchModel);
	}

	@Override
	public String searchPartyIdByName(ModelMap modelMap) throws Exception {
		return modelMapDao.searchPartyIdByName(modelMap);
	}

	@Override
	public int searchChannelByNameCount(String partyId, String customerName) throws Exception {
		return modelMapDao.searchChannelByNameCount(partyId,customerName);
	}
	
}
