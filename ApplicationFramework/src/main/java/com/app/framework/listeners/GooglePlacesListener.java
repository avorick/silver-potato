package com.app.framework.listeners;

import com.app.framework.entities.Place;

import java.util.List;

public interface GooglePlacesListener {
    void onSuccess(List<Place> places);

    void onFailure();
}
