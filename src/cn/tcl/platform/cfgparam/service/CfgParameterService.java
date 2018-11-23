package cn.tcl.platform.cfgparam.service;

import java.util.List;

import cn.tcl.platform.cfgparam.vo.CfgParameter;

public interface CfgParameterService 
{
	/**
	 * 根据条件查找参数配置
	 * @param conditions 查询条件语句字符串
	 * @return
	 * @throws Exception
	 */
	public List<CfgParameter> selectCfgParameter(String conditions) throws Exception;
}
