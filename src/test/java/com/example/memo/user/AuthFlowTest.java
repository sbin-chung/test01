package com.example.memo.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void homeRedirectsToLoginWhenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "alice")
    void homeGreetsLoggedInUser() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, <span>alice</span>")));
    }

    @Test
    void signupStoresEncryptedPasswordAndAllowsLogin() throws Exception {
        mockMvc.perform(post("/signup").with(csrf())
                        .param("username", "alice")
                        .param("password", "password123")
                        .param("passwordConfirm", "password123"))
                .andExpect(redirectedUrl("/login"));

        User saved = userRepository.findByUsername("alice").orElseThrow();
        assertThat(saved.getPassword()).isNotEqualTo("password123").startsWith("$2");

        mockMvc.perform(formLogin("/login").user("alice").password("password123"))
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("alice"));
    }

    @Test
    void loginFailsWithWrongPassword() throws Exception {
        signup("bob", "password123");

        mockMvc.perform(formLogin("/login").user("bob").password("wrong-password"))
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @Test
    void signupRejectsDuplicateUsername() throws Exception {
        signup("carol", "password123");

        mockMvc.perform(post("/signup").with(csrf())
                        .param("username", "carol")
                        .param("password", "password456")
                        .param("passwordConfirm", "password456"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("이미 사용 중인 아이디입니다.")));
    }

    @Test
    void signupRejectsPasswordMismatch() throws Exception {
        mockMvc.perform(post("/signup").with(csrf())
                        .param("username", "dave")
                        .param("password", "password123")
                        .param("passwordConfirm", "password999"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("비밀번호가 서로 다릅니다.")));

        assertThat(userRepository.existsByUsername("dave")).isFalse();
    }

    @Test
    @WithMockUser(username = "alice")
    void logoutRedirectsToLogin() throws Exception {
        mockMvc.perform(logout())
                .andExpect(redirectedUrl("/login?logout"));
    }

    private void signup(String username, String password) throws Exception {
        mockMvc.perform(post("/signup").with(csrf())
                .param("username", username)
                .param("password", password)
                .param("passwordConfirm", password));
    }
}
