package org.block.blockbackend.core.utils;

import org.springframework.beans.factory.annotation.Value;

public class ApplicationProfileCheck {

    private static String profile;

    public ApplicationProfileCheck(@Value("${spring.profiles.active}") String profile) {
        ApplicationProfileCheck.profile = profile;
    }

    public static boolean isProd() {
        return "prod".equals(profile);
    }
}
