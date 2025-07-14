import axios from 'axios';

const axiosClient = axios.create({
    baseURL: process.env.NEXT_PUBLIC_CLI_BASE_URL,
    withCredentials: true
});

axiosClient.interceptors.request.use(
    (config) => {
        // // 요청 전에 할 작업 (예: 토큰 추가)
        // const token = document.cookie.split('; ').find(row => row.startsWith('accessToken='))?.split('=')[1];
        // if (token) {
        //     config.headers['Authorization'] = `Bearer ${token}`;
        // }
        return config;
    },
    (error) => {
        // 요청 에러 처리
        return Promise.reject(error);
    }
);

axiosClient.interceptors.response.use(
    (response) => {
        // 응답 데이터 가공
        return response;
    },
    (error) => {
        // 응답 에러 처리
        // if (error.response.status === 401) {
        //     // 로그인이 필요한 경우 로그인 페이지로 리디렉션
        //     window.location.href = '/login';
        // }
        return Promise.reject(error);
    }
);

export default axiosClient;
