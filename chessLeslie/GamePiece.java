package chessLeslie;

import java.util.ArrayList;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

//abstract definition of a game piece
//provides support for dragging
//keeps track of visual position on board, and its state in the game
//also provides support for checking the validity of a move performed by a user dragging
abstract class GamePiece extends StackPane{
	//each gamepiece gets access to the grid 
	protected BoardGrid grid;
	private Color c;
	private double dx;
	private double dy;
	protected int row;
	protected int col;
	protected double initX;
	protected double initY;
	protected boolean firstMove = true;
	private String type;
	private boolean inPlay = true;
	protected boolean inCheck = false;

	protected boolean canMove;
		
	protected ArrayList<Move> potentialMoves = new ArrayList<Move>();
	
	public GamePiece(Color color, String l, BoardGrid bg) {
		this.c = color;
		if(color == Color.WHITE) {
			canMove = true;
		}
		else {
			canMove = false;
		}
		this.type = l;
		this.grid = bg;		
		
	}
	//ALL ABSTRACT METHODS
	//movement and attack calculations are different for every piece
	public abstract ArrayList<Move> getMoveOptions();
	public abstract ArrayList<BoardSquare> getAttackSquares();
	//let each piece select a unique image
	protected abstract void setSprite(Color c);
	
	//CONVENIENCE METHODS
	public void check(boolean t) {this.inCheck = t;}
	
	public void doFirstMove() {this.firstMove = false;}
	public void toggleCanMove() {this.canMove = !this.canMove;}
	
	public boolean canMove() {return this.canMove;}
		
	public Color getColor() {return c;}
	
	public void updateMoves(ArrayList<Move> updatedList) {this.potentialMoves = updatedList;}
	
	public boolean isInPlay() {return this.inPlay;}
	
	public void removeFromPlay() {
		this.inPlay = false;
		this.setVisible(false);
	}
	
	public void returnToPlay() {
		this.inPlay = true;
		this.setVisible(true);
	}
	
	//set the initial x and y of the piece
	//if the user drops the piece in an invalid location, the piece will return
	//to these coordinates
	public void setInitXY(double x, double y) {
		this.initX = x;
		this.initY = y;
		this.setLayoutX(x);
		this.setLayoutY(y);
	}
	
	//set coordinates on screen
	public void setRowCol(int x, int y) {
		this.col = x;
		this.row = y;
	}
	public int getRow() {return row;}
	public int getCol() {return col;}
	
	public String getType() {return this.type;}
	
	
	
	//DRAGGING AND DROPPING -----
	//whenever the mouse is pressed over the shape's pane, record the distance 
	//that the mouse has traveled from its original location
	public void recordDist(MouseEvent m) {
		//always have piece visible when dragging it around
		this.toFront();
		this.setCursor(Cursor.CLOSED_HAND);
        this.dx = this.getLayoutX() - m.getSceneX();
        this.dy = this.getLayoutY() - m.getSceneY();	

	}
	//whenever the mouse presses the shape's pane and drags from that position,
	//update the shape's pane's position with new coordinates
	public void drag(MouseEvent m) {
		this.setLayoutX(m.getSceneX() + dx);
		this.setLayoutY(m.getSceneY() + dy);	
	}
	
	//---------------

	
	
	//----MOVEMENT
	//move piece to given boardsquare
	public void doMove(BoardSquare b) {
		//clear current square
		grid.getSquare(row, col).clearPiece();
		//set new square
		b.setPiece(this);
		//if we're moving the king, update the grid's king tracker
		if(this.getType().equals("king")) {
			if(this.getColor() == Color.WHITE) {
				grid.setWKS(b);
			}
			else {
				grid.setBKS(b);
			}
		}
		//update visual location
		this.setRowCol(b.getRow(), b.getCol());
	}
	
	//check if selected boardsquare is a place that the piece can move to
	public Move checkValidKeyPress(BoardSquare b) {
		Move candidate = null;
		//check square against all potential moves
		for(int i = 0; i < potentialMoves.size(); i++) {
			if(b == potentialMoves.get(i).getSquare()) {
				candidate = potentialMoves.get(i);
				break;
			}
		}
		//if valid square, we'll move the piece to valid square
		//so clear the highlights from everything else
		if(candidate != null) {
			for(Move m : potentialMoves) {
				m.getSquare().removeHighlight();
			}
		}
		return candidate;
	}
	
	//see if the user's drop was valid
	public Move checkValidPlacement(MouseEvent m) {
		Move candidate = null;
		//see if the square the piece was dropped into matches
		//a square in the piece's potential moves list
		for(int i = 0; i < potentialMoves.size(); i++) {
			BoardSquare b = potentialMoves.get(i).getSquare();
			Double bX = b.getLayoutX();
			Double bY = b.getLayoutY();
			//subtract 25 to account for boardpane offset
			//should probably come back and remove these magic numbers if there's time
			Double mX = m.getSceneX() - 25;
			Double mY = m.getSceneY() - 25;
			
			//if we found a match, return the move
			if(mX >= bX && mX <= bX + 100 && mY >= bY && mY <= bY + 100 && canMove) {
				candidate = potentialMoves.get(i);
				break;
			}
		}
		//if nothing valid was found, return the piece to its original position
		if(candidate == null) {
			this.setLayoutX(initX);
			this.setLayoutY(initY);
		}
		//get rid of all of the highlights
		for(int i = 0; i < potentialMoves.size(); i++) {
			potentialMoves.get(i).getSquare().removeHighlight();
		}
		potentialMoves.clear();
		this.setCursor(Cursor.DEFAULT);

		return candidate;
	}
	
	//make sure the move's board square can accept this piece
	protected boolean checkMoveValidity(int i, int j) {
		BoardSquare bs = grid.getSquare(i, j);
		if(bs.canMove(this.getColor())) {
			this.potentialMoves.add(new Move(bs, "regular"));
			if(bs.getCurrentPiece() == null) {
				return true;
			}
		}
		return false;
	}
	
	//---------
}
