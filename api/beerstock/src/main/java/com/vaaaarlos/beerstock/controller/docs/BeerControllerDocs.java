package com.vaaaarlos.beerstock.controller.docs;



import com.vaaaarlos.beerstock.dto.request.BeerInsertRequest;
import com.vaaaarlos.beerstock.dto.response.BeerResponse;
import com.vaaaarlos.beerstock.dto.response.MessageResponse;

import org.springframework.data.domain.Page;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Api("Manages a Beer Stock")
public interface BeerControllerDocs {

  @Operation(summary = "Beer save operation")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Beer saved"),
    @ApiResponse(responseCode = "400", description = "Missing required fields or wrong field range value"),
    @ApiResponse(responseCode = "409", description = "Beer with provided name already exists")
  })
  MessageResponse save(BeerInsertRequest beerInsertRequest);

  @Operation(summary = "Beer find by ID operation")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Beer found"),
    @ApiResponse(responseCode = "404", description = "Beer with provided ID not found")
  })
  BeerResponse findById(long id);

  @Operation(summary = "Beer find all operation")
  @ApiResponse(responseCode = "200", description = "Beers found")
  Page<BeerResponse> findAll(Integer page, Integer linesPerPage, String direction, String orderBy, String name);

  @Operation(summary = "Beer delete by ID operation")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Beer deleted"),
    @ApiResponse(responseCode = "404", description = "Beer with provided ID not found")
  })
  MessageResponse deleteById(long id);

  @Operation(summary = "Beer increment quantity operation")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Beer quantity incremented"),
    @ApiResponse(responseCode = "400", description = "Provided quantity increment exceeds max Beer stock capacity"),
    @ApiResponse(responseCode = "404", description = "Beer with provided ID not found")
  })
  MessageResponse incrementBeerQuantity(long id, int increment);

}
