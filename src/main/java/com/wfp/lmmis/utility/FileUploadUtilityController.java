/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import com.wfp.lmmis.applicant.forms.PersonalInfoForm;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 *
 * @author PCUser
 */
@Controller
public class FileUploadUtilityController
{

    /**
     *
     */
    public static final String FILE_CREATION_PATH = "F:/SPFMSP-LM-MIS/spfmsp/LM-MIS/web/resources/uploadedFile/";

    @RequestMapping("/saveFiles")
//    @RequestMapping("/saveFiles/{folderId}")
    @ResponseBody
    public String saveFiles(
            @RequestParam("file") MultipartFile multipartFile,
            Model map) throws IllegalStateException, IOException
    {
        System.out.println("in saveFiles...");
//        List<MultipartFile> files = uploadForm.getFiles();
        String saveDirectory = FILE_CREATION_PATH + "/";
        String fileName = multipartFile.getOriginalFilename();
        System.out.println("fileName=" + fileName);
        new File(FILE_CREATION_PATH).mkdirs();
        if (!"".equalsIgnoreCase(fileName))
        {
            // Handle file content - multipartFile.getInputStream()
            multipartFile.transferTo(new File(saveDirectory + fileName));
        }
        return fileName;
//        return "";
    }
}
