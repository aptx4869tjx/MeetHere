package com.tjx.MeetHere.service.Impl;

import com.tjx.MeetHere.controller.viewObject.VenueVO;
import com.tjx.MeetHere.dao.OccupiedTimeSlotDao;
import com.tjx.MeetHere.dao.TimeSlotDao;
import com.tjx.MeetHere.dao.VenueDao;
import com.tjx.MeetHere.dataObject.OccupiedTimeSlot;
import com.tjx.MeetHere.dataObject.TimeSlot;
import com.tjx.MeetHere.dataObject.Venue;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.error.ErrorEm;
import com.tjx.MeetHere.service.VenueService;
import com.tjx.MeetHere.service.model.VenueModel;
import com.tjx.MeetHere.tool.ValidationResult;
import com.tjx.MeetHere.tool.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VenueServiceImpl implements VenueService {
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private VenueDao venueDao;
    @Autowired
    private TimeSlotDao timeSlotDao;
    @Autowired
    private OccupiedTimeSlotDao occupiedTimeSlotDao;


    @Override
    @Transactional
    public VenueModel createVenue(Long userId, String venueName, String description, String site, BigDecimal price, Byte[] timeSlots, String imgUrl) throws BusinessException {
        if (userId == null) {
            throw new BusinessException(ErrorEm.USER_NOT_LOGIN);
        }
        VenueModel venueModel = new VenueModel();
        venueModel.setUserId(userId);
        venueModel.setVenueName(venueName);
        venueModel.setDescription(description);
        venueModel.setSite(site);
        venueModel.setPrice(price);
        venueModel.setTimeSlots(timeSlots);
        venueModel.setImgUrl(imgUrl);
        ValidationResult result = validator.validate(venueModel);
        if (result.isHasError()) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR, result.getErrorMessage());
        }
        venueModel.setCreateTime(LocalDateTime.now());
        Venue venue = getVenueFromVenueModel(venueModel);
        venueDao.save(venue);
        venueModel.setVenueId(venue.getVenueId());
        List<TimeSlot> timeSlots1 = getTimeSlotsFromVenueModel(venueModel);
        for (TimeSlot t : timeSlots1
        ) {
            timeSlotDao.save(t);
        }
        return venueModel;
    }

    @Override
    public VenueVO getVenueVO(Long venueId, LocalDate date) throws BusinessException {
        if (venueId == null || date == null) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR);
        }
        VenueVO venueVO = new VenueVO();
        Venue venue = venueDao.findByVenueId(venueId);
        if (venue == null) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR);
        }
        BeanUtils.copyProperties(venue, venueVO);
        venueVO.setDate(date);
        List<TimeSlot> timeSlots = timeSlotDao.findByVenueId(venueId);
        List<Byte> tss = new ArrayList<>();
        for (TimeSlot ts : timeSlots
        ) {
            tss.add(ts.getTimeSlot());
        }
        venueVO.setTimeSlots(tss.toArray(new Byte[tss.size()]));
        List<OccupiedTimeSlot> occupiedTimeSlots = occupiedTimeSlotDao.findByVenueIdAndDate(venueId, date);
        List<Byte> otss = new ArrayList<>();
        for (OccupiedTimeSlot ots : occupiedTimeSlots
        ) {
            otss.add(ots.getOccupiedTimeSlot());
        }
        venueVO.setOccupiedTimeSlots((Byte[]) otss.toArray(new Byte[otss.size()]));
        return venueVO;
    }

    @Override
    public List<Venue> getAllVenues() {
        List<Venue> venues;
        venues = venueDao.findAll();
//        List<VenueVO> venueVOS = new ArrayList<>();
        return venues;
    }

    @Override
    public boolean updateVenueInfo(Long venueId, Venue venue, String imgUrl) throws BusinessException{
        String venueName = venue.getVenueName();
        String site = venue.getSite();
        String description = venue.getDescription();
        Double price = venue.getPrice();
        if (venueName == null || venueName.equals("")) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR, "场地名不能为空");
        }
        if (site == null || site.equals("")) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR, "场地地址不能为空");
        }
        if (description == null || description.equals("")) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR, "场地描述不能为空");
        }
        if (price == null) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR, "场地价格不能为空");
        }
        int result = 0;
        if (imgUrl == null) {
            result = venueDao.updateVenueInfo(venueId, venueName, site, description, price);
        } else {
            result = venueDao.updateVenueInfoWithImage(venueId, venueName, site, description, price, imgUrl);
        }
        return result != 0;
    }

    private Venue getVenueFromVenueModel(VenueModel venueModel) {
        if (venueModel == null) {
            return null;
        }
        Venue venue = new Venue();
        BeanUtils.copyProperties(venueModel, venue);
        venue.setPrice(venueModel.getPrice().doubleValue());
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
