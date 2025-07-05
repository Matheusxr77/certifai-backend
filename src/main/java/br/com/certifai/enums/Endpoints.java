package br.com.certifai.enums;

import java.util.stream.Stream;

public enum Endpoints {
    HOME("/home"),
    SWAGGER_UI(
            "/api-docs/**",
            "/api-docs",
            "/api-docs.json",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**"
    ),

    REGISTER("/register"),
    AUTH_LOGIN("/auth/login"),
    AUTH_LOGOUT("/auth/logout"),
    USUARIO_CRIAR("/certifai/auth/register", "/auth/register"),
    VERIFICAR_USUARIO("/auth/verify"),

    STATIC_RESOURCES("/assets/**", "/css/**", "/js/**");

    private final String[] patterns;

    Endpoints(String... patterns) {
        this.patterns = patterns;
    }

    public String[] getPatterns() {
        return patterns;
    }

    public static String[] getPublicEndpoints() {
        return Stream.of(
                        HOME,
                        SWAGGER_UI,
                        STATIC_RESOURCES,
                        AUTH_LOGIN,
                        AUTH_LOGOUT,
                        REGISTER,
                        USUARIO_CRIAR,
                        VERIFICAR_USUARIO
                ).flatMap(e -> Stream.of(e.getPatterns()))
                .toArray(String[]::new);
    }

//    public static String[] getAdminEndpoints() {
//        return Stream.of(
//                        REGISTER
//                ).flatMap(e -> Stream.of(e.getPatterns()))
//                .toArray(String[]::new);
//    }
}
