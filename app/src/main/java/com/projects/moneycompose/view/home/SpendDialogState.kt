package com.projects.moneycompose.view.home

data class SpendDialogState(
    val isVisible: Boolean = false,
    val isEdit: Boolean = false,
    val spentId: Int? = null,
    val form: SpendFormState = SpendFormState()
)

data class SpendFormState(
    val description: String = "",
    val date: String = "",
    val price: String = ""
)
