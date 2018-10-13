package com.spike.giantdataanalysis.flink.example.streamingledger

import java.nio.file.Paths
import java.util.SplittableRandom

import com.dataartisans.streamingledger.sdk.api.AccessType.READ_WRITE
import com.dataartisans.streamingledger.sdk.api.{StreamingLedger, TransactionProcessFunction}
import com.spike.giantdataanalysis.flink.example.model.streamingledger2._
import org.apache.flink.api.java.functions.KeySelector
import org.apache.flink.runtime.state.StateBackend
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}

object ExampleStreamingLedger {
  def main(args: Array[String]): Unit = {

    // set up the execution environment and the configuration
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // configure Flink
    env.setParallelism(4)
    env.getConfig.enableObjectReuse

    // enable checkpoints once a minute
    env.enableCheckpointing(60000)
    val uri = Paths.get("./checkpoints").toAbsolutePath.normalize.toUri
    val backend = new FsStateBackend(uri, true)
    env.setStateBackend(backend)

    // start building the transactional streams
    val tradeLedger = StreamingLedger.create("simple trade example")

    // define the transactional states
    // 账户表
    val accounts: StreamingLedger.State[String, Long] =
    tradeLedger.declareState("accounts").withKeyType(classOf[String]).withValueType(classOf[Long])
    // 流水表
    val books: StreamingLedger.State[String, Long] =
      tradeLedger.declareState("bookEntries").withKeyType(classOf[String]).withValueType(classOf[Long])

    // produce the deposits transaction stream
    // 取款事件流
    val deposits = env.addSource(new DepositsGenerator(1))
    // define transactors on states
    tradeLedger.usingStream[DepositEvent](deposits, "deposits")
      .apply(new DepositHandler().asInstanceOf[TransactionProcessFunction[DepositEvent, Void]])
      .on(accounts, new KeySelector[DepositEvent, String]() {
        override def getKey(value: DepositEvent): String = value.getAccountId
      }, "account", READ_WRITE)
      .on(books, new KeySelector[DepositEvent, String]() {
        override def getKey(value: DepositEvent): String = value.getBookEntryId
      }, "asset", READ_WRITE)

    // produce transactions stream
    // 转账事件流
    val transfers = env.addSource(new TransactionsGenerator(1))
    val transactionResults = tradeLedger.usingStream(transfers, "transactions")
      .apply(new TxnHandler().asInstanceOf[TransactionProcessFunction[TransactionEvent, TransactionResult]])
      .on(accounts, new KeySelector[TransactionEvent, String]() {
        override def getKey(value: TransactionEvent): String = value.getSourceAccountId
      }, "source-account", READ_WRITE)
      .on(accounts, new KeySelector[TransactionEvent, String]() {
        override def getKey(value: TransactionEvent): String = value.getTargetAccountId
      }, "target-account", READ_WRITE)
      .on(books, new KeySelector[TransactionEvent, String]() {
        override def getKey(value: TransactionEvent): String = value.getSourceBookEntryId
      }, "source-asset", READ_WRITE)
      .on(books, new KeySelector[TransactionEvent, String]() {
        override def getKey(value: TransactionEvent): String = value.getTargetBookEntryId
      }, "target-asset", READ_WRITE).output

    //  compute the resulting streams.
    val resultsStreams = tradeLedger.resultStreams

    // output to the console
    resultsStreams.getResultStream(transactionResults).print

    // trigger program execution
    env.execute
  }
}


//---------------------------------------------------------------------------
// Txn process
//---------------------------------------------------------------------------

//
// so the 'TransactionProcessFunction' cannot be implemented in Scala??? - 20180917
//

//@SerialVersionUID(1)
//class DepositHandler extends TransactionProcessFunction[DepositEvent, Unit] {
//
//  import com.dataartisans.streamingledger.sdk.api.TransactionProcessFunction.State
//
//  @ProcessTransaction
//  def process(event: DepositEvent,
//              ctx: TransactionProcessFunction.Context[Unit],
//              @State("account") account: StateAccess[Long],
//              @State("asset") asset: StateAccess[Long]): Unit = {
//    val newAccountValue = account.readOr(Constant.ZERO) + event.accountTransfer
//    account.write(newAccountValue)
//    val newAssetValue = asset.readOr(Constant.ZERO) + event.bookEntryTransfer
//    asset.write(newAssetValue)
//  }
//}
//
//@SerialVersionUID(1)
//class TxnHandler extends TransactionProcessFunction[TransactionEvent, TransactionResult] {
//
//  import com.dataartisans.streamingledger.sdk.api.TransactionProcessFunction.State
//
//  @ProcessTransaction
//  def process(txn: TransactionEvent,
//              ctx: TransactionProcessFunction.Context[TransactionResult],
//              @State("source-account") sourceAccount: StateAccess[Long],
//              @State("target-account") targetAccount: StateAccess[Long],
//              @State("source-asset") sourceAsset: StateAccess[Long],
//              @State("target-asset") targetAsset: StateAccess[Long]): Unit = {
//    val sourceAccountBalance = sourceAccount.readOr(Constant.ZERO)
//    val sourceAssetValue = sourceAsset.readOr(Constant.ZERO)
//    val targetAccountBalance = targetAccount.readOr(Constant.ZERO)
//    val targetAssetValue = targetAsset.readOr(Constant.ZERO)
//    // check the preconditions
//    if (sourceAccountBalance > txn.minAccountBalance
//      && sourceAccountBalance > txn.accountTransfer
//      && sourceAssetValue > txn.bookEntryTransfer) { // compute the new balances
//      val newSourceBalance = sourceAccountBalance - txn.accountTransfer
//      val newTargetBalance = targetAccountBalance + txn.accountTransfer
//      val newSourceAssets = sourceAssetValue - txn.bookEntryTransfer
//      val newTargetAssets = targetAssetValue + txn.bookEntryTransfer
//      // write back the updated values
//      sourceAccount.write(newSourceBalance)
//      targetAccount.write(newTargetBalance)
//      sourceAsset.write(newSourceAssets)
//      targetAsset.write(newTargetAssets)
//      // emit result event with updated balances and flag to mark transaction as processed
//      ctx.emit(new TransactionResult(txn, true, newSourceBalance, newTargetBalance))
//    } else { // emit result with unchanged balances and a flag to mark transaction as rejected
//      ctx.emit(new TransactionResult(txn, false, sourceAccountBalance, targetAccountBalance))
//    }
//  }
//}

//---------------------------------------------------------------------------
// Domain
//---------------------------------------------------------------------------
//// 事件: 取款
//case class DepositEvent(var accountId: String,
//                        var bookEntryId: String,
//                        var accountTransfer: Long,
//                        var bookEntryTransfer: Long) {
//  def this() = this("", "", 0L, 0L)
//}
//
//
//// 事件: 转账
//case class TransactionEvent(var sourceAccountId: String,
//                            var targetAccountId: String,
//                            var sourceBookEntryId: String,
//                            var targetBookEntryId: String,
//                            var accountTransfer: Long,
//                            var bookEntryTransfer: Long,
//                            var minAccountBalance: Long) {
//  def this() = this("", "", "", "", 0L, 0L, 0L)
//}
//
//// 查询: 转账结果
//case class TransactionResult(var transaction: TransactionEvent,
//                             var success: Boolean,
//                             var newSourceAccountBalance: Long,
//                             var newTargetAccountBalance: Long) {
//  def this() = this(null, false, 0L, 0L)
//}


object Constant {
  val NUM_ACCOUNTS: Int = 1000000
  val NUM_BOOK_ENTRIES: Int = 1000000
  val ACCOUNT_ID_PREFIX = "ACCT-"
  val BOOK_ENTRY_ID_PREFIX = "BOOK-"
  val MAX_ACCOUNT_TRANSFER: Long = 10000
  val MAX_BOOK_TRANSFER: Long = 1000
  val MIN_BALANCE = 0

  val ZERO = new java.util.function.Supplier[Long]() {
    override def get(): Long = 0L
  }
}

//---------------------------------------------------------------------------
// Data Generator
//---------------------------------------------------------------------------

abstract class BaseGenerator[T](maxRecordsPerSecond: Int) extends RichParallelSourceFunction[T] {

  checkArgument(maxRecordsPerSecond == -1 || maxRecordsPerSecond > 0,
    "maxRecordsPerSecond must be positive or -1 (infinite)": String)

  var running: Boolean = false

  override def run(ctx: SourceFunction.SourceContext[T]): Unit = {
    import java.util.SplittableRandom
    val numberOfParallelSubtasks = getRuntimeContext.getNumberOfParallelSubtasks
    val throttler = new Throttler(maxRecordsPerSecond, numberOfParallelSubtasks)
    val rnd = new SplittableRandom

    while ( {
      running
    }) {
      val event = randomEvent(rnd)
      ctx.collect(event)
      throttler.throttle
    }
  }

  override def cancel(): Unit = {
    running = false
  }


  def randomEvent(rnd: SplittableRandom): T
}

class DepositsGenerator(maxRecordsPerSecond: Int) extends BaseGenerator[DepositEvent](maxRecordsPerSecond) {
  override def randomEvent(rnd: SplittableRandom): DepositEvent = {
    val account = rnd.nextInt(Constant.NUM_ACCOUNTS)
    val book = rnd.nextInt(Constant.NUM_BOOK_ENTRIES)
    val accountsDeposit = rnd.nextLong(Constant.MAX_ACCOUNT_TRANSFER)
    val deposit = rnd.nextLong(Constant.MAX_BOOK_TRANSFER)

    new DepositEvent(Constant.ACCOUNT_ID_PREFIX + account,
      Constant.BOOK_ENTRY_ID_PREFIX + book,
      accountsDeposit,
      deposit)
  }
}

class TransactionsGenerator(maxRecordsPerSecond: Int) extends BaseGenerator[TransactionEvent](maxRecordsPerSecond) {
  override def randomEvent(rnd: SplittableRandom): TransactionEvent = {
    def inner(): TransactionEvent = {
      val accountsTransfer = rnd.nextLong(Constant.MAX_ACCOUNT_TRANSFER)
      val transfer = rnd.nextLong(Constant.MAX_BOOK_TRANSFER)

      val sourceAcct = rnd.nextInt(Constant.NUM_ACCOUNTS)
      val targetAcct = rnd.nextInt(Constant.NUM_ACCOUNTS)
      val sourceBook = rnd.nextInt(Constant.NUM_BOOK_ENTRIES)
      val targetBook = rnd.nextInt(Constant.NUM_BOOK_ENTRIES)
      if (sourceAcct != targetAcct && sourceBook != targetBook) {
        new TransactionEvent(Constant.ACCOUNT_ID_PREFIX + sourceAcct,
          Constant.ACCOUNT_ID_PREFIX + targetAcct,
          Constant.BOOK_ENTRY_ID_PREFIX + sourceBook,
          Constant.BOOK_ENTRY_ID_PREFIX + targetBook,
          accountsTransfer, transfer, Constant.MIN_BALANCE)
      } else {
        null
      }
    }

    var result = inner()
    while (result == null) {
      result = inner()
    }
    result
  }
}

class Throttler(maxRecordsPerSecond: Long, numberOfParallelSubtasks: Int) {
  final private var throttleBatchSize = 0L
  final private var nanosPerBatch = 0L
  private var endOfNextBatchNanos = 0L
  private var currentBatch = 0

  checkArgument(maxRecordsPerSecond == -1 || maxRecordsPerSecond > 0, "maxRecordsPerSecond must be positive or -1 (infinite)")
  checkArgument(numberOfParallelSubtasks > 0, "numberOfParallelSubtasks must be greater than 0")
  if (maxRecordsPerSecond == -1) { // unlimited speed
    throttleBatchSize = -1
    nanosPerBatch = 0
    endOfNextBatchNanos = System.nanoTime + nanosPerBatch
    currentBatch = 0
  } else {
    val ratePerSubtask: Float = maxRecordsPerSecond.toFloat / numberOfParallelSubtasks
    if (ratePerSubtask >= 10000) { // high rates: all throttling in intervals of 2ms
      throttleBatchSize = ratePerSubtask.toInt / 500
      nanosPerBatch = 2000000L
    } else {
      throttleBatchSize = (ratePerSubtask / 20).toInt + 1
      nanosPerBatch = (1000000000L / ratePerSubtask).toInt * throttleBatchSize
    }
    this.endOfNextBatchNanos = System.nanoTime + nanosPerBatch
    this.currentBatch = 0
  }


  @throws[InterruptedException]
  def throttle(): Unit = {
    if (throttleBatchSize == -1) return
    if ( {
      currentBatch += 1;
      currentBatch
    } != throttleBatchSize) return
    currentBatch = 0
    val now = System.nanoTime
    val millisRemaining = ((endOfNextBatchNanos - now) / 1000000).toInt
    if (millisRemaining > 0) {
      endOfNextBatchNanos += nanosPerBatch
      Thread.sleep(millisRemaining)
    }
    else endOfNextBatchNanos = now + nanosPerBatch
  }
}
