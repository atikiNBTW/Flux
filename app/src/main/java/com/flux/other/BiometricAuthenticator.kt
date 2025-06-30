package com.flux.other

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

class BiometricAuthenticator(
    private val activity: FragmentActivity,
    private val onSuccess: () -> Unit,
    private val onError: (String) -> Unit,
    private val onFailed: () -> Unit
) {
    private val executor: Executor = ContextCompat.getMainExecutor(activity)

    private val biometricPrompt: BiometricPrompt by lazy {
        BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(errString.toString())
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailed()
            }
        })
    }

    private val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Fingerprint Authentication")
        .setSubtitle("Authenticate to access the app")
        .setNegativeButtonText("Cancel")
        .build()

    fun authenticate() {
        biometricPrompt.authenticate(promptInfo)
    }

    fun isAvailable(): Boolean {
        val biometricManager = BiometricManager.from(activity)
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
                BiometricManager.BIOMETRIC_SUCCESS
    }
}