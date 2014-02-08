package com.ajmst.android.util;

public class KeyUtils {
    /**
     * Send a single key event.
     *
     * @param event is a string representing the keycode of the key event you
     * want to execute.
     */
    public static void sendKeyEvent(int keyCode) {
/*        int eventCode = keyCode;
        long now = SystemClock.uptimeMillis();
        try {
            KeyEvent down = new KeyEvent(now, now, KeyEvent.ACTION_DOWN, eventCode, 0);
            KeyEvent up = new KeyEvent(now, now, KeyEvent.ACTION_UP, eventCode, 0);
            (IWindowManager.Stub
                .asInterface(ServiceManager.getService("window")))
                .injectInputEventNoWait(down);
            (IWindowManager.Stub
                .asInterface(ServiceManager.getService("window")))
                .injectInputEventNoWait(up);
        } catch (RemoteException e) {
            Log.i(TAG, "DeadOjbectException");
        }
        
*/
    }
}
