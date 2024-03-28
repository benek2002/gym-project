package com.example.backend.Exception;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(Class<?> clazz, Long id){
        super("Could not find " + clazz.getSimpleName() + " with id:  " + id);
    }
}
