package com.vaaaarlos.beerstock.cucumber.stepdef.controller;

import static com.vaaaarlos.beerstock.util.BeerstockUtils.BASIC_MESSAGE;
import static com.vaaaarlos.beerstock.util.BeerstockUtils.createMessageResponse;
import static com.vaaaarlos.beerstock.util.BeerstockUtils.Operation.SAVED;
import static com.vaaaarlos.beerstock.utils.JsonConverter.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vaaaarlos.beerstock.cucumber.context.controller.BeerControllerContextConfiguration;
import com.vaaaarlos.beerstock.cucumber.stepdef.CommonStepDefs;
import com.vaaaarlos.beerstock.dto.request.BeerInsertRequest;
import com.vaaaarlos.beerstock.dto.response.MessageResponse;
import com.vaaaarlos.beerstock.exception.BeerAlreadyExistsException;
import com.vaaaarlos.beerstock.service.BeerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@ContextConfiguration(classes = { BeerControllerContextConfiguration.class })
public class BeerControllerStepDefs {

  private static final String BEER_API_URL_PATH = "/api/v1/beers";

  @Autowired @Qualifier("testMockMvc")
  private MockMvc mockMvc;

  @Autowired @Qualifier("mockedBeerService")
  private BeerService beerService;

  private MessageResponse expectedMessageResponse;
  private BeerInsertRequest expectedBeerInsertRequest = CommonStepDefs.getExpectedBeerInsertRequest();

  @When("service save method is called")
  public void service_save_method_is_called() {
    expectedMessageResponse = createMessageResponse(BASIC_MESSAGE, SAVED, 1L);
    when(beerService.save(expectedBeerInsertRequest)).thenReturn(expectedMessageResponse);
  }

  @When("service save method is called and a Beer already exists with provided name")
  public void service_save_method_is_called_and_a_beer_already_exists_with_provided_name() {
    when(beerService.save(expectedBeerInsertRequest)).thenThrow(BeerAlreadyExistsException.class);
  }

  @Then("a success message response should be shown with status code created")
  public void a_success_message_response_should_be_shown_with_status_code_created() throws Exception {
    mockMvc.perform(post(BEER_API_URL_PATH)
        .contentType(APPLICATION_JSON)
        .content(asJsonString(expectedBeerInsertRequest)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message", is(expectedMessageResponse.getMessage())));
  }

  @Then("an error message response should be shown with status code conflict")
  public void an_error_message_response_should_be_shown_with_status_code_conflict() throws Exception {
    mockMvc.perform(post(BEER_API_URL_PATH)
        .contentType(APPLICATION_JSON)
        .content(asJsonString(expectedBeerInsertRequest)))
        .andDo(print())
        .andExpect(status().isConflict());
  }

  @Then("an error message response should be shown with status code bad request")
  public void an_error_message_response_should_be_shown_with_status_code_bad_request() throws Exception {
    mockMvc.perform(post(BEER_API_URL_PATH)
        .contentType(APPLICATION_JSON)
        .content(asJsonString(expectedBeerInsertRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

}