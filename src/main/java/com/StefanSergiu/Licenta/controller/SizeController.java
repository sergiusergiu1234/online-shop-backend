package com.StefanSergiu.Licenta.controller;

import com.StefanSergiu.Licenta.dto.attribute.AttributeDto;
import com.StefanSergiu.Licenta.dto.attribute.CreateNewAttributeDto;
import com.StefanSergiu.Licenta.dto.size.SizeDto;
import com.StefanSergiu.Licenta.entity.Attribute;
import com.StefanSergiu.Licenta.entity.Size;
import com.StefanSergiu.Licenta.repository.SizeRepository;
import com.StefanSergiu.Licenta.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/size")
@CrossOrigin(origins = {"https://slope-emporium.vercel.app","http://localhost:3000"})
public class SizeController {


    @Autowired
    SizeService sizeService;
    @Autowired
    SizeRepository sizeRepository;

    //create new size
    @PostMapping("/admin/add")
    public ResponseEntity<SizeDto> createAttribute(@RequestBody final SizeDto sizeDto){
        Size size =   sizeService.addSize(sizeDto);
        return new ResponseEntity<>(SizeDto.from(size), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("admin/delete/{id}")
    public ResponseEntity<SizeDto> deleteSize(@PathVariable final Long id){
        Size size = sizeService.deleteSize(id);
        return new ResponseEntity<>(SizeDto.from(size), HttpStatus.OK);
    }

    @GetMapping("/get/{type_id}")
    public ResponseEntity<List<SizeDto>> getSizes(@PathVariable final Long type_id){
        List<Size> sizes = sizeRepository.findByType_Id(type_id);
        List<SizeDto> sizeDtos = sizes.stream()
                .map(SizeDto::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(sizeDtos);
    }

}
