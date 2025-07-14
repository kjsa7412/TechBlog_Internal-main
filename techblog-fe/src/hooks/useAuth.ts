'use client';

import { useQuery } from 'react-query';

import axiosClient from "@/libs/axiosClient";

const fetchAuthStatus = async () => {
    const { data } = await axiosClient.get('/api/auth/check');
    return data;
};

const useAuth = () => {
    const { data, isLoading, isError } = useQuery('authStatus', fetchAuthStatus, {
        retry: false, // 실패 시 재시도하지 않음
    });

    if (isLoading || isError) {
        return null;
    }

    return data.userId; // 인증된 사용자 ID 반환
};

export default useAuth;
