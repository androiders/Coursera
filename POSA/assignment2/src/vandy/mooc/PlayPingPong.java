package vandy.mooc;

import java.util.concurrent.CyclicBarrier;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * @class PlayPingPong
 *
 * @brief This class uses elements of the Android HaMeR framework to
 *        create two Threads that alternately print "Ping" and "Pong",
 *        respectively, on the display.
 */
public class PlayPingPong implements Runnable {
    
	private final String TAG = this.getClass().getName();
	
	/**
     * Keep track of whether a Thread is printing "ping" or "pong".
     */
    private enum PingPong {
        PING, PONG
    };

    /**
     * Number of iterations to run the ping-pong algorithm.
     */
    private final int mMaxIterations;

    /**
     * The strategy for outputting strings to the display.
     */
    private final OutputStrategy mOutputStrategy;

    /**
     * Define a pair of Handlers used to send/handle Messages via the
     * HandlerThreads.
     */
    // @@ TODO - you fill in here.
    private final Handler[] mHandlers = new Handler[PingPong.values().length];

    /**
     * Define a CyclicBarrier synchronizer that ensures the
     * HandlerThreads are fully initialized before the ping-pong
     * algorithm begins.
     */
    // @@ TODO - you fill in here.
    private final CyclicBarrier mBarrier = new CyclicBarrier(PingPong.values().length);

    /**
     * Implements the concurrent ping/pong algorithm using a pair of
     * Android Handlers (which are defined as an array field in the
     * enclosing PlayPingPong class so they can be shared by the ping
     * and pong objects).  The class (1) extends the HandlerThread
     * superclass to enable it to run in the background and (2)
     * implements the Handler.Callback interface so its
     * handleMessage() method can be dispatched without requiring
     * additional subclassing.
     */
    class PingPongThread extends HandlerThread implements Handler.Callback {
        /**
         * Keeps track of whether this Thread handles "pings" or
         * "pongs".
         */
        private PingPong mMyType;

        /**
         * Number of iterations completed thus far.
         */
        private int mIterationsCompleted = 0;

        /**
         * Constructor initializes the superclass and type field
         * (which is either PING or PONG).
         */
        public PingPongThread(PingPong myType) {
        	super(myType.toString());
        	// @@ TODO - you fill in here.
         	mMyType = myType;
        }

        /**
         * This hook method is dispatched after the HandlerThread has
         * been started.  It performs ping-pong initialization prior
         * to the HandlerThread running its event loop.
         */
        @Override    
        protected void onLooperPrepared() {
            // Create the Handler that will service this type of
            // Handler, i.e., either PING or PONG.
        	// @@ TODO - you fill in here.
        	mHandlers[mMyType.ordinal()] = new Handler(this);
        	
            try {
                // Wait for both Threads to initialize their Handlers.
            	// @@ TODO - you fill in here.
            	mBarrier.await();                
                
            } catch (Exception e) {
                e.printStackTrace();
            }

            //if we are not PING just return
            if(mMyType == PingPong.PONG)
            	return;
            
            // Start the PING_THREAD first by (1) creating a Message
            // where the PING Handler is the "target" and the PONG
            // Handler is the "obj" to use for the reply and (2)
            // sending the Message to the PING_THREAD's Handler.
            // @@ TODO - you fill in here.
            Handler pingHandler = mHandlers[PingPong.PING.ordinal()];
            Handler pongHandler = mHandlers[PingPong.PONG.ordinal()];
            Message msg = pingHandler.obtainMessage();
            msg.setTarget(pingHandler);
            msg.obj = pongHandler;
            msg.sendToTarget();
            
        }

        /**
         * Hook method called back by HandlerThread to perform the
         * ping-pong protocol concurrently.
         */
        public boolean handleMessage(Message reqMsg) {
            // Print the appropriate string if this thread isn't done
            // with all its iterations yet.
            // @@ TODO - you fill in here, replacing "true" with the
            // appropriate code.
            if (mIterationsCompleted < mMaxIterations) {

            	mOutputStrategy.print("\n" + mMyType.toString() + "(" + String.valueOf(mIterationsCompleted) + ")");
            	mIterationsCompleted++;
            	
            } else {
            	
            	Log.i(TAG, "Thread " + mMyType.toString() + " shutting down");
            	// Shutdown the HandlerThread so the main PingPong
                // thread can join with it.
            	// @@ TODO - you fill in here.
            	
            	Handler newTarget = (Handler)reqMsg.obj;
            	if(newTarget != null && newTarget.getLooper().getThread().isAlive())
            		newTarget.getLooper().quit();
            	
            	Looper.myLooper().quit();
            	//just prettify the screen print :)
            	mOutputStrategy.print("\n");
            	return false;
            }

            // Create a Message that contains the Handler as the
            // reqMsg "target" and our Handler as the "obj" to use for
            // the reply.
            // @@ TODO - you fill in here.
            Handler myHandler = mHandlers[mMyType.ordinal()];
            Handler newTarget = (Handler)reqMsg.obj;
            Message msg = newTarget.obtainMessage();
            msg.setTarget(newTarget);
            msg.obj = myHandler;

            // Return control to the Handler in the other
            // HandlerThread, which is the "target" of the msg
            // parameter.
            // @@ TODO - you fill in here.
            msg.sendToTarget();
            //newTarget.sendMessage(msg);

            return true;
        }
    }

    /**
     * Constructor initializes the data members.
     */
    public PlayPingPong(int maxIterations,
                        OutputStrategy outputStrategy) {
        // Number of iterations to perform pings and pongs.
        mMaxIterations = maxIterations;

        // Strategy that controls how output is displayed to the user.
        mOutputStrategy = outputStrategy;
    }

    /**
     * Start running the ping/pong code, which can be called from a
     * main() method in a Java class, an Android Activity, etc.
     */
    public void run() {
        // Let the user know we're starting. 
        mOutputStrategy.print("Ready...Set...Go!");
       
        // Create the ping and pong threads.
        // @@ TODO - you fill in here.
        PingPongThread ping = new PingPongThread(PingPong.PING);
        PingPongThread pong = new PingPongThread(PingPong.PONG);

        // Start ping and pong threads, which cause their Looper to
        // loop.
        // @@ TODO - you fill in here.
        ping.start();
        pong.start();

        // Barrier synchronization to wait for all work to be done
        // before exiting play().
        // @@ TODO - you fill in here.
        try{
        	ping.join();
        	pong.join();
        }catch(InterruptedException ie){
        	
        }
        
        
        
        // Let the user know we're done.
        mOutputStrategy.print("Done!");
    }
}
