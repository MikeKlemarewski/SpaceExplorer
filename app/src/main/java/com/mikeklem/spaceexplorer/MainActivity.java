package com.mikeklem.spaceexplorer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;

import static com.mikeklem.spaceexplorer.GLHelpers.*;

public class MainActivity extends CardboardActivity implements CardboardView.StereoRenderer {

    private static final String TAG = "MainActivity";

    private static final float CAMERA_Z = 0.01f;
    private static final float TIME_DELTA = 0.3f;

    // We keep the light always position just above the user.
    private final float[] mLightPosInWorldSpace = new float[] {0.0f, 2.0f, 0.0f, 1.0f};
    private final float[] mLightPosInEyeSpace = new float[4];

    // Keep a constant speed for the ship's movement and track ship movement changes
    private final float[] mShipMovement = new float[] {0.0f, 0.0f, -0.1f};

    private static final int COORDS_PER_VERTEX = 3;

    private FloatBuffer mCubeVertices;
    private FloatBuffer mCubeColors;
    private FloatBuffer mCubeNormals;

    private int mGlProgram;
    private int mPositionParam;
    private int mNormalParam;
    private int mColorParam;
    private int mModelViewProjectionParam;
    private int mLightPosParam;
    private int mModelViewParam;
    private int mModelParam;
    private int mIsFloorParam;

    private float[] mModelCube;
    private float[] mCamera;
    private float[] mView;
    private float[] mHeadView;
    private float[] mModelViewProjection;
    private float[] mModelView;

    private float[] mModelFloor;

    private float mObjectDistance = 12f;
    private float mFloorDepth = 20f;

    private Vibrator mVibrator;

    private float currentX;
    private float currentY;
    private float currentZ;

    private ArrayList<Cube> cubes = new ArrayList<Cube>();
    private boolean isMoving = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRenderer(this);
        setCardboardView(cardboardView);


        mCamera = new float[16];
        mView = new float[16];
        mModelViewProjection = new float[16];
        mModelView = new float[16];
        mModelFloor = new float[16];
        mHeadView = new float[16];
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        currentX = 0.0f;
        currentY = 0.0f;
        currentZ = 0.0f;

        Cube cube1 = new Cube(0.0f, 0.0f, 0.0f, 1.0f, 5000f);
        Cube cube2 = new Cube(4.0f, 0.0f, 0.0f, 1.0f, 5000f);
        Cube cube3 = new Cube(4.0f, 3.0f, 0.0f, 1.0f, 5000f);
        Cube cube4 = new Cube(2.0f, -6.0f, 0.0f, 1.0f, 5000f);
        Cube cube5 = new Cube(4.0f, 0.0f, -1.0f, 1.0f, 5000f);
        Cube cube6 = new Cube(0.0f, 1.0f, 4.0f, 1.0f, 5000f);
        cubes.add(cube1);
        cubes.add(cube2);
        cubes.add(cube3);
        cubes.add(cube4);
        cubes.add(cube5);
        cubes.add(cube6);

        mModelCube = new float[cubes.size() * 16];
    }

    @Override
    public void onCardboardTrigger() {
        isMoving = !isMoving;
        mVibrator.vibrate(50);

        String message = (isMoving) ? "Engines engaged." : "Engines disengaged.";
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        GLES20.glUseProgram(mGlProgram);

        if( isMoving ) {
            Matrix.setIdentityM(mModelCube, 0);
            Matrix.translateM(mModelCube, 0, 0, 0, -currentZ);
            currentX -= mShipMovement[0];
            currentY -= mShipMovement[1];
            currentZ -= mShipMovement[2];
        }

        mModelViewProjectionParam = GLES20.glGetUniformLocation(mGlProgram, "u_MVP");
        mLightPosParam = GLES20.glGetUniformLocation(mGlProgram, "u_LightPos");
        mModelViewParam = GLES20.glGetUniformLocation(mGlProgram, "u_MVMatrix");
        mModelParam = GLES20.glGetUniformLocation(mGlProgram, "u_Model");
        mIsFloorParam = GLES20.glGetUniformLocation(mGlProgram, "u_IsFloor");

        // Build the Model part of the ModelView matrix.
        Matrix.rotateM(mModelCube, 0, TIME_DELTA, 0.5f, 0.5f, 1.0f);

        // Build the camera matrix and apply it to the ModelView.
        Matrix.setLookAtM(mCamera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        headTransform.getHeadView(mHeadView, 0);

        checkGLError("onReadyToDraw");
    }

    @Override
    public void onSurfaceCreated(EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f); // Dark background so text shows up well

        float[] cubeCoords = getCubeCoordinates(cubes);
        ByteBuffer bbVertices = ByteBuffer.allocateDirect(cubeCoords.length * 4);
        bbVertices.order(ByteOrder.nativeOrder());
        mCubeVertices = bbVertices.asFloatBuffer();
        mCubeVertices.put(cubeCoords);
        mCubeVertices.position(0);

        float[] cubeColors = getCubeColors(cubes);

        ByteBuffer bbColors = ByteBuffer.allocateDirect(cubeColors.length * 4);
        bbColors.order(ByteOrder.nativeOrder());
        mCubeColors = bbColors.asFloatBuffer();
        mCubeColors.put(cubeColors);
        mCubeColors.position(0);

        float[] cubeNormals = getCubeNormals(cubes);

        ByteBuffer bbNormals = ByteBuffer.allocateDirect(cubeNormals.length * 4);
        bbNormals.order(ByteOrder.nativeOrder());
        mCubeNormals = bbNormals.asFloatBuffer();
        mCubeNormals.put(cubeNormals);
        mCubeNormals.position(0);

        int vertexShader = loadGLShader(this, GLES20.GL_VERTEX_SHADER, R.raw.light_vertex);
        int gridShader = loadGLShader(this, GLES20.GL_FRAGMENT_SHADER, R.raw.grid_fragment);

        mGlProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mGlProgram, vertexShader);
        GLES20.glAttachShader(mGlProgram, gridShader);
        GLES20.glLinkProgram(mGlProgram);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);

        // Object first appears directly in front of user
        Matrix.setIdentityM(mModelCube, 0);
        Matrix.translateM(mModelCube, 0, 0, 0, -mObjectDistance);

        Matrix.setIdentityM(mModelFloor, 0);
        Matrix.translateM(mModelFloor, 0, 0, -mFloorDepth, 0); // Floor appears below user

        checkGLError("onSurfaceCreated");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged");
    }

    @Override
    public void onDrawEye(EyeTransform transform) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        mPositionParam = GLES20.glGetAttribLocation(mGlProgram, "a_Position");
        mNormalParam = GLES20.glGetAttribLocation(mGlProgram, "a_Normal");
        mColorParam = GLES20.glGetAttribLocation(mGlProgram, "a_Color");

        GLES20.glEnableVertexAttribArray(mPositionParam);
        GLES20.glEnableVertexAttribArray(mNormalParam);
        GLES20.glEnableVertexAttribArray(mColorParam);
        checkGLError("mColorParam");

        // Apply the eye transformation to the camera.
        Matrix.multiplyMM(mView, 0, transform.getEyeView(), 0, mCamera, 0);

        // Set the position of the light
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mView, 0, mLightPosInWorldSpace, 0);
        GLES20.glUniform3f(mLightPosParam, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1],
                mLightPosInEyeSpace[2]);

        // Build the ModelView and ModelViewProjection matrices
        // for calculating cube position and light.
        Matrix.multiplyMM(mModelView, 0, mView, 0, mModelCube, 0);
        Matrix.multiplyMM(mModelViewProjection, 0, transform.getPerspective(), 0, mModelView, 0);
        drawCubes();

        // Set mModelView for the floor, so we draw floor in the correct location
        Matrix.multiplyMM(mModelView, 0, mView, 0, mModelFloor, 0);
        Matrix.multiplyMM(mModelViewProjection, 0, transform.getPerspective(), 0,
                mModelView, 0);
    }

    /**
     * Draw the cube. We've set all of our transformation matrices. Now we simply pass them into
     * the shader.
     */
    public void drawCubes() {
        // This is not the floor!
        GLES20.glUniform1f(mIsFloorParam, 0f);

        // Set the Model in the shader, used to calculate lighting
        GLES20.glUniformMatrix4fv(mModelParam, 1, false, mModelCube, 0);

        // Set the ModelView in the shader, used to calculate lighting
        GLES20.glUniformMatrix4fv(mModelViewParam, 1, false, mModelView, 0);

        // Set the position of the cube
        GLES20.glVertexAttribPointer(mPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT,
                false, 0, mCubeVertices);

        // Set the ModelViewProjection matrix in the shader.
        GLES20.glUniformMatrix4fv(mModelViewProjectionParam, 1, false, mModelViewProjection, 0);

        // Set the normal positions of the cube, again for shading
        GLES20.glVertexAttribPointer(mNormalParam, 3, GLES20.GL_FLOAT,
                false, 0, mCubeNormals);

        GLES20.glVertexAttribPointer(mColorParam, 4, GLES20.GL_FLOAT, false,
                0, mCubeColors);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, (cubes.size()*36));
        checkGLError("Drawing cube");
    }

    private static void checkGLError(String func) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, func + ": glError " + error);
            throw new RuntimeException(func + ": glError " + error);
        }
    }

    private static float[] getCubeCoordinates(ArrayList<Cube> cubes) {
        int length = cubes.size();
        // 108 coordinates per cube
        float[] result = new float[length * 108];

        for (int i = 0; i < cubes.size(); i++) {
            float[] cubeCoords = cubes.get(i).getCoordinates();
            for (int t = 0; t < cubeCoords.length; t++) {
                int offset = i * cubeCoords.length;
                result[offset + t] = cubeCoords[t];
            }
        }
        return result;
    };

    private static float[] getCubeColors(ArrayList<Cube> cubes) {
        int length = cubes.size();
        float[] result = new float[length * 144];

        for (int i = 0; i < cubes.size(); i++) {
            float[] cubeColors = cubes.get(i).getColors();
            for (int t = 0; t < cubeColors.length; t++) {
                int offset = i * cubeColors.length;
                result[offset + t] = cubeColors[t];
            }
        }
        return result;
    };

    private static float[] getCubeNormals(ArrayList<Cube> cubes) {
        int length = cubes.size();
        float[] result = new float[length * 108];

        for (int i = 0; i < cubes.size(); i++) {
            float[] cubeNormals = Cube.CUBE_NORMALS;
            for (int t = 0; t < cubeNormals.length; t++) {
                int offset = i * cubeNormals.length;
                result[offset + t] = cubeNormals[t];
            }
        }
        return result;
    };
}
