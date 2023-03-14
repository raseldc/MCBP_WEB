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
public class UserTrainingCourse {

    private String title;
    private int course_id;
    private int course_cb_id;
    private String courseName;

    public String getCourseName() {
        return courseName;
    }

    /**
     *
     * @param courseName
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    /**
     *
     * @return
     */
    public int getCourse_cb_id() {
        return course_cb_id;
    }

    public void setCourse_cb_id(int course_cb_id) {
        this.course_cb_id = course_cb_id;
    }

}
