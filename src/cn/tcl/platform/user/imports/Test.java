package cn.tcl.platform.user.imports;

import java.io.File;
import java.util.List;

import cn.tcl.excel.imports.ExcelImportUtil;
import cn.tcl.platform.user.vo.UserLogin;

public class Test 
{
	public static void main(String[] args) throws Exception 
	{
		ExcelImportUtil export = new ExcelImportUtil("D:/Program Files/apache/tomcat/apache-tomcat-6.0.32/webapps/toms/WEB-INF/classes/cn/tcl/platform/user/imports/imports.xml");
		export.init(new File("F:\\aaa.xlsx"),"aaa.xlsx","zh");
		List<UserLogin> users = export.bindToModelsAndImport(UserLogin.class);
		
		//Import ixb = (Import) XmlDomUtil.xmlStrToBean("E:/空间包/myeclipse/toms/src/cn/tcl/platform/user/imports/imports.xml");
		//ReflectUtil.mainReflect(new Mapping());
		
	}
}
