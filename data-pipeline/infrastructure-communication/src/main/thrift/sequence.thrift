

namespace java com.spike.giantdataanalysis.communication.example.thrift


struct SequenceNameValuePair {
	1: string name,
	2: i64 value,
}

struct SequenceGroupNameValuesPair {
	1: string name,
	2: list<i64> values,
}


struct SequenceServiceResult {
	1: i32 status,
	2: optional string message,
}

struct SequenceInitializeRequest {
	1: string name,
	2: i64 value,
}

struct SequenceInitializeResponse {
	1: SequenceServiceResult result,
}

struct SequenceCurrentRequest {
	1: string name,
}

struct SequenceCurrentResponse {
	1: SequenceServiceResult result,
	2: optional SequenceNameValuePair value,
}

struct SequenceNextRequest {
	1: string name,
	2: optional i32 count,
}

struct SequenceNextOneResponse {
	1: SequenceServiceResult result,
	2: optional SequenceNameValuePair value,
}

struct SequenceNextNResponse {
	1: SequenceServiceResult result,
	2: optional SequenceGroupNameValuesPair value,
}


service SequenceService {
	SequenceInitializeResponse initialize (1:SequenceInitializeRequest request),
	SequenceCurrentResponse current (1:SequenceCurrentRequest request),
	SequenceNextOneResponse nextOne (1:SequenceNextRequest request),
	SequenceNextNResponse nextN (1:SequenceNextRequest request),
}


