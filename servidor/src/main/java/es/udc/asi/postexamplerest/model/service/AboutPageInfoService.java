package es.udc.asi.postexamplerest.model.service;

import es.udc.asi.postexamplerest.model.domain.AboutPageInfo;
import es.udc.asi.postexamplerest.model.repository.AboutPageInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class AboutPageInfoService {

    @Autowired
    private AboutPageInfoDao aboutPageInfoDao;

    private final String UPLOAD_DIR = "D:/TFG/tfg-barber/tfg/servidor/uploads/";

    public AboutPageInfo getAboutPageInfo() {
        AboutPageInfo info = aboutPageInfoDao.find();

        if (info == null) {
            info = new AboutPageInfo();
            info.setDescripcion("Aquí va la descripción inicial por defecto de la barbería.");
        }

        if (info.getImagen() != null && !info.getImagen().isEmpty()) {
            String imageUrl = "http://localhost:8080/api/images/about/" + info.getImagen();
            info.setImagen(imageUrl);
        }

        return info;
    }

    @Transactional(readOnly = false)
    public void updateAboutPageInfo(AboutPageInfo updatedInfo, MultipartFile imagen) {
        try {
            AboutPageInfo existingInfo = aboutPageInfoDao.find();
            if (existingInfo == null) {
                existingInfo = new AboutPageInfo();
            }

            existingInfo.setDescripcion(updatedInfo.getDescripcion());

            if (imagen != null && !imagen.isEmpty()) {
                String fileName = saveAboutPageImage(imagen);
                existingInfo.setImagen(fileName);
            }

            aboutPageInfoDao.update(existingInfo);

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen de la sección 'Acerca de'", e);
        }
    }

    private String saveAboutPageImage(MultipartFile imagen) throws IOException {
        String uploadDir = UPLOAD_DIR + "/about-images/";

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
        File destinationFile = new File(uploadDir + fileName);
        imagen.transferTo(destinationFile);

        return fileName;
    }
}

