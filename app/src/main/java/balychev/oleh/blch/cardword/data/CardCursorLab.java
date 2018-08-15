package balychev.oleh.blch.cardword.data;

import android.database.Cursor;

public class CardCursorLab {
    private static CardCursorLab sLab;
    private static Cursor sCursor;

    private CardCursorLab(){ }

    public static CardCursorLab getInstance(){
        if (sLab==null){
            sLab = new CardCursorLab();
        }
        return sLab;
    }

    public Cursor getCursor() {
        return sCursor;
    }

    public void setCursor(Cursor cursor) {
        if (sCursor != null)
            sCursor.close();
        sCursor = cursor;
    }
}
