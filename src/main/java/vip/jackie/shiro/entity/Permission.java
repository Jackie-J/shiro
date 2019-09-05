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
@Table(name = "shiro_permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String permission;
    private String description;
    private Boolean available;

    public Permission() {
    }

    public Permission(Integer id) {
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

    @Column(name = "PERMISSION", length = 64)
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Column(name = "DESCRIPTION", length = 64)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "AVAILABLE")
    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

}