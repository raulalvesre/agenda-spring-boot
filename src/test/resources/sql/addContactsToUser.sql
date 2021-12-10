SET TIMEZONE TO 'UTC';

INSERT INTO public.contacts (id, created_date, last_modified_date, "name", owner_id)
VALUES (3, NOW(), NULL, 'contact3', 2);
	
INSERT INTO public.contacts_telephones (id, created_date, last_modified_date, ddd, description, telephone_formatted, telephone_only_numbers, type_id, contact_id)
VALUES(5, NOW(), NULL, 11, 'tel5', '(11) 97645-4635', '976454635', 1, 3);
	
INSERT INTO public.contacts_telephones (id, created_date, last_modified_date, ddd, description, telephone_formatted, telephone_only_numbers, type_id, contact_id)
VALUES(6, NOW(), NULL, 11, 'tel6', '(11) 97645-4636', '976454636', 1, 3);

INSERT INTO public.contacts (id, created_date, last_modified_date, "name", owner_id)
VALUES (4, NOW(), NULL, 'contact4', 2);
	
INSERT INTO public.contacts_telephones (id, created_date, last_modified_date, ddd, description, telephone_formatted, telephone_only_numbers, type_id, contact_id)
VALUES(7, NOW(), NULL, 11, 'tel7', '(11) 97645-4637', '976454637', 1, 4);
	
INSERT INTO public.contacts_telephones (id, created_date, last_modified_date, ddd, description, telephone_formatted, telephone_only_numbers, type_id, contact_id)
VALUES(8, NOW(), NULL, 11, 'tel8', '(11) 97645-4638', '976454638', 1, 4);
