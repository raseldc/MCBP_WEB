/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wfp.lmmis.applicant.detail;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Shamiul Islam at Anunad Solution
 */
@Getter
@Setter
public class ApplicantDetail {

    private int id;
    private String fullNameInEnglish;
    private String fullNameInBangla;
    private String fatherName;
    private String motherName;
    private String spouseName;
    private String nid;
    private String dateOfBirth;
    private Integer conceptionDuration;
}
//We have made the API
//The response will be like this:
//{
//	"ANC4": "1",
//	"ANC2": "1",
//	"ANC3": "1",
//	"Pregnancy_week": "96",
//	"ANC1": "1",
//	"DOB": "1996-05-15",
//	"MotherName": " \u098f\u0987 \u0996\u09be\u09a8\u09be\u09b0 \u09b8\u09a6\u09b8\u09cd\u09af \u09a8\u09be",
//	"HusbandName": " XXXX",
//	"Name": "XXXXX",
//	"FatherName": " \u09ae\u09be\u09b0\u09be \u0997\u09c7\u099b\u09c7"
//}
//e.g. "ANC4"=1 means ANC 4 Has taken
