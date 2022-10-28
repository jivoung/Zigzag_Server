package com.example.demo.src.review;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.review.model.*;
import com.example.demo.src.user.model.DeleteFavoriteStoreReq;
import com.example.demo.src.user.model.PostFavoriteStoreReq;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class ReviewService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewDao reviewDao;
    private final ReviewProvider reviewProvider;
    private final JwtService jwtService;


    @Autowired
    public ReviewService(ReviewDao reviewDao, ReviewProvider reviewProvider, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.reviewProvider = reviewProvider;
        this.jwtService = jwtService;
    }

    /**리뷰 등록*/
    public String createReview(int userIdx, PostReviewReq postReviewReq) throws BaseException {
        try{
            int result = reviewDao.createReview(userIdx, postReviewReq);
            String msg ="";
            if(result == 0){
                msg = "등록 실패";
            }else{
                msg = "등록 성공";
            }
            return msg;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**리뷰 수정*/
    public String updateReview(int userIdx, PostReviewReq postReviewReq) throws BaseException {
        try{
            int result = reviewDao.updateReview(userIdx, postReviewReq);
            String msg ="";
            if(result == 0){
                msg = "수정 실패";
            }else{
                msg = "수정 성공";
            }
            return msg;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**리뷰 평가 등록*/
    public String createReviewEval(int reviewIdx, PostReviewEvalReq postReviewEvalReq) throws BaseException {
        if(reviewDao.checkReviewEvalPost(reviewIdx, postReviewEvalReq.getUserIdx())==1){
            throw new BaseException(FAILED_TO_REVIEW_EVAL_REGISTER);
        }
        try{
            int result = reviewDao.createReviewEval(reviewIdx, postReviewEvalReq);
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

    /**리뷰 평가 변경*/
    public String updateReviewEval(int reviewIdx, PatchReviewEvalReq patchReviewEvalReq) throws BaseException {
        if(reviewDao.checkReviewEvalPatch(reviewIdx, patchReviewEvalReq.getUserIdx(), patchReviewEvalReq.getEvalValue())==1){
            throw new BaseException(FAILED_TO_REVIEW_EVAL_UPDATE);
        }
        try{
            int result = reviewDao.updateReviewEval(reviewIdx, patchReviewEvalReq);
            String msg ="";
            if(result == 0){
                msg = "변경 실패";
            }
            else{
                msg = "변경 성공";
            }
            return msg;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**리뷰 평가 취소*/
    public String deleteReviewEval(DeleteReviewEvalReq deleteReviewEvalReq) throws BaseException {
        try{
            int result = reviewDao.deleteReviewEval(deleteReviewEvalReq);
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
}