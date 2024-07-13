package com.example.loans.controller;

import com.example.loans.constants.LoansConstants;
import com.example.loans.constants.ServerConstants;
import com.example.loans.dto.LoanDto;
import com.example.loans.dto.ResponseDto;
import com.example.loans.service.LoanService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/loans", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class LoansController {

    private final LoanService loanService;

    @Autowired
    public LoansController(LoanService loanService) {
        this.loanService = loanService;
    }

    private static final Logger logger = LogManager.getLogger(LoansController.class);

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createLoan(@RequestParam
                                                  @Pattern(regexp = "(^$|\\d{10})", message = "Mobile number must be 10 digits")
                                                  String mobileNumber) {
        loanService.createLoan(mobileNumber);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(LoansConstants.LOAN_CREATED_SUCCESSFULLY));
    }

    @GetMapping("/fetch")
    public ResponseEntity<LoanDto> fetchLoan(@RequestHeader("nakoual-correlation-id") String correlationId,
            @RequestParam @Pattern(regexp = "(^$|\\d{10})", message = "Mobile number must be 10 digits")
                                             String mobileNumber) {

        logger.info("nakoual-correlation-id found : {}", correlationId);
        LoanDto loanDto = loanService.fetchLoan(mobileNumber);
        return new ResponseEntity<>(loanDto, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateLoan(@Valid @RequestBody LoanDto loanDto) {
        boolean result = loanService.updateLoan(loanDto);

        if (!result) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(ServerConstants.INTERNAL_SERVER_ERROR));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(LoansConstants.LOAN_UPDATED_SUCCESSFULLY));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteLoan(@RequestParam
                                                  @Pattern(regexp = "(^$|\\d{10})", message = "Mobile number must be 10 digits")
                                                  String mobileNumber) {
        boolean result = loanService.deleteLoan(mobileNumber);

        if (!result) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(ServerConstants.INTERNAL_SERVER_ERROR));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(LoansConstants.LOAN_DELETED_SUCCESSFULLY));
    }
}
