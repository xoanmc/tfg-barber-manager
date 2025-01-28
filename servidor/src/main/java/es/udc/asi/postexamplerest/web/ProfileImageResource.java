package es.udc.asi.postexamplerest.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/images/profile")
public class ProfileImageResource {

    @Value("${properties.upload.path}")
    private String uploadBasePath;

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getProfileImage(@PathVariable String fileName) {

        String uploadDir = uploadBasePath + "/profile-images/";
        try {
            File file = new File(uploadDir + fileName);
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Resource resource = new UrlResource(file.toURI());
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}


