package visitor

import "fmt"

type Visitor interface {
	VistPPTFile(f *PPTFile)
	VistPdfFile(f *PdfFile)
	VistWordFile(f *WordFile)
}

type ResourceFile interface {
	Accept(visitor Visitor)
}

type resourceFile struct {
	filePath string
}

type PPTFile resourceFile

func NewPPTFile(filePath string) *PPTFile {
	return &PPTFile{filePath}
}

func (f *PPTFile) Accept(visitor Visitor) {
	visitor.VistPPTFile(f)
}

type PdfFile resourceFile

func NewPdfFile(filePath string) *PdfFile {
	return &PdfFile{filePath}
}

func (f *PdfFile) Accept(visitor Visitor) {
	visitor.VistPdfFile(f)
}

type WordFile resourceFile

func NewWordFile(filePath string) *WordFile {
	return &WordFile{filePath}
}

func (f *WordFile) Accept(visitor Visitor) {
	visitor.VistWordFile(f)
}

type Extractor struct{}

func (_ Extractor) VistPPTFile(_ *PPTFile) {
	fmt.Println("Extract PPT.")
}

func (_ Extractor) VistPdfFile(_ *PdfFile) {
	fmt.Println("Extract PDF.")
}

func (_ Extractor) VistWordFile(_ *WordFile) {
	fmt.Println("Extract WORD.")
}

type Compressor struct{}

func (_ Compressor) VistPPTFile(f *PPTFile) {
	fmt.Println("Compress PPT.")
}

func (_ Compressor) VistPdfFile(_ *PdfFile) {
	fmt.Println("Compress PDF.")
}

func (_ Compressor) VistWordFile(_ *WordFile) {
	fmt.Println("Compress WORD.")
}
