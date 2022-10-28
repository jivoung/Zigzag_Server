package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    /**회원가입*/
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //이메일 중복 확인
        if(userProvider.checkEmail(postUserReq.getEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**스토어 즐겨찾기 등록*/
    public String createFavoriteStore(PostFavoriteStoreReq postFavoriteStoreReq) throws BaseException {
        if(userDao.checkFavoriteStore(postFavoriteStoreReq.getUserIdx(), postFavoriteStoreReq.getStoreIdx())==1){
            throw new BaseException(FAILED_TO_FAVORITE_REGISTER);
        }
        try{
            int result = userDao.createFavoriteStore(postFavoriteStoreReq);
            String msg ="";
            if(result == 0){
                msg = "등록 실패";
            }
            else{
                msg = "등록 성공";
            }
            return msg;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**스토어 즐겨찾기 취소*/
    public String deleteFavoriteStore(DeleteFavoriteStoreReq deleteFavoriteStoreReq) throws BaseException {
        if(userDao.checkFavoriteStore(deleteFavoriteStoreReq.getUserIdx(), deleteFavoriteStoreReq.getStoreIdx())==0){
            throw new BaseException(FAILED_TO_FAVORITE_DELETE);
        }
        try{
            int result = userDao.deleteFavoriteStore(deleteFavoriteStoreReq);
            String msg ="";
            if(result == 0){
                msg = "취소 실패";
            }
            else{
                msg = "취소 성공";
            }
            return msg;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**특정 상품 주문 정보 삭제*/
    public String deleteUserProduct(int userIdx, int orderProductIdx) throws BaseException {
        if(userDao.checkOrderProductIdx(userIdx, orderProductIdx)==0){
            throw new BaseException(DELETE_FAIL_ORDER_PRODUCT);
        }
        try{
            int result = userDao.deleteUserProduct(userIdx, orderProductIdx);
            String msg ="";
            if(result == 0){
                msg = "삭제 실패";
            }
            else{
                msg = "삭제 성공";
            }
            return msg;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**쿠폰 등록*/
    public String createUserCoupon(PostCouponReq postCouponReq) throws BaseException {
        if(userProvider.checkCouponIdx(postCouponReq.getCouponIdx())==0){
            throw new BaseException(INVALID_COUPON_ID);
        }
        if(userDao.checkUserCoupon(postCouponReq.getUserIdx(), postCouponReq.getCouponIdx())==1){
            throw new BaseException(FAILED_TO_COUPON_REGISTER);
        }
        try{
            int result = userDao.createUserCoupon(postCouponReq);
            String msg ="";
            if(result == 0){
                msg = "등록 실패";
            }
            else{
                msg = "등록 성공";
            }
            return msg;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**스토어 쿠폰 등록*/
    public String createStoreCoupon(int userIdx, PostStoreCouponReq postStoreCouponReq) throws BaseException {
        if(userProvider.checkStoreCouponIdx(postStoreCouponReq.getStoreIdx(), postStoreCouponReq.getCouponIdx())==0){
            throw new BaseException(INVALID_COUPON_ID);
        }
        if(userDao.checkUserCoupon(userIdx, postStoreCouponReq.getCouponIdx())==1){
            throw new BaseException(FAILED_TO_COUPON_REGISTER);
        }
        try{
            int result = userDao.createStoreCoupon(userIdx, postStoreCouponReq);
            String msg ="";
            if(result == 0){
                msg = "등록 실패";
            }
            else{
                msg = "등록 성공";
            }
            return msg;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 찜한 상품 정보 수정 API*/
    public String updateLikedProduct(PostFolderReq postReq) throws BaseException {
        try{
            int count = userDao.updateLikedProduct(postReq);
            String msg = "";
            if(count == 0) {
                msg = "수정 실패";
            }else{
                msg = "수정 성공";
            }
            return new String(msg);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 폴더 추가 API*/
    public String addLikedProductFolder(PostFolderReq postReq) throws BaseException {
        try{
            int count = userDao.addLikedProductFolder(postReq);
            String msg = "";
            if(count == 0) {
                msg = "추가 실패";
            }else{
                msg = "추가 성공";
            }
            return new String(msg);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 폴더 삭제 API*/
    public String deleteLikedProductFolder(PostFolderReq postReq) throws BaseException {
        try{
            int count = userDao.deleteLikedProductFolder(postReq);
            String msg = "";
            if(count == 0) {
                msg = "삭제 실패";
            }else{
                msg = "삭제 성공";
            }
            return new String(msg);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 유저 정보 수정  API*/
    public String updateUser(PatchUserReq patchUserReq) throws BaseException {
        try{
            int count = userDao.updateUser(patchUserReq);
            String msg = "";
            if(count == 0) {
                msg = "수정 실패";
            }else{
                msg = "수정 성공";
            }
            return new String(msg);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 유저 체형정보 등록 API*/
    public String createUserPhysical(PostUserPhysicalReq postUserPhysicalReq) throws BaseException {
        try{
            int count = userDao.createUserPhysical(postUserPhysicalReq);
            String msg = "";
            if(count == 0) {
                msg = "등록 실패";
            }else{
                msg = "등록 성공";
            }
            return new String(msg);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 유저 체형정보 수정 API*/
    public String updateUserPhysical(PostUserPhysicalReq postUserPhysicalReq) throws BaseException {
        try{
            int count = userDao.updateUserPhysical(postUserPhysicalReq);
            String msg = "";
            if(count == 0) {
                msg = "수정 실패";
            }else{
                msg = "수정 성공";
            }
            return new String(msg);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 유저 환불계좌 등록 API*/
    public String createUserAccount(PostUserRefundAccountReq postAccountReq) throws BaseException {
        try{
            int count = userDao.createUserAccount(postAccountReq);
            String msg = "";
            if(count == 0) {
                msg = "등록 실패";
            }else{
                msg = "등록 성공";
            }
            return new String(msg);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 회원탈퇴 API*/
    public String deleteUser(int userIdx) throws BaseException {
        try{
            int count = userDao.deleteUser(userIdx);
            String msg = "";
            if(count == 0) {
                msg = "탈퇴 실패";
            }else{
                msg = "탈퇴 성공";
            }
            return new String(msg);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
