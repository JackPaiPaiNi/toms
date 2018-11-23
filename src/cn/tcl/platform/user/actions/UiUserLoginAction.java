package cn.tcl.platform.user.actions;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.Cookie;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.WebUtils;

import org.pkutcl.aap.client.JAAPAsker;
import org.pkutcl.aap.client.JAAPClient;
import org.pkutcl.aap.exception.JAAPException;
import org.pkutcl.aap.proto.IAAPValues;
import org.pkutcl.aap.proto.JAAPUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.excel.imports.ExcelImportUtil;
import cn.tcl.platform.product.vo.Product;
import cn.tcl.platform.role.service.IRoleService;
import cn.tcl.platform.role.vo.Role2User;
import cn.tcl.platform.shop.vo.ShopParty;
import cn.tcl.platform.user.service.IUserLoginService;
import cn.tcl.platform.user.vo.LoginHistory;
import cn.tcl.platform.user.vo.UserLevel;
import cn.tcl.platform.user.vo.UserLogin;

import com.opensymphony.xwork2.ActionContext;
@SuppressWarnings( { "serial", "unchecked" })
public class UiUserLoginAction extends BaseAction 
{
	@Autowired(required = false)
	@Qualifier("userLoginService")
	private IUserLoginService userLoginService;
	
	@Autowired(required = false)
	@Qualifier("roleService")
	private IRoleService roleService;
	
	/**
	 * 鍔犺浇鐢ㄦ埛缁存姢鐣岄潰
	 * @return
	 */
	public String loadUserLoginPage()
	{
		try
		{
			return SUCCESS;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}

	/**
	 * 鏂板鐢ㄦ埛action
	 */
	public void insertUserLogin()
	{
		//杩斿洖JSON瀛楃涓�
		String result = "";
		try
		{
			//鍓嶅彴鑾峰彇鍙傛暟
			String userLoginId = request.getParameter("userLoginId");
			String userWorkNum = request.getParameter("userWorkNum");
			String userName = request.getParameter("userName");
			String password = request.getParameter("password");
//			String passwordHint = request.getParameter("passwordHint");
			String enabled = request.getParameter("enabled");
			String disabledDateTime = request.getParameter("disabledDateTime");
			String loginType = request.getParameter("loginType");
			String createBy = WebPageUtil.getLoginedUserId();
			Date createDate = new Date();
			String userEmail = request.getParameter("userEmail");
			String userMcId = request.getParameter("userMcId");
			String userTelNum = request.getParameter("userTelNum");
			String userLocale = request.getParameter("userLocale");
			String partyId = request.getParameter("partyId");
			String [] roleIdArray = request.getParameterValues("roleId");
			String level = request.getParameter("userLevel");
			
			
			
			//楠岃瘉鐢ㄦ埛鏄惁瀛樺湪
			String conditions = " and a.USER_LOGIN_ID='"+userLoginId+"'";
			List<UserLogin> ulList = userLoginService.selectUserLogin(conditions);
			if(null!=ulList && ulList.size()>0)
			{
				result = "{\"success\":\"true\",\"msg\":\"exists\"}";
			}
			else
			{
				UserLogin ul = new UserLogin();
				ul.setCreateBy(createBy);
				ul.setCreateDate(createDate);
				ul.setDisabledDateTime(WebPageUtil.strToDate(disabledDateTime));
				ul.setEnabled(enabled);
				ul.setLoginType(loginType);
//				ul.setPasswordHint(passwordHint);
				ul.setPassword(password);
				ul.setUserEmail(userEmail);
				ul.setUserLoginId(userLoginId);
				ul.setUserName(userName);
				ul.setUserWorkNum(userWorkNum);
				ul.setUserMcId(userMcId);
				ul.setUserTelNum(userTelNum);
				ul.setUserLocale(userLocale);
				ul.setPartyId(partyId);
				ul.setLevel(level);
				
				List<Role2User> ruList = new ArrayList<Role2User>();
				for(int i=0;i<roleIdArray.length;i++)
				{
					String rId = roleIdArray[i];
					if(null!=rId && !"".equals(rId))
					{
						Role2User ru = new Role2User();
						ru.setUserLoginId(userLoginId);
						ru.setRoleId(rId);
						ruList.add(ru);
					}
				}
				userLoginService.insertUserLogin(ul,ruList);
				
				result = "{\"success\":\"true\",\"msg\":\"success\"}";
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result = "{\"success\":\"true\",\"msg\":\""+msg+"\"}";
		}
		WebPageUtil.writeBack(result);
	}

	/**
	 * 鑾峰彇鐢ㄦ埛鍒楄〃鏁版嵁
	 */
	public void loadUserLoginGridData()
	{
		JSONObject result = new JSONObject();
		try
		{
			String sort = request.getParameter("sort");
			sort = WebPageUtil.tansBeanFiledToDbField(sort);
			String order = request.getParameter("order");
			String searchMsg = request.getParameter("searchMsg");
			String selectQuertyPartyId = request.getParameter("selectQuertyPartyId");
			String pageIndex = request.getParameter("page");
			String pageSize = request.getParameter("rows");
			String startRow = (Integer.valueOf(pageIndex)-1)*Integer.valueOf(pageSize) + "";
			
			Map<String, Object> map = userLoginService.selectUserLoginGridData(searchMsg, startRow, pageSize, order, sort,selectQuertyPartyId);
			int total = (Integer)map.get("total");
			List<UserLogin> list = (ArrayList<UserLogin>)map.get("list");
			JSONArray jsonArray = JSONArray.fromObject(list);
			String rows = jsonArray.toString();
			result.accumulate("rows", rows);
			result.accumulate("total", total);
			result.accumulate("success", true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", true);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 鑾峰彇寰呬慨鏀圭殑鐢ㄦ埛鏁版嵁
	 */
	public void loadUpdateUserLoginData()
	{
		JSONObject result = new JSONObject();
		try
		{
			String userLoginId = request.getParameter("userLoginId");
			String conditions = " and a.USER_LOGIN_ID='"+userLoginId+"'";
			List<UserLogin> ulList = userLoginService.selectUserLogin(conditions);
			UserLogin ul = new UserLogin();
			if(null!=ulList && ulList.size()>0)
			{
				ul = ulList.get(0);
			}
			result.accumulate("userLoginId", ul.getUserLoginId());
			result.accumulate("userName", ul.getUserName());
			result.accumulate("password", ul.getPassword());
			result.accumulate("passwordHint", ul.getPasswordHint());
			result.accumulate("enabled", ul.getEnabled());
			result.accumulate("disabledDateTime", WebPageUtil.dateToStr(ul.getDisabledDateTime()));
			result.accumulate("loginType", ul.getLoginType());
			result.accumulate("userEmail", ul.getUserEmail());
			result.accumulate("userWorkNum", ul.getUserWorkNum());
			result.accumulate("userMcId", ul.getUserMcId());
			result.accumulate("userTelNum", ul.getUserTelNum());
			result.accumulate("userLocale", ul.getUserLocale());
			result.accumulate("addOrEdit", "update");
			result.accumulate("partyId", ul.getPartyId());
			result.accumulate("userLevel", ul.getLevel());
			
			List<String> partyList = userLoginService.selectUserParty(userLoginId);
			if(partyList.size()>0){
				result.accumulate("party",partyList.get(0));
			}
			
			conditions = " and a.USER_LOGIN_ID='"+userLoginId+"'";
			List<Role2User> ruList = roleService.selectRole2User(conditions);
			/**
			 * 鏂伴�昏緫锛氫竴涓敤鎴峰彧鑳芥湁涓�涓鑹�
			 */
			Role2User r2u = new Role2User();
			if(null!=ruList && ruList.size()>0){
				r2u = ruList.get(0);
			}
			/**
			 * 鍘熼�昏緫锛氫竴涓敤鎴锋湁澶氫釜瑙掕壊
			 
			String roleId = "";
			for(Role2User ru : ruList)
			{
				roleId += ","+ru.getRoleId();
			}
			*/
			result.accumulate("roleId", r2u.getRoleId());
		
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 缂栬緫淇濆瓨鐢ㄦ埛鏁版嵁
	 */
	public void editUserLogin()
	{
		//杩斿洖JSON瀛楃涓�
		String result = "";
		try
		{
			//鍓嶅彴鑾峰彇鍙傛暟
			String userLoginId = request.getParameter("userLoginId") == null ? request.getParameter("addUserLoginId"):request.getParameter("userLoginId");
			String userWorkNum = request.getParameter("userWorkNum");
			String userName = request.getParameter("userName");
//			String password = request.getParameter("password");
//			String passwordHint = request.getParameter("passwordHint");
			String enabled = request.getParameter("enabled");
			String disabledDateTime = request.getParameter("disabledDateTime");
			String loginType = request.getParameter("loginType");
			String userEmail = request.getParameter("userEmail");
			String userMcId = request.getParameter("userMcId");
			String userTelNum = request.getParameter("userTelNum");
			String userLocale = request.getParameter("userLocale");
			String partyId = request.getParameter("partyId");
			String [] roleIdArray = request.getParameterValues("roleId");
			
			//--new 微信号
			String newUserLoginId = request.getParameter("newUserLoginId");
			
			//用户星级
			String level = request.getParameter("userLevel");
			
			Date updateTime= new Date();
			
			UserLogin ul = new UserLogin();
			ul.setUserLoginId(userLoginId);
			ul.setUserName(userName);
//			ul.setPassword(password);
//			ul.setPasswordHint(passwordHint);
			ul.setEnabled(enabled);
			ul.setDisabledDateTime(WebPageUtil.strToDate(disabledDateTime,"yyyy-MM-dd"));
			ul.setLoginType(loginType);
			ul.setUserEmail(userEmail);
			ul.setUserWorkNum(userWorkNum);
			ul.setUserMcId(userMcId);
			ul.setUserTelNum(userTelNum);
			ul.setUserLocale(userLocale);
			ul.setPartyId(partyId);
			ul.setLevel(level);
			ul.setUpdateTime(updateTime);
			
			List<Role2User> ruList = new ArrayList<Role2User>();
			for(int i=0;i<roleIdArray.length;i++)
			{
				String rId = roleIdArray[i];
				if(null!=rId && !"".equals(rId))
				{
					Role2User ru = new Role2User();
					ru.setUserLoginId(userLoginId);
					ru.setRoleId(rId);
					ruList.add(ru);
				}
			}
			
			Map<String,String> idMap = new  HashMap<String,String>();
			idMap.put("newUserLoginId", newUserLoginId);
			idMap.put("userLoginId", userLoginId);
			
			userLoginService.updateUserLoginTableId(idMap);
			userLoginService.updateUserRoleTableId(idMap);
			userLoginService.updateUserSalerTableId(idMap);
			
			
			userLoginService.updateUserLoginById(ul,ruList);
			result = "{\"success\":\"true\",\"msg\":\"success\"}";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result = "{\"success\":\"true\",\"msg\":\""+msg+"\"}";
		}
		WebPageUtil.writeBack(result);
	}
	
	/**
	 * 淇敼鐢ㄦ埛瀵嗙爜
	 * @return
	 */
	public void updateUserPassword(){
		//杩斿洖JSON瀛楃涓�
		String result = "";
		try
		{
			//鍓嶅彴鑾峰彇鍙傛暟
			String userLoginId = request.getParameter("editUserLoginId");
			String updatePassword = request.getParameter("updatePassword");
			
			UserLogin ul = new UserLogin();
			ul.setUserLoginId(userLoginId);
			ul.setPassword(updatePassword);
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
			ul.setUpdatePassword(date);
			userLoginService.updateUserPassword(ul);
			result = "{\"success\":\"true\",\"msg\":\"success\"}";
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result = "{\"success\":\"true\",\"msg\":\""+msg+"\"}";
		}
		WebPageUtil.writeBack(result);
	}
	
	/**
	 * 删除用户
	 */
	public void deleteUserLogin(){
		String result="";
		try {
			String userLoginId = request.getParameter("userLoginId");
			UserLogin ul = new UserLogin();
			ul.setUserLoginId(userLoginId);
			ul.setOperatingUser(WebPageUtil.getLoginedUserId());
			userLoginService.deleteUserLogin(ul);
			result = "{\"success\":\"true\",\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result = "{\"success\":\"true\",\"msg\":\""+msg+"\"}";
		}
		WebPageUtil.writeBack(result);
	}
	
	/**
	 * 鍚敤銆佺鐢ㄧ敤鎴�
	 */
	public void enableOrDisableUser()
	{
		//杩斿洖JSON瀛楃涓�
		String result = "";
		try
		{
			//鍓嶅彴鑾峰彇鍙傛暟
			String userLoginId = request.getParameter("userLoginId");
			String enabled = request.getParameter("enabled");
			
			UserLogin ul = new UserLogin();
			ul.setUserLoginId(userLoginId);
			ul.setEnabled(enabled);
			userLoginService.updateUserLoginById(ul,null);
			result = "{\"success\":\"true\",\"msg\":\"success\"}";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result = "{\"success\":\"true\",\"msg\":\""+msg+"\"}";
		}
		WebPageUtil.writeBack(result);
	}

	/**
	 * 鐧婚檰
	 */
	public void login()
	{
		//杩斿洖JSON瀛楃涓�
		String result = "";
		LoginHistory lh = new LoginHistory();
		try
		{
			UserLogin user = new UserLogin();
			//鍓嶅彴鑾峰彇鍙傛暟
			String userLoginId = request.getParameter("name");
			String password = request.getParameter("password");
			
			lh.setUserLoginId(userLoginId);
			lh.setPasswordUsed(password);
			Timestamp nousedate = new Timestamp(new Date().getTime());
			lh.setLoginDatetime(nousedate);
			
			//String conditions = " and a.USER_LOGIN_ID='"+userLoginId+"'";
			List<UserLogin> ulList = userLoginService.selectUserLoginInfo(userLoginId,null,true);
			if(null!=ulList && ulList.size()>0)
			{
				UserLogin ul = ulList.get(0);
				lh.setPartyId(ul.getPartyId());
				if(!"1".equals(ul.getEnabled()))
				{
					result = "{\"success\":\"true\",\"msg\":\""+getText("login.error.stopuse")+"\"}";
				}else if(ul.getDisabledDateTime().before(new Date())){
					result = "{\"success\":\"true\",\"msg\":\""+getText("login.error.expired")+"\"}";
				}
				else if("LDAP".equals(ul.getLoginType()))
				{
					String str = webDeamLogin(ul.getUserLoginId(), password);
					if (!str.equals("SUCCESS"))
					{
						if (str.equals("login002"))
						{
							result = "{\"success\":\"true\",\"msg\":\""+getText("login.error.guoqi")+"\"}";
						}
						else if (str.equals("login003"))
						{
							result = "{\"success\":\"true\",\"msg\":\""+getText("login.error.mimacuowu")+"\"}";
						}
						else if (str.equals("login004"))
						{
							result = "{\"success\":\"true\",\"msg\":\""+getText("login.error.zscuowu")+"\"}";
						}
						else if (str.equals("login005"))
						{
							result = "{\"success\":\"true\",\"msg\":\""+getText("login.error.lock")+"\"}";
						}
						else if (str.equals("login013"))
						{
							result = "{\"success\":\"true\",\"msg\":\""+getText("login.error.yhmcuowu")+"\"}";
						}
						else if (str.equals("login007"))
						{
							result = "{\"success\":\"true\",\"msg\":\""+getText("login.error.yhbcz")+"\"}";
						}
						else if (str.equals("login008"))
						{
							result = "{\"success\":\"true\",\"msg\":\""+getText("login.error.yhgq")+"\"}";
						}
						else if (str.equals("login010"))
						{
							result = "{\"success\":\"true\",\"msg\":\""+getText("login.error.jhzs")+"\"}";
						}
						else if (str.equals("login011"))
						{
							result = "{\"success\":\"true\",\"msg\":\""+getText("login.error.yzsb")+"\"}";
						}
						else if (str.equals("login"))
						{
							result = "{\"success\":\"true\",\"msg\":\""+getText("login.error.bqdcw")+"\"}";
						}
					}
					else
					{
						result = "{\"success\":\"true\",\"msg\":\"success\"}";
						user = ul;
					}
				}
				else if("LOCAL".equals(ul.getLoginType()))
				{
					//String conditionsTemp = " and a.USER_LOGIN_ID='"+userLoginId+"' and a.PASSWORD='"+password+"'";
					List<UserLogin> ulListTemp = userLoginService.selectUserLoginInfo(userLoginId,password,false);
					if(null!=ulListTemp && ulListTemp.size()>0)
					{
						user = ulListTemp.get(0);
						result = "{\"success\":\"true\",\"msg\":\"success\"}";
					}
					else
					{
						result = "{\"success\":\"true\",\"msg\":\""+getText("login.error.mimacuowu")+"\"}";
					}
				}
			}
			else
			{
				result = "{\"success\":\"true\",\"msg\":\""+getText("login.error.yhbcz")+"\"}";
			}
			//濡傛灉鐧诲綍鎴愬姛,璁剧疆session
			JSONObject tmp = JSONObject.fromObject(result);
			if("success".equals(tmp.get("msg")))
			{
				WebPageUtil.setSessionAndCookie(user,request,response);
				lh.setSuccessfulLogin("1".charAt(0));
			}
			else
			{
				lh.setSuccessfulLogin("0".charAt(0));
			}
			lh.setComments(tmp.get("msg")+"");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			log.error(e.getMessage(),e);
			result = "{\"success\":\"true\",\"msg\":\""+msg+"\"}";
			lh.setSuccessfulLogin("0".charAt(0));
			lh.setComments(msg);
		}
		//淇濆瓨鐧诲綍鍘嗗彶
		try
		{
			userLoginService.insertLoginHistory(lh);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getMessage();
			result = "{\"success\":\"true\",\"msg\":\""+msg+"\"}";
		}
		WebPageUtil.writeBack(result);
	}
	/**
	 * webdeam楠岃瘉
	 * @param userName
	 * @param password
	 * @param timeStamp
	 * @return
	 * @throws Exception
	 */
	private String webDeamLogin(String userName, String password) throws Exception 
	{
		String strUserId = userName;
		String strPassword = password;
		String strCredential = "";
		Long time=new Date().getTime();
		String strTimeStamp = time.toString();
		String strUserSeal = strTimeStamp;
		String strClientIp = request.getRemoteAddr();
		long lTimeStamp = 0L;
		Properties props = WebPageUtil.getPropertiesByFileName(WebPageUtil.configFileName);
		Properties params = new Properties();
		params.put("loginURL", props.getProperty("WEBDEAM.loginURL"));
		params.put("JAAPSESSIONID", props.getProperty("WEBDEAM.wdCookieName"));
		params.put("AAS_Host", props.getProperty("WEBDEAM.AAS_Host"));
		params.put("AAS_Port", props.getProperty("WEBDEAM.AAS_Port"));
		params.put("AppServerName", props.getProperty("WEBDEAM.AppServerName"));
		params.put("SharedSecret", props.getProperty("WEBDEAM.SharedSecret"));
		params.put("CookieValidMinute", props.getProperty("WEBDEAM.CookieValidMinute"));
		params.put("SocketTimeoutSecond", props.getProperty("WEBDEAM.SocketTimeoutSecond"));
		int returncode = -1;
		try 
		{
			org.pkutcl.aap.client.JAAPClient client = new org.pkutcl.aap.client.JAAPClient(params);
			if (client == null) 
			{
				log.info("webDaemon's client v3 is null");
				return "login";
			}
			JAAPAsker asker = new JAAPAsker(client);
			JAAPUser user = new JAAPUser(strUserId, strPassword, strUserSeal, strUserSeal, lTimeStamp, strClientIp);
			user.setCredential(strCredential);
		
			try 
			{
				returncode = asker.login(user);
				switch (returncode) 
				{
					case 1: // '\001'
						String jaapsessionid = "";
						jaapsessionid = asker.getAttributeValue(IAAPValues.SESSION_ID);
						String username = asker.checkSession(jaapsessionid);
						Cookie cookie = new Cookie("JAAPSESSIONID", jaapsessionid);
			            cookie.setSecure(false);
			            cookie.setPath("/");//璁剧疆璺緞
			            cookie.setMaxAge(-1);//-1锛氬綋鍓嶉〉闈㈡湁鏁� ; >0 :澶氬皯绉抍ookie鏈夋晥
			            cookie.setDomain("tcl.com");//浣滅敤鍩�
			            response.addCookie(cookie);
						log.info("[" + strUserId + "] login ok");
						return "SUCCESS";
					case 2: // '\002'
						log.info("[" + strUserId + "] prompt to modify passwod");
						return "login002";
					case 3: // '\003'
						log.info("[" + strUserId + "] password failed");
						return "login003";
					case 4: // '\004'
						log.info("[" + strUserId + "] credential failed");
						return "login004";
					case 5: // '\005'
						log.info("[" + strUserId + "] has locked");
						return "login005";
					case 13: // '\r'
						log.info("[" + strUserId + "] is invalid");
						return "login013";
					case 6: // '\006'
						log.info("[" + strUserId + "] has login");
						return "login006";
					case 7: // '\007'
						log.info("[" + strUserId + "] has no such user");
						return "login007";
					case 8: // '\b'
						log.info("[" + strUserId + "] is overdue");
						return "login008";
					case 10: // '\n'
						log.info("[" + strUserId + "] need active credential in http://info.tcl.com");
						return "login010";
					case 11: // '\013'
						log.info("[" + strUserId + "] X.509 certification failed");
						return "login011";
					case 9: // '\t'
					case 12: // '\f'
					default:
						return "login";
				}
			} 
			catch (JAAPException e) 
			{
				return "login";
			}
		} 
		catch (InvalidParameterException e1) 
		{
			e1.printStackTrace();
			return "login";
		}
	}
	
	/**
	 * 閫氳繃cookie杩涜鍗曠偣鐧诲綍鐨刟ction
	 * @return
	 * @throws Exception
	 */
	public String cookieLogin() throws Exception
	{
		try
		{
			//棣栧厛娓呴櫎鍘熸湁鐨剆ession
			ActionContext context = ActionContext.getContext();
			context.getSession().remove("loginUserId");
			context.getSession().remove("loginUser");
			context.getSession().clear();
			
			//鑾峰彇cookie锛屽彇寰梪serLoginId
			Cookie[] cookies = request.getCookies();
			Properties props = WebPageUtil.getPropertiesByFileName(WebPageUtil.configFileName);
			String wdCookieName = props.getProperty("WEBDEAM.wdCookieName");
			String _userLoginId = "";
			if(null!=cookies)
			{
				for (int i = 0; i < cookies.length; i++) {
					if(wdCookieName.equalsIgnoreCase(cookies[i].getName()))
					{
						_userLoginId = cookies[i].getValue();
					}
				}
			}
			//濡傛灉cookie瀛樺湪鐢ㄦ埛ID锛屽垯楠岃瘉锛屼笉瀛樺湪鐩存帴杩斿洖鐧诲綍鐣岄潰
			if(!"".equals(_userLoginId) && null!=_userLoginId)
			{
				//杞爜宸插姞瀵嗙殑userLoginId
				Properties params = new Properties();
				params.put("loginURL", props.getProperty("WEBDEAM.loginURL"));
				params.put("JAAPSESSIONID", props.getProperty("WEBDEAM.wdCookieName"));
				params.put("AAS_Host", props.getProperty("WEBDEAM.AAS_Host"));
				params.put("AAS_Port", props.getProperty("WEBDEAM.AAS_Port"));
				params.put("AppServerName", props.getProperty("WEBDEAM.AppServerName"));
				params.put("SharedSecret", props.getProperty("WEBDEAM.SharedSecret"));
				params.put("CookieValidMinute", props.getProperty("WEBDEAM.CookieValidMinute"));
				params.put("SocketTimeoutSecond", props.getProperty("WEBDEAM.SocketTimeoutSecond"));
				JAAPClient jaapClient = new JAAPClient(params);
				JAAPAsker asker = new JAAPAsker(jaapClient);
				String userLoginId = asker.checkSession(_userLoginId);
				
				//鏍规嵁鐢ㄦ埛Id鑾峰彇鏈郴缁熺敤鎴蜂俊鎭�
				String conditions = " and a.USER_LOGIN_ID='"+userLoginId+"'";
				List<UserLogin> ulList = userLoginService.selectUserLogin(conditions);
				UserLogin userLogin;
				if(null!=ulList && ulList.size()>0)
				{
					userLogin = ulList.get(0);
					WebPageUtil.setSessionAndCookie(userLogin,request,response);
					return SUCCESS;
				}
				else
				{
					return ERROR;
				}
			}
			else
			{
				return ERROR;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	/**
	 * 娉ㄩ攢
	 */
	public String loginOut()
	{
		try
		{
			ActionContext context = ActionContext.getContext();
			context.getSession().clear();
			Cookie cookie=new Cookie("loginUserId",null);
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
			return SUCCESS;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	/**
	 * Excel瀵煎叆鐢ㄦ埛
	 */
	public void importUser()
	{
		try {

			String errorMsg = userLoginService.readExcel(uploadExcel,
					uploadExcelFileName);

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
	//鍔犺浇褰撳墠鐧诲綍鐢ㄦ埛鎵�鍦ㄦ満鏋勪笅鐨勯攢鍞憳 涓氬姟鍛樺垪琛�
	//select a.* from user_login a, user_role_mapping c	where a.user_login_id = c.user_login_id	and c.role_id like 'SALE_%';
/*	public void loadSalersData(){
		JSONObject result=new JSONObject();
		String shopId = request.getParameter("shopId");
		String customerId = request.getParameter("customerId");
		response.setHeader("Content-Type", "application/json");
		try{
			//String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += "and a.ENABLED = '1' and a.USER_LOGIN_ID in " +
							"(select DISTINCT m.USER_LOGIN_ID from user_role_mapping m,role_data_permission n " +
							"where m.ROLE_ID = n.ROLE_ID and n.PERMISSION_TYPE = '"+Contents.ROLE_DATA_PERMISSION_PARTY+"'	and n.permission_value in (" +
							userPartyIds + ")";
					if(!"".equals(customerId) && customerId != null){
						conditions += " and n.permission_value = (select ci.PARTY_ID from customer_info ci where ci.customer_id = '"+customerId+"'))";
					}else{
						conditions += ")";
					}
				}
				else
				{
					conditions += " and 1=2 ";
				}
			}
			else
			{
				conditions += " and 1=1 ";
			}
			
			List<UserLogin> list2=userLoginService.selectSalerListData(conditions,0,null);
			JSONArray bussinessers = new JSONArray();
			for (UserLogin userLogin : list2) {
				JSONObject item=new JSONObject();
				item.put("id", userLogin.getUserLoginId());
				item.put("text", userLogin.getUserName());
				bussinessers.add(item);
			}
			result.put("bussinessers", bussinessers);
			
			List<UserLogin> list=userLoginService.selectSalerListData(conditions,1,shopId);
			JSONArray salers = new JSONArray();
			for (UserLogin userLogin : list) {
				JSONObject item=new JSONObject();
				item.put("id", userLogin.getUserLoginId());
				item.put("text", userLogin.getUserName());
				salers.add(item);
			}
			result.put("salers", salers);
			
			List<UserLogin> list3=userLoginService.selectSalerListData(conditions,2,null);
			JSONArray supervisors = new JSONArray();
			for (UserLogin userLogin : list3) {
				JSONObject item=new JSONObject();
				item.put("id", userLogin.getUserLoginId());
				item.put("text", userLogin.getUserName());
				supervisors.add(item);
			}
			result.put("supervisors", supervisors);
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}*/
	public void loadSalersData(){
		JSONObject result=new JSONObject();
		String shopId = request.getParameter("shopId");
		String customerId = request.getParameter("customerId");
		String partyId = request.getParameter("partyId");
		response.setHeader("Content-Type", "application/json");
		System.out.println(customerId+"--------------------11111111111111111");
		try{
			//String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
			//String searchBus=request.getParameter("searchBus");
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += "and a.ENABLED = '1'  AND n.`PERMISSION_TYPE`=  '"+Contents.ROLE_DATA_PERMISSION_PARTY+"'	AND a.PARTY_ID in (" +
							userPartyIds + ")";
					if(!"".equals(customerId) && customerId != null){
						conditions += " and n.permission_value = (select ci.PARTY_ID from customer_info ci where ci.customer_id = '"+customerId+"')";
					}
					/*if(!"".equals(searchBus )&& searchBus!=null){
						conditions +=" AND a.`USER_NAME` LIKE CONCAT ('%','"+searchBus+"','%')";
					}*/
				}
				else
				{
					conditions += " and 1=2 ";
				}
			}
			else
			{
//				conditions += " and 1=1 ";
				conditions +="and a.ENABLED = '1'  AND n.`PERMISSION_TYPE`=  '"+Contents.ROLE_DATA_PERMISSION_PARTY+"'	AND a.PARTY_ID = " +
							partyId + "";
			}
			
			System.out.println("==========1==============="+conditions+"================1===============");
			List<UserLogin> list2=userLoginService.selectSalerListData(conditions,0,null);
			JSONArray bussinessers = new JSONArray();
			for (UserLogin userLogin : list2) {
				JSONObject item=new JSONObject();
				item.put("id", userLogin.getUserLoginId());
				item.put("text", userLogin.getUserName());
				bussinessers.add(item);
			}
			result.put("bussinessers", bussinessers);
			
			List<UserLogin> list=userLoginService.selectSalerListData(conditions,1,shopId);
			JSONArray salers = new JSONArray();
			for (UserLogin userLogin : list) {
				JSONObject item=new JSONObject();
				item.put("id", userLogin.getUserLoginId());
				item.put("text", userLogin.getUserName());
				salers.add(item);
			}
			result.put("salers", salers);
			
			List<UserLogin> list3=userLoginService.selectSalerListData(conditions,2,null);
			JSONArray supervisors = new JSONArray();
			for (UserLogin userLogin : list3) {
				JSONObject item=new JSONObject();
				item.put("id", userLogin.getUserLoginId());
				item.put("text", userLogin.getUserName());
				supervisors.add(item);
			}
			result.put("supervisors", supervisors);
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void SalersData(){
		JSONObject result=new JSONObject();
		String shopId = request.getParameter("shopId");
		//String customerId = request.getParameter("customerId");
		String partyId = request.getParameter("partyId");
		response.setHeader("Content-Type", "application/json");
		try{
			//String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += "and a.ENABLED = '1'  AND n.`PERMISSION_TYPE`=  '"+Contents.ROLE_DATA_PERMISSION_PARTY+"'	AND a.PARTY_ID in (" +
							userPartyIds + ")";
				/*	if(!"".equals(customerId) && customerId != null){
						conditions += " and n.permission_value = (select ci.PARTY_ID from customer_info ci where ci.customer_id = '"+customerId+"')";
						
					}*/
				}
				
				else
				{
					conditions += " and 1=2 ";
				}
			}
			else
			{
//				conditions += " and 1=1 ";
				conditions +="and a.ENABLED = '1'  AND n.`PERMISSION_TYPE`=  '"+Contents.ROLE_DATA_PERMISSION_PARTY+"'	AND a.PARTY_ID = " +
						partyId + "";
			}
			String searchBus=request.getParameter("searchBus");
			System.out.println("searchBus"+"============"+request.getParameter("searchBus"));
			if(!"".equals(searchBus )&& searchBus!=null){
				conditions +=" AND a.`USER_NAME` LIKE CONCAT ('%','"+searchBus+"','%')";
			}
			
			String searchSup=request.getParameter("searchSup");
			if(!"".equals(searchSup )&& searchSup!=null){
				conditions +=" AND a.`USER_NAME` LIKE CONCAT ('%','"+searchSup+"','%')";
			}
			String searchSal=request.getParameter("searchSal");
			if(!"".equals(searchSal )&& searchSal!=null){
				conditions +=" AND a.`USER_NAME` LIKE CONCAT ('%','"+searchSal+"','%')";
			}
			
			System.out.println("==============2============"+conditions+"=================2==============");
			
			List<UserLogin> list2=userLoginService.selectSalerList(conditions,0,shopId);
			JSONArray bussinessers = new JSONArray();
			for (UserLogin userLogin : list2) {
				JSONObject item=new JSONObject();
				item.put("id", userLogin.getUserLoginId());
				item.put("text", userLogin.getUserName());
				bussinessers.add(item);
			}
			result.put("bussinessers", bussinessers);
			
			List<UserLogin> list=userLoginService.selectSalerList(conditions,1,shopId);
			JSONArray salers = new JSONArray();
			for (UserLogin userLogin : list) {
				JSONObject item=new JSONObject();
				item.put("id", userLogin.getUserLoginId());
				item.put("text", userLogin.getUserName());
				salers.add(item);
			}
			result.put("salers", salers);
			
			List<UserLogin> list3=userLoginService.selectSalerList(conditions,2,shopId);
			JSONArray supervisors = new JSONArray();
			for (UserLogin userLogin : list3) {
				JSONObject item=new JSONObject();
				item.put("id", userLogin.getUserLoginId());
				item.put("text", userLogin.getUserName());
				supervisors.add(item);
			}
			result.put("supervisors", supervisors);
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//判断微信号是否存在
	public void selectUserLoginId(){
		
		JSONObject result=new JSONObject();
		try {
			
			String newUserLoginId = request.getParameter("newUserLoginId");
			if(!"".equals(newUserLoginId) && null != newUserLoginId){
				result.accumulate("msg", userLoginService.selectUserLoginId(newUserLoginId)+"");
			}else{
				result.accumulate("msg", 0+"");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
//	//鍔犺浇
//	public void loadBusinessersData(){
//		JSONArray jsonArray = new JSONArray();
//		response.setHeader("Content-Type", "application/json");
//		try{
//			String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
//			List<UserLogin> list=userLoginService.selectBusinesserListData(conditions);
//			
//			for (UserLogin userLogin : list) {
//				JSONObject item=new JSONObject();
//				item.put("id", userLogin.getUserLoginId());
//				item.put("text", userLogin.getUserName());
//				jsonArray.add(item);
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			log.error(e.getMessage(),e);
//		}
//		WebPageUtil.writeBack(jsonArray.toString());
//	}
	
	/**
	 * 鏌ヨ 鐢ㄦ埛
	 */
//	public void getUserList(){
//		JSONArray result = new JSONArray();
//		response.setHeader("Content-Type", "application/json");
//		try {
//			request.setCharacterEncoding("utf-8");
//			String shopId = request.getParameter("shopId");
//			List<UserLogin> plist = userLoginService.getUserLoginList(shopId);
//			result = JSONArray.fromObject(plist);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error(e.getMessage(),e);
//		}
//		WebPageUtil.writeBack(result.toString());
//	}
	
	
	
	
	
	
	
	public void loadPartyListByUser(){
		
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try {
			request.setCharacterEncoding("utf-8");
			String countryId = request.getParameter("countryId");
			List<UserLogin> partyList = userLoginService.selectParty(countryId);
			result = JSONArray.fromObject(partyList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
		
	
	public void passwordExpired(){
		String msg = "{\"success\":\"true\",\"msg\":\"NO\"}";
		response.setHeader("Content-Type", "application/json");
		try {
			request.setCharacterEncoding("utf-8");
			String  userLoginId=WebPageUtil.getLoginedUserId();
			
			
			String conditions = " and a.USER_LOGIN_ID='"+userLoginId+"'";
			List<UserLogin> ulList = userLoginService.selectUserLogin(conditions);
			
			if(null!=ulList && ulList.size()>0)
			{
				UserLogin ul = ulList.get(0);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
				Date start = df.parse(ul.getUpdatePassword());
		        Date end = df.parse(df.format(new Date()));
		        System.out.println("###start:==" + start);
		        System.out.println("###end:==" + end);

		        getMonth(start, end);
		        System.out.println("###getMonth():=" + getMonth(start, end));
		        
				  if( getMonth(start, end)>=3){
				   System.out.println(getText("login.error.password"));
					msg = "{\"success\":\"true\",\"msg\":\""+getText("login.error.password")+"\"}";

				  }
			}

				
		} catch (Exception e) {
			e.printStackTrace();
		}
			WebPageUtil.writeBack(msg);

	}
	
	
	public static int getMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);

        int year = endCalendar.get(Calendar.YEAR)
                - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);

        if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }
	
	public void selectUserLevel(){
		JSONObject result = new JSONObject();
		try {
			List<UserLevel> list = userLoginService.selectUserLevel();
			String rows = JSONArray.fromObject(list).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
			WebPageUtil.writeBack(result.toString());
	}
	
	//添加门店时，根据国家添加加载用户对应关系
	public void loadShopSalesDataByConditons(){
		JSONObject result=new JSONObject();
		String shopId = request.getParameter("shopId");
		String customerId = request.getParameter("customerId");
		String partyId = request.getParameter("partyId");
		response.setHeader("Content-Type", "application/json");
		try{
			//String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
			//String searchBus=request.getParameter("searchBus");
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += "and a.ENABLED = '1'  AND n.`PERMISSION_TYPE`=  '"+Contents.ROLE_DATA_PERMISSION_PARTY+"'	AND a.PARTY_ID in (" +
							userPartyIds + ")";
					if(!"".equals(customerId) && customerId != null){
						conditions += " and n.permission_value = (select ci.PARTY_ID from customer_info ci where ci.customer_id = '"+customerId+"')";
					}
					/*if(!"".equals(searchBus )&& searchBus!=null){
						conditions +=" AND a.`USER_NAME` LIKE CONCAT ('%','"+searchBus+"','%')";
					}*/
				}
				else
				{
					conditions += " and 1=2 ";
				}
			}
			else
			{
//				conditions += " and 1=1 ";
				conditions +="and a.ENABLED = '1'  AND n.`PERMISSION_TYPE`=  '"+Contents.ROLE_DATA_PERMISSION_PARTY+"'	AND a.PARTY_ID = " +
							partyId + "";
			}
			
			System.out.println("==========1==============="+conditions+"================1===============");
			List<UserLogin> list2=userLoginService.selectSalerListDataBycondition(conditions,0,null);
			JSONArray bussinessers = new JSONArray();
			for (UserLogin userLogin : list2) {
				JSONObject item=new JSONObject();
				item.put("id", userLogin.getUserLoginId());
				item.put("text", userLogin.getUserName());
				bussinessers.add(item);
			}
			result.put("bussinessers", bussinessers);
			
			List<UserLogin> list=userLoginService.selectSalerListDataBycondition(conditions,1,shopId);
			JSONArray salers = new JSONArray();
			for (UserLogin userLogin : list) {
				JSONObject item=new JSONObject();
				item.put("id", userLogin.getUserLoginId());
				item.put("text", userLogin.getUserName());
				salers.add(item);
			}
			result.put("salers", salers);
			
			List<UserLogin> list3=userLoginService.selectSalerListDataBycondition(conditions,2,null);
			JSONArray supervisors = new JSONArray();
			for (UserLogin userLogin : list3) {
				JSONObject item=new JSONObject();
				item.put("id", userLogin.getUserLoginId());
				item.put("text", userLogin.getUserName());
				supervisors.add(item);
			}
			result.put("supervisors", supervisors);
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
}
