document.addEventListener("DOMContentLoaded", () => {
      const driverEmail = localStorage.getItem("userEmail");

      fetch(`/api/rides/driver-past?email=${driverEmail}`)
        .then(res => res.json())
        .then(rides => renderHistoryCards(rides));
    });

    function renderHistoryCards(rides) {
      const container = document.querySelector('.events-grid');
      container.innerHTML = "";

      if (!rides || rides.length === 0) {
        container.innerHTML = "<p style='text-align:center;color:#777;'>لا توجد رحلات سابقة</p>";
        return;
      }

      rides.forEach(ride => {
        const card = document.createElement("div");
        card.className = "trip-card";

        card.innerHTML = `
          <div class="card-header">
            <h3 class="card-title">رحلة سابقة</h3>
             <button class="delete-icon" onclick="cancelRide(${ride.rideId})">
    <i class="fas fa-trash"></i>
</button>
          </div>

          <div class="trip-info-grid">
            <div class="info-box">
              <i class="fas fa-map-marker-alt"></i>
              <div>
                <span class="label">نقطة الانطلاق:</span>
                <span class="value">${ride.currentLocation}</span>
              </div>
            </div>

            <div class="info-box">
              <i class="fas fa-flag"></i>
              <div>
                <span class="label">نقطة الوصول:</span>
                <span class="value">${ride.destination}</span>
              </div>
            </div>

            <div class="info-box">
              <i class="fas fa-calendar"></i>
              <div>
                <span class="label">اليوم:</span>
                <span class="value">${ride.date}</span>
              </div>
            </div>

            <div class="info-box">
              <i class="fas fa-clock"></i>
              <div>
                <span class="label">الوقت:</span>
                <span class="value">${ride.time}</span>
              </div>
            </div>

            <div class="info-box">
              <i class="fas fa-users"></i>
              <div>
                <span class="label">المقاعد:</span>
                <span class="value">${ride.totalSeats}</span>
              </div>
            </div>
          </div>
     <div class="passenger-header">
      <h3 class="sub-title">الركاب المنضمون</h3>
      <button class="chat-icon" onclick="openChat(${ride.rideId})">
          <i class="fas fa-comment"></i>
      </button>
    </div>
          <h3 class="sub-title">طلبات إعادة الركوب</h3>

          <div class="requests-container" id="again-requests-${ride.rideId}"></div>
        `;

        container.appendChild(card);
        loadRepeatRequests(ride.rideId);
      });
    }

    function loadRepeatRequests(rideId) {
      fetch(`/api/ride-again/driver-requests?rideId=${rideId}`)
        .then(res => res.json())
        .then(requests => {
          const container = document.getElementById(`again-requests-${rideId}`);
          container.innerHTML = "";

          if (!requests || requests.length === 0) {
            container.innerHTML = "<p style='color:#777;'>لا توجد طلبات</p>";
            return;
          }

          requests.forEach(req => {
            const div = document.createElement("div");
            div.className = "again-request-card";

            div.innerHTML = `
              <div class="again-request-info">
                <p><b>من:</b> ${req.passenger.name}</p>
              </div>

              <div class="again-request-actions">
                <button onclick="respondRepeat(${req.bookingId}, 'accepted')" class="accept-btn">قبول</button>
                <button onclick="respondRepeat(${req.bookingId}, 'rejected')" class="reject-btn">رفض</button>
              </div>
            `;

            container.appendChild(div);
          });
        });
    }

    function respondRepeat(bookingId, status) {
      fetch(`/api/ride-again/respond?bookingId=${bookingId}&status=${status}`, {
        method: "POST"
      }).then(() => location.reload());
    }



    function cancelRide(rideId) {
      if (!confirm("هل أنت متأكد من حذف الرحلة?.")) return;

      fetch(`/api/rides/${rideId}/cancel`, {
        method: "POST"
      })
        .then(res => {
          if (!res.ok) {
            alert("فشل إلغاء الرحلة");
            return;
          }

          alert("تم إلغاء الرحلة بنجاح");

          // حذف الكارد من الشاشة مباشرة
          const card = document.getElementById(`ride-${rideId}`);
          if (card) card.remove();

        });
    }
