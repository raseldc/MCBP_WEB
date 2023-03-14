package com.wfp.lmmis.grievance.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.grievance.dao.GrievanceDao;
import com.wfp.lmmis.grievance.model.Grievance;
import com.wfp.lmmis.report.data.GrievanceReportData;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GrievanceServiceImpl implements GrievanceService
{

    @Autowired
    private GrievanceDao grievanceDao;

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Grievance getGrievance(Integer id)
    {
        return grievanceDao.getGrievance(id);
    }

    /**
     *
     * @param grievance
     */
    @Override
    public void save(Grievance grievance)
    {
        this.grievanceDao.save(grievance);
    }

    @Override
    public void edit(Grievance grievance)
    {
        this.grievanceDao.edit(grievance);
    }

    @Override
    public List<Grievance> getGrievanceList()
    {
        return this.grievanceDao.getGrievanceList();
    }
    
    @Override
    public void delete(Grievance grievance) throws ExceptionWrapper
    {
        this.grievanceDao.delete(grievance);
    }

//    @Override
//    public List<GrievanceReportData> getGrievanceReportData()
//    {
//        List<Grievance> grievances = getGrievanceList();
//        List<GrievanceReportData> grievanceReports = new ArrayList<>();
//        grievances.stream().map((Grievance grievance) ->
//        {
//            GrievanceReportData grievanceReport = new GrievanceReportData();
//            String benName = grievance.getBeneficiary().getMiddleNameInEnglish() != null ? grievance.getBeneficiary().getFirstNameInEnglish() + " " + grievance.getBeneficiary().getMiddleNameInEnglish() + " " + grievance.getBeneficiary().getLastNameInEnglish()
//                    : grievance.getBeneficiary().getFirstNameInEnglish() + " " + grievance.getBeneficiary().getLastNameInEnglish();
//            grievanceReport.setBenName(benName);
//            grievanceReport.setGrievanceType(grievance.getGrievanceType().getNameInEnglish());
//            grievanceReport.setDescription(grievance.getDescription());
//            grievanceReport.setComment(grievance.getComment());
//            return grievanceReport;
//        }).forEachOrdered((grievanceReport) ->
//        {
//            grievanceReports.add(grievanceReport);
//        });
//        return grievanceReports;
//    }
    
    @Override
    public List<GrievanceReportData> getGrievanceReportData(Map parameter)
    {
        return this.grievanceDao.getGrievanceReportData(parameter);
    }
    
    @Override
    public List<Object> getGrievanceListBySearchParameter(Map parameter, int beginIndex, int pageSize)
    {
        return this.grievanceDao.getGrievanceListBySearchParameter(parameter, beginIndex, pageSize);
    }

}
