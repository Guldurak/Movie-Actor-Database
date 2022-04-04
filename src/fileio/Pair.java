package fileio;

import java.util.Comparator;

public class Pair {
    public String name;
    public double value;

    public Pair(final String n, final double v) {
        name = n;
        value = v;
    }

    //comparators
    public static Comparator<Pair> ratingAsc = new Comparator<Pair>() {
        @Override
        public int compare(final Pair p1, final Pair p2) {
            return Double.compare(p1.value, p2.value);
        }
    };

    public static Comparator<Pair> ratingDes = new Comparator<Pair>() {
        @Override
        public int compare(final Pair p1, final Pair p2) {
            return Double.compare(p2.value, p1.value);
        }
    };

    public static Comparator<Pair> nameAsc = new Comparator<Pair>() {
        @Override
        public int compare(final Pair p1, final Pair p2) {
            return p1.name.compareTo(p2.name);
        }
    };

    public static Comparator<Pair> nameDesc = new Comparator<Pair>() {
        @Override
        public int compare(final Pair p1, final Pair p2) {
            return p2.name.compareTo(p1.name);
        }
    };

    /**
     * @return
     */
    @Override
    public String toString() {
        return
                "name='" + name
                        + '\'' + ", value="
                        + value;
    }
}
