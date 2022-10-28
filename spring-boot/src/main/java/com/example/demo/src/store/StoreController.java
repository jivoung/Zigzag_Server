package com.example.demo.src.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.store.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_STORE_ID;

@RestController
@RequestMapping("/app/stores")
public class StoreController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final StoreProvider storeProvider;
    @Autowired
    private final StoreService storeService;
    @Autowired
    private final JwtService jwtService;

    public StoreController(StoreProvider storeProvider, StoreService storeService, JwtService jwtService){
        this.storeProvider = storeProvider;
        this.storeService = storeService;
        this.jwtService = jwtService;
    }

    /**
     * 스토어 - 쇼핑몰 랭킹 순으로 조회 API
     * [GET] /stores/ranking-shoppingmall
     * @return BaseResponse<List<GetMallRankingRes>>
     */
    @ResponseBody
    @GetMapping("/ranking-shoppingmall") // (GET) 127.0.0.1:9000/app/stores/ranking-shoppingmall
    public BaseResponse<List<GetMallRankingRes>> getMallRankingResults() {
        try{
            List<GetMallRankingRes> getMallRankingRes = storeProvider.getMallRankingResults();
            return new BaseResponse<>(getMallRankingRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토어 - 브랜드 랭킹 순으로 조회 API
     * [GET] /stores/ranking-brand
     * @return BaseResponse<List<GetBrandRankingRes>>
     */
    @ResponseBody
    @GetMapping("/ranking-brand") // (GET) 127.0.0.1:9000/app/stores/ranking-brand
    public BaseResponse<List<GetBrandRankingRes>> getBrandRankingResults() {
        try{
            List<GetBrandRankingRes> getBrandRankingRes = storeProvider.getBrandRankingResults();
            return new BaseResponse<>(getBrandRankingRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토어 - 신규 입점 브랜드 조회 API
     * [GET] /stores/new-brand
     * @return BaseResponse<GetBrandRankingRes>
     */
    @ResponseBody
    @GetMapping("/new-brand") // (GET) 127.0.0.1:9000/app/stores/new-brand
    public BaseResponse<GetNewBrandExtendRes> getNewBrandResults() {
        try{
            GetNewBrandExtendRes getNewBrandExtendRes = storeProvider.getNewBrandResults();
            return new BaseResponse<>(getNewBrandExtendRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 스토어 기본정보 API
     * [GET] /stores/:storeIdx
     * @return BaseResponse<GetStoreRes>
     */
    @ResponseBody
    @GetMapping("/{storeIdx}") // (GET) 127.0.0.1:9000/app/stores/:storeIdx
    public BaseResponse<GetStoreRes> getStore(@PathVariable ("storeIdx") int storeIdx) {
        try{
            if(storeProvider.checkStoreIdx(storeIdx)==0){
                return new BaseResponse<>(INVALID_STORE_ID);
            }
            GetStoreRes getStoreRes = storeProvider.getStore(storeIdx);
            return new BaseResponse<>(getStoreRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토어 이번주 판매 베스트 상품 목록 조회 API
     * [GET] /stores/:storeIdx/weekly-best
     * @return BaseResponse<List<GetStoreProductRes>>
     */
    @ResponseBody
    @GetMapping("{storeIdx}/weekly-best") // (GET) 127.0.0.1:9000/app/stores/:storeIdx/weekly-best
    public BaseResponse<List<GetStoreProductRes>> getBestStoreProducts(@PathVariable ("storeIdx") int storeIdx) {
        try{
            if(storeProvider.checkStoreIdx(storeIdx)==0){
                return new BaseResponse<>(INVALID_STORE_ID);
            }
            List<GetStoreProductRes> getStoreProductRes = storeProvider.getBestStoreProducts(storeIdx);
            return new BaseResponse<>(getStoreProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토어 직진 배송 상품 조회 API
     * [GET] /stores/:storeIdx/quick-delivery
     * @return BaseResponse<List<GetStoreProductRes>>
     */
    @ResponseBody
    @GetMapping("{storeIdx}/quick-delivery") // (GET) 127.0.0.1:9000/app/stores/:storeIdx/quick-delivery
    public BaseResponse<List<GetStoreProductRes>> getQuickStoreProducts(@PathVariable ("storeIdx") int storeIdx) {
        try{
            if(storeProvider.checkStoreIdx(storeIdx)==0){
                return new BaseResponse<>(INVALID_STORE_ID);
            }
            List<GetStoreProductRes> getStoreProductRes = storeProvider.getQuickStoreProducts(storeIdx);
            return new BaseResponse<>(getStoreProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토어 전체 상품 조회 API
     * [GET] /stores/:storeIdx/products
     * @return BaseResponse<List<GetStoreProductRes>>
     */
    @ResponseBody
    @GetMapping("{storeIdx}/products") // (GET) 127.0.0.1:9000/app/stores/:storeIdx/prducts
    public BaseResponse<List<GetStoreProductRes>> getStoreProducts(@PathVariable ("storeIdx") int storeIdx) {
        try{
            if(storeProvider.checkStoreIdx(storeIdx)==0){
                return new BaseResponse<>(INVALID_STORE_ID);
            }
            List<GetStoreProductRes> getStoreProductRes = storeProvider.getStoreProducts(storeIdx);
            return new BaseResponse<>(getStoreProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
