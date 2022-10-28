package com.example.demo.src.membership;
import com.example.demo.config.BaseException;
import com.example.demo.src.membership.model.*;
import com.example.demo.src.search.model.GetKeywordRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class MembershipProvider {
    private final MembershipDao membershipDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MembershipProvider(MembershipDao membershipDao, JwtService jwtService) {
        this.membershipDao = membershipDao;
        this.jwtService = jwtService;
    }

    /**전체 멤버십 등급 조회*/
    public List<GetMembershipRes> getMemberships() throws BaseException{
        try{
            List<GetMembershipRes> getMembershipRes = membershipDao.getMemberships();
            return getMembershipRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**사용자 멤버십 등급 조회*/
    public GetUserMembershipRes getUserMembership(int userIdx) throws BaseException{
        try{
            String membershipType = membershipDao.getMembershipType(userIdx);
            int additionalPrice;
            String nextMembershipType;

            if(membershipType.equals("VVIP")){
                additionalPrice = 0;
                nextMembershipType = null;
            }
            else {
                additionalPrice = membershipDao.getMaximumPrice(userIdx) - membershipDao.getTotalPrice(userIdx);
                nextMembershipType = membershipDao.getNextMembershipType(userIdx);
            }
            return new GetUserMembershipRes(membershipType, additionalPrice, nextMembershipType);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
