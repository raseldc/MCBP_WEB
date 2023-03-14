/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.controller;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public class Batch {

    private int id;

    private String title;
    private String title_en;
    private String objective;
    private String course_motto;
    private String requirement;
    private String course_requirement;
    private String share;
    private String featured;

    private String admin_featured;
    private String banner_cb_id;

    private String video_cb_id;
    private String discount_id;
    private String clone_status;
    private String payment_status;
    private String resticted_url;

    public String getResticted_url() {
        return resticted_url;
    }

    public void setResticted_url(String resticted_url) {
        this.resticted_url = resticted_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getCourse_motto() {
        return course_motto;
    }

    public void setCourse_motto(String course_motto) {
        this.course_motto = course_motto;
    }

    public String getRequirement() {
        return requirement;
    }

    /**
     *
     * @param requirement
     */
    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    /**
     *
     * @return
     */
    public String getCourse_requirement() {
        return course_requirement;
    }

    public void setCourse_requirement(String course_requirement) {
        this.course_requirement = course_requirement;
    }

    public String getShare() {
        return share;
    }

    /**
     *
     * @param share
     */
    public void setShare(String share) {
        this.share = share;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getAdmin_featured() {
        return admin_featured;
    }

    public void setAdmin_featured(String admin_featured) {
        this.admin_featured = admin_featured;
    }

    /**
     *
     * @return
     */
    public String getBanner_cb_id() {
        return banner_cb_id;
    }

    public void setBanner_cb_id(String banner_cb_id) {
        this.banner_cb_id = banner_cb_id;
    }

    public String getVideo_cb_id() {
        return video_cb_id;
    }

    public void setVideo_cb_id(String video_cb_id) {
        this.video_cb_id = video_cb_id;
    }

    public String getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(String discount_id) {
        this.discount_id = discount_id;
    }

    public String getClone_status() {
        return clone_status;
    }

    /**
     *
     * @param clone_status
     */
    public void setClone_status(String clone_status) {
        this.clone_status = clone_status;
    }

    /**
     *
     * @return
     */
    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

}
