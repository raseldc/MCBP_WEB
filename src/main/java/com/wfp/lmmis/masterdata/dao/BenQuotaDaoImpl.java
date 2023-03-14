/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.masterdata.forms.BeneficiaryQuotaForm;
import com.wfp.lmmis.masterdata.model.BenQuota;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Philip
 */
@Repository
public class BenQuotaDaoImpl implements BenQuotaDao
{

    //private static final logger logger = //logger.getlogger(BenQuotaDaoImpl.class);
    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public BenQuota getBeneficiaryQuota(Integer id)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param beneficiaryQuota
     */
    @Override
    public void save(BenQuota beneficiaryQuota)
    {
        try
        {
            if (null != beneficiaryQuota.getApplicantType())
            {
                switch (beneficiaryQuota.getApplicantType())
                {
                    case REGULAR:
                    {
                        String selectQuery = "select o from BenQuota o where o.union.id=:unionId"
                                + " and o.fiscalYear.id=:fiscalYearId and o.scheme.id=:schemeId";
                        BenQuota benQuota = (BenQuota) this.getCurrentSession().createQuery(selectQuery).setParameter("unionId", beneficiaryQuota.getUnion().getId()).
                                setParameter("fiscalYearId", beneficiaryQuota.getFiscalYear().getId()).setParameter("schemeId", beneficiaryQuota.getScheme().getId()).uniqueResult();
                        if (benQuota != null)
                        {
                            delete(benQuota);
                        }
                        break;
                    }
                    case BGMEA:
                    case BKMEA:
                    {
                        String selectQuery = "select o from BenQuota o where o.factory.id=:factoryId"
                                + " and o.fiscalYear.id=:fiscalYearId and o.scheme.id=:schemeId";
                        BenQuota benQuota = (BenQuota) this.getCurrentSession().createQuery(selectQuery).setParameter("factoryId", beneficiaryQuota.getFactory().getId()).
                                setParameter("fiscalYearId", beneficiaryQuota.getFiscalYear().getId()).setParameter("schemeId", beneficiaryQuota.getScheme().getId()).uniqueResult();
                        if (benQuota != null)
                        {
                            delete(benQuota);
                        }
                        break;
                    }
                }
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        this.getCurrentSession().save(beneficiaryQuota);
    }

    @Override
    public void edit(BenQuota beneficiaryQuota)
    {
        try
        {
            this.getCurrentSession().merge(beneficiaryQuota);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param benQuota
     */
    public void delete(BenQuota benQuota)
    {
        this.getCurrentSession().delete(benQuota);
    }

    @Override
    public List<BenQuota> getBeneficiaryQuotaList()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveBeneficiaryQuota(BeneficiaryQuotaForm beneficiaryQuotaForm)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BenQuota getBenQuotaByUnion(Integer unionId, Integer fiscalYearId, Integer schemeId)
    {
        String query = "Select o from BenQuota o join o.union u join o.fiscalYear fy join o.scheme s where u.id=:unionId"
                + " and fy.id=:fiscalYearId and s.id=:schemeId";
        BenQuota quota = null;
        try
        {
            quota = (BenQuota) this.getCurrentSession().createQuery(query).setParameter("unionId", unionId).setParameter("fiscalYearId", fiscalYearId).setParameter("schemeId", schemeId).uniqueResult();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return quota;
    }

    @Override
    public BenQuota getBenQuotaByFactory(Integer factoryId, Integer fiscalYearId, Integer schemeId)
    {
        String query = "Select o from BenQuota o join o.factory u join o.fiscalYear fy join o.scheme s where u.id=:factoryId"
                + " and fy.id=:fiscalYearId and s.id=:schemeId";
        BenQuota quota = null;
        try
        {
            quota = (BenQuota) this.getCurrentSession().createQuery(query).setParameter("factoryId", factoryId).setParameter("fiscalYearId", fiscalYearId).setParameter("schemeId", schemeId).uniqueResult();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return quota;
    }

    @Override
    public void delete(Integer id)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
