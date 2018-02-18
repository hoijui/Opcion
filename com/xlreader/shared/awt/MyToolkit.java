package com.xlreader.shared.awt;

/**
usage:
 com.xlreader.shared.awt.MyToolkit.s_beep();
**/

public class MyToolkit
{
    public static void s_beep()
    {
        String strWhat = "MyToolkit.s_beep()";
        
        try
		{
	    	java.awt.Toolkit.getDefaultToolkit().beep();
	    }
	    
	    catch(java.awt.AWTError wte)
	    {
	        wte.printStackTrace();
	        com.xlreader.shared.lang.MySystem.s_printOutWarning(strWhat, "exception caught");
	    }
    }
}


