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
    private Long Id;

    private String Name ;
    private String Phone;
    private String Password;
    private String userName; 

    public Admin() {
    }

    public Admin(Long Id, String Name, String Phone, String Password, String userName) {
        this.Id = Id;
        this.Name = Name;
        this.Phone = Phone;
        this.Password = Password;
        this.userName = userName;
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
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

    public String getuserName() {
        return this.userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }

    public Admin Id(Long Id) {
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

    public Admin userName(String userName) {
        setuserName(userName);
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
        return Id == admin.Id && Objects.equals(Name, admin.Name) && Objects.equals(Phone, admin.Phone) && Objects.equals(Password, admin.Password) && Objects.equals(userName, admin.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, Name, Phone, Password, userName);
    }

    @Override
    public String toString() {
        return "{" +
            " Id='" + getId() + "'" +
            ", Name='" + getName() + "'" +
            ", Phone='" + getPhone() + "'" +
            ", Password='" + getPassword() + "'" +
            ", userName='" + getuserName() + "'" +
            "}";
    }
}
