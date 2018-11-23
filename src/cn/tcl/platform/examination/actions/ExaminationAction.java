package cn.tcl.platform.examination.actions;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.excel.imports.ExcelImportUtil;
import cn.tcl.platform.barcode.service.IBarcodeService;
import cn.tcl.platform.examination.service.IExaminationService;
import cn.tcl.platform.examination.vo.Examination;
import cn.tcl.platform.product.vo.Product;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("all")
public class ExaminationAction extends BaseAction{
	
	@Autowired(required = false)
	@Qualifier("exService")
    private IExaminationService exService;
	
	/**
	 * 页面
	 * @return
	 */
	public String loadExaminationPage(){
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	public void selectExamQuestions(){
		String cType = request.getParameter("cType");
		String keyword = request.getParameter("keyword");
		String pageStr=request.getParameter("page");
		int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
		String rowStr=request.getParameter("rows");
		int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
		int start = (page-1)*limit;
		
		Map<String,Object> map = new HashMap<String,Object>();
		JSONObject result = new JSONObject();
		map.put("keyword", keyword);
		map.put("start", start);
		map.put("limit", limit);
		map.put("cType", cType);
		map.put("language", WebPageUtil.getLoginedUser().getUserLocaleDesc());
		map.put("countryId", WebPageUtil.getLoginedUser().getPartyId());
		map.put("userLoginId", WebPageUtil.getLoginedUser().getUserLoginId());
		map.put("isHq", "" + WebPageUtil.isHQRole());
		
		
		try {
			Map<String,Object> resultMap = exService.selectExamQuestions(map);
			
			result.accumulate("rows", JSONArray.fromObject(resultMap.get("rows")).toString());
			result.accumulate("total", resultMap.get("total"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	public void deleteExamQuestions(){
		String id = request.getParameter("id");
		
		JSONObject result = new JSONObject();
		try {
			result.accumulate("success", exService.deleteExamQuestions(WebPageUtil.isStringNullAvaliable(id) ? Integer.parseInt(id) : 0));
		} catch (Exception e) {
			e.printStackTrace();
			result.accumulate("success", false);
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	public void onDeleteExamQuestions(){
		String ids = request.getParameter("ids");
		JSONObject result = new JSONObject();
		try {
			result.accumulate("success", exService.onDeleteExamQuestions(ids));
		} catch (Exception e) {
			e.printStackTrace();
			result.accumulate("success", false);
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	public void selectCorrectAnswerById(){
		String id = request.getParameter("id");
		
		JSONObject result = new JSONObject();
		String rows = "";
		try {
			rows = JSONArray.fromObject(exService.selectCorrectAnswerById(id)).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(rows);
	};
	
	public void selectSubclassCategoriesById(){
		String id = request.getParameter("id");
		
		JSONObject result = new JSONObject();
		String rows = "";
		try {
			rows = JSONArray.fromObject(exService.selectSubclassCategoriesById(WebPageUtil.isStringNullAvaliable(id) ? Integer.parseInt(id) : -1)).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(rows);
	};
	
	public void selectExamQuestionsById(){
		String id = request.getParameter("id");
		
		JSONObject result = new JSONObject();
		String rows = "";
		try {
			rows = JSONArray.fromObject(exService.selectExamQuestionsById(WebPageUtil.isStringNullAvaliable(id) ? Integer.parseInt(id) : -1)).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(rows);
	};
	
	public void selectCountry(){
		String rows = "";
		try {
			rows = JSONArray.fromObject(exService.selectCountry(WebPageUtil.getLoginedUserId())).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(rows);
	};
	
	public void insertExamQuestions(){
		String cType = request.getParameter("cType");
		String fractions = request.getParameter("fractions");
		String categories = request.getParameter("categories");
		String mediums = request.getParameter("mediumss");
		String smaClass = request.getParameter("smaClasss");
		String exQuestions = request.getParameter("exQuestions");
		String alAnswersA = request.getParameter("alAnswersA");
		String alAnswersB = request.getParameter("alAnswersB");
		String alAnswersC = request.getParameter("alAnswersC");
		String alAnswersD = request.getParameter("alAnswersD");
		String alAnswersE = request.getParameter("alAnswersE");
		String alAnswersF = request.getParameter("alAnswersF");
		String alAnswersG = request.getParameter("alAnswersG");
		String corAnswer = request.getParameter("corAnswer");
		String analysis = request.getParameter("analysis");
		String countryId = request.getParameter("countryId");
		
		Examination ex = new Examination();
		ex.setcType(cType);
		ex.setFractions(Integer.parseInt(fractions));
		ex.setCategories(categories);
		ex.setExQuestions(exQuestions);
		ex.setMediums(mediums);
		ex.setSmaClass(smaClass);
		ex.setCorAnswer(corAnswer);
		ex.setAlAnswersA(alAnswersA);
		ex.setAlAnswersB(alAnswersB);
		ex.setAlAnswersC(alAnswersC);
		ex.setAlAnswersD(alAnswersD);
		ex.setAlAnswersE(alAnswersE);
		ex.setAlAnswersF(alAnswersF);
		ex.setAlAnswersG(alAnswersG);
		ex.setAnalysis(analysis);
		ex.setCountryId(countryId);
		ex.setUserId(WebPageUtil.getLoginedUserId());
		
		JSONObject result = new JSONObject();
		try {
			exService.insertExamQuestions(ex);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	public void importQuestion() {
		importQue();
	}
	
	public void importQue (){
		try {

			String errorMsg = exService.readExcel(uploadExcel,
					uploadExcelFileName);

			if ("".equals(errorMsg)) {
				WebPageUtil.writeBack("success");
			} else {
				WebPageUtil.writeBack(errorMsg);
			}
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			if (null == errorMsg || "".equals(errorMsg)) {
				errorMsg = this.getText("import.error.exist");
			}
			log.error(e.getStackTrace());
			e.printStackTrace();
			WebPageUtil.writeBack(errorMsg);
		}
	}
	
	public void updateExamQuestionsById(){
		String id = request.getParameter("id");
		String cType = request.getParameter("cType");
		String fractions = request.getParameter("fractions");
		String categories = request.getParameter("categories");
		String mediums = request.getParameter("mediumss");
		String smaClass = request.getParameter("smaClasss");
		String exQuestions = request.getParameter("exQuestions");
		String alAnswersA = request.getParameter("alAnswersA");
		String alAnswersB = request.getParameter("alAnswersB");
		String alAnswersC = request.getParameter("alAnswersC");
		String alAnswersD = request.getParameter("alAnswersD");
		String alAnswersE = request.getParameter("alAnswersE");
		String alAnswersF = request.getParameter("alAnswersF");
		String alAnswersG = request.getParameter("alAnswersG");
		String corAnswer = request.getParameter("corAnswer");
		String analysis = request.getParameter("analysis");
		String countryId = request.getParameter("countryId");
		
		Examination ex = new Examination();
		ex.setId(Integer.parseInt(id));
		ex.setcType(cType);
		ex.setFractions(Integer.parseInt(fractions));
		ex.setCategories(categories);
		ex.setExQuestions(exQuestions);
		ex.setMediums(mediums);
		ex.setSmaClass(smaClass);
		ex.setCorAnswer(corAnswer);
		ex.setAlAnswersA(alAnswersA);
		ex.setAlAnswersB(alAnswersB);
		ex.setAlAnswersC(alAnswersC);
		ex.setAlAnswersD(alAnswersD);
		ex.setAlAnswersE(alAnswersE);
		ex.setAlAnswersF(alAnswersF);
		ex.setAlAnswersG(alAnswersG);
		ex.setAnalysis(analysis);
		ex.setCountryId(countryId);
		
		JSONObject result = new JSONObject();
		try {
			exService.updateExamQuestionsById(ex);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
}
