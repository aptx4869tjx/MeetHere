package com.tjx.MeetHere.controller;

import com.tjx.MeetHere.dataObject.Venue;
import com.tjx.MeetHere.response.CommonReturnType;
import com.tjx.MeetHere.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class VenueController extends BaseController {
    @Autowired
    private VenueService venueService;

    @RequestMapping(method = RequestMethod.GET,value = "/venues")
    public CommonReturnType getAllVenues(){
        List<Venue> venues = venueService.getAllVenues();
        return new CommonReturnType(venues);
    }
}
