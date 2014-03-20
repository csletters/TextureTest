package com.example.texturetest;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
	
	
	MyRenderer renderer;
    public MyGLSurfaceView(Context context){
        super(context);

        // Set the Renderer for drawing on the GLSurfaceView
        setEGLContextClientVersion(2);
        renderer = new MyRenderer(context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }
    
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.
    	float x = e.getX();
    	float y = e.getY();
        switch (e.getAction()) {
        case MotionEvent.ACTION_DOWN:
        	
        	if(y > (renderer.heightView*3)/4)
        		renderer.updateTranslation(0, 1);
        	else if(y < renderer.heightView/4)
        		renderer.updateTranslation(0, -1);
        	else if (x < renderer.widthView/2)
        	{
        		renderer.updateTranslation(-1, 0);
        	}
        	else
        	{
        		renderer.updateTranslation(1, 0);
        	}
            requestRender();
    }
        return true;
    }
}
