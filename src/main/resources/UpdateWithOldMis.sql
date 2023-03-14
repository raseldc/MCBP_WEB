/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  shamiul Islam-AnunadSolution
 * Created: Apr 7, 2021
 */

UPDATE imlma.beneficiary ib
JOIN (SELECT b.nid old_nid,
b.present_division_id old_division,
b.present_district_id old_district,
b.present_upazila_id old_upazila,
b.present_union_id old_union,
f.name_in_english fiscalYearNameInEn,
f.name_in_bangla fiscalYearNameInBn,
s.name_in_bangla schem_name_in_banlga,
b.scheme_id scheme_id,
bse.conception_duration old_conception_duration,
bse.conception_term old_conception_term

 FROM mowca_new.beneficiary b
 JOIN mowca_new.fiscal_year f ON b.fiscal_year_id = f.id
 JOIN mowca_new.scheme s ON s.id = b.scheme_id
 JOIN mowca_new.beneficiary_socio_economic_info bse ON bse.beneficiary_id = b.id
 WHERE b.`status` = '0') mb
 ON ib.nid = mb.old_nid
 SET ib.is_lm_mis_exist = mb.scheme_id, 
 ib.old_mis_division = mb.old_division,
 ib.old_mis_district = mb.old_district, 
  ib.old_mis_upazila = mb.old_upazila,
    ib.old_mis_union = mb.old_union,
    ib.old_mis_fiscal_year_name = mb.fiscalYearNameInBn,
    ib.old_mis_scheme_name = mb.schem_name_in_banlga,
    ib.old_conception_term = mb.old_conception_duration,
    ib.old_conception_duration = mb.old_conception_term
  