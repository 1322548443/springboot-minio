package com.example.minio.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * 编写测试类，采用了MockMvc模拟前端发送get或post请求
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MinioControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setMockMvc() {
        // 在测试开始之前，需要初始化MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void create() throws Exception {
        // 向/create接口发送post请求，参数为{"bucketName": "abc"}
        mockMvc.perform(MockMvcRequestBuilders.post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .param("bucketName", "abc")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void remove() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/remove")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .param("bucketName", "abc")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void listBucket() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/listBucket")
                .accept(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void listObjects() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/listObjects")
                .param("bucketName", "mybuckets")
                .accept(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

    }
}