/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Shamiul Islam at Anunad Solution
 * Created: Apr 4, 2023
 */

SELECT 
REPLACE(
REPLACE(
REPLACE(
REPLACE(
REPLACE(
REPLACE(
REPLACE(
REPLACE(
REPLACE(
REPLACE(a.permanent_ward_no,'১',1),'২','2'),'৩','3'),'৪','4'),'৫','5'),'৬','6'),'৭','7'),'৮','8'),'৯','9'),'০','0') ward


 FROM applicant a 
WHERE 
a.present_ward_no LIKE '%১%'OR
a.present_ward_no LIKE '%২%'OR 
a.present_ward_no LIKE '%৩%'OR 
a.present_ward_no LIKE '%৪%'OR 
a.present_ward_no LIKE '%৫%'OR 
a.present_ward_no LIKE '%৬%'OR 
a.present_ward_no LIKE '%৭%'OR 
a.present_ward_no LIKE '%৮%'OR 
a.present_ward_no LIKE '%৯%'OR 
a.present_ward_no LIKE '%০%'