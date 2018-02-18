package com.xlreader.shared.lang;

import java.lang.*;
import java.util.*;
import java.text.*;

public class MySystem
{
    // ------------------
    // STATIC INITIALIZER
    
    static private String _s_strDateStart = null;
    
    static
    {
        _s_strDateStart = s_getDateCurrent();
    }
    
    // ---------------------------
    // FINAL STATIC PRIVATE STRING
    
    final static private String _f_s_strClass = "com.xlreader.shared.lang.MySystem.";
    

    
    final static private String _f_s_strBundleFileShort =
        com.xlreader.shared.Shared.f_s_strBundleDir +
        ".MySystem" // class name
        ;
        
    final static private String _f_s_strBundleFileLong = _f_s_strBundleFileShort + ".properties";

     
    
    // --------------
    // STATIC PRIVATE
    
    // (used by "main" method)
    
    // ---------------------
    // begin resource bundle
    
    // dialog error wrong JVM
    static private String _s_strDialogErrorJvmBody1 = null;
    static private String _s_strDialogErrorJvmBody3 = null;
    //static private String _s_strDialogErrorJvmBody5 = null;
    
    
    // dialog error exiting
    static private String _s_strDialogErrorExitTitle = null;
    static private String _s_strDialogErrorExitBody = null;
    
    
    /**
        memo: tested under 1.3.0 final & 1.3.1-beta
    **/
    static public boolean s_checkJvmVersion(String strTitleApplication)
    {
        String f_strMethod = _f_s_strClass + "s_checkJvmVersion(strTitleApplication)";
        
        final String[] f_strsJavaVersionTested = { "1.3.0", "1.3.1" };
        
        String strJavaVersion = System.getProperty("java.version");
        
        
        for (int i=0; i<f_strsJavaVersionTested.length; i++)
        {
            if (strJavaVersion.startsWith(f_strsJavaVersionTested[i]))
                return true;
        }
        
        // ----
        
        for (int i=2; i<10; i++)
        {
            if (strJavaVersion.startsWith("1.3." + Integer.toString(i)))
            {
                MySystem.s_printOutWarning(f_strMethod, "NOT YET TESTED UNDER JVM:" + strJavaVersion);
                return true;
            }
        }
                
        for (int i=4; i<10; i++)
        {
            if (strJavaVersion.startsWith("1." + Integer.toString(i)))
            {
                MySystem.s_printOutWarning(f_strMethod, "NOT YET TESTED UNDER JVM:" + strJavaVersion);
                return true;
            }
        }
                
                
        MySystem.s_printOutError(f_strMethod, "wrong java version: " + strJavaVersion + ", should be " + f_strsJavaVersionTested[0]  + ", or higher");   
        
        /**
            memo: JVM 1.1.7A (webgain): dialog works ok
        **/
        if (! strJavaVersion.startsWith("1.0"))
        {

            com.xlreader.shared.awt.MyToolkit.s_beep();	
            String strErrorJvmBody2 = "\n    " + strJavaVersion + "\n\n";
            String strErrorJvmBody4 = "\n    " + f_strsJavaVersionTested[0] + "\n\n";
            String strBody = _s_strDialogErrorJvmBody1 + strErrorJvmBody2 + _s_strDialogErrorJvmBody3 + strErrorJvmBody4 /*+ _s_strDialogErrorJvmBody5*/;
            com.xlreader.shared.swing.optionpane.OPAbstract.s_showErrorDialog(null, strTitleApplication, strBody);
        }
        
        // ending
        return false;
    }
    
    static
    {
        String f_strMethod = _f_s_strClass + "initializer";
        
        try
        {
            java.util.ResourceBundle rbeResources = java.util.ResourceBundle.getBundle(_f_s_strBundleFileShort, 
               java.util.Locale.getDefault());
            
            _s_strDialogErrorJvmBody1 = rbeResources.getString("dialogErrorJvmBody1");
            _s_strDialogErrorJvmBody3 = rbeResources.getString("dialogErrorJvmBody3");
            //_s_strDialogErrorJvmBody5 = rbeResources.getString("dialogErrorJvmBody5");
            
            _s_strDialogErrorExitTitle = rbeResources.getString("dialogErrorExitTitle");
            _s_strDialogErrorExitBody = rbeResources.getString("dialogErrorExitBody");
        }
        
        catch (java.util.MissingResourceException excMissingResource)
        {
            excMissingResource.printStackTrace();
            MySystem.s_printOutExit(f_strMethod, "excMissingResource caught");
            
        }
    } 
    
    
    // -------------
    // STATIC PUBLIC
    
    static public String s_getFileSeparator()
    {
        if (_s_strFileSeparator == null)
            _s_getFileSeparator();  
        
        return _s_strFileSeparator;
    }
    
    static public String s_getDateFromTime(long lngTime)
    {
        final String f_strWhere = _f_s_strClass + "s_getDateFromTime(lngTime)";
        
        Date dte = new Date(lngTime);
        
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.FULL,
            java.util.Locale.getDefault());
            
        DateFormat timeFormatter = DateFormat.getTimeInstance(
				DateFormat.SHORT, java.util.Locale.getDefault());
        
        
        String dateOut = dateFormatter.format(dte);
        String timeOut = timeFormatter.format(dte);

        return dateOut + " " + timeOut;
    }
    
    static public String s_getDateFromTime(String strTime)
    {
        final String f_strWhere = _f_s_strClass + "s_getDateFromTime(strTime)";
        
        if (strTime == null)
        {
            MySystem.s_printOutError(f_strWhere, "nil strTime");
            return null;
        }
        
        //long lng = Long.parseLong(strTime);
        
        return s_getDateFromTime(Long.parseLong(strTime));
    }
    
     /**
        ENGLISH FORMAT
        EG: 18:35 2000-02-10
    **/
    
    static public String s_getDateCurrent()
    {
        return s_getDate(new Date());
    }
    
    static public String s_getDate(Date dat)
    {
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.FULL,
            java.util.Locale.getDefault());
            
        DateFormat timeFormatter = DateFormat.getTimeInstance(
				DateFormat.SHORT, java.util.Locale.getDefault());
        
        String strDateOut = dateFormatter.format(dat);
        String strTimeOut = timeFormatter.format(dat);

        return strDateOut + " " + strTimeOut;
    }
    
    // used by statics
    static public void s_printOutTrace(String strWhat, String strMessage)
    {
        if (_S_BLN_PRINTOUTTRACE)
            _s_printOut("> TRACE", strWhat, strMessage);
    }
    
    static public void s_printOutWarning(String strWhat, String strMessage)
    {
        if (_S_BLN_PRINTOUTWARNING)
            _s_printOut("! WARNING", strWhat, strMessage);
    }
    
    static public void s_printOutError(String strWhat, String strMessage)
    {
        if (_S_BLN_PRINTOUTERROR)
            _s_printOut("? ERROR", strWhat, strMessage);
    }
    
    /**
        1) print current error in buffer
        2) save buffer in "error.log"
        3) shows a error dialog
        4) exit
    **/
    
    static public void s_printOutExit(String strWhat, String strMessage)
    {
        // 1) print current error in buffer
        if (_S_BLN_PRINTOUTERROR)
             _s_printOut("? ERROR", strWhat, strMessage);
             
        _s_printOutExit();
            
        
    }
    
    // used by non-statics
    static public void s_printOutTrace(Object obj, String strMethod, String strMessage)
    {
        if (_S_BLN_PRINTOUTTRACE)
            _s_printOut("> TRACE", obj.getClass().getName() + "." + strMethod, strMessage);
    }
    
    static public void s_printOutWarning(Object obj, String strMethod, String strMessage)
    {
        if (_S_BLN_PRINTOUTWARNING)
            _s_printOut("! WARNING", obj.getClass().getName() + "." + strMethod, strMessage);
    }
    
    static public void s_printOutError(Object obj, String strMethod, String strMessage)
    {
        if (_S_BLN_PRINTOUTERROR)
            _s_printOut("? ERROR", obj.getClass().getName() + "." + strMethod, strMessage);
    }
    
    
    /**
        1) print current error in buffer
        2) save buffer in "error.log"
        3) shows a error dialog
        4) exit
    **/
    
    static public void s_printOutExit(Object obj, String strMethod, String strMessage)
    {
        // 1) print current error in buffer
        if (_S_BLN_PRINTOUTERROR)
            _s_printOut("? ERROR", obj.getClass().getName() + "." + strMethod, strMessage);
            
        _s_printOutExit();
    }
    
    // --------------
    // STATIC PRIVATE
    
    static private String _s_strFileSeparator = null;
    
    static private int _S_INT_PRINTOUTCOUNT = 1;
    static private boolean _S_BLN_PRINTOUTTRACE = true;
    static private boolean _S_BLN_PRINTOUTWARNING = true;
    static private boolean _S_BLN_PRINTOUTERROR = true;
    
    static private void _s_getFileSeparator()
    {
        String strWhere = _f_s_strClass + "_s_getFileSeparator()";
        
        try
        {
            _s_strFileSeparator = System.getProperty("file.separator");      
        }
        
        catch (SecurityException excSecurity)
        {
            excSecurity.printStackTrace();
            s_printOutExit(strWhere, "excSecurity caught");
        }
        
        if (_s_strFileSeparator == null)
        {
            s_printOutExit(strWhere, "nil _s_strFileSeparator");
            
        }
    
    }
    
    static private void _s_printOut(String strWhat, String strWhere, String strMessage)
    {
        String str = "\n" + _S_INT_PRINTOUTCOUNT + " " + strWhat;
        str += " (instance started: " + _s_strDateStart + ")";
        System.out.println(str);
        System.out.println(" . location: " + strWhere);
        System.out.println(" . message: " + strMessage);
        _S_INT_PRINTOUTCOUNT ++;
    }
    
    /**
        memo: error caught, forcing an exit
    **/
    static private void _s_printOutExit()
    {
        //2) save all errors in "error.log"
        
        //3) shows a error dialog
        //String strApplicationTitle = "[application.title]";
        //String strErrorBody = "an error occured, a log of the error is located on [appli.home]/usr/[user.name]/[appli.shortname]/error.log";
        
        // -----
        // TRICK
        
        String strApplicationTitle = "application";
        
        if (_s_strDialogErrorExitTitle != null)
            strApplicationTitle = _s_strDialogErrorExitTitle;
            
        String strApplicationBody = "An error occured in the application.";
        strApplicationBody += "\n";
        strApplicationBody += "The application will now terminate";
        strApplicationBody += "\n\n";
        strApplicationBody += "Please contact your system administrator";
        
        if (_s_strDialogErrorExitBody != null)
            strApplicationBody = _s_strDialogErrorExitBody;
        
        com.xlreader.shared.swing.optionpane.OPAbstract.s_showErrorDialog((java.awt.Component) null, strApplicationTitle, strApplicationBody);

        //4) exit
    
        System.exit(1); // "1" ==> wrong exit, "0" ==>  correct exit
    }
}