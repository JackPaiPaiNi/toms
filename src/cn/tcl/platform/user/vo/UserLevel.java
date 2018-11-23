package cn.tcl.platform.user.vo;
/**
 * 用户星级
 * @author fzl
 *
 */
public class UserLevel {
	private String id;
	
	private String value;
	
	private String countryId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}	
}
