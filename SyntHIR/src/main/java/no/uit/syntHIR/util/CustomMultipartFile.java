package no.uit.syntHIR.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class CustomMultipartFile implements MultipartFile{

	private final byte[] fileContent;
	//private String fileName;
	//private File file;
	
	public CustomMultipartFile(byte[] fileData, String destinationPath, String name) {
	    this.fileContent = fileData;
	    //this.fileName = name;
	    //file = new File(destinationPath + fileName);

	}
	
	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getOriginalFilename() {
		return null;
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return fileContent == null || fileContent.length == 0;
	}

	@Override
	public long getSize() {
		return fileContent.length;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return fileContent;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(fileContent);
	}

	@Override
	public void transferTo(File destination) throws IOException, IllegalStateException {
		try(FileOutputStream fos = new FileOutputStream(destination)) {
            fos.write(fileContent);
        }
		
	}

}
