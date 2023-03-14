/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.service;

import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.training.controller.Batch;
import com.wfp.lmmis.training.controller.EnrollmentInfo;
import com.wfp.lmmis.training.controller.UserTrainingCourse;
import com.wfp.lmmis.training.controller.UserTrainingUpdateApi;
import com.wfp.lmmis.training.model.Training;
import com.wfp.lmmis.training.model.TrainingAttendee;
import com.wfp.lmmis.training.model.UserTraining;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.UserTrainingView;
import com.wfp.lmmis.utility.UserViewForTraining;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Philip
 */
public interface TrainingService {

    /**
     *
     * @param id
     * @return
     */
    public Training getTraining(Integer id);

    public void save(Training training);

    public void edit(Training training);

    public List<Training> getTrainingList();

    public void delete(Integer id);

    /**
     *
     * @param parameter
     * @return
     */
    public List<Training> getTrainingReportData(Map parameter);

    public List<Object> getTrainingListBySearchParameter(Map parameter, int offset, int numofRecords);

    /**
     *
     * @param trainingAttendee
     */
    public void saveTrainingAttendee(TrainingAttendee trainingAttendee);

    public void deleteTrainingAttendee(Integer id);

    public List<TrainingAttendee> getTrainingAttendeeList(Integer id);

    public boolean checkBeneficiaryTrainingDuplicacy(Integer id, Integer trainingTypeId);

    public void createUserTrainingAndEnrollment(EnrollmentInfo enrollmentInfo, UserDetail loginUser);

    public List<UserTrainingView> getAllUserTraining(int courseId, int batchId, int fiscalYearId);

    public UserTraining getUserTraining(int courseId, int batchId, int fiscalYearId);

    public List<UserTrainingCourse> getCourseListByUserId(int userId);

    public List<Batch> getBatchListByCourseAndUserId(int userId, int courseId);

    /**
     *
     * @param userId
     * @param courseId
     * @param batchId
     * @param fiscalYearId
     * @return
     */
    public List<UserViewForTraining> getAllTraingDetailsByUser(int userId, int courseId, int batchId, int fiscalYearId);

    /**
     *
     * @param trainingUpdateApi
     * @return
     */
    public JsonResult userTrainingAttendanceUpdate(UserTrainingUpdateApi trainingUpdateApi);

    public List<Batch> getCreatedBranchForCourse(int courseId, int fiscalYearId);

    public JsonResult userTrainingAttendanceUpdateOnGetCertificate(int userTrainingAttenId, int getCertificate);

    public JsonResult userTrainingAttendanceUpdateOnZoomMeeting(int userTrainingAttenId, int zoomMeeting);

    public JsonResult userTrainingAttendanceUpdateOnRemarks(int userTrainAttId, String remarks);

}
