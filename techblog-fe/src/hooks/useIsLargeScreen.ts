'use client';

import { useState, useEffect } from "react";

import { BREAKPOINT } from "@/contants/common";

const useIsLargeScreen = () => {
    const [isLargeScreen, setIsLargeScreen] = useState(true);

    const handleResize = () => {
        setIsLargeScreen(window.innerWidth > BREAKPOINT.MD);
    };

    useEffect(() => {
        handleResize();
        window.addEventListener('resize', handleResize);

        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);

    return isLargeScreen;
};

export default useIsLargeScreen;
