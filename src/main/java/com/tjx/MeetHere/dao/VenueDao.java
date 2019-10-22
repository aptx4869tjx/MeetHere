package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueDao extends JpaRepository<Venue,Long> {
    Venue findByVenueId(Long venueId);
}
