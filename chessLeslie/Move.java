package chessLeslie;

//representation of a potential move for a piece
//keeps track of the square the piece could move to
//and the type of move (regular, castle, qcastle, defense, etc.)
public class Move {
	
	private BoardSquare square;
	private String type;
	
	public Move(BoardSquare square, String type) {
		this.square = square;
		this.type = type;
	}
	
	public String getMoveType() {
		return this.type;
		
	}
	public BoardSquare getSquare() {
		return square;
	}
}
