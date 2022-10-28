package com.example.demo.src.review;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/reviews")
public class ReviewController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ReviewProvider reviewProvider;
    @Autowired
    private final ReviewService reviewService;
    @Autowired
    private final JwtService jwtService;


    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService){
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    /**
     * 작성 가능한 리뷰 조회 API
     * [GET] reviews/possible/:userIdx
     * @return BaseResponse<List<GetPossibleReviewRes>>
     */
    @ResponseBody
    @GetMapping("/possible/{userIdx}")
    public BaseResponse<List<GetPossibleReviewRes>> getPossibleReview(@PathVariable("userIdx") int userIdx){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            List<GetPossibleReviewRes> getPossibleReviewRes = reviewProvider.getPossibleReview(userIdx);
            return new BaseResponse<>(getPossibleReviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 작성한 리뷰 조회 API
     * [GET] :userIdx
     * @return BaseResponse<List<GetReviewRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetReviewRes>> getReview(@PathVariable("userIdx") int userIdx){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            List<GetReviewRes> getReviewRes = reviewProvider.getReview(userIdx);
            return new BaseResponse<>(getReviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 리뷰 등록 API
     * [POST] /:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<String> createReview(@PathVariable("userIdx") int userIdx, @RequestBody PostReviewReq postReviewReq){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            String getPossibleReviewRes = reviewService.createReview(userIdx, postReviewReq);
            return new BaseResponse<>(getPossibleReviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 리뷰 수정 API
     * [PATCH] /:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> updateReview(@PathVariable("userIdx") int userIdx, @RequestBody PostReviewReq postReviewReq){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            String getPossibleReviewRes = reviewService.updateReview(userIdx, postReviewReq);
            return new BaseResponse<>(getPossibleReviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 상품의 구매 만족도 조회 API
     * [GET] /:productIdx/result
     * @return BaseResponse<List<GetReviewRes>>
     */
    @ResponseBody
    @GetMapping("/{productIdx}/result")
    public BaseResponse<List<GetReviewResultRes>> getReviewResult(@PathVariable("productIdx") int productIdx){
        try{
            List<GetReviewResultRes> getPossibleReviewRes = reviewProvider.getReviewResult(productIdx);
            return new BaseResponse<>(getPossibleReviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 상품의 전체 리뷰 조회 API
     * [GET] /:productIdx
     * @return BaseResponse<List<GetReviewRes>>
     */
    @ResponseBody
    @GetMapping("/product/{productIdx}")
    public BaseResponse<List<GetReviewAllRes>> getReviewAll(@PathVariable("productIdx") int productIdx){
        try{
            List<GetReviewAllRes> getPossibleReviewRes = reviewProvider.getReviewAll(productIdx);
            return new BaseResponse<>(getPossibleReviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 리뷰 평가 등록 API
     * [POST] reviews/:reviewIdx/evaluation
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{reviewIdx}/evaluation")
    public BaseResponse<String> createReviewEval(@PathVariable("reviewIdx") int reviewIdx, @RequestBody PostReviewEvalReq postReviewEvalReq){
        try{
            if(postReviewEvalReq.getUserIdx()==0){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if(postReviewEvalReq.getEvalValue()==null){
                return new BaseResponse<>(USERS_EMPTY_EVAL_VALUE);
            }
            if(reviewProvider.checkReviewIdx(reviewIdx)==0){
                return new BaseResponse<>(INVALID_REVIEW_ID);
            }
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdxByJwt != postReviewEvalReq.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            String result = reviewService.createReviewEval(reviewIdx, postReviewEvalReq);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 리뷰 평가 변경 API
     * [PATCH] reviews/:reviewIdx/evaluation
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{reviewIdx}/evaluation")
    public BaseResponse<String> updateReviewEval(@PathVariable("reviewIdx") int reviewIdx, @RequestBody PatchReviewEvalReq patchReviewEvalReq){
        try{
            if(patchReviewEvalReq.getUserIdx()==0){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if(patchReviewEvalReq.getEvalValue()==null){
                return new BaseResponse<>(USERS_EMPTY_EVAL_VALUE);
            }
            if(reviewProvider.checkReviewIdx(reviewIdx)==0){
                return new BaseResponse<>(INVALID_REVIEW_ID);
            }
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdxByJwt != patchReviewEvalReq.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            String result = reviewService.updateReviewEval(reviewIdx, patchReviewEvalReq);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 리뷰 평가 취소 API
     * [PATCH] reviews/:reviewIdx/evaluation/deletion
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{reviewIdx}/evaluation/deletion")
    public BaseResponse<String> deleteReviewEval(@PathVariable("reviewIdx") int reviewIdx, @RequestBody DeleteReviewEvalReq deleteReviewEvalReq){
        try{
            if(deleteReviewEvalReq.getUserIdx()==0){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if(deleteReviewEvalReq.getReviewIdx()==0){
                return new BaseResponse<>(USERS_EMPTY_REVIEW_ID);
            }
            if(reviewProvider.checkReviewIdx(reviewIdx)==0 || reviewProvider.checkReviewIdx(deleteReviewEvalReq.getReviewIdx())==0 || reviewIdx != deleteReviewEvalReq.getReviewIdx()){
                return new BaseResponse<>(INVALID_REVIEW_ID);
            }
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdxByJwt != deleteReviewEvalReq.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(reviewIdx != deleteReviewEvalReq.getReviewIdx()){
                return new BaseResponse<>(INVALID_REVIEW_ID);
            }
            //같다면 아래 과정 진행
            String result = reviewService.deleteReviewEval(deleteReviewEvalReq);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}