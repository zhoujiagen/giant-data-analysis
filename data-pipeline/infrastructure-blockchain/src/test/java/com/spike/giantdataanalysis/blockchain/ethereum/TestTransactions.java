package com.spike.giantdataanalysis.blockchain.ethereum;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

// Ropsten
// FROM: 0x6dC5F594Dde00f50ff8Fd2CB741dA43f51AEeeBf
// TO: 0x1dda16bF700cB56FB28e60D2de97D16191d6c3FF

public class TestTransactions {
  private static final Logger LOG = LoggerFactory.getLogger(TestTransactions.class);

  public static void main(String[] args) {
    try {

      String url = "https://ropsten.infura.io/v3/008b7b01c7cc4049a29a326bae2ccfaf";
      Web3j web3j = Web3j.build(new HttpService(url)); // defaults to http://localhost:8545/
      // Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");
      String privateKey = ""; // TODO put your private key here
      Credentials credentials =
          Credentials.create(privateKey);

      // get the next available nonce
      String fromAddress = "0x6dC5F594Dde00f50ff8Fd2CB741dA43f51AEeeBf";
      EthGetTransactionCount ethGetTransactionCount =
          web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send();
      BigInteger nonce = ethGetTransactionCount.getTransactionCount();
      System.err.println(nonce);

      // create our transaction
      // gas price
      // Convert.toWei("1",Convert.Unit.GWEI).toBigInteger();
      BigInteger gasPrice = DefaultGasProvider.GAS_PRICE; // 22_000_000_000L
      gasPrice = BigInteger.valueOf(1_000_000_000L);
      System.err.println(gasPrice);
      // gas limit
      // BigInteger.valueOf(21000L);
      BigInteger gasLimit = DefaultGasProvider.GAS_LIMIT; // 4300000
      String toAddress = "0x1dda16bF700cB56FB28e60D2de97D16191d6c3FF";
      BigInteger value = BigInteger.ZERO;
      String data = new String(Hash.sha256("1234".getBytes()));
      RawTransaction rawTransaction =
          RawTransaction.createTransaction(nonce, gasPrice, gasLimit, toAddress, value, data);

      // sign & send our transaction
      byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
      String hexValue = Numeric.toHexString(signedMessage);
      System.err.println(hexValue);
      EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
      System.err.println(ethSendTransaction.getResult());

      System.in.read();
    } catch (Exception e) {
      LOG.error("", e);
    }

  }
}
