/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  shamiul Islam-AnunadSolution
 * Created: Sep 1, 2022
 */
---select union user by upazila

SELECT * FROM unions u
-- WHERE u.upazilla_id IN (108,134,138,156,158,160,173,177,304,314,351,373,389,391,419,428,444,447,485,602,603,607,610,632,636,651,694,921,925,929,954,965,991,1006,1020,1027,1033,1040,1054,1067,1081,1085,1094,1095,1202,1204,1207,1213,1233,1263,1285,1294,1322,1345,1347,1349,1358,1379,1395,1504,1512,1518,1533,1537,1547,1552,1553,1561,1570,1574,1578,1582,1586,1807,1823,1855,1909,1915,1918,1927,1931,1933,1936,1940,1954,1967,1972,1975,1981,1987,1994,1998,2216,2224,2245,2249,2256,2266,2290,2614,2618,2638,2672,2692,2710,2712,2717,2721,2730,2738,2747,2756,2760,2764,2769,2777,2903,2910,2918,2921,2947,2956,2962,2984,3014,3025,3029,3041,3051,3221,3224,3230,3267,3282,3291,3330,3332,3336,3386,3532,3543,3551,3591,3605,3611,3626,3644,3668,3671,3677,3813,3847,3858,3861,3907,3929,3936,3958,3961,3985,4104,4109,4123,4138,4147,4161,4190,4240,4243,4284,4414,4419,4433,4442,4471,4643,4649,4655,4661,4665,4667,4670,4677,4712,4717,4740,4753,4764,4769,4775,4794,4802,4806,4811,4827,4833,4842,4845,4849,4854,4876,4879,4892,4906,4909,4918,4952,4961,4977,4979,4994,5015,5039,5071,5094,5133,5158,5165,5173,5202,5239,5255,5270,5440,5454,5480,5557,5585,5595,5622,5628,5646,5670,5678,5682,5760,5787,5814,5856,5865,5874,5880,5883,5924,5944,5974,5984,5994,6113,6116,6120,6122,6123,6124,6131,6155,6165,6172,6181,6194,6403,6406,6428,6447,6450,6460,6469,6475,6485,6486,6552,6576,6702,6706,6758,6768,6807,6852,6860,6864,6876,6906,6909,6915,6941,6963,6991,7018,7037,7056,7088,7204,7209,7218,7238,7240,7247,7256,7274,7283,7312,7315,7336,7345,7385,7507,7510,7521,7536,7547,7580,7583,7587,7605,7619,7622,7633,7639,7655,7672,7683,7704,7725,7734,7790,7838,7852,7855,7857,7866,7876,7887,7914,7947,7958,7980,7987,7990,8110,8112,8125,8131,8153,8172,8182,8194,8207,8229,8276,8407,8421,8425,8436,8447,8458,8475,8478,8487,8503,8542,8549,8558,8573,8576,8592,8614,8625,8665,8669,8694,8704,8725,8743,8747,8782,8790,8811,8827,8844,8850,8861,8878,8889,8894,8937,8967,8970,8990,9018,9023,9027,9029,9032,9033,9047,9050,9086,9089,9108,9117,9120,9122,9127,9131,9135,9138,9153,9162,9194,9309,9319,9323,9325,9328,9338,9347,9357,9366,9385,9395,9408,9451,9482,9486)
Where u.upazilla_id in (395,409,669,918,1088,1290,1376,1508,1831,1974,2294,2662,2743,2990,3094,3288,3334,3558,3602,3874,3915,4111,4273,4480,4680,4730,4859,4908,5063,5079,5143,5233,5487,5566,5610,5747,5835,5956,6152,6479,6528,6704,6863,6944,7066,7364,7585,7616,7773,7895,7976,8247,8273,8429,8636,8786,8867,8988,9092,9159,9376,9494)
and u.coverage_area = 0 AND u.active =1 AND u.name_in_english NOT LIKE 'Ward%'

---select muni user by upazila

SELECT * FROM unions u
-- WHERE u.upazilla_id IN (108,134,138,156,158,160,173,177,304,314,351,373,389,391,419,428,444,447,485,602,603,607,610,632,636,651,694,921,925,929,954,965,991,1006,1020,1027,1033,1040,1054,1067,1081,1085,1094,1095,1202,1204,1207,1213,1233,1263,1285,1294,1322,1345,1347,1349,1358,1379,1395,1504,1512,1518,1533,1537,1547,1552,1553,1561,1570,1574,1578,1582,1586,1807,1823,1855,1909,1915,1918,1927,1931,1933,1936,1940,1954,1967,1972,1975,1981,1987,1994,1998,2216,2224,2245,2249,2256,2266,2290,2614,2618,2638,2672,2692,2710,2712,2717,2721,2730,2738,2747,2756,2760,2764,2769,2777,2903,2910,2918,2921,2947,2956,2962,2984,3014,3025,3029,3041,3051,3221,3224,3230,3267,3282,3291,3330,3332,3336,3386,3532,3543,3551,3591,3605,3611,3626,3644,3668,3671,3677,3813,3847,3858,3861,3907,3929,3936,3958,3961,3985,4104,4109,4123,4138,4147,4161,4190,4240,4243,4284,4414,4419,4433,4442,4471,4643,4649,4655,4661,4665,4667,4670,4677,4712,4717,4740,4753,4764,4769,4775,4794,4802,4806,4811,4827,4833,4842,4845,4849,4854,4876,4879,4892,4906,4909,4918,4952,4961,4977,4979,4994,5015,5039,5071,5094,5133,5158,5165,5173,5202,5239,5255,5270,5440,5454,5480,5557,5585,5595,5622,5628,5646,5670,5678,5682,5760,5787,5814,5856,5865,5874,5880,5883,5924,5944,5974,5984,5994,6113,6116,6120,6122,6123,6124,6131,6155,6165,6172,6181,6194,6403,6406,6428,6447,6450,6460,6469,6475,6485,6486,6552,6576,6702,6706,6758,6768,6807,6852,6860,6864,6876,6906,6909,6915,6941,6963,6991,7018,7037,7056,7088,7204,7209,7218,7238,7240,7247,7256,7274,7283,7312,7315,7336,7345,7385,7507,7510,7521,7536,7547,7580,7583,7587,7605,7619,7622,7633,7639,7655,7672,7683,7704,7725,7734,7790,7838,7852,7855,7857,7866,7876,7887,7914,7947,7958,7980,7987,7990,8110,8112,8125,8131,8153,8172,8182,8194,8207,8229,8276,8407,8421,8425,8436,8447,8458,8475,8478,8487,8503,8542,8549,8558,8573,8576,8592,8614,8625,8665,8669,8694,8704,8725,8743,8747,8782,8790,8811,8827,8844,8850,8861,8878,8889,8894,8937,8967,8970,8990,9018,9023,9027,9029,9032,9033,9047,9050,9086,9089,9108,9117,9120,9122,9127,9131,9135,9138,9153,9162,9194,9309,9319,9323,9325,9328,9338,9347,9357,9366,9385,9395,9408,9451,9482,9486)
Where u.upazilla_id in (395,409,669,918,1088,1290,1376,1508,1831,1974,2294,2662,2743,2990,3094,3288,3334,3558,3602,3874,3915,4111,4273,4480,4680,4730,4859,4908,5063,5079,5143,5233,5487,5566,5610,5747,5835,5956,6152,6479,6528,6704,6863,6944,7066,7364,7585,7616,7773,7895,7976,8247,8273,8429,8636,8786,8867,8988,9092,9159,9376,9494)
and u.coverage_area = 1 AND u.active =1 AND u.name_in_english NOT LIKE 'Ward%'




-------Unions User

--get Already which upazila has user 
INSERT ignore INTO user
(`active`, `designation`, `email`, `full_name_bn`, `full_name_en`, `mobile_no`, `password`, `user_id`, `profile_pic`, `salt`, `head_office_user`, 
`district_id`, `division_id`, `ministry_id`, `role_id`, `union_id`, `upazilla_id`, `is_deleted`, `user_type`, `status`, `creation_date`, `modification_date`, `created_by`, `modified_by`)
 
SELECT 1,'ইউনিয়ন কর্মকর্তা',CONCAT('uisc.',LOWER(u.name_in_english),'@gmail.com') email,
u.name_in_bangla, CONCAT('uisc.',REPLACE( LOWER(up.name_in_english),' ',''),'.', REPLACE( LOWER(u.name_in_english),' ','')) name_in_English,'' mobile,
'3a8f99e9e744de26c8e75270a9016a499c3a1dfad9749854e6e5573718a3b8081591b33542bd73f80094d54426200d372bbc27886d6e5afde3d1032ab9054a89' password,
 CONCAT('uisc.',REPLACE( LOWER(up.name_in_english),' ',''),'.', REPLACE( LOWER(u.name_in_english),' ','')) userName,'' profile_pic,
 '7QP59L/vmZoZKAVkiGnYUwyN2+8=' salt,b'0' head_office_user ,up.district_id DistrictId, di.id divisionId, 8 ministry_id, 13 role_id,u.id union_id,
 up.id upazilla_id,0 is_deleted,2 user_type,2 status,SYSDATE() creation_date,NULL,1,null

FROM unions u
 JOIN upazila up ON up.id = u.upazilla_id
 JOIN district d ON d.id = up.district_id
 JOIN division di ON di.id = d.division_id
WHERE u.id IN (40920,40922,40988,66969,66975,91811,91816,108875,108888,137671,137692,150843,150899,183160,949559,274325,274353,309488,309494,333434,333487,355857,355865,360212,360238,387455,387475,391524,391537,411113,411133,427337,427366,448014,448077,468075,468086,506330,506374,507957,514326,548755,548775,574722,574777,595666,615255,652833,670466,670495,686381,694435,694497,706670,736452,736464,761612,761685,777350,789575,827366,863641,863699,886725,886755,898866,915937,915953,949476);


INSERT INTO `user_per_scheme` ( `active`, `is_deleted`, `head_office_user`, `is_default`, `user_type`, `district_id`, `division_id`, `role_id`, 
`scheme_id`, `union_id`, `upazilla_id`, `bgmea_factory_id`, `bkmea_factory_id`, `user_id`, `creation_date`) 

SELECT  b'0', b'0', b'0', b'0', 2, u.district_id, u.division_id, 13 roleId, 3 scheme_id, NULL, u.upazilla_id, NULL bgmea_factory_id, NULL bkmea_factory_id, u.id, SYSDATE() creation_date FROM user u 
WHERE u.id>6766;







-------Muni User

--get Already which upazila has user 
INSERT ignore INTO user
(`active`, `designation`, `email`, `full_name_bn`, `full_name_en`, `mobile_no`, `password`, `user_id`, `profile_pic`, `salt`, `head_office_user`, 
`district_id`, `division_id`, `ministry_id`, `role_id`, `union_id`, `upazilla_id`, `is_deleted`, `user_type`, `status`, `creation_date`, `modification_date`, `created_by`, `modified_by`)
 
SELECT 1,'ইউনিয়ন কর্মকর্তা',CONCAT('misc.',LOWER(u.name_in_english),'@gmail.com') email,
u.name_in_bangla, CONCAT('misc.',REPLACE( LOWER(up.name_in_english),' ',''),'.', REPLACE( LOWER(u.name_in_english),' ','')) name_in_English,'' mobile,
'3a8f99e9e744de26c8e75270a9016a499c3a1dfad9749854e6e5573718a3b8081591b33542bd73f80094d54426200d372bbc27886d6e5afde3d1032ab9054a89' password,
 CONCAT('misc.',REPLACE( LOWER(up.name_in_english),' ',''),'.', REPLACE( LOWER(u.name_in_english),' ','')) userName,'' profile_pic,
 '7QP59L/vmZoZKAVkiGnYUwyN2+8=' salt,b'0' head_office_user ,up.district_id DistrictId, di.id divisionId, 8 ministry_id, 13 role_id,u.id union_id,
 up.id upazilla_id,0 is_deleted,2 user_type,2 status,SYSDATE() creation_date,NULL,1,null

FROM unions u
 JOIN upazila up ON up.id = u.upazilla_id
 JOIN district d ON d.id = up.district_id
 JOIN division di ON di.id = d.division_id
WHERE u.id IN (15854,16025,35139,44768,48568,60766,61045,63277,69475,92187,92558,92979,95468,100688,102733,103377,103398,104077,105482,106785,108177,109492,109592,120296,120487,126383,128563,134575,134721,134982,135883,137974,139542,151233,151863,151896,153365,153392,153788,155345,155375,156135,157057,157477,157849,158266,158648,180778,185554,190985,192792,193192,193682,194085,195492,197298,198785,221688,224943,229066,261499,261891,267292,271088,271222,271242,271743,273845,274753,277773,290354,291069,291851,295653,296269,301492,302576,305188,323092,329157,333037,333046,333210,333287,338688,355156,359111,359156,362634,367122,367727,381325,381333,385888,386188,390769,392972,395897,396152,396196,398556,410474,410974,412374,413874,416174,419074,441435,443345,444248,447185,467073,471745,476463,480222,480675,481189,482775,484272,484570,485473,487974,496143,499427,501573,507136,509485,515870,516592,517364,527092,544058,548059,568267,581456,585648,586581,588336,611398,612098,612270,612345,612445,613169,616556,617252,618192,619451,642865,647592,655285,670290,670292,676845,676894,685282,686483,687685,690992,691552,691585,694145,699145,703775,705697,708852,721863,724795,725635,731587,733645,738578,750783,751092,752158,753673,754788,758077,758399,758710,760533,761998,762255,763392,763999,767285,768334,772535,773424,783868,785782,786648,786684,791476,795875,798756,811041,811053,811258,811292,812592,813144,815347,817252,817256,818256,819455,819485,822967,840785,850378,854292,857686,861452,862554,866552,869453,874326,881192,885085,886158,888935,889485,896763,897045,899085,902343,902927,904728,911776,913854,919473,930961,931962,932560,932879,933878,934710,934777,934778,935776,936664,938565,939576,948221,948634,949506,949538,949567,949574,949575);


INSERT INTO `user_per_scheme` ( `active`, `is_deleted`, `head_office_user`, `is_default`, `user_type`, `district_id`, `division_id`, `role_id`, 
`scheme_id`, `union_id`, `upazilla_id`, `bgmea_factory_id`, `bkmea_factory_id`, `user_id`, `creation_date`) 

SELECT  b'0', b'0', b'0', b'0', 2, u.district_id, u.division_id, 11 roleId, 3 scheme_id, NULL, u.upazilla_id, NULL bgmea_factory_id,
 NULL bkmea_factory_id, u.id, SYSDATE() creation_date FROM user u 
WHERE u.id>5856;
