package com.StefanSergiu.Licenta.service;

import com.StefanSergiu.Licenta.dto.size.NewSizeModel;
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

import java.util.HashSet;
import java.util.List;

@Service
public class SizeService {
    @Autowired
    SizeRepository sizeRepository;

    @Autowired
    TypeRepository typeRepository;



    public Size addSize(NewSizeModel newSizeModel){
        Type type =typeRepository.findById(newSizeModel.getTypeId())
                .orElseThrow(()->new EntityNotFoundException("Type with id "+ newSizeModel.getTypeId()+ " not found!"));
        Size size = new Size();

        size.setValue(newSizeModel.getValue());
        size.setType(type);
        return sizeRepository.save(size);

    }

    @Transactional
    public Size deleteSize(Long sizeId){
        System.out.println("THis is called");
        Size size = sizeRepository.findById(sizeId)
                .orElseThrow(()-> new EntityNotFoundException(("Size with id " + sizeId +" not found!")));
        sizeRepository.delete(size); // Delete the Size entity
        return size;
    }

    public List<Size> getSizes(){
       return sizeRepository.findAll();
    }
}
