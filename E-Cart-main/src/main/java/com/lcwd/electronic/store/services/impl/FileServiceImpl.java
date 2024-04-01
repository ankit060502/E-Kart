package com.lcwd.electronic.store.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework .web.multipart.MultipartFile;

import com.lcwd.electronic.store.exception.ImageBadApiRequest;
import com.lcwd.electronic.store.services.FileService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImpl  implements FileService{

	 
	@Override
	public String uploadImage(MultipartFile file, String path) throws IOException {
		// TODO Auto-generated method stub
		
		String originalFilename = file.getOriginalFilename();
		
		String filename = UUID.randomUUID().toString();
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String filenamewithextension = filename+extension;
		
		String fullpathwithFileName = path+filenamewithextension;
		 log.info("file path name  {} " + fullpathwithFileName );
		
		
		
		if(extension.equalsIgnoreCase(".png")
				|| extension.equalsIgnoreCase(".jpg")|| extension.equalsIgnoreCase(".jpeg")) {
			
			log.info("file extension is{]  "+ extension);
			File folder = new File(path);
			if(!folder.exists()) {
				// create the folder
				folder.mkdirs();
			}
			
			// upload 
			
			Files.copy(file.getInputStream(),Paths.get(fullpathwithFileName));
		}else {
			throw new ImageBadApiRequest("File with this " + extension + " is not allowed");
		}
		
		return filenamewithextension;
	}

	@Override
	public InputStream getResources(String path, String name) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		String   fullpath = path + File.separator+ name;
		InputStream inputStream = new FileInputStream(fullpath);
		
		return inputStream;
	}
	

}
