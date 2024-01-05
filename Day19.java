package me.illumination;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {

    public static void main(String[] args) throws IOException {
        List<String> ss = FileUtils.readLines(new File("C:\\Users\\cuohuoqiu\\IdeaProjects\\aoc2023\\src\\main\\resources\\day19.txt"), StandardCharsets.UTF_8);
        System.out.println(solve(ss));
        System.out.println(solve2(ss));
    }
    
    private static long solve2(List<String> ss) {
        Result result = getResult(ss);
        Map<String, List<Rule>> map = result.map;
        List<List<Expr>> xxs = new ArrayList<>();
        List<Expr> xs = new ArrayList<>();
        dfs("in", 0, xs, xxs, map);
        long total = 0;
        List<Map<String, Pair>> existed = new ArrayList<>();
        for (int i = 0; i < xxs.size(); i++) {
            var expressions = xxs.get(i);
            Map<String, Pair> ranges = new HashMap<>() {{
                put("x", new Pair(0, 4001));
                put("m", new Pair(0, 4001));
                put("a", new Pair(0, 4001));
                put("s", new Pair(0, 4001));
            }};
            for (var expr : expressions) {
                var varName = expr.varName;
                ranges.put(varName, expr.narrow(ranges.get(varName)));
            }
            var product = 1L;
            var flag = true;
            for (var entry : ranges.entrySet()) {
                var pr = entry.getValue();
                if (pr.lhs >= pr.rhs) {
                    flag = false;
                    break;
                }
                product *= pr.rhs - pr.lhs - 1;
            }
            total += flag ? product : 0;
            // 去掉和前面重复的
            for (var existedMap : existed) {
                total -= calcDupCount(existedMap, ranges);
            }
            existed.add(ranges);
            System.out.println(ranges);
        }
        return total;
    }

    private static final String[] KEYS = {"a", "s", "m", "x"};

    private static long calcDupCount(Map<String, Pair> lhs, Map<String, Pair> rhs) {
        long product = 1; boolean flag = true;
        for (var key : KEYS) {
            if (!lhs.containsKey(key) && !rhs.containsKey(key)) {
                product *= 4000;
            } else if (!lhs.containsKey(key)) {
                var p = rhs.get(key);
                product *= p.rhs - p.lhs - 1;
            } else if (!rhs.containsKey(key)) {
                var p = lhs.get(key);
                product *= p.rhs - p.lhs - 1;
            } else {
                var pl = lhs.get(key);
                var pr = rhs.get(key);
                var p = new Pair(Math.max(pl.lhs, pr.lhs), Math.min(pl.rhs, pr.rhs));
                if (p.lhs >= p.rhs) {
                    flag = false;
                    break;
                }
                product *= p.rhs - p.lhs - 1;
            }
        }
        return flag ? product : 0;
    }

    private static void dfs(String curr, int start, List<Expr> xs, List<List<Expr>> xxs, Map<String, List<Rule>> map) {
        if ("A".equals(curr)) {
            xxs.add(new ArrayList<>(xs));
            return;
        }
        if ("R".equals(curr)) {
            return;
        }
        var rules = map.get(curr);
        if (start >= rules.size()) {
            return;
        }
        var rule = rules.get(start);
        if (rule.expr == null) {
            dfs(rule.ret, 0, xs, xxs, map);
        } else {
            xs.add(rule.expr.negate());
            dfs(curr, start + 1, xs, xxs, map);
            xs.removeLast();
            xs.add(rule.expr);
            dfs(rule.ret, 0, xs, xxs, map);
            xs.removeLast();
        }
    }

    private static int solve(List<String> ss) {
        Result result = getResult(ss);
        var start = "in";
        var totalList = result.envList().stream().filter(env -> {
            var curr = start;
            do {
                for (Rule rule : result.map().get(curr)) {
                    if (rule.expr == null || rule.expr.eval(env)) {
                        curr = rule.ret;
                        break;
                    }
                }
            } while (!"A".equals(curr) && !"R".equals(curr));
            return "A".equals(curr);
        }).toList();
        return totalList.stream().map(env -> env.values().stream().reduce(Integer::sum).get()).reduce(Integer::sum).get();
    }

    private static Result getResult(List<String> ss) {
        Map<String, List<Rule>> map = new HashMap<>();
        List<Map<String, Integer>> envList = new ArrayList<>();
        boolean shouldParseEnv = false;
        for (var s : ss) {
            if (s.isBlank()) {
                shouldParseEnv = true;
                continue;
            }
            if (!shouldParseEnv) {
                List<Rule> rules = new ArrayList<>();
                var openCurlyIdx = s.indexOf('{');
                var name = s.substring(0, openCurlyIdx);
                var rulesStr = s.substring(openCurlyIdx + 1, s.length() - 1);
                for (var ruleStr : rulesStr.split(",")) {
                    rules.add(Rule.from(ruleStr));
                }
                map.put(name, rules);
            } else {
                Map<String, Integer> env = new HashMap<>();
                for (var vs : s.substring(1, s.length() -1 ).split(",")) {
                    env.put(vs.substring(0, 1), Integer.parseInt(vs.substring(2)));
                }
                envList.add(env);
            }
        }
        return new Result(map, envList);
    }

    private record Result(Map<String, List<Rule>> map, List<Map<String, Integer>> envList) {
    }

    private static class Rule {
        Expr expr;
        String ret;

        public Rule(Expr expr, String ret) {
            this.expr = expr;
            this.ret = ret;
        }

        public static Rule from(String s) {
            if (s.indexOf(':') != -1) {
                var parts = s.split(":");
                String varName = parts[0].substring(0, 1);
                String op = parts[0].substring(1, 2);
                int value = Integer.parseInt(parts[0].substring(2));
                return new Rule(new Expr(varName, ">".equals(op) ? OPER.GT : OPER.LT, value), parts[1]);
            } else {
                return new Rule(null, s);
            }
        }
    }

    enum OPER {
        LT,
        GT;
    }

    private static class Expr {
        String varName;
        OPER oper;
        int value;

        public Expr(String varName, OPER oper, int value) {
            this.varName = varName;
            this.oper = oper;
            this.value = value;
        }

        public boolean eval(Map<String, Integer> env) {
            return switch (this.oper) {
                case GT -> env.get(varName) > value;
                case LT -> env.get(varName) < value;
            };
        }

        public Expr negate() {
            return switch (this.oper) {
                case GT -> new Expr(varName, OPER.LT, value + 1);
                case LT -> new Expr(varName, OPER.GT, value - 1);
            };
        }

        public Pair narrow(Pair origin) {
            int lhs = origin.lhs, rhs = origin.rhs;
            return switch (this.oper) {
                case GT -> new Pair(Math.max(lhs, value), rhs);
                case LT -> new Pair(lhs, Math.min(rhs, value));
            };
        }
    }

    private static class Pair {
        int lhs, rhs;

        public Pair(int lhs, int rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "lhs=" + lhs +
                    ", rhs=" + rhs +
                    '}';
        }
    }
}
