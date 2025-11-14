// اختيار عناصر النموذج وحقول الإدخال
const otpForm = document.getElementById("otpForm");
const inputs = document.querySelectorAll(".otp-input-field input");
const email = localStorage.getItem("userEmail"); // استرجاع الإيميل من localStorage
const verifyBtn = document.getElementById("verifyBtn");

// أول حقل مفعل والباقي معطّل
inputs.forEach((input, index) => {
  if (index !== 0) input.disabled = true;

  input.addEventListener("keyup", (e) => {
    const nextInput = input.nextElementSibling;
    const prevInput = input.previousElementSibling;

    // منع أكثر من رقم
    if (input.value.length > 1) input.value = "";

    // الانتقال للحقل التالي تلقائيًا
    if (nextInput && nextInput.disabled && input.value !== "") {
      nextInput.removeAttribute("disabled");
      nextInput.focus();
    }

    // الرجوع للحقل السابق عند Backspace
    if (e.key === "Backspace" && prevInput) {
      input.value = "";
      prevInput.focus();
    }

    // تفعيل الزر فقط بعد ملئ كل الحقول
    const allFilled = Array.from(inputs).every(inp => inp.value !== "");
    verifyBtn.disabled = !allFilled;
  });
});


window.addEventListener("load", () => inputs[0].focus());


otpForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const otpCode = Array.from(inputs).map(i => i.value).join("");

  if (otpCode.length !== 6) {
    alert("الرجاء إدخال كود مكون من 6 أرقام");
    return;
  }

  try {
    const response = await fetch("/api/auth/verify-otp", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, otpCode }),
    });

    const result = await response.text();
    alert(result);

    if (result.toLowerCase().includes("success")) {
      window.location.href = "signup-form.html"; 
    }
  } catch (err) {
    console.error(err);
    alert("حدث خطأ أثناء التحقق من الكود.");
  }
});
