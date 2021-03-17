package com.cartas.jaktani.dto;

public class CourierDetailDto {
    private String image;
    private String title;
    private String value;
    private String description;

    public CourierDetailDto() {
    }

    public CourierDetailDto(String image, String title, String value, String description) {
        this.image = image;
        this.title = title;
        this.value = value;
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CourierDetailDto{");
        sb.append("image='").append(image).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
