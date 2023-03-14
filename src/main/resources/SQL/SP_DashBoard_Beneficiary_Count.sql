/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  shamiul Islam-AnunadSolution
 * Created: Mar 27, 2022
 */

BEGIN
	DECLARE Count_ INT DEFAULT 0;	
	DECLARE ACTIVE INT DEFAULT 0;	
	DECLARE TEMPORARILY_ACTIVE INT DEFAULT 0;	
	DECLARE INACTIVE INT DEFAULT 0;	
	DECLARE PAYMENT_HOLD INT DEFAULT 0;
	DECLARE PAYMENT_COMPLETE INT DEFAULT 0;
				
	UPDATE union_extend SET total_beneficiary = 0;
	UPDATE union_extend ue
	JOIN (
		SELECT COUNT(1) COUNT,
			beneficiary.permanent_union_id unionId,
			SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) ACTIVE, 
			SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) TEMPORARILY_ACTIVE,
			SUM(CASE WHEN STATUS = 2 THEN 1 ELSE 0 END) INACTIVE, 
			SUM(CASE WHEN STATUS = 3 THEN 1 ELSE 0 END) PAYMENT_HOLD,
			SUM(CASE WHEN STATUS = 4 THEN 1 ELSE 0 END) PAYMENT_COMPLETE,
			SUM(CASE WHEN applicant_type = b'000' THEN 1 ELSE 0 END) total_union_beneficiary,
			SUM(CASE WHEN applicant_type = b'001'THEN 1 ELSE 0 END) total_municipal_beneficiary,
			SUM(CASE WHEN applicant_type = b'010' THEN 1 ELSE 0 END) total_city_corporation_beneficiary,
			SUM(CASE WHEN applicant_type = b'011' THEN 1 ELSE 0 END) total_bgme_beneficiary,
			SUM(CASE WHEN applicant_type = b'100' THEN 1 ELSE 0 END) total_bkme_beneficiary,
			SUM(CASE WHEN applicant_type = b'101' THEN 1 ELSE 0 END) REGULAR_beneficiary,
	  		

			COALESCE(SUM(CASE WHEN applicant_type = b'000' AND status = 0 THEN 1 ELSE 0 END),0) total_union_beneficiary_active,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'000' AND STATUS = 1 THEN 1 ELSE 0 END),0) total_union_beneficiary_temporarily_active,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'000' AND STATUS = 2 THEN 1 ELSE 0 END),0) total_union_beneficiary_inactive,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'000' AND STATUS = 3 THEN 1 ELSE 0 END),0) total_union_beneficiary_payment_hold,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'000' AND STATUS = 4 THEN 1 ELSE 0 END),0) total_union_beneficiary_payment_complete,
																							
			COALESCE(SUM(CASE WHEN applicant_type = b'001' AND status = 0 THEN 1 ELSE 0 END),0) total_municipal_beneficiary_active,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'001' AND STATUS = 1 THEN 1 ELSE 0 END),0) total_municipal_beneficiary_temporarily_active,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'001' AND STATUS = 2 THEN 1 ELSE 0 END),0) total_municipal_beneficiary_inactive,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'001' AND STATUS = 3 THEN 1 ELSE 0 END),0) total_municipal_beneficiary_payment_hold,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'001' AND STATUS = 4 THEN 1 ELSE 0 END),0) total_municipal_beneficiary_payment_complete,
																							
			COALESCE(SUM(CASE WHEN applicant_type = b'010' AND status = 0 THEN 1 ELSE 0 END),0) total_city_corporation_beneficiary_active,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'010' AND STATUS = 1 THEN 1 ELSE 0 END),0) total_city_corporation_beneficiary_temporarily_active,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'010' AND STATUS = 2 THEN 1 ELSE 0 END),0) total_city_corporation_beneficiary_inactive,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'010' AND STATUS = 3 THEN 1 ELSE 0 END),0) total_city_corporation_beneficiary_payment_hold,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'010' AND STATUS = 4 THEN 1 ELSE 0 END),0) total_city_corporation_beneficiary_payment_complete,
																							
			COALESCE(SUM(CASE WHEN applicant_type = b'011' AND status = 0 THEN 1 ELSE 0 END),0) total_bgme_beneficiary_active,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'011' AND STATUS = 1 THEN 1 ELSE 0 END),0) total_bgme_beneficiary_temporarily_active,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'011' AND STATUS = 2 THEN 1 ELSE 0 END),0) total_bgme_beneficiary_inactive,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'011' AND STATUS = 3 THEN 1 ELSE 0 END),0) total_bgme_beneficiary_payment_hold,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'011' AND STATUS = 4 THEN 1 ELSE 0 END),0) total_bgme_beneficiary_payment_complete,
																							
			COALESCE(SUM(CASE WHEN applicant_type = b'100' AND status = 0 THEN 1 ELSE 0 END),0) total_bkme_beneficiary_active,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'100' AND STATUS = 1 THEN 1 ELSE 0 END),0) total_bkme_beneficiary_temporarily_active,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'100' AND STATUS = 2 THEN 1 ELSE 0 END),0) total_bkme_beneficiary_inactive,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'100' AND STATUS = 3 THEN 1 ELSE 0 END),0) total_bkme_beneficiary_payment_hold,
	  		COALESCE(SUM(CASE WHEN applicant_type = b'100' AND STATUS = 4 THEN 1 ELSE 0 END),0) total_bkme_beneficiary_payment_complete


		FROM beneficiary 
		GROUP BY beneficiary.permanent_union_id
	
	
		) bu ON ue.union_id = bu.unionId

	SET ue.total_beneficiary = bu.count,
		 ue.active_beneficiary=bu.ACTIVE,
		 ue.temporarily_active_beneficiary = bu.TEMPORARILY_ACTIVE,
		 ue.inactive_beneficiary = bu.INACTIVE,
		 ue.payment_hold_beneficiary = bu.PAYMENT_HOLD,
		 ue.payment_complete_beneficiary = bu.PAYMENT_COMPLETE,
		 
		 ue.total_union_beneficiary = bu.total_union_beneficiary,
		 ue.total_municipal_beneficiary = bu.total_municipal_beneficiary,
		 ue.total_city_corporation_beneficiary = bu.total_city_corporation_beneficiary,
		 ue.total_bgme_beneficiary = bu.total_bgme_beneficiary,
	     ue.total_bkme_beneficiary = bu.total_bkme_beneficiary,
	    
	     ue.total_union_beneficiary_active = bu.total_union_beneficiary_active,
		 ue.total_municipal_beneficiary_active = bu.total_municipal_beneficiary_active,
		 ue.total_city_corporation_beneficiary_active = bu.total_city_corporation_beneficiary_active,
		 ue.total_bgme_beneficiary_active = bu.total_bgme_beneficiary_active,
		 ue.total_bkme_beneficiary_active =bu.total_bkme_beneficiary_active

		 ;


	UPDATE upazila_extend ue
	JOIN (
		SELECT sum(union_extend.total_beneficiary) bCount,
		sum(union_extend.active_beneficiary) ACTIVE,
		sum(union_extend.temporarily_active_beneficiary) TEMPORARILY_ACTIVE,
		sum(union_extend.inactive_beneficiary) INACTIVE,
		sum(union_extend.payment_hold_beneficiary) PAYMENT_HOLD,
		sum(union_extend.payment_complete_beneficiary) PAYMENT_COMPLETE,
		
		sum(union_extend.total_union_beneficiary) total_union_beneficiary,
		sum(union_extend.total_city_corporation_beneficiary) total_city_corporation_beneficiary,
		sum(union_extend.total_municipal_beneficiary) total_municipal_beneficiary,
		sum(union_extend.total_bgme_beneficiary) total_bgme_beneficiary,
		sum(union_extend.total_bkme_beneficiary) total_bkme_beneficiary,
		
		
		
		sum(union_extend.total_union_beneficiary_active) total_union_beneficiary_active,
		sum(union_extend.total_municipal_beneficiary_active) total_municipal_beneficiary_active,		
		sum(union_extend.total_city_corporation_beneficiary_active) total_city_corporation_beneficiary_active,
		sum(union_extend.total_bgme_beneficiary_active) total_bgme_beneficiary_active,
		sum(union_extend.total_bkme_beneficiary_active) total_bkme_beneficiary_active,
		unions.upazilla_id upazilaId
		FROM union_extend
		JOIN unions ON unions.id = union_extend.union_id
			GROUP BY unions.upazilla_id 
		) bu ON ue.upazila_id = bu.upazilaId
		
	SET  ue.total_beneficiary = bu.bCount,	
		 ue.active_beneficiary=bu.ACTIVE,
		 ue.temporarily_active_beneficiary = bu.TEMPORARILY_ACTIVE,
		 ue.inactive_beneficiary = bu.INACTIVE,
		 ue.payment_hold_beneficiary = bu.PAYMENT_HOLD,
		 ue.payment_complete_beneficiary = bu.PAYMENT_COMPLETE,
		 
   	 ue.total_union_beneficiary = bu.total_union_beneficiary,
		 ue.total_municipal_beneficiary = bu.total_municipal_beneficiary,
		 ue.total_city_corporation_beneficiary = bu.total_city_corporation_beneficiary,
		 ue.total_bgme_beneficiary = bu.total_bgme_beneficiary,
	    ue.total_bkme_beneficiary = bu.total_bkme_beneficiary,
		
		 ue.total_union_beneficiary_active = bu.total_union_beneficiary_active,
		 ue.total_municipal_beneficiary_active = bu.total_municipal_beneficiary_active,
		 ue.total_city_corporation_beneficiary_active = bu.total_city_corporation_beneficiary_active,
		 ue.total_bgme_beneficiary_active = bu.total_bgme_beneficiary_active,
		 ue.total_bkme_beneficiary_active =bu.total_bkme_beneficiary_active
		 ;

	UPDATE district_extend ue
	JOIN (
		SELECT sum(upazila_extend.total_beneficiary) bCount,
		sum(upazila_extend.active_beneficiary) ACTIVE,
		sum(upazila_extend.temporarily_active_beneficiary) TEMPORARILY_ACTIVE,
		sum(upazila_extend.inactive_beneficiary) INACTIVE,
		sum(upazila_extend.payment_hold_beneficiary) PAYMENT_HOLD,
		sum(upazila_extend.payment_complete_beneficiary) PAYMENT_COMPLETE,
		
		sum(upazila_extend.total_union_beneficiary) total_union_beneficiary,
		sum(upazila_extend.total_city_corporation_beneficiary) total_city_corporation_beneficiary,
		sum(upazila_extend.total_municipal_beneficiary) total_municipal_beneficiary,
		sum(upazila_extend.total_bgme_beneficiary) total_bgme_beneficiary,
		sum(upazila_extend.total_bkme_beneficiary) total_bkme_beneficiary,
		
		sum(upazila_extend.total_union_beneficiary_active) total_union_beneficiary_active,
		sum(upazila_extend.total_municipal_beneficiary_active) total_municipal_beneficiary_active,		
		sum(upazila_extend.total_city_corporation_beneficiary_active) total_city_corporation_beneficiary_active,
		sum(upazila_extend.total_bgme_beneficiary_active) total_bgme_beneficiary_active,
		sum(upazila_extend.total_bkme_beneficiary_active) total_bkme_beneficiary_active,
		
		upazila.district_id districtId
		FROM upazila_extend
		JOIN upazila ON upazila.id = upazila_extend.upazila_id
			GROUP BY upazila.district_id 
		) bu ON ue.district_id = bu.districtId

	SET  ue.total_beneficiary = bu.bCount,
		 ue.active_beneficiary=bu.ACTIVE,
		 ue.temporarily_active_beneficiary = bu.TEMPORARILY_ACTIVE,
		 ue.inactive_beneficiary = bu.INACTIVE,
		 ue.payment_hold_beneficiary = bu.PAYMENT_HOLD,
		 ue.payment_complete_beneficiary = bu.PAYMENT_COMPLETE,
		 
		ue.total_union_beneficiary = bu.total_union_beneficiary,
		 ue.total_municipal_beneficiary = bu.total_municipal_beneficiary,
		 ue.total_city_corporation_beneficiary = bu.total_city_corporation_beneficiary,
		 ue.total_bgme_beneficiary = bu.total_bgme_beneficiary,
	    ue.total_bkme_beneficiary = bu.total_bkme_beneficiary,
		
		 ue.total_union_beneficiary_active = bu.total_union_beneficiary_active,
		 ue.total_municipal_beneficiary_active = bu.total_municipal_beneficiary_active,
		 ue.total_city_corporation_beneficiary_active = bu.total_city_corporation_beneficiary_active,
		 ue.total_bgme_beneficiary_active = bu.total_bgme_beneficiary_active,
		 ue.total_bkme_beneficiary_active =bu.total_bkme_beneficiary_active
		 ;

	UPDATE division_extend ue
	JOIN (
		SELECT sum(district_extend.total_beneficiary) bCount,
		sum(district_extend.active_beneficiary) ACTIVE,
		sum(district_extend.temporarily_active_beneficiary) TEMPORARILY_ACTIVE,
		sum(district_extend.inactive_beneficiary) INACTIVE,
		sum(district_extend.payment_hold_beneficiary) PAYMENT_HOLD,
		sum(district_extend.payment_complete_beneficiary) PAYMENT_COMPLETE,
		
		sum(district_extend.total_union_beneficiary) total_union_beneficiary,
		sum(district_extend.total_city_corporation_beneficiary) total_city_corporation_beneficiary,
		sum(district_extend.total_municipal_beneficiary) total_municipal_beneficiary,
		sum(district_extend.total_bgme_beneficiary) total_bgme_beneficiary,
		sum(district_extend.total_bkme_beneficiary) total_bkme_beneficiary,
		
		sum(district_extend.total_union_beneficiary_active) total_union_beneficiary_active,
		sum(district_extend.total_municipal_beneficiary_active) total_municipal_beneficiary_active,		
		sum(district_extend.total_city_corporation_beneficiary_active) total_city_corporation_beneficiary_active,
		sum(district_extend.total_bgme_beneficiary_active) total_bgme_beneficiary_active,
		sum(district_extend.total_bkme_beneficiary_active) total_bkme_beneficiary_active,
		
		district.division_id divisionId
		FROM district_extend
		JOIN district ON district.id = district_extend.district_id
		GROUP BY district.division_id 
		) bu ON ue.division_id = bu.divisionId

	SET ue.total_beneficiary = bu.bCount,
		 ue.active_beneficiary=bu.ACTIVE,
		 ue.temporarily_active_beneficiary = bu.TEMPORARILY_ACTIVE,
		 ue.inactive_beneficiary = bu.INACTIVE,
		 ue.payment_hold_beneficiary = bu.PAYMENT_HOLD,
		 ue.payment_complete_beneficiary = bu.PAYMENT_COMPLETE,
		 
		 ue.total_union_beneficiary = bu.total_union_beneficiary,
		 ue.total_municipal_beneficiary = bu.total_municipal_beneficiary,
		 ue.total_city_corporation_beneficiary = bu.total_city_corporation_beneficiary,
		 ue.total_bgme_beneficiary = bu.total_bgme_beneficiary,
	    ue.total_bkme_beneficiary = bu.total_bkme_beneficiary,
		
		ue.total_union_beneficiary_active = bu.total_union_beneficiary_active,
		 ue.total_municipal_beneficiary_active = bu.total_municipal_beneficiary_active,
		 ue.total_city_corporation_beneficiary_active = bu.total_city_corporation_beneficiary_active,
		 ue.total_bgme_beneficiary_active = bu.total_bgme_beneficiary_active,
		 ue.total_bkme_beneficiary_active =bu.total_bkme_beneficiary_active
		;
	



UPDATE dashboard d
LEFT JOIN (
SELECT SUM(d.total_beneficiary) total_beneficiary,
SUM(d.active_beneficiary)active_beneficiary , 
SUM(d.temporarily_active_beneficiary)temporarily_active_beneficiary, 
SUM(d.inactive_beneficiary) inactive_beneficiary, 
SUM(d.payment_hold_beneficiary) payment_hold_beneficiary, 
SUM(d.payment_complete_beneficiary) payment_complete_beneficiary,
SUM(d.total_union_beneficiary_active) total_union_beneficiary_active,
SUM(d.total_bgme_beneficiary_active) total_bgme_beneficiary_active,
SUM(d.total_bkme_beneficiary_active) total_bkme_beneficiary_active,
SUM(d.total_municipal_beneficiary_active) total_municipal_beneficiary_active,
SUM(d.total_city_corporation_beneficiary_active) total_city_corporation_beneficiary_active
FROM division_extend d
) u
ON d.id = 1
set
d.total_beneficiary = u.total_beneficiary,
d.active_beneficiary = u.active_beneficiary,
d.temporarily_active_beneficiary = u.temporarily_active_beneficiary,
d.inactive_beneficiary = u.inactive_beneficiary,
d.payment_hold_beneficiary =u.payment_hold_beneficiary,
d.payment_complete_beneficiary= u.payment_complete_beneficiary ,
d.total_union_beneficiary_active = u.total_union_beneficiary_active,
d.total_municipal_beneficiary_active = u.total_municipal_beneficiary_active,
d.total_city_corporation_beneficiary_active = u.total_city_corporation_beneficiary_active,
d.total_bgme_beneficiary_active = u.total_bgme_beneficiary_active,
d.total_bkme_beneficiary_active = u.total_bkme_beneficiary_active
WHERE d.id = 1;
END