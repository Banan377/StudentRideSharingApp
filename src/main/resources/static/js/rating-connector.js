class RatingSystem {
    constructor() {
        this.API_URL = 'http://localhost:8080/api/ratings';
        this.userId = 1;
    }

    async submitRating(raterId, ratedUserId, rideId, stars, comment) {
        try {
            const ratingData = {
                raterId: raterId,
                ratedUserId: ratedUserId,
                rideId: rideId,
                ratingValue: stars,
                comment: comment
            };

            console.log(' إرسال التقييم:', ratingData);

            const response = await fetch(this.API_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(ratingData)
            });

            const result = await response.json();
            console.log(' النتيجة:', result);

            if (result.success) {
                this.notifyAllPages();
                return { success: true, message: 'تم التقييم بنجاح' };
            } else {
                return { success: false, message: result.error };
            }

        } catch (error) {
            console.error(' خطأ:', error);
            return { success: false, message: 'خطأ في الاتصال' };
        }
    }

    notifyAllPages() {
        if (window.updateRatingPage) {
            window.updateRatingPage();
        }
        
        localStorage.setItem('rating_updated', Date.now().toString());
    }

    async getRatings(userId) {
        try {
            const response = await fetch(`${this.API_URL}/user/${userId}`);
            return await response.json();
        } catch (error) {
            console.error('خطأ في جلب التقييمات:', error);
            return { success: false, ratings: [] };
        }
    }

    async getAverageRating(userId) {
        try {
            const response = await fetch(`${this.API_URL}/user/${userId}/average`);
            return await response.json();
        } catch (error) {
            console.error('خطأ في جلب المتوسط:', error);
            return { success: false, averageRating: 0, totalRatings: 0 };
        }
    }
}

window.ratingSystem = new RatingSystem();

window.submitSimpleRating = async function(stars, comment) {
    return await window.ratingSystem.submitRating(1, 2, Date.now(), stars, comment);
};