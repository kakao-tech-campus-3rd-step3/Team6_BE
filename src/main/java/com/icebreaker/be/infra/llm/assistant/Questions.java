package com.icebreaker.be.infra.llm.assistant;

import dev.langchain4j.model.output.structured.Description;
import java.util.List;

public record Questions(
        @Description("Icebreaking questions in korean word")
        List<String> values
) {

}
