function toggleProfileDropdown() {
      document.getElementById('profile-dropdown').classList.toggle('hidden');
    }

    document.addEventListener('click', function (event) {
      const profile = document.querySelector('.profile-section');
      if (!profile.contains(event.target)) {
        document.getElementById('profile-dropdown').classList.add('hidden');
      }
    });



    async function loadUsers() {
      const res = await fetch('/api/admin/users');
      const users = await res.json();
      window.allUsers = users;
      renderUsers(users);
    }

    function statusPill(status) {
      if (!status) return "-";
      let cls = "status-pill ";
      if (status === "active") cls += "status-active";
      else if (status === "suspended") cls += "status-suspended";
      else if (status === "pending_driver") cls += "status-pending_driver";
      else if (status === "approved_driver") cls += "status-approved_driver";
      else if (status === "rejected_driver") cls += "status-rejected_driver";
      return `<span class="${cls}">${status}</span>`;
    }

    function renderUsers(list) {
      const table = document.getElementById("usersTable");
      table.innerHTML = "";

      if (!list || list.length === 0) {
        table.innerHTML = `<tr><td colspan="5" style="padding:20px;">لا يوجد بيانات</td></tr>`;
        return;
      }

      list.forEach(user => {
        table.innerHTML += `
          <tr style="border-bottom:1px solid #eee;">
            <td style="padding:10px;">${user.name || "-"}</td>
            <td style="padding:10px;">${user.email}</td>
            <td style="padding:10px;">${user.role || "-"}</td>
            <td style="padding:10px;">${statusPill(user.status)}</td>
            <td style="padding:10px; display:flex; gap:8px; justify-content:center; flex-wrap:wrap;">

              <button onclick="changeStatus('${user.email}', 'active')"
                      style="padding:6px 10px; background:#4caf50; color:white; border-radius:6px; font-size:12px;">
                تفعيل
              </button>

              <button onclick="changeStatus('${user.email}', 'suspended')"
                      style="padding:6px 10px; background:#f39c12; color:white; border-radius:6px; font-size:12px;">
                إيقاف
              </button>

              <button onclick="deleteUser('${user.email}')"
                      style="padding:6px 10px; background:#e74c3c; color:white; border-radius:6px; font-size:12px;">
                حذف
              </button>

            </td>
          </tr>
        `;
      });
    }

    async function changeStatus(email, status) {
      if (!confirm("هل أنت متأكد من تغيير الحالة؟")) return;

      await fetch(`/api/admin/users/status?email=${encodeURIComponent(email)}&status=${encodeURIComponent(status)}`, {
        method: 'PUT'
      });

      alert("تم التحديث بنجاح");
      loadUsers();
    }

    async function deleteUser(email) {
      if (!confirm("هل تريد حذف هذا المستخدم نهائياً؟")) return;

      await fetch(`/api/admin/users?email=${encodeURIComponent(email)}`, {
        method: 'DELETE'
      });

      alert("تم حذف المستخدم");
      loadUsers();
    }

    function applyFilters() {
      const text = document.getElementById('searchInput').value.toLowerCase();
      const role = document.getElementById('roleFilter').value;
      const status = document.getElementById('statusFilter').value;

      let filtered = window.allUsers.filter(u =>
        (u.email.toLowerCase().includes(text) || (u.name || "").toLowerCase().includes(text)) &&
        (role === "" || u.role === role) &&
        (status === "" || u.status === status)
      );

      renderUsers(filtered);
    }

    loadUsers();