package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.VwProductDetails;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VwProductDetailsRepository extends JpaRepository<VwProductDetails, Integer> {
    List<VwProductDetails> findAllByShopId(Integer shopId);
    VwProductDetails findFirstByProductId(Integer productId);
    
    @Query("FROM VwProductDetails vw WHERE vw.shopId =:shopId AND vw.categoryId =:categoryId AND (LOWER(vw.productName) LIKE %:keySearch% OR LOWER(vw.brand) LIKE %:keySearch%) ORDER BY vw.createdTime asc" )
    List<VwProductDetails> findAllByShopIdAndCategoryIdAndKeySearch(Pageable pageable, @Param("shopId") Integer shopId, @Param("categoryId") Integer categoryId, @Param("keySearch") String keySearch);
    
    @Query("FROM VwProductDetails vw WHERE vw.shopId =:shopId AND (LOWER(vw.productName) LIKE %:keySearch% OR LOWER(vw.brand) LIKE %:keySearch% OR LOWER(vw.categoryName) LIKE %:keySearch%) ORDER BY vw.createdTime asc" )
    List<VwProductDetails> findAllByShopIdAndKeySearch(Pageable pageable, @Param("shopId") Integer shopId, @Param("keySearch") String keySearch);
    
    @Query("FROM VwProductDetails vw WHERE vw.categoryId =:categoryId AND (LOWER(vw.productName) LIKE %:keySearch% OR LOWER(vw.brand) LIKE %:keySearch% OR LOWER(vw.shopName) LIKE %:keySearch%) ORDER BY vw.createdTime asc" )
    List<VwProductDetails> findAllByCategoryIdAndKeySearch(Pageable pageable, @Param("categoryId") Integer categoryId, @Param("keySearch") String keySearch);
    
    @Query("FROM VwProductDetails vw WHERE vw.shopId =:shopId AND vw.categoryId =:categoryId ORDER BY vw.createdTime asc" )
    List<VwProductDetails> findAllByShopIdAndCategoryId(Pageable pageable, @Param("shopId") Integer shopId, @Param("categoryId") Integer categoryId);
    
    @Query("FROM VwProductDetails vw WHERE vw.shopId =:shopId ORDER BY vw.createdTime asc" )
    List<VwProductDetails> findAllByShopId(Pageable pageable, @Param("shopId") Integer shopId);
    
    @Query("FROM VwProductDetails vw WHERE vw.categoryId =:categoryId ORDER BY vw.createdTime asc" )
    List<VwProductDetails> findAllByCategoryId(Pageable pageable, @Param("categoryId") Integer categoryId);
    
    @Query("FROM VwProductDetails vw WHERE LOWER(vw.productName) LIKE %:keySearch% OR LOWER(vw.categoryName) LIKE %:keySearch% OR LOWER(vw.shopName) LIKE %:keySearch% OR LOWER(vw.brand) LIKE %:keySearch% ORDER BY vw.createdTime asc" )
    List<VwProductDetails> findAllWithKeySearch(Pageable pageable, @Param("keySearch") String keySearch);
    
    @Query("FROM VwProductDetails vw ORDER BY vw.createdTime asc" )
    List<VwProductDetails> findAllWithPaging(Pageable pageable);
    
    
}
