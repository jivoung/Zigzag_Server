package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    /**이메일 확인*/
    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**로그인*/
    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        User user = userDao.getPwd(postLoginReq);
        String password;
        if(user == null){
            throw new BaseException(FAILED_TO_LOGIN);
        }
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword());

        } catch (Exception exception) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }
        if(!user.getStatus().equals("activity")){
            throw new BaseException(FAILED_TO_LOGIN);
        }
        else if(postLoginReq.getPassword().equals(password)){
            int userIdx = userDao.getPwd(postLoginReq).getUserIdx();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    /**특정 유저 조회 (마이페이지 상단)*/
    public GetUserExtendRes getUser(int userIdx) throws BaseException {
        try {
            String name = userDao.getUser(userIdx).getName();
            String email = userDao.getUser(userIdx).getEmail();
            String membershipType = userDao.getUser(userIdx).getMembershipType();
            int orderDeliveryCount = userDao.getOrderDeliveryCount(userIdx);
            int reviewCount = userDao.getReviewCount(userIdx);
            int couponCount = userDao.getCouponCount(userIdx);
            int point= userDao.getPointSum(userIdx);
            return new GetUserExtendRes(userIdx, name,email, membershipType,orderDeliveryCount,reviewCount, couponCount,point);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**즐겨찾기한 스토어 조회*/
    public GetFavoriteStoreExtendRes getFavoriteStoreList(int userIdx) throws BaseException{
        try{
            int count = userDao.getFavoriteStoreNum(userIdx);

            List<GetFavoriteStoreRes> result = new ArrayList<>();
            List<FavoriteStore> favoriteStoreList = userDao.getFavoriteStoreResults(userIdx);
            for(FavoriteStore favoriteStore : favoriteStoreList ) {
                int storeIdx = favoriteStore.getStoreIdx();
                String storeImg = favoriteStore.getStoreImg() ;
                String storeName = favoriteStore.getStoreName();
                int maxCouponPrice = userDao.getMaxCouponPrice(storeIdx);
                int newProductCount = userDao.getNewProductCount(storeIdx);
                result.add(new GetFavoriteStoreRes(storeIdx, storeImg, storeName,maxCouponPrice,newProductCount));
            }
            return new GetFavoriteStoreExtendRes(count, result);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**즐겨찾기한 스토어 스토리 목록 조회*/
    public List<GetFavoriteStoreStoryRes> getFavoriteStoreStoryList(int userIdx) throws BaseException{
        try{
            List<GetFavoriteStoreStoryRes> getFavoriteStoreStoryRes = userDao.getFavoriteStoreStoryList(userIdx);
            return getFavoriteStoreStoryRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    /**즐겨찾기한 스토어 스토리 목록 조회 - 쿠폰 제공하는 스토어*/
    public GetFavoriteStoreExtendRes getFavoriteStoreListCoupon(int userIdx) throws BaseException{
        try{
            int count = userDao.getFavoriteStoreNum(userIdx);

            List<GetFavoriteStoreRes> result = new ArrayList<>();
            List<FavoriteStore> favoriteStoreCouponList = userDao.getFavoriteStoreResultsCoupon(userIdx);
            for(FavoriteStore favoriteStore : favoriteStoreCouponList ) {
                int storeIdx = favoriteStore.getStoreIdx();
                String storeImg = favoriteStore.getStoreImg() ;
                String storeName = favoriteStore.getStoreName();
                int maxCouponPrice = userDao.getMaxCouponPrice(storeIdx);
                int newProductCount = userDao.getNewProductCount(storeIdx);
                result.add(new GetFavoriteStoreRes(storeIdx, storeImg, storeName,maxCouponPrice,newProductCount));
            }
            return new GetFavoriteStoreExtendRes(count, result);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**즐겨찾기한 스토어 스토리 상세 조회*/
    public List<GetFavoriteStoreStoryContentRes> getFavoriteStoreStoryContentList(int userIdx, int storeIdx) throws BaseException{
        try{
            List<GetFavoriteStoreStoryContentRes> getFavoriteStoreStoryContentRes = userDao.getFavoriteStoreStoryContentList(userIdx, storeIdx);
            return getFavoriteStoreStoryContentRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    /**스토어 식별자 확인*/
    public int checkStoreIdx(int storeIdx)throws BaseException {
        try{
            int result = userDao.checkStoreIdx(storeIdx);
            return result;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**전체 주문,배송 상품 조회*/
    public List<GetUserProductRes> getUserProductList(int userIdx) throws BaseException{
        try{
            List<GetUserProductRes> getUserProductRes = userDao.getUserProductList(userIdx);
            return getUserProductRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**특정 상품 주문 정보 조희*/
    public GetUserProductDetailRes getUserProductDetail(int orderProductIdx) throws BaseException{
        try{
            GetUserProductDetailRes getUserProductDetailRes = userDao.getUserProductDetail(orderProductIdx);
            return getUserProductDetailRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    /**구매한 상품 식별자 확인*/
    public int checkOrderProductIdx(int userIdx, int orderProductIdx)throws BaseException {
        try{
            int result = userDao.checkOrderProductIdx(userIdx,orderProductIdx);
            return result;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**쿠폰 식별자 확인*/
    public int checkCouponIdx(int couponIdx)throws BaseException {
        try{
            int result = userDao.checkCouponIdx(couponIdx);
            return result;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**스토어 쿠폰 식별자 확인*/
    public int checkStoreCouponIdx(int storeIdx, int couponIdx)throws BaseException {
        try{
            int result = userDao.checkStoreCouponIdx(storeIdx, couponIdx);
            return result;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**스토어별 쿠폰 조회*/
    public GetStoreCouponExtendRes getStoreCoupons(int storeIdx) throws BaseException{
        try{
            int count = userDao.getStoreCoupon(storeIdx);
            List<GetStoreCouponRes> getStoreCouponRes = userDao.getStoreCoupons(storeIdx);
            return new GetStoreCouponExtendRes(count, getStoreCouponRes);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**유저가 보유하고 있는 쿠폰 조회*/
    public GetUserCouponExtendRes getUserCoupons(int userIdx) throws BaseException{
        try{
            int count = userDao.getCouponCount(userIdx);
            int zigzagCount = userDao.getZigzagCouponCount(userIdx);
            int storeCount = userDao.getStoreCouponCount(userIdx);
            int deliveryCount = userDao.getDeliveryCouponCount(userIdx);
            List<GetUserCouponRes> getUserCouponRes = userDao.getUserCoupons(userIdx);
            return new GetUserCouponExtendRes(count, zigzagCount, storeCount, deliveryCount, getUserCouponRes);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    /**유저가 보유하고 있는 쿠폰 조회 - 지그재그/직진배송/스토어*/
    public GetUserCouponExtendRes getUserCouponsType(int userIdx, String couponType) throws BaseException{
        try{
            int count = userDao.getCouponCount(userIdx);
            int zigzagCount = userDao.getZigzagCouponCount(userIdx);
            int storeCount = userDao.getStoreCouponCount(userIdx);
            int deliveryCount = userDao.getDeliveryCouponCount(userIdx);
            List<GetUserCouponRes> getUserCouponZigzagRes = userDao.getUserCouponsType(userIdx, couponType);
            return new GetUserCouponExtendRes(count, zigzagCount, storeCount, deliveryCount, getUserCouponZigzagRes);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**유저가 보유하고 있는 완료,만료 쿠폰 조회*/
    public List<GetExpiredUserCouponRes> getExpiredUserCoupons(int userIdx) throws BaseException{
        try{
            List<GetExpiredUserCouponRes> getExpiredUserCouponRes = userDao.getExpiredUserCoupons(userIdx);
            return getExpiredUserCouponRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**유저 포인트 정보 조희*/
    public GetUserPointListRes getUserPointList(int userIdx) throws BaseException{
        try{
            int pointSum = userDao.getPointSum(userIdx);
            List<GetUserPointRes> userPointList = userDao.getUserPointList(userIdx);
            return new GetUserPointListRes(pointSum, userPointList);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    /**유저 포인트 정보 조희 - 적립/사용/소멸*/
    public GetUserPointListRes getUserPointListType(int userIdx, String pointType) throws BaseException{
        try{
            int pointSum = userDao.getPointSum(userIdx);
            List<GetUserPointRes> userPointList = userDao.getUserPointListType(userIdx, pointType);
            return new GetUserPointListRes(pointSum, userPointList);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 찜한 상품 조회*/
    public List<GetLikedProductRes> getLikedProduct(int userIdx) throws BaseException{
        try{
            List<GetLikedProductRes> getProductRes = userDao.getLikedProduct(userIdx);
            return getProductRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 찜한 상품 스토어별 조회*/
    public List<GetLikedProductStoreRes> getLikedProductStore(int userIdx) throws BaseException{
        try{
            List<GetLikedProductStoreRes> getProductRes = userDao.getLikedProductStore(userIdx);
            return getProductRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 특정 스토어에서 찜한 상품 목록 조회 */
    public List<GetLikedProductRes> getLikedProductStoreDetail(int userIdx) throws BaseException{
        try{
            List<GetLikedProductRes> getProductRes = userDao.getLikedProductStoreDetail(userIdx);
            return getProductRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
