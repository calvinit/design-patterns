import java.util.ArrayList;
import java.util.List;
import java.util.Map;

interface Expression {
    boolean interpret(Map<String, Long> stats);
}

class GreaterExpression implements Expression {
    private final String key;
    private final long value;

    public GreaterExpression(String strExpression) {
        String[] es;
        if (strExpression == null || strExpression.isBlank()
                || (es = strExpression.trim().split("\\s+")).length != 3
                || !es[1].trim().equals(">")) {
            throw new RuntimeException("Expression is invalid: " + strExpression);
        }
        key = es[0].trim();
        value = Long.parseLong(es[2].trim());
    }

    public GreaterExpression(String key, long value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean interpret(Map<String, Long> stats) {
        if (!stats.containsKey(key)) {
            return false;
        }
        return stats.get(key) > value;
    }
}

class LessExpression implements Expression {
    private final String key;
    private final long value;

    public LessExpression(String strExpression) {
        String[] es;
        if (strExpression == null || strExpression.isBlank()
                || (es = strExpression.trim().split("\\s+")).length != 3
                || !es[1].trim().equals("<")) {
            throw new RuntimeException("Expression is invalid: " + strExpression);
        }
        key = es[0].trim();
        value = Long.parseLong(es[2].trim());
    }

    public LessExpression(String key, long value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean interpret(Map<String, Long> stats) {
        if (!stats.containsKey(key)) {
            return false;
        }
        return stats.get(key) < value;
    }
}

class EqualExpression implements Expression {
    private final String key;
    private final long value;

    public EqualExpression(String strExpression) {
        String[] es;
        if (strExpression == null || strExpression.isBlank()
                || (es = strExpression.trim().split("\\s+")).length != 3
                || !es[1].trim().equals("==")) {
            throw new RuntimeException("Expression is invalid: " + strExpression);
        }
        key = es[0].trim();
        value = Long.parseLong(es[2].trim());
    }

    public EqualExpression(String key, long value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean interpret(Map<String, Long> stats) {
        if (!stats.containsKey(key)) {
            return false;
        }
        return stats.get(key) == value;
    }
}

class AndExpression implements Expression {
    private final List<Expression> expressions = new ArrayList<>();

    public AndExpression(String strExpression) {
        if (strExpression == null || strExpression.isBlank()) {
            throw new RuntimeException("Expression is invalid: " + strExpression);
        }
        String[] es = strExpression.trim().split("&&");
        for (String e : es) {
            if (e.contains(">")) {
                expressions.add(new GreaterExpression(e));
            } else if (e.contains("<")) {
                expressions.add(new LessExpression(e));
            } else if (e.contains("==")) {
                expressions.add(new EqualExpression(e));
            } else {
                throw new RuntimeException("Expression is invalid: " + strExpression);
            }
        }
    }

    public AndExpression(List<Expression> expressions) {
        this.expressions.addAll(expressions);
    }

    @Override
    public boolean interpret(Map<String, Long> stats) {
        return expressions.stream().allMatch(expression -> expression.interpret(stats));
    }
}

class OrExpression implements Expression {
    private final List<Expression> expressions = new ArrayList<>();

    public OrExpression(String strExpression) {
        if (strExpression == null || strExpression.isBlank()) {
            throw new RuntimeException("Expression is invalid: " + strExpression);
        }
        String[] es = strExpression.trim().split("\\|\\|");
        for (String e : es) {
            expressions.add(new AndExpression(e));
        }
    }

    public OrExpression(List<Expression> expressions) {
        this.expressions.addAll(expressions);
    }

    @Override
    public boolean interpret(Map<String, Long> stats) {
        return expressions.stream().anyMatch(expression -> expression.interpret(stats));
    }
}

class AlertRuleInterpreter {
    private final Expression expression;

    public AlertRuleInterpreter(String ruleExpression) {
        if (ruleExpression == null || ruleExpression.isBlank()) {
            throw new RuntimeException("Expression is invalid: " + ruleExpression);
        }
        this.expression = new OrExpression(ruleExpression);
    }

    public boolean interpret(Map<String, Long> stats) {
        return expression.interpret(stats);
    }
}