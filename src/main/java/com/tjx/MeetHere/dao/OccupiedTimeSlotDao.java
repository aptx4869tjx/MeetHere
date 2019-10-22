package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.OccupiedTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OccupiedTimeSlotDao extends JpaRepository<OccupiedTimeSlot, Long> {
    List<OccupiedTimeSlot> findByVenueId(Long venueId);
    List<OccupiedTimeSlot> findByVenueIdAndDate(Long venueId, LocalDate date);
}
