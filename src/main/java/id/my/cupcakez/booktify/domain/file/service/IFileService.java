package id.my.cupcakez.booktify.domain.file.service;


import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    String uploadFile(MultipartFile file);
}
