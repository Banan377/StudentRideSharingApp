function toggleProfileDropdown() {
    document.getElementById('profile-dropdown').classList.toggle('hidden');
}

document.addEventListener('click', function (event) {
    const section = document.querySelector('.profile-section');
    if (!section.contains(event.target)) {
        document.getElementById('profile-dropdown').classList.add('hidden');
    }
});



// تحميل الإحصائيات
async function loadDashboardStats() {
    try {
        const res = await fetch('/api/admin/stats');
        const stats = await res.json();

        document.getElementById('stat-pending').innerText = stats.pendingDrivers ?? 0;
        document.getElementById('stat-approved').innerText = stats.approvedDrivers ?? 0;
        document.getElementById('stat-users').innerText = stats.totalUsers ?? 0;
        document.getElementById('stat-rides').innerText = stats.todayRides ?? 0;

    } catch (e) {
        console.error("Error loading stats:", e);
    }
}


async function loadPendingPreview() {
    try {
        const res = await fetch('/api/admin/pending-drivers');
        const data = await res.json();
        const cont = document.getElementById('pendingPreview');

        cont.innerHTML = "";

        const latest = data.slice(0, 3);

        if (latest.length === 0) {
            cont.innerHTML = `
        <p style="color:#777; text-align:center; padding:15px;">
          لا توجد طلبات جديدة حالياً
        </p>`;
            return;
        }

        latest.forEach(driver => {
            cont.innerHTML += `
        <div class="preview-card">
          <div style="display:flex; justify-content:space-between; align-items:center;">
<h4>${driver.name}</h4>
            <span class="preview-badge">بانتظار المراجعة</span>
          </div>

          <p class="preview-meta">
            <i class="fas fa-envelope"></i> ${driver.email}
          </p>

          <p class="preview-meta">
            <i class="fas fa-car"></i> ${driver.carModel} - ${driver.carColor}
          </p>
        </div>
      `;
        });

    } catch (e) {
        console.error("Error loading pending preview:", e);
    }
}

loadDashboardStats();
loadPendingPreview();