/*
 * EWD123a.java
 *
 * Copyright (c) 2013-2014 HS Emden/Leer
 * All Rights Reserved.
 *
 * @version 1.20 - 30 Oct 2014 - GJV - updated to EWD123a instead of EWD35a 
 * @version 1.10 - 27 Oct 2014 - GJV - updated for changed assignment 
 * @version 1.00 - 07 Oct 2013 - GJV - initial version 
 */

package ewd123;

/**
 * A class to represent the first mutex strategy, as described in EWD123.
 * <br><code><b>[OMI-PDS]</b></code>
 * 
 * version 1.20 - 30 Oct 2014 
 * 
 * @author Gert Veltink, gert.veltink@hs-emden-leer.de
 */
public class EWD123a {
    
    int turn = 1;  // the guarding variable


    /**
     * Basic constructor initiating two different threads.
     */
    EWD123a() {
        Process1 process1 = new EWD123a.Process1();
        Process2 process2 = new EWD123a.Process2();
    }
    

    /**
     * Inner class representing process 1 from the first mutex strategy, as described in EWD123.
     */
    public class Process1 implements Runnable {

        Thread myThread;
         
        /**
         * Basic constructor for process 1.
         */
        Process1() { 
            myThread = new Thread(this, "1"); 
            myThread.start(); 
        }  
        
        /**
         * The endless loop representing the work to be done by process A.
         */
        public void run() { 
            while (true) {                              // endless loop! 
                
                while (turn == 2) {                     // loop as long as the other process is in the critical section
                    Controller.message("wait");
                    Controller.sleep(1);
                }
                
                Controller.sleep(1);                    // simulate some set-up time, improves chances for a clash!
                
                Controller.enterCS();        

                Controller.workInsideCS(5);             // simulate work in the critical section

                Controller.leaveCS();
                turn = 2;                               // set the guard variable for the other process

                Controller.workOutsideCS(10);           // simulate work in the non-critical section
                
                /* exit the loop/thread with a certain probability */
                if (Controller.randomBoolean(0.1)) {    // with a random chance to ...
                    break;                              // ... exit the loop and stop the thread
                }
            } 
            Controller.errorMessage("thread is stopping");
        }         
    }

    
    /**
     * Inner class representing process 2 from the first mutex strategy, as described in EWD123.
     */
    private class Process2 implements Runnable {

        Thread myThread; 
         
        /**
         * Basic constructor for process 2.
         */
        Process2() { 
            myThread = new Thread(this, "2"); 
            myThread.start(); 
        }  
        
        /**
         * The endless loop representing the work to be done by process B.
         */
        public void run() { 
            while (true) {                              // endless loop! 
                
                while (turn == 1) {                     // loop as long as the other process is in the critical section
                    Controller.message("wait");
                    Controller.sleep(1);
                }
                
                Controller.sleep(1);                    // simulate some set-up time, improves chances for a clash!
                
                Controller.enterCS();        

                Controller.workInsideCS(3);             // simulate work in the critical section

                Controller.leaveCS();
                turn = 1;                               // set the guard variable for the other process

                Controller.workOutsideCS(5);            // simulate work in the non-critical section
            } 
            // Controller.errorMessage("thread is stopping");
        }         
    }
}
