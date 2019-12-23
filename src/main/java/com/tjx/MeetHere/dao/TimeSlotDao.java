package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TimeSlotDao extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByVenueId(Long venueId);

    @Modifying
    @Transactional
    void deleteByVenueId(Long venueId);

    boolean existsByVenueIdAndTimeSlot(Long venueId,Byte timeSlot);
}
