package vip.jackie.shiro.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import vip.jackie.shiro.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "SELECT ROLE FROM shiro_user u INNER JOIN shiro_user_role ur ON u.ID = ur.USER_ID INNER JOIN shiro_role r ON ur.ROLE_ID = r.ID WHERE u.USER_NAME = ?", nativeQuery = true)
    public Set<String> findUserRoles(String userName);

}
