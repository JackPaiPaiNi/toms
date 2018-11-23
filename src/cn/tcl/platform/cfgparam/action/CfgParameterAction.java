package cn.tcl.platform.cfgparam.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.cfgparam.service.CfgParameterService;
import cn.tcl.platform.cfgparam.vo.CfgParameter;

@SuppressWarnings( { "serial", "unchecked" })
public class CfgParameterAction extends BaseAction
{
	@Autowired(required=false)
	@Qualifier("cfgParameterService")
    private CfgParameterService cfgParameterService;
	
	/**
	 * 获取参数配置列表数据
	 */
	public void loadParameter()
	{
		response.setHeader("Content-Type", "application/json");
		try
		{
			JSONObject result = new JSONObject();
			
			String pdomain = request.getParameter("pdomain");
			String ptype = request.getParameter("ptype");
			String pkey = request.getParameter("pkey");
			
			String conditions = "";
			if(null!=pdomain && !"".equals(pdomain))
			{
				conditions += " and (PDOMAIN='"+pdomain+"' or PDOMAIN='TCL_TOMS')";
			}
			else
			{
				conditions += " and PDOMAIN='TCL_TOMS'";
			}
			
			if(null!=ptype && !"".equals(ptype))
			{
				conditions += " and PTYPE='"+ptype+"'";
			}
			if(null!=pkey && !"".equals(pkey))
			{
				conditions += " and PKEY='"+pkey+"'";
			}
			
			List<CfgParameter> cfgList = cfgParameterService.selectCfgParameter(conditions);
			
			Map<String,List<CfgParameter>> map = new HashMap<String,List<CfgParameter>>();
			for(CfgParameter cp : cfgList)
			{
				String _ptype = cp.getPtype();
				List<CfgParameter> cpList = null;
				if(map.containsKey(_ptype))
				{
					cpList = map.get(_ptype);
				}
				else
				{
					cpList = new ArrayList<CfgParameter>();
				}
				cpList.add(cp);
				map.put(_ptype, cpList);
			}
			
			result = JSONObject.fromObject(map);
			WebPageUtil.writeBack(result.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
	}
}
