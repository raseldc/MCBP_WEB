package com.wfp.lmmis.applicant.service;

import com.wfp.lmmis.applicant.model.ApplicantAncInformation;

public interface ApplicantAncInformationService {

    ApplicantAncInformation getAncInformationByApplicantId(int applicantId);
}
