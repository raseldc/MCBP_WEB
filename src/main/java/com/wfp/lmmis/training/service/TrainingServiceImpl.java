package com.wfp.lmmis.training.service;

import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.training.controller.Batch;
import com.wfp.lmmis.training.controller.EnrollmentInfo;
import com.wfp.lmmis.training.controller.UserTrainingCourse;
import com.wfp.lmmis.training.controller.UserTrainingUpdateApi;
import com.wfp.lmmis.training.dao.TrainingDao;
import com.wfp.lmmis.training.model.Training;
import com.wfp.lmmis.training.model.TrainingAttendee;
import com.wfp.lmmis.training.model.UserTraining;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.UserTrainingView;
import com.wfp.lmmis.utility.UserViewForTraining;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private TrainingDao trainingDao;

    @Override
    public Training getTraining(Integer id) {
        return trainingDao.getTraining(id);
    }

    @Override
    public void save(Training training) {
        this.trainingDao.save(training);
    }

    @Override
    public void edit(Training training) {
        this.trainingDao.edit(training);
    }

    @Override
    public List<Training> getTrainingList() {
        return this.trainingDao.getTrainingList();
    }

    @Override
    public void delete(Integer id) {
        this.trainingDao.delete(id);
    }

    @Override
    public List<Training> getTrainingReportData(Map parameter) {
        return this.trainingDao.getTrainingReportData(parameter);
    }

    @Override
    public List<Object> getTrainingListBySearchParameter(Map parameter, int offset, int numofRecords) {
        return this.trainingDao.getTrainingListBySearchParameter(parameter, offset, numofRecords);
    }

    @Override
    public void saveTrainingAttendee(TrainingAttendee trainingAttendee) {
        this.trainingDao.saveTrainingAttendee(trainingAttendee);
    }

    @Override
    public void deleteTrainingAttendee(Integer id) {
        this.trainingDao.deleteTrainingAttendee(id);
    }

    @Override
    public List<TrainingAttendee> getTrainingAttendeeList(Integer id) {
        return this.trainingDao.getTrainingAttendeeList(id);
    }

    @Override
    public boolean checkBeneficiaryTrainingDuplicacy(Integer id, Integer trainingTypeId) {
        return this.trainingDao.checkBeneficiaryTrainingDuplicacy(id, trainingTypeId);
    }

    @Override
    public void createUserTrainingAndEnrollment(EnrollmentInfo enrollmentInfo, UserDetail loginUser) {
        this.trainingDao.createUserTrainingAndEnrollment(enrollmentInfo, loginUser);
    }

    @Override
    public List<UserTrainingView> getAllUserTraining(int courseId, int batchId, int fiscalYearId) {
        return this.trainingDao.getAllUserTraining(courseId, batchId, fiscalYearId);
    }

    @Override
    public UserTraining getUserTraining(int courseId, int batchId, int fiscalYearId) {
        return this.trainingDao.getUserTraining(courseId, batchId, fiscalYearId);
    }

    /**
     *
     * @param userId
     * @return
     */
    @Override
    public List<UserTrainingCourse> getCourseListByUserId(int userId) {
        return trainingDao.getCourseListByUserId(userId);
    }

    @Override
    public List<Batch> getBatchListByCourseAndUserId(int userId, int courseId) {
        return trainingDao.getBatchListByCourseAndUserId(userId, courseId);
    }

    @Override
    public List<UserViewForTraining> getAllTraingDetailsByUser(int userId, int courseId, int batchId, int fiscalYearId) {
        return trainingDao.getAllTraingDetailsByUser(userId, courseId, batchId, fiscalYearId);
    }

    @Override
    public JsonResult userTrainingAttendanceUpdate(UserTrainingUpdateApi trainingUpdateApi) {
        return trainingDao.userTrainingAttendanceUpdate(trainingUpdateApi);
    }

    /**
     *
     * @param courseId
     * @param fiscalYearId
     * @return
     */
    @Override
    public List<Batch> getCreatedBranchForCourse(int courseId, int fiscalYearId) {
        return trainingDao.getCreatedBranchForCourse(courseId, fiscalYearId);
    }

    @Override
    public JsonResult userTrainingAttendanceUpdateOnGetCertificate(int userTrainingAttenId, int getCertificate) {
        return trainingDao.userTrainingAttendanceUpdateOnGetCertificate(userTrainingAttenId, getCertificate);
    }

    /**
     *
     * @param userTrainingAttenId
     * @param isZoomMeeting
     * @return
     */
    @Override
    public JsonResult userTrainingAttendanceUpdateOnZoomMeeting(int userTrainingAttenId, int isZoomMeeting) {
        return trainingDao.userTrainingAttendanceUpdateOnZoomMeeting(userTrainingAttenId, isZoomMeeting);
    }

    @Override
    public JsonResult userTrainingAttendanceUpdateOnRemarks(int userTrainAttId, String remarks) {
        return trainingDao.userTrainingAttendanceUpdateOnRemarks(userTrainAttId, remarks);
    }
}
