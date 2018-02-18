package com.xlreader.shared.swing.optionpane;

import com.xlreader.shared.lang.*;


import javax.swing.*;

public class OPInputQuestion extends OPAbstract
{ 
    // --------------------
    // FINAL STATIC PRIVATE
    
    final static private String _f_s_strClass = "com.xlreader.shared.swing.optionpane.OPInputQuestion.";

    // ------
    // PUBLIC
    
    public OPInputQuestion(Object[] objsArray, Object[] objsOption)
    {
        super(objsArray, QUESTION_MESSAGE, objsOption);
    }
    
    public boolean init()
    {
        return super._init_();
    }
    
    
    public void destroy()
    {
        super._destroy_();
    }
    
    // -------
    // PRIVATE
}