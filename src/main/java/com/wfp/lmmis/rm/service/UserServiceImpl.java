package com.wfp.lmmis.rm.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.dao.UserDao;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.rm.model.UserPerScheme;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.UserViewForTraining;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     *
     * @param user
     */
    @Override
    public void save(User user) {
        this.userDao.save(user);
    }

    @Override
    public void edit(User user) {
        this.userDao.edit(user);
    }

    @Override
    public void edit(User user, List<UserPerScheme> userPerSchemeRemovables) {
        this.userDao.edit(user, userPerSchemeRemovables);
    }

    @Override
    public void delete(User user, User loggedUser) throws ExceptionWrapper {
        this.userDao.delete(user, loggedUser);
    }

    /**
     *
     * @param login
     * @return
     * @throws ExceptionWrapper
     */
    @Override
    public User getUser(String login) throws ExceptionWrapper {
        return userDao.getUser(login);
    }

    @Override
    public List<User> getUserList() throws ExceptionWrapper {
        return this.userDao.getUserList();
    }

    @Override
    public User getUser(Integer id) {
        return this.userDao.getUser(id);
    }

    @Override
    public void changePassword(Integer userId, String hashedPassword, String salt) {
        this.userDao.changePassword(userId, hashedPassword, salt);
    }

    @Override
    public List<User> getApproverList() {
        return this.userDao.getApproverList();
    }

    @Override
    public boolean checkUserAvailibility(String userID) throws ExceptionWrapper {
        return this.userDao.checkUserAvailibility(userID);
    }

    /**
     *
     * @param userID
     * @param id
     * @return
     * @throws ExceptionWrapper
     */
    @Override
    public boolean checkUserAvailibilityForProfileUpdate(String userID, int id) throws ExceptionWrapper {
        return this.userDao.checkUserAvailibilityForProfileUpdate(userID, id);
    }

    @Override
    public boolean checkMobileNoAvailibility(String mobileNo, Integer userId) throws ExceptionWrapper {
        return this.userDao.checkMobileNoAvailibility(mobileNo, userId);
    }

    @Override
    public boolean checkEmailAddressAvailibility(String email, Integer userId) throws ExceptionWrapper {
        return this.userDao.checkEmailAddressAvailibility(email, userId);
    }

    @Override
    public List<Object> getUserListByParameter(Map parameter, int offset, int numofRecords) {
        return this.userDao.getUserListByParameter(parameter, offset, numofRecords);
    }

    @Override
    public void requestForgotPassword(String email, String contextPath) throws ExceptionWrapper {
        this.userDao.requestForgotPassword(email, contextPath);
    }

    @Override
    public void updateVisitingLog(Integer userId, boolean logged) {
        this.userDao.updateVisitingLog(userId, logged);
    }

    /**
     *
     * @param userId
     * @return
     */
    @Override
    public boolean isLoggedIn(Integer userId) {
        return this.userDao.isLoggedIn(userId);
    }

    @Override
    public void recoverForgotPassword(String password, String salt, String token) throws ExceptionWrapper {
        this.userDao.recoverForgotPassword(password, salt, token);
    }

    /**
     *
     * @param parameter
     * @param offset
     * @param numofRecords
     * @return
     */
    @Override
    public List<Object> getAudittrailListBySearchParameter(Map parameter, int offset, int numofRecords) {
        return this.userDao.getAudittrailListBySearchParameter(parameter, offset, numofRecords); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UserViewForTraining> getUserListForTraining(int courseId, int batchId, int fiscalYearId) {
        return userDao.getUserListForTraining(courseId, batchId, fiscalYearId);
    }

    @Override
    public List<UserViewForTraining> getUserInfoForEnrollment(List<Integer> userIdList) {
        return userDao.getUserInfoForEnrollment(userIdList);
    }

    @Override
    public List<UserViewForTraining> getTrainingUserList(int courseId, int batchId) {
        return userDao.getTrainingUserList(courseId, batchId);
    }

    @Override
    public JsonResult userInfoUpdate(UserDetail userDetail) {

        return userDao.userInfoUpdate(userDetail);
    }

    public JsonResult changePasswordAndBasicInfo(UserDetail userDetail) {
        return userDao.changePasswordAndBasicInfo(userDetail);
    }

    public void resetPassword(Integer userId, String hashedPassword, String salt, int loginedUserId) {

        userDao.resetPassword(userId, hashedPassword, salt, loginedUserId);
    }
}
