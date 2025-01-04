package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.AboutPageInfo;

public interface AboutPageInfoDao {
    AboutPageInfo find();
    void update(AboutPageInfo aboutPageInfo);
}
