package com.cartas.jaktani.util;

import com.cartas.jaktani.config.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

public class Utils {
    private static final String AUTHORIZATION = "authorization";
    private static final String LANG = "accept-language";

    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    public static Timestamp getTimeStamp(Long time) {
        return new Timestamp(time);
    }

    public static Boolean isUserEligibleToEdit(String tokenJWT, Integer userIDWantToBeEdited) {
        // call jwt parser and compare it to userIDWantToBeEdited
        Claims claims = JwtTokenUtil.getAllClaimsFromToken(tokenJWT);
        Object userIDObj = claims.get(JwtTokenUtil.USER_ID);
        if (userIDObj != null) {
            Integer userID = (Integer) userIDObj;
            return userID.equals(userIDWantToBeEdited);
        }

        return false;
    }

    public static BaseHeader getHeaderValue(Map<String, String> headers) {
        BaseHeader baseHeader = new BaseHeader();
        headers.forEach((key, value) -> {
            System.out.println(String.format("Header '%s' = %s", key, value));
            switch (key) {
                case AUTHORIZATION:
                    String[] tokenArray = value.split(" ");
                    if (tokenArray.length > 1 && tokenArray[0].equalsIgnoreCase("Bearer") && !tokenArray[1].trim().equalsIgnoreCase("")) {
                        baseHeader.setToken(tokenArray[1]);
                    }
                    break;
                case LANG:
                    baseHeader.setLang(value);
                    break;
                default:
                    //nothing
            }
        });
        return baseHeader;
    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
}
