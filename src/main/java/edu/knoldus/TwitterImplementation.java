package edu.knoldus;

import twitter4j.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TwitterImplementation {
    public static void main(String[] args) throws InterruptedException {
        final LocalDate date = LocalDate.of(2018,03,06);
        TwitterApplication analysis = new TwitterApplication();

        CompletableFuture<List<Status>> fetchedTweets = analysis.fetchLatestTweets();
        fetchedTweets.thenAccept(statuses -> statuses.forEach(status-> System.out.println(status.getText())));

        CompletableFuture<List<Status>> oldToNewTweets = analysis.olderToNewerTweets();
        oldToNewTweets.thenAccept(statuses -> statuses.forEach(status-> System.out.println(status.getText())));

        CompletableFuture<List<Status>> accordingToRetweets = analysis.reTweets();
        accordingToRetweets.thenAccept(statuses -> statuses
                .forEach(status -> System.out.println(status.getText())));

        CompletableFuture<List<Status>> accordingToHigherLikes = analysis.likesHigherToLower();
        accordingToHigherLikes.thenAccept(statuses -> statuses.forEach(status -> System.out.println(status.getText())));

        CompletableFuture<List<Status>> tweetsOnGivenDate = analysis.listTweetsOnGivenDate(date);
        tweetsOnGivenDate.thenAccept(statuses -> statuses.forEach(status -> System.out.println(status.getText())));

        CompletableFuture<Integer> likesInInterval = analysis.numberOfLikesInGivenInterval();
        likesInInterval.thenAccept(x -> System.out.println("number of likes in given interval are : " + x)) ;


        Thread.sleep(5000);
    }
}
