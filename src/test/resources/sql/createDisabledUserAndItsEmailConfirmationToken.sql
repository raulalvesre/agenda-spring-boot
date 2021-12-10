SET TIMEZONE TO 'UTC';

INSERT INTO public.users (id, created_date, last_modified_date, email, enabled, "name", "password", username, role_id)
VALUES(1, '2021-11-28 03:35:37.057', NULL, 'rara@gmail.com', false, 'rar', '$2a$10$OUhx5lJJBtcq1ffsxBibnOvqOdZUHx/k0eyqg0q6Nk9OvtiFl8z6.', 'raar', 2);

INSERT INTO public.confirm_email_token (token, owner_id ,created_date)
VALUES ('confirmemailtesttoken', 1,  NOW());