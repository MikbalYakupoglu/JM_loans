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
        Loan loans = loanRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber));

        return LoansMapper.mapToLoanDto(loans);
    }

    @Override
    public boolean updateLoan(LoanDto loanDto) {
        Loan loans = loanRepository.findByLoanNumber(loanDto.getLoanNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "loanNumber", loanDto.getLoanNumber()));

        loanRepository.save(LoansMapper.mapToLoan(loanDto, loans));
        return true;
    }

    @Override
    public boolean deleteLoan(String mobileNumber) {
        Loan loans = loanRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber));

        loanRepository.delete(loans);
        return true;
    }

    private Loan createNewLoan(String mobileNumber) {
        Loan newLoans = new Loan();
        long randomLoanNumber = 100000000000L + random.nextInt(900000000);
        newLoans.setLoanNumber(Long.toString(randomLoanNumber));
        newLoans.setMobileNumber(mobileNumber);
        newLoans.setLoanType(LoansConstants.HOME_LOAN);
        newLoans.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoans.setAmountPaid(0);
        newLoans.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoans;
    }
}
