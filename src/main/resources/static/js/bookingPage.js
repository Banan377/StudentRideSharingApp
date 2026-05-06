// ====== القائمة والبروفايل ======
function toggleProfileDropdown() {
    document.getElementById('profile-dropdown').classList.toggle('hidden');
}

function showRatingPage() {
    document.getElementById('profile-dropdown').classList.add('hidden');
    document.getElementById('rating-page').classList.remove('hidden');
    document.body.style.overflow = 'hidden';
}

function closePage() {
    document.querySelectorAll('.full-page').forEach(p => p.classList.add('hidden'));
    document.body.style.overflow = 'auto';
}


document.addEventListener('click', function (event) {
    const profileSection = document.querySelector('.profile-section');
    const dropdown = document.getElementById('profile-dropdown');
    if (!profileSection.contains(event.target)) {
        dropdown.classList.add('hidden');
    }
});

// ====== تحميل الرحلات عند فتح الصفحة ======
window.addEventListener("DOMContentLoaded", () => {
    loadAvailableRides();
});

function loadAvailableRides() {
    const passengerEmail = localStorage.getItem("userEmail");

    fetch(`/api/rides/available?passengerEmail=${passengerEmail}`)


        .then(res => res.json())
        .then(data => {
            const container = document.querySelector(".trips-grid");
            if (!container) return;

            container.innerHTML = ""; // امسح القديم

            if (!Array.isArray(data) || data.length === 0) {
                container.innerHTML = `<p style="padding:20px; text-align:center; color:#666;">لا توجد رحلات متاحة حالياً</p>`;
                return;
            }

            data.forEach(ride => {
                const card = document.createElement("article");
                card.className = "trip-card";

                card.innerHTML = `
                <div>
                  <div class="card-header">
                    <h3>رحلة متاحة</h3>
                  </div>
                  <div class="trip-info">
                    <div class="location-row">
                      <div class="location-item">
                        <i class="fas fa-map-marker-alt"></i>
                        <div>
                          <span class="label">نقطة الانطلاق:</span>
                          <span class="value">${ride.currentLocation || 'غير محدد'}</span>
                        </div>
                      </div>
                      <div class="location-item">
                        <i class="fas fa-flag"></i>
                        <div>
                          <span class="label">نقطة الوصول:</span>
                          <span class="value">${ride.destination || 'غير محددة'}</span>
                        </div>
                      </div>
                    </div>
                    <div class="details-row">
                      <div class="detail-item">
                        <i class="fas fa-calendar"></i>
                        <div>
                          <span class="label">اليوم:</span>
                          <span class="value">${ride.date || ''}</span>
                        </div>
                      </div>
                      <div class="detail-item">
                        <i class="fas fa-clock"></i>
                        <div>
                          <span class="label">الوقت:</span>
                          <span class="value">${ride.time || ''}</span>
                        </div>
                      </div>
                      <div class="detail-item">
                        <i class="fas fa-users"></i>
                        <div>
                          <span class="label">المقاعد المتاحة:</span>
                          <span class="value">${ride.seatsAvailable ?? ''}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              <div class="driver-section">
  <div class="driver-info driver-clickable"
       data-driver-name="${ride.driverName || ride.driverEmail || 'سائق'}"
       data-driver-rating="${ride.driverRating || 'غير متوفر'}">
       
    <div class="default-avatar driver-avatar">
      <i class="fas fa-user"></i>
    </div>

    <div class="driver-details">
      <div class="driver-name">
        ${ride.driverName || ride.driverEmail || 'سائق'}
      </div>
    </div>
  </div>

  <button class="book-btn" data-ride-id="${ride.rideId}">حجز</button>
</div>

              `;

                container.appendChild(card);
            });

            // تفعيل أزرار الحجز بعد إنشاء الكروت
            document.querySelectorAll(".book-btn").forEach(btn => {
                btn.addEventListener("click", () => {
                    const rideId = btn.getAttribute("data-ride-id");
                    const card = btn.closest(".trip-card");
                    sendBookingRequest(rideId, card);
                });
            });
        })
        .catch(err => {
            console.error("فشل في جلب الرحلات:", err);
        });
}

// ====== إرسال طلب الحجز ======
function sendBookingRequest(rideId, cardElement) {
    const passengerEmail = localStorage.getItem("userEmail");

   fetch(`http://localhost:8080/api/bookings/request?passengerEmail=${encodeURIComponent(passengerEmail)}&rideId=${rideId}`, {
    method: "POST"
})
        .then(async res => {
            if (res.ok) {
                alert("تم إرسال طلب الحجز للسائق!");
                if (cardElement) {
                    cardElement.remove();
                }
            } else {
                const errorMessage = await res.text();
                alert(errorMessage);
            }
        })
        .catch(err => {
            console.error("خطأ في طلب الحجز:", err);
            alert("حدث خطأ غير متوقع");
        });
}
function searchRides() {
    const keyword = document.getElementById("searchInput").value.trim();
    const passengerEmail = localStorage.getItem("userEmail");

    // لو فاضي... رجّعي كل الرحلات
    if (keyword === "") {
        loadAvailableRides();
        return;
    }

    fetch(`/api/rides/search?destination=${encodeURIComponent(keyword)}`)
        .then(res => res.json())
        .then(data => {
            const container = document.querySelector(".trips-grid");
            container.innerHTML = "";

            if (!data || data.length === 0) {
                container.innerHTML = `
                    <p style="padding:20px; text-align:center; color:#666;">
                        لا توجد نتائج مطابقة
                    </p>`;
                return;
            }

            // إعادة رسم الكروت نفس loadAvailableRides()
            data.forEach(ride => {
                const card = document.createElement("article");
                card.className = "trip-card";

                card.innerHTML = `
                    <div>
                        <div class="card-header">
                            <h3>رحلة متاحة</h3>
                        </div>
                        <div class="trip-info">
                            <div class="location-row">
                                <div class="location-item">
                                    <i class="fas fa-map-marker-alt"></i>
                                    <div>
                                        <span class="label">نقطة الانطلاق:</span>
                                        <span class="value">${ride.currentLocation || 'غير محدد'}</span>
                                    </div>
                                </div>
                                <div class="location-item">
                                    <i class="fas fa-flag"></i>
                                    <div>
                                        <span class="label">نقطة الوصول:</span>
                                        <span class="value">${ride.destination || 'غير محددة'}</span>
                                    </div>
                                </div>
                            </div>

                            <div class="details-row">
                                <div class="detail-item">
                                    <i class="fas fa-calendar"></i>
                                    <div><span class="label">اليوم:</span> <span class="value">${ride.date || ''}</span></div>
                                </div>
                                <div class="detail-item">
                                    <i class="fas fa-clock"></i>
                                    <div><span class="label">الوقت:</span> <span class="value">${ride.time || ''}</span></div>
                                </div>
                                <div class="detail-item">
                                    <i class="fas fa-users"></i>
                                    <div><span class="label">المقاعد المتاحة:</span> <span class="value">${ride.seatsAvailable ?? ''}</span></div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="driver-section">
                        <div class="driver-info">
                            <div class="default-avatar driver-avatar"><i class="fas fa-user"></i></div>
                            <div class="driver-details">
                               <div class="driver-name">${ride.driverName || ride.driverEmail || 'سائق'}</div>
                            </div>
                        </div>
                        <button class="book-btn" data-ride-id="${ride.rideId}">حجز</button>
                    </div>
                `;

                container.appendChild(card);
            });

            // تفعيل الحجز للكروت
            document.querySelectorAll(".book-btn").forEach(btn => {
                btn.addEventListener("click", () => {
                    const rideId = btn.getAttribute("data-ride-id");
                    const card = btn.closest(".trip-card");
                    sendBookingRequest(rideId, card);
                });
            });
        })
        .catch(err => {
            console.error("خطأ في البحث:", err);
        });
}
