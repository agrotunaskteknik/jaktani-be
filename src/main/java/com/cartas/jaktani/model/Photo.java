package com.cartas.jaktani.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "photo")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Integer id;
    @Column(name = "refference_id")
    private Integer refferenceId;
    @Column(name = "url_path")
    public String urlPath;
    @Column(name = "url_path_home")
    public String urlPathHome;
    @Column(name = "image_file_path")
    public String imageFilePath;
    @Column(name = "image_file_path_home")
    public String imageFilePathHome;
    @Column(name = "created_time")
    public Timestamp createdTime;
    @Column(name = "updated_time")
    public Timestamp updatedTime;
    @Column(name = "status")
    public Integer status;
    @Column(name = "order_number")
    public Integer orderNumber;
    @Column(name = "parent_url")
    public String parentUrl;

    public Photo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRefferenceId() {
        return refferenceId;
    }

    public void setRefferenceId(Integer refferenceId) {
        this.refferenceId = refferenceId;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getUrlPathHome() {
        return urlPathHome;
    }

    public void setUrlPathHome(String urlPathHome) {
        this.urlPathHome = urlPathHome;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getImageFilePathHome() {
        return imageFilePathHome;
    }

    public void setImageFilePathHome(String imageFilePathHome) {
        this.imageFilePathHome = imageFilePathHome;
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

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Photo{");
        sb.append("id=").append(id);
        sb.append(", refferenceId=").append(refferenceId);
        sb.append(", urlPath='").append(urlPath).append('\'');
        sb.append(", urlPathHome='").append(urlPathHome).append('\'');
        sb.append(", imageFilePath='").append(imageFilePath).append('\'');
        sb.append(", imageFilePathHome='").append(imageFilePathHome).append('\'');
        sb.append(", createdTime=").append(createdTime);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append(", status=").append(status);
        sb.append(", orderNumber=").append(orderNumber);
        sb.append(", parentUrl='").append(parentUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
