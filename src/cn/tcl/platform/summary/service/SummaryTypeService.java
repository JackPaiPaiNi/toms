package cn.tcl.platform.summary.service;

import java.util.List;

import cn.tcl.platform.summary.vo.SummaryType;

public interface SummaryTypeService {
	public List<SummaryType> getMessageType() throws Exception;
}
