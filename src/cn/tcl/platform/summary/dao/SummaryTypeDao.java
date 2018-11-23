package cn.tcl.platform.summary.dao;

import java.util.List;

import cn.tcl.platform.summary.vo.SummaryType;

public interface SummaryTypeDao {
	/**
	 * 总结栏目
	 * @return
	 * @throws Exception
	 */
	public List<SummaryType> getMessageType() throws Exception;
}
