import processing.core.*;
import de.voidplus.leapmotion.*;

public class GUISketch extends PApplet {
	LeapMotion leap;
	
	public void setup() {
		size(800, 550);  // w, h
		background(255);
		PImage img;
		img = loadImage("44pattern.jpg");
		img.resize(800, 550);
		//background(img);
		leap = new LeapMotion(this);
	}
	
	public void draw() {
		background(255);
		PImage img;
		img = loadImage("44pattern.jpg");
		img.resize(800, 550);
		//background(img);
		int fps = leap.getFrameRate();
		
		// ========= HANDS =========
		
		for(Hand hand : leap.getHands()) {
	        int     hand_id          = hand.getId();
	        PVector hand_position    = hand.getPosition();
	        PVector hand_stabilized  = hand.getStabilizedPosition();
	        PVector hand_direction   = hand.getDirection();
	        PVector hand_dynamics    = hand.getDynamics();
	        float   hand_roll        = hand.getRoll();
	        float   hand_pitch       = hand.getPitch();
	        float   hand_yaw         = hand.getYaw();
	        boolean hand_is_left     = hand.isLeft();
	        boolean hand_is_right    = hand.isRight();
	        float   hand_grab        = hand.getGrabStrength();
	        float   hand_pinch       = hand.getPinchStrength();
	        float   hand_time        = hand.getTimeVisible();
	        PVector sphere_position  = hand.getSpherePosition();
	        float   sphere_radius    = hand.getSphereRadius();
	        
	        hand.draw();
	        hand.drawSphere();
	        
	     // ========= FINGERS =========

	        for(Finger finger : hand.getOutstrechtedFingers()){
	        					// was hand.getFingers()
	
	            // ----- BASICS -----
	
	            int     finger_id         = finger.getId();
	            PVector finger_position   = finger.getPosition();
	            PVector finger_stabilized = finger.getStabilizedPosition();
	            PVector finger_velocity   = finger.getVelocity();
	            PVector finger_direction  = finger.getDirection();
	            float   finger_time       = finger.getTimeVisible();
	            
	            // ========= ARM =========

	            if(hand.hasArm()){
	                Arm     arm               = hand.getArm();
	                float   arm_width         = arm.getWidth();
	                PVector arm_wrist_pos     = arm.getWristPosition();
	                PVector arm_elbow_pos     = arm.getElbowPosition();
	            }


	            // ----- SPECIFIC FINGER -----

	            switch(finger.getType()){
	                case 0:
	                    // System.out.println("thumb");
	                    break;
	                case 1:
	                    // System.out.println("index");
	                    break;
	                case 2:
	                    // System.out.println("middle");
	                    break;
	                case 3:
	                    // System.out.println("ring");
	                    break;
	                case 4:
	                    // System.out.println("pinky");
	                    break;
	            }


	            // ----- SPECIFIC BONE -----

	            Bone    bone_distal       = finger.getDistalBone();
	            // or                       finger.get("distal");
	            // or                       finger.getBone(0);

	            Bone    bone_intermediate = finger.getIntermediateBone();
	            // or                       finger.get("intermediate");
	            // or                       finger.getBone(1);

	            Bone    bone_proximal     = finger.getProximalBone();
	            // or                       finger.get("proximal");
	            // or                       finger.getBone(2);

	            Bone    bone_metacarpal   = finger.getMetacarpalBone();
	            // or                       finger.get("metacarpal");
	            // or                       finger.getBone(3);


	            // ----- DRAWING -----

	             finger.draw(20); // = drawLines()+drawJoints()
	             finger.drawLines();
	             finger.drawJoints();

	          // ========= TOOLS =========
	        
			for(Tool tool : hand.getTools()){
	            int     tool_id           = tool.getId();
	            PVector tool_position     = tool.getPosition();
	            PVector tool_stabilized   = tool.getStabilizedPosition();
	            PVector tool_velocity     = tool.getVelocity();
	            PVector tool_direction    = tool.getDirection();
	            float   tool_time         = tool.getTimeVisible();
	            
	            tool.draw();
			}
			
		}
	}
}
}
