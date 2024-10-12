package com.example.mvidecomposetest.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.mvidecomposetest.presentation.addcontact.AddContactComponent
import com.example.mvidecomposetest.presentation.contactlist.ContactListComponent
import com.example.mvidecomposetest.presentation.editcontact.EditContactComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class ContactList(val component: ContactListComponent) : Child
        class AddContact(val component: AddContactComponent) : Child
        class EditContact(val component: EditContactComponent) : Child
    }

}