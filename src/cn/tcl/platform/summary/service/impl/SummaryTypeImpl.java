package cn.tcl.platform.summary.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.platform.summary.dao.SummaryTypeDao;
import cn.tcl.platform.summary.service.SummaryTypeService;
import cn.tcl.platform.summary.vo.SummaryType;

@Service("summaryTypeService")
public class SummaryTypeImpl implements SummaryTypeService{
	
	@Autowired
	private SummaryTypeDao summaryTypeDao;
	
	@Override
	public List<SummaryType> getMessageType() throws Exception {
		return summaryTypeDao.getMessageType();
	}
	
}
