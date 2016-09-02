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
	id uuid,
	component_type text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	local_correlation_id text,
	global_correlation_id text,
	sequence_id text,
	msg_status text,
	secured boolean,
	msg_size_bytes int,
	msg_context map<text, text>,
	payload_id uuid,
	occur_time timestamp,
	PRIMARY KEY ((interval), occur_time, id)
)
WITH CLUSTERING ORDER BY (occur_time DESC, id ASC);

// Q2. Consulta de eventos por el interfaz norte en un rango de tiempo y para un usuario.
CREATE TABLE IF NOT EXISTS audit.north_messages_by_user_and_interval (
	interval bigint,
	id uuid,
	component_type text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	local_correlation_id text,
	global_correlation_id text,
	sequence_id text,
	msg_status text,
	secured boolean,
	msg_size_bytes int,
	msg_context map<text, text>,
	payload_id uuid,
	occur_time timestamp,
	PRIMARY KEY ((interval, user), occur_time, id)
)
WITH CLUSTERING ORDER BY (occur_time DESC, id ASC);

// Q3. Consulta de eventos por el interfaz norte en un rango de tiempo para un usuario y para un subject.
CREATE TABLE IF NOT EXISTS audit.north_messages_by_user_subject_and_interval (
	interval bigint,
	id uuid,
	component_type text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	local_correlation_id text,
	global_correlation_id text,
	sequence_id text,
	msg_status text,
	secured boolean,
	msg_size_bytes int,
	msg_context map<text, text>,
	payload_id uuid,
	occur_time timestamp,
	PRIMARY KEY ((interval, user, subject), occur_time, id)
)
WITH CLUSTERING ORDER BY (occur_time DESC, id ASC);


/**
 * SOUTH API MESSAGES TABLES
 */

// Q4. Consulta de eventos por los interfaces sur en un rango de tiempo.
CREATE TABLE IF NOT EXISTS audit.south_messages_by_interval (
	interval bigint,
	id uuid,
	component_type text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	local_correlation_id text,
	global_correlation_id text,
	sequence_id text,
	msg_status text,
	secured boolean,
	msg_size_bytes int,
	msg_context map<text, text>,
	payload_id uuid,
	occur_time timestamp,
	PRIMARY KEY ((interval), occur_time, id)
)
WITH CLUSTERING ORDER BY (occur_time DESC, id ASC);

// Q5. Consulta de eventos por los interfaces sur en un rango de tiempo y para un subject
CREATE TABLE IF NOT EXISTS audit.south_messages_by_subject_and_interval (
	interval bigint,
	id uuid,
	component_type text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	local_correlation_id text,
	global_correlation_id text,
	sequence_id text,
	msg_status text,
	secured boolean,
	msg_size_bytes int,
	msg_context map<text, text>,
	payload_id uuid,
	occur_time timestamp,
	PRIMARY KEY ((interval, subject), occur_time, id)
)
WITH CLUSTERING ORDER BY (occur_time DESC, id ASC);

// Q6. Consulta de eventos por los interfaces sur en un rango de tiempo y para un subject y usuario.
CREATE TABLE IF NOT EXISTS audit.south_messages_by_subject_user_and_interval (
	interval bigint,
	id uuid,
	component_type text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	local_correlation_id text,
	global_correlation_id text,
	sequence_id text,
	msg_status text,
	secured boolean,
	msg_size_bytes int,
	msg_context map<text, text>,
	payload_id uuid,
	occur_time timestamp,
	PRIMARY KEY ((interval, subject, user), occur_time, id)
)
WITH CLUSTERING ORDER BY (occur_time DESC, id ASC);

/**
 * ALARM MESSAGES TABLES
 */

// Q7. Consulta de eventos relacionados con alarmas en un rango de tiempo.
CREATE TABLE IF NOT EXISTS audit.alarm_messages_by_interval (
	interval bigint,
	id uuid,
	component_type text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	local_correlation_id text,
	global_correlation_id text,
	sequence_id text,
	msg_status text,
	secured boolean,
	msg_size_bytes int,
	msg_context map<text, text>,
	payload_id uuid,
	occur_time timestamp,
	PRIMARY KEY ((interval), occur_time, id)
)
WITH CLUSTERING ORDER BY (occur_time DESC, id ASC);

// Q8. Consulta de eventos relacionados con alarmas en un rango de tiempo y para un subject
CREATE TABLE IF NOT EXISTS audit.alarm_messages_by_subject_and_interval (
	interval bigint,
	id uuid,
	component_type text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	local_correlation_id text,
	global_correlation_id text,
	sequence_id text,
	msg_status text,
	secured boolean,
	msg_size_bytes int,
	msg_context map<text, text>,
	payload_id uuid,
	occur_time timestamp,
	PRIMARY KEY ((interval, subject), occur_time, id)
)
WITH CLUSTERING ORDER BY (occur_time DESC, id ASC);

// Q9. Consulta de eventos relacionados con alarmas en un rango de tiempo y para un subject y usuario.
CREATE TABLE IF NOT EXISTS audit.alarm_messages_by_subject_user_and_interval (
	interval bigint,
	id uuid,
	component_type text,
	msg_name text,
	msg_type text,
	msg_direction text,
	subject text,
	subject_type text,
	user text,
	local_correlation_id text,
	global_correlation_id text,
	sequence_id text,
	msg_status text,
	secured boolean,
	msg_size_bytes int,
	msg_context map<text, text>,
	payload_id uuid,
	occur_time timestamp,
	PRIMARY KEY ((interval, subject, user), occur_time, id)
)
WITH CLUSTERING ORDER BY (occur_time DESC, id ASC);

/**
 * PAYLOAD TABLE
 */

//Q10. Consulta de payloads por id
CREATE TABLE IF NOT EXISTS audit.payloads_by_id (
	id uuid,
	msg_payload list<text>,
	PRIMARY KEY ((id))
);