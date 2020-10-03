package com.cartas.jaktani.controller;

import com.cartas.jaktani.config.JwtTokenUtil;
import com.cartas.jaktani.dto.*;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.jwt.JwtUserDetailsService;
import com.cartas.jaktani.model.OTP;
import com.cartas.jaktani.service.UserService;
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
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;

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

    @RequestMapping(value = "/authenticate/request_register_otp", method = RequestMethod.POST)
    public ResponseEntity<?> requestRegisterOTP(@RequestBody OTPRequest otpRequest) throws Exception {
        userDetailsService.requestOTPForRegister(otpRequest.getEmail(), otpRequest.getMobileNumber(), otpRequest.getUsername());
        return ResponseEntity.ok().body("Success Request OTP for register");
    }

    // can only save user if otp is correct
    @RequestMapping(value = "/authenticate/register_user", method = RequestMethod.POST)
    public ResponseEntity<?> saveUserPassword(@RequestBody UserDto userDto) throws Exception {
        OTP otp = userDetailsService.validateOTP(userDto);
        if (otp == null) {
            throw new ResourceNotFoundException("OTP is not valid");
        }
        UserDto userSaved = userService.addUser(userDto);
        if (userSaved == null) {
            throw new ResourceNotFoundException("Failed save User");
        }
        return ResponseEntity.ok().body(userSaved);
    }

    @RequestMapping(value = "/authenticate/request_reset_password_otp", method = RequestMethod.POST)
    public ResponseEntity<?> requestResetPasswordOTP(@RequestBody OTPRequest otpRequest) throws Exception {
        userDetailsService.requestOTPForForgotPassword(otpRequest.getEmail(), otpRequest.getMobileNumber(), otpRequest.getUsername());
        return ResponseEntity.ok().body("Success Request OTP for forgot password");
    }

    // can only reset password if otp is correct
    @RequestMapping(value = "/authenticate/reset_password", method = RequestMethod.POST)
    public ResponseEntity<?> resetPassword(@RequestBody UserDto userDto) throws Exception {
        OTP otp = userDetailsService.validateOTP(userDto);
        if (otp == null) {
            throw new ResourceNotFoundException("OTP is not valid");
        }
        UserDto userSaved = userService.editPasswordByUserID(Long.parseLong(otp.getUserID().toString()), userDto);
        if (userSaved == null) {
            throw new ResourceNotFoundException("Failed reset password");
        }
        return ResponseEntity.ok().body(userSaved);
    }

    @RequestMapping(value = "/authenticate/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody UserDto authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        UserDto user = userService.getUserByUsername(userDetails.getUsername());
        if (null == user) {
            throw new ResourceNotFoundException("User not found for this username :: " + userDetails.getUsername());
        }
        return ResponseEntity.ok(new LoginResponse("", "OK", "Success login user!", user, new JwtResponse(token)));
    }

}
