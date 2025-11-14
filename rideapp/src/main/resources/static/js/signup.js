const signupForm = document.getElementById('signupForm');
const emailInput = document.getElementById('Email');



signupForm.addEventListener('submit', async (e) => {
    e.preventDefault(); 
    const email = emailInput.value.trim();

    if (!email.endsWith('@sm.imamu.edu.sa')) {
        alert('المستخدم يجب أن يكون من طلاب جامعة الامام محمد بن سعود الاسلامية');
        return;
    }

    try {
        const response = await fetch('/api/auth/send-otp', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email })
        });

        if (response.ok) {
            localStorage.setItem("userEmail", email); 
            alert('تم إرسال كود التحقق');
            window.location.href = 'otp.html'; 
        } else {
            const data = await response.json();
            alert('Error: ' + data.message);
        }
    } catch (err) {
        console.error(err);
        alert('حصل خطأ أعد المحاولة مرة أخرى');
    }
});
