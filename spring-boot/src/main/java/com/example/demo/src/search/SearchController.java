package com.example.demo.src.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.search.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/search")
public class SearchController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SearchProvider searchProvider;
    @Autowired
    private final SearchService searchService;
    @Autowired
    private final JwtService jwtService;

    public SearchController(SearchProvider searchProvider, SearchService searchService, JwtService jwtService){
        this.searchProvider = searchProvider;
        this.searchService = searchService;
        this.jwtService = jwtService;
    }

    /**
     * 검색 - 상품 검색 결과 조회 API
     * [GET] /search/results-product
     * @return BaseResponse<GetProductResultExtendRes>
     */
    @ResponseBody
    @GetMapping("/results-product") // (GET) 127.0.0.1:9000/app/search/results-product
    public BaseResponse<GetProductResultExtendRes> getProductResults(@RequestParam(required = true) String keyword) {
        try{
            if(keyword.isEmpty()){
                return new BaseResponse<>(SEARCH_EMPTY_KEYWORD);
            }
            GetProductResultExtendRes getProductResultExtendRes = searchProvider.getProductResults(keyword);
            return new BaseResponse<>(getProductResultExtendRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 검색 - 스토어 검색 결과 조회 API
     * [GET] /search/results-store
     * @return BaseResponse<GetStoreResultExtendRes>
     */
    @ResponseBody
    @GetMapping("/results-store") // (GET) 127.0.0.1:9000/app/search/results-store
    public BaseResponse<GetStoreResultExtendRes> getStoreResults(@RequestParam(required = true) String keyword) {
        try{
            if(keyword.isEmpty()){
                return new BaseResponse<>(SEARCH_EMPTY_KEYWORD);
            }
            GetStoreResultExtendRes getStoreResultExtendRes = searchProvider.getStoreResults(keyword);
            return new BaseResponse<>(getStoreResultExtendRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 검색 - 지금 인기있는 키워드 조회 API
     * [GET] /search/popular-keywords
     * @return BaseResponse<List<GetKeywordRes>>
     */
    @ResponseBody
    @GetMapping("/popular-keywords") // (GET) 127.0.0.1:9000/app/search/popular-keywords
    public BaseResponse<List<GetKeywordRes>> getPopularKeywords() {
        try{
            List<GetKeywordRes> getPopularKeywordRes = searchProvider.getPopularKeywords();
            return new BaseResponse<>(getPopularKeywordRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 검색 - 키워드 등록 API
     * [POST] /search/keywords
     * @return BaseResponse<PostKeywordRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/keywords") // (POST) 127.0.0.1:9000/app/search/keywords
    public BaseResponse<String> createKeyword(@RequestBody PostKeywordReq postKeywordReq) {
        try{
            if(postKeywordReq.getUserIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if(postKeywordReq.getKeyword() == null){
                return new BaseResponse<>(SEARCH_EMPTY_KEYWORD);
            }
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(postKeywordReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            String result = searchService.createKeyword(postKeywordReq);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 검색 - 내가 찾아 봤던 키워드 조회 API
     * [GET] /search/keywords/:userIdx
     * @return BaseResponse<List<GetKeywordRes>>
     */
    @ResponseBody
    @GetMapping("/keywords/{userIdx}") // (GET) 127.0.0.1:9000/app/search/keywords/:userIdx
    public BaseResponse<List<GetKeywordRes>> getKeywords(@PathVariable int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetKeywordRes> getKeywordRes = searchProvider.getKeywords(userIdx);
            return new BaseResponse<>(getKeywordRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 검색 - 내가 찾아 봤던 키워드 삭제 API
     * [PATCH] /search/keywords/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/keywords/{userIdx}") // (GET) 127.0.0.1:9000/app/search/keywords/:userIdx
    public BaseResponse<String> deleteKeywords(@PathVariable int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            String result = searchService.deleteKeywords(userIdx);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
