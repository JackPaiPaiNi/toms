package cn.tcl.platform.target.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.excel.service.IExcelService;
import cn.tcl.platform.excel.service.ImportExcelService;
import cn.tcl.platform.target.service.ImportTargetService;

public class ImportTargetAction extends BaseAction {

	@Autowired(required = false)
	@Qualifier("importTargetService")
	private ImportTargetService importTargetService;
	
	
	
	public  void  importChannel(){
		System.out.println("importChannelTarget");
		try {

			String errorMsg = importTargetService.readExcel(uploadExcel,
					uploadExcelFileName,"channel");

			if ("".equals(errorMsg)) {
				WebPageUtil.writeBack("success");
			} else {
				WebPageUtil.writeBack(errorMsg);
			}
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			if (null == errorMsg || "".equals(errorMsg)) {
				errorMsg = this.getText("import.error.exist");
			}
			log.error(e.getStackTrace());
			e.printStackTrace();
			WebPageUtil.writeBack(errorMsg);
		}		
		
	}
	
	
	
	public  void  importOffice(){
		System.out.println("importOfficeTarget");
		try {

			String errorMsg = importTargetService.readExcel(uploadExcel,
					uploadExcelFileName,"office");

			if ("".equals(errorMsg)) {
				WebPageUtil.writeBack("success");
			} else {
				WebPageUtil.writeBack(errorMsg);
			}
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			if (null == errorMsg || "".equals(errorMsg)) {
				errorMsg = this.getText("import.error.exist");
			}
			log.error(e.getStackTrace());
			e.printStackTrace();
			WebPageUtil.writeBack(errorMsg);
		}		
		
	}
	
	
	public  void  importReg(){
		System.out.println("importRegTarget");
		try {

			String errorMsg = importTargetService.readExcel(uploadExcel,
					uploadExcelFileName,"reg");

			if ("".equals(errorMsg)) {
				WebPageUtil.writeBack("success");
			} else {
				WebPageUtil.writeBack(errorMsg);
			}
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			if (null == errorMsg || "".equals(errorMsg)) {
				errorMsg = this.getText("import.error.exist");
			}
			log.error(e.getStackTrace());
			e.printStackTrace();
			WebPageUtil.writeBack(errorMsg);
		}		
		
	}
	
	
	
	public  void  importBranch(){
		System.out.println("importBranch");
		try {

			String errorMsg = importTargetService.readExcel(uploadExcel,
					uploadExcelFileName,"branch");

			if ("".equals(errorMsg)) {
				WebPageUtil.writeBack("success");
			} else {
				WebPageUtil.writeBack(errorMsg);
			}
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			if (null == errorMsg || "".equals(errorMsg)) {
				errorMsg = this.getText("import.error.exist");
			}
			log.error(e.getStackTrace());
			e.printStackTrace();
			WebPageUtil.writeBack(errorMsg);
		}		
		
	}
	
	
	public  void  importRole(){
		System.out.println("importRole");
		try {
			String role="";

			if(request.getParameter("role")!=null && !request.getParameter("role").equals("")){
				role=request.getParameter("role");
			}
			String errorMsg = importTargetService.readExcel(uploadExcel,
					uploadExcelFileName,role);

			if ("".equals(errorMsg)) {
				WebPageUtil.writeBack("success");
			} else {
				WebPageUtil.writeBack(errorMsg);
			}
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			if (null == errorMsg || "".equals(errorMsg)) {
				errorMsg = this.getText("import.error.exist");
			}
			log.error(e.getStackTrace());
			e.printStackTrace();
			WebPageUtil.writeBack(errorMsg);
		}		
		
	}
	
}
