package com.dhl.aidlclienet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;

import com.imooc_606.IIMoocAidl;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatEditText etNum1, etNum2, etNumRes;

    private AppCompatButton btnAdd;

    private IIMoocAidl aidl;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aidl = IIMoocAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //startAppByPkg("com.imooc_606");

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkRemoteApp();
    }

    private void initView() {
        etNum1 = findViewById(R.id.et_num1);
        etNum2 = findViewById(R.id.et_num2);
        etNumRes = findViewById(R.id.et_res);
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (!TextUtils.isEmpty(etNum1.getText().toString()) && !TextUtils.isEmpty(etNum2.getText().toString())) {
            int num1 = Integer.parseInt(etNum1.getText().toString());
            int num2 = Integer.parseInt(etNum2.getText().toString());
            try {
                int res = aidl.add(num1, num2);
                etNumRes.setText(res + "");
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.imooc_606", "com.imooc_606.IRemoteService"));
        // intent.setAction("com.imooc_606.IRemoteService");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        //startService(intent);

    }

    private void checkRemoteApp() {
        if(aidl ==null){
            startAppByPkg("com.imooc_606");
            bindService();
        }
//        if(checkApplication("com.imooc_606")){
//            bindService();
//        }else {
//            startAppByPkg("com.imooc_606");
//        }
    }

    public boolean checkApplication(String packageName) {

        if (packageName == null || "".equals(packageName)) {
            return false;
        }

        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }

    private void startAppByPkg(String package_name){
        PackageManager packageManager = getPackageManager();
        Intent it = packageManager.getLaunchIntentForPackage(package_name);
        startActivity(it);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}