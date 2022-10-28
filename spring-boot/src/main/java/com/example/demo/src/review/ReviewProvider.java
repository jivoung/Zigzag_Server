package com.example.demo.src.review;
import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.review.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ReviewProvider {
    private final ReviewDao reviewDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReviewProvider(ReviewDao reviewDao, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.jwtService = jwtService;
    }

    public int checkReviewIdx(int reviewIdx)throws BaseException {
        try{
            int result = reviewDao.checkReviewIdx(reviewIdx);
            return result;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 작성 가능한 리뷰 조회 */
    public List<GetPossibleReviewRes> getPossibleReview(int userIdx) throws BaseException{
        try{
            List<GetPossibleReviewRes> getReviewRes = reviewDao.getPossibleReview(userIdx);
            return getReviewRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 작성한 리뷰 조회 */
    public List<GetReviewRes> getReview(int userIdx) throws BaseException{
        try{
            List<GetReviewRes> getReviewRes = reviewDao.getReview(userIdx);
            return getReviewRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 특정 상품의 구매 만족도 조회 */
    public List<GetReviewResultRes> getReviewResult(int productIdx) throws BaseException{
        try{
            List<GetReviewResultRes> getProductRes = reviewDao.getReviewResult(productIdx);
            return getProductRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 특정 상품의 전체 리뷰 조회 */
    public List<GetReviewAllRes> getReviewAll(int productIdx) throws BaseException{
        try{
            List<GetReviewAllRes> getProductRes = reviewDao.getReviewAll(productIdx);
            return getProductRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}