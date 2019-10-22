package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotDao extends JpaRepository<TimeSlot,Long> {
}
