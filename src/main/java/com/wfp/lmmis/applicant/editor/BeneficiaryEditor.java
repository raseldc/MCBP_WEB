/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.editor;

import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.applicant.service.BeneficiaryService;
import com.wfp.lmmis.utility.CommonUtility;
import java.beans.PropertyEditorSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author user
 */
@Component
public class BeneficiaryEditor extends PropertyEditorSupport
{

    @Autowired
    private BeneficiaryService beneficiaryService;

    // Converts a String to a Beneficiary (when submitting form)
    @Override
    public void setAsText(String text)
    {
        try
        {
            Beneficiary beneficiary = null;
            if (!text.equals(""))
            {
//                beneficiary = this.beneficiaryService.getBeneficiaryByID(text);
                beneficiary = this.beneficiaryService.getBeneficiaryByID(CommonUtility.getNumberInEnglish(text));
            }
            this.setValue(beneficiary);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.setValue(null);
        }

    }

    // Converts a Beneficiary to a String (when displaying form)
    @Override
    public String getAsText()
    {
        Beneficiary i = (Beneficiary) this.getValue();
        if (i != null)
        {
            //System.out.println("i = " + i.getApplicationID());
            //return i.getApplicationID();
            return i.getNid().toString();
        }
        return null;
    }
}
