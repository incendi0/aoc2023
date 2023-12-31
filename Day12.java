package me.illumination;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day12 {

    public static void main(String[] args) throws IOException {
        var ss = FileUtils.readLines(new File(Objects.requireNonNull(Day10.class.getResource("/day12.txt")).getPath()), StandardCharsets.UTF_8);
        solve(ss);
        solve2(ss);
//        System.out.println(calc("???.###. 1,1,3"));
//        System.out.println(calc(".??..??...?##. 1,1,3"));
//        System.out.println(calc("?#?#?#?#?#?#?#? 1,3,1,6"));
//        System.out.println(calc("????.#...#... 4,1,1"));
//        System.out.println(calc("????.######..#####. 1,6,5"));
//        System.out.println(calc("?###???????? 3,2,1"));
    }

    private static void solve2(List<String> ss) {
        System.out.println(ss.stream().map(Day12::calc).reduce(Long::sum));
    }

    private static long calc(String s) {
        String[] xs = s.split(" ");
        var count = Arrays.stream(StringUtils.repeat(xs[1] + ",", 5).split(",")).map(Integer::parseInt).toList();
//        avoid corner case
        var record = StringUtils.repeat(xs[0] + "?", 4) + xs[0] + ".";
//        https://www.reddit.com/r/adventofcode/comments/18ge41g/2023_day_12_solutions/
        int m = count.size(), n = record.length();
        long[][] dp = new long[m + 1][n + 1];
        dp[0][n] = 1;
        for (int j = n - 1; j >= 0; j--) {
            if (record.charAt(j) == '#') {
                break;
            }
            dp[0][j] = 1;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = n - 1; j >= 0; j--) {
                if (record.charAt(j) == '.') {
                    dp[i][j] = dp[i][j + 1];
                } else if (record.charAt(j) == '#') {
                    if (canMatch(record, j, count.get(m - i))) {
                        dp[i][j] = dp[i - 1][j + count.get(m - i) + 1];
                    } else {
                        dp[i][j] = 0;
                    }
                } else {
                    dp[i][j] = dp[i][j + 1] +
                            (canMatch(record, j, count.get(m - i)) ? dp[i - 1][j + count.get(m - i) + 1] : 0);
                }
            }
        }
        for (var row : dp) {
            for (var ch : row) {
                System.out.print(ch);
            }
            System.out.println();
        }
        return dp[m][0];
    }

    private static boolean canMatch(String record, int start, int c) {
        return (record.length() - start > c && (record.charAt(start + c) == '.' || record.charAt(start + c) == '?'))
                && record.substring(start, start + c).chars().allMatch(ch -> ch == '#' || ch == '?');
    }

    private static void solve(List<String> ss) {
        System.out.println(ss.stream().map(Day12::figure).reduce(Integer::sum));
    }

    private static int figure(String s) {
        String[] xs = s.split(" ");
        var count = Arrays.stream(xs[1].split(",")).map(Integer::parseInt).toList();
        List<String> records = new ArrayList<>();
        records.add("");
        for (int i = 0; i < xs[0].length(); i++) {
            if (xs[0].charAt(i) == '?') {
                var tmp = new ArrayList<String>();
                for (String record : records) {
                    tmp.add(record + '#');
                    tmp.add(record + '.');
                }
                records = tmp;
            } else {
                for (int j = 0; j < records.size(); j++) {
                    records.set(j, records.get(j) + xs[0].charAt(i));
                }
            }
        }
        var total = 0;
        for (var record : records) {
            var c = Arrays.stream(record.split("\\.")).map(String::length).filter(length -> length > 0).toList();
            total += count.equals(c) ? 1 : 0;
        }
        return total;
    }
}
