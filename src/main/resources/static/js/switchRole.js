 function showPendingDriverModal() {
      document.getElementById('pendingDriverModal').classList.remove('hidden');
    }

    function closePendingDriverModal() {
      document.getElementById('pendingDriverModal').classList.add('hidden');
    }

    async function switchRole() {
      const email = localStorage.getItem("userEmail");

      if (!email) {
        alert("يجب تسجيل الدخول أولاً");
        window.location.href = "login.html";
        return;
      }

      try {
        console.log(" التحقق من حالة السائق لـ:", email);

        const response = await fetch(`/api/switch/checkDriverStatus?email=${encodeURIComponent(email)}`);

        if (!response.ok) {
          throw new Error('فشل في التحقق من حالة السائق');
        }

        const result = await response.json();
        console.log(" نتيجة التحقق:", result);

        if (result.status === "verified_driver") {
          console.log(" سائق موثق");
          showConfirmedDriverModal();

        } else if (result.status === "pending_driver") {
          console.log("طلب تحت المعالجة");
          showPendingDriverModal();
        } else if (result.status === "not_driver") {
          console.log(" غير مسجل كسائق");
          showDriverCheckModal(
            "هل ترغب في التسجيل كسائق؟",
            () => openDriverRegistrationForm()
          );

        } else if (result.status === "rejected_driver") {
          alert("عذرًا، تم رفض طلبك كسائق. يمكنك المحاولة مجددًا.");
        }
        else {
          alert("حدث خطأ أثناء التحقق من حالتك كسائق.");
        }


      } catch (error) {
        console.error('Error in switchRole:', error);
        alert("حدث خطأ في النظام. يرجى المحاولة مرة أخرى.");
      }
    }



    async function checkDriverStatus(email) {
      try {
        const response = await fetch(`/api/switch/checkDriverStatus?email=${encodeURIComponent(email)}`);

        if (!response.ok) {
          throw new Error('فشل في التحقق من حالة السائق');
        }

        const result = await response.json();
        return result.status;

      } catch (error) {
        console.error('Error checking driver status:', error);
        return "no_driver_data";
      }
    }

    async function registerDriver(driverData) {
      try {
        const response = await fetch('/api/switch/registerDriver', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(driverData)
        });

        if (!response.ok) throw new Error('Registration failed');
        return await response.json();
      } catch (error) {
        console.error('Error registering driver:', error);
        return { success: false };
      }
    }

    function showDriverCheckModal(message, onConfirm) {
      const modal = document.getElementById('driverCheckModal');
      const messageElement = document.getElementById('driverCheckMessage');
      const confirmBtn = document.getElementById('confirmYesBtn');

      messageElement.textContent = message;
      confirmBtn.onclick = onConfirm;

      modal.classList.remove('hidden');
    }

    function closeDriverCheckModal() {
      document.getElementById('driverCheckModal').classList.add('hidden');
    }

    function openDriverRegistrationForm() {
      closeDriverCheckModal();
      document.getElementById("uploadModal").classList.remove("hidden");
    }

    function closeUploadModal() {
      document.getElementById("uploadModal").classList.add("hidden");
    }

    document.getElementById('uploadForm').addEventListener('submit', async function (e) {
      e.preventDefault();

      const email = localStorage.getItem("userEmail");
      if (!email) {
        alert("يجب تسجيل الدخول أولاً");
        return;
      }

      const driverData = {
        email: email,
        licenseNumber: document.getElementById('licenseNumber').value,
        carModel: document.getElementById('carModel').value,
        carColor: document.getElementById('carColor').value,
        plateNumber: document.getElementById('plateNumber').value,
        availableSeats: parseInt(document.getElementById('availableSeats').value),
        licenseFile: document.getElementById('licenseUpload').files[0]?.name || '' // اسم الملف فقط
      };

      // التحقق من صحة البيانات
      if (!validateDriverData(driverData)) {
        return;
      }

      // إرسال البيانات للتسجيل
      const result = await registerDriver(driverData);

      if (result.success) {
        //  بعد التسجيل الناجح - الانتقال لواجهة السائق
        alert("تم ارسال الطلب بنجاح، انتظر 48 ساعة ");
        window.location.href = "passengerRides.html";
      } else {
        alert("حدث خطأ في التسجيل: " + (result.message || "يرجى المحاولة مرة أخرى."));
      }
    });

    // التحقق من صحة البيانات
    function validateDriverData(data) {
      if (!data.licenseNumber.trim()) {
        alert("يرجى إدخال رقم رخصة القيادة");
        return false;
      }

      if (!data.carModel.trim()) {
        alert("يرجى إدخال موديل السيارة");
        return false;
      }

      if (!data.carColor.trim()) {
        alert("يرجى إدخال لون السيارة");
        return false;
      }

      if (!data.plateNumber.trim()) {
        alert("يرجى إدخال رقم لوحة السيارة");
        return false;
      }

      if (!data.availableSeats || data.availableSeats < 1) {
        alert("يرجى إدخال عدد مقاعد صحيح");
        return false;
      }

      const licenseFile = document.getElementById('licenseUpload').files[0];
      if (!licenseFile) {
        alert("يرجى رفع صورة رخصة القيادة");
        return false;
      }

      return true;
    }
    function showConfirmedDriverModal() {
      document.getElementById('confirmedDriverModal').classList.remove('hidden');
    }

    function closeConfirmedDriverModal() {
      document.getElementById('confirmedDriverModal').classList.add('hidden');
    }

    function goToDriverUI() {
      window.location.href = "driverUI.html";
    }