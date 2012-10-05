package com.kylin.tankwar.jgroups;

import org.apache.log4j.Logger;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import com.kylin.tankwar.core.Event;
import com.kylin.tankwar.core.MainFrame;

public class AsychReceiver extends ReceiverAdapter {
	
	private static final Logger logger = Logger.getLogger(AsychCommunication.class);

	private Session session;
	
	private MainFrame mainFrame;
	
	public AsychReceiver(Session session, MainFrame mainFrame) {
		super();
		this.session = session;
		this.mainFrame = mainFrame;
		
		logger.info("Initialized AsychReceiver");
	}

	public void receive(Message msg) {
		
		if(logger.isDebugEnabled()) {
			logger.debug("handle message, " + msg.printHeaders() + " | " + msg.getSrc() + " | " + msg.toString()) ;
		}
		
		Session rec = (Session) msg.getObject();
		
		if(rec.getEvent() == Event.TM || rec.getEvent() == Event.TN) {
			mainFrame.getHandler().recieveHandler(mainFrame, session, rec);
		} else if(rec.getEvent() == Event.MM ) {
			mainFrame.getHandler().recieveHandler(mainFrame.getMissileMap(), session, rec);
		} else if(rec.getEvent() == Event.EM) {
			mainFrame.getHandler().recieveHandler(mainFrame.getExplodes(), session, rec);
		}else if(rec.getEvent() == Event.DEATH) {
			String missileId = rec.missileIdSet().iterator().next() ;
			String tankId = rec.tankIdSet().iterator().next() ;
			mainFrame.getHandler().recieveHandler(missileId, tankId, session, mainFrame);
		}
		
		//--TODO Explode
	}

	public void viewAccepted(View view) {
		
		logger.info("** view: " + view);
		
		if(logger.isDebugEnabled()) {
			logger.debug(" -> View Id: " + view.getViewId());
			logger.debug(" -> View Creater: " + view.getCreator());
			logger.debug(" -> View Coordinator: " + view.getMembers().get(0));
			logger.debug(" -> View Memembers: " + view.getMembers());
		}
		
	}

}