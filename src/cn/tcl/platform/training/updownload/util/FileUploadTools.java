package cn.tcl.platform.training.updownload.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class FileUploadTools {
	private String username;
    private File uploadFile[];// 上传的文件是数组类型
    private String uploadFileFileName[];// 文件名是数组类型
    private String uploadFileContentType[];

    public String beginUpload(String targetDirectory) throws IOException {

        StringBuilder sb=new StringBuilder();
        if (uploadFile != null && uploadFile.length > 0 && uploadFileFileName!=null && uploadFileFileName.length>0) {
            for (int i = 0; i < uploadFile.length; i++) {
            	targetDirectory=targetDirectory+new SimpleDateFormat("yyyyMM")
                .format(new Date()).toString()+"/";
            	 File file=new File(targetDirectory);
            	 if(!file.exists()){
            		      file.mkdir();  		 
            	 }
               // File target = new File(targetDirectory, uploadFileFileName[i]);
                File target = new File(targetDirectory, new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
	                    .format(new Date()).toString() + System.nanoTime() + uploadFileFileName[i].replace(" ", "").toLowerCase());
                FileUtils.copyFile(uploadFile[i], target);
                sb.append(target.getPath()+";");
            }
            sb=sb.deleteCharAt(sb.length()-1);
        }   
        return sb.toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public File[] getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File[] uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String[] getUploadFileFileName() {
        return uploadFileFileName;
    }

    public void setUploadFileFileName(String[] uploadFileFileName) {
        this.uploadFileFileName = uploadFileFileName;
    }

    public String[] getUploadFileContentType() {
        return uploadFileContentType;
    }

    public void setUploadFileContentType(String[] uploadFileContentType) {
        this.uploadFileContentType = uploadFileContentType;
    }
}
