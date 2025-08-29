package com.icebreaker.be.infra.llm.assisstant;

import java.util.List;
import jdk.jfr.Description;

public record Keywords(
        @Description("Keyword list in Korean words, not sentences")
        List<String> values
) {

}
