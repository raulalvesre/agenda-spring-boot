SET TIMEZONE TO 'UTC';

INSERT INTO public.contacts (id, created_date, last_modified_date, "name", owner_id)
VALUES (1, NOW(), NULL, 'contact1', 1);

INSERT INTO public.contacts_telephones (id, created_date, last_modified_date, ddd, description, telephone_formatted, telephone_only_numbers, type_id, contact_id)
VALUES(1, NOW(), NULL, 11, 'tel1', '(11) 97645-4631', '976454631', 1, 1);

INSERT INTO public.contacts_telephones (id, created_date, last_modified_date, ddd, description, telephone_formatted, telephone_only_numbers, type_id, contact_id)
VALUES(2, NOW(), NULL, 11, 'tel2', '(11) 97645-4632', '976454632', 1, 1);

INSERT INTO public.contacts (id, created_date, last_modified_date, "name", owner_id)
VALUES (2, NOW(), NULL, 'contact2', 1);

INSERT INTO public.contacts_telephones (id, created_date, last_modified_date, ddd, description, telephone_formatted, telephone_only_numbers, type_id, contact_id)
VALUES(3, NOW(), NULL, 11, 'tel3', '(11) 97645-4633', '976454633', 1, 2);

INSERT INTO public.contacts_telephones (id, created_date, last_modified_date, ddd, description, telephone_formatted, telephone_only_numbers, type_id, contact_id)
VALUES(4, NOW(), NULL, 11, 'tel4', '(11) 97645-4634', '976454634', 1, 2);
