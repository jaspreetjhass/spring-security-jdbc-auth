INSERT INTO users (username, password, enabled)
  values ('admin',
    '$2a$10$IuAwkg2HUi.NIURRqO7HJ.dEXHKFylMu3YNWgsM.cC8R0KjoxEHqi',
    1);

INSERT INTO authorities (username, authority)
  values ('admin', 'ROLE_user');
  
INSERT INTO authorities (username, authority)
  values ('admin', 'ROLE_admin');