 const resetForm = document.getElementById("resetForm");
        const msg = document.getElementById("message");

        const email = localStorage.getItem("userEmail");
        const flow = localStorage.getItem("passwordFlow");

        // رابط العودة حسب المصدر
        const resetReturn = document.getElementById("resetReturn");
        if (flow === "forgot") {
            resetReturn.innerHTML = `<a href="login.html" style="color:#265262; font-weight:bold">العودة إلى تسجيل الدخول</a>`;
        } else if (flow === "passengerSetting") {
            resetReturn.innerHTML = `<a href="passengerSetting.html" style="color:#265262; font-weight:bold">العودة إلى الإعدادات</a>`;
        } else if (flow === "driverSetting") {
            resetReturn.innerHTML = `<a href="driverSetting.html" style="color:#265262; font-weight:bold">العودة إلى الإعدادات</a>`;
        }

        resetForm.addEventListener("submit", async (e) => {
            e.preventDefault();

            const newPassword = document.getElementById("newPassword").value.trim();
            const confirmPassword = document.getElementById("confirmPassword").value.trim();

            if (newPassword !== confirmPassword) {
                msg.textContent = "كلمتا المرور غير متطابقتين";
                msg.className = "error";
                msg.style.display = "block";
                return;
            }

            const response = await fetch("/api/auth/reset-password", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, newPassword })
            });

            const result = await response.json();

            msg.textContent = result.message;
            msg.style.display = "block";

            if (response.ok) {
                msg.className = "success";

                setTimeout(() => {
                    localStorage.removeItem("userEmail");
                    window.location.href = "login.html";
                }, 1500);
            } else {
                msg.className = "error";
            }
        });