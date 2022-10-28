package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.src.store.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class StoreProvider {
    private final StoreDao storeDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public StoreProvider(StoreDao storeDao, JwtService jwtService) {
        this.storeDao = storeDao;
        this.jwtService = jwtService;
    }

    /**스토어 - 쇼핑몰 랭킹 순으로 조회*/
    public List<GetMallRankingRes> getMallRankingResults() throws BaseException{
        try{
            List<GetMallRankingRes> result = new ArrayList<>();
            List<ShoppingmallRanking> shoppingmallList = storeDao.getMallRankingResults();
            for(ShoppingmallRanking shoppingmall : shoppingmallList ) {
                int storeIdx = shoppingmall.getStoreIdx();
                String storeImg = storeDao.getStoreImg(storeIdx) ;
                String storeName = storeDao.getStoreName(storeIdx);
                List<String> ageGroup = storeDao.getStoreAgeGroup(storeIdx);
                List<String> style = storeDao.getStoreStyle(storeIdx);
                int likeNum = storeDao.getLikeNum(storeIdx);
                boolean freeDelivery = storeDao.getFreeDelivery(storeIdx);
                int maxCouponPrice = storeDao.getMaxCouponPrice(storeIdx);
                result.add(new GetMallRankingRes(storeIdx, storeImg, storeName, ageGroup, style, likeNum, freeDelivery, maxCouponPrice));
            }
            return result;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 스토어 - 브랜드 랭킹 순으로 조회 API*/
    public List<GetBrandRankingRes> getBrandRankingResults() throws BaseException{
        try{
            List<GetBrandRankingRes> result = new ArrayList<>();
            List<BrandRanking> brandList = storeDao.getBrandRankingResults();
            for(BrandRanking brand : brandList ) {
                int storeIdx = brand.getStoreIdx();
                String storeImg = storeDao.getStoreImg(storeIdx) ;
                String storeName = storeDao.getStoreName(storeIdx);
                String storeCategoryName = storeDao.getStoreCategoryName(storeIdx);
                int likeNum = storeDao.getLikeNum(storeIdx);
                boolean freeDelivery = storeDao.getFreeDelivery(storeIdx);
                int maxCouponPrice = storeDao.getMaxCouponPrice(storeIdx);
                result.add(new GetBrandRankingRes(storeIdx, storeImg, storeName, storeCategoryName, likeNum, freeDelivery, maxCouponPrice));
            }
            return result;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 스토어 - 신규 입점 브랜드 조회 API*/
    public GetNewBrandExtendRes getNewBrandResults() throws BaseException{
        try{
            int count = storeDao.getNewBrandNum();

            List<GetNewBrandRes> result = new ArrayList<>();
            List<NewBrand> newBrandList = storeDao.getNewBrandResults();
            for(NewBrand brand : newBrandList ) {
                int storeIdx = brand.getStoreIdx();
                String storeImg = storeDao.getStoreImg(storeIdx) ;
                String storeName = storeDao.getStoreName(storeIdx);
                String storeCategoryName = storeDao.getStoreCategoryName(storeIdx);
                int likeNum = storeDao.getLikeNum(storeIdx);
                boolean freeDelivery = storeDao.getFreeDelivery(storeIdx);
                result.add(new GetNewBrandRes(storeIdx, storeImg, storeName, storeCategoryName, likeNum, freeDelivery));
            }
            return new GetNewBrandExtendRes(count,result);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**스토어 식별자 확인*/
    public int checkStoreIdx(int storeIdx)throws BaseException {
        try{
            int result = storeDao.checkStoreIdx(storeIdx);
            return result;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**특정 스토어 기본 정보 조회*/
    public GetStoreRes getStore(int storeIdx) throws BaseException{
        try{
            String storeImg = storeDao.getStoreImg(storeIdx) ;
            String storeName = storeDao.getStoreName(storeIdx);
            List<String> ageGroup = storeDao.getStoreAgeGroup(storeIdx);
            List<String> style = storeDao.getStoreStyle(storeIdx);
            int likeNum = storeDao.getLikeNum(storeIdx);
            int maxCouponPrice = storeDao.getMaxCouponPrice(storeIdx);
            int productCount=storeDao.getProductCount(storeIdx);
            return new GetStoreRes(storeIdx, storeImg, storeName, ageGroup, style, likeNum,maxCouponPrice, productCount);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**스토어 이번주 판매 베스트 상품 조회*/
    public List<GetStoreProductRes> getBestStoreProducts(int storeIdx) throws BaseException {
        try{
            List<GetStoreProductRes> result = new ArrayList<>();
            List<StoreProduct> storeProductList = storeDao.getBestStoreProducts(storeIdx);

            for(StoreProduct storeProduct : storeProductList ) {
                int productIdx = storeProduct.getProductIdx();
                String productName = storeDao.getProductName(productIdx);
                List<String> productImg = storeDao.getProductImg(productIdx);
                int price = storeDao.getPrice(productIdx);
                int shippingCost = storeDao.getShippingCost(productIdx);
                String isNew = storeDao.getIsNew(productIdx);
                String isQuick = storeDao.getIsQuick(productIdx);
                String isZdiscount = storeDao.getIsZdiscount(productIdx);
                int discountRate = storeDao.getDiscountRate(productIdx);
                int reviewAverage = storeDao.getReviewAverage(productIdx);
                int reviewCount = storeDao.getReviewCount(productIdx);

                result.add(new GetStoreProductRes(productIdx, productName, productImg, price,shippingCost,
                        isNew, isQuick,isZdiscount, discountRate, reviewAverage, reviewCount));
            }
            return result;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**스토어 직진 배송 상품 조회*/
    public List<GetStoreProductRes> getQuickStoreProducts(int storeIdx) throws BaseException {
        try{
            List<GetStoreProductRes> result = new ArrayList<>();
            List<StoreProduct> storeProductList = storeDao.getQuickStoreProducts(storeIdx);

            for(StoreProduct storeProduct : storeProductList ) {
                int productIdx = storeProduct.getProductIdx();
                String productName = storeDao.getProductName(productIdx);
                List<String> productImg = storeDao.getProductImg(productIdx);
                int price = storeDao.getPrice(productIdx);
                int shippingCost = storeDao.getShippingCost(productIdx);
                String isNew = storeDao.getIsNew(productIdx);
                String isQuick = storeDao.getIsQuick(productIdx);
                String isZdiscount = storeDao.getIsZdiscount(productIdx);
                int discountRate = storeDao.getDiscountRate(productIdx);
                int reviewAverage = storeDao.getReviewAverage(productIdx);
                int reviewCount = storeDao.getReviewCount(productIdx);

                result.add(new GetStoreProductRes(productIdx, productName, productImg, price,shippingCost,
                        isNew, isQuick,isZdiscount, discountRate, reviewAverage, reviewCount));
            }
            return result;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**스토어 전체 상품 조회*/
    public List<GetStoreProductRes> getStoreProducts(int storeIdx) throws BaseException {
        try{
            List<GetStoreProductRes> result = new ArrayList<>();
            List<StoreProduct> storeProductList = storeDao.getStoreProducts(storeIdx);

            for(StoreProduct storeProduct : storeProductList ) {
                int productIdx = storeProduct.getProductIdx();
                String productName = storeDao.getProductName(productIdx);
                List<String> productImg = storeDao.getProductImg(productIdx);
                int price = storeDao.getPrice(productIdx);
                int shippingCost = storeDao.getShippingCost(productIdx);
                String isNew = storeDao.getIsNew(productIdx);
                String isQuick = storeDao.getIsQuick(productIdx);
                String isZdiscount = storeDao.getIsZdiscount(productIdx);
                int discountRate = storeDao.getDiscountRate(productIdx);
                int reviewAverage = storeDao.getReviewAverage(productIdx);
                int reviewCount = storeDao.getReviewCount(productIdx);

                result.add(new GetStoreProductRes(productIdx, productName, productImg, price,shippingCost,
                        isNew, isQuick,isZdiscount, discountRate, reviewAverage, reviewCount));
            }
            return result;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
