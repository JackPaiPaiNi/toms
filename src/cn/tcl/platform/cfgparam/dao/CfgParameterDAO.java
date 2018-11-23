package cn.tcl.platform.cfgparam.dao;

import java.util.List;
import java.util.Map;

import cn.tcl.platform.cfgparam.vo.CfgParameter;

public interface CfgParameterDAO
{
	/**
	 * 根据条件查找参数配置
	 * @param conditions 查询条件语句字符串
	 * @return
	 * @throws Exception
	 */
	public List<CfgParameter> selectCfgParameter(Map<String,Object> paramMap) throws Exception;
}