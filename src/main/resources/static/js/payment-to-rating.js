document.addEventListener('DOMContentLoaded', function() {
    if (document.getElementById('farePerPassenger')) {
        initPaymentPage();
    }
});

function initPaymentPage() {
    const originalProcessPayment = window.processPayment;
    
    if (typeof originalProcessPayment === "function") {
    originalProcessPayment();
}

        
        // الانتقال إلى صفحة التقييم بعد نجاح الدفع
        setTimeout(function() {
            window.location.href = 'rating.html';
        }, 1500);
    };

