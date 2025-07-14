"use client";

import useScrollRestore from "@/hooks/useScrollRestore";

export default function ScrollRestoreProvider({ children }: { children: React.ReactNode }) {
    useScrollRestore();
    return <>{children}</>;
}