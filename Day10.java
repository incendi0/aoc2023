package me.illumination;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day10 {

    private static final int[][] neighbours = {
            {0, 1}, {1, 0}, {0, -1}, {-1, 0}
    };

    private static final int[][] around = {
            {0, 1}, {1, 0}, {0, -1}, {-1, 0}, {-1, -1}, {1, 1}, {1, -1}, {-1, 1}
    };

    private static final Map<Character, int[][]> pipes = Map.of(
            '|', new int[][] {{1, 0}, {-1, 0}},
            '-', new int[][] {{0, 1}, {0, -1}},
            'L', new int[][] {{-1, 0}, {0, 1}},
            'J', new int[][] {{0, -1}, {-1, 0}},
            '7', new int[][] {{1, 0}, {0, -1}},
            'F', new int[][] {{1, 0}, {0, 1}}
    );

    public static void main(String[] args) throws IOException {
        var ss = FileUtils.readLines(new File(Objects.requireNonNull(Day10.class.getResource("/day10.txt")).getPath()), StandardCharsets.UTF_8);
        var grid = solve(ss);
        solve2(ss, grid);
    }

    //https://zh.wikipedia.org/wiki/%E7%9A%AE%E5%85%8B%E5%AE%9A%E7%90%86
    private static void solve2(List<String> ss, List<Pair<Integer, Integer>> grid) {
        Function<List<Pair<Integer, Integer>>, Integer> area  = g -> {
            var points = g.stream().filter(p -> ss.get(p.getLeft()).charAt(p.getRight()) != '|' && ss.get(p.getLeft()).charAt(p.getRight()) != '-').collect(Collectors.toList());
            points.add(g.get(0));
            int ret = 0;
            for (int i = 1; i < points.size(); i++) {
                var last = points.get(i - 1);
                var curr = points.get(i);
                ret += last.getLeft() * curr.getRight() - last.getRight() * curr.getLeft();
            }
            return Math.abs(ret) / 2;
        };
        System.out.println(area.apply(grid) - grid.size() / 2 + 1);
    }

    private static void dfs(char[][] matrix, int x, int y, Map<Pair<Integer, Integer>, Integer> fence) {
        if (x < 0 || x >= matrix.length || y < 0 || y >= matrix[0].length || matrix[x][y] != '.') {
            return;
        }
        matrix[x][y] = 'O';
        for (var delta : around) {
            int nx = x + delta[0], ny = y + delta[1];
            dfs(matrix, nx, ny, fence);
        }
    }

    public static List<Pair<Integer, Integer>> solve(List<String> ss) {
        var p = findStartPoint(ss);
        var map = new HashMap<Pair<Integer, Integer>, Integer>();
        var grid = new ArrayList<Pair<Integer, Integer>>();
        map.put(p, 0);
        grid.add(p);
        var curr = findFirstConnectsToCurrExcept(p, null, ss);
        map.put(curr, 1);
        grid.add(curr);
        int count = 2;
        while (true) {
            curr = findFirstConnectsToCurrExcept(curr, k -> !map.containsKey(k), ss);
            if (curr == null) {
                break;
            }
            grid.add(curr);
            map.put(curr, count++);
        }
        System.out.println((count + 1) / 2);
        return grid;
    }


    private static Pair<Integer, Integer> findFirstConnectsToCurrExcept(Pair<Integer, Integer> curr, Function<Pair<Integer, Integer>, Boolean> func, List<String> ss) {
        char currChar = ss.get(curr.getLeft()).charAt(curr.getRight());
        if (currChar == 'S') {
            for (int[] neighbour : neighbours) {
                int dx = neighbour[0], dy = neighbour[1];
                int nx = curr.getLeft() + dx, ny = curr.getRight() + dy;
                if (nx < 0 || nx >= ss.size() || ny < 0 || ny >= ss.get(0).length() || pipes.containsKey(ss.get(nx).charAt(ny))) {
                    for (var delta : pipes.get(ss.get(nx).charAt(ny))) {
                        if (curr.getLeft() == nx + delta[0] && curr.getRight() == ny + delta[1] && (func == null || func.apply(Pair.of(nx, ny)))) {
                            return Pair.of(nx, ny);
                        }
                    }
                }
            }
        } else if (pipes.containsKey(currChar)) {
            for (int[] neighbour : pipes.get(currChar)) {
                int dx = neighbour[0], dy = neighbour[1];
                int nx = curr.getLeft() + dx, ny = curr.getRight() + dy;
                if (nx < 0 || nx >= ss.size() || ny < 0 || ny >= ss.get(0).length() || pipes.containsKey(ss.get(nx).charAt(ny))) {
                    for (var delta : pipes.get(ss.get(nx).charAt(ny))) {
                        if (curr.getLeft() == nx + delta[0] && curr.getRight() == ny + delta[1] && (func == null || func.apply(Pair.of(nx, ny)))) {
                            return Pair.of(nx, ny);
                        }
                    }
                }
            }
        }
        return null;
    }

    private static Pair<Integer, Integer> findStartPoint(List<String> ss) {
        int x = ss.size(), y = ss.get(0).length();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (ss.get(i).charAt(j) == 'S') {
                    return Pair.of(i, j);
                }
            }
        }
        throw new RuntimeException("Illegal input");
    }
}
