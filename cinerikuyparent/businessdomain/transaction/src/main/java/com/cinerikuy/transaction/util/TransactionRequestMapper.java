package com.cinerikuy.transaction.util;


import com.cinerikuy.transaction.dto.TransactionRequest;
import com.cinerikuy.transaction.entity.Transaction;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionRequestMapper {

    @Mappings({ // The source in this case is TransactionRequest
            @Mapping(source = "cinemaCode", target = "cinema.cinemaCode")})
    Transaction TransactionRequestToTransaction(TransactionRequest source);

    List<Transaction> TransactionRequestListToTransactionList(List<TransactionRequest> source);

    @InheritInverseConfiguration
    TransactionRequest TransactionToTransactionRequest(Transaction source);

    @InheritInverseConfiguration
    List<TransactionRequest> TransactionListToTransactionRequestList(List<Transaction> source);

}
