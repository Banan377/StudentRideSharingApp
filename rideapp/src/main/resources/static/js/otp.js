const otpForm = document.getElementById("otpForm");
const inputs = document.querySelectorAll(".otp-input-field input");
const email = localStorage.getItem("userEmail");
const verifyBtn = document.getElementById("verifyBtn");
const resendLink = document.getElementById("resendLink");
const timerBox = document.getElementById("timerBox");
const timerSpan = document.getElementById("timer");

let countdown = null;


inputs.forEach((input, index) => {
  if (index !== 0) input.disabled = true;

  input.addEventListener("keyup", (e) => {
    const next = input.nextElementSibling;
    const prev = input.previousElementSibling;

    if (input.value.length > 1) {
      input.value = input.value.charAt(0);
    }

    if (input.value !== "" && next) {
      next.removeAttribute("disabled");
      next.focus();
    }

    if (e.key === "Backspace") {
      input.value = "";
      if (prev) {
        prev.value = "";
        prev.focus();
      }
    }
  });
});


function startTimer(durationSeconds = 120) {
  let timeLeft = durationSeconds;

  resendLink.style.display = "none";
  timerBox.style.display = "block";

  if (countdown) clearInterval(countdown);

  updateTimerUI(timeLeft);

  countdown = setInterval(() => {
    timeLeft--;
    updateTimerUI(timeLeft);

    if (timeLeft < 0) {
      clearInterval(countdown);
      timerBox.style.display = "none";
      resendLink.style.display = "inline";
    }
  }, 1000);
}

function updateTimerUI(timeLeft) {
  let minutes = Math.floor(timeLeft / 60);
  let seconds = timeLeft % 60;

  timerSpan.textContent =
    `${minutes.toString().padStart(2, "0")}:${seconds
      .toString()
      .padStart(2, "0")}`;
}

startTimer(120);

otpForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const otpCode = Array.from(inputs).map(i => i.value).join("");

  if (otpCode.length !== inputs.length) {
    alert("من فضلك أدخل جميع أرقام رمز التحقق");
    return;
  }

  try {
    const response = await fetch("/api/auth/verify-otp", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, otpCode }),
    });

    const result = await response.json();
    alert(result.message);

   if (response.ok) {
    const resetMode = localStorage.getItem("resetPasswordMode") === "true";

    // تنظيف
    localStorage.removeItem("resetPasswordMode");
    localStorage.removeItem("userEmail");

    if (resetMode) {
        window.location.href = "forgotPassword.html";
    } else {
        window.location.href = "signup-form.html";
    }
}

    
  } catch (err) {
    alert("حدث خطأ أثناء التحقق من الكود.");
  }
});


resendLink.addEventListener("click", async () => {
  try {
    const response = await fetch("/api/auth/send-otp", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        email,
        resetPasswordMode: localStorage.getItem("resetPasswordMode") === "true"
      }),
    });

    const result = await response.json();
    alert(result.message);

    if (response.ok) startTimer(120);

  } catch (err) {
    alert("حدث خطأ أثناء إعادة إرسال الكود.");
  }
});
