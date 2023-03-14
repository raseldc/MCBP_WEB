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
UPDATE beneficiary b
JOIN (SELECT beneficiary.id benID
		,COUNT(1)
		,(sum(payment.allowance_amount) / 800)		
		,MAX(payment_cycle.id) lastPaymentCycleId 
	FROM payment
	LEFT JOIN beneficiary ON beneficiary.id = payment.beneficiary_id
	LEFT JOIN payroll_summary ON payment.payroll_summary_id = payroll_summary.id
	LEFT JOIN payment_cycle ON payroll_summary.payment_cycle_id = payment_cycle.id
	LEFT JOIN fiscal_year ON fiscal_year.name_in_english = payment_cycle.fiscal_year_in_english
	WHERE payroll_summary.is_exported_to_spbmu = 1
		AND payment.payment_status = 1
	GROUP BY payment.beneficiary_id
	HAVING (sum(payment.allowance_amount) / 800) >= 33
	ORDER BY beneficiary.id
	) a ON b.Id = a.benID

SET b.`status` = 4,b.last_payment_cycle_id = a.lastPaymentCycleId,b.modification_date = current_timestamp()
WHERE b.`status` !=4;
END