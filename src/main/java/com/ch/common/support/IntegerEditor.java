package com.ch.common.support;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

public class IntegerEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null) {
            setValue(null);
            return;
        }
        text = text.trim().replaceAll(",", "");
        if (text.equals("")) {
            setValue(null);
            return;
        }
        if (!StringUtils.hasText(text)) {
            setValue(null);
        } else {
            setValue(Integer.parseInt(text));
            //parse property
        }
    }

    @Override
    public String getAsText() {
        return getValue().toString();
    }
}
