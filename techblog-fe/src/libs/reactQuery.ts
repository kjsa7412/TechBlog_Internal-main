import {QueryCache, QueryClient} from "react-query";

const queryClient = new QueryClient({
    queryCache: new QueryCache({
        onSuccess: data => {
            // 성공 시 처리
        },
        onError: (error, query) => {
            if (query.state.data !== undefined) {
                // 에러 발생 시 처리
            }
        }
    }),
    defaultOptions: {
        queries: {
            retry: 0,
            refetchOnWindowFocus: false
        }
    }
});

export default queryClient;