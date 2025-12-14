package com.example.carRental.models;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

    private String name;
    private String lastname;

    private LocalDate birthday;

    private Integer drivingExperience;

    private String email;

    private String phone;

    private List<Booking> bookings;

    public Customer(String name, String lastname, LocalDate birthday, Integer drivingExperience, String email, String phone) {
        this.name = name;
        this.lastname = lastname;
        this.birthday = birthday;
        this.drivingExperience = drivingExperience;
        this.email = email;
        this.phone = phone;
    }

    protected Customer() {
    }

    @Column(nullable = false)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Column(nullable = false)
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    @Column(nullable = false)
    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }

    @Column(nullable = false)
    public Integer getDrivingExperience() {return drivingExperience;}

    public void setDrivingExperience(Integer drivingExperience) {
        this.drivingExperience = drivingExperience;
    }

    @Column(nullable = false)
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Column(nullable = false)
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @OneToMany(mappedBy = "customer")
    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}
