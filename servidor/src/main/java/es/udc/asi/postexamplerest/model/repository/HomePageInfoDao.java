package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.HomePageInfo;

public interface HomePageInfoDao {
  HomePageInfo find();
  void update(HomePageInfo homePageInfo);
}
