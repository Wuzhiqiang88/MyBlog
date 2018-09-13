package com.sao.service;

import com.sao.domain.Authority;

/**
 * Authority 服务接口.
 */
public interface AuthorityService {


    /**
     * 根据id获取 Authority
     * @param id
     * @return
     */
    Authority getAuthorityById(Long id);
}
