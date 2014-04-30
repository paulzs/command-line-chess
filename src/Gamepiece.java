import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * Gamepiece.java
 * 
 * @author Paul Shi
 *
 */

public class Gamepiece{
	
	private String piece; //String to hold piece value
	private String position; //String to hold position value
	private String player; //String to hold player value
	private ArrayList<String> moves; //List of possible valid moves
	private int movecount; //Number of moves this piece has taken
	
	public Gamepiece(){ // Constructor
		this.piece = "";
		this.position = "";
		this.player = "";
		this.moves = new ArrayList<String>();
		movecount = 0;
	}
	
	//Getters and Setters
	
	public String getPiece(){
		return this.piece;
	}
	
	public boolean setPiece(String newPiece){
		
		if(newPiece.length() > 1){
			return false;
		}
		
		if(!newPiece.equalsIgnoreCase("p") && 
				!newPiece.equalsIgnoreCase("r") && 
				!newPiece.equalsIgnoreCase("n") && 
				!newPiece.equalsIgnoreCase("b") && 
				!newPiece.equalsIgnoreCase("q") && 
				!newPiece.equalsIgnoreCase("k")){
			return false;
		}
		
		this.piece = newPiece;
		return true;
	}
	
	public String getPosition(){
		return this.position;
	}
	
	public boolean setPosition(String newPosition){
		
		if(newPosition.length() != 2){
			return false;
		}
		
		String file = newPosition.substring(0, 1);
		String rank = newPosition.substring(1, 2);
		
		char fileChar = file.charAt(0);
		char rankNum = rank.charAt(0);
		int rankInt = Character.getNumericValue(rankNum);
		
		if(!Character.isLetter(fileChar) || (file.compareToIgnoreCase("h") > 0)){
			return false;
		}
		
		if(!Character.isDigit(rankNum) || ((rankInt < 1) || (rankInt > 8))){
			return false;
		}
		
		this.position = newPosition.toLowerCase();
		return true;
	}
	
	public String getPlayer(){
		return this.player;
	}
	
	public boolean setPlayer(String newPlayer){
		
		if(!newPlayer.equalsIgnoreCase("w") && !newPlayer.equalsIgnoreCase("b")){
			return false;
		}
		
		this.player = newPlayer;
		return true;
	}
	
	public ArrayList<String> getMoves(){
		return this.moves;
	}
	
	public void setMoves(ArrayList<String> moveList){
		this.moves = moveList;
	}
	
	public int getMoveCount(){
		return this.movecount;
	}
	
	public void setMoveCount(int newMC){
		this.movecount = newMC;
	}
	
	//Generate all the possible moves for a given piece
	
	public void createMoveSet(Board currentBoard){
		
		ArrayList<String> newMoveSet = new ArrayList<String>();
		
		//CASE: Pawn
		
		if(this.getPiece().equalsIgnoreCase("P")){
			int pieceI = Controller.rankToInd(this.getPosition());
			int pieceJ = Controller.fileToInd(this.getPosition());
			String tempMove = "";
			
			if(this.getPlayer().equalsIgnoreCase("w")){ //CASE: White
				
				if(this.getMoveCount() == 0){ //If this is the pawns first move, it can move 2 spaces ahead
					if((currentBoard.GBoard[pieceI+1][pieceJ] == null) && 
							(currentBoard.GBoard[pieceI+2][pieceJ] == null)){
						tempMove = Controller.indToPos(pieceI + 2, pieceJ);
						this.moves.add(tempMove);
					}
				}
				
				if(currentBoard.GBoard[pieceI+1][pieceJ] == null){ //If the space in front of the pawn is free, it can move there.
					tempMove = Controller.indToPos(pieceI + 1, pieceJ);
					this.moves.add(tempMove);
				}
				
				if((pieceJ+1) < 8){ //If the space to the diagonal of the pawn holds an opponent's piece, it can take it
					if(currentBoard.GBoard[pieceI+1][pieceJ+1] != null){
						if(currentBoard.GBoard[pieceI+1][pieceJ+1].getPlayer().equalsIgnoreCase("b")){
							tempMove = Controller.indToPos(pieceI+1, pieceJ+1);
							this.moves.add(tempMove);
						}
					}
				}
				
				if((pieceJ-1) > -1){ //Other diagonal
					if(currentBoard.GBoard[pieceI+1][pieceJ-1] != null){
						if(currentBoard.GBoard[pieceI+1][pieceJ-1].getPlayer().equalsIgnoreCase("b")){
							tempMove = Controller.indToPos(pieceI+1, pieceJ-1);
							this.moves.add(tempMove);
						}
					}	
				}
				
				//En passant
				
				if(pieceI == 4){ //If the pawn is on the 5th rank, it has a chance of capturing en passant
					//If last piece moved is a pawn to either file next to the pawn on rank 5, make en passant a valid move 
					if(Controller.fileToInd(currentBoard.lastPieceMoved.getPosition()) == (pieceJ + 1)){
						if(currentBoard.lastPieceMoved.getPiece().equalsIgnoreCase("p") && currentBoard.lastPieceMoved.movecount == 1){
							tempMove = Controller.indToPos(pieceI+1, pieceJ+1);
							this.moves.add(tempMove);
						}
					}
					
					if(Controller.fileToInd(currentBoard.lastPieceMoved.getPosition()) == (pieceJ - 1)){
						if(currentBoard.lastPieceMoved.getPiece().equalsIgnoreCase("p") && currentBoard.lastPieceMoved.movecount == 1){
							tempMove = Controller.indToPos(pieceI+1, pieceJ-1);
							this.moves.add(tempMove);
						}
					}
				}
				
			}
			else{ //Repeat all pawn options for black
				
				if(this.getMoveCount() == 0){
					if((currentBoard.GBoard[pieceI-1][pieceJ] == null) && 
							(currentBoard.GBoard[pieceI-2][pieceJ] == null)){
						tempMove = Controller.indToPos(pieceI - 2, pieceJ);
						this.moves.add(tempMove);
					}
				}
				
				if(currentBoard.GBoard[pieceI-1][pieceJ] == null){
					tempMove = Controller.indToPos(pieceI - 1, pieceJ);
					this.moves.add(tempMove);
				}
				
				if((pieceJ-1) > -1){
					if(currentBoard.GBoard[pieceI-1][pieceJ-1] != null){
						if(currentBoard.GBoard[pieceI-1][pieceJ-1].getPlayer().equalsIgnoreCase("w")){
							tempMove = Controller.indToPos(pieceI-1, pieceJ-1);
							this.moves.add(tempMove);
						}
					}
				}
				
				if((pieceJ+1) < 8){
					if(currentBoard.GBoard[pieceI-1][pieceJ+1] != null){
						if(currentBoard.GBoard[pieceI-1][pieceJ+1].getPlayer().equalsIgnoreCase("w")){
							tempMove = Controller.indToPos(pieceI-1, pieceJ+1);
							this.moves.add(tempMove);
						}
					}	
				}
				
				//En passant
				
				if(pieceI == 3){
					if((Controller.rankToInd(currentBoard.lastPieceMoved.getPosition()) == pieceI) && 
							(Controller.fileToInd(currentBoard.lastPieceMoved.getPosition()) == (pieceJ + 1))){
						if(currentBoard.lastPieceMoved.getPiece().equalsIgnoreCase("p") && currentBoard.lastPieceMoved.movecount == 1){
							tempMove = Controller.indToPos(pieceI-1, pieceJ+1);
							this.moves.add(tempMove);
						}
					}
					
					if((Controller.rankToInd(currentBoard.lastPieceMoved.getPosition()) == pieceI) &&
							(Controller.fileToInd(currentBoard.lastPieceMoved.getPosition()) == (pieceJ - 1))){
						if(currentBoard.lastPieceMoved.getPiece().equalsIgnoreCase("p") && currentBoard.lastPieceMoved.movecount == 1){
							tempMove = Controller.indToPos(pieceI-1, pieceJ-1);
							this.moves.add(tempMove);
						}
					}
				}
			}
		
		}
		
		if(this.getPiece().equalsIgnoreCase("R")){ //CASE: Rook
			int pieceI = Controller.rankToInd(this.getPosition());
			int pieceJ = Controller.fileToInd(this.getPosition());
			String tempMove = "";
			
			if(this.getPlayer().equalsIgnoreCase("w")){ //CASE: White
				
				for(int i = pieceI + 1;i < 8;i++){ //Check spaces to the north of rook for empty spaces/black pieces
					if(currentBoard.GBoard[i][pieceJ] == null){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						continue;
					} //If a black piece occupies a space in front of rook, add that space as a valid move, but stop iterating (rook cannot go through opponent)
					else if(currentBoard.GBoard[i][pieceJ].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
				
				for(int i = pieceI - 1;i > -1;i--){ //Check south
					if(currentBoard.GBoard[i][pieceJ] == null){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						continue;
					}
					else if(currentBoard.GBoard[i][pieceJ].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
				
				for(int j = pieceJ - 1;j > -1;j--){ //Check west
					if(currentBoard.GBoard[pieceI][j] == null){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						continue;
					}
					else if(currentBoard.GBoard[pieceI][j].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
				
				for(int j = pieceJ + 1;j < 8;j++){ //Check east
					if(currentBoard.GBoard[pieceI][j] == null){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						continue;
					}
					else if(currentBoard.GBoard[pieceI][j].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
			}
			else{ //Repeat for black
				for(int i = pieceI + 1;i < 8;i++){
					if(currentBoard.GBoard[i][pieceJ] == null){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						continue;
					}
					else if(currentBoard.GBoard[i][pieceJ].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
				
				for(int i = pieceI - 1;i > -1;i--){
					if(currentBoard.GBoard[i][pieceJ] == null){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						continue;
					}
					else if(currentBoard.GBoard[i][pieceJ].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
				
				for(int j = pieceJ - 1;j > -1;j--){
					if(currentBoard.GBoard[pieceI][j] == null){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						continue;
					}
					else if(currentBoard.GBoard[pieceI][j].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
				
				for(int j = pieceJ + 1;j < 8;j++){
					if(currentBoard.GBoard[pieceI][j] == null){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						continue;
					}
					else if(currentBoard.GBoard[pieceI][j].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
				
			}
		}
		
		if(this.getPiece().equalsIgnoreCase("N")){ //CASE: knight
			int pieceI = Controller.rankToInd(this.getPosition());
			int pieceJ = Controller.fileToInd(this.getPosition());
			String tempMove = "";
			
			if(this.getPlayer().equalsIgnoreCase("w")){ //CASE:white
				
				int iOffset = pieceI + 2; //Combinations of 2 rank and 1 file
				int jOffset = pieceJ + 1;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){ //If space is empty or occupied by opponent, it is a valid move
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
				
				iOffset = pieceI + 2;
				jOffset = pieceJ - 1;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
				
				iOffset = pieceI - 2;
				jOffset = pieceJ + 1;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
				
				iOffset = pieceI - 2;
				jOffset = pieceJ - 1;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
				
				iOffset = pieceI + 1; //Combinations of 1 rank and 2 file
				jOffset = pieceJ + 2;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
				
				iOffset = pieceI + 1;
				jOffset = pieceJ - 2;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
				
				iOffset = pieceI - 1;
				jOffset = pieceJ + 2;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
				
				iOffset = pieceI - 1;
				jOffset = pieceJ - 2;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
			}
			else{ //Repeat for black
				int iOffset = pieceI + 2;
				int jOffset = pieceJ + 1;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
				
				iOffset = pieceI + 2;
				jOffset = pieceJ - 1;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
				
				iOffset = pieceI - 2;
				jOffset = pieceJ + 1;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
				
				iOffset = pieceI - 2;
				jOffset = pieceJ - 1;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
				
				iOffset = pieceI + 1;
				jOffset = pieceJ + 2;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
				
				iOffset = pieceI + 1;
				jOffset = pieceJ - 2;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
				
				iOffset = pieceI - 1;
				jOffset = pieceJ + 2;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
				
				iOffset = pieceI - 1;
				jOffset = pieceJ - 2;
				
				if((iOffset < 8) && (jOffset < 8) && (iOffset > -1) && (jOffset > -1)){
					if((currentBoard.GBoard[iOffset][jOffset] == null) || currentBoard.GBoard[iOffset][jOffset].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(iOffset, jOffset);
						this.moves.add(tempMove);
					}
				}
			}
		}
		
		if(this.getPiece().equalsIgnoreCase("B")){ //Case: Bishop
			int pieceI = Controller.rankToInd(this.getPosition());
			int pieceJ = Controller.fileToInd(this.getPosition());
			String tempMove = "";
			
			if(this.getPlayer().equalsIgnoreCase("w")){//Case: White
				
				for(int i = 1;i < 8;i++){ //Test diagonals starting from 1 space away to the Northeast. Max is 7.
					
					int newI = pieceI + i;
					int newJ = pieceJ + i;
					
					//If the space is on the board and empty or contains opponent piece, add to valid move list
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){ 
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("b")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
				
				for(int i = 1;i < 8;i++){ 
					
					int newI = pieceI - i; //Southeast
					int newJ = pieceJ + i;
					
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("b")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
				
				for(int i = 1;i < 8;i++){
					
					int newI = pieceI + i; //Northwest
					int newJ = pieceJ - i;
					
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("b")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
				
				for(int i = 1;i < 8;i++){
					
					int newI = pieceI - i; //Southwest
					int newJ = pieceJ - i;
					
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("b")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
			}
			else{ //Repeat for black
				
				for(int i = 1;i < 8;i++){
					
					int newI = pieceI + i;
					int newJ = pieceJ + i;
					
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("w")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
				
				for(int i = 1;i < 8;i++){
					
					int newI = pieceI - i;
					int newJ = pieceJ + i;
					
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("w")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
				
				for(int i = 1;i < 8;i++){
					
					int newI = pieceI + i;
					int newJ = pieceJ - i;
					
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("w")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
				
				for(int i = 1;i < 8;i++){
					
					int newI = pieceI - i;
					int newJ = pieceJ - i;
					
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("w")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
				
			}
		}
		
		if(this.getPiece().equalsIgnoreCase("Q")){ //CASE Queen
			int pieceI = Controller.rankToInd(this.getPosition());
			int pieceJ = Controller.fileToInd(this.getPosition());
			String tempMove = "";
			
			//Combine the previously programmed move search for Bishop and Rook
			
			if(this.getPlayer().equalsIgnoreCase("w")){
				for(int i = pieceI + 1;i < 8;i++){
					if(currentBoard.GBoard[i][pieceJ] == null){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						continue;
					}
					else if(currentBoard.GBoard[i][pieceJ].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
				
				for(int i = pieceI - 1;i > -1;i--){
					if(currentBoard.GBoard[i][pieceJ] == null){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						continue;
					}
					else if(currentBoard.GBoard[i][pieceJ].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
				
				for(int j = pieceJ - 1;j > -1;j--){
					if(currentBoard.GBoard[pieceI][j] == null){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						continue;
					}
					else if(currentBoard.GBoard[pieceI][j].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
				
				for(int j = pieceJ + 1;j < 8;j++){
					if(currentBoard.GBoard[pieceI][j] == null){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						continue;
					}
					else if(currentBoard.GBoard[pieceI][j].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
				
				for(int i = 1;i < 8;i++){
					
					int newI = pieceI + i;
					int newJ = pieceJ + i;
					
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("b")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
				
				for(int i = 1;i < 8;i++){
					
					int newI = pieceI - i;
					int newJ = pieceJ + i;
					
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("b")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
				
				for(int i = 1;i < 8;i++){
					
					int newI = pieceI + i;
					int newJ = pieceJ - i;
					
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("b")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
				
				for(int i = 1;i < 8;i++){
					
					int newI = pieceI - i;
					int newJ = pieceJ - i;
					
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("b")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
			}
			else{
				for(int i = pieceI + 1;i < 8;i++){
					if(currentBoard.GBoard[i][pieceJ] == null){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						continue;
					}
					else if(currentBoard.GBoard[i][pieceJ].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
				
				for(int i = pieceI - 1;i > -1;i--){
					if(currentBoard.GBoard[i][pieceJ] == null){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						continue;
					}
					else if(currentBoard.GBoard[i][pieceJ].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(i, pieceJ);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
				
				for(int j = pieceJ - 1;j > -1;j--){
					if(currentBoard.GBoard[pieceI][j] == null){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						continue;
					}
					else if(currentBoard.GBoard[pieceI][j].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
				
				for(int j = pieceJ + 1;j < 8;j++){
					if(currentBoard.GBoard[pieceI][j] == null){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						continue;
					}
					else if(currentBoard.GBoard[pieceI][j].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(pieceI, j);
						this.moves.add(tempMove);
						break;
					}
					else{
						break;
					}
				}
				
				for(int i = 1;i < 8;i++){
					
					int newI = pieceI + i;
					int newJ = pieceJ + i;
					
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("w")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
				
				for(int i = 1;i < 8;i++){
					
					int newI = pieceI - i;
					int newJ = pieceJ + i;
					
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("w")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
				
				for(int i = 1;i < 8;i++){
					
					int newI = pieceI + i;
					int newJ = pieceJ - i;
					
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("w")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
				
				for(int i = 1;i < 8;i++){
					
					int newI = pieceI - i;
					int newJ = pieceJ - i;
					
					if((newI < 8) && (newI > -1) && (newJ < 8) && (newJ > -1)){
						if((currentBoard.GBoard[newI][newJ] == null)){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							continue;
						}
						else if(currentBoard.GBoard[newI][newJ].getPlayer().equalsIgnoreCase("w")){
							tempMove = Controller.indToPos(newI, newJ);
							this.moves.add(tempMove);
							break;
						}
						else{
							break;
						}
					}
					else{
						break;
					}
				}
				
			}
		}
		
		if(this.getPiece().equalsIgnoreCase("K")){ //Case king
			int pieceI = Controller.rankToInd(this.getPosition());
			int pieceJ = Controller.fileToInd(this.getPosition());
			String tempMove = "";
			
			if(this.getPlayer().equalsIgnoreCase("w")){ //Case white
				
				if((pieceI + 1) < 8){ //Check all adjacent spaces for empty spaces or enemy units
					if(currentBoard.GBoard[pieceI + 1][pieceJ] == null || 
							currentBoard.GBoard[pieceI + 1][pieceJ].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(pieceI + 1, pieceJ);
						this.moves.add(tempMove);
					}
				}
				
				if((pieceI - 1) > -1){
					if(currentBoard.GBoard[pieceI - 1][pieceJ] == null || 
							currentBoard.GBoard[pieceI - 1][pieceJ].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(pieceI - 1, pieceJ);
						this.moves.add(tempMove);
					}
				}
				
				if((pieceJ + 1) < 8){
					if(currentBoard.GBoard[pieceI][pieceJ + 1] == null || 
							currentBoard.GBoard[pieceI][pieceJ + 1].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(pieceI, pieceJ + 1);
						this.moves.add(tempMove);
					}
				}
				
				if((pieceJ - 1) > -1){
					if(currentBoard.GBoard[pieceI][pieceJ - 1] == null || 
							currentBoard.GBoard[pieceI][pieceJ - 1].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(pieceI, pieceJ - 1);
						this.moves.add(tempMove);
					}
				}
				
				if(((pieceI + 1) < 8) && ((pieceJ + 1) < 8)){
					if(currentBoard.GBoard[pieceI + 1][pieceJ + 1] == null || 
							currentBoard.GBoard[pieceI + 1][pieceJ + 1].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(pieceI + 1, pieceJ + 1);
						this.moves.add(tempMove);
					}
				}
				
				if(((pieceI + 1) < 8) && ((pieceJ - 1) > -1)){
					if(currentBoard.GBoard[pieceI + 1][pieceJ - 1] == null || 
							currentBoard.GBoard[pieceI + 1][pieceJ - 1].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(pieceI + 1, pieceJ - 1);
						this.moves.add(tempMove);
					}
				}
				
				if(((pieceI - 1) > -1) && ((pieceJ + 1) < 8)){
					if(currentBoard.GBoard[pieceI - 1][pieceJ + 1] == null || 
							currentBoard.GBoard[pieceI - 1][pieceJ + 1].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(pieceI - 1, pieceJ + 1);
						this.moves.add(tempMove);
					}
				}
				
				if(((pieceI - 1) > -1) && ((pieceJ - 1) > -1)){
					if(currentBoard.GBoard[pieceI - 1][pieceJ - 1] == null || 
							currentBoard.GBoard[pieceI - 1][pieceJ - 1].getPlayer().equalsIgnoreCase("b")){
						tempMove = Controller.indToPos(pieceI - 1, pieceJ - 1);
						this.moves.add(tempMove);
					}
				}
				
				//Castling
				
				if((this.getMoveCount() == 0)  && (currentBoard.GBoard[0][7] != null)){
					if((currentBoard.GBoard[0][7].getMoveCount() == 0) && 
							(currentBoard.GBoard[0][6] == null) && 
							(currentBoard.GBoard[0][5] == null)){
						tempMove = Controller.indToPos(0, 6);
						this.moves.add(tempMove);
					}
				}
				
				if((this.getMoveCount() == 0)  && (currentBoard.GBoard[0][0] != null)){
					if((currentBoard.GBoard[0][0].getMoveCount() == 0) && 
							(currentBoard.GBoard[0][1] == null) && 
							(currentBoard.GBoard[0][2] == null) &&
							(currentBoard.GBoard[0][3] == null)){
						tempMove = Controller.indToPos(0, 2);
						this.moves.add(tempMove);
					}
				}
				
			}
			else{ //Repeat for black
				
				if((pieceI + 1) < 8){
					if(currentBoard.GBoard[pieceI + 1][pieceJ] == null || 
							currentBoard.GBoard[pieceI + 1][pieceJ].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(pieceI + 1, pieceJ);
						this.moves.add(tempMove);
					}
				}
				
				if((pieceI - 1) > -1){
					if(currentBoard.GBoard[pieceI - 1][pieceJ] == null || 
							currentBoard.GBoard[pieceI - 1][pieceJ].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(pieceI - 1, pieceJ);
						this.moves.add(tempMove);
					}
				}
				
				if((pieceJ + 1) < 8){
					if(currentBoard.GBoard[pieceI][pieceJ + 1] == null || 
							currentBoard.GBoard[pieceI][pieceJ + 1].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(pieceI, pieceJ + 1);
						this.moves.add(tempMove);
					}
				}
				
				if((pieceJ - 1) > -1){
					if(currentBoard.GBoard[pieceI][pieceJ - 1] == null || 
							currentBoard.GBoard[pieceI][pieceJ - 1].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(pieceI, pieceJ - 1);
						this.moves.add(tempMove);
					}
				}
				
				if(((pieceI + 1) < 8) && ((pieceJ + 1) < 8)){
					if(currentBoard.GBoard[pieceI + 1][pieceJ + 1] == null || 
							currentBoard.GBoard[pieceI + 1][pieceJ + 1].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(pieceI + 1, pieceJ + 1);
						this.moves.add(tempMove);
					}
				}
				
				if(((pieceI + 1) < 8) && ((pieceJ - 1) > -1)){
					if(currentBoard.GBoard[pieceI + 1][pieceJ - 1] == null || 
							currentBoard.GBoard[pieceI + 1][pieceJ - 1].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(pieceI + 1, pieceJ - 1);
						this.moves.add(tempMove);
					}
				}
				
				if(((pieceI - 1) > -1) && ((pieceJ + 1) < 8)){
					if(currentBoard.GBoard[pieceI - 1][pieceJ + 1] == null || 
							currentBoard.GBoard[pieceI - 1][pieceJ + 1].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(pieceI - 1, pieceJ + 1);
						this.moves.add(tempMove);
					}
				}
				
				if(((pieceI - 1) > -1) && ((pieceJ - 1) > -1)){
					if(currentBoard.GBoard[pieceI - 1][pieceJ - 1] == null || 
							currentBoard.GBoard[pieceI - 1][pieceJ - 1].getPlayer().equalsIgnoreCase("w")){
						tempMove = Controller.indToPos(pieceI - 1, pieceJ - 1);
						this.moves.add(tempMove);
					}
				}
				
				//Castling
				
				if((this.getMoveCount() == 0)  && (currentBoard.GBoard[7][7] != null)){
					if((currentBoard.GBoard[7][7].getMoveCount() == 0) && 
							(currentBoard.GBoard[7][6] == null) && 
							(currentBoard.GBoard[7][5] == null)){
						tempMove = Controller.indToPos(7, 6);
						this.moves.add(tempMove);
					}
				}
				
				if((this.getMoveCount() == 0)  && (currentBoard.GBoard[7][0] != null)){
					if((currentBoard.GBoard[7][0].getMoveCount() == 0) && 
							(currentBoard.GBoard[7][1] == null) && 
							(currentBoard.GBoard[7][2] == null) &&
							(currentBoard.GBoard[7][3] == null)){
						tempMove = Controller.indToPos(7, 2);
						this.moves.add(tempMove);
					}
				}
			}
		}
	}
	
}