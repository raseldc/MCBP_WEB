/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.payroll.model.PaymentInformationHistory;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.PaymentInformationView;
import com.wfp.lmmis.utility.PaymnetInformationViewSearchInfo;
import java.util.List;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public interface BeneficiaryPaymentDao {

    public List<PaymentInformationView> getPaymentInformation(int skip, int take, PaymnetInformationViewSearchInfo paymnetInformationViewSearchInfo);

    public List<PaymentInformationView> getBeneficiaryPaymentHistoryList(int skip, int take, PaymnetInformationViewSearchInfo paymnetInformationViewSearchInfo);

    public JsonResult updatePaymentInfo(PaymentInformationView informationView, User loginedUser);

    public PaymentInformationHistory getBeneficiaryPaymentHistoryById(int paymentHistoryId);

}
