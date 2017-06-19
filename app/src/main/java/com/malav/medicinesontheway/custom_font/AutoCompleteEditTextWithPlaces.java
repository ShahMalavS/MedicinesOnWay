package com.malav.medicinesontheway.custom_font;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

import java.util.HashMap;

/**
 * Created by shahmalav on 18/06/17.
 */

public class AutoCompleteEditTextWithPlaces extends AppCompatAutoCompleteTextView {

    public AutoCompleteEditTextWithPlaces(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Returns the place description corresponding to the selected item */
    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        /** Each item in the autocompetetextview suggestion list is a hashmap object */
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("description");
    }
}
