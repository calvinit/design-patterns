package flyweight

import "testing"

func TestFlyweight(t *testing.T) {
	cb1 := NewChessBoard()
	cb1.Move(1, 1, 2)
	cb2 := NewChessBoard()
	cb2.Move(2, 2, 3)
	// cb1.ChessPieceUnit==cb2.ChessPieceUnit? true
	t.Logf("cb1.ChessPieceUnit==cb2.ChessPieceUnit? %t", cb1.GetChessPieces()[1].ChessPieceUnit == cb2.GetChessPieces()[1].ChessPieceUnit)
	// cb1.ChessPieceUnit==cb2.ChessPieceUnit? true
	t.Logf("cb1.ChessPieceUnit==cb2.ChessPieceUnit? %t", cb1.GetChessPieces()[2].ChessPieceUnit == cb2.GetChessPieces()[2].ChessPieceUnit)
}
