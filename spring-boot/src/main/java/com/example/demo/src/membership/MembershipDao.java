package com.example.demo.src.membership;
import com.example.demo.src.membership.model.*;
import com.example.demo.src.search.model.GetKeywordRes;
import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class MembershipDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**전체 멤버심 등급 조회*/
    public List<GetMembershipRes> getMemberships(){
        String getMembershipsQuery = "SELECT membershipType, minimumPrice, maximumPrice, rewardRate FROM MemberShip";
        return this.jdbcTemplate.query(getMembershipsQuery,
                (rs,rowNum) -> new GetMembershipRes(
                        rs.getString("membershipType"),
                        rs.getInt("minimumPrice"),
                        rs.getInt("maximumPrice"),
                        rs.getFloat("rewardRate"))
        );
    }

    /**특정 사용자의 멤버십 등급 조회*/
    public String getMembershipType(int userIdx){
        String getUserQuery = "select membershipType from User left join MemberShip on User.membershipIdx = MemberShip.membershipIdx where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new String(
                        rs.getString("membershipType")),
                getUserParams);
    }
    /**특정 사용자의 멤버십 등급의 최대 금액 조회*/
    public int getMaximumPrice(int userIdx){
        String getUserQuery = "select maximumPrice from User left join MemberShip on User.membershipIdx = MemberShip.membershipIdx where userIdx = ?";
        return this.jdbcTemplate.queryForObject(getUserQuery,Integer.class, userIdx);
    }
    /**사용자가 구매한 금액 조회*/
    public int getTotalPrice(int userIdx){
        String getUserQuery = "SELECT sum(totalPrice) as totalPrice FROM UserProduct group by userIdx having userIdx = ?";
        return this.jdbcTemplate.queryForObject(getUserQuery,Integer.class, userIdx);
    }
    /**사용자의 다음 멤버십 등급 조회*/
    public String getNextMembershipType(int userIdx){
        String getUserQuery = "select membershipType From MemberShip Where membershipIdx " +
                "= (select (MemberShip.membershipIdx + 1) as nextMembershipIdx from User left join MemberShip on User.membershipIdx = MemberShip.membershipIdx where userIdx = ?)";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new String(
                        rs.getString("membershipType")),
                getUserParams);
    }
}
