/**
 * 
 */
package com.fanwei.test;

import com.fanwei.event.Event;
import com.fanwei.event.EventPerform;

/**
 * @author fanwei
 *
 */
public class MultiThreadTest implements EventPerform{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Event.register(1, "Test");
		Event.publish(1);
		Event.subscribe(new MultiThreadTest(), 1);
		Event.publish(2);
		Event.publish(1);
		
	}

	@Override
	public void perform(int eventID) {
		// TODO Auto-generated method stub
		if(eventID == 1)
			System.out.println("Event1 Performed.");
		else
			System.out.println("Event else Performed.");
	}

}
