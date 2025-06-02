package com.example.demo.services;

import com.example.demo.repositories.ViewInMonthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewInMonthService {

    @Autowired
    private ViewInMonthRepository viewInMonthRepository;

    public int totalListenAmountInMonth(int monthId) {
        return viewInMonthRepository.findTotalListenAmount(monthId).orElse(0);
    }
}
