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
	PRIMARY KEY ((interval), user, audit_id)
)
WITH CLUSTERING ORDER BY (user ASC, audit_id DESC);

// La PRIMARY KEY de la vista materializada tiene que contener todos los campos de la
// PRIMARY KEY de la tabla y opcionalmente un campo adicional más. Dado que las dos vistas
// materializadas serán:
//
// PRIMARY KEY ((user, interval), audit_id)
// PRIMARY KEY ((user, subject, interval), audit_id)
//
// Para dar soporte a la segunda PRIMARY KEY necesitamos que la PRIMARY KEY de la tabla
// incluya user o subject.
//
// Un inconveniente de usar vistas materializadas es que no podemos cambiar el valor del campo interval.

// Q2. Consulta de eventos por el interfaz norte en un rango de tiempo y para un usuario.
CREATE MATERIALIZED VIEW IF NOT EXISTS audit.north_messages_by_user_and_interval AS
SELECT * FROM audit.north_messages_by_interval
WHERE user IS NOT NULL AND interval IS NOT NULL and audit_id IS NOT NULL
PRIMARY KEY ((user, interval), audit_id)
WITH CLUSTERING ORDER BY (audit_id DESC);

// Q3. Consulta de eventos por el interfaz norte en un rango de tiempo para un usuario y para un subject.
CREATE MATERIALIZED VIEW IF NOT EXISTS audit.north_messages_by_user_subject_and_interval AS
SELECT * FROM audit.north_messages_by_interval
WHERE user IS NOT NULL AND subject IS NOT NULL and interval IS NOT NULL and audit_id IS NOT NULL
PRIMARY KEY ((user, subject, interval), audit_id)
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
	PRIMARY KEY ((interval), subject, audit_id)
)
WITH CLUSTERING ORDER BY (subject ASC, audit_id DESC);

// Q5. Consulta de eventos por los interfaces sur en un rango de tiempo y para un subject
CREATE MATERIALIZED VIEW IF NOT EXISTS audit.south_messages_by_subject_and_interval AS
SELECT * FROM audit.south_messages_by_interval
WHERE subject IS NOT NULL AND interval IS NOT NULL and audit_id IS NOT NULL
PRIMARY KEY ((subject, interval), audit_id)
WITH CLUSTERING ORDER BY (audit_id DESC);

// Q6. Consulta de eventos por los interfaces sur en un rango de tiempo y para un subject y usuario.
CREATE MATERIALIZED VIEW IF NOT EXISTS audit.south_messages_by_subject_user_and_interval AS
SELECT * FROM audit.south_messages_by_interval
WHERE subject IS NOT NULL AND user IS NOT NULL and interval IS NOT NULL and audit_id IS NOT NULL
PRIMARY KEY ((subject, user, interval), audit_id)
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
	PRIMARY KEY ((interval), subject, audit_id)
)
WITH CLUSTERING ORDER BY (subject ASC, audit_id DESC);

// Q8. Consulta de eventos relacionados con alarmas en un rango de tiempo y para un subject
CREATE MATERIALIZED VIEW IF NOT EXISTS audit.alarm_messages_by_subject_and_interval AS
SELECT * FROM audit.alarm_messages_by_interval
WHERE subject IS NOT NULL AND interval IS NOT NULL and audit_id IS NOT NULL
PRIMARY KEY ((subject, interval), audit_id)
WITH CLUSTERING ORDER BY (audit_id DESC);

// Q9. Consulta de eventos relacionados con alarmas en un rango de tiempo y para un subject y usuario.
CREATE MATERIALIZED VIEW IF NOT EXISTS audit.alarm_messages_by_subject_user_and_interval AS
SELECT * FROM audit.alarm_messages_by_interval
WHERE subject IS NOT NULL AND user IS NOT NULL and interval IS NOT NULL and audit_id IS NOT NULL
PRIMARY KEY ((subject, user, interval), audit_id)
WITH CLUSTERING ORDER BY (audit_id DESC);