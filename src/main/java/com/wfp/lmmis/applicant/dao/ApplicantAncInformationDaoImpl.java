package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.applicant.model.ApplicantAncInformation;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicantAncInformationDaoImpl
        implements ApplicantAncInformationDao {

    @Autowired
    SessionFactory sessionFactory;

    public ApplicantAncInformation getAncInformationByApplicantId(int applicantId) {
        com.wfp.lmmis.applicant.model.ApplicantAncInformation applicantAncInformationDB = (com.wfp.lmmis.applicant.model.ApplicantAncInformation) sessionFactory.getCurrentSession().createQuery("SELECT a FROM ApplicantAncInformation a WHERE a.applicant.Id =:applicantId")
                .setParameter("applicantId", applicantId).uniqueResult();
        return applicantAncInformationDB;
    }

}
