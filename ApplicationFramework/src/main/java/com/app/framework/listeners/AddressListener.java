package com.app.framework.listeners;

import com.app.framework.entities.Address;

public interface AddressListener {
    void onAddressResponse(Address address);

    void onAddressError();

    void onZeroResults();
}
