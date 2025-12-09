CREATE DATABASE product_management_db;
CREATE ROLE product_manager_user LOGIN ENCRYPTED PASSWORD '123456' NOSUPERUSER NOCREATEDB NOCREATEROLE NOINHERIT;
CREATE SCHEMA product_management_app AUTHORIZATION product_manager_user;
GRANT USAGE ON SCHEMA product_management_app TO product_manager_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA product_management_app TO product_manager_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA product_management_app GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO product_manager_user;