package test.com.pgis.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;

public class FileManager {
	
	public static String getTestResourcePath() {
		return "/media/Files/premiumGIS/Software/Programms/"
				+ "busApps/busWeb.data/src/test/resources/";

	}
	public static boolean isFileAlreadyExist(String fullFileName){
		return (new File(fullFileName)).exists();
	}
	public static String getFileData(String fullFileName) {
		String source = null;
		try {
			File file = new File(fullFileName);
			InputStream in = new FileInputStream(file);
			byte[] b  = new byte[(int)file.length()];
			int len = b.length;
			int total = 0;

			while (total < len) {
			  int result = in.read(b, total, len - total);
			  if (result == -1) {
			    break;
			  }
			  total += result;
			}
			source = new String( b , Charset.forName("UTF-8"));


		} catch (Exception e) {
			e.printStackTrace();
		}
		return source;
	}

	public static void createFile(String fullFileName, String data) {
		try {
			System.out.println(fullFileName);
			File f = new File(fullFileName);
			f.createNewFile();

			OutputStreamWriter isw = new OutputStreamWriter(
					new FileOutputStream(fullFileName), "UTF-8");
			isw.write(data);
			isw.close();
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean createFolder(String folder){
		try {
			 boolean success = (
					  new File(folder)).mkdir();
					 return success;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
