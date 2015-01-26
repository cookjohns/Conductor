//package leap;

import java.awt.BasicStroke;
import java.awt.Color;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

public class DrawListener extends Listener {
    private Integer lastX, lastY;
    boolean draw = true;
    Canvas canvas;

    public DrawListener(Canvas canvas) {
        this.canvas = canvas;
    }

    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
    }

    public void onDisconnect(Controller controller) {
        // Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
        Frame frame = controller.frame();

        // Get the most recent frame and report some basic information

        if (!frame.hands().isEmpty()) {
            // Get the first hand
            Hand hand = frame.hands().get(0);

            // Check if the hand has any fingers
            FingerList fingers = hand.fingers();
            if (!fingers.isEmpty()) {
                // Calculate the hand's average finger tip position
                Vector avgPos = Vector.zero();
                for (Finger finger : fingers) {
                    avgPos = avgPos.plus(finger.tipPosition());
                }
                avgPos = avgPos.divide(fingers.count());
                int X = (int)convert(avgPos.get(0),0);// -350<x<350
                int Y = (int)convert(avgPos.get(1),1);// 20<y<730 flipped
                if (lastX != null && lastY != null) {
                    if (draw)
                        canvas.changeColorAndSize(Color.BLACK, new BasicStroke(
                                1));
                    else
                        canvas.changeColorAndSize(Color.WHITE, new BasicStroke(
                                20));
                    canvas.drawLineSegment(lastX, lastY, X, Y);
                }
                lastX = X;
                lastY = Y;
            }

        }

        GestureList gestures = frame.gestures();
        for (int i = 0; i < gestures.count(); i++) {
            Gesture gesture = gestures.get(i);

            switch (gesture.type()) {
            case TYPE_SCREEN_TAP:
                if (draw)
                    draw = false;
                else
                    draw = true;
                break;
            default:
                System.out.println("Unknown gesture type.");
                break;
            }
        }
    }

    private float convert(float num, int dir) {
        switch (dir) {
        case 0:
            return (num+175)*500/350;
        case 1:
            return 500 - (num - 20) * 500 / 735;
        default:
            return 0;

        }
    }
}
