import java.util.HashMap;
import java.util.Map;

public class InterpreterTest {
    public static void main(String[] args) {
        String rule = "key1 > 100 && key2 < 30 || key3 < 100 || key4 == 88";
        AlertRuleInterpreter interpreter = new AlertRuleInterpreter(rule);

        Map<String, Long> stats = new HashMap<>();
        stats.put("key1", 101L);
        stats.put("key3", 121L);
        stats.put("key4", 88L);

        boolean matched = interpreter.interpret(stats);
        System.out.printf("[%s] 规则匹配结果：%s ==> %s！\n", rule, stats, matched ? "匹配" : "不匹配");

        stats = new HashMap<>();
        stats.put("key1", 95L);
        stats.put("key2", 29L);
        stats.put("key4", 63L);

        matched = interpreter.interpret(stats);
        System.out.printf("[%s] 规则匹配结果：%s ==> %s！\n", rule, stats, matched ? "匹配" : "不匹配");
    }
}
