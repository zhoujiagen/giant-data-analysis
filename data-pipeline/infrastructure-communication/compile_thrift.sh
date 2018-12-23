export GENIN=src/main/thrift
export GENOUT=target/generated-sources/thrift

rm -rf $GENOUT
mkdir -p $GENOUT

cd $GENIN
thrift -r -out . --gen java tutorial.thrift
echo "Due to 0.11.0 changes to 0.10.0, should comment org.apache.thrift.ProcessFunction.handleRuntimeExceptions() Overrides."
cd ../../../