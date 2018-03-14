package edu.knoldus;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.ConfigurationFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class TwitterApplication {

    static final Integer limit = 50;
    private Twitter twitter;


    TwitterApplication() {
        Config conf = ConfigFactory.load();
        this.twitter = new TwitterFactory().getInstance();
        this.twitter.setOAuthConsumer(conf.getString("ConsumerKey"),
                conf.getString("ConsumerSecret"));
        this.twitter.setOAuthAccessToken(new AccessToken(
                conf.getString("AccessToken"),
                conf.getString("AccessTokenSecret")));
    }

    /**
     *
     * @return list of recent status on the basis of particular hashtag wrapped in
     * completable futures
     */
    public CompletableFuture<List<Status>> fetchLatestTweets() {
        Query query = new Query("#KatrinaKaif");
        query.setCount(limit);
        query.resultType(Query.RECENT);
        return CompletableFuture.supplyAsync(() -> {
                    List<Status> latestTweets = new ArrayList<>();
                    try {
                        latestTweets = this.twitter.search(query).getTweets();
                    } catch (TwitterException e) {
                        System.out.println("error occured " + e.getMessage());
                    }
                    return latestTweets;
                }
        );
    }

    /**
     *
     * @return list of status on the basis of older to newer tweets on a particular hashtag
     * wrapped in completable future
     */
    public CompletableFuture<List<Status>> olderToNewerTweets() {
        Query query = new Query("#KatrinaKaif");
        query.setCount(limit);
        query.resultType(Query.RECENT);
        return CompletableFuture.supplyAsync(() -> {
                    List<Status> latestTweets = new ArrayList<>();
                    try {
                        QueryResult queryResult = this.twitter.search(query);
                        queryResult.getTweets().sort(Comparator.comparing(status ->
                                status.getCreatedAt().getTime()));
                        latestTweets.addAll(queryResult.getTweets());

                    } catch (TwitterException e) {
                        System.out.println("error occured " + e.getMessage());
                    }
                    return latestTweets;
                }
        );
    }

    /**
     *
     * @return list of status on the basis of retweets on a particular hashtag
     * wrapped in completable future
     */
    public CompletableFuture<List<Status>> reTweets() {
        Query query = new Query("#KatrinaKaif");
        query.setCount(limit);
        query.resultType(Query.RECENT);
        return CompletableFuture.supplyAsync(() -> {
                    List<Status> latestTweets = new ArrayList<>();
                    try {
                        QueryResult queryResult = this.twitter.search(query);
                        queryResult.getTweets().sort((statusFirst,statusSecond) ->
                                statusSecond.getRetweetCount() - statusFirst.getRetweetCount());
                        latestTweets.addAll(queryResult.getTweets());
                    } catch (TwitterException e) {
                        System.out.println("error occured " + e.getMessage());
                    }
                    return latestTweets;
                }
        );
    }

    /**
     *
     * @return list of tweets based on the likes in descending order wrapped in completable
     * future
     */
    public CompletableFuture<List<Status>> likesHigherToLower() {
        Query query = new Query("KatrinaKaif");
        query.setCount(limit);
        query.resultType(Query.RECENT);
        return CompletableFuture.supplyAsync(() -> {
            List<Status> latestTweets = new ArrayList<>();
                    try {
                        QueryResult queryResult = this.twitter.search(query);
                        queryResult.getTweets().sort((firstStatus,secondStatus) ->
                                secondStatus.getFavoriteCount() - firstStatus.getFavoriteCount());
                        latestTweets.addAll(queryResult.getTweets());
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                    return latestTweets;
                }
        );
    }

    /**
     *
     * @param date for finding out tweets on a particular date
     * @return list of tweets on a particular day wrapped in completable future
     */
    public CompletableFuture<List<Status>> listTweetsOnGivenDate(LocalDate date) {
        Query query = new Query("KatrinaKaif");
        query.setCount(limit);
        query.resultType(Query.RECENT);
        query.setSince(date.toString());
        String name = "myname";
        return CompletableFuture.supplyAsync(() -> {
                    List<Status> latestTweets = new ArrayList<>();
                    try {
                        QueryResult queryResult = this.twitter.search(query);
                        latestTweets.addAll(queryResult.getTweets());
                        System.out.println("number of tweets on given date are : " + latestTweets.size());
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                    return latestTweets;
                }
        );
    }

    /**
     *
     * @return number of likes in an interval of 15 mins wrapped in completable future
     */
    public CompletableFuture<Integer> numberOfLikesInGivenInterval() {
        Query query = new Query("#bbkivines");
        query.since(LocalDateTime.now().minusMinutes(15).toString());
        return CompletableFuture.supplyAsync(() -> {
            List<Status> interval = new ArrayList<>();
            int c = 0;
            try {
                interval = this.twitter.search(query).getTweets();
                interval.stream().forEach(x -> System.out.println(x.getCreatedAt()));
                c = interval.stream().mapToInt(Status::getFavoriteCount).reduce(0, (a, b) -> (a + b));
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return c;
        });
    }
}
