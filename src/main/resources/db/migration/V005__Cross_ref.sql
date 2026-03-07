
-- =========================
-- Populate Data elements cross references
-- =========================


INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'General Declaration'
and ic."General Declaration" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Cargo Declaration'
and ic."Cargo Declaration" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Ships Stores Declaration'
and ic."Ships Stores Declaration" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Crew’s Effects Declaration'
and ic."Crew’s Effects Declaration" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Crew List'
and ic."Crew List" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Passenger List'
and ic."Passenger List" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Dangerous Goods Manifest'
and ic."Dangerous Goods Manifest" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Security Report'
and ic."Security Report" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Advance Notification Waste Delivery'
and ic."Advance Notification Waste Delivery" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Maritime Declaration Health'
and ic."Maritime Declaration Health" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Just In Time Arrival'
and ic."Just In Time Arrival" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Stowaways'
and ic."Stowaways" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Ship and Company Certificates'
and ic."Ship and Company Certificates" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Acknowledgement Receipt'
and ic."Acknowledgement Receipt" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Maritime Service'
and ic."Maritime Service" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Ship Registry  Comp. Details'
and ic."Ship Registry  Comp. Details" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Inspections'
and ic."Inspections" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'PSC Inspection History Data'
and ic."PSC Inspection History Data" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Ship Reporting Systems'
and ic."Ship Reporting Systems" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Ballast Water Report'
and ic."Ballast Water Report" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Waste Delivery Receipt'
and ic."Waste Delivery Receipt" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Advance Passenger Information'
and ic."Advance Passenger Information" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Verified Gross Mass'
and ic."Verified Gross Mass" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Noon Data Report'
and ic."Noon Data Report" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Electronic Bunker Delivery Note'
and ic."Electronic Bunker Delivery Note" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Container Inspection Programme'
and ic."Container Inspection Programme" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Transport of Dangerous Goods'
and ic."Transport of Dangerous Goods" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Electronic Bill of Lading'
and ic."Electronic Bill of Lading" = 'X' ; 

INSERT INTO vrs.meta_data_cross_reference (meta_data_class_id, meta_data_element_id) 
SELECT mc.id, me.id
from vrs.meta_data_class mc, public.imo_compendium ic, vrs.meta_data_element me
where me.data_number = ic."Data Number" 
and mc.name = 'Consumption'
and ic."[Fuel Oil Consumption and CII Reporting]" = 'X' ; 