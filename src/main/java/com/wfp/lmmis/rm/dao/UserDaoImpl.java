package com.wfp.lmmis.rm.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.model.ForgotPasswordRequest;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.rm.model.UserPerScheme;
import com.wfp.lmmis.rm.model.VisitingLog;
import com.wfp.lmmis.utility.CalendarUtility;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.Localizer;
import com.wfp.lmmis.utility.UserViewForTraining;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import java.util.stream.Collectors;
import javax.mail.internet.MimeMessage;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SessionFactory sessionFactory;

    Localizer localizer = Localizer.getBrowserLocalizer();

    private Session openSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(User user) {
        try {
            this.sessionFactory.getCurrentSession().save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void edit(User user) {
        try {
            System.out.println("user.get = " + user.getUserPerScheme().getScheme());
            this.sessionFactory.getCurrentSession().update(user);
        } catch (Exception e) {
            try {
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));

                System.out.println(errors.toString());
                throw new ExceptionWrapper(e.getMessage());
            } catch (ExceptionWrapper ex) {
                //logger.getlogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void edit(User user, List<UserPerScheme> userPerSchemeRemovables) {
        this.sessionFactory.getCurrentSession().merge(user);
        for (UserPerScheme userPerScheme : userPerSchemeRemovables) {
            if (userPerScheme.getId() != null) {
                deleteUserPerScheme(userPerScheme.getId());
            }
        }
    }

    private void deleteUserPerScheme(Integer userPerSchemeId) {
        System.out.println("in delete");
        UserPerScheme userPerScheme = (UserPerScheme) sessionFactory.getCurrentSession().load(UserPerScheme.class, userPerSchemeId);
        if (userPerScheme != null) {
            this.sessionFactory.getCurrentSession().delete(userPerScheme);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public User getUser(String login) throws ExceptionWrapper {
        try {
            System.out.println("login = " + login);
            User user = (User) openSession().createQuery("from User u where u.userID = :login").setParameter("login", login).uniqueResult();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public List<User> getUserList() throws ExceptionWrapper {
        try {
            @SuppressWarnings("unchecked")
            List<User> list = sessionFactory.getCurrentSession().createQuery("from User where deleted=0").list();
            return list;
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            System.out.println(errors.toString());
            throw new ExceptionWrapper(e.getMessage() + "============" + e.getCause() + "=========" + errors.toString());
        }
    }

    @Override
    public List<UserViewForTraining> getUserListForTraining(int courseId, int batchId, int fiscalYearId) {
        try {

            String execql = "SELECT user.id id, \n"
                    + "user.email email,\n"
                    + "user.full_name_bn fullNameBn,\n"
                    + "user.full_name_en fullNameEn,\n"
                    + "user.user_id userId,\n"
                    + "user.mobile_no mobileNo,\n"
                    + "user_training.id userTrainingId,\n"
                    + "user_training.course_name courseName,\n"
                    + "user_training.batch_name batchName,\n"
                    + "user_training.course_id courseId,\n"
                    + "user_training.batch_id batchId\n"
                    + "FROM user\n"
                    + "LEFT JOIN user_training_attendance ON user_training_attendance.user_id = user.id\n"
                    + "	AND user_training_attendance.user_training_id = \n"
                    + "	(SELECT a.id  FROM user_training a WHERE a.course_id = " + courseId + " AND a.batch_id = " + batchId + ""
                    + " and a.fiscal_year_id = " + fiscalYearId + " )\n"
                    + "LEFT JOIN user_training ON user_training.id = user_training_attendance.user_training_id \n"
                    + "WHERE user.active = 1";

            List<UserViewForTraining> userViewFortrainings = sessionFactory.getCurrentSession().createSQLQuery(execql)
                    .addScalar("id", IntegerType.INSTANCE)
                    .addScalar("email", StringType.INSTANCE)
                    .addScalar("fullNameBn", StringType.INSTANCE)
                    .addScalar("fullNameEn", StringType.INSTANCE)
                    .addScalar("mobileNo", StringType.INSTANCE)
                    .addScalar("userTrainingId", IntegerType.INSTANCE)
                    .addScalar("courseName", StringType.INSTANCE)
                    .addScalar("batchName", StringType.INSTANCE)
                    .addScalar("courseId", IntegerType.INSTANCE)
                    .addScalar("batchId", IntegerType.INSTANCE)
                    .addScalar("userID", StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(UserViewForTraining.class)).list();

            return userViewFortrainings;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<UserViewForTraining> getTrainingUserList(int courseId, int batchId) {
        try {
            String execql = "SELECT user.id id\n"
                    + "	,user.full_name_en fullNameEn\n"
                    + "	,user.full_name_bn fullNameBn\n"
                    + "	,user.email email\n"
                    + "	,user.mobile_no mobileNo\n"
                    + "	,user_training.course_name courseName\n"
                    + "	,user_training.batch_name batchName\n"
                    + ",user_training_attendance.id userTrainingAttendanceId"
                    + "	,COALESCE(user_training_attendance.journyStatus,0) status\n"
                    + "	,COALESCE(user_training_attendance.complepte,0) complete\n"
                    + "	,COALESCE(user_training_attendance.enrollComplete,0) enrollComplete\n"
                    + "	,COALESCE(user_training_attendance.remarks, '') remarks\n"
                    + "	,COALESCE(user_training_attendance.atleastOneTimeLogin,0) atleastOneTimeLogin\n"
                    + "	,COALESCE(user_training_attendance.lastActivieDate,null) lastActiveDate\n"
                    + "	,COALESCE(user_training_attendance.lastActivieDate,null) lastActiveDate\n"
                    + "	,COALESCE(user_training_attendance.marks,0) marks\n"
                    + "	,COALESCE(user_training_attendance.certificate_link,\"\") certificateLink\n"
                    + "	,COALESCE(user_training_attendance.getCertificate,0) getCertificate\n"
                    + "	,COALESCE(user_training_attendance.isZoomMeeting,0) isZoomMeeting\n"
                    + "FROM user_training_attendance\n"
                    + "LEFT JOIN user_training ON user_training.id = user_training_attendance.user_training_id\n"
                    + "LEFT JOIN user ON user.id = user_training_attendance.user_id\n"
                    + "WHERE user_training.course_id = " + courseId + "\n"
                    + "	AND user_training.batch_id = " + batchId;
            List<UserViewForTraining> userViewFortrainings = sessionFactory.getCurrentSession().createSQLQuery(execql)
                    .addScalar("id", IntegerType.INSTANCE)
                    .addScalar("email", StringType.INSTANCE)
                    .addScalar("fullNameBn", StringType.INSTANCE)
                    .addScalar("fullNameEn", StringType.INSTANCE)
                    .addScalar("mobileNo", StringType.INSTANCE)
                    .addScalar("courseName", StringType.INSTANCE)
                    .addScalar("batchName", StringType.INSTANCE)
                    .addScalar("complete", IntegerType.INSTANCE)
                    .addScalar("enrollComplete", IntegerType.INSTANCE)
                    .addScalar("status", IntegerType.INSTANCE)
                    .addScalar("remarks", StringType.INSTANCE)
                    .addScalar("atleastOneTimeLogin", IntegerType.INSTANCE)
                    .addScalar("lastActiveDate", DateType.INSTANCE)
                    .addScalar("marks", DoubleType.INSTANCE)
                    .addScalar("certificateLink", StringType.INSTANCE)
                    .addScalar("getCertificate", IntegerType.INSTANCE)
                    .addScalar("isZoomMeeting", IntegerType.INSTANCE)
                    .addScalar("userTrainingAttendanceId", IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(UserViewForTraining.class)).list();

            return userViewFortrainings;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<UserViewForTraining> getUserInfoForEnrollment(List<Integer> userIdList) {
        String userId = userIdList.stream().map(i -> i.toString()).collect(Collectors.joining(", "));

        String query = "SELECT user.id id, \n"
                + "COALESCE(user.email ,'')email,\n"
                + "user.full_name_en name\n"
                //      + "user.full_name_bn fullNameBn,\n"
                //                + "user.mobile_no phone\n"
                + "FROM user\n"
                + "WHERE user.id IN (" + userId + ");";
        List<UserViewForTraining> userViewFortrainings = sessionFactory.getCurrentSession().createSQLQuery(query)
                .addScalar("id", IntegerType.INSTANCE)
                .addScalar("email", StringType.INSTANCE)
                .addScalar("name", StringType.INSTANCE)
                //   .addScalar("fullNameBn", StringType.INSTANCE)
                //                .addScalar("phone", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(UserViewForTraining.class)).list();

        return userViewFortrainings;
    }

    @Override
    public User getUser(Integer id) {
        try {
            return (User) this.openSession().get(User.class, id);
        } catch (Exception e) {
            System.out.println("exc = " + e.getMessage());
        }
        return null;
    }

    @Override
    public void changePassword(Integer userId, String hashedPassword, String salt) {
        try {
//        User dbUser = getUser(userId);
            String sql = "UPDATE user SET user.salt =:salt, user.password=:password, status=0 where user.id=:id";
            sessionFactory.getCurrentSession().createSQLQuery(sql)
                    .setParameter("id", userId)
                    .setParameter("salt", salt)
                    .setParameter("password", hashedPassword)
                    .executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void resetPassword(Integer userId, String hashedPassword, String salt, int loginedUserId) {
        try {

            String sql = "UPDATE user SET user.salt =:salt, user.password=:password, status=2,modification_date = SYSDATE(),modified_by =:loginedUserId  where user.id=:id ";
            sessionFactory.getCurrentSession().createSQLQuery(sql)
                    .setParameter("id", userId)
                    .setParameter("salt", salt)
                    .setParameter("password", hashedPassword)
                    .setParameter("loginedUserId", loginedUserId)
                    .executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<User> getApproverList() {
        @SuppressWarnings("unchecked")
        List<User> list = sessionFactory.getCurrentSession().createQuery("from User").list();
        return list;
    }

    @Override
    public boolean checkUserAvailibility(String userID) throws ExceptionWrapper {
        try {
            User user = (User) openSession().createQuery("from User u where u.userID = :userID").setParameter("userID", userID).uniqueResult();
            return user == null;
        } catch (Exception e) {
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public boolean checkUserAvailibilityForProfileUpdate(String userID, int id) throws ExceptionWrapper {
        try {
            User user = (User) openSession().createQuery("from User u where u.userID = :userID and u.id !=:id")
                    .setParameter("id", id)
                    .setParameter("userID", userID).uniqueResult();
            return user == null;
        } catch (Exception e) {
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public boolean checkMobileNoAvailibility(String mobileNo, Integer userId) throws ExceptionWrapper {
        System.out.println("mobileNo = " + CommonUtility.getNumberInEnglish(mobileNo));
        String querySt;
        if (userId == null) {
            querySt = String.format("from User o where o.mobileNo ='%s'", CommonUtility.getNumberInEnglish(mobileNo));
        } else {
            querySt = String.format("from User o where o.id!=%s and o.mobileNo ='%s'", userId, CommonUtility.getNumberInEnglish(mobileNo));
        }
        try {
            User user = (User) openSession().createQuery(querySt).uniqueResult();
            return user == null;
        } catch (Exception e) {
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public boolean checkEmailAddressAvailibility(String email, Integer userId) throws ExceptionWrapper {
        String querySt;
        if (userId == null) {
            querySt = String.format("from User o where o.email ='%s'", email);
        } else {
            querySt = String.format("from User o where o.id!=%s and o.email ='%s'", userId, email);
        }
        try {
            User user = (User) openSession().createQuery(querySt).uniqueResult();
            return user == null;
        } catch (Exception e) {
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public void delete(User user, User loggedUser) throws ExceptionWrapper {
        try {
            Query q = this.openSession().createQuery("update User set deleted=1 where id=" + user.getId());
            q.executeUpdate();
            Calendar cal = Calendar.getInstance();
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.User, user.getId(), loggedUser, cal, user.getUserID(), "deleted");
            this.openSession().save(changeLog);
        } catch (Exception e) {
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public List<Object> getUserListByParameter(Map parameter, int offset, int numofRecords) {
        try {
            String userID = (String) parameter.get("userID");
            int userType = (int) parameter.get("userType");
            int userStatus = (int) parameter.get("userStatus");
            BigInteger scheme = null;
            if (parameter.get("schemeId") != null) {
                scheme = new BigInteger((String) parameter.get("schemeId"));
            }
            Integer divisionId = parameter.get("divisionId") != null ? (Integer) parameter.get("divisionId") : null;
            Integer districtId = parameter.get("districtId") != null ? (Integer) parameter.get("districtId") : null;
            Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;

            String mainQuerySt = "select distinct(o.user) from UserPerScheme o where 0=0 ";
            String countQuerySt = "select count(distinct o.user.id) from UserPerScheme o where 0=0 ";

            String querySt = "";

            if (userID != null) {
                querySt += " AND o.user.userID like '%" + userID + "%'";
            }
            if (userType != -1) {
                querySt += " and o.userType = " + userType;
            }
            if (scheme != null) {
                querySt += " and o.scheme = " + scheme;
            }
            System.out.println("userStatus = " + userStatus);
            System.out.println("userType = " + userType);
            if (userStatus != -1) {
                userStatus = userStatus == 0 ? 1 : 0;
                querySt += " and o.user.active = " + userStatus;
            }
            if (divisionId != null && divisionId != 0) {
                querySt += " AND o.division.id = " + divisionId;
            }
            if (districtId != null && districtId != 0) {
                querySt += " AND o.district.id = " + districtId;
            }
            if (upazilaId != null && upazilaId != 0) {
                querySt += " AND o.upazilla.id = " + upazilaId;
            }
            System.out.println("user query is = " + mainQuerySt + querySt);
            System.out.println("off " + offset + " num " + numofRecords);
            System.out.println("countQuerySt----------------->" + countQuerySt + querySt);
            List<User> list = this.openSession().createQuery(mainQuerySt + querySt).setFirstResult(offset).setMaxResults(numofRecords).list();
            System.out.println("countQuerySt----------------->" + countQuerySt + querySt);
            long count = (Long) this.openSession().createQuery(countQuerySt + querySt).list().get(0);
            System.out.println("Count----------------->" + count);
            List<Object> result = new ArrayList<Object>();
            result.add(list);
            result.add(count);
            result.add(count); // ????

            return result;
        } catch (Exception e) {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void requestForgotPassword(String email, String contextPath) throws ExceptionWrapper {
        User user = (User) this.openSession().createQuery("select o from User o where o.email='" + email + "'").uniqueResult();
        if (user == null) {
            throw new ExceptionWrapper(localizer.getLocalizedText("emailNotFound", LocaleContextHolder.getLocale()));
        }
        System.out.println("user = " + user);
        //need to check pending request to block multiple request
        try {
            String token = getToken();
            ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
            forgotPasswordRequest.setId(null);
            forgotPasswordRequest.setUser(user);
            forgotPasswordRequest.setRequestedOn(new Date());
            forgotPasswordRequest.setToken(token);
            this.sessionFactory.getCurrentSession().save(forgotPasswordRequest);
            sendForgotPasswordMail(user, token, contextPath);
        } catch (ExceptionWrapper | HibernateException e) {
            throw new ExceptionWrapper(e.getMessage());
        }

    }

    private String getToken() {
        String token;
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        token = org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
        return token;
    }

    private void sendForgotPasswordMail(User user, String token, String contextPath) throws ExceptionWrapper {
        Locale locale = LocaleContextHolder.getLocale();
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setTo(user.getEmail());
            if (locale.getLanguage().equals("bn")) {
                helper.setSubject("পাসওয়ার্ড রিসেটের তথ্য");
                String msgBody = "<html>প্রিয় " + user.getFullNameBn() + ",<br><br>"
                        + "এই ই-মেইল টি পাসওয়ার্ড রিসেটের অনুরোধের সাপেক্ষে পাঠানো হয়েছে। এটি আপনার নিরাপত্তার জন্যই করা হয়েছে, এবং কেবলমাত্র আপনিই পাসওয়ার্ড রিসেট করতে পারবেন।"
                        + "পাসওয়ার্ড রিসেট করার জন্য নিচের লিঙ্কটিতে ক্লিক করুন অথবা লিঙ্কটি কপি করে ব্রাউজারের অ্যাড্রেস বারে পেস্ট করুন।<br><br>"
                        + contextPath + "/passwordReset/?token=" + token
                        + "<br>"
                        + "আপনি যদি পাসওয়ার্ড ভুলে না যান তবে দয়া করে মেইলটি উপেক্ষা করুন।<br><br>"
                        + "প্রেরক<br>এমএইএস অ্যাডমিন"
                        + "</html>";
                helper.setText(msgBody, true);
            } else {
                helper.setSubject("Password Reset Information");
                String msgBody = "<html>Dear " + user.getFullNameEn() + ",<br><br>"
                        + "This email was sent automatically by MIS in response to your request to reset your password. This is done for your protection; only you, the recipient of this email can take the next step in the password reset process."
                        + "<br>To choose new password and access your account click or copy and paste the following link into the address bar of your browser:<br><br>"
                        + contextPath + "/passwordReset/?token=" + token
                        + "<br>"
                        + "If you did not forget your password, please ignore this email.<br><br>"
                        + "Sent From<br>MIS Admin"
                        + "</html>";
                helper.setText(msgBody, true);
            }

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionWrapper(e.getMessage());
        }
//        catch (MessagingException e)
//        {
//            throw new MailParseException(e);
//        }
//        catch (javax.mail.MessagingException ex)
//        {
//            //logger.getlogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public void recoverForgotPassword(String password, String salt, String token) throws ExceptionWrapper {
        if (token != null && !token.isEmpty()) {
            try {
                System.out.println("token = " + token);
                ForgotPasswordRequest forgotPasswordRequest = (ForgotPasswordRequest) this.sessionFactory.getCurrentSession().createQuery("select o from ForgotPasswordRequest o where o.token=:token").setParameter("token", token).uniqueResult();
                System.out.println("forgotPasswordRequest.getUser() = " + forgotPasswordRequest.getUser().getId());
                User user = forgotPasswordRequest.getUser();
                user.setPassword(password);
                user.setSalt(salt);
                this.sessionFactory.getCurrentSession().merge(user);
            } catch (Exception nre) {
                nre.printStackTrace();
                throw new ExceptionWrapper("No request found with this token!");
            }

        } else {
            throw new ExceptionWrapper("Requested url is not valid!");
        }
    }

    @Override
    public void updateVisitingLog(Integer userId, boolean logged) {
        List<VisitingLog> resultList = this.sessionFactory.getCurrentSession().createQuery("select v from VisitingLog v where v.userId = " + userId + " and v.logged = 1 order by v.loginDate DESC").setMaxResults(3).list();
        for (VisitingLog visitingLog : resultList) {
            visitingLog.setLogged(false);
            visitingLog.setLogoutDate(CalendarUtility.getCurrentDateAsCalendar());
            this.sessionFactory.getCurrentSession().merge(visitingLog);
        }
        if (logged) {
            VisitingLog visitingLog = new VisitingLog();
            visitingLog.setUserId(userId);
            this.sessionFactory.getCurrentSession().save(visitingLog);
        }
    }

    @Override
    public boolean isLoggedIn(Integer userId) {
        VisitingLog result = (VisitingLog) this.sessionFactory.getCurrentSession().createQuery("select v from VisitingLog v where v.userId = " + userId + " and v.logged = 1 and DATE(v.loginDate)=:today").setParameter("today", Calendar.getInstance().getTime()).uniqueResult();
        System.out.println("result = " + result);
        return result != null;
    }

    @Override
    public List<Object> getAudittrailListBySearchParameter(Map parameter, int offset, int numofRecords) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            LoggingServiceType loggingServiceType = !parameter.get("loggingServiceType").equals("") ? LoggingServiceType.valueOf((String) parameter.get("loggingServiceType")) : null;
            LoggingTableType loggingTableType = !parameter.get("loggingTableType").equals("") ? LoggingTableType.valueOf((String) parameter.get("loggingTableType")) : null;
            String startDate = !parameter.get("startDate").equals("") ? (String) parameter.get("startDate") : null;
            String endDate = !parameter.get("endDate").equals("") ? (String) parameter.get("endDate") : date;

            @SuppressWarnings("unchecked")
            String mainQuerySt = "from ChangeLog o where 0=0 and changedDate between '" + startDate + "' and '" + endDate + "'";
            String countQuerySt = "select count(distinct o.id) from ChangeLog o where 0=0 and changedDate between '" + startDate + "' and '" + endDate + "'";

            String querySt = "";

            if (loggingServiceType != null) {
                querySt += " AND o.loggingServiceType in :loggingServiceType";
            }
            if (loggingTableType != null) {
                querySt += " AND o.loggingTableType in :loggingTableType";
            }
            Query query = sessionFactory.getCurrentSession().createQuery(mainQuerySt + querySt).setFirstResult(offset).setMaxResults(numofRecords);
            Query countQuery = sessionFactory.getCurrentSession().createQuery(countQuerySt + querySt);
            if (loggingServiceType != null) {
                query = query.setParameter("loggingServiceType", loggingServiceType);
                countQuery = countQuery.setParameter("loggingServiceType", loggingServiceType);
            }
            if (loggingTableType != null) {
                query = query.setParameter("loggingTableType", loggingTableType);
                countQuery = countQuery.setParameter("loggingTableType", loggingTableType);
            }
            List<ChangeLog> list = query.list();
            long count = (Long) countQuery.list().get(0);
            System.out.println("ChangeLog size = " + list.size());
            System.out.println("ChangeLog total = " + count);

            List<Object> result = new ArrayList<Object>();
            result.add(list);
            result.add(count);
            result.add(count); // ????

            return result;
        } catch (Exception e) {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JsonResult userInfoUpdate(UserDetail userDetail) {
        JsonResult jr = new JsonResult(false, "");
        try {
            User dbUser = (User) sessionFactory.getCurrentSession().createQuery("SELECT u FROM User as u where u.id =:userId")
                    .setParameter("userId", userDetail.getUserId()).uniqueResult();
            dbUser.setEmail(userDetail.getEmail());
            dbUser.setFullNameBn(userDetail.getUserNameBn());
            dbUser.setFullNameEn(userDetail.getUserNameEn());
            dbUser.setMobileNo(userDetail.getMobile());
            this.sessionFactory.getCurrentSession().update(dbUser);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jr;
    }

    @Override
    public JsonResult changePasswordAndBasicInfo(UserDetail userDetail) {
        JsonResult jr = new JsonResult(false, "");
        try {
            User dbUser = (User) sessionFactory.getCurrentSession().createQuery("SELECT u FROM User as u where u.id =:userId")
                    .setParameter("userId", userDetail.getId()).uniqueResult();

            String sql = "UPDATE user SET user.salt =:salt, user.password=:password, status=0 , email =:email, mobile_no=:mobile,designation=:designation,full_name_bn=:full_name_bn,full_name_en=:full_name_en, modification_date= SYSDATE(),modified_by=:id  where user.id=:id";
            sessionFactory.getCurrentSession().createSQLQuery(sql)
                    .setParameter("id", userDetail.getId())
                    .setParameter("salt", userDetail.getSalt())
                    .setParameter("password", userDetail.getPasseword())
                    .setParameter("email", userDetail.getEmail())
                    .setParameter("mobile", userDetail.getMobile())
                    .setParameter("full_name_bn", userDetail.getUserNameBn())
                    .setParameter("full_name_en", userDetail.getUserNameEn())
                    .setParameter("designation", userDetail.getDesignation())
                    .executeUpdate();
//            dbUser.setEmail(userDetail.getEmail());
//            dbUser.setFullNameBn(userDetail.getUserNameBn());
//            dbUser.setFullNameEn(userDetail.getUserNameEn());
//            dbUser.setDesignation(userDetail.getDesignation());
//            dbUser.setMobileNo(userDetail.getMobile());
//            dbUser.setStatus(2);
//            dbUser.setPassword(userDetail.getPasseword());
//            dbUser.setSalt(userDetail.getSalt());
//            this.sessionFactory.getCurrentSession().update(dbUser);
        } catch (Exception ex) {
            jr.setIsError(true);
            jr.setErrorMsg(ex.getMessage());
            ex.printStackTrace();
        }
        return jr;
    }

//     @Override
//    public void changePasswordAndBasicInfo(User user) {
//        try {
////        User dbUser = getUser(userId);
//            String sql = "UPDATE user SET user.salt =:salt, user.password=:password, status=0 , email =:email, mobile_no=:mobile where user.id=:id";
//            sessionFactory.getCurrentSession().createSQLQuery(sql)
//                    .setParameter("id", user.getId())
//                    .setParameter("salt", user.getSalt())
//                    .setParameter("password", user.getPassword())
//                    .setParameter("email", user.getEmail())
//                    .setParameter("mobile", user.getMobileNo())                    
//                    .executeUpdate();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}
