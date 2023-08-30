package factory

import "fmt"

type ISimpleReader interface {
	read(path string) string
}

type XMLReader struct{}
type JSONReader struct{}
type YAMLReader struct{}

func (r *XMLReader) read(path string) string {
	return "simple-xml:" + path
}

func (r *JSONReader) read(path string) string {
	return "simple-json:" + path
}

func (r *YAMLReader) read(path string) string {
	return "simple-yaml:" + path
}

type SimpleFactory struct{}

func (f SimpleFactory) createReader(typ int) (ISimpleReader, error) {
	var reader ISimpleReader
	switch typ {
	case 1:
		reader = &XMLReader{}
	case 2:
		reader = &JSONReader{}
	case 3:
		reader = &YAMLReader{}
	default:
		return nil, fmt.Errorf("unknown simple reader type: %d", typ)
	}
	return reader, nil
}
