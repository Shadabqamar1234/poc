package com.local.common.utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class GCSFileUploader {

	  public static void main(String[] args) throws Exception {
	        String credentialsPath = "";
	        String filePath = "c:/temp/fastexcel-demo.xlsx";
	        String bucketName = "";
	        String objectName = "fastexcel-demo1gb.xlsx";
	        
	        System.out.println("File upload in progress...");

	        
	        InputStream credentialsStream = new FileInputStream(credentialsPath);
	        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
	        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

	        BlobId blobId = BlobId.of(bucketName, objectName);
	        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
	        Storage.BlobWriteOption precondition;
	        if (storage.get(bucketName, objectName) == null) {
	          
	          precondition = Storage.BlobWriteOption.doesNotExist();
	        } else {
	          precondition =
	              Storage.BlobWriteOption.generationMatch(
	                  storage.get(bucketName, objectName).getGeneration());
	        }
	        storage.createFrom(blobInfo, Paths.get(filePath), precondition);
	        System.out.println("File upload in completed!!!");

	        System.out.println(
	            "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
	      }
	}