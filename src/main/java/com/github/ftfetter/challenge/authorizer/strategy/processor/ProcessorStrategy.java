package com.github.ftfetter.challenge.authorizer.strategy.processor;

import java.util.function.Function;

public interface ProcessorStrategy {

    Boolean isEligible(String input);

    Function<String,String> process();
}
