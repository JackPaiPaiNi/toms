package cn.tcl.platform.examination.imports;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionContext;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.examination.service.IExaminationService;
import cn.tcl.platform.examination.vo.Examination;

public class ValidationImportExamination extends BaseAction{
	private static final long serialVersionUID = 1L;
	private static final String Judgement = "3";//3为判断题
//	private static final String MULTIPLE = "2";//2为多选题
	private static final String EXCLUSIVE = "1";//1为单选题
	
	public String validationImport(List<Examination> exs){
		StringBuffer msg = new StringBuffer();
		try {
			ActionContext context = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) context.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
			ServletContext sc = request.getSession().getServletContext();
			ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
			IExaminationService exService = (IExaminationService) ac.getBean("exService");
			
			for (int i = 0; i < exs.size(); i++) {
				Examination ex = exs.get(i);
				
				String userId = (String) request.getSession().getAttribute("loginUserId");
				ex.setUserId(userId);
				
				/*总部用户需要校验国家是否存在*/
				if(WebPageUtil.isHQRole()){
					if(!WebPageUtil.isStringNullAvaliable(ex.getCountryName()) || !WebPageUtil.isStringNullAvaliable(exService.selectPartyIdByPartyName(ex.getCountryName().trim()))){
						msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt10"));
					}else{
						ex.setCountryId(exService.selectPartyIdByPartyName(ex.getCountryName().trim()));
					}
				}else{
					ex.setCountryId(WebPageUtil.getLoginedUser().getPartyId());
				}
				
				/*题目类型是否存在*/
				if(!WebPageUtil.isStringNullAvaliable(ex.getcType()) || !WebPageUtil.isStringNullAvaliable(exService.selectStypeIsExist(ex.getcType().trim()))){
					msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt01"));
				}
				
				/*当类型为判断题时，备选答案只识别前两个*/
				String clearAlteAnsws = exService.selectStypeIsExist(ex.getcType().trim());
				clearAlteAnswSe(clearAlteAnsws,ex);
				
				/*题目分数必须是正整数且不能为0和空*/
				if(!IsIntNumber("" + ex.getFractions())){
					msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt02"));
				}
				
				/*大类不能为空且存在*/
				if(!WebPageUtil.isStringNullAvaliable(ex.getCategories()) || (exService.selectTypeCount(ex.getCategories().trim()) <= 0)){
					msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt03"));
				}
				
				/*题目不能为空*/
				if(!WebPageUtil.isStringNullAvaliable(ex.getExQuestions())){
					msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt04"));
				}
				
				/*正确答案校验*/
				Object [] o = corAnsVrify(clearAlteAnsws,ex);
				if(!(boolean) o[0]){
					msg.append(getText("product.error.firstStr")+(i+2)+getText((String)o[1]));
				}
			}
			if(msg.length() > 0){
				return msg.toString();
			}else{
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg.append(e.getMessage());
			return msg.toString();
		}
	}
	
	public static Object[] corAnsVrify(String clearAlteAnsws,Examination ex){//正确答案校验
		Object[] obj = new Object[2];
		if(!WebPageUtil.isStringNullAvaliable(ex.getCorAnswer())){//正确答案不能为空
			obj[0] = false;
			obj[1] = "product.error.prompt06";
			return obj;
		}
		
		String [] alteAnsw = {"A","B","C","D","E","F","G"};//跟随模板变化而设置，备选答案目前7个。
		String [] operAlteAnsw = ex.getCorAnswer().trim().split("/");//备选答案不能为空
		String [] alteAnswSe = {ex.getAlAnswersA(),ex.getAlAnswersB(),ex.getAlAnswersC(),ex.getAlAnswersD(),ex.getAlAnswersE(),ex.getAlAnswersF(),ex.getAlAnswersG()};
		
		for (int j = 0; j < operAlteAnsw.length; j++) {
			boolean isEq = true; 
			for (int i = 0; i < alteAnsw.length; i++) {//备选答案必须是A-G
				if(alteAnsw[i].toLowerCase().equals(operAlteAnsw[j].toLowerCase())){
					isEq = false;
					break;
				}
			}
			if(isEq){
				obj[0] = false;
				obj[1] = "product.error.prompt07";
				return obj;
			}
		}
		
		/*单选题、判断题答案不能为多个*/
		if(Judgement.equals(clearAlteAnsws) || EXCLUSIVE.equals(clearAlteAnsws)){
			if(operAlteAnsw.length >= 2){
				obj[0] = false;
				obj[1] = "product.error.prompt08";
				return obj;
			}
		}
		
		for (int j = 0; j < operAlteAnsw.length; j++) {
			for (int i = 0; i < alteAnsw.length; i++) {//正确答案选项不能为空
				if(alteAnsw[i].toLowerCase().equals(operAlteAnsw[j].toLowerCase())){
					if(!WebPageUtil.isStringNullAvaliable(alteAnswSe[i])){
						obj[0] = false;
						obj[1] = "product.error.prompt08";
						return obj;
					}else{
						break;
					}
				}
			}
		}
		obj[0] = true;
		obj[1] = "";
		return obj;
	};
	
	public static boolean IsIntNumber(String str) {
		String regex = "^\\+?[1-9][0-9]*$";
		return match(regex, str);
	}
	
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	//模板导入时，题目为判断题，只识别前面两个备选答案。
	public void clearAlteAnswSe(String c,Examination ex){
		if(Judgement.equals(c)){//判断题型
			ex.setAlAnswersC("");
			ex.setAlAnswersD("");
			ex.setAlAnswersE("");
			ex.setAlAnswersF("");
			ex.setAlAnswersG("");
		}
	}
}
