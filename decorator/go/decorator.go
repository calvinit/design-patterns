package decorator

import (
	"bytes"
	"compress/zlib"
	"encoding/base64"
	"fmt"
	"io"
)

type DataSource interface {
	Read() string
	Write(data string)
}

type FileDataSource struct {
	Name        string
	fileContent string
}

func NewFileDataSource(name string) *FileDataSource {
	return &FileDataSource{Name: name}
}

func (f *FileDataSource) Read() string {
	fmt.Printf("FileDataSource 读取文件 %s\n", f.Name)
	return f.fileContent
}

func (f *FileDataSource) Write(data string) {
	f.fileContent = data
	fmt.Printf("FileDataSource 写入到文件 %s 中，数据为：%s\n", f.Name, data)
}

// ============================================================

type DataSourceDecorator struct {
	wrapper DataSource
}

func (d *DataSourceDecorator) Read() string {
	return d.wrapper.Read()
}

func (d *DataSourceDecorator) Write(data string) {
	d.wrapper.Write(data)
}

type EncryptionDecorator struct {
	*DataSourceDecorator
}

func NewEncryptionDecorator(source DataSource) *EncryptionDecorator {
	return &EncryptionDecorator{&DataSourceDecorator{source}}
}

func (e *EncryptionDecorator) Read() string {
	decoded, _ := base64.StdEncoding.DecodeString(e.wrapper.Read())
	for i := 0; i < len(decoded); i++ {
		decoded[i] -= 1
	}
	return string(decoded)
}

func (e *EncryptionDecorator) Write(data string) {
	result := []byte(data)
	for i := 0; i < len(result); i++ {
		result[i] += 1
	}
	e.wrapper.Write(base64.StdEncoding.EncodeToString(result))
}

type CompressionDecorator struct {
	CompLevel int
	*DataSourceDecorator
}

func NewCompressionDecorator(source DataSource) *CompressionDecorator {
	return &CompressionDecorator{
		CompLevel:           zlib.DefaultCompression,
		DataSourceDecorator: &DataSourceDecorator{source},
	}
}

func (c *CompressionDecorator) Read() string {
	decompressed, _ := c.decompress(c.wrapper.Read())
	return decompressed
}

func (c *CompressionDecorator) Write(data string) {
	compressed, _ := c.compress(data)
	c.wrapper.Write(compressed)
}

func (c *CompressionDecorator) compress(src string) (target string, err error) {
	input := []byte(src)

	var compressed bytes.Buffer
	writer, err := zlib.NewWriterLevel(&compressed, c.CompLevel)
	if err != nil {
		return
	}

	_, err = writer.Write(input)
	if err != nil {
		_ = writer.Close()
		return
	}

	// 注意这里不能用 defer，因为要保证 base64.StdEncoding.EncodeToString(...) 在 writer.Close() 之后执行
	err = writer.Close()
	target = base64.StdEncoding.EncodeToString(compressed.Bytes())
	return
}

func (c *CompressionDecorator) decompress(src string) (target string, err error) {
	decoded, err := base64.StdEncoding.DecodeString(src)
	if err != nil {
		return
	}

	reader, err := zlib.NewReader(bytes.NewReader(decoded))
	if err != nil {
		return
	}

	defer func() {
		e := reader.Close()
		if err == nil {
			err = e
		}
	}()

	var decompressed bytes.Buffer
	_, err = io.Copy(&decompressed, reader)
	if err != nil {
		return
	}
	target = decompressed.String()
	return
}
