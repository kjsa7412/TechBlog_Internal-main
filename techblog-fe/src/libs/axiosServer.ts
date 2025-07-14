import axios from 'axios';

import {EJWT} from "@/types/enums/common-enum";

const axiosServer = axios.create({
    baseURL: process.env.NEXT_PUBLIC_SVR_BASE_URL,
    withCredentials: true
});

const deleteCookie = (name: string) => {
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/`;
};

axiosServer.interceptors.request.use(
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

axiosServer.interceptors.response.use(
    (response) => {
        // 응답 데이터 가공
        return response;
    },
    async (error) =>  {
        // 응답 에러 처리
        const {config, response: {status}} = error;

        // 인증 관련 401 error 확인
        if (status === 401) {
            // 토큰은 존재하나 만료된경우 (토큰 재발급 신청 후 기존의 요청 다시 전달)
            if (error.response.data.errorMsg === EJWT.Expire_ERR) {
                // 원래의 요청을 변수에 저장
                const originalRequest = config;

                // 토큰 재발급 요청
                const renewResponse = await axiosServer.post('/public/post/auth/renewToken', {}, {
                    headers: {
                        Authorization: originalRequest.headers.Authorization, // Access 토큰 전달
                        Cookie: originalRequest.headers.Cookie // Refresh 토큰 전달
                    }
                });

                // 토큰 재발급이 성공한 경우
                if (renewResponse.status === 200 && !renewResponse.data.isError) {
                    // 실패했던 원래의 요청을 토큰을 재발급 받은뒤에 다시 요청
                    return axios(originalRequest);
                } else {
                    // 로그인 관련 정보 삭제
                    deleteCookie('userAtom');
                    deleteCookie('loginAtom')

                    window.location.href = '/'

                    alert('로그인 정보가 없습니다. 메인화면으로 이동합니다.')
                }
            }

            // 인증이 필요한 요청이지만 토큰이 존재하지 않는경우 or 올바르지 못한 토큰인 경우
            if (error.response.data.errorMsg === EJWT.Invalid_ERR) {
                // 로그인 관련 정보 삭제
                deleteCookie('userAtom');
                deleteCookie('loginAtom')

                window.location.href = '/'

                alert('로그인 정보가 없습니다. 메인화면으로 이동합니다.')
            }
        }

        // 권한 관련 403 error 확인
        if (status === 403) {
            alert('권한 정보가 없습니다. 메인화면으로 이동합니다.')

            window.location.href = '/'
        }

        return Promise.reject(error);
    }
);

export default axiosServer;
