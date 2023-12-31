package me.illumination;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day15 {

    public static void main(String[] args) throws IOException {
        var ss = FileUtils.readLines(new File(Objects.requireNonNull(Day10.class.getResource("/day15.txt")).getPath()), StandardCharsets.UTF_8);
        solve(ss);
        solve2(ss);
    }

    private static void solve2(List<String> ss) {
        for (var s : ss) {
            List<List<Pair<String, Integer>>> xxs = new ArrayList<>();
            for (int i = 0; i < 256; i++) {
                xxs.add(new ArrayList<>());
            }
            for (var e : s.split(",")) {
                if (e.endsWith("-")) {
                    String len = e.substring(0, e.length() - 1);
                    int slot = hash(len);
                    int idx = -1;
                    for (int i = 0; i < xxs.get(slot).size(); i++) {
                        if (xxs.get(slot).get(i).getLeft().equals(len)) {
                            idx = i;
                            break;
                        }
                    }
                    if (idx != -1) {
                        xxs.get(slot).remove(idx);
                    }
                } else {
                    var xs = e.split("=");
                    String len = xs[0];
                    int slot = hash(len);
                    int focal = Integer.parseInt(xs[1]);
                    int idx = -1;
                    for (int i = 0; i < xxs.get(slot).size(); i++) {
                        if (xxs.get(slot).get(i).getLeft().equals(len)) {
                            idx = i;
                            break;
                        }
                    }
                    if (idx != -1) {
                        xxs.get(slot).set(idx, Pair.of(len, focal));
                    } else {
                        xxs.get(slot).add(Pair.of(len, focal));
                    }
                }
            }
            int total = 0;
            for (int i = 0; i < xxs.size(); i++) {
                for (int j = 0; j < xxs.get(i).size(); j++) {
                    total += (i + 1) * (j + 1) * xxs.get(i).get(j).getRight();
                }
            }
            System.out.println(total);
        }
    }

    private static void solve(List<String> ss) {
        int total = 0;
        for (var s : ss) {
            for (var e : s.split(",")) {
                total += hash(e);
            }
        }
        System.out.println(total);
    }

    private static int hash(String s) {
        int value = 0;
        for (int i = 0; i < s.length(); i++) {
            value += s.charAt(i);
            value *= 17;
            value %= 256;
        }
        return value;
    }
}
