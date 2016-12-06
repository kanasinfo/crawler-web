package com.ch.common.support;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author J.Mars
 */
public abstract class SupportController {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    protected final Logger logger;

    public SupportController() {
        logger = Logger.getLogger(this.getClass());
    }

    protected String dateFormat = DEFAULT_DATE_FORMAT;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

        //used to parse a comma separated integer field.
        binder.registerCustomEditor(Integer.class, new IntegerEditor());
    }

}
