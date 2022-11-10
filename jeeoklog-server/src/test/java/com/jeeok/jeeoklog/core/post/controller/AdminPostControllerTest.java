package com.jeeok.jeeoklog.core.post.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeoklog.common.json.Code;
import com.jeeok.jeeoklog.core.post.controller.request.SavePostRequest;
import com.jeeok.jeeoklog.core.post.controller.request.UpdatePostRequest;
import com.jeeok.jeeoklog.core.post.domain.Post;
import com.jeeok.jeeoklog.core.post.dto.PostSearchCondition;
import com.jeeok.jeeoklog.core.post.dto.SearchCondition;
import com.jeeok.jeeoklog.core.post.dto.UpdatePostParam;
import com.jeeok.jeeoklog.core.post.service.PostService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminPostController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "jeeoklog.jeeok.com", uriPort = 443)
class AdminPostControllerTest {

    //CREATE_POST
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final int HITS = 0;

    //UPDATE_POST
    public static final String UPDATE_TITLE = "update_title";
    public static final String UPDATE_CONTENT = "update_content";

    //ERROR_MESSAGE
    public static final String POST = "Post";
    public static final Long NOT_FOUND_ID = 1L;
    public static final String HAS_MESSAGE_STARTING_WITH = "존재하지 않는 ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";

    //REQUEST_URL
    public static final String API_FIND_POSTS = "/admin/posts";
    public static final String API_FIND_POST = "/admin/posts/{postId}";
    public static final String API_SAVE_POST = "/admin/posts";
    public static final String API_UPDATE_POST = "/admin/posts/{postId}";
    public static final String API_DELETE_POST = "/admin/posts/{postId}";

    @MockBean PostService postService;

    @Autowired ObjectMapper objectMapper;

    @Autowired MockMvc mockMvc;

    private Post getPost(String title, String content) {
        return Post.createPost()
                .title(title)
                .content(content)
                .build();
    }

    @Test
    @DisplayName("게시물 목록 조회")
    void findPosts() throws Exception {
        //given
        List<Post> posts = new ArrayList<>();
        IntStream.range(0, 5).forEach(i -> posts.add(getPost(TITLE + i, CONTENT)));
        given(postService.findPosts(any(PostSearchCondition.class), any(Pageable.class))).willReturn(new PageImpl<>(posts));

        PostSearchCondition condition = new PostSearchCondition();
        condition.setSearchCondition(SearchCondition.TITLE);
        condition.setSearchKeyword(TITLE);

        PageRequest pageRequest = PageRequest.of(0, 10);

        LinkedMultiValueMap<String, String> conditionParams = new LinkedMultiValueMap<>();
        conditionParams.setAll(objectMapper.convertValue(condition, new TypeReference<Map<String, String>>() {}));

        LinkedMultiValueMap<String, String> pageRequestParams = new LinkedMultiValueMap<>();
        pageRequestParams.add("page", String.valueOf(pageRequest.getOffset()));
        pageRequestParams.add("size", String.valueOf(pageRequest.getPageSize()));

        //expected
        mockMvc.perform(get(API_FIND_POSTS)
                        .queryParams(conditionParams)
                        .queryParams(pageRequestParams))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.length()", Matchers.is(5)))
                .andDo(print())
                .andDo(document("findPosts",
                        RequestDocumentation.requestParameters(
                                RequestDocumentation.parameterWithName("searchCondition").description("검색 조건"),
                                RequestDocumentation.parameterWithName("searchKeyword").description("검색 키워드"),
                                RequestDocumentation.parameterWithName("page").description("검색 페이지"),
                                RequestDocumentation.parameterWithName("size").description("검색 사이즈")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("transaction_time").description("api 요청 시간"),
                                PayloadDocumentation.fieldWithPath("code").description("SUCCESS or ERROR"),
                                PayloadDocumentation.fieldWithPath("message").description("메시지"),
                                PayloadDocumentation.fieldWithPath("errors").description("에러"),
                                PayloadDocumentation.fieldWithPath("data[*].postId").description("게시물 고유번호"),
                                PayloadDocumentation.fieldWithPath("data[*].postTitle").description("게시물 제목"),
                                PayloadDocumentation.fieldWithPath("data[*].postContent").description("게시물 내용"),
                                PayloadDocumentation.fieldWithPath("data[*].hits").description("조회수"),
                                PayloadDocumentation.fieldWithPath("data[*].createdDate").description("등록일"),
                                PayloadDocumentation.fieldWithPath("data[*].createdBy").description("등록자")
                        )
                ))
        ;

        //verify
        verify(postService, times(1)).findPosts(any(PostSearchCondition.class), any(Pageable.class));
    }

    @Test
    @DisplayName("게시물 단건 조회")
    void findPost() throws Exception {
        //given
        Post post = getPost(TITLE, CONTENT);
        given(postService.findPost(any(Long.class))).willReturn(post);

        //expected
        mockMvc.perform(get(API_FIND_POST, 0L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.postTitle").value(TITLE))
                .andExpect(jsonPath("$.data.postContent").value(CONTENT))
                .andExpect(jsonPath("$.data.hits").value(HITS))
                .andDo(print())
                .andDo(MockMvcRestDocumentation.document("findPost",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("postId").description("게시물 고유번호")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("transaction_time").description("api 요청 시간"),
                                PayloadDocumentation.fieldWithPath("code").description("SUCCESS or ERROR"),
                                PayloadDocumentation.fieldWithPath("message").description("메시지"),
                                PayloadDocumentation.fieldWithPath("errors").description("에러"),
                                PayloadDocumentation.fieldWithPath("data.postId").description("게시물 고유번호"),
                                PayloadDocumentation.fieldWithPath("data.postTitle").description("게시물 제목"),
                                PayloadDocumentation.fieldWithPath("data.postContent").description("게시물 내용"),
                                PayloadDocumentation.fieldWithPath("data.hits").description("조회수"),
                                PayloadDocumentation.fieldWithPath("data.createdDate").description("등록일"),
                                PayloadDocumentation.fieldWithPath("data.createdBy").description("등록자")
                        )
                ))
        ;

        //verify
        verify(postService, times(1)).findPost(any(Long.class));
    }

    @Test
    @DisplayName("게시물 저장")
    void savePost() throws Exception {
        //given
        Post post = getPost(TITLE, CONTENT);
        given(postService.savePost(any(Post.class))).willReturn(post);

        SavePostRequest request = SavePostRequest.builder()
                .postTitle(TITLE)
                .postContent(CONTENT)
                .build();

        //expected
        mockMvc.perform(post(API_SAVE_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.savedPostId").hasJsonPath())
                .andDo(print())
                .andDo(MockMvcRestDocumentation.document("savePost",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("postTitle").description("게시물 제목"),
                                PayloadDocumentation.fieldWithPath("postContent").description("게시물 내용")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("transaction_time").description("api 요청 시간"),
                                PayloadDocumentation.fieldWithPath("code").description("SUCCESS or ERROR"),
                                PayloadDocumentation.fieldWithPath("message").description("메시지"),
                                PayloadDocumentation.fieldWithPath("errors").description("에러"),
                                PayloadDocumentation.fieldWithPath("data.savedPostId").description("저장된 게시물 고유번호")
                        )
                ))
        ;

        //verify
        verify(postService, times(1)).savePost(any(Post.class));
    }

    @Test
    @DisplayName("게시물 수정")
    void updatePost() throws Exception {
        //given
        Post post = getPost(TITLE, CONTENT);
        given(postService.findPost(any(Long.class))).willReturn(post);

        UpdatePostRequest request = UpdatePostRequest.builder()
                .postTitle(UPDATE_TITLE)
                .postContent(UPDATE_CONTENT)
                .build();

        //expected
        mockMvc.perform(put(API_UPDATE_POST, 0L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.updatedPostId").hasJsonPath())
                .andDo(print())
                .andDo(MockMvcRestDocumentation.document("updatePost",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("postId").description("게시물 고유번호")
                        ),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("postTitle").description("게시물 제목"),
                                PayloadDocumentation.fieldWithPath("postContent").description("게시물 내용")

                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("transaction_time").description("api 요청 시간"),
                                PayloadDocumentation.fieldWithPath("code").description("SUCCESS or ERROR"),
                                PayloadDocumentation.fieldWithPath("message").description("메시지"),
                                PayloadDocumentation.fieldWithPath("errors").description("에러"),
                                PayloadDocumentation.fieldWithPath("data.updatedPostId").description("수정된 게시물 고유번호")
                        )
                ))
        ;

        //verify
        verify(postService, times(1)).updatePost(any(Long.class), any(UpdatePostParam.class));
        verify(postService, times(1)).findPost(any(Long.class));
    }

    @Test
    @DisplayName("게시물 삭제")
    void deletePost() throws Exception {
        //given

        //expected
        mockMvc.perform(delete(API_DELETE_POST, 0L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(MockMvcRestDocumentation.document("deletePost",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("postId").description("게시물 고유번호")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("transaction_time").description("api 요청 시간"),
                                PayloadDocumentation.fieldWithPath("code").description("SUCCESS or ERROR"),
                                PayloadDocumentation.fieldWithPath("message").description("메시지"),
                                PayloadDocumentation.fieldWithPath("errors").description("에러"),
                                PayloadDocumentation.fieldWithPath("data").description("데이터")
                        )
                ))
        ;

        //verify
        verify(postService, times(1)).deletePost(any(Long.class));
    }
}