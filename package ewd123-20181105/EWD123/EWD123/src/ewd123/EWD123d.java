package ewd123;

public class EWD123d {
	
	int c1 = 1;  // the guarding variable
	int c2 = 1;

    /**
     * Basic constructor initiating two different threads.
     */
    EWD123d() {
        Process1 process1 = new EWD123d.Process1();
        Process2 process2 = new EWD123d.Process2();
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
                
            	// c1 inside cs
            	c1 = 0;									// L1
            	
            	// if c2 inside cs 
                if (c2 == 0) {                     // loop as long as the other process is in the critical section
                    // c1 outside cs
                	c1 = 1;
                    Controller.message("wait");
					Controller.sleep(1);
                    continue;
                }
                
                Controller.sleep(1);                    // simulate some set-up time, improves chances for a clash!
                
                Controller.enterCS();        

                Controller.workInsideCS(5);             // simulate work in the critical section

                Controller.leaveCS();
                
                // c1 outside cs
                c1 = 1;                               // set the guard variable for the other process

                Controller.workOutsideCS(10);           // simulate work in the non-critical section
            }
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
                
            	//c2 inside cs
            	c2 = 0;
            	
                if (c1 == 0) {                     // loop as long as the other process is in the critical section
                    //c2 outside cs
                	c2 = 1;
                    Controller.message("wait");
					Controller.sleep(1);
                    continue;
                }
                
                Controller.sleep(1);                    // simulate some set-up time, improves chances for a clash!
                
                Controller.enterCS();        

                Controller.workInsideCS(3);             // simulate work in the critical section

                Controller.leaveCS();
                
                // c2 outside cs
                c2 = 1;                               // set the guard variable for the other process

                Controller.workOutsideCS(5);            // simulate work in the non-critical section
            } 
        }         
    }

}
