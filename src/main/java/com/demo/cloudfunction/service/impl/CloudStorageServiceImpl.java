package com.demo.cloudfunction.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.demo.cloudfunction.service.CloudStorageService;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CloudStorageServiceImpl implements CloudStorageService {
	private static final Logger logger = LogManager.getLogger(CloudStorageServiceImpl.class);

	@Autowired
	Storage storage;

	@Override
	public void downloadObject(String bucketName, String objectName, String destFilePath) {
		// TODO Auto-generated method stub
		
		Blob blob = storage.get(BlobId.of(bucketName, objectName));
		
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss");
        ZonedDateTime zonedDateTime = ZonedDateTime.of(now, ZoneId.of("Asia/Taipei"));
		
		if (!Objects.isNull(blob)) {
			logger.info(blob.toString());
			blob.downloadTo(Paths.get(destFilePath + "\\" + zonedDateTime.format(DTF) + "_" + objectName));
			logger.info("Downloaded object " + objectName + " from bucket name " + bucketName + " to " + destFilePath);
		}
	}

	/**
	 * Upload file to GCS
	 * 
	 * @param bucketName The ID of your GCS bucket
	 * @param objectName The ID of your GCS object
	 * @param filePath   The path to your file to upload
	 */
	@Override
	public void uploadObject(String bucketName, String objectName, String filePath) throws IOException {

		BlobId blobId = BlobId.of(bucketName, objectName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

		// Optional: set a generation-match precondition to avoid potential race
		// conditions and data corruptions. The request returns a 412 error if the
		// preconditions are not met.
		Storage.BlobTargetOption precondition;
		if (storage.get(bucketName, objectName) != null) {
			this.deleteObject(bucketName, objectName);
		}

		precondition = Storage.BlobTargetOption.doesNotExist();
		storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)), precondition);

		logger.info("File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
	}

	@Override
	public void deleteObject(String bucketName, String objectName) {

		Blob blob = storage.get(bucketName, objectName);
		if (blob == null) {
			System.out.println("The object " + objectName + " wasn't found in " + bucketName);
			return;
		}

		// Optional: set a generation-match precondition to avoid potential race
		// conditions and data corruptions. The request to upload returns a 412 error if
		// the object's generation number does not match your precondition.
		Storage.BlobSourceOption precondition = Storage.BlobSourceOption.generationMatch(blob.getGeneration());

		storage.delete(bucketName, objectName, precondition);
		logger.info("Object " + objectName + " was deleted from " + bucketName);
	}
	
	@Override
	public void uploadObjectFromMemory(String bucketName, String objectName, String contents) throws IOException {
		    // The ID of your GCS bucket
		    // String bucketName = "your-unique-bucket-name";

		    // The ID of your GCS object
		    // String objectName = "your-object-name";

		    // The string of contents you wish to upload
		    // String contents = "Hello world!";

		    BlobId blobId = BlobId.of(bucketName, objectName);
		    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		    byte[] content = contents.getBytes(StandardCharsets.UTF_8);
		    storage.createFrom(blobInfo, new ByteArrayInputStream(content));

		    System.out.println(
		        "Object "
		            + objectName
		            + " uploaded to bucket "
		            + bucketName
		            + " with contents "
		            + contents);
		  }

//	@Override
//	public void deleteObjects(String projectId, String bucketName, List<String> objectNames) {
//
//		// TO DO
//	}

}
