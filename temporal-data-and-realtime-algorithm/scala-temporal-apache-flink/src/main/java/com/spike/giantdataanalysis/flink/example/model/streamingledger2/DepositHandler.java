package com.spike.giantdataanalysis.flink.example.model.streamingledger2;

import com.dataartisans.streamingledger.sdk.api.StateAccess;
import com.dataartisans.streamingledger.sdk.api.TransactionProcessFunction;

/**
 * The implementation of the logic that executes a deposit.
 */
public class DepositHandler extends TransactionProcessFunction<DepositEvent, Void> {

    private static final long serialVersionUID = 1;

    public DepositHandler() {
    }

    @ProcessTransaction
    public void process(
            final DepositEvent event,
            final Context<Void> ctx,
            final @State("account") StateAccess<Long> account,
            final @State("asset") StateAccess<Long> asset) {

        long newAccountValue = account.readOr(SimpleTradeExample.ZERO) + event.getAccountTransfer();

        account.write(newAccountValue);

        long newAssetValue = asset.readOr(SimpleTradeExample.ZERO) + event.getBookEntryTransfer();
        asset.write(newAssetValue);
    }
}