 let originalUser = {};
    let originalDriver = {};

    // دالة لجلب بيانات السائق والمستخدم
    async function loadDriverSettings() {
      const email = localStorage.getItem("userEmail");

      if (!email) {
        alert("يجب تسجيل الدخول أولاً");
        window.location.href = "login.html";
        return;
      }

      try {
        // جلب بيانات المستخدم
        const userRes = await fetch(`/api/profile/${email}`);
        const user = await userRes.json();
        originalUser = {
          college: user.college || "",
          emergency1: (user.emergencyContacts?.split(",")[0]) || "",
          emergency2: (user.emergencyContacts?.split(",")[1]) || ""
        };

        document.getElementById("firstName").value = user.name?.split(" ")[0] || "";
        document.getElementById("lastName").value = user.name?.split(" ")[1] || "";
        document.getElementById("email").value = user.email || "";
        document.getElementById("college").value = originalUser.college;
        document.getElementById("emergency1").value = originalUser.emergency1;
        document.getElementById("emergency2").value = originalUser.emergency2;

        // جلب بيانات السائق
        const driverRes = await fetch(`/api/profile/driver/${email}`);
        const result = await driverRes.json();

        if (result.isDriver && result.driverData) {
          const driverData = result.driverData;
          originalDriver = {
            carModel: driverData.carModel || "",
            carColor: driverData.carColor || "",
            seatsAvailable: driverData.seatsAvailable || "",
          };

          document.getElementById('licenseNumber').value = driverData.licenseNumber || '';
          document.getElementById('carModel').value = originalDriver.carModel;
          document.getElementById('carColor').value = originalDriver.carColor;
          document.getElementById('carPlate').value = driverData.carPlate || '';
          document.getElementById('seatsAvailable').value = originalDriver.seatsAvailable;
        } else {
          alert("أنت غير مسجل كسائق");
        }

      } catch (error) {
        console.error('Error loading settings:', error);
        alert("حدث خطأ في تحميل البيانات");
      }
    }

    function isUserDataChanged() {
      return (
        document.getElementById("college").value !== originalUser.college ||
        document.getElementById("emergency1").value !== originalUser.emergency1 ||
        document.getElementById("emergency2").value !== originalUser.emergency2 ||
        document.getElementById("currentPassword").value !== ""
      );
    }

    function isDriverDataChanged() {
      return (
        document.getElementById("carModel").value !== originalDriver.carModel ||
        document.getElementById("carColor").value !== originalDriver.carColor ||
        document.getElementById("seatsAvailable").value !== originalDriver.seatsAvailable
      );
    }

    function checkFormChanges() {
      const saveBtn = document.getElementById("saveBtn");
      if (isUserDataChanged() || isDriverDataChanged()) {
        saveBtn.disabled = false;
      } else {
        saveBtn.disabled = true;
      }
    }

    // تحديث بيانات المستخدم
    async function updateUserProfile() {
      const email = localStorage.getItem("userEmail");
      const emergencyCombined =
        document.getElementById("emergency1").value + "," +
        document.getElementById("emergency2").value;

      const userUpdate = {
        college: document.getElementById("college").value,
        emergencyContacts: emergencyCombined,
        password: document.getElementById("currentPassword").value || null
      };

      try {
        const response = await fetch(`/api/profile/update/${email}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(userUpdate)
        });

        return response.ok;

      } catch (error) {
        console.error('Error updating user profile:', error);
        alert("حدث خطأ في تحديث البيانات الشخصية");
        return false;
      }
    }

    // تحديث بيانات السائق
    async function updateDriverSettings() {
      const email = localStorage.getItem("userEmail");
      const updateData = {
        licenseNumber: document.getElementById('licenseNumber').value,
        carModel: document.getElementById('carModel').value,
        carColor: document.getElementById('carColor').value,
        seatsAvailable: parseInt(document.getElementById('seatsAvailable').value) || 0
      };

      if (!updateData.licenseNumber || !updateData.carModel || !updateData.carColor || updateData.seatsAvailable <= 0) {
        alert("يرجى ملء جميع الحقول المطلوبة بشكل صحيح");
        return false;
      }

      try {
        const response = await fetch(`/api/profile/driver/update/${email}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(updateData)
        });

        return response.ok;

      } catch (error) {
        console.error('Error updating driver settings:', error);
        alert("حدث خطأ في تحديث بيانات السائق");
        return false;
      }
    }

    document.getElementById("driverSettingsForm").addEventListener("submit", async (e) => {
      e.preventDefault();

      const driverUpdated = await updateDriverSettings();
      const userUpdated = await updateUserProfile();

      if (driverUpdated && userUpdated) {
        alert("تم حفظ جميع التغييرات بنجاح!");
        window.location.href = "driverUI.html";
      } else if (driverUpdated) {
        alert("تم تحديث بيانات السيارة فقط");
        window.location.href = "driverUI.html";
      } else if (userUpdated) {
        alert("تم تحديث البيانات الشخصية فقط");
        window.location.href = "driverUI.html";
      } else {
        alert("لم يتم تعديل أي بيانات");
      }
    });

    document.addEventListener("DOMContentLoaded", () => {
      loadDriverSettings();

      const inputsToWatch = [
        "college", "emergency1", "emergency2",
        "currentPassword", "carModel", "carColor", "seatsAvailable"
      ];

      inputsToWatch.forEach(id => {
        const input = document.getElementById(id);
        if (input) {
          input.addEventListener("input", checkFormChanges);
        }
      });
    });
    function startPasswordChange() {
      localStorage.setItem("passwordFlow", "driverSetting");
      window.location.href = "verifyEmail.html";
    }

