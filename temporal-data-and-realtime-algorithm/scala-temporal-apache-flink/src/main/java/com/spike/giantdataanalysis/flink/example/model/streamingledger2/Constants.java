package com.spike.giantdataanalysis.flink.example.model.streamingledger2;

final class Constants {
    static final int NUM_ACCOUNTS = 1_000_000;
    static final int NUM_BOOK_ENTRIES = 1_000_000;
    static final String ACCOUNT_ID_PREFIX = "ACCT-";
    static final String BOOK_ENTRY_ID_PREFIX = "BOOK-";
    static final long MAX_ACCOUNT_TRANSFER = 10_000;
    static final long MAX_BOOK_TRANSFER = 1_000;
    static final long MIN_BALANCE = 0;

    private Constants() {
    }
}