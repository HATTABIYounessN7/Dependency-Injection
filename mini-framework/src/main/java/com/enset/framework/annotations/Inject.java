package com.enset.framework.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.FIELD,
        ElementType.CONSTRUCTOR,
        ElementType.METHOD
})
public @interface Inject {

}
