package com.tjx.MeetHere.service;

import com.tjx.MeetHere.controller.viewObject.VenueVO;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.model.VenueModel;

import java.time.LocalDate;
import java.util.List;

public interface VenueService {
    VenueModel createVenue(String venueName, String description, String site, Double price, Byte[] timeSlots) throws BusinessException;
    VenueVO getVenueVO(Long venueId, LocalDate date) throws BusinessException;
}
