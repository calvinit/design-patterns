package factory

import "fmt"

type ISimpleReader interface {
	Read(path string) string
}

type XMLSimpleReader struct{}
type JSONSimpleReader struct{}
type YAMLSimpleReader struct{}

func (r *XMLSimpleReader) Read(path string) string {
	return "simple-xml:" + path
}

func (r *JSONSimpleReader) Read(path string) string {
	return "simple-json:" + path
}

func (r *YAMLSimpleReader) Read(path string) string {
	return "simple-yaml:" + path
}

// ============================================================

type SimpleFactory struct{}

func (f *SimpleFactory) NewSimpleReader(typ int) (ISimpleReader, error) {
	var reader ISimpleReader
	switch typ {
	case 1:
		reader = &XMLSimpleReader{}
	case 2:
		reader = &JSONSimpleReader{}
	case 3:
		reader = &YAMLSimpleReader{}
	default:
		return nil, fmt.Errorf("unknown simple reader type: %d", typ)
	}
	return reader, nil
}
