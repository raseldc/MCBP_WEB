/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  shamiul Islam-AnunadSolution
 * Created: Sep 1, 2022
 */

--get Already which upazila has user 
INSERT ignore INTO user
(`active`, `designation`, `email`, `full_name_bn`, `full_name_en`, `mobile_no`, `password`, `user_id`, `profile_pic`, `salt`, `head_office_user`, 
`district_id`, `division_id`, `ministry_id`, `role_id`, `union_id`, `upazilla_id`, `is_deleted`, `user_type`, `status`, `creation_date`, `modification_date`, `created_by`, `modified_by`)
 
SELECT 1,'উপপরিচালক',CONCAT('dd.',LOWER(u.name_in_english),'@gmail.com') ,
u.name_in_bangla, 'Deputy Director' ,'',
'3a8f99e9e744de26c8e75270a9016a499c3a1dfad9749854e6e5573718a3b8081591b33542bd73f80094d54426200d372bbc27886d6e5afde3d1032ab9054a89' password,
 CONCAT('dd.',REPLACE( LOWER(u.name_in_english),' ','') ) userId,'' profilePic,'7QP59L/vmZoZKAVkiGnYUwyN2+8=' salt,
b'0' headOfficeUser,u.id districtId, di.id divisionId, 8 ministryId, 25 roleId,NULL unionId,NULL UpazilaId,
0 isDelete,2 userType,2 status,SYSDATE(),NULL,1 createdBy,null

FROM district u
JOIN division di ON di.id = u.division_id;

INSERT INTO `user_per_scheme` ( `active`, `is_deleted`, `head_office_user`, `is_default`, `user_type`, `district_id`, `division_id`, `role_id`, 
`scheme_id`, `union_id`, `upazilla_id`, `bgmea_factory_id`, `bkmea_factory_id`, `user_id`, `creation_date`) 

SELECT  b'0', b'0', b'0', b'0', 2, u.district_id, u.division_id, 25 roleId, 3 scheme_id, NULL, u.upazilla_id,
 NULL bgmea_factory_id, NULL bkmea_factory_id, u.id, SYSDATE() creation_date FROM user u 
WHERE u.id>=10058;
