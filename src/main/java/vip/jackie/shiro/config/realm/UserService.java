package vip.jackie.shiro.config.realm;

import java.util.Set;

import vip.jackie.shiro.entity.User;

public interface UserService {

    public User findByUserName(String userName);

    public Set<String> findUserRoles(String userName);

    public Set<String> findUserPermissions(String userName);

}
