CREATE KEYSPACE IF NOT EXISTS audit 
WITH replication = {
	'class':'SimpleStrategy', 
	'replication_factor':1
};

/**
 * NORTH API MESSAGES TABLES
 */

 // Q1. Consulta de eventos por el interfaz norte en un rango de tiempo.
CREATE TABLE IF NOT EXISTS audit.north_messages_by_interval (
	interval bigint,
	audit_id timeuuid,
	component text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	transaction_id text,
	sequence_id text,
	msg_status text,
	msg_size_bytes int,
	msg_context map<text, text>,
	occur_time timestamp,
	PRIMARY KEY ((interval), audit_id)
)
WITH CLUSTERING ORDER BY (audit_id DESC);

// Q2. Consulta de eventos por el interfaz norte en un rango de tiempo y para un usuario.
CREATE TABLE IF NOT EXISTS audit.north_messages_by_user_and_interval (
	interval bigint,
	audit_id timeuuid,
	component text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	transaction_id text,
	sequence_id text,
	msg_status text,
	msg_size_bytes int,
	msg_context map<text, text>,
	occur_time timestamp,
	PRIMARY KEY ((interval, user),audit_id)
)
WITH CLUSTERING ORDER BY (audit_id DESC);

// Q3. Consulta de eventos por el interfaz norte en un rango de tiempo para un usuario y para un subject.
CREATE TABLE IF NOT EXISTS audit.north_messages_by_user_subject_and_interval (
	interval bigint,
	audit_id timeuuid,
	component text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	transaction_id text,
	sequence_id text,
	msg_status text,
	msg_size_bytes int,
	msg_context map<text, text>,
	occur_time timestamp,
	PRIMARY KEY ((interval, user, subject), audit_id)
)
WITH CLUSTERING ORDER BY (audit_id DESC);


/**
 * SOUTH API MESSAGES TABLES
 */

// Q4. Consulta de eventos por los interfaces sur en un rango de tiempo.
CREATE TABLE IF NOT EXISTS audit.south_messages_by_interval (
	interval bigint,
	audit_id timeuuid,
	component text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	transaction_id text,
	sequence_id text,
	msg_status text,
	msg_size_bytes int,
	msg_context map<text, text>,
	occur_time timestamp,
	PRIMARY KEY ((interval), audit_id)
)
WITH CLUSTERING ORDER BY (audit_id DESC);

// Q5. Consulta de eventos por los interfaces sur en un rango de tiempo y para un subject
CREATE TABLE IF NOT EXISTS audit.south_messages_by_subject_and_interval (
	interval bigint,
	audit_id timeuuid,
	component text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	transaction_id text,
	sequence_id text,
	msg_status text,
	msg_size_bytes int,
	msg_context map<text, text>,
	occur_time timestamp,
	PRIMARY KEY ((interval, subject), audit_id)
)
WITH CLUSTERING ORDER BY (audit_id DESC);

// Q6. Consulta de eventos por los interfaces sur en un rango de tiempo y para un subject y usuario.
CREATE TABLE IF NOT EXISTS audit.south_messages_by_subject_user_and_interval (
	interval bigint,
	audit_id timeuuid,
	component text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	transaction_id text,
	sequence_id text,
	msg_status text,
	msg_size_bytes int,
	msg_context map<text, text>,
	occur_time timestamp,
	PRIMARY KEY ((interval, subject, user), audit_id)
)
WITH CLUSTERING ORDER BY (audit_id DESC);

/**
 * ALARM MESSAGES TABLES
 */

// Q7. Consulta de eventos relacionados con alarmas en un rango de tiempo.
CREATE TABLE IF NOT EXISTS audit.alarm_messages_by_interval (
	interval bigint,
	audit_id timeuuid,
	component text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	transaction_id text,
	sequence_id text,
	msg_status text,
	msg_size_bytes int,
	msg_context map<text, text>,
	occur_time timestamp,
	PRIMARY KEY ((interval), audit_id)
)
WITH CLUSTERING ORDER BY (audit_id DESC);

// Q8. Consulta de eventos relacionados con alarmas en un rango de tiempo y para un subject
CREATE TABLE IF NOT EXISTS audit.alarm_messages_by_subject_and_interval (
	interval bigint,
	audit_id timeuuid,
	component text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	transaction_id text,
	sequence_id text,
	msg_status text,
	msg_size_bytes int,
	msg_context map<text, text>,
	occur_time timestamp,
	PRIMARY KEY ((interval, subject), audit_id)
)
WITH CLUSTERING ORDER BY (audit_id DESC);

// Q9. Consulta de eventos relacionados con alarmas en un rango de tiempo y para un subject y usuario.
CREATE TABLE IF NOT EXISTS audit.alarm_messages_by_subject_user_and_interval (
	interval bigint,
	audit_id timeuuid,
	component text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	transaction_id text,
	sequence_id text,
	msg_status text,
	msg_size_bytes int,
	msg_context map<text, text>,
	occur_time timestamp,
	PRIMARY KEY ((interval, subject, user), audit_id)
)
WITH CLUSTERING ORDER BY (audit_id DESC);