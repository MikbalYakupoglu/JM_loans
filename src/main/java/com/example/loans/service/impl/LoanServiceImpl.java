package com.example.loans.service.impl;

import com.example.loans.dto.LoanDto;
import com.example.loans.repository.LoanRepository;
import com.example.loans.service.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;

    @Override
    public void createLoan(String mobileNumber) {

    }

    @Override
    public LoanDto fetchLoan(String mobileNumber) {
        return null;
    }

    @Override
    public boolean updateLoan(LoanDto loanDto) {
        return false;
    }

    @Override
    public boolean deleteLoan(String mobileNumber) {
        return false;
    }
}
