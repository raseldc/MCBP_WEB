package com.wfp.lmmis.rm.dao;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.rm.model.UserPerScheme;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.UserViewForTraining;
import java.util.List;
import java.util.Map;

public interface UserDao {

    public void save(User user);

    public void edit(User user);

    /**
     *
     * @param user
     * @param userPerSchemeRemovables
     */
    public void edit(User user, List<UserPerScheme> userPerSchemeRemovables);

    public void delete(User user, User loggedUser) throws ExceptionWrapper;

    /**
     *
     * @param login
     * @return
     * @throws ExceptionWrapper
     */
    public User getUser(String login) throws ExceptionWrapper;

    public List<User> getUserList() throws ExceptionWrapper;

    public User getUser(Integer id);

    public void changePassword(Integer userId, String hashedPassword, String salt);

    public List<User> getApproverList();

    public boolean checkUserAvailibility(String userID) throws ExceptionWrapper;

    /**
     *
     * @param userID
     * @param id
     * @return
     * @throws ExceptionWrapper
     */
    public boolean checkUserAvailibilityForProfileUpdate(String userID, int id) throws ExceptionWrapper;

    public boolean checkMobileNoAvailibility(String mobileNo, Integer userId) throws ExceptionWrapper;

    /**
     *
     * @param email
     * @param userId
     * @return
     * @throws ExceptionWrapper
     */
    public boolean checkEmailAddressAvailibility(String email, Integer userId) throws ExceptionWrapper;

    public List<Object> getUserListByParameter(Map parameter, int offset, int numofRecords);

    public void requestForgotPassword(String email, String contextPath) throws ExceptionWrapper;

    public void updateVisitingLog(Integer userId, boolean logged);

    public boolean isLoggedIn(Integer userId);

    public void recoverForgotPassword(String password, String salt, String token) throws ExceptionWrapper;

    public List<Object> getAudittrailListBySearchParameter(Map parameter, int offset, int numofRecords);

    /**
     *
     * @param courseId
     * @param batchId
     * @param fiscalYearId
     * @return
     */
    public List<UserViewForTraining> getUserListForTraining(int courseId, int batchId, int fiscalYearId);

    /**
     *
     * @param userIdList
     * @return
     */
    public List<UserViewForTraining> getUserInfoForEnrollment(List<Integer> userIdList);

    public List<UserViewForTraining> getTrainingUserList(int courseId, int batchId);

    /**
     *
     * @param userDetail
     * @return
     */
    public JsonResult userInfoUpdate(UserDetail userDetail);

    public JsonResult changePasswordAndBasicInfo(UserDetail userDetail);

     public void resetPassword(Integer userId, String hashedPassword, String salt, int loginedUserId);
}
