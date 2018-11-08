/*
 * Peterson.java
 *
 * Copyright (c) 2014-2014 HS Emden/Leer
 * All Rights Reserved.
 *
 * @version 1.00 - 28 Oct 2014 - GJV - initial version 
 */

package ewd123;

/**
 * A class to represent the Peterson mutex strategy.
 * <br><code><b>[OMI-PDS]</b></code>
 * 
 * version 1.00 - 28 Oct 2014 
 * 
 * @author Gert Veltink, gert.veltink@hs-emden-leer.de
 */
public class Peterson {
    int turn;  // indicates which process wants to enter the critical region
    boolean interested[] = new boolean[2];  // boolean guards

    /**
     * Basic constructor initiating two different threads.
     */
    Peterson() {
        ProcessA processA = new Peterson.ProcessA();
        ProcessB processB = new Peterson.ProcessB();
    }
    

    /**
     * Petersons's method to enter the critical region.
     */
    public void enterRegion(int thisProcess) {
        int otherProcess = 1 - thisProcess;
        interested[thisProcess] = true;
        turn = thisProcess;
        while (turn == thisProcess && interested[otherProcess]) {  // waiting loop!
            Controller.message("wait");
        };
    }         


    /**
     * Petersons's method to leave the critical region.
     */
    public void leaveRegion(int thisProcess) { 
        interested[thisProcess] = false;
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
            int processID = 0;                          // process A has ID: 0!
            while (true) {                              // endless loop! 
                
                enterRegion(processID);
                
                //Controller.sleep(1);                    // simulate some set-up time, improves chances for a clash!
                
                Controller.enterCS();        
                Controller.workInsideCS(2);             // simulate work in the critical section

                Controller.leaveCS();
                leaveRegion(processID);
                
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
            int processID = 1;                          // process B has ID: 1!
            
            while (true) {                              // endless loop! 
                
                enterRegion(processID);
                
                Controller.sleep(1);                    // simulate some set-up time, improves chances for a clash!
                
                Controller.enterCS();        
                Controller.workInsideCS(3);             // simulate work in the critical section

                Controller.leaveCS();
                leaveRegion(processID);
                
                Controller.workOutsideCS(25);           // simulate work in the non-critical section
            } 
            // Controller.errorMessage("thread is stopping");
        }         
    }
}
