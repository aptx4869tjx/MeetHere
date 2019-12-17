package com.tjx.MeetHere.controller;


import com.alibaba.fastjson.JSON;
import com.tjx.MeetHere.controller.viewObject.OrderVO;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.error.ErrorEm;
import com.tjx.MeetHere.response.CommonReturnType;
import com.tjx.MeetHere.service.OrderService;
import com.tjx.MeetHere.service.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class OrderController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private OrderService orderService;
    @Autowired
    HttpServletRequest httpServletRequest;

    @GetMapping("/order")
    public CommonReturnType getUserOrder() {
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("loginUser");
        List<OrderVO> orderVOList = orderService.getOrderByUserId(userModel.getUserId());
        return new CommonReturnType(orderVOList);
    }

    @GetMapping("/orders")
    public CommonReturnType getAllOrders(@RequestParam("page") Integer page) {
        List<OrderVO> orderVOList = orderService.getAllOrders(page);
        return new CommonReturnType(orderVOList);
    }

    @PostMapping("/statistics")
    public CommonReturnType getStatistics(@RequestBody Map<String, Object> params) {
        System.out.println(params.size());
        //前端没有指定场馆id，就返回默认的查询
        if (params.size() == 0) {
            return new CommonReturnType(orderService.defaultGetStatistics());
        }
        if(params.get("venueIdList")==null){
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR,"查询场馆不能为空");
        }
        String venueIdList = params.get("venueIdList").toString();
        String day1 = (String) params.get("startDate");
        String day2 = (String) params.get("endDate");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        LocalDate startDate = LocalDate.parse(day1, formatter);
        LocalDate endDate = LocalDate.parse(day2, formatter);
        List<Long> venueIdLists = JSON.parseArray(venueIdList, Long.class);
        List<Map<Object, Object>> result = orderService.getStatistics(venueIdLists, startDate, endDate);

        return new CommonReturnType(result);
    }
}
