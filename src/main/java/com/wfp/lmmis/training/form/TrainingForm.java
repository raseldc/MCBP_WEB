/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.form;

import com.wfp.lmmis.training.model.*;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.model.BaseModel;
import com.wfp.lmmis.payroll.model.FiscalYear;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Philip
 */
@Getter
@Setter
public class TrainingForm extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private FiscalYear fiscalYear;

    private TrainingType trainingType;

    private Trainer trainer;

    private Integer numberOfPerticipants;

    private Boolean headOffice;

    private Division division;

    private District district;

    private Upazilla upazilla;

    private Union union;

    private String trainingVenue;

    private Float trainingCost;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Calendar startDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Calendar endDate;

    private Integer durationDay;

    private String comment;

    private ApplicantType applicantType;

    private boolean deleted;

    private String fileBase64;

    private String fileExtension;

    private boolean divisionAvailable;

    private boolean districtAvailable;

    private boolean upazilaAvailable;

    private MultipartFile file;
    private String photoData;

    public Training getTrainingModel() {
        
        Training training = new Training();
        training.setId(id);
        training.setFiscalYear(fiscalYear);
        training.setTrainingType(trainingType);
        training.setTrainer(trainer);
        training.setNumberOfPerticipants(numberOfPerticipants);
        training.setHeadOffice(headOffice);
        training.setTrainingVenue(trainingVenue);
        training.setTrainingCost(trainingCost);
        training.setStartDate(startDate);
        training.setEndDate(endDate);
        training.setDurationDay(durationDay);
        training.setComment(comment);
        training.setApplicantType(applicantType);
        training.setDeleted(deleted);
        training.setFileBase64(fileBase64);
        training.setFileExtension(fileExtension);
        training.setDivisionAvailable(divisionAvailable);
        training.setDistrictAvailable(districtAvailable);
        training.setUpazilaAvailable(upazilaAvailable);
        training.setDistrict(district);
        training.setDivision(division);
        training.setUpazilla(upazilla);
        training.setUnion(union);
        return training;
    }
}
