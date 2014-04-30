import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * Chess.java
 * 
 * @author Paul Shi
 *
 */

public class Chess{
	public static void main(String[] args){
		
		Board Chessboard = Controller.createBoard(); //Make the board
		System.out.println("");
		Chessboard.printBoard(); //Print the board and some information about the game
		System.out.println("Welcome to Chess! Input -h for usage or a valid move to start");
		System.out.println("Players: \'w\' = white | \'b\' = black");
		System.out.println("Pieces: \'K\' = King | \'Q\' = Queen | \'R\' = Rook | \'B\' = Bishop | \'N\' = Knight | \'P\' = Pawn");
		System.out.println("");
		boolean whiteTurn = true; //Turn boolean
		
		//User Interface
		
		while(true){
			
			Gamepiece king = new Gamepiece();
			
			//Find the king and determine if checkmate, stalemate or check conditions exist
			
			if(whiteTurn){
				for(int i = 0;i<8;i++){
					for(int j = 0;j<8;j++){
						if(Chessboard.GBoard[i][j] == null){
							continue;
						}
						
						if(Chessboard.GBoard[i][j].getPlayer().equalsIgnoreCase("w") && 
								Chessboard.GBoard[i][j].getPiece().equalsIgnoreCase("k")){
							king = Chessboard.GBoard[i][j];
							break;
						}
					}
				}
				
				if(Chessboard.isCheckmate(king, whiteTurn)){
					System.out.println("Checkmate.");
					System.out.println("Black Wins");
					System.exit(0);
				}
				else if(Chessboard.isStalemate(king, whiteTurn)){
					System.out.println("Stalemate");
					System.out.println("Draw");
					System.exit(0);
				}
				else if(Chessboard.isCheck(king.getPosition(), whiteTurn)){
					System.out.println("White king at " + king.getPosition() + " is in CHECK!");
				}
				else{
					
				}
			}
			else{
				for(int i = 0;i<8;i++){
					for(int j = 0;j<8;j++){
						if(Chessboard.GBoard[i][j] == null){
							continue;
						}
						
						if(Chessboard.GBoard[i][j].getPlayer().equalsIgnoreCase("b") && 
								Chessboard.GBoard[i][j].getPiece().equalsIgnoreCase("k")){
							king = Chessboard.GBoard[i][j];
							break;
						}
					}
				}
				
				if(Chessboard.isCheckmate(king, whiteTurn)){
					System.out.println("Checkmate.");
					System.out.println("White Wins");
					System.exit(0);
				}
				else if(Chessboard.isStalemate(king, whiteTurn)){
					System.out.println("Stalemate");
					System.out.println("Draw");
					System.exit(0);
				}
				else if(Chessboard.isCheck(king.getPosition(), whiteTurn)){
					System.out.println("Black king at " + king.getPosition() + " is in CHECK!");
				}
				else{
					
				}
				
			}
			
			//Turn prompt
			
			if(whiteTurn){
				System.out.println("White's turn, please enter move: ");
			}
			else{
				System.out.println("Black's turn, please enter move: ");
			}
			
			//Take in user input

			Scanner input = new Scanner(System.in);
			String userInput = input.nextLine();
			userInput = userInput.trim();
			userInput = userInput + " ";
			String[] inputTokens = userInput.split(" ");
			
			//If the user only entered one token, it could be help or resign
			
			if(inputTokens.length == 1){
				if(inputTokens[0].equalsIgnoreCase("-h")){
					System.out.println("");
					System.out.println("There are 5 valid inputs at the turn prompt:\n");
					System.out.println("1) <FileRank> <FileRank> e.g. d2 d3 to move the piece at d2 to d3");
					System.out.println("2) <FileRank> <FileRank> \"draw?\" e.g. d2 d3 draw? to propose a draw");
					System.out.println("3) \"resign\" to resign from the game");
					System.out.println("4) -h to see this help menu again");
					System.out.println("5) <FileRank> <FileRank> <Piece> e.g. g7 g8 N to promote a pawn to a unit other than queen");
					System.out.println("");
					continue;
				}
				else if(inputTokens[0].equalsIgnoreCase("resign")){
					if(whiteTurn){
						System.out.println("White Resigns");
						System.out.println("Black Wins");
						System.exit(0);
					}
					else{
						System.out.println("Black Resigns");
						System.out.println("White Wins");
						System.exit(0);
					}
				}
				else{
					System.out.println("Invalid single argument. Please try again or enter -h for usage.");
					continue;
				}
			}
			else if(inputTokens.length == 2){ //If the user entered 2 tokens, it must be a move entry. check if valid then if valid, perform move
				boolean canMove = Controller.isValidMove(inputTokens[0], inputTokens[1], Chessboard, whiteTurn);
				if(canMove == false){
					continue;
				}
				else{
					Chessboard = Controller.processMove(inputTokens[0], inputTokens[1], Chessboard, whiteTurn);
					Chessboard.printBoard();
					whiteTurn = !whiteTurn;
					continue;
				}
			}
			else if(inputTokens.length == 3){ //If user entered 3 tokens, it could be draw proposal or promotion
				if(inputTokens[2].equalsIgnoreCase("draw?")){ 
					if(whiteTurn){
						System.out.println("White has proposed a draw.");
						System.out.println("Black, please enter \"draw\" to accept or any other input to continue the game: ");
						Scanner inputDraw = new Scanner(System.in); 
						String userInputDraw = inputDraw.nextLine();
						userInputDraw = userInputDraw.trim();
						if(userInputDraw.equalsIgnoreCase("draw")){
							System.out.println("Draw");
							System.exit(0);
						}
						else{ //Draw rejected. Perform entered move and continue game
							System.out.println("Black did not accept the draw");
							boolean canMove = Controller.isValidMove(inputTokens[0], inputTokens[1], Chessboard, whiteTurn);
							if(canMove == false){
								continue;
							}
							else{
								Chessboard = Controller.processMove(inputTokens[0], inputTokens[1], Chessboard, whiteTurn);
								Chessboard.printBoard();
								whiteTurn = !whiteTurn;
								continue;
							}
						}
					}
					else{
						System.out.println("Black has proposed a draw.");
						System.out.println("White, please enter \"draw\" to accept or any other input to continue the game: ");
						Scanner inputDraw = new Scanner(System.in);
						String userInputDraw = inputDraw.nextLine();
						userInputDraw = userInputDraw.trim();
						if(userInputDraw.equalsIgnoreCase("draw")){
							System.out.println("Draw");
							System.exit(0);
						}
						else{ //Draw rejected
							System.out.println("White did not accept the draw");
							boolean canMove = Controller.isValidMove(inputTokens[0], inputTokens[1], Chessboard, whiteTurn);
							if(canMove == false){
								continue;
							}
							else{
								Chessboard = Controller.processMove(inputTokens[0], inputTokens[1], Chessboard, whiteTurn);
								Chessboard.printBoard();
								whiteTurn = !whiteTurn;
								continue;
							}
						}
					}
				} // CASE: Promotion
				else if(inputTokens[2].equalsIgnoreCase("R") || 
						inputTokens[2].equalsIgnoreCase("K") ||
						inputTokens[2].equalsIgnoreCase("N") ||
						inputTokens[2].equalsIgnoreCase("B") || 
						inputTokens[2].equalsIgnoreCase("Q")){
					boolean canMove = Controller.isValidMove(inputTokens[0], inputTokens[1], Chessboard, whiteTurn);
					
					int pawnI = Controller.rankToInd(inputTokens[0]);
					int pawnJ = Controller.fileToInd(inputTokens[0]);
					
					//Check if trying to promote to king
					if(inputTokens[2].equalsIgnoreCase("K")){
						System.out.println("Cannot promote to king. Try again.");
						continue;
					}
					
					//If trying to promote a piece that is not pawn, reprompt
					if(!Chessboard.GBoard[pawnI][pawnJ].getPiece().equalsIgnoreCase("P")){
						System.out.println("Piece you are trying to promote is not a pawn. Try again.");
						continue;
					}
					
					//See if the move to the far rank is valid then perform promotion if valid
					if(canMove == false){
						System.out.println("Invalid move entry. Please try again.");
						continue;
					}
					else{
						Chessboard = Controller.processPromo(inputTokens[0], inputTokens[1], Chessboard, whiteTurn, inputTokens[2]);
						Chessboard.printBoard();
						whiteTurn = !whiteTurn;
						continue;
					}
				}
				else{
					System.out.println("Invalid third argument. Please try again or enter -h for usage.");
					continue;	
				}
			}
			else{
				System.out.println("Too many arguments. Please try again or enter -h for usage");
				continue;
			}
		}
		
	}
}