package main;

import Classses.Action;
import Classses.Command;
import Classses.Query;
import Classses.Recommendation;
//import actor.ActorsAwards;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import fileio.*;
import org.json.simple.JSONArray;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());
        int id = 0;
        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
                id++;
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();
        Writer fileWriter = new Writer(filePath2);
        ArrayList<Action> actions = new ArrayList<>();
        ArrayList<UserInputData> users = new ArrayList<>();
        JSONArray arrayResult = new JSONArray();

        for (ActionInputData a : input.getCommands()) {
            if (a.getActionType().equals("command")) {
                actions.add(new Command(input.getActors(), a, input.getMovies(), input.getSerials(),
                        input.getUsers(), arrayResult));
            } else if (a.getActionType().equals("query")) {
                actions.add(new Query(input.getActors(), a, input.getMovies(), input.getSerials(),
                        input.getUsers(), arrayResult));
            } else {
                actions.add(new Recommendation(input.getActors(), a, input.getMovies(),
                        input.getSerials(), input.getUsers(), arrayResult));
            }
        }

        //TODO add here the entry point to your implementation
        for (Action a : actions) {
            a.execute();
        }
        fileWriter.closeJSON(arrayResult);
    }
}
