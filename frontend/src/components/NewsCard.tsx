import React from "react";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { News } from "@/types/news";
import { IconArrowBigUp, IconArrowBigUpFilled, IconArrowBigDown, IconArrowBigDownFilled, IconExternalLink, IconShare } from '@tabler/icons-react';
import { Button } from "./ui/button";

function NewsCard({news}: {news: News}) {
  return (
    <Card>
      <CardHeader>
        <CardTitle className="text-start">{news.title}</CardTitle>
        <CardDescription className="text-start">
            {news.publishedAt.toLocaleString("sr-RS")}
            <span className="mx-2">-</span>
            {news.source}
        </CardDescription>
      </CardHeader>
      <CardContent className="text-start">{news.summary}</CardContent>
      <CardFooter className="flex items-center justify-between">
        <div className="flex items-center justify-between gap-5">
            <div className="flex items-center justify-between gap-1">
              <IconArrowBigUp stroke={2} />
              <span>{news.upVotes - news.downVotes}</span>
              <IconArrowBigDown stroke={2} />
            </div>
            <div>
                <span>{news.commentCount}</span>
                <span className="mx-1">comments</span>
            </div>
        </div>
        <div className="flex items-center justify-between gap-1">
            <Button variant="default" className="text-start" size="icon">
                <IconShare stroke={2} />
            </Button>
            <Button variant="default" className="text-start" size="icon">
                <IconExternalLink stroke={2} />
            </Button>
        </div>
      </CardFooter>
    </Card>
  );
}

export default NewsCard;
