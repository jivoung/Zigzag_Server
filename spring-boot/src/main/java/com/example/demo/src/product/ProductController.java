package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/products")
public class ProductController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final JwtService jwtService;

    public ProductController(ProductProvider productProvider, ProductService productService, JwtService jwtService){
        this.productProvider = productProvider;
        this.productService = productService;
        this.jwtService = jwtService;
    }

    /**
     * 홈 상품 조회 API
     * [GET] /home
     * @return BaseResponse<List<GetProductRes>>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/home") // (GET) 127.0.0.1:9000/app/products/home
    public BaseResponse<List<GetProductRes>> getProduct() {
        try{
            // Get Product
            List<GetProductRes> getProductRes = productProvider.getProduct();
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 홈 추천 상품 조회 API
     * [GET] /recommended-products
     * @return BaseResponse<List<GetProductRes>>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/recommended-products") // (GET) 127.0.0.1:9000/app/products/recommended-products
    public BaseResponse<List<GetProductRes>> getRecommendProduct() {
        try{
            // Get Recommend Product
            List<GetProductRes> getProductRes = productProvider.getRecommendProduct();
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 홈 같은 상품을 찜한 유저들의 관심 상품 조회 API
     * [GET] /interested-products
     * @return BaseResponse<List<GetProductRes>>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/interested-products") // (GET) 127.0.0.1:9000/app/products/interested-products
    public BaseResponse<List<GetProductRes>> getInterestedProduct() {
        try{
            // Get interested Product
            List<GetProductRes> getProductRes = productProvider.getInterestedProduct();
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 홈 베스트 상품 조회 API
     * [GET]
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @GetMapping("/best-products") // (GET) 127.0.0.1:9000/app/products/best-products?category=
    public BaseResponse<List<ProductRes>> getBestProducts(@RequestParam String category) {
        try{
            List<ProductRes> getProductRes = productProvider.getBestProducts(category);
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 모아보기 API
     * [GET]
     * @return BaseResponse<List<GetCategoryProductRes>>
     */
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/products
    public BaseResponse<List<GetCategoryProductRes>> getCategoryProducts(@RequestParam String mainCategory, @RequestParam String subCategory) {

        try{
            List<GetCategoryProductRes> getCategoryProductRes = productProvider.getCategoryProducts(mainCategory, subCategory);
            return new BaseResponse<>(getCategoryProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    
    /**
     * 최근 본 상품 조회 API
     * [GET] /recent-products
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @GetMapping("/recent-products/{userIdx}") // (GET) 127.0.0.1:9000/app/products/recent-products
    public BaseResponse<List<GetRecentProductRes>> getRecentProduct(@PathVariable int userIdx) {
        try{
            List<GetRecentProductRes> getProductRes = productProvider.getRecentProduct(userIdx);
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 최근 본 상품 등록 API
     * [POST] /recent-products
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @PostMapping("/recent-products") // (POST) 127.0.0.1:9000/app/products/recent-products
    public BaseResponse<String> createRecentProduct(@RequestBody PostRecentProductReq postRecentProductReq) {
        try{
            if(postRecentProductReq.getUserIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if(postRecentProductReq.getProductIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_PRODUCT_ID);
            }
            String msg = productService.addRecentProduct(postRecentProductReq);
            return new BaseResponse<>(msg);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 최근 본 상품 삭제 API
     * [PATCH] /recent-products/:productIdx
     * @return BaseResponse<List<GetProductRes>>
     */
    // Path-variable
    @ResponseBody
    @PatchMapping("/recent-products/{productIdx}") // (PATCH) 127.0.0.1:9000/app/products/recent-products/:productIdx
    public BaseResponse<String> deleteRecentProduct(@RequestBody PostRecentProductReq postRecentProductReq) {
        try{
            if(postRecentProductReq.getUserIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if(postRecentProductReq.getProductIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_PRODUCT_ID);
            }
            String msg = productService.deleteRecentProduct(postRecentProductReq);
            return new BaseResponse<>(msg);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 장바구니 담긴 상품 조회 API
     * [GET] /shoppingbag-products
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @GetMapping("/shoppingbag-products/{userIdx}") // (GET) 127.0.0.1:9000/app/products/shoppingbag-products/{userIdx}
    public BaseResponse<List<GetShoppingBagRes>> getShoppingbagProduct(@PathVariable int userIdx) {
        try{
            List<GetShoppingBagRes> getProductRes = productProvider.getShoppingbagProduct(userIdx);
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 장바구니 상품 추가 API
     * [POST] /shoppingbag-products
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @PostMapping("/{productIdx}/shoppingbag-products") // (POST) 127.0.0.1:9000/app/products/{productIdx}/shoppingbag-products
    public BaseResponse<String> addShoppingbag(@RequestBody PostShoppingBagReq postReq) {
        try{
            if(postReq.getUserIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if(postReq.getStoreIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_STORE_ID);
            }
            if(postReq.getProductIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_PRODUCT_ID);
            }
            if(postReq.getProductDetailIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_PRODUCT_DETAIL_ID);
            }
            if(postReq.getCount() == 0){
                return new BaseResponse<>(USERS_EMPTY_COUNT_ID);
            }
            if(postReq.getTotalProductPrice() == 0){
                return new BaseResponse<>(USERS_EMPTY_PRICE_ID);
            }

            String getProductRes = productService.addShoppingbag(postReq);
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 장바구니 담긴 상품 삭제 API
     * [PATCH] /shoppingbag-products
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @PatchMapping("/shoppingbag/{bagProductIdx}") // (PATCH) 127.0.0.1:9000/app/products/shoppingbag/:bagProductIdx
    public BaseResponse<String> deleteShoppingbag(@RequestBody PostRecentProductReq postReq) {
        try{
            if(postReq.getUserIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if(postReq.getProductIdx() == 0){
                return new BaseResponse<>(USERS_EMPTY_PRODUCT_ID);
            }
            String getProductRes = productService.deleteShoppingbag(postReq);
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 특정 상품 조회 API
     * [GET] /products/:productIdx
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @GetMapping("/{productIdx}") // (GET) 127.0.0.1:9000/app/products/:productIdx
    public BaseResponse<List<GetProductDetailRes>> getProductDetail(@PathVariable int productIdx) {
        try{
            List<GetProductDetailRes> getProductRes = productProvider.getProductDetail(productIdx);
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
