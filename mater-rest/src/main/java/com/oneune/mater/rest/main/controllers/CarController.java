package com.oneune.mater.rest.main.controllers;

import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.readers.CarReader;
import com.oneune.mater.rest.main.services.CarService;
import com.oneune.mater.rest.main.store.dtos.CarDto;
import com.oneune.mater.rest.main.store.dtos.FileDto;
import com.oneune.mater.rest.main.store.entities.CarEntity;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("cars")
@CrossOrigin("*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CarController implements CRUDable<CarDto, CarEntity> {

    CarService carService;
    CarReader carReader;

    @PostMapping
    @Override
    public CarDto post(@RequestBody CarDto car) {
        return carService.post(car);
    }

    @PostMapping("by-params")
    public CarDto postByParams(@RequestParam(name = "seller-id") Long sellerId,
                               @RequestBody CarDto car) {
        return carService.postByParams(sellerId, car);
    }

    @PutMapping("{id}")
    @Override
    public CarDto put(@PathVariable(name = "id") Long carId,
                      @RequestBody CarDto car) {
        return carService.put(carId, car);
    }

    @PostMapping("search")
    @Override
    public PageResponse<CarDto> search(@RequestBody PageQuery pageQuery) {
        return carReader.search(pageQuery);
    }

    @DeleteMapping("{id}")
    @Override
    public CarDto deleteById(@PathVariable(name = "id") Long carId) {
        return carService.deleteById(carId);
    }

    @GetMapping("{id}")
    @Override
    public CarDto getById(@PathVariable(name = "id") Long carId) {
        return carService.getById(carId);
    }

    @PutMapping("{id}/files")
    public CompletableFuture<List<FileDto>> putFiles(@PathVariable(name = "id") Long carId,
                                                     @RequestParam(name = "files", required = false) List<MultipartFile> files) {
        return carService.putFiles(carId, files);
    }
}
