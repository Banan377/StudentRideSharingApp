function toggleProfileDropdown() {
    document.getElementById('profile-dropdown').classList.toggle('hidden');
}

document.addEventListener('click', function (event) {
    const profile = document.querySelector('.profile-section');
    if (!profile.contains(event.target)) {
        document.getElementById('profile-dropdown').classList.add('hidden');
    }
});



async function loadPendingDrivers() {
    try {
        const res = await fetch('/api/admin/pending-drivers');
        const data = await res.json();

        const container = document.getElementById('requestsContainer');
        container.innerHTML = "";

        if (!data || data.length === 0) {
            container.innerHTML = `
            <p style="text-align:center; color:#666; padding:20px;">
              لا توجد طلبات سائقين معلّقة حالياً.
            </p>
          `;
            return;
        }

        data.forEach(driver => {
            container.innerHTML += `
            <div class="request-card">
              <div class="request-header">
                <div>
                  <div class="request-name">${driver.firstName || ''} ${driver.lastName || ''}</div>
                  <div class="request-email"><i class="fas fa-envelope"></i> ${driver.email}</div>
                </div>
                <span class="badge-pending">
                  <i class="fas fa-clock"></i> بانتظار المراجعة
                </span>
              </div>

              <div class="request-body">
                <div class="info-item">
                  <div class="info-label">الكلية</div>
                  <div class="info-value">${driver.college || 'غير محدد'}</div>
                </div>
                <div class="info-item">
                  <div class="info-label">موديل السيارة</div>
                  <div class="info-value">${driver.carModel || '-'}</div>
                </div>
                <div class="info-item">
                  <div class="info-label">لون السيارة</div>
                  <div class="info-value">${driver.carColor || '-'}</div>
                </div>
                <div class="info-item">
                  <div class="info-label">رقم اللوحة</div>
                  <div class="info-value">${driver.carPlate || '-'}</div>
                </div>
                <div class="info-item">
                  <div class="info-label">المقاعد المتاحة</div>
                  <div class="info-value">${driver.seatsAvailable ?? '-'}</div>
                </div>
              </div>

              <div class="request-files">
                <span><strong>الملفات:</strong></span>
                <a class="file-link" href="${driver.licenseUrl}" target="_blank">رخصة القيادة</a>
                <a class="file-link" href="${driver.registrationUrl}" target="_blank">استمارة السيارة</a>
              </div>

              <div class="request-actions">
                <button class="btn-approve" onclick="approveDriver('${driver.email}')">موافقة</button>
                <button class="btn-reject" onclick="rejectDriver('${driver.email}')">رفض</button>
              </div>
            </div>
          `;
        });
    } catch (e) {
        console.error(e);
    }
}

async function approveDriver(email) {
    if (!confirm("هل تريد الموافقة على هذا الطلب؟")) return;
    await fetch(`/api/admin/approve?email=${encodeURIComponent(email)}`, { method: 'PUT' });
    alert("تمت الموافقة بنجاح");
    loadPendingDrivers();
}

async function rejectDriver(email) {
    if (!confirm("هل تريد رفض هذا الطلب؟")) return;
    await fetch(`/api/admin/reject?email=${encodeURIComponent(email)}`, { method: 'PUT' });
    alert("تم رفض الطلب");
    loadPendingDrivers();
}

loadPendingDrivers();