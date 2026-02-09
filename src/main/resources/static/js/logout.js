 function logout() {
      document.getElementById('profile-dropdown').classList.add('hidden');
      const overlay = document.createElement('div');
      overlay.className = 'logout-overlay';
      overlay.id = 'logout-confirmation';

      overlay.innerHTML = `
        <div class="logout-modal">
          <div class="logout-content">
            <i class="fas fa-sign-out-alt logout-icon"></i>
            <h3>تسجيل الخروج</h3>
            <p>هل أنت متأكد؟</p>
            <div class="logout-buttons">
              <button onclick="confirmLogout()" class="confirm-logout-btn">نعم</button>
              <button onclick="cancelLogout()" class="cancel-logout-btn">إلغاء</button>
            </div>
          </div>
        </div>`;
      document.body.appendChild(overlay);
    }

    function confirmLogout() {
      localStorage.removeItem("currentUser");
      localStorage.removeItem("userEmail");
      localStorage.removeItem("resetPasswordMode");

      const overlay = document.getElementById('logout-confirmation');
      overlay.innerHTML = `
        <div class="logout-modal">
          <div class="logout-content success">
            <i class="fas fa-check-circle success-icon"></i>
            <h3>تم تسجيل الخروج</h3>
            <button onclick="closeLogoutSuccess()" class="success-btn">موافق</button>
          </div>
        </div>`;
    }

    function cancelLogout() {
      const overlay = document.getElementById('logout-confirmation');
      if (overlay) overlay.remove();
    }

    function closeLogoutSuccess() {
      window.location.href = "login.html";
    }

      function toggleProfileDropdown() {
            document.getElementById('profile-dropdown').classList.toggle('hidden');
        }

        function showRatingPage() {
            document.getElementById('profile-dropdown').classList.add('hidden');
            document.getElementById('rating-page').classList.remove('hidden');
            document.body.style.overflow = 'hidden';
        }



        function closePage() {
            document.querySelectorAll('.full-page').forEach(page => page.classList.add('hidden'));
            document.body.style.overflow = 'auto';
        }

        document.addEventListener('click', function (event) {
            const profileSection = document.querySelector('.profile-section');
            if (!profileSection.contains(event.target)) {
                document.getElementById('profile-dropdown').classList.add('hidden');
            }
        });
