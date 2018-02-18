package com.xlreader.shared.io;

import com.xlreader.shared.lang.*;


/**
 Example:
    S_ToBrowserDefault.s_displayURL("http://www.javasoft.com")
 **/

final public class S_ToBrowserDefault
{
    // ---------------------------
    // FINAL STATIC PRIVATE STRING
    
    final static private String _f_s_strClass = "com.xlreader.shared.io.S_ToBrowserDefault.";
    
    // Used to identify the windows platform.
    final static private String _f_s_strOsWindows = "windows";
    final static private String _f_s_strOsLinux = "linux";

    // The default system browser under windows.
    final static private String _f_s_strActionWindows = "rundll32";
    // The flag to display a url.
    final static private String _f_s_strFlagWindows = "url.dll,FileProtocolHandler";

    // The default browser under linux/unix.
    final static private String _f_s_strActionUnix = "netscape";
    // The flag for remote (netscape should be activated).
    final static private String _f_s_strFlagUnix = "-remote openURL";
    
    
     
    
    
    // --------------
    // STATIC PRIVATE
    
    static private String _s_strWarningTitle = null;
    static private String _s_strWarningBody = null;
   
    // ------------------
    // STATIC INITIALIZER
   
    static
    {
        final String f_strBundleFileShort =
        com.xlreader.shared.Shared.f_s_strBundleDir +
        ".S_ToBrowserDefault" // class name
        ;
        
        final String f_strBundleFileLong = f_strBundleFileShort + ".properties";  
    
        try
        {
            java.util.ResourceBundle rbeResources = java.util.ResourceBundle.getBundle(f_strBundleFileShort, 
                java.util.Locale.getDefault());
                
            // resources      
	        _s_strWarningTitle = rbeResources.getString("warningTitle");     
	        _s_strWarningBody = rbeResources.getString("warningBody"); 
	  	}
	    
        
        catch (java.util.MissingResourceException excMissingResource)
        {
            excMissingResource.printStackTrace();
            MySystem.s_printOutExit(_f_s_strClass, "excMissingResource caught");
        }
    }

    // -------------
    // STATIC PUBLIC
    
    static public boolean s_displayURL(String strUrl)
    {    
        String f_strMethod = _f_s_strClass + "s_displayURL(strUrl)";
   
        if (strUrl == null)
        {
            MySystem.s_printOutError(f_strMethod, "nil strUrl");
            return false;
        }

        MySystem.s_printOutTrace(f_strMethod, "strUrl=" + strUrl);

        boolean blnWindows = _s_isWindowsPlatform();  
        
        String strCmd = null;
        
        try
        {            
            if (blnWindows)            
            {
                // strCmd = 'rundll32 url.dll,FileProtocolHandler http://...'
                strCmd = _f_s_strActionWindows + " " + _f_s_strFlagWindows + " " + strUrl;
                Runtime.getRuntime().exec(strCmd);    
                
            }

            /**
                 either linux or unix (mac: not done yet)
            **/            
            else            
            {
                /**
                    modif 17/1/1 bug in linux/rh6.1/kde
                    . if sending an email that contains a subject, only the first word of the subject is taken 
                      in Netscape Messenger

                    ==> ???? TODO: replace all mail-related in the future by JavaMail stuff
                   
                **/


                boolean blnSendMail = strUrl.toLowerCase().startsWith("mailto:");

                if (blnSendMail && _s_isLinuxPlatform())
                {
                    MySystem.s_printOutWarning(f_strMethod, "blnSendMail && _s_isLinuxPlatform: not done yet, handle spaces in subject (if any)");
                }

                strCmd = _f_s_strActionUnix + " " + _f_s_strFlagUnix + "(" + strUrl + ")";

		        MySystem.s_printOutTrace(f_strMethod, "strCmd=" + strCmd);
                Process prc = Runtime.getRuntime().exec(strCmd);                
                
                try
                {
                    int intExitValue = prc.waitFor();
		            MySystem.s_printOutTrace(f_strMethod, "intExitValue=" + intExitValue);

                    if (intExitValue != 0)                    
                    {
                        /**
                             modif 17/1/1 bug in linux/rh6.1/kde
                             . if netscape not already started & sending a mail,
                               then cannot open up netscape even if intExitValue == 0

                             ==> open up a warning_confirm dialog
                        **/

                        if (blnSendMail)
                        {
                           if (! com.xlreader.shared.swing.optionpane.OPAbstract.s_showWarningConfirmDialog((java.awt.Component) null, _s_strWarningTitle, _s_strWarningBody))
                           {
		                        MySystem.s_printOutTrace(f_strMethod, "action cancelled by user, strCmd=" + strCmd);
                                return true;
                           }
                        }

                        else // should be http, ftp, ...
                        {
                            strCmd = _f_s_strActionUnix + " "  + strUrl;
                        } 

		                MySystem.s_printOutTrace(f_strMethod, "strCmd=" + strCmd);
                        Runtime.getRuntime().exec(strCmd);                    
                    }
                }                
                
                catch(InterruptedException excInterrupted)                
                {
                    excInterrupted.printStackTrace();
                    MySystem.s_printOutError(f_strMethod, "excInterrupted caught, strCmd=" + strCmd); 
                    return false;               
                }
            }    
         }        
         
         catch(java.io.IOException excIO)
         {
            excIO.printStackTrace();
            MySystem.s_printOutError(f_strMethod, "excIO caught, strCmd=" + strCmd); 
            return false;     
         }  

         catch(java.lang.Exception exc)
         {
            exc.printStackTrace();
            MySystem.s_printOutError(f_strMethod, "exc caught, strCmd=" + strCmd); 
            return false;     
         }  
        
         
         return true;
     }  
     
    // --------------
    // STATIC PRIVATE
    
    static private boolean _s_isLinuxPlatform() 
    {
        String f_strMethod = _f_s_strClass + "_s_isLinuxPlatform()";
        
        String strOsName = null;
        
        try
        {
            strOsName = System.getProperty("os.name");
        }

        catch (SecurityException excSecurity)
        {
            excSecurity.printStackTrace();
            MySystem.s_printOutExit(f_strMethod, "excSecurity caught");
        }

        if (strOsName == null)
        {
            MySystem.s_printOutExit(f_strMethod, "nil strOsName");
        }
        
        return strOsName.toLowerCase().startsWith(_f_s_strOsLinux);
    }
    
    
    static private boolean _s_isWindowsPlatform()    
    {
        String f_strMethod = _f_s_strClass + "_s_isWindowsPlatform()";
        
        String strOsName = null;
        
        try
        {
            strOsName = System.getProperty("os.name");
        }

        catch (SecurityException excSecurity)
        {
            excSecurity.printStackTrace();
            MySystem.s_printOutExit(f_strMethod, "excSecurity caught");
        }

        if (strOsName == null)
        {
            MySystem.s_printOutExit(f_strMethod, "nil strOsName");
        }
        
        return strOsName.toLowerCase().startsWith(_f_s_strOsWindows);
    }

 }
