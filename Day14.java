package me.illumination;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day14 {

    public static void main(String[] args) throws IOException {
        var ss2 = FileUtils.readLines(new File(Objects.requireNonNull(Day10.class.getResource("/day14.txt")).getPath()), StandardCharsets.UTF_8);
        var ss = new ArrayList<>(ss2);
        var col = ss.get(0).length();
        var bar = StringUtils.repeat('#', col);
        ss.addFirst(bar);
        ss.add(bar);
        solve(ss);
        solve2(ss2);
    }

    private static void solve2(List<String> ss) {
        int m = ss.size(), n = ss.get(0).length();
        char[][] matrix = new char[m + 2][n + 2];
        for (int i = 0; i < m + 2; i++) {
            for (int j = 0; j < n + 2; j++) {
                if (i == 0 || i == m + 1 || j == 0 || j == n + 1) {
                    matrix[i][j] = '#';
                } else {
                    matrix[i][j] = ss.get(i - 1).charAt(j - 1);
                }
            }
        }
        Map<String, Integer> map = new HashMap<>();
        int last = -1, curr = -1;
        List<char[][]> xs = new ArrayList<>();
        for (int i = 0; i < 107; i++) {
            StringBuilder repr = new StringBuilder();
            for (var row : matrix) {
                repr.append(new String(row));
            }
            var s = repr.toString();
            if (map.containsKey(s)) {
                last = map.get(s);
                curr = i;
                break;
            }
            xs.add(deepCopy(matrix));
            map.put(repr.toString(), i);
            cycle(matrix);
        }
        int idx = (1000000000 - curr) % (curr - last) + last;
        matrix = xs.get(idx);
        List<String> ls = new ArrayList<>();
        for (int i = 1; i < m + 1; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 1; j < n + 1; j++) {
                sb.append(matrix[i][j]);
            }
            ls.add(sb.toString());
        }
        solve3(ls);
    }

    private static void solve3(List<String> ss) {
        int m = ss.size(), n = ss.get(0).length();
        int total = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (ss.get(i).charAt(j) == 'O') {
                    total += m - i;
                }
            }
        }
        System.out.println(total);
    }

    private static char[][] deepCopy(char[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        char[][] ret = new char[m][n];
        for (int i = 0; i < m; i++) {
            System.arraycopy(matrix[i], 0, ret[i], 0, n);
        }
        return ret;
    }

    private static void tiltNorth(char[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        for (int j = 1; j < n - 1; j++) {
            int count = 0, lastIndex = 0;
            for (int i = 0; i < m; i++) {
                if (matrix[i][j] == '#') {
                    for (int k = 1; k <= count; k++) {
                        matrix[lastIndex + k][j] = 'O';
                    }
                    for (int idx = lastIndex + count + 1; idx < i; idx++) {
                        matrix[idx][j] = '.';
                    }
                    count = 0;
                    lastIndex = i;
                } else if (matrix[i][j] == 'O') {
                    count++;
                }
            }
        }
    }

    private static void tiltWest(char[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        for (int i = 1; i < m - 1; i++) {
            int count = 0, lastIndex = 0;
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == '#') {
                    for (int k = 1; k <= count; k++) {
                        matrix[i][lastIndex + k] = 'O';
                    }
                    for (int idx = lastIndex + count + 1; idx < j; idx++) {
                        matrix[i][idx] = '.';
                    }
                    count = 0;
                    lastIndex = j;
                } else if (matrix[i][j] == 'O') {
                    count++;
                }
            }
        }
    }

    private static void swapVertical(char[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        for (int i = 0; i < m / 2; i++) {
            var tmp = matrix[i];
            matrix[i] = matrix[m - i - 1];
            matrix[m - i - 1] = tmp;
        }
    }

    private static void tiltSouth(char[][] matrix) {
        swapVertical(matrix);
        tiltNorth(matrix);
        swapVertical(matrix);
    }

    private static void swapHorizontal(char[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        for (int i = 1; i < m - 1; i++) {
            for (int j = 0; j < n / 2; j++) {
                var tmp = matrix[i][j];
                matrix[i][j] = matrix[i][n - j - 1];
                matrix[i][n - j - 1] = tmp;
            }
        }
    }

    private static void tiltEast(char[][] matrix) {
        swapHorizontal(matrix);
        tiltWest(matrix);
        swapHorizontal(matrix);
    }

    private static void debug(char[][] matrix) {
        for (var row : matrix) {
            System.out.println(new String(row));
        }
    }

    private static void cycle(char[][] matrix) {
        tiltNorth(matrix);
        tiltWest(matrix);
        tiltSouth(matrix);
        tiltEast(matrix);
    }

    private static void solve(List<String> ss) {
        int row = ss.size(), col = ss.get(0).length();
        int total = 0;
        for (int j = 0; j < col; j++) {
            int count = 0, lastIndex = 0;
            for (int i = 0; i < row; i++) {
                if (ss.get(i).charAt(j) == '#') {
                    int a = row - 2 - lastIndex, b = row - 2 - lastIndex - count + 1;
                    total += (a + b) * count / 2;
                    count = 0;
                    lastIndex = i;
                } else if (ss.get(i).charAt(j) == 'O') {
                    count++;
                }
            }
        }
        System.out.println(total);
    }
}
