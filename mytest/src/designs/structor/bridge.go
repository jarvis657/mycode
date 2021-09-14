package structor

import "fmt"

//https://refactoringguru.cn/design-patterns/bridge

type computer2 interface {
	print()
	setPrinter(printer)
}

type mac2 struct {
	printer printer
}

func (m *mac2) print() {
	fmt.Println("Print request for mac")
	m.printer.printFile()
}

func (m *mac2) setPrinter(p printer) {
	m.printer = p
}

type windows2 struct {
	printer printer
}

func (w *windows2) print() {
	fmt.Println("Print request for windows")
	w.printer.printFile()
}

func (w *windows2) setPrinter(p printer) {
	w.printer = p
}

type printer interface {
	printFile()
}

type epson struct {
}

func (p *epson) printFile() {
	fmt.Println("Printing by a EPSON Printer")
}

type hp struct {
}

func (p *hp) printFile() {
	fmt.Println("Printing by a HP Printer")
}

func main() {

	hpPrinter := &hp{}
	epsonPrinter := &epson{}

	macComputer := &mac2{}

	macComputer.setPrinter(hpPrinter)
	macComputer.print()
	fmt.Println()

	macComputer.setPrinter(epsonPrinter)
	macComputer.print()
	fmt.Println()

	winComputer := &windows2{}

	winComputer.setPrinter(hpPrinter)
	winComputer.print()
	fmt.Println()

	winComputer.setPrinter(epsonPrinter)
	winComputer.print()
	fmt.Println()
}
