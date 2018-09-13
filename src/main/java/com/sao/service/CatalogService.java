package com.sao.service;

import com.sao.domain.Catalog;
import com.sao.domain.User;
import java.util.List;

public interface CatalogService {
    /**
     * 保存Catalog
     * @param catalog
     * @return
     */
    Catalog saveCatalog(Catalog catalog);

    /**
     * 删除Catalog
     * @param id
     * @return
     */
    void removeCatalog(Long id);

    /**
     * 根据id获取Catalog
     * @param id
     * @return
     */
    Catalog getCatalogById(Long id);

    /**
     * 获取Catalog列表
     * @return
     */
    List<Catalog> listCatalogs(User user);
}
