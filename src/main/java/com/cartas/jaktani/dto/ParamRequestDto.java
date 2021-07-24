package com.cartas.jaktani.dto;

public class ParamRequestDto {
    public Integer pageNumber;
    public Integer pageSize;
    public String keySearch;
    public Integer shopId;
    public Integer categoryId;
    public Boolean isShowMitraOnly;

	public ParamRequestDto() {
	}

	public ParamRequestDto(Integer pageNumber, Integer pageSize, String keySearch, Integer shopId, Integer categoryId, Boolean isShowMitraOnly) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.keySearch = keySearch;
		this.shopId = shopId;
		this.categoryId = categoryId;
		this.isShowMitraOnly = isShowMitraOnly;
	}

	public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }

	public Boolean getShowMitraOnly() {
		return isShowMitraOnly;
	}

	public void setShowMitraOnly(Boolean showMitraOnly) {
		isShowMitraOnly = showMitraOnly;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ParamRequestDto{");
		sb.append("pageNumber=").append(pageNumber);
		sb.append(", pageSize=").append(pageSize);
		sb.append(", keySearch='").append(keySearch).append('\'');
		sb.append(", shopId=").append(shopId);
		sb.append(", categoryId=").append(categoryId);
		sb.append(", isShowMitraOnly=").append(isShowMitraOnly);
		sb.append('}');
		return sb.toString();
	}
}
