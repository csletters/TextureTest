package com.example.texturetest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.Context;
import android.opengl.GLES20;

public class Cube {

	private final String vertexShaderCode = 
			"uniform mat4 mvp;"+
			"uniform mat4 mv;" +
			"attribute vec2 aTexCord;"+
			"attribute vec4 aPosition;" +
			"attribute vec4 acolor;"+
			"attribute vec3 aNormal;"+
			"varying vec3 vPosition;"+
			"varying vec3 vNormal;"+
			"varying vec4 vcolor;" +
			"varying vec2 vTexCord;"+
			"void main() {"	+
			"vTexCord = aTexCord;"+
			"vPosition = vec3(mv * aPosition);"+
			"vNormal = vec3(mv * vec4(aNormal, 0.0));"+
			"  gl_Position = mvp *aPosition;" +
			"  vcolor = acolor;" +
			"}";

	private final String fragmentShaderCode = 
			"precision mediump float;"+
			"uniform sampler2D uTexture;"+
			"uniform vec3 u_lightPos;"+
			"uniform vec3 viewpoint;"+
			"varying vec3 vPosition;"+
			"varying vec3 vNormal;"+
			"varying vec4 vcolor;" +
			"varying vec2 vTexCord;"+
			"void main() {"+
			"vec3 lightVector = normalize(u_lightPos - vPosition);"+
			"vec3 viewVector = normalize(viewpoint - vPosition);"+
			"float diffuse = max(dot(vNormal, lightVector), 0.1);"+
			"float specular = 0.1*pow(dot(viewVector,-lightVector),4.0);"+
			"  gl_FragColor = texture2D(uTexture, vTexCord)*vcolor *(specular+ (6.0*diffuse)) + vec3(0.2,0.2,0.2);" +
			"}";

	static float vertexcube[] = {
	-3.0f, 3.0f, 3.0f,
	-3.0f, -3.0f, 3.0f,
	3.0f, -3.0f, 3.0f,
	3.0f, 3.0f, 3.0f,
	-3.0f, 3.0f, -3.0f,
	3.0f, 3.0f, -3.0f,
	-3.0f, -3.0f, -3.0f,
	3.0f,-3.0f, -3.0f };

	short vertexOrder[] = { 0, 1, 2, 0, 2, 3, 4, 0, 3, 4, 3, 5, 4, 6, 7, 4, 7,
			5, 6, 1, 2, 6, 2, 7, 5, 7, 2, 5, 2, 3, 4, 6, 1, 4, 1, 0 };
	
	static float color[] = { 
		0.583f, 0.771f, 0.014f,0.0f,
		0.609f, 0.115f, 0.436f,0.0f,
		0.327f, 0.483f, 0.844f,0.0f,
		0.822f, 0.569f, 0.201f,0.0f,
		0.435f, 0.602f,	0.223f,0.0f,
		0.310f, 0.747f, 0.185f,0.0f,
		0.597f, 0.770f, 0.761f,0.0f,
		0.583f, 0.771f, 0.014f,0.0f};
	
	float normals[] = {
			 -0.33f,0.33f,0.33f,
			 -0.33f,-0.33f,0.33f,
			 0.33f,-0.33f,0.33f,
			 0.33f,0.33f,0.33f,
			 -0.33f,0.33f,-0.33f,
			 0.33f,0.33f,-0.33f,
			 -0.33f,-0.33f,-0.33f,
			 0.33f,-0.33f,-0.33f
	};
	
	float textureCords[] = {
			0.0f,1.0f,
			0.0f,0.0f,
			1.0f,0.0f,
			1.0f,1.0f,
			1.0f,1.0f,
			0.0f,1.0f,
			1.0f,0.0f,
			0.0f,0.0f
	};
	
	private FloatBuffer vertexBuffer, colorBuffer, normalBuffer,texBuffer;
	private ShortBuffer drawlistBuffer;
	int mProgram,mTextureCoordinateHandle,mTextureUniformHandle, positionHandle, colorHandle, mvpHandle,mvHandle,normalHandle,lightHandle, viewHandle, mTextureDataHandle;

	public Cube(final Context activityContext) {

		// buffer for cube vertices
		ByteBuffer buffer = ByteBuffer.allocateDirect(4 * vertexcube.length);
		buffer.order(ByteOrder.nativeOrder());

		vertexBuffer = buffer.asFloatBuffer();
		vertexBuffer.put(vertexcube);
		vertexBuffer.position(0);

		// buffer for cube color
		ByteBuffer cBuffer = ByteBuffer.allocateDirect(4 * color.length);
		cBuffer.order(ByteOrder.nativeOrder());

		colorBuffer = cBuffer.asFloatBuffer();
		colorBuffer.put(color);
		colorBuffer.position(0);
		
		// buffer for cube normals
		ByteBuffer nBuffer = ByteBuffer.allocateDirect(4 * normals.length);
		nBuffer.order(ByteOrder.nativeOrder());

		normalBuffer = cBuffer.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);
		
		//buffer for texture cords
		ByteBuffer tBuffer = ByteBuffer.allocateDirect(4 * textureCords.length);
		tBuffer.order(ByteOrder.nativeOrder());

		texBuffer = tBuffer.asFloatBuffer();
		texBuffer.put(textureCords);
		texBuffer.position(0);

		// buffer for vertex order
		ByteBuffer bufferOrder = ByteBuffer
				.allocateDirect(2 * vertexOrder.length);
		bufferOrder.order(ByteOrder.nativeOrder());

		drawlistBuffer = bufferOrder.asShortBuffer();
		drawlistBuffer.put(vertexOrder);
		drawlistBuffer.position(0);

		// compile shader program
		int vertexShader = Glshader.load(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode);
		int fragmentShader = Glshader.load(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);
		mProgram = Glshader.createProgram(vertexShader, fragmentShader);
		
		//load texture
		mTextureDataHandle = TextureHelper.loadTexture(activityContext, R.drawable.bumpy_bricks_public_domain);

		//attributes
		positionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		colorHandle = GLES20.glGetAttribLocation(mProgram, "acolor");
		normalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
		mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "aTexCord");
		
		//uniforms
		mvpHandle = GLES20.glGetUniformLocation(mProgram, "mvp");
		mvHandle = GLES20.glGetUniformLocation(mProgram, "mv");
		lightHandle = GLES20.glGetUniformLocation(mProgram, "u_lightPos");
		viewHandle =  GLES20.glGetUniformLocation(mProgram, "viewpoint");
		mTextureUniformHandle =GLES20.glGetUniformLocation(mProgram, "uTexture");
	}

	public void draw(float[] mvpMatrix, float[] mv) {
		GLES20.glUseProgram(mProgram);
		
		//position
		GLES20.glEnableVertexAttribArray(positionHandle);
		GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false,
				3 * 4, vertexBuffer);

		//color
		GLES20.glEnableVertexAttribArray(colorHandle);
		GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false,
				4 * 4, colorBuffer);
		
		//normals
		GLES20.glEnableVertexAttribArray(normalHandle);
		GLES20.glVertexAttribPointer(normalHandle, 3, GLES20.GL_FLOAT, false,
				3 * 4, normalBuffer);
		
		//texture
		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false,
				2 * 4, texBuffer);
		
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);   
		
		GLES20.glUniformMatrix4fv(mvpHandle, 1, false, mvpMatrix, 0);
		GLES20.glUniformMatrix4fv(mvHandle, 1, false, mv, 0);
		GLES20.glUniform3f(lightHandle, 0.0f,1.0f,4.0f);
		GLES20.glUniform3f(viewHandle, 0.0f, 5.0f, 10.0f);
		
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, vertexOrder.length,
				GLES20.GL_UNSIGNED_SHORT, drawlistBuffer);
		GLES20.glDisableVertexAttribArray(positionHandle);
		GLES20.glDisableVertexAttribArray(colorHandle);
		GLES20.glDisableVertexAttribArray(normalHandle);
		GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle);
	}

}
