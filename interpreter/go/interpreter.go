package interpreter

import (
	"log"
	"strconv"
	"strings"
)

type expression interface {
	interpret(stats map[string]int64) bool
}

type kvExpr struct {
	key   string
	value int64
}

type greater kvExpr

func newGreaterFromStrExpression(expr string) *greater {
	es := strings.Fields(expr)
	if len(es) != 3 || strings.TrimSpace(es[1]) != ">" {
		log.Panicf("Expression is invalid: %s", expr)
	}
	v, _ := strconv.ParseInt(es[2], 10, 64)
	return &greater{
		key:   es[0],
		value: v,
	}
}

func newGreater(key string, value int64) *greater {
	return &greater{
		key:   key,
		value: value,
	}
}

func (g *greater) interpret(stats map[string]int64) bool {
	if v, ok := stats[g.key]; ok {
		return v > g.value
	}
	return false
}

type less kvExpr

func newLessFromStrExpression(expr string) *less {
	es := strings.Fields(expr)
	if len(es) != 3 || strings.TrimSpace(es[1]) != "<" {
		log.Panicf("Expression is invalid: %s", expr)
	}
	v, _ := strconv.ParseInt(es[2], 10, 64)
	return &less{
		key:   es[0],
		value: v,
	}
}

func newLess(key string, value int64) *less {
	return &less{
		key:   key,
		value: value,
	}
}

func (l *less) interpret(stats map[string]int64) bool {
	if v, ok := stats[l.key]; ok {
		return v < l.value
	}
	return false
}

type equal kvExpr

func newEqualFromStrExpression(expr string) *equal {
	es := strings.Fields(expr)
	if len(es) != 3 || strings.TrimSpace(es[1]) != "==" {
		log.Panicf("Expression is invalid: %s", expr)
	}
	v, _ := strconv.ParseInt(es[2], 10, 64)
	return &equal{
		key:   es[0],
		value: v,
	}
}

func newEqual(key string, value int64) *equal {
	return &equal{
		key:   key,
		value: value,
	}
}

func (e *equal) interpret(stats map[string]int64) bool {
	if v, ok := stats[e.key]; ok {
		return v == e.value
	}
	return false
}

type compoundExpr struct {
	expressions []expression
}

type and compoundExpr

func newAndFromStrExpression(expr string) *and {
	if strings.TrimSpace(expr) == "" {
		log.Panicf("Expression is invalid: %s", expr)
	}
	es := strings.Split(expr, "&&")
	expressions := make([]expression, len(es))
	for i, e := range es {
		if strings.Contains(e, ">") {
			expressions[i] = newGreaterFromStrExpression(e)
		} else if strings.Contains(e, "<") {
			expressions[i] = newLessFromStrExpression(e)
		} else if strings.Contains(e, "==") {
			expressions[i] = newEqualFromStrExpression(e)
		} else {
			log.Panicf("Expression is invalid: %s", expr)
		}
	}
	return &and{expressions}
}

func newAnd(expressions []expression) *and {
	return &and{expressions}
}

func (a *and) interpret(stats map[string]int64) bool {
	for _, expr := range a.expressions {
		if !expr.interpret(stats) {
			return false
		}
	}
	return true
}

type or compoundExpr

func newOrFromStrExpression(expr string) *or {
	if strings.TrimSpace(expr) == "" {
		log.Panicf("Expression is invalid: %s", expr)
	}
	es := strings.Split(expr, "||")
	expressions := make([]expression, len(es))
	for i, e := range es {
		expressions[i] = newAndFromStrExpression(e)
	}
	return &or{expressions}
}

func newOr(expressions []expression) *or {
	return &or{expressions}
}

func (o *or) interpret(stats map[string]int64) bool {
	for _, expr := range o.expressions {
		if expr.interpret(stats) {
			return true
		}
	}
	return false
}

type AlertRuleInterpreter struct {
	expression expression
}

func NewAlertRuleInterpreter(ruleExpression string) *AlertRuleInterpreter {
	if strings.TrimSpace(ruleExpression) == "" {
		log.Panicf("Expression is invalid: %s", ruleExpression)
	}
	return &AlertRuleInterpreter{newOrFromStrExpression(ruleExpression)}
}

func (i *AlertRuleInterpreter) interpret(stats map[string]int64) bool {
	return i.expression.interpret(stats)
}
