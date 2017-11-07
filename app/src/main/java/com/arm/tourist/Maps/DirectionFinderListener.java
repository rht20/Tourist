package com.arm.tourist.Maps;

import java.util.List;

/**
 * Created by rht on 10/14/17.
 */

public interface DirectionFinderListener {

    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
