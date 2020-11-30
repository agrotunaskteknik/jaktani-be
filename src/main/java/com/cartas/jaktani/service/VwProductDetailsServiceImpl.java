package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.ParamRequestDto;
import com.cartas.jaktani.model.*;
import com.cartas.jaktani.repository.*;
import com.cartas.jaktani.util.BaseResponse;
import com.cartas.jaktani.util.JSONUtil;
import com.cartas.jaktani.util.YouTubeHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VwProductDetailsServiceImpl implements VwProductDetailsService {
    Integer PRODUCT_DOC_TYPE = 2;
    Integer PRODUCT_DOC_ORDER_NO_FRONT = 1;
    Integer STATUS_DELETED = 0;

    BaseResponse response = new BaseResponse();

    @Autowired
    private VwProductDetailsRepository repository;
    @Autowired
    private ProductTypeRepository productTypeRepo;
    @Autowired
    private DocumentRepository documentRepo;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private PhotoRepository photoRepository;


    @Override
    public Object findByProductId(Integer productId) {
        if (productId == null) {
            response.setResponseCode("FAILED");
            response.setResponseMessage("Bad Request");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }

        VwProductDetails productDetails = null;
        try {
            productDetails = repository.findFirstByProductId(productId);
            if (productDetails != null) {
                List<ProductType> productTypeList = new ArrayList<>();
                List<ProductType> allProductType = productTypeRepo.findAllByProductIdAndStatusIsNot(productId, STATUS_DELETED);
//                List<Document> documentList = documentRepo.findAllByRefferenceIdAndTypeAndStatusIsNot(productId, PRODUCT_DOC_TYPE, STATUS_DELETED);
                List<Photo> photoList = photoRepository.findAllByRefferenceId(productId);
                for (Photo photo : photoList) {
                    photo.setParentUrl("http://jaktani.com/photo/getImageByUniqueKey/");
                }
                for (ProductType productType : allProductType) {
                    Optional<Type> type = typeRepository.findByIdAndStatusIsNot(productType.getTypeId(), STATUS_DELETED);
                    if (type.isPresent()) {
                        productType.setCategoryId(type.get().getCategoryId());
                        productType.setName(type.get().getName());
                        productTypeList.add(productType);
                    }
                }
                if (productDetails.getYoutubeLink() != null && productDetails.getYoutubeLink() != "") {
                    YouTubeHelper youTubeHelper = new YouTubeHelper();
                    String youtubeId = youTubeHelper.extractVideoIdFromUrl(productDetails.getYoutubeLink());
                    productDetails.setYoutubeId(youtubeId);
                }
                productDetails.setProductTypeList(productTypeList);
                productDetails.setDocumentList(new ArrayList<>());
                productDetails.setPhotoList(photoList);
            }

        } catch (Exception e) {
            response.setResponseCode("ERROR");
            response.setResponseMessage("Error " + e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>(JSONUtil.createJSON(productDetails), HttpStatus.OK);
    }

    @Override
    public Object findAllByShopId(Integer shopId) {
        if (shopId == null) {
            response.setResponseCode("FAILED");
            response.setResponseMessage("Bad Request");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }

        List<VwProductDetails> productDetailsList = new ArrayList<>();
        List<VwProductDetails> findProductDetails = repository.findAllByShopId(shopId);
        if (findProductDetails != null && findProductDetails.size() > 0) {
            for (VwProductDetails productDetails : findProductDetails) {
                List<ProductType> productTypeList = new ArrayList<>();
                List<ProductType> allProductType = productTypeRepo.findAllByProductIdAndStatusIsNot(productDetails.getProductId(), STATUS_DELETED);
                List<Document> documentList = documentRepo.findAllByRefferenceIdAndTypeAndStatusIsNot(productDetails.getProductId(), PRODUCT_DOC_TYPE, STATUS_DELETED);

                for (ProductType productType : allProductType) {
                    Optional<Type> type = typeRepository.findByIdAndStatusIsNot(productType.getTypeId(), STATUS_DELETED);
                    if (type.isPresent()) {
                        productType.setCategoryId(type.get().getCategoryId());
                        productType.setName(type.get().getName());
                        productTypeList.add(productType);
                    }
                }
                if (productDetails.getYoutubeLink() != null && productDetails.getYoutubeLink() != "") {
                    YouTubeHelper youTubeHelper = new YouTubeHelper();
                    String youtubeId = youTubeHelper.extractVideoIdFromUrl(productDetails.getYoutubeLink());
                    productDetails.setYoutubeId(youtubeId);
                }
                productDetails.setProductTypeList(productTypeList);
                productDetails.setDocumentList(documentList);
                productDetailsList.add(productDetails);

            }
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(productDetailsList), HttpStatus.OK);
    }

    @Override
    public Object searchAllProduct(ParamRequestDto paramRequestDto) {
        List<VwProductDetails> productDetailsList = new ArrayList<>();
        String keySearch = null;
        if (paramRequestDto.pageSize == null || paramRequestDto.pageNumber == null) {
            response.setResponseCode("FAILED");
            response.setResponseMessage("Bad Request");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        if (paramRequestDto.getKeySearch() != null && paramRequestDto.getKeySearch() != "") {
            keySearch = paramRequestDto.getKeySearch().toLowerCase();
        }

        Pageable pageable = PageRequest.of(paramRequestDto.getPageNumber(), paramRequestDto.getPageSize());
        try {
            List<VwProductDetails> findProductDetails = null;
            if (paramRequestDto.getShopId() != null && paramRequestDto.getCategoryId() != null && keySearch != null) {
                findProductDetails = repository.findAllByShopIdAndCategoryIdAndKeySearch(pageable, paramRequestDto.getShopId(), paramRequestDto.getCategoryId(), keySearch);
            } else if (paramRequestDto.getCategoryId() != null && keySearch != null) {
                findProductDetails = repository.findAllByCategoryIdAndKeySearch(pageable, paramRequestDto.getCategoryId(), keySearch);
            } else if (paramRequestDto.getShopId() != null && keySearch != null) {
                findProductDetails = repository.findAllByShopIdAndKeySearch(pageable, paramRequestDto.getShopId(), keySearch);
            } else if (paramRequestDto.getShopId() != null && paramRequestDto.getCategoryId() != null) {
                findProductDetails = repository.findAllByShopIdAndCategoryId(pageable, paramRequestDto.getShopId(), paramRequestDto.getCategoryId());
            } else if (paramRequestDto.getShopId() != null) {
                findProductDetails = repository.findAllByShopId(pageable, paramRequestDto.getShopId());
            } else if (paramRequestDto.getCategoryId() != null) {
                findProductDetails = repository.findAllByCategoryId(pageable, paramRequestDto.getCategoryId());
            } else if (keySearch != null) {
                findProductDetails = repository.findAllWithKeySearch(pageable, keySearch);
            } else {
                findProductDetails = repository.findAllWithPaging(pageable);
            }

            if (findProductDetails != null && findProductDetails.size() > 0) {
                for (VwProductDetails productDetails : findProductDetails) {
//                    List<Document> documentList = new ArrayList<Document>();
                    List<ProductType> productTypeList = new ArrayList<>();
                    List<ProductType> allProductType = productTypeRepo.findAllByProductIdAndStatusIsNot(productDetails.getProductId(), STATUS_DELETED);
                    List<Photo> photoList = photoRepository.findAllByRefferenceId(productDetails.getProductId());
                    for (Photo photo : photoList) {
                        photo.setParentUrl("http://jaktani.com/photo/getImageByUniqueKey/");
                    }
//                    Document documentFront = documentRepo.findFirstByRefferenceIdAndTypeAndOrderNumberAndStatusIsNot(productDetails.getProductId(), PRODUCT_DOC_TYPE, PRODUCT_DOC_ORDER_NO_FRONT, STATUS_DELETED);
//                    if (documentFront != null) {
//                        documentList.add(documentFront);
//                    }

                    for (ProductType productType : allProductType) {
                        Optional<Type> type = typeRepository.findByIdAndStatusIsNot(productType.getTypeId(), STATUS_DELETED);
                        if (type.isPresent()) {
                            productType.setCategoryId(type.get().getCategoryId());
                            productType.setName(type.get().getName());
                            productTypeList.add(productType);
                        }
                    }
                    if (productDetails.getYoutubeLink() != null && productDetails.getYoutubeLink() != "") {
                        YouTubeHelper youTubeHelper = new YouTubeHelper();
                        String youtubeId = youTubeHelper.extractVideoIdFromUrl(productDetails.getYoutubeLink());
                        productDetails.setYoutubeId(youtubeId);
                    }
                    productDetails.setProductTypeList(productTypeList);
//                    productDetails.setDocumentList(documentList);
                    productDetails.setPhotoList(photoList);
                    productDetailsList.add(productDetails);
                }
            }
        } catch (Exception e) {
            response.setResponseCode("ERROR");
            response.setResponseMessage("Error " + e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>(JSONUtil.createJSON(productDetailsList), HttpStatus.OK);
    }
    
    @Override
    public Object allProductTypeByProductId(Integer productId) {
    	if(productId==null) {
       	 	response.setResponseCode("FAILED");
            response.setResponseMessage("Bad Request");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }

		List<ProductType> productTypeList = new ArrayList<>();
        try {
        	 List<ProductType> allProductType = productTypeRepo.findAllByProductIdAndStatusIsNot(productId, STATUS_DELETED);
             for(ProductType productType :allProductType) {
            	 Optional<Type> type = typeRepository.findByIdAndStatusIsNot(productType.getTypeId(), STATUS_DELETED);
            	 if(type.isPresent()) {
            		 productType.setCategoryId(type.get().getCategoryId());
            		 productType.setName(type.get().getName());
            		 productTypeList.add(productType);
            	 }
             }
		} catch (Exception e) {
			response.setResponseCode("ERROR");
            response.setResponseMessage("Error "+e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
		}
        
        return new ResponseEntity<String>(JSONUtil.createJSON(productTypeList), HttpStatus.OK);
    }

}
