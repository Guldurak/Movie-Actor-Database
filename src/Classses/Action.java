package Classses;

import fileio.*;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public abstract class Action {
    private List<ActorInputData> actors = new ArrayList<>();
    private ActionInputData action;
    private List<MovieInputData> movies = new ArrayList<>();
    private List<SerialInputData> shows = new ArrayList<>();
    private List<UserInputData> users = new ArrayList<>();
    private JSONArray arrayResult = new JSONArray();


    public Action(final List<ActorInputData> act, final ActionInputData action,
                  final List<MovieInputData> movies, final List<SerialInputData> shows,
                  final List<UserInputData> users, final JSONArray arrayResult) {
        actors = act;
        this.action = action;
        this.movies = movies;
        this.shows = shows;
        this.arrayResult = arrayResult;
        this.users = users;
    }


    /**
     * @return
     */
    public List<ActorInputData> getActors() {
        return actors;
    }

    /**
     * @return
     */
    public ActionInputData getAction() {
        return action;
    }

    /**
     * @return
     */
    public List<MovieInputData> getMovies() {
        return movies;
    }

    /**
     * @return
     */
    public List<SerialInputData> getShows() {
        return shows;
    }

    /**
     * @return
     */
    public List<UserInputData> getUsers() {
        return users;
    }

    /**
     * @return
     */
    public JSONArray getArrayResult() {
        return arrayResult;
    }

    /**
     *
     */
    public abstract void execute();
}
