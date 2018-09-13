package com.sao.repository;

import com.sao.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户权限仓库
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}

