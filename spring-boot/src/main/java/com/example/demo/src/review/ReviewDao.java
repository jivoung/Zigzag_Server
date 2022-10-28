package com.example.demo.src.review;
import com.example.demo.src.review.model.*;
import com.example.demo.src.user.model.DeleteFavoriteStoreReq;
import com.example.demo.src.user.model.PostFavoriteStoreReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class
ReviewDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int checkReviewIdx(int reviewIdx) throws DataAccessException {
        try{
            String checkReviewQuery = "select exists(select reviewIdx from Review where reviewIdx = ? and status = 'y')";
            return this.jdbcTemplate.queryForObject(checkReviewQuery, int.class, reviewIdx);
        }catch (DataAccessException e){
            return 0;
        }
    }

    /** 작성 가능한 리뷰 조회 */
    public List<GetPossibleReviewRes> getPossibleReview(int userIdx) {
        String getReviewQuery = "SELECT " +
                                "    u.productIdx, " +
                                "    pi.productImgUrl, " +
                                "    p.productName, " +
                                "    c.colorName, " +
                                "    s.sizeName, " +
                                "    u.confirmedDate " +
                                "FROM UserProduct u " +
                                "    JOIN Product p ON p.productIdx = u.productIdx " +
                                "    JOIN ProductImg pi ON p.productIdx = pi.productIdx " +
                                "    JOIN ProductDetail pd ON p.productIdx = pd.productIdx " +
                                "    JOIN Color c ON pd.colorIdx = c.colorIdx " +
                                "    JOIN Size s ON pd.sizeIdx = s.sizeIdx " +
                                "WHERE userIdx = ? AND " +
                                "    NOT EXISTS(SELECT userProductIdx FROM Review r WHERE u.userProductIdx = r.userProductIdx) " +
                                "GROUP BY u.userProductIdx " +
                                "ORDER BY u.confirmedDate desc";
        return this.jdbcTemplate.query(getReviewQuery,
                (rs, rowNum) -> new GetPossibleReviewRes(
                        rs.getInt("productIdx"),
                        rs.getString("productImgUrl"),
                        rs.getString("productName"),
                        rs.getString("colorName"),
                        rs.getString("sizeName"),
                        rs.getString("confirmedDate")
                ), userIdx
        );
    }

    /** 작성한 리뷰 조회 */
    public List<GetReviewRes> getReview(int userIdx) {

        String getReviewQuery = "SELECT " +
                                "    r.productIdx, " +
                                "    pi.productImgUrl, " +
                                "    p.productName, " +
                                "    c.colorName, " +
                                "    s.sizeName " +
                                "FROM Review r " +
                                "    JOIN Product p ON p.productIdx = r.productIdx " +
                                "    JOIN ProductImg pi ON p.productIdx = pi.productIdx " +
                                "    JOIN ProductDetail pd ON p.productIdx = pd.productIdx " +
                                "    JOIN Color c ON pd.colorIdx = c.colorIdx " +
                                "    JOIN Size s ON pd.sizeIdx = s.sizeIdx " +
                                "    JOIN UserProduct up ON p.productIdx = up.productIdx " +
                                "WHERE userIdx = ? " +
                                "GROUP BY p.productIdx " +
                                "ORDER BY r.createdAt desc";
        return this.jdbcTemplate.query(getReviewQuery,
                (rs, rowNum) -> new GetReviewRes(
                        rs.getInt("productIdx"),
                        rs.getString("productImgUrl"),
                        rs.getString("productName"),
                        rs.getString("colorName"),
                        rs.getString("sizeName")
                ), userIdx
        );
    }

    /** 리뷰 등록 */
    public int createReview(int userIdx, PostReviewReq postReviewReq){

        String sql = "SELECT userProductIdx FROM UserProduct WHERE userIdx = ? AND productIdx = ?";
        int userProductIdx = this.jdbcTemplate.queryForObject(sql, int.class, userIdx, postReviewReq.getProductIdx());

        String createReviewQuery = "INSERT INTO Review(productIdx, userProductIdx, starRate, contents, reviewColorIdx, reviewSizeIdx, reviewQualityIdx) " +
                "   VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] createReviewParams = new Object[]{postReviewReq.getProductIdx(), userProductIdx,  postReviewReq.getStarRate(),
                postReviewReq.getContents(), postReviewReq.getReviewColor(), postReviewReq.getReviewSize(), postReviewReq.getReviewQuality()};
        return this.jdbcTemplate.update(createReviewQuery, createReviewParams);
    }

    /** 리뷰 수정 */
    public int updateReview(int userIdx, PostReviewReq postReviewReq){

        String sql = "SELECT userProductIdx FROM UserProduct WHERE userIdx = ? and productIdx = ?";
        int userProductIdx = this.jdbcTemplate.queryForObject(sql, int.class, userIdx, postReviewReq.getProductIdx());

        String updateReviewQuery = "UPDATE Review SET starRate = ?, contents = ?, reviewColorIdx = ?, reviewSizeIdx = ?, reviewQualityIdx = ? " +
                "WHERE userProductIdx = ?";
        Object[] updateReviewParams = new Object[]{postReviewReq.getStarRate(), postReviewReq.getContents(),
                postReviewReq.getReviewColor(), postReviewReq.getReviewSize(), postReviewReq.getReviewQuality(), userProductIdx};
        return this.jdbcTemplate.update(updateReviewQuery, updateReviewParams);
    }

    /** 특정 상품의 구매 만족도 조회 */
    public List<GetReviewResultRes> getReviewResult(int productIdx) {

        String getReviewQuery = "select productIdx, " +
                                "       TRUNCATE(AVG(starRate),1) as starRate, " +
                                "       RS.desc as reviewSize, " +
                                "       TRUNCATE(AVG(RS.reviewSizeIdx = 2) *100,0) as sizeValue, " +
                                "       RC.desc as reviewColor, " +
                                "       TRUNCATE(AVG(RC.reviewColorIdx = 2) * 100, 0) as colorValue, " +
                                "       RQ.desc as reviewQuality, " +
                                "       TRUNCATE(AVG(RQ.reviewQualityIdx = 2) * 100,0) as qualityValue " +
                                "FROM Review " +
                                "    JOIN ReviewColor RC on Review.reviewColorIdx = RC.reviewColorIdx " +
                                "    JOIN ReviewSize RS on RS.reviewSizeIdx = Review.reviewSizeIdx " +
                                "    JOIN ReviewQuality RQ on Review.reviewQualityIdx = RQ.reviewQualityIdx " +
                                "WHERE productIdx = ?";
        return this.jdbcTemplate.query(getReviewQuery,
                (rs, rowNum) -> new GetReviewResultRes(
                        rs.getInt("productIdx"),
                        rs.getDouble("starRate"),
                        rs.getString("reviewSize"),
                        rs.getInt("sizeValue"),
                        rs.getString("reviewColor"),
                        rs.getInt("colorValue"),
                        rs.getString("reviewQuality"),
                        rs.getInt("qualityValue")
                ), productIdx
        );
    }

    /** 특정 상품의 전체 리뷰 조회 */
    public List<GetReviewAllRes> getReviewAll(int productIdx) {

        String getReviewQuery = "select " +
                                "        concat(substr(u.email,1,2), '**') AS name, " +
                                "        r.starRate, " +
                                "        up.confirmedDate, " +
                                "        u.height, " +
                                "        u.weight, " +
                                "        u.topSize, " +
                                "        u.bottomSize, " +
                                "        u.shoesSize, " +
                                "        c.colorName, " +
                                "        s.sizeName, " +
                                "        rs.`desc` AS reviewSize, " +
                                "        rq.`desc` AS reviewQuality, " +
                                "        rc.`desc` AS reviewColor, " +
                                "        r.contents, " +
                                "        re.value " +
                                "from Review r " +
                                "    JOIN UserProduct up on r.userProductIdx = up.userProductIdx " +
                                "    JOIN User u on up.userIdx = u.userIdx " +
                                "    JOIN ReviewColor rc on r.reviewColorIdx = rc.reviewColorIdx " +
                                "    JOIN ReviewSize rs on r.reviewSizeIdx = rs.reviewSizeIdx " +
                                "    JOIN ReviewQuality rq on r.reviewQualityIdx = rq.reviewQualityIdx " +
                                "    JOIN Product p ON p.productIdx = r.productIdx " +
                                "    JOIN ProductDetail pd ON p.productIdx = pd.productIdx " +
                                "    JOIN Color c ON pd.colorIdx = c.colorIdx " +
                                "    JOIN Size s ON pd.sizeIdx = s.sizeIdx " +
                                "    JOIN ReviewEval re on r.reviewIdx = re.reviewIdx " +
                                "WHERE up.productIdx = ?";
        return this.jdbcTemplate.query(getReviewQuery,
                (rs, rowNum) -> new GetReviewAllRes(
                        rs.getString("name"),
                        rs.getInt("starRate"),
                        rs.getString("confirmedDate"),
                        rs.getInt("height"),
                        rs.getInt("weight"),
                        rs.getInt("topSize"),
                        rs.getInt("bottomSize"),
                        rs.getInt("shoesSize"),
                        rs.getString("colorName"),
                        rs.getString("sizeName"),
                        rs.getString("reviewSize"),
                        rs.getString("reviewQuality"),
                        rs.getString("reviewColor"),
                        rs.getString("contents"),
                        rs.getString("value")
                ), productIdx
        );
    }

    /**리뷰 평가 등록*/
    public int createReviewEval(int reviewIdx, PostReviewEvalReq postReviewEvalReq){
        String createReviewEvalQuery = "insert into ReviewEval (reviewIdx, userIdx, value) VALUES (?,?,?)";
        Object[] createReviewEvalParams = new Object[]{reviewIdx, postReviewEvalReq.getUserIdx(), postReviewEvalReq.getEvalValue()};
        this.jdbcTemplate.update(createReviewEvalQuery, createReviewEvalParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    /**사용자가 평가한 리뷰 확인*/
    public int checkReviewEvalPost(int reviewIdx, int userIdx) throws DataAccessException{
        try{
            String checkReviewEvalQuery = "select exists(select reviewEvalIdx from ReviewEval where reviewIdx = ? and userIdx = ? and status = 'y')";
            return this.jdbcTemplate.queryForObject(checkReviewEvalQuery, int.class, reviewIdx, userIdx);
        }catch (DataAccessException e){
            return 0;
        }
    }

    /**리뷰 평가 변경*/
    public int updateReviewEval(int reviewIdx, PatchReviewEvalReq patchReviewEvalReq){
        String deleteReviewEvalQuery = "update ReviewEval set value = ? where reviewIdx = ? and userIdx = ? and status = 'y' ";
        Object[] deleteReviewEvalParams = new Object[]{patchReviewEvalReq.getEvalValue(), reviewIdx, patchReviewEvalReq.getUserIdx()};

        return this.jdbcTemplate.update(deleteReviewEvalQuery,deleteReviewEvalParams);
    }
    /**사용자가 평가한 리뷰 확인*/
    public int checkReviewEvalPatch(int reviewIdx, int userIdx, String value) throws DataAccessException{
        try{
            String checkReviewEvalQuery = "select exists(select reviewEvalIdx from ReviewEval where reviewIdx = ? and userIdx = ? and value =? and status = 'y')";
            return this.jdbcTemplate.queryForObject(checkReviewEvalQuery, int.class, reviewIdx, userIdx, value);
        }catch (DataAccessException e){
            return 0;
        }
    }

    /**리뷰 평가 취소*/
    public int deleteReviewEval(DeleteReviewEvalReq deleteReviewEvalReq){
        String deleteReviewEvalQuery = "update ReviewEval set status = 'n' where reviewIdx = ? and userIdx = ? and status = 'y' ";
        Object[] deleteReviewEvalParams = new Object[]{deleteReviewEvalReq.getReviewIdx(),deleteReviewEvalReq.getUserIdx()};

        return this.jdbcTemplate.update(deleteReviewEvalQuery,deleteReviewEvalParams);
    }
}
