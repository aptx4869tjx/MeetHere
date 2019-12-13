package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.OccupiedTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OccupiedTimeSlotDao extends JpaRepository<OccupiedTimeSlot, Long> {
    List<OccupiedTimeSlot> findByVenueId(Long venueId);

    List<OccupiedTimeSlot> findByVenueIdAndDate(Long venueId, LocalDate date);

    List<OccupiedTimeSlot> findByOrderId(Long orderId);

    @Query(value = "select O.occupiedTimeSlot from OccupiedTimeSlot as O where O.orderId=:orderId")
    List<Byte> selectOccupiedTimeSlotsByOrderId(@Param("orderId") Long orderId);

    @Query(value = "select O.date from OccupiedTimeSlot as O where O.orderId=:orderId")
    List<LocalDate> selectReservationDateByOrderId(@Param("orderId") Long orderId);


    @Query(value = "select count(O.occupiedTimeSlot) from OccupiedTimeSlot as O where O.date=:date and O.venueId=:venueId")
    Integer getSlotCountByDateAndVenueId(@Param("date") LocalDate date, @Param("venueId") Long venueId);
}
