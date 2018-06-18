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

    public WorldPopulation(String jobname, String image, String profilename, String username, String jobId, String employerId, String employeeId, String channelid,String userid) {
        this.jobname = jobname;
        this.image = image;
        this.profilename = profilename;
        this.username = username;
        this.jobId = jobId;
        this.employerId = employerId;
        this.employeeId = employeeId;
        this.channelid = channelid;
        this.userid = userid;
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

}
