package Classses;

import actor.ActorsAwards;
import fileio.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class Query extends Action {


    public Query(final List<ActorInputData> act, final ActionInputData action,
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

        if (a.getObjectType().equals("actors")) {
            if (a.getCriteria().equals("average")) {
                for (ActorInputData act : actors) {
                    double sum = 0;
                    int nr = 0;
                    for (String title : act.getFilmography()) {
                        for (MovieInputData m : movies) {
                            if (m.getTitle().equals(title) && m.getRating() != 0) {
                                sum += m.getRating();
                                nr++;
                            }
                        }
                        for (SerialInputData s : serials) {
                            if (s.getTitle().equals(title)) {
                                sum += s.getRating();
                                nr++;
                            }
                        }
                    }
                    if (sum > 0) {
                        pairs.add(new Pair(act.getName(), sum / nr));
                    }
                }
            } else if (a.getCriteria().equals("awards")) {
                for (ActorInputData act : actors) {
                    int sum = 0, numAwards = 0;
                    for (List<String> l : a.getFilters()) {
                        if (l != null) {
                            for (String elem : l) {
                                if (elem != null) {
                                    for (Map.Entry<ActorsAwards, Integer> e
                                            : act.getAwards().entrySet()) {
                                        if (elem.equals(e.getKey().name())) {
                                            numAwards++;
                                        }
                                    }
                                }
                            }
                            if (numAwards == l.size()) {
                                for (Map.Entry<ActorsAwards, Integer> entry
                                        : act.getAwards().entrySet()) {
                                    sum += entry.getValue();
                                }
                            }
                        }
                    }
                    if (sum > 0) {
                        pairs.add(new Pair(act.getName(), sum));
                    }
                }
            } else if (a.getCriteria().equals("filter_description")) {
                for (ActorInputData act : actors) {
                    int numDesc = 0;
                    for (List<String> l : a.getFilters()) {
                        if (l != null) {
                            for (String elem : l) {
                                if (elem != null) {
                                    if (Arrays.asList(act.getCareerDescription().toLowerCase(
                                    ).split("[ ,?!.-]+")).contains(elem)) {
                                        numDesc++;
                                    }
                                }
                            }
                            if (numDesc == l.size()) {
                                pairs.add(new Pair(act.getName(), 0));
                            }
                        }
                    }
                }
            }
        } else if (a.getObjectType().equals("movies")) {
            for (MovieInputData m : movies) {
                int year = 0;
                boolean flagGenre = true;

                //if their is no year filter, initialize year with the year of the current movie
                //since their is no reason to apply this filter
                if (a.getFilters().get(0).get(0) == null) {
                    year = m.getYear();
                }
                for (List<String> l : a.getFilters()) {
                    if (l != null) {
                        //this is the year
                        if (l.equals(a.getFilters().get(0)) && l.get(0) != null) {
                            year = Integer.parseInt(l.get(0));
                            //if the year is matching we are looking for the genres
                        } else if (year == m.getYear()) {
                            for (int i = 0; i < l.size(); ++i) {
                                if (!m.getGenres().contains(l.get(i)) && l.get(i) != null) {
                                    flagGenre = false;
                                }
                            }
                        }
                    }
                }
                if (a.getCriteria().equals("ratings")) {
                    if (flagGenre && year == m.getYear() && m.getRating() > 0) {
                        pairs.add(new Pair(m.getTitle(), m.getRating()));
                    }
                } else if (a.getCriteria().equals("favorite")) {
                    int numFav = 0;
                    for (UserInputData u : users) {
                        if (u.getFavoriteMovies().contains(m.getTitle())) {
                            numFav++;
                        }
                    }
                    if (flagGenre && year == m.getYear() && numFav > 0) {
                        pairs.add(new Pair(m.getTitle(), numFav));
                    }
                } else if (a.getCriteria().equals("longest")) {
                    if (flagGenre && year == m.getYear()) {
                        pairs.add(new Pair(m.getTitle(), m.getDuration()));
                    }
                } else if (a.getCriteria().equals("most_viewed")) {
                    int sum = 0;
                    for (UserInputData u : users) {
                        if (u.getHistory().containsKey(m.getTitle())) {
                            for (Map.Entry<String, Integer> e : u.getHistory().entrySet()) {
                                if (e.getKey().equals(m.getTitle())) {
                                    sum += e.getValue();
                                }
                            }
                        }
                    }
                    if (flagGenre && year == m.getYear() && sum > 0) {
                        pairs.add(new Pair(m.getTitle(), sum));
                    }
                }
            }
        } else if (a.getObjectType().equals("shows")) {
            for (SerialInputData s : serials) {
                int year = 0;
                boolean flagGenre = true;
                // same reason as before
                if (a.getFilters().get(0).get(0) == null) {
                    year = s.getYear();
                }

                for (List<String> list : a.getFilters()) {
                    if (list != null) {
                        if (list.equals(a.getFilters().get(0)) && list.get(0) != null) {
                            year = Integer.parseInt(list.get(0));
                        } else if (year == s.getYear()) {
                            for (int i = 0; i < list.size(); ++i) {
                                if (list.get(i) != null) {
                                    if (!s.getGenres().contains(list.get(i))) {
                                        flagGenre = false;
                                    }
                                }
                            }
                        }
                    }
                }
                if (a.getCriteria().equals("ratings")) {
                    if (flagGenre && year == s.getYear() && s.getRating() > 0) {
                        pairs.add(new Pair(s.getTitle(), s.getRating()));
                    }
                } else if (a.getCriteria().equals("favorite")) {
                    int numFav = 0;
                    for (UserInputData u : users) {
                        if (u.getFavoriteMovies().contains(s.getTitle())) {
                            numFav++;
                        }
                    }
                    if (flagGenre && year == s.getYear() && numFav > 0) {
                        pairs.add(new Pair(s.getTitle(), numFav));
                    }
                } else if (a.getCriteria().equals("longest")) {
                    int sum = 0;
                    for (int i = 0; i < s.getSeasons().size(); i++) {
                        sum += s.getSeasons().get(i).getDuration();
                    }
                    if (flagGenre && year == s.getYear() && sum > 0) {
                        pairs.add(new Pair(s.getTitle(), sum));
                    }
                } else if (a.getCriteria().equals("most_viewed")) {
                    int sum = 0;
                    for (UserInputData u : users) {
                        if (u.getHistory().containsKey(s.getTitle())) {
                            for (Map.Entry<String, Integer> e : u.getHistory().entrySet()) {
                                if (e.getKey().equals(s.getTitle())) {
                                    sum += e.getValue();
                                }
                            }
                        }
                    }
                    if (flagGenre && year == s.getYear() && sum > 0) {
                        pairs.add(new Pair(s.getTitle(), sum));
                    }
                }
            }
        } else if (a.getObjectType().equals("users")) {
            for (UserInputData u : users) {
                if (!u.getRatings().isEmpty()) {
                    pairs.add(new Pair(u.getUsername(), u.getRatings().size()));
                }
            }
        }
        //sort the array of pairs
        if (a.getSortType().equals("asc")) {
            Collections.sort(pairs, Pair.nameAsc);
            Collections.sort(pairs, Pair.ratingAsc);
        } else {
            Collections.sort(pairs, Pair.nameDesc);
            Collections.sort(pairs, Pair.ratingDes);
        }

        //create the message of the query
        String finalMessage = "Query result: [";
        if (pairs.size() != 0) {
            for (int i = 0; i < pairs.size(); ++i) {
                if (i < a.getNumber()) {
                    finalMessage += pairs.get(i).name + ", ";
                } else {
                    break;
                }
            }
            finalMessage = finalMessage.substring(0, finalMessage.length() - 2);
        }
        finalMessage = finalMessage + "]";
        message.put("message", finalMessage);
        super.getArrayResult().add(message);
    }
}
