/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.forms.BeneficiaryQuotaForm;
import com.wfp.lmmis.masterdata.model.BenQuota;
import java.util.List;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Philip
 */
public interface BenQuotaService
{

    public BenQuota getBeneficiaryQuota(Integer id);

    public void save(BenQuota beneficiaryQuota);

    public void edit(BenQuota beneficiaryQuota);

    public void delete(Integer id);

    public List<BenQuota> getBeneficiaryQuotaList();
    
    public void saveBeneficiaryQuota(BeneficiaryQuotaForm beneficiaryQuotaForm, HttpSession session);
    
    public BenQuota getBenQuotaByUnion(Integer unionId, Integer fiscalYearId, Integer schemeId);
    
    public BenQuota getBenQuotaByFactory(Integer factoryId, Integer fiscalYearId, Integer schemeId);
}
