package ir.jatlin.topmarket.core.domain.signin

import javax.inject.Inject

class IsValidEmailUseCase @Inject constructor() {

    operator fun invoke(input: String): Boolean =
        input.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()
}