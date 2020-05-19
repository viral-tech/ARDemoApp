/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.sceneform.samples.hellosceneform;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class HelloSceneformActivity extends AppCompatActivity {

    private static final String TAG = HelloSceneformActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ModelRenderable imageRenderable;

    private RecyclerView rvArImages;
    private ArImagesRecyclerAdapter arImagesRecyclerAdapter;

    ArrayList<Integer> Number;

    private ModelRenderable cubeRenderable;
    private Session session;
    private Anchor startAnchor;
    private int noOfHit = 0;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_ux);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        rvArImages = findViewById(R.id.rv_ar_images);

        AddItemsToRecyclerViewArrayList();
        arImagesRecyclerAdapter = new ArImagesRecyclerAdapter(Number);
        LinearLayoutManager layoutManager = new LinearLayoutManager(HelloSceneformActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvArImages.setLayoutManager(layoutManager);
        rvArImages.setAdapter(arImagesRecyclerAdapter);

        try {
            session = new Session(this);
        } catch (UnavailableArcoreNotInstalledException e) {
            e.printStackTrace();
        } catch (UnavailableApkTooOldException e) {
            e.printStackTrace();
        } catch (UnavailableSdkTooOldException e) {
            e.printStackTrace();
        } catch (UnavailableDeviceNotCompatibleException e) {
            e.printStackTrace();
        }

        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(this, R.raw.andy)
                .build()
                .thenAccept(renderable -> imageRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        rvArImages.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(HelloSceneformActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View ChildView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {
                    //Getting clicked value.
                    int itemPosition = recyclerView.getChildAdapterPosition(ChildView);
                    // Showing clicked item value on screen using toast message.
                    Toast.makeText(HelloSceneformActivity.this, "Clicked " + itemPosition, Toast.LENGTH_LONG).show();

                    if (itemPosition == 0) {
                        ModelRenderable.builder()
                                .setSource(HelloSceneformActivity.this, R.raw.andy)
                                .build()
                                .thenAccept(renderable -> imageRenderable = renderable)
                                .exceptionally(
                                        throwable -> {
                                            Toast toast =
                                                    Toast.makeText(HelloSceneformActivity.this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                            return null;
                                        });
                    } else if (itemPosition == 1) {
                        ModelRenderable.builder()
                                .setSource(HelloSceneformActivity.this, R.raw.model)
                                .build()
                                .thenAccept(renderable -> imageRenderable = renderable)
                                .exceptionally(
                                        throwable -> {
                                            Toast toast =
                                                    Toast.makeText(HelloSceneformActivity.this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                            return null;
                                        });
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (imageRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(imageRenderable);
                    andy.select();
                });

//        MaterialFactory.makeTransparentWithColor(this, new Color(android.graphics.Color.RED))
//                .thenAccept(
//                        material -> {
//                            Vector3 vector3 = new Vector3(0.05f, 0.01f, 0.01f);
//                            imageRenderable = ShapeFactory.makeCube(vector3, Vector3.zero(), material);
//                            imageRenderable.setShadowCaster(false);
//                            imageRenderable.setShadowReceiver(false);
//                        });

//        arFragment.setOnTapArPlaneListener((HitResult hitResult, Plane plane, MotionEvent motionEvent) ->
//        {
//            Log.v(TAG, "setOnTapArPlaneListener Called... ");
//            if (imageRenderable == null)
//                return;
//
//            //On First Hit
//            if (noOfHit == 0) {
////            Anchor startAnchor=session.addAnchor(hitResult.getHitPose());
////                try {
////                    startAnchor = session.createAnchor(hitResult.getHitPose());
//                startAnchor = hitResult.createAnchor();
//                    noOfHit++;
//                    Toast.makeText(HelloSceneformActivity.this, "First Hit... ", Toast.LENGTH_LONG).show();
//                    Log.v(TAG, "First Hit... ");
//                MaterialFactory.makeTransparentWithColor(this, new Color(android.graphics.Color.RED))
//                        .thenAccept(
//                                material -> {
//                                    Vector3 vector3 = new Vector3(0.05f, 0.01f, 0.01f);
//                                    imageRenderable = ShapeFactory.makeCube(vector3, Vector3.zero(), material);
//                                    imageRenderable.setShadowCaster(false);
//                                    imageRenderable.setShadowReceiver(false);
//                                });
////                } catch (UnavailableArcoreNotInstalledException e) {
////                    e.printStackTrace();
////                } catch (UnavailableApkTooOldException e) {
////                    e.printStackTrace();
////                } catch (UnavailableSdkTooOldException e) {
////                    e.printStackTrace();
////                } catch (UnavailableDeviceNotCompatibleException e) {
////                    e.printStackTrace();
////                }
//            } else if (noOfHit == 1) {
//                //On Second Hit
//                noOfHit = 0;
//                Pose startPose = startAnchor.getPose();
//                Pose endPose = hitResult.getHitPose();
//
//                // Clean up the anchor
////            session.removeAnchors(Collections.singleton(startAnchor));
////                session.getAllAnchors().clear();
//                startAnchor = null;
//                Log.v(TAG, "Second Hit... ");
//
//                // Compute the difference vector between the two hit locations.
//                float dx = startPose.tx() - endPose.tx();
//                float dy = startPose.ty() - endPose.ty();
//                float dz = startPose.tz() - endPose.tz();
//
//                // Compute the straight-line distance.
//                float distanceMeters = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
//                Toast.makeText(HelloSceneformActivity.this, "Distance: " + distanceMeters+" Meter", Toast.LENGTH_LONG).show();
//                Log.v(TAG, "Distance: " + distanceMeters);
//            }
//        });
    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    public void AddItemsToRecyclerViewArrayList() {
        Number = new ArrayList<>();
        Number.add(R.drawable.a);
        Number.add(R.drawable.a1);
    }
}
