'use client';
import { getCookie, setCookie, deleteCookie } from "cookies-next";

const cookieStorageEffect = (key: string) => ({ setSelf, onSet }: any) => {
    if (typeof window !== "undefined") {
        const savedValue = getCookie(key);

        if (savedValue != null) {
            setSelf(JSON.parse(savedValue));
        }
    }

    // 상태가 변경될 때 쿠키에 저장 또는 제거
    onSet((newValue: any, _: any, isReset: boolean) => {
        isReset ? deleteCookie(key) : setCookie(key, JSON.stringify(newValue));
    });
};

export default cookieStorageEffect;
