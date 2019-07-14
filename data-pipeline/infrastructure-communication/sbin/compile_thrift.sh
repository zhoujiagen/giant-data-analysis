export THRIFT_CODE_DIR=../src/main/thrift
export THRIFT_OUTPUT_DIR=../target/generated-resources/thrift

rm -rf $THRIFT_OUTPUT_DIR
mkdir -p $THRIFT_OUTPUT_DIR

thrift -r -out $THRIFT_OUTPUT_DIR --gen java $THRIFT_CODE_DIR/tutorial.thrift
thrift -r -out $THRIFT_OUTPUT_DIR --gen java $THRIFT_CODE_DIR/sequence.thrift
echo "Due to 0.11.0 changes to 0.10.0, should comment org.apache.thrift.ProcessFunction.handleRuntimeExceptions() Overrides."
