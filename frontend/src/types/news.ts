export type News = {
    id: string;
    title: string;
    summary: string;
    imageUrl: string;
    publishedAt: Date;
    postUrl: string;
    source: string;
    upVotes: number;
    downVotes: number;
    commentCount: number;
}