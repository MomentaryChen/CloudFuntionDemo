package com.demo.cloudfunction.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Configuration
@PropertySource("classpath:gcs.properties")
public class GCSConfig {

	@Value("${projectId}")
	String projectId;

//	@Value("${credentials}")
//	String credentials;

	@Value("${bucketName}")
	public String bucketName;

	@Bean
	public Storage getGCSStorage() throws FileNotFoundException, IOException {

		return StorageOptions.newBuilder().setProjectId(projectId)
//				.setCredentials(GoogleCredentials.fromStream(new FileInputStream(credentials)))
				.build().getService();
	}

}
