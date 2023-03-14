/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import com.wfp.lmmis.enums.EnumMappingClass;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public class PaymentInformationView {

    private int serial;
    private int id;
    private Integer benID;
    private String benNameBn;
    private String benNameEn;

    private String nid;
    private String accountName;
    private String accountNo;
    private String accountNumber;
    private String mobile;
    private String paymentProvider;

    private Integer bankId;
    private String bankName;

    private Integer branchId;
    private String branchName;

    private Integer accountType;

    private Integer mobileBankProviderNameId;
    private String mobileBankProviderName;

    private Integer postOfficeBranch;
    private String postOfficeBranchName;
    private Integer accountTypePO;

    private String paymentType;
    private Integer paymentTypeInt;

    private int divisionId;
    private int districtId;
    private int upazilaId;
    private int unionId;
    private int wardId;

    private String divisionNameEN;
    private String districtNameEN;
    private String upazilaNameEN;
    private String unionNameEN;
    private String wardNameEN;

    private String divisionNameBN;
    private String districtNameBN;
    private String upazilaNameBN;
    private String unionNameBN;
    private String wardNameBN;

    private String village;
    private String villageNameEn;
    private String villageNameBn;
    private int count;
    private int isBounceBack;

    private int reasonToPayInformationChange;
    private EnumMappingClass reasonEnumMappingClass;

    private Date creationDate;
    private String creationDateSt;

    private String paymentCycleNameEN;
    private String paymentCycleNameBN;
    private MultipartFile[] files;
    private int hasFile;

    public String getUnion() {
        return union;
    }

    /**
     *
     * @param union
     */
    public void setUnion(String union) {
        this.union = union;
    }

    private String union;

    private String lastPaymentCycleDate;

    public String getLastPaymentCycleDate() {
        return lastPaymentCycleDate;
    }

    public void setLastPaymentCycleDate(String lastPaymentCycleDate) {
        this.lastPaymentCycleDate = lastPaymentCycleDate;
    }

    public String getPaymentCycleNameEN() {
        return paymentCycleNameEN;
    }

    public void setPaymentCycleNameEN(String paymentCycleNameEN) {
        this.paymentCycleNameEN = paymentCycleNameEN;
    }

    public String getPaymentCycleNameBN() {
        return paymentCycleNameBN;
    }

    public void setPaymentCycleNameBN(String paymentCycleNameBN) {
        this.paymentCycleNameBN = paymentCycleNameBN;
    }

    public int getHasFile() {
        return hasFile;
    }

    /**
     *
     * @param hasFile
     */
    public void setHasFile(int hasFile) {
        this.hasFile = hasFile;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    /**
     *
     * @return
     */
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationDateSt() {
        return creationDateSt;
    }

    public void setCreationDateSt(String creationDateSt) {
        this.creationDateSt = creationDateSt;
    }

    public String getPostOfficeBranchName() {
        return postOfficeBranchName;
    }

    public void setPostOfficeBranchName(String postOfficeBranchName) {
        this.postOfficeBranchName = postOfficeBranchName;
    }

    public EnumMappingClass getReasonEnumMappingClass() {
        return reasonEnumMappingClass;
    }

    /**
     *
     * @param reasonEnumMappingClass
     */
    public void setReasonEnumMappingClass(EnumMappingClass reasonEnumMappingClass) {
        this.reasonEnumMappingClass = reasonEnumMappingClass;
    }

    public int getIsBounceBack() {
        return isBounceBack;
    }

    public void setIsBounceBack(int isBounceBack) {
        this.isBounceBack = isBounceBack;
    }

    /**
     *
     * @return
     */
    public int getReasonToPayInformationChange() {
        return reasonToPayInformationChange;
    }

    /**
     *
     * @param reasonToPayInformationChange
     */
    public void setReasonToPayInformationChange(int reasonToPayInformationChange) {
        this.reasonToPayInformationChange = reasonToPayInformationChange;
    }

    public Integer getPaymentTypeInt() {
        return paymentTypeInt;
    }

    public void setPaymentTypeInt(Integer paymentTypeInt) {
        this.paymentTypeInt = paymentTypeInt;
    }

    public Integer getBenID() {
        return benID;
    }

    public void setBenID(Integer benID) {
        this.benID = benID;
    }

    public String getBenNameBn() {
        return benNameBn;
    }

    public void setBenNameBn(String benNameBn) {
        this.benNameBn = benNameBn;
    }

    public String getBenNameEn() {
        return benNameEn;
    }

    public void setBenNameEn(String benNameEn) {
        this.benNameEn = benNameEn;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    /**
     *
     * @param accountNo
     */
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     *
     * @param accountNumber
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Integer getMobileBankProviderNameId() {
        return mobileBankProviderNameId;
    }

    public void setMobileBankProviderNameId(Integer mobileBankProviderNameId) {
        this.mobileBankProviderNameId = mobileBankProviderNameId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     *
     * @return
     */
    public String getMobileBankProviderName() {
        return mobileBankProviderName;
    }

    public void setMobileBankProviderName(String mobileBankProviderName) {
        this.mobileBankProviderName = mobileBankProviderName;
    }

    public Integer getPostOfficeBranch() {
        return postOfficeBranch;
    }

    /**
     *
     * @param postOfficeBranch
     */
    public void setPostOfficeBranch(Integer postOfficeBranch) {
        this.postOfficeBranch = postOfficeBranch;
    }

    public Integer getAccountTypePO() {
        return accountTypePO;
    }

    public void setAccountTypePO(Integer accountTypePO) {
        this.accountTypePO = accountTypePO;
    }

    public String getPaymentType() {
        return paymentType;
    }

    /**
     *
     * @param paymentType
     */
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     *
     * @return
     */
    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     *
     * @return
     */
    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public int getUpazilaId() {
        return upazilaId;
    }

    /**
     *
     * @param upazilaId
     */
    public void setUpazilaId(int upazilaId) {
        this.upazilaId = upazilaId;
    }

    public int getUnionId() {
        return unionId;
    }

    /**
     *
     * @param unionId
     */
    public void setUnionId(int unionId) {
        this.unionId = unionId;
    }

    public int getWardId() {
        return wardId;
    }

    public void setWardId(int wardId) {
        this.wardId = wardId;
    }

    /**
     *
     * @return
     */
    public String getDivisionNameEN() {
        return divisionNameEN;
    }

    public void setDivisionNameEN(String divisionNameEN) {
        this.divisionNameEN = divisionNameEN;
    }

    public String getDistrictNameEN() {
        return districtNameEN;
    }

    /**
     *
     * @param districtNameEN
     */
    public void setDistrictNameEN(String districtNameEN) {
        this.districtNameEN = districtNameEN;
    }

    public String getUpazilaNameEN() {
        return upazilaNameEN;
    }

    public void setUpazilaNameEN(String upazilaNameEN) {
        this.upazilaNameEN = upazilaNameEN;
    }

    public String getUnionNameEN() {
        return unionNameEN;
    }

    public void setUnionNameEN(String unionNameEN) {
        this.unionNameEN = unionNameEN;
    }

    public String getWardNameEN() {
        return wardNameEN;
    }

    /**
     *
     * @param wardNameEN
     */
    public void setWardNameEN(String wardNameEN) {
        this.wardNameEN = wardNameEN;
    }

    public String getDivisionNameBN() {
        return divisionNameBN;
    }

    public void setDivisionNameBN(String divisionNameBN) {
        this.divisionNameBN = divisionNameBN;
    }

    public String getDistrictNameBN() {
        return districtNameBN;
    }

    public void setDistrictNameBN(String districtNameBN) {
        this.districtNameBN = districtNameBN;
    }

    /**
     *
     * @return
     */
    public String getUpazilaNameBN() {
        return upazilaNameBN;
    }

    public void setUpazilaNameBN(String upazilaNameBN) {
        this.upazilaNameBN = upazilaNameBN;
    }

    public String getUnionNameBN() {
        return unionNameBN;
    }

    public void setUnionNameBN(String unionNameBN) {
        this.unionNameBN = unionNameBN;
    }

    public String getWardNameBN() {
        return wardNameBN;
    }

    public void setWardNameBN(String wardNameBN) {
        this.wardNameBN = wardNameBN;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getVillageNameEn() {
        return villageNameEn;
    }

    public void setVillageNameEn(String villageNameEn) {
        this.villageNameEn = villageNameEn;
    }

    /**
     *
     * @return
     */
    public String getVillageNameBn() {
        return villageNameBn;
    }

    public void setVillageNameBn(String villageNameBn) {
        this.villageNameBn = villageNameBn;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
