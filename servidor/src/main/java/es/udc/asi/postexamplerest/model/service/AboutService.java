package es.udc.asi.postexamplerest.model.service;

import es.udc.asi.postexamplerest.model.domain.About;
import es.udc.asi.postexamplerest.model.repository.AboutDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class AboutService {

  @Autowired
  private AboutDao aboutDao;

  public About getAbout(Long id) {
    return aboutDao.findById(id);
  }

  @Transactional(readOnly = false)
  public void updateAbout(About about) {
    aboutDao.update(about);
  }
}
