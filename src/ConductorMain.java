import java.io.IOException;
import java.util.Scanner;
import com.leapmotion.leap.Controller;
import de.voidplus.leapmotion.*;
import processing.core.*;

/**
 * 
 * Pedagogical Conducting Tool designed to assist in the teaching of
 * musical conducting in the absensce of an instructor. Current focus
 * is to determine center point of horizontal wrist flex, and assist
 * user in defining conducting pattern using wrist movement only
 * (program should inform user not to use arm movement when detected).
 * 
 * @author John Cook
 * @version 08-08-2014
 *
 */

class ConductorMain extends PApplet {
   
   public static void main(String[] args) {//throws IOException {
   
      runStandard();
      
      //setup();
      //draw();
       //new LeapViewer();
   //	  Scanner input = new Scanner(System.in);
   //	  System.out.println("Which mode would you like to run?");
   //	  System.out.println("Enter 1 for CENTER or 2 for WRIST MOVEMENT: ");
   //	  String choice = input.nextLine();
   //	  switch (choice) {
   //	  	case "1":
   //	  	    System.out.print("\nPlace hand over device, then press ENTER. \n\nPress ENTER again to STOP.");
   //		    System.in.read();
   //	  		runCenterOnly();
   //	  		break;
   //	  	case "2":
   //	  	    System.out.print("\nPlace hand over device, then press ENTER. \n\nPress ENTER again to STOP.");
   //		    System.in.read();
   //	  		runWristOnly();
   //	  		break;
   //	  	default:
   //	  		// nothing here
   //	  }
   //	  input.close();
   }
   
   static void runStandard() {
      ConductorListener listener = new ConductorListener(new LeapViewer());
      Controller controller      = new Controller();
      controller.addListener(listener);
      controller.removeListener(listener);
   }
   
  // LeapMotion leapMotion;
   
//    public void setup()
//   {
//     size(16 * 50, 9 * 50);
//     background(20);
//
//     LeapMotion leapMotion = new LeapMotion(this);
//   }
//
//   public void draw()
//   {
//     fill(20);
//     rect(0, 0, 200, 200); // width X height
//   }
//
//   void onInit(final Controller controller)
//   {
//	   println("Initialized");
//   }
//
//   void onConnect(final Controller controller)
//   {
//	   println("Connected");
//   }
//
//   void onDisconnect(final Controller controller)
//   {
//	   println("Disconnected");
//   }
//
//   void onExit(final Controller controller)
//   {
//	   println("Exited");
//   }
//
//   void onFrame(final Controller controller)
//   {
//	   println("Frame");
//   }
//   
//    static void runWristOnly() {
//    	  // Create a listener and controller
//	      WristMovementListener listener = new WristMovementListener();
//	      Controller controller          = new Controller();
//	      
//	      // Have the listener receive events from the controller
//	      controller.addListener(listener);
//	      
//	      // Keep this process running until Enter is pressed
//	      try {
//	         System.in.read();
//	      } 
//	      catch (IOException e) {
//	         e.printStackTrace();
//	      }
//	      
//	      // Remove the sample listener when done
//	      controller.removeListener(listener);
//   }
//    
//    static void runCenterOnly() {
//  	  // Create a listener and controller
//	      CenterListener listener = new CenterListener(new LeapViewer());
//	      Controller controller   = new Controller();
//	      
//	      // Have the listener receive events from the controller
//	      controller.addListener(listener);
//	      
//	      // Keep this process running until Enter is pressed
//	      try {
//	         System.in.read();
//	      } 
//	      catch (IOException e) {
//	         e.printStackTrace();
//	      }
//	      
//	      // Remove the sample listener when done
//	      controller.removeListener(listener);
//    }
}