package com.example.loans.service.impl;

import com.example.loans.constants.LoansConstants;
import com.example.loans.dto.LoanDto;
import com.example.loans.entity.Loan;
import com.example.loans.exception.LoanAlreadyExistsException;
import com.example.loans.exception.ResourceNotFoundException;
import com.example.loans.mapper.LoansMapper;
import com.example.loans.repository.LoanRepository;
import com.example.loans.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);
    private final Random random = new Random();


    @Override
    public void createLoan(String mobileNumber) {
        var loan = loanRepository.findByMobileNumber(mobileNumber);

        if (loan.isPresent()) {
            logger.warn("Loan already exist with mobileNumber : {}", mobileNumber);
            throw new LoanAlreadyExistsException("Loan already exist with mobileNumber");
        }

        loanRepository.save(createNewLoan(mobileNumber));
    }

    @Override
    public LoanDto fetchLoan(String mobileNumber) {
        Loan loan = loanRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber));

        return LoansMapper.mapToLoanDto(loan);
    }

    @Override
    public boolean updateLoan(LoanDto loanDto) {
        Loan loan = loanRepository.findByLoanNumber(loanDto.getLoanNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "loanNumber", loanDto.getLoanNumber()));

        loanRepository.save(LoansMapper.mapToLoan(loanDto, loan));
        return true;
    }

    @Override
    public boolean deleteLoan(String mobileNumber) {
        Loan loan = loanRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber));

        loanRepository.delete(loan);
        return true;
    }

    private Loan createNewLoan(String mobileNumber) {
        Loan newLoan = new Loan();
        long randomLoanNumber = 100000000000L + random.nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }
}
