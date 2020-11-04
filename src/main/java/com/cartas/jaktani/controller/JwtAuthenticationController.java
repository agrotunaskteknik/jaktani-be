package com.cartas.jaktani.controller;

import com.cartas.jaktani.config.JwtTokenUtil;
import com.cartas.jaktani.dto.*;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.jwt.JwtUserDetailsService;
import com.cartas.jaktani.model.OTP;
import com.cartas.jaktani.service.TokenService;
import com.cartas.jaktani.service.UserService;
import com.cartas.jaktani.util.MessagesProperties;
import io.jsonwebtoken.Claims;
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

@RestController
@CrossOrigin
public class JwtAuthenticationController {
    Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);


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

        try {
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(authenticationRequest.getUsername());

            //get user by username
            UserDto userDto = userService.getUserByUsername(userDetails.getUsername());
            if (null == userDto) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Maaf,  akun dengan username/email tersebut tidak ditemukan",
                        "NOT_OK", "")));
            }

            //save token with user id
            final String generatedToken = jwtTokenUtil.generateTokenWithUserDetails(userDetails, userDto);
            JwtResponse token = tokenService.save(generatedToken, userDto, "");
            if (null == token) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Error Save Token", "NOT_OK", "")));
            }

            LoginResponse loginResponse = new LoginResponse("", "OK", "Success login user!", userDto, token);
            return ResponseEntity.ok().body(new ParentResponse(loginResponse));
        } catch (Exception e) {
            logger.debug("Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }

    }

}
