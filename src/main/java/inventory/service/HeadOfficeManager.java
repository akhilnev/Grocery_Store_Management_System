package inventory.service;

import inventory.interfaces.HeadOfficeInterface;

public class HeadOfficeManager implements HeadOfficeInterface {
    // From test.java lines 159-173
    @Override
    public boolean approveChanges() {
        return true;
    }

    @Override
    public boolean reviewPerformance() {
        return true;
    }

    @Override
    public boolean manageStores() {
        return true;
    }
}
