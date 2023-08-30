package factory

import "testing"

func TestSimpleFactory(t *testing.T) {
	f := SimpleFactory{}
	r1, _ := f.createReader(1)
	r2, _ := f.createReader(2)
	r3, _ := f.createReader(3)
	t.Logf("1. 简单工厂: reader(1)=>%s, reader(2)=>%s, reader(3)=>%s\n",
		r1.read("/path/to/xml-file"), r2.read("/path/to/json-file"), r3.read("/path/to/yaml-file"))
}
