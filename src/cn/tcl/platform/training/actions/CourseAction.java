package cn.tcl.platform.training.actions;


import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.exchange.vo.Exchange;
import cn.tcl.platform.message.service.MessageService;
import cn.tcl.platform.message.vo.Message;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.product.vo.Product;
import cn.tcl.platform.training.service.CourseService;
import cn.tcl.platform.training.vo.CourseType;
import cn.tcl.platform.training.vo.TrainingCourse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("all")
public class CourseAction extends BaseAction {
	@Autowired
	@Qualifier("courseService")
	private CourseService courseService;
	
	@Autowired(required=false)
	@Qualifier("messageService")
	private MessageService messageService;
		
	public String loadCreateCoursePage()
	{
		try
		{
			return SUCCESS;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String loadCourseListPage()
	{
		try
		{
			return SUCCESS;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	
	public String loadExaminationPage()
	{
		try
		{
			return SUCCESS;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String loadMessageTagPage()
	{
		try
		{
			return SUCCESS;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String loadSendMultiMessagesPage()
	{
		try
		{
			return SUCCESS;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String loadStatisticsSummaryPage()
	{
		try
		{
			return SUCCESS;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public void CreateCourse()
	{
		JSONObject result=new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try{
			request.setCharacterEncoding("UTF-8");
			int typeId=Integer.parseInt(request.getParameter("typeId"));
			int levelOneTypeId = Integer.parseInt(request.getParameter("levelOneTypeId"));
			
			int levelTwoTypeId = Integer.parseInt(request.getParameter("levelTwoTypeId"));
			int levelThreeTypeId = Integer.parseInt(request.getParameter("levelThreeTypeId"));
			char messageType=request.getParameter("messageType").charAt(0);
			String title=request.getParameter("courseTitle");
			String coverImgUrl=request.getParameter("coverImgUrl").toLowerCase();
			coverImgUrl=coverImgUrl.replace("/var/www/topsale/topsale/", "/topsale/");
//			coverImgUrl=coverImgUrl.replace("\\","/").replace("D:/apache-tomcat-7.0.75/webapps/toms/cover/", "http://localhost:8080/toms/cover/");
//			String attamentUrl=request.getParameter("attamentUrl");
//			if(attamentUrl!=""){
//				attamentUrl=attamentUrl.replace("/var/www/topsale/topsale/", "http://obctop.tcl.com.cn/topsale/");
//			}
			
			String summary=request.getParameter("courseSummary");
			String content=request.getParameter("courseContent"); 
			
			String partyId=request.getParameter("partyId");
			String regionId=request.getParameter("regionId");
			String countryId=request.getParameter("countryId");
			content=content.replace("/var/www/topsale/topsale/","/topsale/");
			TrainingCourse course=new TrainingCourse();
			course.setTypeId(typeId);
			course.setLevelOneTypeId(levelOneTypeId);
			course.setLevelTwoTypeId(levelTwoTypeId);
			course.setLevelThreeTypeId(levelThreeTypeId);
			
			course.setMessageType(messageType);
			course.setCourseTitle(java.net.URLDecoder.decode(title,"UTF-8"));
			course.setCourseSummary(java.net.URLDecoder.decode(summary,"UTF-8"));
			course.setCourseContent(content);
			course.setCoverImgUrl(coverImgUrl.replaceAll(" ", ""));
//			course.setAttachUrl(attamentUrl);
			course.setCreateDate(new Date());
			course.setCreateBy(WebPageUtil.getLoginedUserId());
			course.setCreaterRoleId(WebPageUtil.getLoginedUser().getRoleId());
			course.setCreaterPartyId(WebPageUtil.getLoginedUser().getPartyId());
			course.setCorrespondingPartyId(partyId);
			course.setCorrespondingRegionId(regionId);
			course.setCorrespondingCountryId(countryId);
			course.setState("0");
			result.accumulate("msg","success");	
			courseService.InsertCourse(course); 
			
			
			String allUserStr = request.getParameter("allUserStr");
//			String roleType = request.getParameter("roleTypeName");
			String roleId =UUID.randomUUID().toString().replace("-", "").toLowerCase();
//			roleId = roleType + "_" + roleId;
			
			
			//创建消息
			Message msg = new Message();
			
			msg.setMsgType("1");
			msg.setMsgTitle(java.net.URLDecoder.decode(title,"UTF-8"));
			msg.setMsgComment(content);
			msg.setMsgTitleUrl("<a href=reminderCoursedetail.jsp?courseId="+course.getCourseId()+">");
			msg.setCreateBy(WebPageUtil.getLoginedUserId());
			msg.setCreateTime(new Date());
			msg.setCreateCountryId(countryId);
			msg.setCreatePartyId(WebPageUtil.getLoginedUser().getPartyId());
			msg.setMsgRoleId(roleId);
			msg.setMsgSummary(java.net.URLDecoder.decode(summary,"UTF-8"));
			msg.setMsgCoverUrl(coverImgUrl.replaceAll(" ", ""));
			msg.setCourseId(String.valueOf(course.getCourseId()));
			messageService.insertMessage(msg,allUserStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage(), e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", true);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//上传图片
/*	public String uploadPicture(){
		//判断enctype属性是否为multipart/form-data
		boolean isMultipart=ServletFileUpload.isMultipartContent(request);
		//Create a factory for disk-based file items
		DiskFileItemFactory factory=new DiskFileItemFactory();
		//当上传文件太大时,因为虚拟机能使用的内存是有限的,所以此时要通过临时
		//文件来实现上传文件的保存,此方法是设置是否使用临时文件的临界值
		factory.setSizeThreshold(3*1024*1024);
		//与上一个结合使用,设置临时文件的路径(绝对路径)
		factory.setRepository(new File(TempFilePath));
		//Create a new file upload handler
		ServletFileUpload upload=new ServletFileUpload(factory);
		//设置上传内容的大小限制
		upload.setSizeMax(2*1024*1024);
		//parse the request
		try {
			List<FileItem> items=upload.parseRequest(request);
			@SuppressWarnings("rawtypes")
			Iterator iter=items.iterator();
			while(iter.hasNext())
			{
				FileItem item=(FileItem)iter.next();
				if(!item.isFormField())
				{
					//如果是文件字段
					String fieldName=item.getFieldName();
					String fileName=item.getName();
					String contentType=item.getContentType();
					boolean isInMemory=item.isInMemory();
					long sizeInBytes=item.getSize();
					//File uploadedFile=new File("");
					try {
						//item.write(uploadedFile);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	*/
//	public void loadCourseListData()
//	{
//		JSONObject result=new JSONObject();
//		response.setHeader("Content-Type", "application/json");
//		try{
//			int typeId=Integer.parseInt(request.getParameter("typeId"));
//			String keyword=request.getParameter("keyword");
//			TrainingCourse course=new TrainingCourse();
//			course.setTypeId(typeId);
//			List<TrainingCourse> tcLst=courseService.SelectCourseList(course, keyword);
//			JSONArray jsonArray=JSONArray.fromObject(tcLst);
//			result.accumulate("rows",jsonArray.toString());
//			result.accumulate("total",tcLst.size());
//			result.accumulate("success",true);
//		}catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			log.error(e.getMessage(), e);
//			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
//			result.accumulate("success", true);
//			result.accumulate("msg", msg);
//		}
//		WebPageUtil.writeBack(result.toString());
//	}
//	
	public void loadCourseListData(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try {
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
//			int typeId=Integer.parseInt(request.getParameter("typeId"));
			TrainingCourse course=new TrainingCourse();
//			course.setTypeId(typeId);
//			String keyword = request.getParameter("keyword");
//			String selectQuertyPartyId = request.getParameter("selectQuertyPartyId");
//			keyword = "".equals(keyword)?null:keyword;
			String pageStr=request.getParameter("page");
			int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
			String rowStr=request.getParameter("rows");
			int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
			int start = (page-1)*limit;
			
			String searchStr = "1=1 ";
			String partyId = request.getParameter("partyId");
			String typeId = request.getParameter("typeId");
			String typeIdSubId = request.getParameter("typeIdSubId");
			String typeIdSubtoId = request.getParameter("typeIdSubtoId");
			String MessagetypeId = request.getParameter("MessagetypeId");
			String searchKey = request.getParameter("searchKey");
			
			if(searchKey!=null && !searchKey.equals("")){
				searchStr+="and t.course_title like CONCAT('%','"+searchKey+"','%')";
			}
			
			String userLoginId = WebPageUtil.getLoginedUser().getUserLoginId();
			String partyIdsByUserId = WebPageUtil.loadPartyIdsByUserId();
			boolean isBranch = WebPageUtil.isBranchRole();
			String conditions = "";
			
			if(!WebPageUtil.isHQRole())
			{
				if(null!=userLoginId && !"".equals(userLoginId)){
					if(isBranch){
//						conditions += "	tmm.user_login_id ='"+userLoginId+"'";
						conditions += "	 tc.corresponding_country_id IN (select distinct tt.country_id from party tt where tt.party_id in ("+partyIdsByUserId+"))";
					}else{
						conditions += "	tmm.user_login_id ='"+userLoginId+"'";
//						conditions += "tc.corresponding_country_id IN (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+"))";
					}
				}else{
					conditions += " and 1=2 ";
				}
			}
			else
			{	
				if(WebPageUtil.isHAdmin()){
					conditions += "  1=1";
				}else{
					conditions += "	 tc.corresponding_country_id IN (select distinct tt.party_id from party tt where tt.party_id in ("+partyIdsByUserId+"))";
					conditions +="	 and p.PARTY_ID=tc.creater_party_id union all select DISTINCT tc.course_id , tc.type_id , tc.corresponding_party_id , tc.course_title , tc.course_summary , tc.coverimg_url , tc.course_content , tc.message_type , DATE_FORMAT(tc.create_date,'%Y-%m-%d') " +
							"as create_date, tc.create_by , tc.creater_role_id , tc.creater_party_id , tc.last_modify_user, tc.last_modify_date, tc.corresponding_region_id,tc.corresponding_country_id, tc.state, tc.levelone_type_id as levelOneTypeId, tc.leveltwo_type_id as levelTwoTypeId, " +
							"tc.levelthree_type_id as levelThreeTypeId, p.PARTY_NAME as partyName, tmt.msg_role_id as msgRoleId from training_course tc,party p,t_msg_text tmt,t_msg_mapping tmm WHERE 1=1 and tmt.course_id=tc.course_id and tmt.msg_role_id=tmm.msg_role_id and tc.corresponding_region_id=-1 ";
//					conditions += (WebPageUtil.isStringNullAvaliable(selectQuertyPartyId) ? "AND t.`COUNTRY_ID` = "+selectQuertyPartyId : "and 1=1" );
					conditions += "	 and tmm.user_login_id ='"+userLoginId+"'";
				}				
			}
			
			Map<String, Object> map = courseService.SelectCourseList(start, limit,searchStr,partyId,typeId,typeIdSubId,typeIdSubtoId,MessagetypeId,order, sort, conditions);
			int total = (Integer)map.get("total");
			List<TrainingCourse> list = (ArrayList<TrainingCourse>)map.get("rows");
			JSONArray jsonArray = JSONArray.fromObject(list);
			String rows = jsonArray.toString();
			result.accumulate("rows", rows);
			result.accumulate("total", total);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void editCourse(){
		JSONObject result = new JSONObject();
		String courseId = request.getParameter("editId");
		System.out.println(courseId+"-------------------------------");
		if(courseId!=null && courseId.trim()!=""){
			String messageType = request.getParameter("messageType");
			String coverImgUrl = request.getParameter("coverImgUrl").toLowerCase();
			System.out.println(coverImgUrl+"----------------coverImgUrl");
			System.out.println(coverImgUrl.replace("\\","/")+"----coverImgUrl");
//			String coverImg=coverImgUrl.replace("\\","/").replace("D:/apache-tomcat-7.0.75/webapps/toms/cover/", "http://localhost:8080/toms/cover/");
			String coverImg=coverImgUrl.replace("\\","/").replace("/var/www/topsale/topsale/", "/topsale/");
			System.out.println(coverImg+"-----coverImg");
			String correspondingRegionId = request.getParameter("correspondingRegionId");
			String correspondingCountryId = request.getParameter("correspondingCountryId");
			String courseTitle = request.getParameter("courseTitle");
//			new String(request.getParameter("a").getBytes("iso8859-1"), "UTF-8");
//			System.out.println(courseTitle+"9999999999999999999999999999");
			String courseSummary = request.getParameter("courseSummary");
			String courseContent = request.getParameter("courseContents");
		
			String state = request.getParameter("state");
			int levelOneTypeId=Integer.parseInt(request.getParameter("levelOneTypeId"));
			int levelTwoTypeId = Integer.parseInt(request.getParameter("levelTwoTypeId"));
			int levelThreeTypeId = Integer.parseInt(request.getParameter("levelThreeTypeId"));
			int typeId = Integer.parseInt(request.getParameter("levelThreeTypeId"));
			
			String allUserStr = request.getParameter("param");
			String userParam = request.getParameter("userParam");
			System.out.println(allUserStr+"-----------------allUserStr");
			System.out.println(userParam+"-----------------userParam");
			String roleType = request.getParameter("roleTypeName");
			String roleId =UUID.randomUUID().toString().replace("-", "").toLowerCase();
			roleId = roleType + "_" + roleId;
			System.out.println(roleId+"----------"+roleType+"correspondingCountryId"+correspondingCountryId);
			
			try {
//				   Message msgRoleId = messageService.getMsgRoleId(courseId);
//				messageService.deleteMessageByUser(msgRoleId);
				
				TrainingCourse tc = courseService.selectTrainCourseById(courseId);
				tc.setMessageType(messageType.charAt(0));
				tc.setCoverImgUrl(coverImg.replaceAll(" ", ""));
				tc.setCorrespondingRegionId(correspondingRegionId);
				tc.setCorrespondingCountryId(correspondingCountryId);
				tc.setCourseTitle(java.net.URLDecoder.decode(courseTitle,"UTF-8"));			
				tc.setCourseSummary(java.net.URLDecoder.decode(courseSummary,"UTF-8"));
				tc.setCourseContent(courseContent);
				tc.setCourseId(Integer.parseInt(courseId));
				tc.setState(state);
				tc.setLevelOneTypeId(levelOneTypeId);
				tc.setLevelTwoTypeId(levelTwoTypeId);
				tc.setLevelThreeTypeId(levelThreeTypeId);
				tc.setTypeId(typeId);
				
				String msgRoleIdByCourseId = messageService.getMsgRoleIdByCourseId(courseId);	
					Message msg = new Message();
					msg.setMsgRoleId(msgRoleIdByCourseId);
					msg.setCourseId(courseId);
					msg.setMsgTitle(java.net.URLDecoder.decode(courseTitle,"UTF-8"));
					msg.setCreateCountryId(correspondingCountryId);
					msg.setMsgSummary(java.net.URLDecoder.decode(courseSummary,"UTF-8"));
					msg.setCreateTime(new Date());
					msg.setMsgCoverUrl(coverImg.replaceAll(" ", ""));
					msg.setMsgComment(courseContent);
					courseService.updateTrainCourse(tc,msg,allUserStr,userParam);

				result.accumulate("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(),e);
				String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
				result.accumulate("success", false);
				result.accumulate("msg", msg);
			}
		}	
		WebPageUtil.writeBack(result.toString());
	}
	
	/*public void saveCourse(){
		JSONObject result = new JSONObject();
		
		try {
			String messageType = request.getParameter("messageType");
			String coverImgUrl = request.getParameter("coverImgUrl");
			String correspondingPartyId = request.getParameter("correspondingPartyId");
			String courseTitle = request.getParameter("courseTitle");
			String courseSummary = request.getParameter("courseSummary");
			String courseContent = request.getParameter("courseContent");
			
			TrainingCourse tc =new TrainingCourse();
			tc.setMessageType(messageType.charAt(0));
			tc.setCoverImgUrl(coverImgUrl);
			tc.setCorrespondingPartyId(correspondingPartyId);
			tc.setCourseTitle(courseTitle);			
			tc.setCourseSummary(courseSummary);
			tc.setCourseContent(courseContent);
			courseService.saveTrainCourse(tc);
			result.accumulate("success", true);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}*/
	
	//删除课件同时删除阅读记录
	public void deleteCourse(){
		JSONObject result = new JSONObject();
		String courseId = request.getParameter("courseId");
		System.out.println(request.getParameter("courseId")+"--------------------------------");
		try {
			TrainingCourse tc =new TrainingCourse();
			tc.setCourseId(Integer.parseInt(courseId));
			
			Message msg =new Message();
			msg.setCourseId(courseId);
			msg.setMsgTitleUrl("<a href=../train/commentRemove.jsp?courseId="+courseId+">");
			int readNum = messageService.getReadByCourseId(courseId);
			if(WebPageUtil.isHAdmin()){
				messageService.deleteReadCourse(courseId);
				courseService.deleteTrainCourse(tc,msg);
				result.accumulate("success", true);
			}else{
				if(readNum>0){
					result.accumulate("num", true);
				}else{
					courseService.deleteTrainCourse(tc,msg);
					result.accumulate("success", true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectRegion(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type","application/json");
		String partyId = request.getParameter("partyId");
		try {
			List<Party> list = courseService.selectRegion(partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectAllParty(){
		JSONArray  result = new JSONArray();
		response.setHeader("Content-Type","application/json");
		try {
			List<Party> list = courseService.selectAllParty();
			result = JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
//	public void selectAllCountry(){
//	JSONArray  result = new JSONArray();
//	response.setHeader("Content-Type","application/json");
//	String partyId = request.getParameter("countryId");
//	try {
//		List<Party> list = courseService.selectAllCountry(partyId);
//		result= JSONArray.fromObject(list);
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
//	WebPageUtil.writeBack(result.toString());
//	}
	
	public void selectCountry(){
		JSONArray  result = new JSONArray();
		response.setHeader("Content-Type","application/json");
		String partyId = request.getParameter("countryId");
		try {
			List<Party> list = courseService.selectAllCountry(partyId);
			result= JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void getLevelOneTypeId(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type","application/json");
		try {
			List<CourseType> list = courseService.getLevelOneTypeId();
			result = JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void getLevelTwoOrthreeTypeId(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type","application/json");
		String typeId = request.getParameter("typeId");
		try {
			List<CourseType> list = courseService.getLevelTwoOrthreeTypeId(typeId);
			result=JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	public void getLevelthreeTypeId(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type","application/json");
		String typeId = request.getParameter("typeId");
		try {
			List<CourseType> list = courseService.getLevelthreeTypeId(typeId);
			result=JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void getPartyList(){
		JSONObject result = new JSONObject();
		String userLoginId = request.getParameter("userLoginId");
		try {
			List<Party> list;
			if(WebPageUtil.isHAdmin()){
				 list = courseService.getPartyList(userLoginId);
			}else{
				list = courseService.getBranchPartyList(userLoginId);
			}
			String rows = JSONArray.fromObject(list).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//分公司用户显示的国家列表
	public void getBranchPartyList(){
		JSONObject result = new JSONObject();
		String userLoginId = request.getParameter("userLoginId");
		try {
			List<Party> list = courseService.getBranchPartyList(userLoginId);
			String rows = JSONArray.fromObject(list).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
}

