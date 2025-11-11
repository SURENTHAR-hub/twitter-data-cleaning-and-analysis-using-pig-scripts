import java.io.*;
import java.util.*;
import java.util.regex.*;

public class TwitterDataAnalysis {

    // Method to clean tweet text
    public static String cleanTweet(String tweet) {
        if (tweet == null || tweet.trim().isEmpty())
            return "";
        
        // Remove URLs, mentions, hashtags symbols, and non-alphabetic characters
        tweet = tweet.toLowerCase();
        tweet = tweet.replaceAll("http\\S+", ""); // Remove links
        tweet = tweet.replaceAll("@\\S+", "");    // Remove mentions
        tweet = tweet.replaceAll("#", "");        // Remove '#' symbol
        tweet = tweet.replaceAll("[^a-z\\s]", ""); // Keep only letters
        tweet = tweet.replaceAll("\\s+", " ").trim(); // Clean spaces
        
        return tweet;
    }

    // Method to count hashtags
    public static Map<String, Integer> countHashtags(List<String> tweets) {
        Map<String, Integer> hashtagCount = new HashMap<>();
        Pattern pattern = Pattern.compile("#(\\w+)");
        for (String tweet : tweets) {
            Matcher matcher = pattern.matcher(tweet);
            while (matcher.find()) {
                String tag = matcher.group(1).toLowerCase();
                hashtagCount.put(tag, hashtagCount.getOrDefault(tag, 0) + 1);
            }
        }
        return hashtagCount;
    }

    // Method to count tweets per user
    public static Map<String, Integer> countTweetsPerUser(Map<String, String> userTweets) {
        Map<String, Integer> userCount = new HashMap<>();
        for (String user : userTweets.keySet()) {
            userCount.put(user, userCount.getOrDefault(user, 0) + 1);
        }
        return userCount;
    }

    // Method to calculate word frequency
    public static Map<String, Integer> wordFrequency(List<String> cleanedTweets) {
        Map<String, Integer> freq = new HashMap<>();
        for (String tweet : cleanedTweets) {
            for (String word : tweet.split(" ")) {
                if (word.length() > 2) { // ignore short words
                    freq.put(word, freq.getOrDefault(word, 0) + 1);
                }
            }
        }
        return freq;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> tweets = new ArrayList<>();
        Map<String, String> userTweets = new HashMap<>();

        System.out.println("=== Twitter Data Cleaning and Analysis ===");
        System.out.print("Enter number of tweets: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            System.out.print("Enter username: ");
            String user = sc.nextLine();

            System.out.print("Enter tweet: ");
            String tweet = sc.nextLine();

            userTweets.put(user + "_" + i, tweet);
            tweets.add(tweet);
        }

        // Clean tweets
        List<String> cleanedTweets = new ArrayList<>();
        for (String tweet : tweets) {
            cleanedTweets.add(cleanTweet(tweet));
        }

        // Analysis
        Map<String, Integer> hashtags = countHashtags(tweets);
        Map<String, Integer> userCounts = countTweetsPerUser(userTweets);
        Map<String, Integer> wordCount = wordFrequency(cleanedTweets);

        // Display results
        System.out.println("\n=== Cleaned Tweets ===");
        for (String t : cleanedTweets)
            System.out.println("- " + t);

        System.out.println("\n=== Hashtag Frequency ===");
        hashtags.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> System.out.println("#" + e.getKey() + " : " + e.getValue()));

        System.out.println("\n=== User Tweet Counts ===");
        userCounts.forEach((user, count) -> System.out.println(user + " : " + count));

        System.out.println("\n=== Top 10 Frequent Words ===");
        wordCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .forEach(e -> System.out.println(e.getKey() + " : " + e.getValue()));

        sc.close();
    }
}