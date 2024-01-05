package me.illumination;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day23 {

    public static void main(String[] args) throws IOException {
        List<String> ss = FileUtils.readLines(new File("C:\\Users\\cuohuoqiu\\IdeaProjects\\aoc2023\\src\\main\\resources\\day23.txt"), StandardCharsets.UTF_8);
        int m = ss.size(), n = ss.get(0).length();
        char[][] matrix = new char[m][n];
        MutablePair<Integer, Integer> entrance = new MutablePair<>(-1, -1);
        MutablePair<Integer, Integer> destination = new MutablePair<>(-1, -1);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = ss.get(i).charAt(j);
                if (i == 0 && matrix[i][j] == '.') {
                    entrance.setLeft(i);
                    entrance.setRight(j);
                }
                if (i == m - 1 && matrix[i][j] == '.') {
                    destination.setLeft(i);
                    destination.setRight(j);
                }
            }
        }
        System.out.println(solve(matrix, entrance, destination));
//        https://github.com/MeisterLLD/aoc2023/blob/main/23.py
        System.out.println(solve2(matrix, entrance, destination));
    }

    public static int solve(char[][] matrix, MutablePair<Integer, Integer> entrance, MutablePair<Integer, Integer> destination) {
        List<Pair<Integer, Integer>> xs = new ArrayList<>();
        List<List<Pair<Integer, Integer>>> xxs = new ArrayList<>();
        Set<Pair<Integer, Integer>> visited = new HashSet<>();
        dfs(entrance, destination, visited, xs, xxs, matrix, directions);
        return xxs.stream().map(List::size).max(Comparator.naturalOrder()).get();
    }

    private static final int[][] directions = new int[][] {
            {0, 1}, {1, 0}, {0, -1}, {-1, 0}
    };

    private static final Map<Character, int[]> map = new HashMap<>() {{
        put('>', new int[] {0, 1});
        put('<', new int[] {0, -1});
        put('^', new int[] {-1, 0});
        put('v', new int[] {1, 0});
    }};

    private static void dfs(Pair<Integer, Integer> curr,
                            Pair<Integer, Integer> destination,
                            Set<Pair<Integer, Integer>> visited,
                            List<Pair<Integer, Integer>> xs,
                            List<List<Pair<Integer, Integer>>> xxs,
                            char[][] matrix,
                            int[][] ds) {
        if (curr.equals(destination)) {
            xxs.add(new ArrayList<>(xs));
            return;
        }
        for (int[] dir : ds) {
            int nx = curr.getLeft() + dir[0], ny = curr.getRight() + dir[1];
            var next = new ImmutablePair<>(nx, ny);
            if (nx >= 0 && nx < matrix.length && ny >= 0 && ny < matrix[0].length && matrix[nx][ny] != '#' && !visited.contains(next)) {
                visited.add(next);
                xs.add(next);
                if (matrix[nx][ny] != '.') {
                    int[][] singleDirection = new int[][] {map.get(matrix[nx][ny])};
                    dfs(next, destination, visited, xs, xxs, matrix, singleDirection);
                } else {
                    dfs(next, destination, visited, xs, xxs, matrix, directions);
                }
                xs.removeLast();
                visited.remove(next);
            }
        }
    }

    public static int solve2(char[][] matrix,
                             Pair<Integer, Integer> entrance,
                             Pair<Integer, Integer> destination) {
        Set<Pair<Integer, Integer>> bifurcationsSet = new HashSet<>();
        bifurcationsSet.add(entrance);
        bifurcationsSet.add(destination);
        int m = matrix.length, n = matrix[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                var ns = neighbours(new ImmutablePair<>(i, j), matrix);
                if (ns.size() > 2) {
                    bifurcationsSet.add(new ImmutablePair<>(i, j));
                }
            }
        }
        var graph = new HashMap<Pair<Integer, Integer>, List<Pair<Pair<Integer, Integer>, Integer>>>();
        for (var b : bifurcationsSet) {
            for (var neighbour : neighbours(b, matrix)) {
                var previous = b;
                var curr = neighbour;
                var d = 1;
                while (!bifurcationsSet.contains(curr)) {
                    var tmp = curr;
                    Pair<Integer, Integer> finalPrevious = previous;
                    curr = neighbours(curr, matrix).stream().filter(p -> !p.equals(finalPrevious)).findFirst().get();
                    previous = tmp;
                    d += 1;
                }
                var vs = graph.getOrDefault(b, new ArrayList<>());
                vs.add(new ImmutablePair<>(curr, d));
                graph.put(b, vs);
            }
        }

        List<Integer> lens = new ArrayList<>();
        Set<Pair<Integer, Integer>> seen = new HashSet<>();
        seen.add(entrance);
        dfs2(entrance, destination, 0, seen, lens, graph);
        return lens.stream().max(Comparator.naturalOrder()).get();
    }

    private static void dfs2(Pair<Integer, Integer> curr, Pair<Integer, Integer> destination,
                             int dist, Set<Pair<Integer, Integer>> seen, List<Integer> lens,
                             Map<Pair<Integer, Integer>, List<Pair<Pair<Integer, Integer>, Integer>>> graph) {
        if (curr.equals(destination)) {
            lens.add(dist);
            return;
        }
        for (var entry: graph.get(curr)) {
            var v = entry.getKey();
            var d = entry.getValue();
            if (!seen.contains(v)) {
                seen.add(v);
                dfs2(v, destination, dist + d, seen, lens, graph);
                seen.remove(v);
            }
        }
    }

    private static List<Pair<Integer, Integer>> neighbours(Pair<Integer, Integer> curr, char[][] matrix) {
        List<Pair<Integer, Integer>> ret = new ArrayList<>();
        for (var d : directions) {
            int nx = curr.getLeft() + d[0], ny = curr.getRight() + d[1];
            if (nx >= 0 && nx < matrix.length && ny >= 0 && ny < matrix[0].length && matrix[nx][ny] != '#') {
                ret.add(new ImmutablePair<>(nx, ny));
            }
        }
        return ret;
    }
}
