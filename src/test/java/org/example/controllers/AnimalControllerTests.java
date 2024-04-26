package org.example.controllers;

import org.example.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AnimalControllerTests {
    private MockMvc mockMvc;

    @Autowired
    public AnimalControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testGetFilteredAndSortedAnimals_SortByIdAsc() throws Exception {
        MockMultipartFile file = TestDataUtil.createXMLFile();

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/files/uploads")
                                .file(file)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                ).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(7)));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/files/uploads")
                                .param("sortField", "id")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[1].id").value("2"))
                .andExpect(jsonPath("$.content[2].id").value("3"));
    }

    @Test
    public void testGetFilteredAndSortedAnimals_SortByIdDescWithTypeDog() throws Exception {
        MockMultipartFile file = TestDataUtil.createCSVFile();

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/files/uploads")
                                .file(file)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                ).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(7)));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/files/uploads")
                                .param("type", "dog")
                                .param("sortField", "id")
                                .param("sortDirection", "desc")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("3"));
    }

    @Test
    public void testPostXMLUploadAnimals() throws Exception {
        MockMultipartFile file = TestDataUtil.createXMLFile();

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/files/uploads")
                                .file(file)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(7)));
    }

    @Test
    public void testPostCSVUploadAnimals() throws Exception {
        MockMultipartFile file = TestDataUtil.createCSVFile();

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/files/uploads")
                                .file(file)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(7)));
    }
}
