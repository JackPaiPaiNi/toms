package sunny;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import cn.tcl.common.WebPageUtil;


/**
 * 整理国际化文件到EXCEL中，方便海外人员翻译
 * @author sunxuanying
 *
 */
public class BuildProperties 
{
	private static String [] yu_yan = {"简体中文","繁体中文","英文"};
	private static String [] yu_yan_hz = {"zh","ftzw","en"};
	private static String saveExcelPath = "G:\\资料包\\公司资料\\海外TOP\\国际化待翻译\\整理后的Excel\\";
	
	/**
	 * 获取当前项目的绝对路径
	 * @return
	 * @throws Exception 
	 */
	private static String loadBaseRealPath() throws Exception
	{
		String path = BuildProperties.class.getResource("/").getPath();
		path = URLDecoder.decode(path,"UTF-8"); 
		return path;
	}
	
	/**
	 * 递归所有的文件
	 * @return
	 */
	private static void loadFoldFiles(File file,Map<String,String> paramMap)
	{
		if(file.isDirectory())
		{
			File [] listFile = file.listFiles();
			for(File _file : listFile)
			{
				if(file.isDirectory())
				{
					loadFoldFiles(_file, paramMap);
				}
			}
		}
		else
		{
			String fileName = file.getName();
			if(fileName.endsWith("properties") && !"config_sc.properties".equals(fileName) && !"config.properties".equals(fileName) && !"log4j.properties".equals(fileName) && !"struts.properties".equals(fileName))
			{
				String parentFold = file.getParent();
				String packageStr = parentFold.substring(parentFold.indexOf("classes")+8, parentFold.length());
				packageStr = packageStr.replaceAll("\\\\", ".");
				//保存文件信息
				if(!paramMap.containsKey(packageStr))
				{
					//获取文件基础名(package_en.properties)
					if(fileName.indexOf("_")>-1)
					{
						fileName = fileName.substring(0, fileName.indexOf("_"));
					}
					//获取文件基础名(package.properties)
					else
					{
						fileName = fileName.substring(0, fileName.indexOf("."));
					}
					String _filePath = parentFold + File.separator + fileName;
					paramMap.put(packageStr, _filePath);
				}
			}
		}
	}
	
	/**
	 * 遍历文件夹下所有的properties文件，转换为MAP对象(key:包名 value：文件名)
	 * @return
	 * @throws Exception 
	 */
	private static Map<String,String> initPropertiesToMap() throws Exception
	{
		Map<String,String> paramMap = new HashMap<String,String>();
		String rootFoldPath = loadBaseRealPath();
		File rootFold = new File(rootFoldPath);
		loadFoldFiles(rootFold, paramMap);
		return paramMap;
	}
	
	/**
	 * 导出国际化数据到excel
	 * @throws Exception
	 */
	private static void exportPropertiesToExcel() throws Exception
	{
		String excelPath = saveExcelPath+"TOMS国际化(后台)-"+WebPageUtil.dateToStr(new Date(), "yyyyMMdd")+".xls";
		FileOutputStream os = new FileOutputStream(excelPath);
		HSSFWorkbook wb = new HSSFWorkbook();
		Map<String,String> fileMap = initPropertiesToMap();
		Iterator<String> fileKeyIt = fileMap.keySet().iterator();
		
		//初始化表头样式
		//设置字体
		HSSFFont font = wb.createFont();
		font.setFontName("Verdana");
		font.setBoldweight((short) 100);
		font.setFontHeight((short) 300);
		font.setColor(HSSFColor.BLUE.index);
		// 创建单元格样式
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// 设置边框
		style.setBottomBorderColor(HSSFColor.RED.index);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setFont(font);
		
		while(fileKeyIt.hasNext())
		{
			String key_package = fileKeyIt.next();
			String fileBasePath = fileMap.get(key_package);
			
			HSSFSheet sheet = wb.createSheet(key_package);
			
			//循环列(从第1列开始，第0列输出配置项)
			for(int j=1;j<=yu_yan.length;j++)
			{
				sheet.setColumnWidth(j-1, 10000);
				sheet.setColumnWidth(j, 10000);
				
				String yy = yu_yan[j-1];
				String hz = yu_yan_hz[j-1];
				String fileName = fileBasePath + "_" + hz + ".properties";
				
				//第一次遍历，把配置项的key输出(第0列)
				InputStream in = new BufferedInputStream(new FileInputStream(fileName));
				Properties p = new OrderedProperties();
				p.load(in);
				if(j==1)
				{
					Enumeration<?> e = p.keys();
					//创建表头(第0行)
					HSSFRow row0 = sheet.getRow(0);
					if(null==row0)
					{
						row0 = sheet.createRow(0);
					}
					HSSFCell cell = row0.createCell(j-1);
					cell.setCellStyle(style);
					cell.setCellValue("配置项");
					
					//循环行(从第一行开始)
					int m=1;
					while(e.hasMoreElements())
					{
						String key = (String) e.nextElement();
						
						HSSFRow rowm = sheet.getRow(m);
						if(null==rowm)
						{
							rowm = sheet.createRow(m);
						}
						HSSFCell cellj = rowm.createCell(j-1);
						cellj.setCellValue(key);
						m++;
					}
				}
				//第1列及其后面
				//创建表头(第0行)
				HSSFRow row0 = sheet.getRow(0);
				if(null==row0)
				{
					row0 = sheet.createRow(0);
				}
				HSSFCell cell = row0.createCell(j);
				cell.setCellStyle(style);
				cell.setCellValue(yy);
				//循环航(从第一行开始)
				Enumeration<?> e = p.keys();
				int m=1;
				while(e.hasMoreElements())
				{
					String key = (String) e.nextElement();
					String value = p.getProperty(key);
					
					HSSFRow rowm = sheet.getRow(m);
					if(null==rowm)
					{
						rowm = sheet.createRow(m);
					}
					HSSFCell cellj = rowm.createCell(j);
					cellj.setCellValue(value);
					m++;
				}
			}
		}
		wb.write(os);
		os.close();
	}
	
	/**
	 * 读取文件内容到字符串
	 * @param Path
	 * @return
	 */
	private static String ReadFile(String Path) 
	{
		BufferedReader reader = null;
		String laststr = "";
		try 
		{
			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) 
			{
				tempString = tempString.replaceAll("\\/\\*.*\\*\\/", "").replaceAll("\\/\\/.*", "");
				laststr += tempString;
			}
			reader.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			if (reader != null) 
			{
				try 
				{
					reader.close();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}
		return laststr;
	}
	
	/**
	 * 导出js端的国际化数据到excel
	 * @throws Exception
	 */
	private static void exportJsToExcel() throws Exception
	{
		String classPath = loadBaseRealPath();
		File classFile = new File(classPath);
		File webInf = new File(classFile.getParent());
		String webRootPath = webInf.getParent();
		
		String JSFilePath = webRootPath + File.separator + "js" + File.separator + "platform" + File.separator + "locale" + File.separator + "tomsLocale";
		
		String excelPath = saveExcelPath+"TOMS国际化(前台)-"+WebPageUtil.dateToStr(new Date(), "yyyyMMdd")+".xls";
		FileOutputStream os = new FileOutputStream(excelPath);
		HSSFWorkbook wb = new HSSFWorkbook();
		
		//初始化表头样式
		//设置字体
		HSSFFont font = wb.createFont();
		font.setFontName("Verdana");
		font.setBoldweight((short) 100);
		font.setFontHeight((short) 300);
		font.setColor(HSSFColor.BLUE.index);
		// 创建单元格样式
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// 设置边框
		style.setBottomBorderColor(HSSFColor.RED.index);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setFont(font);
		
		HSSFSheet sheet = wb.createSheet("前端JS国际化文件");
		//循环列(从第1列开始，第0列输出配置项)
		for(int j=1;j<=yu_yan.length;j++)
		{
			sheet.setColumnWidth(j-1, 10000);
			sheet.setColumnWidth(j, 10000);
			
			String yy = yu_yan[j-1];
			String hz = yu_yan_hz[j-1];
			String fileName = JSFilePath + "-" + hz + ".js";
			
			//第一次遍历，把配置项的key输出(第0列)
			String jsonStr = ReadFile(fileName).replaceAll("var", "").replaceAll("jsLocale", "").replaceAll("=", "").replaceAll(";", "").trim();
			JSONObject json = JSONObject.fromObject(jsonStr);
			if(j==1)
			{
				Iterator<?> jsonKeys = json.keys();
				//创建表头(第0行)
				HSSFRow row0 = sheet.getRow(0);
				if(null==row0)
				{
					row0 = sheet.createRow(0);
				}
				HSSFCell cell = row0.createCell(j-1);
				cell.setCellStyle(style);
				cell.setCellValue("配置项");
				
				//循环行(从第一行开始)
				int m=1;
				while(jsonKeys.hasNext())
				{
					String key = (String) jsonKeys.next();
					
					HSSFRow rowm = sheet.getRow(m);
					if(null==rowm)
					{
						rowm = sheet.createRow(m);
					}
					HSSFCell cellj = rowm.createCell(j-1);
					cellj.setCellValue(key);
					m++;
				}
			}
			//第1列及其后面
			//创建表头(第0行)
			HSSFRow row0 = sheet.getRow(0);
			if(null==row0)
			{
				row0 = sheet.createRow(0);
			}
			HSSFCell cell = row0.createCell(j);
			cell.setCellStyle(style);
			cell.setCellValue(yy);
			//循环航(从第一行开始)
			Iterator<?> jsonValues = json.values().iterator();
			int m=1;
			while(jsonValues.hasNext())
			{
				String value = jsonValues.next().toString();
				
				HSSFRow rowm = sheet.getRow(m);
				if(null==rowm)
				{
					rowm = sheet.createRow(m);
				}
				HSSFCell cellj = rowm.createCell(j);
				cellj.setCellValue(value);
				m++;
			}
		}
		wb.write(os);
		os.close();
	}
	
	public static void main(String[] args) throws Exception 
	{
		exportPropertiesToExcel();
		exportJsToExcel();
	}
}
