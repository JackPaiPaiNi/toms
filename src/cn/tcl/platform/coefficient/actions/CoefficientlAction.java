package cn.tcl.platform.coefficient.actions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import cn.tcl.common.BaseAction;
import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.coefficient.service.ICoefficientService;
import cn.tcl.platform.coefficient.vo.Coefficient;
import cn.tcl.platform.excel.service.IExcelService;
import cn.tcl.platform.excel.service.ImportExcelService;
import cn.tcl.platform.product.vo.Product;
import cn.tcl.platform.sale.vo.Sale;
import cn.tcl.platform.training.updownload.util.FileUploadTools;
import cn.tcl.platform.user.vo.UserLogin;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.opensymphony.xwork2.util.logging.Logger;

@SuppressWarnings("serial")
public class CoefficientlAction extends BaseAction {
	@Autowired(required = false)
	@Qualifier("coefficientService")
	private ICoefficientService coefficientService;

	String user;
	String countryId;

	public String loadCoefficientPage() {
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public void addCoefficient() throws UnsupportedEncodingException {
		JSONObject result = new JSONObject();
		String core = request.getParameter("core");
		String all = request.getParameter("all");
		String country = request.getParameter("countryId");
		String file = request.getParameter("file");
		
		file=URLDecoder.decode(file , "utf-8");
		System.err.println("=======file=============="+file);
		Coefficient coefficient = new Coefficient();

		if (WebPageUtil.getLoginedUserId() != null) {
			String userID = WebPageUtil.getLoginedUserId();
			coefficient.setUser(userID);
		}

		if (core != null && !core.equals("")) {
			double cores = Double.parseDouble(core);
			coefficient.setCore(cores);
		}

		if (all != null && !all.equals("")) {
			double alls = Double.parseDouble(all);
			coefficient.setAll(alls);
		}
		
		if (file != null && !file.equals("")) {
			coefficient.setFile(file);
		}
		String conditions=null;
		if (country != null && !country.equals("")) {
			coefficient.setCountry(country);
			conditions=" co.country="+country;
		}

		coefficient.setIsUsing("YES");
		try {
			int one=coefficientService.saveCoefficient(coefficient);
			if(one>0){
			int two	=coefficientService.selectCoefficientFinally(null, conditions);
			if(two>0){
				coefficientService.updateCoefficient(coefficient);

			}else{
				coefficientService.saveCoefficientFinally(coefficient);
			}
			}
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			String msg = e.getCause() == null ? e.getMessage() : e.getCause()
					.getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}

	public void selectCoefficient(){
		JSONObject result=new JSONObject();
		String country = request.getParameter("countryId");
		response.setHeader("Content-Type", "application/json");
		try{

			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			String isUsing = request.getParameter("isUsing");

			System.out.println("======WebPageUtil.getLoginedUser().getPartyId()======"+WebPageUtil.getLoginedUser().getPartyId());
			if(
					
					!WebPageUtil.getLoginedUser().getPartyId().equals("999"))
			{
				conditions=" co.country="+WebPageUtil.getLoginedUser().getPartyId();
				if(isUsing!=null && !isUsing.equals("")){
					conditions+="  AND co.Is_using='"+isUsing+"'";
				}
			}else{
				if(country!=null && !country.equals("")){
					conditions=" co.country="+country;
					if(isUsing!=null && !isUsing.equals("")){
						conditions+="  AND co.Is_using='"+isUsing+"'";
					}
				}else{
					if(isUsing!=null && !isUsing.equals("")){
						conditions+="   co.Is_using='"+isUsing+"'";
					}
				}
			}
			
			
			
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			String pageStr=request.getParameter("page");
			int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
			String rowStr=request.getParameter("rows");
			int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
			int start = (page-1)*limit;
			
			
			Map<String, Object> map =coefficientService.selectCoefficient(start, limit, null, order, sort, conditions);
			int total = (Integer)map.get("total");
			List<Product> list = (ArrayList<Product>)map.get("rows");
			JSONArray jsonArray = JSONArray.fromObject(list);
			String rows = jsonArray.toString();
			result.accumulate("rows", rows);
			result.accumulate("total", total);
			result.accumulate("success", true);
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
		    
}