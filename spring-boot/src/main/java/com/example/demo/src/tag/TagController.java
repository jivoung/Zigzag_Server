package com.example.demo.src.tag;

import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.PostFavoriteStoreReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.tag.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/app/tags")
public class TagController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final TagProvider tagProvider;
    @Autowired
    private final TagService tagService;
    @Autowired
    private final JwtService jwtService;


    public TagController(TagProvider tagProvider, TagService tagService, JwtService jwtService){
        this.tagProvider = tagProvider;
        this.tagService = tagService;
        this.jwtService = jwtService;
    }

    /**
     * 전체 태그 조회 API
     * [GET] /tags/:userIdx
     * @return BaseResponse<GetTagRes>
     */
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/tags/:userIdx
    public BaseResponse<List<GetTagRes>> getTags(@PathVariable("userIdx") int userIdx) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            List<GetTagRes> getTagRes = tagProvider.getTags(userIdx);
            return new BaseResponse<>(getTagRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 태그별 스토어 조회 API
     * [GET] /tags/stores/:userIdx
     * @return BaseResponse<GetTagStoreRes>
     */
    @ResponseBody
    @GetMapping("/stores/{userIdx}") // (GET) 127.0.0.1:9000/app/tags/stores/:userIdx
    public BaseResponse<List<GetTagStoreRes>> getTagStores(@PathVariable("userIdx") int userIdx, @RequestBody GetTagStoreReq getTagStoreReq) {
        try {
            if(getTagStoreReq.getUserIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if(getTagStoreReq.getKeyword() == null){
                return new BaseResponse<>(TAGS_EMPTY_KEYWORD);
            }
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (getTagStoreReq.getUserIdx() != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            List<GetTagStoreRes> getTagStoreRes = tagProvider.getTagStores(userIdx, getTagStoreReq.getKeyword());
            return new BaseResponse<>(getTagStoreRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토어 태그 등록 API
     * [POST] /tags
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> createTag(@RequestBody PostTagReq postTagReq){
        if(postTagReq.getUserIdx()==0){
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }
        if(postTagReq.getStoreIdx()==0){
            return new BaseResponse<>(USERS_EMPTY_STORE_ID);
        }
        if(postTagReq.getKeyword()==null){
            return new BaseResponse<>(TAGS_EMPTY_KEYWORD);
        }
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdxByJwt != postTagReq.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            String result = tagService.createTag(postTagReq);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토어 태그 삭제 API
     * [PATCH] /tags
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("")
    public BaseResponse<String> deleteTag(@RequestBody PatchTagReq patchTagReq){
        if(patchTagReq.getUserIdx()==0){
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }
        if(patchTagReq.getKeyword()==null){
            return new BaseResponse<>(TAGS_EMPTY_KEYWORD);
        }
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdxByJwt != patchTagReq.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            String result = tagService.deleteTag(patchTagReq);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 최근 태그 조회 API
     * [GET] /tags/recent/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("/recent/{userIdx}") // (GET) 127.0.0.1:9000/app/tags/recent/:userIdx=
    public BaseResponse<List<String>> getRecentTags(@PathVariable("userIdx") int userIdx) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 아래 과정 진행
            List<String> result = tagProvider.getRecentTags(userIdx);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 추천 태그 조회 API
     * [GET] /tags/recommendation
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("/recommendation") // (GET) 127.0.0.1:9000/app/tags/recommendation
    public BaseResponse<List<String>> getRecommendationTags() {
        try {
            List<String> result = tagProvider.getRecommendationTags();
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
