package cn.tcl.platform.examination.vo;

/**
 * @author admin
 *
 */
public class Examination {
	
	private Integer id;//id编号
	private String names;//名称
	private Integer fractions;//分值
	private String cType;//类别
	private String categories;//大类
	private String mediums;//中类
	private String smaClass;//小类
	private String exQuestions;//考题题目
	private String exQuestionsId;//题目编号
	private String exQuestionsTypeShow;//题目类型显示
	private String categoriesId;//大类编号
	private String mediumsId;//中类编号
	private String smaClassId;//小类编号
	private String countryId;//国家区域
	private String countryName;//国家名称
	private String analysis;//解析
	private String userId;
	private String language;//语言
	private String corAnswer;//正確答案
	private String alAnswersA;//备选答案
	private String alAnswersB;//备选答案
	private String alAnswersC;//备选答案
	private String alAnswersD;//备选答案
	private String alAnswersE;//备选答案
	private String alAnswersF;//备选答案
	private String alAnswersG;//备选答案
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getAnalysis() {
		return analysis;
	}
	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getExQuestionsTypeShow() {
		return exQuestionsTypeShow;
	}
	public void setExQuestionsTypeShow(String exQuestionsTypeShow) {
		this.exQuestionsTypeShow = exQuestionsTypeShow;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCategoriesId() {
		return categoriesId;
	}
	public void setCategoriesId(String categoriesId) {
		this.categoriesId = categoriesId;
	}
	public String getMediumsId() {
		return mediumsId;
	}
	public void setMediumsId(String mediumsId) {
		this.mediumsId = mediumsId;
	}
	public String getSmaClassId() {
		return smaClassId;
	}
	public void setSmaClassId(String smaClassId) {
		this.smaClassId = smaClassId;
	}
	public String getExQuestionsId() {
		return exQuestionsId;
	}
	public void setExQuestionsId(String exQuestionsId) {
		this.exQuestionsId = exQuestionsId;
	}
	public String getNames() {
		return names;
	}
	public void setNames(String names) {
		this.names = names;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public String getMediums() {
		return mediums;
	}
	public void setMediums(String mediums) {
		this.mediums = mediums;
	}
	public String getSmaClass() {
		return smaClass;
	}
	public void setSmaClass(String smaClass) {
		this.smaClass = smaClass;
	}
	public String getCorAnswer() {
		return corAnswer;
	}
	public void setCorAnswer(String corAnswer) {
		this.corAnswer = corAnswer;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getFractions() {
		return fractions;
	}
	public void setFractions(Integer fractions) {
		this.fractions = fractions;
	}
	public String getcType() {
		return cType;
	}
	public void setcType(String cType) {
		this.cType = cType;
	}
	public String getCType() {
		return cType;
	}
	public void setCType(String cType) {
		this.cType = cType;
	}
	public String getExQuestions() {
		return exQuestions;
	}
	public void setExQuestions(String exQuestions) {
		this.exQuestions = exQuestions;
	}
	public String getAlAnswersA() {
		return alAnswersA;
	}
	public void setAlAnswersA(String alAnswersA) {
		this.alAnswersA = alAnswersA;
	}
	public String getAlAnswersB() {
		return alAnswersB;
	}
	public void setAlAnswersB(String alAnswersB) {
		this.alAnswersB = alAnswersB;
	}
	public String getAlAnswersC() {
		return alAnswersC;
	}
	public void setAlAnswersC(String alAnswersC) {
		this.alAnswersC = alAnswersC;
	}
	public String getAlAnswersD() {
		return alAnswersD;
	}
	public void setAlAnswersD(String alAnswersD) {
		this.alAnswersD = alAnswersD;
	}
	public String getAlAnswersE() {
		return alAnswersE;
	}
	public void setAlAnswersE(String alAnswersE) {
		this.alAnswersE = alAnswersE;
	}
	public String getAlAnswersF() {
		return alAnswersF;
	}
	public void setAlAnswersF(String alAnswersF) {
		this.alAnswersF = alAnswersF;
	}
	public String getAlAnswersG() {
		return alAnswersG;
	}
	public void setAlAnswersG(String alAnswersG) {
		this.alAnswersG = alAnswersG;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alAnswersA == null) ? 0 : alAnswersA.hashCode());
		result = prime * result + ((alAnswersB == null) ? 0 : alAnswersB.hashCode());
		result = prime * result + ((alAnswersC == null) ? 0 : alAnswersC.hashCode());
		result = prime * result + ((alAnswersD == null) ? 0 : alAnswersD.hashCode());
		result = prime * result + ((alAnswersE == null) ? 0 : alAnswersE.hashCode());
		result = prime * result + ((alAnswersF == null) ? 0 : alAnswersF.hashCode());
		result = prime * result + ((alAnswersG == null) ? 0 : alAnswersG.hashCode());
		result = prime * result + ((analysis == null) ? 0 : analysis.hashCode());
		result = prime * result + ((cType == null) ? 0 : cType.hashCode());
		result = prime * result + ((categories == null) ? 0 : categories.hashCode());
		result = prime * result + ((categoriesId == null) ? 0 : categoriesId.hashCode());
		result = prime * result + ((corAnswer == null) ? 0 : corAnswer.hashCode());
		result = prime * result + ((countryId == null) ? 0 : countryId.hashCode());
		result = prime * result + ((countryName == null) ? 0 : countryName.hashCode());
		result = prime * result + ((exQuestions == null) ? 0 : exQuestions.hashCode());
		result = prime * result + ((exQuestionsId == null) ? 0 : exQuestionsId.hashCode());
		result = prime * result + ((exQuestionsTypeShow == null) ? 0 : exQuestionsTypeShow.hashCode());
		result = prime * result + ((fractions == null) ? 0 : fractions.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((mediums == null) ? 0 : mediums.hashCode());
		result = prime * result + ((mediumsId == null) ? 0 : mediumsId.hashCode());
		result = prime * result + ((names == null) ? 0 : names.hashCode());
		result = prime * result + ((smaClass == null) ? 0 : smaClass.hashCode());
		result = prime * result + ((smaClassId == null) ? 0 : smaClassId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Examination other = (Examination) obj;
		if (alAnswersA == null) {
			if (other.alAnswersA != null)
				return false;
		} else if (!alAnswersA.equals(other.alAnswersA))
			return false;
		if (alAnswersB == null) {
			if (other.alAnswersB != null)
				return false;
		} else if (!alAnswersB.equals(other.alAnswersB))
			return false;
		if (alAnswersC == null) {
			if (other.alAnswersC != null)
				return false;
		} else if (!alAnswersC.equals(other.alAnswersC))
			return false;
		if (alAnswersD == null) {
			if (other.alAnswersD != null)
				return false;
		} else if (!alAnswersD.equals(other.alAnswersD))
			return false;
		if (alAnswersE == null) {
			if (other.alAnswersE != null)
				return false;
		} else if (!alAnswersE.equals(other.alAnswersE))
			return false;
		if (alAnswersF == null) {
			if (other.alAnswersF != null)
				return false;
		} else if (!alAnswersF.equals(other.alAnswersF))
			return false;
		if (alAnswersG == null) {
			if (other.alAnswersG != null)
				return false;
		} else if (!alAnswersG.equals(other.alAnswersG))
			return false;
		if (analysis == null) {
			if (other.analysis != null)
				return false;
		} else if (!analysis.equals(other.analysis))
			return false;
		if (cType == null) {
			if (other.cType != null)
				return false;
		} else if (!cType.equals(other.cType))
			return false;
		if (categories == null) {
			if (other.categories != null)
				return false;
		} else if (!categories.equals(other.categories))
			return false;
		if (categoriesId == null) {
			if (other.categoriesId != null)
				return false;
		} else if (!categoriesId.equals(other.categoriesId))
			return false;
		if (corAnswer == null) {
			if (other.corAnswer != null)
				return false;
		} else if (!corAnswer.equals(other.corAnswer))
			return false;
		if (countryId == null) {
			if (other.countryId != null)
				return false;
		} else if (!countryId.equals(other.countryId))
			return false;
		if (countryName == null) {
			if (other.countryName != null)
				return false;
		} else if (!countryName.equals(other.countryName))
			return false;
		if (exQuestions == null) {
			if (other.exQuestions != null)
				return false;
		} else if (!exQuestions.equals(other.exQuestions))
			return false;
		if (exQuestionsId == null) {
			if (other.exQuestionsId != null)
				return false;
		} else if (!exQuestionsId.equals(other.exQuestionsId))
			return false;
		if (exQuestionsTypeShow == null) {
			if (other.exQuestionsTypeShow != null)
				return false;
		} else if (!exQuestionsTypeShow.equals(other.exQuestionsTypeShow))
			return false;
		if (fractions == null) {
			if (other.fractions != null)
				return false;
		} else if (!fractions.equals(other.fractions))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (mediums == null) {
			if (other.mediums != null)
				return false;
		} else if (!mediums.equals(other.mediums))
			return false;
		if (mediumsId == null) {
			if (other.mediumsId != null)
				return false;
		} else if (!mediumsId.equals(other.mediumsId))
			return false;
		if (names == null) {
			if (other.names != null)
				return false;
		} else if (!names.equals(other.names))
			return false;
		if (smaClass == null) {
			if (other.smaClass != null)
				return false;
		} else if (!smaClass.equals(other.smaClass))
			return false;
		if (smaClassId == null) {
			if (other.smaClassId != null)
				return false;
		} else if (!smaClassId.equals(other.smaClassId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
}
