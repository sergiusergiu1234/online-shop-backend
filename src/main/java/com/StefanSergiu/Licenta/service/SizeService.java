package com.StefanSergiu.Licenta.service;

import com.StefanSergiu.Licenta.dto.size.SizeDto;
import com.StefanSergiu.Licenta.entity.ProductAttribute;
import com.StefanSergiu.Licenta.entity.Size;
import com.StefanSergiu.Licenta.entity.Type;
import com.StefanSergiu.Licenta.repository.SizeRepository;
import com.StefanSergiu.Licenta.repository.TypeRepository;
import jakarta.persistence.EntityNotFoundException;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SizeService {
    @Autowired
    SizeRepository sizeRepository;

    @Autowired
    TypeRepository typeRepository;



    public Size addSize(SizeDto sizeDto){
        Size size = new Size();
        Type type = typeRepository.findById(sizeDto.getTypeId())
                .orElseThrow(()->new EntityNotFoundException("Type with id "+ " not found"));
        size.setType(type);
        size.setValue(sizeDto.getValue());

        type.addSize(size);
        return sizeRepository.save(size);

    }

    @Transactional
    public Size deleteSize(Long sizeId){
        System.out.println("THis is called");
        Size size = sizeRepository.findById(sizeId)
                .orElseThrow(()-> new EntityNotFoundException(("Size with id " + sizeId +" not found!")));
        Type type = size.getType(); // Get the associated Type entity

        type.getSizes().remove(size); // Remove the Size entity from the Type entity's sizes collection
        sizeRepository.delete(size); // Delete the Size entity

        // If necessary, you can update the Type entity in the repository to reflect the changes
        typeRepository.save(type);
        return size;

    }

}
