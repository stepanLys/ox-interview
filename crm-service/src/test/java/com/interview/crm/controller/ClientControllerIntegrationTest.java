package com.interview.crm.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.crm.BaseIntegrationTest;
import com.interview.crm.client.dto.ClientCriteria;
import com.interview.crm.client.dto.ClientRequest;
import com.interview.crm.client.dto.ClientResponse;
import com.interview.crm.client.repository.ClientRepository;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@WithMockUser
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientControllerIntegrationTest implements BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    private static final String TEST_COMPANY_NAME = "Test Corp";
    private static final String TEST_INDUSTRY = "IT";
    private static final String TEST_ADDRESS = "123 Test St";
    private static final String UPDATED_COMPANY_NAME = "Test Corp Updated";
    private static final String UPDATED_INDUSTRY = "Finance";
    private static final String UPDATED_ADDRESS = "456 New St";

    private static Long testClientId;

    @Test
    @Order(1)
    @DisplayName("Should create new client successfully")
    void testCreateClient() throws Exception {
        ClientRequest newClient = createClientRequest(
                TEST_COMPANY_NAME,
                TEST_INDUSTRY,
                TEST_ADDRESS);

        MvcResult result = performPost("/api/clients", newClient)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.companyName").value(TEST_COMPANY_NAME))
                .andExpect(jsonPath("$.industry").value(TEST_INDUSTRY))
                .andExpect(jsonPath("$.address").value(TEST_ADDRESS))
                .andReturn();

        ClientResponse response = parseResponse(result, ClientResponse.class);
        testClientId = response.id();

        assertThat(testClientId).isNotNull().isPositive();
    }

    @Test
    @Order(2)
    @DisplayName("Should retrieve client by ID")
    void testGetClientById() throws Exception {
        assertThat(testClientId).isNotNull();

        mockMvc.perform(get("/api/clients/{id}", testClientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testClientId))
                .andExpect(jsonPath("$.companyName").value(TEST_COMPANY_NAME))
                .andExpect(jsonPath("$.industry").value(TEST_INDUSTRY))
                .andExpect(jsonPath("$.address").value(TEST_ADDRESS));
    }

    @Test
    @DisplayName("Should return 404 for non-existent client")
    void testGetClientById_NotFound() throws Exception {
        long nonExistentId = 999999L;

        mockMvc.perform(get("/api/clients/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(3)
    @DisplayName("Should update existing client")
    void testUpdateClient() throws Exception {
        assertThat(testClientId).isNotNull();

        ClientRequest updatedClient = createClientRequest(
                UPDATED_COMPANY_NAME,
                UPDATED_INDUSTRY,
                UPDATED_ADDRESS);

        mockMvc.perform(put("/api/clients/{id}", testClientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testClientId))
                .andExpect(jsonPath("$.companyName").value(UPDATED_COMPANY_NAME))
                .andExpect(jsonPath("$.industry").value(UPDATED_INDUSTRY))
                .andExpect(jsonPath("$.address").value(UPDATED_ADDRESS));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent client")
    void testUpdateClient_NotFound() throws Exception {
        long nonExistentId = 999999L;
        ClientRequest updateRequest = createClientRequest(
                UPDATED_COMPANY_NAME,
                UPDATED_INDUSTRY,
                UPDATED_ADDRESS);

        mockMvc.perform(put("/api/clients/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 for invalid client data")
    void testCreateClient_InvalidData() throws Exception {
        ClientRequest invalidClient = createClientRequest("", "", "");

        mockMvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidClient)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    @DisplayName("Should search clients by industry")
    void testSearchClientsByIndustry() throws Exception {
        assertThat(testClientId).isNotNull();

        ClientCriteria searchRequest = new ClientCriteria("", UPDATED_INDUSTRY);

        mockMvc.perform(post("/api/clients/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clients").isArray())
                .andExpect(jsonPath("$.clients[?(@.id == " + testClientId + ")]").exists())
                .andExpect(jsonPath("$.clients[?(@.id == " + testClientId + ")].industry").value(UPDATED_INDUSTRY));
    }

    @Test
    @Order(5)
    @DisplayName("Should search clients by company name")
    void testSearchClientsByCompanyName() throws Exception {
        assertThat(testClientId).isNotNull();
        ClientCriteria searchRequest = new ClientCriteria(UPDATED_COMPANY_NAME, "");

        mockMvc.perform(post("/api/clients/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clients").isArray())
                .andExpect(jsonPath("$.clients[?(@.id == " + testClientId + ")]").exists())
                .andExpect(jsonPath("$.clients[?(@.id == " + testClientId + ")].companyName").value(UPDATED_COMPANY_NAME));
    }

    @Test
    @DisplayName("Should return empty list when no clients match criteria")
    void testSearchClients_NoResults() throws Exception {
        ClientCriteria searchRequest = new ClientCriteria("NonExistentCompany", "");

        mockMvc.perform(post("/api/clients/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clients").isArray())
                .andExpect(jsonPath("$.clients").isEmpty());
    }

    @Test
    @Order(6)
    @DisplayName("Should delete client successfully")
    void testDeleteClient() throws Exception {
        assertThat(testClientId).isNotNull();

        mockMvc.perform(delete("/api/clients/{id}", testClientId))
                .andExpect(status().isNoContent());

        assertThat(clientRepository.findById(testClientId)).isEmpty();
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent client")
    void testDeleteClient_NotFound() throws Exception {
        long nonExistentId = 999999L;

        mockMvc.perform(delete("/api/clients/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    @DisplayName("Should return 404 when getting deleted client")
    void testGetDeletedClient_ShouldReturnNotFound() throws Exception {
        assertThat(testClientId).isNotNull();

        mockMvc.perform(get("/api/clients/{id}", testClientId))
                .andExpect(status().isNotFound());
    }

    private ClientRequest createClientRequest(String companyName, String industry, String address) {
        return new ClientRequest(companyName, industry, address);
    }

    private ResultActions performPost(String url, Object content) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(content)));
    }

    private <T> T parseResponse(MvcResult result, Class<T> clazz) throws Exception {
        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                clazz);
    }
}