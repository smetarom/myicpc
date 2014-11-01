package com.myicpc.service;

import com.myicpc.model.ExampleEntity;
import com.myicpc.repository.ExampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Roman Smetana
 */
@Service
public class ExampleService {
    @Autowired
    private ExampleRepository exampleRepository;

    @Transactional
    public void helloWord() {
        System.out.println("Hello from service...");
        ExampleEntity entity = new ExampleEntity();
        entity.setName("ahoj");
        exampleRepository.save(entity);
        throw new IllegalStateException(("bla bla"));
    }
}
