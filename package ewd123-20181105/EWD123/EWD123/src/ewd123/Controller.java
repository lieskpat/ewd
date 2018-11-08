/*
 * Controller.java
 *
 * Copyright (c) 2014-2014 HS Emden/Leer
 * All Rights Reserved.
 *
 * @version 1.10 - 04 Nov 2014 - GJV - corrected NullPointerException in leaveCS 
 * @version 1.00 - 27 Oct 2014 - GJV - initial version 
 */

package ewd123;

/**
 * A class to 'supervise' and 'control' the processes using the critical section in the EWD 123 experiment. <br>
 * <code><b>[OMI-PDS]</b></code>
 * 
 * version 1.10 - 04 Nov 2014
 * 
 * @author Gert Veltink, gert.veltink@hs-emden-leer.de
 */
public class Controller {
    
    static String current = null;
   
    static final int ERROR_ENTER_NON_EMPTY_CS = -1;
    static final int ERROR_LEAVE_UNRESERVED_CS = -2;
    static final int ERROR_ACTIVE_IN_UNRESERVED_CS = -3;
    static final int ERROR_ACTIVE_OUTSIDE_RESERVED_CS = -4;

    static final double ALWAYS_FALSE = 0.0;
    static final double ALWAYS_TRUE = 1.0;

    
    /**
     * Signals that a thread has entered the critical section.
     */
    public static void enterCS() {
        if (current == null) {
            message("entered critical section");            
            current = Thread.currentThread().getName();
        } else {
            errorMessage("entered non-empty critical section");            
            System.exit(ERROR_ENTER_NON_EMPTY_CS);
        }            
    }

    
    /**
     * Signals that a thread has left the critical section.
     */    
    public static void leaveCS() {
        if (Thread.currentThread().getName().equals(current)) {        
            message("left critical section");            
            current = null; 
        } else {
            errorMessage("left unreserved critical section");            
            System.exit(ERROR_LEAVE_UNRESERVED_CS);
        }
    }

    
    /**
     * Tells a thread to halt operations for a number of milliseconds.
     * 
     * @param millis  the duration of the time spent in milliseconds. 
     */        
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);  // let the thread sleep some time
        } catch (InterruptedException e) {
            errorMessage("thread was interrupted");
        }
    }
    
    
    /**
     * Returns a random boolean value.
     * 
     * @param probability  the probability true is returned. 0: always false, 1: always true.
     * 
     * @return  the random truth value as indicated by the probability
     */        
    public static boolean randomBoolean(double probability) {
        return (Math.random() < probability);
    }

    
    /**
     * Signals that a thread carries out some work within the critical section.
     * 
     * @param millis  the duration of the time spent inside the critical section in milliseconds. 
     */        
    public static void workInsideCS(long millis) {
        if (Thread.currentThread().getName().equals(current)) {        
            message("works inside critical section");            
            try {
                Thread.sleep(millis);  // simulate some work inside the critical section
            } catch (InterruptedException e) {
                errorMessage("thread was interrupted");
            }
        } else {         
            errorMessage("works inside non-reserved critical section");
            System.exit(ERROR_ACTIVE_IN_UNRESERVED_CS);
        }
    }

    
    /**
     * Signals that a thread carries out some work outside the critical section.
     * 
     * @param millis  the duration of the time spent outside the critical section in milliseconds. 
     */        
    public static void workOutsideCS(long millis) {
        if (!Thread.currentThread().getName().equals(current)) {        
            message("works outside critical section");
            try {
                Thread.sleep(millis);  // simulate some work outside the critical section
            } catch (InterruptedException e) {
                errorMessage("thread was interrupted");
            }
        } else {
            System.err.println(); // introduce empty line before
            errorMessage("works outside of reserved critical section while in critical section");
            System.exit(ERROR_ACTIVE_OUTSIDE_RESERVED_CS);
        }
    }

    
    /**
     * Writes a given message to the standard output stream identifying the current thread.
     * 
     * @param msg  the message to be shown. 
     */        
    public static void message(String msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);            
    }

    
    /**
     * Writes a given message to both the standard output and standard error stream identifying the current thread.
     * 
     * @param msg  the message to be shown. 
     */        
    public static void errorMessage(String msg) {
        System.out.println("#Error: " + Thread.currentThread().getName() + ": " + msg);            
        System.err.println("#Error: " + Thread.currentThread().getName() + ": " + msg);            
    }

    
}
