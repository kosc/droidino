package ru.tuluk.kosc.droidino;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;

public class MainActivity extends Activity {
	int width;
	static float volume;
	AudioManager audioManager;
	MediaPlayer mp;
	static SoundPool sp;
	SparseIntArray soundsMap;
	GraphicsView gv;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		gv = new GraphicsView(this);
		setContentView(gv);
		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundsMap = new SparseIntArray();
        soundsMap.append(1, sp.load(this, R.raw.note_do, 1));
        soundsMap.append(2, sp.load(this, R.raw.note_re, 1));
        soundsMap.append(3, sp.load(this, R.raw.note_mi, 1));
        soundsMap.append(4, sp.load(this, R.raw.note_fa, 1));
        soundsMap.append(5, sp.load(this, R.raw.note_sol, 1));
        soundsMap.append(6, sp.load(this, R.raw.note_la, 1));
        soundsMap.append(7, sp.load(this, R.raw.note_si, 1));
        soundsMap.append(8, sp.load(this, R.raw.second_do, 1));
        soundsMap.append(9, sp.load(this, R.raw.second_re, 1));
        soundsMap.append(10, sp.load(this, R.raw.second_mi, 1));
        soundsMap.append(11, sp.load(this, R.raw.second_fa, 1));
        soundsMap.append(12, sp.load(this, R.raw.second_sol, 1));
        soundsMap.append(13, sp.load(this, R.raw.second_la, 1));
        soundsMap.append(14, sp.load(this, R.raw.second_si, 1));
        // dies
        soundsMap.append(15, sp.load(this, R.raw.do_dies, 1));
        soundsMap.append(16, sp.load(this, R.raw.re_dies, 1));
        soundsMap.append(17, sp.load(this, R.raw.fa_dies, 1));
        soundsMap.append(18, sp.load(this, R.raw.sol_dies, 1));
        soundsMap.append(19, sp.load(this, R.raw.la_dies, 1));
        soundsMap.append(20, sp.load(this, R.raw.second_dies_do, 1));
        soundsMap.append(21, sp.load(this, R.raw.second_dies_re, 1));
        soundsMap.append(22, sp.load(this, R.raw.second_dies_fa, 1));
        soundsMap.append(23, sp.load(this, R.raw.second_dies_sol, 1));
        soundsMap.append(24, sp.load(this, R.raw.second_dies_la, 1));
        // dies end
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actualVolume / maxVolume;
	}
	
	static public class GraphicsView extends View implements OnTouchListener{
		Paint black;
		int keyWidth, touchX, touchY, sound, width, height;
		public GraphicsView(Context context) {
			super(context);
			black = new Paint();
			black.setColor(Color.BLACK);
			this.setOnTouchListener(this);
		}
		@Override
		public boolean onTouch(View view, MotionEvent event){
			if(event.getAction() == MotionEvent.ACTION_UP){
				sound = 0;
				// invalidate();
				return true;
			}
			touchX = (int)event.getX();
			touchY = (int)event.getY();
			keyWidth = view.getWidth()/14;
			sound = 0;
			// black keys
			if(touchY < 0.67*height){
				int zone = (touchX - (int)(0.5*keyWidth)) / keyWidth + 1;
				if((touchX > zone*keyWidth - 0.25*keyWidth) && (touchX < zone*keyWidth + 0.25*keyWidth)){
					if(zone != 3 && zone != 7 && zone != 10){
						sound = zone + 14;
						if(sound > 16) sound--;
						if(sound > 19) sound--;
						if(sound > 21) sound--;
					}
				}
			}
			// white keys
			if(sound == 0){
				keyWidth = view.getWidth()/14;
				sound = touchX/keyWidth + 1;
			}
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				Log.d("Sound", sound + "");
				MainActivity.sp.play(sound, volume, volume, 1, 0, 1f);
			}
			// invalidate();
			return true;
		}
		@Override
		public void onDraw(Canvas canvas){
			height = canvas.getHeight();
			width = canvas.getWidth();
			keyWidth = width/14;
			for(int i=1; i<14; i++){
				canvas.drawLine(i*keyWidth, 0, i*keyWidth, height, black);
				// dies
				if(i != 3 && i != 7 && i != 10){
					canvas.drawRect((float)(i-1)*keyWidth + (float)0.5*keyWidth + (float)0.25*keyWidth, 
							        (float)0, 
							        (float)i*keyWidth + (float)0.25*keyWidth, 
							        (float)0.67*height, black);
				}
			}
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
