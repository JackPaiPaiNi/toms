package cn.tcl.platform.examination.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.examination.dao.IPaperDao;
import cn.tcl.platform.examination.service.IPaperService;
import cn.tcl.platform.examination.vo.Analysis;
import cn.tcl.platform.examination.vo.Examination;
import cn.tcl.platform.examination.vo.Paper;
import cn.tcl.platform.message.dao.MessageDao;
import cn.tcl.platform.message.vo.Message;
import cn.tcl.platform.message.vo.SendMessageUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("paService")
public class PaperServiceImpl implements IPaperService{
	
	private final String mui = "2";//澶氶��
	private final String sin = "1";//鍗曢��
	private final String jud = "3";//鍒ゆ柇
	
	@Autowired
	private IPaperDao exDao;
	@Autowired
	private MessageDao messageDao;

	@Override
	public Map<String,Object> selectPaperData(Map<String, Object> map) throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("rows", exDao.selectPaperData(map));
		resultMap.put("total", exDao.selectPaperDataCount(map));
		return resultMap;
	}

	@Override
	public void insertPaperData(Paper p) throws Exception {
		exDao.insertPaperData(p);
		Integer pId = exDao.selectPaperId();
		List<Paper> paList = null;
		if(Contents.IS_AUTOMATIC.equals(p.getIsAutomatic())){
			paList = getPaperList(p,pId);
		}else{
			paList = paperGenerate(p,pId);
		}
		exDao.insertPaperTopicData(paList);
	}
	
	public List<Paper> getPaperList(Paper p,Integer pId){
		List<Paper> paList = new ArrayList<Paper>();
		String [] examIdArr = p.getExamIdStr().split(";");
		for (int i = 0; i < examIdArr.length; i++) {
			Paper pa = new Paper();
			pa.setId(pId);
			pa.setTopicId(Integer.parseInt(examIdArr[i]));
			paList.add(pa);
		}
		return paList;
	}
	
	@Override
	public boolean updatePaperData(Paper p,Message msg,String allUserStr,String userParam,String currentTime) throws Exception {
		
		messageDao.updateMsgRoleId(msg);
		messageDao.deleteMessageByUser(msg.getMsgRoleId());//鍙垹闄ゆ湭鍙備笌鑰冭瘯鐨勭敤鎴�
		String userLoginId="";
		
		JSONArray jsonArray = JSONArray.fromObject(allUserStr);  
		String [] userLogin = new String[jsonArray.size()];
		int userLoginIndex = 0;
		@SuppressWarnings("unchecked")
		Iterator <Object> it = jsonArray.iterator();
		while (it.hasNext()) {
			JSONObject ob = (JSONObject) it.next();
			userLogin[userLoginIndex] = (String) ob.get("userId");
			userLoginIndex ++;
		}
		
		List<SendMessageUser> list = new ArrayList<SendMessageUser>();
		for(int i=0;i<userLogin.length;i++){
			userLoginId = userLogin[i];
			String userLogin1 = messageDao.getUserLoginByMsgRoleId(userLoginId,msg.getMsgRoleId());
			
			
			SendMessageUser smu = new SendMessageUser();
			if(userLogin1==null){
				smu.setUserLoginId(userLoginId);
				smu.setMsgRoleId(msg.getMsgRoleId());
				smu.setMsgState("0");
				}else{
					messageDao.deleteMessageByUser2(userLogin1,msg.getMsgRoleId());//删除阅读课件的用户
					smu.setUserLoginId(userLoginId);
					smu.setMsgRoleId(msg.getMsgRoleId());
					smu.setMsgState("1");
				}
			list.add(smu);	
		}
	
		if(userLogin!=null && !userLogin.equals("")){
			messageDao.insertSendMessageUser(list);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("p_id", p.getId());
		map.put("currentTime", currentTime);
		
		int cou =  (int)exDao.isSpecifiedTime(map);
		boolean isSpeTime = cou > 0;
		if(!isSpeTime){
			exDao.deletePaperTopicData(""+p.getId());
			List<Paper> paList = getPaperList(p,p.getId());
			exDao.insertPaperTopicData(paList);
			exDao.updatePaperData(p);
			return true;
		}else{
			exDao.updatePaperEndDate(p);
			return false;
		}
	}	
	
	/**
	 * 璇曞嵎鐢熸垚
	 * @return
	 * @throws Exception 
	 */
	public List<Paper> paperGenerate(Paper p,Integer pId) throws Exception{
		List<Paper> pList = new ArrayList<Paper>();
		
		/*if(){
			
		}*/
		randomSample(sin,pList,p.getSinNum(),pId,p.getCountryId(),testTheSelectedWhere(p));
		randomSample(mui,pList,p.getMuiUum(),pId,p.getCountryId(),testTheSelectedWhere(p));
		randomSample(jud,pList,p.getJudNum(),pId,p.getCountryId(),testTheSelectedWhere(p));
		return pList;
	}
	
	/**
	 * 璇曞嵎绛涢�夊悗鏈熸潯浠跺鍔�
	 * @return
	 */
	public String testTheSelectedWhere(Paper p){
		StringBuffer sb = new StringBuffer();
		if(WebPageUtil.isStringNullAvaliable(p.getCategoriesId())){
			sb.append(" AND oqb.`categories` =" + p.getCategoriesId());
		};
		
		if(WebPageUtil.isStringNullAvaliable(p.getMediumsId())){
			sb.append(" AND oqb.`medium` =" + p.getMediumsId());
		};
		
		if(WebPageUtil.isStringNullAvaliable(p.getSmaClassId())){
			sb.append(" AND oqb.`sma_class` =" + p.getSmaClassId());
		}
		return sb.toString();
	};
	
	/**
	 * 闅忔満鎶藉彇棰樼洰
	 * @param type
	 * @param pList
	 * @param count
	 * @throws Exception
	 */
	public void randomSample(String type,List<Paper> pList,Integer count,Integer pId,String countryId,String conditions) throws Exception{
		if(count > 0){
			Integer topicCount = exDao.selectTopicTypeCount(type,countryId,conditions);//棰樼洰绫诲瀷瀛樺湪鏁伴噺
			if(topicCount == 0){
				return;
			}
			int randomNum = 1;//闅忔満鎶藉彇鏁伴噺(涓嶉噸澶�)
			Random random=new Random();// 瀹氫箟闅忔満绫�
			int result = random.nextInt(topicCount);// 杩斿洖[0,10)闆嗗悎涓殑鏁存暟锛屾敞鎰忎笉鍖呮嫭10
			Paper pa = exDao.selectTopicLIMIT(result,type,countryId,conditions);
			pa.setId(pId);
			pList.add(pa);
			
			while(true){
				if(randomNum == count){
					break;
				}
				result = random.nextInt(topicCount);// 杩斿洖[0,10)闆嗗悎涓殑鏁存暟锛屾敞鎰忎笉鍖呮嫭10
				pa = exDao.selectTopicLIMIT(result,type,countryId,conditions);
				boolean isNo = true;
				for (int i = 0; i < pList.size(); i++) {
					if((int)pList.get(i).getTopicId() == (int)pa.getTopicId()){
						isNo = false;
						break;
					}
				}
				if(isNo){
					pa.setId(pId);
					pList.add(pa);
					randomNum ++;
				}
			}
		}
	};
	
	@Override
	public boolean deletePaperData(Map<String, Object> map) throws Exception {
		int cou =  (int)exDao.isSpecifiedTime(map);
		boolean isSpeTime = cou > 0;
		if(!isSpeTime){
			exDao.deletePaperData((Integer)map.get("p_id"));
			exDao.deletePaperTopicData(""+(Integer)map.get("p_id"));
			exDao.deleteQRcode((Integer)map.get("p_id"));
			return true;
		}else{
			return false;
		}
//		exDao.deletePaperMsg(id);
	}

	@Override
	public Integer selectTopicTypeCount(String type, String countryId,Paper p) throws Exception {
		return exDao.selectTopicTypeCount(type, countryId,testTheSelectedWhere(p));
	}

	@Override
	public List<Examination> selectTopicByPaperId(String id) throws Exception {
		return exDao.selectTopicByPaperId(id);
	}

	@Override
	public void insertQRcode(Paper p) throws Exception {
			exDao.insertQRcode(p);
		
	}

	@Override
	public boolean updateEndTime(Paper p) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("p_id", p.getId());
		map.put("currentTime", p.geteTime());
		
		int cou =  (int)exDao.isSpecifiedEndTime(map);
		boolean isSpeTime = cou > 0;
		if(isSpeTime){
			exDao.updateEndTime(p);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Map<String,Object> selectExaminationSituation(String conditions,int start,int limit) throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		/*resultMap.put("rows", exDao.selectExaminationSituation(conditions, start, limit));
		resultMap.put("total", exDao.selectExaminationSituationCount(conditions));*/
		
		resultMap.put("rows", exDao.selectRusExaminationSituation(conditions, start, limit));
		resultMap.put("total", exDao.selectRusExaminationExportCount(conditions));
		return resultMap;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public HSSFWorkbook exporSale(String conditions,String[] excelHeader,String title,int start,int limit) throws Exception {
		//鍏堟煡璇㈠嚭 瀵煎嚭鐨勬暟鎹唴瀹�
		List<Analysis> exporSaleList = null;
		if(start > -1){
			exporSaleList = exDao.selectExaminationExport(conditions, start, limit);
		}
		
		//璁剧疆 琛ㄥご瀹藉害
		int[] excelWidth = {120,120,120,120,130,120,200,130,130,120};
		
		 HSSFWorkbook workbook = new HSSFWorkbook();
		 HSSFSheet sheet = workbook.createSheet(title);
		 HSSFRow headerRow = sheet.createRow(0);
		 
		 //瀵煎嚭瀛椾綋鏍峰紡
		 HSSFFont font = workbook.createFont();
		 
		 //瀵煎嚭鏍峰紡
         HSSFCellStyle style = workbook.createCellStyle();    
         style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         
         style.setFont(font);
		 
         
		 sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, excelHeader.length - 1)); 
		 
		 HSSFCell headerCell = headerRow.createCell(0);
		 headerCell.setCellValue(new HSSFRichTextString(title));
		 headerCell.setCellStyle(style);
		 
         //--------------------------------------------------------------
		 for (int i = 0; i < excelWidth.length; i++) {  
			    sheet.setColumnWidth(i, 32 * excelWidth[i]);  
		 } 
		 
         HSSFRow row = sheet.createRow(1);
         //琛ㄥご鏁版嵁
         for (int i = 0; i < excelHeader.length; i++) {   
             HSSFCell cell = row.createCell(i);    
             cell.setCellValue(excelHeader[i]);    
             cell.setCellStyle(style);  
             
         }   
         
         if(exporSaleList != null && exporSaleList.size() != 0){
	         //琛ㄤ綋鏁版嵁
	         for (int i = 0; i < exporSaleList.size(); i++) {    
	             row = sheet.createRow(i + 2);    
	             Analysis an = exporSaleList.get(i);
	             
	             //-------------鍗曞厓鏍�-------------------
	             
	             /**
	              * 鍥藉
	              */
	             HSSFCell cell0 = row.createCell(0);
	             cell0.setCellValue(an.getCountryName());    
	             cell0.setCellStyle(style);
	             /**
	              * 鐢ㄦ埗Id
	              */
	             HSSFCell cell1 = row.createCell(1);
	             cell1.setCellValue(an.getUserId());    
	             cell1.setCellStyle(style);
	             /**
	              * 鐢ㄦ埛鍚�
	              */
	             HSSFCell cell2 = row.createCell(2);
	             cell2.setCellValue(an.getUserName());    
	             cell2.setCellStyle(style);
	             /**
	              * 寰俊
	              */
	             HSSFCell cell3 = row.createCell(3);
	             cell3.setCellValue(an.getWechat());    
	             cell3.setCellStyle(style);
	             /**
	              * 鐢ㄦ埛瑙掕壊
	              */
	             HSSFCell cell4 = row.createCell(4);
	             cell4.setCellValue(an.getRoleName());    
	             cell4.setCellStyle(style);
	             /**
	              * 缂栧彿
	              */
	             HSSFCell cell5 = row.createCell(5);
	             cell5.setCellValue(an.getWorkNum());    
	             cell5.setCellStyle(style);
	             /**
	              * 浜ゅ嵎鏃堕棿
	              */
	             HSSFCell cell6 = row.createCell(6);
	             cell6.setCellValue(an.getSubTimes());    
	             cell6.setCellStyle(style);
	             /**
	              * 鏍囬
	              */
	             HSSFCell cell7 = row.createCell(7);
	             cell7.setCellValue(an.getTitle());    
	             cell7.setCellStyle(style);
	             /**
	              * 鍒嗘暟
	              */
	             HSSFCell cell8 = row.createCell(8);
	             cell8.setCellValue(an.getScore());    
	             cell8.setCellStyle(style);
	             /**
	              * 璁″垝鑰冭瘯浜烘暟
	              */
	             HSSFCell cell9 = row.createCell(9);
	             cell9.setCellValue(an.getNops());    
	             cell9.setCellStyle(style);
	             /**
	              * 瀹為檯鑰冭瘯浜烘暟
	              */
	             HSSFCell cell10 = row.createCell(10);
	             cell10.setCellValue(an.getAnos());    
	             cell10.setCellStyle(style);
	             /**
	              * 鑰冭瘯鐜�
	              */
	             HSSFCell cell11 = row.createCell(11);
	             if((int)an.getAnos() != 0 && (int)an.getNops() != 0){
	            	 int anosInt = an.getAnos();
	            	 int nopsInt = an.getNops();
	            	 int vector = (int) Math.round((double)anosInt / (double)nopsInt * 100);
	            	 cell11.setCellValue(vector +"%");
	             }else if(an.getAnos() == 0){
	            	 cell11.setCellValue("0%");
	             }else{
	            	 cell11.setCellValue("100%");
	             }
	             cell11.setCellStyle(style);
	          
	         }    
         }
         return workbook;
	}


	@Override
	public List<Examination> selectExamByTypeAndCountry(String countryId, String conditions) throws Exception {
		return exDao.selectExamByTypeAndCountry(countryId, conditions);
	}

	@Override
	public Map<String, Object> selectExamSelectedInfo(String paperId, String countryId, String conditions)
			throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		List<Examination> ex = selectTopicByPaperId(paperId);
		m.put("rowsSele", ex);
		StringBuffer sb = new StringBuffer();
		for (Examination e : ex) {
			sb.append("'" + e.getId() + "',");
		}
		String examIds = sb.toString();
		conditions += WebPageUtil.isStringNullAvaliable(sb.toString())?"AND oqb.`id` NOT IN ( "+ examIds.substring(0, examIds.length()-1) +" )":"";
		List<Examination> e = selectExamByTypeAndCountry(countryId,conditions);
		m.put("rowsFrus", e);
		return m;
	}
	
	/**
	 * 用户是否已经考完试
	 * @param userId
	 * @param paperId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String,Object>> selectUserIsExam(String userId, String paperId) throws Exception {
		
		List<Map<String,Object>> l = new ArrayList<Map<String,Object>>();
		JSONArray jsonArray = JSONArray.fromObject(userId);  
		@SuppressWarnings("unchecked")
		Iterator <Object> it = jsonArray.iterator();
		while (it.hasNext()) {
			JSONObject ob = (JSONObject) it.next();
			
			int n = exDao.selectUserIsExam((String) ob.get("userId"),paperId);
			if(n > 0){
				Map<String,Object> m = new HashMap<String,Object>();
				m.put("userName", (String) ob.get("userName"));
				l.add(m);
			}
		}
		return l;
	}

	@Override
	public Integer isSpecifiedEndTime(Map<String, Object> map) throws Exception {
		return exDao.isSpecifiedEndTime(map);
	}

	@Override
	public Map<String, Object> selectRusExaminationSituation(String conditions,
			int start, int limit) throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("rows", exDao.selectRusExaminationSituation(conditions, start, limit));
		resultMap.put("total", exDao.selectRusExaminationExportCount(conditions));
		return resultMap;
	}

	@Override
	public HSSFWorkbook exporRusSale(String conditions, String[] excelHeader,
			String title, int start, int limit) throws Exception {

				List<Analysis> exporSaleList = null;
				if(start > -1){
					exporSaleList = exDao.selectRusExaminationExport(conditions, start, limit);
				}
				
				
				int[] excelWidth = {120,120,120,120,130,120,200,130,130,120};
				
				 HSSFWorkbook workbook = new HSSFWorkbook();
				 HSSFSheet sheet = workbook.createSheet(title);
				 HSSFRow headerRow = sheet.createRow(0);
				 
				 
				 HSSFFont font = workbook.createFont();
				 
		         HSSFCellStyle style = workbook.createCellStyle();    
		         style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		         style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		         
		         style.setFont(font);
				 
		         
				 sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, excelHeader.length - 1)); 
				 
				 HSSFCell headerCell = headerRow.createCell(0);
				 headerCell.setCellValue(new HSSFRichTextString(title));
				 headerCell.setCellStyle(style);
				 
		         //--------------------------------------------------------------
				 for (int i = 0; i < excelWidth.length; i++) {  
					    sheet.setColumnWidth(i, 32 * excelWidth[i]);  
				 } 
				 
		         HSSFRow row = sheet.createRow(1);
		         //琛ㄥご鏁版嵁
		         for (int i = 0; i < excelHeader.length; i++) {   
		             HSSFCell cell = row.createCell(i);    
		             cell.setCellValue(excelHeader[i]);    
		             cell.setCellStyle(style);  
		             
		         }   
		         
		         if(exporSaleList != null && exporSaleList.size() != 0){
			         //琛ㄤ綋鏁版嵁
			         for (int i = 0; i < exporSaleList.size(); i++) {    
			             row = sheet.createRow(i + 2);    
			             Analysis an = exporSaleList.get(i);
			             
			             //-------------鍗曞厓鏍�-------------------
			             
			             /**
			              * 鍥藉
			              */
			             HSSFCell cell0 = row.createCell(0);
			             cell0.setCellValue(an.getCountryName());    
			             cell0.setCellStyle(style);
			             /**
			              * 鐢ㄦ埗Id
			              */
			             HSSFCell cell1 = row.createCell(1);
			             cell1.setCellValue(an.getUserId());    
			             cell1.setCellStyle(style);
			             /**
			              * 鐢ㄦ埛鍚�
			              */
			             HSSFCell cell2 = row.createCell(2);
			             cell2.setCellValue(an.getUserName());    
			             cell2.setCellStyle(style);
			             /**
			              * 寰俊
			              */
			             HSSFCell cell3 = row.createCell(3);
			             cell3.setCellValue(an.getWechat());    
			             cell3.setCellStyle(style);
			             
			             HSSFCell cell4 = row.createCell(4);
			             cell4.setCellValue(an.getCustomerName());
			             cell4.setCellStyle(style);
			             /**
			              * 鐢ㄦ埛瑙掕壊
			              */
			             HSSFCell cell5 = row.createCell(5);
			             cell5.setCellValue(an.getRoleName());    
			             cell5.setCellStyle(style);
			             /**
			              * 缂栧彿
			              */
			             HSSFCell cell6 = row.createCell(6);
			             cell6.setCellValue(an.getWorkNum());    
			             cell6.setCellStyle(style);
			             /**
			              * 浜ゅ嵎鏃堕棿
			              */
			             HSSFCell cell7 = row.createCell(7);
			             cell7.setCellValue(an.getSubTimes());    
			             cell7.setCellStyle(style);
			             /**
			              * 鏍囬
			              */
			             HSSFCell cell8 = row.createCell(8);
			             cell8.setCellValue(an.getTitle());    
			             cell8.setCellStyle(style);
			             /**
			              * 鍒嗘暟
			              */
			             HSSFCell cell9 = row.createCell(9);
			             cell9.setCellValue(an.getScore());    
			             cell9.setCellStyle(style);
			             /**
			              * 璁″垝鑰冭瘯浜烘暟
			              */
			             HSSFCell cell10 = row.createCell(10);
			             cell10.setCellValue(an.getNops());    
			             cell10.setCellStyle(style);
			             /**
			              * 瀹為檯鑰冭瘯浜烘暟
			              */
			             HSSFCell cell11 = row.createCell(11);
			             cell11.setCellValue(an.getAnos());    
			             cell11.setCellStyle(style);
			             /**
			              * 鑰冭瘯鐜�
			              */
			             HSSFCell cell12 = row.createCell(12);
			             if((int)an.getAnos() != 0 && (int)an.getNops() != 0){
			            	 int anosInt = an.getAnos();
			            	 int nopsInt = an.getNops();
			            	 int vector = (int) Math.round((double)anosInt / (double)nopsInt * 100);
			            	 cell12.setCellValue(vector +"%");
			             }else if(an.getAnos() == 0){
			            	 cell12.setCellValue("0%");
			             }else{
			            	 cell12.setCellValue("100%");
			             }
			             cell12.setCellStyle(style);
			          
			         }    
		         }
		         return workbook;
	}
	
	/**
	 * 考试是否已经开始
	 */
	@Override
	public Integer isSpecifiedTime(Map<String, Object> map) throws Exception {
		return exDao.isSpecifiedTime(map);
	};
}
