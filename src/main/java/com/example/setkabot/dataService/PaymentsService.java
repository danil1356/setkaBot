package com.example.setkabot.dataService;

import com.example.setkabot.data.Entity.Payments;
import com.example.setkabot.data.Entity.Users;

import java.util.List;

public interface PaymentsService {

    Payments getById(Long id);
    List<Payments> getAll();
    void delete(Long id);
    Payments save(Payments entity);
}
