package com.vhaibrother.vehicle_client_mng.controller;

import com.vhaibrother.vehicle_client_mng.annotations.ApiController;
import com.vhaibrother.vehicle_client_mng.dto.CarModelDto;
import com.vhaibrother.vehicle_client_mng.dto.Response;
import com.vhaibrother.vehicle_client_mng.service.CarModelService;
import com.vhaibrother.vehicle_client_mng.util.UrlConstraint;
import org.springframework.web.bind.annotation.*;

@ApiController
@RequestMapping(UrlConstraint.CarModelManagement.ROOT)
public class CarModelController {
    private final CarModelService carModelService;

    public CarModelController(CarModelService carModelService) {
        this.carModelService = carModelService;
    }

    @PostMapping
    public Response save(@RequestBody CarModelDto carModelDto) {
        return carModelService.save(carModelDto);
    }

    @GetMapping
    public Response getAll() {
        return carModelService.getAll();
    }

    @GetMapping(value = UrlConstraint.CarModelManagement.GET)
    public Response getById(@PathVariable("carModelId") Long carModelId) {
        return carModelService.getById(carModelId);
    }

    @DeleteMapping(value = UrlConstraint.CarModelManagement.DELETE)
    public Response del(@PathVariable("carModelId") Long carModelId) {
        return carModelService.del(carModelId);
    }

    @PutMapping(value = UrlConstraint.CarModelManagement.PUT)
    public Response update(@RequestBody CarModelDto carModelDto, @PathVariable("carModelId") Long carModelId) {
        return carModelService.update(carModelId, carModelDto);
    }
}
