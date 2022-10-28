package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.PostRecentProductReq;
import com.example.demo.src.product.model.PostShoppingBagReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

// Service Create, Update, Delete 의 로직 처리
@Service
public class ProductService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductDao productDao;
    private final ProductProvider productProvider;
    private final JwtService jwtService;


    @Autowired
    public ProductService(ProductDao productDao, ProductProvider productProvider, JwtService jwtService) {
        this.productDao = productDao;
        this.productProvider = productProvider;
        this.jwtService = jwtService;
    }

    /** 최근 본 상품 등록*/
    public String addRecentProduct(PostRecentProductReq postRecentProductReq) throws BaseException {
        try{
            int count = productDao.addRecentProduct(postRecentProductReq);
            String msg = "";
            if(count == 0) {
                msg = "등록 실패";
            }else{
                msg = "등록 성공";
            }
            return new String(msg);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 최근 본 상품 삭제*/
    public String deleteRecentProduct(PostRecentProductReq postRecentProductReq) throws BaseException {
        try{
            int count = productDao.deleteRecentProduct(postRecentProductReq);
            String msg = "";
            if(count == 0) {
                msg = "삭제 실패";
            }else{
                msg = "삭제 성공";
            }
            return new String(msg);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 장바구니 상품 추가*/
    public String addShoppingbag(PostShoppingBagReq postReq) throws BaseException {
        try{
            int count = productDao.addShoppingbag(postReq);
            String msg = "";
            if(count == 0) {
                msg = "등록 실패";
            }else{
                msg = "등록 성공";
            }
            return new String(msg);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 장바구니 담긴 상품 삭제*/
    public String deleteShoppingbag(PostRecentProductReq postReq) throws BaseException {
        try{
            int count = productDao.deleteShoppingbag(postReq);
            String msg = "";
            if(count == 0) {
                msg = "삭제 실패";
            }else{
                msg = "삭제 성공";
            }
            return new String(msg);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
