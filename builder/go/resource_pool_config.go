package builder

import (
	"errors"
	"fmt"
	"strings"
)

type ResourcePoolConfig struct {
	name     string
	maxTotal int
	maxIdle  int
	minIdle  int
}

func (c *ResourcePoolConfig) String() string {
	return fmt.Sprintf("ResourcePoolConfig{name='%s', maxTotal=%d, maxIdle=%d, minIdle=%d}",
		c.name, c.maxTotal, c.maxIdle, c.minIdle)
}

func (c *ResourcePoolConfig) Name() string {
	return c.name
}

func (c *ResourcePoolConfig) MaxTotal() int {
	return c.maxTotal
}

func (c *ResourcePoolConfig) MaxIdle() int {
	return c.maxIdle
}

func (c *ResourcePoolConfig) MinIdle() int {
	return c.minIdle
}

// ============================================================
// 参考了《Uber Go 语言编码规范》实现：https://github.com/uber-go/guide/blob/master/style.md#functional-options

type ResourcePoolConfigOption interface {
	apply(*ResourcePoolConfig) error
}

type maxTotalOption int

func (o maxTotalOption) apply(c *ResourcePoolConfig) error {
	maxTotal := int(o)
	if maxTotal < 0 {
		return errors.New("maxTotal cannot be less than zero")
	}
	c.maxTotal = maxTotal
	return nil
}

func WithMaxTotal(maxTotal int) ResourcePoolConfigOption {
	return maxTotalOption(maxTotal)
}

type maxIdleOption struct {
	maxIdle int
}

func (o maxIdleOption) apply(c *ResourcePoolConfig) error {
	maxIdle := o.maxIdle
	if maxIdle < 0 {
		return errors.New("maxIdle cannot be less than zero")
	}
	c.maxIdle = maxIdle
	return nil
}

func WithMaxIdle(maxIdle int) ResourcePoolConfigOption {
	return maxIdleOption{maxIdle}
}

type minIdleOption int

func (o minIdleOption) apply(c *ResourcePoolConfig) error {
	minIdle := int(o)
	if minIdle < 0 {
		return errors.New("minIdle cannot be less than zero")
	}
	c.minIdle = minIdle
	return nil
}

func WithMinIdle(minIdle int) ResourcePoolConfigOption {
	return minIdleOption(minIdle)
}

const (
	defaultMaxTotal = 8
	defaultMaxIdle  = 8
	defaultMinIdle  = 0
)

func NewResourcePoolConfig(name string, opts ...ResourcePoolConfigOption) (*ResourcePoolConfig, error) {
	// name 为必填属性，所以将其作为单独参数传入并且进行校验
	if strings.TrimSpace(name) == "" {
		return nil, errors.New("name cannot be blank")
	}

	// 先设置默认值
	conf := ResourcePoolConfig{
		name:     name,
		maxTotal: defaultMaxTotal,
		maxIdle:  defaultMaxIdle,
		minIdle:  defaultMinIdle,
	}

	// 应用传入的所有配置选项
	for _, o := range opts {
		if err := o.apply(&conf); err != nil {
			return nil, err
		}
	}

	// 依赖关系校验、约束条件校验等
	if conf.maxIdle > conf.maxTotal {
		return nil, errors.New("maxIdle cannot be greater than maxTotal")
	}
	if conf.minIdle > conf.maxTotal || conf.minIdle > conf.maxIdle {
		return nil, errors.New("minIdle cannot be greater than maxTotal or maxIdle")
	}
	return &conf, nil
}
