package com.local.common.utilities;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class GCSBucketManager {
	
	
	
	static String credentialsPath = "";
	static String bucketName = "";
	static String projectId = "";

	public static void main(String[] args) throws Exception {

		try {
			listAllFilesWithSize(projectId, bucketName, credentialsPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void listAllFilesWithSize(String projectId, String bucketName, String credentialsPath)
			throws IOException {
		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
		Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build()
				.getService();

		Iterable<Blob> blobs = storage.list(bucketName).iterateAll();
		for (Blob blob : blobs) {
			System.out.println("File: " + blob.getName() + ", Size: " + blob.getSize());
		}
	}

}