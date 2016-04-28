package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Random;

import Controller.Directions;
import Controller.Keyboard;
import Controller.Point;
import model.DrawUtils;
import model.Game;
import model.Tile;

public class GameBoard {
	public static final int ROWS = 4;
	public static final int COLS = 4;
	
	private final int startingTiles = 2;
	private Tile [][] board;
	private boolean dead;
	private boolean won;
	private BufferedImage gameBoard;
	private BufferedImage finalBoard;
	private int x;
	private int y;
	
	//Implementing elapsing time for current game, and all time fastest time achieved
	private long elapsedTime;
	private long fastestTime;
	private long startTime;
	
	private boolean hasStarted;
	
	private String formattedTime = "00:00:000";
	
	//Implementing score for the current game, and all time high score achieved
	private int score = 0;
	private int highScore = 0;
	private Font fontScore;
	
	//Creating a file that will store fastest and highest score
	private String saveData;
	private String fileName = "save";
	
	
	private static int SPACING = 5;
	public static int Board_Width = (COLS + 1) * SPACING + COLS * Tile.Width;
	public static int Board_Height = (ROWS + 1) * SPACING + ROWS * Tile.Height;

	//Added file path to store .jar file for recorded in the project path.
	public GameBoard(int x, int y){
		try{
			saveData = GameBoard.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		fontScore = Game.main.deriveFont(22F);
		this.x = x;
		this.y = y;
		board = new Tile[ROWS][COLS];
		gameBoard = new BufferedImage(Board_Width, Board_Height, BufferedImage.TYPE_INT_RGB);
		finalBoard = new BufferedImage(Board_Width, Board_Height, BufferedImage.TYPE_INT_RGB);
		startTime = System.nanoTime();
		loadHS();
		createBoardImage();
		start();
	}
		
		// methods for setting a high score, loading a high score and 
		// then creating a save data for the high score.
		//method that creates save file by using FileWriter
		private void createSaveData(){
			try {
				File f = new File(saveData,fileName);
				FileWriter result = new FileWriter(f);
				BufferedWriter writer = new BufferedWriter(result);
				writer.write("" + 0);
				writer.newLine();
				writer.write("" + Integer.MAX_VALUE);
				writer.close();
			}
			catch(Exception e){
			e.printStackTrace();
			}
		}	
		//method that reads file for high score using buffered reader.
		private void loadHS(){
		try{
			File f2 = new File(saveData,fileName);
			if(!f2.isFile()){
				createSaveData();	
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f2)));
			highScore = Integer.parseInt(r.readLine());
			fastestTime = Long.parseLong(r.readLine());
			r.close();
		}
		catch(Exception e){
		e.printStackTrace();
		}
		}
		private void setHS(){
			
		FileWriter result = null;
		try{
			File f3 = new File(saveData,fileName);
			result = new FileWriter(f3);
			BufferedWriter writer = new BufferedWriter(result);	
			
			
				writer.write("" + highScore);
				writer.newLine();
				if(elapsedTime <= fastestTime && won){
					writer.write("" + elapsedTime);
				}
				else{
					writer.write("" + fastestTime);
				}
			
			writer.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		}
		
	private void createBoardImage(){
		Graphics2D g = (Graphics2D) gameBoard.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Board_Width, Board_Height);
		g.setColor(Color.GRAY);
		
		for(int row = 0; row < ROWS; row++){
			for(int col = 0; col < COLS; col++){
				int x = SPACING + SPACING * col + Tile.Width * col;
				int y = SPACING + SPACING * row + Tile.Width * row;
				g.fillRoundRect(x, y, Tile.Width, Tile.Height, Tile.A_Width, Tile.A_Height);
			}
		}
	}
	public void render(Graphics2D g){
		Graphics2D g2d = (Graphics2D)finalBoard.getGraphics();
		g2d.drawImage(gameBoard, 0, 0, null);
		
		for(int row = 0; row < ROWS; row++){
			for(int col = 0; col < COLS; col++){
				Tile cur = board[row][col];
		if(cur == null) continue;
		cur.render(g2d);
			}
		}
	
		g.drawImage(finalBoard,x,y,null);
		g2d.dispose();
		
		
		g.setColor(Color.white);
		g.setFont(fontScore);
		g.drawString("" + score, 30, 40);
		g.setColor(Color.BLUE);
		g.drawString("Best: "+ highScore, Game.Width - DrawUtils.getMsgWidth("Best: " + highScore, fontScore, g)-20, 35);
		g.setColor(Color.black);
		g.drawString("Time: " + formattedTime, 30, 90);
		g.setColor(Color.red);
		g.drawString("Fastest: " + formatTime(fastestTime), Game.Width - DrawUtils.getMsgWidth("Fastest: " + formatTime(fastestTime), fontScore, g)-20, 90) ;
	}
	

	private void start(){
		for(int i = 0; i < startingTiles; i++){
			spawnRandom();
		}
	}
	private void spawnRandom(){
		Random random = new Random();
		boolean notValid = true;
		
		while(notValid){
			int location = random.nextInt(ROWS * COLS);
			int row = location / ROWS;
			int col = location % COLS;
			Tile current = board[row][col];
			if(current == null){
				int value = random.nextInt(10) < 9 ? 64 : 32;
				Tile tile = new Tile(value, getTileX(col), getTileY(row));
			board[row][col] = tile;
			notValid = false;
			}
		}			
	}
	
	public int getTileX(int col){
		return SPACING + col * Tile.Width + col * SPACING;
	}
	public int getTileY(int row){
		return SPACING + row * Tile.Height + row * SPACING;
	}
	
	public void update(){	
		if(won == true || !dead){
			if(hasStarted){
				elapsedTime = (System.nanoTime() - startTime) / 1000000;
				formattedTime = formatTime(elapsedTime);
			}
			else if(dead == true){
				startTime = System.nanoTime();
			}
		}
		
		
		
		checkKeyboard();
		
		if(score>=highScore){
			highScore = score;
		}
		
		for(int row = 0; row < ROWS; row++){
			for (int col = 0; col < COLS; col++){
				Tile cur = board[row][col];
				if(cur == null) continue;
				cur.update();
				resetPosition(cur,row,col);
				if(cur.getValue() == 2048){
					won = true;
				}
			}
		}
	}
	private void resetPosition(Tile cur, int row, int col){
		if(cur == null) return;
		
		int x = getTileX(col);
		int y = getTileY(row);
		int distX = cur.getX() - x;
		int distY = cur.getY() - y;
		
		if(Math.abs(distX)< Tile.Speed){
			cur.setX(cur.getX()-distX);
		}
		if(Math.abs(distY)< Tile.Speed){
			cur.setY(cur.getY()-distY);
		}
		if(distX < 0){
			cur.setX(cur.getX() + Tile.Speed);
		}
		if(distY < 0){
			cur.setY(cur.getY() + Tile.Speed);
		}
		if(distX > 0){
			cur.setX(cur.getX() - Tile.Speed);
		}
		if(distY > 0){
			cur.setY(cur.getY() - Tile.Speed);
		}
	}

		
	
	private boolean move(int row, int col, int horDir, int verDir, Directions dir){
	boolean canMove = false;
	Tile cur = board[row][col];
	if(cur == null) return false;
	boolean move = true;
	int newCol = col;
	int newRow = row;
	while(move){
		newCol += horDir;
		newRow += verDir;
		if(checkOutOfBounds(dir,newRow,newCol))break;
		if(board[newRow][newCol] == null){
			board[newRow][newCol] = cur;
			board[newRow - verDir][newCol - horDir] = null;
			board[newRow][newCol].setSlide(new Point(newRow, newCol));
			canMove = true;
		}
		else if(board[newRow][newCol].getValue() == cur.getValue() && board[newRow][newCol].isCombine()){
			board[newRow][newCol].setCombine(false);
			board[newRow][newCol].setValue(board[newRow][newCol].getValue() * 2);
			canMove = true;
			board[newRow - verDir][newCol - horDir] = null;
			board[newRow][newCol].setSlide(new Point(newRow, newCol));
			
			score += board[newRow][newCol].getValue();
		}
		else{
			move = false;
		}
	}
	
	return canMove;
	}
	
	private boolean checkOutOfBounds(Directions dir, int row, int col) {
		if (dir == Directions.LEFT){
			return col < 0;
		}
		else if(dir == Directions.RIGHT){
			return col > COLS-1;
		}
		else if(dir == Directions.UP){
			return row < 0;
		}
		else if(dir == Directions.DOWN){
			return row > ROWS-1;
		}	
		return false;
	}
	
	private void moveTiles(Directions dir){
		boolean canMove = false;
		int horDir = 0;
		int verDir = 0;
		
		if(dir == Directions.LEFT){
			horDir = -1;
			for(int row = 0; row < ROWS; row++){
				for(int col = 0; col < COLS; col++){
					if(!canMove){
						canMove = move(row,col,horDir,verDir,dir);
					}
					else move(row,col,horDir,verDir,dir);
					}
			}		
		}
	
		else if(dir == Directions.RIGHT){
			horDir = 1;
			for(int row = 0; row < ROWS; row++){
				for(int col = COLS -1; col >= 0; col--){
					if(!canMove){
						canMove = move(row,col,horDir,verDir,dir);
					}
					else move(row,col,horDir,verDir,dir);
					}
			}		
		}
		else if(dir == Directions.UP){
			verDir = -1;
			for(int row = 0; row < ROWS; row++){
				for(int col = 0; col < COLS; col++){
					if(!canMove){
						canMove = move(row,col,horDir,verDir,dir);
					}
					else move(row,col,horDir,verDir,dir);
					}
			}		
		}
		else if(dir == Directions.DOWN){
			verDir = 1;
			for(int row = ROWS - 1; row >= 0; row--){
				for(int col = 0; col < COLS; col++){
					if(!canMove){
						canMove = move(row,col,horDir,verDir,dir);
					}
					else move(row,col,horDir,verDir,dir);
					}
			}		
		}
		
		else{
			System.out.println(dir + " direction is not valid");
		}
		
		for(int row = 0; row < ROWS; row++){
			for(int col = 0; col < COLS; col++){
				Tile cur = board[row][col];
				if(cur == null) continue;
				cur.setCombine(true);
			}
		}
		if(canMove){
			spawnRandom();
			checkDead();
		}
	}
	
	
	private void checkDead(){
		for(int row = 0; row < ROWS; row++){
			for(int col = 0; col < COLS; col++){
				if(board[row][col] == null) return;
				if(checkSurroundingTiles(row,col,board [row][col])){
					return;
				}
			}
		}
		dead = true;
		setHS();
	}
	 
		
	private boolean checkSurroundingTiles(int row, int col, Tile cur){
		if(row >0){
			Tile check = board[row -1][col];
			if(check == null) return true;
			if(cur.getValue() == check.getValue()) return true;
		}
		if(row < ROWS - 1){
			Tile check = board[row+1][col];
			if(check == null) return true;
			if(cur.getValue() == check.getValue()) return true;
		}
		if(col >0){
			Tile check = board[row][col-1];
			if(check == null) return true;
			if(cur.getValue() == check.getValue()) return true;
		}
		if(col < COLS-1){
			Tile check = board[row][col+1];
			if(check == null) return true;
			if(cur.getValue() == check.getValue()) return true;
		}
	return false;
	}
	
	private void checkKeyboard(){
		if(Keyboard.typed(KeyEvent.VK_LEFT)){
			moveTiles(Directions.LEFT);
			if(!hasStarted)hasStarted = true;
		}
		if(Keyboard.typed(KeyEvent.VK_RIGHT)){
			moveTiles(Directions.RIGHT);
			if(!hasStarted)hasStarted = true;
		}
		if(Keyboard.typed(KeyEvent.VK_UP)){
			moveTiles(Directions.UP);
			if(!hasStarted)hasStarted = true;
		}
		if(Keyboard.typed(KeyEvent.VK_DOWN)){
			moveTiles(Directions.DOWN);
			if(!hasStarted)hasStarted = true;
		}
	}
	private String formatTime(long milliSecs) {
		String formattedTime;
		final int msPerSecond = 1000,
				  msPerMinute = 60000,
				    msPerHour = 3600000;
		String hour = "";
		
		int hours = (int)(milliSecs / msPerHour);
		if(hours >= 1){
			milliSecs -= hours * msPerHour;
			if(hours < 10){
				hour = "0" + hours;
			}
			else{
				hour = "" + hours;
			}
			hour += ":";
		}
		
		String minute;
		int minutes = (int)(milliSecs / msPerMinute);
		if(minutes >= 1){
			milliSecs -= minutes * msPerMinute;
			if(minutes < 10){
				minute= "0" + minutes;
			}
			else{
				minute= "" + minutes;
			}
		}
		else{
			minute= "00";
		}
		
		String second;
		int seconds = (int)(milliSecs / msPerSecond);
		if(seconds >= 1){
			milliSecs -= seconds * msPerSecond;
			if(seconds < 10){
				second= "0" + seconds;
			}
			else{
				second= "" + seconds;
			}
		}
		else{
			second= "00";
		}
		
		String ms;
		if(milliSecs > 99){
			ms = "" + milliSecs;
		}
		else if(milliSecs > 9){
			ms = "0" + milliSecs;
		}
		else{
			ms = "00" + milliSecs;
		}
		
		formattedTime = hour + minute + ":" + second + ":" + ms;
		return formattedTime;
	}
	
}