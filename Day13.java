package me.illumination;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Day13 {

    public static void main(String[] args) throws IOException {
        var ss = FileUtils.readLines(new File(Objects.requireNonNull(Day10.class.getResource("/day13.txt")).getPath()), StandardCharsets.UTF_8);
        ss.add("");
        solve(ss);
        solve2(ss);
    }

    private static void solve2(List<String> ss) {
        int lastRow = 0;
        List<Integer> xs = new ArrayList<>();
        for (var i = 0; i < ss.size(); i++) {
            if (StringUtils.isBlank(ss.get(i))) {
                if (lastRow == i) {
                    lastRow = i + 1;
                    continue;
                }
                List<Integer> row = new ArrayList<>();
                for (int j = lastRow; j < i; j++) {
                    int repr = 0;
                    for (int k = 0; k < ss.get(j).length(); k++) {
                        repr = repr * 2 + (ss.get(j).charAt(k) == '#' ? 1 : 0);
                    }
                    row.add(repr);
                }
                List<Integer> col = new ArrayList<>();
                for (int j = 0; j < ss.get(lastRow).length(); j++) {
                    int repr = 0;
                    for (int k = lastRow; k < i; k++) {
                        repr = repr * 2 + (ss.get(k).charAt(j) == '#' ? 1 : 0);
                    }
                    col.add(repr);
                }
                int r = calcMirror(row), c = calcMirror(col);
                int r2 = calcMirror2(row, col.size(), r);
                int c2 = calcMirror2(col, row.size(), c);
                if (r2 > 0) {
                    xs.add(100 * r2);
                }
                if (c2 > 0) {
                    xs.add(c2);
                }
                lastRow = i + 1;
            }
        }
        System.out.println(xs.stream().reduce(0, Integer::sum));
    }


    private static void solve(List<String> ss) {
        int lastRow = 0;
//        long total = 0;
        List<Integer> xs = new ArrayList<>();
        for (var i = 0; i < ss.size(); i++) {
            if (StringUtils.isBlank(ss.get(i))) {
                if (lastRow == i) {
                    lastRow = i + 1;
                    continue;
                }
                List<Integer> row = new ArrayList<>();
                for (int j = lastRow; j < i; j++) {
                    int repr = 0;
                    for (int k = 0; k < ss.get(j).length(); k++) {
                        repr = repr * 2 + (ss.get(j).charAt(k) == '#' ? 1 : 0);
                    }
                    row.add(repr);
                }
                List<Integer> col = new ArrayList<>();
                for (int j = 0; j < ss.get(lastRow).length(); j++) {
                    int repr = 0;
                    for (int k = lastRow; k < i; k++) {
                        repr = repr * 2 + (ss.get(k).charAt(j) == '#' ? 1 : 0);
                    }
                    col.add(repr);
                }
                int r = calcMirror(row), c = calcMirror(col);
                if (r > 0) {
                    xs.add(100 * r);
                }
                if (c > 0) {
                    xs.add(c);
                }
                lastRow = i + 1;
            }
        }
        System.out.println(xs.stream().reduce(0, Integer::sum));
    }

    private static int calcMirror(List<Integer> xs) {
        int sz = xs.size();
        for (int row = 0; row < sz - 1; row++) {
            int len = Math.min(row + 1, sz - row - 1);
            boolean mirror = true;
            for (int i = 0; i < len; i++) {
                if (!xs.get(row - i).equals(xs.get(row + i + 1))) {
                    mirror = false;
                    break;
                }
            }
            if (mirror) {
                return row + 1;
            }
        }
        return 0;
    }

    private static int calcMirror2(List<Integer> xs, int bits, int excluded) {
        for (int i = 0; i < xs.size(); i++) {
            int val = xs.get(i);
            for (int j = 0; j < bits; j++) {
                xs.set(i, val ^ (1 << j));
                int line = calcMirror3(xs, excluded);
                if (line != 0 && line != excluded) {
                    return line;
                }
            }
            xs.set(i, val);
        }
        return 0;
    }

    private static int calcMirror3(List<Integer> xs, int excluded) {
        int sz = xs.size();
        for (int row = 0; row < sz - 1; row++) {
            int len = Math.min(row + 1, sz - row - 1);
            boolean mirror = true;
            for (int i = 0; i < len; i++) {
                if (!xs.get(row - i).equals(xs.get(row + i + 1))) {
                    mirror = false;
                    break;
                }
            }
            if (mirror && row + 1 != excluded) {
                return row + 1;
            }
        }
        return 0;
    }

}
