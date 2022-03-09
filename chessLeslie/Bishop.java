package chessLeslie;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Bishop extends GamePiece{

	public Bishop(Color color, String l, BoardGrid bg) {
		super(color, l, bg);
		setSprite(color);
	}
	
	@Override
	protected void setSprite(Color c) {
		Image img;
		if(c == Color.WHITE) {
			img = new Image(getClass().getResourceAsStream("/resources/white_bishop.png"));
		}
		else {
			img = new Image(getClass().getResourceAsStream("/resources/black_bishop.png"));
		}
		ImageView iv = new ImageView(img);
		this.getChildren().add(iv);
	}


	@Override
	//get all diagonal moves
	//each while loop analyzes the board squares in each diagonal direction
	//adding moves if the square is unoccupied or the square has a piece of the opposite color
	//if the square has a piece of the same color, no move is added and the 
	//program checks the other diagonals
	public ArrayList<Move> getMoveOptions() {
				
		int x = this.col - 1;
		int y = this.row - 1;
		
		while(x >= 0 && y >= 0) {
			if(grid.getSquare(y, x).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(y, x), "regular"));
				if(grid.getSquare(y, x).getCurrentPiece() != null) {
					break;
				}
			}
			else {
				break;
			}
			x--;
			y--;
		}
		
		x = this.col + 1;
		y = this.row + 1;
		while(x < 8 && y < 8) {
			if(grid.getSquare(y, x).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(y, x), "regular"));
				if(grid.getSquare(y, x).getCurrentPiece() != null) {
					break;
				}
			}
			else {
				break;
			}
			x++;
			y++;
		}
		
		//get right diag
		x = this.col + 1;
		y = this.row - 1;
		while(x < 8 && y >= 0) {
			if(grid.getSquare(y, x).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(y, x), "regular"));
				if(grid.getSquare(y, x).getCurrentPiece() != null) {
					break;
				}
			}
			else {
				break;
			}
			x++;
			y--;
		}
		x = this.col - 1;
		y = this.row + 1;
		while(x >= 0 && y < 8) {
			if(grid.getSquare(y, x).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(y, x), "regular"));
				if(grid.getSquare(y, x).getCurrentPiece() != null) {
					break;
				}
			}
			else {
				break;
			}
			x--;
			y++;
		}
		return potentialMoves;
	}

	//this method gets all of the squares that the bishop threatens
	//including any squares that the king could back up into 
	//used primarily in determining a valid move to get out of check
	@Override
	public ArrayList<BoardSquare> getAttackSquares() {
		
		//get left diag
		int x = this.col - 1;
		int y = this.row - 1;
		
		while(x >= 0 && y >= 0) {
			GamePiece p = grid.getSquare(y, x).getCurrentPiece();
			if(grid.getSquare(y, x).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(y, x), "regular"));
				if(p != null && !p.getType().equals("king")) {
					break;
				}
			}
			else if(p != null && p.getColor() == this.getColor()) {
				potentialMoves.add(new Move(grid.getSquare(y, x), "defense"));
				break;
			}
			else {
				break;
			}
			x--;
			y--;
		}
		
		x = this.col + 1;
		y = this.row + 1;
		while(x < 8 && y < 8) {
			GamePiece p = grid.getSquare(y, x).getCurrentPiece();
			if(grid.getSquare(y, x).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(y, x), "regular"));
				if(p != null && !p.getType().equals("king")) {
					break;
				}
			}
			else if(p != null && p.getColor() == this.getColor()) {
				potentialMoves.add(new Move(grid.getSquare(y, x), "defense"));
				break;
			}
			else {
				break;
			}
			x++;
			y++;
		}
		
		//get right diag
		x = this.col + 1;
		y = this.row - 1;
		while(x < 8 && y >= 0) {
			GamePiece p = grid.getSquare(y, x).getCurrentPiece();
			if(grid.getSquare(y, x).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(y, x), "regular"));
				if(p != null && !p.getType().equals("king")) {
					break;
				}
			}
			else if(p != null && p.getColor() == this.getColor()) {
				potentialMoves.add(new Move(grid.getSquare(y, x), "defense"));
				break;
			}
			else {
				break;
			}
			x++;
			y--;
		}
		x = this.col - 1;
		y = this.row + 1;
		while(x >= 0 && y < 8) {
			GamePiece p = grid.getSquare(y, x).getCurrentPiece();
			if(grid.getSquare(y, x).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(y, x), "regular"));
				if(p != null && !p.getType().equals("king")) {
					break;
				}
			}
			else if(p != null && p.getColor() == this.getColor()) {
				potentialMoves.add(new Move(grid.getSquare(y, x), "defense"));
				break;
			}
			else {
				break;
			}
			x--;
			y++;
		}
		
		ArrayList<BoardSquare> squares = new ArrayList<BoardSquare>();
		for(Move m : potentialMoves) {
			squares.add(m.getSquare());
		}
		this.potentialMoves.clear();
		return squares;
	}
}
