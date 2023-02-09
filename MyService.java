package com.example.acc;

import android.accessibilityservice.AccessibilityService;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


import java.util.HashMap;

import java.util.Map;


public class MyService extends AccessibilityService {


    boolean hdkg = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo != null) {
            int eventType = event.getEventType();
            if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                    eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            ) {

                kg = true;

                if (hdkg) {
                    Log.d("acc", "~~~~~~~~~~~~~~~");
                    hdkg = false;
                }


                cdkg = false;
                tckg = false;


                inprint(nodeInfo);

            }


        }


    }


    boolean kg = false;
    boolean nextkg = false;
    boolean cdkg = false;
    boolean tckg = false;
    String laststr;
    String lastclass;
    int tc = 1;
    private Map<Integer, String> handleMap = new HashMap<>();


    private boolean inprint(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            int childCount = nodeInfo.getChildCount();


            if (nodeInfo.getText() != null) {
                String nodeCotent = nodeInfo.getText().toString();
                if (lastclass.equals("android.widget.FrameLayout"))
                    laststr = nodeCotent;

                if ("移动设备书签".equals(nodeInfo.getText().toString())) {

                    kg = true;

                } else if ("编辑".equals(nodeInfo.getText().toString())) {

                    if (kg)
                        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if ("网址".equals(nodeInfo.getText().toString())
                        || "名称".equals(nodeInfo.getText().toString())
                ) {

                    nextkg = true;
                } else if (nextkg) {

                    nextkg = false;

                    if (nodeInfo.getText().toString().length() >= 4) {

                        if (nodeInfo.getText().toString().substring(0, 4).equals("http")) {
                            tc++;
                            Log.e("acc", nodeInfo.getText().toString());
                            performGlobalAction(GLOBAL_ACTION_BACK);
                            try {
                                Thread.sleep(110);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        } else
                            Log.e("acc", tc + ": " + nodeInfo.getText().toString());

                    } else
                        Log.e("acc", tc + ": " + nodeInfo.getText().toString());


                    cdkg = false;

                }
            }


            if ("android.widget.ImageButton".equals(nodeInfo.getClassName()) && lastclass.equals("android.widget.TextView")) {

                if (handleMap.get((nodeInfo.hashCode())) == null) {
                    hdkg = false;
                    handleMap.put(nodeInfo.hashCode(), laststr);
                    if (kg) {
                        cdkg = true;

                        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        tckg = true;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return false;

                    }
                } else {
                    if (!handleMap.get((nodeInfo.hashCode())).equals(laststr)) {

                        handleMap.put(nodeInfo.hashCode(), laststr);
                        if (kg) {
                            cdkg = true;

                            tckg = true;
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return false;

                        }
                    }
                    hdkg = true;


                }
            }

            lastclass = nodeInfo.getClassName().toString();
            for (int i = 0; i < childCount; i++) {
                if (tckg)
                    return false;
                AccessibilityNodeInfo childNodeInfo = nodeInfo.getChild(i);

                if (inprint(childNodeInfo)) {
                    return true;
                }


            }
        }
        return false;
    }

    @Override
    public void onInterrupt() {

    }

}

