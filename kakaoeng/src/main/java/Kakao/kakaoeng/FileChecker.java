package Kakao.kakaoeng;

import java.io.File;

public class FileChecker {
	public static boolean isFileExist(String file){
		File f = new File(FileSaver.getWebContentFolder()+file);
		if(f.isFile())
			return true;
		else
			return false;
	}
}
