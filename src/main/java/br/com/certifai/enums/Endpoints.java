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
    USUARIO_CRIAR("/auth/register"),
    VERIFICAR_USUARIO("/auth/verify"),
    OAUTH2("/login/oauth2/**", "/oauth2/**"),
    ESQUECI_SENHA("/auth/esqueci-senha"),
    DASHBOARD("/dashboard"),
    LOGIN("/login"),
    USUARIO_EDITAR("/usuarios/{id}"),
    USUARIO_DESATIVAR("/usuarios/{id}/desativar"),
    USUARIO_REMOVER("/usuarios/{id}"),
    USUARIOS_LISTAR("/usuarios/listar"),
    USUARIO_ALTERAR_SENHA("usuarios/{id}/senha"),
    USUARIO_LOGADO("/auth/me"),
    RECUPERAR_TOKEN("auth/validate-reset-token"),
    RECUPERAR_SENHA("auth/reset-password"),

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
                        SWAGGER_UI,
                        STATIC_RESOURCES,
                        AUTH_LOGIN,
                        REGISTER,
                        USUARIO_CRIAR,
                        VERIFICAR_USUARIO,
                        OAUTH2,
                        ESQUECI_SENHA,
                        LOGIN,
                        AUTH_LOGOUT,
                        RECUPERAR_TOKEN,
                        RECUPERAR_SENHA
                ).flatMap(e -> Stream.of(e.getPatterns()))
                .toArray(String[]::new);
    }

    public static String[] getAdminEndpoints() {
        return Stream.of(
                        USUARIOS_LISTAR
                ).flatMap(e -> Stream.of(e.getPatterns()))
                .toArray(String[]::new);
    }
}
