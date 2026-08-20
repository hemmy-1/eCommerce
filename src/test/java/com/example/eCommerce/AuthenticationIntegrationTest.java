package com.example.eCommerce;

import com.example.eCommerce.Dtos.LoginUserRequestDto;
import com.example.eCommerce.Dtos.RegUserRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
// @AutoConfigureMockMvc
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterAndLoginFlow() throws Exception {
        // 1. Register User
        RegUserRequestDto regDto = new RegUserRequestDto();
        regDto.setEmail("testuser@example.com");
        regDto.setNickName("Tester");
        regDto.setPassword("Password123!");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regDto)))
                .andExpect(status().isOk());

        // 2. Test Login (Will fail until email is verified per FR-AUTH-02)
        LoginUserRequestDto loginDto = new LoginUserRequestDto();
        loginDto.setEmail("testuser@example.com");
        loginDto.setPassword("Password123!");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().is4xxClientError());
    }
}