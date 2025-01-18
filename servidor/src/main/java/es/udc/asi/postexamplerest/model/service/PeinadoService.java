package es.udc.asi.postexamplerest.model.service;

import es.udc.asi.postexamplerest.model.domain.EstructuraFacial;
import es.udc.asi.postexamplerest.model.domain.Peinado;
import es.udc.asi.postexamplerest.model.repository.PeinadoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PeinadoService {

    @Autowired
    private PeinadoDao peinadoDao;

    @Value("${properties.upload.path}")
    private String uploadBasePath;

    @Value("${properties.clientHost}")
    private String clientHost;

    public List<Peinado> obtenerTodosLosPeinados() {
        return peinadoDao.findAll();
    }

    public List<Peinado> obtenerPeinadosEnTendencia() {
        return peinadoDao.findByTendenciaTrue();
    }

    public void guardarPeinado(String nombre, String descripcionTendencias, String estructurasFaciales, MultipartFile imagen) {
        try {
            // Convertir estructuras faciales a lista
            List<EstructuraFacial> listaEstructuras = Arrays.stream(estructurasFaciales.split(","))
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .map(EstructuraFacial::valueOf)
                    .collect(Collectors.toList());

            // Guardar la imagen en el disco
            String urlImagen = guardarImagenPeinado(imagen);

            // Crear y guardar el objeto Peinado
            Peinado nuevoPeinado = new Peinado();
            nuevoPeinado.setNombre(nombre);
            nuevoPeinado.setDescripcionTendencias(descripcionTendencias);
            nuevoPeinado.setEstructurasFaciales(listaEstructuras);
            nuevoPeinado.setUrlImagen(urlImagen);

            peinadoDao.save(nuevoPeinado);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Valor inválido en estructuras faciales: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el peinado", e);
        }
    }

    private String guardarImagenPeinado(MultipartFile imagen) throws Exception {
        String uploadDir = uploadBasePath + "/peinados-images/";
        Path rutaDirectorio = Paths.get(uploadDir);

        if (!Files.exists(rutaDirectorio)) {
            Files.createDirectories(rutaDirectorio);
        }

        String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
        Path rutaArchivo = rutaDirectorio.resolve(nombreArchivo);

        Files.copy(imagen.getInputStream(), rutaArchivo);

        // Log para depuración
        System.out.println("Archivo guardado en: " + rutaArchivo.toString());

        return clientHost + "/api/peinados/images/" + nombreArchivo;
    }

    public void toggleTendencia(Long id) {
        Peinado peinado = peinadoDao.findById(id).orElseThrow(() -> new RuntimeException("Peinado no encontrado"));
        peinado.setTendencia(!peinado.isTendencia());
        peinadoDao.save(peinado);
    }
}
