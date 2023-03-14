/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.controller;

import com.wfp.lmmis.applicant.service.BeneficiaryChildService;
import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.beneficiary.model.BeneficiaryChildInformation;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.BeneficiaryChildInformationDetail;
import com.wfp.lmmis.utility.DateUtilities;
import com.wfp.lmmis.utility.DoubleTypeAdapter;
import com.wfp.lmmis.utility.IntegerTypeAdapter;
import com.wfp.lmmis.utility.JsonResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Controller
public class BeneficiaryChildController {

//    //private final org.apache.log4j.logger logger = org.apache.log4j.//logger.getlogger(BeneficiaryController.class);
    @Autowired
    private BeneficiaryChildService beneficiaryChildService;

    @RequestMapping(value = "/beneficiary/child-info-add", method = RequestMethod.POST)
    @ResponseBody
    public String beneficiaryChildInformationSave(MultipartHttpServletRequest request,
            @RequestPart("file") MultipartFile[] files) {
        JsonResult jr = new JsonResult(false, "");
        try {

            //User loginedUser = (User) request.getSession().getAttribute("user");
            String JSonData = request.getParameter("childInfo");
            System.out.println("request of job assign " + request.getParameter("cycleDetailDetail"));

            BeneficiaryChildInformation beneficiaryChildInformation = new BeneficiaryChildInformation();
            Gson g = new GsonBuilder()
                    .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
                    .registerTypeAdapter(Date.class, new DateTypeAdapter())
                    .registerTypeAdapter(Double.class, new DoubleTypeAdapter()).create();;
            beneficiaryChildInformation = g.fromJson(JSonData, BeneficiaryChildInformation.class);

            Beneficiary ben = new Beneficiary();
            ben.setId(beneficiaryChildInformation.getBenId());
            beneficiaryChildInformation.setBeneficiary(ben);
            beneficiaryChildInformation.setChildDob(DateUtilities.stringToDate(beneficiaryChildInformation.getDob_st()));
            if (files.length > 0) {
                MultipartFile file = files[0];
                beneficiaryChildInformation.setFile(file);;
            }

            beneficiaryChildInformation.setUserByCreatedBy((User) request.getSession().getAttribute("user"));
            beneficiaryChildInformation.setCreationDate(new Date());
            if (beneficiaryChildInformation.getId() != null && beneficiaryChildInformation.getId() != 0) {
                BeneficiaryChildInformation db = beneficiaryChildService.getBeneficiaryChildInformationById(beneficiaryChildInformation.getId());
                db.setChildName(beneficiaryChildInformation.getChildName());
                db.setChildNo(beneficiaryChildInformation.getChildNo());
                db.setChildDob(beneficiaryChildInformation.getChildDob());
                db.setChildBirthCertificate(beneficiaryChildInformation.getChildBirthCertificate());
                db.setFile(beneficiaryChildInformation.getFile());
                jr = beneficiaryChildService.updateChildInfo(db);
            } else {

                jr = beneficiaryChildService.addChildInfo(beneficiaryChildInformation);
            }

        } catch (Exception ex) {
            //logger.getlogger(ApplicantController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jr.toJsonString();
    }

    @RequestMapping(value = {"/beneficiary/child-information/file-download"})
    public void childFileDownload(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "id", required = false) int id) {

        BeneficiaryChildInformation cycleDetailFile = beneficiaryChildService.getBeneficiaryChildInformationById(id);
        String base64 = "";
        try {
            base64 = getFileBase64(cycleDetailFile.getAttachedFileLocation());
        } catch (IOException ex) {
            //logger.getlogger(BeneficiaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] fileByte = Base64.decodeBase64(base64);

        InputStream is = new ByteArrayInputStream(fileByte);

        try {

            response.setHeader("Content-disposition", "attachment; filename=\"" + cycleDetailFile.getId() + "");
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @RequestMapping(value = "/beneficiary/get-child-info/{childId}", method = RequestMethod.GET)
    @ResponseBody
    public String getChildInfo(@PathVariable("childId") Integer id, HttpServletRequest request) {

        try {
            JsonResult jr = new JsonResult(false, "");

            BeneficiaryChildInformation beneficiaryChildInformation = beneficiaryChildService.getBeneficiaryChildInformationById(id);

            if (beneficiaryChildInformation.getAttachedFileLocation() != null && !beneficiaryChildInformation.getAttachedFileLocation().equals("")) {
                beneficiaryChildInformation.setBase64(getFileBase64(beneficiaryChildInformation.getAttachedFileLocation()));
            }
            beneficiaryChildInformation.setBeneficiary(null);

            BeneficiaryChildInformationDetail detail = new BeneficiaryChildInformationDetail(beneficiaryChildInformation);

            jr.setReturnSingleObject(detail);
            Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").setPrettyPrinting().create();
            String jsonArray = gson.toJson(jr);
            return jsonArray;
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }

        return null;
    }

    @RequestMapping(value = "/beneficiary/get-child-info/file-delete//{childId}", method = RequestMethod.GET)
    @ResponseBody
    public String cildPhoptoDelete(@PathVariable("childId") Integer id, HttpServletRequest request) {
        JsonResult jr = new JsonResult(false, "");
        try {

            BeneficiaryChildInformation beneficiaryChildInformation = beneficiaryChildService.getBeneficiaryChildInformationById(id);

            beneficiaryChildInformation.setAttachedFileLocation("");
            beneficiaryChildService.updateChildInfo(beneficiaryChildInformation);

        } catch (Exception e) {
            jr.setIsError(true);
            //logger.infoer(e.getMessage());
        }

        return jr.toJsonString();
    }

    /**
     *
     * @param realPath
     * @return
     * @throws IOException
     */
    public String getFileBase64(String realPath) throws IOException {

        if (realPath != null && !realPath.equals("")) {

            File serverFile = new File(realPath);
            if (serverFile.exists() == true) {
                BufferedImage bImage = ImageIO.read(serverFile);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bImage, "jpg", bos);
                byte[] data = bos.toByteArray();
                String base64String = Base64.encodeBase64String(data);

                return base64String;
            } else {
                return "";
            }
        } else {
            return "";
        }

    }
}
