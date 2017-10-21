package com.liveteamvn.archmvp.helper;

import android.app.Application;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

/**
 * Created by liam on 10/20/2017.
 */

public class KeyStoreHelper {
    private static final String TAG = "KeyStoreHelper";

    private static final String mAlias = "LiveTeam VN KeyStore";

    private static final String KEYSTORE_PROVIDER_ANDROID_KEYSTORE = "AndroidKeyStore";

    private static final String TYPE_RSA = "RSA";

    public interface IOnUpdateKeyStore {
        void onBeforeUpdate();

        void onUpdateSuccess();

        void onSuccess();

        void onError();
    }

    /**
     * Creates a public and private key and stores it using the Android Key Store, so that only
     * this application will be able to access the keys.
     */
    private static void updateKeys(Application application) throws NoSuchProviderException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        // BEGIN_INCLUDE(create_valid_dates)
        // Create a start and end time, for the validity range of the key pair that's about to be
        // generated.
        Calendar start = new GregorianCalendar();
        Calendar end = new GregorianCalendar();
        end.add(Calendar.YEAR, 1);
        //END_INCLUDE(create_valid_dates)

        // BEGIN_INCLUDE(create_keypair)
        // Initialize a KeyPair generator using the the intended algorithm (in this example, RSA
        // and the KeyStore.  This example uses the AndroidKeyStore.
        KeyPairGenerator kpGenerator = KeyPairGenerator
                .getInstance(TYPE_RSA,
                        KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
        // END_INCLUDE(create_keypair)

        // BEGIN_INCLUDE(create_spec)
        // The KeyPairGeneratorSpec object is how parameters for your key pair are passed
        // to the KeyPairGenerator.
        AlgorithmParameterSpec spec;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Below Android M, use the KeyPairGeneratorSpec.Builder.

            spec = new KeyPairGeneratorSpec.Builder(application)
                    // You'll use the alias later to retrieve the key.  It's a key for the key!
                    .setAlias(mAlias)
                    // The subject used for the self-signed certificate of the generated pair
                    .setSubject(new X500Principal("CN=" + mAlias + ", O=LiveTeam VN"))
                    // The serial number used for the self-signed certificate of the
                    // generated pair.
                    .setSerialNumber(BigInteger.valueOf(1337))
                    // Date range of validity for the generated pair.
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
        } else {
            // On Android M or above, use the KeyGenParameterSpec.Builder and specify permitted
            // properties  and restrictions of the key.
            spec = new KeyGenParameterSpec.Builder(mAlias, KeyProperties.PURPOSE_SIGN)
                    .setCertificateSubject(new X500Principal("CN=" + mAlias))
                    .setDigests(KeyProperties.DIGEST_SHA256)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .setCertificateSerialNumber(BigInteger.valueOf(1337))
                    .setCertificateNotBefore(start.getTime())
                    .setCertificateNotAfter(end.getTime())
                    .build();
        }

        kpGenerator.initialize(spec);

        kpGenerator.generateKeyPair();
    }

    public static void checkKeystore(Application application, IOnUpdateKeyStore onUpdateKeyStore) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
            keyStore.load(null);
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(mAlias, null);
            if (privateKeyEntry == null) {
                updateKeys(application);
            }
            onUpdateKeyStore.onSuccess();
            return;
        } catch (KeyStoreException e) {
            Log.w(TAG, "KeyStore not Initialized", e);
        } catch (UnrecoverableEntryException e) {
            Log.w(TAG, "KeyPair not recovered", e);
        } catch (NoSuchAlgorithmException e) {
            Log.w(TAG, "RSA not supported", e);
        } catch (IOException e) {
            Log.w(TAG, "IO Exception", e);
        } catch (CertificateException e) {
            Log.w(TAG, "Error occurred while loading certificates", e);
        } catch (InvalidAlgorithmParameterException e) {
            Log.w(TAG, "Error InvalidAlgorithmParameterException while create keystore", e);
        } catch (NoSuchProviderException e) {
            Log.w(TAG, "Error NoSuchProviderException while create keystore", e);
        }
        try {
            onUpdateKeyStore.onBeforeUpdate();
            updateKeys(application);
            onUpdateKeyStore.onUpdateSuccess();
            return;
        } catch (NoSuchProviderException e) {
            Log.w(TAG, "Error NoSuchProviderException while create keystore", e);
        } catch (NoSuchAlgorithmException e) {
            Log.w(TAG, "RSA not supported", e);
        } catch (InvalidAlgorithmParameterException e) {
            Log.w(TAG, "Error InvalidAlgorithmParameterException while create keystore", e);
        }
        onUpdateKeyStore.onError();
    }

    public static String encryptString(String value) {
        try {
            RSAPublicKey publicKey = getRsaPublicKey();

            // Encrypt the text
            Cipher input = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            input.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, input);
            cipherOutputStream.write(value.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte[] result = outputStream.toByteArray();
            return Base64.encodeToString(result, Base64.DEFAULT);
        } catch (KeyStoreException e) {
            Log.w(TAG, "KeyStore not Initialized", e);
        } catch (UnrecoverableEntryException e) {
            Log.w(TAG, "KeyPair not recovered", e);
        } catch (NoSuchAlgorithmException e) {
            Log.w(TAG, "RSA not supported", e);
        } catch (IOException e) {
            Log.w(TAG, "IO Exception", e);
        } catch (CertificateException e) {
            Log.w(TAG, "Error occurred while loading certificates", e);
        } catch (NoSuchProviderException e) {
            Log.w(TAG, "Error NoSuchProviderException while encrypting", e);
        } catch (NoSuchPaddingException e) {
            Log.w(TAG, "Error NoSuchPaddingException while encrypting", e);
        } catch (InvalidKeyException e) {
            Log.w(TAG, "Error InvalidKeyException while encrypting", e);
        }
        return null;
    }

    private static RSAPublicKey getRsaPublicKey() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException {
        KeyStore keyStore = getKeyStore();
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(mAlias, null);
        return (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
    }

    @SuppressWarnings("UnnecessaryUnboxing")
    public static String decryptString(String value) {
        try {
            RSAPrivateKey privateKey = getRsaPrivateKey();

            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            output.init(Cipher.DECRYPT_MODE, privateKey);

            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(value, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            return new String(bytes, 0, bytes.length, "UTF-8");

        } catch (KeyStoreException e) {
            Log.w(TAG, "KeyStore not Initialized", e);
        } catch (UnrecoverableEntryException e) {
            Log.w(TAG, "KeyPair not recovered", e);
        } catch (NoSuchAlgorithmException e) {
            Log.w(TAG, "RSA not supported", e);
        } catch (IOException e) {
            Log.w(TAG, "IO Exception", e);
        } catch (CertificateException e) {
            Log.w(TAG, "Error occurred while loading certificates", e);
        } catch (NoSuchProviderException e) {
            Log.w(TAG, "Error NoSuchProviderException while decrypting", e);
        } catch (NoSuchPaddingException e) {
            Log.w(TAG, "Error NoSuchPaddingException while decrypting", e);
        } catch (InvalidKeyException e) {
            Log.w(TAG, "Error InvalidKeyException while decrypting", e);
        }
        return null;
    }

    private static RSAPrivateKey getRsaPrivateKey() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException {
        KeyStore keyStore = getKeyStore();
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(mAlias, null);
        return (RSAPrivateKey) privateKeyEntry.getPrivateKey();
    }

    @NonNull
    private static KeyStore getKeyStore() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
        keyStore.load(null);
        return keyStore;
    }

}
