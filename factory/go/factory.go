package factory

import "fmt"

type IReader interface {
	Read(path string) string
}

type XMLReader struct{}
type JSONReader struct{}
type YAMLReader struct{}

func (r *XMLReader) Read(path string) string {
	return "xml:" + path
}

func (r *JSONReader) Read(path string) string {
	return "json:" + path
}

func (r *YAMLReader) Read(path string) string {
	return "yaml:" + path
}

// ============================================================

type IFactory interface {
	CreateReader() IReader
}

type XMLReaderFactory struct{}
type JSONReaderFactory struct{}
type YAMLReaderFactory struct{}

func (f *XMLReaderFactory) CreateReader() IReader {
	return &XMLReader{}
}

func (f *JSONReaderFactory) CreateReader() IReader {
	return &JSONReader{}
}

func (f *YAMLReaderFactory) CreateReader() IReader {
	return &YAMLReader{}
}

// ============================================================

var cachedReaderFactories = map[int]IFactory{
	1: &XMLReaderFactory{},
	2: &JSONReaderFactory{},
	3: &YAMLReaderFactory{},
}

func GetReaderFactory(typ int) (IFactory, error) {
	if readerFactory, ok := cachedReaderFactories[typ]; ok {
		return readerFactory, nil
	}
	return nil, fmt.Errorf("unknown reader type: %d", typ)
}
