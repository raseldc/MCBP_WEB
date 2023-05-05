package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.applicant.model.ApplicantAncInformation;

public interface ApplicantAncInformationDao {

    public ApplicantAncInformation getAncInformationByApplicantId(int applicantId);
}
