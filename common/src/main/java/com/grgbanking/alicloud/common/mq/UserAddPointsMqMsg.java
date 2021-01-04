package com.grgbanking.alicloud.common.mq;

import java.io.Serializable;

/**
 * @author machao
 */
public class UserAddPointsMqMsg implements Serializable {
    private String userid;
    private int points;
    private String description;
    private String event;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
