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
	msg_size_bytes int,
	msg_context map<text, text>,
	payload_id uuid,
	occur_time timestamp,
	PRIMARY KEY ((interval), user, occur_time, id)
)
WITH CLUSTERING ORDER BY (user ASC, occur_time DESC, id ASC);

// La PRIMARY KEY de la vista materializada tiene que contener todos los campos de la
// PRIMARY KEY de la tabla y opcionalmente un campo adicional más. Dado que las dos vistas
// materializadas serán:
//
// PRIMARY KEY ((user, interval), id)
// PRIMARY KEY ((user, subject, interval), id)
//
// Para dar soporte a la segunda PRIMARY KEY necesitamos que la PRIMARY KEY de la tabla
// incluya user o subject.
//
// Un inconveniente de usar vistas materializadas es que no podemos cambiar el valor del campo interval.

// Q2. Consulta de eventos por el interfaz norte en un rango de tiempo y para un usuario.
CREATE MATERIALIZED VIEW IF NOT EXISTS audit.north_messages_by_user_and_interval AS
SELECT * FROM audit.north_messages_by_interval
WHERE user IS NOT NULL AND interval IS NOT NULL and occur_time IS NOT NULL and id IS NOT NULL
PRIMARY KEY ((user, interval), occur_time, id)
WITH CLUSTERING ORDER BY (occur_time DESC, id ASC);

// Q3. Consulta de eventos por el interfaz norte en un rango de tiempo para un usuario y para un subject.
CREATE MATERIALIZED VIEW IF NOT EXISTS audit.north_messages_by_user_subject_and_interval AS
SELECT * FROM audit.north_messages_by_interval
WHERE user IS NOT NULL AND subject IS NOT NULL and interval IS NOT NULL and occur_time IS NOT NULL
and id IS NOT NULL
PRIMARY KEY ((user, subject, interval), occur_time, id)
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
	msg_size_bytes int,
	msg_context map<text, text>,
	payload_id uuid,
	occur_time timestamp,
	PRIMARY KEY ((interval), subject, occur_time, id)
)
WITH CLUSTERING ORDER BY (subject ASC, occur_time DESC, id ASC);

// Q5. Consulta de eventos por los interfaces sur en un rango de tiempo y para un subject
CREATE MATERIALIZED VIEW IF NOT EXISTS audit.south_messages_by_subject_and_interval AS
SELECT * FROM audit.south_messages_by_interval
WHERE subject IS NOT NULL AND interval IS NOT NULL and occur_time IS NOT NULL and id IS NOT NULL
PRIMARY KEY ((subject, interval), occur_time, id)
WITH CLUSTERING ORDER BY (occur_time DESC, id ASC);

// Q6. Consulta de eventos por los interfaces sur en un rango de tiempo y para un subject y usuario.
CREATE MATERIALIZED VIEW IF NOT EXISTS audit.south_messages_by_subject_user_and_interval AS
SELECT * FROM audit.south_messages_by_interval
WHERE subject IS NOT NULL AND user IS NOT NULL and interval IS NOT NULL and occur_time IS NOT NULL
and id IS NOT NULL
PRIMARY KEY ((subject, user, interval), occur_time, id)
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
	msg_size_bytes int,
	msg_context map<text, text>,
	payload_id uuid,
	occur_time timestamp,
	PRIMARY KEY ((interval), subject, occur_time, id)
)
WITH CLUSTERING ORDER BY (subject ASC, occur_time DESC, id ASC);

// Q8. Consulta de eventos relacionados con alarmas en un rango de tiempo y para un subject
CREATE MATERIALIZED VIEW IF NOT EXISTS audit.alarm_messages_by_subject_and_interval AS
SELECT * FROM audit.alarm_messages_by_interval
WHERE subject IS NOT NULL AND interval IS NOT NULL and occur_time IS NOT NULL and id IS NOT NULL
PRIMARY KEY ((subject, interval), occur_time, id)
WITH CLUSTERING ORDER BY (occur_time DESC, id ASC);

// Q9. Consulta de eventos relacionados con alarmas en un rango de tiempo y para un subject y usuario.
CREATE MATERIALIZED VIEW IF NOT EXISTS audit.alarm_messages_by_subject_user_and_interval AS
SELECT * FROM audit.alarm_messages_by_interval
WHERE subject IS NOT NULL AND user IS NOT NULL and interval IS NOT NULL and occur_time IS NOT NULL
and id IS NOT NULL
PRIMARY KEY ((subject, user, interval), occur_time, id)
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