function showSwitchRoleConfirmation() {
    document.getElementById("switch-role-confirmation").classList.remove("hidden");
    document.body.style.overflow = "hidden";
}

function confirmSwitchRole() {
    window.location.href = "passengerRides.html";
}

function cancelSwitchRole() {
    document.getElementById("switch-role-confirmation").classList.add("hidden");
    document.body.style.overflow = "auto";
}

function updateStats(pending, accepted, rejected) {
    document.getElementById("new-requests").textContent = pending;
    document.getElementById("accepted").textContent = accepted;
    document.getElementById("rejected").textContent = rejected;
}

function initStats() {
    const accepted = parseInt(localStorage.getItem("driverAcceptedCount")) || 0;
    const rejected = parseInt(localStorage.getItem("driverRejectedCount")) || 0;
    updateStats(0, accepted, rejected);
}

function loadDriverRides() {
    const driverEmail = localStorage.getItem("userEmail");

    fetch(`http://localhost:8083/api/rides/driver-rides?email=${driverEmail}`)
        .then(res => res.json())
        .then(rides => {

            console.log("رحلات السائق بعد التحديث:", rides);

            const mainArea = document.getElementById("driver-rides-area");
            mainArea.innerHTML = "";


            rides.forEach(ride => {
                const rideCard = document.createElement("div");
                rideCard.className = "ride-details-card";
                rideCard.id = `ride-${ride.rideId}`;

                rideCard.style = `
            background:white;
            padding:20px;
            border-radius:15px;
            box-shadow:0 2px 5px rgba(0,0,0,0.1);
            margin-bottom:25px;
          `;

                rideCard.innerHTML = `
<div class="trip-card">

    <div class="card-header">
        <h3 class="card-title">رحلة منشورة</h3>
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
                <span class="label">المقاعد المتبقية:</span>
                <span class="value">${ride.seatsAvailable}/${ride.totalSeats}</span>
            </div>
        </div>
            <button class="startRideBtn" onclick="startRide(${ride.rideId}, '${ride.destinationLatLng}')">بدء الرحلة</button>

    </div>
<div class="passenger-box">

  <div class="passenger-header">
      <h3 class="sub-title">الركاب المنضمون</h3>
      <button class="chat-icon" onclick="openChat(${ride.rideId})">
          <i class="fas fa-comment"></i>
      </button>
  </div>

  <div id="passengers-${ride.rideId}" class="accepted-passengers"></div>
</div>
    <h3 class="sub-title">طلبات الحجز</h3>
    <div id="requests-${ride.rideId}" class="requests-container"></div>

</div>
`;



                mainArea.appendChild(rideCard);
                loadAcceptedPassengers(ride.rideId);

            });

            loadBookingRequests();
        });
}

function loadBookingRequests() {
    const driverEmail = localStorage.getItem("userEmail");

    fetch(`http://localhost:8083/api/bookings/driver-requests?driverEmail=${driverEmail}`)
        .then(res => res.json())
        .then(requests => {

            const pendingCount = requests.length;
            const accepted = parseInt(localStorage.getItem("driverAcceptedCount")) || 0;
            const rejected = parseInt(localStorage.getItem("driverRejectedCount")) || 0;

            updateStats(pendingCount, accepted, rejected);

            if (requests.length === 0) return;

            const grouped = {};
            requests.forEach(req => {
                const rId = req.ride.rideId;
                if (!grouped[rId]) grouped[rId] = [];
                grouped[rId].push(req);
            });

            Object.keys(grouped).forEach(rId => {
                const container = document.getElementById(`requests-${rId}`);
                if (!container) return;

                container.innerHTML = "";

                grouped[rId].forEach(req => {

                    const card = document.createElement("div");
                    card.className = "booking-request-card";
                    card.id = `booking-${req.bookingId}`;
                    card.className = "booking-request-card";
                    card.style = `
              border:1px solid #ddd;
              padding:15px;
              border-radius:10px;
              background:white;
              display:flex;
              justify-content:space-between;
              align-items:center;
            `;

                    card.innerHTML = `
              <div class="request-info" style="width:100%;">
                <h3 style="font-size:20px;color:#333;">طلب من: ${req.passenger.name}</h3>
                <p><b>موقع الانطلاق:</b> ${req.ride.currentLocation}</p>
              </div>

              <div class="request-actions" style="display:flex; gap:10px;">
                <button onclick="respondToBooking(${req.bookingId}, 'accepted')"
                  style="background:#28a745; color:white; padding:8px 15px; border-radius:5px;">
                  قبول
                </button>

                <button onclick="respondToBooking(${req.bookingId}, 'rejected')"
                  style="background:#dc3545; color:white; padding:8px 15px; border-radius:5px;">
                  رفض
                </button>
              </div>
            `;

                    container.appendChild(card);
                });
            });

        });
}

function respondToBooking(bookingId, status) {
    fetch(`http://localhost:8083/api/bookings/respond?bookingId=${bookingId}&status=${status}`, {
        method: "POST"
    })
        .then(res => {
            if (!res.ok) {
                alert("فشل الرد على الطلب");
                return;
            }

            alert(status === "accepted" ? "تم قبول الحجز" : "تم رفض الحجز");

            let accepted = parseInt(localStorage.getItem("driverAcceptedCount")) || 0;
            let rejected = parseInt(localStorage.getItem("driverRejectedCount")) || 0;

            if (status === "accepted") {
                localStorage.setItem("driverAcceptedCount", ++accepted);
            } else {
                localStorage.setItem("driverRejectedCount", ++rejected);

                //  حذف كارد الراكب فقط إذا كان مرفوض
                const card = document.getElementById(`booking-${bookingId}`);
                if (card) card.remove();
            }

            // الاحتفاظ بتحميل طلبات القبول فقط
            if (status === "accepted") {
                loadDriverRides();
            }
        });
}

document.addEventListener("DOMContentLoaded", () => {
    initStats();
    loadDriverRides();
});

function loadAcceptedPassengers(rideId) {
    fetch(`http://localhost:8083/api/bookings/accepted-passengers?rideId=${rideId}`)
        .then(res => res.json())
        .then(passengers => {
            const container = document.getElementById(`passengers-${rideId}`);
            container.innerHTML = "";

            if (passengers.length === 0) {
                container.innerHTML = "<p style='color:#777;font-size:14px;'>لا يوجد ركاب بعد</p>";
                return;
            }

            passengers.forEach(p => {
                const div = document.createElement("div");
                div.className = "accepted-passenger-card";
                div.innerHTML = `
          <p><i class="fas fa-user"></i> ${p.passengerName}
</p>
        `;
                container.appendChild(div);
            });
        });
}

function startRide(rideId, destinationLatLng) {
    fetch(`http://localhost:8083/api/rides/${rideId}/start`, {
        method: "POST"
    }).then(res => {
        if (!res.ok) { alert("فشل بدء الرحلة"); return; }
        // ننتقل للصفحة مع تمرير المتغيرات في رابط URL
        window.location.href = `driverCurrentRide.html?rideId=${rideId}&destinationLatLng=${encodeURIComponent(destinationLatLng)}`;
    }).catch(err => {
        console.error("خطأ في بدء الرحلة:", err);
        alert("فشل الاتصال بالخادم");
    });
}



function openChat(rideId) {
    // حفظ رقم الرحلة
    localStorage.setItem('currentRideId', rideId);
    localStorage.setItem('currentUserId', 'DRIVER_' + localStorage.getItem("userEmail"));
    // الانتقال لصفحة المحادثة
    window.location.href = 'chat.html';
}

function cancelRide(rideId) {
    if (!confirm("هل أنت متأكد من إلغاء الرحلة؟ سيتم إعلام الركاب.")) return;

    fetch(`http://localhost:8083/api/rides/${rideId}/cancel`, {
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

            // إرسال إشعار للركاب
            notifyPassengersRideCancelled(rideId);
        });
}

function notifyPassengersRideCancelled(rideId) {
    fetch(`http://localhost:8083/api/bookings/ride-cancelled?rideId=${rideId}`)
        .then(() => {
            console.log("تم إعلام الركاب!");
        });
}
