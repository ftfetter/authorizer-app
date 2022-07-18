package com.github.ftfetter.challenge.authorizer.mapper;

import com.github.ftfetter.challenge.authorizer.controller.response.AuthorizationResponse;
import com.github.ftfetter.challenge.authorizer.model.Authorization;

import java.util.Optional;

public final class AuthorizationMapper {

    private AuthorizationMapper() {
    }

    public static AuthorizationResponse toResponse(Authorization authorization) {
        return Optional.ofNullable(authorization)
                .map(nonNullAuthorization -> AuthorizationResponse.AuthorizationResponseBuilder.anAuthorizationResponse()
                        .withAccount(AccountMapper.toResponse(nonNullAuthorization.getAccount()))
                        .withViolations(nonNullAuthorization.getViolations())
                        .build())
                .orElse(new AuthorizationResponse());
    }
}
