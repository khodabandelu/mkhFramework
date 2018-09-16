package org.mkh.frm.utility;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Utility {

	public static String getReportPath() {
		return Utility.getRealApplicationPath() + "/WEB-INF/classes/report";
	}

	public static String applicationPath;

	public static String getRealApplicationPath() {
		if (applicationPath != null)
			return applicationPath;

		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		applicationPath = requestAttributes.getRequest().getSession().getServletContext().getRealPath("");
		return applicationPath;
	}

	public static String getJsonByObject(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(mapper.writeValueAsString(object));
			return mapper.writeValueAsString(object);

		} catch (JsonGenerationException e) {
		} catch (JsonMappingException e) {
		} catch (IOException e) {
		}
		return "Error ...";
	}
	
	public static Object getObjectByJson(String json,Class objectClass) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			// Convert JSON string to Object
			return mapper.readValue(json, objectClass);
			
		} catch (JsonGenerationException e) {
		} catch (JsonMappingException e) {
		} catch (IOException e) {
		}
		return "Error ...";
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	
	public static String getMultiPartFileExtention(MultipartFile file) {
		int index = file.getOriginalFilename().lastIndexOf(".");
		return file.getOriginalFilename().substring(index + 1);
	}

	public static Blob convertMultiPartToBlob(MultipartFile file) throws IOException {
		byte[] bFile = file.getBytes();
		try {
			return new SerialBlob(bFile);
		} catch (SerialException e) {
		} catch (SQLException e) {
		}
		return null;
	}

	public static Blob convertByteArrayToBlob(byte[] bFile) {
		try {
			return new SerialBlob(bFile);
		} catch (SerialException e) {
		} catch (SQLException e) {
		}
		return null;
	}

	public static long ipToLong(InetAddress ip) {
		byte[] octets = ip.getAddress();
		long result = 0;
		for (byte octet : octets) {
			result <<= 8;
			result |= octet & 0xff;
		}
		return result;
	}

	public static List<String> getAFilesNameInDirectoy(String path) {
		File folder = new File(path);
		List<String> result = new ArrayList<String>();
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile() && !file.isHidden())
				result.add(file.getName());
		}
		return result;
	}

	public static List<Integer> convertArrayToList(Integer[] array) {
		List<Integer> lst = new ArrayList<Integer>();
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				lst.add(array[i]);
			}
		}
		return lst;
	}
	public static int test(){
		int result=1;
		try {
			return result;
		} 
		finally{
			result=result+1;
		}
		
	}
}
