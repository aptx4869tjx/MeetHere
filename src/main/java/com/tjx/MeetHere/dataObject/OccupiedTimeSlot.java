package com.tjx.MeetHere.dataObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class OccupiedTimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Byte occupiedTimeSlot;
    private Long venueId;
    private LocalDate date;
    private Long orderId;

    public OccupiedTimeSlot( Long venueId, LocalDate date, Long orderId,Byte occupiedTimeSlot) {
        this.occupiedTimeSlot = occupiedTimeSlot;
        this.venueId = venueId;
        this.date = date;
        this.orderId = orderId;
    }

    public OccupiedTimeSlot() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getOccupiedTimeSlot() {
        return occupiedTimeSlot;
    }

    public void setOccupiedTimeSlot(Byte occupiedTimeSlot) {
        this.occupiedTimeSlot = occupiedTimeSlot;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }
}
