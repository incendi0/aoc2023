import java.util.*;

public class Main {

    private static class Pair<L, R> {
        L left;
        R Right;

        public Pair(L left, R right) {
            this.left = left;
            Right = right;
        }

        public L getLeft() {
            return left;
        }

        public R getRight() {
            return Right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(left, pair.left) && Objects.equals(Right, pair.Right);
        }

        @Override
        public int hashCode() {
            return Objects.hash(left, Right);
        }
    }

    private static final int MOVING_VERTICAL = 0;
    private static final int MOVING_HORIZONTAL = 1;

    private static class State {
        Pair<Integer, Integer> coordinate;
        int direction;

        public Pair<Integer, Integer> getCoordinate() {
            return coordinate;
        }

        public int getDirection() {
            return direction;
        }

        public State(Pair<Integer, Integer> coordinate, int direction) {
            this.coordinate = coordinate;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return direction == state.direction && Objects.equals(coordinate, state.coordinate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(coordinate, direction);
        }
    }


    public static void main(String[] args) {
        var s = "2413432311323\n" +
                "3215453535623\n" +
                "3255245654254\n" +
                "3446585845452\n" +
                "4546657867536\n" +
                "1438598798454\n" +
                "4457876987766\n" +
                "3637877979653\n" +
                "4654967986887\n" +
                "4564679986453\n" +
                "1224686865563\n" +
                "2546548887735\n" +
                "4322674655533";
        String[] lines = s.split("\\s+");
        int m = lines.length, n = lines[0].length();
        int[][] matrix = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = lines[i].charAt(j) - '0';
            }
        }
        System.out.println(solve(matrix, 1, 3));
        System.out.println(solve(matrix, 4, 10));
    }

    private static final int[] ds = new int[] {-1, 1};

    private static int solve(int[][] matrix, int minMoves, int maxMoves) {
        int m = matrix.length, n = matrix[0].length;
        var q = new PriorityQueue<Pair<Integer, State>>(Comparator.comparingInt(Pair::getLeft));
        q.offer(new Pair<>(0, new State(new Pair<>(0, 0), 0)));
        q.offer(new Pair<>(0, new State(new Pair<>(0, 0), 1)));
        Set<State> seen = new HashSet<>();
        while (!q.isEmpty()) {
            var curr = q.poll();
            int x = curr.getRight().getCoordinate().getLeft(), y = curr.getRight().getCoordinate().getRight();
            int d = curr.getRight().getDirection(), c = curr.getLeft();
            if (x == m - 1 && y == n - 1) {
                return c;
            }
            if (seen.contains(curr.getRight())) {
                continue;
            }
            seen.add(curr.getRight());
            for (var s : ds) {
                int cost = c;
                int nx = x, ny = y;
                for (int i = 1; i <= maxMoves; i++) {
                    if (d == 1) {
                        nx = x + i * s;
                    } else {
                        ny = y + i * s;
                    }
                    if (nx < 0 || nx >= m || ny < 0 || ny >= n) {
                        break;
                    }
                    cost += matrix[nx][ny];
                    if (seen.contains(new State(new Pair<>(nx, ny), 1 - d))) {
                        continue;
                    }
                    if (i >= minMoves) {
                        q.offer(new Pair<>(cost, new State(new Pair<>(nx, ny), 1 - d)));
                    }
                }
            }
        }
        return -1;
    }
}
