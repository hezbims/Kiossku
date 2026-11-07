package com.itpens.kiossku.domain.use_case

interface IUseCases<T> {
    suspend operator fun invoke() : T
}