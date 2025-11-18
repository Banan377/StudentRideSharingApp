const otpForm = document.getElementById("otpForm");
const inputs = document.querySelectorAll(".otp-input-field input");
const email = localStorage.getItem("userEmail");
const verifyBtn = document.getElementById("verifyBtn");

inputs.forEach((input, index) => {
  if (index !== 0) input.disabled = true;

  input.addEventListener("keyup", (e) => {
    const nextInput = input.nextElementSibling;
    const prevInput = input.previousElementSibling;

    if (input.value.length > 1) input.value = "";

    if (nextInput && nextInput.disabled && input.value !== "") {
      nextInput.removeAttribute("disabled");
      nextInput.focus();
    }

    if (e.key === "Backspace" && prevInput) {
      input.value = "";
      prevInput.focus();
    }

    verifyBtn.disabled = !Array.from(inputs).every(inp => inp.value !== "");
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

  
    const contentType = response.headers.get("content-type");
    let result;

    if (contentType && contentType.includes("application/json")) {
      result = await response.json();
    } else {
      result = await response.text();
    }

    if (response.ok) {
      if (result.success || (typeof result === 'string' && result.toLowerCase().includes("نجاح")) ||
        (result.message && result.message.toLowerCase().includes("نجاح"))) {

        localStorage.setItem("userEmail", email);
        alert("تم التحقق بنجاح");
        window.location.href = "signup-form.html";
      } else {
        alert(result.message || result);
      }
    } else {
      alert(result.message || result || "حدث خطأ أثناء التحقق");
    }

  } catch (err) {
    console.error(err);
    alert("حدث خطأ أثناء التحقق من الكود.");
  }
});