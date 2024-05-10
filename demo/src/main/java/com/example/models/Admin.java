package com.example.models;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;

    private String Name ;
    private String Phone;
    private String Password;
    private String UserName;

    public Admin() {
    }

    public Admin(int Id, String Name, String Phone, String Password, String UserName) {
        this.Id = Id;
        this.Name = Name;
        this.Phone = Phone;
        this.Password = Password;
        this.UserName = UserName;
    }

    public int getId() {
        return this.Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhone() {
        return this.Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getUserName() {
        return this.UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public Admin Id(int Id) {
        setId(Id);
        return this;
    }

    public Admin Name(String Name) {
        setName(Name);
        return this;
    }

    public Admin Phone(String Phone) {
        setPhone(Phone);
        return this;
    }

    public Admin Password(String Password) {
        setPassword(Password);
        return this;
    }

    public Admin UserName(String UserName) {
        setUserName(UserName);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Admin)) {
            return false;
        }
        Admin admin = (Admin) o;
        return Id == admin.Id && Objects.equals(Name, admin.Name) && Objects.equals(Phone, admin.Phone) && Objects.equals(Password, admin.Password) && Objects.equals(UserName, admin.UserName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, Name, Phone, Password, UserName);
    }

    @Override
    public String toString() {
        return "{" +
            " Id='" + getId() + "'" +
            ", Name='" + getName() + "'" +
            ", Phone='" + getPhone() + "'" +
            ", Password='" + getPassword() + "'" +
            ", UserName='" + getUserName() + "'" +
            "}";
    }
}
