package me.illumination;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day11 {

    public static void main(String[] args) throws IOException {
        var ss = FileUtils.readLines(new File(Objects.requireNonNull(Day10.class.getResource("/day11.txt")).getPath()), StandardCharsets.UTF_8);
        var matrix = expand(ss);
        solve(matrix);
        solve2(ss);
    }

    public static void solve2(List<String> ss) {
        int x = ss.size(), y = ss.get(0).length();
        var rowHasGalaxy = new boolean[x];
        var colHasGalaxy = new boolean[y];
        var galaxies = new ArrayList<Pair<Integer, Integer>>();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (ss.get(i).charAt(j) == '#') {
                    rowHasGalaxy[i] = true;
                    colHasGalaxy[j] = true;
                    galaxies.add(Pair.of(i, j));
                }
            }
        }
        var total = 0L;
        for (var i = 0; i < galaxies.size(); i++) {
            for (var j = i + 1; j < galaxies.size(); j++) {
                var p1 = galaxies.get(i);
                var p2 = galaxies.get(j);
                int l = Math.min(p1.getLeft(), p2.getLeft()), r = Math.max(p1.getLeft(), p2.getLeft());
                int n = Math.min(p1.getRight(), p2.getRight()), s = Math.max(p1.getRight(), p2.getRight());
                total += r - l + s - n;
                for (int k = l; k < r; k++) {
                    if (!rowHasGalaxy[k]) {
                        total += 1000000 - 1;
                    }
                }
                for (int m = n; m < s; m++) {
                    if (!colHasGalaxy[m]) {
                        total += 1000000 - 1;
                    }
                }
            }
        }
        System.out.println(total);
    }

    public static void solve(char[][] matrix) {
        var galaxies = new ArrayList<Pair<Integer, Integer>>();
        for (var i = 0; i < matrix.length; i++) {
            for (var j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == '#') {
                    galaxies.add(Pair.of(i, j));
                }
            }
        }
        var total = 0;
        for (var i = 0; i < galaxies.size(); i++) {
            for (var j = i + 1; j < galaxies.size(); j++) {
                var lhs = galaxies.get(i);
                var rhs = galaxies.get(j);
                total += Math.abs(lhs.getLeft() - rhs.getLeft()) + Math.abs(lhs.getRight() - rhs.getRight());
            }
        }
        System.out.println(total);
    }

    private static char[][] expand(List<String> ss) {
        int x = ss.size(), y = ss.get(0).length();
        var rowHasGalaxy = new boolean[x];
        var colHasGalaxy = new boolean[y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (ss.get(i).charAt(j) == '#') {
                    rowHasGalaxy[i] = true;
                    colHasGalaxy[j] = true;
                }
            }
        }

        int dx = 0;
        for (int i = 0; i < x; i++) {
            if (!rowHasGalaxy[i]) {
                dx++;
            }
        }
        int dy = 0;
        for (int j = 0; j < y; j++) {
            if (!colHasGalaxy[j]) {
                dy++;
            }
        }
        int nx = x + dx, ny = y + dy;
        char[][] matrix = new char[nx][ny];
        int idx = 0;
        for (int i = 0; i < x; i++) {
            if (!rowHasGalaxy[i]) {
                for (int j = 0; j < ny; j++) {
                    matrix[idx][j] = '.';
                    matrix[idx + 1][j] = '.';
                }
                idx += 2;
            } else {
                for (int j = 0; j < ny; j++) {
                    matrix[idx][j] = j >= ss.get(i).length() ? '.' : ss.get(i).charAt(j);
                }
                idx++;
            }
        }
        List<List<Integer>> expandCol = new ArrayList<>();
        idx = 0;
        for (int i = 0; i < y; i++) {
            if (!colHasGalaxy[i]) {
                expandCol.add(List.of(idx, idx + 1));
                idx += 2;
            } else {
                expandCol.add(List.of(idx++));
            }
        }
        for (int j = y - 1; j >= 0; j--) {
            for (int col : expandCol.get(j)) {
                if (col == j) {
                    continue;
                }
                for (int i = 0; i < nx; i++) {
                    matrix[i][col] = matrix[i][j];
                }
            }
        }
        return matrix;
    }
}
