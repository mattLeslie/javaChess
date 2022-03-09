package chessLeslie;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

//the representation of a square in the grid
//can hold a piece and be highlighted
public class BoardSquare extends StackPane{
	String baseStyle;
	private GamePiece currPiece;
	private int row;
	private int col;
	private boolean isHighlighted = false;
	
	public BoardSquare(int col, int row) {
		this.row = row;
		this.col = col;
		
		//let user use arrow keys to traverse grid, uniquely highlighting the selected square
	    this.setFocusTraversable(true);
	    this.focusedProperty().addListener
	    ((observalbe, oldState, newState) -> 
	       {
	    	  if (newState){
	    		 this.setStyle("-fx-border-color: gold; -fx-border-width: 5px; -fx-cursor: hand;");
	          }
	    	  else {
	    		  if(isHighlighted) {
	    			  this.setStyle(baseStyle + "; -fx-border-color:orangered; -fx-border-width: 8px");
	    		  }
	    		  else {
	    			  this.setStyle(baseStyle);
	    		  }
	    	  }
	       }
	    );
	    
	}

	//keep track of the original coloring of the board square
	public void setInitStyle() {
		baseStyle = this.getStyle();
	}

	public GamePiece getCurrentPiece() {
		return currPiece;
	}
	
	//remove the current piece (if any) and update the piece
	//that this square holds
	//also update the x,y coordinates of the new piece to visually
	//represent the new board state
	public void setPiece(GamePiece g) {
		if(currPiece != null) {
			currPiece.removeFromPlay();
		}
		currPiece = g;
		currPiece.setRowCol(row, col);
		//always make the piece visible
		currPiece.toFront();
		currPiece.setInitXY(this.getLayoutX() + 5, this.getLayoutY() + 5);

	}
	public int getRow() {
		return this.row;
	}
	
	public int getCol() {
		return this.col;
	}

	public void highlight() {
		isHighlighted = true;
		String newStyle = baseStyle + "; -fx-border-color:orangered; -fx-border-width: 8px";
		this.setStyle(newStyle);
	}
	public void removeHighlight() {
		isHighlighted = false;
		this.setStyle(baseStyle);
	}
	
	public boolean isHighlighted() {
		return isHighlighted;
	}
	
	public void clearPiece() {
		this.currPiece = null;
	}
	
	//see if the board square can accept a piece of the specified color
	public boolean canMove(Color c) {
		if(currPiece == null) {
			return true;
		}
		if(currPiece.getColor() != c) {
			return true;
		}
		return false;
	}
	
}
