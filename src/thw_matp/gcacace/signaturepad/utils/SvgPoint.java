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
package thw_matp.gcacace.signaturepad.utils;

/**
 * Represent a point as it would be in the generated SVG document.
 */
class SvgPoint {

    final Integer x, y;

    public SvgPoint(TimedPoint point) {
        // one optimisation is to get rid of decimals as they are mostly non-significant in the
        // produced SVG image
        x = Math.round(point.x);
        y = Math.round(point.y);
    }

    public SvgPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toAbsoluteCoordinates() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(x);
        stringBuilder.append(",");
        stringBuilder.append(y);
        return stringBuilder.toString();
    }

    public String toRelativeCoordinates(final SvgPoint referencePoint) {
        return (new SvgPoint(x - referencePoint.x, y - referencePoint.y)).toString();
    }

    @Override
    public String toString() {
        return toAbsoluteCoordinates();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SvgPoint svgPoint = (SvgPoint) o;

        if (!x.equals(svgPoint.x)) return false;
        return y.equals(svgPoint.y);

    }

    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        return result;
    }
}
