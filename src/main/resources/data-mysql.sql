INSERT INTO users (email,username, password, enabled)
  values ('admin@gmail.com','admin',
    '$2a$10$IuAwkg2HUi.NIURRqO7HJ.dEXHKFylMu3YNWgsM.cC8R0KjoxEHqi',
    1);

INSERT INTO authorities (email, authority)
  values ('admin@gmail.com', 'ROLE_user');
  
INSERT INTO authorities (email, authority)
  values ('admin@gmail.com', 'ROLE_admin');