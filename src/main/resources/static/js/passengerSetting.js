let originalData = {
      college: "",
      emergency1: "",
      emergency2: ""
    };

    function checkFormChanges() {
      const currentCollege = document.getElementById("college").value;
      const currentEmergency1 = document.getElementById("emergency1").value;
      const currentEmergency2 = document.getElementById("emergency2").value;

      const changed =
        currentCollege !== originalData.college ||
        currentEmergency1 !== originalData.emergency1 ||
        currentEmergency2 !== originalData.emergency2 ;

        document.getElementById("saveBtn").disabled = !changed;
    }

    document.addEventListener("DOMContentLoaded", async function () {
      const email = localStorage.getItem("userEmail");

      if (!email) {
        alert("يجب تسجيل الدخول أولاً");
        window.location.href = "login.html";
        return;
      }

      try {
        const response = await fetch(`/api/profile/${email}`);
        const user = await response.json();

        document.getElementById("firstName").value = user.name.split(" ")[0] || "";
        document.getElementById("lastName").value = user.name.split(" ")[1] || "";
        document.getElementById("email").value = user.email;
        document.getElementById("college").value = user.college || "";

        if (user.emergencyContacts) {
          const parts = user.emergencyContacts.split(",");
          document.getElementById("emergency1").value = parts[0] || "";
          document.getElementById("emergency2").value = parts[1] || "";
        } else {
          document.getElementById("emergency1").value = "";
          document.getElementById("emergency2").value = "";
        }

        originalData.college = document.getElementById("college").value;
        originalData.emergency1 = document.getElementById("emergency1").value;
        originalData.emergency2 = document.getElementById("emergency2").value;

        document.querySelector(".submit-btn").disabled = true;

      } catch (err) {
        alert("خطأ في تحميل البيانات");
      }

      const inputsToWatch = [
        document.getElementById("college"),
        document.getElementById("emergency1"),
        document.getElementById("emergency2"),
      ];

      inputsToWatch.forEach(input => {
        input.addEventListener("input", checkFormChanges);
      });
    });

    document.getElementById("passengerSettingsForm").addEventListener("submit", async function (e) {
      e.preventDefault();

      const college = document.getElementById("college").value;
      const emergency1 = document.getElementById("emergency1").value;
      const emergency2 = document.getElementById("emergency2").value;
      const email = document.getElementById("email").value;

      const emergencyCombined = emergency1 + "," + emergency2;

      if (
        college === originalData.college &&
        emergency1 === originalData.emergency1 &&
        emergency2 === originalData.emergency2 
      ) {
        alert("لم يتم تعديل أي بيانات.");
        return;
      }

      const updatedUser = {
        email: email,
        college: college,
        emergencyContacts: emergencyCombined,
      };

      try {
        const response = await fetch(`/api/profile/update/${email}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(updatedUser)
        });

        const result = await response.text();
        alert(result);
        window.location.href = "passengerRides.html";

      } catch (err) {
        alert("حدث خطأ أثناء تحديث البيانات");
      }
    });


    function startPasswordChange() {
      localStorage.setItem("passwordFlow", "passengerSetting");
      window.location.href = "verifyEmail.html";
    }
