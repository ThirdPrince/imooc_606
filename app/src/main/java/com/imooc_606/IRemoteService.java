package com.imooc_606;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import java.security.Provider;

public class IRemoteService extends Service {

    private static final String TAG = IRemoteService.class.getSimpleName();
    /**
     * 实现IIMoocAIDL
     */
    private IIMoocAidl iiMoocAidl = new IIMoocAidl.Stub() {
        @Override
        public int add(int num1, int num2) throws RemoteException {
            Log.e(TAG,"收到了远程的请求：参数 num1 ="+num1+"::num2="+num2);
            return num1  + num2;
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iiMoocAidl.asBinder();
    }
}
