package com.example.mvidecomposetest.presentation.editcontact

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.EditContactUseCase

class EditContactStoreFactory {

    private val storeFactory = LoggingStoreFactory(DefaultStoreFactory())
    private val repository = RepositoryImpl
    private val editContactUseCase = EditContactUseCase(repository)

    fun create(contact: Contact): EditContactStore = object : EditContactStore,
        Store<EditContactStore.Intent, EditContactStore.State, EditContactStore.Label> by storeFactory.create(
            name = "EditContactStore",
            initialState = EditContactStore.State(
                username = contact.username,
                phone = contact.phone,
                id = contact.id
            ),
            reducer = ReducerImpl,
            executorFactory = ::ExecutorImpl,
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class ChangeUsername(val username: String) : Msg
        data class ChangePhone(val phone: String) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<EditContactStore.Intent, Action, EditContactStore.State, Msg, EditContactStore.Label>() {
        override fun executeIntent(
            intent: EditContactStore.Intent,
            getState: () -> EditContactStore.State
        ) {
            when (intent) {
                is EditContactStore.Intent.ChangePhone -> {
                    dispatch(Msg.ChangePhone(intent.phone))
                }

                is EditContactStore.Intent.ChangeUsername -> {
                    dispatch(Msg.ChangeUsername(intent.username))
                }

                EditContactStore.Intent.SaveContact -> {
                    val state = getState()
                    editContactUseCase(
                        Contact(
                            id = state.id,
                            username = state.username,
                            phone = state.phone
                        )
                    )
                    publish(EditContactStore.Label.ContactSaved)
                }
            }
        }
    }


    private object ReducerImpl : Reducer<EditContactStore.State, Msg> {
        override fun EditContactStore.State.reduce(msg: Msg) = when (msg) {
            is Msg.ChangeUsername -> copy(username = msg.username)
            is Msg.ChangePhone -> copy(phone = msg.phone)
        }
    }

}

