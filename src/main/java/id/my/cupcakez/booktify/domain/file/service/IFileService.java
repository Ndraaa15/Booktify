package id.my.cupcakez.booktify.domain.file.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileService {
    String uploadFile(String keyName, MultipartFile file) throws IOException;
}
