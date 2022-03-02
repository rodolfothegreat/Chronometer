package com.rde.apps.chronometer;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainChrono extends  AppCompatActivity implements OnClickListener , OnItemClickListener {
	ArrayList<ChronoLap> arrayOfLaps;
	TimeAdapter adapter;
	Button btnStart;
	Button btnReset;
	TextView bigView;
	TextView smallView;
	private long startTime = 0;
	private long startLap = 0;
	private long elapsedstart = 0;
	private long elapsedlap = 0;
	private Handler handler = new Handler();
	private boolean started = false;
	private String convertTimetoStr(long atime) {
		int secs = (int) (atime / 1000);
		int mins = secs / 60;
		int hours = mins / 60;
		//hours = hours % 60;
		mins = mins % 60;
		secs = secs % 60;
		int msecs = (int)atime % 1000;
		int csecs = msecs / 10;
		return String.format("%02d:%02d:%02d.%02d", hours, mins, secs, csecs);
	}
	
	private Runnable runnable = new Runnable() {
		   @Override
		   public void run() {
		      /* do what you need to do */
		      //foobar();
		      /* and here comes the "trick" */
			  long diftime = SystemClock.elapsedRealtime() - startTime ; 
			  String bigtime = convertTimetoStr(diftime + elapsedstart);
			  diftime = SystemClock.elapsedRealtime() - startLap; 
			  String smalltime = convertTimetoStr(diftime + elapsedlap);
			  bigView.setText(bigtime);
			  smallView.setText(smalltime);
		      handler.postDelayed(this, 10);
		   }
		};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_chrono);
		arrayOfLaps = new ArrayList<ChronoLap>();
		adapter = new TimeAdapter(this, arrayOfLaps);
		ListView listView = (ListView) findViewById(R.id.lvlaps);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		btnStart = (Button) findViewById(R.id.btnStart);
		btnReset = (Button) findViewById(R.id.btnReset);
		
		bigView = (TextView) findViewById(R.id.tvBig);
		smallView = (TextView) findViewById(R.id.tvSmall);
		
		
		btnStart.setOnClickListener(this);
		btnReset.setOnClickListener(this);
		
		btnStart.setText(R.string.Start);
		btnReset.setText(R.string.Reset);
		btnReset.setEnabled(false);
		btnStart.setTextColor(0xff009900);
		correctWidth(bigView, 0.6);
		correctWidth(smallView, 0.35);
		//handler.postDelayed(runnable, 10);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_chrono, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Toast.makeText(this, "Chronometer v2.1\n" + "rodolfothegreat@hotmail.com", Toast.LENGTH_LONG).show();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Resources res = getResources();
		if (v == btnStart) {
			if (btnStart.getText().toString().equals(res.getString(R.string.Start))   ) {
				//correctWidth(bigView, 0.6);
				//correctWidth(smallView, 0.35);
				btnStart.setText(R.string.Stop);
				btnStart.setTextColor(0xff990000);
				btnReset.setText(R.string.Lap);
				btnReset.setEnabled(true);
				started = true;
				startTime = SystemClock.elapsedRealtime();
				startLap = SystemClock.elapsedRealtime();
				
				handler.postDelayed(runnable, 0);
			} else if (btnStart.getText().toString().equals(res.getString(R.string.Stop))   ) {
				btnStart.setText(R.string.Start);
				btnStart.setTextColor(0xff009900);
				btnReset.setText(R.string.Reset);
				btnReset.setEnabled(true);
				started = false;
				//elapsedstart = SystemClock.lapsedRealtime() ;
				//elapsedlap = SystemClock.lapsedRealtime() ;
				elapsedstart = elapsedstart + SystemClock.elapsedRealtime() - startTime;
				elapsedlap = elapsedlap + SystemClock.elapsedRealtime() - startLap;
				handler.removeCallbacks(runnable) ;
			}

			
			
			
		}
		
		if (v == btnReset) {
			if (btnReset.getText().toString().equals(res.getString(R.string.Reset))   ) {
				btnReset.setEnabled(false);
				elapsedlap = 0;
				elapsedstart = 0;
				smallView.setText("00:00:00.00");
				bigView.setText("00:00:00.00");
				adapter.clear();
				
			}
				
			if (btnReset.getText().toString().equals(res.getString(R.string.Lap))   ) {
				startLap = SystemClock.elapsedRealtime();
				elapsedlap = 0;
				ChronoLap aLap = new ChronoLap(smallView.getText().toString(), bigView.getText().toString());
				arrayOfLaps.add(0, aLap);
				adapter.notifyDataSetChanged();
				smallView.setText("00:00:00.00");
				
			}
			
			
		}
		
	}
	

	public void correctWidth(TextView textView, double afactor)
	{
		
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		
        float dpHeight = displayMetrics.heightPixels ;
       //float dpWidth = displayMetrics.widthPixels / displayMetrics.density;		
        float dpWidth = displayMetrics.widthPixels ;	
        float awidth = dpWidth;
        if (awidth > dpHeight) {
        	awidth = dpHeight;
        }
        int desiredWidth =   (int) (afactor *  awidth);
	    Paint paint = new Paint();
	    Rect bounds = new Rect();

	    paint.setTypeface(textView.getTypeface());
	    float textSize = textView.getTextSize();
	    paint.setTextSize(textSize);
	    String text = textView.getText().toString();
	    paint.getTextBounds(text, 0, text.length(), bounds);

	    while (bounds.width() > desiredWidth)
	    {
	        textSize--;
	        paint.setTextSize(textSize);
	        paint.getTextBounds(text, 0, text.length(), bounds);
	    }
	    while (bounds.width() < desiredWidth)
	    {
	        textSize++;
	        paint.setTextSize(textSize);
	        paint.getTextBounds(text, 0, text.length(), bounds);
	    }

	    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
	    
	     
	}	
	
	
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    // Checks the orientation of the screen
	   // if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	   //     Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
	   // } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
	   //     Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
	   // } else 
	   // 	Toast.makeText(this, "else", Toast.LENGTH_SHORT).show();
	}	
	
	@Override
	public void onPause() {
	    super.onPause();  // Always call the superclass method first
	    handler.removeCallbacks(runnable) ;
	    saveAll();
	    
	}

	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    getAll();
	    if (this.started) {
	    	handler.postDelayed(runnable, 0);
	    }
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AlertDialog.Builder builder = new Builder(MainChrono.this);
		builder.setMessage("Are you sure you want to delete Split " +  Integer.toString(adapter.getCount() - position) + "?");
		//builder.setTitle("Confirmation Dialog");
		final int posy = position;
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
                                    // do something after confirm
				arrayOfLaps.remove(posy);
				adapter.notifyDataSetChanged();
			}
		});	

		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});	

		builder.create().show();	
	}
	
	public void saveAll() {
		ChronoLap aLap;
		int arrSize = arrayOfLaps.size();
		SharedPreferences sharedPreferences = this.getPreferences(Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit().clear();
		editor.putBoolean("started", this.started);
		editor.putLong("startTime", this.startTime);
		editor.putLong("startLap", this.startLap);
		editor.putLong("elapsedlap", this.elapsedlap);
		editor.putLong("elapsedstart", this.elapsedstart);
		editor.putInt("arraysize", arrSize );
		editor.putString("bigView", bigView.getText().toString());
		editor.putString("smallView", smallView.getText().toString());
		for( int i = 0; i < arrayOfLaps.size(); i++) {
			 
			aLap = arrayOfLaps.get(i);
			
			editor.putString("key" + Integer.toString(i), aLap.toString());
			
		}
		editor.commit();
		
	}
	
	public void getAll() {
		ChronoLap aLap;
		SharedPreferences sharedPreferences = this.getPreferences(Activity.MODE_PRIVATE);
		this.started = sharedPreferences.getBoolean("started", false);
		this.startTime = sharedPreferences.getLong("startTime", 0);
		this.startLap = sharedPreferences.getLong("startLap", 0);
		this.elapsedstart = sharedPreferences.getLong("elapsedstart", 0);
		this.elapsedlap = sharedPreferences.getLong("elapsedlap", 0);
		int arraysize = sharedPreferences.getInt("arraysize", 0);
		
		arrayOfLaps.clear();
		
		for (int i = 0; i < arraysize; i++) {
			String anitem = sharedPreferences.getString("key" + Integer.toString(i), "");
			if (anitem.length() > 0 ){
				aLap = new ChronoLap(anitem);
				arrayOfLaps.add(aLap);
			}
		}
		
		if (this.started) {
			//correctWidth(smallView, 0.35);
			btnStart.setText(R.string.Stop);
			btnStart.setTextColor(0xff990000);
			btnReset.setText(R.string.Lap);
			btnReset.setEnabled(true);
			
		} else {
			
			btnStart.setText(R.string.Start);
			btnStart.setTextColor(0xff009900);
			btnReset.setText(R.string.Reset);
			btnReset.setEnabled(true);
			smallView.setText(sharedPreferences.getString("smallView", "00:00:00.00"));
			bigView.setText(sharedPreferences.getString("bigView", "00:00:00.00"));
		}
		
		adapter.notifyDataSetChanged();
		
	}
	
	
	
}
