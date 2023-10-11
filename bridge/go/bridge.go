package bridge

import "fmt"

type Computer interface {
	print()
}

type Mac struct {
	printer Printer
}

func (m *Mac) print() {
	fmt.Println("Print request for Mac")
	m.printer.printFile()
}

type Linux struct {
	printer Printer
}

func (l *Linux) print() {
	fmt.Println("Print request for Linux")
	l.printer.printFile()
}

type Windows struct {
	printer Printer
}

func (w *Windows) print() {
	fmt.Println("Print request for Windows")
	w.printer.printFile()
}

// ============================================================

type Printer interface {
	printFile()
}

type Epson struct{}

func (*Epson) printFile() {
	fmt.Println("Printing by a EPSON Printer")
}

type Hp struct{}

func (*Hp) printFile() {
	fmt.Println("Printing by a Hp Printer")
}
