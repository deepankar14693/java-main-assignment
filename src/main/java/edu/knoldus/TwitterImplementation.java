package edu.knoldus;

import twitter4j.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TwitterImplementation {
    public static void main(String[] args) throws InterruptedException {
        LocalDate date = LocalDate.of (2018,03,06);
        TwitterApplication analysis = new TwitterApplication ();

        CompletableFuture<List<Status>> fetchedTweets = analysis.fetchLatestTweets ();
        fetchedTweets.thenAccept (status -> status.forEach (System.out::println));

        CompletableFuture<List<Status>> oldToNewTweets = analysis.olderToNewerTweets ();
        oldToNewTweets.thenAccept (status -> status.forEach (System.out::println));

        CompletableFuture<List<Status>> accordingToRetweets = analysis.reTweets ();
        accordingToRetweets.thenAccept (System.out::println);

        CompletableFuture<List<Status>> accordingToHigherLikes = analysis.likesHigherToLower ();
        accordingToHigherLikes.thenAccept (System.out::println);

        CompletableFuture<List<Status>> tweetsOnGivenDate = analysis.listTweetsOnGivenDate (date);
        tweetsOnGivenDate.thenAccept (System.out::println);

        Thread.sleep (5000);
    }
}
