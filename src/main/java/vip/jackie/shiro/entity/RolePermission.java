package vip.jackie.shiro.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author encoder by he_zp and xiang_hj
 */
@Entity
@Table(name = "shiro_role_permission")
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer roleId;
    private Integer permissionId;

    public RolePermission() {
    }

    public RolePermission(Integer id) {
        this.id = id;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "ROLE_ID", unique = true, nullable = false)
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Column(name = "PERMISSION_ID")
    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

}