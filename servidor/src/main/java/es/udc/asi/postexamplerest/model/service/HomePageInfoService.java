package es.udc.asi.postexamplerest.model.service;

import es.udc.asi.postexamplerest.model.domain.HomePageInfo;
import es.udc.asi.postexamplerest.model.repository.HomePageInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class HomePageInfoService {

  @Autowired
  private HomePageInfoDao homePageInfoDao;

  public HomePageInfo getHomePageInfo() {
    return homePageInfoDao.find();
  }

  @Transactional(readOnly = false)  // Necesitamos una transacción para las operaciones de escritura
  public void updateHomePageInfo(HomePageInfo homePageInfo) {
    homePageInfoDao.update(homePageInfo);
  }
}
