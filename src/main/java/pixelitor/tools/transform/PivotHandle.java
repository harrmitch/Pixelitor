/*
 * Copyright 2023 Laszlo Balazs-Csiki and Contributors
 *
 * This file is part of Pixelitor. Pixelitor is free software: you
 * can redistribute it and/or modify it under the terms of the GNU
 * General Public License, version 3 as published by the Free
 * Software Foundation.
 *
 * Pixelitor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pixelitor. If not, see <http://www.gnu.org/licenses/>.
 */

package pixelitor.tools.transform;

import pixelitor.gui.View;
import pixelitor.tools.util.DragDisplay;
import pixelitor.tools.util.DraggablePoint;
import pixelitor.tools.util.PPoint;
import pixelitor.utils.Cursors;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.Serial;

/**
 * The handle that can be used to change the rotation pivot point
 * of a {@link TransformBox}
 */
public class PivotHandle extends DraggablePoint {
    @Serial
    private static final long serialVersionUID = 1L;

    private final TransformBox box;

    public PivotHandle(String name, TransformBox box, PPoint pos, View view) {
        super(name, pos, view);
        this.box = box;
        cursor = Cursors.DEFAULT;
    }

    public PivotHandle copy(TransformBox newBox) {
        PPoint pos;
        if (view != null) {
            pos = PPoint.fromIm(getImX(), getImY(), view);
        } else {
            pos = PPoint.lazyFromIm(getImX(), getImY(), view);
        }

        return new PivotHandle(name, newBox, pos, view);
    }

    @Override
    public void mouseDragged(double x, double y) {
        super.mouseDragged(x, y);
        box.setPivotMoved(true);
    }

    @Override
    protected Shape createShape(double startX, double startY) {
        Line2D horLine = new Line2D.Double(startX, startY + HANDLE_SIZE/2, startX + HANDLE_SIZE, startY + HANDLE_SIZE/2);
        Line2D verLine = new Line2D.Double(startX + HANDLE_SIZE/2, startY, startX + HANDLE_SIZE/2, startY + HANDLE_SIZE);

        Path2D pivotShape = new Path2D.Double();
        pivotShape.append(horLine, false);
        pivotShape.append(verLine, false);
        return pivotShape;
    }

    @Override
    public void paintHandle(Graphics2D g) {
        super.paintHandle(g);
        if (isActive()) {
            int displayBgWidth = DragDisplay.BG_WIDTH_ANGLE;
            DragDisplay dd = new DragDisplay(g, displayBgWidth);

            String pivotInfo = "x=" + x + "y=" + y;

            dd.drawOneLine(pivotInfo, (float) x - (float) DragDisplay.BG_WIDTH_ANGLE/2, (float) y - DragDisplay.ONE_LINER_BG_HEIGHT);
        }
    }
}
