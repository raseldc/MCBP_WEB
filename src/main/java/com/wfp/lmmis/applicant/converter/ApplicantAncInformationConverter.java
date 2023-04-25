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
        entity.setDob(detail.getDob());
        entity.setPregnancyWeek(detail.getPregnancyWeek());
        entity.setAnc1(detail.getAnc1());
        entity.setAnc2(detail.getAnc2());
        entity.setAnc3(detail.getAnc3());
        entity.setAnc4(detail.getAnc4());
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
        detail.setDob(entity.getDob());
        detail.setPregnancyWeek(entity.getPregnancyWeek());
        detail.setAnc1(entity.getAnc1());
        detail.setAnc2(entity.getAnc2());
        detail.setAnc3(entity.getAnc3());
        detail.setAnc4(entity.getAnc4());
        detail.setModifiedBy(entity.getModifiedBy());
        detail.setModficationDate(entity.getModficationDate());
        detail.setCreatedBy(entity.getCreatedBy());
        detail.setCreationDate(entity.getCreationDate());

        return detail;
    }
}
