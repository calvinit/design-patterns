package decorator

import "testing"

func TestDecorator(t *testing.T) {
	writtenNeed := "这是需要写入的内容数据。！@#123456ABCabcQWErty"
	decorator := NewCompressionDecorator(NewEncryptionDecorator(NewFileDataSource("path/to/read-and-write.txt")))
	// 写入内容
	decorator.Write(writtenNeed)
	// 读取内容
	fileContent := decorator.Read()
	t.Logf("读取到的文件内容为：%s\n", fileContent)
}
