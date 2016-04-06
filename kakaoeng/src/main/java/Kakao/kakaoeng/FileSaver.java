package Kakao.kakaoeng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.Part;

import org.apache.log4j.Logger;

public class FileSaver {

	private static Logger logger = Logger.getLogger(FileSaver.class);
	private static String resourceFolder = "resource"+"/";
	private static String WebContentFolder = null;

	public static String getWebContentFolder() {
		return WebContentFolder;
	}

	@SuppressWarnings("serial")
	public static class FileLimitExceed extends RuntimeException{

		public FileLimitExceed() {
			super();
			// TODO Auto-generated constructor stub
		}

		public FileLimitExceed(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
			// TODO Auto-generated constructor stub
		}

		public FileLimitExceed(String message, Throwable cause) {
			super(message, cause);
			// TODO Auto-generated constructor stub
		}

		public FileLimitExceed(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}

		public FileLimitExceed(Throwable cause) {
			super(cause);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	public static void setWebContentFolder(String path) {
		WebContentFolder = path;
	}

	protected static String[] splitFileName(String fileName) {
		int pathIndex = fileName.lastIndexOf("/");
		if(pathIndex != -1)
			fileName = fileName.substring(pathIndex+1);
		int index = fileName.lastIndexOf('.');
		String fileNameWithoutExtension = null;
		String fileExtension = null;
		if(index != -1){
			fileNameWithoutExtension = fileName.substring(0, index);
			fileExtension = fileName.substring(index + 1);
		}else{
			fileNameWithoutExtension = fileName;
			fileExtension = "noExt";
		}
		String[] result = { fileNameWithoutExtension, fileExtension };
		return result;
	}

	public static String makeFilePath(String dbPath) {
		String result = WebContentFolder + dbPath;
		return result;
	}

	public static String makeLinkPath(String fileName, long timeStamp) {
		String[] splited = splitFileName(fileName);
		String result = resourceFolder + splited[0] + "_" + timeStamp + "." + splited[1];
		return result;
	}
	
	public static String DBFilePath(String fileName, String folderName, long timeStamp){
		String[] splited = splitFileName(fileName);
		String result = folderName + "/" + splited[0] + "_" + timeStamp + "." + splited[1];
		return result;
	}

	public static String fileSave(Part part, String submiteName, String folderName, long timeStamp, boolean bTimePath, int limit) throws IOException {

		logger.info("fileSave called");
		int fileSize = (int) part.getSize();
		if (!(fileSize > 0))
			return null;
		logger.info("fileSize : " + fileSize);
		if(limit != -1 &&fileSize > limit){
			throw new FileLimitExceed();
		}
		
		String fileName = null;
		String DBName = FileSaver.DBFilePath(submiteName, folderName, timeStamp);
		fileName = FileSaver.makeFilePath(DBName);

		logger.info("file DBName : " + DBName);
		logger.info("fileName : " + fileName);
		InputStream is = null;
		FileOutputStream fos = null;

		try {

			byte[] byteArray = new byte[fileSize];
			is = part.getInputStream();
			logger.info("open input stream");
			is.read(byteArray);
			File file = new File(fileName);
			fos = new FileOutputStream(file);
			fos.write(byteArray);
			logger.info("write file");
		} 
		catch(IOException e){
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		finally {
			try {
				if (is != null){
					is.close();
					logger.info("close file input stream");
				}
			} catch (IOException e) {
			}
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
			}
		}
		logger.info("File Save : " + fileName);
		return DBName;
	}
}
