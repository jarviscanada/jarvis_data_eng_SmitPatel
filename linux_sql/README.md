````markdown
# Linux Cluster Monitoring Agent

## Introduction
This project is designed to monitor and log hardware and system usage metrics for Linux servers in a cluster. It helps system administrators and DevOps teams track CPU, memory, and disk performance over time. The system is built using Bash scripts for data collection, PostgreSQL as the backend database, Docker to host the database, and Git for version control. The main users are administrators who want a simple and scalable solution to monitor multiple servers and analyze performance trends.

## Quick Start
```bash
# Start a PostgreSQL Docker container
./scripts/psql_docker.sh create [db_username] [db_password]

# Create tables using ddl.sql
psql -h localhost -U [db_username] -d host_agent -f sql/ddl.sql

# Insert hardware specifications into the DB
./scripts/host_info.sh localhost 5432 host_agent [db_username] [db_password]

# Insert real-time hardware usage data into the DB
./scripts/host_usage.sh localhost 5432 host_agent [db_username] [db_password]

# Set up crontab for periodic data collection
crontab -e
* * * * * bash /full/path/to/scripts/host_usage.sh localhost 5432 host_agent [db_username] [db_password] > /tmp/host_usage.log
````

## Implementation

The project collects static hardware information once per host and dynamic usage metrics at periodic intervals. The scripts insert collected data into a PostgreSQL database running inside Docker, which allows for easy deployment and scaling.

### Architecture

Create a cluster diagram with three Linux hosts running monitoring agents, all reporting to a central PostgreSQL database. Save the diagram as `assets/cluster_diagram.png`.

### Scripts

* **psql_docker.sh**: Creates, starts, or stops the PostgreSQL Docker container.

```bash
./scripts/psql_docker.sh create [db_username] [db_password]
./scripts/psql_docker.sh start
./scripts/psql_docker.sh stop
```

* **host_info.sh**: Collects hardware specifications and inserts into the `host_info` table.

```bash
./scripts/host_info.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password]
```

* **host_usage.sh**: Collects dynamic resource usage metrics and inserts into `host_usage` table.

```bash
./scripts/host_usage.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password]
```

* **crontab**: Automates periodic execution of `host_usage.sh`.
* **queries.sql**: Contains example analytical queries to monitor trends in CPU, memory, and disk usage across hosts.

### Database Modeling

#### host_info

| Column Name      | Data Type | Description                     |
| ---------------- | --------- | ------------------------------- |
| id               | SERIAL    | Unique identifier for each host |
| hostname         | VARCHAR   | Name of the host machine        |
| cpu_number       | INT2      | Number of CPU cores             |
| cpu_architecture | VARCHAR   | CPU architecture                |
| cpu_model        | VARCHAR   | CPU model name                  |
| cpu_mhz          | FLOAT8    | CPU clock speed                 |
| l2_cache         | INT4      | L2 cache size in KB             |
| total_mem        | INT4      | Total memory in KB              |
| timestamp        | TIMESTAMP | Data collection time            |

#### host_usage

| Column Name    | Data Type | Description                 |
| -------------- | --------- | --------------------------- |
| timestamp      | TIMESTAMP | Time of metric collection   |
| host_id        | SERIAL    | Foreign key to host_info.id |
| memory_free    | INT4      | Free memory in MB           |
| cpu_idle       | INT2      | CPU idle percentage         |
| cpu_kernel     | INT2      | CPU kernel usage percentage |
| disk_io        | INT4      | Disk I/O count              |
| disk_available | INT4      | Available disk space in MB  |

## Test

Testing was done by creating and dropping tables via `ddl.sql` and verifying correct insertion of hardware and usage data using the scripts. The scripts successfully collected and stored both static and dynamic data.

## Deployment

All project files are managed via GitHub. PostgreSQL runs in a Docker container. Crontab automates periodic execution of `host_usage.sh` for continuous monitoring.

## Improvements

* Automatically update hardware data if server configuration changes.
* Integrate a monitoring dashboard for visualization of metrics.
* Add alerting for abnormal resource usage.

```
```

