package com.lernix.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lernix.application.usecase.user.*;
import com.lernix.infrastructure.web.dto.request.ChangePasswordRequest;
import com.lernix.infrastructure.web.mapper.UserMapper;
import com.lernix.usecase.user.*;
import com.lernix.shared.exception.InvalidPasswordException;
import com.lernix.usecase.user.PurgeUserAccountUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UserMapper.class)
@DisplayName("Web: UserController Unit Tests")
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private CreateUserUseCase createUserUseCase; // Paramètre 0
    @MockitoBean private GetUserProfileUseCase getUserProfileUseCase;
    @MockitoBean private ChangePasswordUseCase changePasswordUseCase;
    @MockitoBean private UpdateUserEmailUseCase updateUserEmailUseCase;
    @MockitoBean private DisableUserAccountUseCase disableUserAccountUseCase;
    @MockitoBean private PurgeUserAccountUseCase purgeUserAccountUseCase;
    @MockitoBean private GetAccountStatisticsUseCase getAccountStatisticsUseCase;

    @Test
    @DisplayName("PATCH /password - Should return 401 when current password is wrong")
    void shouldReturn401WhenPasswordIsInvalid() throws Exception {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequest request = new ChangePasswordRequest("wrong", "NewPass123!");

        doThrow(new InvalidPasswordException("Invalid current password"))
                .when(changePasswordUseCase).execute(any(), eq("wrong"), any());

        mockMvc.perform(patch("/api/v1/users/{id}/password", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }
}


