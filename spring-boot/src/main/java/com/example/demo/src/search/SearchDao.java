package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.src.search.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Repository
public class SearchDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**상품 검색 결과 조회*/
    public List<ProductResult> getProductResults(String keyword){
        String getProductResultsQuery = "SELECT Product.productIdx, Product.storeName, Product.productName, Product.price, Product.shippingCost, Product.isNew, Product.isQuick, Product.isBrand, Product.isZdiscount, Product.discountRate "+
                "FROM Product WHERE productName like ?";
        String getProductResultsParams = "%"+keyword+"%";
        return this.jdbcTemplate.query(getProductResultsQuery,
                (rs,rowNum) -> new ProductResult(
                        rs.getInt("productIdx"),
                        rs.getString("storeName"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getInt("shippingCost"),
                        rs.getString("isNew"),
                        rs.getString("isQuick"),
                        rs.getString("isBrand"),
                        rs.getString("isZdiscount"),
                        rs.getInt("discountRate")),
                getProductResultsParams);
    }
    /**상품 이미지 조회*/
    public List<String> getProductImgUrl(int productIdx) throws DataAccessException{
        try {
            String getCategoriesQuery = "SELECT productImgUrl FROM ProductImg where productIdx = ?";
            return this.jdbcTemplate.query(getCategoriesQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("productImgUrl")),
                    productIdx
            );
        }catch (DataAccessException e){
            return null;
        }
    }

    /**스토어 검색 결과 조회*/
    public List<StoreResult> getStoreResults(String keyword){
        String getStoreResultsQuery = "SELECT Store.storeIdx, Store.storeImg, Store.storeName, StoreCategory.storeCategoryName as storeType, count(StoreFavorite.userIdx) as likeNum FROM Store " +
                "left join StoreFavorite on Store.storeIdx = StoreFavorite.storeFavoriteIdx " +
                "left join StoreCategory on Store.storeCategoryIdx = StoreCategory.storeCategoryIdx " +
                "WHERE Store.storeName like ? group by StoreFavorite.storeIdx order by likeNum desc ";
        String getStoreResultsParams = "%" + keyword + "%";
        return this.jdbcTemplate.query(getStoreResultsQuery,
                (rs, rowNum) -> new StoreResult(
                        rs.getInt("storeIdx"),
                        rs.getString("storeImg"),
                        rs.getString("storeName"),
                        rs.getString("storeType"),
                        rs.getInt("likeNum")),
                getStoreResultsParams);
    }
    /**관련 키워드 조회*/
    public List<String> getKeywordList(String keyword){
        String getCategoriesQuery = "SELECT categoryDetailName FROM CategoryDetail where categoryDetailName like ?";
        String getKeywordParams = "%" + keyword + "%";
        return this.jdbcTemplate.query(getCategoriesQuery,
                (rs, rowNum) -> new String(
                        rs.getString("categoryDetailName")),
                getKeywordParams
        );
    }
    /**스토어 연령대*/
    public List<String> getStoreAgeGroup(int storeIdx) throws DataAccessException{
        try {
            String getCategoriesQuery = "SELECT groupName FROM StoreAgeGroup join AgeGroup on StoreAgeGroup.ageGroupIdx = AgeGroup.ageGroupIdx where storeIdx = ?";
            return this.jdbcTemplate.query(getCategoriesQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("groupName")),
                    storeIdx
            );
        }catch (DataAccessException e){
            return null;
        }
    }
    /**스토어 스타일*/
    public List<String> getStoreStyle(int storeIdx) throws DataAccessException{
        try {
            String getCategoriesQuery = "SELECT styleName FROM StoreStyle join Style on StoreStyle.styleIdx = Style.styleIdx where storeIdx = ?";
            return this.jdbcTemplate.query(getCategoriesQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("styleName")),
                    storeIdx
            );
        }catch (DataAccessException e){
            return null;
        }
    }

    /**지금 인기있는 키워드 조회*/
    public List<GetKeywordRes> getPopularKeywords(){
        String getPopularKeywordsQuery = "SELECT keyword FROM Search WHERE date(createdAt) = date(now()) GROUP BY keyword having count(keyword) > 0 ORDER BY count(keyword) desc limit 20";
        return this.jdbcTemplate.query(getPopularKeywordsQuery,
                (rs,rowNum) -> new GetKeywordRes(
                        rs.getString("keyword"))
        );
    }

    /**키워드 등록*/
    public int createKeyword(PostKeywordReq postKeywordReq){
        String createKeywordQuery = "insert into Search (userIdx, keyword) VALUES (?,?)";
        Object[] createKeywordParams = new Object[]{postKeywordReq.getUserIdx(), postKeywordReq.getKeyword()};
        this.jdbcTemplate.update(createKeywordQuery, createKeywordParams);
        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }
    /**해당 키워드에 대한 스토어 확인*/
    public int checkKeywordStore(String keyword) throws DataAccessException {
        try {
            String checkKeywordQuery = "select exists(select storeName from Store where storeName like ?)";
            String checkKeywordParams = "%" + keyword + "%";
            return this.jdbcTemplate.queryForObject(checkKeywordQuery,
                    int.class,
                    checkKeywordParams);
        }catch (DataAccessException e){
            return 0;
        }
    }
    /**해당 키워드에 대한 상품 확인*/
    public int checkKeywordProduct(String keyword) throws DataAccessException {
        try {
            String checkKeywordQuery = "select exists(select productName from Product where productName like ?)";
            String checkKeywordParams = "%" + keyword + "%";
            return this.jdbcTemplate.queryForObject(checkKeywordQuery,
                    int.class,
                    checkKeywordParams);
        }catch (DataAccessException e){
            return 0;
        }
    }
    /**유저가 검색한 키워드 확인*/
    public int checkKeyword(PostKeywordReq postKeywordReq){
        String checkKeywordQuery = "select exists(select searchIdx from Search where userIdx = ? and keyword = ? and isDeleted = 0)";
        return this.jdbcTemplate.queryForObject(checkKeywordQuery,
                int.class,
                postKeywordReq.getUserIdx(), postKeywordReq.getKeyword());
    }

    /**내가 찾아봤던 키워드 조회*/
    public List<GetKeywordRes> getKeywords(int userIdx){
        String getKeywordsQuery = "SELECT keyword FROM Search where userIdx = ? and isDeleted = 0 order by createdAt desc";
        int getKeywordParams = userIdx;
        return this.jdbcTemplate.query(getKeywordsQuery,
                (rs, rowNum) -> new GetKeywordRes(
                        rs.getString("keyword")),
                getKeywordParams);
    }

    /**내가 찾아봤던 키워드 삭제*/
    public int deleteKeywords(int userIdx) {
        String deleteKeywordQuery = "update Search set isDeleted = 1 where userIdx = ? and isDeleted = 0";
        return this.jdbcTemplate.update(deleteKeywordQuery,userIdx);
    }
}
