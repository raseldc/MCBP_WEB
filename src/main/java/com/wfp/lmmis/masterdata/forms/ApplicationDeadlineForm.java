/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.forms;

import com.wfp.lmmis.masterdata.model.ApplicationDeadline;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.payroll.model.FiscalYear;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Philip
 */
public class ApplicationDeadlineForm
{

    private List<ApplicationDeadline> applicationDeadlineList;

    @NotNull
    private Division division;
    @NotNull
    private District district;
    @NotNull
    private Upazilla upazilla;
    @NotNull
    private Scheme scheme;
    @NotNull
    private FiscalYear fiscalYear;

    public Division getDivision()
    {
        return division;
    }

    /**
     *
     * @param division
     */
    public void setDivision(Division division)
    {
        this.division = division;
    }

    public District getDistrict()
    {
        return district;
    }

    /**
     *
     * @param district
     */
    public void setDistrict(District district)
    {
        this.district = district;
    }

    public Scheme getScheme()
    {
        return scheme;
    }

    /**
     *
     * @param scheme
     */
    public void setScheme(Scheme scheme)
    {
        this.scheme = scheme;
    }

    public FiscalYear getFiscalYear()
    {
        return fiscalYear;
    }

    /**
     *
     * @param fiscalYear
     */
    public void setFiscalYear(FiscalYear fiscalYear)
    {
        this.fiscalYear = fiscalYear;
    }

    public List<ApplicationDeadline> getApplicationDeadlineList()
    {
        return applicationDeadlineList;
    }

    public void setApplicationDeadlineList(List<ApplicationDeadline> applicationDeadlineList)
    {
        this.applicationDeadlineList = applicationDeadlineList;
    }

    public Upazilla getUpazilla()
    {
        return upazilla;
    }

    public void setUpazilla(Upazilla upazilla)
    {
        this.upazilla = upazilla;
    }

}
