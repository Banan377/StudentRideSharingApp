
// Driver modal
function openDriverModal(driverName, driverRating) {
    const root = document.getElementById('driver-modal-root');
    root.innerHTML = `
                <div class="driver-modal-overlay" id="driver-modal-overlay">
                    <div class="driver-modal" role="dialog" aria-modal="true" aria-label="ملف السائق">
                        <div class="driver-header">
                            <div class="driver-avatar"><i class="fas fa-user"></i></div>
                            <div>
                                <h4>${driverName}</h4>
                                <p>التقييم: ${driverRating}</p>
                            </div>
                        </div>
                        <p>معلومات إضافية عن السائق يمكن وضعها هنا.</p>
                        <div style="margin-top:12px; display:flex; gap:8px; justify-content:flex-end;">
                            <button onclick="closeDriverModal()" class="cancel-logout-btn">إغلاق</button>
                        </div>
                    </div>
                </div>
            `;
    document.getElementById('driver-modal-overlay').addEventListener('click', function (e) {
        if (e.target.id === 'driver-modal-overlay') closeDriverModal();
    });
}
function closeDriverModal() { document.getElementById('driver-modal-root').innerHTML = ''; }

document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.driver-rating').forEach(el => {
        el.addEventListener('click', function () {
            const card = el.closest('.trip-card');
            const name = card.getAttribute('data-driver-name') || card.querySelector('.driver-name')?.textContent.trim();
            const rating = card.getAttribute('data-driver-rating') || card.querySelector('.rating-text')?.textContent.trim();
            openDriverModal(name, rating);
        });
    });

    document.querySelectorAll('.card-profile-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            const card = btn.closest('.trip-card');
            const name = card.getAttribute('data-driver-name') || card.querySelector('.driver-name')?.textContent.trim();
            const rating = card.getAttribute('data-driver-rating') || card.querySelector('.rating-text')?.textContent.trim();
            openDriverModal(name, rating);
        });
    });
});

//cards
document.addEventListener("DOMContentLoaded", () => {
    const userEmail = localStorage.getItem("userEmail");
    fetch(`/api/rides/past?passengerEmail=${userEmail}`)
        .then(res => res.json())
        .then(data => renderPastRides(data))
        .catch(err => console.error(err));
});


function renderPastRides(rides) {
    const container = document.querySelector('.trips-grid');
    container.innerHTML = "";

    if (!rides || rides.length === 0) {
        container.innerHTML = "<p class='no-data'>لا توجد رحلات سابقة.</p>";
        return;
    }

    rides.forEach(ride => {
        const card = document.createElement("article");
        card.className = "trip-card";

        card.innerHTML = `
            <div>
                <div class="card-header">
                    <h3>رحلة سابقة</h3>
                     <div class="chat-icon" ><i class="fas fa-comment"></i></div>

                </div>
                <div class="trip-info">
                    <div class="location-row">
                        <div class="location-item">
                            <i class="fas fa-map-marker-alt"></i>
                            <div>
                                <span class="label">نقطة الانطلاق:</span>
                                <span class="value">${ride.startArea || ride.currentLocation}</span>
                            </div>
                        </div>
                        <div class="location-item">
                            <i class="fas fa-flag"></i>
                            <div>
                                <span class="label">نقطة الوصول:</span>
                                <span class="value">${ride.destination}</span>
                            </div>
                        </div>
                    </div>

                    <div class="details-row">
                        <div class="detail-item">
                            <i class="fas fa-calendar"></i>
                            <div><span class="label">التاريخ:</span>
                                <span class="value">${ride.date}</span></div>
                        </div>

                        <div class="detail-item">
                            <i class="fas fa-clock"></i>
                            <div><span class="label">الوقت:</span>
                                <span class="value">${ride.time}</span></div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="driver-section">
                <div class="driver-info">
                    <div class="default-avatar driver-avatar"><i class="fas fa-user"></i></div>
                    <div class="driver-details">
                        <div class="driver-name">${ride.driverName || "غير معروف"}</div>
                        <div class="driver-rating">
                            <span class="rating-text">${ride.driverRating || "0"}</span>
                        </div>
                    </div>
                </div>

                <button class="book-btn" onclick="requestAgain(${ride.rideId})">
                    إعادة طلب الرحلة
                </button>
            </div>
        `;

        container.appendChild(card);
    });
}

function requestAgain(rideId) {
    const email = localStorage.getItem("userEmail");

    fetch(`/api/ride-again/request?passengerEmail=${email}&rideId=${rideId}`, {
        method: "POST"
    })
        .then(res => res.text())
        .then(msg => {
            alert("تم إرسال طلب إعادة الرحلة للسائق!");
        })
        .catch(err => {
            console.error("Error:", err);
            alert("حدث خطأ أثناء إرسال الطلب");
        });
}
