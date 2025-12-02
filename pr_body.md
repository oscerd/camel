This PR updates outdated container images in test-infra to their latest versions.

## Updated Images

### aws.container
- **Image**: `mirror.gcr.io/localstack/localstack`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-aws-v2/src/main/resources/org/apache/camel/test/infra/aws2/services/container.properties`
- **Old version**: `4.9.2`
- **New version**: `stable`

### azure.container
- **Image**: `mcr.microsoft.com/azure-storage/azurite`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-azure-common/src/main/resources/org/apache/camel/test/infra/azure/common/services/container.properties`
- **Old version**: `3.35.0`
- **New version**: `table-alpha.1`

### consul.container
- **Image**: `mirror.gcr.io/hashicorp/consul`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-consul/src/main/resources/org/apache/camel/test/infra/consul/services/container.properties`
- **Old version**: `1.21`
- **New version**: `1.22`

### couchbase.container
- **Image**: `mirror.gcr.io/couchbase/server`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-couchbase/src/main/resources/org/apache/camel/test/infra/couchbase/services/container.properties`
- **Old version**: `7.6.2`
- **New version**: `enterprise-7.6.8`

### couchdb.container
- **Image**: `mirror.gcr.io/couchdb`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-couchdb/src/main/resources/org/apache/camel/test/infra/couchdb/services/container.properties`
- **Old version**: `2.3.1`
- **New version**: `3.5.0`

### docling.container
- **Image**: `quay.io/docling-project/docling-serve`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-docling/src/main/resources/org/apache/camel/test/infra/docling/services/container.properties`
- **Old version**: `v1.6.0`
- **New version**: `v1.9.0`

### fhir.container
- **Image**: `mirror.gcr.io/hapiproject/hapi`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-fhir/src/main/resources/org/apache/camel/test/infra/fhir/services/container.properties`
- **Old version**: `v8.2.0-2`
- **New version**: `v8.6.0-1-tomcat`

### google.pubsub.container
- **Image**: `gcr.io/google.com/cloudsdktool/cloud-sdk`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-google-pubsub/src/main/resources/org/apache/camel/test/infra/google/pubsub/services/container.properties`
- **Old version**: `emulators`
- **New version**: `stable`

### hashicorp.vault.container
- **Image**: `mirror.gcr.io/hashicorp/vault`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-hashicorp-vault/src/main/resources/org/apache/camel/test/infra/hashicorp/vault/services/container.properties`
- **Old version**: `1.20.4`
- **New version**: `1.21.1`

### hashicorp.vault.container.ppc64le
- **Image**: `icr.io/ppc64le-oss/vault-ppc64le`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-hashicorp-vault/src/main/resources/org/apache/camel/test/infra/hashicorp/vault/services/container.properties`
- **Old version**: `v1.13.1`
- **New version**: `v1.14.8`

### ibm.mq.container
- **Image**: `icr.io/ibm-messaging/mq`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-ibmmq/src/main/resources/org/apache/camel/test/infra/ibmmq/services/container.properties`
- **Old version**: `9.3.2.0-r2`
- **New version**: `9.4.4.0-r3-s390x`

### infinispan.container
- **Image**: `quay.io/infinispan/server`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-infinispan/src/main/resources/org/apache/camel/test/infra/infinispan/services/container.properties`
- **Old version**: `16.0.1`
- **New version**: `16.0.3`

### kafka3.container
- **Image**: `mirror.gcr.io/apache/kafka`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-kafka/src/main/resources/org/apache/camel/test/infra/kafka/services/container.properties`
- **Old version**: `3.9.1`
- **New version**: `4.1.1`

### redpanda.container.image
- **Image**: `mirror.gcr.io/redpandadata/redpanda`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-kafka/src/main/resources/org/apache/camel/test/infra/kafka/services/container.properties`
- **Old version**: `v24.1.16`
- **New version**: `v25.3.1`

### strimzi.container.image
- **Image**: `quay.io/strimzi/kafka`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-kafka/src/main/resources/org/apache/camel/test/infra/kafka/services/container.properties`
- **Old version**: `latest-kafka-3.9.1`
- **New version**: `sha256-ffcba1ad2bdff39f0b59a935990617d539cc9a29720e44168a8cdd021c0a05b5.sig`

### confluent.container.image
- **Image**: `mirror.gcr.io/confluentinc/cp-kafka`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-kafka/src/main/resources/org/apache/camel/test/infra/kafka/services/container.properties`
- **Old version**: `7.9.2`
- **New version**: `latest-ubi9`

### keycloak.container
- **Image**: `mirror.gcr.io/keycloak/keycloak`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-keycloak/src/main/resources/org/apache/camel/test/infra/keycloak/services/container.properties`
- **Old version**: `26.3.5`
- **New version**: `nightly`

### microprofile.lra.container
- **Image**: `quay.io/jbosstm/lra-coordinator`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-microprofile-lra/src/main/resources/org/apache/camel/test/infra/microprofile/lra/services/container.properties`
- **Old version**: `5.13.1.Final-2.16.6.Final`
- **New version**: `7.2.2.Final-3.29.3`

### milvus.container
- **Image**: `mirror.gcr.io/milvusdb/milvus`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-milvus/src/main/resources/org/apache/camel/test/infra/milvus/services/container.properties`
- **Old version**: `v2.5.11`
- **New version**: `v2.6.6`

### milvus.container.ppc64le
- **Image**: `icr.io/ppc64le-oss/milvus-ppc64le`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-milvus/src/main/resources/org/apache/camel/test/infra/milvus/services/container.properties`
- **Old version**: `v2.4.11`
- **New version**: `v2.5.3`

### minio.container
- **Image**: `mirror.gcr.io/minio/minio`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-minio/src/main/resources/org/apache/camel/test/infra/minio/services/container.properties`
- **Old version**: `RELEASE.2025-07-23T15-54-02Z-cpuv1`
- **New version**: `latest-cicd`

### mongodb.container
- **Image**: `mirror.gcr.io/mongo`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-mongodb/src/main/resources/org/apache/camel/test/infra/mongodb/services/container.properties`
- **Old version**: `7.0.12-jammy`
- **New version**: `8.0.9-noble`

### mongodb.container.ppc64le
- **Image**: `icr.io/ppc64le-oss/mongodb-ppc64le`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-mongodb/src/main/resources/org/apache/camel/test/infra/mongodb/services/container.properties`
- **Old version**: `4.4.24`
- **New version**: `6.0.13-bv`

### mosquitto.container
- **Image**: `mirror.gcr.io/eclipse-mosquitto`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-mosquitto/src/main/resources/org/apache/camel/test/infra/mosquitto/services/container.properties`
- **Old version**: `2.0.18`
- **New version**: `2.0.22`

### nats.container
- **Image**: `mirror.gcr.io/nats`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-nats/src/main/resources/org/apache/camel/test/infra/nats/services/container.properties`
- **Old version**: `2.11.6`
- **New version**: `2.12.2`

### neo4j.container
- **Image**: `mirror.gcr.io/neo4j`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-neo4j/src/main/resources/org/apache/camel/test/infra/neo4j/services/container.properties`
- **Old version**: `5.26-community-ubi9`
- **New version**: `5.26.17-ubi9`

### ollama.container
- **Image**: `mirror.gcr.io/ollama/ollama`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-ollama/src/main/resources/org/apache/camel/test/infra/ollama/services/container.properties`
- **Old version**: `0.12.5`
- **New version**: `rocm`

### openldap.container
- **Image**: `mirror.gcr.io/osixia/openldap`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-openldap/src/main/resources/org/apache/camel/test/infra/openldap/services/container.properties`
- **Old version**: `1.5.0`
- **New version**: `stable`

### opensearch.container
- **Image**: `mirror.gcr.io/opensearchproject/opensearch`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-opensearch/src/main/resources/org/apache/camel/test/infra/opensearch/services/container.properties`
- **Old version**: `3.3.1`
- **New version**: `3.3.2`

### postgres.container
- **Image**: `mirror.gcr.io/postgres`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-postgres/src/main/resources/org/apache/camel/test/infra/postgres/services/container.properties`
- **Old version**: `17.5-alpine`
- **New version**: `trixie`

### qdrant.container
- **Image**: `mirror.gcr.io/qdrant/qdrant`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-qdrant/src/main/resources/org/apache/camel/test/infra/qdrant/services/container.properties`
- **Old version**: `v1.15.1-unprivileged`
- **New version**: `v1.9.7-unprivileged`

### rabbitmq.container
- **Image**: `mirror.gcr.io/rabbitmq`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-rabbitmq/src/main/resources/org/apache/camel/test/infra/rabbitmq/services/container.properties`
- **Old version**: `4.1.2-management`
- **New version**: `management`

### redis.container
- **Image**: `mirror.gcr.io/redis`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-redis/src/main/resources/org/apache/camel/test/infra/redis/services/container.properties`
- **Old version**: `7.4.0-alpine`
- **New version**: `alpine3.21`

### tensorflow.serving.container
- **Image**: `mirror.gcr.io/tensorflow/serving`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-tensorflow-serving/src/test/resources/org/apache/camel/test/infra/tensorflow/serving/services/container.properties`
- **Old version**: `2.18.0`
- **New version**: `latest-gpu`

### tensorflow.serving.container.ppc64le
- **Image**: `ibmcom/powerai`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-tensorflow-serving/src/test/resources/org/apache/camel/test/infra/tensorflow/serving/services/container.properties`
- **Old version**: `1.6.2-tensorflow-serving-ubuntu18.04-py37-ppc64le-21.036`
- **New version**: `1.7.0-xgboost-ubuntu18.04-py37-x86_64-20.335`

### torchserve.container
- **Image**: `mirror.gcr.io/pytorch/torchserve`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-torchserve/src/main/resources/org/apache/camel/test/infra/torchserve/services/container.properties`
- **Old version**: `0.12.0-cpu`
- **New version**: `latest-gpu`

### weaviate.container
- **Image**: `cr.weaviate.io/semitechnologies/weaviate`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-weaviate/src/main/resources/org/apache/camel/test/infra/weaviate/services/container.properties`
- **Old version**: `1.32.0`
- **New version**: `very-long-classification-timeouts`

### zookeeper.container
- **Image**: `mirror.gcr.io/zookeeper`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-zookeeper/src/main/resources/org/apache/camel/test/infra/zookeeper/services/container.properties`
- **Old version**: `3.9`
- **New version**: `3.9.3`

### zookeeper.container.ppc64le
- **Image**: `icr.io/ppc64le-oss/zookeeper-ppc64le`
- **File**: `/home/runner/work/camel/camel/test-infra/camel-test-infra-zookeeper/src/main/resources/org/apache/camel/test/infra/zookeeper/services/container.properties`
- **Old version**: `3.5.10`
- **New version**: `v3.9.3-debian-12-r19-bv`


## Verification

Please verify:
- [ ] All container image versions are compatible with existing tests
- [ ] No breaking changes in the updated versions
- [ ] Tests pass with the new versions

---

This PR was automatically created by the [Container Version Upgrade workflow](https://github.com/oscerd/camel/actions/runs/19855395353).
