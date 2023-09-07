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

func TestAbstractFactory(t *testing.T) {
	f1, _ := GetReaderAbstractFactory(1)
	f2, _ := GetReaderAbstractFactory(2)
	f3, _ := GetReaderAbstractFactory(3)
	gr1 := f1.CreateGoReader()
	gr2 := f2.CreateGoReader()
	gr3 := f3.CreateGoReader()
	rr1 := f1.CreateRustReader()
	rr2 := f2.CreateRustReader()
	rr3 := f3.CreateRustReader()
	t.Logf("3.1 抽象工厂: go_reader(1)=>%s, go_reader(2)=>%s, go_reader(3)=>%s\n",
		gr1.Read("/path/to/xml-file"), gr2.Read("/path/to/json-file"), gr3.Read("/path/to/yaml-file"))
	t.Logf("3.2 抽象工厂: rust_reader(1)=>%s, rust_reader(2)=>%s, rust_reader(3)=>%s\n",
		rr1.Read("/path/to/xml-file"), rr2.Read("/path/to/json-file"), rr3.Read("/path/to/yaml-file"))
}
