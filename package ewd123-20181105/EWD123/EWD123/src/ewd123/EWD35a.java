/*
 * EWD123.java
 *
 * Copyright (c) 2013-2014 HS Emden/Leer
 * All Rights Reserved.
 *
 * @version 1.10 - 27 Oct 2014 - GJV - updated for changed assignment 
 * @version 1.00 - 07 Oct 2013 - GJV - initial version 
 */

package ewd123;

/**
 * A class to represent the first mutex strategy, as described in EWD123.
 * <br><code><b>[OMI-PDS]</b></code>
 * 
 * version 1.10 - 27 Oct 2014 
 * 
 * @author Gert Veltink, gert.veltink@hs-emden-leer.de
 */
public class EWD35a {
    
    boolean lA;
    boolean lB;

    /**
     * Basic constructor initiating two different threads.
     */
    EWD35a() {
        ProcessA processA = new EWD35a.ProcessA();
        ProcessB processB = new EWD35a.ProcessB();
    }
    

    /**
     * Inner class representing process A from the first mutex strategy, as described in EWD123.
     */
    public class ProcessA implements Runnable {

        Thread myThread; 
         
        /**
         * Basic constructor for process A.
         */
        ProcessA() { 
            myThread = new Thread(this, "A"); 
            myThread.start(); 
        }  
        
        /**
         * The endless loop representing the work to be done by process A.
         */
        public void run() { 
            while (true) {                              // endless loop! 
                
                while (lB) {                            // loop as long as the other process is in the critical section
                    Controller.message("wait");
                    //Controller.sleep(1);
                }
                
                //Controller.sleep(1);                    // simulate some set-up time, improves chances for a clash!
                
                lA = true;                              // set the guard variable 
                Controller.enterCS();        

                Controller.workInsideCS(2);             // simulate work in the critical section

                Controller.leaveCS();
                lA = false;                             // reset the guard variable

                Controller.workOutsideCS(45);           // simulate work in the non-critical section
                
                /* exit the loop/thread with a certain probability */
                if (Controller.randomBoolean(Controller.ALWAYS_FALSE)) {    // with a chance of ...
                    break;                                                  // ... exit the loop
                }
            } 
            Controller.errorMessage("thread is stopping");
        }         
    }

    
    /**
     * Inner class representing process B from the first mutex strategy, as described in EWD123.
     */
    private class ProcessB implements Runnable {

        Thread myThread; 
         
        /**
         * Basic constructor for process B.
         */
        ProcessB() { 
            myThread = new Thread(this, "B"); 
            myThread.start(); 
        }  
        
        /**
         * The endless loop representing the work to be done by process B.
         */
        public void run() { 
            while (true) {                              // endless loop! 
                
                while (lA) {                            // loop as long as the other process is in the critical section
                    Controller.message("wait");
                    //Controller.sleep(1);
                }
                
                Controller.sleep(1);                    // simulate some set-up time, improves chances for a clash!
                
                lB = true;                              // set the guard variable 
                Controller.enterCS();        

                Controller.workInsideCS(3);             // simulate work in the critical section

                Controller.leaveCS();
                lB = false;                             // reset the guard variable

                Controller.workOutsideCS(25);           // simulate work in the non-critical section
            } 
            // Controller.errorMessage("thread is stopping");
        }         
    }
}
