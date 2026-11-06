package local.ryan.tvinputs;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class InputsActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        Intent intent = new Intent("com.xiaomi.mitv.settings.INPUTSOURCE_POPUP");
        intent.setComponent(new ComponentName("com.xiaomi.mitv.settings",
                "com.xiaomi.mitv.settings.SettingsBroadcastReceiver"));
        try {
            if (getPackageManager().queryBroadcastReceivers(intent, 0).isEmpty()) {
                Toast.makeText(this, "未找到原生信号源菜单，请从电视设置切换", Toast.LENGTH_LONG).show();
            } else {
                sendBroadcast(intent);
            }
        } catch (SecurityException error) {
            Toast.makeText(this, "无法打开信号源，请从电视设置切换", Toast.LENGTH_LONG).show();
        }
        finish();
    }
}
