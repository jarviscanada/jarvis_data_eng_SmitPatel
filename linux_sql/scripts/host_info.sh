#!/bin/bash

# Assign input parameters to variables
host=$1
port=$2
database=$3
username=$4
password=$5

# Validating number of arguments are provided
if [[ $# -ne 5 ]]; then
  echo "Usage: $0 <host> <port> <database> <user> <password>"
  exit 1
fi

# Get all the Variables
system_info=$(lscpu)
machine_name=$(hostname -f)

# Extract CPU and memory details
cpu_count=$(echo "$system_info" | awk -F: '/^CPU\(s\)/ {print $2}' | xargs)
cpu_arch=$(echo "$system_info" | awk -F: '/^Architecture/ {print $2}' | xargs)
cpu_model_name=$(echo "$system_info" | awk -F: '/^Model name/ {print $2}' | xargs)
cpu_speed_mhz=$(awk -F: '/^cpu MHz/ {print $2}' /proc/cpuinfo | tail -n1 | xargs)
l2_cache_kb=$(echo "$system_info" | awk -F: '/^L2 cache/ {print $2}' | sed 's/[^0-9]*//g')
total_memory_kb=$(awk '/^MemTotal:/ {print $2}' /proc/meminfo)
record_time=$(date -u "+%Y-%m-%d %H:%M:%S")


sql_query="
INSERT INTO host_info(
  hostname,
  cpu_number,
  cpu_architecture,
  cpu_model,
  cpu_mhz,
  l2_cache,
  total_mem,
  timestamp
)
VALUES (
  '$machine_name',
  $cpu_count,
  '$cpu_arch',
  '$cpu_model_name',
  $cpu_speed_mhz,
  $l2_cache_kb,
  $total_memory_kb,
  '$record_time'
);
"

export PGPASSWORD=$password
psql -h "$host" -p "$port" -d "$database" -U "$username" -c "$sql_query"

exit $?
