package com.example.skucise.services.interfaces;

import com.example.skucise.models.Type;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAdTypeService {
    public List<Type> getAllTypes();
}
