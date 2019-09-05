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
@Table(name = "shiro_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String role;
    private String description;
    private Boolean available;

    public Role() {
    }

    public Role(Integer id) {
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

    @Column(name = "ROLE", length = 64)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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