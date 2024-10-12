package com.example.mvidecomposetest.presentation.contactlist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.mvidecomposetest.core.componentScope
import com.example.mvidecomposetest.domain.Contact
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class DefaultContactListComponent(
    componentContext: ComponentContext,
    val onAddContactRequest: () -> Unit,
    val onEditContactRequest: (Contact) -> Unit
) : ContactListComponent, ComponentContext by componentContext {

    private var store = instanceKeeper.getStore {
        val storeFactory = ContactListStoreFactory()
        storeFactory.create()
    }

    init {
        componentScope().launch {
            store.labels.collect {
                when (it) {
                    is ContactListStore.Label.ContactSelected -> {
                        onEditContactRequest(it.contact)
                    }

                    ContactListStore.Label.AddContactClicked -> {
                        onAddContactRequest()
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ContactListStore.State> = store.stateFlow

    override fun onAddContactClick() {
        store.accept(ContactListStore.Intent.AddContact)
    }

    override fun onContactClick(contact: Contact) {
        store.accept(ContactListStore.Intent.SelectContact(contact))
    }
}