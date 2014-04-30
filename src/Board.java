import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * Board.java
 * 
 * @author Paul Shi
 *
 */

public class Board{
	
	public Gamepiece[][] GBoard; //A gameboard that is a 2d array of gamepieces and empty squares
	public Gamepiece lastPieceMoved; //Last piece moved. Self explanatory.
	public ArrayList<String> blackPieceList; //Hold a list of black pieces. Could be handy.
	public ArrayList<String> whitePieceList; //Hold a list of white pieces. Could be handy again.
	public int turn; //Turn of game. For testing.
	
	public Board(){
		this.GBoard = new Gamepiece[8][8];
		this.lastPieceMoved = null;
		this.blackPieceList = new ArrayList<String>();
		this.whitePieceList = new ArrayList<String>();
		this.turn = 0;
	}
	
	public void printBoard(){
		
		//Print the board
		for(int i = 7; i>=0; i--){
			for(int j = 0; j<8; j++){
				if(this.GBoard[i][j] == null){
					if((isEven(i) && isEven(j)) || (!isEven(i) && !isEven(j))){
						System.out.print("%% ");
						if(j == 7){
							int k = i+1;
							System.out.print(" " + k + "\n");
						}
						continue;
					}
					else{
						System.out.print("   ");
						if(j == 7){
							int k = i+1;
							System.out.print(" " + k + "\n");
						}
						continue;
					}
				}
				
				System.out.print(this.GBoard[i][j].getPlayer() + this.GBoard[i][j].getPiece() + " ");

				if(j == 7){
					int k = i+1;
					System.out.print(" " + k + "\n");
				}
			}
			if(i == 0){

				System.out.println(" a  b  c  d  e  f  g  h \n");
				
				System.out.println(" ");
				if(this.lastPieceMoved != null){
					System.out.println("Last piece moved was: " + this.lastPieceMoved.getPlayer() + this.lastPieceMoved.getPiece() + " to " + this.lastPieceMoved.getPosition());
				}
				
			}
		}
	}
	
	//Standard even check
	public boolean isEven(int i){
		if(i % 2 == 0){
			return true;
		}
		return false;
	}
	
	//See if a certain position is attackable by enemy piece. Used to find if a king's position of potential move is in check
	public boolean isCheck(String pos, boolean white){
		int I = Controller.rankToInd(pos);
		int J = Controller.fileToInd(pos);

		if(white){
			for(String h:this.getblackPiecelist()){
				int i = Controller.rankToInd(h);
				int j = Controller.fileToInd(h);
				ArrayList<String> pieceMoves = this.GBoard[i][j].getMoves();
				
				for(int x = 0; x<pieceMoves.size();x++){
					if(pieceMoves.get(x).equalsIgnoreCase(pos)){
						return true;
					}
				}
				
			}
			return false;
		}
		else{
			for(String h:this.getwhitePieceList()){
				int i = Controller.rankToInd(h);
				int j = Controller.fileToInd(h);
				ArrayList<String> pieceMoves = this.GBoard[i][j].getMoves();
				
				for(int x = 0; x<pieceMoves.size();x++){
					if(pieceMoves.get(x).equalsIgnoreCase(pos)){
						return true;
					}
				}
				
			}
			return false;
		}
		
	}
	
	//Method to find checkmate condition
	public boolean isCheckmate(Gamepiece king, boolean isWhite){
		
		if(!this.isCheck(king.getPosition(), isWhite)){
			return false;
		}
		
		for(String h:king.getMoves()){
			if(!this.isCheck(h, isWhite)){
				return false;
			}
		}
		
		return true;
	}
	
	//Method to find stalemate condition
	public boolean isStalemate(Gamepiece king, boolean isWhite){
		
		
		if(this.isCheck(king.getPosition(), isWhite)){
			return false;
		}
		
		if(king.getMoves().size() == 0){
			return false;
		}
		
		for(String h:king.getMoves()){
			if(!this.isCheck(h, isWhite)){
				return false;
			}
		}
		
		for(int i = 0;i<8;i++){
			for(int j =0;j<8;j++){
				if(this.GBoard[i][j] == null){
					continue;
				}
				else{
					if(isWhite){
						if((this.GBoard[i][j].getPlayer().equalsIgnoreCase("w")) && 
								(!this.GBoard[i][j].getPiece().equalsIgnoreCase("k")) && 
								(this.GBoard[i][j].getMoves().size() != 0)){
							return false;
						}
					}
					else{
						if((this.GBoard[i][j].getPlayer().equalsIgnoreCase("b")) && 
								(!this.GBoard[i][j].getPiece().equalsIgnoreCase("k")) && 
								(this.GBoard[i][j].getMoves().size() != 0)){
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}
	
	//Generate black and white piece lists
	public void makePieceList(){
		for(int i = 0;i<8;i++){
			for(int j = 0;j<8;j++){
				if(this.GBoard[i][j] == null){
					continue;
				}
				else{
					if(this.GBoard[i][j].getPlayer().equalsIgnoreCase("w")){
						this.whitePieceList.add(this.GBoard[i][j].getPosition());
					}
					else{
						this.blackPieceList.add(this.GBoard[i][j].getPosition());
					}
				}
			}
		}
	}
	
	public ArrayList<String> getblackPiecelist(){
		return this.blackPieceList;
	}
	
	public ArrayList<String> getwhitePieceList(){
		return this.whitePieceList;
	}
	
	public void updateMoves(){
		for(int i = 0; i<8; i++){
			for(int j = 0; j<8; j++){
				if(this.GBoard[i][j] != null){
					ArrayList<String> newML = new ArrayList<String>();
					this.GBoard[i][j].setMoves(newML);
					this.GBoard[i][j].createMoveSet(this);
				}
			}
		}
	}
}