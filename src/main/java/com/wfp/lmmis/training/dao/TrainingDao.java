/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.dao;

import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.model.User;
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
public interface TrainingDao {

    public Training getTraining(Integer id);

    public void save(Training training);

    public void edit(Training training);

    public List<Training> getTrainingList();

    public void delete(Integer id);

    public List<Training> getTrainingReportData(Map parameter);

    /**
     *
     * @param parameter
     * @param offset
     * @param numofRecords
     * @return
     */
    public List<Object> getTrainingListBySearchParameter(Map parameter, int offset, int numofRecords);

    public void saveTrainingAttendee(TrainingAttendee trainingAttendee);

    /**
     *
     * @param id
     */
    public void deleteTrainingAttendee(Integer id);

    public List<TrainingAttendee> getTrainingAttendeeList(Integer id);

    public boolean checkBeneficiaryTrainingDuplicacy(Integer id, Integer trainingTypeId);

    /**
     *
     * @param enrollmentInfo
     * @param loginUser
     */
    public void createUserTrainingAndEnrollment(EnrollmentInfo enrollmentInfo, UserDetail loginUser);

    public List<UserTrainingView> getAllUserTraining(int courseId, int batchId, int fiscalYearId);

    public UserTraining getUserTraining(int courseId, int batchId, int fiscalYearId);

    public List<UserTrainingCourse> getCourseListByUserId(int userId);

    /**
     *
     * @param userId
     * @param courseId
     * @return
     */
    public List<Batch> getBatchListByCourseAndUserId(int userId, int courseId);

    public List<UserViewForTraining> getAllTraingDetailsByUser(int userId, int courseId, int batchId, int fiscalYearId);

    public JsonResult userTrainingAttendanceUpdate(UserTrainingUpdateApi trainingUpdateApi);

    public List<Batch> getCreatedBranchForCourse(int courseId, int fiscalYearId);

    /**
     *
     * @param userTrainAttId
     * @param isGetCertificate
     * @return
     */
    public JsonResult userTrainingAttendanceUpdateOnGetCertificate(int userTrainAttId, int isGetCertificate);

    public JsonResult userTrainingAttendanceUpdateOnZoomMeeting(int userTrainAttId, int isZoomMeeting);

    /**
     *
     * @param userTrainAttId
     * @param remarks
     * @return
     */
    public JsonResult userTrainingAttendanceUpdateOnRemarks(int userTrainAttId, String remarks);
}
