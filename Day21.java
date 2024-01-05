package me.illumination;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

//https://github.com/villuna/aoc23/wiki/A-Geometric-solution-to-advent-of-code-2023,-day-21
public class Day21 {

    public static void main(String[] args) throws IOException {
        List<String> ss = FileUtils.readLines(new File("C:\\Users\\cuohuoqiu\\IdeaProjects\\aoc2023\\src\\main\\resources\\day20"), StandardCharsets.UTF_8);
        int m = ss.size(), n = ss.get(0).length();
        char[][] matrix = new char[m][n];
        Pair s = new Pair(-1, -1);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = ss.get(i).charAt(j);
                if (matrix[i][j] == 'S') {
                    matrix[i][j] = '.';
                    s.x = i;
                    s.y = j;
                }
            }
        }
        System.out.println(solve(matrix, s));

        System.out.println(solve2(matrix, s));
    }

    public static long solve2(char[][] matrix, Pair s) {
        int m = matrix.length, n = matrix[0].length;
        Map<Pair, Integer> visited = new HashMap<>();
        Queue<State> q = new ArrayDeque<>();
        q.offer(new State(0, s));
        while (!q.isEmpty()) {
            var curr = q.poll();
            if (visited.containsKey(curr.pos)) {
                continue;
            }
            visited.put(curr.pos, curr.dist);
            for (var d : directions) {
                int nx = curr.pos.x + d[0];
                int ny = curr.pos.y + d[1];
                if (nx >= 0 && nx < m && ny >= 0 && ny < n) {
                    if (!visited.containsKey(new Pair(nx, ny)) && matrix[nx][ny] != '#') {
                        q.offer(new State(curr.dist + 1, new Pair(nx, ny)));
                    }
                }
            }
        }
        long evenCorners = visited.values().stream().filter(v -> v % 2 == 0 && v > 65).count();
        long oddCorners = visited.values().stream().filter(v -> v % 2 == 1 && v > 65).count();
        long n1 = ((26501365 - (m / 2)) / m);
        assert n1 == 202300;
        long even = n1 * n1, odd = (n1 + 1) * (n1 + 1);
        return odd * visited.values().stream().filter(v -> v % 2 == 1).count() +
                even * visited.values().stream().filter(v -> v % 2 == 0).count() +
                - ((n1 + 1) * oddCorners)
                + n1 * evenCorners;
    }

    public static class State {
        int dist;
        Pair pos;

        public State(int dist, Pair pos) {
            this.dist = dist;
            this.pos = pos;
        }
    }

    private static final int[][] directions = {
            {0, 1}, {1, 0}, {0, -1}, {-1, 0}
    };

    public static int solve(char[][] matrix, Pair s) {
        int m = matrix.length, n = matrix[0].length;
        Set<Pair> collections = new HashSet<>();
        collections.add(s);
        for (int i = 0; i < 64; i++) {
            Set<Pair> tmp = new HashSet<>();
            collections.forEach(p -> {
                for (var dir : directions) {
                    int nx = p.x + dir[0], ny = p.y + dir[1];
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n && matrix[nx][ny] == '.') {
                        tmp.add(new Pair(nx, ny));
                    }
                }
            });
            collections = tmp;
        }
        return collections.size();
    }

    public static class Pair {
        int x, y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return x == pair.x && y == pair.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
