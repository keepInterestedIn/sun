package com.bizdata.app.content.article.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bizdata.app.content.article.domain.Article;

import me.sdevil507.base.JpaBaseRepository;

public interface ArticleRepository extends JpaBaseRepository<Article, String> {
	

	@Query(value="select max(column_id) from app_article",nativeQuery=true)
	Integer findMaxid();
	@Query(value="select count(id) from app_article_pra where article_id = ?1",nativeQuery=true)
	Integer getpraisenum(String article_id);
	@Query(value="select sum(num) from app_art_criticism where target_id = ?1",nativeQuery=true)
	Integer getcrinum(String article_id);
	@Query(value="select count(id) from app_article where columnart_id = ?1",nativeQuery=true)
	Integer getNumByColumnartid(String columnartid);
	@Query(value="select id from app_article where column_id = ?1",nativeQuery=true)
	String getIdByArticleid(int s);
	@Transactional
	@Modifying
	@Query(value="UPDATE app_article SET state = ?2 WHERE columnart_id = ?1",nativeQuery=true)
	void setState(String columnartid,String state);

}
