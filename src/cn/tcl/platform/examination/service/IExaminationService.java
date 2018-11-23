package cn.tcl.platform.examination.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import cn.tcl.platform.examination.vo.Examination;

public interface IExaminationService {
	
	public Map<String, Object> selectExamQuestions (Map<String,Object> map) throws Exception;
	
	public boolean deleteExamQuestions (Integer  id) throws Exception;
	
	public List<Examination> selectCorrectAnswerById (String  id) throws Exception;
	
	public List<Examination> selectSubclassCategoriesById (Integer  id) throws Exception;
	
	public void insertExamQuestions (Examination ex) throws Exception;
	
	public Examination selectExamQuestionsById (Integer  id) throws Exception;
	
	public void updateExamQuestionsById (Examination corAnswerList) throws Exception;
	
	public List<Examination> selectCountry (String userId) throws Exception;
					
	public String selectStypeIsExist (String eType) throws Exception;
	
	public Integer selectTypeCount (String eName) throws Exception;
	
	public void importExamination(List<Examination> Examination) throws Exception;
	
	public String onDeleteExamQuestions(String ids) throws Exception;
	
	public String selectPartyIdByPartyName (String partyName) throws Exception;

	String readExcel(File file, String fileName) throws IOException;

	String read2007Excel(File file) throws IOException;
}
