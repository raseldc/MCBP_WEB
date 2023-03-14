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
		SELECT SUM(p.allowance_amount) total_payment,
		SUM( CASE WHEN f.id = activeFiscalYear THEN p.allowance_amount ELSE 0 END ) total_payemt_amount_current_year,
		SUM( CASE WHEN f.id = 3 THEN p.allowance_amount ELSE 0 END ) payment_fiscal_year_3,
  		SUM( CASE WHEN f.id = 2 THEN p.allowance_amount ELSE 0 END ) payment_fiscal_year_2,
		SUM( CASE WHEN f.id = 1 THEN p.allowance_amount ELSE 0 END ) payment_fiscal_year_1,
		p.union_id unionId FROM payment p
		JOIN payroll_summary ps ON p.payroll_summary_id = ps.id
		JOIN payment_cycle pc ON pc.id = ps.payment_cycle_id
		JOIN fiscal_year f ON f.name_in_english = pc.fiscal_year_in_english
		WHERE p.payment_status = 1 
		GROUP BY p.union_id
		) bu ON ue.union_id = bu.unionId

	SET ue.total_payemt_amount = bu.total_payment,
	ue.total_payemt_amount_current_year = bu.total_payemt_amount_current_year,
	ue.payment_fiscal_year_1 = bu.payment_fiscal_year_1,
	ue.payment_fiscal_year_2 = bu.payment_fiscal_year_2,
	ue.payment_fiscal_year_3 = bu.payment_fiscal_year_3,
	ue.payment_fiscal_year_4 = bu.total_payemt_amount_current_year;


	
	UPDATE upazila_extend ue
	JOIN (
		SELECT sum(union_extend.total_payemt_amount)total_payment, 
		sum(union_extend.total_payemt_amount_current_year)  total_payemt_amount_current_year,
                sum(union_extend.payment_fiscal_year_1)  payment_fiscal_year_1,
                sum(union_extend.payment_fiscal_year_2)  payment_fiscal_year_2,
                sum(union_extend.payment_fiscal_year_3)  payment_fiscal_year_3,
                sum(union_extend.payment_fiscal_year_4)  payment_fiscal_year_4,
                
                unions.upazilla_id upazilaId
		FROM union_extend
		JOIN unions ON unions.id = union_extend.union_id
			GROUP BY unions.upazilla_id 
		) u ON ue.upazila_id = u.upazilaId

	SET ue.total_payemt_amount = u.total_payment,
	ue.total_payemt_amount_current_year = u.total_payemt_amount_current_year,
        ue.payment_fiscal_year_1 = u.payment_fiscal_year_1,
	ue.payment_fiscal_year_2 = u.payment_fiscal_year_2,
	ue.payment_fiscal_year_3 = u.payment_fiscal_year_3,
	ue.payment_fiscal_year_4 = u.payment_fiscal_year_4;

	UPDATE district_extend ue
	JOIN (
		SELECT sum(upazila_extend.total_payemt_amount) total_payment,
                    sum(upazila_extend.total_payemt_amount_current_year) total_payemt_amount_current_year,
                    sum(upazila_extend.payment_fiscal_year_1)  payment_fiscal_year_1,
                    sum(upazila_extend.payment_fiscal_year_2)  payment_fiscal_year_2,
                    sum(upazila_extend.payment_fiscal_year_3)  payment_fiscal_year_3,
                    sum(upazila_extend.payment_fiscal_year_4)  payment_fiscal_year_4,
                    upazila.district_id districtId
		FROM upazila_extend
		JOIN upazila ON upazila.id = upazila_extend.upazila_id
			GROUP BY upazila.district_id 
		) u ON ue.district_id = u.districtId

	SET ue.total_payemt_amount = u.total_payment,
		ue.total_payemt_amount_current_year = u.total_payemt_amount_current_year,
                ue.payment_fiscal_year_1 = u.payment_fiscal_year_1,
                ue.payment_fiscal_year_2 = u.payment_fiscal_year_2,
                ue.payment_fiscal_year_3 = u.payment_fiscal_year_3,
                ue.payment_fiscal_year_4 = u.payment_fiscal_year_4;

	UPDATE division_extend ue
	JOIN (
		SELECT sum(district_extend.total_payemt_amount) total_payment,
		    sum(district_extend.total_payemt_amount_current_year) total_payemt_amount_current_year,
                    sum(district_extend.payment_fiscal_year_1)  payment_fiscal_year_1,
                    sum(district_extend.payment_fiscal_year_2)  payment_fiscal_year_2,
                    sum(district_extend.payment_fiscal_year_3)  payment_fiscal_year_3,
                    sum(district_extend.payment_fiscal_year_4)  payment_fiscal_year_4,
			district.division_id divisionId
		FROM district_extend
		JOIN district ON district.id = district_extend.district_id
		GROUP BY district.division_id 
		) u ON ue.division_id = u.divisionId

	SET ue.total_payemt_amount = u.total_payment,
                ue.total_payemt_amount_current_year = u.total_payemt_amount_current_year,
                ue.payment_fiscal_year_1 = u.payment_fiscal_year_1,
                ue.payment_fiscal_year_2 = u.payment_fiscal_year_2,
                ue.payment_fiscal_year_3 = u.payment_fiscal_year_3,
                ue.payment_fiscal_year_4 = u.payment_fiscal_year_4;

	
	UPDATE dashboard d SET d.total_payemt_amount = (SELECT SUM(total_payemt_amount) FROM division_extend );
	UPDATE dashboard d SET d.total_payemt_amount_current_year = (SELECT SUM(total_payemt_amount_current_year) FROM division_extend );
	



UPDATE dashboard ue
LEFT JOIN (
SELECT SUM(d.total_payemt_amount) total_payemt_amount,
SUM(d.total_payemt_amount_current_year)total_payemt_amount_current_year , 
SUM(d.payment_fiscal_year_1)payment_fiscal_year_1, 
SUM(d.payment_fiscal_year_2) payment_fiscal_year_2, 
SUM(d.payment_fiscal_year_3) payment_fiscal_year_3, 
SUM(d.payment_fiscal_year_4) payment_fiscal_year_4
FROM division_extend d
) u
ON ue.id = 1
set
ue.total_payemt_amount = u.total_payemt_amount,
                ue.total_payemt_amount_current_year = u.total_payemt_amount_current_year,
               ue.payment_fiscal_year_1 = u.payment_fiscal_year_1,
                ue.payment_fiscal_year_2 = u.payment_fiscal_year_2,
                ue.payment_fiscal_year_3 = u.payment_fiscal_year_3,
                ue.payment_fiscal_year_4 = u.payment_fiscal_year_4
WHERE ue.id = 1;






	
	UPDATE dashboard_fiscal_year_wise_payment ue
JOIN (

SELECT 
SUM(CASE  when a.id = 1 then a.amount END) f1Amount,
SUM(CASE  when a.id = 2 then a.amount END) f2Amount,
SUM(CASE  when a.id = 3 then a.amount END) f3Amount,
SUM(CASE  when a.id = 4 then a.amount END) f4Amount
FROM
(
SELECT SUM(p.allowance_amount) amount,f.id,f.name_in_bangla FROM payment p
JOIN payroll_summary ps ON p.payroll_summary_id = ps.id
JOIN payment_cycle pc ON pc.id = ps.payment_cycle_id
JOIN fiscal_year f ON pc.fiscal_year_in_english = f.name_in_english
WHERE p.payment_status = 1
GROUP BY f.id
ORDER BY f.id
) a) u 
ON ue.id = 1
SET 
ue.fiscal_year_1 = f1Amount,
ue.fiscal_year_2 = f2Amount,
ue.fiscal_year_3 = f3Amount,
ue.fiscal_year_4 = f4Amount;
END