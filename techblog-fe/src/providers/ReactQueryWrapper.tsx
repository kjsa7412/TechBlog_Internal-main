'use client';

import { QueryClientProvider } from 'react-query';

import queryClient from "@/libs/reactQuery";

interface ReactQueryWrapperProps {
    children: React.ReactNode;
}

export default function ReactQueryWrapper({children}: ReactQueryWrapperProps) {
    return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
}