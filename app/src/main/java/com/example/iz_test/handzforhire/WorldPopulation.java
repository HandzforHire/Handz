package com.example.iz_test.handzforhire;

public class WorldPopulation {
    private String jobname;
    private String image;
    private String profilename;
    private String username;
    private String jobId;
    private String employerId;
    private String employeeId;
    private String channelid;
    private String userid;
    private String rating;
    private String rating_id;
    private String cat1;
    private String cat2;
    private String cat3;
    private String cat4;
    private String cat5;

    public WorldPopulation(String jobname, String image, String profilename, String username, String jobId, String employerId, String employeeId, String channelid,String userid,String ratingId,String ratingValue,String category1,String category2,String category3,String category4,String category5) {
        this.jobname = jobname;
        this.image = image;
        this.profilename = profilename;
        this.username = username;
        this.jobId = jobId;
        this.employerId = employerId;
        this.employeeId = employeeId;
        this.channelid = channelid;
        this.userid = userid;
        this.rating_id = ratingId;
        this.rating = ratingValue;
        this.cat1 = category1;
        this.cat2 = category2;
        this.cat3 = category3;
        this.cat4 = category4;
        this.cat5 = category5;
    }

    public String getName() {
        return this.jobname;
    }

    public String getImage() {
        return this.image;
    }

    public String getProfilename() {
        return this.profilename;
    }

    public String getUsername() {
        return this.username;
    }

    public String getJobId() {
        return this.jobId;
    }

    public String getEmployerId() {
        return this.employerId;
    }

    public String getEmployeeId() {
        return this.employeeId;
    }

    public String getChannel() {
        return this.channelid;
    }

    public String getUserid() {
        return this.userid;
    }

    public String getRatingId() {
        return this.rating_id;
    }

    public String getRatingValue() {
        return this.rating;
    }

    public String getCategory1() {
        return this.cat1;
    }
    public String getCategory2() {
        return this.cat2;
    }
    public String getCategory3() {
        return this.cat3;
    }
    public String getCategory4() {
        return this.cat4;
    }
    public String getCategory5() {
        return this.cat5;
    }


}
