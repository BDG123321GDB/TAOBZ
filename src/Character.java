import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Character extends Sprite {
	BufferedImage charset = null;
	int frame = 0;
	int animation = 0;
	int animeSpeed = 100;
	int timeAnimating = 0;
	int charsetX = 0;
	int charsetY = 0;
	int fireTimer = 0;
	int moveDirection = 1;
	int frameWidth;
	int frameHeight;
	int charsetWidth;
	int charsetHeight;
	int numberOfFrames;
	float speed;
	
	int gravity = 500;
	boolean onTheFloor = false;
	boolean hasJumped = false;
	boolean isAlive = true;
	int jumpSpeed = 1100;
	
	float radius = 24;
	
	int centerX;
	int centerY;
	
	float oldX = 0;
	float oldY = 0;
	
	boolean isStunned = false;
	boolean isEating = false;
	boolean isFollowing = false;
	int countTime = 0;
	public long respawnCountTime;
	
	public Character(float x, float y, BufferedImage charset, int charsetX, int charsetY, int frameWidth, 
						int frameHeight, int numberOfFrames) {
		this.x = x;
		this.y = y;
		this.charset = charset;
		this.charsetX = charsetX;
		this.charsetY = charsetY;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.charsetWidth = charset.getWidth();
		this.charsetHeight = charset.getHeight();
		this.numberOfFrames = numberOfFrames;
		this.centerX = frameWidth / 2;
		this.centerY = (frameHeight / 2) + 10;
	}
	
	@Override
	public void selfSimulates(long diffTime){
		fireTimer += diffTime;
		timeAnimating += diffTime;
		frame = (timeAnimating / animeSpeed) % numberOfFrames;
		
		int blockX = (int)((x+(frameWidth)/2)/16);
		int blockY = (int)((y+frameHeight)/16);
		
		if(CanvasGame.map.mapLayer2[blockY][blockX]>0) {
			double ang = Math.atan2(100, 1);
			ang+=Math.PI;
			for(int j = 0; j < 20; j++){
				double ang2 = ang - (Math.PI/4)+((Math.PI/2)*Math.random());
				float vel = (float)(100+100*Math.random());
				float vx = (float)(Math.cos(ang2)*vel);
				float vy = (float)(Math.sin(ang2)*vel);
				CanvasGame.effectsList.add(new Effect(x+26, y+40, vx, vy, 900, 255, 0, 0));
			}
			isAlive = false;
		}
		
		if(!onTheFloor) {
			gravity = 500;
		} else {
			gravity = 0;
		}
	}

	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		dbg.drawImage(charset, (int)(x-mapX), (int)(y-mapY), (int)((x+frameWidth)-mapX), (int)((y+frameHeight)-mapY),
				(frameWidth*frame+charsetX*charsetWidth), (frameHeight*animation+charsetY*charsetHeight),
				(frameWidth*frame+frameWidth+charsetX*charsetWidth), (frameHeight*animation+frameHeight+charsetY*charsetHeight), null);
		
//		dbg.setColor(Color.RED);
//		if(getBounds() != null)	dbg.draw(getBounds());
//		dbg.drawOval((int)(x-CanvasGame.map.MapX-radius+centerX), (int)(y-CanvasGame.map.MapY-radius+centerY), (int)(radius*2),(int)(radius*2));
	}
	
	
	public boolean floorCollision(int bxi, int bxm, int bxf, int byi, int bym, int byf) {
		if((CanvasGame.map.mapLayer1[byi][bxi]>0) || (CanvasGame.map.mapLayer1[byi][bxm]>0) || (CanvasGame.map.mapLayer1[byi][bxf]>0) ||
		   (CanvasGame.map.mapLayer1[bym][bxi]>0) || (CanvasGame.map.mapLayer1[bym][bxm]>0) || (CanvasGame.map.mapLayer1[bym][bxf]>0) ||
		   (CanvasGame.map.mapLayer1[byf][bxi]>0) || (CanvasGame.map.mapLayer1[byf][bxm]>0) || (CanvasGame.map.mapLayer1[byf][bxf]>0)) {
			onTheFloor = true;
			return true;
		}
		return false;
	}
	
	public boolean lateralCollision(int bxi, int bxf, int byi, int bym, int byf) {
		if((CanvasGame.map.mapLayer1[byi][bxi]>0) || (CanvasGame.map.mapLayer1[byi][bxf]>0) || (CanvasGame.map.mapLayer1[bym][bxi]>0) ||
		   (CanvasGame.map.mapLayer1[bym][bxf]>0) || (CanvasGame.map.mapLayer1[byf][bxi]>0) || (CanvasGame.map.mapLayer1[byf][bxf]>0)) {
			return true;
		}
		return false;
	}
	
	public boolean topCollision(int bxi, int bxm, int bxf, int byi, int byf) {
		if((CanvasGame.map.mapLayer1[byi][bxi]>0) || (CanvasGame.map.mapLayer1[byi][bxm]>0) || (CanvasGame.map.mapLayer1[byi][bxf]>0) ||
		   (CanvasGame.map.mapLayer1[byf][bxi]>0) || (CanvasGame.map.mapLayer1[byf][bxm]>0) || (CanvasGame.map.mapLayer1[byf][bxf]>0)) {
			return true;
		}
		return false;
	}
	
	public Rectangle getBounds() {
		return null;
	}
	
	public void hitByProjectile(Projectile p) {
		if(p.getClass() == ProjMeat.class){
			isFollowing = true;
		}
		if(p.getClass() == ProjBone.class){
			isStunned = true;
			speed = 0;
		}
	}

	public void respawn() {	}
}