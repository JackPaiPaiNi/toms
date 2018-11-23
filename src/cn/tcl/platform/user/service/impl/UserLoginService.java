package cn.tcl.platform.user.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.tcl.common.BaseAction;
import cn.tcl.common.MD5;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.excel.vo.ImportExcel;
import cn.tcl.platform.role.dao.IRoleDAO;
import cn.tcl.platform.role.vo.Role2User;
import cn.tcl.platform.shop.dao.IShopDao;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.user.dao.IUserLoginDAO;
import cn.tcl.platform.user.service.IUserLoginService;
import cn.tcl.platform.user.vo.LoginHistory;
import cn.tcl.platform.user.vo.UserLevel;
import cn.tcl.platform.user.vo.UserLogin;

@Service("userLoginService")
public class UserLoginService  extends BaseAction implements IUserLoginService {
	@Autowired
	private IShopDao shopDao;
	@Autowired
	private IUserLoginDAO userLoginDAO;
	@Autowired
	private IRoleDAO roleDAO;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public IUserLoginDAO getUserLoginDAO() {
		return userLoginDAO;
	}
	public void setUserLoginDAO(IUserLoginDAO userLoginDAO) {
		this.userLoginDAO = userLoginDAO;
	}
	@Override
	public List<UserLogin> selectUserLogin(String conditions) throws Exception
	{
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("conditions", conditions);
		return userLoginDAO.selectUserLogin(paramMap);
	}
	@Override
	@Transactional
	public void insertUserLogin(UserLogin ul,List<Role2User> ruList) throws Exception
	{
		userLoginDAO.insertUserLogin(ul);
		if(null!=ruList && ruList.size()>0)
		{
			for(Role2User ru : ruList)
			{
				roleDAO.insertRole2User(ru);
			}
		}
	}
	@Override
	public Map<String, Object> selectUserLoginGridData(String searchMsg,
			String startRow, String pageSize, String order, String sort,String selectQuertyPartyId) throws Exception
	{
		Map<String,Object> result = new HashMap<String,Object>();
		
		pageSize = "".equals(pageSize)?"0":pageSize;
		startRow = "".equals(startRow)?"0":startRow;
		//String conditions = " and " + WebPageUtil.buildDataPermissionSql("a.PARTY_ID",Contents.ROLE_DATA_PERMISSION_PARTY);
		//鏈烘瀯鏉冮檺鎺у埗锛岀敤鎴疯〃娌℃湁鏈烘瀯ID瀛楁锛岄渶瑕佷粠瑙掕壊涓幏鍙栨満鏋勶紝鍐嶄粠鏈烘瀯涓幏鍙栨墍鏈夎鑹诧紝鍐嶈幏鍙栨墍鏈夎鑹插搴旂殑鐢ㄦ埛淇℃伅,admin涓嶉渶瑕佹帶鍒舵潈闄�
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		String conditions = "";
		if(!WebPageUtil.isHAdmin())
		{
//			conditions += " and a.USER_LOGIN_ID not in (select tt.USER_LOGIN_ID from user_role_mapping tt where tt.role_id like '%"+Contents.ROLE_TYPE_HADMIN+"%')";
			if(!WebPageUtil.isHQRole())
			{
				conditions+="and r.ROLE_ID not like 'H%'";
			}				
			if(null!=userPartyIds && !"".equals(userPartyIds))
			{
//				conditions += " and a.USER_LOGIN_ID in " +
//						"(select DISTINCT m.USER_LOGIN_ID from user_role_mapping m,role_data_permission n " +
//						"where m.ROLE_ID = n.ROLE_ID and n.PERMISSION_TYPE = '"+Contents.ROLE_DATA_PERMISSION_PARTY+"'	and n.permission_value in (" +
//						userPartyIds + "))";
				conditions += "and a.party_id  in (" +userPartyIds + ")";
			}
			else
			{
				conditions += " and 1=2 ";
			}
		}
		else
		{
			//conditions += "  ";
			conditions += (WebPageUtil.isStringNullAvaliable(selectQuertyPartyId) ? "AND p.`COUNTRY_ID` = "+selectQuertyPartyId : "and 1=1" );
		}
		
		conditions += ("".equals(searchMsg) || null==searchMsg)?"":" and (a.USER_LOGIN_ID like '%"+searchMsg+"%' or a.USER_NAME like '%"+searchMsg+"%')";
		
		String orderBy= " order by a." + sort + " " + order;
		
		//oracle 1-10 startIndex:寮�濮嬩綅缃储寮� endIndex:缁撴潫浣嶇疆绱㈠紩
		//int startIndex = Integer.valueOf(startRow)+1;
		//int endIndex = Integer.valueOf(startRow)+Integer.valueOf(pageSize);
		//mysql 0-9 startIndex锛氬紑濮嬩綅缃储寮曪紝endIndex锛氬亸绉婚噺,姣忛〉鏄剧ず鏉＄洰鏁�
		int startIndex = Integer.valueOf(startRow);
		int endIndex = Integer.valueOf(pageSize);
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("conditions", conditions);
		paramMap.put("startIndex", startIndex);
		paramMap.put("endIndex", endIndex);
		paramMap.put("orderBy", orderBy);
		List<UserLogin> ulList = userLoginDAO.selectUserLoginWithPage(paramMap);
		
		//澧炲姞tips鎻愮ず
		for(UserLogin ul : ulList)
		{
			String userName = ul.getUserName();
			String _userName = "<div tips='"+userName+"' title='"+userName+"'>"+userName+"</div>";
			ul.setUserName(_userName);
		}
		
		int total = userLoginDAO.selectUserLoginCount(paramMap);
		result.put("total", total);
		result.put("list", ulList);
		return result;
	}
	@Override
	@Transactional
	public int updateUserLoginById(UserLogin ul,List<Role2User> ruList) throws Exception
	{
		if(null!=ruList && ruList.size()>0)
		{
			roleDAO.deleteRole2UserByUserId(ul.getUserLoginId());
			for(Role2User ru : ruList)
			{
				roleDAO.insertRole2User(ru);
			}
		}
		return userLoginDAO.updateUserLoginById(ul);
	}
	@Override
	public List<UserLogin> selectUserLoginForRole(String conditions, String roleId) throws Exception 
	{
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("conditions", conditions);
		paramMap.put("roleId", roleId);
		return userLoginDAO.selectUserLoginForRole(paramMap);
	}
	@Override
	@Transactional
	public void importUser(List<UserLogin> ulList) throws Exception{
		for(UserLogin ul : ulList){
			userLoginDAO.insertUserLogin(ul);
		}
	}
	@Override
	public void insertLoginHistory(LoginHistory lh) throws Exception{
		userLoginDAO.insertLoginHistory(lh);
	}
	@Override
	//鍙栧綋鍓嶇敤鎴锋墍灞炴満鏋勪笅鐨勬墍鏈夐攢鍞憳淇冮攢鍛�
	public List<UserLogin> selectSalerListData(String conditions,int type,String shopId) throws Exception{
		System.out.println(conditions);
		return userLoginDAO.selectSalerListData(conditions,type,shopId);
	}
	@Override
	public void updateUserPassword(UserLogin ul) throws Exception {
		ul.setIsUpdatePass("true");
		userLoginDAO.updateUserLoginById(ul);
	}
	@Override
	public List<String> selectDataPermissionValues(String permissionType,
			String userLoginId) throws Exception {
		return userLoginDAO.selectDataPermissionValues(permissionType, userLoginId);
	}
	@Override
	public List<UserLogin> getUserLoginList(String shopId) throws Exception {
		return userLoginDAO.getUserLoginList(shopId);
	}
	
	//修改微信号
	
	@Override
	public void updateUserLoginTableId(Map<String, String> map) throws Exception {
		userLoginDAO.updateUserLoginTableId(map);
	}
	@Override
	public void updateUserRoleTableId(Map<String, String> map) throws Exception {
		userLoginDAO.updateUserRoleTableId(map);
	}
	@Override
	public void updateUserSalerTableId(Map<String, String> map) throws Exception {
		userLoginDAO.updateUserSalerTableId(map);
		
	}
	@Override
	public Integer selectUserLoginId(String newUserLoginId) throws Exception {
		return userLoginDAO.selectUserLoginId(newUserLoginId);
	}
	@Override
	public void deleteUserLogin(UserLogin ul) throws Exception {
		/*String s = WebPageUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss");
		ul.setFalseUserId(ul.getUserLoginId() + "_" + s);*/
		userLoginDAO.deleteUserLogin(ul);
	}
	@Override
	public List<UserLogin> selectSalerList(String conditions, int type,
			String shopId) throws Exception {
		 
		return userLoginDAO.selectSalerList(conditions, type, shopId);
	}
	@Override
	public List<String> selectUserParty(String userId) throws Exception {
		return userLoginDAO.selectUserParty(userId);
	}
	@Override
	public List<UserLogin> selectParty(String countryId) throws Exception {
		return userLoginDAO.selectParty(countryId);
	}
	@Override
	public String read2007Excel(File file) throws IOException {
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
		
			Set<String> sett= new HashSet<String>();
			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);

				if (null==row ) {
					break;
				}
				HashMap<String, Object> modelMap = new HashMap<String, Object>();

				
/*				//验证用户名不能为中文
				var loginUser = $("#userLoginId").val();
				var userRegExp = new RegExp("[\u4e00-\u9fa5]");
				if(userRegExp.test(loginUser)){
					showMsg(jsLocale.get('user.list.form.save.error.loginUser'));
					falg = false;
					return falg;
				}else if("" == $.trim(loginUser)){
					showMsg(jsLocale.get('user.list.form.save.error_1.loginUser'));
					falg = false;
					return falg;
				}
				if("update" != addOrEdit){
					//验证密码复杂性   密码规则： 至少6位密码,字母和数组的组合,以字母开头
					var loginPassword = $("#password").val();
					var passwordRegExp = new RegExp("^[a-zA-Z]{1}[a-zA-Z0-9]*$");
					if(!passwordRegExp.test(loginPassword)){
						showMsg(jsLocale.get('user.list.form.save.error.loginPassword'));
						falg = false;
						return falg;
					}
				}
				*/
				//userID
				if(row.getCell(0)!=null
						&&
						row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					//楠岃瘉鐢ㄦ埛鏄惁瀛樺湪
					String userId=row.getCell(0).getStringCellValue();
					String  regEx="[\u4e00-\u9fa5]";
					Pattern pattern = Pattern.compile(regEx);
				    // 忽略大小写的写法
				    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
				    Matcher matcher = pattern.matcher(userId);
				    // 字符串是否与正则表达式相匹配
				    boolean rs = matcher.matches();
				    
					if(rs){
						msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.list.form.save.error.loginUser")+"</br>");

					}else{
						String conditions = " and a.USER_LOGIN_ID='"+userId+"'";
						
						List<UserLogin> ulList = selectUserLogin(conditions);
						if(null!=ulList && ulList.size()>0)
						{
						
							msg.append(getText("user.error.row") + (i + 1)
									+ getText("user.error.yhcz") + "<br/>");
						}else{
							
							modelMap.put("userId",userId);
						}
					}
					
					
					boolean is=sett.add(row.getCell(0).getStringCellValue());
					if( !is){
						msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.error.yhcf")+"</br>");
			          }
				}else{
					msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.error.userNotNull")+"</br>");

				}
				
				
				
				if(row.getCell(1)!=null
						&&
						row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					String userName=row.getCell(1).getStringCellValue();
					
						modelMap.put("userName", userName);
					
				}else{
					msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.error.userNameNo")+"</br>");

				}
				
				
				
				//Level	Date of joining TCL		
				if(row.getCell(2)!=null
						&&
						row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					
				String language=row.getCell(2).getStringCellValue();
				
					if(language.trim().equals("简体中文")){
						modelMap.put("language","zh");
					}else  if(language.trim().toUpperCase().equals("ENGLISH")){
						modelMap.put("language","en");
						
					}else  if(language.trim().equals("繁體中文")){
						modelMap.put("language","ftzw");
						
					}else{
						msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.error.yycw")+"</br>");

					}
				}else{
					msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.error.yyNotNull")+"</br>");

					}
					
				
				
			
				
				
				
				SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				String dt = dfd.format(date);
			
				if(row.getCell(3)!=null
						&&
						row.getCell(3).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					try{
						format.setLenient(false);
						date = format.parse(row.getCell(3).getStringCellValue());//有异常要捕获
						dfd.setLenient(false);
						String newD = dfd.format(date);
						
						modelMap.put("date",newD);
					}catch(Exception e){
						msg.append(getText("user.error.row") + (i + 1)+getText("user.error.cell")+(3+1)
								+ getText("user.error.date") + "<br/>");
					}
					
					
					
				}else{
					msg.append(getText("user.error.row") + (i + 1)+getText("user.error.cell")+(3+1)
							+ getText("user.error.dateNo") + "<br/>");
					
				}
				
				
				
				
				//email
				if(row.getCell(4)!=null
						&&
						row.getCell(4).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					String email=row.getCell(4).getStringCellValue();

					modelMap.put("email",email);
				}else{
					msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.error.emailNo")+"</br>");

				}
				
				
				
				
				
				
				// country
				
				if(row.getCell(5)!=null
						&&
						row.getCell(5).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					String   partyName=row.getCell(5).getStringCellValue();
					Shop party = shopDao.selectPartyByName(partyName);
					if(party == null){
						msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.error.countryNo")+"</br>");
					}else{
						modelMap.put("country",party.getPartyId());
					}
					
				}else{
					msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.error.countryNotNull")+"</br>");
				}
				
				
				
				
				//查找role得到role_id
				//往user_role_mapping添加用户角色
				
				//role
				if(row.getCell(6)!=null
						&&
						row.getCell(6).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					String   role=row.getCell(6).getStringCellValue();
					role=userLoginDAO.selectRoleByName(role);
					if(role != null  &&  !role.equals("")){
						modelMap.put("role",role);
					}else{
						msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.error.roleNo")+"</br>");
					}
				}else{
					msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.error.roleNotNull")+"</br>");
				}
				
				
				
				
				//password
				if(row.getCell(7)!=null
						&&
						row.getCell(7).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					
					String password = row.getCell(7).getStringCellValue();
					//  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,1000}$/
					//String  regEx="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[^]{8,1000}$";
					boolean rs=true;
					if(password.length()>7){
						rs=checkPassword(password);
					}else{
						rs=false;
					}
					
					//Pattern pattern = Pattern.compile(regEx);
				    // 忽略大小写的写法

					//Matcher matcher = pattern.matcher(password);
				    // 字符串是否与正则表达式相匹配
					//System.out.println(regEx);
				    //boolean rs = password.matches(regEx) /*matcher.matches()*/;
				    
					if(!rs){
						msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.list.form.save.error.loginPassword")+"</br>");

					}else{
						String _password = MD5.md5(password);
						
						modelMap.put("password",_password);
					}
					
					
				}else{
					msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.error.passwordNo")+"</br>");

				}
				
				
//				if(row.getCell(8)!=null
//						&&
//						row.getCell(8).getCellType()!=HSSFCell.CELL_TYPE_BLANK
//						){
				if(row.getCell(8)!=null && row.getCell(8).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					String   level=row.getCell(8).getStringCellValue();
					String id = userLoginDAO.selectIdByValue(level);
					if(id ==null){
						msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.error.levelNo")+"</br>");
					}
					modelMap.put("id",id);
				}else{
					modelMap.put("id","");
				}
//					if(id != null  &&  !id.equals("")){
//					}
//					else{
//						msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.error.levelNo")+"</br>");
//					}
//				}
//				else{
//					msg.append(getText("user.error.row") + (i + 1)+" "+getText("user.error.levelNotNull")+"</br>");
//				}
				
				allModelList.add(modelMap);
					
				
				
				List<Object> linked = new LinkedList<Object>();
				List<ImportExcel> test = new LinkedList<ImportExcel>();
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
					cell = row.getCell(j);
					if (cell == null) {
						continue;
					}
					DecimalFormat df = new DecimalFormat("0");// 格式化 number
																// String 字符
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
					DecimalFormat nf = new DecimalFormat("0");// 格式化数字
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						if ("@".equals(cell.getCellStyle()
								.getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
						} else if ("General".equals(cell.getCellStyle()
								.getDataFormatString())) {
							value = nf.format(cell.getNumericCellValue());
						} else {
							value = sdf.format(HSSFDateUtil.getJavaDate(cell
									.getNumericCellValue()));
						}
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN:
						value = cell.getBooleanCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						value = "";
						break;
					default:
						value = cell.toString();
					}
					if (value == null || "".equals(value)) {
						continue;
					}
					linked.add(value);

				}
				list.add(linked);
			}
			
			System.out.println("=======allModelList======================="+allModelList);
			if(msg.length()<=0){
			
				String userId="";
				if(WebPageUtil.getLoginedUserId()!=null){
					userId=(WebPageUtil.getLoginedUserId());
				}
			for (int i = 0; i < allModelList.size(); i++) {
						if(
								allModelList.get(i)
								.get("userId")!=null
								&& allModelList.get(i)
								.get("userId")!=""
								
								&& allModelList.get(i).get("userName")!=null
								&&  allModelList.get(i).get("userName")!=""
								
								&& allModelList.get(i).get("language")!=null
								&& allModelList.get(i).get("language")!=""
								
								&& allModelList.get(i).get("date")!=null
								&& allModelList.get(i).get("date")!=""
								&& allModelList.get(i).get("email")!=null
								&&allModelList.get(i).get("email")!=""
								&& allModelList.get(i).get("country")!=null
								&&allModelList.get(i).get("country")!=""
								&& allModelList.get(i).get("role")!=null
								&&allModelList.get(i).get("role")!=""
								&& allModelList.get(i).get("password")!=null
								&&allModelList.get(i).get("password")!=""
//								&& allModelList.get(i).get("id")!=null
//								&&allModelList.get(i).get("id")!=""
								){
							//*Shop name	Dealer code 
							
							UserLogin user = new UserLogin();
							user.setUserLoginId(allModelList.get(i)
								.get("userId").toString());
							
							user.setUserName(allModelList.get(i).get("userName").toString());
							user.setUserLocale(allModelList.get(i).get("language").toString());
							
							String  mydate=allModelList.get(i).get("date").toString();
				            Date date=dateFormat.parse(mydate);
				            
							user.setDisabledDateTime(date);
							user.setUserEmail(allModelList.get(i).get("email").toString());
							user.setPartyId(allModelList.get(i).get("country").toString());
							user.setRoleId(allModelList.get(i).get("role").toString());
							user.setPassword(allModelList.get(i).get("password").toString());
							if(allModelList.get(i).get("id")!=null&&allModelList.get(i).get("id")!=""									&&allModelList.get(i).get("id")!=""){
								user.setLevel(allModelList.get(i).get("id").toString());
							}
							date=dateFormat.parse(dateFormat.format(new Date()));
							user.setCreateDate(date);
							user.setStatus("1");
							user.setEnabled("1");
							user.setCreateBy(userId);
							user.setLoginType("LOCAL");
							Role2User role2User=new Role2User();
							role2User.setUserLoginId(allModelList.get(i)
									.get("userId").toString());
							role2User.setRoleId(allModelList.get(i).get("role").toString());
							
							userLoginDAO.insertUserLogin(user);
							roleDAO.insertRole2User(role2User);
						}
							
					}
						
		
			
			}
			

			if (msg.length() > 0) {
				return msg.toString();
			} else {
				
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg.append(e.getMessage());
			return msg.toString();
		}
	}

	@Override
	public String readExcel(File file, String fileName) throws IOException {
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		} else if ("xlsx".equals(extension)) {
			return read2007Excel(file);
		} else {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		}
	}
	@Override
	public List<UserLevel> selectUserLevel() throws Exception {
		return userLoginDAO.selectUserLevel();
	}
	@Override
	public List<UserLogin> selectUserLoginInfo(String userId,String pass,boolean account) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("pass", pass);
		
		if(account){
			return userLoginDAO.selectLoginUserId(map);
		}else{
			return userLoginDAO.selectLoginUserIdAndPass(map);
		}
	}

	public static boolean checkPassword(String password){
        if(password.matches("\\w+")){
            Pattern p1= Pattern.compile("[a-z]+");
            Pattern p2= Pattern.compile("[A-Z]+");
            Pattern p3= Pattern.compile("[0-9]+");
            Matcher m=p1.matcher(password);
            if(!m.find())
                return false;
            else{
                m.reset().usePattern(p2);
                if(!m.find())
                    return false;
                else{
                    m.reset().usePattern(p3);
                    if(!m.find())
                        return false;
                    else{
                        return true;
                    }
                }
            }
        }else{
            return false;
        }
 
    }
	@Override
	public List<UserLogin> selectSalerListDataBycondition(String conditions,
			int type, String shopId) throws Exception {
		return userLoginDAO.selectSalerListDataBycondition(conditions, type, shopId);
	}
	
}
