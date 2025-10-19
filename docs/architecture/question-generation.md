# AI 기반 질문 생성 파이프라인

## 개요

'얼음땡' 서비스의 핵심 기능 중 하나는 사용자의 관심사(Topic)에 맞춰 자연스럽고 흥미로운 아이스브레이킹 질문을 제공하는 것입니다. 이 기능은 단순히 미리 정의된 질문 목록을 사용하는 것을 넘어, LLM(Large Language Model)과 벡터 데이터베이스(Vector Database)를 활용한 동적 생성 및 검색 파이프라인을 통해 구현됩니다.

## 아키텍처 다이어그램

질문 생성 및 검색 파이프라인은 크게 **스케줄링 기반의 질문 풀 빌딩(Batch)** 과정과 **실시간 질문 검색(Real-time)** 과정으로 나뉩니다.

```mermaid
graph TD
    subgraph Batch: 스케줄링 기반 질문 풀 빌딩
        A[QuestionScheduler] -- 매일 자정 실행 --> B{모든 관심사(Topic) 순회};
        B -- 각 Topic에 대해 --> C[QuestionAsyncExecutor];
        C -- 분산 락(Redisson) 획득 --> D[QuestionPoolService];
        D -- 1. 질문 생성 요청 --> E[QuestionGenerator];
        E -- Gemini API 호출 --> F(Google Gemini);
        F -- 생성된 질문 --> E;
        E -- 2. 임베딩 및 저장 요청 --> G[QuestionEmbeddingStore];
        G -- Text to Vector --> H(Embedding Model);
        H -- Vector 저장 --> I[(Milvus Vector DB)];
    end

    subgraph Real-time: 실시간 질문 검색
        J[Game Logic] -- 특정 Topic의 질문 요청 --> K[QuestionPoolService];
        K -- 3. 유사 질문 검색 --> L[QuestionEmbeddingStore];
        L -- Query to Vector --> H;
        L -- 유사도 검색 --> I;
        I -- 검색 결과 --> L;
        L -- 최종 질문 --> K;
        K -- 반환 --> J;
    end
```

## 핵심 구성 요소

- **`QuestionScheduler`**: 매일 자정(`0 0 0 * * *`)에 모든 `Interest`(`Topic`)에 대한 질문 생성 파이프라인을 트리거하는 스케줄러입니다.
- **`QuestionAsyncExecutor`**: Redisson을 사용한 분산 락(Distributed Lock)을 통해 여러 서버 인스턴스 환경에서도 각 토픽에 대한 질문 생성이 단 한 번만 실행되도록 보장합니다.
- **`QuestionPoolService`**: 질문 생성, 저장, 검색의 전체적인 흐름을 관장하는 서비스입니다.
- **`QuestionGenerator` (LLM)**: Google Gemini 모델을 사용하여 주어진 토픽과 프롬프트(`question-generator.md`)를 기반으로 실제 아이스브레이킹 질문들을 생성합니다. (LangChain4j `AiServices` 활용)
- **`EmbeddingModel`**: `all-MiniLM-L6-v2` 모델을 사용하여 질문 텍스트를 384차원의 벡터(Vector)로 변환(Embedding)합니다.
- **`QuestionEmbeddingStore` (Vector DB)**: 변환된 질문 벡터를 Milvus 벡터 데이터베이스에 저장하고, 특정 토픽과의 유사도를 기반으로 질문을 검색하는 역할을 담당합니다.

## 파이프라인 상세 과정

### 1. 스케줄링 기반 질문 풀 빌딩 (Batch)

1.  **트리거**: `QuestionScheduler`가 정해진 시간에 실행되어 모든 `Interest` 목록을 가져옵니다.
2.  **비동기 처리 및 분산 락**: 각 `Interest`(`Topic`)에 대해 `QuestionAsyncExecutor`가 비동기적으로 작업을 시작합니다. 이때 Redisson 분산 락을 사용하여 동일한 토픽에 대한 작업이 여러 서버에서 중복 실행되는 것을 방지합니다.
3.  **질문 생성**: `QuestionGenerator`가 Gemini 모델을 호출하여 토픽과 관련된 새로운 아이스브레이킹 질문들을 대량으로 생성합니다.
4.  **임베딩 및 저장**: `QuestionEmbeddingStore`는 생성된 질문들을 임베딩 모델을 통해 벡터로 변환한 후, Milvus에 저장합니다. 이 과정에서 이미 저장된 질문과 매우 유사한 질문은 중복 저장을 방지하는 로직이 포함되어 있습니다.

### 2. 실시간 질문 검색 (Real-time)

1.  **질문 요청**: `RANDOM_ROULETTE` 같은 게임 로직에서 특정 토픽과 관련된 질문이 필요한 경우, `QuestionPoolService`에 질문을 요청합니다.
2.  **유사도 검색**: `QuestionPoolService`는 `QuestionEmbeddingStore`를 통해 주어진 토픽을 벡터로 변환하고, Milvus에서 코사인 유사도(Cosine Similarity)가 가장 높은 질문 벡터들을 검색합니다.
3.  **결과 반환**: 검색된 질문 중 유사도 점수가 일정 기준(>0.719) 이상인 질문들만 필터링하여 최종적으로 게임 로직에 반환합니다.
