package id.my.cupcakez.booktify.domain.file.controller;


import id.my.cupcakez.booktify.domain.file.service.IFileService;
import id.my.cupcakez.booktify.response.ResponseWrapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "file", description = "File API")
@SecurityRequirement(name = "Bearer Authentication")
public class FileController {
    private IFileService fileService;

    @Autowired
    public FileController(IFileService fileService){
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseWrapper<String>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String url = fileService.uploadFile(file.getOriginalFilename(), file);

        ResponseWrapper<String> response = ResponseWrapper.<String>builder()
                .message("file uploaded successfully")
                .data(url)
                .build();

        return ResponseEntity.created(URI.create("/api/v1/file/upload")).body(response);
    }
}
