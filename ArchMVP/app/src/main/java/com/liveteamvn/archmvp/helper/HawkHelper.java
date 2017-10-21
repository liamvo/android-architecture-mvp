package com.liveteamvn.archmvp.helper;

import android.content.Context;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

/**
 * Created by liam on 10/21/2017.
 */

public class HawkHelper {
    public static void init(Context context) {
        NoEncryption encryption = new NoEncryption() {
            @Override
            public String encrypt(String key, String value) throws Exception {
                return KeyStoreHelper.encryptString(value);
            }

            @Override
            public String decrypt(String key, String value) throws Exception {
                return KeyStoreHelper.decryptString(value);
            }
        };
        Hawk.init(context).setEncryption(encryption).build();
    }
}
