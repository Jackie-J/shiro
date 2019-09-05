package vip.jackie.shiro.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import vip.jackie.shiro.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    
    @Query(value = "SELECT PERMISSION FROM shiro_permission p INNER JOIN shiro_role_permission rp ON p.ID = rp.PERMISSION_ID WHERE EXISTS ( SELECT ur.ROLE_ID FROM shiro_user u INNER JOIN shiro_user_role ur ON u.ID = ur.USER_ID WHERE u.USER_NAME = ? AND rp.ROLE_ID = ur.ROLE_ID )", nativeQuery = true)
    public Set<String> findUserPermissions(String userName);

}
