package com.cartas.jaktani.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;


@Entity
@Table(name = "users")
public class Users {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Integer id;
	
	@Column(name = "full_name", nullable = false)
    public String fullName;
	
	@Column(name = "username", nullable = false)
    public String username;
	
	@Column(name = "type")
    public Integer type;
	
	@Column(name = "password", nullable = false)
    public String password;
	
	@Column(name = "email", nullable = false)
    public String email;
	
	@Column(name = "created_by", nullable = false)
    public Integer createdBy;
	
	@Column(name = "updated_by")
    public Integer updatedBy;
	
	@Column(name = "created_time", nullable = false)
    public Timestamp createdTime;
	
	@Column(name = "updated_time")
    public Timestamp updatedTime;
	
	@Column(name = "status", nullable = false)
    public Integer status;
	
	@Column(name = "gender")
    public Integer gender;
	
	@Column(name = "mobile_phone_number")
    public String mobilePhoneNumber;
	
	@Column(name = "birth_date")
    public Date birthDate;
    
    @Column(name = "ktp_number")
    public String ktpNumber;
    
    //KTP
    @Column(name = "ktp_file_path")
    public String ktpFilePath;
	
	@Column(name = "ktp_url_path")
    public String ktpUrlPath;
	
	@Column(name = "ktp_file_path_home")
    public String ktpFilePathHome;
	
	@Column(name = "ktp_url_path_home")
    public String ktpUrlPathHome;
	
	//Profile
	@Column(name = "profile_file_path")
    public String profileFilePath;
	
	@Column(name = "profile_url_path")
    public String profileUrlPath;
	
	@Column(name = "profile_file_path_home")
    public String profileFilePathHome;
	
	@Column(name = "profile_url_path_home")
    public String profileUrlPathHome;

    public Users() {
    }

    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
    
    public Timestamp getUpdatedTime() {
        return updatedTime;
    }
    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getGender() {
        return gender;
    }
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }
    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

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
    
}
