/*
 * Copyright (c) 2014-present, ZTRIP. All rights reserved.
 */

package com.app.framework.utilities;

import android.content.Context;

/**
 * Created by leonard on 11/13/2015.
 * Utility class used to map error codes to error messages.
 */
public class ErrorUtils {

    /**
     * Method to show the error message
     *
     * @param context
     * @param code
     * @param defaultMessage
     * @param packageName
     * @return message
     */
    public static String getErrorMessage(Context context, int code, String defaultMessage, String packageName) {
        int resourceId = context.getResources().getIdentifier("err_code_" + code, "string", packageName);
        // initialize message
        String message;

        // if the associated resource identifier returns 0, no resource was found
        if (resourceId == 0) {
            message = defaultMessage;
        } else {
            message = context.getResources().getString(resourceId);
            if (FrameworkUtils.isStringEmpty(message)) {
                message = defaultMessage;
            }
        }
        return message;
    }
}
