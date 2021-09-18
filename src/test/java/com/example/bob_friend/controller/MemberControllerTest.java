package com.example.bob_friend.controller;

import com.example.bob_friend.model.dto.MemberDto;
import com.example.bob_friend.model.entity.Member;
import com.example.bob_friend.model.entity.Sex;
import com.example.bob_friend.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.example.bob_friend.document.ApiDocumentUtils.getDocumentRequest;
import static com.example.bob_friend.document.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(MemberController.class)
@WebMvcTest(useDefaultFilters = false)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class MemberControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    MemberService memberService;

    Member testMember;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        testMember = Member.builder()
                .id(1)
                .email("testMember@test.com")
                .nickname("testMember")
                .password("testPassword")
                .sex(Sex.FEMALE)
                .birth(LocalDate.now())
                .agree(true)
                .active(true)
                .verified(false)
                .build();
    }

    @Test
    public void signup() throws Exception {
        MemberDto.Signup signupDto = MemberDto.Signup.builder()
                .email("testMember@test.com")
                .nickname("testMember")
                .password("testPassword")
                .sex(Sex.FEMALE)
                .birth(LocalDate.now())
                .agree(true)
                .build();
        MemberDto.Response responseDto =
                new MemberDto.Response(signupDto.convertToEntityWithPasswordEncoder(passwordEncoder));
        when(memberService.signup(any())).thenReturn(responseDto);
        mvc.perform(post("/api/signup")
                        .content(objectMapper.writeValueAsString(signupDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)))
                .andDo(document("member/signup",
                        getDocumentRequest(),
                        getDocumentResponse())
                );
    }

    @Test
    public void getMyUserInfo() throws Exception {
        MemberDto.Response responseDto = new MemberDto.Response(testMember);
        when(memberService.getMyMemberWithAuthorities())
                .thenReturn(responseDto);
        mvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)))
                .andDo(document("member/getMyUserInfo",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }

    @Test
    public void getUserInfo() throws Exception {
        MemberDto.Response responseDto = new MemberDto.Response(testMember);
        when(memberService.getMemberWithAuthorities(any()))
                .thenReturn(responseDto);
        mvc.perform(get("/api/user/{email}",testMember.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)))
                .andDo(document("member/getUserInfo",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }

    @Test
    public void checkEmail() throws Exception {
        when(memberService.isExistByEmail(any())).thenReturn(false);
        mvc.perform(get("/api/email/{email}",testMember.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andDo(document("member/check-email",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }

    @Test
    public void checkNickname() throws Exception {
        when(memberService.isExistByEmail(any())).thenReturn(false);
        mvc.perform(get("/api/nickname/{nickname}",testMember.getNickname()))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andDo(document("member/check-nickname",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }


}