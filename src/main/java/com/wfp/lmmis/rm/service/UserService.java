package com.wfp.lmmis.rm.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.rm.model.UserPerScheme;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.UserViewForTraining;
import java.util.List;
import java.util.Map;

public interface UserService {

    public void save(User user);

    /**
     *
     * @param user
     */
    public void edit(User user);

    public void edit(User user, List<UserPerScheme> userPerSchemeRemovables);

    /**
     *
     * @param user
     * @param loggedUser
     * @throws ExceptionWrapper
     */
    public void delete(User user, User loggedUser) throws ExceptionWrapper;

    public User getUser(String login) throws ExceptionWrapper;

    public User getUser(Integer id);

    public List<User> getUserList() throws ExceptionWrapper;

    public void changePassword(Integer userId, String hashedPassword, String salt);

    public List<User> getApproverList();

    /**
     *
     * @param userID
     * @return
     * @throws ExceptionWrapper
     */
    public boolean checkUserAvailibility(String userID) throws ExceptionWrapper;

    public boolean checkUserAvailibilityForProfileUpdate(String userID, int id) throws ExceptionWrapper;

    public boolean checkMobileNoAvailibility(String mobileNo, Integer userId) throws ExceptionWrapper;

    public boolean checkEmailAddressAvailibility(String email, Integer userId) throws ExceptionWrapper;

    public List<Object> getUserListByParameter(Map parameter, int offset, int numofRecords);

    /**
     *
     * @param email
     * @param contextPath
     * @throws ExceptionWrapper
     */
    public void requestForgotPassword(String email, String contextPath) throws ExceptionWrapper;

    /**
     *
     * @param userId
     * @param logged
     */
    public void updateVisitingLog(Integer userId, boolean logged);

    public boolean isLoggedIn(Integer userId);

    /**
     *
     * @param password
     * @param salt
     * @param token
     * @throws ExceptionWrapper
     */
    public void recoverForgotPassword(String password, String salt, String token) throws ExceptionWrapper;

    public List<Object> getAudittrailListBySearchParameter(Map parameter, int offset, int numofRecords);

    public List<UserViewForTraining> getUserListForTraining(int courseId, int batchId, int fiscalYearId);

    public List<UserViewForTraining> getUserInfoForEnrollment(List<Integer> userIdList);

    public List<UserViewForTraining> getTrainingUserList(int courseId, int batchId);

    public JsonResult userInfoUpdate(UserDetail userDetail);

    public JsonResult changePasswordAndBasicInfo(UserDetail userDetail);

    /**
     *
     * @param userId
     * @param hashedPassword
     * @param salt
     * @param loginedUserId
     */
    public void resetPassword(Integer userId, String hashedPassword, String salt, int loginedUserId);
}
