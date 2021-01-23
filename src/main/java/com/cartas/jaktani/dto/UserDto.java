package com.cartas.jaktani.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

import javax.persistence.Column;

public class UserDto {
    public Integer id;
    public String fullName;
    public String username;
    public Integer type;
    public String password;
    public String email;
    public Integer status;
    public Integer gender;
    public String mobilePhoneNumber;
    public Date birthDate;
    public String otp;

    public String ktpNumber;
    public String ktpFilePath;
    public String ktpUrlPath;
    public String ktpFilePathHome;
    public String ktpUrlPathHome;
    public String ktpBase64;
	
    public String profileFilePath;
    public String profileUrlPath;
    public String profileFilePathHome;
    public String profileUrlPathHome;
    public String profileBase64;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public UserDto() {
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("full_name")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("gender")
    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @JsonProperty("mobile_phone_number")
    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    @JsonProperty("birth_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    
    

    public String getKtpNumber() {
		return ktpNumber;
	}

	public void setKtpNumber(String ktpNumber) {
		this.ktpNumber = ktpNumber;
	}

	public String getKtpFilePath() {
		return ktpFilePath;
	}

	public void setKtpFilePath(String ktpFilePath) {
		this.ktpFilePath = ktpFilePath;
	}

	public String getKtpUrlPath() {
		return ktpUrlPath;
	}

	public void setKtpUrlPath(String ktpUrlPath) {
		this.ktpUrlPath = ktpUrlPath;
	}

	public String getKtpFilePathHome() {
		return ktpFilePathHome;
	}

	public void setKtpFilePathHome(String ktpFilePathHome) {
		this.ktpFilePathHome = ktpFilePathHome;
	}

	public String getKtpUrlPathHome() {
		return ktpUrlPathHome;
	}

	public void setKtpUrlPathHome(String ktpUrlPathHome) {
		this.ktpUrlPathHome = ktpUrlPathHome;
	}

	public String getProfileFilePath() {
		return profileFilePath;
	}

	public void setProfileFilePath(String profileFilePath) {
		this.profileFilePath = profileFilePath;
	}

	public String getProfileUrlPath() {
		return profileUrlPath;
	}

	public void setProfileUrlPath(String profileUrlPath) {
		this.profileUrlPath = profileUrlPath;
	}

	public String getProfileFilePathHome() {
		return profileFilePathHome;
	}

	public void setProfileFilePathHome(String profileFilePathHome) {
		this.profileFilePathHome = profileFilePathHome;
	}

	public String getProfileUrlPathHome() {
		return profileUrlPathHome;
	}

	public void setProfileUrlPathHome(String profileUrlPathHome) {
		this.profileUrlPathHome = profileUrlPathHome;
	}

	public String getKtpBase64() {
		return ktpBase64;
	}

	public void setKtpBase64(String ktpBase64) {
		this.ktpBase64 = ktpBase64;
	}

	public String getProfileBase64() {
		return profileBase64;
	}

	public void setProfileBase64(String profileBase64) {
		this.profileBase64 = profileBase64;
	}

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDto{");
        sb.append("id=").append(id);
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", type=").append(type);
        sb.append(", password='").append(password).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", status=").append(status);
        sb.append(", gender=").append(gender);
        sb.append(", mobilePhoneNumber='").append(mobilePhoneNumber).append('\'');
        sb.append(", birthDate=").append(birthDate);
        sb.append(", otp='").append(otp).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
