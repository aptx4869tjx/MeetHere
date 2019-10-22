package com.tjx.MeetHere.service;

import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.model.VenueModel;

public interface VenueService {
    VenueModel createVenue(String venueName, String description, String site, Double price, Byte[] timeSlots) throws BusinessException;
}
