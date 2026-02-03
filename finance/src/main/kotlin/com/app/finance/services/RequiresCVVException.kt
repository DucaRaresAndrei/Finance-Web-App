package com.app.finance.services

import java.lang.RuntimeException

class RequiresCvvException(message: String): RuntimeException(message)