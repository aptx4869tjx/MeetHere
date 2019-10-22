package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeSlotDao extends JpaRepository<TimeSlot,Long> {
    List<TimeSlot> findByVenueId(Long venueId);
}
