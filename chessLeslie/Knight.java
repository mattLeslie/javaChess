package chessLeslie;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Knight extends GamePiece{

	public Knight(Color color, String l, BoardGrid bg) {
		super(color, l, bg);
		setSprite(color);
	}

	@Override
	//go over each of the 8 possible knight jumps
	//a little arduous but it's explicit and works
	public ArrayList<Move> getMoveOptions() {
		
		int farLeftCol = this.col - 2;
		int farRightCol = this.col + 2;
		int leftCol = this.col - 1;
		int rightCol = this.col + 1;

		int farUpperRow = this.row - 2;
		int farLowerRow = this.row + 2;
		int lowerRow = this.row + 1;
		int upperRow = this.row - 1;
		
		if(farLeftCol >= 0) {
			if(upperRow >= 0) {
				BoardSquare b = grid.getSquare(upperRow, farLeftCol);
				if(b.canMove(this.getColor())) {
					potentialMoves.add(new Move(b, "regular"));
				}
			}		
			if(lowerRow < 8) {
				BoardSquare b = grid.getSquare(lowerRow, farLeftCol);
				if(b.canMove(this.getColor())) {
					potentialMoves.add(new Move(b, "regular"));
				}
			}
		}
		if(farRightCol < 8) {
			if(upperRow >= 0) {
				BoardSquare b = grid.getSquare(upperRow, farRightCol);
				if(b.canMove(this.getColor())) {
					potentialMoves.add(new Move(b, "regular"));
				}
			}		
			if(lowerRow < 8) {
				BoardSquare b = grid.getSquare(lowerRow, farRightCol);
				if(b.canMove(this.getColor())) {
					potentialMoves.add(new Move(b, "regular"));
				}
			}
		}
		
		if(leftCol >= 0) {
			if(farUpperRow >= 0) {
				BoardSquare b = grid.getSquare(farUpperRow, leftCol);
				if(b.canMove(this.getColor())) {
					potentialMoves.add(new Move(b, "regular"));
				}
			}		
			if(farLowerRow < 8) {
				BoardSquare b = grid.getSquare(farLowerRow, leftCol);
				if(b.canMove(this.getColor())) {
					potentialMoves.add(new Move(b, "regular"));
				}
			}
		}
		
		if(rightCol < 8) {
			if(farUpperRow >= 0) {
				BoardSquare b = grid.getSquare(farUpperRow, rightCol);
				if(b.canMove(this.getColor())) {
					potentialMoves.add(new Move(b, "regular"));
				}
			}		
			if(farLowerRow < 8) {
				BoardSquare b = grid.getSquare(farLowerRow, rightCol);
				if(b.canMove(this.getColor())) {
					potentialMoves.add(new Move(b, "regular"));
				}
			}
		}
		return potentialMoves;
	}

	@Override
	protected void setSprite(Color c) {
		Image img;
		if(c == Color.WHITE) {
			img = new Image(getClass().getResourceAsStream("/resources/white_knight.png"));
		}
		else {
			img = new Image(getClass().getResourceAsStream("/resources/black_knight.png"));
		}
		ImageView iv = new ImageView(img);
		this.getChildren().add(iv);		
	}

	@Override
	//luckily, attack squares for the knight are the same as the knight's move squares!
	public ArrayList<BoardSquare> getAttackSquares() {
		this.getMoveOptions();
		ArrayList<BoardSquare> squares = new ArrayList<BoardSquare>();
		for(Move m : potentialMoves) {
			squares.add(m.getSquare());
		}
		this.potentialMoves.clear();
		return squares;
	}


}
