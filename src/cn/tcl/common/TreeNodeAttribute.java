package cn.tcl.common;

public class TreeNodeAttribute 
{
	/**
	 * 树节点的URL
	 */
	private String url;
	/**
	 * 节点排序序号
	 */
	private int index;
	/**
	 * 菜单编码
	 */
	private String code;
	/**
	 * 国际化标签Key
	 */
	private String labelKey;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLabelKey() {
		return labelKey;
	}
	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}
}
