package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.search.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class SearchProvider {
    private final SearchDao searchDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SearchProvider(SearchDao searchDao, JwtService jwtService) {
        this.searchDao = searchDao;
        this.jwtService = jwtService;
    }

    /**상품 검색 결과 조회*/
    public GetProductResultExtendRes getProductResults(String keyword) throws BaseException{
        try{
            List<String> keywordList = searchDao.getKeywordList(keyword);

            List<GetProductResultRes> result = new ArrayList<>();
            List<ProductResult> productResultList = searchDao.getProductResults(keyword);
            for(ProductResult productResult : productResultList){
                int productIdx = productResult.getProductIdx();
                List<String> productImgUrl = searchDao.getProductImgUrl(productIdx);
                String storeName = productResult.getStoreName();
                String productName = productResult.getProductName();
                int price = productResult.getPrice();
                int shippingCost = productResult.getShippingCost();
                String isNew = productResult.getIsNew();
                String isQuick = productResult.getIsQuick() ;
                String isBrand = productResult.getIsBrand();
                String isZdiscount = productResult.getIsZdiscount();
                int discountRate = productResult.getDiscountRate();

                result.add(new GetProductResultRes(productIdx, productImgUrl,storeName, productName, price, shippingCost, isNew, isQuick, isBrand, isZdiscount, discountRate));
            }
            return new GetProductResultExtendRes(keywordList,result);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**스토어 검색 결과 조회*/
    public GetStoreResultExtendRes getStoreResults(String keyword) throws BaseException{
        try{
            List<String> keywordList = searchDao.getKeywordList(keyword);

            List<GetStoreResultRes> result = new ArrayList<>();
            List<StoreResult> storeResultList = searchDao.getStoreResults(keyword);
            for(StoreResult storeResult : storeResultList){
                int storeIdx = storeResult.getStoreIdx();
                String storeImg = storeResult.getStoreImg();
                String storeName = storeResult.getStoreName();
                String storeType = storeResult.getStoreType();
                List<String> ageGroup = searchDao.getStoreAgeGroup(storeIdx);
                List<String> style = searchDao.getStoreStyle(storeIdx);
                int likeNum = storeResult.getLikeNum();

                result.add(new GetStoreResultRes(storeIdx, storeImg, storeName, storeType, ageGroup, style,likeNum));
            }
            return new GetStoreResultExtendRes(keywordList,result);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**지금 인기있는 키워드 조회*/
    public List<GetKeywordRes> getPopularKeywords() throws BaseException{
        try{
            List<GetKeywordRes> getPopularKeywordRes = searchDao.getPopularKeywords();
            return getPopularKeywordRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**내가 찾아봤던 키워드 조회*/
    public List<GetKeywordRes> getKeywords(int userIdx) throws BaseException{
        if(searchDao.getKeywords(userIdx).isEmpty()){
            //throw new BaseException(FAILED_TO_SEARCH_USER);
            return null;
        }
        try{
            List<GetKeywordRes> getKeywordRes = searchDao.getKeywords(userIdx);
            return getKeywordRes;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
