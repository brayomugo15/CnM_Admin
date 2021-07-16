package com.project.cmadmin.models;

public class Order {

    private String orderno, companyid, planname, email, orderdate, orderno_status;

    public Order() {
    }

    public Order(String orderno, String companyid, String planname, String email, String orderdate, String orderno_status) {
        this.orderno = orderno;
        this.companyid = companyid;
        this.planname = planname;
        this.email = email;
        this.orderdate = orderdate;
        this.orderno_status = orderno_status;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getOrderno_status() {
        return orderno_status;
    }

    public void setOrderno_status(String orderno_status) {
        this.orderno_status = orderno_status;
    }
}
