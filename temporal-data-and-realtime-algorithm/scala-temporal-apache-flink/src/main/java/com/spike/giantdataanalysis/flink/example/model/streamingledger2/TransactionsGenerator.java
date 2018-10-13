package com.spike.giantdataanalysis.flink.example.model.streamingledger2;


import java.util.SplittableRandom;

import static com.spike.giantdataanalysis.flink.example.model.streamingledger2.Constants.*;


/**
 * A {@link TransactionEvent} generator.
 */
public final class TransactionsGenerator extends BaseGenerator<TransactionEvent> {

    private static final long serialVersionUID = 1L;

    public TransactionsGenerator(int maxRecordsPerSecond) {
        super(maxRecordsPerSecond);
    }

    protected TransactionEvent randomEvent(SplittableRandom rnd) {
        final long accountsTransfer = rnd.nextLong(MAX_ACCOUNT_TRANSFER);
        final long transfer = rnd.nextLong(MAX_BOOK_TRANSFER);
        while (true) {
            final int sourceAcct = rnd.nextInt(NUM_ACCOUNTS);
            final int targetAcct = rnd.nextInt(NUM_ACCOUNTS);
            final int sourceBook = rnd.nextInt(NUM_BOOK_ENTRIES);
            final int targetBook = rnd.nextInt(NUM_BOOK_ENTRIES);

            if (sourceAcct == targetAcct || sourceBook == targetBook) {
                continue;
            }
            return new TransactionEvent(
                    ACCOUNT_ID_PREFIX + sourceAcct,
                    ACCOUNT_ID_PREFIX + targetAcct,
                    BOOK_ENTRY_ID_PREFIX + sourceBook,
                    BOOK_ENTRY_ID_PREFIX + targetBook,
                    accountsTransfer,
                    transfer,
                    MIN_BALANCE);
        }
    }
}