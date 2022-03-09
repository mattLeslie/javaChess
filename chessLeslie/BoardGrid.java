package chessLeslie;

import java.util.Vector;

//a 2D container keeping track of all board squares in the game
//also keeps track of where each king is
//pretty much all pieces have access to this grid and regularly operate on it,
//so I used vectors to keep everything synchronized

public class BoardGrid {
	private Vector<Vector<BoardSquare>> grid;
	
	private BoardSquare blackKingSquare;
	private BoardSquare whiteKingSquare;
	
	public BoardGrid() {
		grid = new Vector<Vector<BoardSquare>>();
		for(int i = 0; i< 8; i++) {
			grid.add(new Vector<BoardSquare>());
			for(int j = 0; j < 8; j++) {
				BoardSquare b = new BoardSquare(i, j);
				grid.get(i).add(b);
			}
		}
	}
	
	public BoardSquare getSquare(int i, int j) {return grid.get(i).get(j);}
	
	//keep track of where the black king and white king are at all times
	//this makes it easier to check when somebody is in check
	public void setBKS(BoardSquare b) {this.blackKingSquare = b;}
	public void setWKS(BoardSquare b) {this.whiteKingSquare = b;}
	public BoardSquare getWKS() {return whiteKingSquare;}
	public BoardSquare getBKS() {return blackKingSquare;}

}
