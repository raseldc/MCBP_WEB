package com.wfp.lmmis.applicant.service;

import com.wfp.lmmis.applicant.dao.ApplicantAncInformationDao;
import com.wfp.lmmis.applicant.model.ApplicantAncInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplicantAncInformationServiceImpl
        implements ApplicantAncInformationService {

    @Autowired
    private ApplicantAncInformationDao applicantAncInformationDao;

    public ApplicantAncInformation getAncInformationByApplicantId(int applicantId) {
        return applicantAncInformationDao.getAncInformationByApplicantId(applicantId);
    }
}
