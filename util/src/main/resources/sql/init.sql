DROP TABLE IF EXISTS task CASCADE;
DROP TABLE IF EXISTS contact CASCADE;
DROP TABLE IF EXISTS client CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE client (
    id BIGSERIAL PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    industry VARCHAR(255),
    address VARCHAR(255)
);

CREATE TABLE contact (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(255),
    client_id BIGINT,
    
    CONSTRAINT fk_client
        FOREIGN KEY(client_id) 
        REFERENCES client(id)
        ON DELETE CASCADE
);

CREATE TABLE task (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    due_date DATE,
    contact_id BIGINT,
    
    CONSTRAINT fk_contact
        FOREIGN KEY(contact_id) 
        REFERENCES contact(id)
        ON DELETE SET NULL
);


INSERT INTO users (email, password) VALUES
-- password - 'string'
('user@example.com', '$2a$12$NlbaEsPhNkjOlo1ERtPZQuXmS404TmXGVYC2dV4o4H7muupakbQUO'),
('user2@example.com', '$2a$12$NlbaEsPhNkjOlo1ERtPZQuXmS404TmXGVYC2dV4o4H7muupakbQUO');

INSERT INTO client (company_name, industry, address) VALUES
('Quantum Dynamics', 'Fintech', '1 Financial Square, New York, NY'),
('Stellar Space Systems', 'Aerospace', '300 Rocket Road, Houston, TX'),
('Apex Health AI', 'Healthcare Tech', '50 Medical Plaza, Boston, MA'),
('Nomad Travel Co.', 'Tourism', '17 Journey Lane, Miami, FL'),
('Cygnus Security', 'Cybersecurity', '99 Secure Block, Austin, TX'),
('HarvestMoon Farms', 'Agriculture', '25 Valley Fields, Sacramento, CA');

INSERT INTO contact (first_name, last_name, email, phone, client_id) VALUES
('Michael', 'Lee', 'm.lee@quantum.fi', '+12125550101', 1),
('Sarah', 'Chen', 's.chen@quantum.fi', '+12125550102', 1),
('David', 'Brown', 'david.brown@stellar.sp', '+17135550103', 2),
('Dr. Emily', 'White', 'e.white@apex.ai', '+16175550104', 3),
('James', 'Wilson', 'j.wilson@apex.ai', '+16175550105', 3),
('Jessica', 'Miller', 'jessica@nomad.tr', '+13055550106', 4),
('Alex', 'Johnson', 'alex.j@nomad.tr', '+13055550107', 4),
('Chris', 'Kent', 'c.kent@cygnus.sec', '+15125550108', 5),
('Patricia', 'Hall', 'p.hall@harvest.ag', '+19165550109', 6),
('Daniel', 'Young', 'd.young@harvest.ag', '+19165550110', 6);

INSERT INTO task (description, status, due_date, contact_id) VALUES
('Develop risk assessment algorithm', 'IN_PROGRESS', '2025-11-28', 1),
('Finalize Q4 compliance report', 'OPEN', '2025-11-20', 2),
('Audit transaction ledger', 'OPEN', '2025-11-22', 1),
('Review new engine schematics', 'COMPLETED', '2025-11-14', 3),
('Schedule launch window simulation', 'IN_PROGRESS', '2025-11-21', 3),
('Train neural network on new patient data', 'IN_PROGRESS', '2025-12-01', 4),
('Prepare FDA approval documents', 'OPEN', '2025-11-30', 4),
('Validate diagnostic accuracy', 'OPEN', '2025-12-05', 5),
('Launch holiday booking campaign', 'COMPLETED', '2025-11-15', 6),
('Update mobile app for new destinations', 'OPEN', '2025-11-24', 7),
('Conduct penetration test for Client X', 'IN_PROGRESS', '2025-11-19', 8),
('Deploy firewall patch v2.1', 'OPEN', '2025-11-17', 8),
('Analyze soil sample data', 'COMPLETED', '2025-11-12', 9),
('Optimize drone crop-dusting routes', 'IN_PROGRESS', '2025-11-25', 10),
('Forecast Q1 yield', 'OPEN', '2025-12-01', 9);