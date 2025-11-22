const signupForm = document.getElementById('signupForm');
const emailInput = document.getElementById('Email');
const firstName = document.getElementById('firstName');
const lastName = document.getElementById('lastName');
const college = document.getElementById('college');
const genderRadios = document.getElementsByName('gender');
const password = document.getElementById('password');
const confirmPassword = document.getElementById('confirmPassword');
const emergency1 = document.getElementById('emergency1');
const emergency2 = document.getElementById('emergency2');
const isDriverCheckbox = document.getElementById('isDriver');
const license = document.getElementById('license');
const registration = document.getElementById('registration');
const licenseNumber = document.getElementById('licenseNumber');
const carModel = document.getElementById('carModel');
const carColor = document.getElementById('carColor');
const carPlate = document.getElementById('carPlate');
const seatsAvailable = document.getElementById('seatsAvailable');

// عناصر عرض الحالة
const passwordHelp = document.getElementById('passwordHelp');
const confirmHelp = document.getElementById('confirmHelp');
const emergency1Help = document.getElementById('emergency1Help') || createHelpElement('emergency1Help');
const emergency2Help = document.getElementById('emergency2Help') || createHelpElement('emergency2Help');

// دالة لإنشاء عناصر المساعدة إذا ما موجودة
function createHelpElement(id) {
    const helpElement = document.createElement('div');
    helpElement.id = id;
    helpElement.style.color = 'red';
    helpElement.style.fontSize = '0.9em';
    helpElement.style.marginTop = '5px';
    return helpElement;
}

// ========== تحقق رقم الطوارئ السعودي ==========
function validateSaudiPhone(phoneNumber, isRequired = true) {
    if (!phoneNumber && !isRequired) {
        return { isValid: true, message: "" };
    }
    
    if (!phoneNumber && isRequired) {
        return { isValid: false, message: "رقم الطوارئ مطلوب" };
    }

    // تنظيف الرقم من المسافات والشرطات
    const cleanNumber = phoneNumber.replace(/[\s\-]/g, '');
    
    const saudiPattern = /^(05)(5|0|3|6|4|9|1|8|7|2)([0-9]{7})$/;
    
    if (!saudiPattern.test(cleanNumber)) {
        return { 
            isValid: false, 
            message: " رقم غير صحيح. يجب أن يبدأ بـ 05 ويتبعه 8 أرقام" 
        };
    }
    
    const secondDigit = cleanNumber.charAt(2);
    const telecomCompanies = {
        '5': ' شركة الاتصالات السعودية (STC)',
        '0': ' شركة الاتصالات السعودية (STC)',
        '3': ' موبايلي (Mobily)',
        '6': ' موبايلي (Mobily)',
        '4': ' زين (Zain)',
        '9': ' زين (Zain)',
        '1': 'الاتصالات السعودية (STC)',
        '8': ' اتحاد اتصالات (Etihad Atheeb)',
        '7': ' اتحاد اتصالات (Etihad Atheeb)',
        '2': ' الاتصالات السعودية (STC)'
    };
    
    const company = telecomCompanies[secondDigit] || ' شركة اتصالات';
    
    return { 
        isValid: true, 
        message: ` رقم صحيح ` 
    };
}

// ========== تحقق الباسوورد أثناء الكتابة ==========
function validatePassword() {
    const passwordValue = password.value;
    const requirements = {
        length: passwordValue.length >= 8,
        lowercase: /[a-z]/.test(passwordValue),
        uppercase: /[A-Z]/.test(passwordValue),
        number: /\d/.test(passwordValue),
        symbol: /[@$!%*?&]/.test(passwordValue)
    };

    const allValid = Object.values(requirements).every(Boolean);
    
    if (allValid) {
        passwordHelp.innerHTML = " كلمة المرور صحيحة";
        passwordHelp.style.color = "green";
        return true;
    } else {
        let message = " كلمة المرور يجب أن تحتوي على:";
        if (!requirements.length) message += "<br>• 8 خانات على الأقل";
        if (!requirements.lowercase) message += "<br>• حرف صغير واحد على الأقل (a-z)";
        if (!requirements.uppercase) message += "<br>• حرف كبير واحد على الأقل (A-Z)";
        if (!requirements.number) message += "<br>• رقم واحد على الأقل (0-9)";
        if (!requirements.symbol) message += "<br>• رمز خاص واحد على الأقل (@$!%*?&)";
        
        passwordHelp.innerHTML = message;
        passwordHelp.style.color = "red";
        return false;
    }
}

// ========== تحقق تطابق الباسوورد ==========
function validateConfirm() {
    if (confirmPassword.value === password.value && confirmPassword.value !== "") {
        confirmHelp.innerHTML = " كلمتا المرور متطابقتان";
        confirmHelp.style.color = "green";
        return true;
    } else if (confirmPassword.value !== "") {
        confirmHelp.innerHTML = " كلمتا المرور غير متطابقتين";
        confirmHelp.style.color = "red";
        return false;
    } else {
        confirmHelp.innerHTML = "";
        return false;
    }
}

// ========== تحقق أرقام الطوارئ ==========
function validateEmergency1() {
    const result = validateSaudiPhone(emergency1.value, true);
    
    if (!emergency1Help.parentNode) {
        emergency1.parentNode.appendChild(emergency1Help);
    }
    
    emergency1Help.innerHTML = result.message;
    emergency1Help.style.color = result.isValid ? "green" : "red";
    
    return result.isValid;
}

function validateEmergency2() {
    const result = validateSaudiPhone(emergency2.value, false);
    
    if (!emergency2Help.parentNode && emergency2.value) {
        emergency2.parentNode.appendChild(emergency2Help);
    }
    
    if (emergency2Help.parentNode) {
        emergency2Help.innerHTML = result.message;
        emergency2Help.style.color = result.isValid ? "green" : "red";
    }
    
    return emergency2.value ? result.isValid : true;
}

// ========== تحقق عام قبل الإرسال ==========
function validateForm() {
    const isPasswordValid = validatePassword();
    const isConfirmValid = validateConfirm();
    const isEmergency1Valid = validateEmergency1();
    const isEmergency2Valid = validateEmergency2();
    
    if (!isPasswordValid) {
        alert("يرجى إدخال كلمة مرور صحيحة");
        password.focus();
        return false;
    }
    
    if (!isConfirmValid) {
        alert("كلمتا المرور غير متطابقتين");
        confirmPassword.focus();
        return false;
    }
    
    if (!isEmergency1Valid) {
        alert("رقم الطوارئ الأساسي غير صحيح");
        emergency1.focus();
        return false;
    }
    
    if (!isEmergency2Valid) {
        alert("رقم الطوارئ الإضافي غير صحيح");
        emergency2.focus();
        return false;
    }
    
    return true;
}

password.addEventListener('input', validatePassword);
confirmPassword.addEventListener('input', validateConfirm);
emergency1.addEventListener('input', validateEmergency1);
emergency2.addEventListener('input', validateEmergency2);

isDriverCheckbox.addEventListener('change', function () {
    const driverSection = document.getElementById('driverSection');
    driverSection.classList.toggle('show', this.checked);
});

document.addEventListener('DOMContentLoaded', function() {
    const savedEmail = localStorage.getItem("userEmail");
    console.log("Saved email from localStorage:", savedEmail);
    
    if (savedEmail) {
        emailInput.value = savedEmail;
        emailInput.readOnly = true;
        emailInput.style.backgroundColor = "#f0f0f0";
        console.log("Email field populated with:", savedEmail);
    }
    
    if (!document.getElementById('emergency1Help')) {
        emergency1.parentNode.appendChild(emergency1Help);
    }
    if (!document.getElementById('emergency2Help')) {
        emergency2.parentNode.appendChild(emergency2Help);
    }
});

signupForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const email = emailInput.value.trim();
    
    if (!email) {
        alert('يرجى إدخال الإيميل');
        return;
    }
    
    if (!email.endsWith('@sm.imamu.edu.sa')) {
        alert('المستخدم يجب أن يكون من طلاب الجامعة');
        return;
    }

    // تحقق من صحة كل البيانات قبل الإرسال
    if (!validateForm()) {
        return;
    }

    const gender = Array.from(genderRadios).find(r => r.checked)?.value || '';

    // تحديد الـ role بناءً على اختيار السائق
    const role = isDriverCheckbox.checked ? "driver" : "passenger";

    const userData = {
        email: email,
        name: firstName.value + " " + lastName.value,
        password: password.value,
        role: role,
        college: college.value,
        gender: gender,
        emergencyContacts: emergency1.value + (emergency2.value ? ',' + emergency2.value : ''),
        rateAverage: 0,
        status: 'active'
    };

    // إذا كان سائق، أضف بيانات السائق
    if (isDriverCheckbox.checked) {
        userData.driverData = {
            licenseNumber: licenseNumber.value || "", 
            carModel: carModel.value || "",
            carColor: carColor.value || "",
            carPlate: carPlate.value || "",
            seatsAvailable: parseInt(seatsAvailable.value) || 4
        };
    }

    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        });

        let result;
        const contentType = response.headers.get("content-type");
        
        if (contentType && contentType.includes("application/json")) {
            result = await response.json();
        } else {
            result = await response.text();
        }

        if (response.ok) {
            alert(result);
            localStorage.removeItem("userEmail");
            window.location.href = 'login.html';
        } else {
            alert("خطأ: " + (result.message || result));
        }
    } catch (err) {
        console.error(err);
        alert('حدث خطأ أثناء إنشاء الحساب');
    }
});