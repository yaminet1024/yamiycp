package com.example.yami.yamiycp.model;

import java.io.Serializable;

public class MyInfo implements Serializable {
    private String name;
    private String reportData;
    private String idCard;
    private String phone;
    private String carStyle;
    private String learning;
    private String remainProject;
    private String learnProject;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReportData() {
        return reportData;
    }

    public void setReportData(String reportData) {
        this.reportData = reportData;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCarStyle() {
        return carStyle;
    }

    public void setCarStyle(String carStyle) {
        this.carStyle = carStyle;
    }

    public String getLearning() {
        return learning;
    }

    public void setLearning(String learning) {
        this.learning = learning;
    }

    public String getRemainProject() {
        return remainProject;
    }

    public void setRemainProject(String remainProject) {
        this.remainProject = remainProject;
    }

    public String getLearnProject() {
        return learnProject;
    }

    public void setLearnProject(String learnProject) {
        this.learnProject = learnProject;
    }
}
