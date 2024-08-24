package com.oneune.mater.rest.main.controllers;

import com.oneune.mater.rest.main.services.CarService;
import com.oneune.mater.rest.main.store.dtos.CarDto;
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
public class CarController {

    CarService carService;

    @GetMapping("search")
    public List<CarDto> search(@RequestParam Integer page, @RequestParam Integer size) {
        return carService.search(page, size);
    }
}
