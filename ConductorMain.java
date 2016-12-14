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
   
   public static void main(String[] args) {
      runStandard();
   }
   
   static void runStandard() {
      ConductorListener listener = new ConductorListener(new LeapViewer());
      Controller controller      = new Controller();
      controller.addListener(listener);
      controller.removeListener(listener);
   }
}
