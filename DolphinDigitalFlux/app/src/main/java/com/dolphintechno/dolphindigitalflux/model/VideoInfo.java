package com.dolphintechno.dolphindigitalflux.model;

public class VideoInfo {

    String vId;
    String vCtg;
    String vLink;
    String vDesc;
    String AuthName;
    String time;
    String youTubeVideoId;

    public String getvId() {
        return vId;
    }

    public void setvId(String vId) {
        this.vId = vId;
    }

    public String getvCtg() {
        return vCtg;
    }

    public void setvCtg(String vCtg) {
        this.vCtg = vCtg;
    }

    public String getvLink() {
        return vLink;
    }

    public void setvLink(String vLink) {
        this.vLink = vLink;
    }

    public String getvDesc() {
        return vDesc;
    }

    public void setvDesc(String vDesc) {
        this.vDesc = vDesc;
    }

    public String getAuthName() {
        return AuthName;
    }

    public void setAuthName(String authName) {
        AuthName = authName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getYouTubeVideoId() {
        return youTubeVideoId;
    }

    public void setYouTubeVideoId(String youTubeVideoId) {
        this.youTubeVideoId = youTubeVideoId;
    }
}
