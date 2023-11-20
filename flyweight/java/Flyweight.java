import java.util.HashMap;
import java.util.Map;

// 享元类
record ChessPieceUnit(int id, String text, Color color) {
    enum Color {
        RED, BLACK
    }
}

class ChessPieceUnitFactory {
    private static final Map<Integer, ChessPieceUnit> pieces = new HashMap<>();

    static {
        pieces.put(1, new ChessPieceUnit(1, "車", ChessPieceUnit.Color.BLACK));
        pieces.put(2, new ChessPieceUnit(2, "馬", ChessPieceUnit.Color.BLACK));
        pieces.put(3, new ChessPieceUnit(3, "象", ChessPieceUnit.Color.BLACK));
        pieces.put(4, new ChessPieceUnit(4, "士", ChessPieceUnit.Color.BLACK));
        pieces.put(5, new ChessPieceUnit(5, "將", ChessPieceUnit.Color.BLACK));
        pieces.put(6, new ChessPieceUnit(6, "士", ChessPieceUnit.Color.BLACK));
        pieces.put(7, new ChessPieceUnit(7, "象", ChessPieceUnit.Color.BLACK));
        pieces.put(8, new ChessPieceUnit(8, "馬", ChessPieceUnit.Color.BLACK));
        pieces.put(9, new ChessPieceUnit(9, "車", ChessPieceUnit.Color.BLACK));
        pieces.put(10, new ChessPieceUnit(10, "砲", ChessPieceUnit.Color.BLACK));
        pieces.put(11, new ChessPieceUnit(11, "砲", ChessPieceUnit.Color.BLACK));
        pieces.put(12, new ChessPieceUnit(12, "卒", ChessPieceUnit.Color.BLACK));
        pieces.put(13, new ChessPieceUnit(13, "卒", ChessPieceUnit.Color.BLACK));
        pieces.put(14, new ChessPieceUnit(14, "卒", ChessPieceUnit.Color.BLACK));
        pieces.put(15, new ChessPieceUnit(15, "卒", ChessPieceUnit.Color.BLACK));
        pieces.put(16, new ChessPieceUnit(16, "卒", ChessPieceUnit.Color.BLACK));
        pieces.put(17, new ChessPieceUnit(17, "俥", ChessPieceUnit.Color.RED));
        pieces.put(18, new ChessPieceUnit(18, "傌", ChessPieceUnit.Color.RED));
        pieces.put(19, new ChessPieceUnit(19, "相", ChessPieceUnit.Color.RED));
        pieces.put(20, new ChessPieceUnit(20, "仕", ChessPieceUnit.Color.RED));
        pieces.put(21, new ChessPieceUnit(21, "帥", ChessPieceUnit.Color.RED));
        pieces.put(22, new ChessPieceUnit(22, "仕", ChessPieceUnit.Color.RED));
        pieces.put(23, new ChessPieceUnit(23, "相", ChessPieceUnit.Color.RED));
        pieces.put(24, new ChessPieceUnit(24, "傌", ChessPieceUnit.Color.RED));
        pieces.put(25, new ChessPieceUnit(25, "俥", ChessPieceUnit.Color.RED));
        pieces.put(26, new ChessPieceUnit(26, "炮", ChessPieceUnit.Color.RED));
        pieces.put(27, new ChessPieceUnit(27, "炮", ChessPieceUnit.Color.RED));
        pieces.put(28, new ChessPieceUnit(28, "兵", ChessPieceUnit.Color.RED));
        pieces.put(29, new ChessPieceUnit(29, "兵", ChessPieceUnit.Color.RED));
        pieces.put(30, new ChessPieceUnit(30, "兵", ChessPieceUnit.Color.RED));
        pieces.put(31, new ChessPieceUnit(31, "兵", ChessPieceUnit.Color.RED));
        pieces.put(32, new ChessPieceUnit(32, "兵", ChessPieceUnit.Color.RED));
    }

    public static ChessPieceUnit getChessPiece(int chessPieceId) {
        return pieces.get(chessPieceId);
    }
}

// ============================================================

class ChessPiece {
    private ChessPieceUnit chessPieceUnit;
    private int positionX;
    private int positionY;

    public ChessPiece(ChessPieceUnit chessPieceUnit, int positionX, int positionY) {
        this.chessPieceUnit = chessPieceUnit;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public ChessPieceUnit getChessPieceUnit() {
        return chessPieceUnit;
    }

    public void setChessPieceUnit(ChessPieceUnit chessPieceUnit) {
        this.chessPieceUnit = chessPieceUnit;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}

class ChessBoard {
    private final Map<Integer, ChessPiece> chessPieces = new HashMap<>();

    public ChessBoard() {
        init();
    }

    private void init() {
        chessPieces.put(1, new ChessPiece(ChessPieceUnitFactory.getChessPiece(1), 0, 0));
        chessPieces.put(2, new ChessPiece(ChessPieceUnitFactory.getChessPiece(2), 1, 0));
        // ...省略摆放其他棋子的代码...
    }

    // Only for tests!
    public Map<Integer, ChessPiece> getChessPieces() {
        return chessPieces;
    }

    public void move(int chessPieceId, int toPositionX, int toPositionY) {
        ChessPiece chessPiece = chessPieces.get(chessPieceId);
        if (chessPiece != null) {
            chessPiece.setPositionX(toPositionX);
            chessPiece.setPositionY(toPositionY);
        }
    }
}
