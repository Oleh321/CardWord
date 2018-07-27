package balychev.oleh.blch.cardword.utils;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;


public class SystemUtils {

    public static void hideKeyBoard(Activity context){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
    }
}
