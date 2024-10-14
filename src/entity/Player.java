package entity;

import main.KeyHandler;

// import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Player extends Entity{

  GamePanel gp;
  KeyHandler keyH;

  public Player(GamePanel gp, KeyHandler keyH) {
    this.gp = gp;
    this.keyH = keyH;

    setDefaultValues();
    getPlayerImage();
  }

  public void setDefaultValues() {
    x = 100;
    y = 100;
    speed = 4;
    direction = "down";
  }

  public void getPlayerImage() {
    try {
      idle1 = ImageIO.read(getClass().getResourceAsStream("/res/player/idle1.png"));
      idle2 = ImageIO.read(getClass().getResourceAsStream("/res/player/idle2.png"));
      up1 = ImageIO.read(getClass().getResourceAsStream("/res/player/up1.png"));
      down1 = ImageIO.read(getClass().getResourceAsStream("/res/player/down1.png"));
      left1 = ImageIO.read(getClass().getResourceAsStream("/res/player/left1.png"));
      right1 = ImageIO.read(getClass().getResourceAsStream("/res/player/right1.png"));
      up2 = ImageIO.read(getClass().getResourceAsStream("/res/player/up2.png"));
      down2 = ImageIO.read(getClass().getResourceAsStream("/res/player/down2.png"));
      left2 = ImageIO.read(getClass().getResourceAsStream("/res/player/left2.png"));
      right2 = ImageIO.read(getClass().getResourceAsStream("/res/player/right2.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void update() {

    if(keyH.upPressed == true) {
      direction = "up";
      y -= speed;
    }
    else if(keyH.downPressed == true) {
      direction = "down";
      y += speed;
    }
    else if(keyH.leftPressed == true) {
      direction = "left";
      x-= speed;
    }
    else if(keyH.rightPressed == true) {
      direction = "right";
      x+= speed;
    }
    else {
      direction = "idle";
    }

    spriteCounter++;
    if(spriteCounter > 15) {
      if(spriteNum == 1) {
        spriteNum = 2;
      }
      else if(spriteNum == 2) {
        spriteNum = 1;
      }
      spriteCounter = 0;
    }
  }

  public void draw(Graphics2D g2) {
    // g2.setColor(Color.white);

    // g2.fillRect(x, y, gp.tileSize, gp.tileSize);

    BufferedImage image = null;

    switch(direction) {
      case "up":
        if(spriteNum == 1) {
          image = up1;
        }
        if(spriteNum == 2) {
          image = up2;
        }
        break;
      case "down":
        if(spriteNum == 1) {
          image = down1;
        }
        if(spriteNum == 2) {
          image = down2;
        }
        break;
      case "left":
        if(spriteNum == 1) {
          image = left1;
        }
        if(spriteNum == 2) {
          image = left2;
        }
        break;
      case "right":
        if(spriteNum == 1) {
          image = right1;
        }
        if(spriteNum == 2) {
          image = right2;
        }
        break;
      case "idle":
        if(spriteNum == 1) {
          image = idle1;
        }
        if(spriteNum == 2) {
          image = idle2;
        }
    }
    g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
  }

}
