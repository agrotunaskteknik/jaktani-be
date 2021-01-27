package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.DocumentDto;
import com.cartas.jaktani.dto.ProductDto;
import com.cartas.jaktani.dto.TypeDto;
import com.cartas.jaktani.model.Document;
import com.cartas.jaktani.model.Photo;
import com.cartas.jaktani.model.Product;
import com.cartas.jaktani.model.ProductType;
import com.cartas.jaktani.repository.DocumentRepository;
import com.cartas.jaktani.repository.PhotoRepository;
import com.cartas.jaktani.repository.ProductRepository;
import com.cartas.jaktani.repository.ProductTypeRepository;
import com.cartas.jaktani.util.BaseResponse;
import com.cartas.jaktani.util.JSONUtil;
import com.cartas.jaktani.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {
    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    Integer STATUS_DEFAULT = 1;
    static Integer STATUS_DELETED = 0;
    Integer STATUS_ACTIVE = 1;
    Integer ADD_TYPE = 1;
    Integer EDIT_TYPE = 2;
    Integer PRODUCT_DOC_TYPE = 2;

    BaseResponse response = new BaseResponse();

    @Autowired
    private ProductRepository repository;
    @Autowired
    private ProductTypeRepository productTypeRepo;
    @Autowired
    private DocumentRepository documentRepo;
    @Autowired
    private PhotoRepository photoRepository;


    @Override
    @Transactional
    public Object getProductByID(Integer id) {
        Optional<Product> product = repository.findByIdAndStatusIsNot(id, STATUS_DELETED);
        if (!product.isPresent()) {
            response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(product.get()), HttpStatus.OK);
    }

    @Override
    @Transactional
    public Object getProductByName(String name) {
        List<Product> products = repository.findByNameAndStatusIsNot(name, STATUS_DELETED);
        List<Product> productList = new ArrayList<>();
        if (products != null) {
            productList = products;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(productList), HttpStatus.OK);
    }

    @Override
    public Object getAllProducts() {
        List<Product> products = repository.findAllProductByAndStatusIsNot(STATUS_DELETED);
        List<Product> productList = new ArrayList<>();
        if (products != null) {
            productList = products;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(productList), HttpStatus.OK);
    }

    @Override
    @Transactional
    public Object deleteProductByID(Integer id) {
        Optional<Product> product = repository.findById(id);
        if (!product.isPresent()) {
            response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }

        try {
            product.get().setStatus(STATUS_DELETED);
            repository.save(product.get());
            deleteAllProductType(id);
        } catch (Exception e) {
            response.setResponseCode("ERROR");
            response.setResponseMessage("Error " + e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }

        response.setResponseCode("SUCCESS");
        response.setResponseMessage("Delete Success");
        return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.OK);
    }

    @Override
    @Transactional
    public Object addProduct(ProductDto product) {
        Product entity = new Product();
        if (!validateRequest(product, ADD_TYPE)) {
            response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        
        if (product.getDocumentList() == null || product.getDocumentList().size() < 1) {
            response.setResponseCode("FAILED");
            response.setResponseMessage("Product document is empty");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }

        try {
            entity.setShopId(product.getShopId());
            entity.setName(product.getName());
            entity.setDescription(product.getName());
            entity.setStatus(STATUS_DEFAULT);
            entity.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
            entity.setStock(product.getStock());
            entity.setMinOrder(product.getMinOrder());
            entity.setMaxOrder(product.getMaxOrder());
            entity.setUnitType(product.getUnitType());
            entity.setUnitValue(product.getUnitValue());
            entity.setSold(product.getSold());
            entity.setCategoryId(product.getCategoryId());
            entity.setSubCategoryId(product.getSubCategoryId());
            entity.setTypeGroupId(product.getTypeGroupId());
            entity.setTypeId(product.getTypeId());
            entity.setBrand(product.getBrand());
            entity.setPrice(product.getPrice());
            entity.setDiscount(product.getDiscount());
            entity.setSize(product.getSize());
            entity.setYoutubeLink(product.getYoutubeLink());
            entity.setCondition(product.getCondition());
            repository.save(entity);
            saveProductDocument(product.getDocumentList(), entity.getId());
            saveProductPhoto(product.getDocumentList(), entity.getId());
        } catch (Exception e) {
            response.setResponseCode("ERROR");
            response.setResponseMessage("Error " + e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        response.setResponseCode("SUCCESS");
        response.setResponseMessage("Add Success");
        return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.OK);
    }

    @Override
    @Transactional
    public Object editProduct(ProductDto product) {
        Product entity = new Product();
        if (!validateRequest(product, ADD_TYPE)) {
            response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }

        Optional<Product> productById = repository.findByIdAndStatusIsNot(product.getId(), STATUS_DELETED);
        if (!productById.isPresent()) {
            response.setResponseCode("FAILED");
            response.setResponseMessage("Shop is not exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }

        try {
            entity = productById.get();
            entity.setName(product.getName());
            entity.setDescription(product.getName());
            entity.setStatus(STATUS_DEFAULT);
            entity.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
            entity.setStock(product.getStock());
            entity.setSold(product.getSold());
            entity.setMinOrder(product.getMinOrder());
            entity.setMaxOrder(product.getMaxOrder());
            entity.setUnitType(product.getUnitType());
            entity.setUnitValue(product.getUnitValue());
            entity.setCategoryId(product.getCategoryId());
            entity.setSubCategoryId(product.getSubCategoryId());
            entity.setTypeGroupId(product.getTypeGroupId());
            entity.setTypeId(product.getTypeId());
            entity.setBrand(product.getBrand());
            entity.setPrice(product.getPrice());
            entity.setDiscount(product.getDiscount());
            entity.setSize(product.getSize());
            entity.setYoutubeLink(product.getYoutubeLink());
            entity.setCondition(product.getCondition());
            repository.save(entity);
            saveProductDocument(product.getDocumentList(), entity.getId());
            saveProductPhoto(product.getDocumentList(), entity.getId());
        } catch (Exception e) {
            response.setResponseCode("ERROR");
            response.setResponseMessage("Error " + e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        response.setResponseCode("SUCCESS");
        response.setResponseMessage("Add Success");
        return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.OK);
    }

    @Override
    @Transactional
    public Object getAllProductByShopId(Integer shopId) {
        List<Product> products = repository.findAllProductByShopIdAndStatusIsNot(shopId, STATUS_DELETED);
        List<Product> productList = new ArrayList<>();
        if (products != null) {
            productList = products;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(productList), HttpStatus.OK);
    }

    private Boolean validateRequest(ProductDto product, Integer type) {
        if (product.getName() == null || product.getName() == ""
                || product.getDescription() == null || product.getDescription() == ""
                || product.getShopId() == null) {
            return false;
        }

        if (type == EDIT_TYPE) {
            if (product.getId() == null) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    private void saveProductDocument(List<DocumentDto> documentDtoList, Integer productId) {
        deleteAllProductDocument(productId);
        for (DocumentDto documentDto : documentDtoList) {
            Document entity = documentRepo.findFirstByRefferenceIdAndTypeAndOrderNumberAndStatusIsNot(productId, PRODUCT_DOC_TYPE, documentDto.getOrderNumber(), STATUS_DELETED);
            if (entity != null) {
                entity.setStatus(STATUS_ACTIVE);
                entity.setContentData(documentDto.getContentData());
                entity.setFormat(documentDto.getFormat());
                entity.setName(documentDto.getName());
                entity.setSize(documentDto.getSize());
                entity.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
                documentRepo.save(entity);
            } else {
                entity = new Document();
                entity.setRefferenceId(productId);
                entity.setType(PRODUCT_DOC_TYPE);
                entity.setOrderNumber(documentDto.getOrderNumber());
                documentRepo.save(entity);
                entity.setStatus(STATUS_ACTIVE);
                entity.setContentData(documentDto.getContentData());
                entity.setFormat(documentDto.getFormat());
                entity.setName(documentDto.getName());
                entity.setSize(documentDto.getSize());
                entity.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
            }
            entity = null;
        }
    }

    @Transactional
    void saveProductPhoto(List<DocumentDto> documentDtoList, Integer productId) {
        List<Photo> photoList = deleteAllProductPhoto(productId);
        for (Photo photo : photoList) {
            File directory = new File(photo.getImageFilePath());
            boolean fileDeleted = directory.delete();
            if (fileDeleted) {
                logger.debug("success delete file : " + photo.getImageFilePath());
            }
        }
        for (DocumentDto documentDto : documentDtoList) {
            Long timeInMilis = Utils.getCalendar().getTimeInMillis();
            String pathFolder = "img/product/" + productId + "/";
            String imageFileName = productId + "_" + timeInMilis + ".jpg";
            Path destinationFile = Paths.get(pathFolder, imageFileName);
            File directory = new File(pathFolder);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (created) {
                    logger.debug("success create folder");
                }
            }
            try {
                String rawBase64 = documentDto.getContentData();
                String base64ReplaceNewline = rawBase64.replaceAll("\n", "");
                byte[] decodedImg = Base64.getDecoder()
                        .decode(base64ReplaceNewline.getBytes(StandardCharsets.UTF_8));
                Files.write(destinationFile, decodedImg);
            } catch (Exception ex) {
                logger.debug(ex.getMessage());

            }
            Photo savedPhoto = new Photo();
            savedPhoto.setRefferenceId(productId);
            savedPhoto.setCreatedTime(Utils.getTimeStamp(timeInMilis));
            savedPhoto.setImageFilePath(destinationFile.toString());
            savedPhoto.setUrlPath(imageFileName);
            savedPhoto.setStatus(STATUS_ACTIVE);
            savedPhoto.setOrderNumber(documentDto.getOrderNumber());
            try {
                String compressImagePath = compressImage(pathFolder, imageFileName, 0.05f);
                savedPhoto.setUrlPathHome(compressImagePath);
                savedPhoto.setImageFilePathHome(pathFolder + compressImagePath);
            } catch (Exception ex) {
                logger.debug("error : " + ex.getMessage());
            }
            photoRepository.save(savedPhoto);
        }

    }

    @Transactional
    private void deleteAllProductType(Integer productId) {
        if (productId != null) {
            List<ProductType> productTypeList = productTypeRepo.findAllByProductIdAndStatusIsNot(productId, STATUS_DELETED);
            if (productTypeList != null && productTypeList.size() > 0) {
                for (ProductType entity : productTypeList) {
                    entity.setStatus(STATUS_DELETED);
                    productTypeRepo.save(entity);
                }
            }
        }
    }

    @Transactional
    private void deleteAllProductDocument(Integer productId) {
        if (productId != null) {
            List<Document> documentList = documentRepo.findAllByRefferenceIdAndTypeAndStatusIsNot(productId, PRODUCT_DOC_TYPE, STATUS_DELETED);
            if (documentList != null && documentList.size() > 0) {
                for (Document entity : documentList) {
                    entity.setStatus(STATUS_DELETED);
                    documentRepo.save(entity);
                }
            }
        }
    }

    @Transactional
    List<Photo> deleteAllProductPhoto(Integer productId) {
        if (productId != null) {
            List<Photo> photoList = photoRepository.findAllByRefferenceIdAndStatusIsNot(productId, STATUS_DELETED);
            for (Photo entity : photoList) {
                entity.setStatus(STATUS_DELETED);
                photoRepository.save(entity);
            }
            if (photoList != null && photoList.size() > 0) {
                return photoList;
            }
        }
        return new ArrayList<>();
    }

    String compressImage(String fileParentPath, String filePath, Float compressQuality) throws IOException {
        File input = new File(fileParentPath + filePath);
        BufferedImage image = ImageIO.read(input);


        String outputFileCompress = "home_";
        File compressedImageFile = new File(fileParentPath + outputFileCompress + filePath);
        OutputStream os = new FileOutputStream(compressedImageFile);

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = (ImageWriter) writers.next();

        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();

        if (compressQuality == null || compressQuality == 0) {
            compressQuality = 0.05f;
        }

        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(compressQuality);  // Change the quality value you prefer
        writer.write(null, new IIOImage(image, null, null), param);

        os.close();
        ios.close();
        writer.dispose();
        return outputFileCompress + filePath;
    }

}
