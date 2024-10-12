package com.example.mvidecomposetest.presentation.addcontact

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.mvidecomposetest.core.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultAddContactComponent(
    componentContext: ComponentContext,
    val onContactSaveRequest: () -> Unit
) : AddContactComponent, ComponentContext by componentContext {


    private var store: AddContactStore = instanceKeeper.getStore {
        val storeFactory = AddContactStoreFactory()
        storeFactory.create()
    }

    init {
        componentScope().launch {
            store.labels.collect {
                when (it) {
                    AddContactStore.Label.ContactSaved -> onContactSaveRequest()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AddContactStore.State>
        get() = store.stateFlow

    override fun onUsernameChanged(username: String) {
        store.accept(AddContactStore.Intent.ChangeUsername(username))
    }

    override fun onPhoneChanged(phone: String) {
        store.accept(AddContactStore.Intent.ChangePhone(phone))
    }

    override fun onSaveClicked() {
        store.accept(AddContactStore.Intent.SaveContact)
    }

}