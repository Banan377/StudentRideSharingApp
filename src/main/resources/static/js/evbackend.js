const express = require('express');
const cors = require('cors');       

const app = express();

app.use(cors());         
app.use(express.json());  

const tripData = { /* بياناتك هنا */ };

app.get('/api/trip', (req, res) => {
    res.json(tripData); 
});

app.post('/api/trips', (req, res) => {
    console.log('وصلت بيانات:', req.body); 
    res.json({ success: true });
});

app.listen(3000, () => console.log(' السيرفر شغال على 3000'));