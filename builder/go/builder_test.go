package builder

import "testing"

func TestBuilder(t *testing.T) {
	opts := []ResourcePoolConfigOption{
		WithMaxTotal(30),
		WithMaxIdle(30),
		WithMinIdle(15),
	}
	if conf, err := NewResourcePoolConfig("jdbc-mysql.properties", opts...); err != nil {
		t.Errorf("建造者模式：%s", err)
	} else {
		t.Logf("建造者模式: %s\n", conf)
	}
}
