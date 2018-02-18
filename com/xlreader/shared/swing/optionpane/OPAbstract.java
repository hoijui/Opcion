package com.xlreader.shared.swing.optionpane;

import com.xlreader.shared.lang.*;

import javax.swing.JOptionPane;

abstract public class OPAbstract extends JOptionPane
{ 
    // --------------------
    // FINAL STATIC PRIVATE
    
    final static private String _f_s_strClass = "com.xlreader.shared.swing.optionpane.OPAbstract.";
    
    final static private String _f_s_strBundleFileShort =
        com.xlreader.shared.Shared.f_s_strBundleDir +
        ".OPAbstract" // class name
        ;
        
    final static private String _f_s_strBundleFileLong = _f_s_strBundleFileShort + ".properties";

    // ---------------------
    // STATIC PRIVATE STRING
    
    
    static private String _s_strTitleError = null;
    static private String _s_strButtonClose = null;
    static private String _s_strButtonOk = null;
    static private String _s_strButtonCancel = null;
    
    // ------------------
    // STATIC INITIALIZER
    
    static
    {
        
        try
        {
            java.util.ResourceBundle rbeResources = java.util.ResourceBundle.getBundle(_f_s_strBundleFileShort, 
                java.util.Locale.getDefault());
                
            _s_strTitleError = rbeResources.getString("titleError"); 
	        _s_strButtonClose = rbeResources.getString("buttonClose"); 
	        _s_strButtonOk = rbeResources.getString("buttonOk"); 
	        _s_strButtonCancel = rbeResources.getString("buttonCancel"); 
        }
        
        catch (java.util.MissingResourceException excMissingResource)
        {
            excMissingResource.printStackTrace();
            MySystem.s_printOutExit(_f_s_strClass, "excMissingResource caught");
        }
    } 
    
    // -------------
    // STATIC PUBLIC
    
    static public void s_showWarningDialog(java.awt.Component cmpFrameParent, String strTitle, String strBody)
    {    
        Object[] options = { _s_strButtonClose };
        
        JOptionPane.showOptionDialog(cmpFrameParent, strBody, strTitle, 
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);
    }
    
    static public void s_showInformationDialog(java.awt.Component cmpFrameParent, String strTitle, String strBody)
    {    
        Object[] options = { _s_strButtonClose };
        
        JOptionPane.showOptionDialog(cmpFrameParent, strBody, strTitle, 
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
    }
    
    
    static public void s_showErrorDialog(java.awt.Component cmpFrameParent, String strApplicationTitle, String strBody)
    {
        String strTitle = strApplicationTitle + " - " + _s_strTitleError;
    
        Object[] options = { _s_strButtonClose };
        
        JOptionPane.showOptionDialog(cmpFrameParent, strBody, strTitle, 
                JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
                null, options, options[0]);
    }
    
    /**
        if any error, exiting
    **/
    static public boolean s_showConfirmDialog(java.awt.Component cmpFrameParent, String strTitle, String strBody)
    {
        final String f_strWhere = _f_s_strClass + "s_showConfirmDialog(cmpFrameParent, strTitle, strBody)";    
          
        Object[] options = { _s_strButtonOk, _s_strButtonCancel};
        
        int intReply = JOptionPane.showOptionDialog(cmpFrameParent, strBody, strTitle, 
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]);

		if (intReply==1 || intReply==-1) // cancelButton or "X" on topRight
		{
		    //MySystem.s_printOutTrace(f_strWhere, "action cancelled");
		    return false;
		}
        
        if (intReply != 0)
        {
           MySystem.s_printOutExit(f_strWhere, "unexpected value: " + intReply);
           
        }
        
        return true;
    }
    
    // there is a warning, the user may choose to solve the problem
    // icon=warning
    // buttons: choice between Y/N
    static public boolean s_showWarningConfirmDialog(java.awt.Component cmpFrameParent, String strTitle, String strBody)
    {
        final String f_strWhere = _f_s_strClass + "s_showWarningConfirmDialog(cmpFrameParent, strTitle, strBody)";    
          
        Object[] options = { _s_strButtonOk, _s_strButtonCancel};
        
        int intReply = JOptionPane.showOptionDialog(cmpFrameParent, strBody, strTitle, 
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);

		if (intReply==1 || intReply==-1) // cancelButton or "X" on topRight
		{
		    //MySystem.s_printOutTrace(f_strWhere, "action cancelled");
		    return false;
		}
        
        if (intReply != 0)
        {
           MySystem.s_printOutExit(f_strWhere, "unexpected value: " + intReply);
           
        }
        
        return true;
    }
    
    static public String s_showQuestionInputDialog(java.awt.Component cmpFrameParent, String strTitle, String strBody, Object[] objsOptions, Object objInitialValue)
    {
        final String f_strWhere = _f_s_strClass + "s_showQuestionInputDialog(cmpFrameParent, strTitle, strBody, objsOptions, objInitialValue)";    
        
        if (cmpFrameParent==null || strTitle==null || strBody==null || objsOptions==null || objInitialValue==null)
        {
            MySystem.s_printOutExit(f_strWhere, "nil arg");
            
        }
        
        
        int intMessageType = JOptionPane.QUESTION_MESSAGE;
        
        String strResult = (String) JOptionPane.showInputDialog(
                cmpFrameParent, strBody, strTitle, intMessageType,
                null, objsOptions, objInitialValue);
        
        // ending
        return strResult;
    }
    
    // ---------------
    // ABSTRACT PUBLIC
    
    abstract public boolean init();
    abstract public void destroy();
    
    // ---------
    // PROTECTED
    
    protected OPAbstract(
        Object[] objsArray,
        int intMessageType,
        Object[] objsOption)
    {
        super(objsArray, 
            intMessageType,
            JOptionPane.YES_NO_OPTION,
            null,
            objsOption,
           objsOption[0]);
    }
    
    protected boolean _init_()
    {
        return true;
    }
    
    protected void _destroy_()
    {
    }
    
}