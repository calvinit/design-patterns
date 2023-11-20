public class FlyweightTest {
    public static void main(String[] args) {
        ChessBoard chessBoard1 = new ChessBoard();
        chessBoard1.move(1, 1, 2);
        ChessBoard chessBoard2 = new ChessBoard();
        chessBoard2.move(2, 2, 3);
        // true
        System.out.println(chessBoard1.getChessPieces().get(1).getChessPieceUnit() == chessBoard2.getChessPieces().get(1).getChessPieceUnit());
        // true
        System.out.println(chessBoard1.getChessPieces().get(2).getChessPieceUnit() == chessBoard2.getChessPieces().get(2).getChessPieceUnit());
    }
}
