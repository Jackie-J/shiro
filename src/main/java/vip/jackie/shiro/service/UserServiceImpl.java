package vip.jackie.shiro.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vip.jackie.shiro.config.realm.UserService;
import vip.jackie.shiro.entity.User;
import vip.jackie.shiro.repository.PermissionRepository;
import vip.jackie.shiro.repository.RoleRepository;
import vip.jackie.shiro.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User findByUserName(String userName) {
        if (userName == null) {
            return null;
        }
        return userRepository.findByUserName(userName);
    }

    @Override
    public Set<String> findUserRoles(String userName) {
        if (userName == null) {
            return null;
        }
        return roleRepository.findUserRoles(userName);
    }

    @Override
    public Set<String> findUserPermissions(String userName) {
        if (userName == null) {
            return null;
        }
        return permissionRepository.findUserPermissions(userName);
    }

}
