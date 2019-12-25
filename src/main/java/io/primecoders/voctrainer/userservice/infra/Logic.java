package io.primecoders.voctrainer.userservice.infra;

import io.primecoders.voctrainer.userservice.infra.exceptions.APIException;
import io.primecoders.voctrainer.userservice.infra.exceptions.BadRequestException;
import io.primecoders.voctrainer.userservice.infra.exceptions.ForbiddenException;
import io.primecoders.voctrainer.userservice.infra.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logic {
    public static final Logger LOGGER = LoggerFactory.getLogger(Logic.class);

    public static void affirm(boolean that, APIException ex) {
        if (!that) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public static void affirm(boolean that) {
        affirm(that, new BadRequestException());
    }

    public static void affirm(boolean that, String code) {
        affirm(that, new BadRequestException(code));
    }

    public static void affirmAccess(boolean that) {
        affirm(that, new ForbiddenException());
    }

    public static void affirmAccess(boolean that, String code) {
        affirm(that, new ForbiddenException(code));
    }

    public static void affirmExists(boolean that) {
        affirm(that, new NotFoundException());
    }

    public static void affirmExists(boolean that, String code) {
        affirm(that, new NotFoundException(code));
    }

    public static <T> T requireExists(T object) {
        if (object == null) {
            throw new NotFoundException();
        }
        return object;
    }

}
