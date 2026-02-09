let currentRideId = null;

        document.addEventListener('DOMContentLoaded', function() {
            currentRideId = localStorage.getItem('currentRideId');
            if (currentRideId) {
                document.getElementById('chatTitle').textContent = `محادثة الرحلة #${currentRideId}`;
                loadChat();
                setInterval(loadChat, 3000);
            } else {
                alert('لم يتم تحديد رحلة');
            }
        });

        async function loadChat() {
            if (!currentRideId) return;
            try {
                const response = await fetch(`/api/chat/room/${currentRideId}/messages`);
                const messages = await response.json();
                const container = document.getElementById('messagesContainer');
                container.innerHTML = '';
                
                if (messages.length === 0) {
                    container.innerHTML = '<div class="no-messages">لا توجد رسائل بعد. ابدأ المحادثة!</div>';
                    return;
                }

                messages.forEach(msg => {
                    const messageDiv = document.createElement('div');
                    const isMe = checkIfMessageIsMine(msg.senderId);
                    messageDiv.className = `msg ${isMe ? 'me' : 'other'}`;
                    messageDiv.innerHTML = `
                        <div class="message-content">${msg.content}</div>
                        <div class="message-time">${formatTime(msg.timestamp)}</div>
                    `;
                    container.appendChild(messageDiv);
                });
                
                container.scrollTop = container.scrollHeight;
            } catch (error) {
                console.error('Error:', error);
            }
        }

        async function sendMessage() {
            const messageInput = document.getElementById('messageInput');
            const content = messageInput.value.trim();
            if (!content || !currentRideId) return;

            try {
                let roomResponse = await fetch(`/api/chat/room/${currentRideId}`);
                let room = await roomResponse.json();
                if (!room || !room.id) {
                    roomResponse = await fetch(`/api/chat/room/create?rideId=${currentRideId}`, {method: 'POST'});
                    room = await roomResponse.json();
                }

                await fetch('/api/chat/message/send', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({
                        roomId: room.id,
                        senderId: getCurrentSenderId(),
                        content: content
                    })
                });

                messageInput.value = '';
                loadChat();
            } catch (error) {
                alert('فشل في إرسال الرسالة');
            }
        }

        function handleKeyPress(event) {
            if (event.key === 'Enter') sendMessage();
        }

        function formatTime(timestamp) {
            if (!timestamp) return '';
            const date = new Date(timestamp);
            return date.toLocaleTimeString('ar-SA', {hour: '2-digit', minute: '2-digit'});
        }

        function checkIfMessageIsMine(senderId) {
            const currentUser = localStorage.getItem('currentUserId');
            
            if (currentUser && currentUser.startsWith('DRIVER_')) {
                return senderId == 2;
            } else if (currentUser && currentUser.startsWith('PASSENGER_')) {
                return senderId == 1;
            } else {
                return false;
            }
        }

        function getCurrentSenderId() {
            const currentUser = localStorage.getItem('currentUserId');
            if (currentUser && currentUser.startsWith('DRIVER_')) {
                return 2;
            } else {
                return 1;
            }
        }

        function goBack() {
            window.history.back();
        }