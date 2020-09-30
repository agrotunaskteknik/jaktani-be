CREATE TABLE USERS (
                       id INT PRIMARY KEY,
                       full_name VARCHAR (50) NOT NULL,
                       username VARCHAR (50) NOT NULL,
                       type INT NOT NULL,
                       password VARCHAR (50) NOT NULL,
                       email VARCHAR ( 255 ) NOT NULL,
                       created_by INT NOT NULL,
                       updated_by INT,
                       created_time TIMESTAMP NOT NULL,
                       updated_time TIMESTAMP,
                       status INT NOT NULL,
                       gender INT,
                       mobile_phone_number VARCHAR(15),
                       birth_date DATE
);

CREATE TABLE TOKEN (
                       id INT PRIMARY KEY,
                       user_id INT NOT NULL,
                       token VARCHAR (255),
                       last_request TIMESTAMP,
                       created_time TIMESTAMP NOT NULL,
                       device_id VARCHAR (50),
                       refresh_token VARCHAR (100)
);

CREATE TABLE REQUEST_OTP (
                             id INT PRIMARY KEY,
                             user_id INT NOT NULL,
                             device_id VARCHAR (50),
                             created_time TIMESTAMP NOT NULL,
                             request_time TIMESTAMP,
                             otp_code VARCHAR (10)
);

CREATE TABLE SHOP (
                      id INT PRIMARY KEY,
                      user_id INT NOT NULL,
                      name VARCHAR (50),
                      created_by INT NOT NULL,
                      updated_by INT,
                      created_time TIMESTAMP NOT NULL,
                      updated_time TIMESTAMP,
                      status INT NOT NULL,
                      description VARCHAR (255)
);

CREATE TABLE CONFIGURATION (
                               id INT PRIMARY KEY,
                               string_value VARCHAR(255),
                               integer_value INT,
                               decimal_value DECIMAL(10,3),
                               created_time TIMESTAMP NOT NULL,
                               description VARCHAR (255),
                               type INT
);

CREATE TABLE DOCUMENT(
                         id INT PRIMARY KEY,
                         user_id INT NOT NULL,
                         name VARCHAR (200) NOT NULL,
                         type VARCHAR (15),
                         format VARCHAR (10),
                         content_data TEXT,
                         size VARCHAR (15),
                         created_by INT NOT NULL,
                         updated_by INT,
                         created_time TIMESTAMP NOT NULL,
                         updated_time TIMESTAMP,
                         status INT NOT NULL
);

CREATE TABLE ADDRESS(
                        id INT PRIMARY KEY,
                        type INT,
                        relation_id INT,
                        description VARCHAR (255),
                        city VARCHAR (150),
                        province VARCHAR (150),
                        country VARCHAR (100),
                        district VARCHAR (150),
                        pos_code VARCHAR (10),
                        village VARCHAR (150),
                        created_by INT NOT NULL,
                        updated_by INT,
                        created_time TIMESTAMP NOT NULL,
                        updated_time TIMESTAMP,
                        status INT NOT NULL
);
