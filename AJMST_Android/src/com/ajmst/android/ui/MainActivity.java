package com.ajmst.android.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.ajmst.android.R;
import com.ajmst.android.barcode.client.CaptureActivity;
import com.ajmst.android.ui.price.ChinesePriceActivity;
import com.ajmst.android.ui.spkfk.SpkfkSelectActivity;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
    /**
     * TabHost�ؼ�
     */
    private TabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//���ò�����http����,���򾭳�����java.io.EOFException
		System.setProperty("http.keepAlive", "false");
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab_main);
		mTabHost = this.getTabHost();
		Resources rs = getResources();
		
        Intent layout1intent = new Intent();
        layout1intent.setClass(this, MaintainActivity.class);
        TabHost.TabSpec layout1spec = mTabHost.newTabSpec("layout1");
        layout1spec.setIndicator("����",
                rs.getDrawable(android.R.drawable.stat_sys_phone_call));
        layout1spec.setContent(layout1intent);
        mTabHost.addTab(layout1spec);
        
        Intent layoutIntentConfig = new Intent();
        layoutIntentConfig.setClass(this, ConfigActivity.class);
        TabHost.TabSpec tabSpecConfig = mTabHost.newTabSpec("layout2");
        tabSpecConfig.setIndicator("����",
                rs.getDrawable(android.R.drawable.stat_sys_phone_call));
        tabSpecConfig.setContent(layoutIntentConfig);
        mTabHost.addTab(tabSpecConfig);
        
/*        Intent layoutIntentPrice = new Intent();
        layoutIntentPrice.setClass(this, ChinesePriceActivity.class);
        TabHost.TabSpec tabSpecPrice = mTabHost.newTabSpec("layout3");
        tabSpecPrice.setIndicator("��ҩ�۸�",
                rs.getDrawable(android.R.drawable.stat_sys_phone_call));
        tabSpecPrice.setContent(layoutIntentPrice);
        mTabHost.addTab(tabSpecPrice);*/
        
        Intent layoutIntentBarcode = new Intent();
        layoutIntentBarcode.setClass(this, CaptureActivity.class);
        TabHost.TabSpec tabBarcode = mTabHost.newTabSpec("layout4");
        tabBarcode.setIndicator("ɨ��",
                rs.getDrawable(android.R.drawable.stat_sys_phone_call));
        tabBarcode.setContent(layoutIntentBarcode);
        mTabHost.addTab(tabBarcode);

        Intent layoutIntentSpSelect = new Intent();
        layoutIntentSpSelect.setClass(this, SpkfkSelectActivity.class);
        TabHost.TabSpec tabSpSelect = mTabHost.newTabSpec("layout5");
        tabSpSelect.setIndicator("��Ʒ��ѯ",
                rs.getDrawable(android.R.drawable.stat_sys_phone_call));
        tabSpSelect.setContent(layoutIntentSpSelect);
        mTabHost.addTab(tabSpSelect);
        
        Intent layoutIntentPopSelect = new Intent();
        layoutIntentPopSelect.setClass(this, PopSelectActivity.class);
        TabHost.TabSpec tabPopSelect = mTabHost.newTabSpec("layout6");
        tabPopSelect.setIndicator("���Ե���ʽѡ���",
                rs.getDrawable(android.R.drawable.stat_sys_phone_call));
        tabPopSelect.setContent(layoutIntentPopSelect);
        mTabHost.addTab(tabPopSelect);
        
        //�����ɨ��tabʱ,�л������ģʽ;�������,�л�������ģʽ
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				System.out.println(tabId);
				if("layout4".equals(tabId)){
					MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				}else{
					MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				}
			}
		});
        
        //����������Ϣ����
        Intent sendMsgintent = new Intent(this,MsgSendService.class);
        startService(sendMsgintent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tab_main, menu);
		return true;
	}

	@Override
	public void finish() {
		Intent stopIntent = new Intent(this,MsgSendService.class);
		stopService(stopIntent);
		super.finish();
	}

	
}
