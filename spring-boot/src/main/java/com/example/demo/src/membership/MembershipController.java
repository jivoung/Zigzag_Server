package com.example.demo.src.membership;

import com.example.demo.src.search.SearchProvider;
import com.example.demo.src.search.SearchService;
import com.example.demo.src.search.model.GetKeywordRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.membership.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/memberships")
public class MembershipController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MembershipProvider membershipProvider;
    @Autowired
    private final MembershipService membershipService;
    @Autowired
    private final JwtService jwtService;

    public MembershipController(MembershipProvider membershipProvider, MembershipService membershipService, JwtService jwtService){
        this.membershipProvider = membershipProvider;
        this.membershipService = membershipService;
        this.jwtService = jwtService;
    }

    /**전체 멤버십 등급 조회 API
     * [GET] /memberships/:userIdx
     * @return BaseResponse<List<GetMembershipRes>>
     * */
    @ResponseBody
    @GetMapping("{userIdx}") // (GET) 127.0.0.1:9000/app/memberships/:userIdx
    public BaseResponse<List<GetMembershipRes>> getMemberships(@PathVariable int userIdx){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            List<GetMembershipRes> getMembershipRes = membershipProvider.getMemberships();
            return new BaseResponse<>(getMembershipRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**사용자 멤버십 등급 조회 API
     * [GET] /memberships/:userIdx/info
     * @return BaseResponse<GetMembershipRes>
     * */
    @ResponseBody
    @GetMapping("{userIdx}/info") // (GET) 127.0.0.1:9000/app/memberships/:userIdx/info
    public BaseResponse<GetUserMembershipRes> getUserMembership(@PathVariable int userIdx){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            GetUserMembershipRes getUserMembershipRes = membershipProvider.getUserMembership(userIdx);
            return new BaseResponse<>(getUserMembershipRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
