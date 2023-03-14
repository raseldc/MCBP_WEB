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
DECLARE activeFiscalYear INT DEFAULT 0;
SELECT id INTO activeFiscalYear FROM fiscal_year f WHERE f.active;

UPDATE union_extend ue
JOIN (
SELECT COUNT(1) totalApplicant,
COALESCE(SUM(CASE  when a.recommendation_status = 1 then 1 END),0) rec,
COALESCE(SUM(CASE  when a.recommendation_status  = 0 then 1 END),0) nonRec,
a.permanent_union_id unionId

FROM applicant a
GROUP BY a.permanent_union_id )u 
ON ue.union_id = u.unionId
SET ue.total_applicant = u.totalApplicant,
ue.imlma_referenc_applicant =u.rec,
ue.imlma_non_referenc_applicant = u.nonRec
WHERE ue.union_id = u.unionId;


UPDATE union_extend ue
JOIN (
		SELECT a.unionId,
		COALESCE(SUM(CASE  when a.month = 1 then a.count_ END),0) jan,
		COALESCE(SUM(CASE  when a.month = 2 then a.count_ end),0) feb,
		COALESCE(SUM(CASE  when a.month = 3 then a.count_ end),0) mar,
		COALESCE(SUM(CASE  when a.month =4 then a.count_ end),0) apr,
		COALESCE(SUM(CASE  when a.month = 5 then a.count_ end),0) may,
		COALESCE(SUM(CASE  when a.month = 6 then a.count_ end),0) jun,
		COALESCE(SUM(CASE  when a.month = 7 then a.count_ end),0) jul,
		COALESCE(SUM(CASE  when a.month = 8 then a.count_ end),0) aug,
		COALESCE(SUM(CASE  when a.month = 9 then a.count_ end),0) sep,
		COALESCE(SUM(CASE  when a.month = 10 then a.count_ END),0) octo,
		COALESCE(SUM(CASE  when a.month = 11 then a.count_ end),0) nov,
		COALESCE(SUM(CASE  when a.month = 12 then a.count_ end),0) dece
		FROM (
		SELECT COUNT(a.id) count_,a.permanent_union_id unionId,
		MONTH(a.creation_date) month FROM applicant a
		WHERE a.fiscal_year_id = activeFiscalYear
                and a.applicant_type in (b'000',b'010',b'001')
		GROUP BY MONTH(a.creation_date),
		a.permanent_union_id
		ORDER BY (MONTH(a.creation_date))) a GROUP BY a.unionId) u
		ON ue.union_id = u.unionId
SET 
	ue.applicant_jan = u.jan,
	ue.applicant_feb = u.feb,
	ue.applicant_mar = u.mar,
	ue.applicant_apr = u.apr,
	ue.applicant_may = u.may,
	ue.applicant_jun = u.jun,
	ue.applicant_jul = u.jul,
	ue.applicant_aug = u.aug,
	ue.applicant_sep = u.sep,
	ue.applicant_oct = u.octo,
	ue.applicant_nov = u.nov,
	ue.applicant_dec = u.dece;

UPDATE upazila_extend ue
JOIN (
SELECT 
COALESCE(SUM(union_extend.applicant_jan),0) jan,
COALESCE(SUM(union_extend.applicant_feb),0) feb,
COALESCE(SUM(union_extend.applicant_mar),0) mar,
COALESCE(SUM(union_extend.applicant_apr),0) apr,
COALESCE(SUM(union_extend.applicant_may),0) may,
COALESCE(SUM(union_extend.applicant_jun),0) jun,
COALESCE(SUM(union_extend.applicant_jul),0) jul,
COALESCE(SUM(union_extend.applicant_aug),0) aug,
COALESCE(SUM(union_extend.applicant_sep),0) sep,
COALESCE(SUM(union_extend.applicant_oct),0) octo,
COALESCE(SUM(union_extend.applicant_nov),0) nov,
COALESCE(SUM(union_extend.applicant_dec),0) dece,
COALESCE(SUM(union_extend.total_applicant),0) total_applicant,
COALESCE(SUM(union_extend.imlma_referenc_applicant),0) imlma_referenc_applicant,
COALESCE(SUM(union_extend.imlma_non_referenc_applicant),0) imlma_non_referenc_applicant,
unions.upazilla_id upazilaId
FROM union_extend
		JOIN unions ON unions.id = union_extend.union_id
			GROUP BY unions.upazilla_id )u
			ON ue.upazila_id =u.upazilaId
			SET 
	ue.applicant_jan = u.jan,
	ue.applicant_feb = u.feb,
	ue.applicant_mar = u.mar,
	ue.applicant_apr = u.apr,
	ue.applicant_may = u.may,
	ue.applicant_jun = u.jun,
	ue.applicant_jul = u.jul,
	ue.applicant_aug = u.aug,
	ue.applicant_sep = u.sep,
	ue.applicant_oct = u.octo,
	ue.applicant_nov = u.nov,
	ue.applicant_dec = u.dece,
	ue.total_applicant = u.total_applicant,
	ue.imlma_referenc_applicant = u.imlma_referenc_applicant,
	ue.imlma_non_referenc_applicant = u.imlma_non_referenc_applicant;

UPDATE district_extend ue
JOIN (
SELECT 
COALESCE(SUM(upazila_extend.applicant_jan),0) jan,
COALESCE(SUM(upazila_extend.applicant_feb),0) feb,
COALESCE(SUM(upazila_extend.applicant_mar),0) mar,
COALESCE(SUM(upazila_extend.applicant_apr),0) apr,
COALESCE(SUM(upazila_extend.applicant_may),0) may,
COALESCE(SUM(upazila_extend.applicant_jun),0) jun,
COALESCE(SUM(upazila_extend.applicant_jul),0) jul,
COALESCE(SUM(upazila_extend.applicant_aug),0) aug,
COALESCE(SUM(upazila_extend.applicant_sep),0) sep,
COALESCE(SUM(upazila_extend.applicant_oct),0) octo,
COALESCE(SUM(upazila_extend.applicant_nov),0) nov,
COALESCE(SUM(upazila_extend.applicant_dec),0) dece,
COALESCE(SUM(upazila_extend.total_applicant),0) total_applicant,
COALESCE(SUM(upazila_extend.imlma_referenc_applicant),0) imlma_referenc_applicant,
COALESCE(SUM(upazila_extend.imlma_non_referenc_applicant),0) imlma_non_referenc_applicant,
upazila.district_id districtId
FROM upazila_extend
		JOIN upazila ON upazila.id = upazila_extend.upazila_id
			GROUP BY upazila.district_id )u
			ON ue.district_id =u.districtId
			SET 
	ue.applicant_jan = u.jan,
	ue.applicant_feb = u.feb,
	ue.applicant_mar = u.mar,
	ue.applicant_apr = u.apr,
	ue.applicant_may = u.may,
	ue.applicant_jun = u.jun,
	ue.applicant_jul = u.jul,
	ue.applicant_aug = u.aug,
	ue.applicant_sep = u.sep,
	ue.applicant_oct = u.octo,
	ue.applicant_nov = u.nov,
	ue.applicant_dec = u.dece,
	ue.total_applicant = u.total_applicant,
	ue.imlma_referenc_applicant = u.imlma_referenc_applicant,
	ue.imlma_non_referenc_applicant = u.imlma_non_referenc_applicant;
	
	UPDATE division_extend ue
JOIN (
SELECT 
COALESCE(SUM(district_extend.applicant_jan),0) jan,
COALESCE(SUM(district_extend.applicant_feb),0) feb,
COALESCE(SUM(district_extend.applicant_mar),0) mar,
COALESCE(SUM(district_extend.applicant_apr),0) apr,
COALESCE(SUM(district_extend.applicant_may),0) may,
COALESCE(SUM(district_extend.applicant_jun),0) jun,
COALESCE(SUM(district_extend.applicant_jul),0) jul,
COALESCE(SUM(district_extend.applicant_aug),0) aug,
COALESCE(SUM(district_extend.applicant_sep),0) sep,
COALESCE(SUM(district_extend.applicant_oct),0) octo,
COALESCE(SUM(district_extend.applicant_nov),0) nov,
COALESCE(SUM(district_extend.applicant_dec),0) dece,
COALESCE(SUM(district_extend.total_applicant),0) total_applicant,
COALESCE(SUM(district_extend.imlma_referenc_applicant),0) imlma_referenc_applicant,
COALESCE(SUM(district_extend.imlma_non_referenc_applicant),0) imlma_non_referenc_applicant,
district.division_id divisionId
FROM district_extend
		JOIN district ON district.id = district_extend.district_id
			GROUP BY district.division_id )u
			ON ue.division_id =u.divisionId
			SET 
	ue.applicant_jan = u.jan,
	ue.applicant_feb = u.feb,
	ue.applicant_mar = u.mar,
	ue.applicant_apr = u.apr,
	ue.applicant_may = u.may,
	ue.applicant_jun = u.jun,
	ue.applicant_jul = u.jul,
	ue.applicant_aug = u.aug,
	ue.applicant_sep = u.sep,
	ue.applicant_oct = u.octo,
	ue.applicant_nov = u.nov,
	ue.applicant_dec = u.dece,
	ue.total_applicant = u.total_applicant,
	ue.imlma_referenc_applicant = u.imlma_referenc_applicant,
	ue.imlma_non_referenc_applicant = u.imlma_non_referenc_applicant;
	
	
UPDATE dashboard ue
left JOIN (
SELECT 
COALESCE(SUM(division_extend.applicant_jan),0) jan,
COALESCE(SUM(division_extend.applicant_feb),0) feb,
COALESCE(SUM(division_extend.applicant_mar),0) mar,
COALESCE(SUM(division_extend.applicant_apr),0) apr,
COALESCE(SUM(division_extend.applicant_may),0) may,
COALESCE(SUM(division_extend.applicant_jun),0) jun,
COALESCE(SUM(division_extend.applicant_jul),0) jul,
COALESCE(SUM(division_extend.applicant_aug),0) aug,
COALESCE(SUM(division_extend.applicant_sep),0) sep,
COALESCE(SUM(division_extend.applicant_oct),0) octo,
COALESCE(SUM(division_extend.applicant_nov),0) nov,
COALESCE(SUM(division_extend.applicant_dec),0) dece,
COALESCE(SUM(division_extend.total_applicant),0) total_applicant,
COALESCE(SUM(division_extend.imlma_referenc_applicant),0) imlma_referenc_applicant,
COALESCE(SUM(division_extend.imlma_non_referenc_applicant),0) imlma_non_referenc_applicant
FROM division_extend
		)u
			ON ue.id =1
			SET 
	ue.applicant_jan = u.jan,
	ue.applicant_feb = u.feb,
	ue.applicant_mar = u.mar,
	ue.applicant_apr = u.apr,
	ue.applicant_may = u.may,
	ue.applicant_jun = u.jun,
	ue.applicant_jul = u.jul,
	ue.applicant_aug = u.aug,
	ue.applicant_sep = u.sep,
	ue.applicant_oct = u.octo,
	ue.applicant_nov = u.nov,
	ue.applicant_dec = u.dece,
	ue.total_applicant = u.total_applicant,
	ue.imlma_referenc_applicant = u.imlma_referenc_applicant,
	ue.imlma_non_referenc_applicant = u.imlma_non_referenc_applicant;
	
	


END