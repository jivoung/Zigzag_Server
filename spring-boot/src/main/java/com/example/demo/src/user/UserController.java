package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        //비밀번호 정규표현
        if(!isRegexPassword(postUserReq.getPassword())){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        //이메일 정규표현
        if(!isRegexEmail(postLoginReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        //비밀번호 정규표현
        if(!isRegexPassword(postLoginReq.getPassword())){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }
        try{
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 특정 유저 조회 (마이페이지 상단) API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserExtendRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetUserExtendRes> getUser(@PathVariable("userIdx") int userIdx) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            GetUserExtendRes getUserExtendRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserExtendRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 즐겨찾기한 스토어 목록 조회 API
     * [GET] /users/:userIdx/favorite-stores
     * @return BaseResponse<List<GetFavoriteStoreRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}/favorite-stores") // (GET) 127.0.0.1:9000/app/users/:userIdx/favorite-stores
    public BaseResponse<GetFavoriteStoreExtendRes> getFavoriteStoreList(@PathVariable("userIdx") int userIdx, @RequestParam(required=false) boolean couponPossible){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            if(couponPossible){
                GetFavoriteStoreExtendRes getFavoriteStoreExtendRes = userProvider.getFavoriteStoreListCoupon(userIdx);
                return new BaseResponse<>(getFavoriteStoreExtendRes);
            }
            GetFavoriteStoreExtendRes getFavoriteStoreExtendRes = userProvider.getFavoriteStoreList(userIdx);
            return new BaseResponse<>(getFavoriteStoreExtendRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토어 즐겨찾기 등록 API
     * [POST] /users/:userIdx/favorite-stores
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{userIdx}/favorite-stores")
    public BaseResponse<String> createFavoriteStore(@PathVariable("userIdx") int userIdx, @RequestBody PostFavoriteStoreReq postFavoriteStoreReq){
        try{
            if(postFavoriteStoreReq.getUserIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if(postFavoriteStoreReq.getStoreIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_STORE_ID);
            }
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdxByJwt != postFavoriteStoreReq.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            String result = userService.createFavoriteStore(postFavoriteStoreReq);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토어 즐겨찾기 취소 API
     * [PATCH] /users/:userIdx/favorite-stores
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/favorite-stores")
    public BaseResponse<String> deleteFavoriteStore(@PathVariable("userIdx") int userIdx, @RequestBody DeleteFavoriteStoreReq deleteFavoriteStoreReq){
        try{
            if(deleteFavoriteStoreReq.getUserIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if(deleteFavoriteStoreReq.getStoreIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_STORE_ID);
            }
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdxByJwt != deleteFavoriteStoreReq.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            String result = userService.deleteFavoriteStore(deleteFavoriteStoreReq);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 즐겨찾기한 스토어 스토리 목록 조회 API
     * [GET] users/:userIdx/favorite-stores/stories
     * @return BaseResponse<List<GetFavoriteStoreRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}/favorite-stores/stories") // (GET) 127.0.0.1:9000/app/users/:userIdx/favorite-stores/stories
    public BaseResponse<List<GetFavoriteStoreStoryRes>> getFavoriteStoreStoryList(@PathVariable("userIdx") int userIdx){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            List<GetFavoriteStoreStoryRes> getFavoriteStoreStoryRes = userProvider.getFavoriteStoreStoryList(userIdx);
            return new BaseResponse<>(getFavoriteStoreStoryRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 즐겨찾기한 스토어 스토리 상세 조회 API
     * [GET] users/:userIdx/favorite-stores/:storeIdx/stories
     * @return BaseResponse<List<GetFavoriteStoreRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}/favorite-stores/{storeIdx}/stories") // (GET) 127.0.0.1:9000/app/users/:userIdx/favorite-stores/:storeIdx/stories
    public BaseResponse<List<GetFavoriteStoreStoryContentRes>> getFavoriteStoreStoryContentList(@PathVariable("userIdx") int userIdx, @PathVariable("storeIdx") int storeIdx){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(userProvider.checkStoreIdx(storeIdx)==0){
                return new BaseResponse<>(INVALID_STORE_ID);
            }
            List<GetFavoriteStoreStoryContentRes> getFavoriteStoreStoryContentRes = userProvider.getFavoriteStoreStoryContentList(userIdx,storeIdx);
            return new BaseResponse<>(getFavoriteStoreStoryContentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 전체 주문,배송 상품 조회 API
     * [GET] /users/:userIdx/order-products
     * @return BaseResponse<List<GetUserProductRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}/order-products") // (GET) 127.0.0.1:9000/app/users/:userIdx/order-products
    public BaseResponse<List<GetUserProductRes>> getUserProductList(@PathVariable("userIdx") int userIdx){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetUserProductRes> getUserProductRes = userProvider.getUserProductList(userIdx);
            return new BaseResponse<>(getUserProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 상품 주문 정보 조희 API
     * [GET] /users/:userIdx/order-products/:orderProductIdx
     * @return BaseResponse<GetUserProductDetailRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}/order-products/{orderProductIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx/order-products/:orderProductIdx
    public BaseResponse<GetUserProductDetailRes> getUserProductDetail(@PathVariable("userIdx") int userIdx, @PathVariable("orderProductIdx") int orderProductIdx) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(userProvider.checkOrderProductIdx(userIdx, orderProductIdx)==0){
                return new BaseResponse<>(INVALID_ORDER_PRODUCT_ID);
            }
            //같다면 아래 과정 진행
            GetUserProductDetailRes getUserProductDetailRes = userProvider.getUserProductDetail(orderProductIdx);
            return new BaseResponse<>(getUserProductDetailRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 특정 상품 주문 정보 삭제 API
     * [PATCH] /users/:userIdx/order-products/:orderProductIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/order-products/{orderProductIdx}")
    public BaseResponse<String> deleteUserProduct(@PathVariable("userIdx") int userIdx, @PathVariable("orderProductIdx") int orderProductIdx){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdxByJwt != userIdx){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            String result = userService.deleteUserProduct(userIdx, orderProductIdx);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 쿠폰 등록 API
     * [POST] /users/:userIdx/coupons
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{userIdx}/coupons")
    public BaseResponse<String> createUserCoupon(@PathVariable("userIdx") int userIdx, @RequestBody PostCouponReq postCouponReq){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdxByJwt != postCouponReq.getUserIdx() || userIdxByJwt != userIdx ){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(postCouponReq.getUserIdx()==0){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if(postCouponReq.getCouponIdx()==0){
                return new BaseResponse<>(EMPTY_COUPON_ID);
            }
            if(userProvider.checkCouponIdx(postCouponReq.getCouponIdx())==0){
                return new BaseResponse<>(INVALID_COUPON_ID);
            }
            //같다면 아래 과정 진행
            String result = userService.createUserCoupon(postCouponReq);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토어 쿠폰 내려받기 API
     * [POST] /users/:userIdx/:storeIdx/coupons
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{userIdx}/{storeIdx}/coupons")
    public BaseResponse<String> createStoreCoupon(@PathVariable("userIdx") int userIdx, @PathVariable("storeIdx") int storeIdx, @RequestBody(required = false) PostStoreCouponReq postStoreCouponReq){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdxByJwt != userIdx){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            if(postStoreCouponReq.getStoreIdx()==0){
                return new BaseResponse<>(USERS_EMPTY_STORE_ID);
            }
            if(postStoreCouponReq.getCouponIdx()==0){
                return new BaseResponse<>(EMPTY_COUPON_ID);
            }
            if(userProvider.checkStoreIdx(storeIdx)==0 || userProvider.checkStoreIdx(postStoreCouponReq.getStoreIdx())==0 || storeIdx != postStoreCouponReq.getStoreIdx()){
                return new BaseResponse<>(INVALID_STORE_ID);
            }
            String result = userService.createStoreCoupon(userIdx, postStoreCouponReq);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토어별 쿠폰 조회 API
     * [GET] /users/:userIdx/:storeIdx/coupons
     * @return BaseResponse <GetStoreCouponExtendRes>
     */
    @ResponseBody
    @GetMapping("/{userIdx}/{storeIdx}/coupons")
    public BaseResponse<GetStoreCouponExtendRes> getStoreCoupons(@PathVariable("userIdx") int userIdx, @PathVariable("storeIdx") int storeIdx){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdxByJwt != userIdx){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(userProvider.checkStoreIdx(storeIdx)==0){
                return new BaseResponse<>(INVALID_STORE_ID);
            }
            GetStoreCouponExtendRes getStoreCouponExtendRes = userProvider.getStoreCoupons(storeIdx);
            return new BaseResponse<>(getStoreCouponExtendRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저가 보유하고 있는 쿠폰 조회 API
     * [GET] /users/:userIdx/coupons
     * @return BaseResponse <GetUserCouponExtendRes>
     */
    @ResponseBody
    @GetMapping("/{userIdx}/coupons")
    public BaseResponse<GetUserCouponExtendRes> getUserCoupons(@PathVariable("userIdx") int userIdx, @RequestParam(required=false) String couponType){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdxByJwt != userIdx){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            if(couponType != null){
                GetUserCouponExtendRes getUserCouponExtendRes = userProvider.getUserCouponsType(userIdx, couponType);
                return new BaseResponse<>(getUserCouponExtendRes);
            }
            GetUserCouponExtendRes getUserCouponExtendRes = userProvider.getUserCoupons(userIdx);
            return new BaseResponse<>(getUserCouponExtendRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저가 보유하고 있는 완료,만료 쿠폰 조회 API
     * [GET] /users/:userIdx/expiry-coupons
     * @return BaseResponse <List<GetExpiredUserCouponRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}/expiry-coupons")
    public BaseResponse<List<GetExpiredUserCouponRes>> getExpiredUserCoupons(@PathVariable("userIdx") int userIdx){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdxByJwt != userIdx){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            List<GetExpiredUserCouponRes> getExpiredUserCouponRes = userProvider.getExpiredUserCoupons(userIdx);
            return new BaseResponse<>(getExpiredUserCouponRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 포인트 정보 조회 API
     * [GET] /users/:userIdx/point
     * @return BaseResponse<GetUserPointListRes>
     */
    @ResponseBody
    @GetMapping("/{userIdx}/point") // (GET) 127.0.0.1:9000/app/users/:userIdx/point
    public BaseResponse<GetUserPointListRes> getUserPointList(@PathVariable("userIdx") int userIdx, @RequestParam(required=false) String pointType) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            if(pointType != null){
                GetUserPointListRes getUserPointListRes = userProvider.getUserPointListType(userIdx, pointType);
                return new BaseResponse<>(getUserPointListRes);
            }
            GetUserPointListRes getUserPointListRes = userProvider.getUserPointList(userIdx);
            return new BaseResponse<>(getUserPointListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 찜한 상품 조회 API
     * [GET] /:userIdx/liked-products
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}/liked-products") // (GET) 127.0.0.1:9000/app/users/:userIdx/liked-products
    public BaseResponse<List<GetLikedProductRes>> getLikedProduct(@PathVariable int userIdx) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            List<GetLikedProductRes> getProductRes = userProvider.getLikedProduct(userIdx);
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 찜한 상품 스토어별 조회 API
     * [GET] /:userIdx/liked-products/store
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}/liked-products/store") // (GET) 127.0.0.1:9000/app/users/:userIdx/liked-products/store
    public BaseResponse<List<GetLikedProductStoreRes>> getLikedProductStore(@PathVariable int userIdx) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            List<GetLikedProductStoreRes> getProductRes = userProvider.getLikedProductStore(userIdx);
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 특정 스토어에서 찜한 상품 목록 조회 API
     * [GET] /:userIdx/liked-products/store/:storeIdx
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}/liked-products/store/{storeIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx/liked-products/store/:storeIdx
    public BaseResponse<List<GetLikedProductRes>> getLikedProductStoreDetail(@PathVariable int userIdx) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            List<GetLikedProductRes> getProductRes = userProvider.getLikedProductStoreDetail(userIdx);
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 찜한 상품 정보 수정 API
     * [PATCH] /:userIdx/liked-products/:likeProductIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/liked-products/{likeProductIdx}") // (PATCH) 127.0.0.1:9000/app/users/:userIdx/liked-products/:likeProductIdx
    public BaseResponse<String> updateLikedProduct(@RequestBody PostFolderReq postReq, @PathVariable int userIdx) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(postReq.getUserIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if(postReq.getFolderIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_FOLDER_ID);
            }
            String getProductRes = userService.updateLikedProduct(postReq);
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 폴더 추가 API
     * [POST] /:userIdx/liked-products/folder
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{userIdx}/liked-products/folder") // (POST) 127.0.0.1:9000/app/users/:userIdx/liked-products/folder
    public BaseResponse<String> addFolder(@RequestBody PostFolderReq postReq, @PathVariable int userIdx) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(postReq.getUserIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if(postReq.getFolderName() == null){
                return new BaseResponse<>(USERS_EMPTY_FOLDER_NAME);
            }
            String getFolderRes = userService.addLikedProductFolder(postReq);
            return new BaseResponse<>(getFolderRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 폴더 삭제 API
     * [PATCH] /:userIdx/liked-products/folder/:folderIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/liked-products/folder/{folderIdx}") // (PATCH) 127.0.0.1:9000/app/users/:userIdx/liked-products/folder/:folderIdx
    public BaseResponse<String> deleteFolder(@RequestBody PostFolderReq postReq, @PathVariable int userIdx) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(postReq.getUserIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if(postReq.getFolderIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_FOLDER_ID);
            }
            String getProductRes = userService.deleteLikedProductFolder(postReq);
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 정보 수정 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PatchMapping("/{userIdx}") // (PATCH) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<String> updateUser(@PathVariable("userIdx") int userIdx, @RequestBody PatchUserReq patchUserReq) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            String updateUserRes = userService.updateUser(patchUserReq);
            return new BaseResponse<>(updateUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 체형정보 등록 API
     * [POST] /users/:userIdx/physical-size
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PostMapping("/{userIdx}/physical-size") // (POST) 127.0.0.1:9000/app/users/:userIdx/physical-size
    public BaseResponse<String> createUserPhysical(@PathVariable("userIdx") int userIdx, @RequestBody PostUserPhysicalReq postUserPhysicalReq) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            String getUserExtendRes = userService.createUserPhysical(postUserPhysicalReq);
            return new BaseResponse<>(getUserExtendRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 체형정보 수정 API
     * [PATCH] /users/:userIdx/physical-size
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PatchMapping("/{userIdx}/physical-size") // (PATCH) 127.0.0.1:9000/app/users/:userIdx/physical-size
    public BaseResponse<String> updateUserPhysical(@PathVariable("userIdx") int userIdx, @RequestBody PostUserPhysicalReq postUserPhysicalReq) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            String getUserExtendRes = userService.updateUserPhysical(postUserPhysicalReq);
            return new BaseResponse<>(getUserExtendRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 환불계좌 등록 API
     * [POST] /users/:userIdx/refund-account
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PostMapping("/{userIdx}/refund-account") // (POST) 127.0.0.1:9000/app/users/:userIdx/refund-account
    public BaseResponse<String> createUserAccount(@PathVariable("userIdx") int userIdx, @RequestBody PostUserRefundAccountReq postAccountReq) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            String getUserExtendRes = userService.createUserAccount(postAccountReq);
            return new BaseResponse<>(getUserExtendRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원탈퇴 API
     * [PATCH] /users/:userIdx/delection
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PatchMapping("/{userIdx}/delection") // (PATCH) 127.0.0.1:9000/app/users/:userIdx/delection
    public BaseResponse<String> deleteUser(@PathVariable("userIdx") int userIdx) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            String getUserExtendRes = userService.deleteUser(userIdx);
            return new BaseResponse<>(getUserExtendRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
