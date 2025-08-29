package com.icebreaker.be.infra.llm.assisstant;

import dev.langchain4j.model.output.structured.Description;
import java.util.List;

public record Questions(
        @Description("generate a list of questions consisting of 5 that are related to the given user text")
        List<String> values
) {

}
