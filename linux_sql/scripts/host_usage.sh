# Assign input arguments to variables
host=$1
port=$2
db=$3
user=$4
password=$5

# Validate number of arguments
if [[ $# -ne 5 ]]; then
  echo "Error: Expected 5 arguments (host, port, db_name, username, password)"
  exit 1
fi

#Get all the Variables
machine_name=$(hostname -f)
sys_stats=$(vmstat -SM)
disk_info=$(df -BM /)

# Extract system metrics
mem_free=$(echo "$sys_stats" | awk 'NR==3 {print $4}')
cpu_idle_pct=$(echo "$sys_stats" | awk 'NR==3 {print $15}')
cpu_sys_pct=$(echo "$sys_stats" | awk 'NR==3 {print $14}')
disk_io_count=$(vmstat -d | awk 'END {print $10}')
disk_free_mb=$(echo "$disk_info" | awk 'NR==2 {gsub(/M/,"",$4); print $4}')
record_time=$(date -u "+%Y-%m-%d %H:%M:%S")

sql_query="
INSERT INTO host_usage(timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
VALUES(
  '$record_time',
  (SELECT id FROM host_info WHERE hostname='$machine_name'),
  $mem_free,
  $cpu_idle_pct,
  $cpu_sys_pct,
  $disk_io_count,
  $disk_free_mb
);
"

export PGPASSWORD=$password
psql -h "$host" -p "$port" -d "$db" -U "$user" -c "$sql_query"

exit $?
