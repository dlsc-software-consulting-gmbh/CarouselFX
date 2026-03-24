package com.dlsc.carousel.animation;

import com.dlsc.carousel.Direction;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Shape reveal transition. An arbitrary {@link Shape} is used as a clip and
 * animated via scale and rotation to reveal or hide a page.
 *
 * <p>Defaults to a star SVG shape if no custom shape is provided.</p>
 *
 * <p>Formerly AnimScaleRotateShape.</p>
 */
public class AnimShapeReveal extends CarouselAnimationBase {

    private Interpolator interpolator = Interpolator.EASE_BOTH;
    private Shape shape;
    private double scaleXFrom;
    private double scaleXTo;
    private double scaleYFrom;
    private double scaleYTo;
    private double rotateFrom;
    private double rotateTo;
    private Point3D rotationAxis;
    private final DoubleProperty progress = new SimpleDoubleProperty(0);
    private ChangeListener<Number> progressListener;

    /**
     * Creates a shape reveal with default star shape, scaling 1→100, no rotation.
     */
    public AnimShapeReveal() {
        this(null, 1, 100, 1, 100, 0, 0, Rotate.Z_AXIS);
    }

    /**
     * Creates a shape reveal with uniform scale.
     *
     * @param shape     the clip shape (null for default star)
     * @param scaleFrom the initial scale
     * @param scaleTo   the final scale
     */
    public AnimShapeReveal(Shape shape, double scaleFrom, double scaleTo) {
        this(shape, scaleFrom, scaleTo, scaleFrom, scaleTo, 0, 0, Rotate.Z_AXIS);
    }

    /**
     * Creates a shape reveal with full configuration.
     *
     * @param shape         the clip shape (null for default star)
     * @param scaleXFrom    initial X scale
     * @param scaleXTo      final X scale
     * @param scaleYFrom    initial Y scale
     * @param scaleYTo      final Y scale
     * @param rotateFrom    initial rotation angle
     * @param rotateTo      final rotation angle
     * @param rotationAxis  the rotation axis
     */
    public AnimShapeReveal(Shape shape, double scaleXFrom, double scaleXTo,
                           double scaleYFrom, double scaleYTo,
                           double rotateFrom, double rotateTo,
                           Point3D rotationAxis) {
        this.shape = shape != null ? shape : createDefaultShape();
        this.scaleXFrom = Math.max(0, scaleXFrom);
        this.scaleXTo = Math.max(0, scaleXTo);
        this.scaleYFrom = Math.max(0, scaleYFrom);
        this.scaleYTo = Math.max(0, scaleYTo);
        this.rotateFrom = rotateFrom;
        this.rotateTo = rotateTo;
        this.rotationAxis = rotationAxis;
    }

    private static Shape createDefaultShape() {
        SVGPath path = new SVGPath();
        path.setContent("M14,6L9.61,5.2L7.5,1.3L5.4,5.2L1,6l3,3.3l-0.6,4.4l4-1.9l4,1.9L11,9.29L14,6z");
        return path;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape != null ? shape : createDefaultShape();
    }

    public double getScaleXFrom() { return scaleXFrom; }
    public void setScaleXFrom(double v) { scaleXFrom = Math.max(0, v); }
    public double getScaleXTo() { return scaleXTo; }
    public void setScaleXTo(double v) { scaleXTo = Math.max(0, v); }
    public double getScaleYFrom() { return scaleYFrom; }
    public void setScaleYFrom(double v) { scaleYFrom = Math.max(0, v); }
    public double getScaleYTo() { return scaleYTo; }
    public void setScaleYTo(double v) { scaleYTo = Math.max(0, v); }
    public double getRotateFrom() { return rotateFrom; }
    public void setRotateFrom(double v) { rotateFrom = v; }
    public double getRotateTo() { return rotateTo; }
    public void setRotateTo(double v) { rotateTo = v; }
    public Point3D getRotationAxis() { return rotationAxis; }
    public void setRotationAxis(Point3D axis) { rotationAxis = axis; }

    @Override
    public Animation getAnimation(TransitionContext context) {
        Node currentPage = context.getCurrentPage();
        Node nextPage = context.getNextPage();
        Duration duration = context.getDuration();
        Direction direction = context.getDirection();
        StackPane contentPane = context.getContentPane();

        boolean forward = direction == Direction.FORWARD;

        shape.setRotationAxis(rotationAxis);

        if (nextPage != null) {
            nextPage.setVisible(true);
        }

        Node clippedNode;
        if (forward) {
            clippedNode = nextPage;
            if (clippedNode != null) {
                clippedNode.toFront();
            }
        } else {
            clippedNode = currentPage;
            if (clippedNode != null) {
                clippedNode.toFront();
            }
        }
        if (clippedNode != null) {
            clippedNode.setClip(shape);
        }

        Runnable finish = () -> {
            if (currentPage != null) {
                currentPage.setClip(null);
                currentPage.setVisible(false);
            }
            if (nextPage != null) {
                nextPage.setClip(null);
                nextPage.setVisible(true);
            }
            resetShape();
        };
        setFinishAction(finish);

        context.fireClosing(context.getCurrentIndex());
        context.fireOpening(context.getNextIndex());

        // Remove old listener
        if (progressListener != null) {
            progress.removeListener(progressListener);
        }
        progress.set(0);

        // Create new listener that recomputes from current dimensions each frame
        progressListener = (obs, oldVal, newVal) -> {
            double p = newVal.doubleValue();
            double w = contentPane.getWidth();
            double h = contentPane.getHeight();

            // Center the shape at current pane center
            shape.setTranslateX(w / 2);
            shape.setTranslateY(h / 2);

            if (forward) {
                shape.setScaleX(scaleXFrom + (scaleXTo - scaleXFrom) * p);
                shape.setScaleY(scaleYFrom + (scaleYTo - scaleYFrom) * p);
                shape.setRotate(rotateFrom + (rotateTo - rotateFrom) * p);
            } else {
                shape.setScaleX(scaleXTo + (scaleXFrom - scaleXTo) * p);
                shape.setScaleY(scaleYTo + (scaleYFrom - scaleYTo) * p);
                shape.setRotate(rotateTo + (rotateFrom - rotateTo) * p);
            }
        };
        progress.addListener(progressListener);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(progress, 0)),
                new KeyFrame(duration,
                        new KeyValue(progress, 1, interpolator))
        );

        timeline.setOnFinished(e -> {
            finish.run();
            context.fireClosed(context.getCurrentIndex());
            context.fireOpened(context.getNextIndex());
        });

        setCurrentAnimation(timeline);
        return timeline;
    }

    private void resetShape() {
        shape.setScaleX(scaleXFrom);
        shape.setScaleY(scaleYFrom);
        shape.setRotate(rotateFrom);
        shape.setRotationAxis(rotationAxis);
    }

    @Override
    public void clearEffects(TransitionContext context) {
        super.clearEffects(context);
        if (progressListener != null) {
            progress.removeListener(progressListener);
            progressListener = null;
        }
        for (Node child : context.getContentPane().getChildren()) {
            child.setClip(null);
        }
        resetShape();
    }

    @Override
    public void dispose() {
        if (progressListener != null) {
            progress.removeListener(progressListener);
            progressListener = null;
        }
        super.dispose();
    }
}
