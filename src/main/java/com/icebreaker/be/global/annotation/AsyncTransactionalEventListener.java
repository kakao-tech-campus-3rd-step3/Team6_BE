package com.icebreaker.be.global.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Async
@TransactionalEventListener
public @interface AsyncTransactionalEventListener {

    @AliasFor(annotation = TransactionalEventListener.class, attribute = "phase")
    TransactionPhase phase() default TransactionPhase.AFTER_COMMIT;

    @AliasFor(annotation = TransactionalEventListener.class, attribute = "classes")
    Class<?>[] classes() default {};
}
