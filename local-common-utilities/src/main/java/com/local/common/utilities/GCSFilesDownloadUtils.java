package com.local.common.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public final class GCSFilesDownloadUtils {

	public static void downloadFile(String projectId, String bucketName, String objectName, int chunkSize,
			String credeString, String destinationFilePath) throws FileNotFoundException, IOException {

		System.out.println("#####File download started.....");

		Storage storage = getStorageService(projectId, credeString);

		Blob blob = storage.get(bucketName, objectName);
		if (blob == null) {
			throw new RuntimeException("Blob does not exist.");
		}
		ByteBuffer buffer = ByteBuffer.allocate(chunkSize);
		int totalbytesread = 0;
		int bytesRead;

		try (ReadChannel reader = blob.reader();
				FileOutputStream outputStream = new FileOutputStream(destinationFilePath);) {

			while ((bytesRead = reader.read(buffer)) != -1) {
				buffer.flip();
				totalbytesread += bytesRead;
				System.out.println("READ BYTES : " + totalbytesread);

				outputStream.write(buffer.array());
				buffer.clear();
			}
			outputStream.close();
			reader.close();

		} catch (IOException e) {
			throw new IOException("Error downloading file from Google Cloud Storage.", e);
		}
	}

	private static Storage getStorageService(String projectId, String credeString)
			throws IOException, FileNotFoundException {
		return StorageOptions.newBuilder()
				.setCredentials(GoogleCredentials.fromStream(new FileInputStream(credeString)))
				.setProjectId(projectId)
				.build()
				.getService();
	}

}
