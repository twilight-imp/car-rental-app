package com.example.carRental.repository.impl;

import com.example.carRental.models.Booking;

import com.example.carRental.models.Status;
import com.example.carRental.repository.BookingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingRepositoryImpl extends BaseRepositoryImpl<Booking> implements BookingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    protected BookingRepositoryImpl() {
        super(Booking.class);
    }

    @Override
    public List<Booking> findByCarId(String carId) {
        TypedQuery<Booking> query = entityManager.createQuery(
                "select b from Booking b " +
                        "join b.car c " +
                        "where c.id = :carId and b.isDeleted = false", Booking.class);
        return query.setParameter("carId", carId)
                .getResultList();
    }

    @Override
    public List<Booking> findByCustomerId(String customerId) {
        TypedQuery<Booking> query = entityManager.createQuery(
                "select b from Booking b " +
                        "join b.customer c " +
                        "where c.id = :customerId and b.isDeleted = false", Booking.class);
        return query.setParameter("customerId", customerId)
                .getResultList();
    }


    @Override
    public List<Booking> findActiveBookings() {
        TypedQuery<Booking> query = entityManager.createQuery(
                "SELECT b FROM Booking b " +
                        "WHERE b.status IN (:status1, :status2) " +
                        "AND b.isDeleted = false", Booking.class);
        return query.setParameter("status1", Status.CREATED)
                .setParameter("status2", Status.CONFIRMED)
                .getResultList();
    }
}
