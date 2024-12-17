package es.udc.asi.postexamplerest.model.service;

import es.udc.asi.postexamplerest.model.domain.HomePageInfo;
import es.udc.asi.postexamplerest.model.repository.HomePageInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class HomePageInfoService {

    @Autowired
    private HomePageInfoDao homePageInfoDao;

    private final String UPLOAD_DIR = "D:/TFG/tfg-barber/tfg/servidor/uploads/";

    public HomePageInfo getHomePageInfo() {
        HomePageInfo info = homePageInfoDao.find();

        if (info.getImagen() != null && !info.getImagen().isEmpty()) {
            // URL absoluta al endpoint REST
            String imageUrl = "http://localhost:8080/api/images/homepage/" + info.getImagen();
            info.setImagen(imageUrl);
        }

        return info;
    }

    @Transactional(readOnly = false)
    public void updateHomePageInfo(HomePageInfo homePageInfo) {
        // Método de sobrecarga para aceptar solo HomePageInfo sin imagen
        homePageInfoDao.update(homePageInfo);
    }

    @Transactional(readOnly = false)
    public void updateHomePageInfo(HomePageInfo updatedInfo, MultipartFile imagen) {
        try {
            // Recupera la instancia existente desde la base de datos
            HomePageInfo existingInfo = homePageInfoDao.find();

            if (existingInfo == null) {
                throw new RuntimeException("No existe información de la página de inicio.");
            }

            // Actualiza los campos que se reciben
            existingInfo.setNombre(updatedInfo.getNombre());
            existingInfo.setDescripcion(updatedInfo.getDescripcion());

            // Si se proporciona una nueva imagen, guárdala y actualiza el nombre del archivo
            if (imagen != null && !imagen.isEmpty()) {
                String fileName = saveHomePageImage(imagen);
                existingInfo.setImagen(fileName);
            }

            // Guarda la información actualizada
            homePageInfoDao.update(existingInfo);

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen de portada", e);
        }
    }

    public String saveHomePageImage(MultipartFile imagen) throws IOException {
        String uploadDir = UPLOAD_DIR + "/homepage-images/";

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
        File destinationFile = new File(uploadDir + fileName);

        imagen.transferTo(destinationFile);

        // Verificar si la imagen se guarda correctamente
        System.out.println("Imagen guardada en: " + destinationFile.getAbsolutePath());

        return fileName; // Retorna solo el nombre del archivo
    }
}
