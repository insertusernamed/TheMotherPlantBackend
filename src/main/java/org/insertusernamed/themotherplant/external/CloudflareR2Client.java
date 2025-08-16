package org.insertusernamed.themotherplant.external;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudflareR2Client {

	private final S3Client s3Client;

	@Value("${cloudflare.r2.bucket-name}")
	private String bucketName;

	@Value("${cloudflare.r2.public-url}")
	private String publicUrl;

	public String uploadFile(MultipartFile file) throws IOException {
		String uniqueFileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(uniqueFileName)
				.contentType(file.getContentType())
				.build();

		s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

		return publicUrl + "/" + uniqueFileName;
	}

	public void deleteFile(String fileName) {
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
				.bucket(bucketName)
				.key(fileName)
				.build();

		s3Client.deleteObject(deleteObjectRequest);
	}

	public String extractFileNameFromUrl(String imageUrl) {
		if (imageUrl == null || !imageUrl.startsWith(publicUrl)) {
			return null;
		}
		return imageUrl.substring(publicUrl.length() + 1);
	}
}
