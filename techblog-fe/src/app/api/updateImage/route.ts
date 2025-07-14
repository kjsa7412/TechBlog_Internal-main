import axios from 'axios';
import cron from 'node-cron';
import { NextRequest, NextResponse } from 'next/server';

import globalState from "@/server/GlobalState";


const ACCESS_KEY = process.env.NEXT_PUBLIC_UNSPLASH_ACCESS_KEY; // Unsplash API 키

async function fetchImage(topic: string, perPage: number) {
    try {
        const response = await axios.get('https://api.unsplash.com/search/photos', {
            params: {
                query: topic,
                client_id: ACCESS_KEY,
                per_page: perPage,
                order_by: 'relevant',
            },
        });

        if (response.status === 200) {
            const photos = response.data.results;
            if (photos.length > 0) {
                const randomIndex = Math.floor(Math.random() * photos.length);
                return !!photos && photos[randomIndex].urls.regular;
            }
        }
    } catch (error) {
        console.error('Error fetching images from Unsplash:', error);
    }
    return null;
}

// 30분마다 실행
cron.schedule('*/30 * * * *', async () => {
    const imageUrl = await fetchImage(globalState.getLastTopic(), globalState.getLastPerPage());
    globalState.setImageUrl(imageUrl);
    console.log(`New random image URL updated: ${imageUrl}`);
});

export async function GET(req: NextRequest) {
    const topic = req.nextUrl.searchParams.get('query') || globalState.getLastTopic();
    const perPageParam = req.nextUrl.searchParams.get('per_page');
    const perPage = perPageParam ? parseInt(perPageParam) : globalState.getLastPerPage();


    if (req.method === 'GET') {
        if (!globalState.getImageUrl()) {
            const imageUrl = await fetchImage(topic, perPage);
            globalState.setImageUrl(imageUrl);
            console.log(`First image URL updated: ${imageUrl}`);
        }
        globalState.setLastTopic(topic);
        globalState.setLastPerPage(perPage);
    }

    return NextResponse.json({ imageUrl: globalState.getImageUrl() }, { status: 200 });
}
