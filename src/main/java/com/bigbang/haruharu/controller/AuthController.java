package com.bigbang.haruharu.controller;


import com.bigbang.haruharu.advice.payload.ErrorResponse;
import com.bigbang.haruharu.config.security.token.CurrentUser;
import com.bigbang.haruharu.config.security.token.UserPrincipal;
import com.bigbang.haruharu.domain.entity.user.User;
import com.bigbang.haruharu.dto.request.auth.ChangePasswordRequest;
import com.bigbang.haruharu.dto.request.auth.RefreshTokenRequest;
import com.bigbang.haruharu.dto.request.auth.SignInRequest;
import com.bigbang.haruharu.dto.request.auth.SignUpRequest;
import com.bigbang.haruharu.dto.response.AuthResponse;
import com.bigbang.haruharu.dto.response.Message;
import com.bigbang.haruharu.service.auth.AuthService;
import com.bigbang.haruharu.service.clova.ClovaApiService;
import com.bigbang.haruharu.service.s3.UploadService;
import com.bigbang.haruharu.util.JsonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Tag(name = "Authorization", description = "Authorization API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final ClovaApiService clovaApiService;
    private final JsonUtils jsonUtils;
    private final UploadService uploadService;

    @Operation(summary = "유저 정보 확인", description = "현제 접속된 유저정보를 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 확인 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class) ) } ),
        @ApiResponse(responseCode = "400", description = "유저 확인 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping(value = "/")
    public ResponseEntity<?> whoAmI(
        @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return authService.whoAmI(userPrincipal);
    }

    @Operation(summary = "유저 정보 삭제", description = "현제 접속된 유저정보를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 삭제 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "유저 삭제 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @DeleteMapping(value = "/")
    public ResponseEntity<?> delete(
        @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ){
        return authService.delete(userPrincipal);
    }

    @Operation(summary = "유저 정보 갱신", description = "현제 접속된 유저의 비밀번호를 새로 지정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 정보 갱신 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "유저 정보 갱신 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PutMapping(value = "/")
    public ResponseEntity<?> modify(
        @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal, 
        @Parameter(description = "Schemas의 ChangePasswordRequest를 참고해주세요.", required = true) @Valid @RequestBody ChangePasswordRequest passwordChangeRequest
    ){
        return authService.modify(userPrincipal, passwordChangeRequest);
    }

    @Operation(summary = "유저 로그인", description = "유저 로그인을 수행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 로그인 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class) ) } ),
        @ApiResponse(responseCode = "400", description = "유저 로그인 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value = "/signin")
    public ResponseEntity<?> signin(
        @Parameter(description = "Schemas의 SignInRequest를 참고해주세요.", required = true) @Valid @RequestBody SignInRequest signInRequest
    ) {
        return authService.signin(signInRequest);
    }

    @Operation(summary = "유저 회원가입", description = "유저 회원가입을 수행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "회원가입 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value = "/signup")
    public ResponseEntity<?> signup(
        @Parameter(description = "Schemas의 SignUpRequest를 참고해주세요.", required = true) @Valid @RequestBody SignUpRequest signUpRequest
    ) {
        return authService.signup(signUpRequest);
    }

    @Operation(summary = "토큰 갱신", description = "신규 토큰 갱신을 수행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "토큰 갱신 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class) ) } ),
        @ApiResponse(responseCode = "400", description = "토큰 갱신 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value = "/refresh")
    public ResponseEntity<?> refresh(
        @Parameter(description = "Schemas의 RefreshTokenRequest를 참고해주세요.", required = true) @Valid @RequestBody RefreshTokenRequest tokenRefreshRequest
    ) {
        return authService.refresh(tokenRefreshRequest);
    }


    @Operation(summary = "유저 로그아웃", description = "유저 로그아웃을 수행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "로그아웃 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value="/signout")
    public ResponseEntity<?> signout(
        @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal, 
        @Parameter(description = "Schemas의 RefreshTokenRequest를 참고해주세요.", required = true) @Valid @RequestBody RefreshTokenRequest tokenRefreshRequest
    ) {
        return authService.signout(tokenRefreshRequest);
    }

    @Operation(summary = "닉네임 수정", description = "닉네임 수정을 수행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "닉네임 수정 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "닉네임 수정 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PutMapping(value="/nickname")
    public ResponseEntity<?> updateNickname(
        @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
        @Parameter(description = "변경할 닉네임을 입력해주세요.", required = true) @Valid @RequestParam String toUpdateNickname
    ) {
        return authService.updateNickname(userPrincipal, toUpdateNickname);
    }


}
