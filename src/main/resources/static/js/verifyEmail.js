 const emailInput = document.getElementById('email');
    const sendBtn = document.getElementById('sendBtn');
    const errorDiv = document.getElementById('error');

    sendBtn.addEventListener('click', async () => {
      const email = emailInput.value.trim().toLowerCase();
      sendBtn.disabled = true;
      sendBtn.textContent = "جاري الإرسال...";
      errorDiv.style.display = "none";

      if (!email.endsWith("@sm.imamu.edu.sa")) {
        errorDiv.textContent = " البريد غير صحيح، يجب أن ينتهي بـ @sm.imamu.edu.sa";
        errorDiv.style.display = "block";
        return;
      }

      try {
        const response = await fetch('/api/auth/send-otp', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            email,
            otpType: "password_reset",
            resetPasswordMode: true
          })

        });

        const result = await response.json();

        if (!response.ok) {
          errorDiv.textContent = result.message;
          errorDiv.style.display = "block";
          return;
        }

        localStorage.setItem("userEmail", email);
        window.location.href = "otp.html";




        localStorage.setItem("userEmail", email);
        localStorage.setItem("resetPasswordMode", "true");

        window.location.href = "otp.html";

      } catch (err) {
        errorDiv.textContent = "خطأ أثناء إرسال رمز التحقق";
        errorDiv.style.display = "block";
      }
    });


    const mode = localStorage.getItem("passwordFlow");


    const title = document.querySelector("h2");
    const lead = document.querySelector(".lead");
    const returnLink = document.getElementById("returnLink");

    if (mode === "forgot") {
      title.textContent = "إعادة تعيين كلمة المرور";
      lead.textContent = "أدخل بريدك لإرسال رمز استعادة كلمة المرور.";
      returnLink.innerHTML = `
    <div style="text-align:center;">
      <a href="login.html" style="color:#265262; font-size:14px; font-weight:bold;">
        العودة إلى تسجيل الدخول
      </a>
    </div>`;
    }

    else if (mode === "passengerSetting") {
      title.textContent = "تأكيد البريد لتغيير كلمة المرور";
      lead.textContent = "سنرسل رمز تأكيد لتعديل كلمة المرور من الإعدادات.";
      returnLink.innerHTML = `
    <div style="text-align:center;">
      <a href="passengerSetting.html" style="color:#265262; font-size:14px; font-weight:bold;">
        العودة إلى الإعدادات
      </a>
    </div>`;
    }

    else if (mode === "driverSetting") {
      title.textContent = "تأكيد البريد لتغيير كلمة المرور";
      lead.textContent = "سنرسل رمز تأكيد لتعديل كلمة المرور من الإعدادات.";
      returnLink.innerHTML = `
    <div style="text-align:center;">
      <a href="driverSetting.html" style="color:#265262; font-size:14px; font-weight:bold;">
        العودة إلى الإعدادات
      </a>
    </div>`;
    }
