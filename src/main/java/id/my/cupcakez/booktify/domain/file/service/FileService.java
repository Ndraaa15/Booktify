package id.my.cupcakez.booktify.domain.file.service;

import id.my.cupcakez.booktify.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

@Service
public class FileService implements IFileService {
    @Value("${file.upload-dir}")
    private String storagePath;

    @Override
    public String uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String customFileName = LocalDate.now() + "_" + fileName;

        File dir = new File(storagePath+customFileName);

        if(dir.exists()){
            throw new CustomException("File already exists", HttpStatus.CONFLICT);
        }

        Path path = Path.of(storagePath+customFileName);
        try{
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return customFileName;
        } catch (Exception e){
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
