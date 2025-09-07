package com.icebreaker.be.infra.llm.assistant;

import dev.langchain4j.model.output.structured.Description;
import java.util.List;

public record Keywords(
        @Description("Keyword list in Korean words, not sentences")
        List<String> values
) {

}
