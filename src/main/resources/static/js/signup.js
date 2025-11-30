const signupForm = document.getElementById('signupForm');
const emailInput = document.getElementById('Email');
const sendOtpBtn = document.getElementById('signupBtn');

signupForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    sendOtpBtn.disabled = true;
    sendOtpBtn.textContent = "جاري الإرسال...";

    const email = emailInput.value.trim().toLowerCase();

    if (!email.endsWith('@sm.imamu.edu.sa')) {
        alert('المستخدم يجب أن يكون من طلاب جامعة الإمام محمد بن سعود الإسلامية');
        sendOtpBtn.disabled = false;
        sendOtpBtn.textContent = "إنشاء حساب";
        return;
    }

    try {
        const response = await fetch('/api/auth/send-otp', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                email,
                otpType: "signup"   
            })

        });

        if (response.ok) {
            localStorage.setItem("userEmail", email);
            localStorage.setItem("passwordFlow", "signup");
            window.location.href = "otp.html";
        } else {
            const data = await response.json();
            if (data.message && data.message.includes("تم إرسال كود مسبقًا")) {
                alert(data.message);
                localStorage.setItem("userEmail", email);
                localStorage.setItem("passwordFlow", "signup");
                window.location.href = "otp.html";
                return;
            }

            alert("Error: " + data.message);
        }

    } catch (err) {
        console.error(err);
        alert('حصل خطأ، أعد المحاولة مرة أخرى');
    } finally {
        sendOtpBtn.disabled = false;
        sendOtpBtn.textContent = "إنشاء حساب";
    }
});
