import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.*;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

/**
 * Display GUI for Conductor Listener data. 
 */

public class LeapViewer extends JFrame implements ActionListener {
			
//   public class GUISketchController extends JFrame {
//      private GUISketch sketch;
//      public Tool globalTool;
//      public GUISketchController() {
//         setTitle("Conductor");
//         setDefaultCloseOperation(EXIT_ON_CLOSE);
//         sketch = new GUISketch();
//         sketch.init();
//         add(sketch);
//      }
//   }
	
   private JTextField frameID_TF,       timeHands_TF,   frameHands_TF, frameFingers_TF, 
                       frameTools_TF,   frameGestures_TF,
                       handFingers_TF,  handTips_TF,    handRadius_TF,
                       handPalm_TF,     handPalmIB_TF, 
                       handPitch_TF,    handRoll_TF,    handYaw_TF,    handTouchTF,
                       circleDir_TF,    circleID_TF,    circleProgess_TF, 
                       circleRadius_TF, circleAngle_TF, circleState_TF,
                       swipeID_TF,      swipePosn_TF,   swipeDir_TF, 
                       swipeState_TF,   swipeSpeed_TF,
                       tapID_TF,        tapPosn_TF,     tapDir_TF, 
                       tapState_TF,     tapFingerID_TF,
                       keyTapID_TF,     keyTapPosn_TF,  keyTapDir_TF, 
                       keyTapState_TF,  keyTapFingerID_TF, 
                       centerIs,        goDirection,    wristMoveNotify;

   private JCheckBox circle_CB, swipe_CB, tap_CB, keyTap_CB;
   
   public de.voidplus.leapmotion.Tool globalTool;
   
   GUISketch sketch = new GUISketch(this.globalTool);

   private ConductorListener listener;
   private Controller controller;

   private JButton twoFourButton = new JButton("2/4");
   boolean twoButtonPress = false;
   private JButton threeFourButton = new JButton("3/4");
   private JButton fourFourButton = new JButton("4/4");

   public LeapViewer() {
      super("Conductor Information");
   
      buildGUI();
      //GUISketchController control = new GUISketchController();
      //control.pack();
      //control.setVisible(true);
      
      // Paint controller

   
      // Create listener and controller
      listener   = new ConductorListener(this);
      controller = new Controller();
      controller.addListener(listener);
   
      addWindowListener( 
            new WindowAdapter() {
               public void windowClosing(WindowEvent e)
               { controller.removeListener(listener);    // Remove the sample listener when done
                  System.exit(0);
               }
            });
   
      pack();
      setResizable(false);
      setLocationRelativeTo(null);  // center the window 
      setVisible(true);
   
      try {
         Thread.sleep(5000);
      }
      catch(InterruptedException e) {}
   
      // ends program if Leap Motion controller not connected
   //    if (!controller.isConnected()) {
   //      System.out.println("Cannot connect to Leap");
   //      System.exit(1);
   //    }
   } // end of LeapViewer()



   private void buildGUI() {
      Container c = getContentPane();
   // use BoxLayout: align components vertically
      c.setLayout( new BoxLayout(c, BoxLayout.Y_AXIS) );  
   
   // ------------------------- Add Sketch to JFRAME -------------------
   
      sketch.init();
      add(sketch);
      sketch.setVisible(true);
   
      pack();
      setResizable(false);
      setLocationRelativeTo(null);  // center the window 
      setVisible(true);
            
      JFrame frame = new JFrame();
      frame.setSize(500, 500);
      final Canvas canvas = new Canvas(100, 100);
      frame.add(canvas);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
      Thread t = new Thread(new Runnable() {
          public void run() {

              // Create a sample listener and controller
              DrawListener drawListener = new DrawListener(canvas, sketch);
              Controller controller = new Controller();
              //globalTool = drawListener.globalTool;

              // Have the sample listener receive events from the
              // controller
              controller.addListener(drawListener);

              // Keep this process running until Enter is pressed
              System.out.println("Press Enter to quit...");
              try {
                  System.in.read();
              } catch (IOException e) {
                  e.printStackTrace();
              }
              // Remove the sample listener when done
              controller.removeListener(drawListener);
          }
      });
      t.start();
      
      // ------------------------- Buttons ------------------------
      JPanel buttonPanel = new JPanel();
      buttonPanel.setBorder( BorderFactory.createTitledBorder("Pattern") );
      buttonPanel.add(twoFourButton);
      buttonPanel.add(threeFourButton);
      buttonPanel.add(fourFourButton);
      twoFourButton.addActionListener(this);
      threeFourButton.addActionListener(this);
      fourFourButton.addActionListener(this);
      
      c.add(buttonPanel);
            
      // ------------------------- Feedback ------------------------
      
      JPanel feedbackPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      feedbackPanel.setBorder( BorderFactory.createTitledBorder("Feedback") );
      
      centerIs        = new JTextField(3);
      labelText(feedbackPanel, "Center is at", centerIs);
      
      goDirection     = new JTextField(3);
      labelText(feedbackPanel, "Go", goDirection);
      
      wristMoveNotify = new JTextField(3);
      labelText(feedbackPanel, "Wrist Movement?", wristMoveNotify);
      
      c.add(feedbackPanel);
   
   
      // ------------------------- Hand Info -------------------
   
      JPanel handPanel = new JPanel();
      handPanel.setLayout( new BoxLayout(handPanel, BoxLayout.Y_AXIS) );
      handPanel.setBorder( BorderFactory.createTitledBorder("Hand Info") );
   
      JPanel hPanel    = new JPanel(new FlowLayout(FlowLayout.LEFT));
      handPanel.add(hPanel);
   
      handFingers_TF   = new JTextField(5);
      labelText(hPanel, "Number of Extended Fingers", handFingers_TF);
   
      handTips_TF      = new JTextField(10);
      labelText(hPanel, "Finger Tip Position", handTips_TF);
   
   //    handPalm_TF = new JTextField(10);
   //    labelText(hPanel, "Palm Pos", handPalm_TF);
   //       // center of the palm measured in millimeters from the Leap origin
   
   //    handPalmIB_TF = new JTextField(10);
   //    labelText(hPanel, "Palm Position in Box", handPalmIB_TF);
   //       // center of the palm measured in millimeters from the interaction box origin
   
   
      //JPanel hand2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      //handPanel.add(hand2Panel);
   
   //    // 3 axes for hand rotation
   //    handPitch_TF = new JTextField(3);
   //    labelText(hand2Panel, "Pitch (around x-axis)", handPitch_TF);
   //
   //    handYaw_TF = new JTextField(3);
   //    labelText(hand2Panel, "Yaw (around y-axis)", handYaw_TF);
   //
   //    handRoll_TF = new JTextField(3);   
   //    labelText(hand2Panel, "Roll (around z-axis)", handRoll_TF);
   //
   //    handRadius_TF = new JTextField(5);
   //    labelText(hand2Panel, "Radius", handRadius_TF);
   //        /* The curvature of the sphere decreases as the fingers 
   //           are curled, defining a sphere with a smaller radius */
   //
   //    handTouchTF = new JTextField(3);   
   //    labelText(hand2Panel, "Touching", handTouchTF);
   
      //c.add(handPanel);
      
      
      // ------------------------- Frame Info -------------------
   
      JPanel framePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      framePanel.setBorder( BorderFactory.createTitledBorder("Frame Info") );
   
   //    frameID_TF = new JTextField(6);
   //    labelText(framePanel, "ID", frameID_TF);
   
   //    timeHands_TF = new JTextField(10);
   //    labelText(framePanel, "Timestamp", timeHands_TF);
   
   //    frameHands_TF = new JTextField(2);
   //    labelText(framePanel, "No. Hands", frameHands_TF);
   
      frameFingers_TF = new JTextField(2);
      labelText(framePanel, "Number of Fingers in View", frameFingers_TF);
   
      frameTools_TF   = new JTextField(2);
      labelText(framePanel, "Baton", frameTools_TF);
            // a tool is longer, thinner, and straighter than a finger
   
   //    frameGestures_TF = new JTextField(2);
   //    labelText(framePanel, "No. Gestures", frameGestures_TF);
   
      c.add(framePanel);

   }  // end of buildGUI()

   private void createCanvasGUI() {
	   JFrame frame = new JFrame();
	      frame.setSize(500, 500);
	      final Canvas canvas = new Canvas(100, 100);
	      frame.add(canvas);
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      frame.setVisible(true);
	      Thread t = new Thread(new Runnable() {
	          public void run() {

	              // Create a sample listener and controller
	              DrawListener drawListener = new DrawListener(canvas, sketch);
	              Controller controller = new Controller();

	              // Have the sample listener receive events from the
	              // controller
	              controller.addListener(drawListener);

	              // Keep this process running until Enter is pressed
	              System.out.println("Press Enter to quit...");
	              try {
	                  System.in.read();
	              } catch (IOException e) {
	                  e.printStackTrace();
	              }
	              // Remove the sample listener when done
	              controller.removeListener(drawListener);
	          }
	      });
	      t.start();
   }

   private void labelText(JPanel p, String label, JTextField tf)
   { p.add( new JLabel(label + ":"));
      p.add(tf);
   }


   // ---------------- update the fields ---------------

   public void clear()
    // clear all the textfields
   {
      frameID_TF.setText(""); 
      timeHands_TF.setText("");     frameHands_TF.setText("");
      frameFingers_TF.setText("");  frameTools_TF.setText("");
      frameGestures_TF.setText("");
   
      handFingers_TF.setText("");   handTips_TF.setText("");
      handRadius_TF.setText(""); 
      handPalm_TF.setText("");      handPalmIB_TF.setText("");
      handPitch_TF.setText("");     handRoll_TF.setText("");
      handYaw_TF.setText("");       handTouchTF.setText("");
      
      centerIs.setText("");         wristMoveNotify.setText("");
      goDirection.setText("");
   
      circle_CB.setSelected(false);
      circleDir_TF.setText("");     circleID_TF.setText("");
      circleProgess_TF.setText(""); circleRadius_TF.setText("");
      circleAngle_TF.setText("");   circleState_TF.setText("");
   
      swipe_CB.setSelected(false);
      swipeID_TF.setText("");       swipePosn_TF.setText("");
      swipeDir_TF.setText("");      swipeState_TF.setText("");
      swipeSpeed_TF.setText("");
   
      tap_CB.setSelected(false);
      tapID_TF.setText("");         tapPosn_TF.setText("");
      tapDir_TF.setText("");        tapState_TF.setText("");
      tapFingerID_TF.setText("");
   
      keyTap_CB.setSelected(false);
      keyTapID_TF.setText("");      keyTapPosn_TF.setText("");
      keyTapDir_TF.setText("");     keyTapState_TF.setText("");
      keyTapFingerID_TF.setText("");
   }  // end of clear()

   /* Handles button press events */
   public void actionPerformed(ActionEvent evt) {
	    Object src = evt.getSource();
	    if (src == twoFourButton) {
	    	sketch.changeBackground(24);
	    	//createCanvasGUI();
	    	//System.out.println("Two Four");
	    } else if (src == threeFourButton) {
	    	sketch.changeBackground(34);
	    	//System.out.println("Three Four");
	    } else if (src == fourFourButton) {
	    	sketch.changeBackground(44);
	    	//System.out.println("Four Four");
	    }
	  }

   public void setFrameInfo(int fCount, boolean tool)
   {
      String hasTool = "";
      frameFingers_TF.setText(""+fCount); frameTools_TF.setText(""+hasTool);
   }  // end of setFrameInfo();


   public void setHandInfo(int fCount, Vector tipsPos)
   {
      handFingers_TF.setText(""+fCount); handTips_TF.setText(""+tipsPos);
   }  // end of setHandInfo();
   
   public void setFeedbackInfo(double centerPos, String leftOrRight, String yesOrNo) {
      centerIs.setText(""+centerPos);
      goDirection.setText(""+leftOrRight);
      wristMoveNotify.setText(""+yesOrNo);
   }


   public void setCircleInfo(long id, State state, boolean isClockwise, float progress, 
                             float radius, int angle)
   {
      circle_CB.setSelected(true);
      circleID_TF.setText(""+id); 
      String dir = (isClockwise) ? "CW" : "CCW";
      circleDir_TF.setText(dir); 
      circleProgess_TF.setText(""+progress); circleRadius_TF.setText(""+radius);
      circleAngle_TF.setText(""+angle);      circleState_TF.setText(""+state);
   }  // end of setCircleInfo();


   public void setSwipeInfo(long id, State state, Vector posn, Vector dir, float speed)
   {
      swipe_CB.setSelected(true);
      swipeID_TF.setText(""+id);   swipePosn_TF.setText(""+posn);
      swipeDir_TF.setText(""+dir); swipeState_TF.setText(""+state);
      swipeSpeed_TF.setText(""+speed);
   }  // end of setSwipeInfo();


   public void setTapInfo(long id, State state, Vector posn, Vector dir, int fID)
   {
      tap_CB.setSelected(true);
      tapID_TF.setText(""+id);   tapPosn_TF.setText(""+posn);
      tapDir_TF.setText(""+dir); tapState_TF.setText(""+state);
      tapFingerID_TF.setText(""+fID);
      System.out.println("Screen tap(" + fID + "): " + dir + " / " + state);
          // print as well since GUI appearance is so brief
   }  // end of setTapInfo();


   public void setKeyTapInfo(long id, State state, Vector posn, Vector dir, int fID)
   {
      keyTap_CB.setSelected(true);
      keyTapID_TF.setText(""+id);   keyTapPosn_TF.setText(""+posn);
      keyTapDir_TF.setText(""+dir); keyTapState_TF.setText(""+state);
      keyTapFingerID_TF.setText(""+fID);
      System.out.println("Key tap(" + fID + "): " + dir + " / " + state);
          // print as well since GUI appearance is so brief
   }  // end of setKeyTapInfo();


   // ----------------------------------------
} // end of LeapViewer class