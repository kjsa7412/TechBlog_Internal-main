/** @type {import('next').NextConfig} */
const nextConfig = {
    images: {
        domains: ['images.unsplash.com', 'localhost'],
    },
    reactStrictMode: false,

    env: {
        NEXT_PUBLIC_API_BASE_URL: process.env.NEXT_PUBLIC_CLI_BASE_URL,
    },

    logging: {
        fetches: {
            fullUrl: true,
        },
    },
    // 라우터 캐시 갱신 주기 재설정
    // 프레임워크 디폴트 값 dynamic : 30, static: 300
    experimental: {
        staleTimes: {
            dynamic: 0,
            static: 300,
        },
    },
};

export default nextConfig;