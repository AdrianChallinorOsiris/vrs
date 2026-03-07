
-- =========================
-- Populate core data from IMO Compendium
-- =========================
INSERT INTO  vrs.meta_data_element (data_number, name, definition, data_format, code_list, business_rule) 
select "Data Number", "Data Element", "Definition","Format",  "Code lists", "Business rules"
from public.imo_compendium 
;

INSERT INTO vrs.meta_data_class (name, meta_data_source_id) VALUES
('General Declaration', 1), 
('Cargo Declaration', 1), 
('Ships Stores Declaration', 1), 
('Crew’s Effects Declaration', 1), 
('Crew List', 1), 
('Passenger List', 1), 
('Dangerous Goods Manifest', 1), 
('Security Report', 1), 
('Advance Notification Waste Delivery', 1), 
('Maritime Declaration Health', 1), 
('Just In Time Arrival', 1), 
('Stowaways', 1), 
('Ship and Company Certificates', 1), 
('Acknowledgement Receipt', 1), 
('Maritime Service', 1), 
('Ship Registry  Comp. Details', 1), 
('Inspections', 1), 
('PSC Inspection History Data', 1), 
('Ship Reporting Systems', 1), 
('Ballast Water Report', 1), 
('Waste Delivery Receipt', 1), 
('Advance Passenger Information', 1), 
('Verified Gross Mass', 1), 
('Noon Data Report', 1), 
('Electronic Bunker Delivery Note', 1), 
('Container Inspection Programme', 1), 
('Transport of Dangerous Goods', 1), 
('Electronic Bill of Lading', 1), 
('Consumption', 1)
;

