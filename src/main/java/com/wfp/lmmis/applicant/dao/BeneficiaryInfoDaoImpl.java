/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.utility.JsonResult;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Repository
public class BeneficiaryInfoDaoImpl implements BeneficiaryInfoDao {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public JsonResult beneficiaryInfoEdit(Beneficiary beneficiary) {
        JsonResult jr = new JsonResult(false, "");

        if (beneficiary.getMobileNo().toString().length() != 10) {
            jr.setErrorMsg("Mobile Number Length is not correct");
            jr.setIsError(true);
            return jr;
        }
        String updateQuery = String.format("UPDATE beneficiary SET beneficiary.mobile_number = %d,spouse_name = '%s' WHERE beneficiary.Id = %d",
                beneficiary.getMobileNo(), beneficiary.getSpouseName(), beneficiary.getId());

        try {
            sessionFactory.getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
        } catch (Exception ex) {
            jr.setErrorMsg("Exception occured");
            jr.setIsError(true);

            ex.printStackTrace();
        }
        return jr;
    }
}
