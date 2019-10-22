package com.tjx.MeetHere.service.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

//创建场馆时所需要的全部信息
public class VenueModel {
    private Long venueId;

    @NotBlank(message = "场馆名称不能为空")
    private String venueName;

    @NotBlank(message = "场馆介绍不能为空")
    private String description;

    @NotNull(message = "场馆价格不能为空")
    private Double price;

    @NotBlank(message = "场馆地址不能为空")
    private String site;

//    private LocalDate date;//日期

    @NotEmpty(message = "场馆开放时段不能为空")
    private Byte[] timeSlots;//开放的所有时段,以24小时计数

    private LocalDateTime createTime;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    //private int[] occupiedTimeSlots;//占用的时段


    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public VenueModel() {
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }


    public Byte[] getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(Byte[] timeSlots) {
        this.timeSlots = timeSlots;
    }
}
