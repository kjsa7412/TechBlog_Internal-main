// GlobalState.js
class GlobalState {
    private static instance: GlobalState | null = null;

    private imageUrl: string | null;
    private lastTopic: string;
    private lastPerPage: number;

    private constructor() {
        this.imageUrl = null;
        this.lastTopic = 'office';
        this.lastPerPage = 30;
    }

    public static getInstance(): GlobalState {
        if (!GlobalState.instance) {
            GlobalState.instance = new GlobalState();
        }
        return GlobalState.instance;
    }

    public setImageUrl(url: string | null) {
        this.imageUrl = url;
    }

    public getImageUrl(): string | null {
        return this.imageUrl;
    }

    public setLastTopic(topic: string) {
        this.lastTopic = topic;
    }

    public getLastTopic(): string {
        return this.lastTopic;
    }

    public setLastPerPage(perPage: number) {
        this.lastPerPage = perPage;
    }

    public getLastPerPage(): number {
        return this.lastPerPage;
    }
}

// Export the singleton instance
export default GlobalState.getInstance();
