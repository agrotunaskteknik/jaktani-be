package com.cartas.jaktani.dto;

public class UserGooglePayloadDto {
    private String email;
    private boolean emailVerified;
    private String name;
    private String pictureUrl;
    private String locale;
    private String familyName;
    private String givenName;
    private String googleUserID;

    public UserGooglePayloadDto() {
    }

    public UserGooglePayloadDto(String email, boolean emailVerified, String name, String pictureUrl, String locale, String familyName, String givenName, String googleUserID) {
        this.email = email;
        this.emailVerified = emailVerified;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.locale = locale;
        this.familyName = familyName;
        this.givenName = givenName;
        this.googleUserID = googleUserID;
    }

    public String getGoogleUserID() {
        return googleUserID;
    }

    public void setGoogleUserID(String googleUserID) {
        this.googleUserID = googleUserID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserGooglePayloadDto{");
        sb.append("email='").append(email).append('\'');
        sb.append(", emailVerified=").append(emailVerified);
        sb.append(", name='").append(name).append('\'');
        sb.append(", pictureUrl='").append(pictureUrl).append('\'');
        sb.append(", locale='").append(locale).append('\'');
        sb.append(", familyName='").append(familyName).append('\'');
        sb.append(", givenName='").append(givenName).append('\'');
        sb.append(", googleUserID=").append(googleUserID);
        sb.append('}');
        return sb.toString();
    }
}
