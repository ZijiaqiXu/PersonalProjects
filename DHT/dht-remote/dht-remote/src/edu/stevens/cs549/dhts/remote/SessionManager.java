package edu.stevens.cs549.dhts.remote;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.Session;

import edu.stevens.cs549.dhts.main.LocalContext;
import edu.stevens.cs549.dhts.main.LocalShell;

/**
 * Maintain a stack of shells.
 * @author dduggan
 *
 */
public class SessionManager {
	
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SessionManager.class.getCanonicalName());
	
	public static final String ACK = "ACK";
	
	private static final SessionManager SESSION_MANAGER = new SessionManager();
	
	public static SessionManager getSessionManager() {
		return SESSION_MANAGER;
	}
	
	private Lock lock = new ReentrantLock();
	
	private ControllerServer currentServer;
	
	public boolean isSession() {
		return currentServer != null;
	}

	public Session getCurrentSession() {
		return currentServer != null ? currentServer.getSession() : null;
	}

	public boolean setCurrentSession(ControllerServer server) {
		lock.lock();
		try {
			if (currentServer == null) {
				currentServer = server;
				return true;
			} else {
				return false;
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void acceptSession() throws IOException {
		lock.lock();
		try {
			/*  
			 * The CLI of the newly installed shell
			 *  will be executed by the underlying CLI as part of the "accept" command.
			 */
			// create remotely controlled local shell with info from current local shell and basic of the client
			LocalShell shell = LocalShell.createRemotelyControlled(LocalContext.toplevel, 
					ProxyContext.createProxyContext(getCurrentSession().getBasicRemote()));
			
			// push the shell on the shell stack
			ShellManager.getShellManager().addShell(shell);
			
			// end initializing flag
			currentServer.endInitialization();
			
			// confirm acceptance by sending ACK to the client
			currentServer.getSession().getBasicRemote().sendText(ACK);

		} finally {
			lock.unlock();
		}
	}
	
	public static void rejectSession(Session session) {
		try {
			// reject remote control request by closing the session
			session
			.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Remote control request rejected: there is pending request on the server!"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void rejectSession() {
		lock.lock();
		try {
			// reject the pending remote control request by closing the session
			getCurrentSession()
			.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Remote control request rejected!"));
			
			// set current session to null(need?)
			currentServer = null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void closeCurrentSession() {
		lock.lock();
		try {
			// normal shutdown of remote control session 
			getCurrentSession()
			.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Connection closed normally!"));
			
			// set current session to null(need?)
			currentServer = null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

}
