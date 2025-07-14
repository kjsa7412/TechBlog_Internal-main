'use client'

import { usePathname } from 'next/navigation'
import { useEffect, useRef } from 'react'
import { useRecoilState } from "recoil"

import { IRouterState } from "@/types/interfaces/common-interface"
import { routerAtom } from "@/atoms/routerAtom"

export default function ScrollRestoreProvider() {
    const pathname = usePathname()
    const [routerState, setRouterState] = useRecoilState<IRouterState>(routerAtom)

    // 최신 routerState를 유지하기 위한 useRef
    const routerStateRef = useRef(routerState.routerState)

    // 상태가 바뀔 때마다 ref도 갱신
    useEffect(() => {
        routerStateRef.current = routerState.routerState
    }, [routerState.routerState])

    // 브라우저 뒤로가기 / 앞으로가기 전 URL
    const prevPathRef = useRef(pathname);
    
    // pathname 이 바뀔때마다 URL 변수 갱신
    useEffect(() => {
        prevPathRef.current = pathname;
    }, [pathname]);

    const saveScroll = (url: string, y: number) => {
        requestAnimationFrame(() => {
            requestAnimationFrame(() => {
                sessionStorage.setItem(`scroll-${url}`, String(y))
            })
        })
    }

    const restoreScroll = (url: string) => {
        requestAnimationFrame(() => {
            requestAnimationFrame(() => {
                const y = sessionStorage.getItem(`scroll-${url}`)
                if (y !== null) {
                    document.body.scrollTo(0, parseInt(y, 10));
                }
            })
        })
    }

    // 뒤로/앞으로 가기 시 스크롤 복원
    useEffect(() => {
        const handlePopState = () => {
            const prevPath = prevPathRef.current;                                                       // 이전 URL
            const y = window.scrollY || document.documentElement.scrollTop || document.body.scrollTop // 현재 y값
            const newPath = window.location.pathname;                                                   // 이미 바뀐 URL

            saveScroll(prevPath, y); // 기존 URL에 대한 스크롤 저장
            restoreScroll(newPath);  // 새 URL의 스크롤 복원
            
            // useActionAndNavigate 에서 사용하는 router 변수 초기화
            setRouterState(prev => ({
                ...prev,
                routerState: false
            }))
        }
        window.addEventListener('popstate', handlePopState)
        return () => window.removeEventListener('popstate', handlePopState)
    }, [])

    return null
}
