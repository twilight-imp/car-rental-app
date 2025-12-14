package com.example.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.Objects;

@Relation(collectionRelation = "customers", itemRelation = "customer")
public class CustomerResponse extends RepresentationModel<CustomerResponse> {

    private  String id;
    private  String name;
    private  String lastname;
    private  LocalDate birthday;
    private  Integer drivingExperience;
    private  String email;
    private  String phone;

    public CustomerResponse(String id, String name, String lastname, LocalDate birthday, Integer drivingExperience, String email, String phone) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.birthday = birthday;
        this.drivingExperience = drivingExperience;
        this.email = email;
        this.phone = phone;
    }

    protected CustomerResponse() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setDrivingExperience(Integer drivingExperience) {
        this.drivingExperience = drivingExperience;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }




    public String getId() { return id; }
    public String getName() { return name; }
    public String getLastname() { return lastname; }

    @JsonFormat(pattern = "dd-MM-yyyy")
    public LocalDate getBirthday() { return birthday; }
    public Integer getDrivingExperience() { return drivingExperience; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CustomerResponse that = (CustomerResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(lastname, that.lastname) &&
                Objects.equals(birthday, that.birthday) &&
                Objects.equals(drivingExperience, that.drivingExperience) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, lastname, birthday, drivingExperience, email, phone);
    }
}