package com.laciesbox.repo;


import com.laciesbox.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByNameContainingIgnoreCase(String name);
}
