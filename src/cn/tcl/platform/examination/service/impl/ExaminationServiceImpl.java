package cn.tcl.platform.examination.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.examination.dao.IExaminationDao;
import cn.tcl.platform.examination.service.IExaminationService;
import cn.tcl.platform.examination.vo.Examination;
import cn.tcl.platform.excel.actions.DateUtil;

@Service("exService")
public class ExaminationServiceImpl extends BaseAction implements IExaminationService{
	
	private static final String Judgement = "3";//3为判断题
	private static final String EXCLUSIVE = "1";//1为单选题
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private IExaminationDao exDao;

	@Override
	public Map<String, Object> selectExamQuestions(Map<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("rows", exDao.selectExamQuestions(map));
		resultMap.put("total", exDao.selectExamQuestionsCount(map));
		return resultMap;
	}

	@Override
	public boolean deleteExamQuestions(Integer id) throws Exception {
		boolean isUse = (exDao.selectUseTheTitleById("" + id) > 0) ? false : true;
		if(isUse){
			exDao.deleteExamQuestions(id);
			exDao.deleteTopicAnswer(id);
		}
		return isUse;
	}

	@Override
	public List<Examination> selectCorrectAnswerById(String id) throws Exception {
		return exDao.selectCorrectAnswerById(id);
	}

	@Override
	public List<Examination> selectSubclassCategoriesById(Integer id) throws Exception {
		return exDao.selectSubclassCategoriesById(id);
	}
	
	@Override
	public void importExamination(List<Examination> exs) throws Exception {
		if(exs!=null){
			for (Examination ex : exs) {
				ex.setcType(ex.getcType());//类型储存编号
				insertExamQuestions(ex);
			}
		}
	}
	
	@Override
	public void insertExamQuestions(Examination ex) throws Exception {
		
		int n = exDao.selectTitleByEQAndType(ex);
		if(n == 0){
			String [] cArr = ex.getCorAnswer().toUpperCase().trim().split("/"); 
			exDao.insertExamQuestions(ex);
			Integer exQuestionsId = exDao.selectExamQuestionsId();
			List<Examination> corAnswerList = new ArrayList<Examination>(); 
			for (int i = 0; i < cArr.length; i++) {
				Examination e = new Examination();
				e.setExQuestionsId(""+exQuestionsId);
				e.setCorAnswer(cArr[i]);
				corAnswerList.add(e);
			}
			exDao.insertCorrectAnswer(corAnswerList);
		}
	}

	@Override
	public Examination selectExamQuestionsById(Integer id) throws Exception {
		return exDao.selectExamQuestionsById(id);
	}

	@Override
	public void updateExamQuestionsById(Examination ex) throws Exception {
		int n = exDao.selectTitleByEQAndType(ex);
		if(n == 0){
			exDao.updateExamQuestionsById(ex);
			exDao.deleteTopicAnswer(ex.getId());
			String [] cArr = ex.getCorAnswer().split("/"); 
			Integer exQuestionsId = ex.getId();
			List<Examination> corAnswerList = new ArrayList<Examination>(); 
			for (int i = 0; i < cArr.length; i++) {
				Examination e = new Examination();
				e.setExQuestionsId(""+exQuestionsId);
				e.setCorAnswer(cArr[i]);
				corAnswerList.add(e);
			}
			exDao.insertCorrectAnswer(corAnswerList);
		}
	}

	@Override
	public List<Examination> selectCountry(String userId) throws Exception {
		return exDao.selectCountry(userId);
	}

	@Override
	public String selectStypeIsExist(String eType) throws Exception {
		return exDao.selectStypeIsExist(eType);
	}

	@Override
	public Integer selectTypeCount(String eName) throws Exception {
		return exDao.selectTypeCount(eName,eName);
	}
	
	@Override
	public String onDeleteExamQuestions(String ids) throws Exception {
		if(!WebPageUtil.isStringNullAvaliable(ids)){
			return "false";
		}
		
		String [] idStr = ids.split(",");
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < idStr.length; j++) {
			boolean isUse = (exDao.selectUseTheTitleById(idStr[j]) > 0) ? false : true;
			if(isUse){
				if(WebPageUtil.isStringNullAvaliable(idStr[j])){
					exDao.deleteExamQuestions(Integer.parseInt(idStr[j]));
					exDao.deleteTopicAnswer(Integer.parseInt(idStr[j]));
				}
			}else{
				sb.append((j + 1) + ",");
			}
		}
		if(WebPageUtil.isStringNullAvaliable(sb.toString())){
			return sb.toString();
		}else{
			return "true";
		}
	}

	@Override
	public String selectPartyIdByPartyName(String partyName) throws Exception {
		return exDao.selectPartyIdByPartyName(partyName);
	}
	
	@Override
	public String readExcel(File file, String fileName) throws IOException {
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		} else if ("xlsx".equals(extension)) {
			return read2007Excel(file);
		} else {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		}
	}
	
	/**
	 * 替换下划线为空格
	 * @param s
	 * @return
	 */
	public String replaceUnderline(String s){
		return s != null ? s.replaceAll("_", " ") : "";
	}
	
	/**
	 * 替换下划线为横杠
	 * @param s
	 * @return
	 */
	public String replaceABar(String s){
		return s != null ? s.replaceAll("_", "-") : "";
	}
	
	@Override
	public String read2007Excel(File file) throws IOException {
		StringBuffer msg = new StringBuffer();
		List<Examination> list = new ArrayList<Examination>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			
			Set<Examination> setModelSTO= new HashSet<Examination>();
			
			for (int j = 1; j <= sheet.getLastRowNum(); j++) {
				XSSFRow row = sheet.getRow(j);//获取每一行
				Examination ex = new Examination();
				
				String userId = WebPageUtil.getLoginedUserId();
				ex.setUserId(userId);
				
				
				/*题目类型是否存在*/
				String type = "";
				if(row.getCell(0)!=null  && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					type= row.getCell(0).getStringCellValue();
					String typeId = exDao.selectStypeIsExist(type.trim());
					if(!WebPageUtil.isStringNullAvaliable(typeId)){
						msg.append(getText("excel.error.row") + (j+1) +" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(1)+" "
								+ getText("product.error.prompt01") + "<br/>");
					}else{
						ex.setcType(typeId);//类型储存编号
					}
				}else{
					msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(1)+" "
							+ getText("excel.error.null") + "<br/>");
				}
				
				/*题目分数必须是正整数且不能为0和空*/
				if(row.getCell(1)!=null  && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					 switch (row.getCell(1).getCellType()) {
					 
					  case HSSFCell.CELL_TYPE_STRING://提示單元格為文本，應設置數字
						 msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(2)+" "
									+ getText("excel.error.num") + "<br/>");
				       break;
				 
				      case HSSFCell.CELL_TYPE_FORMULA://公式格式
				    	  msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(2)+" "
									+ getText("excel.error.num") + "<br/>");
				
				       break;
				 
				      case HSSFCell.CELL_TYPE_NUMERIC:
				    	  if(row.getCell(1).getNumericCellValue()!=0){
								double score=row.getCell(1).getNumericCellValue();
								if(IsIntNumber(score)){
									ex.setFractions((int)score);
								}else{
									msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(2)+" "
											+ getText("product.error.prompt02") + "<br/>");
								}
							}
				        break;
				 
				      case HSSFCell.CELL_TYPE_ERROR:
				 
				       break;
				      }
					
				}else{
					msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(2)+" "
							+ getText("excel.error.null") + "<br/>");
				}
				
				/*大类不能为空且存在*/
				String categoriesId = "-1";//-1： 默认大类不存在
				if(row.getCell(2)!=null  && row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					String categories = row.getCell(2).getStringCellValue().trim();
					String typeId = exDao.selectTypeIdByEnameByparentId(replaceUnderline(categories),replaceABar(categories),"0");
					if(!WebPageUtil.isStringNullAvaliable(typeId)){
						msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(3)+" "
								+ getText("product.error.prompt03") + "<br/>");
					}else{
						categoriesId = typeId;
						ex.setCategories(categories);
					}
				}else{
					msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(3)+" "
							+ getText("excel.error.null") + "<br/>");
				}
				
				/*中类可为空，但填写值需存在*/
				String mediumsId = "-1";
				if(row.getCell(3)!=null  && row.getCell(3).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					String mediums = row.getCell(3).getStringCellValue().trim();
					String typeId = exDao.selectTypeIdByEnameByparentId(replaceUnderline(mediums),replaceABar(mediums),categoriesId);
					if(!WebPageUtil.isStringNullAvaliable(typeId)){
						msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(4)+" "
								+ getText("product.error.prompt18") + "<br/>");
					}else{
						mediumsId = typeId;
						ex.setMediums(mediums);
					}
				}
				
				/*小类可为空，但填写值需存在*/
				if(row.getCell(4)!=null  && row.getCell(4).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					String smaClass = row.getCell(4).getStringCellValue().trim();
					String typeId = exDao.selectTypeIdByEnameByparentId(replaceUnderline(smaClass),replaceABar(smaClass),mediumsId);
					if(!WebPageUtil.isStringNullAvaliable(typeId)){
						msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(5)+" "
								+ getText("product.error.prompt19") + "<br/>");
					}else{
						ex.setSmaClass(smaClass);
					}
				}
				
				/*题目不能为空*/
				if(row.getCell(5)!=null  && row.getCell(5).getCellType()!=HSSFCell.CELL_TYPE_BLANK ){
					String exQuestions;
					if(row.getCell(5).getCellType()==HSSFCell.CELL_TYPE_STRING){
						if(strIsNullCharacter(row.getCell(5).getStringCellValue())){//题目字符串格式
							exQuestions = row.getCell(5).getStringCellValue();
							ex.setExQuestions(exQuestions);
						}else{
							msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(6)+" "
									+ getText("product.error.prompt04") + "<br/>");
						}
					}else {
						ex.setExQuestions(getXSSFCellValue(row.getCell(5)));
					}
				}else{
					msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(6)+" "
							+ getText("product.error.prompt04") + "<br/>");
				}
				
				/**
				 * 备选答案不能隔位空
				 */
				boolean isAnswerNotNull = true;
				boolean isAnswerthrough = true;
				for (int i = 12; i > 5; i--) {
					if(isAnswerNotNull){
						if(row.getCell(i)!=null  && row.getCell(i).getCellType()!=HSSFCell.CELL_TYPE_BLANK  ){
							if(row.getCell(i).getCellType()==HSSFCell.CELL_TYPE_STRING  && strIsNullCharacter(row.getCell(i).getStringCellValue())){
								isAnswerNotNull = false;
								i++;
							}else if(row.getCell(i).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
								isAnswerNotNull = false;
								i++;
							}
						}
					}else{
						if(row.getCell(i)==null  || row.getCell(i).getCellType()==HSSFCell.CELL_TYPE_BLANK || (row.getCell(i).getCellType()==HSSFCell.CELL_TYPE_STRING  && !strIsNullCharacter(row.getCell(i).getStringCellValue()))){
							msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(i+1)+" "
									+ getText("product.error.prompt13") + "<br/>");
								isAnswerthrough = false;
						}
					}
				}
				
				/**
				 * 备选答案不能全部为空
				 */
				if(isAnswerNotNull){
					msg.append(getText("excel.error.row") + (j+1)+" "+ getText("product.error.prompt13") + "<br/>");
				}
				
				if(isAnswerthrough && !isAnswerNotNull){
					//备选答案
					if(row.getCell(6)!=null  && row.getCell(6).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						ex.setAlAnswersA(getXSSFCellValue(row.getCell(6)));
					}
					
					if(row.getCell(7)!=null  && row.getCell(7).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						ex.setAlAnswersB(getXSSFCellValue(row.getCell(7)));
					}
					
					if(row.getCell(8)!=null  && row.getCell(8).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						ex.setAlAnswersC(getXSSFCellValue(row.getCell(8)));
					}
					
					if(row.getCell(9)!=null  && row.getCell(9).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						ex.setAlAnswersD(getXSSFCellValue(row.getCell(9)));
					}
					
					if(row.getCell(10)!=null  && row.getCell(10).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						ex.setAlAnswersE(getXSSFCellValue(row.getCell(10)));
					}
					
					if(row.getCell(11)!=null  && row.getCell(11).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						ex.setAlAnswersF(getXSSFCellValue(row.getCell(11)));
					}
					
					if(row.getCell(12)!=null  && row.getCell(12).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						ex.setAlAnswersG(getXSSFCellValue(row.getCell(12)));
					}
					
					/*当类型为判断题时，备选答案只识别前两个*/
					String clearAlteAnsws = exDao.selectStypeIsExist(type.trim());
					clearAlteAnswSe(clearAlteAnsws,ex);
					
					/*正确答案校验*/
					if(row.getCell(13)!=null  && row.getCell(13).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						String corAnswer = row.getCell(13).getStringCellValue();
						Object [] o = corAnsVrify(clearAlteAnsws,ex,corAnswer);
						if(!(boolean) o[0]){
							msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(14)+" "
									+ getText((String)o[1]) + "<br/>");
						}else{
							ex.setCorAnswer(corAnswer);
						}
					}else{
						msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(14)+" "
								+ getText("excel.error.null") + "<br/>");
					}
				}
				
				/*解析*/
				if(row.getCell(14)!=null  && row.getCell(14).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					ex.setAnalysis(row.getCell(14).getStringCellValue());
				}
				
				/*总部用户需要校验国家是否存在*/
				if(WebPageUtil.isHQRole()){
					if(row.getCell(15)!=null  && row.getCell(15).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						String country = row.getCell(15).getStringCellValue();
						if(!WebPageUtil.isStringNullAvaliable(exDao.selectPartyIdByPartyName(country.trim()))){
							msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(16)+" "
									+ getText("product.error.prompt10") + "<br/>");
						}else{
							ex.setCountryId(exDao.selectPartyIdByPartyName(country.trim()));
						}
					}else{
						msg.append(getText("excel.error.row") + (j+1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(16)+" "
								+ getText("excel.error.null") + "<br/>");
					}
				}else{
					ex.setCountryId(WebPageUtil.getLoginedUser().getPartyId());
				}
				addNumb(ex);//增加序号
				msg.append(examinationIsRepeat(ex,j,setModelSTO).toString());
				list.add(ex);
			}
			
			if(msg.length() > 0){
				return msg.toString();
			}else{
				importExamination(list);
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg.append(e.getMessage());
			return msg.toString();
		}
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
	
	public  Object[] corAnsVrify(String clearAlteAnsws,Examination ex,String corAnswer){//正确答案校验
		Object[] obj = new Object[2];
		String [] alteAnsw = {"A","B","C","D","E","F","G"};//跟随模板变化而设置，备选答案目前7个。
		if(corAnswer.length() >= 2 && !regexCorrectResponse(corAnswer.toUpperCase())){
			obj[0] = false;
			obj[1] = "product.error.prompt20";
			return obj;
		}
		String [] operAlteAnsw = corAnswer.trim().split("/");//备选答案不能为空
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
						obj[1] = "product.error.prompt06";
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
	
	public  boolean IsIntNumber(double score) {
		String scoreS = ((int)score)+"";
		String regex = "^\\+?[1-9][0-9]*$";
		return  match(regex, scoreS);
	}
	
	public boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	// 模板是否存在同一类型、同一题目的数据
	public StringBuffer examinationIsRepeat(Examination ex,int j,Set<Examination> setModelSTO) throws Exception{
		
		//考题是否已经导入
		StringBuffer msg = new StringBuffer();
		int n = exDao.selectTitleByEQAndType(ex);
		if(n >= 1){
			msg.append(getText("excel.error.row") + (j+1)+" "+ getText("product.error.prompt16") + "<br/>");
		};
		
		//模板是否存在重复数据
		boolean one= setModelSTO.add(ex);
		if(!one){
			msg.append(getText("excel.error.row") + (j+1)+" "+ getText("product.error.prompt15") + "<br/>");
		}
		return msg;
	}
	
	public void addNumb(Examination ex){
		try {
			String categories = ex.getCategories().trim();
			ex.setCategories(exDao.selectTypeIdByEname(replaceUnderline(categories),replaceABar(categories)));
			String mediums = ex.getMediums();
			String mediumsId = WebPageUtil.isStringNullAvaliable(mediums) ? exDao.selectTypeIdByEname(replaceUnderline(mediums.trim()),replaceABar(mediums.trim())) : "" ;
			ex.setMediums(mediumsId);
			String smaClass = ex.getSmaClass();
			ex.setSmaClass(WebPageUtil.isStringNullAvaliable(smaClass) ? exDao.selectTypeIdByEnameByparentId(replaceUnderline(smaClass.trim()),replaceABar(smaClass.trim()),mediumsId) : "" );

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ex.setExQuestions(WebPageUtil.isStringNullAvaliable(ex.getExQuestions()) ? ex.getExQuestions().trim() : "");
		ex.setAnalysis(WebPageUtil.isStringNullAvaliable(ex.getAnalysis()) ? ex.getAnalysis().trim() : "");
		
		ex.setAlAnswersA(WebPageUtil.isStringNullAvaliable(ex.getAlAnswersA()) ? "A. " + ex.getAlAnswersA().trim() : "");
		ex.setAlAnswersB(WebPageUtil.isStringNullAvaliable(ex.getAlAnswersB()) ? "B. " + ex.getAlAnswersB().trim() : "");
		ex.setAlAnswersC(WebPageUtil.isStringNullAvaliable(ex.getAlAnswersC()) ? "C. " + ex.getAlAnswersC().trim() : "");
		ex.setAlAnswersD(WebPageUtil.isStringNullAvaliable(ex.getAlAnswersD()) ? "D. " + ex.getAlAnswersD().trim() : "");
		ex.setAlAnswersE(WebPageUtil.isStringNullAvaliable(ex.getAlAnswersE()) ? "E. " + ex.getAlAnswersE().trim() : "");
		ex.setAlAnswersF(WebPageUtil.isStringNullAvaliable(ex.getAlAnswersF()) ? "F. " + ex.getAlAnswersF().trim() : "");
		ex.setAlAnswersG(WebPageUtil.isStringNullAvaliable(ex.getAlAnswersG()) ? "G. " + ex.getAlAnswersG().trim() : "");
	}
	
	/**
	 * 字符串是否为空字符
	 * @return
	 */
	public boolean strIsNullCharacter(String str){
		if(str == null){
			return false;
		}
		return (str.trim().length() > 0);
	};
	
	/**
	 * 去掉已.0结尾 (.0)字符
	 * @param str
	 * @return
	 */
	public String strIsEndsWith(String str){
		if(str == null){
			return "";
		}
		if(str.endsWith(".0")){
			int i = str.indexOf(".0");
			return str.substring(0, i);
		}else{
			return str;
		}
	};
	
	/**
	 * 检验正确答案表达式(A/B/C...../G)
	 * @param str
	 * @return
	 */
	public boolean regexCorrectResponse(String str){
		if(str ==  null){
			return false;
		}
        String regex = "^[A-G][/A-G]+$";
        boolean flag = str.matches(regex);
        return flag;
        /*if(flag)
            System.out.println(qq+"...is ok");
        else
            System.out.println(qq+"... 不合法");*/
	};
	
	/**
	 * 获取excel值
	 * @param x
	 * @return
	 */
	public String getXSSFCellValue(XSSFCell x){
		if(x == null){
			return "";
		}
		if(x.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){//兼容boolean类型(true/false)
			return (x.getBooleanCellValue() + "").toUpperCase();
		}else if(x.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){//兼容数字类型
			return strIsEndsWith(x.getNumericCellValue() + "");
		}else{//兼容字符串
			return x.getStringCellValue();
		}
	};
}
