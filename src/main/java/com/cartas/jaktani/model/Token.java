package com.cartas.jaktani.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "token")
public class Token {
    private Integer id;
    private Integer userID;
    private String token;
    private Timestamp lastRequest;
    private Timestamp createdTime;
    private String deviceID;
    private String refreshToken;

    public Token() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "user_id")
    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(name = "last_request")
    public Timestamp getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(Timestamp lastRequest) {
        this.lastRequest = lastRequest;
    }

    @Column(name = "created_time")
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Column(name = "device_id")
    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    @Column(name = "refresh_token")
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Token{");
        sb.append("id=").append(id);
        sb.append(", userID=").append(userID);
        sb.append(", token='").append(token).append('\'');
        sb.append(", lastRequest=").append(lastRequest);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", deviceID='").append(deviceID).append('\'');
        sb.append(", refreshToken='").append(refreshToken).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
