package interpreter

import (
	"strconv"
	"strings"
	"testing"
)

type strlongMap map[string]int64

func (m strlongMap) String() string {
	var builder strings.Builder
	builder.WriteString("{")
	i := 0
	for k, v := range m {
		if i != 0 {
			builder.WriteString(", ")
		}
		i++
		builder.WriteString(k)
		builder.WriteString("=")
		builder.WriteString(strconv.FormatInt(v, 10))
	}
	builder.WriteString("}")
	return builder.String()
}

func matchString(matched bool) string {
	if matched {
		return "匹配"
	}
	return "不匹配"
}

func TestInterpreter(t *testing.T) {
	rule := "key1 > 100 && key2 < 30 || key3 < 100 || key4 == 88"
	interpreter := NewAlertRuleInterpreter(rule)

	stats := strlongMap{
		"key1": 101,
		"key3": 121,
		"key4": 88,
	}

	matched := interpreter.interpret(stats)
	t.Logf("[%s] 规则匹配结果：%s ==> %s！\n", rule, stats, matchString(matched))

	stats = strlongMap{
		"key1": 95,
		"key2": 29,
		"key4": 63,
	}

	matched = interpreter.interpret(stats)
	t.Logf("[%s] 规则匹配结果：%s ==> %s！\n", rule, stats, matchString(matched))
}
