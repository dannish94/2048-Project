package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Controller.Point;

public class Tile {

	public static final int Width = 80;
	public static final int Height = 80;
	public static final int A_Width = 15;
	public static final int A_Height = 15;
	public static final int Speed = 20;
	
	private int value; 
	private BufferedImage tileImg;
	private Color bg;
	private Color txt;
	private Font font;
	private int x;
	private int y;
	private Point slide;
	
	private boolean combine = true;
	
	public Tile(int value, int x, int y){
		this.value = value;
		this.x = x;
		this.y = y;
		slide = new Point(x,y);
		tileImg = new BufferedImage(Width,Height, BufferedImage.TYPE_INT_ARGB);
		drawImg();
	}
	private void drawImg(){
		Graphics2D g = (Graphics2D)tileImg.getGraphics();
		if(value == 2){
			bg = new Color (0xe9e9e9);
			txt = new Color(0x000000);
		}
		else if (value == 4){
			bg = new Color(0xe9e9e9);
			txt = new Color(0x000000);
		}
		else if (value == 8){
			bg = new Color(0xe9e9e9);
			txt = new Color(0x000000);
		}
		else if (value == 16){
			bg = new Color(0xe9e9e9);
			txt = new Color(0x000000);
		}
		else if (value == 32){
			bg = new Color(0xe9e9e9);
			txt = new Color(0x000000);
		}
		else if (value == 64){
			bg = new Color(0xe9e9e9);
			txt = new Color(0x000000);
		}
		else if (value == 128){
			bg = new Color(0xe9e9e9);
			txt = new Color(0x000000);
		}
		else if (value == 256){
			bg = new Color(0xe9e9e9);
			txt = new Color(0x000000);
		}
		else if (value == 512){
			bg = new Color(0xe9e9e9);
			txt = new Color(0x000000);
		}
		else if (value == 1024){
			bg = new Color(0xe9e9e9);
			txt = new Color(0x000000);
		}
		else if (value == 2048){
			bg = new Color(0xe9e9e9);
			txt = new Color(0x000000);
		}
		else{
			bg = Color.BLACK;
			txt = Color.WHITE;
		}
		
		g.setColor(new Color(0,0,0,0));
		g.fillRect(0, 0, Width, Height);
		g.setColor(bg);
		g.fillRoundRect(0, 0, Width, Height, A_Width, A_Height);
		g.setColor(txt);
		
		if(value <= 64){
			font = Game.main.deriveFont(36f);
		}
		else{
			font = Game.main;
		}
		g.setFont(font);
		
			int drawX = Width / 2 - DrawUtils.getMsgWidth(""+value, font, g) / 2;
			int drawY = Width / 2 - DrawUtils.getMsgHeight(""+value, font, g) / 2;
			g.drawString("" + value, drawX, drawY);
			g.dispose();
		}
		
		public int getValue(){
			return value;
		}
		public void setValue(int value){
			this.value = value;
			drawImg();
		}
		public void update(){
			
		}
		public void render(Graphics2D g){
		g.drawImage(tileImg, x, y, null);
		}
		public boolean isCombine() {
			return combine;
		}
		public void setCombine(boolean combine) {
			this.combine = combine;
		}
		public Point getSlide() {
			return slide;
		}
		public void setSlide(Point slide) {
			this.slide = slide;
		}
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		
}
