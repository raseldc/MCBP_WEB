/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  shamiul Islam-AnunadSolution
 * Created: Jul 28, 2022
 */
proc_payment:BEGIN
DECLARE done INT DEFAULT FALSE;
Declare count_ int Default 0;
Declare totalAmount double Default 0.0;
Declare benId INT;
Declare new_ps_id INT;
Declare schemeCode INT;
Declare divisionCode INT;
Declare districtCode INT;
Declare upazilaCode INT;
Declare allowance_amount_var float;
Declare prev_ps_id INT;
Declare grant_count INT;
Declare payroll_status_var INT;
Declare month_count INT;
Declare charge float;
DECLARE curs1 CURSOR FOR

SELECT distinct(b.Id) from beneficiary b 
left join payment p on b.Id = p.beneficiary_id
left join payroll_summary ps on p.payroll_summary_id = ps.id
where 
b.`status` = 0 and 
case
 when payroll_list_type_val = 0 then b.present_upazila_id = upazila_val and b.applicant_type = 0
 when payroll_list_type_val = 1 then b.present_upazila_id = upazila_val  and b.applicant_type = 1
 when payroll_list_type_val = 2 then b.present_upazila_id = upazila_val  and b.applicant_type = 2
 when payroll_list_type_val = 3 then b.applicant_type = 3  
 when payroll_list_type_val = 4 then b.applicant_type = 4
end 
and CHAR_LENGTH(b.account_no)>=11 and b.account_no is not null 
and b.Id not in
(select benf.Id from beneficiary benf join payment pay on benf.Id = pay.beneficiary_id
where pay.id is not null and (FIND_IN_SET(pay.cycle_id, func_payment_cycle(payment_cycle_val))>0)
and (pay.returned_code is null or pay.returned_code = 2 or pay.returned_code = 3 or pay.returned_code = 4 or pay.is_granted=1))
AND b.enrollment_date <= (SELECT pc.end_date FROM payment_cycle pc WHERE pc.id=payment_cycle_val);

DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

select p.allowance_amount * p.month_count, p.month_count into allowance_amount_var, month_count from payment_cycle p where p.id = payment_cycle_val;
select s.code into schemeCode from scheme s where s.id = scheme_id_val;
select t.ps into prev_ps_id from (select id as ps from payroll_summary a where a.scheme_id = scheme_id_val and 
a.payment_cycle_id =payment_cycle_val and 
case
 when payroll_list_type_val = 0 then upazila_id = upazila_val 
 when payroll_list_type_val = 1 then upazila_id = upazila_val
 when payroll_list_type_val = 2 then payroll_list_type = 3  
 when payroll_list_type_val = 3 then payroll_list_type = 4
end 
union select 0 as ps limit 1) t;

if prev_ps_id > 0  then
	SELECT p.payroll_status into payroll_status_var from payroll_summary p where p.id=prev_ps_id;
	if payroll_status_var = 2 then
		set prev_ps_id = 0;
		Select prev_ps_id;
		leave proc_payment;
	else
		delete from payment where payroll_summary_id = prev_ps_id;
		delete from payroll_summary where id=prev_ps_id;
	end if;
end if; 

INSERT INTO payroll_summary (`created_by`, `creation_date`, `allowance_per_beneficiary`, `payroll_status`, `district_id`, `division_id`,
`payment_cycle_id`, `scheme_id`, `upazila_id`, total_allowance, total_beneficiary, payroll_list_type) VALUES (user_val, NOW(), allowance_amount_var, 0, district_val, division_val,
payment_cycle_val, scheme_id_val, upazila_val, 0, 0, payroll_list_type_val);
SET new_ps_id = LAST_INSERT_ID();
set count_ = 0;
set totalAmount = 0.0;
OPEN curs1;
	read_loop: LOOP FETCH curs1 INTO benId;
	IF done THEN LEAVE read_loop;
	END IF;
        
        SELECT COALESCE(m.charge,0) charge into charge FROM beneficiary b
            LEFT JOIN mobile_banking_provider m ON m.id = b.mobile_banking_provider_id
                WHERE b.id = benId;
        
        INSERT INTO payment (creation_date,account_number,allowance_amount,bank,bank_bn,beneficiary_name,branch,branch_bn,branch_id,
	mobile_number,payment_status,routing_number,created_by,account_type_id,nid,
	district_id,division_id,ministry_code,cycle_id,payment_type,payroll_summary_id,union_id,upazila_id,
	scheme_code, division_code, district_code, upazila_code, union_code,comments, payment_date, paid_amount, beneficiary_id, approval_status,payment_charge) 
	select now(), b.account_no, (allowance_amount_var+(charge*month_count)), 
	case when b.payment_type = 0 then bn.name_in_english 
             when b.payment_type =1 then m.name_in_english 
             when b.payment_type =2 then 'Post Office' end,
	case when b.payment_type = 0 then bn.name_in_bangla 
             when b.payment_type =1 then m.name_in_bangla 
             when b.payment_type =2 then 'পোস্ট অফিস' end,
	b.account_name, 
	case when b.payment_type = 0 then br.name_in_english when b.payment_type =1 then ''  when b.payment_type =2 then pb.name_in_english end, 
	case when b.payment_type = 0 then br.name_in_bangla when b.payment_type =1 then ''  when b.payment_type =2 then pb.name_in_bangla end, 
	case when b.payment_type = 0 then br.id when b.payment_type =1 then b.mobile_banking_provider_id when b.payment_type =2 then pb.id end,
	b.mobile_number, 0, case when b.payment_type = 0 then br.routing_number when b.payment_type =1 
	then m.routing_number else pb.routing_number end as routing_number, user_val, b.account_type_id, b.nid,
	b.present_district_id, b.present_division_id, 3005, payment_cycle_val, b.payment_type, new_ps_id, b.present_union_id, 
	b.present_upazila_id, schemeCode, d.code, ds.code, up.code, u.code, '', now(),0,b.Id,0,charge
	from  beneficiary b
	left join bank bn on b.bank_id = bn.id
	left join branch br on b.branch_id = br.id
	left join mobile_banking_provider m on b.mobile_banking_provider_id = m.id
	left join post_office_branch pb on b.post_office_branch_id = pb.id
	join division d on b.present_division_id = d.id
	join district ds on b.present_district_id = ds.id
	join upazila up on b.present_upazila_id = up.id
	join unions u on b.present_union_id = u.id
	where b.Id = benId;

	select t.payId into grant_count from
	((select p.id as payId from payment p where p.beneficiary_id = benId and 
	(FIND_IN_SET(p.cycle_id, func_payment_cycle(payment_cycle_val))>0)
	and (p.returned_code is not null and p.returned_code !=2))
	union (select 0 as payId) limit 1) t;
	if grant_count>0 then
	update payment p set p.is_granted =1 where p.id = grant_count;
	end if;
	set count_= count_+1;
   set totalAmount = totalAmount + (allowance_amount_var+(charge*month_count));
END LOOP read_loop;
CLOSE curs1;
if count_ = 0  then
	delete from payroll_summary where id = new_ps_id;
	set prev_ps_id = -1;
	Select prev_ps_id;
	leave proc_payment;
end if;

update payroll_summary p set p.total_beneficiary = count_, p.total_allowance = totalAmount where p.id = new_ps_id;
select new_ps_id;

END


-------------------------------0LD-------------------------------------------------

proc_payment:BEGIN
DECLARE done INT DEFAULT FALSE;
Declare count_ int Default 0;
Declare benId INT;
Declare new_ps_id INT;
Declare schemeCode INT;
Declare divisionCode INT;
Declare districtCode INT;
Declare upazilaCode INT;
Declare allowance_amount_var float;
Declare prev_ps_id INT;
Declare grant_count INT;
Declare payroll_status_var INT;
DECLARE curs1 CURSOR FOR

SELECT distinct(b.Id) from beneficiary b 
left join payment p on b.Id = p.beneficiary_id
left join payroll_summary ps on p.payroll_summary_id = ps.id
where 
b.`status` = 0 and 
case
 when payroll_list_type_val = 0 then b.present_upazila_id = upazila_val and b.applicant_type = 0
 when payroll_list_type_val = 1 then b.present_upazila_id = upazila_val  and b.applicant_type = 1
 when payroll_list_type_val = 2 then b.present_upazila_id = upazila_val  and b.applicant_type = 2
 when payroll_list_type_val = 3 then b.applicant_type = 3  
 when payroll_list_type_val = 4 then b.applicant_type = 4
end 
and CHAR_LENGTH(b.account_no)>=11 and b.account_no is not null 
and b.Id not in
(select benf.Id from beneficiary benf join payment pay on benf.Id = pay.beneficiary_id
where pay.id is not null and (FIND_IN_SET(pay.cycle_id, func_payment_cycle(payment_cycle_val))>0)
and (pay.returned_code is null or pay.returned_code = 2 or pay.returned_code = 3 or pay.returned_code = 4 or pay.is_granted=1))
AND b.enrollment_date <= (SELECT pc.end_date FROM payment_cycle pc WHERE pc.id=payment_cycle_val);

DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

select p.allowance_amount * p.month_count into allowance_amount_var from payment_cycle p where p.id = payment_cycle_val;
select s.code into schemeCode from scheme s where s.id = scheme_id_val;
select t.ps into prev_ps_id from (select id as ps from payroll_summary a where a.scheme_id = scheme_id_val and 
a.payment_cycle_id =payment_cycle_val and 
case
 when payroll_list_type_val = 0 then upazila_id = upazila_val 
 when payroll_list_type_val = 1 then upazila_id = upazila_val
 when payroll_list_type_val = 2 then payroll_list_type = 3  
 when payroll_list_type_val = 3 then payroll_list_type = 4
end 
union select 0 as ps limit 1) t;

if prev_ps_id > 0  then
	SELECT p.payroll_status into payroll_status_var from payroll_summary p where p.id=prev_ps_id;
	if payroll_status_var = 2 then
		set prev_ps_id = 0;
		Select prev_ps_id;
		leave proc_payment;
	else
		delete from payment where payroll_summary_id = prev_ps_id;
		delete from payroll_summary where id=prev_ps_id;
	end if;
end if; 

INSERT INTO payroll_summary (`created_by`, `creation_date`, `allowance_per_beneficiary`, `payroll_status`, `district_id`, `division_id`,
`payment_cycle_id`, `scheme_id`, `upazila_id`, total_allowance, total_beneficiary, payroll_list_type) VALUES (user_val, NOW(), allowance_amount_var, 0, district_val, division_val,
payment_cycle_val, scheme_id_val, upazila_val, 0, 0, payroll_list_type_val);
SET new_ps_id = LAST_INSERT_ID();
set count_ = 0;
OPEN curs1;
	read_loop: LOOP FETCH curs1 INTO benId;
	IF done THEN LEAVE read_loop;
	END IF;
		INSERT INTO payment (creation_date,account_number,allowance_amount,bank,bank_bn,beneficiary_name,branch,branch_bn,branch_id,
	mobile_number,payment_status,routing_number,created_by,account_type_id,nid,
	district_id,division_id,ministry_code,cycle_id,payment_type,payroll_summary_id,union_id,upazila_id,
	scheme_code, division_code, district_code, upazila_code, union_code,comments, payment_date, paid_amount, beneficiary_id, approval_status) 
	select now(), b.account_no, allowance_amount_var, 
	case when b.payment_type = 0 then bn.name_in_english when b.payment_type =1 then m.name_in_english when b.payment_type =2 then 'Post Office' end,
	case when b.payment_type = 0 then bn.name_in_bangla when b.payment_type =1 then m.name_in_bangla when b.payment_type =2 then 'পোস্ট অফিস' end,
	b.account_name, 
	case when b.payment_type = 0 then br.name_in_english when b.payment_type =1 then ''  when b.payment_type =2 then pb.name_in_english end, 
	case when b.payment_type = 0 then br.name_in_bangla when b.payment_type =1 then ''  when b.payment_type =2 then pb.name_in_bangla end, 
	case when b.payment_type = 0 then br.id when b.payment_type =1 then b.mobile_banking_provider_id when b.payment_type =2 then pb.id end,
	b.mobile_number, 0, case when b.payment_type = 0 then br.routing_number when b.payment_type =1 
	then m.routing_number else pb.routing_number end as routing_number, user_val, b.account_type_id, b.nid,
	b.present_district_id, b.present_division_id, 3005, payment_cycle_val, b.payment_type, new_ps_id, b.present_union_id, 
	b.present_upazila_id, schemeCode, d.code, ds.code, up.code, u.code, '', now(),0,b.Id,0
	from  beneficiary b
	left join bank bn on b.bank_id = bn.id
	left join branch br on b.branch_id = br.id
	left join mobile_banking_provider m on b.mobile_banking_provider_id = m.id
	left join post_office_branch pb on b.post_office_branch_id = pb.id
	join division d on b.present_division_id = d.id
	join district ds on b.present_district_id = ds.id
	join upazila up on b.present_upazila_id = up.id
	join unions u on b.present_union_id = u.id
	where b.Id = benId;
	select t.payId into grant_count from
	((select p.id as payId from payment p where p.beneficiary_id = benId and 
	(FIND_IN_SET(p.cycle_id, func_payment_cycle(payment_cycle_val))>0)
	and (p.returned_code is not null and p.returned_code !=2))
	union (select 0 as payId) limit 1) t;
	if grant_count>0 then
	update payment p set p.is_granted =1 where p.id = grant_count;
	end if;
	set count_= count_+1;
END LOOP read_loop;
CLOSE curs1;
if count_ = 0  then
	delete from payroll_summary where id = new_ps_id;
	set prev_ps_id = -1;
	Select prev_ps_id;
	leave proc_payment;
end if;
update payroll_summary p set p.total_beneficiary = count_, p.total_allowance = count_ * p.allowance_per_beneficiary where p.id = new_ps_id;
select new_ps_id;
END