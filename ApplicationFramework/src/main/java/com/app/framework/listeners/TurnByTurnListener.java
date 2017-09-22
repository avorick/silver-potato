package com.app.framework.listeners;

import com.app.framework.utilities.map.model.TurnByTurnModel;

import java.util.List;

/**
 * Created by leonard on 5/4/2017.
 */

public interface TurnByTurnListener {
    void onSuccess(List<TurnByTurnModel> turnByTurnList);

    void onFailure();
}
