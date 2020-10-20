package com.cartas.jaktani.jwt;

import com.cartas.jaktani.controller.SendMail;
import com.cartas.jaktani.dto.OTPRequest;
import com.cartas.jaktani.dto.UserDto;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.model.OTP;
import com.cartas.jaktani.model.Users;
import com.cartas.jaktani.repository.OTPRepository;
import com.cartas.jaktani.repository.UserRepository;
import com.cartas.jaktani.service.UserServiceImpl;
import com.cartas.jaktani.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);

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
            Optional<Users> user = userRepository.findByUsernameAndStatus(username, UserServiceImpl.USER_STATUS_ACTIVE);
            if (user.isPresent()) {
                return new User(user.get().getUsername(), user.get().getPassword(), new ArrayList<>());
            }
            logger.debug("User not found with username: " + username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public void requestOTPForRegister(OTPRequest otpRequest) throws Exception {
        if (!otpRequest.getEmail().trim().equals("") || !otpRequest.getUsername().trim().equals("")) {
            Optional<Users> user = userRepository.findByUsernameOrEmailAndStatus(otpRequest.getUsername(), otpRequest.getEmail(), UserServiceImpl.USER_STATUS_ACTIVE);
            if (user.isPresent()) {
                logger.debug("Username/Email tersebut sudah terdaftar");
                throw new ResourceNotFoundException("Username/Email tersebut sudah terdaftar");
            }
            String messageBodyRegister = "Halo! Terimakasih telah melakukan pendaftaran akun\n" +
                    "Aktifkan akun Anda dengan kode di bawah ini \n" +
                    "\n" +
                    "((otpCode))\n" +
                    "\n" +
                    "Kami perlu memastikan bahwa email Anda benar dan tidak disalahgunakan oleh pihak yang tidak berkepentingan.\n" +
                    "\n" +
                    "Terima Kasih,\n" +
                    "Team Jak Tani\n" +
                    "\n" +
                    "Pesan ini akan otomatis kadaluwarsa dalam waktu 24 jam kedepan. ";
            String messageSubjectRegister = "Kode Verifikasi Pendaftaran Akun Baru";
            generateOTPAndSentToEmail(otpRequest.getEmail(), otpRequest.getMobileNumber(), otpRequest.getUsername(), 0,
                    messageBodyRegister, messageSubjectRegister);
            return;
        }
        throw new ResourceNotFoundException("empty field");
    }

    public void requestOTPForForgotPassword(String email, String mobilePhoneNumber, String username) throws Exception {
        if (!email.trim().equals("") || !username.trim().equals("")) {
            Optional<Users> user = userRepository.findByUsernameOrEmailAndStatus(username, email, UserServiceImpl.USER_STATUS_ACTIVE);
            if (!user.isPresent()) {
                logger.debug("Maaf, akun dengan username/email tersebut tidak terdaftar");
                throw new ResourceNotFoundException("Maaf, akun dengan username/email tersebut tidak terdaftar");
            }
            Users userModel = user.get();
            String messageBodyForgotPassword = "Halo, Anda telah meminta untuk mengubah password \n" +
                    "Ubah password Anda dengan kode di bawah ini \n" +
                    "\n" +
                    "((otpCode))\n" +
                    "\n" +
                    "Jika Anda tidak merasa meminta hal ini, harap abaikan email ini\n" +
                    "Terima Kasih,\n" +
                    "Team Jak Tani\n" +
                    "\n" +
                    "Pesan ini akan otomatis kadaluwarsa dalam waktu 24 jam kedepan. ";
            String messageSubjectForgotPassword = "Kode Verifikasi Ubah Password";
            generateOTPAndSentToEmail(userModel.getEmail(), userModel.getMobilePhoneNumber(), userModel.getUsername(),
                    userModel.getId(), messageBodyForgotPassword,
                    messageSubjectForgotPassword);
            return;
        }
        throw new ResourceNotFoundException("empty field");
    }


    public void generateOTPAndSentToEmail(String email, String mobilePhoneNumber, String username, Integer userID, String messageBody,
                                          String messageSubject) {
        String otpGen = Utils.getRandomNumberString();
        OTP otp = new OTP();
        otp.setUserID(userID);
        otp.setOtpCode(otpGen);
        otp.setEmail(email);
        otp.setUsername(username);
        otp.setMobilePhoneNumber(mobilePhoneNumber);
        otp.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        otp.setRequestTime(otp.getCreatedTime());
        Optional<OTP> otpOptional = otpRepository.findByEmail(email);
        // if present then update it
        if (otpOptional.isPresent()) {
            otp.setId(otpOptional.get().getId());
            otp.setCreatedTime(otpOptional.get().getCreatedTime());
            otp.setRequestTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        }

        otpRepository.save(otp);
        messageBody = messageBody.replace("((otpCode))",otpGen);
        try {
            String finalMessageBody = messageBody;
            Thread newThread = new Thread(() -> {
                try {
                    SendMail.sentOTPToEmail(email, finalMessageBody, messageSubject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            newThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("Error caught : " + e.getMessage());
        }

    }

    public OTP validateOTP(UserDto userDto) throws Exception {
        if (null == userDto || userDto.getEmail().trim().equalsIgnoreCase("")) {
            logger.debug("Empty Field");
            throw new ResourceNotFoundException("empty field");
        }
        Optional<OTP> otp = otpRepository.findByOtpCode(userDto.getOtp());
        if (!otp.isPresent()) {
            logger.debug("OTP not found email : "+ userDto.getEmail());
            throw new ResourceNotFoundException("otp not found");

        }
        OTP otpModel = otp.get();
        // if email is not the same then throw error
        if (!otpModel.getEmail().equalsIgnoreCase(userDto.getEmail())) {
            logger.debug("OTP is wrong email : "+ userDto.getEmail());
            throw new ResourceNotFoundException("Kode verifikasi yang Anda masukkan salah");
        }
        return otpModel;
    }

}
