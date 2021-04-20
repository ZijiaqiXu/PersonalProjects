package edu.stevens.cs549.dhts.remote;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import edu.stevens.cs549.dhts.main.IShell;
import edu.stevens.cs549.dhts.main.Log;

public class ControllerClient extends Endpoint implements MessageHandler.Whole<String> {

	public static final Logger logger = Logger.getLogger(ControllerClient.class.getCanonicalName());

	private final CountDownLatch messageLatch = new CountDownLatch(1);

	private final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
	.encoders(Collections.singletonList(CommandLineEncoder.class))
	.build();
	
	private final ShellManager shellManager = ShellManager.getShellManager();

	private IShell shell;

	private boolean initializing = true;
		
	private Session session;

	public ControllerClient(IShell shell) {
		this.shell = shell;
	}
	
	public void connect(URI uri) throws DeploymentException, IOException {
		try {
			shell.msg("Requesting control of node at " + uri.toString() + "...");
			
			// make connection request
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();

			
			container.connectToServer(this, cec, uri);

			while (true) {
				try {
					// Synchronize with receipt of ACK from the remote node.
					boolean connected = messageLatch.await(100, TimeUnit.SECONDS);
					// If we are connected, a new top level shell has been pushed, execute its CLI.
					// Be sure to return when done, to exit the loop.
					if (connected) {
						shellManager.getCurrentShell().cli();
						return;
					}

				} catch (InterruptedException e) {
					// Keep on waiting for the specified time interval
				}
			}
		} catch (IOException e) {
			shell.err(e);
		}
	}
	
	protected void endInitialization() {
		initializing = false;
		messageLatch.countDown();
	}

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		// cache the session for use by some of the other operations.
		this.session = session;
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			@Override
			public void onMessage(String message) {
				if (initializing) {
					if (SessionManager.ACK.equals(message)) {
						/*
						 * server has accepted our remote control requests
						 */
						// cache the ProxyShell, and push it on the shell stack
						shell = ProxyShell.createRemoteController(shellManager.getCurrentShell(), session.getBasicRemote());
						shellManager.addShell(shell);
						
						// end initializing flag
						endInitialization();
						
						// unblock the thread
						messageLatch.countDown();

					} else {
						// If the server rejects our request, they will just close the channel.
						throw new IllegalStateException("Unexpected response to remote control request: " + message);
					}
				} else {
					// controller client already initialized
					try {
						shell.msgln("Message received from remotely controlled server: " + message);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}); 
	}

	@Override
	public void onMessage(String message) {
		if (initializing) {
			if (SessionManager.ACK.equals(message)) {
				/*
				 * server has accepted our remote control requests
				 */
				// cache the ProxyShell, and push it on the shell stack
				shell = ProxyShell.createRemoteController(shellManager.getCurrentShell(), session.getBasicRemote());
				shellManager.addShell(shell);
				
				// end initializing flag
				endInitialization();
				
				// unblock the thread
				messageLatch.countDown();

			} else {
				// If the server rejects our request, they will just close the channel.
				throw new IllegalStateException("Unexpected response to remote control request: " + message);
			}
		} else {
			// controller client already initialized
			try {
				shell.msgln("Message received from remotely controlled server: " + message);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	} 
	
	@Override
	public void onClose(Session session, CloseReason reason) {
		Log.info("Server closed Websocket connection: "+reason.getReasonPhrase());
		try {
			shutdown();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failure while trying to report connection error.", e);
		}
	}
	
	@Override
	public void onError(Session session, Throwable t) {
		try {
			shell.err(t);
			shutdown();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failure while trying to report connection error.", t);
		}
	}
	
	protected void shutdown() throws IOException {
		if (initializing) {
			Log.info("...shutdown of remote control request.");
			shell.msgln("request rejected.");
			endInitialization();
		} else if (!shell.isTerminated()) {
			Log.info("...removing shell proxy from shell stack.");
			shellManager.removeShell();
		}
	}

}
