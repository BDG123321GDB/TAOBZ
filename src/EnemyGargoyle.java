import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class EnemyGargoyle extends Character {
	final float DEFAULT_SPEED = 150;
	final int FIELD_OF_VIEW = 300;
	float spawnX, spawnY;
	double projDx, projDy, projDist;
	Projectile proj;
	
	public EnemyGargoyle(float x, float y, BufferedImage charset, int charsetX, int charsetY) {
		super(x, y, charset, charsetX, charsetY, 71, 84, 6, 425, 168);
		spawnX = x;
		spawnY = y;
		speed = DEFAULT_SPEED;
	}

	@Override
	public void selfSimulates(long diffTime){
		oldX = x;
		oldY = y;
		
		
		double dx = CanvasGame.billy.x - x;
		double dy = CanvasGame.billy.y - y;
		double dist = Math.hypot(dx,dy);
		
		double spawnDx = spawnX - x;
		double spawnDy = spawnY - y;
		double spawnDist = Math.hypot(spawnDx, spawnDy);
		
//		if(CanvasGame.FIRE) {
//			if(CanvasGame.instance.projectilesList.get(0) != null) {
//				proj = CanvasGame.instance.projectilesList.get(0);
//			}
//		}
		
		if(proj != null) {
			projDx = proj.x - x;
			projDy = proj.y - y;
			projDist = Math.hypot(projDx, projDy);
		}
		
		double velX = speed * diffTime / 1000.0f;
		double velY = speed * diffTime / 1000.0f;
		
		if(!this.isStunned && !this.isEating) {
			if(dist <= FIELD_OF_VIEW) {					// her�i dentro do campo de vis�o, in�cio da persegui��o
				x += velX * dx / dist;
				y += velY * dy / dist;
				if(dx >= 0) {
					animation = 0;
					moveDirection = 1;
				} else if(dx < 0) {
					animation = 1;
					moveDirection = -1;
				}
			} else {
				if(spawnDist >= 1) {					// her�i saiu do campo de vis�o, in�cio da volta para spawn point
					x += velX * spawnDx / spawnDist;
					y += velY * spawnDy / spawnDist;
					if((x - spawnX) >= 0) {
						animation = 1;
					} else {
						animation = 0;
					}
				} 
				if(spawnDist < 1) {						// this est� no spawn point
					x = spawnX;
					y = spawnY;
					if(moveDirection == 1) {
						animation = 0;
					} else if(moveDirection == -1) {
						animation = 1;
					}
				}
			}
		}
		if(this.isStunned) {
			System.out.println("Bones");
			y += gravity * diffTime / 1000.0f;
			countTime += diffTime;
			animeSpeed = 300;
			if(moveDirection == 1) {
				animation = 2;
			} else if(moveDirection == -1) {
				animation = 3;
			}
			if(countTime >= 5000) {
				isStunned = false;
				animeSpeed = 100;
				speed = DEFAULT_SPEED;
				countTime = 0;
			}
		}
		
		if(this.isEating){
			x += velX * projDx / projDist;
			y += velY * projDy / projDist;
			
			if(projDx >= 0) {
				animation = 0;
				moveDirection = 1;
			} else if(projDx < 0) {
				animation = 1;
				moveDirection = -1;
			}
			countTime += diffTime;
			animeSpeed = 300;
			if(moveDirection == 1) {
				animation = 2;
			} else if(moveDirection == -1) {
				animation = 3;
			}
			if(countTime >= 5000) {
				proj.active = false;
				isEating = false;
				animeSpeed = 100;
				speed = DEFAULT_SPEED;
				countTime = 0;
			}
		}
		
		if(hasCollidedWithLayer1((int)((x+15)/16), (int)((x+35)/16), (int)((y+75)/16))) {
			y = oldY;
			onTheFloor = true;
		} else {
			onTheFloor = false;
		}
		
		if(hasCollidedWithLayer1((int)((x+5)/16), (int)((x+70)/16), (int)((y+50)/16))) {
			x = oldX;
		}
		
		if(x < 0) x = oldX;
		if(y < 0) y = oldY;
		if(x >= (CanvasGame.map.Largura << 4) - this.frameWidth+1) x = oldX;
		if(y >= (CanvasGame.map.Altura << 4) - this.frameHeight+1) y = oldY;
		
		super.selfSimulates(diffTime);
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		super.selfDraws(dbg, mapX, mapY);
	}
	
	@Override
	public void hitByProjectile(Projectile p) {
		super.hitByProjectile(p);
		proj = p;
	}
}