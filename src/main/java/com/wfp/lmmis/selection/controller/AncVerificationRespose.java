/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wfp.lmmis.selection.controller;

import com.wfp.lmmis.applicant.detail.ApplicantAncInformationDetail;
import com.wfp.lmmis.applicant.detail.ApplicantDetail;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Shamiul Islam at Anunad Solution
 */
@Getter
@Setter
public class AncVerificationRespose {

    private ApplicantAncInformationDetail ancInformationDetail;
    private ApplicantDetail applicantDetail;
    private int status;
    private String msg;
    private int userId;
}

