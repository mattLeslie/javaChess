package chessLeslie;

//AUTHOR: Matt Leslie
//ITP 368 FINAL PROJECT
//BEK 12/7/21
//HANDS ON CHESS TEACHER

import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//the main driver for my chess application
//builds the board, sets up the pieces, and provides user control by enabling dragging pieces
//and using the keyboard
//importantly, this driver also performs the necessary checks to enforce valid moves and endgame conditions
public class ChessLeslie extends Application{

	//all the visual building blocks
	private StackPane root;
	private BoardGrid grid;
	private Group boardPane = null;
	private VBox winPane = null;
	private Scene scene;
	private Label prompt = null;

	//keep track of all game pieces
	private ArrayList<GamePiece> blackPieces;
	private ArrayList<GamePiece> whitePieces;

	//game state variables
	private boolean searching = false;
	private boolean whiteInCheck = false;
	private boolean blackInCheck = false;
	private boolean stalemate = false;
	private boolean whiteTurn = true;
	private boolean whiteWin = false;
	private boolean blackWin = false;
	
	//the current selected piece
	//used in keyboard play
	private GamePiece currPiece = null;
		
	public static void main(String[] args) {launch(args);}
	
	//boilerplate
	public void start(Stage stage) {
	    stage.setTitle("Chess Leslie");
		root = new StackPane();
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: white;");
		scene = new Scene(root, 850, 950);
		stage.setScene(scene);
		stage.setResizable(false);
		
		stage.show();
		scene.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent k)->{processKeyPress(k);});
		
		createBoard();		
	}
	
	//set up main board display
	private void createBoard() {	
		//create grid of boardsquares
		grid = new BoardGrid();
		//draw board grid and populate data structure with board squares
		boardPane = new Group();
		
		//used to place squares on screen in grid pattern
		int xSpace = 0;
		int ySpace = 0;
	
		//place each square, alternating square colors as we go
		for(int i = 0; i < 8; i++) {
			boolean toggle = true;
			if(i%2 == 0) {toggle = false;}
			for(int j = 0; j < 8; j++) {
				BoardSquare r = grid.getSquare(i, j);
				if(toggle) {
					r.setStyle("-fx-background-color: mediumseagreen; -fx-border-color: black");
				}
				else {
					r.setStyle("-fx-background-color: oldlace; -fx-border-color: black");
				}
				r.setInitStyle();
				toggle = !toggle;
	
				r.setPrefSize(100, 100);
				r.setLayoutX(xSpace);
				r.setLayoutY(ySpace);
	
				boardPane.getChildren().add(r);
				xSpace += 100;
			}
			ySpace += 100;
			xSpace = 0;
		}
		
		//finally, add a text box at the bottom of the screen to indicate who's turn it is
		ySpace += 30;
		xSpace = 0;
		
		StackPane p = new StackPane();
		p.setPrefSize(800, 30);
		p.setLayoutX(xSpace);
		p.setLayoutY(ySpace);
		
		prompt = new Label("WHITE'S TURN");
		
		p.getChildren().add(prompt);
		
		boardPane.getChildren().add(p);
		
		p.setStyle("-fx-background-color: white; "
				+ " -fx-border-color: black;"
				+ " -fx-border-width: 5px;"
				+ " -fx-padding: 20;"
				+ " -fx-spacing: 10;"
				+ " -fx-alignment: center;"
				+ " -fx-font-size: 20;");
						
		root.getChildren().add(boardPane);
		
		//put the pieces on the board
		setUpPieces(Color.WHITE);
		setUpPieces(Color.BLACK);
	}

	//adds the appropiate number and type of pieces to each side, 
	//then adds appropiate event listeners to each piece
	private void setUpPieces(Color c) {
		ArrayList<GamePiece> thesePieces = new ArrayList<GamePiece>();
		int pawnRow = 1;
		int kingRow = 0;
		int direction = 1;
		//get appropiate row/col to start placing pieces
		//and update the direction that pawns move 
		if(c == Color.WHITE) {
			pawnRow = 6;
			kingRow = 7;
			direction = -1;
		}
		
		//add pawns
		for(int i = 0; i < 8; i++) {
			BoardSquare b = grid.getSquare(pawnRow, i);
			Pawn p = new Pawn(c, "pawn", grid, direction);
			b.setPiece(p);
			boardPane.getChildren().add(p);
			thesePieces.add(p);
		}
		
		//add rooks
		for(int i = 0; i < 8; i = i + 7) {
			BoardSquare b = grid.getSquare(kingRow, i);
			Rook r = new Rook(c, "rook", grid);
			b.setPiece(r);
			boardPane.getChildren().add(r);
			thesePieces.add(r);
		}
		
		//add knights
		for(int i = 1; i < 8; i += 5) {
			BoardSquare b = grid.getSquare(kingRow, i);
			Knight k = new Knight(c, "knight", grid);
			b.setPiece(k);
			boardPane.getChildren().add(k);
			thesePieces.add(k);
		}
		
		//add bishops
		for(int i = 2; i < 8; i += 3) {
			BoardSquare b = grid.getSquare(kingRow, i);
			Bishop bi = new Bishop(c, "bishop", grid);
			b.setPiece(bi);
			boardPane.getChildren().add(bi);
			thesePieces.add(bi);

		}
		
		//add queen
		BoardSquare queenSquare = grid.getSquare(kingRow, 3);
		Queen queen = new Queen(c, "queen", grid);
		queenSquare.setPiece(queen);
		thesePieces.add(queen);

		//add king
		BoardSquare kingSquare = grid.getSquare(kingRow, 4);
		King king = new King(c, "king", grid);
		kingSquare.setPiece(king);
		thesePieces.add(king);

		boardPane.getChildren().addAll(queen, king);
		
		//track kings and set listeners for each piece to enable dragging
		if(c == Color.WHITE) {
			grid.setWKS(kingSquare);
			this.whitePieces = thesePieces;
			for(int i = 0; i < 16; i++) {
				GamePiece p = whitePieces.get(i);				
				setMouseListeners(p);
			}	
		}
		else {
			grid.setBKS(kingSquare);
			this.blackPieces = thesePieces;
			for(int i = 0; i < 16; i++) {
				GamePiece p = blackPieces.get(i);				
				setMouseListeners(p);
			}	
		}
	}

	//change board control and update prompt
	private void switchTurn() {
		whiteTurn = !whiteTurn;
		if(whiteTurn) {
			prompt.setText("WHITE'S TURN");
		}
		else {
			prompt.setText("BLACK'S TURN");
		}
		for(int k = 0; k < 16; k++) {
			whitePieces.get(k).toggleCanMove();
			blackPieces.get(k).toggleCanMove();
		}	
	}

	//any time a piece is picked up, determine all possible (and valid) moves for the piece and highlight them on the board
	//then check if the user has dropped the piece on a highlighted square and update the game state
	private void setMouseListeners(GamePiece p) {
		//user selects piece
		p.setOnMousePressed((m)->{
			//the piece will generate all potential moves, but some may not be valid
			//the program will cull all moves that put the king into jeopardy
			cullMoves(p);
			//needed for dragging
			p.recordDist(m);
		});
		p.setOnMouseDragged((m)->{p.drag(m);});
		p.setOnMouseReleased((m)->{
			//see if the player chose a valid square and get the associated move for this piece
			Move candidate = p.checkValidPlacement(m);
			//if the move is valid, do the move!
			if(candidate != null) {
				doMove(p, candidate);
				//remove additional move options for pawns, kings, and rooks
				p.doFirstMove();
				//see if the king is in check now
				checkCheck();
				//switch turn and update the promp
				switchTurn();
				//see if the game has ended (checkmate or stalemate)
				if(checkEndgame()) {
					endGame();
				}
				//get rid of any highlights if the user was using the keyboard before using the mouse
				if(currPiece != null) {
					for(Move mov : currPiece.getMoveOptions()) {
						mov.getSquare().removeHighlight();
					}	
				}
			}
		});	
	}

	//activates when the user uses the keyboard to play the game
	private void processKeyPress(KeyEvent ke) {
		//the user can use the arrow keys to navigate in two dimensions across the board grid
		BoardSquare currSquare = null;
		
		KeyCode kc = ke.getCode();
		//if no piece is currently selected (we aren't searching for a valid move)
		//let the user select a piece by pressing enter
		if(!searching) {
			if(kc == KeyCode.ENTER) {
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						//find the selected square and get the piece on that suare
						if(grid.getSquare(i, j).isFocused()) {
							currSquare = grid.getSquare(i, j);
							currPiece = currSquare.getCurrentPiece();
							//if the board square contains a piece, get all the moves for that piece
							//and highlight them on the board
							if(currPiece != null) {
								cullMoves(currPiece);
								//then, change game state and let the user navigate to a highlighted board square
								searching = true;
							}
						}
					}
				}
			}			
		}
		else {
			//if searching for a valid move, let the user cancel their original selection
			//and find an alternate piece
			if(kc == KeyCode.ESCAPE) {
				searching = false;
				for(Move m : currPiece.getMoveOptions()) {
					m.getSquare().removeHighlight();
				}
				currPiece = null;

			}
			//otherwise, see where the user selected and check if the board square is a valid move for the piece
			//in question
			else if(kc == KeyCode.ENTER) {
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						//find selected square
						if(grid.getSquare(i, j).isFocused() && grid.getSquare(i, j).isHighlighted()) {
							//see if square exists in the piece's potential moves
							Move candidate = currPiece.checkValidKeyPress(grid.getSquare(i, j));
							if(candidate == null) {
								return;
							}
							//if the move is good, do the move!
							doMove(currPiece, candidate);
							currPiece.doFirstMove();
							searching = false;
							//see if the king is in check
							checkCheck();
							
							//change board control
							switchTurn();
							
							//and check endgame conditions
							if(checkEndgame()) {
								endGame();
							}
							//then return the board to an unhighlighted state
							for(Move m : currPiece.getMoveOptions()) {
								m.getSquare().removeHighlight();
							}
							//the piece is no longer selected
							currPiece = null;
						}
					}
				}
			}
		}

		if(currPiece != null) {
			searching = true;
		}
		else {
			searching = false;
		}
	}

	//prompts the piece to do the move, and checks the move type to see if any additional 
	//processing needs to occur
	private void doMove(GamePiece p, Move m) {
		p.doMove(m.getSquare());
		//if the move is a pawn promotion, promote the pawn to a queen
		if(m.getMoveType().equals("promotion")) {
			promotePawn(p, m.getSquare());
		}
		//if the move is a castle, move the rook to the appropiate location
		//we know for sure that the piece we're selecting is a rook
		//otherwise the move would never have been added
		else if(m.getMoveType().equals("castle")) {
			GamePiece r = grid.getSquare(p.getRow(), 7).getCurrentPiece();
			Move rookMove = new Move(grid.getSquare(p.getRow(), p.getCol() - 1), "regular");
			doMove(r, rookMove);
		}
		//same for the queen side castle
		else if(m.getMoveType().equals("qcastle")) {
			GamePiece r = grid.getSquare(p.getRow(), 0).getCurrentPiece();
			Move rookMove = new Move(grid.getSquare(p.getRow(), p.getCol() + 1), "regular");
			doMove(r, rookMove);
		}
		//if the king is moving around, update the king tracker in the grid
		else if(m.getMoveType().equals("king")){
			if(p.getColor() == Color.WHITE) {
				grid.setWKS(m.getSquare());
			}
			else {
				grid.setBKS(m.getSquare());
			}
		}
	}

	//promotes pawn to a queen
	private void promotePawn(GamePiece p, BoardSquare b) {
		//create a new queen and overwrite the pawn with it
		GamePiece newQueen = new Queen(p.getColor(), "queen", grid);	
		if(p.getColor() == Color.WHITE) {
			whitePieces.add(newQueen);
			whitePieces.remove(p);
		}
		else {
			newQueen.toggleCanMove();
			blackPieces.add(newQueen);
			blackPieces.remove(p);
		}
		p.removeFromPlay();
		b.setPiece(newQueen);
		//add the necessary listeners to enable dragging and dropping for play
		setMouseListeners(newQueen);
		boardPane.getChildren().add(newQueen);
	}
	
	//this method takes all of a piece's potential moves and removes any move that either:
	//1) places the king into check (and thus into check mate)
	//2) fails to remove the king from check (and thus forces check mate)
	protected ArrayList<Move> cullMoves(GamePiece piece) {
		ArrayList<Move> updatedList = new ArrayList<Move>();
		
		//ditch any move that lands on a square containing a piece of the same color
		for(Move move : piece.getMoveOptions()) {
			if(move.getSquare().canMove(piece.getColor()) && piece.canMove()) {
				updatedList.add(move);
			}
		}

		//if king is in check, force king to move out of check
		//OR force piece to block current attack direction
		//simulate move and check check again
		
		//keep track of the piece's original location
		BoardSquare origPlace = grid.getSquare(piece.getRow(), piece.getCol());
		ArrayList<Move> onlyValidMoves = new ArrayList<Move>();
		
		//for every move in the piece's potential move list, simulate the move and see if it leads
		//to a valid board state
		for(Move m : updatedList) {
			//keep track of the move to square's occupant
			GamePiece origOccupant = m.getSquare().getCurrentPiece();
			//do the move (we'll undo it in a second)
			piece.doMove(m.getSquare());
			if(whiteTurn) {
				whiteInCheck = false;
			}
			else {
				blackInCheck = false;
			}
			//see if the move leads to the king being in check
			checkCheck();
			if(whiteTurn) {
				//if the king isn't in check after doing this move, it's a valid move!
				if(!whiteInCheck) {
					onlyValidMoves.add(m);
				}
				//otherwise, it isn't a valid move
				//regardless of the outcome, undo the move and return both pieces to their original positions before
				//simulating the next move
				piece.doMove(origPlace);
				if(origOccupant != null) {
					origOccupant.returnToPlay();
					origOccupant.doMove(m.getSquare());
				}
			}
			//do the same checks if it's black's move
			else {
				if(!blackInCheck) {
					onlyValidMoves.add(m);
				}
				piece.doMove(origPlace);
				if(origOccupant != null) {
					origOccupant.returnToPlay();
					origOccupant.doMove(m.getSquare());
				}
			}
		}	
		
		//update list with valid moves
		updatedList = onlyValidMoves;
		//if the list has 1 or more moves, then the king will not be in check 
		if(updatedList.size() > 0) {
			piece.check(false);
			if(whiteTurn) {
				whiteInCheck = false;
			}
			else {
				blackInCheck = false;
			}
		}
		
		//highlight all possible moves
		for(Move move : updatedList) {
			move.getSquare().highlight();
		}
		//tell the piece where it can go
		piece.updateMoves(updatedList);
		//update the check flag on the king
		grid.getBKS().getCurrentPiece().check(blackInCheck);
		grid.getWKS().getCurrentPiece().check(whiteInCheck);
		
		return updatedList;
	}

	//see if either king is currently in check and update the gamestate variables
	private void checkCheck() {
		//see if any white pieces are targeting the black king
		for(GamePiece p : whitePieces) {
			if(p.isInPlay()) {
				ArrayList<BoardSquare> threats = p.getAttackSquares();
				if(threats.contains(grid.getBKS())) {
					blackInCheck = true;
				}
			}
		}
		//see if any black pieces are targeting the white king
		for(GamePiece p : blackPieces) {
			if(p.isInPlay()) {
				ArrayList<BoardSquare> threats = p.getAttackSquares();
				if(threats.contains(grid.getWKS())) {
					whiteInCheck = true;
				}	
			}
		}
		//update the kings 
		grid.getBKS().getCurrentPiece().check(blackInCheck);
		grid.getWKS().getCurrentPiece().check(whiteInCheck);
	}

	//for each player, see if there exists any valid moves on the player's turn
	//if no moves exist AND the player's king is in check, the other player wins by checkmate
	//if no moves exists and the player's king is NOT in check, it's a stalemate!
	private boolean checkEndgame() {
		if(whiteTurn) {
			//aggregate all of the possible moves of each remaining white piece
			//by calling cullMoves(), we are guaranteed that any moves that fail to address
			//a king in check will not be added
			ArrayList<Move> allMoves = new ArrayList<Move>();
			for(GamePiece p : whitePieces) {
				if(p.isInPlay()) {
					ArrayList<Move> someMoves = cullMoves(p);
					for(Move m : someMoves) {
						allMoves.add(m);
						m.getSquare().removeHighlight();
						
					}
					someMoves.clear();
					p.updateMoves(someMoves);
				}
			}
			//end game conditions
			if(allMoves.size() == 0 && whiteInCheck) {
				blackWin = true;
			}
			else if(allMoves.size() == 0 && !whiteInCheck) {
				stalemate = true;
			}
		}
		else if(!whiteTurn) {
			ArrayList<Move> allMoves = new ArrayList<Move>();
			for(GamePiece p : blackPieces) {
				if(p.isInPlay()) {
					ArrayList<Move> someMoves = cullMoves(p);
					for(Move m : someMoves) {
						allMoves.add(m);
						m.getSquare().removeHighlight();
					}
					someMoves.clear();
					p.updateMoves(someMoves);
				}
			}
			if(allMoves.size() == 0 && blackInCheck) {
				whiteWin = true;
			}
			else if(allMoves.size() == 0 && !blackInCheck) {
				stalemate = true;
			}
		}
		//if any of the end game conditions are true, the game will end
		return (blackWin || whiteWin || stalemate);
	}

	//ends the game and adds a text box to the screen stating the outcome
	//also lets the user reset the board and play another game
	private void endGame() {
		winPane = new VBox();
		winPane.setAlignment(Pos.CENTER);
		winPane.setMaxSize(300, 300);
		
		winPane.setStyle("-fx-background-color: white; "
				+ " -fx-border-color: black;"
				+ " -fx-border-width: 5px;"
				+ " -fx-padding: 20;"
				+ " -fx-spacing: 10;"
				+ " -fx-alignment: center;"
				+ " -fx-font-size: 20;");
		
		
		Label l1 = new Label("GAME OVER");

		Label l2 = new Label();

		if(blackWin) {
			l2.setText("WINNER: BLACK");
		}
		else if(whiteWin){
			l2.setText("WINNER: WHITE");
		}
		else {
			l2.setText("STALEMATE");
		}
		
		prompt.setText("GAME OVER");
		
		Button restart = new Button("RESTART");
		restart.setPadding(new Insets(25));
		restart.setPrefSize(150, 50);
		restart.setOnAction(e->{
			root.getChildren().remove(winPane);
			restart();
		});
		
		winPane.getChildren().addAll(l1, l2, restart);
		
		root.getChildren().add(winPane);
	}
	
	//reset all of the gamestate variables and remove all pieces from the game
	//then recreate the board and set up a new bunch of pieces
	private void restart() {
		
		whiteWin = false;
		blackWin = false;
		stalemate = false;
		whiteTurn = true;
		searching = false;
		whiteInCheck = false;
		blackInCheck = false;
		
		blackPieces.clear();
		whitePieces.clear();
		
		root.getChildren().remove(boardPane);
		createBoard();
	}
	
}
