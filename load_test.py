import requests
import time
from datetime import datetime

url = "http://localhost:8080/api/bookings/request"
passenger_email = "555555555@sm.imamu.edu.sa"
total_requests = 200
duration_hours = 6
interval = (duration_hours * 3600) / total_requests 

# Calculation and Analysis Variables
success_count = 0
failure_count = 0
failure_incidents = []  # List to store repair durations in seconds
current_failure_start = None

print(f"Starting Reliability Test...")
print(f"Duration: 6 Hours | Interval between requests: {interval:.1f} seconds")
print("-" * 60)

for ride_id in range(1, total_requests + 1):
    full_url = f"{url}?passengerEmail={passenger_email}&rideId={ride_id}"
    now = datetime.now()
    timestamp = now.strftime('%H:%M:%S')
    
    try:
        # Send POST request with a 5-second timeout
        response = requests.post(full_url, timeout=5)
        
        if response.status_code in [200, 201]:
            # If there was a previous failure and it is now fixed
            if current_failure_start:
                repair_time = (now - current_failure_start).total_seconds()
                failure_incidents.append(repair_time)
                print(f"--- FIXED: System took {repair_time:.1f} seconds to recover ---")
                current_failure_start = None
            
            success_count += 1
            status = "Success"
        else:
            # In case of failure (Status code other than 200/201)
            if not current_failure_start:
                current_failure_start = now
            failure_count += 1
            status = f"Failed (Status Code: {response.status_code})"
            
    except Exception as e:
        # In case of a full server crash or connection error
        if not current_failure_start:
            current_failure_start = now
        failure_count += 1
        status = f"Server Connection Error: {e}"

    print(f"[{timestamp}] Request #{ride_id}: {status}")
    
    # Wait for the next request (except after the last one)
    if ride_id < total_requests:
        time.sleep(interval)

# --- Summary Report ---
print("\n" + "="*50)
print("Software Engineering Report: Maintenance & Reliability")
print(f"Total Requests Sent: {total_requests}")
print(f"Successful Bookings: {success_count}")
print(f"Failed Bookings: {failure_count}")

if failure_incidents:
    avg_mttr = sum(failure_incidents) / len(failure_incidents)
    print(f"Number of Failure Incidents: {len(failure_incidents)}")
    print(f"Mean Time To Repair (MTTR): {avg_mttr:.2f} seconds")
    print("Repair Durations for each Incident:")
    for i, t in enumerate(failure_incidents, 1):
        print(f"   Incident {i}: {t:.1f} seconds")
else:
    print("Reliability: 100% (No failures detected during test duration)")

# Availability Formula
availability = (success_count / total_requests) * 100
print(f"System Availability Rate: {availability:.2f}%")
print("="*50)