package com.wfp.lmmis.applicant.converter;

import com.wfp.lmmis.applicant.detail.ApplicantAncInformationDetail;
import com.wfp.lmmis.applicant.model.ApplicantAncInformation;

public class ApplicantAncInformationConverter {

    public static ApplicantAncInformation getEntity(ApplicantAncInformationDetail detail) {
        if (detail == null) {
            return null;
        }
        ApplicantAncInformation entity = new ApplicantAncInformation();
        entity.setId(detail.getId());

        entity.setNid(detail.getNid());
        entity.setFatherName(detail.getFatherName());
        entity.setMotherName(detail.getMotherName());
        entity.setHusbandName(detail.getHusbandName());
        entity.setName(detail.getName());
        entity.setDob(detail.getDOB());
        entity.setPregnancyWeek(detail.getPregnancy_week());
        entity.setAnc1(detail.getANC1());
        entity.setAnc2(detail.getANC2());
        entity.setAnc3(detail.getANC3());
        entity.setAnc4(detail.getANC4());
        entity.setModifiedBy(detail.getModifiedBy());
        entity.setModficationDate(detail.getModficationDate());
        entity.setCreatedBy(detail.getCreatedBy());
        entity.setCreationDate(detail.getCreationDate());

        return entity;
    }

    public static ApplicantAncInformationDetail getDetail(ApplicantAncInformation entity) {
        if (entity == null) {
            return null;
        }
        ApplicantAncInformationDetail detail = new ApplicantAncInformationDetail();
        detail.setId(entity.getId());

        detail.setNid(entity.getNid());
        detail.setFatherName(entity.getFatherName());
        detail.setMotherName(entity.getMotherName());
        detail.setHusbandName(entity.getHusbandName());
        detail.setName(entity.getName());
        detail.setDOB(entity.getDob());
        detail.setPregnancy_week(entity.getPregnancyWeek());
        detail.setANC1(entity.getAnc1());
        detail.setANC2(entity.getAnc2());
        detail.setANC3(entity.getAnc3());
        detail.setANC4(entity.getAnc4());
        detail.setModifiedBy(entity.getModifiedBy());
        detail.setModficationDate(entity.getModficationDate());
        detail.setCreatedBy(entity.getCreatedBy());
        detail.setCreationDate(entity.getCreationDate());

        return detail;
    }
}
