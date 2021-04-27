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

public class Bezier {

    public TimedPoint startPoint;
    public TimedPoint control1;
    public TimedPoint control2;
    public TimedPoint endPoint;

    public Bezier set(TimedPoint startPoint, TimedPoint control1,
                      TimedPoint control2, TimedPoint endPoint) {
        this.startPoint = startPoint;
        this.control1 = control1;
        this.control2 = control2;
        this.endPoint = endPoint;
        return this;
    }

    public double length() {
        int steps = 10;
        double length = 0;
        double cx, cy, px = 0, py = 0, xDiff, yDiff;

        for (int i = 0; i <= steps; i++) {
            float t = (float) i / steps;
            cx = point(t, this.startPoint.x, this.control1.x,
                    this.control2.x, this.endPoint.x);
            cy = point(t, this.startPoint.y, this.control1.y,
                    this.control2.y, this.endPoint.y);
            if (i > 0) {
                xDiff = cx - px;
                yDiff = cy - py;
                length += Math.sqrt(xDiff * xDiff + yDiff * yDiff);
            }
            px = cx;
            py = cy;
        }
        return length;

    }

    public double point(double t, double start, double c1, double c2, double end) {
        return start * (1.0 - t) * (1.0 - t) * (1.0 - t)
                + 3.0 * c1 * (1.0 - t) * (1.0 - t) * t
                + 3.0 * c2 * (1.0 - t) * t * t
                + end * t * t * t;
    }

}
