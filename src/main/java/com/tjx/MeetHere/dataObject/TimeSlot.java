package com.tjx.MeetHere.dataObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Byte timeSlot;
    private Long venueId;

    public TimeSlot(Long venueId, Byte timeSlot) {
        this.timeSlot = timeSlot;
        this.venueId = venueId;
    }

    public TimeSlot() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(Byte timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }
}
