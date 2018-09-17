package com.spike.giantdataanalysis.flink.example.model.streamingledger2;

import com.dataartisans.streamingledger.sdk.api.StreamingLedger;
import com.dataartisans.streamingledger.sdk.api.StreamingLedger.ResultStreams;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.OutputTag;

import java.net.URI;
import java.nio.file.Paths;
import java.util.function.Supplier;

import static com.dataartisans.streamingledger.sdk.api.AccessType.READ_WRITE;

/**
 * A simple example illustrating the use of stream ledger.
 *
 * <p>The example here uses two states (called "accounts" and "bookEntries") and modifies two keys in each state in one
 * joint transaction.
 */
public class SimpleTradeExample {

    static final Supplier<Long> ZERO = () -> 0L;

    /**
     * The main entry point to the sample application. This runs the program with a
     * built-in data generator and the non-parallel local runtime implementation for
     * the transaction logic.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) throws Exception {

        // set up the execution environment and the configuration
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // configure Flink
        env.setParallelism(4);
        env.getConfig().enableObjectReuse();

        // enable checkpoints once a minute
        env.enableCheckpointing(60_000);
        URI uri = Paths.get("./checkpoints").toAbsolutePath().normalize().toUri();
        StateBackend backend = new FsStateBackend(uri, true);
        env.setStateBackend(backend);

        // start building the transactional streams
        StreamingLedger tradeLedger = StreamingLedger.create("simple trade example");

        // define the transactional states
        StreamingLedger.State<String, Long> accounts = tradeLedger.declareState("accounts")
                .withKeyType(String.class)
                .withValueType(Long.class);

        StreamingLedger.State<String, Long> books = tradeLedger.declareState("bookEntries")
                .withKeyType(String.class)
                .withValueType(Long.class);

        // produce the deposits transaction stream
        DataStream<DepositEvent> deposits = env.addSource(new DepositsGenerator(1));

        // define transactors on states
        tradeLedger.usingStream(deposits, "deposits")
                .apply(new DepositHandler())
                .on(accounts, DepositEvent::getAccountId, "account", READ_WRITE)
                .on(books, DepositEvent::getBookEntryId, "asset", READ_WRITE);

        // produce transactions stream
        DataStream<TransactionEvent> transfers = env.addSource(new TransactionsGenerator(1));

        OutputTag<TransactionResult> transactionResults = tradeLedger.usingStream(transfers, "transactions")
                .apply(new TxnHandler())
                .on(accounts, TransactionEvent::getSourceAccountId, "source-account", READ_WRITE)
                .on(accounts, TransactionEvent::getTargetAccountId, "target-account", READ_WRITE)
                .on(books, TransactionEvent::getSourceBookEntryId, "source-asset", READ_WRITE)
                .on(books, TransactionEvent::getTargetBookEntryId, "target-asset", READ_WRITE)
                .output();

        //  compute the resulting streams.
        ResultStreams resultsStreams = tradeLedger.resultStreams();

        // output to the console
        resultsStreams.getResultStream(transactionResults).print();

        // trigger program execution
        env.execute();
    }


}