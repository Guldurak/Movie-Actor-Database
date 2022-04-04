package Classses;
//import com.fasterxml.jackson.databind.Module;
import fileio.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Command extends Action {


    public Command(final List<ActorInputData> act, final ActionInputData action,
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
        ArrayList<UserInputData> users = (ArrayList<UserInputData>) super.getUsers();
        ArrayList<MovieInputData> movies = (ArrayList<MovieInputData>) super.getMovies();
        ArrayList<SerialInputData> serials = (ArrayList<SerialInputData>) super.getShows();


        //the message we are going to add in the arrayResult
        JSONObject message = new JSONObject();
        message.put("id", a.getActionId());

        //execution of the function
        if (a.getType().equals("favorite")) {
            for (UserInputData u : users) {
                if (u.getUsername().equals(a.getUsername())) {
                    //if the user already has this title in this favorite list, send error message
                    if (u.getHistory().containsKey(a.getTitle())) {
                        if (u.getFavoriteMovies().contains(a.getTitle())) {
                            message.put("message", "error -> "  + a.getTitle()
                                    + " is already in favourite list");
                            super.getArrayResult().add(message);
                        } else {
                            //otherwise add the title in the favorite list
                            message.put("message", "success -> " + a.getTitle()
                                    + " was added as favourite");
                            u.getFavoriteMovies().add(a.getTitle());
                            super.getArrayResult().add(message);
                        }
                    } else {
                        message.put("message", "error -> " + a.getTitle() + " is not seen");
                        super.getArrayResult().add(message);
                    }
                }
            }
        } else if (a.getType().equals("view")) {
            for (UserInputData u : users) {
                if (u.getUsername().equals(a.getUsername())) {
                    u.addToHistory(a.getTitle());
                    message.put("message",  "success -> " + a.getTitle()
                            + " was viewed with total views of "
                            + u.getHistory().get(a.getTitle()));
                    super.getArrayResult().add(message);
                }
            }
        } else if (a.getType().equals("rating")) {
            for (UserInputData u : users) {
                if (a.getSeasonNumber() == 0) {
                    //rating a movie
                    if (u.getUsername().equals(a.getUsername())) {
                        for (MovieInputData m : movies) {
                            if (m.getTitle().equals(a.getTitle())) {
                                u.setRating(m, a.getGrade(),  a, super.getArrayResult());
                            }
                        }
                    }
                } else {
                    if (u.getUsername().equals(a.getUsername())) {
                        for (SerialInputData s : serials) {
                            if (s.getTitle().equals(a.getTitle())) {
                                u.setRating(s, a.getGrade(),  a, super.getArrayResult());
                            }
                        }
                    }
                }
            }
        }
    }
}
