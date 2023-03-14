/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.masterdata.dao.BenQuotaDao;
import com.wfp.lmmis.masterdata.forms.BeneficiaryQuotaForm;
import com.wfp.lmmis.masterdata.model.BenQuota;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.CommonUtility;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Philip
 */
@Service
@Transactional
public class BenQuotaServiceImpl implements BenQuotaService
{

    @Autowired
    private BenQuotaDao benQuotaDao;

    @Override
    public BenQuota getBeneficiaryQuota(Integer id)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void save(BenQuota beneficiaryQuota)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(BenQuota beneficiaryQuota)
    {
        this.benQuotaDao.edit(beneficiaryQuota);
    }

    @Override
    public void delete(Integer id)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BenQuota> getBeneficiaryQuotaList()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveBeneficiaryQuota(BeneficiaryQuotaForm beneficiaryQuotaForm, HttpSession session)
    {
        List<BenQuota> list = beneficiaryQuotaForm.getBenQuotaList();
        for (BenQuota beneficiaryQuota : list)
        {
            beneficiaryQuota.setScheme(beneficiaryQuotaForm.getScheme());
            beneficiaryQuota.setFiscalYear(beneficiaryQuotaForm.getFiscalYear());
            if(beneficiaryQuota.getApplicantType()!=ApplicantType.REGULAR)
            {
                beneficiaryQuota.setUpazila(null);
            }
            else
            {
                beneficiaryQuota.setUpazila(beneficiaryQuotaForm.getUpazila());
            }
            beneficiaryQuota.setApplicantType(beneficiaryQuotaForm.getApplicantType());
            beneficiaryQuota.setFactory(beneficiaryQuota.getFactory());
            // union is set at beneficiary quota
            beneficiaryQuota.setUnion(beneficiaryQuota.getUnion());
            beneficiaryQuota.setQuota(Integer.parseInt(CommonUtility.getNumberInEnglish(beneficiaryQuota.getQuota() + "")));
            beneficiaryQuota.setCreatedBy((User) session.getAttribute("user"));
            beneficiaryQuota.setCreationDate(Calendar.getInstance());
            this.benQuotaDao.save(beneficiaryQuota);
        }
    }

    /**
     *
     * @param unionId
     * @param fiscalYearId
     * @param schemeId
     * @return
     */
    @Override
    public BenQuota getBenQuotaByUnion(Integer unionId, Integer fiscalYearId, Integer schemeId)
    {
        return this.benQuotaDao.getBenQuotaByUnion(unionId, fiscalYearId, schemeId);
    }

    @Override
    public BenQuota getBenQuotaByFactory(Integer factoryId, Integer fiscalYearId, Integer schemeId)
    {
        return this.benQuotaDao.getBenQuotaByFactory(factoryId, fiscalYearId, schemeId);
    }
}
