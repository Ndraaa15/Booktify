package id.my.cupcakez.booktify.domain.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import id.my.cupcakez.booktify.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

@Service
public class FileService implements IFileService {
    private AmazonS3 s3Client;

    @Autowired
    public FileService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Value("${minio.s3.bucket}")
    private String bucketName;

    public String uploadFile(String keyName, MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        PutObjectRequest request = new PutObjectRequest(bucketName, keyName, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        s3Client.putObject(request);

        return s3Client.getUrl(bucketName, keyName).toString();
    }
}