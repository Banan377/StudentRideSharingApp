 //  زر "احجز الآن" يفتح صفحة الحجز
    document.querySelector('.booking-btn').addEventListener('click', function () {
      window.location.href = "bookingPage.html";
    });
    window.addEventListener("DOMContentLoaded", () => {
      loadPassengerBookings();
    });

    function loadPassengerBookings() {
      const passengerEmail = localStorage.getItem("userEmail");

      fetch(`/api/passenger/my-bookings?passengerEmail=${encodeURIComponent(passengerEmail)}`)
        .then(res => res.json())
        .then(bookings => {
          checkEmergencyVisibility(bookings);

          const container = document.getElementById("passenger-bookings");
          container.innerHTML = "";

          if (!Array.isArray(bookings) || bookings.length === 0) {
            container.innerHTML = `<p style="text-align:center; padding:20px;">لا توجد رحلات</p>`;
            return;
          }

          bookings.forEach(booking => {
            if (booking.status === "rejected") {
              return;
            }


            let statusColor = "";
            let statusText = "";

            if (booking.status === "pending") {
              statusColor = "pending";
              statusText = "قيد الانتظار";
            } else if (booking.status === "accepted") {
              statusColor = "accepted";
              statusText = "تم القبول";
            } else if (booking.status === "rejected") {
              statusColor = "rejected";
              statusText = "تم الرفض";
            }

            const card = `

                <article class="trip-card">

                    <div>
                        <div class="card-header">
                            <h3>رحلتك</h3>
<button class="chat-icon" 
    onclick="openPassengerChat(${booking.ride.rideId}, '${booking.status}')">
    <i class="fas fa-comment"></i>
</button>                         </div>

                        <div class="trip-info">

                            <div class="location-row">
                                <div class="location-item">
                                    <i class="fas fa-map-marker-alt"></i>
                                    <div>
                                        <span class="label">نقطة الانطلاق:</span>
                                        <span class="value">${booking.ride.currentLocation}</span>
                                    </div>
                                </div>

                                <div class="location-item">
                                    <i class="fas fa-flag"></i>
                                    <div>
                                        <span class="label">نقطة الوصول:</span>
                                        <span class="value">${booking.ride.destination}</span>
                                    </div>
                                </div>
                            </div>

                            <div class="details-row">
                                <div class="detail-item">
                                    <i class="fas fa-calendar"></i>
                                    <div>
                                        <span class="label">اليوم:</span>
                                        <span class="value">${booking.ride.date}</span>
                                    </div>
                                </div>

                                <div class="detail-item">
                                    <i class="fas fa-clock"></i>
                                    <div>
                                        <span class="label">الوقت:</span>
                                        <span class="value">${booking.ride.time}</span>
                                    </div>
                                </div>

                                <div class="detail-item">
                                    <i class="fas fa-users"></i>
                                    <div>
                                        <span class="label">الحالة:</span>
                                        <span class="value ${statusColor}">${statusText}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

               <div class="driver-section">
  <div class="driver-info driver-clickable"
       data-driver-name="${booking.ride.driverName || booking.ride.driver?.name || booking.ride.driverEmail}"
       data-driver-rating="${booking.ride.driverRating || 'غير متوفر'}">

    <div class="driver-avatar">
      <i class="fas fa-user"></i>
    </div>

    <div class="driver-details">
      <div class="driver-name">
        ${booking.ride.driverName || booking.ride.driver?.name || booking.ride.driverEmail}
      </div>
    </div>

  </div>
</div>

                        </div>
                    </div>
                    

                </article>
                `;

            container.innerHTML += card;
          });
        });
    }

    function getPassengerGender() {
      const email = localStorage.getItem("userEmail");

      return fetch(`/api/user/gender?email=${email}`)
        .then(res => res.text())
        .then(g => g.trim().toLowerCase());
    }

    function loadAvailableRides() {
      const passengerEmail = localStorage.getItem("userEmail");

      fetch(`/api/rides/available?passengerEmail=${passengerEmail}`)
        .then(res => res.json())
        .then(async rides => {

          const container = document.getElementById("rides-container");
          container.innerHTML = "";

          if (rides.length === 0) {
            container.innerHTML = `<p style="text-align:center; padding:20px;">لا توجد رحلات متاحة</p>`;
            return;
          }

          rides.forEach(ride => {

            let seatLabel = ride.seatsAvailable === 0
              ? `<span style="color:red;">لا توجد مقاعد متاحة</span>`
              : `${ride.seatsAvailable} / ${ride.totalSeats}`;

            const card = `
                <div class="ride-card">
                    <p><b>نقطة الانطلاق:</b> ${ride.currentLocation}</p>
                    <p><b>الوجهة:</b> ${ride.destination}</p>
                    <p><b>اليوم:</b> ${ride.date}</p>
                    <p><b>الوقت:</b> ${ride.time}</p>
                    <p><b>المقاعد المتاحة:</b> ${seatLabel}</p>

                    <button class="book-btn"
                        data-ride-id="${ride.rideId}"
                        data-gender-pref="${ride.genderPreference}">
                        حجز
                    </button>
                </div>
                `;

            container.innerHTML += card;
          });

          // =============  إضافة الحدث بعد إنشاء الكروت =============
          document.querySelectorAll(".book-btn").forEach(btn => {
            btn.addEventListener("click", async () => {

              const rideId = btn.getAttribute("data-ride-id");
              const rideGenderPref = btn.getAttribute("data-gender-pref").toLowerCase();

              const passengerGender = await getPassengerGender();

              // ===== شروط الجندر =====
              if (rideGenderPref === "male" && passengerGender !== "male") {
                alert("هذه الرحلة مخصصة للرجال فقط.");
                return;
              }

              if (rideGenderPref === "female" && passengerGender !== "female") {
                alert("هذه الرحلة مخصصة للنساء فقط.");
                return;
              }

              // ===== إذا كله تمام، نرسل طلب الحجز =====
              bookRide(rideId);
            });
          });
        });
    }

    function bookRide(rideId) {
      const passengerEmail = localStorage.getItem("userEmail");

      fetch(`/api/bookings/request?rideId=${rideId}&passengerEmail=${passengerEmail}`, {
        method: "POST"
      })
        .then(res => {
          if (res.ok) {
            alert("تم إرسال طلب الحجز!");
            loadAvailableRides();
          } else {
            alert("لا يوجد مقاعد متاحة!");
          }
        });
    }

    let selectedRideId = null;

    function openEmergencyPopup(rideId) {
      selectedRideId = rideId;
      document.getElementById("emergencyPopup").classList.remove("hidden");
    }

    function closeEmergencyPopup() {
      document.getElementById("emergencyPopup").classList.add("hidden");
      selectedRideId = null;
    }
    function openEmergencyPopupGlobal() {
      document.getElementById("emergencyPopup").classList.remove("hidden");
    }


    document.addEventListener("DOMContentLoaded", () => {
      const btn = document.getElementById("confirmEmergency");

      if (btn) {
        btn.onclick = () => {
          const userEmail = localStorage.getItem("userEmail");

          fetch(`/api/safety/trigger?userId=${userEmail}&message=Emergency`, {
            method: "POST"
          })

            .then(res => {
              if (res.ok) {
                alert(" تم إرسال بلاغ الطوارئ بنجاح!");
              } else {
                alert(" حدث خطأ أثناء إرسال البلاغ");
              }

              closeEmergencyPopup();
            });
        };
      }
    });


    function checkEmergencyVisibility(bookings) {
      const emergencyBar = document.getElementById("global-emergency");

      let hasActiveRide = false;

      const nowDate = new Date();
      const today = nowDate.toISOString().split("T")[0];
      const currentTime = nowDate.toTimeString().split(":").slice(0, 2).join(":");

      bookings.forEach(b => {
        if (b.status === "accepted") {
          const rideDate = b.ride.date;
          const rideTime = b.ride.time;

          if (rideDate === today && rideTime <= currentTime) {
            hasActiveRide = true;
          }
        }
      });

      if (hasActiveRide) {
        emergencyBar.style.display = "block";
      } else {
        emergencyBar.style.display = "none";
      }
    }




    function openPassengerChat(rideId, bookingStatus) {

      if (bookingStatus !== "accepted") {
        alert("لا يمكن بدء المحادثة الا بعد قبول الرحلة ");
        return;
      }

      // حفظ رقم الرحلة
      localStorage.setItem('currentRideId', rideId);
      localStorage.setItem('currentUserId', 'PASSENGER_' + localStorage.getItem("userEmail"));

      window.location.href = 'chat.html';
    }

