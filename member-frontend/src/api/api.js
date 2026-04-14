import axios from 'axios';

// 💡 1. 백엔드(스프링부트) 주소 기본 세팅
const api = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
});

// 💡 2. 문지기 통과용 마법: API 쏠 때마다 로컬 스토리지에 'token'이 있으면 자동으로 헤더에 달아줌!
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;