package upp_udd.project.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import upp_udd.project.services.UploadService;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UploadController {

    private UploadService uploadService;

    @PostMapping("/uploadFile")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println(uploadService.upload(file));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get")
    public ResponseEntity get() {
        return ResponseEntity.ok("lud");
    }

}
