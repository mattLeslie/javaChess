package chessLeslie;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Queen extends GamePiece{

	public Queen(Color color, String l, BoardGrid bg) {
		super(color, l, bg);
		setSprite(color);
	}

	@Override
	//really, the queen is just a piece that combines the powers of the rook and the bishop
	//the code here isn't super pretty, but it's just a copy and paste of the rook's move method
	//combined with the bishop's move method
	public ArrayList<Move> getMoveOptions() {
		//DIAGONAL
		
		//get left diag
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
		
		//LATERAL
		x = this.col;
		x = x + 1;
		y = this.row;
		while(x < 8) {
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
		}
		x = this.col - 1;
		while(x >= 0) {
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
		}
		
		x = this.col;
		y = this.row + 1;
		while(y < 8) {
			if(grid.getSquare(y, x).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(y, x), "regular"));
				if(grid.getSquare(y, x).getCurrentPiece() != null) {
					break;
				}
			}
			else {
				break;
			}
			y++;
		}
		y = this.row - 1;
		while(y >= 0) {
			if(grid.getSquare(y, x).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(y, x), "regular"));
				if(grid.getSquare(y, x).getCurrentPiece() != null) {
					break;
				}
			}
			else {
				break;
			}
			y--;
		}
		return potentialMoves;
	}

	@Override
	protected void setSprite(Color c) {
		Image img;
		if(c == Color.WHITE) {
			img = new Image(getClass().getResourceAsStream("/resources/white_queen.png"));
		}
		else {
			img = new Image(getClass().getResourceAsStream("/resources/black_queen.png"));
		}
		ImageView iv = new ImageView(img);
		this.getChildren().add(iv);
		
	}

	@Override
	//again, this is just a combination of the rook/bishop methods
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
		
		//get x moves
		x = this.col;
		x = x + 1;
		while(x < 8) {
			GamePiece p = grid.getSquare(this.row, x).getCurrentPiece();
			if(grid.getSquare(this.row, x).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(this.row, x), "regular"));
				if(p != null && !p.getType().equals("king")) {
					break;
				}
			}
			else if(p != null && p.getColor() == this.getColor()) {
				potentialMoves.add(new Move(grid.getSquare(this.row, x), "defense"));
				break;
			}
			else {
				break;
			}
			
			x++;
		}
		x = this.col - 1;
		while(x >= 0) {
			GamePiece p = grid.getSquare(this.row, x).getCurrentPiece();
			if(grid.getSquare(this.row, x).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(this.row, x), "regular"));
				if(p != null && !p.getType().equals("king")) {
					break;
				}
			}
			else if(p != null && p.getColor() == this.getColor()) {
				potentialMoves.add(new Move(grid.getSquare(this.row, x), "defense"));
				break;
			}
			else {
				break;
			}
			x--;
		}
		
		y = this.row;
		y = y + 1;
		while(y < 8) {
			GamePiece p = grid.getSquare(y, this.col).getCurrentPiece();
			if(grid.getSquare(y, this.col).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(y, this.col), "regular"));
				if(p != null && !p.getType().equals("king")) {
					break;
				}
			}
			else if(p != null && p.getColor() == this.getColor()) {
				potentialMoves.add(new Move(grid.getSquare(y, this.col), "defense"));
				break;
			}
			else {
				break;
			}

			y++;
		}
		y = this.row - 1;
		while(y >= 0) {
			GamePiece p = grid.getSquare(y, this.col).getCurrentPiece();
			if(grid.getSquare(y, this.col).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(y, this.col), "regular"));
				if(p != null && !p.getType().equals("king")) {
					break;
				}
			}
			else if(p != null && p.getColor() == this.getColor()) {
				potentialMoves.add(new Move(grid.getSquare(y, this.col), "defense"));
				break;
			}
			else {
				break;
			}			
			y--;
		}
		
		
		
		ArrayList<BoardSquare> squares = new ArrayList<BoardSquare>();
		for(Move m : potentialMoves) {
			squares.add(m.getSquare());
		}
		this.potentialMoves.clear();
		return squares;
	}

}
