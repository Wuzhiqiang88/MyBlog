package com.sao.repository;

import com.sao.domain.User;
import org.apache.logging.log4j.message.LoggerNameAwareMessage;
import org.hibernate.engine.jdbc.connections.internal.UserSuppliedConnectionProviderImpl;
import org.hibernate.validator.constraints.EAN;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * 用户仓库.
 */
public interface UserRepository extends CrudRepository<User,Long>{

    User findUserById(Long id);

}
