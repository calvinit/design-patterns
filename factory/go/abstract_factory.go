package factory

import "fmt"

type IGoReader interface {
	Read(path string) string
}

type IRustReader interface {
	Read(path string) string
}

type XMLGoReader struct{}
type XMLRustReader struct{}
type JSONGoReader struct{}
type JSONRustReader struct{}
type YAMLGoReader struct{}
type YAMLRustReader struct{}

func (r *XMLGoReader) Read(path string) string {
	return "(go) xml:" + path
}

func (r *XMLRustReader) Read(path string) string {
	return "(rust) xml:" + path
}

func (r *JSONGoReader) Read(path string) string {
	return "(go) json:" + path
}

func (r *JSONRustReader) Read(path string) string {
	return "(rust) json:" + path
}

func (r *YAMLGoReader) Read(path string) string {
	return "(go) yaml:" + path
}

func (r *YAMLRustReader) Read(path string) string {
	return "(rust) yaml:" + path
}

// ============================================================

type IAbstractFactory interface {
	CreateGoReader() IGoReader
	CreateRustReader() IRustReader
}

type XMLReaderAbstractFactory struct{}
type JSONReaderAbstractFactory struct{}
type YAMLReaderAbstractFactory struct{}

func (f *XMLReaderAbstractFactory) CreateGoReader() IGoReader {
	return &XMLGoReader{}
}

func (f *XMLReaderAbstractFactory) CreateRustReader() IRustReader {
	return &XMLRustReader{}
}

func (f *JSONReaderAbstractFactory) CreateGoReader() IGoReader {
	return &JSONGoReader{}
}

func (f *JSONReaderAbstractFactory) CreateRustReader() IRustReader {
	return &JSONRustReader{}
}

func (f *YAMLReaderAbstractFactory) CreateGoReader() IGoReader {
	return &YAMLGoReader{}
}

func (f *YAMLReaderAbstractFactory) CreateRustReader() IRustReader {
	return &YAMLRustReader{}
}

// ============================================================

var cachedReaderAbstractFactories = map[int]IAbstractFactory{
	1: &XMLReaderAbstractFactory{},
	2: &JSONReaderAbstractFactory{},
	3: &YAMLReaderAbstractFactory{},
}

func GetReaderAbstractFactory(typ int) (IAbstractFactory, error) {
	if readerFactory, ok := cachedReaderAbstractFactories[typ]; ok {
		return readerFactory, nil
	}
	return nil, fmt.Errorf("unknown reader type: %d", typ)
}
