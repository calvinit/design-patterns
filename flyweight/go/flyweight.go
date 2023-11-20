package flyweight

type Color int

const (
	RED Color = iota
	BLACK
)

type ChessPieceUnit struct {
	ID    int
	Text  string
	Color Color
}

var pieces = map[int]*ChessPieceUnit{
	1:  {1, "車", BLACK},
	2:  {2, "馬", BLACK},
	3:  {3, "象", BLACK},
	4:  {4, "士", BLACK},
	5:  {5, "將", BLACK},
	6:  {6, "士", BLACK},
	7:  {7, "象", BLACK},
	8:  {8, "馬", BLACK},
	9:  {9, "車", BLACK},
	10: {10, "砲", BLACK},
	11: {11, "砲", BLACK},
	12: {12, "卒", BLACK},
	13: {13, "卒", BLACK},
	14: {14, "卒", BLACK},
	15: {15, "卒", BLACK},
	16: {16, "卒", BLACK},
	17: {17, "俥", RED},
	18: {18, "傌", RED},
	19: {19, "相", RED},
	20: {20, "仕", RED},
	21: {21, "帥", RED},
	22: {22, "仕", RED},
	23: {23, "相", RED},
	24: {24, "傌", RED},
	25: {25, "俥", RED},
	26: {26, "炮", RED},
	27: {27, "炮", RED},
	28: {28, "兵", RED},
	29: {29, "兵", RED},
	30: {30, "兵", RED},
	31: {31, "兵", RED},
	32: {32, "兵", RED},
}

func getChessPiece(chessPieceID int) *ChessPieceUnit {
	return pieces[chessPieceID]
}

// ============================================================

type ChessPiece struct {
	*ChessPieceUnit
	PositionX int
	PositionY int
}

type ChessBoard struct {
	chessPieces map[int]*ChessPiece
}

func NewChessBoard() *ChessBoard {
	return &ChessBoard{
		chessPieces: map[int]*ChessPiece{
			1: {getChessPiece(1), 0, 0},
			2: {getChessPiece(2), 1, 0},
			// ...省略摆放其他棋子的代码...
		},
	}
}

// GetChessPieces Only for tests!
func (cb ChessBoard) GetChessPieces() map[int]*ChessPiece {
	return cb.chessPieces
}

func (cb ChessBoard) Move(chessPieceID, toPositionX, toPositionY int) {
	cb.chessPieces[chessPieceID].PositionX = toPositionX
	cb.chessPieces[chessPieceID].PositionY = toPositionY
}
