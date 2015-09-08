/**
 * 
 */
package com.fanwei.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * @author fanwei
 *
 */
public class Event implements Runnable {

	private static Map<Integer,String> eventList = new HashMap<Integer,String>();
	
	private static Map<Integer,Set<Object>> subscribeList = 
			new HashMap<Integer,Set<Object>>();
	
	private static LinkedList<Integer> publishList =
			new LinkedList<Integer>();
	
	private static Thread th = null;
	Event() {}
	
	/* (non-Javadoc)
	 * constructor
	 */
	public static boolean register(int eventID, String eventName) {
		// TODO Auto-generated method stub
		
		synchronized(eventList) {
			if(eventList.containsKey(eventID))
			{
				eventList.replace(eventID, eventName);
				/*
				 *事件描述更新，需要记日志，预留空白 
				 */
				return true;
			}
			eventList.put(eventID, eventName);
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see com.fanwei.event.EventOperation#publish(int)
	 */
	public static boolean publish(int eventID) {
		// TODO Auto-generated method stub
		synchronized(publishList) {
			publishList.add(eventID);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.fanwei.event.EventOperation#subscribe(int)
	 */
	public static boolean subscribe(Object obj, int eventID) {
		// TODO Auto-generated method stub
		synchronized(subscribeList) {
			if (subscribeList.containsKey(eventID))
			{
				subscribeList.get(eventID).add(obj);
				return true;
			}
			Set<Object> objList = new HashSet<Object>();
			objList.add(obj);
			subscribeList.put(eventID, objList);
		}
		return true;
	}
	
	static {
		th = new Thread(new Event());
		th.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			synchronized(publishList) {
				if(publishList.isEmpty())
					Thread.yield();
				
				
				synchronized(subscribeList) {
					if(subscribeList.isEmpty())
						Thread.yield();
					for(int event : publishList)
					{
						if (subscribeList.containsKey(event))
							for (Object obj : subscribeList.get(event))
								if (obj instanceof EventPerform)
									((EventPerform) obj).perform(event);
					}
				}
				
				publishList.clear();
			}
		}
	}

}
