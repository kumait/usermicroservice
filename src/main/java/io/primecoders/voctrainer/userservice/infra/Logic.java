package io.primecoders.voctrainer.userservice.infra;

import io.primecoders.voctrainer.userservice.infra.exceptions.BadRequestException;
import io.primecoders.voctrainer.userservice.infra.exceptions.NotFoundException;
import io.primecoders.voctrainer.userservice.infra.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logic {
    public static final Logger LOGGER = LoggerFactory.getLogger(Logic.class);

    public static void affirm(boolean that, RuntimeException ex) {
        if (!that) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public static void affirm(boolean that) {
        affirm(that, new BadRequestException());
    }

    public static void affirm(boolean that, String message) {
        affirm(that, new BadRequestException(message));
    }

    public static void affirmAccess(boolean that) {
        affirm(that, new UnauthorizedException());
    }

    public static void affirmExists(boolean that, RuntimeException ex) {
        affirm(that, new NotFoundException());
    }

    public static <T> T requireExists(T object) {
        if (object == null) {
            throw new NotFoundException();
        }
        return object;
    }

}
