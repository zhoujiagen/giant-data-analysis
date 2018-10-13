package com.spike.giantdataanalysis.flink.example.model.streamingledger2;


import java.util.SplittableRandom;

import static com.spike.giantdataanalysis.flink.example.model.streamingledger2.Constants.*;

/**
 * A {@link DepositEvent} generator.
 */
public final class DepositsGenerator extends BaseGenerator<DepositEvent> {

    private static final long serialVersionUID = 1L;

    public DepositsGenerator(int maxRecordsPerSecond) {
        super(maxRecordsPerSecond);
    }

    @Override
    protected DepositEvent randomEvent(SplittableRandom rnd) {
        final int account = rnd.nextInt(NUM_ACCOUNTS);
        final int book = rnd.nextInt(NUM_BOOK_ENTRIES);
        final long accountsDeposit = rnd.nextLong(MAX_ACCOUNT_TRANSFER);
        final long deposit = rnd.nextLong(MAX_BOOK_TRANSFER);

        return new DepositEvent(
                ACCOUNT_ID_PREFIX + account,
                BOOK_ENTRY_ID_PREFIX + book,
                accountsDeposit,
                deposit);
    }
}