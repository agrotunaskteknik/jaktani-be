package com.cartas.jaktani.controller;

import com.cartas.jaktani.config.JwtTokenUtil;
import com.cartas.jaktani.dto.*;
import com.cartas.jaktani.jwt.JwtUserDetailsService;
import com.cartas.jaktani.model.OTP;
import com.cartas.jaktani.model.Shop;
import com.cartas.jaktani.service.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


@RestController
@CrossOrigin
public class JwtAuthenticationController {
    Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);
    String CLIENT_ID = "259775443073-913e3g72jft5kablf8ejhq5f67d16vio.apps.googleusercontent.com";
    String prefixGooglePassword = "prefixMaster";

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    UserService userService;
    @Autowired
    AddressService addressService;
    @Autowired
    ShopService shopService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @RequestMapping(value = "/authentication/request_register_otp", method = RequestMethod.POST)
    public ResponseEntity<?> requestRegisterOTP(@RequestBody OTPRequest otpRequest) {
        try {
            userDetailsService.requestOTPForRegister(otpRequest);
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("", "OK", "Success Request OTP for register")));
        } catch (Exception e) {
            logger.debug("Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    // can only save user if otp is correct
    @RequestMapping(value = "/authentication/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUserPassword(@RequestBody UserDto userDto) {
        try {
            OTP otp = userDetailsService.validateOTP(userDto);
            if (otp == null) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Kode verifikasi yang Anda masukkan salah", "NOT_OK", "")));
            }
            UserDto userSaved = userService.addUser(userDto);
            if (userSaved == null) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Failed Register User", "NOT_OK", "")));
            }

            LoginResponse loginResponse = new LoginResponse("", "OK", "Success login user!", userSaved, new JwtResponse());
            return ResponseEntity.ok().body(new ParentResponse(loginResponse));
        } catch (Exception e) {
            logger.debug("Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/authentication/request_reset_password_otp", method = RequestMethod.POST)
    public ResponseEntity<?> requestResetPasswordOTP(@RequestBody OTPRequest otpRequest) {
        try {
            userDetailsService.requestOTPForForgotPassword(otpRequest.getEmail(), otpRequest.getMobileNumber(), otpRequest.getUsername());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("", "OK", "Success Request OTP for forgot password")));
        } catch (Exception e) {
            logger.debug("Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    // can only reset password if otp is correct
    @RequestMapping(value = "/authentication/reset_password", method = RequestMethod.POST)
    public ResponseEntity<?> resetPassword(@RequestBody UserDto userDto) {
        try {
            OTP otp = userDetailsService.validateOTP(userDto);
            if (otp == null) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("OTP is not valid", "NOT_OK", "")));
            }
            UserDto userSaved = userService.editPasswordByUserID(Long.parseLong(otp.getUserID().toString()), userDto);
            if (userSaved == null) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Failed Reset Password", "NOT_OK", "")));
            }
            LoginResponse loginResponse = new LoginResponse("", "OK", "Success reset password!", userSaved, new JwtResponse());
            return ResponseEntity.ok().body(new ParentResponse(loginResponse));
        } catch (Exception e) {
            logger.debug("Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/authentication/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody UserDto authenticationRequest) {
        //authenticate user password
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        } catch (Exception e) {
            logger.debug("Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Username atau password yang Anda masukkan tidak valid", "REQUEST_DENIED", "")));
        }

        return getLoginResponseDetailToken(authenticationRequest);
    }

    public ResponseEntity<?> getLoginResponseDetailToken(UserDto authenticationRequest) {
        try {
            LoginResponse loginResponseDetail = getDetailLoginToken(authenticationRequest);
            if (loginResponseDetail.getUser() == null) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Maaf,  akun dengan username/email tersebut tidak ditemukan",
                        "NOT_OK", "")));
            }
            if (loginResponseDetail.getToken() == null) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Error Save Token", "NOT_OK", "")));
            }

            LoginResponse loginResponse = new LoginResponse("", "OK", "Success login user!", loginResponseDetail.getUser(), loginResponseDetail.getToken());
            return ResponseEntity.ok().body(new ParentResponse(loginResponse));
        } catch (Exception e) {
            logger.error("Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    public LoginResponse getDetailLoginToken(UserDto authenticationRequest) throws Exception {
        LoginResponse loginResponse = new LoginResponse();
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        //get user by username
        UserDto userDto = userService.getUserByUsername(userDetails.getUsername());
        if (null == userDto) {
            return loginResponse;
        }
        loginResponse.setUser(userDto);
        //save token with user id
        final String generatedToken = jwtTokenUtil.generateTokenWithUserDetails(userDetails, userDto);
        JwtResponse token = tokenService.save(generatedToken, userDto, "");
        if (null == token) {
            return loginResponse;
        }

        try {
            // get user default address
            AddressDetailDto userAddress = addressService.getDefaultAddressByRelationIdAndType(userDto.getId(), AddressServiceImpl.TYPE_USER);
            userDto.setUserAddress(userAddress);
        } catch (Exception e) {
            logger.error("Caught Error : " + e.getMessage());
            throw e;
        }
        Shop shop = new Shop();
        try {
            // get shop by user id
            shop = shopService.getShopObjectByUserID(userDto.getId());
            userDto.setShop(shop);
        } catch (Exception e) {
            logger.error("Caught Error : " + e.getMessage());
            throw e;
        }

        try {
            // get shop default address
            AddressDetailDto shopAddress = addressService.getDefaultAddressByRelationIdAndType(shop.getId(), AddressServiceImpl.TYPE_SHOP);
            userDto.setUserShopAddress(shopAddress);
        } catch (Exception e) {
            logger.error("Caught Error : " + e.getMessage());
            throw e;
        }

        loginResponse.setToken(token);
        loginResponse.setUser(userDto);
        return loginResponse;
    }

    @RequestMapping(value = "/authentication/login_google", method = RequestMethod.POST)
    public ResponseEntity<?> loginGoogle(@RequestBody LoginGoogleRequest googleRequest) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();
        UserGooglePayloadDto userGooglePayloadDto = getDetailUser(verifier, googleRequest);
        // login by password with prefix+userid, username = userid google
        UserDto authenticationRequest = setupUserDTOFromUserGoogle(userGooglePayloadDto);
        return getLoginResponseDetailToken(authenticationRequest);
    }

    public UserDto setupUserDTOFromUserGoogle(UserGooglePayloadDto userGooglePayloadDto) {
        String password = prefixGooglePassword + userGooglePayloadDto.getGoogleUserID();
        UserDto userDto = new UserDto();
        userDto.setEmail(userGooglePayloadDto.getEmail());
        userDto.setUsername(userGooglePayloadDto.getGoogleUserID() + "");
        userDto.setFullName(userGooglePayloadDto.getName());
        userDto.setPassword(password);
        return userDto;
    }

    @RequestMapping(value = "/authentication/register_google", method = RequestMethod.POST)
    public ResponseEntity<?> registerGoogle(@RequestBody LoginGoogleRequest googleRequest) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();
        UserGooglePayloadDto userGooglePayloadDto = getDetailUser(verifier, googleRequest);
        // save user with password prefix+userid, username = userid google
        UserDto userDto = setupUserDTOFromUserGoogle(userGooglePayloadDto);

        UserDto userSaved = userService.addUser(userDto);
        if (userSaved == null) {
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Failed Register User", "NOT_OK", "")));
        }

        LoginResponse loginResponse = new LoginResponse("", "OK", "Success login user!", userSaved, new JwtResponse());
        return ResponseEntity.ok().body(new ParentResponse(loginResponse));

    }

    public UserGooglePayloadDto getDetailUser(GoogleIdTokenVerifier verifier, LoginGoogleRequest googleRequest) throws GeneralSecurityException, IOException {
        UserGooglePayloadDto userGooglePayloadDto = new UserGooglePayloadDto();
        // (Receive idTokenString by HTTPS POST)
        GoogleIdToken idToken = verifier.verify(googleRequest.getGoogleJwtToken());
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            // Use or store profile information
            userGooglePayloadDto.setGoogleUserID(Long.parseLong(userId));
            userGooglePayloadDto.setEmail(email);
            userGooglePayloadDto.setEmailVerified(emailVerified);
            userGooglePayloadDto.setName(name);
            userGooglePayloadDto.setPictureUrl(pictureUrl);
            userGooglePayloadDto.setLocale(locale);
            userGooglePayloadDto.setFamilyName(familyName);
            userGooglePayloadDto.setGivenName(givenName);

        } else {
            logger.error("id token user not found");
        }
        return userGooglePayloadDto;
    }

}
