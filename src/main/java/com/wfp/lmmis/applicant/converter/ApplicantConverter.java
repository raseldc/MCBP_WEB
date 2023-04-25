/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wfp.lmmis.applicant.converter;

import com.wfp.lmmis.applicant.detail.ApplicantAncInformationDetail;
import com.wfp.lmmis.applicant.detail.ApplicantDetail;
import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.utility.CalendarUtility;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Shamiul Islam at Anunad Solution
 */
public class ApplicantConverter {

    public static ApplicantDetail getDetail(Applicant entity) {
        if (entity == null) {
            return null;
        }
        ApplicantDetail detail = new ApplicantDetail();

        detail.setId(entity.getId());
        detail.setNid(entity.getNid().toString());
        detail.setFullNameInBangla(entity.getFullNameInBangla());
        detail.setFullNameInEnglish(entity.getFullNameInEnglish());
        detail.setConceptionDuration(entity.getConceptionDuration());
        detail.setFatherName(entity.getFatherName());
        detail.setMotherName(entity.getMotherName());
        detail.setSpouseName(entity.getSpouseName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        String dobStr = CalendarUtility.getDateString(entity.getDateOfBirth().getTime(), "yyyy-MM-dd");
        detail.setDateOfBirth(dobStr);

        return detail;
    }
}
