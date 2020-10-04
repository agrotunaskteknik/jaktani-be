package com.cartas.jaktani.jwt;

import com.cartas.jaktani.controller.SendMail;
import com.cartas.jaktani.dto.UserDto;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.model.OTP;
import com.cartas.jaktani.model.Users;
import com.cartas.jaktani.repository.OTPRepository;
import com.cartas.jaktani.repository.UserRepository;
import com.cartas.jaktani.util.Utils;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPRepository otpRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("javainuse".equals(username)) {
            return new User("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                    new ArrayList<>());
        } else {
            Optional<Users> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                return new User(user.get().getUsername(), user.get().getPassword(), new ArrayList<>());
            }
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public void requestOTPForRegister(String email, String mobilePhoneNumber, String username) throws Exception {
        if (!email.trim().equals("") || !username.trim().equals("")) {
            Optional<Users> user = userRepository.findByUsernameOrEmail(username, email);
            if (user.isPresent()) {
                throw new ResourceNotFoundException("User with username and email already exist!");
            }
            generateOTPAndSentToEmail(email, mobilePhoneNumber, username, 0);
            return;
        }
        throw new ResourceNotFoundException("empty field");
    }

    public void requestOTPForForgotPassword(String email, String mobilePhoneNumber, String username) throws Exception {
        if (!email.trim().equals("") || !username.trim().equals("")) {
            Optional<Users> user = userRepository.findByUsernameOrEmail(username, email);
            if (!user.isPresent()) {
                throw new ResourceNotFoundException("User with username and email not exist!");
            }
            Users userModel = user.get();
            generateOTPAndSentToEmail(userModel.getEmail(), userModel.getMobilePhoneNumber(), userModel.getUsername(), userModel.getId());
            return;
        }
        throw new ResourceNotFoundException("empty field");
    }


    public void generateOTPAndSentToEmail(String email, String mobilePhoneNumber, String username, Integer userID) throws Exception {
        String otpGen = RandomStringUtils.randomAlphanumeric(10);
        OTP otp = new OTP();
        otp.setUserID(userID);
        otp.setOtpCode(otpGen);
        otp.setEmail(email);
        otp.setUsername(username);
        otp.setMobilePhoneNumber(mobilePhoneNumber);
        otp.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        otp.setRequestTime(otp.getCreatedTime());
        Optional<OTP> otpOptional = otpRepository.findByUsernameOrEmailOrMobilePhoneNumber(username, email, mobilePhoneNumber);
        if(otpOptional.isPresent()){
            otp.setId(otpOptional.get().getId());
            otp.setCreatedTime(otpOptional.get().getCreatedTime());
            otp.setRequestTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        }

        otpRepository.save(otp);
        SendMail.sentOTPToEmail(email, otpGen);

    }

    public OTP validateOTP(UserDto userDto) throws Exception {
        if (null == userDto || userDto.getEmail().trim().equalsIgnoreCase("") || userDto.getUsername().trim().equalsIgnoreCase("")
                || userDto.getOTP().equalsIgnoreCase("")) {
            throw new ResourceNotFoundException("empty field");
        }
        Optional<OTP> otp = otpRepository.findByOtpCode(userDto.getOTP());
        if (!otp.isPresent()) {
            throw new ResourceNotFoundException("otp not found");

        }
        OTP otpModel = otp.get();
        // if username or email is not the same then throw error
        if (!otpModel.getUsername().equalsIgnoreCase(userDto.getUsername()) || !otpModel.getEmail().equalsIgnoreCase(userDto.getEmail())) {
            throw new ResourceNotFoundException("OTP Mismatch");
        }
        return otpModel;
    }

}
