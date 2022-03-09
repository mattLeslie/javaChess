package chessLeslie;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Pawn extends GamePiece{
	private int direction;

	public Pawn(Color color, String l, BoardGrid bg, int d) {
		super(color, l, bg);
		//pawns have a direction depending on which team they're on
		this.direction = d;
		setSprite(color);
	}	
	
	protected void setSprite(Color c) {
		Image img;
		if(c == Color.WHITE) {
			img = new Image(getClass().getResourceAsStream("/resources/white_pawn.png"));
		}
		else {
			img = new Image(getClass().getResourceAsStream("/resources/black_pawn.png"));
		}
		ImageView iv = new ImageView(img);
		this.getChildren().add(iv);
	}
	
	@Override
	//let each pawn move forward one square
	//if the pawn hasn't moved yet and the space is clear
	//let the pawn move 2 squares
	public ArrayList<Move> getMoveOptions() {
		//make sure that the pawn doesn't go out of bounds
		if(this.row + direction >= 8 || this.row + direction < 0) {
			return potentialMoves;
		}
		
		//see if the pawn can make a 1 square move
		boolean firstSquareBlocked = true;
		BoardSquare bs = grid.getSquare(this.row + direction,  this.col);
		if(bs.getCurrentPiece() == null) {
			firstSquareBlocked = false;
			if(this.row + direction == 7 || this.row + direction == 0){
				//if the pawn reaches the other end of the board, promote the pawn to a queen
				potentialMoves.add(new Move(bs, "promotion"));
			}
			else {
				potentialMoves.add(new Move(bs, "regular"));
			}
		}
				
		//if the first square isn't blocked and the second square is free, let the pawn jump
		if(!firstSquareBlocked && firstMove) {
			BoardSquare bs2 = grid.getSquare(this.row + 2*direction, this.col);
			if(bs2.getCurrentPiece() == null) {
				potentialMoves.add(new Move(bs2, "regular"));
			}
		}

		//see if the pawn can move diagonally with an attack on another piece
		checkAttackAngles();
		return potentialMoves;
	}
	
	//check both forward diagonals
	private void checkAttackAngles() {
		int port = this.col - 1;
		int starboard = this.col + 1;
		//need a target to make diagonal move
		GamePiece target = null;
		
		//make sure that diagonals are within bounds
		if(port >= 0 && port < 8) {
			target = grid.getSquare(this.row + direction, port).getCurrentPiece();
			//see if valid target exists and add attack move if so
			if(target != null && target.getColor() != this.getColor()) {
				if(this.row + direction == 7 || this.row + direction == 0) {
					potentialMoves.add(new Move(grid.getSquare(this.row + direction, port), "promotion"));
				}
				else {
					potentialMoves.add(new Move(grid.getSquare(this.row + direction, port), "regular"));
				}
			}
			
		}
		
		//again, make sure diagonals are in bounds
		if(starboard >= 0 && starboard < 8) {
			target = grid.getSquare(this.row + direction,  starboard).getCurrentPiece();
			if(target != null && target.getColor() != this.getColor()) {
				if(this.row + direction == 7 || this.row + direction == 0) {
					potentialMoves.add(new Move(grid.getSquare(this.row + direction, starboard), "promotion"));
				}
				else {
					potentialMoves.add(new Move(grid.getSquare(this.row + direction, starboard), "regular"));
				}			
			}
		}
	}

	//get diagonal attacks for pawn
	//slightly different method here to prevent out of bounds access when simulating moves
	@Override
	public ArrayList<BoardSquare> getAttackSquares() {
		//
		int port = this.col - 1;
		int starboard = this.col + 1;
		int advanceRow = this.row + direction;
		
		if(port >= 0 && port < 8 && (advanceRow < 8 && advanceRow >= 0)) {
			if(advanceRow == 7 || advanceRow == 0) {
				potentialMoves.add(new Move(grid.getSquare(advanceRow, port), "promotion"));
			}
			else {
				potentialMoves.add(new Move(grid.getSquare(advanceRow, port), "regular"));
			}
		}
		if(starboard >= 0 && starboard <= 7 && (advanceRow < 8 && advanceRow >= 0)) {
			if(advanceRow == 7 || advanceRow == 0) {
				potentialMoves.add(new Move(grid.getSquare(advanceRow, starboard), "promotion"));
			}
			else {
				potentialMoves.add(new Move(grid.getSquare(advanceRow, starboard), "regular"));
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
