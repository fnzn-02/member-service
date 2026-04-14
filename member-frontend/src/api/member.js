// src/api/member.js
import api from './api';

const API_BASE_URL = '/api/members';

export const getMyInfo = async () => {
    const response = await api.get(`${API_BASE_URL}/me`);
    return response.data;
};

export const updateNickname = async (newNickname) => {
    // 백엔드 NicknameUpdateRequestDto 의 필드명인 'newNickname'에 맞춰서 보냄
    const response = await api.put(`${API_BASE_URL}/me/nickname`, {
        newNickname: newNickname
    });
    return response.status === 200;
};

export const updatePassword = async (currentPassword, newPassword) => {
    const response = await api.put(`${API_BASE_URL}/me/password`, {
        currentPassword: currentPassword,
        newPassword: newPassword
    });
    return response.status === 200;
};

// 회원 탈퇴 API 연결
export const deleteMember = async () => {
    const response = await api.delete(`${API_BASE_URL}/me`);
    return response.status === 200;
};

export const sendPasswordResetCode = async (email) => {
    await api.post(`${API_BASE_URL}/password/code?email=${email}`);
};

export const resetPassword = async (email, code, newPassword) => {
    await api.post(`${API_BASE_URL}/password/reset`, { email, code, newPassword });
};