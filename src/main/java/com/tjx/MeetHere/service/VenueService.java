package com.tjx.MeetHere.service;

import com.tjx.MeetHere.controller.viewObject.VenueVO;
import com.tjx.MeetHere.dataObject.Venue;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.model.VenueModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface VenueService {
    VenueModel createVenue(Long userId, String venueName, String description, String site, BigDecimal price, Byte[] timeSlots, String imgUrl) throws BusinessException;

    VenueVO getVenueVO(Long venueId, LocalDate date) throws BusinessException;

    List<Venue> getAllVenues();

    boolean updateVenueInfo(Long venueId, Venue venue, String imgUrl) throws BusinessException;
}
