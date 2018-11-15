package ewd123;

public class EWDDECKER {
	// c1 outside cs
	int c1 = 1;
	// c2 outside cs
	int c2 = 1;
	// which thread will enter next cs
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
				// c1 inside cs
				c1 = 0;

				// if c2 inside cs
				while (c2 == 0) {

					// if thread 1 will enter next
					if (turn == 1) {
						// c1 outside cs
						c1 = 1;

						// wait thread 2 will enter next
						while (turn == 2) {
							Controller.message("wait");
							Controller.sleep(1);
						}
						// c1 inside cs
						c1 = 0;
					}
				}

				// https://www.geeksforgeeks.org/operating-system-dekkers-algorithm/

				Controller.sleep(1); // simulate some set-up time, improves chances for a clash!

				Controller.enterCS();

				Controller.workInsideCS(5); // simulate work in the critical section

				Controller.leaveCS();
				// thread 2 will enter next
				turn = 2;
				// c1 outside cs
				c1 = 1;

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
				// c2 inside cs
				c2 = 0;

				// if c1 inside cs
				while (c1 == 0) {

					// thread 2 enter next cs
					if (turn == 2) {
						continue;
					}

					// c2 outside cs
					c2 = 1;

					// thread 1 enter next
					while (turn == 1) { // loop as long as the other process is in the critical section
						Controller.message("wait");
						Controller.sleep(1);
					}

					// c2 inside cs
					c2 = 0;
				}

				Controller.sleep(1); // simulate some set-up time, improves chances for a clash!

				Controller.enterCS();

				Controller.workInsideCS(3); // simulate work in the critical section

				Controller.leaveCS();
				// thread 1 enter next cs
				turn = 1;
				// c2 outside cs
				c2 = 1;

				Controller.workOutsideCS(5); // simulate work in the non-critical section

			}
		}
	}

}
