package com.tjx.MeetHere.dao;


import com.tjx.MeetHere.dataObject.NewsImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NewsImageDao extends JpaRepository<NewsImage, Long> {
    NewsImage findByImageUrl(String imgUrl);

    @Modifying
    @Transactional
    void deleteByImageUrl(String imgUrl);


    @Modifying
    @Transactional
    @Query(value = "update NewsImage as N set N.newsId=:newsId where N.imageUrl=:imageUrl")
    void updateNewsImageByImgUrl(@Param("newsId") Long newsId, @Param("imageUrl") String imageUrl);


    @Query(value = "select N.imageUrl  from NewsImage as N where N.newsId=:newsId")
    List<String> getImageUrlsByNewsId(@Param("newsId") Long newsId);


}
