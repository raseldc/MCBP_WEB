/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.beneficiary.model.BeneficiaryChildInformation;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.utility.JsonResult;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Repository
public class BeneficiaryChildDaoImpl implements BeneficiaryChildDao {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public JsonResult addChildInfo(BeneficiaryChildInformation beneficiaryChildInformation) {
        JsonResult jr = new JsonResult(false, "");

        try {
            final Session currentSession = this.sessionFactory.getCurrentSession();

            currentSession.save(beneficiaryChildInformation);
            currentSession.flush();
            if (beneficiaryChildInformation.getFile() != null) {
                childAttachedSave(beneficiaryChildInformation);
                currentSession.update(beneficiaryChildInformation);
            }
        } catch (Exception e) {
            jr.setIsError(true);
            jr.setErrorMsg(e.getMessage());
            System.out.println("exception = " + e.getMessage());
            e.printStackTrace();
        }
        return jr;
    }

    private void childAttachedSave(BeneficiaryChildInformation beneficiaryChildInformation) {

        try {
            beneficiaryChildInformation.setAttachedFileLocation("");
            MultipartFile attached = beneficiaryChildInformation.getFile();
            if (attached != null && attached.getSize() > 0) {
                byte[] bytes = attached.getBytes();
                // Settings s = mixService.getSettingsByKey("userProfilePicLocation");
                String ralPath = "D:/imlma/beneficiary/child/";

                File dir = new File(ralPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                ralPath = ralPath + beneficiaryChildInformation.getId() + ".jpg";
                File serverFile = new File(ralPath);

                String filePath = serverFile.getPath();

                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                beneficiaryChildInformation.setAttachedFileLocation(ralPath);
                // updateChildInfo(beneficiaryChildInformation);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
            //java.util.logging.//logger.getlogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public JsonResult updateChildInfo(BeneficiaryChildInformation beneficiaryChildInformation) {
        JsonResult jr = new JsonResult(false, "");
        try {
            if (beneficiaryChildInformation.getFile() != null && beneficiaryChildInformation.getFile().getSize() > 0) {
                childAttachedSave(beneficiaryChildInformation);
            }

            final Session currentSession = this.sessionFactory.getCurrentSession();
            currentSession.update(beneficiaryChildInformation);
            currentSession.flush();

        } catch (Exception e) {
            jr.setIsError(true);
            jr.setErrorMsg(e.getMessage());
            System.out.println("exception = " + e.getMessage());
            e.printStackTrace();
        }
        return jr;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public BeneficiaryChildInformation getBeneficiaryChildInformationById(int id) {
        final Session currentSession = this.sessionFactory.getCurrentSession();
        return (BeneficiaryChildInformation) currentSession.createQuery("SELECT a FROM BeneficiaryChildInformation as a WHERE a.id =:id").setParameter("id", id).uniqueResult();
    }
}
