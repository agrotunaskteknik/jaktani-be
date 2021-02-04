package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.ShopDto;
import com.cartas.jaktani.dto.UserDto;
import com.cartas.jaktani.model.Photo;
import com.cartas.jaktani.model.Shop;
import com.cartas.jaktani.model.Users;
import com.cartas.jaktani.repository.ShopRepository;
import com.cartas.jaktani.repository.UserRepository;
import com.cartas.jaktani.util.BaseResponse;
import com.cartas.jaktani.util.JSONUtil;
import com.cartas.jaktani.util.Utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.transaction.Transactional;

@Service
public class ShopServiceImpl implements ShopService {
    Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);
    
	Integer STATUS_READY = 1;
    static Integer STATUS_DELETED = 0;
    Integer ADD_TYPE = 1;
    Integer EDIT_TYPE = 2;
    Integer USER_TYPE_SHOP_OWNER = 2;
    
    BaseResponse response = new BaseResponse();
    
    @Autowired private ShopRepository repository;
    @Autowired private UserRepository userRepository;

    @Override
    public Object getShopByID(Integer id) {
        Optional<Shop> shop = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
        if(!shop.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        Optional<Users> user = userRepository.findById(shop.get().getUserID());
        shop.get().setUser(user.get());
        return new ResponseEntity<String>(JSONUtil.createJSON(shop.get()), HttpStatus.OK);
    }
    
    @Override
    public Object getShopByUserID(Integer userId) {
        Optional<Shop> shop = repository.findByUserIDAndStatusIsNot(userId,STATUS_DELETED);
        if(!shop.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        Optional<Users> user = userRepository.findById(shop.get().getUserID());
        shop.get().setUser(user.get());
        return new ResponseEntity<String>(JSONUtil.createJSON(shop.get()), HttpStatus.OK);
    }

    @Override
    public Object getShopByName(String name) {
        Optional<Shop> shop = repository.findFirstByNameAndStatusIsNot(name, STATUS_DELETED);
        if(!shop.isPresent()) {
       	    response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
       }
       Optional<Users> user = userRepository.findById(shop.get().getUserID());
       shop.get().setUser(user.get());
       return new ResponseEntity<String>(JSONUtil.createJSON(shop), HttpStatus.OK);
    }

    @Override
    public Object getAllShops() {
        List<Shop> shops= repository.findAllShopByAndStatusIsNot(STATUS_DELETED);
        List<Shop> shopList = new ArrayList<>();
        if(shops!=null) {
        	shopList = shops;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(shopList), HttpStatus.OK);
    }
    
    @Override
    public Object getAllShopsByStatus(Integer status) {
        List<Shop> shops= repository.findAllShopByStatus(status);
        List<Shop> shopList = new ArrayList<>();
        if(shops!=null) {
        	shopList = shops;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(shopList), HttpStatus.OK);
    }

    @Override
    public Object deleteShopByID(Integer id) {
    	Optional<Shop> shop = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
    	if(!shop.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		shop.get().setStatus(STATUS_DELETED);
        	repository.save(shop.get());
		} catch (Exception e) {
			response.setResponseCode("ERROR");
            response.setResponseMessage("Error "+e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
		}
    	
    	response.setResponseCode("SUCCESS");
        response.setResponseMessage("Delete Success");
        return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.OK);
    }
    
    @Override
    public Object updateShopStatusByID(Integer id, Integer status) {
    	Optional<Shop> shop = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
    	if(!shop.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		shop.get().setStatus(status);
        	repository.save(shop.get());
		} catch (Exception e) {
			response.setResponseCode("ERROR");
            response.setResponseMessage("Error "+e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
		}
    	
    	response.setResponseCode("SUCCESS");
        response.setResponseMessage("Update Status Success");
        return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.OK);
    }
    
    @Override
    @Transactional
    public Object addShop(ShopDto shop) {
    	Shop entity = new Shop();
    	if(!validateRequest(shop, ADD_TYPE)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Shop> isExistShop = repository.findFirstByNameAndStatusIsNot(shop.getName(), STATUS_DELETED);
    	if(isExistShop.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Shop name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Shop> shopByUserId = repository.findByUserIDAndStatusIsNot(shop.getUserID(),STATUS_DELETED);
    	if(shopByUserId.isPresent()) {
       	    response.setResponseCode("FAILED");
            response.setResponseMessage("You already have a shop!");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
    	
    	try {
    		entity.setName(shop.getName());
    		entity.setAddress(shop.getAddress());
    		entity.setDescription(shop.getDescription());
    		entity.setUserID(shop.getUserID());
    		entity.setStatus(STATUS_READY);
    		entity.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
    		entity.setPriority(2);
    		entity.setLatitude(shop.getLatitude());
    		entity.setLogoFilePath(shop.getLongitude());
    		repository.save(entity);
    		saveLogo(entity, shop.getLogoBase64());
    		updateUserDatasAndPhotos(shop.getUserDto());
    		
		} catch (Exception e) {
			response.setResponseCode("ERROR");
            response.setResponseMessage("Error "+e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
		}
    	response.setResponseCode("SUCCESS");
        response.setResponseMessage("Add Success");
        return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.OK);
    }
    
    @Override
    public Object editShop(ShopDto shop) {
    	Shop entity = new Shop();
    	if(!validateRequest(shop, EDIT_TYPE) && shop.getId()!=null) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Shop> shopById  = repository.findByIdAndStatusIsNot(shop.getId(), STATUS_DELETED);
    	if(!shopById.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Shop is not exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Shop> isExistShop = repository.findFirstByNameAndIdIsNotAndStatusIsNot(shop.getName(), shop.getId(), STATUS_DELETED);
    	if(isExistShop.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Shop name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity = shopById.get();
    		entity.setName(shop.getName());
    		entity.setAddress(shop.getAddress());
    		entity.setDescription(shop.getDescription());
    		entity.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
    		entity.setUpdatedBy(shop.getUpdatedBy());
    		entity.setLatitude(shop.getLatitude());
    		entity.setLogoFilePath(shop.getLongitude());
    		repository.save(entity);
    		saveLogo(entity, shop.getLogoBase64());
    		updateUserDatasAndPhotos(shop.getUserDto());
		} catch (Exception e) {
			response.setResponseCode("ERROR");
            response.setResponseMessage("Error "+e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
		}
    	
    	response.setResponseCode("SUCCESS");
        response.setResponseMessage("Edit Success");
        return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.OK);
    }
    
    @Override
    public byte[] getLogoFile(String urlPath) {
    	Optional<Shop> shop = repository.findFirstByLogoUrlPathAndStatusIsNot(urlPath, STATUS_DELETED);
        if (!shop.isPresent()) {
            logger.debug("Image logo not found from repository");
            return null;
        }
        String imageFilePath = shop.get().getLogoFilePath();
        if (imageFilePath == null || imageFilePath.trim().equalsIgnoreCase("")) {
            logger.debug("Image logo from repository is empty");
        }
        try {
            File initialFileImage = new File(imageFilePath);
            if (!initialFileImage.exists()) {
                logger.debug("initialFileImage logo Path not found");
                return null;
            }
            InputStream in = new FileInputStream(initialFileImage);
            return IOUtils.toByteArray(in);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }
    
    @Transactional
    void updateUserDatasAndPhotos(UserDto userDto) {
    	logger.debug("Start Update User Data : " + userDto.getId());
    	Optional<Users> users = userRepository.findById(userDto.getId());
		Users entity = users.get();
		entity.setKtpNumber(userDto.getKtpNumber());
		entity.setType(USER_TYPE_SHOP_OWNER);
    	 try {
    		//delete profile photos
	    	if(userDto.getProfileFilePath()!=null) {
	    		File directory = new File(userDto.getProfileFilePath());
	            boolean fileDeleted = directory.delete();
	            if (fileDeleted) {
	                logger.debug("success delete file : " + userDto.getProfileFilePath());
	            }
	    	}
	    	
	    	//delete ktp photos
	    	if(userDto.getKtpFilePath()!=null) {
	    		File directory = new File(userDto.getKtpFilePath());
	            boolean fileDeleted = directory.delete();
	            if (fileDeleted) {
	                logger.debug("success delete file : " + userDto.getKtpFilePath());
	            }
	    	}
	        
	    	//Save Profile Photos
	    	logger.debug("start save profile photos : " + userDto.getProfileFilePath());
	    	Long timeInMilis = Utils.getCalendar().getTimeInMillis();
	        String pathFolder = "img/profile/" + userDto.getId() + "/";
	        String imageFileName = userDto.getId() + "_" + timeInMilis + ".jpg";
	        Path destinationFile = Paths.get(pathFolder, imageFileName);
	        File directory = new File(pathFolder);
	        if (!directory.exists()) {
	            boolean created = directory.mkdirs();
	            if (created) {
	                logger.debug("success create profile folder");
	            }
	        }
	        
	        try {
	            String rawBase64 = userDto.getProfileBase64();
	            String base64ReplaceNewline = rawBase64.replaceAll("\n", "");
	            byte[] decodedImg = Base64.getDecoder()
	                    .decode(base64ReplaceNewline.getBytes(StandardCharsets.UTF_8));
	            Files.write(destinationFile, decodedImg);
	            rawBase64=null;
	        } catch (Exception ex) {
	            logger.debug(ex.getMessage());

	        }
	        
	        entity.setProfileFilePath(destinationFile.toString());
	        entity.setProfileUrlPath(imageFileName);
	        
	        try {
	            String compressImagePath = compressImage(pathFolder, imageFileName, 0.05f);
	            entity.setProfileUrlPathHome(compressImagePath);
	            entity.setProfileFilePathHome(pathFolder + compressImagePath);
	        } catch (Exception ex) {
	            logger.debug("error : " + ex.getMessage());
	        }
    		
	        //Save Ktp Photos
	        pathFolder = "img/ktp/" + userDto.getId() + "/";
	        imageFileName = userDto.getId() + "_" + timeInMilis + ".jpg";
	        destinationFile = Paths.get(pathFolder, imageFileName);
	        File directoryKtp = new File(pathFolder);
	        if (!directoryKtp.exists()) {
	            boolean created = directoryKtp.mkdirs();
	            if (created) {
	                logger.debug("success create ktp folder");
	            }
	        }
	        
	        try {
	            String rawBase64 = userDto.getKtpBase64();
	            String base64ReplaceNewline = rawBase64.replaceAll("\n", "");
	            byte[] decodedImg = Base64.getDecoder()
	                    .decode(base64ReplaceNewline.getBytes(StandardCharsets.UTF_8));
	            Files.write(destinationFile, decodedImg);
	            rawBase64=null;
	        } catch (Exception ex) {
	            logger.debug(ex.getMessage());

	        }
	        
	        entity.setKtpFilePath(destinationFile.toString());
	        entity.setKtpUrlPath(imageFileName);
	        
	        try {
	            String compressImagePath = compressImage(pathFolder, imageFileName, 0.05f);
	            entity.setKtpUrlPathHome(compressImagePath);
	            entity.setKtpFilePathHome(pathFolder + compressImagePath);
	        } catch (Exception ex) {
	            logger.debug("error : " + ex.getMessage());
	        }
	        userRepository.save(entity);
    		 
         } catch (Exception ex) {
        	 logger.error("error save user datas and photos: " + userDto.getId());
             logger.debug(ex.getMessage());
         }
    	 logger.debug("Success Update User Data : " + userDto.getId());
    }
    
    @Transactional
    void saveLogo(Shop shop, String logoBase64) {
    	//delete existing
    	if(shop.getLogoFilePath()!=null) {
    		File directory = new File(shop.getLogoFilePath());
            boolean fileDeleted = directory.delete();
            if (fileDeleted) {
                logger.debug("success delete file : " + shop.getLogoFilePath());
            }
    	}
        
    	Long timeInMilis = Utils.getCalendar().getTimeInMillis();
        String pathFolder = "img/shop/" + shop.getId() + "/";
        String imageFileName = shop.getId() + "_" + timeInMilis + ".jpg";
        Path destinationFile = Paths.get(pathFolder, imageFileName);
        File directory = new File(pathFolder);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                logger.debug("success create folder");
            }
        }
        
        try {
            String rawBase64 = logoBase64;
            String base64ReplaceNewline = rawBase64.replaceAll("\n", "");
            byte[] decodedImg = Base64.getDecoder()
                    .decode(base64ReplaceNewline.getBytes(StandardCharsets.UTF_8));
            Files.write(destinationFile, decodedImg);
        } catch (Exception ex) {
            logger.debug(ex.getMessage());

        }
        
        shop.setLogoFilePath(destinationFile.toString());
        shop.setLogoUrlPath(imageFileName);
        
        try {
            String compressImagePath = compressImage(pathFolder, imageFileName, 0.05f);
            shop.setLogoUrlPathHome(compressImagePath);
            shop.setLogoFilePathHome(pathFolder + compressImagePath);
        } catch (Exception ex) {
            logger.debug("error : " + ex.getMessage());
        }
        repository.save(shop);
        logger.debug("info : save logo success");
    }
    
    private Boolean validateRequest(ShopDto shop, Integer type) {
    	if(shop.getName()==null || shop.getName()=="" 
    		|| shop.getDescription()==null  || shop.getDescription()=="" 
    		|| shop.getUserID()==null) {
    		return false;
    	}
    		
    	if(type == EDIT_TYPE) {
    		if(shop.getId()==null) {
    			return false;
    		}
    	}
    	return true;
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
