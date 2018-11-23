package cn.tcl.platform.cfgparam.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.tcl.platform.cfgparam.dao.CfgParameterDAO;
import cn.tcl.platform.cfgparam.service.CfgParameterService;
import cn.tcl.platform.cfgparam.vo.CfgParameter;

@SuppressWarnings("unchecked")
@Service("cfgParameterService")
public class CfgParameterServiceImpl implements CfgParameterService 
{
	@Autowired(required=false)
	@Qualifier("cfgParameterDAO")
    private CfgParameterDAO cfgParameterDAO;

	@Override
	public List<CfgParameter> selectCfgParameter(String conditions) throws Exception 
	{
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("conditions", conditions);
		return cfgParameterDAO.selectCfgParameter(paramMap);
	}
	
	
}
