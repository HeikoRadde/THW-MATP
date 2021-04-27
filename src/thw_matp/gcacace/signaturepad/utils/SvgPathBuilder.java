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
 * Copyright 2021 Heiko Radde
 */
package thw_matp.gcacace.signaturepad.utils;

/**
 * Build a SVG path as a string.
 *
 * https://www.w3.org/TR/SVGTiny12/paths.html
 */
public class SvgPathBuilder {

    public static final Character SVG_RELATIVE_CUBIC_BEZIER_CURVE = 'c';
    public static final Character SVG_MOVE = 'M';
    private final StringBuilder mStringBuilder;
    private final Integer mStrokeWidth;
    private final SvgPoint mStartPoint;
    private SvgPoint mLastPoint;

    public SvgPathBuilder(final SvgPoint startPoint, final Integer strokeWidth) {
        mStrokeWidth = strokeWidth;
        mStartPoint = startPoint;
        mLastPoint = startPoint;
        mStringBuilder = new StringBuilder();
        mStringBuilder.append(SVG_RELATIVE_CUBIC_BEZIER_CURVE);
    }

    public final Integer getStrokeWidth() {
        return mStrokeWidth;
    }

    public final SvgPoint getLastPoint() {
        return mLastPoint;
    }

    public SvgPathBuilder append(final SvgPoint controlPoint1, final SvgPoint controlPoint2, final SvgPoint endPoint) {
        mStringBuilder.append(makeRelativeCubicBezierCurve(controlPoint1, controlPoint2, endPoint));
        mLastPoint = endPoint;
        return this;
    }

    @Override
    public String toString() {
        return (new StringBuilder())
                .append("<path ")
                .append("stroke-width=\"")
                .append(mStrokeWidth)
                .append("\" ")
                .append("d=\"")
                .append(SVG_MOVE)
                .append(mStartPoint)
                .append(mStringBuilder)
                .append("\"/>")
                .toString();
    }

    private String makeRelativeCubicBezierCurve(final SvgPoint controlPoint1, final SvgPoint controlPoint2, final SvgPoint endPoint) {
        final String sControlPoint1 = controlPoint1.toRelativeCoordinates(mLastPoint);
        final String sControlPoint2 = controlPoint2.toRelativeCoordinates(mLastPoint);
        final String sEndPoint = endPoint.toRelativeCoordinates(mLastPoint);

        final StringBuilder sb = new StringBuilder();
        sb.append(sControlPoint1);
        sb.append(" ");
        sb.append(sControlPoint2);
        sb.append(" ");
        sb.append(sEndPoint);
        sb.append(" ");

        // discard zero curve
        final String svg = sb.toString();
        if ("c0 0 0 0 0 0".equals(svg)) {
            return "";
        } else {
            return svg;
        }
    }
}
