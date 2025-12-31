package com.next.engine.annotations.api;

import com.next.engine.annotations.internal.Experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link Experimental} <br/>
 * Marks a class as the game conductor, who will be assigned in the main loop of the game.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Experimental
public @interface Conductor {
}
