package com.tjx.MeetHere.service.Impl;

import com.tjx.MeetHere.dao.TimeSlotDao;
import com.tjx.MeetHere.dao.VenueDao;
import com.tjx.MeetHere.dataObject.TimeSlot;
import com.tjx.MeetHere.dataObject.Venue;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.error.ErrorEm;
import com.tjx.MeetHere.service.VenueService;
import com.tjx.MeetHere.service.model.VenueModel;
import com.tjx.MeetHere.validator.ValidationResult;
import com.tjx.MeetHere.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VenueServiceImpl implements VenueService {
    @Autowired
    ValidatorImpl validator;
    @Autowired
    VenueDao venueDao;
    @Autowired
    TimeSlotDao timeSlotDao;

    @Override
    @Transactional
    public VenueModel createVenue(String venueName, String description, String site, Double price, Byte[] timeSlots) throws BusinessException {
        VenueModel venueModel = new VenueModel();
        venueModel.setVenueName(venueName);
        venueModel.setDescription(description);
        venueModel.setSite(site);
        venueModel.setPrice(price);
        venueModel.setTimeSlots(timeSlots);
        ValidationResult result = validator.validate(venueModel);
        if (result.isHasError()) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR, result.getErrorMessage());
        }
        venueModel.setCreateTime(LocalDateTime.now());
        Venue venue= getVenueFromVenueModel(venueModel);
        venueDao.save(venue);
        venueModel.setVenueId(venue.getVenueId());
        List<TimeSlot> timeSlots1= getTimeSlotsFromVenueModel(venueModel);
        for (TimeSlot t:timeSlots1
             ) {
            timeSlotDao.save(t);
        }
        return venueModel;
    }

    private Venue getVenueFromVenueModel(VenueModel venueModel) {
        if (venueModel == null) {
            return null;
        }
        Venue venue = new Venue();
        BeanUtils.copyProperties(venueModel, venue);
        return venue;
    }

    private List<TimeSlot> getTimeSlotsFromVenueModel(VenueModel venueModel) {
        if (venueModel == null) {
            return null;
        }
        List<TimeSlot> timeSlots = new ArrayList<>();
        Long venueId = venueModel.getVenueId();
        for (Byte timeSlotId : venueModel.getTimeSlots()
        ) {
            TimeSlot timeSlot = new TimeSlot(venueId, timeSlotId);
            timeSlots.add(timeSlot);
        }
        return timeSlots;
    }
}
