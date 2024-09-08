package com.oneune.mater.rest.main.controllers;

import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.readers.CarReader;
import com.oneune.mater.rest.main.services.CarService;
import com.oneune.mater.rest.main.store.dtos.CarDto;
import com.oneune.mater.rest.main.store.dtos.PhotoDto;
import com.oneune.mater.rest.main.store.dtos.VideoPartDto;
import com.oneune.mater.rest.main.store.entities.CarEntity;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("{id}/photos")
    public void putPhotos(@PathVariable(name = "id") Long carId,
                          @RequestBody List<PhotoDto> photos) {
        carService.putPhotos(carId, photos);
    }

    @DeleteMapping("{id}/videos")
    public void deleteVideos(@PathVariable(name = "id") Long carId) {
        carService.deleteVideos(carId);
    }

    @PutMapping("{id}/videos")
    public void putVideos(@PathVariable(name = "id") Long carId,
                          @RequestBody VideoPartDto videoPart) {
        carService.putVideos(carId, videoPart);
    }
}
