package com.yd1994.alpacablog;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.cli.CliDocumentation.curlRequest;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional(readOnly = true)
public class RestDocsBuild {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Test
    public void articlesGet() throws Exception {
        this.mockMvc.perform(
                get("/categories")
                        .param("page", "1")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(document("index",
                        requestHeaders(
                                headerWithName("Authorization").description("")
                        ),
                        requestParameters(
                                parameterWithName("page").description("页码。默认：1"),
                                parameterWithName("size").description("每页记录量。默认：10")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("ID"),
                                fieldWithPath("[].name").description("分类名称"),
                                fieldWithPath("[].description").description("分类描述"),
                                fieldWithPath("[].available").description("是否可用"),
                                fieldWithPath("[].version").description("乐观锁"),
                                fieldWithPath("[].created").description("创建时间"),
                                fieldWithPath("[].modified").description("最后修改时间")
                        )
                ));
    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)
                        .uris()
                        .withScheme("http").withHost("localhost").withPort(9010)
                        .and().snippets()
                        .withDefaults(curlRequest(), httpResponse(), httpRequest()))
                .build();
    }

}
