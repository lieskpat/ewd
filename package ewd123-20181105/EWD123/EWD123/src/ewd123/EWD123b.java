package ewd123;

public class EWD123b {

	// c = 0 (inside cs) / 1 (outside cs)
	int c1 = 1;
	int c2 = 1;

	/**
	 * Basic constructor initiating two different threads.
	 */
	EWD123b() {
		Process1 process1 = new EWD123b.Process1();
		Process2 process2 = new EWD123b.Process2();
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

				// wenn c2 == 1, d.h. c2 ist outside cs
				// bevor c1 = 0, d.h. c1 ist inside cs, gesetzt wird
				// startet Prozess 2 (c2)
				// für Prozess 2 ist immer noch c1 == 1, d.h. outside cs
				// Prozess 2 betritt die cs
				// Prozess 1 betritt auch die cs
				while (c2 == 0) { // loop as long as the other process is in the critical section
					Controller.message("wait");
					Controller.sleep(1);
				}

				Controller.sleep(1); // simulate some set-up time, improves chances for a clash!
				// c1 inside cs
				c1 = 0;

				Controller.enterCS();

				Controller.workInsideCS(5); // simulate work in the critical section

				Controller.leaveCS();
				// c1 outside cs
				c1 = 1;
				Controller.workOutsideCS(10); // simulate work in the non-critical section
			}
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

				while (c1 == 0) { // loop as long as the other process is in the critical section
					Controller.message("wait");
					Controller.sleep(1);
				}

				Controller.sleep(1); // simulate some set-up time, improves chances for a clash!
				// c2 inside cs
				c2 = 0;

				Controller.enterCS();

				Controller.workInsideCS(3); // simulate work in the critical section

				Controller.leaveCS();
				// c2 outside cs
				c2 = 1;
				Controller.workOutsideCS(5); // simulate work in the non-critical section
			}
		}
	}
}
