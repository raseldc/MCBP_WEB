package com.wfp.lmmis.training.dao;

import com.wfp.lmmis.masterdata.model.Branch;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.training.controller.Batch;
import com.wfp.lmmis.training.controller.EnrollmentInfo;
import com.wfp.lmmis.training.controller.UserTrainingCourse;
import com.wfp.lmmis.training.controller.UserTrainingUpdateApi;
import com.wfp.lmmis.training.model.Training;
import com.wfp.lmmis.training.model.TrainingAttendee;
import com.wfp.lmmis.training.model.UserTraining;
import com.wfp.lmmis.training.model.UserTrainingAttendance;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.UserTrainingView;
import com.wfp.lmmis.utility.UserViewForTraining;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.Map;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDaoImpl implements TrainingDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Training getTraining(Integer id) {
        Training training = (Training) getCurrentSession().get(Training.class, id);
        return training;
    }

    @Override
    public void save(Training training) {
        try {
            this.getCurrentSession().save(training);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param editedTraining
     */
    @Override
    public void edit(Training editedTraining) {
        Training training = (Training) sessionFactory.getCurrentSession().get(Training.class, editedTraining.getId());
        training.setFiscalYear(editedTraining.getFiscalYear());
        training.setTrainingType(editedTraining.getTrainingType());
        training.setTrainer(editedTraining.getTrainer());
        training.setTrainingVenue(editedTraining.getTrainingVenue());
        training.setNumberOfPerticipants(editedTraining.getNumberOfPerticipants());
        training.setHeadOffice(editedTraining.getHeadOffice());
        training.setDivision(editedTraining.getDivision());
        training.setDistrict(editedTraining.getDistrict());
        training.setUpazilla(editedTraining.getUpazilla());
        training.setUnion(editedTraining.getUnion());
        training.setTrainingCost(editedTraining.getTrainingCost());
        training.setStartDate(editedTraining.getStartDate());
        training.setEndDate(editedTraining.getEndDate());
        training.setDurationDay(editedTraining.getDurationDay());
        training.setComment(editedTraining.getComment());
        training.setApplicantType(editedTraining.getApplicantType());
        training.setModifiedBy(editedTraining.getModifiedBy());
        training.setModificationDate(editedTraining.getModificationDate());
        training.setFileBase64(editedTraining.getFileBase64());
        training.setFileExtension(editedTraining.getFileExtension());
        try {
            this.getCurrentSession().update(training);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<Training> getTrainingList() {
        @SuppressWarnings("unchecked")
        List<Training> list = sessionFactory.getCurrentSession().createQuery("from Training WHERE deleted = 0").list();
        return list;
    }

    /**
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        Training training = (Training) sessionFactory.getCurrentSession().get(Training.class, id);
        if (training != null) {
            try {
                //Delete Attendee
//                String deleteQuery = "DELETE FROM TrainingAttendee WHERE training.id = " + id;
//                this.sessionFactory.getCurrentSession().createQuery(deleteQuery).executeUpdate();

                //Delete Training
                String softDelQuery = "UPDATE Training SET deleted = 1 WHERE id = " + id;
                sessionFactory.getCurrentSession().createQuery(softDelQuery).executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("msg = " + e.getMessage());
            }
        }
    }

    @Override
    public List<Training> getTrainingReportData(Map parameter) {
        Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
        Integer divisionId = parameter.get("divisionId") != null ? (Integer) parameter.get("divisionId") : null;
        Integer districtId = parameter.get("districtId") != null ? (Integer) parameter.get("districtId") : null;
        Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;
        Calendar startDate = parameter.get("startDate") != null ? (Calendar) parameter.get("startDate") : null;
        Calendar endDate = parameter.get("endDate") != null ? (Calendar) parameter.get("endDate") : null;
        String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;

        String querySt = "select t from Training t where 0 = 0 ";
        if (fiscalYearId != null) {
            querySt += " and t.fiscalYear.id=" + fiscalYearId;
        }
        if (divisionId != null && divisionId != 0) {
            querySt += " AND t.division.id = " + divisionId;
        }
        if (districtId != null && districtId != 0) {
            querySt += " AND t.district.id = " + districtId;
        }
        if (upazilaId != null && upazilaId != 0) {
            querySt += " AND t.upazilla.id = " + upazilaId;
        }
        if (startDate != null) {
            querySt += " AND t.startDate >= :startDate";
        }
        if (endDate != null) {
            querySt += " AND t.startDate <= :endDate";
        }

        List<Training> trainings;
        Query query = this.sessionFactory.getCurrentSession().createQuery(querySt);
        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }
        trainings = query.list();

        return trainings;
    }

    @Override
    public List<Object> getTrainingListBySearchParameter(Map parameter, int offset, int numofRecords) {
        try {
            Integer fiscalYearId = !parameter.get("fiscalYearId").equals("") ? (Integer) parameter.get("fiscalYearId") : null;
            Integer divisionId = parameter.get("divisionId") != null ? (Integer) parameter.get("divisionId") : null;
            Integer districtId = parameter.get("districtId") != null ? (Integer) parameter.get("districtId") : null;
            Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;
            Integer trainingTypeId = null;
            String trainingTypeIdSt = (String) parameter.get("trainingTypeId");
            if (trainingTypeIdSt != null && !"".equals(trainingTypeIdSt)) {
                trainingTypeId = Integer.valueOf(trainingTypeIdSt);
            }

            @SuppressWarnings("unchecked")
            String mainQuerySt = "from Training o where o.deleted = 0 ";
            String countQuerySt = "select count(distinct o.id) from Training o where 0=0 ";

            String querySt = "";

            if (fiscalYearId != null && fiscalYearId != 0) {
                querySt += " AND o.fiscalYear.id = " + fiscalYearId;
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
            if (trainingTypeId != null) {
                querySt += " and o.trainingType.id = " + trainingTypeId;
            }

            List<Training> list = sessionFactory.getCurrentSession().createQuery(mainQuerySt + querySt).setFirstResult(offset).setMaxResults(numofRecords).list();
            long count = (Long) sessionFactory.getCurrentSession().createQuery(countQuerySt + querySt).list().get(0);
            System.out.println("Training size = " + list.size());
            System.out.println("querySt = " + querySt);

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
    public void saveTrainingAttendee(TrainingAttendee trainingAttendee) {
        try {
            this.getCurrentSession().save(trainingAttendee);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteTrainingAttendee(Integer id) {
        try {
            String querySt = "delete from TrainingAttendee ta where ta.training.id = " + id;
            System.out.println("query = " + querySt);
            this.getCurrentSession().createQuery(querySt).executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<TrainingAttendee> getTrainingAttendeeList(Integer id) {
        String querySt = "from TrainingAttendee ta where ta.training.id = " + id;

        List<TrainingAttendee> taList = this.getCurrentSession().createQuery(querySt).list();

        for (TrainingAttendee ta : taList) {
            Hibernate.initialize(ta.getBeneficiary());
            ta.setNidEn(ta.getBeneficiary().getNid().toString());
            ta.setNidBn(CommonUtility.getNumberInBangla(ta.getBeneficiary().getNid().toString()));
            if (ta.getBeneficiary().getMobileNo() != null) {
                ta.setMobileNoEn('0' + ta.getBeneficiary().getMobileNo().toString());
                ta.setMobileNoBn('০' + CommonUtility.getNumberInBangla(ta.getBeneficiary().getMobileNo().toString()));
            }

        }

        return taList;
    }

    /**
     *
     * @param id
     * @param trainingTypeId
     * @return
     */
    @Override
    public boolean checkBeneficiaryTrainingDuplicacy(Integer id, Integer trainingTypeId) {
        String querySt = "SELECT COUNT(*) FROM training_attendee ta "
                + "JOIN training tr ON (ta.training_id = tr.id) "
                + "JOIN training_type tt ON (tr.training_type_id = tt.id) "
                + "WHERE tr.training_type_id = " + trainingTypeId + " AND ta.beneficiary_id = " + id;
        BigInteger count = null;
        try {
            count = (BigInteger) this.getCurrentSession().createSQLQuery(querySt).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (count.equals(BigInteger.ZERO)) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param enrollmentInfo
     * @param loginUser
     */
    @Override
    public void createUserTrainingAndEnrollment(EnrollmentInfo enrollmentInfo, UserDetail loginUser) {

        UserTraining userTraining = getUserTraining(enrollmentInfo.getCourseId(), enrollmentInfo.getBatchId(), enrollmentInfo.getFiscalYearId());

        if (userTraining == null) {
            FiscalYear fiscalYear = new FiscalYear();
            fiscalYear.setId(enrollmentInfo.getFiscalYearId());
            userTraining = new UserTraining();
            userTraining.setBatchId(enrollmentInfo.getBatchId());
            userTraining.setBatchName(enrollmentInfo.getBatchName());

            userTraining.setCourseId(enrollmentInfo.getCourseId());
            userTraining.setCourseName(enrollmentInfo.getCourseName());
            userTraining.setCreationDate(new Date());
            userTraining.setCreatedBy(loginUser.getUserId());
            userTraining.setFiscalYear(fiscalYear);
            userTraining.setRestictedUrl(enrollmentInfo.getRestictedUrl());

            sessionFactory.getCurrentSession().persist(userTraining);

        } else {

//            String deleteQuery = "DELETE FROM user_training_attendance WHERE \n"
//                    + "user_training_attendance.user_training_id = " + userTraining.getId();
//            this.getCurrentSession().createSQLQuery(deleteQuery).executeUpdate();
        }

        if (userTraining.getRestictedUrl() == null || userTraining.getRestictedUrl().equals("")) {
            userTraining.setRestictedUrl(enrollmentInfo.getRestictedUrl());
            sessionFactory.getCurrentSession().update(userTraining);
        }

        String query_userId = "SELECT a.user_id userId FROM user_training_attendance a \n"
                + "WHERE a.user_training_id = " + userTraining.getId();
        List<Integer> userTrainingAttendancesID = sessionFactory.getCurrentSession().createQuery("SELECT a.user.id FROM UserTrainingAttendance AS a WHERE a.userTraining.id =:userTrainingId").setParameter("userTrainingId", userTraining.getId()).list();
        List<Integer> userIdNeedToDeacive = new ArrayList<Integer>(userTrainingAttendancesID);
        userIdNeedToDeacive.removeAll(enrollmentInfo.getUserIds());

        for (int userId : userIdNeedToDeacive) {

            String sql = "DELETE FROM user_training_attendance\n"
                    + "WHERE user_training_attendance.user_id = " + userId + " AND user_training_attendance.user_training_id = " + userTraining.getId();
            sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();

        }
        for (int userId : enrollmentInfo.getUserIds()) {
            User user = new User();
            user.setId(userId);

            UserTrainingAttendance userTrainingAttendance = new UserTrainingAttendance();
            userTrainingAttendance.setUser(user);
            userTrainingAttendance.setUserTraining(userTraining);
            userTrainingAttendance.setIsActive(1);
            if (userTrainingAttendancesID.contains(userId)) {

            } else {
                sessionFactory.getCurrentSession().persist(userTrainingAttendance);
            }
        }

    }

    @Override
    public UserTraining getUserTraining(int courseId, int batchId, int fiscalYearId) {

        UserTraining userTraining = (UserTraining) sessionFactory.getCurrentSession().createQuery("SELECT ut FROM UserTraining AS ut WHERE ut.courseId =:courseId AND ut.batchId =:batchId and ut.fiscalYear.id =:fiscalYearId").
                setParameter("courseId", courseId).
                setParameter("batchId", batchId).
                setParameter("fiscalYearId", fiscalYearId).uniqueResult();
        if (userTraining != null) {
            Hibernate.initialize(userTraining.getFiscalYear());
        }
        return userTraining;
    }

    /**
     *
     * @param courseId
     * @param batchId
     * @param fiscalYearId
     * @return
     */
    @Override
    public List<UserTrainingView> getAllUserTraining(int courseId, int batchId, int fiscalYearId) {
        List<String> querList = new ArrayList<>();
        if (courseId != 0) {
            querList.add("user_training.course_id = " + courseId);
        }
        if (batchId != 0) {
            querList.add("user_training.batch_id = " + batchId);
        }
        if (fiscalYearId != 0) {
            querList.add("user_training.fiscal_year_id = " + fiscalYearId);
        }

        String queryGenerate = String.join(" and ", querList);
        if (querList.size() > 0) {
            queryGenerate = " where " + queryGenerate;
        }
        String query = "SELECT user_training.id,"
                + " user_training.course_name courseName,\n"
                + "user_training.batch_name batchName,\n"
                + " user_training.course_id courseId,\n"
                + "user_training.batch_id batchId,\n"
                + "COALESCE(user_training.training_name,'') trainingName,\n"
                + "COUNT(user_training_attendance.id) applicantCount,\n"
                + "sum(if(user_training_attendance.getCertificate=1 AND user_training_attendance.certificate_link !=\"\",1,0)) getCertificateCount,\n"
                + "fiscal_year.name_in_bangla fiscalYearName,\n"
                + "fiscal_year.id fiscalYearId\n"
                + "FROM user_training\n"
                + "LEFT JOIN user_training_attendance ON user_training_attendance.user_training_id = user_training.id\n"
                + "left join fiscal_year on fiscal_year.id = user_training.fiscal_year_id\n"
                + queryGenerate
                + " GROUP BY user_training.id";
        List<UserTrainingView> userViewFortrainings = sessionFactory.getCurrentSession().createSQLQuery(query)
                .addScalar("id", IntegerType.INSTANCE)
                .addScalar("courseName", StringType.INSTANCE)
                .addScalar("batchName", StringType.INSTANCE)
                .addScalar("courseId", IntegerType.INSTANCE)
                .addScalar("batchId", IntegerType.INSTANCE)
                .addScalar("trainingName", StringType.INSTANCE)
                .addScalar("applicantCount", IntegerType.INSTANCE)
                .addScalar("getCertificateCount", IntegerType.INSTANCE)
                .addScalar("fiscalYearName", StringType.INSTANCE)
                .addScalar("fiscalYearId", IntegerType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(UserTrainingView.class)).list();

        return userViewFortrainings;
    }

    /**
     *
     * @param userId
     * @return
     */
    @Override
    public List<UserTrainingCourse> getCourseListByUserId(int userId) {
        List<UserTrainingCourse> userViewFortrainings = new ArrayList<>();
        try {
            String query = "SELECT \n"
                    + "DISTINCT( user_training.course_name) title,user_training.course_id course_id\n"
                    + "\n"
                    + "FROM user_training\n"
                    + "LEFT JOIN user_training_attendance ON user_training_attendance.user_training_id = user_training.id\n"
                    + "WHERE user_training_attendance.user_id = " + userId;
            userViewFortrainings = sessionFactory.getCurrentSession().createSQLQuery(query)
                    .addScalar("title", StringType.INSTANCE)
                    .addScalar("course_id", IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(UserTrainingCourse.class)).list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userViewFortrainings;
    }

    /**
     *
     * @param userId
     * @param courseId
     * @return
     */
    @Override
    public List<Batch> getBatchListByCourseAndUserId(int userId, int courseId) {
        List<Batch> batchs = new ArrayList<>();
        try {
            String query = " SELECT \n"
                    + "user_training.batch_name title,user_training.batch_id id\n"
                    + "\n"
                    + "FROM user_training\n"
                    + "LEFT JOIN user_training_attendance ON user_training_attendance.user_training_id = user_training.id\n"
                    + "WHERE user_training_attendance.user_id = " + userId + " AND user_training.course_id = " + courseId;
            batchs = sessionFactory.getCurrentSession().createSQLQuery(query)
                    .addScalar("title", StringType.INSTANCE)
                    .addScalar("id", IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(Batch.class)).list();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return batchs;

    }

    /**
     *
     * @param userId
     * @param courseId
     * @param batchId
     * @param fiscalYearId
     * @return
     */
    @Override
    public List<UserViewForTraining> getAllTraingDetailsByUser(int userId, int courseId, int batchId, int fiscalYearId) {
        try {
            List<String> querList = new ArrayList<>();
            if (userId != 0) {
                querList.add("user_training_attendance.user_id = " + userId);
            }

            if (courseId != 0) {
                querList.add("user_training.course_id = " + courseId);
            }
            if (batchId != 0) {
                querList.add("user_training.batch_id = " + batchId);
            }
            if (fiscalYearId != 0) {
                querList.add("user_training.fiscal_year_id = " + fiscalYearId);
            }

            String queryGenerate = String.join(" and ", querList);
            if (querList.size() > 0) {
                queryGenerate = " where " + queryGenerate;
            }

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
                    + "	,COALESCE(user_training_attendance.remarks, '') remarks\n"
                    + "	,COALESCE(user_training_attendance.atleastOneTimeLogin,0) atleastOneTimeLogin\n"
                    + "	,COALESCE(user_training_attendance.lastActivieDate,null) lastActiveDate\n"
                    + "	,COALESCE(user_training_attendance.marks,0) marks\n"
                    + "	,COALESCE(user_training_attendance.certificate_link,\"\") certificateLink\n"
                    + "	,COALESCE(user_training_attendance.getCertificate,0) getCertificate\n"
                    + "	,COALESCE(user_training_attendance.enrollComplete,0) enrollComplete\n"
                    + " ,COALESCE(user_training.resticted_url,'') restictedUrl\n"
                    + "FROM user_training_attendance\n"
                    + "LEFT JOIN user_training ON user_training.id = user_training_attendance.user_training_id\n"
                    + "LEFT JOIN user ON user.id = user_training_attendance.user_id\n"
                    + queryGenerate;

            List<UserViewForTraining> userViewFortrainings = sessionFactory.getCurrentSession().createSQLQuery(execql)
                    .addScalar("id", IntegerType.INSTANCE)
                    .addScalar("email", StringType.INSTANCE)
                    .addScalar("fullNameBn", StringType.INSTANCE)
                    .addScalar("fullNameEn", StringType.INSTANCE)
                    .addScalar("mobileNo", StringType.INSTANCE)
                    .addScalar("courseName", StringType.INSTANCE)
                    .addScalar("batchName", StringType.INSTANCE)
                    .addScalar("complete", IntegerType.INSTANCE)
                    .addScalar("status", IntegerType.INSTANCE)
                    .addScalar("remarks", StringType.INSTANCE)
                    .addScalar("atleastOneTimeLogin", IntegerType.INSTANCE)
                    .addScalar("lastActiveDate", DateType.INSTANCE)
                    .addScalar("marks", DoubleType.INSTANCE)
                    .addScalar("certificateLink", StringType.INSTANCE)
                    .addScalar("getCertificate", IntegerType.INSTANCE)
                    .addScalar("userTrainingAttendanceId", IntegerType.INSTANCE)
                    .addScalar("enrollComplete", IntegerType.INSTANCE)
                    .addScalar("restictedUrl", StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(UserViewForTraining.class)).list();

            return userViewFortrainings;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public JsonResult userTrainingAttendanceUpdate(UserTrainingUpdateApi trainingUpdateApi) {
        JsonResult jr = new JsonResult(false, "");
        UserTrainingAttendance userTrainingAttendance = (UserTrainingAttendance) sessionFactory.getCurrentSession().createQuery("SELECT uta FROM UserTrainingAttendance as uta WHERE uta.userTraining.courseId =:courseId AND uta.userTraining.batchId =:batchId AND uta.user.email =:email")
                .setParameter("courseId", trainingUpdateApi.getCourseId())
                .setParameter("batchId", trainingUpdateApi.getBatchId())
                .setParameter("email", trainingUpdateApi.getEmail())
                .uniqueResult();
        if (trainingUpdateApi.getHistory().getLastActiveDate() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String date_St = formatter.format(trainingUpdateApi.getHistory().getLastActiveDate());

            String query = "UPDATE user_training_attendance ut\n"
                    + "JOIN (SELECT us_.id id FROM user us_ WHERE \n"
                    + "us_.email ='" + trainingUpdateApi.getEmail() + "')u\n"
                    + "ON u.id = ut.user_id\n"
                    + "SET ut.lastActivieDate = '" + date_St + "'";
            sessionFactory.getCurrentSession().createSQLQuery(query).executeUpdate();

        }

        if (userTrainingAttendance != null) {
            if (trainingUpdateApi.getHistory().getAtleastOneTimeLogin() != null) {
                userTrainingAttendance.setAtleastOneTimeLogin(trainingUpdateApi.getHistory().getAtleastOneTimeLogin());
            }
            if (trainingUpdateApi.getHistory().getCertificateLink() != null) {
                userTrainingAttendance.setCertificateLink(trainingUpdateApi.getHistory().getCertificateLink());
            }

            if (trainingUpdateApi.getHistory().getMarks() != null) {
                userTrainingAttendance.setMarks(trainingUpdateApi.getHistory().getMarks());
            }
            if (trainingUpdateApi.getHistory().getRemarks() != null) {
                userTrainingAttendance.setRemarks(trainingUpdateApi.getHistory().getRemarks());
            }
            if (trainingUpdateApi.getHistory().getCompleteness() != null) {
                userTrainingAttendance.setComplepte(trainingUpdateApi.getHistory().getCompleteness());
            }
            if (trainingUpdateApi.getHistory().getEnrollComplete() != null) {
                userTrainingAttendance.setEnrollComplete(trainingUpdateApi.getHistory().getEnrollComplete());
            }

            //   userTrainingAttendance.setComplepte(trainingUpdateApi.getHistory().getCompleteness());
            sessionFactory.getCurrentSession().update(userTrainingAttendance);
        } else {
            jr.setIsError(false);
            jr.setErrorMsg("User not found");
        }
        return jr;
    }

    @Override
    public JsonResult userTrainingAttendanceUpdateOnGetCertificate(int userTrainAttId, int isGetCertificate) {
        JsonResult jr = new JsonResult(false, "");
        try {
            UserTrainingAttendance userTrainingAttendance = (UserTrainingAttendance) sessionFactory.getCurrentSession().createQuery("SELECT uta FROM UserTrainingAttendance as uta WHERE uta.id =:userTrainAttId ")
                    .setParameter("userTrainAttId", userTrainAttId)
                    .uniqueResult();
            userTrainingAttendance.setGetCertificate(isGetCertificate);
            sessionFactory.getCurrentSession().update(userTrainingAttendance);
        } catch (Exception ex) {
            jr.setIsError(true);
            jr.setErrorMsg("Exp Occure");
            ex.printStackTrace();
        }
        return jr;
    }

    @Override
    public JsonResult userTrainingAttendanceUpdateOnZoomMeeting(int userTrainAttId, int isZoomMeeting) {
        JsonResult jr = new JsonResult(false, "");
        try {
            UserTrainingAttendance userTrainingAttendance = (UserTrainingAttendance) sessionFactory.getCurrentSession().createQuery("SELECT uta FROM UserTrainingAttendance as uta WHERE uta.id =:userTrainAttId ")
                    .setParameter("userTrainAttId", userTrainAttId)
                    .uniqueResult();
            userTrainingAttendance.setIsZoomMeeting(isZoomMeeting);
            sessionFactory.getCurrentSession().update(userTrainingAttendance);
        } catch (Exception ex) {
            jr.setIsError(true);
            jr.setErrorMsg("Exp Occure");
            ex.printStackTrace();
        }
        return jr;
    }

    @Override
    public JsonResult userTrainingAttendanceUpdateOnRemarks(int userTrainAttId, String remarks) {
        JsonResult jr = new JsonResult(false, "");
        try {
            UserTrainingAttendance userTrainingAttendance = (UserTrainingAttendance) sessionFactory.getCurrentSession().createQuery("SELECT uta FROM UserTrainingAttendance as uta WHERE uta.id =:userTrainAttId ")
                    .setParameter("userTrainAttId", userTrainAttId)
                    .uniqueResult();
            userTrainingAttendance.setRemarks(remarks);
            sessionFactory.getCurrentSession().update(userTrainingAttendance);
        } catch (Exception ex) {
            jr.setIsError(true);
            jr.setErrorMsg("Exp Occure");
            ex.printStackTrace();
        }
        return jr;
    }

    @Override
    public List<Batch> getCreatedBranchForCourse(int courseId, int fiscalYearId) {

        List<Batch> batchs = new ArrayList<>();
        try {
            String query = "SELECT user_training.batch_id id, user_training.batch_name title\n"
                    + " FROM user_training WHERE \n"
                    + " user_training.course_id = " + courseId + "\n"
                    + "  AND user_training.fiscal_year_id = " + fiscalYearId;
            batchs = sessionFactory.getCurrentSession().createSQLQuery(query)
                    .addScalar("title", StringType.INSTANCE)
                    .addScalar("id", IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(Batch.class)).list();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return batchs;

    }
}
