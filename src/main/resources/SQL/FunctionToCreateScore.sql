/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Shamiul Islam at Anunad Solution
 * Created: Mar 24, 2023
 */

1 x
2 3d gender 100
3x
4x
5
6x
7x
8
9
10  1 100
11
12
13x
14
15 100
16
17
18
19
20x
21x
22x
23x
24x
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40

CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetFamilyIncomePerson`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption
    WHEN 0 THEN 0.00
    ELSE 4.0
  END;


CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetGuardianRelation`(
	`inputOption` INT,
	`land` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE land
    WHEN 1 THEN 
    	CASE inputOption 
    		when 1 then 1
    		when 2 then 2
    		when 3 then 3
    		ELSE 0
    		End
    ELSE 0
  END;

CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetEthnicMinorityGroup`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption
    WHEN 0 THEN 0.00
    ELSE 4.0
  END;


CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetGuardianMaxEduQua`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption
    WHEN 1 THEN 3.00
    WHEN 2 THEN 2.00
    ELSE 1.0
  END;



CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetGuardianHusbandOrWifeEducationQualification`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption
    WHEN 1 THEN 2.00    
    ELSE 1.0
  END;


CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetReceivedAnotherScholarship`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption
    WHEN 1 THEN 0.00    
    ELSE 2.0
  END;


CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetDoesApplicantsGuardianHaveDisabilityCard`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption
    WHEN 1 THEN 6.00    
    ELSE 0.0
  END;


CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetApplicantsFamilyMemberHavePhysicalMentalDisability`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption
    WHEN 1 THEN 3.00    
    ELSE 0.0
  END;


CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetApplicantSufferingFromLongtermIllnessByBirth`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption
    WHEN 1 THEN 5.00    
    ELSE 0.0
  END;

CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetApplicantsGuardianSufferingFromLongtermIllnessByBirth`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption
    WHEN 1 THEN 5.00    
    ELSE 0.0
  END;


CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetFamilySourceOfIncome`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption
    WHEN 1 THEN 5.00    
    ELSE 0.0
  END;



CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetGuardianJobArea`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 2 THEN 2.00    
    ELSE 0.0
  END;




CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetGuardianFamilyMembersJob`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 1 THEN 4.00    
    WHEN 2 THEN 3.00    
    WHEN 3 THEN 2.00    
    ELSE 0.0
  END;

CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetGuardianMonthlyIncome`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
      WHEN inputOption< 10000 THEN 2.00             
    ELSE 0.0
  END;


CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetGuardianBothOneNoneAlive`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 1 THEN 0.00             
    WHEN 2 THEN 2.00             
    WHEN 3 THEN 4.00             
    ELSE 0.0
  END;


CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetNoOfFamilyMemberUnder18`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 1 THEN 0.00             
    WHEN 2 THEN 2.00             
    WHEN 3 THEN 4.00             
    ELSE 0.0
  END;

CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetStudentLiveArea`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 1 THEN 4.00             
    WHEN 2 THEN 4.00             
    WHEN 3 THEN 4.00             
    WHEN 4 THEN 4.00             
    ELSE 0.0
  END;



CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetNoOfRoomInHouse`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 1 THEN 5.00             
    WHEN 2 THEN 3.00             
    WHEN 3 THEN 1.00                 
    ELSE 0.0
  END;




CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetIsElectricityInHouse`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 1 THEN 0.00                                      
    ELSE 3.0
  END;





CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetToiletTypeInTheHouse`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 1 THEN 0.00                                      
    ELSE 2.0
  END;





CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetHouseHaveAnyTelevision`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 1 THEN 0.00                                      
    ELSE 2.0
  END;



CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetCookOn`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 1 THEN 0.00                                      
    WHEN 2 THEN 1.00    
    WHEN 3 THEN 3.00    
    WHEN 4 THEN 3.00    
    ELSE 0.0
  END;


CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetFloorMaterialInHouse`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 1 THEN 3.00                                          
    ELSE 0.0
  END;



CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetWallMaterialInHouse`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 1 THEN 3.00                                          
    WHEN 2 THEN 3.00                                          
    WHEN 3 THEN 2.00                                          
    WHEN 4 THEN 1.00                                          
    WHEN 5 THEN 0.00                                          
    WHEN 6 THEN 0.00                                          
    ELSE 0.0
  END;

CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetRoofMaterialInHouse`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 1 THEN 5.00                                          
    WHEN 2 THEN 2.00                                          
    WHEN 3 THEN 1.00                                          
    WHEN 4 THEN 0.00                                          
    WHEN 5 THEN 0.00                                          
    WHEN 6 THEN 0.00                                          
    ELSE 0.0
  END;


CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetAmountOfLand`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 1 THEN 5.00                                          
    WHEN 2 THEN 3.00                                          
    WHEN 3 THEN 0.00                                          
    
    ELSE 0.0
  END;

CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetApplicantsGuardianHaveWhichCard`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 1 THEN 5.00                                          
    WHEN 2 THEN 3.00                                          
    WHEN 3 THEN 0.00                                          
    
    ELSE 0.0
  END;



CREATE DEFINER=`root`@`localhost` FUNCTION `fn_GetGuardianMobile`(
	`inputOption` INT
)
RETURNS float
LANGUAGE SQL
DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
RETURN CASE inputOption    
    WHEN 1 THEN 1.00
    ELSE 1.0
  END;













	SELECT id,	family_source_of_income		,	
				 fn_GetAmountOfLand(amount_of_land) `amount_of_land`,
				fn_GetApplicantsFamilyMemberHavePhysicalMentalDisability(applicants_family_member_have_physical_mental_disability) `applicants_family_member_have_physical_mental_disability`,
				fn_GetApplicantsGuardianSufferingFromLongtermIllnessByBirth(applicants_guardian_suffering_from_longterm_illness_by_birth) applicants_guardian_suffering_from_longterm_illness_by_birth,
				fn_GetApplicantSufferingFromLongtermIllnessByBirth(applicant_suffering_from_longterm_illness_by_birth) applicant_suffering_from_longterm_illness_by_birth,
				fn_GetCookOn(cook_on) cook_on, 
				fn_GetDoesApplicantsGuardianHaveDisabilityCard(does_applicant_have_disability_card) does_applicant_have_disability_card,
				fn_GetEthnicMinorityGroup(ethnic_minority_group) ethnic_minority_group,
				fn_GetFamilySourceOfIncome(family_source_of_income) family_source_of_income,
				fn_GetFloorMaterialInHouse(floor_material_in_house) floor_material_in_house,
				fn_GetGuardianBothOneNoneAlive(guardian_both_one_none_alive) guardian_both_one_none_alive,
				fn_GetGuardianFamilyMembersJob(guardian_family_members) guardian_family_members,
				fn_GetGuardianHusbandOrWifeEducationQualification(applicant.guardian_husband_or_wife_education_qualification) guardian_husband_or_wife_education_qualification,
				fn_GetGuardianJobArea(guardian_job_area)guardian_job_area,
				fn_GetGuardianMaxEduQua(guardian_max_edu_qua)guardian_max_edu_qua,
				fn_GetGuardianMobile(1)mobile,
				fn_GetGuardianMonthlyIncome(guardian_monthly_income)guardian_monthly_income,
				fn_GetHouseHaveAnyTelevision(house_have_any_Television)house_have_any_Television,
				fn_GetIsElectricityInHouse(applicant.is_electricity_in_house)is_electricity_in_house,
				fn_GetNoOfFamilyMemberUnder18(applicant.no_of_family_member_under18)no_of_family_member_under18,
				fn_GetNoOfRoomInHouse(applicant.no_of_room_in_house)no_of_room_in_house,
				fn_GetReceivedAnotherScholarship(applicant.received_another_scholarship)received_another_scholarship,
				fn_GetRoofMaterialInHouse(applicant.roof_material_in_house)roof_material_in_house,
				fn_GetStudentLiveArea(applicant.student_live_area)student_live_area,
				fn_GetToiletTypeInTheHouse(applicant.toilet_type_in_the_house)toilet_type_in_the_house,
				fn_GetWallMaterialInHouse(applicant.wall_material_in_house)wall_material_in_house


				 
				 

			FROM applicant
			WHERE id = 6790334