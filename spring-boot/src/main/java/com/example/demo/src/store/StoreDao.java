package com.example.demo.src.store;

import com.example.demo.src.store.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class StoreDao {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**스토어 - 쇼핑몰 랭킹 순 조회*/
    public List<ShoppingmallRanking> getMallRankingResults(){
        String getMallRankingQuery = "SELECT Store.storeIdx FROM ( SELECT * FROM UserProduct GROUP BY storeIdx ORDER BY count(userIdx) desc ) AS t LEFT JOIN Store on t.storeIdx = Store.storeIdx where type = ?";
        String type = "shoppingmall";
        return this.jdbcTemplate.query(getMallRankingQuery,
                (rs,rowNum) -> new ShoppingmallRanking(
                        rs.getInt("storeIdx"))
                ,type
        );
    }

    /**스토어 - 브랜드 랭킹 순 조회*/
    public List<BrandRanking> getBrandRankingResults(){
        String getBrandRankingQuery = "SELECT Store.storeIdx as storeIdx FROM ( SELECT * FROM UserProduct GROUP BY storeIdx ORDER BY count(userIdx) desc ) AS t LEFT JOIN Store on t.storeIdx = Store.storeIdx where type = ?";
        String type = "brand";
        return this.jdbcTemplate.query(getBrandRankingQuery,
                (rs,rowNum) -> new BrandRanking(
                        rs.getInt("storeIdx"))
                ,type
        );
    }
    /**스토어 이미지*/
    public String getStoreImg(int storeIdx) throws DataAccessException{
        try {
            String getCategoriesQuery = "SELECT storeImg FROM Store where storeIdx = ?";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("storeImg")),
                    storeIdx);
        }catch (DataAccessException e){
            return null;
        }
    }
    /**스토어 이름*/
    public String getStoreName(int storeIdx) throws DataAccessException{
        try{
            String getCategoriesQuery = "SELECT storeName FROM Store where storeIdx = ?";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("storeName")),
                    storeIdx
            );
        }catch (DataAccessException e){
            return null;
        }
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
        try{
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
    /**스토어 카테고리*/
    public String getStoreCategoryName(int storeIdx) throws DataAccessException{
        try {
            String getCategoriesQuery = "SELECT storeCategoryName FROM Store join StoreCategory on Store.storeCategoryIdx = StoreCategory.storeCategoryIdx where storeIdx = ?";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("storeCategoryName")),
                    storeIdx
            );
        }catch (DataAccessException e){
            return null;
        }
    }
    /**스토어 즐겨찾기 수*/
    public int getLikeNum(int storeIdx) throws DataAccessException {
        try {
            String getCategoriesQuery = "SELECT count(userIdx) as likeNum FROM StoreFavorite group by storeIdx having storeIdx = ?";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery, Integer.class, storeIdx);
        }catch (DataAccessException e){
            return 0;
        }
    }
    /**스토어 직진배송 여부*/
    public boolean getFreeDelivery(int storeIdx) throws DataAccessException{
        String getCategoriesQuery = "SELECT if( shippingPossible = 0, true, false ) FROM Store WHERE storeIdx = ?";
        return this.jdbcTemplate.queryForObject(getCategoriesQuery,boolean.class, storeIdx);
    }
    /**스토어 쿠폰 최대 금액*/
    public int getMaxCouponPrice(int storeIdx) throws DataAccessException{
        try{
            String getCategoriesQuery = "SELECT max(price) FROM Coupon group by storeIdx having storeIdx = ?";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery,Integer.class, storeIdx);
        }catch (DataAccessException e){
            return 0;
        }
    }

    /**스토어 - 신규 입점 브랜드 조회*/
    public List<NewBrand> getNewBrandResults()throws DataAccessException{
        try{
            String getBrandRankingQuery = "SELECT storeIdx FROM Store WHERE type = ? and createdAt BETWEEN DATE_ADD(NOW(),INTERVAL -1 WEEK ) AND NOW() order by createdAt desc";
            String type = "brand";
            return this.jdbcTemplate.query(getBrandRankingQuery,
                    (rs,rowNum) -> new NewBrand(
                            rs.getInt("storeIdx"))
                    ,type
            );
        }catch (DataAccessException e){
            return null;
        }
    }
    /**신규입점 브랜드 개수*/
    public int getNewBrandNum() throws DataAccessException{
        try {
            String getCategoriesQuery = "SELECT count(storeIdx) FROM Store WHERE type = ? and createdAt BETWEEN DATE_ADD(NOW(),INTERVAL -1 WEEK ) AND NOW() order by createdAt desc";
            String type = "brand";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery, Integer.class, type);
        }catch (DataAccessException e){
            return 0;
        }
    }

    /**스토어 식별자 확인*/
    public int checkStoreIdx(int storeIdx) throws DataAccessException{
        try{
            String checkStoreQuery = "select exists(select storeIdx from Store where storeIdx = ? and status = 'y')";
            return this.jdbcTemplate.queryForObject(checkStoreQuery, int.class, storeIdx);
        }catch (DataAccessException e){
            return 0;
        }
    }

    /**스토어 전체 상품 목록 조회*/
    public List<StoreProduct> getStoreProducts(int storeIdx){
        String getCategoriesQuery = "SELECT productIdx FROM Product WHERE storeIdx = ? and status = 'y' order by createdAt desc";
        return this.jdbcTemplate.query(getCategoriesQuery,
                (rs, rowNum) -> new StoreProduct(
                        rs.getInt("productIdx")),
                storeIdx
        );
    }

    /**스토어 이번주 판매 베스트 상품 조회*/
    public List<StoreProduct> getBestStoreProducts(int storeIdx){
        String getCategoriesQuery = "select t.productIdx " +
                "from (select * from UserProduct group by productIdx order by count(userIdx) desc)as t " +
                "where t.storeIdx = ? and t.updatedAt BETWEEN DATE_ADD(NOW(),INTERVAL -1 WEEK ) AND NOW()";
        return this.jdbcTemplate.query(getCategoriesQuery,
                (rs, rowNum) -> new StoreProduct(
                        rs.getInt("productIdx")),
                storeIdx
        );
    }

    /**스토어 직진 배송 상품 조회*/
    public List<StoreProduct> getQuickStoreProducts(int storeIdx){
        String getCategoriesQuery = "select productIdx from Product where isQuick = 'y' and status ='y' and storeIdx = ? order by createdAt desc";
        return this.jdbcTemplate.query(getCategoriesQuery,
                (rs, rowNum) -> new StoreProduct(
                        rs.getInt("productIdx")),
                storeIdx
        );
    }

    /**스토어 전체 상품 개수*/
    public int getProductCount(int storeIdx) throws DataAccessException{
        try {
            String getCategoriesQuery = "SELECT count(productIdx) as count FROM Product WHERE status = 'y' group by storeIdx having storeIdx = ?";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery, Integer.class, storeIdx);
        }catch (DataAccessException e){
            return 0;
        }
    }
    /**상품 이름*/
    public String getProductName(int productIdx){
        String getCategoriesQuery = "SELECT productName FROM Product WHERE productIdx = ?";
        return this.jdbcTemplate.queryForObject(getCategoriesQuery,
                (rs,rowNum) -> new String(
                        rs.getString("productName")),
                productIdx
        );
    }
    /**상품 이미지*/
    public List<String> getProductImg(int productIdx){
        String getCategoriesQuery = "SELECT productImgUrl FROM ProductImg WHERE productIdx = ?";
        return this.jdbcTemplate.query(getCategoriesQuery,
                (rs,rowNum) -> new String(
                        rs.getString("productImgUrl")),
                productIdx
        );
    }
    /**상품 가격*/
    public int getPrice(int productIdx){
        String getCategoriesQuery = "SELECT price FROM Product WHERE productIdx = ?";
        return this.jdbcTemplate.queryForObject(getCategoriesQuery,Integer.class, productIdx);
    }
    /**상품 배송비*/
    public int getShippingCost(int productIdx){
        String getCategoriesQuery = "SELECT shippingCost FROM Product WHERE productIdx = ?";
        return this.jdbcTemplate.queryForObject(getCategoriesQuery,Integer.class, productIdx);
    }
    /**신상품 여부*/
    public String getIsNew(int productIdx){
        String getCategoriesQuery = "SELECT isNew FROM Product WHERE productIdx = ?";
        return this.jdbcTemplate.queryForObject(getCategoriesQuery,
                (rs,rowNum) -> new String(
                        rs.getString("isNew")),
                productIdx
        );
    }
    /**직진배송 가능 여부*/
    public String getIsQuick(int productIdx){
        String getCategoriesQuery = "SELECT isQuick FROM Product WHERE productIdx = ?";
        return this.jdbcTemplate.queryForObject(getCategoriesQuery,
                (rs,rowNum) -> new String(
                        rs.getString("isQuick")),
                productIdx
        );
    }
    /**제트할인가 여부*/
    public String getIsZdiscount(int productIdx){
        String getCategoriesQuery = "SELECT isZdiscount FROM Product WHERE productIdx = ?";
        return this.jdbcTemplate.queryForObject(getCategoriesQuery,
                (rs,rowNum) -> new String(
                        rs.getString("isZdiscount")),
                productIdx
        );
    }
    /**상품 할인율*/
    public int getDiscountRate(int productIdx){
        String getCategoriesQuery = "SELECT discountRate FROM Product WHERE productIdx = ?";
        return this.jdbcTemplate.queryForObject(getCategoriesQuery,Integer.class, productIdx);
    }
    /**평균 별점*/
    public int getReviewAverage(int productIdx) throws DataAccessException{
        try {
            String getCategoriesQuery = "SELECT avg(starRate) as reviewAverage FROM Review group by productIdx having productIdx = ?";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery, Integer.class, productIdx);
        }catch (DataAccessException e){
            return 0;
        }
    }
    /**리뷰 개수*/
    public int getReviewCount(int productIdx) throws DataAccessException {
        try{
            String getCategoriesQuery = "SELECT count(reviewIdx) as reviewCount FROM Review group by productIdx having productIdx = ?";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery, Integer.class, productIdx);
        }catch (DataAccessException e){
            return 0;
        }
    }
}
