package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;

public interface VenueDao extends JpaRepository<Venue, Long> {
    Venue findByVenueId(Long venueId);

    boolean existsByVenueId(Long venueId);

    List<Venue> findAll();

    @Query(value = "select V.venueId from Venue as V")
    List<Long> getAllVenueId();

    @Query(value = "select V.venueName from Venue as V where V.venueId=:venueId")
    String getVenueNameByVenueId(@Param("venueId") Long venueId);

    @Modifying
    @Transactional
    @Query(value = "update Venue as  V set V.venueName=:venueName, V.site=:site,V.description=:description," +
            "V.price=:price where V.venueId=:venueId")
    int updateVenueInfo(@Param("venueId") Long venueId, @Param("venueName") String venueName, @Param("site") String site,
                        @Param("description") String description, @Param("price") Double price);

    @Modifying
    @Transactional
    @Query(value = "update Venue as V set V.venueName=:venueName, V.site=:site,V.description=:description," +
            "V.price=:price,V.imgUrl=:imgUrl where V.venueId=:venueId")
    int updateVenueInfoWithImage(@Param("venueId") Long venueId, @Param("venueName") String venueName, @Param("site") String site,
                                 @Param("description") String description, @Param("price") Double price, @Param("imgUrl") String imgUrl);
}
