package br.com.six2six.compass.app;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

	private ImageView pointerView;
	private TextView angleView;
	
	private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    private float[] gravity;
    private float[] geomagnetic;

    private float[] rotationMatrix = new float[9];
    private float[] inclinationMatrix = new float[9];
    private float[] orientation = new float[3];
    
    private float previousDegree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	    
	    pointerView = (ImageView) findViewById(R.id.pointer);
	    angleView = (TextView) findViewById(R.id.angle);
	}
	
	protected void onResume() {
	    super.onResume();
	    sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
	}
	 
	protected void onPause() {
	    super.onPause();
	    sensorManager.unregisterListener(this, accelerometer);
        sensorManager.unregisterListener(this, magnetometer);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			gravity = event.values.clone();
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
        	geomagnetic = event.values.clone();
        }
		
		if (gravity != null && geomagnetic != null) {
			SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, gravity, geomagnetic);
			SensorManager.getOrientation(rotationMatrix, orientation);
			
			float degree = (float) Math.toDegrees(-orientation[0])*100/100;
			
			angleView.setText(String.format("%.0f\u00b0", to360Degrees(degree)));
	
			RotateAnimation rotation = new RotateAnimation(
					previousDegree, adjustSignChange(degree, previousDegree),
					Animation.RELATIVE_TO_SELF, 0.5f, 
					Animation.RELATIVE_TO_SELF, 0.5f);
	
			rotation.setFillAfter(true);
			pointerView.startAnimation(rotation);
	
			previousDegree = degree;
		}
	}

	private float to360Degrees(float degree) {
		return degree < 0 ? 360f + degree : degree;
	}

	private float adjustSignChange(float value, float previousValue) {
		float adjustedValue = value;
		if (value > 90f && previousValue < -90f) {
			adjustedValue -= 360f;
		} else if (value < -90 && previousValue > 90f) {
			adjustedValue += 360f;
		} 
		return adjustedValue;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
