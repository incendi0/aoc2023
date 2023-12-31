package me.illumination;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Day17 {

    public static void main(String[] args) throws IOException {
        var ss = FileUtils.readLines(new File(Objects.requireNonNull(Day10.class.getResource("/day17.txt")).getPath()), StandardCharsets.UTF_8);
        int m = ss.size(), n = ss.get(0).length();
        int[][] matrix = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
        }
    }


}
