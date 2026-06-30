# PQC Remediation Summary — Apache Camel

_Applied on 2026-06-30. Scope: the "Migration Roadmap → Phase 1: Can Do Now" items and the
"Priority: High" recommendations from `PQC_READINESS_REPORT.md` that have no external blocker._

This remediation deliberately makes **minimal, focused edits**. Several of the report's headline
items rest on code citations that do not match the repository at this commit, or require a JDK newer
than Camel's JDK 17 baseline; those are skipped with reasons below rather than forced in.

## Changes Applied

### 1. Deprecate the 3DES (Triple DES) option in XML-Encryption — F007, F024 (Phase 1 / Priority Low)
- **File:** `components/camel-xmlsecurity/src/main/java/org/apache/camel/dataformat/xmlsecurity/XMLSecurityDataFormat.java`
- **What changed:** Added a one-line deprecation `LOG.warn(...)` in `setXmlCipherAlgorithm(String)` that
  fires when a user explicitly selects `XMLCipher.TRIPLEDES` (3DES/DESede), steering them to AES-256-GCM.
- **Why:** The report recommends deprecating the 3DES branch (classically weak; quantum-relevance
  secondary). 3DES remains a selectable W3C XML-Enc algorithm, so it cannot be removed without breaking
  the ability to **decrypt legacy data** and breaking backwards compatibility for the public DSL option.
  A runtime deprecation warning is the minimal, non-breaking way to discourage new use while preserving
  interop. The warning is emitted at the single configuration entry point (the default constructor sets
  AES-256-GCM directly and does **not** route through this setter), so it triggers only when 3DES is
  deliberately configured — not on the default path and not per-message.
- **Validation:** `mvn -o compile` for the `camel-xmlsecurity` module succeeds.

## Items Already Satisfied (no change needed)

### Default XML-Encryption algorithm is already AES-256-GCM — F021 (Phase 1)
- **File:** `components/camel-xmlsecurity/.../XMLSecurityDataFormat.java:109`
- The no-arg constructor already initialises `xmlCipherAlgorithm = XMLCipher.AES_256_GCM`. The report's
  "default XML-Enc to AES-256-GCM" recommendation is therefore already met; the data-encryption key path
  (`generateDataEncryptionKey()`) already derives a 256-bit AES key for this default. No edit required.

## Items Skipped (with reasons)

### A. Reorder/prefer `X25519MLKEM768` TLS named groups — F015–F017 (Phase 1 item 1 / **Priority High**)
- **Cited files:** `core/camel-api/.../jsse/SSLContextParameters.java`,
  `core/camel-main/.../SSLConfigurationProperties.java`, `components/camel-netty/.../ssl/SSLEngineFactory.java`.
- **Reason skipped — premise does not match the code, and the runtime does not support it:**
  1. **No named-groups API exists to "reorder."** These classes expose only `secureSocketProtocol(s)` and
     `cipherSuites`/`cipherSuitesInclude`/`cipherSuitesExclude`. There is **no** TLS named-group/key-exchange
     configuration anywhere in Camel's SSL abstraction. A repository-wide search for `namedGroups` /
     `X25519MLKEM768` in the SSL code returns nothing; the only `X25519MLKEM768` references live in the
     `camel-pqc` component (the KEM material) and generated PQC data-format model classes — not TLS config.
     The report's F015–F017 line citations point at unrelated fields/comments (e.g.
     `SSLContextParameters.java:50` is the `keyManagers` field; `SSLConfigurationProperties.java:212` is a
     KeyStore-type Javadoc; `SSLEngineFactory.java:46` is a resource-stream load). There is nothing to reorder.
  2. **Building such an API is blocked on the runtime.** Per-context named-group ordering requires
     `javax.net.ssl.SSLParameters.setNamedGroups(String[])`, which was introduced in **JDK 20**. Camel's
     build/runtime baseline is **JDK 17** (`pom.xml: <jdk.version>17</jdk.version>`), so that API will not
     even compile. The only JDK-17-compatible lever is the JVM-global `jdk.tls.namedGroups` system property,
     which a library must not set by default (it overrides TLS for the entire JVM and would break handshakes
     on stock JDK 17 JSSE, which does not implement the `X25519MLKEM768` group).
  3. The report itself defers native JDK PQC TLS to **"Phase 2: Requires Library/Runtime Updates"**
     (JDK 24/25, JEP 496/497/527). Under the task rules ("do NOT attempt changes the report marks
     'Requires Library/Runtime Updates'" and apply crypto-agility "only where the project's current
     language/library versions already support it"), this is correctly out of scope today.
  - **When to revisit:** once Camel raises its JDK floor to 20+ (ideally 24/25 for the standardised PQC TLS
    groups), add a named-groups option to `SSLContextParameters`/`SSLConfigurationProperties` defaulting to
    a hybrid group first — at that point it becomes a genuine, supported "reorder."

### B. Documentation for the dev self-signed certificate generator — F005, F006, F023 (Phase 1)
- **Cited file:** `core/camel-main/.../SelfSignedCertificateGenerator.java`.
- **Reason skipped:** This file **does not exist** anywhere in the repository (verified by name search and by
  searching for its described crypto, e.g. `secp256r1` / `SHA256withECDSA` / X509 cert-builder usage in
  `core/` main sources — no matches). The only self-signed-certificate material present is test-scoped
  (`components/camel-coap/src/test/resources/selfsigned.jks`, `camel-milo` test factory), which is out of
  scope for production posture and not what the report describes. There is no production code to document.

### C. Continue/expand `camel-pqc` adoption examples — F008–F014 (Phase 1)
- **Reason skipped:** This is open-ended guidance ("continue/expand"), not a concrete defect or change with a
  defined edit. The `camel-pqc` component is already classified PQC-Ready (ML-KEM, ML-DSA, SLH-DSA, plus
  hybrid KEM/signature material). Authoring new examples is outside the requested "minimal, focused edits"
  to remediate quantum-vulnerable code and has no external blocker to clear here. No safe, bounded change to apply.

### D. Interop-bound RSA usages — F001–F004 (Priority Medium / Phase 3)
- **Files:** `camel-ssh` (RSA SSH key parsing), `camel-keycloak` (RSA from OIDC JWK), `camel-jira`
  (OAuth 1.0a RSA-SHA1).
- **Reason skipped:** Explicitly **blocked on external standards/third parties** (OpenSSH/Apache MINA sshd
  PQC KEX, IdPs issuing PQC keys, Atlassian). The report places these in "Phase 3: Blocked on External
  Standards / Third Parties," and the algorithm is dictated by the remote peer/protocol — not changeable
  unilaterally by Camel. The task rules forbid attempting "Blocked on External Standards" changes.

## Net Result
- **1 production code change** applied (3DES deprecation warning), compiled clean.
- **1 item** confirmed already satisfied (AES-256-GCM is the XML-Enc default).
- **4 items/groups** skipped, each because the cited code is absent, the change requires a JDK/runtime
  newer than the JDK 17 baseline, or it is externally blocked — consistent with the task's safety rules.
- No public API signatures changed; no new dependencies added; no unrelated code reformatted.
