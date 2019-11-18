package com.tjx.MeetHere.controller;

import com.tjx.MeetHere.controller.viewObject.VenueVO;
import com.tjx.MeetHere.dataObject.User;
import com.tjx.MeetHere.dataObject.Venue;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.error.ErrorEm;
import com.tjx.MeetHere.response.CommonReturnType;
import com.tjx.MeetHere.service.OrderService;
import com.tjx.MeetHere.service.PictureService;
import com.tjx.MeetHere.service.VenueService;
import com.tjx.MeetHere.service.model.UserModel;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class VenueController extends BaseController {
    @Autowired
    private VenueService venueService;
    @Autowired
    OrderService orderService;
    @Autowired
    PictureService pictureService;
    @Autowired
    HttpServletRequest httpServletRequest;

    @RequestMapping(method = RequestMethod.GET, value = "/venues")
    public CommonReturnType getAllVenues() {
        List<Venue> venues = venueService.getAllVenues();
        return new CommonReturnType(venues);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/venue/{venueId}")
    public CommonReturnType getVenueByVenueId(@PathVariable("venueId") Long venueId) throws BusinessException {
        LocalDate localDate = LocalDate.now();
        List<VenueVO> venueVOS = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LocalDate date = localDate.plusDays(i);
            VenueVO venueVO = venueService.getVenueVO(venueId, date);
            venueVOS.add(venueVO);
        }
        return new CommonReturnType(venueVOS);
    }

    //预约场馆（更新信息）
    @RequestMapping(method = RequestMethod.PATCH, value = "/venue/{venueId}")
    public CommonReturnType updateVenueStatus(@PathVariable("venueId") Long venueId, @RequestBody Map<String, Object> params) throws BusinessException {
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("isLogin");
        if (isLogin == null || !isLogin) {
            throw new BusinessException(ErrorEm.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("loginUser");
        System.out.println(params.get("selectedTimeSlots"));
        String s = params.get("selectedTimeSlots").toString();
        Byte[] byteObjects = parse(s);
        LocalDate date = LocalDate.parse(params.get("date").toString(), DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        orderService.placeOrder(userModel.getUserId(), venueId, byteObjects, date);
        return new CommonReturnType(null);
    }


    //新建场馆
    @RequestMapping(method = RequestMethod.POST, value = "/venue")
    public CommonReturnType createVenue(@RequestParam("venueName") String venueName,
                                        @RequestParam("site") String site,
                                        @RequestParam("price") BigDecimal price,
                                        @RequestParam("description") String description,
                                        @RequestParam("timeSlots") String timeSlots,
                                        @RequestParam("photo") MultipartFile file) throws BusinessException {
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("isLogin");
        if (isLogin == null || !isLogin) {
            throw new BusinessException(ErrorEm.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("loginUser");
        Byte[] tss = parse(timeSlots);
        String newName = RandomUUID();
        Map<String, String> result = pictureService.uploadPicture(file, newName + ".png");
        //TODO
        //项目运行在本地时调用ftp上传，在服务器上时调用下面的注释方法
        if (result.get("error").equals("1")) {
            throw new BusinessException(ErrorEm.PICTURE_UPLOAD_FAIL);
        }
//        try {
//            pictureService.uploadFile(file.getBytes(),newName+".png");
//        } catch (IOException e) {
//            throw new BusinessException(ErrorEm.PICTURE_UPLOAD_FAIL);
//        }
        String imgUrl = "http://47.102.142.229:/" + newName + ".png";
        venueService.createVenue(userModel.getUserId(), venueName, description, site, price, tss, imgUrl);
        return new CommonReturnType(null);
    }

    private Byte[] parse(String s) {
        String s1;
        if (s.startsWith("[")) {
            s1 = s.substring(1, s.length() - 1);
        } else {
            s1 = s;
        }
        String s2 = s1.replace(" ", "");
        String[] strs = s2.split(",");
        int len = strs.length;
        Byte[] bytes = new Byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = Byte.valueOf(strs[i]);
        }
        return bytes;
    }

    private String RandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
