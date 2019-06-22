package test.engineering.com.gourmetsearchjune.Util;

import android.app.AlertDialog;
import android.content.Context;

public class AlertUtil {
    public static void showAlertWithOK(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    public static void showAlertWithOK(Context context, int title, int message) {
        showAlertWithOK(context, context.getString(title), context.getString(message));
    }

    public static void showAlertWithOK(Context context, int title, String message) {
        showAlertWithOK(context, context.getString(title), message);
    }
}
