package com.rj.testebancosolutis.utils

import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager

class FingerprintHelper {

    companion object{

        fun isAuthenticationAvailable(context: Context): Boolean{
            val biometricManager: BiometricManager = BiometricManager.from(context)
            when(biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)){
                BiometricManager.BIOMETRIC_SUCCESS -> return true
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> return false
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> return false
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> return false
            }

            return false

        }
    }

}