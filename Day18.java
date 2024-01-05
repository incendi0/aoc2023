package me.illumination;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day18 {

    private static final Map<Character, int[]> map = Map.of(
            'U', new int[] {-1, 0},
            'D', new int[] {1, 0},
            'L', new int[] {0, -1},
            'R', new int[] {0, 1}
    );

    public static void main(String[] args) throws IOException {
        List<String> ss = FileUtils.readLines(new File("C:\\Users\\cuohuoqiu\\IdeaProjects\\aoc2023\\src\\main\\resources\\day18.txt"), StandardCharsets.UTF_8);
        System.out.println(solve(ss));
        System.out.println(solve2(ss));
    }

    private static long solve2(List<String> ss) {
        List<long[]> vertices = new ArrayList<>();
        long x = 0, y = 0;
        long count = 0;
        for (var s : ss) {
            String dist = s.split("\\s+")[2].substring(2, 8);
            long v = Integer.parseInt(dist.substring(0, 5), 16);
            char d = dist.charAt(dist.length() - 1);
            int[] delta;
            if (d == '0') {
                delta = map.get('R');
            } else if (d == '1') {
                delta = map.get('D');
            } else if (d == '2') {
                delta = map.get('L');
            } else {
                delta = map.get('U');
            }
            x += v * delta[0];
            y += v * delta[1];
            vertices.add(new long[] {x, y});
            count += Math.abs(v * delta[0]) + Math.abs(v * delta[1]);
        }
        long total = 0;
        for (int i = 1; i < vertices.size(); i++) {
            long x1 = vertices.get(i - 1)[0], y1 = vertices.get(i - 1)[1];
            long x2 = vertices.get(i)[0], y2 = vertices.get(i)[1];
            total += (x1 * y2 - x2 * y1);
        }
        return Math.abs(total) / 2 + count - count / 2 + 1;
    }

    private static int solve(List<String> ss) {
        List<int[]> vertices = new ArrayList<>();
        int x = 0, y = 0;
        int count = 0;
        for (var s : ss) {
            String[] parts = s.split("\\s+");
            int[] delta = map.get(parts[0].charAt(0));
            int v = Integer.parseInt(parts[1]);
            x += v * delta[0];
            y += v * delta[1];
            vertices.add(new int[] {x, y});
            count += Math.abs(v * delta[0]) + Math.abs(v * delta[1]);
        }
        int total = 0;
        for (int i = 1; i < vertices.size(); i++) {
            int x1 = vertices.get(i - 1)[0], y1 = vertices.get(i - 1)[1];
            int x2 = vertices.get(i)[0], y2 = vertices.get(i)[1];
            total += (x1 * y2 - x2 * y1);
        }
        return Math.abs(total) / 2 + count - count / 2 + 1;
    }
}
