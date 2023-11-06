package com.example.setkabot.dataService.Impl;

import com.example.setkabot.data.Entity.Payments;
import com.example.setkabot.data.Entity.Users;
import com.example.setkabot.data.Repository.PaymentsRepository;
import com.example.setkabot.dataService.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentsServiceImpl implements PaymentsService {

    final PaymentsRepository paymentsRepository;

    @Autowired
    public PaymentsServiceImpl (PaymentsRepository paymentsRepository){
        this.paymentsRepository = paymentsRepository;
    }

    @Override
    public Payments getById(Long id) {
        Optional<Payments> payments = paymentsRepository.findById(id);
        if (payments.isEmpty()){
            return null;
        }
        return payments.get();
    }

    @Override
    public List<Payments> getAll() {
        List<Payments> payments = paymentsRepository.findAll();
        return payments;
    }

    @Override
    public void delete(Long id) {
        paymentsRepository.deleteById(id);
    }

    @Override
    public Payments save(Payments entity) {
        Payments payments = paymentsRepository.save(entity);
        return payments;
    }
}
