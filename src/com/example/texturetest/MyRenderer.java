package com.example.texturetest;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.Matrix;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;

public class MyRenderer implements Renderer {

	Cube myCube;
	private final Context mActivityContext;
	float[] projection = new float[16];
	float[] view = new float[16];
	float[] model = new float[16];
	float[] mv = new float[16];
	float[] MVP = new float[16];
	float[] rotMatrix = new float[16];
	float[] transMatrix = new float[16];
	
	int widthView,heightView;
	
	public MyRenderer(final Context activityContext)
	{
		mActivityContext = activityContext;
	}
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		 //myTriangle.draw(MVP);
		 //mySquare.draw(MVP);
		long time = SystemClock.uptimeMillis() % 4000L;
		float angle = 0.090f * ((int) time);
		Matrix.setRotateM(rotMatrix, 0, angle, 0.0f, 1.0f, 0.0f);
		Matrix.multiplyMM(model, 0, rotMatrix, 0, model, 0);
		Matrix.multiplyMM(model, 0, transMatrix, 0, model, 0);
		Matrix.multiplyMM(mv, 0, view, 0, model, 0);
		Matrix.multiplyMM(MVP,0,projection,0,mv,0);
		Matrix.setIdentityM(model, 0);
		myCube.draw(MVP,mv);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		GLES20.glViewport(0, 0, width, height);
		float ratio = (float)width/height;
		Matrix.perspectiveM(projection, 0, 90, ratio, 1, 1000);
		Matrix.setLookAtM(view, 0, 0, 5, 10, 0, 0, 0, 0, 1, 0);
		Matrix.setIdentityM(model, 0);
		Matrix.multiplyMM(mv, 0, view, 0, model, 0);
		Matrix.multiplyMM(MVP,0,projection,0,mv,0);
		Matrix.setIdentityM(transMatrix, 0);
		widthView = width;
		heightView = height;
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		 GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		 GLES20.glClearDepthf(1.0f);  
		 GLES20.glEnable( GLES20.GL_DEPTH_TEST );
		 GLES20.glDepthFunc( GLES20.GL_LESS);
		 GLES20.glDepthMask( true );
		 //myTriangle = new Triangle();
		 //mySquare = new Square();
		 myCube = new Cube(mActivityContext);
	}
	
	public void updateTranslation(float xcord, float ycord)
	{
		Matrix.translateM(transMatrix, 0, xcord, 0, ycord);
	}


	
}
