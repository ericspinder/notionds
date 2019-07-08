package com.notionds.dataSource;

public abstract class VendorMainByUsernamePassword extends VendorMain {

    private UsernamePassword usernamePassword;
    private String dbUrl;

    public VendorMainByUsernamePassword() {
        super();
        this.usernamePassword = usernamePassword;
        this.dbUrl = dbUrl;
    }
}
