package visitor

import (
	"fmt"
	"testing"
)

func TestVisitor(t *testing.T) {
	resourceFiles := listAllResourceFiles("~/")

	extractor := &Extractor{}
	for _, f := range resourceFiles {
		// switch f.(type) ... ==> extractor.[VistPPTFile(f) | VistPdfFile(f) | VistWordFile(f)]
		f.Accept(extractor)
	}

	fmt.Println("==========================================")

	compressor := &Compressor{}
	for _, f := range resourceFiles {
		// switch f.(type) ... ==> compressor.[VistPPTFile(f) | VistPdfFile(f) | VistWordFile(f)]
		f.Accept(compressor)
	}
}

func listAllResourceFiles(resourceDirectory string) []ResourceFile {
	return []ResourceFile{
		NewPdfFile(resourceDirectory + "a.pdf"),
		NewWordFile(resourceDirectory + "b.docx"),
		NewPPTFile(resourceDirectory + "c.ppt"),
	}
}
