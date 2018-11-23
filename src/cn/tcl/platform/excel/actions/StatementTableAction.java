package cn.tcl.platform.excel.actions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.excel.service.IStatementTableService;
import net.sf.json.JSONObject;
/**
 * @author Fay
 * 2017年11月7日10:14:20
 */
public class StatementTableAction  extends BaseAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired(required = false)
	@Qualifier("statementTableService")
	private IStatementTableService statementTableService;
	
	
	public String loadDealerSelloutPage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	

public void selectDealerSellout() {
	response.setHeader("Content-Type", "application/json");
	String beginDate = request.getParameter("beginDate");
	String endDate = request.getParameter("endDate");
	
	//String beginDate="2017-07-01";
	//String endDate="2017-07-31";
	
	String conditions="";
	String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
	
	if (!WebPageUtil.isHAdmin()) {
		if (null != userPartyIds && !"".equals(userPartyIds)) {
			conditions = "  pa.party_id in (" + userPartyIds + ")  ";
		} else {
			conditions = "  1=2  ";
		}
	} else {
		conditions = " 1=1 ";

	}
	
	Map<String,Object> whereMap = new HashMap<String,Object>();
	whereMap.put("beginDate", beginDate);
	whereMap.put("endDate", endDate);
	whereMap.put("conditions", conditions);
	whereMap.put("isHq", WebPageUtil.isHQRole());
	
	JSONObject object = null;
	try {
		object=statementTableService.selectDealerSellout(whereMap);
	
	} catch (Exception e) {
		e.printStackTrace();
	}
	System.out.println("=======sumTwo================"+object);
	
	
	WebPageUtil.writeBack(object.toString());
}


	
	
}
