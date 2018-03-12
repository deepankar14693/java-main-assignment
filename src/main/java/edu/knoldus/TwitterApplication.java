package edu.knoldus;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TwitterApplication {

    private final int limit = 50;
    private Twitter twitter;

    TwitterApplication() {
        ConfigurationBuilder configBuilder = new ConfigurationBuilder ();
        configBuilder.setDebugEnabled (true)
                .setOAuthConsumerKey (TwitterConfiguration.consumerKey)
                .setOAuthConsumerSecret (TwitterConfiguration.consumerSecretKey)
                .setOAuthAccessToken (TwitterConfiguration.accessToken)
                .setOAuthAccessTokenSecret (TwitterConfiguration.accsessTokenSecret);
        TwitterFactory tweetFactory = new TwitterFactory (configBuilder.build ());
        this.twitter = tweetFactory.getInstance ();
    }

    public CompletableFuture<List<Status>> fetchLatestTweets() {
        Query query = new Query ("#KatrinaKaif");
        query.setCount (limit);
        query.resultType (Query.RECENT);
        return supplyAsync (() -> {
                    List<Status> latestTweets = new ArrayList<> ();
                    try {
                        latestTweets = this.twitter.search (query).getTweets ();
                    } catch (TwitterException e) {
                        System.out.println ("error occured "+e.getMessage ());
                    }
                    return latestTweets;
                }
        );
    }

    public CompletableFuture<List<Status>> olderToNewerTweets() {
        Query query = new Query ("#KatrinaKaif");
        query.setCount (limit);
        query.resultType (Query.RECENT);
        return supplyAsync (() -> {
                    List<Status> latestTweets = new ArrayList<> ();
                    try {
                        QueryResult queryResult = this.twitter.search (query);
                        queryResult.getTweets ().sort (Comparator.comparing (status ->
                                status.getCreatedAt ().getTime ()));
                        latestTweets.addAll (queryResult.getTweets ());

                    } catch (TwitterException e) {
                        System.out.println ("error occured "+e.getMessage ());
                    }
                    return latestTweets;
                }
        );
    }

    public CompletableFuture<List<Status>> reTweets() {
        Query query = new Query ("#KatrinaKaif");
        query.setCount (limit);
        query.resultType (Query.RECENT);
        return supplyAsync (() -> {
                    List<Status> latestTweets = new ArrayList<> ();
                    try {
                        QueryResult queryResult = this.twitter.search (query);
                        queryResult.getTweets ().sort ((statusFirst,statusSecond) ->
                                statusSecond.getRetweetCount () - statusFirst.getRetweetCount ());
                        latestTweets.addAll (queryResult.getTweets ());
                    } catch (TwitterException e) {
                        System.out.println ("error occured "+e.getMessage ());
                    }
                    return latestTweets;
                }
        );
    }

    public CompletableFuture<List<Status>> likesHigherToLower() {
        Query query = new Query ("KatrinaKaif");
        query.setCount (limit);
        query.resultType (Query.RECENT);
        return supplyAsync ( () -> {
            List<Status> latestTweets = new ArrayList<> ();
                    try {
                        QueryResult queryResult = this.twitter.search (query);
                        queryResult.getTweets ().sort ((firstStatus,secondStatus) ->
                                secondStatus.getFavoriteCount() - firstStatus.getFavoriteCount());
                        latestTweets.addAll(queryResult.getTweets ());
                    } catch (TwitterException e) {
                        e.printStackTrace ();
                    }
                    return latestTweets;
                }
        );
    }

    public CompletableFuture<List<Status>> listTweetsOnGivenDate(LocalDate date) {
        Query query = new Query ("KatrinaKaif");
        query.setCount (limit);
        query.resultType (Query.RECENT);
        query.setSince (date.toString ());
        return supplyAsync ( () -> {
                    List<Status> latestTweets = new ArrayList<> ();
                    try {
                        QueryResult queryResult = this.twitter.search (query);
                        latestTweets.addAll(queryResult.getTweets ());
                        System.out.println ("number of tweets on given date are : " +latestTweets.size ());
                    } catch (TwitterException e) {
                        e.printStackTrace ();
                    }
                    return latestTweets;
                }
        );
    }
}
