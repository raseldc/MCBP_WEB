/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.utility.PaymentInformationView;
import com.wfp.lmmis.utility.PaymnetInformationViewSearchInfo;
import java.util.List;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public interface BeneficiaryPaymentCompleteDao {

    public List<PaymentInformationView> getPaymentCompleteBeneficiaryList(int skip, int take, PaymnetInformationViewSearchInfo paymnetInformationViewSearchInfo);
}
