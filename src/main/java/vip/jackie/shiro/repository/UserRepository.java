package vip.jackie.shiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vip.jackie.shiro.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUserName(String userName);

}
