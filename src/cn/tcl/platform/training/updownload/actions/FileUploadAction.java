package cn.tcl.platform.training.updownload.actions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Value;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.training.updownload.entity.State;
import cn.tcl.platform.training.updownload.util.FileUploadTools;
import net.sf.json.JSONObject;

public class FileUploadAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	// 声明封装了File上传的FileUploadTools类的实例
    // FileUploadTools类也封装了上传的属性及get和set方法
    private FileUploadTools fileUploadTools = new FileUploadTools();
    
    @Value("${COVER.SAVEDIR}")
	private String CoverSaveDir;
    
    @Value("${ATTACHMENT.SAVEDIR}")
    private String AttachmentSaveDir;
    
    
    @Value("${COEFF.SAVEDIR}")
    private String coeff;
    
    @SuppressWarnings("rawtypes")
	private Map jsonMap = new HashMap();

    @SuppressWarnings("rawtypes")
	public Map getJsonMap() {
        return jsonMap;
    }

    @SuppressWarnings("rawtypes")
	public void setJsonMap(Map jsonMap) {
        this.jsonMap = jsonMap;
    }
    
    public String getCoverSaveDir() {
		return CoverSaveDir;
	}

	public void setCoverSaveDir(String coverSaveDir) {
		CoverSaveDir = coverSaveDir;
	}

	public String getAttachmentSaveDir() {
		return AttachmentSaveDir;
	}

	public void setAttachmentSaveDir(String attachmentSaveDir) {
		AttachmentSaveDir = attachmentSaveDir;
	}

    public FileUploadTools getFileUploadTools() {
        return fileUploadTools;
    }

    public void setFileUploadTools(FileUploadTools fileUploadTools) {
        this.fileUploadTools = fileUploadTools;
    }

    /**
     * 处理文件上传
     * 
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @SuppressWarnings("unchecked")
	public void upload() throws IOException, InterruptedException {
    	JSONObject result=new JSONObject();
		response.setHeader("Content-Type","application/json");
		String flag=ServletActionContext.getRequest().getParameter("flag");
		String targetDir="D:/test/";
		if(flag.equals("cover")){
			targetDir=CoverSaveDir;
		}
		if(flag.equals("attach")){
			targetDir=AttachmentSaveDir;
		}
		if(flag.equals("coeff")){
			targetDir=coeff;
		}
        // 文件上传
        String path=fileUploadTools.beginUpload(targetDir); 
        jsonMap.put("flg", true);
        result.accumulate("path",path);
        WebPageUtil.writeBack(result.toString());
    }
}
