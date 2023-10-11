package bridge

import "testing"

func TestBridge(t *testing.T) {
	epson := &Epson{}
	hp := &Hp{}

	mac := &Mac{hp}
	mac.print()

	linux := &Linux{hp}
	linux.print()

	windows := &Windows{epson}
	windows.print()
}
