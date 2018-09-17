package com.spike.giantdataanalysis.flink.example.model.streamingledger2;

import com.dataartisans.streamingledger.sdk.api.StateAccess;
import com.dataartisans.streamingledger.sdk.api.TransactionProcessFunction;

/**
 * The implementation of the logic that executes the transaction. The logic is given the original
 * TransactionEvent plus all states involved in the transaction.
 */
public class TxnHandler extends TransactionProcessFunction<TransactionEvent, TransactionResult> {

    private static final long serialVersionUID = 1;

    public TxnHandler() {
    }

    @ProcessTransaction
    public void process(
            final TransactionEvent txn,
            final Context<TransactionResult> ctx,
            final @State("source-account") StateAccess<Long> sourceAccount,
            final @State("target-account") StateAccess<Long> targetAccount,
            final @State("source-asset") StateAccess<Long> sourceAsset,
            final @State("target-asset") StateAccess<Long> targetAsset) {

        final long sourceAccountBalance = sourceAccount.readOr(SimpleTradeExample.ZERO);
        final long sourceAssetValue = sourceAsset.readOr(SimpleTradeExample.ZERO);
        final long targetAccountBalance = targetAccount.readOr(SimpleTradeExample.ZERO);
        final long targetAssetValue = targetAsset.readOr(SimpleTradeExample.ZERO);

        // check the preconditions
        if (sourceAccountBalance > txn.getMinAccountBalance()
                && sourceAccountBalance > txn.getAccountTransfer()
                && sourceAssetValue > txn.getBookEntryTransfer()) {

            // compute the new balances
            final long newSourceBalance = sourceAccountBalance - txn.getAccountTransfer();
            final long newTargetBalance = targetAccountBalance + txn.getAccountTransfer();
            final long newSourceAssets = sourceAssetValue - txn.getBookEntryTransfer();
            final long newTargetAssets = targetAssetValue + txn.getBookEntryTransfer();

            // write back the updated values
            sourceAccount.write(newSourceBalance);
            targetAccount.write(newTargetBalance);
            sourceAsset.write(newSourceAssets);
            targetAsset.write(newTargetAssets);

            // emit result event with updated balances and flag to mark transaction as processed
            ctx.emit(new TransactionResult(txn, true, newSourceBalance, newTargetBalance));
        } else {
            // emit result with unchanged balances and a flag to mark transaction as rejected
            ctx.emit(new TransactionResult(txn, false, sourceAccountBalance, targetAccountBalance));
        }
    }
}