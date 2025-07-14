'use client'

import {useEffect} from "react";
import {useRecoilState} from "recoil";

import {IBreakPoint} from "@/types/interfaces/common-interface";
import {breakPointAtom} from "@/atoms/breakPointAtom";
import {EBreakPoint} from "@/types/enums/common-enum";

const useBreakPoint = () => {
    const [breakPoint, setBreakPoint] = useRecoilState<IBreakPoint>(breakPointAtom);

    const handleResize = () => {
        const width = window.innerWidth;

        switch (true) {
            case (width > EBreakPoint.MD):
                setBreakPoint((prev) => ({...prev, breakPoint: EBreakPoint.LG}));
                break;
            case (width > EBreakPoint.SM):
                setBreakPoint((prev) => ({...prev, breakPoint: EBreakPoint.MD}));
                break;
            case (width > EBreakPoint.XS):
                setBreakPoint((prev) => ({...prev, breakPoint: EBreakPoint.SM}));
                break;
            default:
                setBreakPoint((prev) => ({...prev, breakPoint: EBreakPoint.XS}));
                break;
        }
    };

    useEffect(() => {
        handleResize();
        window.addEventListener('resize', handleResize);

        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);

    return breakPoint.breakPoint;
}

export default useBreakPoint;