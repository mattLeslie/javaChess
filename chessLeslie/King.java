package chessLeslie;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class King extends GamePiece{
	
	public King(Color color, String l, BoardGrid bg) {
		super(color, l, bg);
		setSprite(color);
	}

	//gets all possible moves within 1 square of king
	//will also attempt to castle the king if the king and the appropiate
	//rook haven't moved
	@Override
	public ArrayList<Move> getMoveOptions() {
		
		int lowerRow = this.row + 1;
		int upperRow = this.row - 1;
		
		for(int i = this.col - 1; i< this.col + 2; i++) {
			if(i >=0 && i <8) {
				if(lowerRow < 8) {
					potentialMoves.add(new Move(grid.getSquare(lowerRow, i), "king"));
				}
				if(upperRow >= 0) {
					potentialMoves.add(new Move(grid.getSquare(upperRow, i), "king"));
				}
				if(i != this.col) {
					potentialMoves.add(new Move(grid.getSquare(this.row, i), "king"));
				}
			}
		}		
		//try to castle in both directions
		if(firstMove) {
			attemptCastle();
			attemptQSideCastle();
		}
				
		return this.potentialMoves;
		
	}
	
	private void attemptQSideCastle() {
		//get rook
		GamePiece p = grid.getSquare(row, col - 4).getCurrentPiece();
		if(p != null && p.getType().equals("rook") && p.firstMove) {
			//make sure pieces in between are gone
			boolean clear = true;
			for(int i = 1; i < 4; i++) {
				if(grid.getSquare(row, col - i).getCurrentPiece() != null) {
					clear= false;
				}
			}
			if(clear) {
				potentialMoves.add(new Move(grid.getSquare(row, col - 2), "qcastle"));
			}
		}
		
	}
	
	private void attemptCastle() {
		//get rook
		GamePiece p = grid.getSquare(row, col + 3).getCurrentPiece();
		if(p != null && p.getType().equals("rook") && p.firstMove) {
			//make sure pieces in between are gone
			boolean clear = true;
			for(int i = 1; i < 3; i++) {
				if(grid.getSquare(row, col + i).getCurrentPiece() != null) {
					clear= false;
				}
			}
			if(clear) {
				potentialMoves.add(new Move(grid.getSquare(row, col + 2), "castle"));
			}
		}
	}
	
	
	@Override
	protected void setSprite(Color c) {
		Image img;
		if(c == Color.WHITE) {
			img = new Image(getClass().getResourceAsStream("/resources/white_king.png"));
		}
		else {
			img = new Image(getClass().getResourceAsStream("/resources/black_king.png"));
		}
		ImageView iv = new ImageView(img);
		this.getChildren().add(iv);
	}

	@Override
	//get the 8 squares surrounding the king that the king can attack
	public ArrayList<BoardSquare> getAttackSquares() {
		// TODO Auto-generated method stub
		
		int lowerRow = this.row + 1;
		int upperRow = this.row - 1;
		
		for(int i = this.col - 1; i< this.col + 2; i++) {
			if(i >=0 && i <8) {
				if(lowerRow < 8) {
					potentialMoves.add(new Move(grid.getSquare(lowerRow, i), "king"));
				}
				if(upperRow >= 0) {
					potentialMoves.add(new Move(grid.getSquare(upperRow, i), "king"));
				}
				if(i != this.col) {
					potentialMoves.add(new Move(grid.getSquare(this.row, i), "king"));
				}
			}
		}		
		ArrayList<BoardSquare> squares = new ArrayList<BoardSquare>();
		for(Move m : potentialMoves) {
			squares.add(m.getSquare());
		}
		this.potentialMoves.clear();
		return squares;
	}

}
