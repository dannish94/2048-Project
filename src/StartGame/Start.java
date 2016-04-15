package StartGame;

import javax.swing.JFrame;

import model.Game;

public class Start {

	public static void main (String[] args){
		Game game = new Game();
		JFrame display = new JFrame("2048 Game");
		display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.setResizable(false);
		display.add(game);
		display.pack();
		display.setLocationRelativeTo(null);
		display.setVisible(true);
		
		game.start();
	}
}
