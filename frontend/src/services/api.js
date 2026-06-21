import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
});

// KẺ ĐÁNH CHẶN: Tự động nhét Token vào Header
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Bắt lỗi toàn cục: Đuổi về trang đăng nhập nếu Token hết hạn
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = '/login'; 
        }
        return Promise.reject(error);
    }
);

export default api;