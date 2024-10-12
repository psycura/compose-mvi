package com.example.mvidecomposetest.presentation.contactlist

import com.arkivanov.mvikotlin.core.store.Store
import com.example.mvidecomposetest.domain.Contact

interface ContactListStore :
    Store<ContactListStore.Intent, ContactListStore.State, ContactListStore.Label> {

    data class State(
        val contactList: List<Contact>
    )

    sealed interface Label {
        data class ContactSelected(val contact: Contact) : Label
        object AddContactClicked : Label
    }

    sealed interface Intent {
        object AddContact : Intent
        data class SelectContact(val contact: Contact) : Intent
    }

}
