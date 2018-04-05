export GENIN=src/main/thrift
export GENOUT=src/main/thrift/generated-resources
export RELPATH=generated-resources

rm -rf $GENOUT
mkdir -p $GENOUT

cd $GENIN
thrift -r -out $RELPATH --gen java tutorial.thrift
echo "Due to 0.11.0 changes to 0.10.0, should comment org.apache.thrift.ProcessFunction.handleRuntimeExceptions() Overrides."
cd ../../../