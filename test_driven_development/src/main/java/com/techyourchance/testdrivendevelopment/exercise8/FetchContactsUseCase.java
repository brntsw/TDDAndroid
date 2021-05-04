package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

public class FetchContactsUseCase {

    public interface Listener {
        void onContactsFetched(List<Contact> contacts);

        void onFetchContactsFailed();
    }

    private final List<Listener> mListeners = new ArrayList<>();
    private GetContactsHttpEndpoint getContactsHttpEndpoint;

    public FetchContactsUseCase(GetContactsHttpEndpoint getContactsHttpEndpoint) {
        this.getContactsHttpEndpoint = getContactsHttpEndpoint;
    }

    public void fetchContacts(String filterTerm) {
        getContactsHttpEndpoint.getContacts(filterTerm, new GetContactsHttpEndpoint.Callback() {

            @Override
            public void onGetContactsSucceeded(List<ContactSchema> contacts) {
                for(Listener listener : mListeners) {
                    listener.onContactsFetched(contactsFromSchemas(contacts));
                }
            }

            @Override
            public void onGetContactsFailed(GetContactsHttpEndpoint.FailReason failReason) {
                switch (failReason) {
                    case GENERAL_ERROR:
                    case NETWORK_ERROR:
                        for (Listener listener : mListeners) {
                            listener.onFetchContactsFailed();
                        }
                        break;
                }
            }
        });
    }

    private List<Contact> contactsFromSchemas(List<ContactSchema> contactSchemas) {
        List<Contact> contactList = new ArrayList<>();
        for(ContactSchema schema : contactSchemas) {
            contactList.add(new Contact(
                    schema.getId(),
                    schema.getFullName(),
                    schema.getImageUrl()
            ));
        }

        return contactList;
    }

    public void registerListener(Listener listener) {
        mListeners.add(listener);
    }

    public void unregisterListener(Listener listener) {
        mListeners.remove(listener);
    }
}
