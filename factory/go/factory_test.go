package factory

import "testing"

func TestSimpleFactory(t *testing.T) {
	f := &SimpleFactory{}
	r1, _ := f.NewSimpleReader(1)
	r2, _ := f.NewSimpleReader(2)
	r3, _ := f.NewSimpleReader(3)
	t.Logf("1. 简单工厂: reader(1)=>%s, reader(2)=>%s, reader(3)=>%s\n",
		r1.Read("/path/to/xml-file"), r2.Read("/path/to/json-file"), r3.Read("/path/to/yaml-file"))
}

func TestFactory(t *testing.T) {
	f1, _ := GetReaderFactory(1)
	f2, _ := GetReaderFactory(2)
	f3, _ := GetReaderFactory(3)
	r1 := f1.CreateReader()
	r2 := f2.CreateReader()
	r3 := f3.CreateReader()
	t.Logf("2. 工厂方法: reader(1)=>%s, reader(2)=>%s, reader(3)=>%s\n",
		r1.Read("/path/to/xml-file"), r2.Read("/path/to/json-file"), r3.Read("/path/to/yaml-file"))
}
