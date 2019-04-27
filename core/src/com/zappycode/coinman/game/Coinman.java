package com.zappycode.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

//import java.awt.Rectangle;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Coinman extends ApplicationAdapter {
	SpriteBatch batch;

	// first we are setting the background
	Texture background;
	Texture[] man;
	 int manState =0;// for looping the man state
	int pause=0;// we are giving this when we loop the man state then it was very fast moving
	float gravity=0.2f;// this is for jumping how the man fall and related to physics
	float velocity=0;
	int manY=0;
	ArrayList<Integer> coinX =new ArrayList<Integer> (  ); //coin in x
	ArrayList<Integer> coinY=new ArrayList<Integer> (  );// coin in y
	ArrayList<Rectangle> coins=new ArrayList<Rectangle> (  );
	Texture coin;// for image
	int coincount;// for count

	Random random;
	ArrayList<Integer> bombX =new ArrayList<Integer> (  ); //coin in x
	ArrayList<Integer> bombY=new ArrayList<Integer> (  );// coin in y
	ArrayList<Rectangle> bombs=new ArrayList<Rectangle> (  );
	Texture bomb;// for image
	int bombcount;// for count
	Rectangle manRect;
	int score;
	BitmapFont bitmapFont;// for the score the font set
	int gameState=0;// for over the game
	Texture dezzy;//when it is out

	@Override
	public void create () {//this method when game is created
		batch = new SpriteBatch();
		background = new Texture("bg.png");// background image
		// we are setting the man images
		man=new Texture[4];
		man[0]=new Texture ( "frame-1.png" );
		man[1]=new Texture ( "frame-2.png" );
		man[2]=new Texture ( "frame-3.png" );
		man[3]=new Texture ( "frame-4.png" );
		manY=Gdx.graphics.getHeight ()/2;
		coin=new Texture ( "coin.png" );
		bomb=new Texture ( "bomb.png" );
		random=new Random (  );
	//	manRect=new Rectangle (  );
		bitmapFont=new BitmapFont (  );
		bitmapFont.setColor ( Color.DARK_GRAY );// for font of score
		bitmapFont.getData ().setScale ( 10 );// scale of font
		dezzy=new Texture (  "dizzy-1.png");
		//tubeX=

	}
	public void mackcoin(){// for making the many coin
		float height=random.nextFloat ()*Gdx.graphics.getHeight ();
		coinY.add ( (int)height );
		coinX.add ( Gdx.graphics.getWidth () );
	}
	public void makebomb() {// for making the many bomb
		float height = random.nextFloat () * Gdx.graphics.getHeight ();
		bombY.add ( (int) height );
		bombX.add ( Gdx.graphics.getWidth () );
	}

		@Override
		public void render () {//ths when game is running
			batch.begin ();
			batch.draw ( background, 0, 0, Gdx.graphics.getWidth (), Gdx.graphics.getHeight () );// here we are give texture position and height and width
			if (gameState==1){

				// game is live
				// for bomb
				if (bombcount<300){
					bombcount++;
				}else {
					bombcount=0;
					makebomb ();
				}
				bombs.clear ();
				for (int i=0;i<bombX.size ();i++){//postion for coins
					batch.draw ( bomb,bombX.get ( i ),bombY.get ( i ));
					//now we haveto update the coin so that they can appear
					bombX.set ( i,bombX.get ( i )-4 );
					bombs.add ( new Rectangle ( bombX.get ( i ),bombY.get ( i ) ,bomb.getWidth (),bomb.getHeight ()) );

				}

				// for coin
				if (coincount < 100) {
					coincount++;
				} else {
					coincount = 0;
					mackcoin ();
				}
				coins.clear ();
				for (int i = 0; i < coinX.size (); i++) {//postion for coins
					batch.draw ( coin, coinX.get ( i ), coinY.get ( i ) );
					//now we haveto update the coin so that they can appear
					coinX.set ( i, coinX.get ( i ) - 4 );
					coins.add ( new Rectangle ( coinX.get ( i ),coinY.get ( i ) ,coin.getWidth (),coin.getHeight ()) );
				}


				if (Gdx.input.justTouched ()) {// it only true once when we touch the screen and this is for jump
					velocity = -10;

				}


				if (pause < 3) {
					pause++;// after 5 times running it will change the man state
				} else {
					pause = 0;
					if (manState < 3) {// we are seetting the man state like loop through them so that it will showing the all 4 postion
						manState++;
					} else {
						manState = 0;
					}

				}
				velocity = velocity + gravity;
				manY -= velocity;
				if (manY <= 0) {// so that it will  not fall in the down
					manY = 0;
				}
			}
			else  if (gameState==0){

				// waiting or start
				if (Gdx.input.justTouched ()){
					gameState=1;
				}
			}
			else if (gameState==2){

				// gameover
				if (Gdx.input.justTouched ()){
					gameState=1;
					manY=Gdx.graphics.getHeight ()/2;// forstoping in the y axis
					score=0;
					velocity=0;
					coinY.clear ();
					coinX.clear ();
					coins.clear ();
					coincount=0;
					bombY.clear ();
					bombX.clear ();
					bombs.clear ();
					bombcount=0;
				}
			}
				if (gameState==2){
					batch.draw ( dezzy,Gdx.graphics.getWidth () / 2 - man[manState].getWidth () / 2, manY );
				}else {
					batch.draw ( man[manState], Gdx.graphics.getWidth () / 2 - man[manState].getWidth () / 2, manY );// we are setting the person to the screen and centre and -man width
				}manRect=new Rectangle ( Gdx.graphics.getWidth () / 2 - man[manState].getWidth () / 2, manY,man[manState].getWidth (),man[manState].getHeight () );
			// for coin colloide
				for (int i=0;i<coins.size ();i++){
					if (Intersector.overlaps (manRect,coins.get ( i )  )){
							//Gdx.app.log ( "coin","coin" );
						score++;
						coins.remove ( i );// it will not collide so remove
						coinX.remove ( i );// remove
						coinY.remove ( i );
						break;
					}

			}
			// for bombs collide
			for (int i=0;i<bombs.size ();i++){
				if (Intersector.overlaps (manRect,bombs.get ( i )  )){
					Gdx.app.log ( "coin","bomb" );
					gameState=2;//game over
				}

			}
			bitmapFont.draw ( batch,String.valueOf ( score ),100,200 );// for score displaying

				batch.end ();
		}

	@Override
	public void dispose () {//when game is end
		batch.dispose();

	}
}
