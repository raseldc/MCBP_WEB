/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.masterdata.forms.BeneficiaryQuotaForm;
import com.wfp.lmmis.masterdata.model.BenQuota;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface BenQuotaDao
{

    /**
     *
     * @param id
     * @return
     */
    public BenQuota getBeneficiaryQuota(Integer id);

    public void save(BenQuota beneficiaryQuota);

    public void edit(BenQuota beneficiaryQuota);

    public void delete(Integer id);

    public List<BenQuota> getBeneficiaryQuotaList();

    public void saveBeneficiaryQuota(BeneficiaryQuotaForm beneficiaryQuotaForm);
    
    public BenQuota getBenQuotaByUnion(Integer unionId, Integer fiscalYearId, Integer schemeId);
    
    /**
     *
     * @param factoryId
     * @param fiscalYearId
     * @param schemeId
     * @return
     */
    public BenQuota getBenQuotaByFactory(Integer factoryId, Integer fiscalYearId, Integer schemeId);
}
