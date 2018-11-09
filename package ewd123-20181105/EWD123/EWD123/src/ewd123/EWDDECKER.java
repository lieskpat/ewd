package ewd123;

public class EWDDECKER {
	int c1 = 1;
	int c2 = 1;
	int turn = 1;

	/**
	 * Basic constructor initiating two different threads.
	 */
	EWDDECKER() {
		Process1 process1 = new EWDDECKER.Process1();
		Process2 process2 = new EWDDECKER.Process2();
	}

	/**
	 * Inner class representing process 1 from the first mutex strategy, as
	 * described in EWD123.
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
			while (true) { // endless loop!

				c1 = 0;
				
				while (c2 == 0) {
					
					while (turn == 1) {
						Controller.message("wait");
						Controller.sleep(1);
					}
					
					while (turn == 2) {
						Controller.message("wait");
						Controller.sleep(1);
					}
				}
				//https://www.geeksforgeeks.org/operating-system-dekkers-algorithm/
				while (c2 == 0) { // loop as long as the other process is in the critical section
					Controller.message("wait");
					Controller.sleep(1);
				}

				Controller.sleep(1); // simulate some set-up time, improves chances for a clash!

				Controller.enterCS();

				Controller.workInsideCS(5); // simulate work in the critical section

				Controller.leaveCS();
				c1 = 1; // set the guard variable for the other process
				Controller.workOutsideCS(10); // simulate work in the non-critical section

				/* exit the loop/thread with a certain probability */
				if (Controller.randomBoolean(0.1)) { // with a random chance to ...
					break; // ... exit the loop and stop the thread
				}
			}
			Controller.errorMessage("thread is stopping");
		}
	}

	/**
	 * Inner class representing process 2 from the first mutex strategy, as
	 * described in EWD123.
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
			while (true) { // endless loop!

					c2 = 0;

					while (c1 == 0) { // loop as long as the other process is in the critical section
						Controller.message("wait");
						Controller.sleep(1);
					}

					Controller.sleep(1); // simulate some set-up time, improves chances for a clash!

					Controller.enterCS();

					Controller.workInsideCS(3); // simulate work in the critical section

					Controller.leaveCS();
					c2 = 1; // set the guard variable for the other process
					Controller.workOutsideCS(5); // simulate work in the non-critical section
				
			}
			// Controller.errorMessage("thread is stopping");
		}
	}


}
