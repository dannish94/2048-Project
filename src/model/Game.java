package model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import Controller.Keyboard;
import view.GameBoard;

public class Game extends JPanel implements KeyListener, Runnable{

	public static final int Height = 550;
	public static final int Width = 420;
	private static final long serialVersionUID = 1L;
	public static final Font main = new Font ("Times New Roman", Font.PLAIN, 26);
	private Thread game;
	private boolean loadGame;
	private BufferedImage image = new BufferedImage(Width,Height, BufferedImage.TYPE_INT_RGB);
	
	private boolean set;
	private long beginTime;
	private long timeElapsed;
	private GameBoard board;
	
	public Game(){
		setFocusable(true);
		setPreferredSize(new Dimension(Width,Height));
		addKeyListener(this);
		
		board = new GameBoard(Width / 2 - GameBoard.Board_Width / 2, Height - GameBoard.Board_Height - 10);
	}
	
	private void update(){
		if(Keyboard.pressed[KeyEvent.VK_SPACE])
			System.out.println("hitspace");
		board.update();
		Keyboard.update();
	}
	
	//Create graphics for game
	private void render(){
		Graphics2D a = (Graphics2D) image.getGraphics();
		a.setColor(Color.lightGray);
		a.fillRect(0,0, Width, Height);
		board.render(a);
		a.dispose();
		Graphics2D a2 = (Graphics2D) getGraphics();
		a2.drawImage(image, 0, 0, null);
		a2.dispose();
		
	}
	
	public void run() {
		final int FPS = 60;
		double nsPerUpdate = 1000000000.0 / FPS;
		
		long milliSecs = System.currentTimeMillis();
		int frames = 0, totalUpdates = 0;

		
		double UpdateTime = System.nanoTime();
		double updatesNeeded = 0;

		while (loadGame) {

			boolean doRender = false;
			double currentTime = System.nanoTime();
			updatesNeeded += (currentTime - UpdateTime) / nsPerUpdate;
			UpdateTime = currentTime ;

			
			while (updatesNeeded >= 1) {
				totalUpdates++;
				update();
				updatesNeeded--;
				doRender = true;
			}

			
			if (doRender) {
				frames++;
				render();
				doRender = false;
			} else {
				try {
					Thread.sleep(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// FPS Timer
		if (System.currentTimeMillis() - milliSecs > 1000) {
			System.out.printf("Total game updates: %d, Total frames per second: %d", totalUpdates, frames);
			System.out.println();
			frames = 0;
			totalUpdates = 0;
			milliSecs += 1000;
		}
	}
	
	public synchronized void start(){
		if(loadGame)return;
		loadGame = true;
		game = new Thread(this,"game");
		game.start();
	}

	public synchronized void stop(){
		if(!loadGame)return;
		loadGame = false;
		System.exit(0);
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		Keyboard.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		Keyboard.keyReleased(e);
	}


}
