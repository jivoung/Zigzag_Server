package com.example.demo.src.product;


import com.example.demo.src.product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /** 홈 상품 조회*/
    public List<ProductRes> getProduct(){
        String getProductQuery = "SELECT " +
                                "       p.productIdx , " +
                                "       s.storeName, " +
                                "       p.productName,  " +
                                "       p.price, " +
                                "       p.discountRate, " +
                                "       p.shippingCost, " +
                                "       p.isNew, " +
                                "       p.isQuick, " +
                                "       p.isBrand, " +
                                "       p.isZdiscount, " +
                                "       p.categoryIdx, " +
                                "       c.mainCategory " +
                                "FROM " +
                                "       Product p " +
                                "          JOIN Store s ON p.storeIdx = s.storeIdx " +
                                "          JOIN Category c ON p.categoryIdx = c.categoryIdx " +
                                "WHERE p.status = 'y' " +
                                "ORDER BY " +
                                "      p.productIdx";
        return this.jdbcTemplate.query(getProductQuery,
                (rs,rowNum) -> new ProductRes(
                        rs.getInt("productIdx"),
                        rs.getString("storeName"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getInt("discountRate"),
                        rs.getInt("shippingCost"),
                        rs.getString("isNew"),
                        rs.getString("isQuick"),
                        rs.getString("isBrand"),
                        rs.getString("isZdiscount"),
                        rs.getInt("categoryIdx"),
                        rs.getString("mainCategory")
                )
        );
    }

    /** 홈 추천 상품 조회*/
    public List<ProductRes> getRecommendProduct(){
        String getProductQuery = "SELECT " +
                                "       p.productIdx , " +
                                "       s.storeName, " +
                                "       p.productName,  " +
                                "       p.price, " +
                                "       p.discountRate, " +
                                "       p.shippingCost, " +
                                "       p.isNew, " +
                                "       p.isQuick, " +
                                "       p.isBrand, " +
                                "       p.isZdiscount, " +
                                "       p.categoryIdx, " +
                                "       c.mainCategory " +
                                "FROM Product p " +
                                "    JOIN ProductLike pl ON p.productIdx = pl.productIdx " +
                                "    JOIN Store s ON p.storeIdx = s.storeIdx " +
                                "    JOIN Category c ON p.categoryIdx = c.categoryIdx " +
                                "    AND exists ( " +
                                "        SELECT productIdx " +
                                "        FROM ProductLike " +
                                "        WHERE likeStatus = 'Y') " +
                                "GROUP BY p.productIdx " +
                                "ORDER BY count(p.productIdx) DESC";
        return this.jdbcTemplate.query(getProductQuery,
                (rs,rowNum) -> new ProductRes(
                        rs.getInt("productIdx"),
                        rs.getString("storeName"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getInt("discountRate"),
                        rs.getInt("shippingCost"),
                        rs.getString("isNew"),
                        rs.getString("isQuick"),
                        rs.getString("isBrand"),
                        rs.getString("isZdiscount"),
                        rs.getInt("categoryIdx"),
                        rs.getString("mainCategory")
                )
        );
    }

    /** 홈 같은 상품을 찜한 유저들의 관심 상품 조회*/
    public List<ProductRes> getInterestedProduct(){
        String getProductQuery = "SELECT " +
                                "       p.productIdx , " +
                                "       s.storeName, " +
                                "       p.productName,  " +
                                "       p.price, " +
                                "       p.discountRate, " +
                                "       p.shippingCost, " +
                                "       p.isNew, " +
                                "       p.isQuick, " +
                                "       p.isBrand, " +
                                "       p.isZdiscount, " +
                                "       p.categoryIdx, " +
                                "       c.mainCategory " +
                                "FROM Product p " +
                                "     JOIN ProductLike pl ON p.productIdx = pl.productIdx " +
                                "    JOIN Store s ON p.storeIdx = s.storeIdx " +
                                "    JOIN Category c ON p.categoryIdx = c.categoryIdx " +
                                "WHERE p.productIdx = pl.productIdx AND pl.likeStatus = 'Y' " +
                                "Group BY p.productIdx ";
        return this.jdbcTemplate.query(getProductQuery,
                (rs,rowNum) -> new ProductRes(
                        rs.getInt("productIdx"),
                        rs.getString("storeName"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getInt("discountRate"),
                        rs.getInt("shippingCost"),
                        rs.getString("isNew"),
                        rs.getString("isQuick"),
                        rs.getString("isBrand"),
                        rs.getString("isZdiscount"),
                        rs.getInt("categoryIdx"),
                        rs.getString("mainCategory")
                )
        );
    }

    /** 홈 베스트 상품 조회*/
    public List<ProductRes> getBestProducts(String category){
        String getProductQuery = "SELECT " +
                                "       p.productIdx , " +
                                "       s.storeName, " +
                                "       p.productName,  " +
                                "       p.price, " +
                                "       p.discountRate, " +
                                "       p.shippingCost, " +
                                "       p.isNew, " +
                                "       p.isQuick, " +
                                "       p.isBrand, " +
                                "       p.isZdiscount, " +
                                "       p.categoryIdx, " +
                                "       c.mainCategory " +
                                "FROM " +
                                "       Product p " +
                                "          JOIN Store s ON p.storeIdx = s.storeIdx " +
                                "          JOIN Category c ON p.categoryIdx = c.categoryIdx " +
                                "WHERE p.status = 'y' AND p.categoryIdx = ?" +
                                "ORDER BY " +
                                "      p.productIdx";
        return this.jdbcTemplate.query(getProductQuery,
                (rs,rowNum) -> new ProductRes(
                        rs.getInt("productIdx"),
                        rs.getString("storeName"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getInt("discountRate"),
                        rs.getInt("shippingCost"),
                        rs.getString("isNew"),
                        rs.getString("isQuick"),
                        rs.getString("isBrand"),
                        rs.getString("isZdiscount"),
                        rs.getInt("categoryIdx"),
                        rs.getString("mainCategory")
                ),category
        );
    }

    /**상품 모아보기*/
    public List<CategoryProductRes> getCategoryProduct (int categoryIdx){
        String getCategoriesQuery = "SELECT productIdx, storeName, productName, price, shippingCost, isNew, isQuick, isBrand, isZdiscount, discountRate "+
                "FROM Product WHERE categoryIdx= ? and status = 'y' order by (select count(userIdx) from UserProduct where productIdx = Product.productIdx) desc";
        return this.jdbcTemplate.query(getCategoriesQuery,
                (rs,rowNum) -> new CategoryProductRes(
                        rs.getInt("productIdx"),
                        rs.getString("storeName"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getInt("shippingCost"),
                        rs.getString("isNew"),
                        rs.getString("isQuick"),
                        rs.getString("isBrand"),
                        rs.getString("isZdiscount"),
                        rs.getInt("discountRate")),
                categoryIdx
        );
    }
    /**상품 이미지 조회*/
    public List<String> getProductImg (int productIdx){
        String getCategoriesQuery = "SELECT productImgUrl FROM ProductImg WHERE productIdx = ?";
        return this.jdbcTemplate.query(getCategoriesQuery,
                (rs,rowNum) -> new String(
                        rs.getString("productImgUrl")),
                productIdx
        );
    }
    /**카테고리 식별자 조회*/
    public int getCategoryIdx (String mainCategory, String subCategory){
        String getCategoriesQuery = "SELECT categoryIdx FROM Category WHERE mainCategory= ? and subCategory= ?";
        return this.jdbcTemplate.queryForObject(getCategoriesQuery,Integer.class, mainCategory,subCategory);
    }
    /**카테고리 확인*/
    public int checkCategory (String mainCategory, String subCategory){
        String getCategoriesQuery = "SELECT EXISTS(SELECT categoryIdx FROM Category WHERE mainCategory= ? and subCategory= ?)";
        return this.jdbcTemplate.queryForObject(getCategoriesQuery,Integer.class, mainCategory,subCategory);
    }

    /** 최근 본 상품 조회*/
    public List<GetRecentProductRes> getRecentProduct(int userIdx) {

        String getProductQuery = "SELECT " +
                                "      uw.userIdx, " +
                                "      p.productIdx, " +
                                "      pi.productImgUrl, " +
                                "      p.storeName, " +
                                "      p.productName, " +
                                "      p.discountRate, " +
                                "      p.price, " +
                                "      pl.likeStatus " +
                                "FROM " +
                                "      Product p " +
                                "          JOIN UserWatchProduct uw ON p.productIdx = uw.productIdx " +
                                "          JOIN ProductLike pl ON uw.productIdx = pl.productIdx " +
                                "          JOIN ProductImg pi ON p.productIdx = pi.productIdx " +
                                "WHERE uw.userIdx = ? " +
                                "       AND uw.status = 'y' " +
                                "GROUP BY uw.userWatchProductIdx " +
                                "ORDER BY " +
                                "       uw.userWatchProductIdx";
        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetRecentProductRes(
                        rs.getInt("userIdx"),
                        rs.getInt("productIdx"),
                        rs.getString("productImgUrl"),
                        rs.getString("storeName"),
                        rs.getString("ProductName"),
                        rs.getInt("discountRate"),
                        rs.getInt("price"),
                        rs.getString("likeStatus")
                ), userIdx
        );
    }

    /** 최근 본 상품 등록*/
    public int addRecentProduct(PostRecentProductReq postRecentProductReq){

        String addQuery = "INSERT INTO UserWatchProduct (userIdx, productIdx, status) " +
                            "        VALUES (?, ?, 'y')";

        Object[] addParams = new Object[]{postRecentProductReq.getUserIdx(), postRecentProductReq.getProductIdx()};

        return this.jdbcTemplate.update(addQuery,addParams);
    }

    /** 최근 본 상품 삭제*/
    public int deleteRecentProduct(PostRecentProductReq postReq){
        String deleteQuery = "UPDATE UserWatchProduct " +
                            "  SET status = 'n' " +
                            "WHERE userIdx = ? " +
                            "  AND productIdx = ?";
        Object[] deleteParams = new Object[]{postReq.getUserIdx(), postReq.getProductIdx()};

        return this.jdbcTemplate.update(deleteQuery,deleteParams);
    }

    /** 장바구니 담긴 상품 조회*/
    public List<GetShoppingBagRes> getShoppingbagProduct(int userIdx) {
        String getProductQuery = "SELECT " +
                                "      sp.userIdx, " +
                                "      p.productIdx, " +
                                "      s.storeImg, " +
                                "      p.storeName, " +
                                "      pi.productImgUrl, " +
                                "      p.productName, " +
                                "      Color.colorName, " +
                                "      Size.sizeName, " +
                                "      sp.count, " +
                                "      p.price, " +
                                "      p.shippingCost " +
                                "FROM " +
                                "      Product p " +
                                "          JOIN ShoppingBagProduct sp ON p.productIdx = sp.productIdx " +
                                "          JOIN User u ON u.userIdx = sp.userIdx " +
                                "          JOIN Store s ON p.storeIdx = s.storeIdx " +
                                "          JOIN ProductImg pi ON p.productIdx = pi.productIdx " +
                                "          JOIN ProductDetail pd ON p.productIdx = pd.productIdx " +
                                "          JOIN Color ON pd.colorIdx = Color.colorIdx " +
                                "          JOIN Size ON pd.sizeIdx = Size.sizeIdx " +
                                "WHERE sp.userIdx = ? " +
                                "    AND sp.status = 'y' " +
                                "GROUP BY sp.shoppingBagProdIdx " +
                                "ORDER BY " +
                                "    sp.shoppingBagProdIdx";
        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetShoppingBagRes(
                        rs.getInt("userIdx"),
                        rs.getInt("productIdx"),
                        rs.getString("storeImg"),
                        rs.getString("storeName"),
                        rs.getString("productImgUrl"),
                        rs.getString("productName"),
                        rs.getString("colorName"),
                        rs.getString("sizeName"),
                        rs.getInt("count"),
                        rs.getInt("price"),
                        rs.getInt("shippingCost")
                ),userIdx
        );
    }

    /** 장바구니 상품 추가*/
    public int addShoppingbag(PostShoppingBagReq postReq){
        String addQuery = "INSERT INTO ShoppingBagProduct (userIdx, " +
                        "storeIdx, " +
                        "productIdx, " +
                        "productDetailIdx, " +
                        "status, " +
                        "count, " +
                        "totalProductPrice) " +
                        "VALUES (?, ?, ?, ?, 'y', ?, ?)";
        Object[] addParams = new Object[]{postReq.getUserIdx(),

                postReq.getStoreIdx(),
                postReq.getProductIdx(),
                postReq.getProductDetailIdx(),
                postReq.getCount(),
                postReq.getTotalProductPrice()};

        return this.jdbcTemplate.update(addQuery,addParams);
    }

    /** 장바구니 담긴 상품 삭제*/
    public int deleteShoppingbag(PostRecentProductReq postReq){
        String deleteQuery = "UPDATE ShoppingBagProduct " +
                "SET status = 'n' " +
                "WHERE userIdx = ? " +
                "AND productIdx = ?";

        Object[] deleteParams = new Object[]{postReq.getUserIdx(), postReq.getProductIdx()};

        return this.jdbcTemplate.update(deleteQuery,deleteParams);
    }

    /** 특정 상품 조회*/
    public List<ProductDetail> getProductDetail(int productIdx) {
        String getProductQuery = "SELECT p.productIdx, " +
                                "       p.storeIdx, " +
                                "       p.storeName, " +
                                "       p.productName, " +
                                "       p.productCode, " +
                                "       p.price, " +
                                "       p.discountRate, " +
                                "       p.shippingCost, " +
                                "       p.contents, " +
                                "       c.colorName, " +
                                "       si.sizeName, " +
                                "       s.shippingPossible " +
                                "FROM " +
                                "   Product p " +
                                "       JOIN ProductDetail pd ON p.productIdx = pd.productIdx " +
                                "       JOIN Store s ON p.storeIdx = s.storeIdx " +
                                "       JOIN Color c on pd.colorIdx = c.colorIdx " +
                                "       JOIN Size si on pd.sizeIdx = si.sizeIdx " +
                                "WHERE p.productIdx = ? ";

        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new ProductDetail(
                        rs.getInt("productIdx"),
                        rs.getInt("storeIdx"),
                        rs.getString("storeName"),
                        rs.getString("productName"),
                        rs.getString("productCode"),
                        rs.getInt("price"),
                        rs.getInt("discountRate"),
                        rs.getInt("shippingCost"),
                        rs.getString("contents"),
                        rs.getString("colorName"),
                        rs.getString("sizeName"),
                        rs.getInt("shippingPossible")
                ),productIdx
        );
    }
}
