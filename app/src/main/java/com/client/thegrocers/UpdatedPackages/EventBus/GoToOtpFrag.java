package com.client.thegrocers.UpdatedPackages.EventBus;

import com.client.thegrocers.UpdatedPackages.CommonNew.NewUserMode;

public class GoToOtpFrag {
    private boolean success;
    private NewUserMode newUserMode;

    public GoToOtpFrag(boolean success, NewUserMode newUserMode) {
        this.success = success;
        this.newUserMode = newUserMode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public NewUserMode getNewUserMode() {
        return newUserMode;
    }

    public void setNewUserMode(NewUserMode newUserMode) {
        this.newUserMode = newUserMode;
    }
}
