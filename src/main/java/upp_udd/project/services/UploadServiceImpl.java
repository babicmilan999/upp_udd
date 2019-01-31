package upp_udd.project.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import upp_udd.project.exception.UploadException;

@Service
public class UploadServiceImpl implements UploadService{

    @Value("${upload.directory}")
    private String uploadDirectory;

    @Override
    public String upload(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new UploadException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Files.copy(file.getInputStream(), Paths.get(uploadDirectory.concat(fileName)), StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new UploadException(String.format("%s. Could not store file %s. Please try again!", ex.getMessage(), fileName));
        }
    }
}
