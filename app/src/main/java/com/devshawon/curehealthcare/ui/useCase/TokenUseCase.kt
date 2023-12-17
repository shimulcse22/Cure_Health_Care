package com.devshawon.curehealthcare.ui.useCase

import com.devshawon.curehealthcare.useCase.UseCase
import com.devshawon.curehealthcare.util.PreferenceStorage
import javax.inject.Inject

open class TokenUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<String, Unit>() {
    override fun execute(parameters: String) {
        preferenceStorage.token = parameters
    }
}