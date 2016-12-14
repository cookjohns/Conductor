


import java.io.IOException;
import java.lang.Math;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;



public class LeapListener extends Listener
{
  private LeapViewer viewer;     // GUI for showing the Leap Controller data
  private boolean reportedBox = false;
                  // for reporting details about the interaction box


  public LeapListener(LeapViewer v)
  { super();
    viewer = v;
  }


  // state-triggered methods

  public void onInit(Controller controller)
  {  System.out.println("Initialized"); }


  public void onConnect(Controller controller)
  // listen for all gestures
  {
    System.out.println("Controller has been connected");
    controller.enableGesture(Gesture.Type.TYPE_SWIPE);
       // to and fro linear movement of a finger tip/hand in any direction

    controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
       // clockwise rotation of a finger tip pointing at the screen

    controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
       // finger tip moves forward towards the screen, then back to the original postion;
       // the finger must pause briefly before beginning the tap

    controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
       // finger rotates down toward the palm, then back to the original postion;
       // the finger must pause briefly before beginning the tap

    Config config = controller.config();

    // key tap parameters
    config.setFloat("Gesture.KeyTap.MinDownVelocity", 30.0f);

    System.out.println("Key Tap MinDownVelocity: " +
                 config.getFloat("Gesture.KeyTap.MinDownVelocity"));
    System.out.println("Key Tap HistorySeconds: " + 
                 config.getFloat("Gesture.KeyTap.HistorySeconds"));
    System.out.println("Key Tap MinDistance: " + 
                 config.getFloat("Gesture.KeyTap.MinDistance"));
    System.out.println();

    // screen tap parameters
    config.setFloat("Gesture.ScreenTap.MinForwardVelocity", 30.0f);
    config.setFloat("Gesture.ScreenTap.MinDistance", 1.0f);

    System.out.println("Screen Tap MinDownVelocity: " +
                 config.getFloat("Gesture.ScreenTap.MinForwardVelocity"));
    System.out.println("Screen Tap HistorySeconds: " + 
                 config.getFloat("Gesture.ScreenTap.HistorySeconds"));
    System.out.println("Screen Tap MinDistance: " + 
                 config.getFloat("Gesture.ScreenTap.MinDistance"));
    System.out.println();
  }  // end of onConnect()


  public void onDisconnect(Controller controller)
  {  System.out.println("Disconnected");  }

  public void onExit(Controller controller)
  {  System.out.println("Exited");  }



  public void onFrame(Controller controller)
  // fired when a frame is received from the Leap controller
  {
    viewer.clear();    // reset the GUI window

    // get the most recent frame
    Frame frame = controller.frame();

    // report frame info to the GUI
    viewer.setFrameInfo(frame.fingers().count(), true);

    InteractionBox ib = frame.interactionBox();
    if (!reportedBox) {
      System.out.println("Interaction Box Info");
      System.out.println("  center: " + round1dp(ib.center()));
      System.out.println("  (x,y,z) dimensions: (" + round1dp(ib.width()) + ", " +
                      round1dp(ib.height()) + ", " + round1dp(ib.depth()) +")");
      reportedBox = true;
    }

    if (!frame.hands().isEmpty())
      examineHand( frame.hands().get(0));     // only examine the first hand

    examineGestures( frame.gestures(), controller);
  }  // end of onFrame()



  private void examineHand(Hand hand)
  {
    int fCount = 0;
    Vector avgPos = Vector.zero();

    // check if the hand has any fingers
    FingerList fingers = hand.fingers();
    if (!fingers.isEmpty()) {
      // Calculate the hand's average finger tip position
      fCount = fingers.count();
      for (Finger finger : fingers)
        avgPos = avgPos.plus(finger.tipPosition());
      avgPos = avgPos.divide(fingers.count());
    }

    /* check if finger is deep within the touch zone, which
       ranges from 1 to -1 (-1 being nearer the screen) */ 
    boolean isTouched = false;
    Finger frontFinger = hand.fingers().frontmost();
    float touchDist = frontFinger.touchDistance();
    if (touchDist< -0.8f) {
      // System.out.println("Pressed: touch distance: " + touchDist);
      isTouched = true;
    }

    // get the hand's normal vector and direction
    Vector normal = hand.palmNormal();
        // a unit vector pointing orthogonally downwards relative to the palm
    Vector direction = hand.direction();
        // a unit vector pointing from the palm position to the fingers

    // calculate the hand's pitch, roll, and yaw angles
    int pitch = (int) Math.round( Math.toDegrees(direction.pitch()));
    int roll = (int) Math.round( Math.toDegrees(normal.roll()));
    int yaw = (int) Math.round( Math.toDegrees(direction.yaw()));

    // convert the palm to interaction box coordinates
    //Vector palmIB = ib.normalizePoint(hand.palmPosition());

    // report hand info to the GUI
    viewer.setHandInfo(fCount, round1dp(avgPos));

  }  // end of examineHand()
  
  private void examineFeedback() {
	  //viewer.setFeedbackInfo(leftOrRight, yesOrNo);
  }



  private void examineGestures(GestureList gestures, Controller controller)
  // look through all the gestures, sending info to the GUI
  {
    int fID;
    for (Gesture gesture : gestures) {
      switch (gesture.type()) {
        case TYPE_CIRCLE:
          CircleGesture circle = new CircleGesture(gesture);
          // calculate clock direction using the angle between circle normal and pointable
          boolean isClockwise = 
                 (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/4);

          // calculate angle swept since last frame
          double sweptAngle = 0;
          if (circle.state() != State.STATE_START) {
            CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
            sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
          }

          int angle = (int) Math.round(Math.toDegrees(sweptAngle));
          viewer.setCircleInfo(circle.id(), circle.state(), isClockwise, 
                        round1dp(circle.progress()), round1dp(circle.radius()), angle);
          break;

        case TYPE_SWIPE:
          SwipeGesture swipe = new SwipeGesture(gesture);
          viewer.setSwipeInfo(swipe.id(), swipe.state(), round1dp(swipe.position()), 
                                  round1dp(swipe.direction()), round1dp(swipe.speed()));
          break;

        case TYPE_SCREEN_TAP:
          ScreenTapGesture screenTap = new ScreenTapGesture(gesture);
          fID = screenTap.pointable().id();   // finger ID will stay the same across frames
          viewer.setTapInfo(screenTap.id(), screenTap.state(), round1dp(screenTap.position()), 
                                                 round1dp(screenTap.direction()), fID);
          break;

        case TYPE_KEY_TAP:
          KeyTapGesture keyTap = new KeyTapGesture(gesture);
          fID = keyTap.pointable().id();   // finger ID will stay the same across frames
          viewer.setKeyTapInfo(keyTap.id(), keyTap.state(), round1dp(keyTap.position()), 
                                                  round1dp(keyTap.direction()), fID);
          break;

        default:
          System.out.println("Unknown gesture type.");
          break;
      }
    }
  }  // end of examineGestures()


  
  private Vector round1dp(Vector v)
  // round the x,y,z values in a vector to 1 dp
  {
    v.setX( (float)Math.round(v.getX()*10)/10 );
    v.setY( (float)Math.round(v.getY()*10)/10 );
    v.setZ( (float)Math.round(v.getZ()*10)/10 );
    return v;
  }


  private float round1dp(float f)
  // round a float to 1 dp
  {  return (float)Math.round(f*10)/10;  }


}  // end of LeapListener class
