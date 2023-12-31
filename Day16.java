package me.illumination;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day16 {

    private static final int RIGHT_MOVING = 0;
    private static final int DOWN_MOVING = 1;
    private static final int UP_MOVING = 2;
    private static final int LEFT_MOVING = 3;
    private static final int[][] DELTA = new int[][]{
            {0, 1}, {1, 0}, {-1, 0}, {0, -1}
    };

    private static final Map<Pair<Integer, Character>, int[]> map = new HashMap<>() {{
        put(Pair.of(RIGHT_MOVING, '/'), new int[]{UP_MOVING});
        put(Pair.of(RIGHT_MOVING, '|'), new int[]{UP_MOVING, DOWN_MOVING});
        put(Pair.of(RIGHT_MOVING, '\\'), new int[]{DOWN_MOVING});
        put(Pair.of(LEFT_MOVING, '/'), new int[]{DOWN_MOVING});
        put(Pair.of(LEFT_MOVING, '|'), new int[]{UP_MOVING, DOWN_MOVING});
        put(Pair.of(LEFT_MOVING, '\\'), new int[]{UP_MOVING});
        put(Pair.of(DOWN_MOVING, '/'), new int[]{LEFT_MOVING});
        put(Pair.of(DOWN_MOVING, '-'), new int[]{LEFT_MOVING, RIGHT_MOVING});
        put(Pair.of(DOWN_MOVING, '\\'), new int[]{RIGHT_MOVING});
        put(Pair.of(UP_MOVING, '/'), new int[]{RIGHT_MOVING});
        put(Pair.of(UP_MOVING, '-'), new int[]{LEFT_MOVING, RIGHT_MOVING});
        put(Pair.of(UP_MOVING, '\\'), new int[]{LEFT_MOVING});
    }};


    public static void main(String[] args) throws IOException {
        var ss = FileUtils.readLines(new File(Objects.requireNonNull(Day10.class.getResource("/day16.txt")).getPath()), StandardCharsets.UTF_8);
        System.out.println(solve(ss, Triple.of(0, -1, RIGHT_MOVING)));
        int row = ss.size(), col = ss.get(0).length();
        int ret2 = -1;
        for (int i = 0; i < row; i++) {
            ret2 = Math.max(ret2, solve(ss, Triple.of(i, -1, RIGHT_MOVING)));
        }
        for (int i = 0; i < row; i++) {
            ret2 = Math.max(ret2, solve(ss, Triple.of(i, col, LEFT_MOVING)));
        }
        for (int i = 0; i < col; i++) {
            ret2 = Math.max(ret2, solve(ss, Triple.of(-1, i, DOWN_MOVING)));
        }
        for (int i = 0; i < col; i++) {
            ret2 = Math.max(ret2, solve(ss, Triple.of(row, i, UP_MOVING)));
        }
        System.out.println(ret2);
    }

    private static int solve(List<String> ss, Triple<Integer, Integer, Integer> start) {
        Set<Triple<Integer, Integer, Integer>> visited = new HashSet<>();
        Queue<Triple<Integer, Integer, Integer>> q = new LinkedList<>();
        var curr = start;
        q.add(curr);
        while (!q.isEmpty()) {
            curr = q.poll();
            if (visited.contains(curr)) {
                continue;
            }
            visited.add(curr);
            int x = curr.getLeft(), y = curr.getMiddle(), d = curr.getRight();
            int nx = x + DELTA[d][0], ny = y + DELTA[d][1];
            if (nx >= 0 && nx < ss.size() && ny >= 0 && ny < ss.get(0).length()) {
                char ch = ss.get(nx).charAt(ny);
                if (map.containsKey(Pair.of(d, ch))) {
                    for (var nd : map.get(Pair.of(d, ch))) {
                        q.add(Triple.of(nx, ny, nd));
                    }
                } else {
                    q.add(Triple.of(nx, ny, d));
                }
            }
        }
        Set<Pair<Integer, Integer>> ret = new HashSet<>();
        for (var e : visited) {
            ret.add(Pair.of(e.getLeft(), e.getMiddle()));
        }
        return ret.size() - 1;
    }
}
