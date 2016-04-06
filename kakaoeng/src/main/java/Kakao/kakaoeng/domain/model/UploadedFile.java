package Kakao.kakaoeng.domain.model;

import java.io.File;

import org.apache.log4j.Logger;

public class UploadedFile {
	String DBPath;
	Logger logger = Logger.getLogger(UploadedFile.class);
	
	public UploadedFile(String DBPath){
		this.DBPath = DBPath;
	}
	
	public String getDBPath(){
		return DBPath;
	}
	
	public String getFileName(){
		if(DBPath == null)
			return "";
		if(DBPath.equals(""))
			return "";
		if(DBPath.equals("dummy"))
			return "dummy";
		int index = DBPath.indexOf("/");
		if(index==-1)
			return "";
		String fileName = DBPath.substring(index+1);
		index = fileName.lastIndexOf('.');
		String ext = "";
		if(index!=-1)
			ext = fileName.substring(index+1);
		index = fileName.lastIndexOf('_');
		String realFileName = fileName.substring(0, index);
		return realFileName+'.'+ext;
	}
	public void removeFileFromStorage(String basePath){
		File f = new File(basePath + System.getProperty("file.separator")+DBPath);
		logger.info("delete file - size : " + f.length() + ", name : "  + DBPath);
		f.delete();
	}
}
