const express = require('express');
const app = express();

// السماح للفرونت إند بالاتصال
app.use((req, res, next) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', 'Content-Type');
    next();
});

app.use(express.json());

// ========== نظام الدفع ==========
app.post('/api/process-payment', (req, res) => {
    console.log(' طلب دفع جديد:', req.body);
    
    const { amount, method, cardNumber } = req.body;
    
    // تحقق من البيانات
    if (!amount || !method) {
        return res.status(400).json({ 
            success: false, 
            message: 'بيانات الدفع ناقصة' 
        });
    }
    
    // محاكاة عملية الدفع (تنجح دائماً للتجربة)
    const transactionId = 'PAY-' + Date.now() + '-' + Math.random().toString(36).substr(2, 6).toUpperCase();
    
    // الرد الناجح
    res.json({
        success: true,
        message: ' تمت عملية الدفع بنجاح',
        transactionId: transactionId,
        amount: amount,
        method: method,
        timestamp: new Date().toLocaleString('ar-SA'),
        reference: 'الرجاء الاحتفاظ برقم المرجع: ' + transactionId
    });
});

// رابط لفحص إذا السيرفر شغال
app.get('/api/check', (req, res) => {
    res.json({ status: 'active', service: 'payment', port: 8083 });
});

// شغل السيرفر على 8083
app.listen(8083, () => {
    console.log('💳 سيرفر الدفع شغال على: http://localhost:8083');
    console.log(' رابط الدفع: POST http://localhost:8083/api/process-payment');
});