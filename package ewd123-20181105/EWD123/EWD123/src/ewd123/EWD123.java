/*
 * EWD123.java
 *
 * Copyright (c) 2013-2014 HS Emden/Leer
 * All Rights Reserved.
 *
 * @version 1.10 - 21 Oct 2014 - GJV - updated for changed assignment 
 * @version 1.00 - 07 Oct 2013 - GJV - initial version 
 */

package ewd123;

/**
 *The main class for the series of EWD 123 experiments.
 * <br><code><b>[OMI-PDS]</b></code>
 * 
 * version 1.10 - 21 Oct 2014 
 * 
 * @author Gert Veltink, gert.veltink@hs-emden-leer.de
 */
public class EWD123 {

    /**
     * Sets up an example of mutual exclusion as described in EWD-123.
     * 
     * @param args not used
     */
    public static void main(String[] args) {

        //EWD123a experiment1 = new EWD123a(); 
        //EWD123b experiment2 = new EWD123b();
        EWD123c experiment3 = new EWD123c();
        //Peterson experiment2 = new Peterson(); 
    }

}