package com.location.codesample.view

import android.view.MotionEvent


fun MotionEvent.getXByPointId(id: Int) = getX(findPointerIndex(id))
fun MotionEvent.getYByPointId(id: Int) = getY(findPointerIndex(id))

