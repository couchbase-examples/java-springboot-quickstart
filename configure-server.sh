set -m

# Start the Couchbase server in the background
/entrypoint.sh couchbase-server &

# Wait for the server to start
sleep 15

# Configure memory quota and index memory quota
curl -v -X POST http://127.0.0.1:8091/pools/default -d memoryQuota=512 -d indexMemoryQuota=512

# Enable required services
curl -v http://127.0.0.1:8091/node/controller/setupServices -d services=kv%2cn1ql%2Cindex

# Configure web settings (port, username, password)
curl -v http://127.0.0.1:8091/settings/web -d port=8091 -d username=$COUCHBASE_ADMINISTRATOR_USERNAME -d password=$COUCHBASE_ADMINISTRATOR_PASSWORD

# Configure index storage mode
curl -i -u $COUCHBASE_ADMINISTRATOR_USERNAME:$COUCHBASE_ADMINISTRATOR_PASSWORD -X POST http://127.0.0.1:8091/settings/indexes -d 'storageMode=memory_optimized'

# Create a new bucket
curl -v -u $COUCHBASE_ADMINISTRATOR_USERNAME:$COUCHBASE_ADMINISTRATOR_PASSWORD -X POST http://127.0.0.1:8091/pools/default/buckets -d name=$COUCHBASE_BUCKET -d bucketType=couchbase -d ramQuotaMB=128 -d authType=sasl -d saslPassword=$COUCHBASE_BUCKET_PASSWORD

# Wait for the bucket to be created
sleep 15

# Create a primary index on the bucket
curl -v http://127.0.0.1:8093/query/service -d "statement=CREATE PRIMARY INDEX ON `$COUCHBASE_BUCKET`"

# Bring the Couchbase server process to the foreground
fg 1