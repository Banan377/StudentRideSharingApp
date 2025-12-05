class RatingBridge {
    constructor() {
        this.API_URL = 'http://localhost:8080/api/ratings';
        this.init();
    }

    init() {
        console.log('جسر التقييمات مفعل');
    }

    async submitRating(ratingData) {
        try {
            console.log('إرسال التقييم عبر الجسر:', ratingData);

            const response = await fetch(this.API_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(ratingData)
            });

            const result = await response.json();
            console.log('نتيجة الإرسال:', result);

            if (result.success) {
                this.triggerPageUpdate();
            }

            return result;

        } catch (error) {
            console.error(' خطأ في الإرسال:', error);
            return { success: false, error: error.message };
        }
    }

    triggerPageUpdate() {
        localStorage.setItem('rating_updated', Date.now().toString());
        
        window.dispatchEvent(new CustomEvent('ratingUpdated'));
        
        console.log(' تم إشعار صفحة التقييمات بالتحديث');
    }

    listenForUpdates() {
        window.addEventListener('storage', (event) => {
            if (event.key === 'rating_updated') {
                console.log('تم استلام تحديث جديد');
                this.refreshRatingData();
            }
        });

        window.addEventListener('ratingUpdated', () => {
            console.log(' تم استلام event تحديث');
            this.refreshRatingData();
        });

        setInterval(() => {
            this.refreshRatingData();
        }, 10000);
    }

    async refreshRatingData() {
        try {
            const [averageResponse, ratingsResponse] = await Promise.all([
                fetch(`${this.API_URL}/user/1/average`),
                fetch(`${this.API_URL}/user/1`)
            ]);

            const averageData = await averageResponse.json();
            const ratingsData = await ratingsResponse.json();

            this.updateUI(averageData, ratingsData);

        } catch (error) {
            console.log(' استخدام البيانات الحالية');
        }
    }

    updateUI(averageData, ratingsData) {
        if (averageData.success) {
            const ratingNumber = document.querySelector('.rating-value, .rating-number, #ratingNumber');
            const ratingCount = document.querySelector('.rating-label, .rating-count, #ratingCount');
            const starsContainer = document.querySelector('.stars, #starsContainer');
            
            if (ratingNumber) {
                ratingNumber.textContent = averageData.averageRating.toFixed(1);
            }
            if (ratingCount) {
                ratingCount.textContent = `تقييم (${averageData.totalRatings})`;
            }
            if (starsContainer) {
                this.updateStars(starsContainer, averageData.averageRating);
            }
        }

        if (ratingsData.success && ratingsData.ratings.length > 0) {
            this.updateRatingsList(ratingsData.ratings);
        }
    }

    updateStars(container, average) {
        container.innerHTML = '';
        const fullStars = Math.floor(average);
        const hasHalfStar = average % 1 >= 0.5;
        
        for (let i = 0; i < 5; i++) {
            const star = document.createElement('i');
            if (i < fullStars) {
                star.className = 'fas fa-star';
            } else if (i === fullStars && hasHalfStar) {
                star.className = 'fas fa-star-half-alt';
            } else {
                star.className = 'far fa-star';
            }
            container.appendChild(star);
        }
    }

    updateRatingsList(ratings) {
        const container = document.querySelector('.ratings-list, .ratings-container, #ratingsContainer');
        if (!container) return;

        container.innerHTML = '';

        ratings.forEach(rating => {
            const ratingElement = document.createElement('div');
            ratingElement.className = 'rating-item';
            ratingElement.innerHTML = this.createRatingHTML(rating);
            container.appendChild(ratingElement);
        });
    }

    createRatingHTML(rating) {
        const date = new Date(rating.createdAt).toLocaleDateString('ar-SA');
        const stars = '★'.repeat(rating.ratingValue) + '☆'.repeat(5 - rating.ratingValue);
        
        return `
            <div style="display: flex; justify-content: space-between; margin-bottom: 8px;">
                <div style="font-weight: bold;">السائق #${rating.raterId}</div>
                <div style="color: #888; font-size: 12px;">${date}</div>
            </div>
            <div style="color: #ffc107; margin-bottom: 8px;">${stars} ${rating.ratingValue}.0</div>
            ${rating.comment ? `<div style="color: #333; background: #f8f9fa; padding: 8px; border-radius: 6px;">${rating.comment}</div>` : ''}
        `;
    }
}

window.ratingBridge = new RatingBridge();