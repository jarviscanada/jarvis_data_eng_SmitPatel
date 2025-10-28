# Linux Cluster Monitoring Agent

## Introduction

This project implements a Linux Cluster Monitoring Agent that helps collect and monitor hardware and resource usage data from Linux servers. The main goal is to allow system administrators and DevOps engineers to track CPU, memory, and disk performance in real-time. The system is designed to scale across multiple servers in a distributed environment. It uses Bash scripts to gather server information, Docker to run a PostgreSQL database, and Git for version control. The primary users are IT teams responsible for server maintenance and performance monitoring.

## Quick Start

```bash
# Start PostgreSQL instance using Docker
./scripts/psql_docker.sh create [db_username] [db_password]

# Create tables in the database
psql -h localhost -U [db_username] -d host_agent -f sql/ddl.sql

# Insert hardware specifications data into the database
./scripts/host_info.sh localhost 5432 host_agent [db_username] [db_password]

# Insert real-time hardware usage data
./scripts/host_usage.sh localhost 5432 host_agent [db_username] [db_password]

# Setup crontab to automate data collection
crontab -e
* * * * * bash /full/path/to/scripts/host_usage.sh localhost 5432 host_agent [db_username] [db_password] > /tmp/host_usage.log
```

## Implementation

The project collects server hardware information and real-time resource usage metrics and stores them in a PostgreSQL database running inside Docker. Bash scripts are used to extract data from system files and Linux commands. Crontab automates the periodic collection of metrics. All development and version control are handled using Git.

### Architecture

Create a cluster diagram with three Linux hosts, a PostgreSQL DB, and agents. Save the diagram in the `assets` directory.

### Scripts

* **psql_docker.sh**

```bash
# Create, start, or stop PostgreSQL Docker container
./scripts/psql_docker.sh create [db_username] [db_password]
./scripts/psql_docker.sh start
./scripts/psql_docker.sh stop
```

* **host_info.sh**

```bash
# Collect hardware specifications of the host
./scripts/host_info.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password]
```

* **host_usage.sh**

```bash
# Collect real-time server usage data
./scripts/host_usage.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password]
```

* **crontab**

```bash
# Automate periodic execution of host_usage.sh
crontab -e
* * * * * bash /full/path/to/scripts/host_usage.sh localhost 5432 host_agent [db_username] [db_password] > /tmp/host_usage.log
```

* **queries.sql**
  Purpose: To analyze resource utilization trends, identify servers with high CPU or memory usage, and monitor disk performance across the cluster.

### Database Modeling

#### host_info

| Column Name      | Data Type | Description            |
| ---------------- | --------- | ---------------------- |
| id               | SERIAL    | Unique host identifier |
| hostname         | VARCHAR   | Host machine name      |
| cpu_number       | INT2      | Number of CPU cores    |
| cpu_architecture | VARCHAR   | CPU architecture type  |
| cpu_model        | VARCHAR   | CPU model name         |
| cpu_mhz          | FLOAT8    | CPU clock speed in MHz |
| l2_cache         | INT4      | L2 cache size in KB    |
| total_mem        | INT4      | Total memory in KB     |
| timestamp        | TIMESTAMP | Record timestamp       |

#### host_usage

| Column Name    | Data Type | Description                          |
| -------------- | --------- | ------------------------------------ |
| timestamp      | TIMESTAMP | Time of data collection              |
| host_id        | SERIAL    | Foreign key referencing host_info.id |
| memory_free    | INT4      | Free memory in MB                    |
| cpu_idle       | INT2      | CPU idle percentage                  |
| cpu_kernel     | INT2      | CPU kernel usage percentage          |
| disk_io        | INT4      | Disk I/O operations                  |
| disk_available | INT4      | Available disk space in MB           |

## Test

Tested the Bash scripts and ddl.sql by creating and dropping tables, then inserting sample data. Verified data correctness using SELECT queries. Result: All scripts worked correctly and data were inserted reliably.

## Deployment

The project files are managed via GitHub. The PostgreSQL database runs in a Docker container, and crontab automates data collection for server metrics.

## Improvements

* Handle dynamic hardware updates automatically.
* Integrate a visual dashboard for real-time monitoring.
* Add alerting for resource anomalies and failures.
* Enable automated backups of the PostgreSQL database.
