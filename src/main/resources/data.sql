SET TIMEZONE TO 'UTC';

INSERT INTO roles (name) VALUES ('ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('USER') ON CONFLICT DO NOTHING;

INSERT INTO telephone_types (name) VALUES ('CELLPHONE') ON CONFLICT DO NOTHING;
INSERT INTO telephone_types (name) VALUES ('LANDLINE') ON CONFLICT DO NOTHING;
INSERT INTO telephone_types (name) VALUES ('COMMERCIAL') ON CONFLICT DO NOTHING;

INSERT INTO public.users (created_date, last_modified_date, email, enabled, "name", "password", username, role_id)
VALUES(NOW(), NULL, 'raul.alves.re@gmail.com', true, 'rar', '$2a$10$2iYO08F33hcy8Cd.jkNdruGrQ74xYDIO6BfOxhoPur3Zg6deMI4Dq', 'rar', 1);

INSERT INTO public.users (created_date, last_modified_date, email, enabled, "name", "password", username, role_id)
VALUES(NOW(), NULL, 'rara@gmail.com', true, 'rar', '$2a$10$OUhx5lJJBtcq1ffsxBibnOvqOdZUHx/k0eyqg0q6Nk9OvtiFl8z6.', 'raar', 2);
