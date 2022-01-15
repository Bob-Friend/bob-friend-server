package com.example.bobfriend.controller;

import com.example.bobfriend.model.dto.member.*;
import com.example.bobfriend.model.entity.Member;
import com.example.bobfriend.model.entity.Sex;
import com.example.bobfriend.service.EmailService;
import com.example.bobfriend.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.example.bobfriend.document.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
    EmailService emailService;
    @MockBean
    MemberService memberService;

    Member testMember;


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
                .emailVerified(false)
                .rating(0.0)
                .numberOfJoin(0)
                .build();
    }

    @Test
    public void getMyUserInfo() throws Exception {
        Response responseDto = new Response(testMember);
        when(memberService.getMyMemberWithAuthorities())
                .thenReturn(responseDto);
        mvc.perform(getRequestBuilder(
                        get("/api/user"))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)))
                .andDo(document("member/getMyUserInfo",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("토큰")
                        )
                ));
    }

    @Test
    public void getUserInfo() throws Exception {
        Response responseDto = new Response(testMember);
        when(memberService.getMemberWithAuthorities(any()))
                .thenReturn(responseDto);
        mvc.perform(getRequestBuilder(
                        get("/api/user/{email}", testMember.getEmail()))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)))
                .andDo(document("member/getUserInfo",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("email").description("이메일")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("토큰")
                        )
                ));
    }

    @Test
    public void checkEmail() throws Exception {
        when(memberService.checkExistByEmail(any()))
                .thenReturn(new DuplicationCheck(false));
        mvc.perform(get("/api/email/{email}", testMember.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new DuplicationCheck(false)
                )))
                .andDo(document("member/check-email",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("email").description("이메일")
                        )
                ));

    }

    @Test
    public void checkNickname() throws Exception {
        when(memberService.checkExistByNickname(any()))
                .thenReturn(
                        new DuplicationCheck(false)
                );
        mvc.perform(get("/api/nickname/{nickname}", testMember.getNickname()))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(
                                new DuplicationCheck(false)
                        )))
                .andDo(document("member/check-nickname",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("nickname").description("닉네임")
                        )
                ));

    }


    @Test
    void deleteMember() throws Exception {
        Delete delete = new Delete(testMember.getPassword());
        mvc.perform(getRequestBuilder(
                        delete("/api/user"))
                        .content(objectMapper.writeValueAsString(delete)))
                .andExpect(status().isOk())
                .andDo(document("member/delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("토큰")
                        )
                ));
    }

    @Test
    void rateMemberTest() throws Exception {
        Score rate = new Score();
        rate.setScore(3.2);
        mvc.perform(getRequestBuilder(
                        post("/api/user/{nickname}/score", testMember.getNickname()))
                        .content(objectMapper.writeValueAsString(rate)))
                .andExpect(status().isOk())
                .andDo(document("member/rate",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("nickname").description("닉네임")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("토큰")
                        )));
    }


    @Test
    void updateMemberTest() throws Exception {
        Update update = new Update();
        update.setNickname("update nickname");
        update.setBirth(LocalDate.now().minusYears(1));
        update.setPassword("update password");
        update.setAgree(false);
        update.setSex(Sex.NONE);

        Member incoming = Member.builder()
                .nickname(update.getNickname())
                .birth(update.getBirth())
                .password(update.getPassword())
                .sex(update.getSex())
                .agree(update.getAgree())
                .build();
        testMember.update(incoming);
        Response response = new Response(testMember);

        when(memberService.update(any()))
                .thenReturn(response);

        mvc.perform(getRequestBuilder(
                        put("/api/user")
                                .content(
                                        objectMapper.writeValueAsString(update))))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(response)
                ))
                .andDo(document("member/update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("토큰")
                        )));
    }


    @Test
    void resetPasswordTest() throws Exception {
        String newPassword = "new-password";
        when(memberService.resetPassword(any()))
                .thenReturn(newPassword);
        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setEmail(testMember.getEmail());
        resetPassword.setBirth(testMember.getBirth());

        mvc.perform(put("/api/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetPassword)))
                .andExpect(status().isOk())
                .andDo(document("member/reset-password",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }

}