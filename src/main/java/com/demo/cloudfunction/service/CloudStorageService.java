package com.demo.cloudfunction.service;

import java.io.IOException;
import java.util.List;

public interface CloudStorageService {

//	 public void uploadObject(String projectId, String bucketName, String objectName, String filePath) throws IOException ;

	public void downloadObject(String bucketName, String objectName, String destFilePath);

	public void uploadObject(String bucketName, String objectName, String filePath) throws IOException;

	public void deleteObject(String bucketName, String objectName);
//	 public void deleteObjects(String bucketName, List<String> objectNames);
	
	public void uploadObjectFromMemory(String bucketName, String objectName, String contents) throws IOException;

}
