package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


public interface NewsDao extends JpaRepository<News, Long> {

    News findByNewsId(Long newsId);

    @Modifying
    @Transactional
    void deleteByNewsId(Long newsId);

//    @Query(value = "select new com.tjx.MeetHere.controller.viewObject.NewsVO(N.newsId,N.title,N.time,N.text) from News as N where N.newsId=:newsId")
//    List<NewsVO> getSimpleNewsVOByNewsId(@Param("newsId") Long newsId);

}
