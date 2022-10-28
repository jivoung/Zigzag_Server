package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.search.model.PostKeywordReq;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DELETE_FAIL_ORDER_PRODUCT;


@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**회원가입*/
    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (email, password) VALUES (?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getEmail(), postUserReq.getPassword()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }
    /**이메일 확인*/
    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    /**로그인*/
    public User getPwd(PostLoginReq postLoginReq) throws EmptyResultDataAccessException {
        try {
            String getPwdQuery = "select userIdx, email, password, status from User where email = ?";
            String getPwdParams = postLoginReq.getEmail();
            return this.jdbcTemplate.queryForObject(getPwdQuery,
                    (rs, rowNum) -> new User(
                            rs.getInt("userIdx"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("status")),
                    getPwdParams
            );
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    /**특정 유저 조회 (마이페이지 상단)*/
    public GetUserRes getUser(int userIdx){
        String getUserQuery = "SELECT userIdx, name, email, membershipType "+
                "from User left join MemberShip on User.membershipIdx = MemberShip.membershipIdx where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("membershipType")),
                getUserParams);
    }
    /**주문, 배송 중인 상품 개수*/
    public int getOrderDeliveryCount(int userIdx) throws EmptyResultDataAccessException{
        try {
            String getCategoriesQuery = "SELECT count(userProductIdx) FROM UserProduct WHERE userIdx = ? and status != 'y'";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery, Integer.class, userIdx);
        }catch(EmptyResultDataAccessException e){
            return 0;
        }
    }
    /**유저가 작성한 리뷰 개수*/
    public int getReviewCount(int userIdx) throws EmptyResultDataAccessException{
        try {
            String getCategoriesQuery = "SELECT count(reviewIdx) FROM Review left join UserProduct on Review.userProductIdx = UserProduct.userProductIdx WHERE UserProduct.userIdx = ?";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery, Integer.class, userIdx);
        }catch(EmptyResultDataAccessException e){
            return 0;
        }
    }
    /**유저가 보유하고 있는 쿠폰 개수*/
    public int getCouponCount(int userIdx) throws EmptyResultDataAccessException{
        try {
            String getCategoriesQuery = "SELECT count(couponIdx) FROM UserCoupon WHERE userIdx = ? and status =\"possible\"";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery, Integer.class, userIdx);
        }catch(EmptyResultDataAccessException e){
            return 0;
        }
    }
    /**유저가 보유하고 있는 포인트 합계*/
    public int getPointSum(int userIdx) throws EmptyResultDataAccessException{
        try {
            String getCategoriesQuery = "SELECT sum(point) FROM Point Where status = 'y' group by userIdx having userIdx = ?";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery, Integer.class, userIdx);
        }catch(EmptyResultDataAccessException e){
            return 0;
        }
    }

    /**즐겨찾기한 스토어 조회*/
    public List<FavoriteStore> getFavoriteStoreResults(int userIdx) {
        String getFavoriteStoreQuery = "SELECT Store.storeIdx, Store.storeImg, Store.storeName FROM StoreFavorite " +
                "left join Store on StoreFavorite.storeIdx=Store.storeIdx WHERE StoreFavorite.status = 'y' and userIdx = ? order by StoreFavorite.createdAt desc";
        return this.jdbcTemplate.query(getFavoriteStoreQuery,
                (rs,rowNum) -> new FavoriteStore(
                        rs.getInt("storeIdx"),
                        rs.getString("storeImg"),
                        rs.getString("storeName")),
                userIdx);
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
    /**스토어 신상품 개수*/
    public int getNewProductCount(int storeIdx) throws DataAccessException{
        try{
            String getCategoriesQuery = "select count(productIdx) from Product Where storeIdx = ? and createdAt BETWEEN DATE_ADD(NOW(),INTERVAL -1 WEEK ) AND NOW()";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery,Integer.class, storeIdx);
        }catch (DataAccessException e){
            return 0;
        }
    }

    public List<FavoriteStore> getFavoriteStoreResultsCoupon(int userIdx) {
        String getFavoriteStoreQuery = "SELECT distinct Store.storeIdx, Store.storeImg, Store.storeName FROM Coupon " +
                "left join Store on Coupon.storeIdx = Store.storeIdx " +
                "left join StoreFavorite on StoreFavorite.storeIdx=Store.storeIdx WHERE StoreFavorite.status = 'y' and userIdx = ? order by StoreFavorite.createdAt desc";
        return this.jdbcTemplate.query(getFavoriteStoreQuery,
                (rs,rowNum) -> new FavoriteStore(
                        rs.getInt("storeIdx"),
                        rs.getString("storeImg"),
                        rs.getString("storeName")),
                userIdx);
    }

    public int getFavoriteStoreNum(int userIdx) throws DataAccessException{
        try{
            String getCategoriesQuery = "SELECT count(Store.storeIdx) FROM StoreFavorite " +
                    "left join Store on StoreFavorite.storeIdx=Store.storeIdx WHERE StoreFavorite.status = 'y' and userIdx = ?";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery, Integer.class, userIdx);
        }catch(DataAccessException e){
            return 0;
        }
    }

    /**유저가 즐겨찾기한 스토어 확인*/
    public int checkFavoriteStore(int userIdx, int storeIdx){
        String checkFavoriteStoreQuery = "select exists(select storeFavoriteIdx from StoreFavorite where userIdx = ? and storeIdx = ? and status = 'y')";
        return this.jdbcTemplate.queryForObject(checkFavoriteStoreQuery,
                int.class,
                userIdx, storeIdx);
    }

    /**스토어 즐겨찾기 등록*/
    public int createFavoriteStore(PostFavoriteStoreReq postFavoriteStoreReq){
        String createFavoriteStoreQuery = "insert into StoreFavorite (userIdx, storeIdx) VALUES (?,?)";
        Object[] createFavoriteStoreParams = new Object[]{postFavoriteStoreReq.getUserIdx(),postFavoriteStoreReq.getStoreIdx()};
        this.jdbcTemplate.update(createFavoriteStoreQuery, createFavoriteStoreParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    /**스토어 즐겨찾기 취소*/
    public int deleteFavoriteStore(DeleteFavoriteStoreReq deleteFavoriteStoreReq){
        String deleteFavoriteStoreQuery = "update StoreFavorite set status = 'n' where userIdx = ? and storeIdx = ? and status = 'y' ";
        Object[] deleteFavoriteStoreParams = new Object[]{deleteFavoriteStoreReq.getUserIdx(), deleteFavoriteStoreReq.getStoreIdx()};

        return this.jdbcTemplate.update(deleteFavoriteStoreQuery,deleteFavoriteStoreParams);
    }

    /**즐겨찾기한 스토어 스토리 목록 조회*/
    public List<GetFavoriteStoreStoryRes> getFavoriteStoreStoryList(int userIdx){
        String getFavoriteStoreStoryQuery = "select distinct Store.storeIdx, Store.storeImg, Store.storeName from StoreStory " +
                "left join Store on StoreStory.storeIdx = Store.storeIdx " +
                "left join StoreFavorite on StoreStory.storeIdx = StoreFavorite.storeIdx " +
                "where userIdx = ? and StoreStory.status = 'y' and StoreFavorite.status = 'y' order by StoreFavorite.createdAt desc";
        return this.jdbcTemplate.query(getFavoriteStoreStoryQuery,
                (rs,rowNum) -> new GetFavoriteStoreStoryRes(
                        rs.getInt("storeIdx"),
                        rs.getString("storeImg"),
                        rs.getString("storeName")),
                userIdx);
    }

    /**즐겨찾기한 스토어 스토리 상세 조회*/
    public List<GetFavoriteStoreStoryContentRes> getFavoriteStoreStoryContentList(int userIdx, int storeIdx){
        String getFavoriteStoreStoryContentQuery = "select Store.storeIdx, Store.storeImg, Store.storeName, StoreStory.createdAt, StoreStory.productIdx, StoreStory.storyImg, Product.productName  from StoreStory " +
                "left join Store on StoreStory.storeIdx = Store.storeIdx " +
                "left join Product on StoreStory.productIdx = Product.productIdx " +
                "left join StoreFavorite on StoreStory.storeIdx = StoreFavorite.storeIdx " +
                "where userIdx = ? and Store.storeIdx = ? and StoreStory.status ='y' and StoreFavorite.status = 'y' and StoreStory.createdAt between date_add(NOW(),INTERVAL -1 WEEK) and now() " +
                "order by StoreStory.createdAt desc limit 10";
        return this.jdbcTemplate.query(getFavoriteStoreStoryContentQuery,
                (rs,rowNum) -> new GetFavoriteStoreStoryContentRes(
                        rs.getInt("storeIdx"),
                        rs.getString("storeImg"),
                        rs.getString("storeName"),
                        rs.getTimestamp("createdAt"),
                        rs.getInt("productIdx"),
                        rs.getString("storyImg"),
                        rs.getString("productName")),
                userIdx,storeIdx);
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

    /**전체 주문,배송 상품 조회*/
    public List<GetUserProductRes> getUserProductList(int userIdx){
        String getUserProductQuery = "SELECT userProductIdx as orderProductIdx, UserProduct.createdAt, Store.storeName, t.productImgUrl, Product.productName, UserProduct.status " +
                "FROM UserProduct left join Product on UserProduct.productIdx = Product.productIdx " +
                "left join Store on UserProduct.storeIdx = Store.storeIdx " +
                "left join (SELECT distinct productImgUrl, productIdx FROM ProductImg group by productIdx) as t on UserProduct.productIdx = t.productIdx " +
                "WHERE UserProduct.userIdx = ? and UserProduct.isDeleted = 0 order by UserProduct.createdAt desc";
        return this.jdbcTemplate.query(getUserProductQuery,
                (rs,rowNum) -> new GetUserProductRes(
                        rs.getInt("orderProductIdx"),
                        rs.getTimestamp("createdAt"),
                        rs.getString("storeName"),
                        rs.getString("productImgUrl"),
                        rs.getString("productName"),
                        rs.getString("status")),
                userIdx);
    }

    /**특정 상품 주문 정보 조희*/
    public GetUserProductDetailRes getUserProductDetail(int orderProductIdx){
        String getUserProductDetailQuery = "SELECT userProductIdx as orderProductIdx, s.productCode, UserProduct.createdAt, Store.storeIdx, Store.storeImg, Store.storeName, Store.storePhoneNum, Product.productIdx, " +
                "t.productImgUrl, Product.productName, s.colorName as productColor, s.sizeName as productSize, UserProduct.count, UserProduct.status, Product.price, Product.shippingCost, UserProduct.totalPrice, " +
                "UserProduct.recipientName, UserProduct.recipientPhoneNum, UserProduct.recipientAddress, UserProduct.memo, UserProduct.payment " +
                "FROM UserProduct left join Product on UserProduct.productIdx = Product.productIdx " +
                "left join Store on UserProduct.storeIdx = Store.storeIdx " +
                "left join (SELECT distinct productImgUrl, productIdx FROM ProductImg group by productIdx) as t on UserProduct.productIdx = t.productIdx " +
                "left join (SELECT productDetailIdx, productCode, colorName, sizeName FROM ProductDetail left join Color on ProductDetail.colorIdx = Color.colorIdx left join Size on ProductDetail.sizeIdx = Size.sizeIdx) as s on UserProduct.productDetailIdx = s.productDetailIdx " +
                "WHERE UserProduct.userProductIdx = ?";
        return this.jdbcTemplate.queryForObject(getUserProductDetailQuery,
                (rs,rowNum) -> new GetUserProductDetailRes(
                        rs.getInt("orderProductIdx"),
                        rs.getString("productCode"),
                        rs.getTimestamp("createdAt"),
                        rs.getInt("storeIdx"),
                        rs.getString("storeImg"),
                        rs.getString("storeName"),
                        rs.getString("storePhoneNum"),
                        rs.getInt("productIdx"),
                        rs.getString("productImgUrl"),
                        rs.getString("productName"),
                        rs.getString("productColor"),
                        rs.getString("productSize"),
                        rs.getInt("count"),
                        rs.getString("status"),
                        rs.getInt("price"),
                        rs.getInt("shippingCost"),
                        rs.getInt("totalPrice"),
                        rs.getString("recipientName"),
                        rs.getString("recipientPhoneNum"),
                        rs.getString("recipientAddress"),
                        rs.getString("memo"),
                        rs.getString("payment")),
                orderProductIdx);
    }
    /**구매한 상품 식별자 확인*/
    public int checkOrderProductIdx(int userIdx, int orderProductIdx) throws DataAccessException{
        try{
            String checkOrderProductQuery = "select exists(select userProductIdx from UserProduct where userIdx = ? and userProductIdx = ?)";
            return this.jdbcTemplate.queryForObject(checkOrderProductQuery, int.class, userIdx, orderProductIdx);
        }catch (DataAccessException e){
            return 0;
        }
    }

    /**특정 상품 주문 정보 삭제*/
    public int deleteUserProduct(int userIdx, int orderProductIdx)throws DataAccessException{
        try {
            String deleteUserProductQuery = "update UserProduct set isDeleted = 1 where userIdx = ? and userProductIdx = ? and isDeleted = 0 ";
            return this.jdbcTemplate.update(deleteUserProductQuery, userIdx, orderProductIdx);
        }catch (DataAccessException e){
            return 0;
        }
    }

    /**쿠폰 등록*/
    public int createUserCoupon(PostCouponReq postCouponReq){
        String createUserCouponQuery = "insert into UserCoupon (userIdx, couponIdx) VALUES (?,?)";
        Object[] createUserCouponParams = new Object[]{postCouponReq.getUserIdx(),postCouponReq.getCouponIdx()};
        this.jdbcTemplate.update(createUserCouponQuery, createUserCouponParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
    /**사용자 쿠폰 확인*/
    public int checkUserCoupon(int userIdx, int couponIdx) throws DataAccessException{
        try{
            String checkUserCouponQuery = "select exists(select userCouponIdx from UserCoupon where userIdx = ? and couponIdx = ?)";
            return this.jdbcTemplate.queryForObject(checkUserCouponQuery, int.class, userIdx, couponIdx);
        }catch (DataAccessException e){
            return 0;
        }
    }
    /**쿠폰 식별자 확인*/
    public int checkCouponIdx(int couponIdx)throws DataAccessException{
        try{
            String checkCouponQuery = "select exists(select couponIdx from Coupon where couponIdx = ? and status = \"possible\")";
            return this.jdbcTemplate.queryForObject(checkCouponQuery, int.class, couponIdx);
        }catch (DataAccessException e){
            return 0;
        }
    }

    /**스토어 쿠폰 등록*/
    public int createStoreCoupon(int userIdx, PostStoreCouponReq postStoreCouponReq){
        String createUserCouponQuery = "insert into UserCoupon (userIdx, couponIdx) VALUES (?,?)";
        Object[] createUserCouponParams = new Object[]{userIdx,postStoreCouponReq.getCouponIdx()};
        this.jdbcTemplate.update(createUserCouponQuery, createUserCouponParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
    /**스토어 쿠폰 식별자 확인*/
    public int checkStoreCouponIdx(int storeIdx, int couponIdx)throws DataAccessException{
        try{
            String checkCouponQuery = "select exists(select couponIdx from Coupon where storeIdx = ? and couponIdx = ? and status = \"possible\")";
            return this.jdbcTemplate.queryForObject(checkCouponQuery, int.class,storeIdx, couponIdx);
        }catch (DataAccessException e){
            return 0;
        }
    }

    /**스토어별 쿠폰 조회*/
    public List<GetStoreCouponRes> getStoreCoupons(int storeIdx) {
        String getStoreCouponQuery = "SELECT couponIdx, price, couponName, minimumCost, couponType FROM Coupon WHERE storeIdx = ?";
        return this.jdbcTemplate.query(getStoreCouponQuery,
                (rs, rowNum) -> new GetStoreCouponRes(
                        rs.getInt("couponIdx"),
                        rs.getInt("price"),
                        rs.getString("couponName"),
                        rs.getInt("minimumCost"),
                        rs.getString("couponType")),
                storeIdx);
    }
    /**스토어별 쿠폰 개수*/
    public int getStoreCoupon(int storeIdx) throws EmptyResultDataAccessException{
        try {
            String getCategoriesQuery = "SELECT count(couponIdx) FROM Coupon WHERE storeIdx = ? and status =\"possible\"";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery, Integer.class, storeIdx);
        }catch(EmptyResultDataAccessException e){
            return 0;
        }
    }

    /**유저가 보유하고 있는 쿠폰 조회*/
    public List<GetUserCouponRes> getUserCoupons(int userIdx){
        String getUserCouponQuery = "SELECT UserCoupon.couponIdx, Coupon.price, Coupon.couponName, Coupon.minimumCost, Coupon.couponType, DATE_ADD(UserCoupon.createdAt, INTERVAL 7 DAY) as deadline, Store.storeIdx, Store.storeImg " +
                "FROM UserCoupon LEFT JOIN Coupon ON UserCoupon.couponIdx = Coupon.couponIdx LEFT JOIN Store ON Coupon.storeIdx = Store.storeIdx " +
                "WHERE UserCoupon.userIdx = ? and UserCoupon.status=\"possible\"";
        return this.jdbcTemplate.query(getUserCouponQuery,
                (rs,rowNum) -> new GetUserCouponRes(
                        rs.getInt("couponIdx"),
                        rs.getInt("price"),
                        rs.getString("couponName"),
                        rs.getInt("minimumCost"),
                        rs.getString("couponType"),
                        rs.getTimestamp("deadline"),
                        rs.getString("storeIdx"),
                        rs.getString("storeImg")),
                userIdx);
    }
    /**유저가 보유하고 있는 쿠폰 조회 - 지그재그/직진배송/스토어*/
    public List<GetUserCouponRes> getUserCouponsType(int userIdx, String couponType){
        String getUserCouponQuery = "SELECT UserCoupon.couponIdx, Coupon.price, Coupon.couponName, Coupon.minimumCost, Coupon.couponType, DATE_ADD(UserCoupon.createdAt, INTERVAL 7 DAY) as deadline, Store.storeIdx, Store.storeImg " +
                "FROM UserCoupon LEFT JOIN Coupon ON UserCoupon.couponIdx = Coupon.couponIdx LEFT JOIN Store ON Coupon.storeIdx = Store.storeIdx " +
                "WHERE UserCoupon.userIdx = ? and Coupon.couponType = ? and UserCoupon.status= \"possible\"";
        return this.jdbcTemplate.query(getUserCouponQuery,
                (rs,rowNum) -> new GetUserCouponRes(
                        rs.getInt("couponIdx"),
                        rs.getInt("price"),
                        rs.getString("couponName"),
                        rs.getInt("minimumCost"),
                        rs.getString("couponType"),
                        rs.getTimestamp("deadline"),
                        rs.getString("storeIdx"),
                        rs.getString("storeImg")),
                userIdx, couponType);
    }
    /**지그재그 쿠폰 개수*/
    public int getZigzagCouponCount(int userIdx) throws EmptyResultDataAccessException{
        try {
            String getCategoriesQuery = "SELECT count(UserCoupon.couponIdx) FROM UserCoupon join Coupon on UserCoupon.couponIdx = Coupon.couponIdx " +
                    "and Coupon.couponType = \"zigzag\" and UserCoupon.userIdx = ? and UserCoupon.status=\"possible\"";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery, Integer.class, userIdx);
        }catch(EmptyResultDataAccessException e){
            return 0;
        }
    }
    /**스토어 쿠폰 개수*/
    public int getStoreCouponCount(int userIdx) throws EmptyResultDataAccessException{
        try {
            String getCategoriesQuery = "SELECT count(UserCoupon.couponIdx) FROM UserCoupon join Coupon on UserCoupon.couponIdx = Coupon.couponIdx " +
                    "and Coupon.couponType = \"store\" and UserCoupon.userIdx = ? and UserCoupon.status=\"possible\"";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery, Integer.class, userIdx);
        }catch(EmptyResultDataAccessException e){
            return 0;
        }
    }
    /**직진배송 쿠폰 개수*/
    public int getDeliveryCouponCount(int userIdx) throws EmptyResultDataAccessException{
        try {
            String getCategoriesQuery = "SELECT count(UserCoupon.couponIdx) FROM UserCoupon join Coupon on UserCoupon.couponIdx = Coupon.couponIdx " +
                    "and Coupon.couponType = \"delivery\" and UserCoupon.userIdx = ? and UserCoupon.status=\"possible\"";
            return this.jdbcTemplate.queryForObject(getCategoriesQuery, Integer.class, userIdx);
        }catch(EmptyResultDataAccessException e){
            return 0;
        }
    }

    /**유저가 보유하고 있는 완료,만료 쿠폰 조회*/
    public List<GetExpiredUserCouponRes> getExpiredUserCoupons(int userIdx){
        String getExpiredUserCouponQuery = "SELECT UserCoupon.couponIdx, Coupon.price, Coupon.couponName, Coupon.minimumCost, Coupon.couponType, DATE_ADD(UserCoupon.createdAt, INTERVAL 7 DAY) as deadline, UserCoupon.status, Store.storeImg " +
                "FROM UserCoupon LEFT JOIN Coupon ON UserCoupon.couponIdx = Coupon.couponIdx LEFT JOIN Store ON Coupon.storeIdx = Store.storeIdx " +
                "WHERE UserCoupon.userIdx = ? and UserCoupon.status != \"possible\"";
        return this.jdbcTemplate.query(getExpiredUserCouponQuery,
                (rs,rowNum) -> new GetExpiredUserCouponRes(
                        rs.getInt("couponIdx"),
                        rs.getInt("price"),
                        rs.getString("couponName"),
                        rs.getInt("minimumCost"),
                        rs.getString("couponType"),
                        rs.getTimestamp("deadline"),
                        rs.getString("status"),
                        rs.getString("storeImg")),
                userIdx);
    }

    /**유저 포인트 정보 조회*/
    public List<GetUserPointRes> getUserPointList(int userIdx){
        String getUserPointQuery = "SELECT createdAt, point, pointTypeDesc, date_add(createdAt, interval 1 month) as deadline FROM Point Where userIdx = ?";
        return this.jdbcTemplate.query(getUserPointQuery,
                (rs,rowNum) -> new GetUserPointRes(
                        rs.getTimestamp("createdAt"),
                        rs.getInt("point"),
                        rs.getString("pointTypeDesc"),
                        rs.getTimestamp("deadline")),
                userIdx);
    }
    /**유저 포인트 정보 조회 - 적립/사용/소멸*/
    public List<GetUserPointRes> getUserPointListType(int userIdx, String pointType){
        String getUserPointQuery = "SELECT createdAt, point, pointTypeDesc, date_add(createdAt, interval 1 month) as deadline FROM Point Where userIdx = ? and pointType = ?";
        return this.jdbcTemplate.query(getUserPointQuery,
                (rs,rowNum) -> new GetUserPointRes(
                        rs.getTimestamp("createdAt"),
                        rs.getInt("point"),
                        rs.getString("pointTypeDesc"),
                        rs.getTimestamp("deadline")),
                userIdx, pointType);
    }

    /** 찜한 상품 조회*/
    public List<GetLikedProductRes> getLikedProduct(int userIdx) {
        String getProductQuery = "SELECT " +
                "            p.productIdx, " +
                "            s.storeIdx, " +
                "            s.storeName, " +
                "            pi.productImgUrl, " +
                "            p.productName, " +
                "            p.price, " +
                "            p.discountRate, " +
                "            p.shippingCost, " +
                "            p.isNew, " +
                "            p.isQuick, " +
                "            p.isBrand, " +
                "            p.isZdiscount, " +
                "            pl.folderIdx, " +
                "            f.folderName " +
                "        FROM " +
                "            Product p " +
                "                JOIN Store s ON p.storeIdx = s.storeIdx " +
                "                LEFT JOIN ProductLike pl ON p.productIdx = pl.productIdx AND pl.userIdx = ? " +
                "                LEFT JOIN ProductImg pi ON p.productIdx = pi.productIdx " +
                "                LEFT JOIN User u ON pl.userIdx = u.userIdx " +
                "                LEFT JOIN Folder f ON pl.folderIdx = f.folderIdx " +
                "        WHERE p.status = 'y' AND pl.likeStatus = 'y' " +
                "        GROUP BY " +
                "            p.productIdx " +
                "        ORDER BY " +
                "            p.productIdx ";
        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetLikedProductRes(
                        rs.getInt("productIdx"),
                        rs.getInt("storeIdx"),
                        rs.getString("storeName"),
                        rs.getString("productImgUrl"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getInt("discountRate"),
                        rs.getInt("shippingCost"),
                        rs.getString("isNew"),
                        rs.getString("isQuick"),
                        rs.getString("isBrand"),
                        rs.getString("isZdiscount"),
                        rs.getInt("folderIdx"),
                        rs.getString("folderName")
                ),userIdx
        );
    }

    /** 찜한 상품 스토어별 조회*/
    public List<GetLikedProductStoreRes> getLikedProductStore(int userIdx) {
        String getProductQuery = "SELECT S2.storeIdx, S2.storeName, COUNT(S2.storeIdx) AS productCount " +
                "FROM (SELECT " +
                "            s.storeIdx, " +
                "            s.storeName " +
                "        FROM " +
                "            Store s " +
                "                JOIN Product p ON p.storeIdx = s.storeIdx " +
                "                LEFT JOIN ProductLike pl ON p.productIdx = pl.productIdx AND pl.userIdx = ? " +
                "                LEFT JOIN User u ON pl.userIdx = u.userIdx " +
                "        WHERE p.status = 'y' AND pl.likeStatus = 'y') S2 GROUP BY storeIdx";
        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetLikedProductStoreRes(
                        rs.getInt("storeIdx"),
                        rs.getString("storeName"),
                        rs.getInt("productCount")
                ),userIdx
        );
    }

    /** 특정 스토어에서 찜한 상품 목록 조회 */
    public List<GetLikedProductRes> getLikedProductStoreDetail(int userIdx) {
        String getProductQuery = "SELECT " +
                "            p.productIdx, " +
                "            s.storeIdx, " +
                "            s.storeName, " +
                "            pi.productImgUrl, " +
                "            p.productName, " +
                "            p.price, " +
                "            p.discountRate, " +
                "            p.shippingCost, " +
                "            p.isNew, " +
                "            p.isQuick, " +
                "            p.isBrand, " +
                "            p.isZdiscount, " +
                "            pl.folderIdx, " +
                "            f.folderName " +
                "        FROM " +
                "            Product p " +
                "                join Store s on p.storeIdx = s.storeIdx " +
                "                left join ProductLike pl on p.productIdx = pl.productIdx AND pl.userIdx = ?" +
                "                left join ProductImg pi on p.productIdx = pi.productIdx " +
                "                left join User u on pl.userIdx = u.userIdx " +
                "                left join Folder f on pl.folderIdx = f.folderIdx " +
                "        where p.status = 'y' AND pl.likeStatus = 'y' AND s.storeIdx = 2 " +
                "        ORDER BY " +
                "            p.productIdx";
        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetLikedProductRes(
                        rs.getInt("productIdx"),
                        rs.getInt("storeIdx"),
                        rs.getString("storeName"),
                        rs.getString("productImgUrl"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getInt("discountRate"),
                        rs.getInt("shippingCost"),
                        rs.getString("isNew"),
                        rs.getString("isQuick"),
                        rs.getString("isBrand"),
                        rs.getString("isZdiscount"),
                        rs.getInt("folderIdx"),
                        rs.getString("folderName")
                ),userIdx
        );
    }

    /** 찜한 상품 정보 수정*/
    public int updateLikedProduct(PostFolderReq postReq) {
        String getProductQuery = "UPDATE ProductLike " +
                "    SET status = 'n' " +
                "    WHERE userIdx = ? AND folderIdx = ?";

        Object[] deleteParams = new Object[]{postReq.getUserIdx(), postReq.getFolderIdx()};

        return this.jdbcTemplate.update(getProductQuery,deleteParams);
    }

    /** 폴더 추가*/
    public int addLikedProductFolder(PostFolderReq postReq) {
        String getProductQuery = "INSERT INTO Folder (userIdx, " +
                "                    folderName, " +
                "                    status, " +
                "                    createdAt, " +
                "                    updatedAt) " +
                "                VALUES (?, ?, 'y', current_timestamp, current_timestamp)";

        Object[] deleteParams = new Object[]{postReq.getUserIdx(), postReq.getFolderName()};

        return this.jdbcTemplate.update(getProductQuery,deleteParams);
    }

    /** 폴더 삭제*/
    public int deleteLikedProductFolder(PostFolderReq postReq) {
        String getProductQuery = "UPDATE Folder " +
                "    SET status = 'n' " +
                "    WHERE userIdx = ? AND folderIdx = ?";

        Object[] deleteParams = new Object[]{postReq.getUserIdx(), postReq.getFolderIdx()};

        return this.jdbcTemplate.update(getProductQuery,deleteParams);
    }

    /** 유저 정보 수정 */
    public int updateUser(PatchUserReq patchUserReq) {
        String updateUserQuery = "UPDATE User " +
                "    SET name = ?, phoneNumber = ? " +
                "    WHERE userIdx = ?";

        Object[] updateParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getPhoneNumber(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(updateUserQuery,updateParams);
    }

    /** 유저 체형정보 등록 */
    public int createUserPhysical(PostUserPhysicalReq postUserPhysicalReq) {
        String createUserPhysicalQuery = "UPDATE User " +
                "    SET height = ?, weight = ?, topSize = ?, bottomSize = ?, shoesSize = ? " +
                "    WHERE userIdx = ?";

        Object[] createParams = new Object[]{postUserPhysicalReq.getHeight(), postUserPhysicalReq.getWeight(),
                postUserPhysicalReq.getTopSize(), postUserPhysicalReq.getBottomSize(),
                postUserPhysicalReq.getShoesSize(), postUserPhysicalReq.getUserIdx()};

        return this.jdbcTemplate.update(createUserPhysicalQuery,createParams);
    }

    /** 유저 체형정보 수정 */
    public int updateUserPhysical(PostUserPhysicalReq postUserPhysicalReq) {
        String updateUserPhysicalQuery = "UPDATE User " +
                "    SET height = ?, weight = ?, topSize = ?, bottomSize = ?, shoesSize = ? " +
                "    WHERE userIdx = ?";

        Object[] updateParams = new Object[]{postUserPhysicalReq.getHeight(), postUserPhysicalReq.getWeight(),
                postUserPhysicalReq.getTopSize(), postUserPhysicalReq.getBottomSize(),
                postUserPhysicalReq.getShoesSize(), postUserPhysicalReq.getUserIdx()};

        return this.jdbcTemplate.update(updateUserPhysicalQuery,updateParams);
    }

    /** 유저 환불계좌 등록 */
    public int createUserAccount(PostUserRefundAccountReq postAccountReq) {
        String getProductQuery = "INSERT INTO Refund(bankName, accountNumber, accountHolder, status, createdAt, updatedAt) " +
                "   VALUES (?, ?, ?, 'y', current_timestamp, current_timestamp)";

        Object[] deleteParams = new Object[]{postAccountReq.getBankName(), postAccountReq.getAccountNumber(), postAccountReq.getAccountHolder()};

        return this.jdbcTemplate.update(getProductQuery,deleteParams);
    }

    /** 회원탈퇴 */
    public int deleteUser(int userIdx) {
        String getProductQuery = "UPDATE User " +
                "   SET status = 'n' " +
                "   WHERE userIdx = ?";

        return this.jdbcTemplate.update(getProductQuery,userIdx);
    }
}
