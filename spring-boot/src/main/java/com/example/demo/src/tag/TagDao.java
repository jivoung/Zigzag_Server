package com.example.demo.src.tag;

import com.example.demo.src.tag.model.*;
import com.example.demo.src.user.model.DeleteFavoriteStoreReq;
import com.example.demo.src.user.model.GetFavoriteStoreRes;
import com.example.demo.src.user.model.PostFavoriteStoreReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class TagDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**전체 태그 조회*/
    public List<GetTagRes> getTags(int userIdx){
        String getTagQuery = "select t.userIdx, t.storeTagIdx, t.keyword, count(t.storeIdx) as count " +
                "from( select * from StoreTag where userIdx = ? and status = 'y' order by createdAt desc) as t group by t.keyword";
        return this.jdbcTemplate.query(getTagQuery,
                (rs,rowNum) -> new GetTagRes(
                        rs.getInt("userIdx"),
                        rs.getInt("storeTagIdx"),
                        rs.getString("keyword"),
                        rs.getInt("count")),
                userIdx);
    }

    /**태그별 스토어 조회*/
    public List<GetTagStoreRes> getTagStores(int userIdx, String keyword){
        String getTagStoreQuery = "select StoreTag.userIdx, StoreTag.storeIdx, Store.storeName, Store.storeImg, StoreTag.keyword, exists(select * from StoreFavorite where userIdx = StoreTag.userIdx and storeIdx =  StoreTag.storeIdx and status = 'y') as isFavorite " +
                "from StoreTag left join Store on StoreTag.storeIdx = Store.storeIdx where StoreTag.status='y' and userIdx = ? and keyword= ? order by StoreTag.createdAt desc";
        return this.jdbcTemplate.query(getTagStoreQuery,
                (rs,rowNum) -> new GetTagStoreRes(
                        rs.getInt("userIdx"),
                        rs.getInt("storeIdx"),
                        rs.getString("storeName"),
                        rs.getString("storeImg"),
                        rs.getString("keyword"),
                        rs.getBoolean("isFavorite")),
                userIdx, keyword);
    }

    /**태그 등록*/
    public int createTag(PostTagReq postTagReq){
        String createTagQuery = "insert into StoreTag (userIdx, storeIdx, keyword) VALUES (?,?,?)";
        Object[] createTagParams = new Object[]{postTagReq.getUserIdx(),postTagReq.getStoreIdx(),postTagReq.getKeyword()};
        this.jdbcTemplate.update(createTagQuery, createTagParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
    /**스토어에 동일한 태그 있는지 확인*/
    public int checkStoreTag(PostTagReq postTagReq) throws DataAccessException {
        try {
            String checkStoreTagQuery = "select exists(select storeTagIdx from StoreTag where userIdx = ? and storeIdx = ? and keyword = ? and status = 'y')";
            return this.jdbcTemplate.queryForObject(checkStoreTagQuery, int.class,
                    postTagReq.getUserIdx(), postTagReq.getStoreIdx(), postTagReq.getKeyword());
        }catch (DataAccessException e){
            return 0;
        }
    }
    /**스토어의 태그 개수*/
    public int checkStoreTagNum(PostTagReq postTagReq) throws DataAccessException{
        try{
            String checkStoreTagNumQuery = "select count(storeTagIdx) from StoreTag where userIdx = ? and storeIdx = ? and status = 'y'";
            return this.jdbcTemplate.queryForObject(checkStoreTagNumQuery, int.class, postTagReq.getUserIdx(), postTagReq.getStoreIdx());
        }catch (DataAccessException e){
            return 0;
        }
    }

    /**태그 삭제*/
    public int deleteTag(PatchTagReq patchTagReq){
        String deleteTagQuery = "update StoreTag set status = 'n' where userIdx = ? and keyword = ? and status = 'y' ";
        Object[] deleteTagParams = new Object[]{patchTagReq.getUserIdx(), patchTagReq.getKeyword()};

        return this.jdbcTemplate.update(deleteTagQuery,deleteTagParams);
    }
    /**해당 태그가 존재하는지 확인*/
    public int checkTagKeyword(PatchTagReq patchTagReq) throws DataAccessException {
        try {
            String checkStoreTagQuery = "select exists(select storeTagIdx from StoreTag where userIdx = ? and keyword = ? and status = 'y')";
            return this.jdbcTemplate.queryForObject(checkStoreTagQuery, int.class,
                    patchTagReq.getUserIdx(), patchTagReq.getKeyword());
        }catch (DataAccessException e){
            return 0;
        }
    }

    /**최근 태그 조회*/
    public List<String> getRecentTags(int userIdx){
        String getRecentTagQuery = "select distinct keyword from StoreTag where userIdx = ? and status = 'y' order by createdAt desc limit 10";
        return this.jdbcTemplate.query(getRecentTagQuery,
                (rs,rowNum) -> new String(
                        rs.getString("keyword")),
                userIdx);
    }

    /**추천 태그 조회*/
    public List<String> getRecommendationTags(){
        String getRecommendationTagQuery = "SELECT keyword FROM StoreTag WHERE createdAt BETWEEN date_add(NOW(),INTERVAL -1 WEEK) AND now() GROUP BY(keyword) ORDER BY count(keyword) desc";
        return this.jdbcTemplate.query(getRecommendationTagQuery,
                (rs,rowNum) -> new String(
                        rs.getString("keyword"))
        );
    }
}
