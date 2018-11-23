package cn.tcl.common;

import java.util.List;

public class TreeNode 
{
	public static String INDETERMINATE_NODES = "indeterminate";
	public static String UNCHECKED_NODES = "unchecked";
	public static String CHECKED_NODES = "checked";
	public static String ROOT_NODE_PID = "root";
	/**
	 * 树节点文本
	 */
	private String text;
	/**
	 * 树节点ID
	 */
	private String id;
	/**
	 * 树节点小图标
	 */
	private String iconCls;
	/**
	 * 是否选中
	 */
	private boolean checked;
	/**
	 * 节点默认状态，open：打开    closed：关闭
	 */
	private String state;
	/**
	 * 树节点的自定义属性对象
	 */
	private TreeNodeAttribute attributes;
    /**
     * 子节点
     */
    private List<TreeNode> children;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public TreeNodeAttribute getAttributes() {
		return attributes;
	}
	public void setAttributes(TreeNodeAttribute attributes) {
		this.attributes = attributes;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
}
