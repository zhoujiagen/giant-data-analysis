package com.spike.giantdataanalysis.flink.example.model.streamingledger2;

public class DepositEvent {

    private String accountId;

    private String bookEntryId;

    private long accountTransfer;

    private long bookEntryTransfer;

    /**
     * Creates a new DepositEvent.
     */
    public DepositEvent(
            String accountId,
            String bookEntryId,
            long accountTransfer,
            long bookEntryTransfer) {
        this.accountId = accountId;
        this.bookEntryId = bookEntryId;
        this.accountTransfer = accountTransfer;
        this.bookEntryTransfer = bookEntryTransfer;
    }

    public DepositEvent() {
    }


    // ------------------------------------------------------------------------
    //  properties
    // ------------------------------------------------------------------------

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getBookEntryId() {
        return bookEntryId;
    }

    public void setBookEntryId(String bookEntryId) {
        this.bookEntryId = bookEntryId;
    }

    public long getAccountTransfer() {
        return accountTransfer;
    }

    public void setAccountTransfer(long accountTransfer) {
        this.accountTransfer = accountTransfer;
    }

    public long getBookEntryTransfer() {
        return bookEntryTransfer;
    }

    public void setBookEntryTransfer(long bookEntryTransfer) {
        this.bookEntryTransfer = bookEntryTransfer;
    }

    // ------------------------------------------------------------------------
    //  miscellaneous
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "DepositEvent {"
                + "accountId=" + accountId
                + ", bookEntryId=" + bookEntryId
                + ", accountTransfer=" + accountTransfer
                + ", bookEntryTransfer=" + bookEntryTransfer
                + '}';
    }
}