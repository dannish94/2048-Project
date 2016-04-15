package aisolver;

import java.util.Map;

import Controller.Directions;
import model.Game;

//Import enumerator for players

public class MinimaxAI {

	public static Directions nextBestMove(Game game, int depthnumber) throws Exception { 
	Map<String, Object> output = Minimax(game, depthnumber, Players.Player_One);
}
