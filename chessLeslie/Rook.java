package chessLeslie;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Rook extends GamePiece{
	
	public Rook(Color color, String l, BoardGrid bg) {
		super(color, l, bg);
		// TODO Auto-generated constructor stub
		setSprite(color);
	}

	@Override
	//looks in each cardinal direction until it either reaches the end of the board,
	//runs into a friendly piece, or finds an enemy piece to attack
	public ArrayList<Move> getMoveOptions() {
		//get x moves
		int x = this.col;
		x = x + 1;
		while(x < 8) {
			if(grid.getSquare(this.row, x).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(this.row, x), "regular"));
				if(grid.getSquare(this.row, x).getCurrentPiece() != null) {
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
			if(grid.getSquare(this.row, x).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(this.row, x), "regular"));
				if(grid.getSquare(this.row, x).getCurrentPiece() != null) {
					break;
				}
			}
			else {
				break;
			}
			
			x--;
		}
		
		int y = this.row;
		y = y + 1;
		while(y < 8) {
			if(grid.getSquare(y, this.col).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(y, this.col), "regular"));
				if(grid.getSquare(y, this.col).getCurrentPiece() != null) {
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
			if(grid.getSquare(y, this.col).canMove(this.getColor())) {
				potentialMoves.add(new Move(grid.getSquare(y, this.col), "regular"));
				if(grid.getSquare(y, this.col).getCurrentPiece() != null) {
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
			img = new Image(getClass().getResourceAsStream("/resources/white_rook.png"));
		}
		else {
			img = new Image(getClass().getResourceAsStream("/resources/black_rook.png"));
		}
		ImageView iv = new ImageView(img);
		this.getChildren().add(iv);
	}

	
	@Override
	//slightly different method than getMoveOptions
	//needs to account for any squares behind a king piece (so that the king cannot back up into check),
	//and for any square containing a friendly piece that this rook is currently defending
	public ArrayList<BoardSquare> getAttackSquares() {
		//get x moves
		int x = this.col;
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
		
		int y = this.row;
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
