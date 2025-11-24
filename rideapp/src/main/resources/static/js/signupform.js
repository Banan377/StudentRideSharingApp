// ---------------------- عناصر الفورم ----------------------
const signupForm = document.getElementById('signupForm');
const emailInput = document.getElementById('signupEmail');
const firstName = document.getElementById('firstName');
const lastName = document.getElementById('lastName');
const college = document.getElementById('college');
const genderRadios = document.getElementsByName('gender');
const password = document.getElementById('password');
const confirmPassword = document.getElementById('confirmPassword');
const emergency1 = document.getElementById('emergency1');
const emergency2 = document.getElementById('emergency2');
const isDriverCheckbox = document.getElementById('isDriver');

const licenseNumber = document.getElementById('licenseNumber');
const carModel = document.getElementById('carModel');
const carColor = document.getElementById('carColor');
const carPlate = document.getElementById('carPlate');
const seatsAvailable = document.getElementById('seatsAvailable');

const license = document.getElementById('license');
const registration = document.getElementById('registration');

const passwordHelp = document.getElementById('passwordHelp');
const confirmHelp = document.getElementById('confirmHelp');

document.addEventListener("DOMContentLoaded", () => {
    const savedEmail = localStorage.getItem("userEmail");
    if (savedEmail) {
        emailInput.value = savedEmail;
        emailInput.readOnly = true;
        emailInput.style.background = "#ececec";
    }
});

function validateRequiredFields() {

    if (!emailInput.value.trim() ||
        !firstName.value.trim() ||
        !lastName.value.trim() ||
        !college.value.trim() ||
        !password.value.trim() ||
        !confirmPassword.value.trim() ||
        !emergency1.value.trim()) {

        alert("يرجى تعبئة جميع الحقول الإلزامية");
        return false;
    }

    const selectedGender = Array.from(genderRadios).some(r => r.checked);
    if (!selectedGender) {
        alert("يرجى اختيار الجنس");
        return false;
    }

    if (isDriverCheckbox.checked) {

        if (!licenseNumber.value.trim() ||
            !carModel.value.trim() ||
            !carColor.value.trim() ||
            !carPlate.value.trim()) {

            alert("يرجى تعبئة جميع بيانات السائق");
            return false;
        }

        if (license.files.length === 0) {
            alert("يرجى رفع صورة رخصة القيادة");
            return false;
        }

        if (registration.files.length === 0) {
            alert("يرجى رفع صورة الاستمارة");
            return false;
        }
    }

    return true;
}

function validatePasswordRules(password) {

    const requirements = {
        length: password.length >= 8,
        lowercase: /[a-z]/.test(password),
        uppercase: /[A-Z]/.test(password),
        number: /\d/.test(password),
        symbol: /[@$!%*?&]/.test(password)
    };

    const errors = [];

    if (!requirements.length) errors.push("• يجب أن تكون 8 خانات على الأقل");
    if (!requirements.lowercase) errors.push("• يجب أن تحتوي على حرف صغير واحد (a-z)");
    if (!requirements.uppercase) errors.push("• يجب أن تحتوي على حرف كبير واحد (A-Z)");
    if (!requirements.number) errors.push("• يجب أن تحتوي على رقم واحد");
    if (!requirements.symbol) errors.push("• يجب أن تحتوي على رمز واحد مثل: @ $ ! % * ? &");

    return {
        valid: Object.values(requirements).every(Boolean),
        errors: errors
    };
}

function showPasswordValidation() {
    const pw = password.value;
    const result = validatePasswordRules(pw);

    if (pw.length === 0) {
        passwordHelp.innerHTML = "";
        return;
    }

    if (!result.valid) {
        passwordHelp.innerHTML = result.errors.join("<br>");
        passwordHelp.style.color = "red";
    } else {
        passwordHelp.innerHTML = "كلمة المرور قوية";
        passwordHelp.style.color = "green";
    }
}

function validateConfirm() {
    const ok = password.value === confirmPassword.value && confirmPassword.value !== "";
    confirmHelp.textContent = ok ? "متطابقة" : "غير متطابقة";
    confirmHelp.style.color = ok ? "green" : "red";
    return ok;
}

password.addEventListener("input", validateConfirm);
confirmPassword.addEventListener("input", validateConfirm);


password.addEventListener("input", showPasswordValidation);
confirmPassword.addEventListener("input", validateConfirm);

isDriverCheckbox.addEventListener("change", () => {
    document.getElementById("driverSection").classList.toggle("show", isDriverCheckbox.checked);
});

signupForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    if (!emailInput.value.endsWith("@sm.imamu.edu.sa")) {
        alert("الإيميل يجب أن يكون جامعي");
        return;
    }

    if (!validateRequiredFields()) return;

    const passwordCheck = validatePasswordRules(password.value);
    if (!passwordCheck.valid) {
        alert("كلمة المرور غير مطابقة للشروط");
        return;
    }

    if (!validateConfirm()) return;

    const gender = Array.from(genderRadios).find(r => r.checked)?.value;
    const role = isDriverCheckbox.checked ? "driver" : "passenger";

    const userData = {
        email: emailInput.value.trim(),
        name: firstName.value.trim() + " " + lastName.value.trim(),
        password: password.value,
        role: role,
        college: college.value,
        gender: gender,
        emergencyContacts: emergency1.value + (emergency2.value ? "," + emergency2.value : ""),
        rateAverage: 0,
        status: "active"
    };

    if (role === "driver") {
        userData.driverData = {
            licenseNumber: licenseNumber.value,
            carModel: carModel.value,
            carColor: carColor.value,
            carPlate: carPlate.value,
            seatsAvailable: seatsAvailable.value
        };
    }

    try {
        const response = await fetch("/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userData)
        });

        const result = await response.text();

        if (response.ok) {
            alert("تم إنشاء الحساب بنجاح");
            localStorage.removeItem("userEmail");
            window.location.href = "login.html";
        } else {
            alert("خطأ: " + result);
        }

    } catch (err) {
        alert("خطأ في السيرفر");
        console.error(err);
    }
});
