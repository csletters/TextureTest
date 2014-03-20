package com.example.texturetest;

import android.opengl.GLES20;

public class Glshader {

	//loads shader
	public static int load(int type, String shaderCode)
	{
		int shader = GLES20.glCreateShader(type);
		
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		
		return shader;
	}
	
	public static int createProgram(int vertexShader, int fragmentShader)
	{
		 int program = GLES20.glCreateProgram();             // create empty OpenGL ES Program
		 GLES20.glAttachShader(program, vertexShader);   // add the vertex shader to program
		 GLES20.glAttachShader(program, fragmentShader); // add the fragment shader to program
		 GLES20.glLinkProgram(program);
		return program;
	}
}
