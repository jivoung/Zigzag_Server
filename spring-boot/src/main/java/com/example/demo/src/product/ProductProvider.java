package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ProductProvider {

    private final ProductDao productDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductProvider(ProductDao productDao, JwtService jwtService) {
        this.productDao = productDao;
        this.jwtService = jwtService;
    }

    /** 홈 상품 조회*/
    public List<GetProductRes> getProduct() throws BaseException{
        try{
            List<GetProductRes> list = new ArrayList<>();
            List<ProductRes> getProductRes = productDao.getProduct();

            for(int i = 0; i < getProductRes.size(); i++) {
                int getProductIdx = getProductRes.get(i).getProductIdx();
                String storeName = getProductRes.get(i).getStoreName();
                List<String> productImg = productDao.getProductImg(getProductRes.get(i).getProductIdx());
                String productName = getProductRes.get(i).getProductName();
                int price = getProductRes.get(i).getPrice();
                int discountRate = getProductRes.get(i).getDiscountRate();
                int shippingCost = getProductRes.get(i).getShippingCost();
                String isNew = getProductRes.get(i).getIsNew();
                String isQuick = getProductRes.get(i).getIsQuick();
                String isBrand = getProductRes.get(i).getIsBrand();
                String isZdiscount = getProductRes.get(i).getIsZdiscount();
                int categoryIdx = getProductRes.get(i).getCategoryIdx();
                String mainCategory = getProductRes.get(i).getMainCategory();

                list.add(new GetProductRes(getProductIdx, storeName, productImg, productName, price, discountRate,
                        shippingCost, isNew, isQuick ,isBrand, isZdiscount, categoryIdx, mainCategory));
            }

            return list;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 홈 추천 상품 조회*/
    public List<GetProductRes> getRecommendProduct() throws BaseException{
        try{
            List<GetProductRes> list = new ArrayList<>();
            List<ProductRes> getProductRes = productDao.getRecommendProduct();

            for(int i = 0; i < getProductRes.size(); i++) {
                int getProductIdx = getProductRes.get(i).getProductIdx();
                String storeName = getProductRes.get(i).getStoreName();
                List<String> productImg = productDao.getProductImg(getProductRes.get(i).getProductIdx());
                String productName = getProductRes.get(i).getProductName();
                int price = getProductRes.get(i).getPrice();
                int discountRate = getProductRes.get(i).getDiscountRate();
                int shippingCost = getProductRes.get(i).getShippingCost();
                String isNew = getProductRes.get(i).getIsNew();
                String isQuick = getProductRes.get(i).getIsQuick();
                String isBrand = getProductRes.get(i).getIsBrand();
                String isZdiscount = getProductRes.get(i).getIsZdiscount();
                int categoryIdx = getProductRes.get(i).getCategoryIdx();
                String mainCategory = getProductRes.get(i).getMainCategory();

                list.add(new GetProductRes(getProductIdx, storeName, productImg, productName, price, discountRate,
                        shippingCost, isNew, isQuick ,isBrand, isZdiscount, categoryIdx, mainCategory));
            }

            return list;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 홈 같은 상품을 찜한 유저들의 관심 상품 조회*/
    public List<GetProductRes> getInterestedProduct() throws BaseException{
        try{
            List<GetProductRes> list = new ArrayList<>();
            List<ProductRes> getProductRes = productDao.getInterestedProduct();

            for(int i = 0; i < getProductRes.size(); i++) {
                int getProductIdx = getProductRes.get(i).getProductIdx();
                String storeName = getProductRes.get(i).getStoreName();
                List<String> productImg = productDao.getProductImg(getProductRes.get(i).getProductIdx());
                String productName = getProductRes.get(i).getProductName();
                int price = getProductRes.get(i).getPrice();
                int discountRate = getProductRes.get(i).getDiscountRate();
                int shippingCost = getProductRes.get(i).getShippingCost();
                String isNew = getProductRes.get(i).getIsNew();
                String isQuick = getProductRes.get(i).getIsQuick();
                String isBrand = getProductRes.get(i).getIsBrand();
                String isZdiscount = getProductRes.get(i).getIsZdiscount();
                int categoryIdx = getProductRes.get(i).getCategoryIdx();
                String mainCategory = getProductRes.get(i).getMainCategory();

                list.add(new GetProductRes(getProductIdx, storeName, productImg, productName, price, discountRate,
                        shippingCost, isNew, isQuick ,isBrand, isZdiscount, categoryIdx, mainCategory));
            }

            return list;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    /** 홈 베스트 상품 조회*/
    public List<ProductRes> getBestProducts(String category) throws BaseException{
        try{
            List<ProductRes> getProductRes = productDao.getBestProducts(category);
            return getProductRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**상품 모아보기*/
    public List<GetCategoryProductRes> getCategoryProducts(String mainCategory, String subCategory) throws BaseException {
        if(productDao.checkCategory(mainCategory,subCategory)==0){
            throw new BaseException(INVALID_CATEGORY);
        }
        try{
            int categoryIdx = productDao.getCategoryIdx(mainCategory, subCategory);

            List<GetCategoryProductRes> result = new ArrayList<>();
            List<CategoryProductRes> categoryProductList = productDao.getCategoryProduct(categoryIdx);

            for(CategoryProductRes categoryProduct : categoryProductList ) {
                List<String> productImg = productDao.getProductImg(categoryProduct.getProductIdx());
                int productIdx = categoryProduct.getProductIdx();
                String storeName = categoryProduct.getStoreName();
                String productName = categoryProduct.getProductName();
                int price = categoryProduct.getPrice();
                int shippingCost = categoryProduct.getShippingCost();
                String isNew = categoryProduct.getIsNew();
                String isQuick = categoryProduct.getIsQuick();
                String isBrand = categoryProduct.getIsBrand();
                String isZdiscount = categoryProduct.getIsZdiscount();
                int discountRate = categoryProduct.getDiscountRate();
                result.add(new GetCategoryProductRes(productImg, productIdx,storeName,productName,price,shippingCost,
                        isNew,isQuick,isBrand,isZdiscount,discountRate));
            }
            return result;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**최근 본 상품 조회*/
    public List<GetRecentProductRes> getRecentProduct(int userIdx) throws BaseException{
        try{
            List<GetRecentProductRes> getProductRes = productDao.getRecentProduct(userIdx);
            return getProductRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 장바구니 담긴 상품 조회*/
    public List<GetShoppingBagRes> getShoppingbagProduct(int userIdx) throws BaseException{
        try{
            List<GetShoppingBagRes> getProductRes = productDao.getShoppingbagProduct(userIdx);
            return getProductRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 특정 상품 조회 */
    public List<GetProductDetailRes> getProductDetail(int productIdx) throws BaseException{
        try{
            List<GetProductDetailRes> list = new ArrayList<>();
            List<ProductDetail> getProductRes = productDao.getProductDetail(productIdx);

            for(int i = 0; i < getProductRes.size(); i++) {
                int getProductIdx = getProductRes.get(i).getProductIdx();
                int storeIdx = getProductRes.get(i).getStoreIdx();
                String storeName = getProductRes.get(i).getStoreName();
                List<String> productImg = productDao.getProductImg(getProductRes.get(i).getProductIdx());
                String productName = getProductRes.get(i).getProductName();
                String productCode = getProductRes.get(i).getProductCode();
                int price = getProductRes.get(i).getPrice();
                int discountRate = getProductRes.get(i).getDiscountRate();
                int shippingCost = getProductRes.get(i).getShippingCost();
                String contents = getProductRes.get(i).getContents();
                String color = getProductRes.get(i).getColorName();
                String size = getProductRes.get(i).getSizeName();
                int shippingPossible = getProductRes.get(i).getShippingPossible();

                list.add(new GetProductDetailRes(getProductIdx, storeIdx, storeName, productImg, productName, productCode,
                        price, discountRate, shippingCost, contents, color, size, shippingPossible));
            }

            return list;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
