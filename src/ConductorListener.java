import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DecimalFormat;

import com.leapmotion.leap.Arm;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Tool;
import com.leapmotion.leap.Vector;

/**
 * 
 * Main Listener
 *
 */
public class ConductorListener extends Listener {
		
	  private LeapViewer viewer;
	  public ConductorListener(LeapViewer v)
	  { super();
	    viewer = v;
	  }

	   // instance variables for static left, right, center
	   protected static double finalLeftMax;
	   protected static double finalRightMax;
	   protected static double finalCenter;
	   int    leftStop    = 0;
	   int    rightStop   = 0;
	   int    centerStop;
	   double prevPos     = 0;

	   // instance variables for floating left, right, center
	   protected static double mLeftPos         = 0.0;
	   protected static double mRightPos        = 0.0;
	   protected static double centerPos        = 0.0;
	   private boolean         atCenter         = true;
	   private double          prevDiff;
	   private double          currentDiff;
	   private double          mLeftDir         = 0.0;
	   private double          mRightDir        = 0.0;
	   protected double        testInteger      = 0;
	   
	   private double          wristHorizStart  = 0.0;
	   private double          wristVertStart   = 0.0;
	   private int             wristStartMarker = 0;
	   
	   public String           leftOrRight      = "   ";
	   public String           yesOrNo          = "No";
	      	   
	   private DecimalFormat form = new DecimalFormat("##.##");
	   
	   private static PropertyChangeSupport propertyChangeSupport =
	       new PropertyChangeSupport(CenterListener.class);
	       
	   public static void addPropertyChangeListener(PropertyChangeListener listener) {
	      propertyChangeSupport.addPropertyChangeListener(listener);
	   }
	       
	   public void setLeft(double leftIn) {
	      double oldValue = CenterListener.mLeftPos;
	      CenterListener.mLeftPos = leftIn;
	      propertyChangeSupport.firePropertyChange("leftIn", oldValue, CenterListener.mLeftPos);
	   }
	   
	   public double getCenterPos() {
	      return centerPos;
	   }

	   public void onInit(Controller controller) {
	      System.out.println("Initialized");
	   }

	   public void onConnect(Controller controller) {
	      System.out.println("Connected");
	      controller.enableGesture(Gesture.Type.TYPE_SWIPE);
	      controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
	      controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
	      controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
	   }

	   public void onDisconnect(Controller controller) {
	      //Note: not dispatched when running in a debugger.
	      System.out.println("Disconnected");
	   }

	   public void onExit(Controller controller) {
	      System.out.println("Exited");
	   }
	   
	   public void onFrame(Controller controller) {   
	      // Get the most recent frame and report some basic information
	      Frame frame = controller.frame();
	      String result = "";
	      boolean hasTools = frame.tools().count() >= 1;//!frame.tools().isEmpty();
	      
	      if (frame.hands().isEmpty()) {
	            mLeftPos         = 0.0;
	            mRightPos        = 0.0;
	            centerPos        = 0.0;
	            finalLeftMax     = 0.0;
	            finalRightMax    = 0.0;
	            finalCenter      = 0.0;
	            leftStop         = 0;
	            rightStop        = 0;
	            centerStop       = 0;
	            mLeftDir         = 0.0;
	            mRightDir        = 0.0;
	            prevPos          = 0;
	            wristStartMarker = 0;
	            //yesOrNo = "No";
	      }
	                      
	      else /*(!frame.hands().isEmpty() || hasTools)*/ {
	         // Get the first hand
	         Hand   hand  = frame.hands().get(0);
	         Tool   tool  = frame.tools().get(0);
	         Arm    arm   = hand.arm();
	         Vector wrist = arm.wristPosition();
	         
	      	// set wrist starting position
	         if (wristStartMarker == 0) {
	        	 wristStartMarker = 1;
	        	 wristHorizStart  = wrist.get(0);
	        	 wristVertStart   = wrist.get(1);
	         }
	         
	         // Check if the hand has any fingers
	         FingerList fingers = hand.fingers();   // <--- add in finger type here
	         
	         viewer.setFrameInfo(fingers.count(), hasTools);
	         
	         if (!fingers.isEmpty() || hasTools) {
	         
	            // Calculate the hand's average finger tip position
	            Vector avgPos = Vector.zero();
	            for (Finger finger : fingers) {
	            	if (finger.isExtended() || frame.tools().count() >= 1) {
	               avgPos = avgPos.plus(finger.tipPosition());
	               Vector direction = finger.direction();
	            
	               /*
	                *
	                * Get tip and base position  -  Set floating max left, right, and center
	                *
	                */

	               // gets tip position, formats to two decimal points
	               double fingPos = finger.tipPosition().get(0);
	               
	               // gets fartherst left (max left) finger base and tip position and direction
	               if (finger.tipPosition().get(0) < mLeftPos) {
	                  setLeft(finger.tipPosition().get(0));
	               }
	               if (direction.get(0) < mLeftDir) {
	                  mLeftDir = direction.get(0);
	               }
	               //result += ", mL: " + form.format(mLeftPos);
	               	               
	               // gets fartherst right (max right) finger tip and base position and direction
	               if (finger.tipPosition().get(0) > mRightPos) {
	                  mRightPos = finger.tipPosition().get(0);
	               }
	               if (direction.get(0) > mRightDir) {
	                  mRightDir = direction.get(0);
	               }
	               //result += ", mR: " + form.format(mRightPos);
	               
	               // gets center position of max left and max right and directs left or right to center
	               centerPos = (mLeftPos + mRightPos) / 2;
	               //result += ", center: " + form.format(centerPos);
	               //result += ", finger tip pos: " + truncFingPos;
	               
	               /*
	                *
	                *             Set static max left, right, center                         
	                *             finalLeftMax, finalRightMax, finalCenter                   
	                *             finalLeftBase, finalRightBase                              
	                *
	                */
	                
	               result += "Finger Position: " + (int) fingPos;//form.format(fingPos);
	               //result += "   basePos: " + form.format(basePos);
	               //result += "   initBasePos: " + form.format(initBase);
	                
	                // fix left
	               if (prevPos < fingPos && prevPos < -40.0 && leftStop == 0) {
	                  finalLeftMax = prevPos;
	                  leftStop = 1;
	               }
	               if (leftStop == 1) {
	                  result += "     MaxL: " + (int) finalLeftMax; //form.format(finalLeftMax);
	               }
	               
	               // fix right
	               if (prevPos > fingPos && prevPos > 10.0 && rightStop == 0) {
	                  finalRightMax = prevPos;
	                  rightStop = 1;
	               }
	               if (rightStop == 1) {
	                  result += "     MaxR: " + (int) finalRightMax; //form.format(finalRightMax);
	               }
	               
	               // fix center
	               if (leftStop == 1 && rightStop == 1) {
	                  finalCenter = (finalRightMax + finalLeftMax) / 2; //centerPos;
	                  centerStop  = 1;
	               }
	               if (centerStop == 1) {
	                  //result += "     Center: " + form.format(finalCenter);
	                  result  += "     Center is: -";
	                  yesOrNo =  "   ";
	               }
	               
	               prevPos = fingPos;
	               
	               /*
	                *
	                *             Commands to user                                          
	                * 
	                */
	               
	               result += "start: ("   + (int)wristHorizStart + "," + (int)wristVertStart + ")   "; 
	               result += "current: (" + (int)wrist.get(0)    + "," + (int)wrist.get(1) + ")   ";
	               result += "Baton: "    + (hasTools ? "Yes   " : "No   ");
	               
	                           if (fingPos > (finalCenter + 5.0) && centerStop == 1) {
	                              result = result.substring(0, result.length() - 1);
	                              result     += "L";
	                              atCenter    = false;
	                              leftOrRight = " L ";
	                           }
	                           if (fingPos < (finalCenter - 5.0) && centerStop == 1) {
	                              result      = result.substring(0, result.length() - 1);
	                              result     += "R";
	                              atCenter    = false;
	                              leftOrRight = " R ";
	                           }
	                           else {
	                              atCenter = true;
	                           }
	               result += "     " + "CENTER: " + finalCenter;  
	               
	               if (wrist.get(0) > (wristHorizStart + 20) || wrist.get(0) < (wristHorizStart - 20)
	                  		|| wrist.get(1) > (wristVertStart + 20) || wrist.get(1) < (wristVertStart - 20)) {
	                     result += "Don't move WRIST!!!";
	                     yesOrNo = "YES";
	                  }
	               else {
	            	   yesOrNo = "No";
	               }
	               testInteger++;   
	            	}
	            }
	            avgPos = avgPos.divide(fingers.count());
	         }
//	         else if (hasTools) {
//	            // enter tool controls here
//	         }
	         // resets positions to 0 if fingers leave frame 
//	         else {
//	            mLeftPos = 0.0;
//	            mRightPos = 0.0;
//	            centerPos = 0.0;
//	            finalLeftMax = 0.0;
//	            finalRightMax = 0.0;
//	            finalCenter = 0.0;
//	            leftStop = 0;
//	            rightStop = 0;
//	            centerStop = 0;
//	         	wristStartMarket = 0;
//	         }
	      } 
	      
	      System.out.println(result);
	      examineHand( frame.hands().get(0));
	      examineFeedback();
	   }

	   
	   private void examineHand(Hand hand)
	   {
	     int    fCount = 0;
	     Vector avgPos = Vector.zero();

	     // check if the hand has any fingers
	     FingerList fingers = hand.fingers();
	     if (!fingers.isEmpty()) {
	       // Calculate the hand's average finger tip position
	       //fCount = fingers.count();
	       for (Finger finger : fingers)
	    	   if (finger.isExtended()) {
	    		   fCount++;
	  	         avgPos = avgPos.plus(finger.tipPosition());
	  	         avgPos = avgPos.divide(fingers.count());
	    	   }
	     }

	     /* check if finger is deep within the touch zone, which
	        ranges from 1 to -1 (-1 being nearer the screen) */ 
	     boolean isTouched   = false;
	     Finger  frontFinger = hand.fingers().frontmost();
	     float   touchDist   = frontFinger.touchDistance();
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
	     int roll  = (int) Math.round( Math.toDegrees(normal.roll()));
	     int yaw   = (int) Math.round( Math.toDegrees(direction.yaw()));

	     // convert the palm to interaction box coordinates
	     //Vector palmIB = ib.normalizePoint(hand.palmPosition());

	     // report hand info to the GUI
	     viewer.setHandInfo(fCount, avgPos);
	   }
	   
	   private void examineFeedback() {
		   viewer.setFeedbackInfo(finalCenter, leftOrRight, yesOrNo);
	   }
}
