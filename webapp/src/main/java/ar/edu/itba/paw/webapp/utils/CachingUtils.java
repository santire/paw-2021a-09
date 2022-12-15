package ar.edu.itba.paw.webapp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.CacheControl;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class CachingUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachingUtils.class);

    public static final int HOUR_TO_SEC = 3600;

    private static final boolean TRANSFORM = false;

    public static CacheControl getCaching(final int time) {
        final CacheControl cache = new CacheControl();
        cache.setNoTransform(TRANSFORM);
        cache.setMaxAge(time);
        LOGGER.info("A new instance of Cache Control was created NO TRANSFORM: " + TRANSFORM + ". MAX AGE: " + time);
        return cache;
    }
    public static Date getExpirationDate(final int time) {
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.of("America/Argentina/Buenos_Aires")).plusSeconds(HOUR_TO_SEC);
        return Date.from(ldt.atZone(ZoneId.of("America/Argentina/Buenos_Aires")).toInstant());
    }
}
