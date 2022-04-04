package Classses;

import entertainment.Genre;
import fileio.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static utils.Utils.stringToGenre;

public class Recommendation extends Action {


    public Recommendation(final List<ActorInputData> act, final ActionInputData action,
                          final List<MovieInputData> movies, final List<SerialInputData> shows,
                          final List<UserInputData> users, final JSONArray arrayResult) {
        super(act, action, movies, shows, users, arrayResult);
    }

    /**
     *
     */
    @Override
    public void execute() {
        //variables used to make the code cleaner
        ActionInputData a = super.getAction();
        ArrayList<ActorInputData> actors = (ArrayList<ActorInputData>) super.getActors();
        ArrayList<UserInputData> users = (ArrayList<UserInputData>) super.getUsers();
        ArrayList<MovieInputData> movies = (ArrayList<MovieInputData>) super.getMovies();
        ArrayList<SerialInputData> serials = (ArrayList<SerialInputData>) super.getShows();

        //the message we are going to add in the arrayResult
        JSONObject message = new JSONObject();
        message.put("id", a.getActionId());
        ArrayList<Pair> pairs = new ArrayList<>();

        if (a.getType().equals("standard")) {
            boolean isShow = true, itsNotWorking = true;
            for (UserInputData u : users) {
                if (u.getUsername().equals(a.getUsername())) {
                    for (MovieInputData m : movies) {
                        if (!u.getHistory().containsKey(m.getTitle())) {
                            itsNotWorking = false;
                            isShow = false;
                            message.put("message", "StandardRecommendation result: "
                                    + m.getTitle());
                            break;
                        }
                    }
                    if (isShow) {
                        for (SerialInputData s : serials) {
                            if (!u.getHistory().containsKey(s.getTitle())) {
                                itsNotWorking = false;
                                message.put("message", "StandardRecommendation result: "
                                        + s.getTitle());
                                break;
                            }
                        }
                    }
                    if (itsNotWorking) {
                        message.put("message", "StandardRecommendation cannot be applied!");
                    }
                }
            }
        } else if (a.getType().equals("best_unseen")) {
            ArrayList<Pair> recommendations = new ArrayList<>();
            ArrayList<String> unrated = new ArrayList<>();
            boolean seenAll = true;

            for (UserInputData u : users) {
                if (u.getUsername().equals(a.getUsername())) {
                    for (MovieInputData m : movies) {
                        if (!u.getHistory().containsKey(m.getTitle())) {
                            seenAll = false;
                            if (m.getRating() != 0) {
                                recommendations.add(new Pair(m.getTitle(), m.getRating()));
                            } else {
                                unrated.add(m.getTitle());
                            }
                        }
                    }
                    for (SerialInputData s : serials) {
                        if (!u.getHistory().containsKey(s.getTitle())) {
                            seenAll = false;
                            if (s.getRating() != 0) {
                                recommendations.add(new Pair(s.getTitle(), s.getRating()));
                            } else {
                                unrated.add(s.getTitle());
                            }
                        }
                    }

                    if (seenAll) {
                        message.put("message", "BestRatedUnseenRecommendation cannot be applied!");
                    } else {
                        if (recommendations.size() > 0) {
                            Collections.sort(recommendations, Pair.ratingDes);
                            message.put("message", "BestRatedUnseenRecommendation result: "
                                    + recommendations.get(0).name);
                        } else {
                            message.put("message", "BestRatedUnseenRecommendation result: "
                                    + unrated.get(0));
                        }
                    }
                }
            }
        } else if (a.getType().equals("popular")) {
            ArrayList<Pair> popularity = new ArrayList<>();
            for (Genre g : Genre.values()) {
                popularity.add(new Pair(g.name(), 0));
            }
            for (UserInputData u : users) {
                for (Map.Entry<String, Integer> e : u.getHistory().entrySet()) {
                    for (MovieInputData m : movies) {
                        if (m.getTitle().equals(e.getKey())) {
                            for (Pair p : popularity) {
                                for (String genre : m.getGenres()) {
                                    if (p.name.equals(stringToGenre(genre).name())) {
                                        p.value++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            boolean exit_loop = false;
            for (UserInputData u : users) {
                if (u.getUsername().equals(a.getUsername())
                        && u.getSubscriptionType().equals("PREMIUM")) {
                    Collections.sort(popularity, Pair.ratingDes);
                    for (int i = 0; i < popularity.size(); ++i) {
                        for (MovieInputData m : movies) {
                            if (!u.getHistory().containsKey(m.getTitle())) {
                                for (String genre : m.getGenres()) {
                                    if (popularity.get(i).name.equals(stringToGenre(
                                            genre).name())) {
                                        message.put("message", "PopularRecommendation result: "
                                                + m.getTitle());
                                        exit_loop = true;
                                    }
                                    if (exit_loop) {
                                        break;
                                    }
                                }
                            }
                            if (exit_loop) {
                                break;
                            }
                        }
                        for (SerialInputData s : serials) {
                            if (!u.getHistory().containsKey(s.getTitle())) {
                                for (String genre : s.getGenres()) {
                                    if (popularity.get(i).name.equals(stringToGenre(
                                            genre).name())) {
                                        message.put("message", "PopularRecommendation result: "
                                                + s.getTitle());
                                        exit_loop = true;
                                    }
                                    if (exit_loop) {
                                        break;
                                    }
                                }
                            }
                            if (exit_loop) {
                                break;
                            }
                        }
                        if (exit_loop) {
                            break;
                        }
                    }
                }
            }
             if (!message.containsKey("message")) {
                message.put("message", "PopularRecommendation cannot be applied!");
            }
        } else if (a.getType().equals("favorite")) {
            for (MovieInputData m : movies) {
                int numFav = 0;
                boolean premium = false;

                for (UserInputData u : users) {
                    if (!u.getUsername().equals(a.getUsername())) {
                        if (u.getFavoriteMovies().contains(m.getTitle())) {
                            numFav++;
                        }
                    } else {
                        if (!u.getHistory().containsKey(m.getTitle())) {
                            if (u.getSubscriptionType().equals("PREMIUM")) {
                                premium = true;
                            }
                        }
                        if (u.getSubscriptionType().equals("BASIC")) {
                            message.put("message", "FavoriteRecommendation cannot be applied!");
                        }
                    }
                }
                if (premium) {
                    pairs.add(new Pair(m.getTitle(), numFav));
                }
            }

            for (SerialInputData s : serials) {
                int numFav = 0;
                boolean premium = false;

                for (UserInputData u : users) {
                    if (!u.getUsername().equals(a.getUsername())) {
                        if (u.getFavoriteMovies().contains(s.getTitle())) {
                            numFav++;
                        }
                    } else {
                        if (!u.getHistory().containsKey(s.getTitle())) {
                            if (u.getSubscriptionType().equals("PREMIUM")) {
                                premium = true;
                            }
                        }
                        if (u.getSubscriptionType().equals("BASIC")) {
                            message.put("message", "FavoriteRecommendation cannot be applied!");
                        }
                    }
                }
                if (premium) {
                    pairs.add(new Pair(s.getTitle(), numFav));
                }
            }

            if (pairs.size() > 0) {
                Collections.sort(pairs, Pair.ratingDes);
                message.put("message", "FavoriteRecommendation result: " + pairs.get(0).name);
            } else {
                message.put("message", "FavoriteRecommendation cannot be applied!");
            }
        } else if (a.getType().equals("search")) {
            for (UserInputData u : users) {
                if (u.getUsername().equals(a.getUsername())
                        && u.getSubscriptionType().equals("PREMIUM")) {
                    for (MovieInputData m : movies) {
                        if (!u.getHistory().containsKey(m.getTitle())) {
                            if (m.getGenres().contains(a.getGenre())) {
                                pairs.add(new Pair(m.getTitle(), m.getRating()));
                            }
                        }
                    }

                    for (SerialInputData s : serials) {
                        if (!u.getHistory().containsKey(s.getTitle())) {
                            if (s.getGenres().contains(a.getGenre())) {
                                pairs.add(new Pair(s.getTitle(), s.getRating()));
                            }
                        }
                    }
                }
            }
            Collections.sort(pairs, Pair.nameAsc);
            String finalMessage = "SearchRecommendation result: [";
            if (pairs.size() > 0) {
                for (int i = 0; i < pairs.size(); ++i) {
                    finalMessage += pairs.get(i).name + ", ";
                }
                finalMessage = finalMessage.substring(0, finalMessage.length() - 2);
                finalMessage += "]";
                message.put("message", finalMessage);
            } else {
                message.put("message", "SearchRecommendation cannot be applied!");
            }
        }
        super.getArrayResult().add(message);
    }
}
