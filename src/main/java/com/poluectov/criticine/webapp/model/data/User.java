package com.poluectov.criticine.webapp.model.data;

public class User {
    int id;
    String name;
    String email;
    String passwordHash;
    boolean isVerified;
    int roleId;
    UserRole role;
    int status;

    public User(int id, String name, String email, String passwordHash, boolean isVerified, int roleId, int status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isVerified = isVerified;
        this.roleId = roleId;
        this.status = status;
    }

    public User(String name, String email, String passwordHash, boolean isVerified, int roleId, int status){
        this.id = -1;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isVerified = isVerified;
        this.roleId = roleId;
        this.status = status;
    }

    public User(String name, String email, String passwordHash) {
        this.id = -1;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isVerified = false;
        this.roleId = 1;
        this.status = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
