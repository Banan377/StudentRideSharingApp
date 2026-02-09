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

      document.addEventListener('click', function (e) {
        const driverCard = e.target.closest('.driver-clickable');
        if (!driverCard) return;

        const name = driverCard.getAttribute('data-driver-name');
        const rating = driverCard.getAttribute('data-driver-rating');

        openDriverModal(name, rating);
      });

      