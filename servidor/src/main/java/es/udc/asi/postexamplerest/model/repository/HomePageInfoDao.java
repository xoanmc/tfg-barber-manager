package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.HomePageInfo;

public interface HomePageInfoDao {
  HomePageInfo find();  // Obtener la información de la pantalla de inicio
  void update(HomePageInfo homePageInfo);  // Actualizar la información de la pantalla de inicio
}
