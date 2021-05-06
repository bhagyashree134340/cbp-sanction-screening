package com.example.demo.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadHelper {
	
	public final String UPLOAD_DIR = "C:\\Users\\raneb\\OneDrive\\Desktop\\cbp\\cbp-sanction-screening-main\\UPLOADS";
	public String fileName;
	

	public boolean uploadFile(MultipartFile multipartFile) {
		
		boolean f = false;
		
		try {
			
			InputStream is = multipartFile.getInputStream();
			
			byte data[] = new byte[is.available()];
			
			is.read(data);
			
			
			//write 
			fileName = multipartFile.getOriginalFilename();
			FileOutputStream fos = new FileOutputStream(UPLOAD_DIR + File.separator + fileName);
			
			fos.write(data);
			
			fos.flush();
			fos.close();
			f = true;
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return f;
	}
	
	
}

