package fileio;

import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Information about an user, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class UserInputData {
    /**
     * User's username
     */
    private final String username;
    /**
     * Subscription Type
     */
    private final String subscriptionType;
    /**
     * The history of the movies seen
     */
    private final Map<String, Integer> history;
    /**
     * Movies added to favorites
     */
    private final ArrayList<String> favoriteMovies;
    /**
     * Titles rated
     */
    private final Map<String, Double> ratings = new HashMap<>();

    public UserInputData(final String username, final String subscriptionType,
                         final Map<String, Integer> history,
                         final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.favoriteMovies = favoriteMovies;
        this.history = history;
    }

    public String getUsername() {
        return username;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }


    //methods

    /**
     * @param movie
     */
    public void addToFavorite(final String movie) {
        favoriteMovies.add(movie);
    }

    /**
     * @param title
     */
    public void addToHistory(final String title) {
        if (history.containsKey(title)) {
            history.put(title, history.get(title) + 1);
        } else {
            history.put(title, 1);
        }
    }

    public Map<String, Double> getRatings() {
        return ratings;
    }

    /**
     * @param o
     * @param grade
     * @param a
     * @param result
     */
    public void setRating(final ShowInput o, final double grade, final ActionInputData a,
                          final JSONArray result) {
        JSONObject message = new JSONObject();
        message.put("id", a.getActionId());
        if (history.containsKey(o.getTitle())) {
            if (o instanceof MovieInputData) {
                if (!ratings.containsKey(o.getTitle())) {
                    ratings.put(o.getTitle(), grade);
                    o.setRating(grade);
                    message.put("message", "success -> "
                            + o.getTitle() + " was rated with "
                            + grade + " by " + username);
                    result.add(message);
                    return;
                }  else {
                    message.put("message", "error -> "
                            + o.getTitle() + " has been already rated");
                    result.add(message);
                    return;
                }
            } else {
                if (!ratings.containsKey(o.getTitle() + a.getSeasonNumber())) {
                    ratings.put(o.getTitle() + a.getSeasonNumber(), grade);
                    ((SerialInputData) o).getSeasons().get(
                            a.getSeasonNumber() - 1).getRatings().add(grade);
                    message.put("message", "success -> "
                            + o.getTitle() + " was rated with "
                            + grade + " by " + username);
                    result.add(message);
                    return;
                } else {
                    message.put("message", "error -> "
                            + o.getTitle() + " has been already rated");
                    result.add(message);
                    return;
                }
            }
        } else {
            message.put("message", "error -> "
                    +  o.getTitle() + " is not seen");
            result.add(message);
        }
    }


    @Override
    public String toString() {
        return "UserInputData{" + "username='"
                + username + '\'' + ", subscriptionType='"
                + subscriptionType + '\'' + ", history="
                + history + ", favoriteMovies="
                + favoriteMovies + '}';
    }
}
