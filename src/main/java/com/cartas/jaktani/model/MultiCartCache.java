package com.cartas.jaktani.model;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

// timeToLive = 20 (20 is second)
@RedisHash(value = "MultiCartCache",timeToLive = (30*60))
public class MultiCartCache implements Serializable {
    private String id;
    private Long userID;
    private List<CartCache> cartCacheList;

    public MultiCartCache(String id, Long userID, List<CartCache> cartCacheList) {
        this.id = id;
        this.userID = userID;
        this.cartCacheList = cartCacheList;
    }

    public MultiCartCache() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public List<CartCache> getCartCacheList() {
        return cartCacheList;
    }

    public void setCartCacheList(List<CartCache> cartCacheList) {
        this.cartCacheList = cartCacheList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MultiCartCache{");
        sb.append("id='").append(id).append('\'');
        sb.append(", userID=").append(userID);
        sb.append(", cartCacheList=").append(cartCacheList);
        sb.append('}');
        return sb.toString();
    }
}
