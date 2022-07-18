package com.github.ftfetter.challenge.authorizer.mapper;

import com.github.ftfetter.challenge.authorizer.controller.response.AccountResponse;
import com.github.ftfetter.challenge.authorizer.controller.response.AuthorizationResponse;
import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.model.Authorization;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationMapperTest {

    @Test
    @DisplayName("When null is passed, then should return an empty AuthorizationResponse")
    void toResponseTestWhenNullIsPassed() {
        AuthorizationResponse expectedAuthorizationResponse = new AuthorizationResponse();

        AuthorizationResponse actualAuthorizationResponse = AuthorizationMapper.toResponse(null);

        assertEquals(expectedAuthorizationResponse, actualAuthorizationResponse);
    }

    @Test
    @DisplayName("When an Authorization is passed, then should return an AuthorizationResponse with the Authorization data")
    void toResponseTestWhenAuthorizationIsPassed() {
        Authorization authorization = new Authorization(new Account(), Collections.emptyList());
        AuthorizationResponse expectedAuthorizationResponse = buildExpectedAuthorizationResponse(authorization);

        AuthorizationResponse actualAuthorizationResponse = AuthorizationMapper.toResponse(authorization);

        assertEquals(expectedAuthorizationResponse, actualAuthorizationResponse);
    }

    private AuthorizationResponse buildExpectedAuthorizationResponse(Authorization authorization) {
        return AuthorizationResponse.AuthorizationResponseBuilder.anAuthorizationResponse()
                .withAccount(AccountResponse.AccountResponseBuilder.anAccountResponse()
                        .withActiveCard(authorization.getAccount().getActiveCard())
                        .withAvailableLimit(authorization.getAccount().getAvailableLimit())
                        .build())
                .withViolations(authorization.getViolations())
                .build();
    }
}