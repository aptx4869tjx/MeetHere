package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueDao extends JpaRepository<Venue,Long> {
    Venue findByVenueId(Long venueId);
    boolean existsByVenueId(Long venueId);
    List<Venue> findAll();
}
