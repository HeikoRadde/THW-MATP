/*
 * Copyright 2014-2016 Gianluca Cacace
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

/*
 * Copyright 2020 Heiko Radde
 */
package thw_matp.gcacace.signaturepad.views;

import org.apache.pdfbox.jbig2.Bitmap;
import thw_matp.gcacace.signaturepad.utils.Bezier;
import thw_matp.gcacace.signaturepad.utils.ControlTimedPoints;
import thw_matp.gcacace.signaturepad.utils.SvgBuilder;
import thw_matp.gcacace.signaturepad.utils.TimedPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SignaturePad extends JPanel implements MouseListener, MouseMotionListener {
    //View state
    private List<TimedPoint> mPoints;
    private boolean mIsEmpty;
    private Boolean mHasEditState;
    private double mLastTouchX;
    private double mLastTouchY;
    private double mLastVelocity;
    private double mLastWidth;
    private Rectangle2D.Double mDirtyRect;
    private Bitmap mBitmapSavedState;

    private final SvgBuilder mSvgBuilder = new SvgBuilder();

    // Cache
    private List<TimedPoint> mPointsCache = new ArrayList<>();
    private ControlTimedPoints mControlTimedPointsCached = new ControlTimedPoints();
    private Bezier mBezierCached = new Bezier();

    //Configurable parameters
    private double mMinWidth;
    private double mMaxWidth;
    private float mVelocityFilterWeight;
    private OnSignedListener mOnSignedListener;
    private Color mColor;
    private boolean mClearOnDoubleClick;

    //Default attribute values
    private final double DEFAULT_ATTR_PEN_MIN_WIDTH_PX = 3.0;
    private final double DEFAULT_ATTR_PEN_MAX_WIDTH_PX = 7.0;
    private final Color DEFAULT_ATTR_PEN_COLOR = Color.BLACK;
    private final float DEFAULT_ATTR_VELOCITY_FILTER_WEIGHT = 0.9f;
    private final boolean DEFAULT_ATTR_CLEAR_ON_DOUBLE_CLICK = false;

    private BufferedImage mSignature = null;

    public SignaturePad() {
        super();

        mMinWidth = DEFAULT_ATTR_PEN_MIN_WIDTH_PX;
        mMaxWidth = DEFAULT_ATTR_PEN_MAX_WIDTH_PX;
        mColor = DEFAULT_ATTR_PEN_COLOR;
    }


    /**
     * Set the pen color from a given color.
     *
     * @param color the color.
     */
    public void setPenColor(Color color) {
        mColor = color;
    }

    /**
     * Set the minimum width of the stroke in pixel.
     *
     * @param minWidth the width in px.
     */
    public void setMinWidth(double minWidth) {
        mMinWidth = minWidth;
    }

    /**
     * Set the maximum width of the stroke in pixel.
     *
     * @param maxWidth the width in px.
     */
    public void setMaxWidth(double maxWidth) {
        mMaxWidth = maxWidth;
    }

    /**
     * Set the velocity filter weight.
     *
     * @param velocityFilterWeight the weight.
     */
    public void setVelocityFilterWeight(float velocityFilterWeight) {
        mVelocityFilterWeight = velocityFilterWeight;
    }

    public void clearView() {
        mSvgBuilder.clear();
        mPoints = new ArrayList<>();
        mLastVelocity = 0;
        mLastWidth = (mMinWidth + mMaxWidth) / 2;

        if (mSignature != null) {
            mSignature = null;
            ensureSignature();
        }

        setIsEmpty(true);

        invalidate();
    }

    public void clear() {
        this.clearView();
        this.mHasEditState = true;
    }

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  {@code MOUSE_DRAGGED} events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations,
     * {@code MOUSE_DRAGGED} events may not be delivered during a native
     * Drag&amp;Drop operation.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (!isEnabled())
            return;

        float eventX = e.getX();
        float eventY = e.getY();

        resetDirtyRect(eventX, eventY);
        addPoint(getNewPoint(eventX, eventY));
        setIsEmpty(false);

        invalidate();
    }

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {

    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && !e.isConsumed()) {
            e.consume();
            //handle double click event.
            onDoubleClick();
        }
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (!isEnabled())
            return;

        float eventX = e.getX();
        float eventY = e.getY();

        mPoints.clear();
        mLastTouchX = eventX;
        mLastTouchY = eventY;
        addPoint(getNewPoint(eventX, eventY));
        if (mOnSignedListener != null) mOnSignedListener.onStartSigning();

        invalidate();
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (!isEnabled())
            return;

        float eventX = e.getX();
        float eventY = e.getY();

        resetDirtyRect(eventX, eventY);
        addPoint(getNewPoint(eventX, eventY));

        invalidate();
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        if (mSignature != null) {
            g.drawImage(mSignature, 0, 0, null);
        }
    }

    public void setOnSignedListener(OnSignedListener listener) {
        mOnSignedListener = listener;
    }

    public boolean isEmpty() {
        return mIsEmpty;
    }

    private void onDoubleClick() {
        if (mClearOnDoubleClick) {
            this.clearView();
        }
    }

    private TimedPoint getNewPoint(float x, float y) {
        int mCacheSize = mPointsCache.size();
        TimedPoint timedPoint;
        if (mCacheSize == 0) {
            // Cache is empty, create a new point
            timedPoint = new TimedPoint();
        } else {
            // Get point from cache
            timedPoint = mPointsCache.remove(mCacheSize - 1);
        }

        return timedPoint.set(x, y);
    }

    private void recyclePoint(TimedPoint point) {
        mPointsCache.add(point);
    }

    private void addPoint(TimedPoint newPoint) {
        mPoints.add(newPoint);

        int pointsCount = mPoints.size();
        if (pointsCount > 3) {

            ControlTimedPoints tmp = calculateCurveControlPoints(mPoints.get(0), mPoints.get(1), mPoints.get(2));
            TimedPoint c2 = tmp.c2;
            recyclePoint(tmp.c1);

            tmp = calculateCurveControlPoints(mPoints.get(1), mPoints.get(2), mPoints.get(3));
            TimedPoint c3 = tmp.c1;
            recyclePoint(tmp.c2);

            Bezier curve = mBezierCached.set(mPoints.get(1), c2, c3, mPoints.get(2));

            TimedPoint startPoint = curve.startPoint;
            TimedPoint endPoint = curve.endPoint;

            double velocity = endPoint.velocityFrom(startPoint);
            velocity = Double.isNaN(velocity) ? 0.0f : velocity;

            velocity = mVelocityFilterWeight * velocity
                    + (1 - mVelocityFilterWeight) * mLastVelocity;

            // The new width is a function of the velocity. Higher velocities
            // correspond to thinner strokes.
            double newWidth = strokeWidth(velocity);

            // The Bezier's width starts out as last curve's final width, and
            // gradually changes to the stroke width just calculated. The new
            // width calculation is based on the velocity between the Bezier's
            // start and end mPoints.
            addBezier(curve, mLastWidth, newWidth);

            mLastVelocity = velocity;
            mLastWidth = newWidth;

            // Remove the first element from the list,
            // so that we always have no more than 4 mPoints in mPoints array.
            recyclePoint(mPoints.remove(0));

            recyclePoint(c2);
            recyclePoint(c3);

        } else if (pointsCount == 1) {
            // To reduce the initial lag make it work with 3 mPoints
            // by duplicating the first point
            TimedPoint firstPoint = mPoints.get(0);
            mPoints.add(getNewPoint(firstPoint.x, firstPoint.y));
        }
        this.mHasEditState = true;
    }

    private void addBezier(Bezier curve, double startWidth, double endWidth) {
        mSvgBuilder.append(curve, (startWidth + endWidth) / 2);
        ensureSignature();
        double widthDelta = endWidth - startWidth;
        double drawSteps = Math.ceil(curve.length());

        for (int i = 0; i < drawSteps; i++) {
            // Calculate the Bezier (x, y) coordinate for this step.
            double t = ((float) i) / drawSteps;
            double tt = t * t;
            double ttt = tt * t;
            double u = 1 - t;
            double uu = u * u;
            double uuu = uu * u;

            double x = uuu * curve.startPoint.x;
            x += 3 * uu * t * curve.control1.x;
            x += 3 * u * tt * curve.control2.x;
            x += ttt * curve.endPoint.x;

            double y = uuu * curve.startPoint.y;
            y += 3 * uu * t * curve.control1.y;
            y += 3 * u * tt * curve.control2.y;
            y += ttt * curve.endPoint.y;

            // Set the incremental stroke width and draw.
            final double strokeWidth = startWidth + ttt * widthDelta;
            final Graphics2D g2d = this.mSignature.createGraphics();
            g2d.setPaint(mColor);
            g2d.fillOval((int)x, (int)y, (int)strokeWidth, (int)strokeWidth);
            g2d.dispose();
            expandDirtyRect(x, y);
        }
    }

    private ControlTimedPoints calculateCurveControlPoints(TimedPoint s1, TimedPoint s2, TimedPoint s3) {
        float dx1 = s1.x - s2.x;
        float dy1 = s1.y - s2.y;
        float dx2 = s2.x - s3.x;
        float dy2 = s2.y - s3.y;

        float m1X = (s1.x + s2.x) / 2.0f;
        float m1Y = (s1.y + s2.y) / 2.0f;
        float m2X = (s2.x + s3.x) / 2.0f;
        float m2Y = (s2.y + s3.y) / 2.0f;

        float l1 = (float) Math.sqrt(dx1 * dx1 + dy1 * dy1);
        float l2 = (float) Math.sqrt(dx2 * dx2 + dy2 * dy2);

        float dxm = (m1X - m2X);
        float dym = (m1Y - m2Y);
        float k = l2 / (l1 + l2);
        if (Float.isNaN(k)) k = 0.0f;
        float cmX = m2X + dxm * k;
        float cmY = m2Y + dym * k;

        float tx = s2.x - cmX;
        float ty = s2.y - cmY;

        return mControlTimedPointsCached.set(getNewPoint(m1X + tx, m1Y + ty), getNewPoint(m2X + tx, m2Y + ty));
    }

    private double strokeWidth(double velocity) {
        return Math.max(mMaxWidth / (velocity + 1), mMinWidth);
    }

    /**
     * Called when replaying history to ensure the dirty region includes all
     * mPoints.
     *
     * @param historicalX the previous x coordinate.
     * @param historicalY the previous y coordinate.
     */
    private void expandDirtyRect(double historicalX, double historicalY) {
        if (historicalX < mDirtyRect.x) {
            mDirtyRect.x = historicalX;
        } else if (historicalX > mDirtyRect.x + mDirtyRect.width) {
            mDirtyRect.width = historicalX - mDirtyRect.x;
        }
        if (historicalY < mDirtyRect.y) {
            mDirtyRect.y = historicalY;
        } else if (historicalY > mDirtyRect.y + mDirtyRect.height) {
            mDirtyRect.height = historicalY - mDirtyRect.y;
        }
    }

    /**
     * Resets the dirty region when the motion event occurs.
     *
     * @param eventX the event x coordinate.
     * @param eventY the event y coordinate.
     */
    private void resetDirtyRect(float eventX, float eventY) {

        // The mLastTouchX and mLastTouchY were set when the ACTION_DOWN motion event occurred.
        mDirtyRect.x = Math.min(mLastTouchX, eventX);
        mDirtyRect.width = Math.max(mLastTouchX, eventX) - mDirtyRect.x;
        mDirtyRect.y = Math.min(mLastTouchY, eventY);
        mDirtyRect.height = Math.max(mLastTouchY, eventY) - mDirtyRect.y;
    }

    private void setIsEmpty(boolean newValue) {
        mIsEmpty = newValue;
        if (mOnSignedListener != null) {
            if (mIsEmpty) {
                mOnSignedListener.onClear();
            } else {
                mOnSignedListener.onSigned();
            }
        }
    }

    private void ensureSignature() {
        if (mSignature == null) {
            mSignature = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
    }

    public interface OnSignedListener {
        void onStartSigning();

        void onSigned();

        void onClear();
    }

    public List<TimedPoint> getPoints() {
        return mPoints;
    }
}

